package com.wonder.learnwithchirath;

import android.content.Intent;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button bt_NotesAndPP,bt_Home,bt_Event,bt_Class;
    private float x1,x2,y1,y2;
    private int changer=0;
    private long backPressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.add(R.id.Fragment_place, new Home());
        ft.commit();



        bt_NotesAndPP=(Button)findViewById(R.id.bt_notes);
        bt_Home=(Button)findViewById(R.id.bt_home);
        bt_Class=(Button)findViewById(R.id.bt_class);
        bt_Event=(Button)findViewById(R.id.bt_event);
        bt_Home.setActivated(true);
        bt_NotesAndPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_NotesAndPP.setActivated(true);
                bt_Home.setActivated(false);
                bt_Class.setActivated(false);
                bt_Event.setActivated(false);
                FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.Fragment_place, new NotesAndPastPapers());
                ft.commit();
            }
        });

        bt_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_NotesAndPP.setActivated(false);
                bt_Home.setActivated(true);
                bt_Class.setActivated(false);
                bt_Event.setActivated(false);
                FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.Fragment_place, new Home());
                ft.commit();
            }
        });

        bt_Class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_NotesAndPP.setActivated(false);
                bt_Home.setActivated(false);
                bt_Class.setActivated(true);
                bt_Event.setActivated(false);
                FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.Fragment_place, new Class());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        bt_Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_NotesAndPP.setActivated(false);
                bt_Home.setActivated(false);
                bt_Class.setActivated(false);
                bt_Event.setActivated(true);
                FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.Fragment_place, new Event());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }if (id == R.id.action_aboutUs) {
            Intent intent=new Intent(this,AboutUs.class);
            startActivity(intent);
            return true;
        }if (id == R.id.action_user) {
            Intent intent=new Intent(this,User.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();

            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();

                if(x1>x2){
//                    Toast.makeText(this, "x"+x1+"y"+x2, Toast.LENGTH_SHORT).show();
                    if (changer == 0) {
                        bt_NotesAndPP.setActivated(true);
                        bt_Home.setActivated(false);
                        bt_Class.setActivated(false);
                        bt_Event.setActivated(false);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.Fragment_place, new NotesAndPastPapers());
                        ft.commit();
                        changer = 1;
                    }else if (changer == 1){
                        bt_NotesAndPP.setActivated(false);
                        bt_Home.setActivated(false);
                        bt_Class.setActivated(false);
                        bt_Event.setActivated(true);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.Fragment_place, new Event());
                        ft.commit();
                        changer = 2;
                    }else if (changer == 2){
                        bt_NotesAndPP.setActivated(false);
                        bt_Home.setActivated(false);
                        bt_Class.setActivated(true);
                        bt_Event.setActivated(false);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.Fragment_place, new Class());
                        ft.commit();
                        changer = 3;
                    }else{
                        bt_NotesAndPP.setActivated(false);
                        bt_Home.setActivated(true);
                        bt_Class.setActivated(false);
                        bt_Event.setActivated(false);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.Fragment_place, new Home());
                        ft.commit();
                        changer = 0;
                    }
                }else if(x1<x2){
                    if (changer == 0) {
                        bt_NotesAndPP.setActivated(false);
                        bt_Home.setActivated(false);
                        bt_Class.setActivated(true);
                        bt_Event.setActivated(false);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.Fragment_place, new Class());
                        ft.commit();
                        changer = 3;
                    }else if (changer == 1){
                        bt_NotesAndPP.setActivated(false);
                        bt_Home.setActivated(true);
                        bt_Class.setActivated(false);
                        bt_Event.setActivated(false);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.Fragment_place, new Home());
                        ft.commit();
                        changer = 0;
                    }else if (changer == 2){
                        bt_NotesAndPP.setActivated(true);
                        bt_Home.setActivated(false);
                        bt_Class.setActivated(false);
                        bt_Event.setActivated(false);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.Fragment_place, new NotesAndPastPapers());
                        ft.commit();
                        changer = 1;
                    }else {
                        bt_NotesAndPP.setActivated(false);
                        bt_Home.setActivated(false);
                        bt_Class.setActivated(false);
                        bt_Event.setActivated(true);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.Fragment_place, new Event());
                        ft.commit();
                        changer = 2;
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime +2000 > System.currentTimeMillis()){
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return;
        } else {
            Toast.makeText(getBaseContext(),"Press again to exit",Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}
