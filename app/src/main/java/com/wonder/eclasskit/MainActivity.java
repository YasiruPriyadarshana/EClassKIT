package com.wonder.eclasskit;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.PageController;

public class MainActivity extends AppCompatActivity {
    Toolbar mToolbar;
    TabLayout mTabLayout;
    TabItem home,note,event,cls;
    ViewPager mPager;
    PageController pageController;
    private long backPressedTime;
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");
        adView=(AdView)findViewById(R.id.adView);
        AdRequest adRequest= new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        mTabLayout=findViewById(R.id.tablayout);
        home=findViewById(R.id.home);
        note=findViewById(R.id.note);
        event=findViewById(R.id.event);
        cls=findViewById(R.id.cls);
        mPager = findViewById(R.id.viewpager);

        pageController = new PageController(getSupportFragmentManager(),mTabLayout.getTabCount());
        mPager.setAdapter(pageController);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (Common.limit == 1) {
            MenuItem item = menu.findItem(R.id.action_addclass);
            item.setVisible(false);   //hide it
        }else {
            MenuItem item = menu.findItem(R.id.action_user);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Settings) {
            Intent intent=new Intent(this,Settings.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_aboutUs) {
            Intent intent=new Intent(this,AboutUs.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_user) {
            Intent intent=new Intent(this,User.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_addclass) {
            Intent intent=new Intent(this,AddNewCourse.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        if(backPressedTime +2000 > System.currentTimeMillis()){
            moveTaskToBack(true);
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
            return;
        } else {
            Toast.makeText(getBaseContext(),"Press again to exit",Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}
