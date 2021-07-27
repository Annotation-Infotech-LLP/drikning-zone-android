package com.annotation.drinking_zone.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class OtpActivity extends AppCompatActivity implements AsyncTaskListener {

    TextInputLayout text_input_otp;
    TextView otp_info;
    Button verify_otp_btn;
    String contact;
    ContactSession contactSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        text_input_otp = findViewById(R.id.text_input_otp);
        otp_info = findViewById(R.id.otp_info);
        verify_otp_btn = findViewById(R.id.verify_otp);


        verify_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateOTP()) {
                    String otp = text_input_otp.getEditText().getText().toString();
                    contactSession = new ContactSession(OtpActivity.this);
                    contact = contactSession.getUser_contact();
                    Log.i("OTP_contact", "onClick: "+contact);
                    String type = "verify_otp";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(OtpActivity.this);
                    backgroundWorker.setAsyncTaskListener(OtpActivity.this);
                    backgroundWorker.execute(type, otp, contact);
                }
                else
                {
                    Toast.makeText(OtpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    @Override
    public void onBackPressed() {

        new CustomDialogs(OtpActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Contact Verification Won't be Completed, \n Registration wont be Completed", "OK", "REGISTRATION INFORMATION SAYS:", OtpActivity.class);

    }

    @Override
    public void updateResult(String result) {

        Log.i("#Result test OTP", "updateResult: " + result);
//                         Parsing JSON DATA
        if(result != null) {


            try {
                JSONObject jsonResult = new JSONObject(result);
//                         int success = jsonResult.getInt("success");
                boolean success = jsonResult.getBoolean("status");
//                         status code positive = true, negative = false;
                if (!success) {
                    String message = jsonResult.getString("msg");
                    Log.i("####test get OTP", "updateResult: " + message);
//                contactSession.removeUser();
//                displaying the message from the server dynamically in alert box
                    otp_info.setText(message);
                    otp_info.setTextColor(getResources().getColor(R.color.colorRedDark));
                    text_input_otp.setError("Please Enter Valid OTP");
                    text_input_otp.getEditText().setText("");
//                new CustomDialogs(OtpActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "OK", "REGISTRATION INFORMATION SAYS:", SignUpActivity.class);
                } else if (success) {
                    String message = jsonResult.getString("msg");
                    Log.i("####OTP", "updateResult: " + message);


                    JSONObject user_details = jsonResult.getJSONObject("data");
                    int user_id = user_details.getInt("Id");
                    String user_firstname = user_details.getString("FirstName");
                    String user_lastname = user_details.getString("LastName");
                    String user_contact = user_details.getString("Mobile");
                    String user_email = user_details.getString("Email");
                    String user_referral_code = user_details.getString("ReferralCode");
                    String user_status = user_details.getString("Status");
//                creating a shared preference xml file so as to be maintained as a session
                    UserSharedPrefDetails user_sharedPref_details = new UserSharedPrefDetails(OtpActivity.this);


                    if(user_sharedPref_details.getUser_Id() != -1)
                    {
                        new CustomDialogs(OtpActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "LOGIN INFORMATION SAYS:", HomeActivity.class);
                    }
                    else
                    {
                        user_sharedPref_details.setUser_email(user_email);
                        user_sharedPref_details.setUser_firstname(user_firstname);
                        user_sharedPref_details.setUser_lastname(user_lastname);
                        user_sharedPref_details.setUser_name(user_firstname + " " + user_lastname);
                        user_sharedPref_details.setUser_contact(user_contact);
                        user_sharedPref_details.setUser_Id(user_id);
                        user_sharedPref_details.setUser_referral_code(user_referral_code);
                        Log.i("code", "updateResult: " + user_sharedPref_details.getUser_referral_code());

                        new CustomDialogs(OtpActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "LOGIN INFORMATION SAYS:", HomeActivity.class);
                        Toast.makeText(this, "Data Didnt stored", Toast.LENGTH_SHORT).show();
                    }

//                    new CustomDialogs(OtpActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "REGISTRATION INFORMATION SAYS:", SignInActivity.class);
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
