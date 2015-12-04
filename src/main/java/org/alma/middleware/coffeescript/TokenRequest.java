package org.alma.middleware.coffeescript;

import java.io.Serializable;

/**
 * Created by Maxime on 04/12/2015.
 */
public class TokenRequest implements Serializable {
    private String token;

    public TokenRequest(String token) {
        this.token = token;
    }

    public TokenRequest() {
        this(null);
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}