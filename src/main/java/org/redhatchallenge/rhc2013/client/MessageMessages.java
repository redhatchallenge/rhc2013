package org.redhatchallenge.rhc2013.client;

import com.google.gwt.i18n.client.LocalizableResource;
import com.google.gwt.i18n.client.Messages;

import javax.mail.Message;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public interface MessageMessages extends Messages {

    @DefaultMessage("You are not logged in, please log in.")
    String errorLogin();

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

    @Messages.DefaultMessage("Thank you for confirming your Email Account, Please <a href='https://redhatchallenge2013-rhc2013.rhcloud.com/?locale=en#login'>Login</a>")
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

    @Messages.DefaultMessage("<h1>Hi {0}, almost there!</h1><br/><h1>Go to {1} and confirm your registration.</h1><br/><h1>Tell your friends that you have registered for Red Hat Challenge 2013. Get them to join as well!</h1>")
    String verifyMailMessage(String firstname, String email);

    @Messages.DefaultMessage("Error with password reset token")
    String passwordTokenError();

    @Messages.DefaultMessage("<h1>Password reset is successful!</h1>")
    String passwordResetSuccess();

    @Messages.DefaultMessage("<h1>An unexpected error has occurred, please try resetting your password again.</h1>")
    String errorResetPassword();

    @Messages.DefaultMessage("<h1>Please check your email for instructions to reset your password.</h1>")
    String passwordResetInstruction();

    @Messages.DefaultMessage("Error with password reset, please check that your email and contact is correct.")
    String passwordResetError();

    @Messages.DefaultMessage("Days")
    String days();

    @Messages.DefaultMessage("Please check the box for the Terms of Use and Privacy Policy before proceeding!")
    String termsCheck();

    @Messages.DefaultMessage("Email field cannot be empty.")
    String emailEmpty();

    @Messages.DefaultMessage("You have entered an invalid email format.")
    String emailInvalidFormat();

    @Messages.DefaultMessage("Password field cannot be empty.")
    String emptyPassword();

    @Messages.DefaultMessage("Your password is not valid. Password should contain at least 8 characters with at least 1 uppercase letter, 1 lowercase letter and 1 numeric character.")
    String passwordInvalidFormat();

    @Messages.DefaultMessage("Confirm password field cannot be empty.")
    String emptyConfirmPassword();

    @Messages.DefaultMessage("Password does not match.")
    String passwordNotMatch();

    @Messages.DefaultMessage("First Name field cannot be empty.")
    String emptyFirstName();

    @Messages.DefaultMessage("Last name cannot be empty.")
    String emptyLastName();

    @Messages.DefaultMessage("Contact field cannot be empty.")
    String emptyContact();

    @Messages.DefaultMessage("You have entered an invalid contact number.")
    String contactInvalid();

    @Messages.DefaultMessage("School field cannot be empty.")
    String emptySchool();

    @Messages.DefaultMessage("You have not been assigned a timeslot")
    String noTimeSlot();

    @Messages.DefaultMessage("Chinese (Traditional)")
    String languageCT();

    @Messages.DefaultMessage("Chinese (Simplified)")
    String languageCS();

    @Messages.DefaultMessage("English")
    String languageEN();

    @Messages.DefaultMessage("Please enter a new password")
    String enterNewPassword();

    @Messages.DefaultMessage("Current password field cannot be empty.")
    String currentPasswordEmpty();

    @Messages.DefaultMessage("Password Changed Successfully!")
    String passwordChangeSuccessful();

    @Messages.DefaultMessage("\"<h2> Please go to <B>\" {0} \"</B> and verify before trying to login again.</h2>")
    String verifyBeforeLogin(String email);

    @Messages.DefaultMessage("<h1><a>Click here to resend the verification email if you have not received it.</a></h1>")
    String verifyBeforeLoginUrl();

    @Messages.DefaultMessage("Error with captcha!")
    String captchaError();

    @Messages.DefaultMessage("Verification Message Sent")
    String verificationMailSent();

    @Messages.DefaultMessage("Password should contain at least 8 characters with at least 1 uppercase letter, 1 lowercase letter and 1 numeric character")
    String passwordPopup();

    @Messages.DefaultMessage("You have enter incorrect password")
    String invalidPassword();


}
