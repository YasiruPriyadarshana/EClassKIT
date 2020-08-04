package com.wonder.eclasskit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wonder.eclasskit.Object.Common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FragmentName extends Fragment {
    private Button bt_Name,change_btn;
    private EditText Name;
    private TextView userType;
    View view;
    private String name;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_name, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }

        bt_Name=(Button) view.findViewById(R.id.bt_name);
        change_btn=(Button) view.findViewById(R.id.user_change_btn);
        Name=(EditText) view.findViewById(R.id.in_name);

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
                Intent intent=new Intent(getActivity(),TeacherLogin.class);
                getActivity().startActivity(intent);
            }
        });
        bt_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_Name.setEnabled(false);
                int length=Name.getText().length();
                if (length>3){

                Common.studentregname=Name.getText().toString();;

                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentplace, new FragmentEmail());
                ft.commit();


                }else {
                    Toast.makeText(getContext(), "Name too short", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
/*
    private void wirteFile() {

        String textToSave = Name.getText().toString();
        String space = ",";
        String num="123";


        try {
            FileOutputStream fileOutputStream = requireActivity().openFileOutput("apprequirement.txt", Context.MODE_APPEND);
            fileOutputStream.write((textToSave + space + num + space).getBytes());
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

*/
}
