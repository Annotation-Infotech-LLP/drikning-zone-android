package com.annotation.drinking_zone.HomeDashboard;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.annotation.drinking_zone.ProductRelated.AllProductsActivity;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.SharedPreferenceManagerFCM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

public class MenuFragment extends Fragment implements View.OnClickListener {

    FlipperLayout flipperLayout;
    CardView all_products, jars, bottles, soft_drinks, monthly_package;
    View view;
    int product_Category;
//    TextView slider_description;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu, container, false);
//        to set the slider here
        flipperLayout = view.findViewById(R.id.slider);
        all_products = view.findViewById(R.id.all_products);
        jars = view.findViewById(R.id.jars);
        bottles = view.findViewById(R.id.bottles);
        soft_drinks = view.findViewById(R.id.soft_drinks);
        monthly_package = view.findViewById(R.id.monthly_package);

//        slider_description = view.findViewById(R.id.slide_description);

//        jars.setText("20L Big Jars");

//        using flipper library
        int[] image_resources ={R.drawable.start_img1, R.drawable.start_img2, R.drawable.start_img3};
//        String[] slide_descriptions ={"SLIDER 1", "SLIDER 2", "SLIDER 3"};
        for (int i = 0; i<image_resources.length; i++)
        {
            FlipperView flipperView = new FlipperView(view.getContext());
            flipperView.setImageDrawable(image_resources[i]);
            flipperLayout.addFlipperView(flipperView);
//            slider_description.setText(slide_descriptions[i]);
        }

//        to display the products categories

        all_products.setOnClickListener(this);
        bottles.setOnClickListener(this);
        jars.setOnClickListener(this);
        soft_drinks.setOnClickListener(this);
        monthly_package.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        CardView cardView = (CardView) v;
        String category_name;
        Intent intent = new Intent(view.getContext(), AllProductsActivity.class);
        switch(cardView.getId())
        {
            case R.id.all_products:
                product_Category = 0;
                category_name = "All Products";
                intent.putExtra("product_category",product_Category);
                intent.putExtra("product_category_name",category_name);
                break;
            case R.id.jars:
                product_Category = 1;
                category_name = "Refillable Big Jars";
                intent.putExtra("product_category",product_Category);
                intent.putExtra("product_category_name", category_name);
                break;
            case R.id.bottles:
                product_Category = 2;
                category_name = "Single Use Water Bottles";
                intent.putExtra("product_category",product_Category);
                intent.putExtra("product_category_name", category_name);
                break;
            case R.id.soft_drinks:
                product_Category = 3;
                category_name = "Soft Drinks";
                intent.putExtra("product_category",product_Category);
                intent.putExtra("product_category_name", category_name);
                break;
            case R.id.monthly_package:
                product_Category = 4;
                category_name = "Monthly Package";
                intent.putExtra("product_category", product_Category);
                intent.putExtra("product_category_name", category_name);
                break;
        }
        view.getContext().startActivity(intent);
    }

}
