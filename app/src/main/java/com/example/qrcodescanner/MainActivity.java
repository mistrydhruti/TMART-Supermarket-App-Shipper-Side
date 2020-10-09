package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity
        implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    private String name,phone,orderid,deliveredTo="";
    private String saveCurrentDate,saveCurrentTime;
    
    private String email="";
    private String purchase="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        purchase=getIntent().getStringExtra("Purchase");
        email=getIntent().getStringExtra("Email");
        name=getIntent().getStringExtra("name");
        phone=getIntent().getStringExtra("phone");
        orderid=getIntent().getStringExtra("orderid");
        deliveredTo=getIntent().getStringExtra("deliveredTo");
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("TAG", rawResult.getText()); // Prints scan results
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);

        nextActivity();
    }

    private void nextActivity() {

        DatabaseReference order = FirebaseDatabase.getInstance().getReference().child("Orders").child("User Order Details").child(orderid);

        order.child("state").setValue("delivered");

        DatabaseReference df=FirebaseDatabase.getInstance().getReference().child("Assigned Orders").child(phone).child(orderid);

        df.child("state").setValue("delivered");

                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Order History");

                Calendar calendar=Calendar.getInstance();

                SimpleDateFormat currentdate=new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate=currentdate.format(calendar.getTime());

                SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime=currenttime.format(calendar.getTime());

                HashMap<String,Object> orderhist=new HashMap<>();
                orderhist.put("OrderId",orderid);
                orderhist.put("DeliveredTo",deliveredTo);
                orderhist.put("ShipperName",name);
                orderhist.put("ShipperPhoneNo",phone);
                orderhist.put("DeliveredDate",saveCurrentDate);
                orderhist.put("DeliveredTime",saveCurrentTime);
                orderhist.put("CustomerEmail",email);
                orderhist.put("PurchaeAmount",purchase);

                ref.child(orderid).updateChildren(orderhist).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(MainActivity.this,NewOrders.class);
                        i.putExtra("Phoneno",phone);
                        startActivity(i);
                        finish();

                        sendmail();

                    }
                });
    }

    private void sendmail() {
        String subject = "Order Delivered!!";
        String message = "Your Order "+orderid+" delivered successfully. Keep using TMART for more exciting offers.";

        //Creating SendMail object
        SendMail sm = new SendMail(MainActivity.this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();

    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }
}