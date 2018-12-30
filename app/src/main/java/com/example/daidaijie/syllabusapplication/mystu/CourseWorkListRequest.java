package com.example.daidaijie.syllabusapplication.mystu;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CourseWorkListRequest {

    private String Cookie;
    private String course_linkid;
    private Handler courseworklistHandler;
    private Context context;

    CourseWorkListRequest() {

    }

    CourseWorkListRequest(String Cookie, String course_linkid, Handler worklistHandler,Context context) {
        this.Cookie = Cookie;
        this.course_linkid = course_linkid;
        this.courseworklistHandler = worklistHandler;
        this.context =context;
    }

    public void getWorkList() {
        //步骤4:创建Retrofit对象

        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl("https://class.stuapps.com") // 设置 网络请求 Url

                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)

                .build();


        // 步骤5:创建 网络请求接口 的实例

        Getdata_Interface cookiesRequest = retrofit.create(Getdata_Interface.class);

        Call<ResponseBody> call = cookiesRequest.getCourseWorkList(Cookie, course_linkid);


        //步骤6:发送网络请求(异步)

        call.enqueue(new Callback<ResponseBody>() {

            //请求成功时回调

            @Override

            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                // 请求处理,输出结果

                try {

                    String str = response.body().string();
                    JSONObject jsonObject = new JSONObject(str);

                    Message worklistmsg = new Message();
                    worklistmsg.what = 30003;
                    worklistmsg.obj = jsonObject;

                    courseworklistHandler.sendMessage(worklistmsg);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(context,"作业列表数据请求失败",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
