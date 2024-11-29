package com.neotechInnovations.retailrevamp.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
 * Use the {@link AllTransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllTransactionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView ivBackBtn;
    RecyclerView rvRecentTransaction;
    private TransactionAdapter transactionAdapter;
    private List<TransactionModel> transactionModelList=new ArrayList<>();
    HomepageActivity activity;

    public AllTransactionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllTransactionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllTransactionsFragment newInstance(String param1, String param2) {
        AllTransactionsFragment fragment = new AllTransactionsFragment();
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
        activity= (HomepageActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_transactions, container, false);
        initialiseViews(view);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initialiseViews(View view) {
        ivBackBtn=view.findViewById(R.id.iv_back_btn);
        rvRecentTransaction=view.findViewById(R.id.rv_recent_transactions);

        transactionModelList = HomepageActivity.transactionModelList;
        ivBackBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ivBackBtn.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ivBackBtn.setAlpha(1f);
                    ((HomepageActivity)activity).onBackPressed();
                }
                return true;
            }
        });
//        addElementsInRecents();
        initialiseTransactionRecyclerView();
    }
//
//    private void addElementsInRecents() {
//        for (int i=0;i<60;i++){
//            TransactionModel transactionModel=new TransactionModel();
//            transactionModel.setMode("Online");
//            transactionModel.setTotalAmount(30000);
//
//            transactionModel.setPaymentType("Debit");
//            transactionModel.setAmountTransferred(29900+i);
//            String s= String.valueOf(Integer.valueOf(i));
//            transactionModel.setUserName(s);
//            transactionModel.setBalance(i+100);
//            transactionModelList.add(transactionModel);
//        }
//    }
    public void initialiseTransactionRecyclerView(){
        transactionAdapter= new TransactionAdapter(transactionModelList, activity, Tags.KEY_SPECIFIC, new TransactionAdapter.OnButtonClick() {
            @Override
            public void onDeleteTransaction(TransactionModel transactionModel) {

            }

            @Override
            public void onRestoreTransaction(TransactionModel transactionModel) {

            }
        });
        rvRecentTransaction.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        rvRecentTransaction.setAdapter(transactionAdapter);
    }
}