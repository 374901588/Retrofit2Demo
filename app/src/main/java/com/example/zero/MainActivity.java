package com.example.zero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void turnToOther(Class temp) {
        startActivity(new Intent(this,temp));
    }

    public void click(View view) {
        Class temp=null;
        switch (view.getId()) {
            case R.id.bt1:temp=Example1Activity.class;break;
            case R.id.bt2:temp=Example2Activity.class;break;
            case R.id.bt3:temp=Example3Activity.class;break;
            default:break;
        }
        turnToOther(temp);
    }
}
