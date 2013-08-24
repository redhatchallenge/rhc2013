package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
@RemoteServiceRelativePath("CaptchaService")
public interface CaptchaService extends RemoteService {

    public String verifyCaptcha(String challenge, String response) throws IllegalArgumentException;

    public static class Util {
        private static final CaptchaServiceAsync Instance = (CaptchaServiceAsync) GWT.create(CaptchaService.class);

        public static CaptchaServiceAsync getInstance() {
            return Instance;
        }
    }
}
