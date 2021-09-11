package com.g0y.auth.oauth.interfacepack;

import com.g0y.auth.oauth.model.GetAccessTokenContext;

/** get AccessToken */
public interface GetAccessTokenService {

    /**
     * within redirect page, api Request for access token after getting authorization code
     * TODO 取得accessToken後把資訊儲存在redis內
     *
     * @param getAccessTokenContext params context */
    String getAccessToken(GetAccessTokenContext getAccessTokenContext);
}
