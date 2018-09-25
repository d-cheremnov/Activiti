package com.activiti.oauth2;

import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuth2AccessToken {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("refresh_expires_in")
    private Integer refreshExpiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("not-before-policy")
    private Long notBeforePolicy;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("session_state")
    private String sessionState;

    private Date created = new Date();

    public OAuth2AccessToken() {
        super();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Integer getRefreshExpiresIn() {
        return refreshExpiresIn;
    }

    public void setRefreshExpiresIn(Integer refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getNotBeforePolicy() {
        return notBeforePolicy;
    }

    public void setNotBeforePolicy(Long notBeforePolicy) {
        this.notBeforePolicy = notBeforePolicy;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSessionState() {
        return sessionState;
    }

    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(created);
        calendar.add(Calendar.SECOND, expiresIn);
        return calendar.getTime();
    }

    public boolean isExpired() {
        return getExpiryDate().before(new Date());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accessToken == null) ? 0 : accessToken.hashCode());
        result = prime * result + ((created == null) ? 0 : created.hashCode());
        result = prime * result + ((expiresIn == null) ? 0 : expiresIn.hashCode());
        result = prime * result + ((notBeforePolicy == null) ? 0 : notBeforePolicy.hashCode());
        result = prime * result + ((refreshExpiresIn == null) ? 0 : refreshExpiresIn.hashCode());
        result = prime * result + ((refreshToken == null) ? 0 : refreshToken.hashCode());
        result = prime * result + ((scope == null) ? 0 : scope.hashCode());
        result = prime * result + ((sessionState == null) ? 0 : sessionState.hashCode());
        result = prime * result + ((tokenType == null) ? 0 : tokenType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OAuth2AccessToken other = (OAuth2AccessToken) obj;
        if (accessToken == null) {
            if (other.accessToken != null) {
                return false;
            }
        } else if (!accessToken.equals(other.accessToken)) {
            return false;
        }
        if (created == null) {
            if (other.created != null) {
                return false;
            }
        } else if (!created.equals(other.created)) {
            return false;
        }
        if (expiresIn == null) {
            if (other.expiresIn != null) {
                return false;
            }
        } else if (!expiresIn.equals(other.expiresIn)) {
            return false;
        }
        if (notBeforePolicy == null) {
            if (other.notBeforePolicy != null) {
                return false;
            }
        } else if (!notBeforePolicy.equals(other.notBeforePolicy)) {
            return false;
        }
        if (refreshExpiresIn == null) {
            if (other.refreshExpiresIn != null) {
                return false;
            }
        } else if (!refreshExpiresIn.equals(other.refreshExpiresIn)) {
            return false;
        }
        if (refreshToken == null) {
            if (other.refreshToken != null) {
                return false;
            }
        } else if (!refreshToken.equals(other.refreshToken)) {
            return false;
        }
        if (scope == null) {
            if (other.scope != null) {
                return false;
            }
        } else if (!scope.equals(other.scope)) {
            return false;
        }
        if (sessionState == null) {
            if (other.sessionState != null) {
                return false;
            }
        } else if (!sessionState.equals(other.sessionState)) {
            return false;
        }
        if (tokenType == null) {
            if (other.tokenType != null) {
                return false;
            }
        } else if (!tokenType.equals(other.tokenType)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OAuth2AccessToken [accessToken=" + accessToken + ", expiresIn=" + expiresIn + ", refreshExpiresIn="
            + refreshExpiresIn + ", refreshToken=" + refreshToken + ", tokenType=" + tokenType
            + ", notBeforePolicy=" + notBeforePolicy + ", scope=" + scope + ", sessionState=" + sessionState
            + ", created=" + created + "]";
    }
}
