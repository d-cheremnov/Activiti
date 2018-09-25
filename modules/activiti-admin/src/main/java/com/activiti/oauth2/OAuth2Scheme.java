/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package com.activiti.oauth2;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.message.BasicHeader;

/**
 * OAuth2 authentication scheme
 */
public class OAuth2Scheme implements AuthScheme {

    public static final String NAME = "oauth2";
    private static final String BEARER_PREFIX = "Bearer ";

    private String iamUrl;
    private String clientSecret;

    private boolean complete;

    public OAuth2Scheme(String iamUrl, String clientSecret) {
        this.iamUrl = iamUrl;
        this.clientSecret = clientSecret;
        this.complete = false;
    }

    @Override
    public void processChallenge(Header header) throws MalformedChallengeException {

    }

    @Override
    public String getSchemeName() {
        return NAME;
    }

    @Override
    public String getParameter(String name) {
        return null;
    }

    @Override
    public String getRealm() {
        return null;
    }

    @Override
    public boolean isConnectionBased() {
        return false;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
        Header header;
        try {
            OAuth2AccessToken accessToken =
                OAuth2Util.getAccessToken(iamUrl, clientSecret, new OAuth2Credentials(credentials.getUserPrincipal().getName(), credentials.getPassword()));
            String authHeader = BEARER_PREFIX + accessToken.getAccessToken();
            header = new BasicHeader(AUTHORIZATION, authHeader);
            complete = true;
        } catch (Exception e) {
            throw new AuthenticationException("Failed authenticate: " + e.getMessage());
        }
        return header;
    }
}
