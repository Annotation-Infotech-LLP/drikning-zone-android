package com.annotation.drinking_zone.ChangeCurrentPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.annotation.drinking_zone.HomeDashboard.HomeActivity;
import com.annotation.drinking_zone.HomeDashboard.ProfileFragment;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity implements AsyncTaskListener {

    TextInputLayout text_input_currentPassword, text_input_newpassword, text_input_newConfirmPassword;
    TextView change_pass_info;
    Button change_password_btn;
    String text_current_password, text_newPassword;
    int userId;
    UserSharedPrefDetails userSharedPrefDetails;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
//                    "(?=.*[0-9])" +         //at least 1 digit
//                    "(?=.*[a-z])" +         //at least 1 lower case letter
//                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
//                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        text_input_currentPassword = findViewById(R.id.text_input_current_password);
        text_input_newpassword = findViewById(R.id.text_input_newpassword);
        text_input_newConfirmPassword = findViewById(R.id.text_input_new_confirmpassword);
        change_pass_info = findViewById(R.id.change_pass_info);
        change_password_btn = findViewById(R.id.change_password_btn);

        change_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSharedPrefDetails = new UserSharedPrefDetails(ChangePassword.this);
                boolean validation_rtrnValue = confirmInput(v);
                String type = "change_password";
                text_current_password = text_input_currentPassword.getEditText().getText().toString();
                text_newPassword = text_input_newpassword.getEditText().getText().toString();
                userId = userSharedPrefDetails.getUser_Id();
                String user_id = Integer.toString(userId);
                if(validation_rtrnValue)
                {
                    BackgroundWorker backgroundWorker = new BackgroundWorker(ChangePassword.this);
                    backgroundWorker.setAsyncTaskListener(ChangePassword.this);
                    backgroundWorker.execute(type,user_id, text_current_password, text_newPassword);
                }
            }
        });
    }

    private  boolean validatePassword()
    {
        String passwordInput=text_input_newpassword.getEditText().getText().toString().trim();
        String confirmPasswordInput=text_input_newConfirmPassword.getEditText().getText().toString().trim();
        String currentPassword = text_input_currentPassword.getEditText().getText().toString().trim();

        if(passwordInput.isEmpty())
        {
            text_input_newpassword.setError("Field can't be empty");
            text_input_currentPassword.setError("Field can't be empty");

            return  false;
        }
        else if(confirmPasswordInput.isEmpty())
        {
            text_input_newConfirmPassword.setError("Field can't be empty");
            return  false;
        }
        else if(currentPassword.isEmpty())
        {
            text_input_currentPassword.setError("Field can't be empty");
            return  false;
        }
//        else if(!PASSWORD_PATTERN.matcher(passwordInput).matches())
//        {
////            text_input_newpassword.setError("Password Too Weak\n 1 digit,Lower & uppercase letters, Special character are necessary");
//            text_input_newpassword.setError("Password Too Weak\n atleast 4 characters are necessary");
//            return false;
//        }
//        else if(!PASSWORD_PATTERN.matcher(currentPassword).matches())
//        {
//            text_input_currentPassword.setError("Password Doesn't Match the pattern");
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
        else if(confirmPasswordInput.contains(" "))
        {
            text_input_newConfirmPassword.setError("There cant be space in password");
            return false;
        }
        else if(currentPassword.contains(" ") )
        {
            text_input_currentPassword.setError("There cant be space in password");
            return false;
        }
        else
        {
            text_input_newpassword.setError(null);
            text_input_newConfirmPassword.setError(null);
            text_input_currentPassword.setError(null);
            return true;
        }
    }

    public boolean confirmInput(View v)
    {
        boolean value = true;
        if(!validatePassword())
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

        Log.i("#Result reset password", "updateResult: " + result);
        if(result != null) {
            try {

                JSONObject jsonResult = new JSONObject(result);
//          int success = jsonResult.getInt("success");
                boolean success = jsonResult.getBoolean("status");
//                         status code positive = true, negative = false;
                if (!success) {
                    String message = jsonResult.getString("msg");
//                displaying the message from the server dynamically in alert box
                    change_pass_info.setText(message);
                    change_pass_info.setTextColor(getResources().getColor(R.color.colorRedDark));
                    text_input_currentPassword.setError("Please Enter Valid Password");
                    text_input_currentPassword.getEditText().setText("");
                    text_input_newpassword.getEditText().setText("");
                    text_input_newConfirmPassword.getEditText().setText("");

//                new CustomDialogs(OtpActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, message, "OK", "REGISTRATION INFORMATION SAYS:", SignUpActivity.class);
                } else if (success) {
                    String message = jsonResult.getString("msg");
                    Log.i("####Reset Password", "updateResult: " + message);
                    new CustomDialogs(ChangePassword.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message, "OK", "INFORMATION SAYS:", HomeActivity.class, "ChangePassword");
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

