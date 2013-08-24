package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public interface CaptchaServiceAsync {
    void verifyCaptcha(String challenge, String response, AsyncCallback<String> async);
}
