package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ReportActivity extends AppCompatActivity {

    private RadioGroup rg;
    private RadioButton radioButton;
    private DatabaseReference report;
    private String shipphone,orderid;
    private String reportselected;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        report = FirebaseDatabase.getInstance().getReference().child("Reports");

        loadingBar=new ProgressDialog(this);

        orderid=getIntent().getStringExtra("OrderId");
        shipphone=getIntent().getStringExtra("Shipperphone");
        rg=(RadioGroup)findViewById(R.id.rg);


    }

    public void report(View view) {
        int selectedId = rg.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        reportselected=radioButton.getText().toString();
        if(selectedId==-1) {
            Toast.makeText(ReportActivity.this, "Nothing selected", Toast.LENGTH_LONG).show(); }
        else
        {
            addtodb();
        }

    }

    private void addtodb() {
        loadingBar.setTitle("Sending Report to Admin !!");
        loadingBar.setMessage("Please wait !");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        HashMap<String,Object> reportmap=new HashMap<>();
        reportmap.put("OrderId",orderid);
        reportmap.put("ShipperPhoneNo",shipphone);
        reportmap.put("Report Details",reportselected);

        report.child(shipphone).updateChildren(reportmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                Toast.makeText(ReportActivity.this, "Report Submitted Successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ReportActivity.this,NewOrders.class);
                startActivity(i);
                overridePendingTransition(0,0);
            }
        });

    }
}
