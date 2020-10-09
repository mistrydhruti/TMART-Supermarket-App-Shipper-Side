package com.example.qrcodescanner.Common;
import com.example.qrcodescanner.Model.Users;
import com.example.qrcodescanner.Remote.APIService;
import com.example.qrcodescanner.Remote.RetrofitClient;

public class Common {

    private static final String BASE_URL="https://fcm.googleapis.com/";
    public static Users currentonlineuser;

    public static final String UserIdKey="UserId";
    public static final String UserPasswordKey="UserPassword";

    public static APIService getFCMService()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
