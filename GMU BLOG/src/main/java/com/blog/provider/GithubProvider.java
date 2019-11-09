package com.blog.provider;

import com.alibaba.fastjson.JSON;
import com.blog.dto.AccessTokenDTO;
import com.blog.dto.GithubUser;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
           MediaType mediaType
                = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));  //BUG:import okhttp3.RequestBody;
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")   //post 网址返回access_token值；get网址返回html标记
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();  //Redirect_uri("http://localhost:8080/callback")返回的code
            System.out.println(string);  //access_token=86b7c6bb79eb9c13693744e6760701dbe6204ed4&scope=user&token_type=bearer
            String token=string.split( "&")[0].split("=")[1];
            System.out.println(token);   //86b7c6bb79eb9c13693744e6760701dbe6204ed4
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return  githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
