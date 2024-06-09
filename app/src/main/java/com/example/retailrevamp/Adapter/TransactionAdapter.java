package com.example.retailrevamp.Adapter;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retailrevamp.Constant.Tags;
import com.example.retailrevamp.Model.TransactionModel;
import com.example.retailrevamp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{
    private static final String TAG = "TransactionAdapter";
    List<TransactionModel> transactionModelList;
    Context mContext;
    public TransactionAdapter(List<TransactionModel> transactionModelList, Context mContext) {
        this.transactionModelList = transactionModelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TransactionAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.TransactionViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+position);
        TransactionModel transactionModel=transactionModelList.get(position);
        if (transactionModel.getDate() != null) {
            // Create a SimpleDateFormat object with desired format
            SimpleDateFormat dateFormat = new SimpleDateFormat(Tags.DATE_FORMAT, Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat(Tags.TIME_FORMAT);
            // Convert Timestamp to Date object
            Date date = new Date(transactionModel.getDate().getTime());

            // Format the Date object into a string
            String dateString = dateFormat.format(date);
            String timeString = timeFormat.format(date);

            holder.txtDate.setText(dateString);
            holder.txtHeaderDate.setText(dateString);
            holder.txtTime.setText(timeString);
        }

        holder.llMainTransaction.setVisibility(View.GONE);
        holder.llDateOfTransaction.setVisibility(View.GONE);
        if (!transactionModel.getTransaction()){
            holder.llMainTransaction.setVisibility(View.GONE);
            holder.llDateOfTransaction.setVisibility(View.VISIBLE);

        }else {
            holder.llMainTransaction.setVisibility(View.VISIBLE);
            holder.llDateOfTransaction.setVisibility(View.GONE);

            holder.txtUserName.setText(transactionModel.getUserName());
            String paymentBalance = String.valueOf(Math.abs(transactionModel.getBalance()));
            String amountTransferred = String.valueOf(transactionModel.getAmountTransferred());
            if (transactionModel.getBalance()>0){
                //put everything red  & write left
                holder.txtPrefixBalance.setTextColor(resolveColorAttribute(mContext, com.google.android.material.R.attr.colorOnSurfaceInverse));
                holder.txtSufixBalance.setTextColor(resolveColorAttribute(mContext, com.google.android.material.R.attr.colorOnSurfaceInverse));
                holder.txtPaymentBalance.setTextColor(resolveColorAttribute(mContext, com.google.android.material.R.attr.colorOnSurfaceInverse));
                holder.txtSufixBalance.setText(" left )");
            }else if(transactionModel.getBalance()<0){
                //put everything green & write extra
                holder.txtPrefixBalance.setTextColor(resolveColorAttribute(mContext, com.google.android.material.R.attr.colorOnSurface));
                holder.txtSufixBalance.setTextColor(resolveColorAttribute(mContext, com.google.android.material.R.attr.colorOnSurface));
                holder.txtPaymentBalance.setTextColor(resolveColorAttribute(mContext, com.google.android.material.R.attr.colorOnSurface));
                holder.txtSufixBalance.setText(" extra )");
            }else{
                //put everything black
                holder.txtPrefixBalance.setTextColor(resolveColorAttribute(mContext, com.google.android.material.R.attr.colorOnPrimary));
                holder.txtSufixBalance.setTextColor(resolveColorAttribute(mContext, com.google.android.material.R.attr.colorOnPrimary));
                holder.txtPaymentBalance.setTextColor(resolveColorAttribute(mContext, com.google.android.material.R.attr.colorOnPrimary));
                holder.txtSufixBalance.setText(" left )");

            }
            holder.txtPaymentBalance.setText(paymentBalance);
            holder.txtAmountTransferred.setText(amountTransferred);

            holder.llBalance.setVisibility(View.GONE);
            if (transactionModel.getKey().equals(Tags.KEY_ADD_PAYMENTS)){
                holder.llBalance.setVisibility(View.VISIBLE);
            }
        }
    }
    @ColorInt
    private int resolveColorAttribute(Context mContext, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    @Override
    public int getItemCount() {
//        if (transactionModelList!=null){
//            return transactionModelList.size();
//        }
//        return 0;
        return transactionModelList != null ? transactionModelList.size() : 0;
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llDateOfTransaction,llMainTransaction,llBalance;
        TextView txtDate,txtTime,txtUserName,txtPaymentBalance,txtAmountTransferred,txtHeaderDate,txtPrefixBalance,txtSufixBalance;
        ImageView ivPaymentMode, ivPaymentType;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            llDateOfTransaction=itemView.findViewById(R.id.ll_date_of_transaction);
            llMainTransaction=itemView.findViewById(R.id.ll_main_transaction);
            txtDate=itemView.findViewById(R.id.txt_date);
            txtTime=itemView.findViewById(R.id.txt_time);
            txtUserName=itemView.findViewById(R.id.txt_user_name);
            txtPaymentBalance=itemView.findViewById(R.id.txt_payment_balance);
            txtPrefixBalance=itemView.findViewById(R.id.txt_prefix_balance);
            txtSufixBalance=itemView.findViewById(R.id.txt_sufix_balance);
            txtAmountTransferred=itemView.findViewById(R.id.txt_amount_transferred);
            ivPaymentType=itemView.findViewById(R.id.iv_payment_type);
            ivPaymentMode=itemView.findViewById(R.id.iv_payment_mode);
            txtHeaderDate=itemView.findViewById(R.id.txt_header_date);
            llBalance=itemView.findViewById(R.id.ll_balance);
        }
    }
}
