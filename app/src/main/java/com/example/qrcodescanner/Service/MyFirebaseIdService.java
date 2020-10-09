package com.example.qrcodescanner.Service;

import com.example.qrcodescanner.Common.Common;
import com.example.qrcodescanner.Model.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String tokenRefreshed=FirebaseInstanceId.getInstance().getToken();
        if (Common.currentonlineuser.getPassword()!=null)
            updateTokentoFirebase(tokenRefreshed);
    }

    private void updateTokentoFirebase(String tokenrefreshed) {

        DatabaseReference appr=FirebaseDatabase.getInstance().getReference().child("Approved Login Details");

       // FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase db= FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference("Token").child("delivery person");

        Token token = new Token(tokenrefreshed,false);
        tokens.child(Common.currentonlineuser.getPassword()).setValue(token);
    }
}
