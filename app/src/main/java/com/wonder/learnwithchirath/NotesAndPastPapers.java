package com.wonder.learnwithchirath;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class NotesAndPastPapers extends Fragment {
    ImageButton PastPapers,ModelPapers,Notes;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notes_and_past_papers, container, false);


        PastPapers=(ImageButton)view.findViewById(R.id.ibtn_pastpaper);
        PastPapers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PastPapers.class);
                getActivity().startActivity(intent);
            }
        });

        ModelPapers=(ImageButton)view.findViewById(R.id.ibtn_modelpapers);
        ModelPapers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ModelPapers.class);
                getActivity().startActivity(intent);
            }
        });

        Notes=(ImageButton)view.findViewById(R.id.ibtn_notes);
        Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Notes.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }
}
