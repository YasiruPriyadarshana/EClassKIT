package com.wonder.eclasskit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetInfo extends AppCompatActivity {
    boolean skip=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);


        if (readFile()){
            skip=true;
        }
        if(!skip) {
            FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
            fr.add(R.id.fragmentplace, new FragmentName());
            fr.commit();
        }else {
            Intent intent=new Intent(GetInfo.this,StudentAccount.class);
            startActivity(intent);
        }
    }

    public boolean readFile(){
        try {
            FileInputStream fileInputStream = openFileInput("apprequirement.txt");
            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer =new StringBuffer();

            String lines;
            if ((lines = bufferedReader.readLine()) != null){
                return true;
            }


        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }


}
