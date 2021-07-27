package com.annotation.drinking_zone.CCAvenueActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.annotation.drinking_zone.HomeDashboard.HomeActivity;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.AvenuesParams;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;

import org.json.JSONException;
import org.json.JSONObject;

public class StatusActivity extends AppCompatActivity implements AsyncTaskListener {

	Intent mainIntent;
	String booking_type, payment_status, status;
	String confirmBookingType, confirmOrderType, orderId;
	BackgroundWorker confirmBooking, confirmOrder;
	UserSharedPrefDetails userSharedPrefDetails;
	int userId;
	JSONObject jsonResult;
	boolean success;
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_status);
		mainIntent = getIntent();

		userSharedPrefDetails = new UserSharedPrefDetails(this);
		userId = userSharedPrefDetails.getUser_Id();

		if(mainIntent.hasExtra(AvenuesParams.BOOKING_TYPE) && mainIntent.hasExtra("transStatus"))
		{
			booking_type = mainIntent.getStringExtra(AvenuesParams.BOOKING_TYPE);
			status = mainIntent.getStringExtra("transStatus");
			orderId = mainIntent.getStringExtra(AvenuesParams.ORDER_ID);
			Log.i("bookingTypeInt", "onCreate: "+booking_type);
			Log.i("OrderId", "onCreate: "+orderId);
		}
		else
		{
			Log.i("intent", "onCreate: Failed to load intents");
		}


		if (status.contains("Failure")) {
			status = "Transaction Declined..! Please try again";
			Log.i("status", "processHTML: "+status);
			new CustomDialogs(StatusActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, status, "Go Back","Information says", HomeActivity.class);
		} else if (status.contains("Success")) {
			status = "Transaction Successful..!!";
			Log.i("status", "processHTML: "+status);
			payment_status = "Paid";
			if(booking_type.equals("Booking"))
			{
				Log.i("ApiCall", "processHTML: "+booking_type+" "+payment_status);
				confirmBookingType = "confirm_booking";
				confirmBooking = new BackgroundWorker(StatusActivity.this);
				confirmBooking.setAsyncTaskListener(StatusActivity.this);
				confirmBooking.execute(confirmBookingType, String.valueOf(userId), payment_status, orderId);
			}
			else if(booking_type.equals("Order"))
			{
				confirmOrderType = "confirm_order";
				confirmOrder = new BackgroundWorker(StatusActivity.this);
				confirmOrder.setAsyncTaskListener(StatusActivity.this);
				confirmOrder.execute(confirmOrderType, String.valueOf(userId), payment_status, orderId);
			}
//                        new CustomDialogs(WebViewActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, status, "Go Back","Information says", HomeActivity.class);
		} else if (status.contains("Aborted")) {
			status = "Transaction Cancelled! Please try again..!!";
			Log.i("status", "processHTML: "+status);
			new CustomDialogs(StatusActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, status, "Go Back","Information says", HomeActivity.class);
		} else {
			status = "Status Not Known, Please try again";
			Log.i("status", "processHTML: "+status);
			new CustomDialogs(StatusActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, status, "Go Back","Information says", HomeActivity.class);
		}

	}

	@Override
	public void updateResult(String result) {
		if(result != null) {
			try {
				jsonResult = new JSONObject(result);
//            int success = jsonResult.getInt("success");
				success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
				if (!success) {
					if (jsonResult.has("msg")) {
						String msg = jsonResult.getString("msg");
						new CustomDialogs(StatusActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Transaction Is successful but failed to place order, please contact the organisation", "RETRY AGAIN", "INFORMATION SAYS:", HomeActivity.class);
					}
				} else {
					if (jsonResult.has("msg")) {
						String message = jsonResult.getString("msg");
						new CustomDialogs(StatusActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message+"/n Transaction is Successfully made. Thank you, Have a Good day", "OK", "INFORMATION SAYS:", HomeActivity.class);
					}
					else
					{
						new CustomDialogs(StatusActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Something went Wrong", "OK", "INFORMATION SAYS:", HomeActivity.class);
					}
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