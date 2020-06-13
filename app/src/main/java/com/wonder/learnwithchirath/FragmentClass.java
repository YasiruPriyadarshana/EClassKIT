package com.wonder.learnwithchirath;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.wonder.learnwithchirath.Firebase.FirebaseDatabaseHelper;
import com.wonder.learnwithchirath.Object.Student;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class FragmentClass extends Fragment {
    private Button bt_Class;
    private Spinner classCategory_spinner;
    private View view;
    private int selector;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the  layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_class, container, false);

        wirteFile();
        readFileAndFirebase();

            Intent intent = new Intent(getActivity(), StudentAccount.class);
            getActivity().startActivity(intent);

/*
        bt_Class=(Button)view.findViewById(R.id.bt_class);
        classCategory_spinner = (Spinner)view.findViewById(R.id.category_spin);

        bt_Class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wirteFile();
                readFileAndFirebase();
                Intent intent=new Intent(getActivity(),MainActivity.class);
                getActivity().startActivity(intent);
            }
        });
*/
        return view;
    }

    private void wirteFile() {

        String textToSave = "11";
                //classCategory_spinner.getSelectedItem().toString();
        String space = ",";
        try {
            FileOutputStream fileOutputStream = requireActivity().openFileOutput("apprequirement.txt", Context.MODE_APPEND);
            fileOutputStream.write((textToSave + space).getBytes());
            fileOutputStream.close();

//            Toast.makeText(getActivity(), "text Saved", Toast.LENGTH_SHORT).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFileAndFirebase(){
        try {
            FileInputStream fileInputStream = requireActivity().openFileInput("apprequirement.txt");
            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer =new StringBuffer();


            String lines;
            while ((lines = bufferedReader.readLine()) != null){
                stringBuffer.append(lines + "\n");
            }
            String str =stringBuffer.toString();
            String[] array = str.split(",");

            Student student=new Student();
            student.setEmail(array[2]);
            student.setName(array[0]);
            student.setGrade(array[3]);
            student.setMobile_number(array[1]);



            new FirebaseDatabaseHelper().addstudent(student, new FirebaseDatabaseHelper.DataStatus() {
                @Override
                public void DataIsLoaded(List<Student> students, List<String> keys) {

                }

                @Override
                public void DataIsInserted() {
                    Toast.makeText(getContext(), "The student record has been inserted", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void DataIsUpdated() {

                }

                @Override
                public void DataIsDeleted() {

                }
            });




//            Toast.makeText(getContext(),"text Saved",Toast.LENGTH_SHORT).show();
//            FileOutputStream fileOutputStream = requireActivity().openFileOutput("appdata.txt",Context.MODE_APPEND);
//            fileOutputStream.write(buffer1.toString().getBytes());




        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
