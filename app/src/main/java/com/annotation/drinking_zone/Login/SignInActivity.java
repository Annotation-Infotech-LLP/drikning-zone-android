package com.annotation.drinking_zone.Login;

import com.annotation.drinking_zone.HomeDashboard.HomeActivity;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.annotation.drinking_zone.R;

import org.json.JSONException;
import org.json.JSONObject;


public class SignInActivity extends AppCompatActivity implements AsyncTaskListener {
    private TextInputLayout textInputEmail, textInputPassword;
    TextView signUp, forgotPassword, help_line;
    Button signIn_btn;
    String text_email, text_password;
    String deviceToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signUp = findViewById(R.id.signup);
        textInputEmail = findViewById(R.id.text_input_email);
        textInputPassword = findViewById(R.id.text_input_password);
        forgotPassword = findViewById(R.id.forgot_password);
        signIn_btn = findViewById(R.id.signIn_btn);
        help_line = findViewById(R.id.help_line);

        String first = "Not an User? ";
        String next = "<font color='#EE0000'>Sign Up </font>";
        String last = " here.";
        signUp.setText(Html.fromHtml(first + next + last));

        if (!SharedPreferenceManagerFCM.getInstance(this).getToken().equals("")) {
            Log.i("tokenTestingIf", "onCreate: " + SharedPreferenceManagerFCM.getInstance(this).getToken());
            deviceToken = SharedPreferenceManagerFCM.getInstance(this).getToken(); // need to pass this token id while caling login api
            Log.i("DeviceTockenIf", "onCreate: " + deviceToken);
        } else {
            deviceToken = SharedPreferenceManagerFCM.getInstance(this).getToken();
            Log.i("tokenTestingIf", "onCreate: " + SharedPreferenceManagerFCM.getInstance(this).getToken());
            Log.i("DeviceokenElse", "onCreate: " + deviceToken);
        }


        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmInput(v)) {
                    String type = "user_login_check";
                    text_email = textInputEmail.getEditText().getText().toString();
                    text_password = textInputPassword.getEditText().getText().toString();
                    BackgroundWorker backgroundWorker = new BackgroundWorker(SignInActivity.this);
                    backgroundWorker.setAsyncTaskListener(SignInActivity.this);
                    backgroundWorker.execute(type, text_email, text_password, deviceToken);
                } else {
                    Toast.makeText(SignInActivity.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        help_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                intent.putExtra("Help", "Help");
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (emailInput.contains(" ")) {
            textInputEmail.setError("There cant be space in email or contact");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (passwordInput.contains(" ")) {
            textInputPassword.setError("There cant be space in password");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    public boolean confirmInput(View v) {
        boolean value = false;
        if (!validateEmail() | !validatePassword()) {
            value = false;
            return value;
        } else {
            value = true;
            return value;
        }
    }

    @Override
    public void updateResult(String result) {
        if(result != null) {
            try {

                JSONObject jsonResult = new JSONObject(result);
                boolean success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
                if (!success) {
//                displaying the message from the server dynamically in alert box
                    String message = jsonResult.getString("msg");
                    new CustomDialogs(SignInActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "RETRY AGAIN", "INFORMATION SAYS:", SignInActivity.class);
                } else if (success) {
                    String message = jsonResult.getString("msg");
                    Log.i("message login", "updateResult: " + message);

                    JSONObject user_details = jsonResult.getJSONObject("data");
                    int user_id = user_details.getInt("Id");
                    String user_firstname = user_details.getString("FirstName");
                    String user_lastname = user_details.getString("LastName");
                    String user_contact = user_details.getString("Mobile");
                    String user_email = user_details.getString("Email");
                    String user_referral_code = user_details.getString("ReferralCode");
                    String user_status = user_details.getString("Status");
//                creating a shared preference xml file so as to be maintained as a session
                    UserSharedPrefDetails user_sharedPref_details = new UserSharedPrefDetails(SignInActivity.this);
                    user_sharedPref_details.setUser_email(user_email);
                    user_sharedPref_details.setUser_firstname(user_firstname);
                    user_sharedPref_details.setUser_lastname(user_lastname);
                    user_sharedPref_details.setUser_name(user_firstname + " " + user_lastname);
                    user_sharedPref_details.setUser_contact(user_contact);
                    user_sharedPref_details.setUser_Id(user_id);
                    user_sharedPref_details.setUser_referral_code(user_referral_code);
                    Log.i("code", "updateResult: " + user_sharedPref_details.getUser_referral_code());
                    new CustomDialogs(SignInActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "LOGIN INFORMATION SAYS:", HomeActivity.class);
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