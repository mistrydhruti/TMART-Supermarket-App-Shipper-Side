package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrcodescanner.Common.Common;
import com.example.qrcodescanner.Model.TakeOrderModel;
import com.example.qrcodescanner.Model.Token;
import com.example.qrcodescanner.ViewHolder.TakeOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class NewOrders extends AppCompatActivity {

    private DatabaseReference ord;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String phoneno = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_orders);
        phoneno = getIntent().getStringExtra("Phoneno");
        ord = FirebaseDatabase.getInstance().getReference().child("Assigned Orders").child(phoneno);



        updateToken(FirebaseInstanceId.getInstance().getToken());

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ord.orderByChild("state").equalTo("shipped").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int a=(int)dataSnapshot.getChildrenCount();

                if (a==0)
                {
                    android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(NewOrders.this);
                    builder1.setMessage("No New Orders");
                    builder1.setCancelable(true);

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateToken(String token1) {

        FirebaseDatabase db= FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference("Token").child("delivery person");

        Token token = new Token(token1,false);
        tokens.child(phoneno).setValue(token);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<TakeOrderModel> options = new FirebaseRecyclerOptions.Builder<TakeOrderModel>()
                .setQuery(ord.orderByChild("state").equalTo("shipped"), TakeOrderModel.class)
                .build();
        FirebaseRecyclerAdapter<TakeOrderModel, TakeOrderViewHolder> adapter = new FirebaseRecyclerAdapter<TakeOrderModel, TakeOrderViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull TakeOrderViewHolder holder, int i, @NonNull final TakeOrderModel model) {
                holder.orderId.setText(model.getOrderId());
                holder.name.setText(model.getDeliveryTo());
                holder.address.setText(model.getDeliveryAddress());
                holder.expectedtime.setText(model.getExpectedTime());
                holder.address.setMovementMethod(new ScrollingMovementMethod());
                if (model.getPayment().equals("PayPal")) {
                    holder.payment.setText("PayPal=Paid");
                } else {
                    holder.payment.setText("Cash =" + model.getTotalamount());
                }

                holder.orderdelivered.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(NewOrders.this,MainActivity.class);
                        i.putExtra("name",model.getShipperName());
                        i.putExtra("orderid",model.getOrderId());
                        i.putExtra("phone",model.getShipperPhoneNo());
                        i.putExtra("deliveredTo",model.getDeliveryTo());
                        i.putExtra("Email",model.getCustomerEmail());
                        i.putExtra("Purchase",model.getTotalamount());
                        startActivity(i);
                    }
                });
                holder.report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(NewOrders.this,ReportActivity.class);
                        i.putExtra("OrderId",model.getOrderId());
                        i.putExtra("Shipperphone",model.getShipperPhoneNo());
                        startActivity(i);
                        overridePendingTransition(0,0);
                    }
                });
            }

            @NonNull
            @Override
            public TakeOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.take_orders_layout, parent, false);
                TakeOrderViewHolder holder = new TakeOrderViewHolder(view);
                return holder;
            }

        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    public void signout(View view) {
        Intent i = new Intent(NewOrders.this,Choose.class);
        startActivity(i);
        overridePendingTransition(0,0);
        Paper.book().destroy();
    }
}