package com.g0y.auth.oauth.google.component;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

/**
 * specification defined by Google
 * */
@Component
public class GoogleOAuthService {

    @Value("${google.platform.channel.channelId:xxxxx}")
    private String channelId;
    @Value("${google.platform.channel.channelSecret:xxxxxx}")
    private String channelSecret;
    @Value("${google.platform.channel.callbackUrl:xxxxxx}")
    private String callbackUrl;

    /** scope of requiring data */
    private static final String[] SCOPE = {"email", "profile"};

    /** class render oauth of google */
    private GoogleAuthorizationCodeFlow authorizationCodeFlow;

    /** constructor */
    @PostConstruct
    public void init(){
        this.authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                channelId,
                channelSecret,
                Arrays.asList(SCOPE)).build();
    }

    /**
     * get Google auth url
     *
     * @param state secure code
     * */
    public String getAuthUrl(String state){
        return authorizationCodeFlow.newAuthorizationUrl().setRedirectUri(callbackUrl).setState(state).build();
    }


    /**
     * get accessToken
     *
     * @param authorizationCode code got from call-back request
     * */
    public GoogleTokenResponse getAccessToken(String authorizationCode) throws IOException {
        GoogleAuthorizationCodeTokenRequest authorizationCodeTokenRequest = authorizationCodeFlow.newTokenRequest(authorizationCode).setRedirectUri(callbackUrl);
        return authorizationCodeTokenRequest.execute();
    }
}
