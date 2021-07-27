package com.annotation.drinking_zone.Utility;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.annotation.drinking_zone.R;


public class CustomDialogs {

    public CustomDialogs(final Context ctx, Integer color, Integer center_drawable, final String message, String btn_txt, String dialog_title, final Class intent_class)
    {
//        ImageButton closepositive;
        final Button alert_btn;
        TextView alert_title;
        TextView alert_message;
//        CardView alert_card;
        ImageView alert_image;
        final Dialog alertDialog = new Dialog(ctx);
        alertDialog.setContentView(R.layout.epic_popup_positive);
//        closepositive = alertDialog.findViewById(R.id.closepostive);
        alert_btn = alertDialog.findViewById(R.id.pos_btn_ok);
        alert_title = alertDialog.findViewById(R.id.pos_pop_title);
        alert_message = alertDialog.findViewById(R.id.pos_pop_msg);
//        alert_card = alertDialog.findViewById(R.id.alert_card);
        alert_image = alertDialog.findViewById(R.id.alert_img);
        alert_title.setText(dialog_title);
        alert_title.setTextColor(ctx.getResources().getColor(color));
        alert_image.setImageResource(center_drawable);
        alert_message.setText(message);
        alert_message.setTextColor(ctx.getResources().getColor(color));
        alert_btn.setText(btn_txt);
        alert_btn.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(color)));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(ctx.getApplicationContext(), intent_class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ctx.startActivity(intent);
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomDialogs(final Context ctx, Integer color, Integer center_drawable, final String message, String btn_txt, String dialog_title)
    {
//        ImageButton closepositive;
        final Button alert_btn;
        TextView alert_title;
        TextView alert_message;
//        CardView alert_card;
        ImageView alert_image;
        final Dialog alertDialog = new Dialog(ctx);
        alertDialog.setContentView(R.layout.epic_popup_positive);
//        closepositive = alertDialog.findViewById(R.id.closepostive);
        alert_btn = alertDialog.findViewById(R.id.pos_btn_ok);
        alert_title = alertDialog.findViewById(R.id.pos_pop_title);
        alert_message = alertDialog.findViewById(R.id.pos_pop_msg);
//        alert_card = alertDialog.findViewById(R.id.alert_card);
        alert_image = alertDialog.findViewById(R.id.alert_img);
        alert_title.setText(dialog_title);
        alert_title.setTextColor(ctx.getResources().getColor(color));
        alert_image.setImageResource(center_drawable);
        alert_message.setText(message);
        alert_message.setTextColor(ctx.getResources().getColor(color));
        alert_btn.setText(btn_txt);
        alert_btn.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(color)));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomDialogs(final Context ctx, Integer color, Integer center_drawable, final String message, String btn_txt, String dialog_title, final Class intent_class, final String putExtra)
    {
//        ImageButton closepositive;
        final Button alert_btn;
        TextView alert_title;
        TextView alert_message;
//        CardView alert_card;
        ImageView alert_image;
        final Dialog alertDialog = new Dialog(ctx);
        alertDialog.setContentView(R.layout.epic_popup_positive);
//        closepositive = alertDialog.findViewById(R.id.closepostive);
        alert_btn = alertDialog.findViewById(R.id.pos_btn_ok);
        alert_title = alertDialog.findViewById(R.id.pos_pop_title);
        alert_message = alertDialog.findViewById(R.id.pos_pop_msg);
//        alert_card = alertDialog.findViewById(R.id.alert_card);
        alert_image = alertDialog.findViewById(R.id.alert_img);
        alert_title.setText(dialog_title);
        alert_title.setTextColor(ctx.getResources().getColor(color));
        alert_image.setImageResource(center_drawable);
        alert_message.setText(message);
        alert_message.setTextColor(ctx.getResources().getColor(color));
        alert_btn.setText(btn_txt);
        alert_btn.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(color)));
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(ctx.getApplicationContext(), intent_class);
                intent.putExtra("Contact", putExtra);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ctx.startActivity(intent);

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomDialogs(final Context ctx, Integer color, Integer center_drawable, final String message, String btn_txt, String dialog_title, final Fragment intent_class)
    {
//        ImageButton closepositive;
        final Button alert_btn;
        TextView alert_title;
        TextView alert_message;
//        CardView alert_card;
        ImageView alert_image;
        final Dialog alertDialog = new Dialog(ctx);
        alertDialog.setContentView(R.layout.epic_popup_positive);
//        closepositive = alertDialog.findViewById(R.id.closepostive);
        alert_btn = alertDialog.findViewById(R.id.pos_btn_ok);
        alert_title = alertDialog.findViewById(R.id.pos_pop_title);
        alert_message = alertDialog.findViewById(R.id.pos_pop_msg);
//        alert_card = alertDialog.findViewById(R.id.alert_card);
        alert_image = alertDialog.findViewById(R.id.alert_img);
        alert_title.setText(dialog_title);
        alert_title.setTextColor(ctx.getResources().getColor(color));
        alert_image.setImageResource(center_drawable);
        alert_message.setText(message);
        alert_message.setTextColor(ctx.getResources().getColor(color));
        alert_btn.setText(btn_txt);
        alert_btn.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(color)));
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(ctx.getApplicationContext(), intent_class.getActivity().getClass());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ctx.startActivity(intent);

            }
        });
    }


}

