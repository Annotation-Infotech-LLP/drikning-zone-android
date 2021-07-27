package com.annotation.drinking_zone.Utility;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.annotation.drinking_zone.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("Registered")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    NotificationCompat.Builder notificationBuilder;
    NotificationManager notificationManager;
    String id = "432806558667"; // this is the unique project id which we get when we create a project in firebase account, is id pe hi hume notifications ayenge
    String title = "DrinkingZone"; // this can be anything
    Fragment fragment;
    Context context;

    @Override
    public void onNewToken(final String token) {
        super.onNewToken(token);    // this class is basically a service which gets called while the user installs the app n ek unique token id create hota hai
        // , wo token id hume server me store krna hota hai n through server the msgs gets pushed
        FirebaseInstanceId.getInstance().getToken();
        Log.v("Token",token);
        context = getApplicationContext();
        Log.i("Token", "onNewToken: "+token);
//        sharedPreferenceManagerFCM = new SharedPreferenceManagerFCM(context);
        if(com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM.getInstance(getApplicationContext()).getToken().equals(""))
        {
            com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM.getInstance(getApplicationContext()).storeToken(token);
            Log.i("StoringTokenIf", "onNewToken: "+token);
        }
        else
        {

            com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM.getInstance(getApplicationContext()).storeToken(token);
            Log.i("StoringTokenElse", "onNewToken: "+token);
        }
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//    title
//            message
//    type_id
//            type

        Log.v("remote Message", String.valueOf(remoteMessage));
        //this is my notification response or messagebody  based on the response of get_n
        // {notification_id=57, type=Order, image=, title=Order Placed, message=Your order with order number ORDER_RM#57 has been received. Order is progressing}
        //{type=Offer, image=http://rmart.talab.biz/uploads/banners/31df099e76b690f358bb0635ef9a1acd.jpg}
        Map<String, String> messageBody =new HashMap<>();
        messageBody=remoteMessage.getData();
        Log.e("Data Payload: " ,""+ remoteMessage.getData().toString());
        Log.v("Data Payload: " ,""+ remoteMessage.getData().toString());

//        sendNotification(messageBody.get("title"),messageBody.get("message"),messageBody.get("type"),remoteMessage);//,imageUri

    }
//
//    private void sendImageNotification(String imageUri, String type, RemoteMessage remoteMessage) {
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String click_action=type;
//        String click_body=imageUri;
//
//        int requestID = (int) System.currentTimeMillis();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
//
//            if (mChannel == null) {
//                mChannel = new NotificationChannel(id, title, importance);
//                mChannel.enableVibration(true);
//                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                notificationManager.createNotificationChannel(mChannel);
//            }
//
//            Intent resultIntent=new Intent(getApplicationContext(), HomeActivity.class);
//            resultIntent.putExtra("body",click_body);
//            resultIntent.putExtra("type",click_action);
//            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),id)
//                    .setSmallIcon(R.drawable.app_icon_initials) // place logo here
//                    .setContentTitle("Visiting Card Pro")
//                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(click_body)))/*Notification with Image*/
//                    .setAutoCancel(true)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setColor(getResources().getColor(R.color.colorPrimaryDark))
//                    .setSound(defaultSoundUri)
//                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(0, notificationBuilder.build());
//
//        }else {
//
//            Intent resultIntent=new Intent(getApplicationContext(), HomeActivity.class);
//            resultIntent.putExtra("From",click_action);
//            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(
//                    getApplicationContext(), requestID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
//                    .setSmallIcon(R.drawable.app_icon_initials)
//                    .setContentTitle("Visiting Card Pro")
//                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(click_body)))
//                    .setAutoCancel(true)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setColor(getResources().getColor(R.color.colorPrimaryDark))
//                    .setSound(defaultSoundUri)
//                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(0, notificationBuilder.build());
//        }
//
//    }

//    private void sendNotification(String title, String body, String clickAction, RemoteMessage remoteMessage) {//, final String imageUri
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String click_action=clickAction;
//        String click_body=body;
//        String click_title=title;
//
//        int requestID = (int) System.currentTimeMillis();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
//
//            if (mChannel == null) {
//                mChannel = new NotificationChannel(id, title, importance);
//                mChannel.enableVibration(true);
//                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                notificationManager.createNotificationChannel(mChannel);
//            }
//            Intent resultIntent=new Intent(getApplicationContext(), HomeActivity.class);
//            resultIntent.putExtra("body",click_body);
//            resultIntent.putExtra("type",click_action);
//            resultIntent.putExtra("title",click_title);
//            resultIntent.putExtra("Notification", "Notification");
//            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),id)
//                    .setSmallIcon(R.drawable.app_icon_initials)
//                    .setContentTitle(title)
//                    .setContentText(body)
//                    .setAutoCancel(true)
//                    .setStyle(new NotificationCompat.BigTextStyle()
//                            .bigText(body))
//                    .setColor(getResources().getColor(R.color.colorPrimaryDark))
//                    .setSound(defaultSoundUri)
//                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
//                    .setContentIntent(pendingIntent);
//        }else {
//            Intent resultIntent=new Intent(getApplicationContext(),HomeActivity.class);
//            resultIntent.putExtra("type",click_action);
//            resultIntent.putExtra("Notification", "Notification");
//            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
//                    .setSmallIcon(R.drawable.app_icon_initials)
//                    .setContentTitle(title)
//                    .setContentText(body)
//                    .setAutoCancel(true)
//                    .setColor(getResources().getColor(R.color.colorPrimaryDark))
//                    .setSound(defaultSoundUri)
//                    .setStyle(new NotificationCompat.BigTextStyle()
//                            .bigText(body))
//                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
//                    .setContentIntent(pendingIntent);
//        }
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notificationBuilder.build());
//    }

//    public Bitmap getBitmapFromURL(String strURL) {
//        try {
//            URL url = new URL(strURL);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            Log.v("Bitmap", String.valueOf(myBitmap));
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
