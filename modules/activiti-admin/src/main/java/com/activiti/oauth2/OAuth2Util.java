package com.activiti.oauth2;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OAuth2Util {

    public static final String BEARER_PREFIX = "Bearer ";

    private final static Logger log = LoggerFactory.getLogger(OAuth2Util.class);

    private static final String UTF_8 = "UTF-8";

    private static Map<OAuth2Credentials, OAuth2AccessToken> tokens = new ConcurrentHashMap<OAuth2Credentials, OAuth2AccessToken>();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        return mapper;
    }

    public static OAuth2AccessToken getAccessToken(String iamUrl, String clientSecret, OAuth2Credentials credentials) throws Exception {
        OAuth2AccessToken accessToken = tokens.get(credentials);
        if (accessToken != null && !accessToken.isExpired()) {
            log.info("getAccessToken, found iamUrl: {}, clientSecret: {}, accessToken: {}", iamUrl, clientSecret, accessToken);
            return accessToken;
        }

        String urlParameters = "client_id=vca-dialogmanager&grant_type=password"
            + "&client_secret=" + clientSecret
            + "&username=" + credentials.getUsername()
            + "&password=" + credentials.getPassword();
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;

        URL url = new URL(iamUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setReadTimeout(30000);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        connection.setUseCaches(false);
        try {
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.write(postData);
        } finally {
            log.debug("getAccessToken, connection.getOutputStream()");
        }
        connection.connect();

        if (connection.getResponseCode() != 200) {
            String errorData = IOUtils.toString(connection.getErrorStream(), UTF_8);
            throw new RuntimeException("Failed getAccessToken: " + errorData);
        }
        String json = IOUtils.toString(connection.getInputStream(), UTF_8);
        ObjectMapper mapper = createObjectMapper();
        OAuth2AccessToken accessTokenResult = mapper.readValue(json, OAuth2AccessToken.class);

        if (accessTokenResult != null) {
            tokens.put(credentials, accessTokenResult);
        }

        log.info("getAccessToken, created iamUrl: {}, clientSecret: {}, accessToken: {}", iamUrl, clientSecret, accessToken);
        return accessToken;
    }
}
