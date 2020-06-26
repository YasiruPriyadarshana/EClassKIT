package com.wonder.eclasskit.Adpter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wonder.eclasskit.MyResult;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.QuizHm;
import com.wonder.eclasskit.R;

import java.util.ArrayList;

public class ListAdapterQuizHome  extends ArrayAdapter<QuizHm> {
    private static final String TAG="ListAdapterQuizHome";
    private Context mContext;
    int mResource;
    private ArrayList<String> keys;
    private CallbackDelete mCallback;

    public interface CallbackDelete{
        void onHandledelete();
    }

    public ListAdapterQuizHome(Context context, int resource, ArrayList<QuizHm> objects, ArrayList<String> keys, CallbackDelete callback) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
        this.keys=keys;
        mCallback=callback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name=getItem(position).getName();
        String time=getItem(position).getTime();
        final String key=keys.get(position);
        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView namef=(TextView)convertView.findViewById(R.id.quest_home_name);
        TextView timef=(TextView)convertView.findViewById(R.id.time_lbl);
        namef.setText(name);
        timef.setText(time+" Min");

        final ImageButton result=(ImageButton) convertView.findViewById(R.id.myresult_btn);
        result.setFocusable(false);
        result.setFocusableInTouchMode(false);
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view;
                PopupMenu popup = new PopupMenu(v.getContext(),v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.memu_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        if (id == R.id.action_result) {
                            Intent intent=new Intent(getContext(), MyResult.class);
                            intent.putExtra("key",key);
                            mContext.startActivity(intent);
                            return true;
                        }else {
                            AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                            adb.setMessage("Are you sure?");
                            adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("quizHome/"+ Common.uid);

                                    mCallback.onHandledelete();
                                    databaseReference.child(keys.get(position)).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(mContext, "Long press deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }
                            });
                            adb.setNegativeButton("No",null);
                            adb.show();
                            return true;
                        }


                    }
                });
                popup.show();
            }
        });

        return convertView;
    }

}
