package com.neotechInnovations.retailrevamp.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
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

import com.neotechInnovations.retailrevamp.Activity.HomepageActivity;
import com.neotechInnovations.retailrevamp.Adapter.TransactionAdapter;
import com.neotechInnovations.retailrevamp.Constant.Tags;
import com.neotechInnovations.retailrevamp.Model.KhataModel;
import com.neotechInnovations.retailrevamp.Model.TransactionModel;
import com.neotechInnovations.retailrevamp.R;

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
    private static final String TAG ="KhataFragment" ;

    // TODO: Rename and change types of parameters
    private KhataModel khataModel;
    private String mParam2;
    private List<TransactionModel> transactionModelList=new ArrayList<>();
    TextView txtKhataUserName,txtKhataUserPhone,txtKhataUserSerialNumber,txtKhataBalance;
    RecyclerView rvKhataTransactions;
    ImageView ivKhataUserImage,ivBackBtn;
    TransactionAdapter khataTransactions;
    CardView cvAddEntryInKhata;
    HomepageActivity activity;
    static int balance;

    public KhataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param khataModel Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KhataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KhataFragment newInstance(KhataModel khataModel, String param2) {
        KhataFragment fragment = new KhataFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, khataModel);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            khataModel = (KhataModel) getArguments().getSerializable(ARG_PARAM1);
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
        txtKhataUserSerialNumber=view.findViewById(R.id.txt_khata_user_serial_number);
        txtKhataUserPhone=view.findViewById(R.id.txt_khata_user_phone);
        rvKhataTransactions=view.findViewById(R.id.rv_khata_transactions);
        ivKhataUserImage=view.findViewById(R.id.iv_khata_user_image);
        ivBackBtn=view.findViewById(R.id.iv_back_btn);
        cvAddEntryInKhata=view.findViewById(R.id.cv_add_entry_khata);
        txtKhataBalance=view.findViewById(R.id.txt_khata_balance);

        balance=0;
    }
    @SuppressLint("ClickableViewAccessibility")
    private void manipulateViews() {
        txtKhataUserName.setText(khataModel.getKhataUserName());
        SpannableStringBuilder sbSerialNumber = new SpannableStringBuilder();
        sbSerialNumber.append("( #",new StyleSpan(Typeface.NORMAL), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbSerialNumber.append(khataModel.getKhataSerialNumber(),new StyleSpan(Typeface.BOLD), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbSerialNumber.append(" ) ",new StyleSpan(Typeface.NORMAL),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtKhataUserSerialNumber.setText(sbSerialNumber);
        txtKhataUserPhone.setText(khataModel.getKhataUserPhone());
//        Log.d(TAG, "manipulateViews: khataModel ID : "+khataModel.getKhataUserId());
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
                    ((HomepageActivity)activity).manipulateAddTransactionFragment(Tags.KEY_ADD_ENTRY_IN_KHATA,true,khataModel.getKhataUserIdString());
                }
                return true;
            }
        });
        addElementsInRecents();
        initialiseTransactionRecyclerView();
    }
    public void addElementsInRecents() {
        transactionModelList=new ArrayList<>();
        balance=0;
        for (int i=0;i<HomepageActivity.khataTransactionModelList.size();i++){
            TransactionModel transactionModel=HomepageActivity.khataTransactionModelList.get(i);
                Log.d(TAG, "addTransaction: AddEntryInKhata: "+transactionModel.getKhataNumber() + " khataModel.getKhataUserIdString(): "+khataModel.getKhataUserIdString());
            if (transactionModel.getKhataNumber()!=null && khataModel.getKhataUserIdString()!=null && khataModel.getKhataUserIdString().equals(transactionModel.getKhataNumber())){
                transactionModelList.add(transactionModel);
                if (transactionModel.getPaymentType()!=null) {
                    if (transactionModel.getPaymentType().equals(Tags.KEY_DEBIT)) {
                        balance -= transactionModel.getAmountTransferred();
                    } else if (transactionModel.getPaymentType().equals(Tags.KEY_CREDIT)) {
                        balance += transactionModel.getAmountTransferred();
                    }
                }
            }
        }
        if (khataTransactions!=null) {
            khataTransactions.transactionModelList = transactionModelList;
            khataTransactions.notifyDataSetChanged();
        }
        String balanceStr=String.valueOf(balance);
        txtKhataBalance.setText(balanceStr);
//        transactionModelList=HomepageActivity.khataTransactionModelList;
    }
    public void initialiseTransactionRecyclerView(){
        khataTransactions= new TransactionAdapter(transactionModelList, activity, Tags.KEY_SPECIFIC, new TransactionAdapter.OnButtonClick() {
            @Override
            public void onDeleteTransaction(TransactionModel transactionModel) {

            }

            @Override
            public void onRestoreTransaction(TransactionModel transactionModel) {

            }
        });
        rvKhataTransactions.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        rvKhataTransactions.setAdapter(khataTransactions);
    }
}