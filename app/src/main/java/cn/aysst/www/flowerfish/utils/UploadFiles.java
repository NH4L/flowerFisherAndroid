package cn.aysst.www.flowerfish.utils;

import android.util.Log;
import okhttp3.*;

import java.io.*;


public class UploadFiles {
    static OkHttpClient client = new OkHttpClient();



    public static String uploadHeadPortrait (String filePath, String username) {
        String result = "fail";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //直接将姓名代替为id
        builder.addFormDataPart("id", username);
        builder.addFormDataPart("image", filePath,
                RequestBody.create(MediaType.parse("image/*"), new File(filePath)));

        RequestBody requestBody1 = builder.build();
        Request.Builder reqBuilder1 = new Request.Builder();
        Request request1 = reqBuilder1
                .url(Http.BASE_URL + "/UploadImageServlet")
                .post(requestBody1)
                .build();
        Log.d("请求地址 ", Http.BASE_URL + "/UploadImageServlet");
        try{
            Response response = client.newCall(request1).execute();
            Log.d("响应码 ", "" + response.code());
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                Log.d("响应body ", resultValue);
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}