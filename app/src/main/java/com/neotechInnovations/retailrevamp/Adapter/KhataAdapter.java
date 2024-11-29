package com.neotechInnovations.retailrevamp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.neotechInnovations.retailrevamp.Activity.HomepageActivity;
import com.neotechInnovations.retailrevamp.Model.KhataModel;
import com.neotechInnovations.retailrevamp.R;

import java.util.ArrayList;
import java.util.List;

public class KhataAdapter extends RecyclerView.Adapter<KhataAdapter.KhataViewHolder> {
    private static final String TAG = "KhataAdapter";
    public List<KhataModel> khataModelList = new ArrayList<>();
    Context mContext;
    OnItemClicked onItemClicked = new OnItemClicked() {
        @Override
        public void khataClicked(KhataModel khataModel) {
            ((HomepageActivity) mContext).manipulateKhataFragment(true,khataModel);
        }
    };


    public KhataAdapter(List<KhataModel> khataModelList, Context mContext) {
        this.khataModelList = khataModelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public KhataAdapter.KhataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khata_entry, parent, false);
        return new KhataAdapter.KhataViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull KhataAdapter.KhataViewHolder holder, int position) {
        KhataModel khataModel = khataModelList.get(position);
        holder.txtKhataUserName.setText(khataModel.getKhataUserName());
        holder.txtKhataPhoneNumber.setText(khataModel.getKhataUserPhone());
        holder.txtKhataSerialNumber.setText(khataModel.getKhataSerialNumber());
        holder.txtKhataBalance.setText(String.valueOf(khataModel.getKhataBalance()));

        holder.cvViewDetails.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    holder.cvViewDetails.setAlpha(0.5f);
                    holder.cvViewDetails.postDelayed(() -> {
                        holder.cvViewDetails.setAlpha(1f);
                    }, 200);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    holder.cvViewDetails.setAlpha(1f);
                    Log.d(TAG, "onTouch: manipulateAllTransactionFragment : to call KhataFragment: "+" : "+khataModel.getKhataUserName());
                    onItemClicked.khataClicked(khataModel);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return khataModelList.size();
    }

    public static class KhataViewHolder extends RecyclerView.ViewHolder {
        CardView cvViewDetails;
        TextView txtKhataUserName, txtKhataPhoneNumber, txtKhataBalance, txtKhataSerialNumber ;
        ImageView ivKhataUserImage;

        public KhataViewHolder(@NonNull View itemView) {
            super(itemView);
            cvViewDetails = itemView.findViewById(R.id.cv_view_details);
            ivKhataUserImage = itemView.findViewById(R.id.iv_khata_user_image);
            txtKhataUserName = itemView.findViewById(R.id.txt_khata_user_name);
            txtKhataPhoneNumber = itemView.findViewById(R.id.txt_khata_phone_number);
            txtKhataBalance = itemView.findViewById(R.id.txt_khata_balance);
            txtKhataSerialNumber = itemView.findViewById(R.id.txt_khata_serial_number);
        }
    }

    public interface OnItemClicked {
        void khataClicked(KhataModel khataModel);
    }
}
