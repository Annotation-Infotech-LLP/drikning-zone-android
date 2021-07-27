package com.annotation.drinking_zone.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.ContactSession;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class ResetPasswordActivity extends AppCompatActivity implements AsyncTaskListener {

    TextInputLayout text_input_otp, text_input_newpassword, text_input_newConfirmPassword;
    TextView otp_info;
    Button verify_otp_btn;
    String contact, text_otp, text_newPassword, text_newConfirmPassword;
    ContactSession contactSession;
//    UserSharedPrefDetails userSharedPrefDetails;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        text_input_otp = findViewById(R.id.text_input_otp);
        text_input_newpassword = findViewById(R.id.text_input_newpassword);
        text_input_newConfirmPassword = findViewById(R.id.text_input_new_confirmpassword);
        otp_info = findViewById(R.id.otp_info);
        verify_otp_btn = findViewById(R.id.verify_otp);
        contactSession = new ContactSession(ResetPasswordActivity.this);
        contact= contactSession.getUser_contact();

        verify_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validation_rtrnValue = confirmInput(v);
                String type = "reset_password";
                Log.i("contactSession", "onClick: "+contact);
                text_otp = text_input_otp.getEditText().getText().toString();
                text_newPassword = text_input_newpassword.getEditText().getText().toString();

                if(validation_rtrnValue)
                {
                    BackgroundWorker backgroundWorker = new BackgroundWorker(ResetPasswordActivity.this);
                    backgroundWorker.setAsyncTaskListener(ResetPasswordActivity.this);
                    backgroundWorker.execute(type, contact, text_otp, text_newPassword);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        new CustomDialogs(ResetPasswordActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Contact Verification Won't be Completed, \n Registration wont be Completed", "OK", "REGISTRATION INFORMATION SAYS:", OtpActivity.class);

    }

    private  boolean validateOTP()
    {
        String contact_number= text_input_otp.getEditText().getText().toString().trim();
        if(contact_number.isEmpty())
        {
            text_input_otp.setError("Field can't be empty");
            return  false;
        }
        else if(contact_number.contains(" "))
        {
            text_input_otp.setError("There cant be space in OTP");
            return false;
        }
        else
        {
            text_input_otp.setError(null);
            return true;
        }
    }

    private  boolean validatePassword()
    {
        String passwordInput=text_input_newpassword.getEditText().getText().toString().trim();
        String confirmPasswordInput=text_input_newConfirmPassword.getEditText().getText().toString().trim();

        if(passwordInput.isEmpty() || confirmPasswordInput.isEmpty())
        {
            text_input_newpassword.setError("Field can't be empty");
            return  false;
        }
//        else if(!PASSWORD_PATTERN.matcher(passwordInput).matches())
//        {
//            text_input_newpassword.setError("Password Too Weak\n 1 digit,Lower & uppercase letters, Special character are necessary");
//            return false;
//        }
        else if(!passwordInput.equals(confirmPasswordInput))
        {
            text_input_newConfirmPassword.setError("Both the passwords Doesn't match");
            return false;
        }
        else if(passwordInput.contains(" "))
        {
            text_input_newpassword.setError("There cant be space in password");
            return false;
        }
        else
        {
            text_input_newpassword.setError(null);
            text_input_newConfirmPassword.setError(null);
            return true;
        }
    }

    public boolean confirmInput(View v)
    {
        boolean value = true;
        if(!validateOTP()  | !validatePassword())
        {
            value = false;
        }else
        {
            value = true;
        }
        return value;
    }

    @Override
    public void updateResult(String result) {

        if(result != null) {
            Log.i("#Result reset password", "updateResult: " + result);
//                         Parsing JSON DATA
            try {

                JSONObject jsonResult = new JSONObject(result);
//          int success = jsonResult.getInt("success");
                boolean success = jsonResult.getBoolean("status");
//                         status code positive = true, negative = false;
                if (!success) {
                    String message = jsonResult.getString("msg");
//                displaying the message from the server dynamically in alert box
                    otp_info.setText(message);
                    otp_info.setTextColor(getResources().getColor(R.color.colorRedDark));
                    text_input_otp.setError("Please Enter Valid OTP");
                    text_input_otp.getEditText().setText("");
                    text_input_newpassword.getEditText().setText("");
                    text_input_newConfirmPassword.getEditText().setText("");

//                new CustomDialogs(OtpActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "OK", "REGISTRATION INFORMATION SAYS:", SignUpActivity.class);
                } else if (success) {
                    String message = jsonResult.getString("msg");
                    Log.i("####Reset Password", "updateResult: " + message);
                    new CustomDialogs(ResetPasswordActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "REGISTRATION INFORMATION SAYS:", SignInActivity.class);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "We didnt got any response...Ooopss Network Error!!", Toast.LENGTH_SHORT).show();
        }
    }
}

