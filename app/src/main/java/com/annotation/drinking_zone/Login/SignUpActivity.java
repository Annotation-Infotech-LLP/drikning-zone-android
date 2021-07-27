package com.annotation.drinking_zone.Login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.annotation.drinking_zone.HomeDashboard.HomeActivity;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.ContactSession;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements AsyncTaskListener {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    private TextInputLayout firstname, lastname, email, password, confirm_password, contact, text_input_referal_code;
    Button signUp_btn;
    TextView signIn;
    String text_firstname, text_lastname, text_email, text_password, text_contact, type, text_referral_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstname = findViewById(R.id.text_input_firstname);
        lastname = findViewById(R.id.text_input_lastname);
        email = findViewById(R.id.text_input_email);
//        username = findViewById(R.id.text_input_username);
        password = findViewById(R.id.text_input_password);
        confirm_password = findViewById(R.id.text_input_confirm_password);
        contact = findViewById(R.id.text_input_contact_number);
        signUp_btn = findViewById(R.id.signUp_btn);
//        send_otp_btn = findViewById(R.id.send_otp_btn);
        signIn = findViewById(R.id.signin);
        text_input_referal_code = findViewById(R.id.text_input_referal_code);

        String first = "Already a registered User? ";
        String next = "<font color='#EE0000'>Sign In </font>";
        String last = " here.";
        signIn.setText(Html.fromHtml(first + next + last));


        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validation_rtrnValue = confirmInput(v);
                text_firstname = firstname.getEditText().getText().toString();
                text_lastname = lastname.getEditText().getText().toString();
//        text_username = username.getEditText().getText().toString();
                text_email = email.getEditText().getText().toString();
                text_password = password.getEditText().getText().toString();
                text_contact = contact.getEditText().getText().toString();
                text_referral_code = text_input_referal_code.getEditText().getText().toString();

                Log.i("Firstname", "Name: "+text_firstname);
                Log.i("Lastname", "last: "+text_lastname);
                Log.i("email", "email: "+text_email);
                Log.i("password", "password: "+text_password);
                Log.i("contact", "contact: "+text_contact);
                Log.i("referral code", "code: "+text_referral_code);

                type = "pre_register_user";
                if(validation_rtrnValue)
                {
                    BackgroundWorker backgroundWorker = new BackgroundWorker(SignUpActivity.this);
                    backgroundWorker.setAsyncTaskListener(SignUpActivity.this);
                    backgroundWorker.execute(type, text_firstname, text_lastname, text_email, text_password, text_contact, text_referral_code);
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateName()
    {
        String firstName=firstname.getEditText().getText().toString().trim();
        String lastName=firstname.getEditText().getText().toString().trim();
        if(firstName.isEmpty() || lastName.isEmpty())
        {
            firstname.setError("Field can't be empty");
            lastname.setError("Field can't be empty");
            return false;
        }
        else if(!firstName.matches("^[A-Za-z]+$"))
        {
//            ^           beginning of the string,
//          [A-Za-z]    search for alphabetical chars either they are CAPITALS or not
//            +           string contains at least one alphabetical char
//            $           end of the string

            firstname.setError("Name should be of only characters");
            return false;
        }
        else if(!lastName.matches("^[A-Za-z]+$"))
        {
            lastname.setError("Name should be of only characters");
            return false;
        }
        else
        {
            firstname.setError(null);
            lastname.setError(null);
            return  true;
        }
    }

    private boolean validateReferralCode() {
        String referralCode = text_input_referal_code.getEditText().getText().toString().trim();
        if (!referralCode.matches("^[A-Za-z0-9]+$")) {
//            ^           beginning of the string,
//          [A-Za-z]    search for alphabetical chars either they are CAPITALS or not
//            +           string contains at least one alphabetical char
//            $           end of the string

            text_input_referal_code.setError("Please Enter a Valide Referral Code");
            return false;
        } else if (referralCode.length() != 8) {
            text_input_referal_code.setError("Referral code should be of 8 Characters");
            return false;
        }
        else {
            text_input_referal_code.setError(null);
            return true;
        }
    }

    private boolean validateEmail()
    {
        String emailInput = email.getEditText().getText().toString().trim();
        if(emailInput.isEmpty())
        {
            email.setError(null);
            return true;
//            email.setError("Field can't be empty");
//            return  false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches())
        {
            email.setError("Please enter a valid Email Address");
            return  false;
        }
        else if(emailInput.contains(" "))
        {
            email.setError("There cant be space in email");
            return false;
        }
        else
        {
            email.setError(null);
            return true;
        }
    }

    private  boolean validatePassword()
    {
        String passwordInput=password.getEditText().getText().toString().trim();
        String confirmPasswordInput=confirm_password.getEditText().getText().toString().trim();

        if(passwordInput.isEmpty() || confirmPasswordInput.isEmpty())
        {
            password.setError("Field can't be empty");
            return  false;
        }
//        else if(!PASSWORD_PATTERN.matcher(passwordInput).matches())
//        {
//            password.setError("Password Too Weak\n 1 digit,Lower & uppercase letters, Special character are necessary");
//            return false;
//        }
        else if(!passwordInput.equals(confirmPasswordInput))
        {
            confirm_password.setError("Both the passwords Doesn't match");
            return false;
        }
        else if(passwordInput.contains(" "))
        {
            password.setError("There cant be space in password");
            return false;
        }
        else
        {
            password.setError(null);
            confirm_password.setError(null);
            return true;
        }
    }

    private  boolean validateContact()
    {
        String contact_number= contact.getEditText().getText().toString().trim();

        if(contact_number.isEmpty())
        {
            contact.setError("Field can't be empty");
            return  false;
        }
        else if(!Patterns.PHONE.matcher(contact_number).matches())
        {
            contact.setError("Please enter a valid contact number");
            return false;
        }
        else if(contact_number.contains(" "))
        {
            contact.setError("There cant be space in contact");
            return false;
        }
        else
        {
            contact.setError(null);
            return true;
        }
    }

    public boolean confirmInput(View v)
    {
        boolean value = true;
        String referral_code = text_input_referal_code.getEditText().getText().toString();

        if(referral_code.isEmpty())
        {
            if(!validateEmail()  | !validatePassword() | !validateContact() | !validateName())
            {
                value = false;
            }else
            {
                value = true;
            }
            return value;
        }
        else
        {
            if(!validateEmail()  | !validatePassword() | !validateContact() | !validateName() | !validateReferralCode())
            {
                value = false;
            }else
            {
                value = true;
            }
            return value;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void updateResult(String result) {
        Log.i("#Result SignUpActivity", "updateResult: " + result);
//                Parsing JSON DATA
        if (result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);
//            int success = jsonResult.getInt("success");
                boolean success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
                if (!success) {
                    String message = jsonResult.getString("msg");
//                displaying the message from the server dynamically in alert box
                    new CustomDialogs(SignUpActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "RETRY AGAIN", "INFORMATION SAYS:");
                } else {
                    String message = jsonResult.getString("msg");
                    Log.i("message OTP", "updateResult: " + message);
                    text_contact = contact.getEditText().getText().toString();
                    ContactSession contactSession = new ContactSession(SignUpActivity.this);
                    contactSession.setUser_contact(text_contact);
                    Log.i("##contactSession", text_contact);
                    new CustomDialogs(SignUpActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "REGISTRATION INFORMATION SAYS:", OtpActivity.class);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "We didnt got any response...Ooopss Network Error!!", Toast.LENGTH_SHORT).show();
        }
    }
}
