package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String[] country = { "Parel", "Lalbaug", "Mulund", "Kanjurmarg", "Kurla"};
    private EditText name,phone,email,aadhar,driving_license;
    private String Name,Email,Phone,Aadhar,Driving,location,saveCurrentDate,saveCurrentTime;
    private CircleImageView photo;
    private Spinner spin;
    private Button submit;
    private static final int GalleryPick=1;
    private Uri ImageUri;
    private String productRandomKey,downloadImageUri;
    private StorageReference delimageref;
    private DatabaseReference delref;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name=findViewById(R.id.delboy_name);
        phone=findViewById(R.id.delboy_phone);
        email=findViewById(R.id.delboy_email);
        aadhar=findViewById(R.id.delboy_adhar);
        driving_license=findViewById(R.id.delboy_license);
        photo=findViewById(R.id.delboy_photo);
        spin=findViewById(R.id.delboy_spinner);
        submit=findViewById(R.id.submit_btn);
        loadingBar=new ProgressDialog(this);





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validations();
            }
        });
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);



        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenGallery();

            }
        });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            photo.setImageURI(ImageUri);
        }
    }



    private void Validations() {
        Name=name.getText().toString();
        Email=email.getText().toString();
        Phone=phone.getText().toString();
        Aadhar=aadhar.getText().toString();
        Driving=driving_license.getText().toString();

        if(TextUtils.isEmpty(Name))
        {
            name.setError("This field is required");
            name.requestFocus();
        }
        else if(TextUtils.isEmpty(Phone))
        {
            phone.setError("This field is required");
            phone.requestFocus();
        }
        else if(TextUtils.isEmpty(Email))
        {
            email.setError("This field is required");
            email.requestFocus();
        }
        else if(TextUtils.isEmpty(Aadhar))
        {
            aadhar.setError("This field is required");
            aadhar.requestFocus();
        }
        else if(TextUtils.isEmpty(Driving))
        {
            driving_license.setError("This field is required");
            driving_license.requestFocus();
        }
        else if(((TextView)spin.getSelectedView()).equals(" "))
        {
            ((TextView) spin.getSelectedView()).setError("this field is required");

        }
        else if(ImageUri == null)
        {
            Toast.makeText(this, "Product image is required", Toast.LENGTH_LONG).show();
        }
        else
        {
            uploadData();
        }


    }

    private void uploadData() {
        loadingBar.setTitle("Registering");
        loadingBar.setMessage("Please wait !");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentdate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currenttime.format(calendar.getTime());

        productRandomKey=saveCurrentDate+saveCurrentTime;
        delimageref= FirebaseStorage.getInstance().getReference().child("Delivery Person Images");

        final StorageReference filepath=delimageref.child(ImageUri.getLastPathSegment() + Phone + ".jpg");


        final UploadTask uploadTask= filepath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(Registration.this, "Error"+message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

               // Toast.makeText(Registration.this, " Image Uploaded succesfuly", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();

                        }
                        downloadImageUri=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUri=task.getResult().toString();
                          //  Toast.makeText(Registration.this, "got the product image uri successfully", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }
                    }
                });

            }
        });




    }

    private void saveProductInfoToDatabase() {
        delref = FirebaseDatabase.getInstance().getReference().child("Delivery Person Details").child(Phone);

        HashMap<String,Object> delmap=new HashMap<>();
        delmap.put("Name",Name);
        delmap.put("Phone",Phone);
        delmap.put("Email",Email);
        delmap.put("Aadhar",Aadhar);
        delmap.put("DrivingLicense",Driving);
        delmap.put("Location",location);
        delmap.put("Photo",downloadImageUri);
        delmap.put("Date",saveCurrentDate);
        delmap.put("Time",saveCurrentTime);
        delmap.put("Status","not approved");


        delref.updateChildren(delmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    Toast.makeText(Registration.this, "Registration Completed ", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Registration.this,Login.class);
                    startActivity(i);
                    loadingBar.dismiss();

                }
                else
                {
                    loadingBar.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(Registration.this, "Error"+message, Toast.LENGTH_SHORT).show();

                }
            }
        });





    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

         location=spin.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
