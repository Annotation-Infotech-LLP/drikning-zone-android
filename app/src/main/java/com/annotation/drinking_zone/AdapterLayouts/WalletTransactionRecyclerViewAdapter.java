package com.annotation.drinking_zone.AdapterLayouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.annotation.drinking_zone.ProductRelated.ProductsData;
import com.annotation.drinking_zone.R;

import java.util.List;

public class WalletTransactionRecyclerViewAdapter extends RecyclerView.Adapter<WalletTransactionRecyclerViewAdapter.PostHolder> {

    private Context ctx;
    private List<ProductsData> walletTransactionsHistory;
    View productItemView;
    int count;

    public WalletTransactionRecyclerViewAdapter(Context ctx, List<ProductsData> walletTransactionsHistory) {
        this.ctx = ctx;
        this.walletTransactionsHistory = walletTransactionsHistory;
    }

    @NonNull
    @Override
    public WalletTransactionRecyclerViewAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_transaction_single_layout, parent, false);
        return  new WalletTransactionRecyclerViewAdapter.PostHolder(productItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletTransactionRecyclerViewAdapter.PostHolder holder, int position) {
//                        {
//                "status": true,
//                    "balance": "20",
//                    "history": [
//                {
//                    "Title": "Refering Bonus of Rahul Basani",
//                        "Type": "Credit",
//                        "Amount": "20",
//                        "ClosingBalance": "20",
//                        "Time": "2019-10-16 17:41:55"
//                }
//              ]
//            }

        holder.transaction_title.setText(walletTransactionsHistory.get(position).getTransaction_title());
        holder.closing_balance.setText(walletTransactionsHistory.get(position).getTransaction_closing_balance());
        holder.transaction_date.setText(walletTransactionsHistory.get(position).getTransaction_time());

        if(walletTransactionsHistory.get(position).getTransaction_type().equals("Credit"))
        {
            holder.transaction_type.setImageResource(R.drawable.plus_sign);
            holder.transacted_amount.setText(walletTransactionsHistory.get(position).getTransaction_amount());
            holder.transacted_amount.setTextColor(ctx.getResources().getColor(R.color.green_plus));
            holder.only_sign.setTextColor(ctx.getResources().getColor(R.color.green_plus));
        }
        else
        {
            holder.transaction_type.setImageResource(R.drawable.minus_sign);
            holder.transacted_amount.setText(walletTransactionsHistory.get(position).getTransaction_amount());
            holder.transacted_amount.setTextColor(ctx.getResources().getColor(R.color.colorred));
            holder.only_sign.setTextColor(ctx.getResources().getColor(R.color.colorred));
        }
    }

    @Override
    public int getItemCount() {
        return walletTransactionsHistory.size();
    }


    public class PostHolder extends RecyclerView.ViewHolder {
        TextView transaction_title, transaction_date, transacted_amount, closing_balance, only_sign;
        ImageView transaction_type;

        PostHolder(@NonNull View itemView) {
            super(itemView);
            transaction_title = itemView.findViewById(R.id.transaction_title);
            transaction_date = itemView.findViewById(R.id.transaction_date);
            transacted_amount = itemView.findViewById(R.id.transacted_amount);
            closing_balance = itemView.findViewById(R.id.closing_balance);
            only_sign = itemView.findViewById(R.id.only_sign);
            transaction_type = itemView.findViewById(R.id.transaction_type);
        }
    }
}
