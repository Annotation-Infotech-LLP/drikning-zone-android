package com.annotation.drinking_zone.CCAvenueActivities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.annotation.drinking_zone.CheckOutBillingStage.PayNowActivity;
import com.annotation.drinking_zone.CheckOutBillingStage.PlaceOrderActivity;
import com.annotation.drinking_zone.HomeDashboard.HomeActivity;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.AvenuesParams;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.Constants;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.LoadingDialog;
import com.annotation.drinking_zone.Utility.RSAUtility;
import com.annotation.drinking_zone.Utility.ServiceUtility;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class WebViewActivity extends AppCompatActivity{
    Intent mainIntent;
    String encVal;
    String vResponse;
    WebView webview;
    String booking_type, payment_status;
    String bankUrl = "";
    String confirmBookingType, confirmOrderType, orderId;
    BackgroundWorker confirmBooking, confirmOrder;
    UserSharedPrefDetails userSharedPrefDetails;
    int userId;
    JSONObject jsonResult;
    boolean success;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        mainIntent = getIntent();

        userSharedPrefDetails = new UserSharedPrefDetails(this);
        userId = userSharedPrefDetails.getUser_Id();

        if(mainIntent.hasExtra(AvenuesParams.BOOKING_TYPE))
        {
            booking_type = mainIntent.getStringExtra(AvenuesParams.BOOKING_TYPE);
            orderId = mainIntent.getStringExtra(AvenuesParams.ORDER_ID);
            Log.i("bookingTypeInt", "onCreate: "+booking_type);
        }
        //get rsa key method
        get_RSA_key(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), mainIntent.getStringExtra(AvenuesParams.ORDER_ID));
    }

//    @Override
//    public void updateResult(String result) {
//        if(result != null) {
//            try {
//                jsonResult = new JSONObject(result);
////            int success = jsonResult.getInt("success");
//                success = jsonResult.getBoolean("status");
////            status code positive = true, negative = false;
//                if (!success) {
//                    if (jsonResult.has("msg")) {
//                        String msg = jsonResult.getString("msg");
//                        new CustomDialogs(WebViewActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Transaction Is successful but failed to place order, please contact the organisation", "RETRY AGAIN", "INFORMATION SAYS:", HomeActivity.class);
//                    }
//                } else {
//                    if (jsonResult.has("msg")) {
//                        String message = jsonResult.getString("msg");
//                        new CustomDialogs(WebViewActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, message+"/n Transaction is Successfully made. Thank you, Have a Good day", "OK", "INFORMATION SAYS:", HomeActivity.class);
//                    }
//                    else
//                    {
//                        new CustomDialogs(WebViewActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, "Something went Wrong", "OK", "INFORMATION SAYS:", HomeActivity.class);
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        else
//        {
//            Toast.makeText(this, "We didnt got any response...Ooopss Network Error!!", Toast.LENGTH_SHORT).show();
//        }
//    }

    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");

        }
//
        @Override
        protected Void doInBackground(Void... arg0) {
            if (!ServiceUtility.chkNull(vResponse).equals("")
                    && !ServiceUtility.chkNull(vResponse).toString().contains("ERROR")) {
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);  //encrypt amount and currency
//                Log.i("encryptedValues", "doInBackground: "+encVal);
            }

            return null;
        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.i("Result", "onPostExecute: "+result);
//             Dismiss the progress dialog
            LoadingDialog.cancelLoading();

            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    // process the html source code to get final status of transaction
