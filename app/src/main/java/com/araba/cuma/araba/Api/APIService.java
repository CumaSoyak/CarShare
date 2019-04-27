package com.araba.cuma.araba.Api;

import com.araba.cuma.araba.Notifications.MyResponse;
import com.araba.cuma.araba.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA93QPqDg:APA91bEd5OXiGsM8MvRxJanr3OpkRz-Wq8rL0MXSdxC52a_a4bBiqjQ5HE2wE1iHYFPlMZluKCmZlP7i_bVOMYr3bZj9eG0AXl6gIEA-pbirIVfRWIwiAllzJJpLxmMs_hxH9FAYOEdJ"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}

