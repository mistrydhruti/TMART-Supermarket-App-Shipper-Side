package com.example.qrcodescanner.Remote;

import com.example.qrcodescanner.Model.MyResponse;
import com.example.qrcodescanner.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAy5nJ4bE:APA91bHM5EwaxJiq3RjQ2U4NzQhNf8E5vtL8s2w3z9c6GyzzTVvR7prxHk0REnsMDF9V4PeXc0YBd0hRwB28J-kacGenH_Ja-bL4JLbtk_3riulgCdnIAIB3l1co-avlMGsAyHVYwqZB"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