//                    webview.setVisibility(View.GONE);
                    Log.i("responseHTML", "processHTML: "+html);
                    String status = null;
                    if (html.contains("Failure")) {
                        status = "Failure";
//                        Log.i("status", "processHTML: "+status);
//                        new CustomDialogs(WebViewActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, status, "Go Back","Information says", HomeActivity.class);
                    } else if (html.contains("Success")) {
                        status = "Success";
//                        Log.i("status", "processHTML: "+status);
//                        payment_status = "Paid";
//                        if(booking_type.equals("Booking"))
//                        {
//                            confirmBookingType = "confirm_booking";
//                            confirmBooking = new BackgroundWorker(WebViewActivity.this);
//                            confirmBooking.setAsyncTaskListener(WebViewActivity.this);
//                            confirmBooking.execute(confirmBookingType, String.valueOf(userId), payment_status);
//                        }
//                        else if(booking_type.equals("Order"))
//                        {
//                            confirmOrderType = "confirm_order";
//                            confirmOrder = new BackgroundWorker(WebViewActivity.this);
//                            confirmOrder.setAsyncTaskListener(WebViewActivity.this);
//                            confirmOrder.execute(confirmOrderType, String.valueOf(userId), payment_status);
//                        }
//                        new CustomDialogs(WebViewActivity.this, R.color.colorgreen, R.drawable.ic_mood_black_24dp, status, "Go Back","Information says", HomeActivity.class);

                    } else if (html.contains("Aborted")) {
                        status = "Aborted";
                        Log.i("status", "processHTML: "+status);
//                        new CustomDialogs(WebViewActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, status, "Go Back","Information says", HomeActivity.class);
                    } else {
                        status = "Illegal";
                        Log.i("status", "processHTML: "+status);
//                        new CustomDialogs(WebViewActivity.this, R.color.colorRedDark, R.drawable.ic_mood_bad_black_24dp, status, "Go Back","Information says", HomeActivity.class);
                    }
                    Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                    intent.putExtra("transStatus", status);
                    intent.putExtra(AvenuesParams.BOOKING_TYPE, booking_type);
                    intent.putExtra(AvenuesParams.ORDER_ID, orderId);
                    Log.i("orderId", "processHTML: "+orderId);
                    startActivity(intent);
                }
            }

            webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setDomStorageEnabled(true);
            webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                    bankUrl = url;
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    LoadingDialog.cancelLoading();
                    if (url.contains("/ccavResponseHandler.php")) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });

            try {
                String postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), "UTF-8") + "&" +
                        AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID), "UTF-8") + "&" +
                        AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ORDER_ID), "UTF-8") + "&" +
                        AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL), "UTF-8") + "&" +
                        AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.CANCEL_URL), "UTF-8") + "&" +
                        AvenuesParams.BILLING_NAME + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_NAME), "UTF-8") + "&" +
                        AvenuesParams.BILLING_ADDRESS + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_ADDRESS), "UTF-8") + "&" +
                        AvenuesParams.BILLING_CITY + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_CITY), "UTF-8") + "&" +
                        AvenuesParams.BILLING_STATE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_STATE), "UTF-8") + "&" +
                        AvenuesParams.BILLING_COUNTRY + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_COUNTRY), "UTF-8") + "&" +
                        AvenuesParams.BILLING_ZIP + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_ZIP), "UTF-8") + "&" +
                        AvenuesParams.BILLING_TEL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_TEL), "UTF-8") + "&" +
                        AvenuesParams.BILLING_EMAIL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_EMAIL), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_NAME + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_NAME), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_ADDRESS + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_ADDRESS), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_CITY + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_CITY), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_STATE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_STATE), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_COUNTRY + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_COUNTRY), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_ZIP + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_ZIP), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_TEL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_TEL), "UTF-8") + "&" +
                        AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");

                webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
                webview.postUrl(Constants.TRANS_URL, postData.getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    public void get_RSA_key(final String ac, final String od) {
        LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LoadingDialog.cancelLoading();

                        if (response != null && !response.equals("")) {
                            vResponse = response;     ///save retrived rsa key
                            if (vResponse.contains("!ERROR!")) {
                                show_alert(vResponse);
                            } else {
                                new RenderView().execute();   // Calling async task to get display content
                            }
                        }
                        else
                        {
                            show_alert("No response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LoadingDialog.cancelLoading();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AvenuesParams.ACCESS_CODE, ac);
                params.put(AvenuesParams.ORDER_ID, od);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void show_alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                WebViewActivity.this).create();

        alertDialog.setTitle("Error!!!");
        if (msg.contains("\n"))
            msg = msg.replaceAll("\\\n", "");

        alertDialog.setMessage(msg);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        alertDialog.show();
    }


}