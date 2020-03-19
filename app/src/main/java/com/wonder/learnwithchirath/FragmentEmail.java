package com.wonder.learnwithchirath;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FragmentEmail extends Fragment {
    private Button bt_Email;
    private EditText Email;
    private String name;
    private View view;
    private FirebaseAuth mAuth;
    private boolean send;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_email, container, false);


        bt_Email=(Button)view.findViewById(R.id.bt_email);
        bt_Email.setEnabled(false);
        Email=(EditText) view.findViewById(R.id.in_email);
        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name=Email.getText().toString().trim();
                bt_Email.setEnabled(!name.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bt_Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wirteFile();
                String email = Email.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[g]+[m]+[a]+[i]+[l]+\\.+[a-z]+";
                if (email.matches(emailPattern)) {
                    if (checkEmail()) {
                        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragmentplace, new FragmentClass(), null);
                        ft.commit();
                    }
                }else {
                    Toast.makeText(getContext(), "Not Valid Email Address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    private void wirteFile() {
        String textToSave = Email.getText().toString();
        String space = ",";
        try {
            FileOutputStream fileOutputStream = requireActivity().openFileOutput("apprequirement.txt", Context.MODE_APPEND);
            fileOutputStream.write((textToSave + space).getBytes());
            fileOutputStream.close();

            Toast.makeText(getActivity(), "text Saved", Toast.LENGTH_SHORT).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean checkEmail(){

        ////

        //       methna firebse user login eka hadapan

        ///

        send=true;
//        FirebaseUser user=mAuth.getCurrentUser();
//
//        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                send=true;
//            }
//        });
        return send;
    }
}
