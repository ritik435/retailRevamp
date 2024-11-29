package com.neotechInnovations.retailrevamp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neotechInnovations.retailrevamp.Activity.HomepageActivity;
import com.neotechInnovations.retailrevamp.Adapter.TransactionAdapter;
import com.neotechInnovations.retailrevamp.Constant.Tags;
import com.neotechInnovations.retailrevamp.Model.TransactionModel;
import com.neotechInnovations.retailrevamp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG ="HistoryFragment" ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rvHistory;
    ImageView ivBackBtn;
    TransactionAdapter transactionAdapter;
    List<TransactionModel> historyTransactionModelList;
    HomepageActivity activity;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        activity=((HomepageActivity) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        initialiseViews(view);
        return view;
    }

    private void initialiseViews(View view) {
        rvHistory=view.findViewById(R.id.rv_history);
        ivBackBtn=view.findViewById(R.id.iv_back_btn);

        ivBackBtn.setOnClickListener(view1 -> {
            ((HomepageActivity)activity).onBackPressed();
        });
        historyTransactionModelList=new ArrayList<>();
        getHistory();
        intialliseHistoryRecyclerView();
    }
    void getHistory(){
        historyTransactionModelList=new ArrayList<>();
        for(int i=HomepageActivity.transactionModelList.size()-1;i>=0;i--){
            TransactionModel transactionModel=HomepageActivity.transactionModelList.get(i);
            if (transactionModel.isDeleted()){
                HomepageActivity.specificTransactionModelsList=historyTransactionModelList;
                boolean is0thElementAdded = ((HomepageActivity)activity).checkAnotherTransaction(transactionModel);
                historyTransactionModelList.add(1,transactionModel);
            }
        }
    }

    private void intialliseHistoryRecyclerView() {
        Log.d(TAG, "intialliseHistoryRecyclerView: historyTransactionModelList: "+historyTransactionModelList.size());
        transactionAdapter=new TransactionAdapter(historyTransactionModelList, getContext(), Tags.KEY_HISTORY, new TransactionAdapter.OnButtonClick() {
            @Override
            public void onDeleteTransaction(TransactionModel transactionModel) {

            }

            @Override
            public void onRestoreTransaction(TransactionModel transactionModel) {
                for(int i=0;i<HomepageActivity.transactionModelList.size();i++){
                    TransactionModel transactionModel1=HomepageActivity.transactionModelList.get(i);
                    if (transactionModel.getId()!=null && transactionModel1.getId()!=null && transactionModel.getId().equals(transactionModel1.getId())){
                        transactionModel1.setDeleted(false);
                        break;
                    }
                }
//                HomepageActivity.toRefreshListInHomepage=true;
                getHistory();
                Log.d(TAG, "onRestoreTransaction: historyTransactionModelList : "+historyTransactionModelList.size());
                transactionAdapter.transactionModelList=historyTransactionModelList;
                transactionAdapter.notifyDataSetChanged();
                ((HomepageActivity)activity).transactionChanged();
            }
        });
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvHistory.setAdapter(transactionAdapter);
    }

}