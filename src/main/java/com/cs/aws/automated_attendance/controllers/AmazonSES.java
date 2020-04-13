package com.cs.aws.automated_attendance.controllers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.cs.aws.automated_attendance.dto.SESEmail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/email")
public class AmazonSES {

  // Replace sender@example.com with your "From" address.
  // This address must be verified with Amazon SES.
  static final String FROM = "sender@example.com";

  // Replace recipient@example.com with a "To" address. If your account
  // is still in the sandbox, this address must be verified.
  static final String TO = "recipient@example.com";

  // The configuration set to use for this email. If you do not want to use a
  // configuration set, comment the following variable and the 
  // .withConfigurationSetName(CONFIGSET); argument below.
  static final String CONFIGSET = "ConfigSet";

  // The subject line for the email.
  static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";
  
  // The HTML body for the email.
  static final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
      + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
      + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>" 
      + "AWS SDK for Java</a>";

  // The email body for recipients with non-HTML email clients.
  static final String TEXTBODY = "This email was sent through Amazon SES "
      + "using the AWS SDK for Java.";

  AWSCredentials credentials;

  @PostMapping("/send")
  public  Boolean sendEmail(@RequestBody SESEmail email) {

    // Validation
    if(email.getFrom()==null)
      email.setFrom("sknut95@gmail.com");

    try {
      // Verify Email First..
      if(!isEmailValid(email.getTo())) {
        Runnable emailVerifyThread = new Runnable() {
          @Override
          public void run() {

            System.out.println("Email needs to Verify..");
            System.out.println("Verification Send ..");
            verifyEmail(email.getTo());
          }
        };

        Thread thread = new Thread(emailVerifyThread, "EmailVerifyThread");
        thread.start();
        return false;
      }
      //Send Email Then...
      credentials = new ProfileCredentialsProvider("default").getCredentials();
      AmazonSimpleEmailService client =
          AmazonSimpleEmailServiceClientBuilder.standard()
          // Replace US_WEST_2 with the AWS Region you're using for
          // Amazon SES.
            .withRegion(Regions.US_EAST_1)
                  .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
      SendEmailRequest request = new SendEmailRequest()
          .withDestination(
              new Destination().withToAddresses(email.getTo()))
          .withMessage(new Message()
              .withBody(new Body()
                  .withHtml(new Content()
                      .withCharset("UTF-8").withData(email.getHtmlcontent()))
                  .withText(new Content()
                      .withCharset("UTF-8").withData(email.getText())))
              .withSubject(new Content()
                  .withCharset("UTF-8").withData(email.getSubject())))
          .withSource(email.getFrom());
          // Comment or remove the next line if you are not using a
          // configuration set
//          .withConfigurationSetName(CONFIGSET);
      client.sendEmail(request);
      System.out.println("Email sent!");
      return true;
    } catch (Exception ex) {
      System.out.println("The email was not sent. Error message: " 
          + ex.getMessage());
    }
    return false;
  }


  @PostMapping("/verify")
  public VerifyEmailIdentityResult verifyEmail(String to){
    credentials = new ProfileCredentialsProvider("default").getCredentials();
    AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    VerifyEmailIdentityRequest request = new VerifyEmailIdentityRequest().withEmailAddress(to);
    VerifyEmailIdentityResult response = client.verifyEmailIdentity(request);
    return response;
  }

  @PostMapping("/check")
  public boolean isEmailValid(@RequestBody String to){
    credentials = new ProfileCredentialsProvider("default").getCredentials();
    AmazonSimpleEmailServiceClient ses= new AmazonSimpleEmailServiceClient(credentials);

    List lids = ses.listIdentities().getIdentities();
    if (lids.contains(to)) {
      //the address is verified so
      return true;
    }
    return false;
  }
}