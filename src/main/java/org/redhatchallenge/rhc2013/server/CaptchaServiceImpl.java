package org.redhatchallenge.rhc2013.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.redhatchallenge.rhc2013.client.CaptchaService;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class CaptchaServiceImpl extends RemoteServiceServlet implements CaptchaService {

    @Override
    public String verifyCaptcha(String challenge, String response) {

        String remoteAddr = getThreadLocalRequest().getRemoteAddr();
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey("6LfzBeUSAAAAAHumSudxu6Aq1ar0mhgU46uH8fiU");

        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, response);

        if(reCaptchaResponse.isValid()) {
            return null;
        }

        else {
            return reCaptchaResponse.getErrorMessage();
        }
    }
}