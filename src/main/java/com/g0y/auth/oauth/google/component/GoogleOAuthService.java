package com.g0y.auth.oauth.google.component;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;

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

    /** decoder of google payload */
    private GoogleIdTokenVerifier googleIdTokenVerifier;

    /** constructor */
    @PostConstruct
    public void init(){
        NetHttpTransport netHttpTransport = new NetHttpTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        this.authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
                netHttpTransport,
                jsonFactory,
                channelId,
                channelSecret,
                Arrays.asList(SCOPE)).build();
        this.googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(
                netHttpTransport,
                jsonFactory)
                .setAudience(Collections.singletonList(channelId))
                .build();
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

    /**
     * get paylaod information of idtoken
     *
     * @param idToken id token received from google.accessToken pojo
     * */
    public GoogleIdToken.Payload getPayload(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdToken googleIdToken;

        googleIdToken = googleIdTokenVerifier.verify(idToken);
        return googleIdToken.getPayload();
    }
}
