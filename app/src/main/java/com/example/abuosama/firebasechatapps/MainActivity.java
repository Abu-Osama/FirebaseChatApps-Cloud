package com.example.abuosama.firebasechatapps;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    ListView listView;
    ArrayList<String>arrayList;
    ArrayAdapter<String>arrayAdapter;
    //cloud url references var
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText= (EditText) findViewById(R.id.editText1);
        button= (Button) findViewById(R.id.button1);
        listView= (ListView) findViewById(R.id.listview1);
        arrayList=new ArrayList<String>();
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        //point to cloud reference
        reference= FirebaseDatabase.getInstance().getReference();
        //now let us read cloud data
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear aaray list
                arrayList.clear();
                for(DataSnapshot s:dataSnapshot.getChildren()){

                    String mobile= (String) s.child("mobile").getValue();
                    String message= (String) s.child("message").getValue();
                    arrayList.add(0,"mobile : "+mobile+'\n'+"message : "+message);


                }

                 arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //button click;
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                //we have to push message from one phone to other phone
                HashMap<String,String> map=new HashMap<String, String>();
                //get user mobile number from telephone
                //TelephonyManager manager= (TelecomManager) getSystemService(TELECOM_SERVICE);
               // TelephonyManager telephonyManager= (TelephonyManager) getSystemService(TELECOM_SERVICE);

                TelephonyManager telephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String mobilenum=telephonyManager.getLine1Number();

                String message=editText.getText().toString();
                //insert message to hash map
                map.put("mobile",mobilenum);
                //insert
                map.put("message",message);
                //now send the hash map which conyains mobile num message
                reference.push().setValue(map);


            }
        });

    }
}
