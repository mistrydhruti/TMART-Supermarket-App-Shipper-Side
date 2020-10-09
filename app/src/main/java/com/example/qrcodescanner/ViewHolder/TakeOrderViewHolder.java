package com.example.qrcodescanner.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcodescanner.Interface.ItemClickListner;
import com.example.qrcodescanner.R;

import org.w3c.dom.Text;

public class TakeOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView orderId,name,address,expectedtime,payment;
    public Button report;
    public Button orderdelivered;
    public ItemClickListner listner;
    public TakeOrderViewHolder(@NonNull View itemView) {
        super(itemView);


        orderId=(TextView)itemView.findViewById(R.id.orderid);
        name=(TextView)itemView.findViewById(R.id.name);
        expectedtime=(TextView)itemView.findViewById(R.id.expectedtime);
        address=(TextView)itemView.findViewById(R.id.address);
        payment=(TextView) itemView.findViewById(R.id.payment);
        orderdelivered=(Button)itemView.findViewById(R.id.orderdelivered);
        report=(Button)itemView.findViewById(R.id.report);





    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner=listner;
    }

    @Override
    public void onClick(View v) {
        listner.onClick(v,getAdapterPosition(),false);

    }
}