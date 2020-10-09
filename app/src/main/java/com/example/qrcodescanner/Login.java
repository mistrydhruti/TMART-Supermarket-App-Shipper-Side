package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcodescanner.Common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    private String ID,Password,i,p;
    private CheckBox remme;
    DatabaseReference ref;
    private EditText id1,pass;
    private Button login;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CircleImageView lo = (CircleImageView) findViewById(R.id.iv1);

        loadingbar=new ProgressDialog(this);

        remme=findViewById(R.id.remeberme);
        Paper.init(this);
         id1=(EditText)findViewById(R.id.Id1);
         pass=(EditText)findViewById(R.id.password);
         login=(Button)findViewById(R.id.login);
        Animation animation = new AlphaAnimation((float) 5, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        // animation
        // rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation
        // infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
        // end so the button will
        // fade back in

        lo.startAnimation(animation);
        
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Validateuser();
            }
        });
        
        

    }

    private void Validateuser() {
        ID=id1.getText().toString().trim();
        Password=pass.getText().toString().trim();

        if(TextUtils.isEmpty(ID))
        {
            id1.setError("This field is required");
            id1.requestFocus();
        }
        if(TextUtils.isEmpty(Password))
        {
            pass.setError("This field is required");
            pass.requestFocus();
        }
        else
        {
            CheckCredentials(ID,Password);
            loadingbar.setTitle("Logging in");
            loadingbar.setMessage("Please wait unitil we check your credentials.");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
        }
    }

    private void CheckCredentials(String ID, String password) {

        if (remme.isChecked())
        {
            Paper.book().write(Common.UserIdKey,ID);
            Paper.book().write(Common.UserPasswordKey,Password);
        }

        ref= FirebaseDatabase.getInstance().getReference().child("Approved Login Details").child(Password);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                     i=dataSnapshot.child("ID").getValue().toString();
                     p=dataSnapshot.child("Password").getValue().toString();


                     check();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void check() {
        if(ID.equals(i) && Password.equals(p))
        {
            loadingbar.dismiss();
            Toast.makeText(this, " Logged in successfully", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Login.this,NewOrders.class);
            i.putExtra("Phoneno",Password);
            startActivity(i);
        }
    }
}
