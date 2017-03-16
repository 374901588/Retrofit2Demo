package com.example.zero;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 自定义CallAdapter，模仿实现RxJavaCallAdapter的功能
 */
public class Example3Activity extends AppCompatActivity {
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        mTv= (TextView) findViewById(R.id.tv);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(Example2Activity.CustomConverterFactory.create())
                .addCallAdapterFactory(CustomCallAdapterFactory.INSTANCE)
                .client(new OkHttpClient())
                .build();

        GitHubService service=retrofit.create(GitHubService.class);



        new Thread(()->{
            service.contributors3("square", "retrofit")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s-> mTv.setText(s.toString()));
        }).start();
    }

    public static class CustomCallAdapter implements CallAdapter<List<Contributor>,Observable<List<Contributor>>> {
        private final Type responseType;

        CustomCallAdapter(Type type) {
            responseType=type;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public Observable<List<Contributor>> adapt(Call<List<Contributor>> call) {
            Log.d("测试-----5","responseType="+responseType);
            try {
                List<Contributor> contributorList=call.execute().body();
                return Observable.just(contributorList);
            } catch (IOException e) {
                Log.e("异常",e.getMessage());
            }
            return null;
        }
    }

    public static class CustomCallAdapterFactory extends CallAdapter.Factory {
        public static final CustomCallAdapterFactory INSTANCE=new CustomCallAdapterFactory();

        @Override
        public CallAdapter<?,?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            Class<?> rawType=getRawType(returnType);//提取returnType的原始类类型，例如，表示List <？扩展Runnable>返回List.class
            //因此在对比的时候我们只要rawType==Observable.class，而不是与new TypeToken<Observable<List<Contributor>>>(){}.getType()对比
            if (rawType==Observable.class&&returnType instanceof ParameterizedType) {
                Type callReturnType=getParameterUpperBound(0,(ParameterizedType)returnType);
                return new CustomCallAdapter(callReturnType);
            }
            return null;
        }
    }
}
