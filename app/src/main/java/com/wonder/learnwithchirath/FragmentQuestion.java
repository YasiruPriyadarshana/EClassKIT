package com.wonder.learnwithchirath;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.wonder.learnwithchirath.Object.Common;
import com.wonder.learnwithchirath.Object.Quizobj;



public class FragmentQuestion extends Fragment {
    private Quizobj quizobj;
    private int count;
    private ImageView imageView;

    int a=0,b=0;
    public FragmentQuestion(Quizobj quizobj,int i) {
        this.quizobj = quizobj;
        count=i;
        Common.answer.add(count,0);
        Common.completelist.add(count,0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_fragment_question, container, false);
        TextView question=(TextView)v.findViewById(R.id.quest);
        final CheckBox ans1=(CheckBox)v.findViewById(R.id.ans_cb1);
        final CheckBox ans2=(CheckBox)v.findViewById(R.id.ans_cb2);
        final CheckBox ans3=(CheckBox)v.findViewById(R.id.ans_cb3);
        final CheckBox ans4=(CheckBox)v.findViewById(R.id.ans_cb4);
        final CheckBox ans5=(CheckBox)v.findViewById(R.id.ans_cb5);
        RadioButton rb1=(RadioButton)v.findViewById(R.id.ans_rb1);
        RadioButton rb2=(RadioButton)v.findViewById(R.id.ans_rb2);
        RadioButton rb3=(RadioButton)v.findViewById(R.id.ans_rb3);
        RadioButton rb4=(RadioButton)v.findViewById(R.id.ans_rb4);
        RadioButton rb5=(RadioButton)v.findViewById(R.id.ans_rb5);


        ImageView imgC=(ImageView)v.findViewById(R.id.frag_questimg);


            if (quizobj.getUriimg() != null) {
                Picasso.with(getContext()).load(quizobj.getUriimg()).into(imgC);
            } else {
                imgC.setVisibility(View.GONE);
            }
            question.setText(quizobj.getQuestion());
            if (TextUtils.equals(quizobj.getType(),"radiobutton")) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                rb1.setText(quizobj.getAns1().substring(1));
                rb2.setText(quizobj.getAns2().substring(1));
                rb3.setText(quizobj.getAns3().substring(1));
                rb4.setText(quizobj.getAns4().substring(1));
                if (TextUtils.isEmpty(quizobj.getAns5())) {
                    rb5.setVisibility(View.GONE);
                } else {
                    rb5.setText(quizobj.getAns5().substring(1));
                }
                //check ans



            } else {
                rb1.setVisibility(View.GONE);
                rb2.setVisibility(View.GONE);
                rb3.setVisibility(View.GONE);
                rb4.setVisibility(View.GONE);
                rb5.setVisibility(View.GONE);
                ans1.setText(quizobj.getAns1().substring(1));
                ans2.setText(quizobj.getAns2().substring(1));
                ans3.setText(quizobj.getAns3().substring(1));
                ans4.setText(quizobj.getAns4().substring(1));
                if (TextUtils.isEmpty(quizobj.getAns5())) {
                    ans5.setVisibility(View.GONE);
                } else {
                    ans5.setText(quizobj.getAns5().substring(1));
                }
                //check ans

            }

        if (!TextUtils.equals(quizobj.getType(),"radiobutton")) {
            ans1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ans1.isChecked() && String.valueOf(quizobj.getAns1().charAt(0)).equals("1")) {
                        a++;
                        Common.answer.set(count,a);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }else if (String.valueOf(quizobj.getAns1().charAt(0)).equals("1")){
                        a--;
                        Common.answer.set(count,a);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ans2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ans2.isChecked() && String.valueOf(quizobj.getAns2().charAt(0)).equals("1")) {
                        a++;
                        Common.answer.set(count,a);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }else if (String.valueOf(quizobj.getAns2().charAt(0)).equals("1")){
                        a--;
                        Common.answer.set(count,a);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ans3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ans3.isChecked() && String.valueOf(quizobj.getAns3().charAt(0)).equals("1")) {
                        a++;
                        Common.answer.set(count,a);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }else if (String.valueOf(quizobj.getAns3().charAt(0)).equals("1")){
                        a--;
                        Common.answer.set(count,a);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ans4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ans4.isChecked() && String.valueOf(quizobj.getAns4().charAt(0)).equals("1")) {
                        a++;
                        Common.answer.set(count,a);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }else if (String.valueOf(quizobj.getAns4().charAt(0)).equals("1")){
                        a--;
                        Common.answer.set(count,a);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ans5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ans5.isChecked() && String.valueOf(quizobj.getAns5().charAt(0)).equals("1")) {
                        a++;
                        Common.answer.set(count,a);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }else if (String.valueOf(quizobj.getAns5().charAt(0)).equals("1")) {
                        a--;
                        Common.answer.set(count,a);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && String.valueOf(quizobj.getAns1().charAt(0)).equals("1")) {
                        Common.answer.set(count,1);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }else if (String.valueOf(quizobj.getAns1().charAt(0)).equals("1")){
                        Common.answer.set(count,0);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && String.valueOf(quizobj.getAns2().charAt(0)).equals("1")) {
                        Common.answer.set(count,1);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }else if (String.valueOf(quizobj.getAns2().charAt(0)).equals("1")) {
                        Common.answer.set(count,0);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && String.valueOf(quizobj.getAns3().charAt(0)).equals("1")) {
                        Common.answer.set(count,1);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }else if (String.valueOf(quizobj.getAns3().charAt(0)).equals("1")){
                        Common.answer.set(count,0);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            rb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && String.valueOf(quizobj.getAns4().charAt(0)).equals("1")) {
                        Common.answer.set(count,1);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }else if (String.valueOf(quizobj.getAns4().charAt(0)).equals("1")){
                        Common.answer.set(count,0);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            rb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && String.valueOf(quizobj.getAns5().charAt(0)).equals("1")) {
                        Common.answer.set(count,1);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }else if (String.valueOf(quizobj.getAns5().charAt(0)).equals("1")){
                        Common.answer.set(count,0);
                        Toast.makeText(getContext(), "ans:"+Common.answer, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return v;
    }


}
