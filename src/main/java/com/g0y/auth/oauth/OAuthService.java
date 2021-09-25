package com.g0y.auth.oauth;

import com.g0y.auth.component.utils.AdapterUtils;
import com.g0y.auth.component.utils.SpringContextUtils;
import com.g0y.auth.controller.model.AuthPageAdapterContext;
import com.g0y.auth.controller.model.AuthPageRq;
import com.g0y.auth.controller.model.GetTokenInfoRs;
import com.g0y.auth.exception.model.InvalidAgencyException;
import com.g0y.auth.oauth.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * TODO should optimize the way throwing parameter - agency
 * whole OAuth Process
 * */
@Service
public class OAuthService {

    /** name of method getting Auth url */
    private static final String AUTHPAGE_URL = "getAuthPageUrl";

    /** name of method getting access token */
    private static final String ACCESSTOKEN = "getTokenInfo";

    /** name of method verifying access token */
    private static final String VERIFYTOKEN= "verifyToken";

    /** name of method decoding id token, whose payload includes user information */
    private static final String DECODE_IDTOKEN = "getUserInfo";

    /** suffix of class implementing oauth of which vendor provides*/
    private static final String SUFFIX_OF_CLASS = "OAuth2";

    /** name of vendor */
    private String agency;

    @Autowired
    private AdapterUtils adapterUtils;

    /**
     * get Auth page provided by vendor
     *
     * @param getAuthPageUrlContext context
     * */
    public String getUrl(GetAuthPageUrlContext getAuthPageUrlContext) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        agency = getAuthPageUrlContext.getAgency();
        return getReflectMethod(agency + SUFFIX_OF_CLASS, AUTHPAGE_URL, getAuthPageUrlContext, String.class);
    }

    /**
     * request for accessToken
     * */
    public GetTokenInfoRs getToken(Map<String, String> authRq, String nonce) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // adapt specific rq
        AuthPageAdapterContext authPageAdapterContext = new AuthPageAdapterContext();
        authPageAdapterContext.setAgency(agency);
        authPageAdapterContext.setRequestParams(authRq);
        authPageAdapterContext.setNonce(nonce);
        AuthPageRq authPageRq = adapterUtils.getAuthPageRq(authPageAdapterContext);

        // get access token
        GetAccessTokenContext getAccessTokenContext = new GetAccessTokenContext();
        getAccessTokenContext.setAuthorizationCode(authPageRq.getCode());
        getAccessTokenContext.setNonce(nonce);
        getAccessTokenContext.setState(authPageRq.getState());
        return getReflectMethod(agency + SUFFIX_OF_CLASS, ACCESSTOKEN, getAccessTokenContext, GetTokenInfoRs.class);
    }

    /**
     * verify token by api requesting agency
     *
     * @param verifyAccessTokenContext context containing params for verifying access Token
     * */
    public VerifyAccessTokenRs verifyAccessToken(VerifyAccessTokenContext verifyAccessTokenContext) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return getReflectMethod(verifyAccessTokenContext.getAgencyName() + SUFFIX_OF_CLASS, VERIFYTOKEN, verifyAccessTokenContext, VerifyAccessTokenRs.class);
    }

    /**
     * decode payload containing user information
     *
     * @param idToken token containing header and payload
     * */
    public GetPayloadInfoRs decodeIdToken(String agency, String idToken) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return getReflectMethod(agency + SUFFIX_OF_CLASS, DECODE_IDTOKEN, idToken, GetPayloadInfoRs.class);
    }

    /**
     * get method object by reflection
     *
     * @param beanName name of bean about to be injected
     * @param methodName function name in bean
     * @param inputParam parameter class for method
     * @param returnType type of return value
     * */
    private <T,R> R getReflectMethod(String beanName, String methodName, T inputParam, Class<R> returnType) {
        Object beanObj = SpringContextUtils.getBean(beanName);
        Object invokeObj = null;
        try{
            Class<? extends OAuth2> agencyObj = (Class<? extends OAuth2>) beanObj.getClass();
            Method getUrlMethod =agencyObj.getMethod(methodName, inputParam.getClass());
            invokeObj = getUrlMethod.invoke(beanObj, inputParam);
        } catch (NoSuchMethodException noSuchMethodException){
            // assume that all valid agency name was set and that the error is only caused by mis injection of agency name from attacker
            throw new InvalidAgencyException("No such auth provider");
        } catch (IllegalAccessException illegalAccessException){

        } catch(InvocationTargetException invocationTargetException){
            // reflecting exceptions included in bean
            try{
                throw invocationTargetException.getCause();
            } catch (GeneralSecurityException ge){

            } catch (IOException ie){

            } catch (Exception e){

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return returnType.cast(invokeObj);
    }

}
