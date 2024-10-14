package com.neotechInnovations.retailrevamp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neotechInnovations.retailrevamp.R;

import java.util.ArrayList;
import java.util.List;

public class SuggestedAdapter extends RecyclerView.Adapter<SuggestedAdapter.SuggestedKhataViewHolder> {
    private static String TAG ="SuggestedAdapter";
    List<String> suggestedKhataList = new ArrayList<>();
    List<String> originalKhataList = new ArrayList<>();
    OnItemClicked onItemClicked;

    public SuggestedAdapter(List<String> suggestedKhataList, OnItemClicked onItemClicked) {
        this.suggestedKhataList = suggestedKhataList;
        this.onItemClicked = onItemClicked;
        this.originalKhataList=suggestedKhataList;
    }

    @NonNull
    @Override
    public SuggestedAdapter.SuggestedKhataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggested_khata, parent, false);
        return new SuggestedAdapter.SuggestedKhataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedAdapter.SuggestedKhataViewHolder holder, int position) {
        holder.llCreateKhata.setVisibility(View.GONE);
        holder.llShowKhata.setVisibility(View.GONE);
        if (position < suggestedKhataList.size()) {
            String SuggestedKhata = suggestedKhataList.get(position);
            holder.llShowKhata.setVisibility(View.VISIBLE);
            holder.txtSuggestedKhataId.setText(SuggestedKhata);
        } else {
            holder.llCreateKhata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return suggestedKhataList != null ? suggestedKhataList.size() + 1 : 1;
    }

    public void filter(String query) {
        List<String> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList.addAll(originalKhataList);
        } else {
            String filterPattern = query.toLowerCase().trim();
            for (String item : originalKhataList) {
                if (item.toLowerCase().contains(filterPattern)) {
                    filteredList.add(item);
                }
            }
        }
        suggestedKhataList = filteredList;
        notifyDataSetChanged();
    }
//    public void filter(String query, List<String> originalList) {
//        List<String> filteredList =originalList;
//        Log.d(TAG, "filter: REFINEKHATA: filter entered : query: "+query +" originalList : "+originalList.size() +" suggestedKhataList: "+suggestedKhataList.size());
////        suggestedKhataList.clear();
//        if (query.isEmpty()) {
//            suggestedKhataList=originalList;
//            Log.d(TAG, "filter: REFINEKHATA :query.isEmpty(): "+originalList.size());
//        } else {
////            String filterPattern = query.toLowerCase().trim();
////            Log.d(TAG, "filter: REFINEKHATA query.isEmpty() is not: "+originalList.size());
////            for (String item : filteredList) {
////                Log.d(TAG, "filter: REFINEKHATA : filterPattern: "+filterPattern+" :: "+item);
////                if (item.toLowerCase().contains(filterPattern)) {
////                    suggestedKhataList.add(item);
////                }
////            }
//            suggestedKhataList=originalList;
//            Log.d(TAG, "filter: REFINEKHATA : "+suggestedKhataList.size());
//        }
//        notifyDataSetChanged();
//    }

    public class SuggestedKhataViewHolder extends RecyclerView.ViewHolder {
        TextView txtSuggestedKhataId;
        LinearLayout llShowKhata, llCreateKhata;

        public SuggestedKhataViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSuggestedKhataId = itemView.findViewById(R.id.txt_suggested_khata_id);
            llCreateKhata = itemView.findViewById(R.id.ll_create_khata);
            llShowKhata = itemView.findViewById(R.id.ll_show_khata);
            llShowKhata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClicked.suggestedKhataClicked(suggestedKhataList.get(getAdapterPosition()), false);
                }
            });
            llCreateKhata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClicked.suggestedKhataClicked("", true);
                }
            });
        }
    }

    public interface OnItemClicked {
        void suggestedKhataClicked(String suggestedKhata, Boolean toCreate);
    }
}
