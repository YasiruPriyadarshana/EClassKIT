package com.wonder.learnwithchirath;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class User extends AppCompatActivity {
    private TextView Name,Email,Number,Class;
    private String[] array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Name=(TextView)findViewById(R.id.name_txt);
        Email=(TextView)findViewById(R.id.email_txt);
        Number=(TextView)findViewById(R.id.number_txt);
        Class=(TextView)findViewById(R.id.class_txt);
        readFile();
    }
    public void readFile(){
        try {
            FileInputStream fileInputStream = openFileInput("apprequirement.txt");
            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer =new StringBuffer();


            String lines;
            while ((lines = bufferedReader.readLine()) != null){
                stringBuffer.append(lines + "\n");
            }
            String str =stringBuffer.toString();
            array = str.split(",");

            Name.setText(array[0]);
            Number.setText(array[1]);
            Email.setText(array[2]);
            Class.setText(array[3]);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
