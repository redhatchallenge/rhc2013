package org.ayrx.rhchallenge.shared;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */

@Entity
public class ConfirmationTokens implements Serializable {

    private String email;
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
