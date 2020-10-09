package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.qrcodescanner.Common.Common;
import com.example.qrcodescanner.Model.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import io.paperdb.Paper;

public class Choose extends AppCompatActivity {

    private String ID,Password,i,p;
    DatabaseReference ref;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        loadingbar=new ProgressDialog(this);
        Paper.init(this);

        String userIdKey=Paper.book().read(Common.UserIdKey);
        String userPasswordKey=Paper.book().read(Common.UserPasswordKey);

        if (userIdKey !=null && userPasswordKey !=null)
        {
            if (!TextUtils.isEmpty(userIdKey) && !TextUtils.isEmpty(userPasswordKey))
            {
                AllowAcess(userIdKey,userPasswordKey);
                loadingbar.setTitle("Already Logged in ");
                loadingbar.setMessage("Please wait unitil we check your credentials.");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
            }
        }
    }

    private void AllowAcess(final String userIdKey, final String userPasswordKey) {
        ref= FirebaseDatabase.getInstance().getReference().child("Approved Login Details").child(userPasswordKey);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    i=dataSnapshot.child("ID").getValue().toString();
                    p=dataSnapshot.child("Password").getValue().toString();


                    check(userIdKey,userPasswordKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void check(String userIdKey, String userPasswordKey) {
        if(userIdKey.equals(i) && userPasswordKey.equals(p))
        {
            loadingbar.dismiss();
            Toast.makeText(this, " Logged in successfully", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Choose.this,NewOrders.class);
            i.putExtra("Phoneno",userPasswordKey);
            startActivity(i);
        }

    }


    public void login(View view) {
        Intent i = new Intent(Choose.this,Login.class);
        startActivity(i);
        finish();
    }

    public void register(View view) {
        Intent i = new Intent(Choose.this,Registration.class);
        startActivity(i);
    }
}
