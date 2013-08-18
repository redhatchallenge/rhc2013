package org.redhatchallenge.rhc2013.client;

import com.google.gwt.i18n.client.Messages;

import javax.mail.Message;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public interface MessageMessages extends Messages {
    @Messages.DefaultMessage("Hello")
    String hello();

    @Messages.DefaultMessage("Oops, are you sure you are logged in?")
    String loginError();

    @Messages.DefaultMessage("Take the Challenge Now")
    String takeChallenge();

    @Messages.DefaultMessage("Oops, something went wrong...")
    String somethingWrong();

    @Messages.DefaultMessage("Error with your confirmation token")
    String confirmationTokenError();

    @Messages.DefaultMessage("Thank you for confirming your account!")
    String confirmedAccount();

    @Messages.DefaultMessage("An unexpected error has occurred, please try again later!")
    String unexpectedError();

    @Messages.DefaultMessage("Your login attempt was unsuccessful, please double check your inputs.")
    String loginUnsuccessful();

    @Messages.DefaultMessage("You are probably not logged in")
    String probablyNotLoginIn();

    @Messages.DefaultMessage("Profile update successful!")
    String profileUpdated();

    @Messages.DefaultMessage("Profile update failed! Please double check your inputs!")
    String profileUpdateFail();

    @Messages.DefaultMessage("Invalid email format!")
    String invalidEmailFormat();

    @Messages.DefaultMessage("Someone has already used this email/contact. Try another?")
    String emailTaken();

    @Messages.DefaultMessage("<h1>Hi {0}, almost there!</h1><br/><h1>Go to {1} and confirm your registration.</h1> <br/> <h1>Tell your friends that you have registered for Red Hat Challenge 2013. Get them to join as well!</h1>")
    String verifyMailMessage(String firstname, String email);

    @Messages.DefaultMessage("Error with password reset token")
    String passwordTokenError();

    @Messages.DefaultMessage("<h1>Password reset is successful!</h1>")
    String passwordResetSuccess();

    @Messages.DefaultMessage("<h1>An unexpected error has occurred, please try resetting your password again.</h1>")
    String errorResetPassword();

    @Messages.DefaultMessage("Please check your email for instructions to reset your password.")
    String passwordResetInstruction();

    @Messages.DefaultMessage("Error with password reset, please check that your email and contact is correct.")
    String passwordResetError();

    @Messages.DefaultMessage("Days")
    String days();



}
