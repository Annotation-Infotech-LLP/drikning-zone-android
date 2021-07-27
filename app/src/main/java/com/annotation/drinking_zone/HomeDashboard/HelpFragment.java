package com.annotation.drinking_zone.HomeDashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.annotation.drinking_zone.R;

public class HelpFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView mobile1, mobile2, mobile3, website_main, email1, email2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help, container, false);
        mobile1 = view.findViewById(R.id.mobile_number);
        mobile2 = view.findViewById(R.id.mobile_two);
        mobile3= view.findViewById(R.id.mobile_three);
//        website_main= view.findViewById(R.id.website_main);
        email1= view.findViewById(R.id.email_main);
        email2= view.findViewById(R.id.email2);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mobile1.setOnClickListener(this);
        mobile2.setOnClickListener(this);
        mobile3.setOnClickListener(this);
        email1.setOnClickListener(this);
        email2.setOnClickListener(this);
        website_main.setOnClickListener(this);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("##tag", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//                    Log.i("##confirmation", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Intent intent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                }
                return false;
            }

        });

    }

    @Override
    public void onClick(View v) {
        if(v.equals(mobile1))
        {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mobile1.getText()));
            startActivity(intent);
        }
        else if(v.equals(mobile2))
        {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mobile2.getText()));
            startActivity(intent);
        }
        if(v.equals(mobile3))
        {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mobile3.getText()));
            startActivity(intent);
        }
        else if (v.equals(website_main))
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+website_main.getText()));
            startActivity(browserIntent);
        }
        else if(v.equals(email1))
        {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",email1.getText().toString(), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }
        else if(v.equals(email2))
        {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",email2.getText().toString(), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }

    }
}
