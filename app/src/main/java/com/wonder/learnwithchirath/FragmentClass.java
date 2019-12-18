package com.wonder.learnwithchirath;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FragmentClass extends Fragment {
    private Button bt_Class;
    private Spinner classCategory_spinner;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_class, container, false);


        bt_Class=(Button)view.findViewById(R.id.bt_class);
        classCategory_spinner = (Spinner)view.findViewById(R.id.category_spin);

        bt_Class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wirteFile();
                Intent intent=new Intent(getActivity(),MainActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    private void wirteFile() {

        String textToSave = classCategory_spinner.getSelectedItem().toString();
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
}
