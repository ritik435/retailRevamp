package com.neotechInnovations.retailrevamp.Adapter;

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

import com.neotechInnovations.retailrevamp.Constant.Tags;
import com.neotechInnovations.retailrevamp.Model.TransactionModel;
import com.neotechInnovations.retailrevamp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{
    private static final String TAG = "TransactionAdapter";
    public List<TransactionModel> transactionModelList;
    Context mContext;
    String openedFrom;
    OnButtonClick onButtonClick;
    public TransactionAdapter(List<TransactionModel> transactionModelList, Context mContext,String openedFrom,OnButtonClick onButtonClick) {
        this.transactionModelList = transactionModelList;
        this.mContext = mContext;
        this.openedFrom=openedFrom;
        this.onButtonClick=onButtonClick;
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
            Log.d(TAG, "onBindViewHolder: isBackedUp: "+ transactionModel.isBackedUp());
        }else {
            if (transactionModel.isDeleted() && !openedFrom.equals(Tags.KEY_HISTORY)){
                holder.llMainTransaction.setVisibility(View.GONE);
                holder.llDateOfTransaction.setVisibility(View.GONE);
                return;
            }
            holder.llMainTransaction.setVisibility(View.VISIBLE);
            holder.llDateOfTransaction.setVisibility(View.GONE);
            Log.d(TAG, "onBindViewHolder: isBackedUp: "+ transactionModel.isBackedUp()+" getTransaction :: " +transactionModel.getTransaction()+" : "+transactionModel.getPaymentType() + " key: "+transactionModel.getKey() +" : userId : "+transactionModel.getUserId());
            if (transactionModel.isBackedUp() && openedFrom.equals(Tags.KEY_HOME)) {
                holder.ivIsBackedUp.setVisibility(View.VISIBLE);
            }
            else {
                holder.ivIsBackedUp.setVisibility(View.GONE);
            }
            if ( openedFrom.equals(Tags.KEY_HOME)) {
                Log.d(TAG, "onBindViewHolder: openedFrom : deleted is visible for position : "+position);
                holder.ivDeleteTransaction.setVisibility(View.VISIBLE);
                holder.ivDeleteTransaction.setOnClickListener(view -> {
                    //delete a transaction.
                    onButtonClick.onDeleteTransaction(transactionModel);
                });
            }
            else {
                Log.d(TAG, "onBindViewHolder: openedFrom : deleted is gone for position : "+position);
                holder.ivDeleteTransaction.setVisibility(View.GONE);
            }
            if ( openedFrom.equals(Tags.KEY_HISTORY)) {
                Log.d(TAG, "onBindViewHolder: openedFrom : deleted is visible for position : "+position);
                holder.ivRestoreTransaction.setVisibility(View.VISIBLE);
                holder.ivRestoreTransaction.setOnClickListener(view -> {
                    //delete a transaction.
                    onButtonClick.onRestoreTransaction(transactionModel);
                });
            }else{
                holder.ivRestoreTransaction.setVisibility(View.GONE);
            }

            if (transactionModel.getKey().equals(Tags.KEY_ADD_PAYMENTS)){
                holder.ivPaymentType.setImageResource(R.drawable.iv_red_up_arrow);
                holder.ivPaymentType2.setImageResource(R.drawable.iv_red_up_arrow);
            }else if (transactionModel.getKey().equals(Tags.KEY_ADD_COLLECTION)){
                holder.ivPaymentType.setImageResource(R.drawable.iv_green_down_arrow);
                holder.ivPaymentType2.setImageResource(R.drawable.iv_green_down_arrow);
            }else if (transactionModel.getKey().equals(Tags.KEY_ADD_ENTRY_IN_KHATA)){
                 if (transactionModel.getPaymentType().equals(Tags.KEY_DEBIT)){
                    holder.ivPaymentType.setImageResource(R.drawable.iv_red_up_arrow);
                    holder.ivPaymentType2.setImageResource(R.drawable.iv_red_up_arrow);
                }
                //if  item taken then and  paid then khata transaction green/red new icon
                else if (transactionModel.getPaymentType().equals(Tags.KEY_CREDIT)){
                    holder.ivPaymentType.setImageResource(R.drawable.iv_green_down_arrow);
                    holder.ivPaymentType2.setImageResource(R.drawable.iv_green_down_arrow);
                }
            }

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
        ImageView ivPaymentMode, ivPaymentType,ivPaymentType2,ivIsBackedUp,ivDeleteTransaction,ivRestoreTransaction;
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
            ivPaymentType2=itemView.findViewById(R.id.iv_payment_type_2);
            ivPaymentMode=itemView.findViewById(R.id.iv_payment_mode);
            txtHeaderDate=itemView.findViewById(R.id.txt_header_date);
            llBalance=itemView.findViewById(R.id.ll_balance);
            ivIsBackedUp=itemView.findViewById(R.id.iv_is_backed_up);
            ivDeleteTransaction=itemView.findViewById(R.id.iv_delete_transaction);
            ivRestoreTransaction=itemView.findViewById(R.id.iv_restore_transaction);
        }
    }
    public interface OnButtonClick{
        public void onDeleteTransaction(TransactionModel transactionModel);
        public void onRestoreTransaction(TransactionModel transactionModel);
    }
}
