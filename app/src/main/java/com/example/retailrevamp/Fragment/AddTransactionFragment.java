package com.example.retailrevamp.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retailrevamp.Activity.HomepageActivity;
import com.example.retailrevamp.Adapter.TransactionAdapter;
import com.example.retailrevamp.Constant.Tags;
import com.example.retailrevamp.Model.TransactionModel;
import com.example.retailrevamp.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTransactionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "AddTransactionFragment";

    // TODO: Rename and change types of parameters
    private String keyAddTransaction;
    private String mParams2;
    TextView txtTransactionType;
    LinearLayout llCloseAddTransaction, llEnterPayment, llPayment, llSales, llCollection, llEntryInKhata;
    EditText etAddUserName, etTotalAmount, etAmountTransferred;

    RecyclerView rvRecents;
    HomepageActivity activity;
    private List<TransactionModel> transactionModelList = new ArrayList<>();
    private List<TransactionModel> specificTransactionModelList = new ArrayList<>();
    private TransactionAdapter transactionAdapter;
    Animation shakeAnimation;
    EditText etAddKhataUserName,etAddSalesUserName,etTotalAmountKhata,etTotalAmountSales,etTotalCollection,etOtherCollections;
    LinearLayout llEnterSales,llEnterCollection,llEnterEntryInKhata,llInitial;
    ImageView ivChecked,ivUnchecked;

    public AddTransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTransactionFragment newInstance(String param1, String param2) {
        AddTransactionFragment fragment = new AddTransactionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            keyAddTransaction = getArguments().getString(ARG_PARAM1);
            mParams2 = getArguments().getString(ARG_PARAM1);
        }
        activity = (HomepageActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);
        initialiseViews(view);
        return view;
    }

    private void initialiseViews(View view) {
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        txtTransactionType = view.findViewById(R.id.txt_transaction_type);
        llCloseAddTransaction = view.findViewById(R.id.ll_close_add_transaction);
        etAddUserName = view.findViewById(R.id.et_add_user_name);
        etTotalAmount = view.findViewById(R.id.et_total_amount);
        etAmountTransferred = view.findViewById(R.id.et_amount_transferred);
        llEnterPayment = view.findViewById(R.id.ll_enter);
        llPayment = view.findViewById(R.id.ll_payment);
        llSales = view.findViewById(R.id.ll_sales);
        llCollection = view.findViewById(R.id.ll_collection);
        llEntryInKhata = view.findViewById(R.id.ll_entry_in_khata);
        rvRecents = view.findViewById(R.id.rv_recents);
        etAddSalesUserName = view.findViewById(R.id.et_add_sales_user_name);
        etTotalAmountSales = view.findViewById(R.id.et_total_amount_sales);
        llEnterSales = view.findViewById(R.id.ll_enter_sales);
        etTotalCollection = view.findViewById(R.id.et_total_collection);
        llEnterCollection = view.findViewById(R.id.ll_enter_collection);
        etOtherCollections = view.findViewById(R.id.et_other_collections);
        llInitial = view.findViewById(R.id.ll_initial);
        ivChecked = view.findViewById(R.id.iv_checked);
        ivUnchecked = view.findViewById(R.id.iv_un_checked);
        etAddKhataUserName = view.findViewById(R.id.et_add_khata_user_name);
        etTotalAmountKhata = view.findViewById(R.id.et_total_amount_khata);
        llEnterEntryInKhata = view.findViewById(R.id.ll_enter_entry_in_khata);

        manipulateViews();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void manipulateViews() {
        Log.d(TAG, "manipulateViews: ");
        manipulateAddTransactionType();
        initialiseTransactionRecyclerView();


        llEnterEntryInKhata.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    llEnterEntryInKhata.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        llEnterEntryInKhata.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment : cvAddCollection to call ");
                        validAsTransaction(etAddKhataUserName,etTotalAmountKhata,etTotalAmountKhata);
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        llEnterEntryInKhata.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return true;
            }
        });
        llEnterSales.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    llEnterSales.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        llEnterSales.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment : cvAddCollection to call ");
                        validAsTransaction(etAddSalesUserName,etTotalAmountSales,etTotalAmountSales);
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        llEnterSales.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return true;
            }
        });
        llEnterCollection.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    llEnterCollection.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        llEnterCollection.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment : cvAddCollection to call ");
                        validAsTransaction(etOtherCollections,etTotalCollection,etTotalCollection);
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        llEnterCollection.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return true;
            }
        });
        llCloseAddTransaction.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    llCloseAddTransaction.setAlpha(0.5f);
                    llCloseAddTransaction.postDelayed(() -> {
                        llCloseAddTransaction.setAlpha(1f);
                    }, 200);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    llCloseAddTransaction.setAlpha(1f);
                    ((HomepageActivity) activity).onBackPressed();
                }
                return true;
            }
        });
        llEnterPayment.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment : llEnterPayment to call ");
                    inArea = true;
                    llEnterPayment.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment : llEnterPayment UP "+inArea);
                    if (inArea) {
                        llEnterPayment.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment : llEnterPayment to call ");
                        validAsTransaction(etAddUserName,etTotalAmount,etAmountTransferred);
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        llEnterPayment.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return true;
            }
        });
    }

    private boolean isInsideButtonArea(View v, MotionEvent event) {
        // Check if the touch event coordinates are within the button's bounds
        return event.getX() >= 0 && event.getX() <= v.getWidth() &&
                event.getY() >= 0 && event.getY() <= v.getHeight();
    }

    private void manipulateAddTransactionType() {
        transactionModelList = HomepageActivity.transactionModelList;
        if (keyAddTransaction.equals(Tags.KEY_ADD_COLLECTION)) {
            llSales.setVisibility(View.GONE);
            llPayment.setVisibility(View.GONE);
            llCollection.setVisibility(View.VISIBLE);
            llEntryInKhata.setVisibility(View.GONE);
            txtTransactionType.setText(Tags.STRING_ADD_COLLECTION);
            specificTransactionModelList = HomepageActivity.collectionTransactionModelList;
        }
        else if (keyAddTransaction.equals(Tags.KEY_ADD_SALES)) {
            llSales.setVisibility(View.VISIBLE);
            llPayment.setVisibility(View.GONE);
            llCollection.setVisibility(View.GONE);
            llEntryInKhata.setVisibility(View.GONE);
            txtTransactionType.setText(Tags.STRING_ADD_SALES);
            specificTransactionModelList = HomepageActivity.salesTransactionModelList;
        }
        else if (keyAddTransaction.equals(Tags.KEY_ADD_ENTRY_IN_KHATA)) {
            llSales.setVisibility(View.GONE);
            llPayment.setVisibility(View.GONE);
            llCollection.setVisibility(View.GONE);
            llEntryInKhata.setVisibility(View.VISIBLE);
            txtTransactionType.setText(Tags.STRING_ADD_ENTRY_IN_KHATA);
            specificTransactionModelList = HomepageActivity.khataTransactionModelList;
        }
        else if (keyAddTransaction.equals(Tags.KEY_ADD_PAYMENTS)) {
            llSales.setVisibility(View.GONE);
            llPayment.setVisibility(View.VISIBLE);
            llCollection.setVisibility(View.GONE);
            llEntryInKhata.setVisibility(View.GONE);
            txtTransactionType.setText(Tags.STRING_ADD_PAYMENTS);
            specificTransactionModelList = HomepageActivity.paymentTransactionModelList;
        }
    }

    private void addTransaction(String userName, Integer amountTransfer, Integer totalAmountInt, Integer balance, String modeOfPayment) {
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setUserName(userName);
        transactionModel.setAmountTransferred(amountTransfer);
        transactionModel.setTotalAmount(totalAmountInt);
        transactionModel.setBalance(balance);
        transactionModel.setMode(modeOfPayment);
        transactionModel.setTransaction(true);
        transactionModel.setKey(keyAddTransaction);

        transactionModel.setDate(new Timestamp(System.currentTimeMillis()));
        checkAnotherTransaction(transactionModel);

        transactionModelList.add(1, transactionModel);
        specificTransactionModelList.add(1, transactionModel);

        Log.d(TAG, "addTransaction: transaction added in transactionModelList: " + transactionModelList.size());
        Log.d(TAG, "addTransaction: transaction added in SPECIFICtransactionModelList: " + specificTransactionModelList.size());
        if (specificTransactionModelList.size() > 2) {
            transactionAdapter.notifyDataSetChanged();
        } else
            initialiseTransactionRecyclerView();
        syncLists();
        hideKeyboard(activity);
        resetAddTransadtion();
    }

    private void validAsTransaction(EditText etUserNameGeneric,EditText etTotalAmountGeneric,EditText etAmountTransferredGeneric) {
        Log.d(TAG, "addTransaction: enteredd.... " + keyAddTransaction);
            String userName = etUserNameGeneric.getText().toString();
            String totalAmount = etTotalAmountGeneric.getText().toString();
            String amountTransferred = etAmountTransferredGeneric.getText().toString();

            if (userName.equals("") || userName.equals("null")) {
                etUserNameGeneric.setAnimation(shakeAnimation);
                etUserNameGeneric.setHintTextColor(getResources().getColor(R.color.decreasing_red));
                return;
            }
            if (totalAmount.equals("") || totalAmount.equals("null")) {
                etTotalAmountGeneric.setHintTextColor(getResources().getColor(R.color.decreasing_red));
                etTotalAmountGeneric.setAnimation(shakeAnimation);
                return;
            }
            if (amountTransferred.equals("") || amountTransferred.equals("null")) {
                etAmountTransferredGeneric.setHintTextColor(getResources().getColor(R.color.decreasing_red));
                etAmountTransferredGeneric.setAnimation(shakeAnimation);
                return;
            }
            Integer amountTransfer = Integer.valueOf(amountTransferred);
            Integer totalAmountInt = Integer.valueOf(totalAmount);
            Integer balance = totalAmountInt - amountTransfer;

            String modeOfPayment = "online";
            addTransaction(userName, amountTransfer, totalAmountInt, balance, modeOfPayment);

    }

    private void checkAnotherTransaction(TransactionModel specificTransaction) {
        Log.d(TAG, "checkAnotherTransaction: enterred... ");
        SimpleDateFormat dateFormat = new SimpleDateFormat(Tags.DATE_FORMAT, Locale.getDefault());
        // Convert Timestamp to Date object
        Date currentDate = new Date(specificTransaction.getDate().getTime());
        Date latestEntryDate = null;
        Date latestEntrySpecificDate = null;
        String latestEntryDateString = "";
        String latestEntrySpecificDateString = "";
        Log.d(TAG, "checkAnotherTransaction 1: ");
        try {
            Log.d(TAG, "checkAnotherTransaction 1 a): " + transactionModelList.get(0).getDate().toString());
            TransactionModel transactionModel = transactionModelList.get(0);
            latestEntryDate = new Date(transactionModel.getDate().getTime());
            latestEntryDateString = dateFormat.format(latestEntryDate);
            TransactionModel specificTransactionModel = specificTransactionModelList.get(0);
            latestEntrySpecificDate = new Date(specificTransactionModel.getDate().getTime());
            latestEntrySpecificDateString = dateFormat.format(latestEntrySpecificDate);
        } catch (Exception e) {
            Log.d(TAG, "checkAnotherTransaction: " + e.getMessage());
        }
        Log.d(TAG, "checkAnotherTransaction 2: ");
        // Format the Date object into a string
        String currentdateString = dateFormat.format(currentDate);
        String currentdateSpecificString = dateFormat.format(currentDate);

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setDate(specificTransaction.getDate());
        transactionModel.setTransaction(false);
        Log.d(TAG, "checkAnotherTransaction 3 a) : currentDateString" + currentdateString + " latestEntrySpecificDateString : " + latestEntrySpecificDateString);

        if ((specificTransactionModelList != null && specificTransactionModelList.size() > 0) && currentdateString.equals(latestEntrySpecificDateString)) {
            //then do nothing

        } else {
            //if not then make new entry of date sequence at 0th element.
            if (specificTransactionModelList == null) {
                specificTransactionModelList = new ArrayList<>();
            }
            specificTransactionModelList.add(0, transactionModel);
            Log.d(TAG, "checkAnotherTransaction: is added in transaction and specific Transaction ... specificTransactionModelList: " + specificTransactionModelList.size());
        }
        Log.d(TAG, "checkAnotherTransaction 3 b ) : currentDateString" + currentdateString + " latestEntryDateString : " + latestEntryDateString);

        //check if latest entry date & current entry is matching
        if ((transactionModelList != null && transactionModelList.size() > 0) && currentdateString.equals(latestEntryDateString)) {
            //then do nothing

        } else {
            //if not then make new entry of date sequence at 0th element.
            if (transactionModelList == null) {
                transactionModelList = new ArrayList<>();
            }
            transactionModelList.add(0, transactionModel);
            Log.d(TAG, "checkAnotherTransaction: is added in transaction and specific Transaction ... transactionModelList: " + transactionModelList.size());
        }

    }

    private void syncLists() {
        ((HomepageActivity) activity).addTransactionInList(transactionModelList, specificTransactionModelList, keyAddTransaction);
    }


    public void resetAddTransadtion() {
        //clear the form
        etAddUserName.setText("");
        etAmountTransferred.setText("");
        etTotalAmount.setText("");
        etAddSalesUserName.setText("");
        etTotalAmountSales.setText("");
        etOtherCollections.setText("");
        etTotalCollection.setText("");
        etAddKhataUserName.setText("");
        etTotalAmountKhata.setText("");
//        txtKhataUserSerialNumber.setText("");
//        String serialNumber1 = String.valueOf(khataModelList.size());
//        txtKhataUserSerialNumber.setText(serialNumber1);
    }

    public void initialiseTransactionRecyclerView() {
//        Log.d(TAG, "initialiseTransactionRecyclerView: "+specificTransactionModelList.size());
        transactionAdapter = new TransactionAdapter(specificTransactionModelList, activity);
        rvRecents.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rvRecents.setAdapter(transactionAdapter);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}