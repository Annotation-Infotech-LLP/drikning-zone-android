package com.annotation.drinking_zone.HomeDashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annotation.drinking_zone.AdapterLayouts.WalletTransactionRecyclerViewAdapter;
import com.annotation.drinking_zone.ProductRelated.ProductsData;
import com.annotation.drinking_zone.R;
import com.annotation.drinking_zone.Utility.AsyncTaskListener;
import com.annotation.drinking_zone.Utility.BackgroundWorker;
import com.annotation.drinking_zone.Utility.CustomDialogs;
import com.annotation.drinking_zone.Utility.UserSharedPrefDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyWalletFragment extends Fragment implements AsyncTaskListener{
    View view;
    RecyclerView transaction_history_recycler;
    private GridLayoutManager gridLayoutManager;
    private WalletTransactionRecyclerViewAdapter walletTransactionRecyclerViewAdapter;
    private List<ProductsData> walletTransactionsData;
    UserSharedPrefDetails userSharedPrefDetails;
    int userId;
    TextView wallet_balance, history_datails;
    BackgroundWorker walletCash;
    String type;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_wallet, container, false);
        transaction_history_recycler = view.findViewById(R.id.transaction_history_recycler);
        walletTransactionsData = new ArrayList<>();
        wallet_balance= view.findViewById(R.id.wallet_balance);
        history_datails = view.findViewById(R.id.history_datails);
        gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        transaction_history_recycler.setLayoutManager(gridLayoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userSharedPrefDetails = new UserSharedPrefDetails(view.getContext());
        userId = userSharedPrefDetails.getUser_Id();

        history_datails.setVisibility(View.GONE);
        String type = "get_my_wallet";
        walletCash = new BackgroundWorker(getActivity());
        walletCash.setAsyncTaskListener(this);
        walletCash.execute(type, Integer.toString(userId));


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("##tag", "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
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
    public void updateResult(String result) {
        Log.i("#Result All Cart", "updateResult: " + result);
//                Parsing JSON DATA
        if(result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);
//            int success = jsonResult.getInt("success");
                boolean success = jsonResult.getBoolean("status");
//            status code positive = true, negative = false;
                if (!success) {
                    if (jsonResult.has("balance") && jsonResult.has("history")) {
                        wallet_balance.setText(jsonResult.getString("balance"));
                        JSONArray history = jsonResult.getJSONArray("history");
                        if (history.length() == 0) {
                            history_datails.setVisibility(View.VISIBLE);
                            transaction_history_recycler.setVisibility(View.GONE);
                        }
                    } else {
                        new CustomDialogs(view.getContext(), R.color.colorgreen, R.drawable.ic_mood_black_24dp, "Your wallet Cash is 0, Refer your friend and Earn", "Lets Refer a Friend", "INFORMATION SAYS:", HomeActivity.class);
                    }

                } else if (success) {
                    if (jsonResult.has("balance") && jsonResult.has("history")) {
                        String balance = jsonResult.getString("balance");
                        JSONArray transaction_history = jsonResult.getJSONArray("history");
                        if (transaction_history.length() != 0) {
                            history_datails.setVisibility(View.GONE);
                            transaction_history_recycler.setVisibility(View.VISIBLE);
                            walletTransactionsData.clear();

                            for (int i = 0; i < transaction_history.length(); i++) {
                                JSONObject single_product = transaction_history.getJSONObject(i);
                                String transaction_title = single_product.getString("Title");
                                String transaction_type = single_product.getString("Type");
                                String transaction_amount = single_product.getString("Amount");
                                String transaction_closingBalance = single_product.getString("ClosingBalance");
                                String transaction_time = single_product.getString("Time");

                                ProductsData oneWalletTransaction = new ProductsData(transaction_title, transaction_type, transaction_amount, transaction_closingBalance, transaction_time);
                                walletTransactionsData.add(oneWalletTransaction);
                            }

                            wallet_balance.setText(balance);

                            walletTransactionRecyclerViewAdapter = new WalletTransactionRecyclerViewAdapter(view.getContext(), walletTransactionsData);
                            transaction_history_recycler.setAdapter(walletTransactionRecyclerViewAdapter);
                            walletTransactionRecyclerViewAdapter.notifyDataSetChanged();
                        } else {
                            transaction_history_recycler.setVisibility(View.GONE);
                            history_datails.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getActivity(), "We didnt got any response...Ooopss Network Error!!", Toast.LENGTH_SHORT).show();
        }
    }
}
