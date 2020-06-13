package com.wonder.learnwithchirath;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FragmentName extends Fragment {
    private Button bt_Name,change_btn;
    private EditText Name;
    private TextView userType;
    View view;
    private String name;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_name, container, false);


        bt_Name=(Button) view.findViewById(R.id.bt_name);
        change_btn=(Button) view.findViewById(R.id.user_change_btn);
        Name=(EditText) view.findViewById(R.id.in_name);
        userType=(TextView) view.findViewById(R.id.status_txt);
        bt_Name.setEnabled(false);
        Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name=Name.getText().toString().trim();
                bt_Name.setEnabled(!name.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=userType.getText().toString();
                if (TextUtils.equals(user,"Student")){
                    userType.setText("Teacher");
                }else {
                    userType.setText("Student");
                }
            }
        });
        bt_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int length=Name.getText().length();
                if (length>3){
                wirteFile();

                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentplace, new FragmentEmail());
                ft.commit();
/*
                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentplace, new FragmentNumber(), null);
                ft.commit();
                */
                }else {
                    Toast.makeText(getContext(), "Name too short", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void wirteFile() {

        String textToSave = Name.getText().toString();
        String space = ",";
        String usr,num="123";
        String user=userType.getText().toString();
        if(TextUtils.equals(user,"Teacher")){
           usr="teacher";
        }else {
            usr="student";
        };
        try {
            FileOutputStream fileOutputStream = requireActivity().openFileOutput("apprequirement.txt", Context.MODE_APPEND);
            fileOutputStream.write((usr + space + textToSave + space + num + space).getBytes());
            fileOutputStream.close();

            Toast.makeText(getActivity(), "text Saved", Toast.LENGTH_SHORT).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
