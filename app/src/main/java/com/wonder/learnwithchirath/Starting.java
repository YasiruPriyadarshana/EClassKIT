package com.wonder.learnwithchirath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Starting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);



        Thread myThred=new Thread(){
            public void run(){
                try{
                    sleep(1000);
                    Intent intent=new Intent(Starting.this,GetInfo.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        myThred.start();
    }
}
