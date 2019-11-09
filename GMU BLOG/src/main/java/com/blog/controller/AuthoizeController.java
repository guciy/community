package com.blog.controller;

import com.blog.dto.AccessTokenDTO;
import com.blog.dto.GithubUser;
import com.blog.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthoizeController {
    @Autowired
    GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.setRedirect.uri}")
    private String redirectUri;
    @Value("${github.Client.secret}")
    private String clientSecret;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) {
        //配置AccessTokenDTO
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        //调用GithubProvider 方法
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);

        System.out.println(user.getName());
        if (user != null) {
            //登录成功
            request.getSession().setAttribute("user", user);
            return "redirect:/";
        } else {
            //登录失败
            return "redirect:/";
        }
    }
}
