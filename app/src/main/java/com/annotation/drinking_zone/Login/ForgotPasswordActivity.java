package com.annotation.drinking_zone.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.ContactSession;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity implements AsyncTaskListener {

    TextInputLayout user_contact;
    String entered_contact;
    Button send_otp;
    ContactSession contactSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        user_contact = findViewById(R.id.text_input_contact);
        send_otp = findViewById(R.id.send_otp);

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                entered_contact = user_contact.getEditText().getText().toString();
                Log.i("#contact", "onCreate: "+entered_contact);

                String type = "forgot_password";
                if(validateContact())
                {
                    contactSession = new ContactSession(ForgotPasswordActivity.this);
                    contactSession.setUser_contact(entered_contact);
                    BackgroundWorker backgroundWorker = new BackgroundWorker(ForgotPasswordActivity.this);
                    backgroundWorker.setAsyncTaskListener(ForgotPasswordActivity.this);
                    backgroundWorker.execute(type, entered_contact);
                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Something Went Wrong, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private  boolean validateContact()
    {
        String contact_number= user_contact.getEditText().getText().toString().trim();

        if(contact_number.isEmpty())
        {
            user_contact.setError("Field can't be empty");
            return  false;
        }
        else if(!Patterns.PHONE.matcher(contact_number).matches())
        {
            user_contact.setError("Please enter a valid Contact");
            return false;
        }
        else if(contact_number.contains(" "))
        {
            user_contact.setError("There cant be space in contact or email");
            return false;
        }
        else
        {
            user_contact.setError(null);
            return true;
        }
    }

    @Override
    public void updateResult(String result) {

        if(result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);
//            int success = jsonResult.getInt("success");
                boolean success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
                if (!success) {
                    String message = jsonResult.getString("msg");
                    Log.i("OTP msg", "updateResult: " + message);
//                displaying the message from the server dynamically in alert box
                    new CustomDialogs(ForgotPasswordActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "RETRY AGAIN", "INFORMATION SAYS:", ForgotPasswordActivity.class);
                } else {
                    String message = jsonResult.getString("msg");
                    Log.i("message OTP", "updateResult: " + message);
                    new CustomDialogs(ForgotPasswordActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "INFORMATION SAYS:", ResetPasswordActivity.class);
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
