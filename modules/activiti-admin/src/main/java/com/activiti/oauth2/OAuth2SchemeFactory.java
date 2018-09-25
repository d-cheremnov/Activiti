package com.activiti.oauth2;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class OAuth2SchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {

    private String iamUrl;
    private String clientSecret;

    public OAuth2SchemeFactory(String iamUrl, String clientSecret) {
        super();
        this.iamUrl = iamUrl;
        this.clientSecret = clientSecret;
    }

    public OAuth2SchemeFactory() {
        this(null, null);
    }

    public AuthScheme newInstance(final HttpParams params) {
        return new OAuth2Scheme(this.iamUrl, this.clientSecret);
    }

    public AuthScheme create(final HttpContext context) {
        return new OAuth2Scheme(this.iamUrl, this.clientSecret);
    }
}
