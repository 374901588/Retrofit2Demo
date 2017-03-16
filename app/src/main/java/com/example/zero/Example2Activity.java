package com.example.zero;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * 自定义Converter，模仿GsonConverter的功能
 */
public class Example2Activity extends AppCompatActivity {
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        mTv= (TextView) findViewById(R.id.tv);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(CustomConverterFactory.create())
                .client(new OkHttpClient())
                .build();

        GitHubService service=retrofit.create(GitHubService.class);
        Call<List<Contributor>> call=service.contributors2("square","retrofit");
        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                Observable.from(response.body()).subscribe(contributor -> mTv.append(contributor.toString()+"\n"));
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {

            }
        });
    }

    private static class CustomConverter implements Converter<ResponseBody,List<Contributor>> {
        public static final CustomConverter INSTANCE=new CustomConverter();

        @Override
        public List<Contributor> convert(ResponseBody value) throws IOException {
            List<Contributor> list=new Gson().fromJson(value.string(),new TypeToken<List<Contributor>>(){}.getType());
            return list;
        }
    }

    public static class CustomConverterFactory extends Converter.Factory {
        public static final CustomConverterFactory INSTANCE = new CustomConverterFactory();

        public static CustomConverterFactory create() {
            return INSTANCE;
        }

        // 我们只关心从ResponseBody 到 List<Contributor>> 的转换，所以其它方法可不覆盖
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            //不能直接用type==new TypeToken<List<Contributor>>(){}.getType())，将会得到false
            //因为==是用于判断两个引用变量是否相等，但是这里的==右边是new的一个新的，所以肯定是“不==”的
            Log.d("测试---》",""+(type==new TypeToken<List<Contributor>>(){}.getType()));
            if (type.equals(new TypeToken<List<Contributor>>(){}.getType())) {
                return CustomConverter.INSTANCE;
            }
            //其它类型我们不处理，返回null就行
            return null;
        }
    }


}
