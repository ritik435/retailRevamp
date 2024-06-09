package com.example.retailrevamp.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retailrevamp.Activity.HomepageActivity;
import com.example.retailrevamp.Adapter.TransactionAdapter;
import com.example.retailrevamp.Constant.Tags;
import com.example.retailrevamp.Model.TransactionModel;
import com.example.retailrevamp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KhataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KhataFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<TransactionModel> transactionModelList=new ArrayList<>();
    TextView txtKhataUserName,txtKhataUserPhone,txtKhataUserNumber;
    RecyclerView rvKhataTransactions;
    ImageView ivKhataUserImage,ivBackBtn;
    TransactionAdapter khataTransactions;
    CardView cvAddEntryInKhata;
    HomepageActivity activity;

    public KhataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KhataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KhataFragment newInstance(String param1, String param2) {
        KhataFragment fragment = new KhataFragment();
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
        View view= inflater.inflate(R.layout.fragment_khata, container, false);
        initialiseViews(view);
        manipulateViews();
        return view;
    }

    private void initialiseViews(View view) {
        txtKhataUserName=view.findViewById(R.id.txt_khata_user_name);
        txtKhataUserNumber=view.findViewById(R.id.txt_khata_user_number);
        txtKhataUserPhone=view.findViewById(R.id.txt_khata_user_phone);
        rvKhataTransactions=view.findViewById(R.id.rv_khata_transactions);
        ivKhataUserImage=view.findViewById(R.id.iv_khata_user_image);
        ivBackBtn=view.findViewById(R.id.iv_back_btn);
        cvAddEntryInKhata=view.findViewById(R.id.cv_add_entry_khata);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void manipulateViews() {
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
        cvAddEntryInKhata.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    cvAddEntryInKhata.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    cvAddEntryInKhata.setAlpha(1f);
                    ((HomepageActivity)activity).manipulateAddTransactionFragment(Tags.KEY_ADD_ENTRY_IN_KHATA,true);
                }
                return true;
            }
        });
        addElementsInRecents();
        initialiseTransactionRecyclerView();
    }
    private void addElementsInRecents() {
        for (int i=0;i<60;i++){
            TransactionModel transactionModel=new TransactionModel();
            transactionModel.setMode("Online");
            transactionModel.setTotalAmount(30000);
            transactionModel.setTransaction(true);
            transactionModel.setKey(Tags.KEY_ADD_ENTRY_IN_KHATA);
            transactionModel.setPaymentType("Debit");
            transactionModel.setAmountTransferred(29900+i);
            String s= String.valueOf(Integer.valueOf(i));
            transactionModel.setUserName(s);
            transactionModel.setBalance(i+100);
            transactionModelList.add(transactionModel);
        }
    }
    public void initialiseTransactionRecyclerView(){
        khataTransactions= new TransactionAdapter(transactionModelList,activity);
        rvKhataTransactions.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        rvKhataTransactions.setAdapter(khataTransactions);
    }
}