package com.neotechInnovations.retailrevamp.Fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neotechInnovations.retailrevamp.Activity.HomepageActivity;
import com.neotechInnovations.retailrevamp.Adapter.SuggestedAdapter;
import com.neotechInnovations.retailrevamp.Adapter.TransactionAdapter;
import com.neotechInnovations.retailrevamp.Constant.Tags;
import com.neotechInnovations.retailrevamp.Model.KhataModel;
import com.neotechInnovations.retailrevamp.Model.TransactionModel;
import com.neotechInnovations.retailrevamp.R;
import com.neotechInnovations.retailrevamp.Utils.SessionManagement;
import com.neotechInnovations.retailrevamp.Utils.SharedPreference;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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
    EditText etAddKhataUserName, etAddSalesUserName, etTotalAmountKhata, etTotalAmountSales, etTotalCollection, etOtherCollections;
    LinearLayout llEnterSales, llEnterCollection, llEnterEntryInKhata, llInitial, llOtherCollections;
    RelativeLayout rlSuggestedKhata;
    ImageView ivChecked, ivUnchecked, ivOpenSuggestedKhata;
    String entryKhataName = "";
    RecyclerView rvSuggestedKhata;
    SuggestedAdapter suggestedAdapter;
    List<String> suggestedKhataList;
    Boolean isSuggestedKhataOpened, isValidKhata;
    RadioGroup rgCollection;
    RadioButton radioButton, rbSalesOnline, rbSalesOffline, rbKhataEntryGiven, rbKhataEntryTaken, rbCollectionCash, rbCollectionOther, rbCollectionOnline;
    ImageView ivPaymentOffline, ivPaymentOnline, ivPaymentCheque;
    Boolean setAsInitial = false;
    TextView txtClear;

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
            entryKhataName = getArguments().getString(ARG_PARAM2);
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
        etOtherCollections = view.findViewById(R.id.et_collection_others);
        llOtherCollections = view.findViewById(R.id.ll_collection_others);
        llInitial = view.findViewById(R.id.ll_initial);
        ivChecked = view.findViewById(R.id.iv_checked);
        ivUnchecked = view.findViewById(R.id.iv_un_checked);
        etAddKhataUserName = view.findViewById(R.id.et_add_khata_user_name);
        etTotalAmountKhata = view.findViewById(R.id.et_total_amount_khata);
        llEnterEntryInKhata = view.findViewById(R.id.ll_enter_entry_in_khata);
        rlSuggestedKhata = view.findViewById(R.id.rl_suggested_khata);
        ivOpenSuggestedKhata = view.findViewById(R.id.iv_open_suggested_khata);
        rvSuggestedKhata = view.findViewById(R.id.rv_suggested_khata);
        ivPaymentOnline = view.findViewById(R.id.iv_payment_online);
        ivPaymentOffline = view.findViewById(R.id.iv_payment_offline);
        ivPaymentCheque = view.findViewById(R.id.iv_payment_cheque);
        rbSalesOffline = view.findViewById(R.id.rb_sales_offline);
        rbSalesOnline = view.findViewById(R.id.rb_sales_online);
        rbCollectionCash = view.findViewById(R.id.rb_collection_cash);
        rbCollectionOnline = view.findViewById(R.id.rb_collection_online);
        rbCollectionOther = view.findViewById(R.id.rb_collection_others);
        rbKhataEntryGiven = view.findViewById(R.id.rb_khata_entry_given);
        rbKhataEntryTaken = view.findViewById(R.id.rb_khata_entry_taken);
        rgCollection = view.findViewById(R.id.rg_collection);
        txtClear = view.findViewById(R.id.txt_clear);
//        radioButton.setButtonDrawable(R.drawable.background_manipulate_modes_debit);

        manipulateViews(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void manipulateViews(View view) {

        Log.d(TAG, "manipulateViews: ");
        isSuggestedKhataOpened = false;
        isValidKhata = false;
        rlSuggestedKhata.setVisibility(View.GONE);
        suggestedKhataList = HomepageActivity.suggestedKhataList;
        Log.d(TAG, "manipulateViews: REFINEKHATA : suggestedKhataList :: " + suggestedKhataList.size());

        manipulateAddTransactionType();
        initialiseTransactionRecyclerView();
        initialiseSuggestedKhata();
        txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAddTransadtion();
            }
        });
        ivChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivUnchecked.setVisibility(View.VISIBLE);
                ivChecked.setVisibility(View.GONE);
                setAsInitial = false;
            }
        });
        ivUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivUnchecked.setVisibility(View.GONE);
                ivChecked.setVisibility(View.VISIBLE);
                setAsInitial = true;
            }
        });
        rgCollection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_collection_cash) {
                    ivUnchecked.setVisibility(View.GONE);
                    ivChecked.setVisibility(View.VISIBLE);
                    setAsInitial = true;
                } else if (checkedId == R.id.rb_collection_online) {
                    llOtherCollections.setVisibility(View.GONE);
                    ivUnchecked.setVisibility(View.VISIBLE);
                    ivChecked.setVisibility(View.GONE);
                    setAsInitial = false;
                } else if (checkedId == R.id.rb_collection_others) {
                    llOtherCollections.setVisibility(View.VISIBLE);
                    ivUnchecked.setVisibility(View.VISIBLE);
                    ivChecked.setVisibility(View.GONE);
                    setAsInitial = false;
                }
            }
        });
        etAddKhataUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (count > 0) {
                    manipulateSuggestedKhata(true);
                } else {
                    manipulateSuggestedKhata(false);
                }
                isValidKhata = false;
                String str = etAddKhataUserName.getText().toString();
                Log.d(TAG, "onTextChanged: REFINEKHATA: str::: " + str + " : originalList suggestedKhataList:::12c: " + suggestedKhataList.size());
                suggestedAdapter.filter(str);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ivOpenSuggestedKhata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manipulateSuggestedKhata(!isSuggestedKhataOpened);
            }
        });

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
                        String paymentType = Tags.KEY_CREDIT;
                        if (rbKhataEntryGiven.isChecked()) {
                            paymentType = Tags.KEY_DEBIT;
                        } else if (rbKhataEntryTaken.isChecked()) {
                            paymentType = Tags.KEY_CREDIT;
                        }
                        validAsTransaction(etAddKhataUserName, etTotalAmountKhata, etTotalAmountKhata, paymentType);
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
                        String paymentType = Tags.KEY_CREDIT;
                        validAsTransaction(etAddSalesUserName, etTotalAmountSales, etTotalAmountSales, paymentType);
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
                        String paymentType = Tags.KEY_CREDIT;

                        validAsTransaction(etOtherCollections, etTotalCollection, etTotalCollection, paymentType);
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
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment : llEnterPayment UP " + inArea);
                    if (inArea) {
                        llEnterPayment.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment : llEnterPayment to call ");
                        String paymentType = Tags.KEY_DEBIT;
                        validAsTransaction(etAddUserName, etTotalAmount, etAmountTransferred, paymentType);
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

    private void manipulateSuggestedKhata(boolean toOpen) {
        if (toOpen != isSuggestedKhataOpened)
            rotateIcon();
        if (toOpen) {
            rlSuggestedKhata.setVisibility(View.VISIBLE);
            isSuggestedKhataOpened = true;
        } else {
            rlSuggestedKhata.setVisibility(View.GONE);
            isSuggestedKhataOpened = false;
        }

    }

    private void initialiseSuggestedKhata() {
        Log.d(TAG, "initialiseSuggestedKhata: REFINEKHATA : suggestedKhataList : " + suggestedKhataList.size());
        suggestedAdapter = new SuggestedAdapter(suggestedKhataList, new SuggestedAdapter.OnItemClicked() {
            @Override
            public void suggestedKhataClicked(String suggestedKhata, Boolean toCreate) {
                if (toCreate) {
                    //close this and open create khata fragment...
                    ((HomepageActivity) activity).manipulateAddTransactionFragment("", false, "");
                    ((HomepageActivity) activity).manipulateCreateKhataFragment(true, Tags.KEY_CREATE_KHATA);
                } else {
                    //fill the khataIdString in etKhataUserName
                    etAddKhataUserName.setText(suggestedKhata);
                    manipulateSuggestedKhata(false);
                    isValidKhata = true;
                }
            }
        });
        rvSuggestedKhata.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rvSuggestedKhata.setAdapter(suggestedAdapter);
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
//            manipulateCollectionMode();
        } else if (keyAddTransaction.equals(Tags.KEY_ADD_SALES)) {
            llSales.setVisibility(View.VISIBLE);
            llPayment.setVisibility(View.GONE);
            llCollection.setVisibility(View.GONE);
            llEntryInKhata.setVisibility(View.GONE);
            txtTransactionType.setText(Tags.STRING_ADD_SALES);
            specificTransactionModelList = HomepageActivity.salesTransactionModelList;
            manipulateSalesMode();
        } else if (keyAddTransaction.equals(Tags.KEY_ADD_ENTRY_IN_KHATA)) {
            llSales.setVisibility(View.GONE);
            llPayment.setVisibility(View.GONE);
            llCollection.setVisibility(View.GONE);
            llEntryInKhata.setVisibility(View.VISIBLE);
            txtTransactionType.setText(Tags.STRING_ADD_ENTRY_IN_KHATA);
            specificTransactionModelList = HomepageActivity.khataTransactionModelList;
            Log.d(TAG, "manipulateAddTransactionType: AddEntryInKhata: " + entryKhataName);
            etAddKhataUserName.setText(entryKhataName);
            if (!entryKhataName.isEmpty()) {
                isValidKhata = true;
            }
            manipulateKhataEntryMode();
        } else if (keyAddTransaction.equals(Tags.KEY_ADD_PAYMENTS)) {
            llSales.setVisibility(View.GONE);
            llPayment.setVisibility(View.VISIBLE);
            llCollection.setVisibility(View.GONE);
            llEntryInKhata.setVisibility(View.GONE);
            txtTransactionType.setText(Tags.STRING_ADD_PAYMENTS);
            specificTransactionModelList = HomepageActivity.paymentTransactionModelList;
            ivPaymentOnline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manipulatePaymentMode(Tags.KEY_ONLINE);
                }
            });
            ivPaymentOffline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manipulatePaymentMode(Tags.KEY_OFFLINE);
                }
            });
            ivPaymentCheque.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manipulatePaymentMode(Tags.KEY_CHEQUE);
                }
            });
        }
    }

    private void addTransaction(String userName, Integer amountTransfer, Integer totalAmountInt, Integer balance, String modeOfPayment, String paymentType) {
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setId(UUID.randomUUID());
        transactionModel.setUserName(userName);
        transactionModel.setAmountTransferred(amountTransfer);
        transactionModel.setTotalAmount(totalAmountInt);
        transactionModel.setBalance(balance);
        transactionModel.setMode(modeOfPayment);
        transactionModel.setTransaction(true);
        transactionModel.setKey(keyAddTransaction);
        transactionModel.setPaymentType(paymentType);
        transactionModel.setBackedUp(false);
        transactionModel.setUserId(SessionManagement.userId);
        if (keyAddTransaction.equals(Tags.KEY_ADD_ENTRY_IN_KHATA)) {
            Log.d(TAG, "addTransaction: AddEntryInKhata: " + userName);
            transactionModel.setKhataNumber(userName);
        }

        transactionModel.setDate(new Timestamp(System.currentTimeMillis()));
        checkAnotherTransaction(transactionModel);

        transactionModelList.add(1, transactionModel);
        specificTransactionModelList.add(1, transactionModel);
        if (keyAddTransaction.equals(Tags.KEY_ADD_ENTRY_IN_KHATA)) {
            for (int i = 0; i < HomepageActivity.newKhataList.size(); i++) {
                KhataModel khataModel = HomepageActivity.newKhataList.get(i);
                if (khataModel.getKhataUserIdString().equals(transactionModel.getKhataNumber())) {
                    Log.d(TAG, "addTransaction: Balance updated for : khataString : " + khataModel.getKhataUserIdString() +" MODE: "+transactionModel.getMode());
                    if (transactionModel.getPaymentType().equals(Tags.KEY_DEBIT)) {
                        khataModel.setKhataBalance(khataModel.getKhataBalance() - transactionModel.getTotalAmount());
                    } else if (transactionModel.getPaymentType().equals(Tags.KEY_CREDIT)) {
                        khataModel.setKhataBalance(khataModel.getKhataBalance() + transactionModel.getTotalAmount());
                    }
                    Log.d(TAG, "addTransaction: Balance updated to : " + khataModel.getKhataBalance());
                    HomepageActivity.newKhataList.set(i,khataModel);
                }
            }
            SharedPreference.savenNewKhataLists(HomepageActivity.newKhataList);
        }

        Log.d(TAG, "addTransaction: transaction added in transactionModelList: " + transactionModelList.size());
        Toast.makeText(getActivity().getApplicationContext(),"Transaction Added",Toast.LENGTH_LONG ).show();

        Log.d(TAG, "addTransaction: transaction added in SPECIFICtransactionModelList: " + specificTransactionModelList.size());
        if (specificTransactionModelList.size() > 2) {
            transactionAdapter.notifyDataSetChanged();
        } else
            initialiseTransactionRecyclerView();
        syncLists();
//        ((HomepageActivity)activity).backupOnCloud(transactionModel);
        hideKeyboard(activity);
        resetAddTransadtion();
    }

    private void validAsTransaction(EditText etUserNameGeneric, EditText etTotalAmountGeneric, EditText etAmountTransferredGeneric, String paymentType) {
        Log.d(TAG, "addTransaction: enteredd.... " + keyAddTransaction);

        String userName = etUserNameGeneric.getText().toString();
        String totalAmount = etTotalAmountGeneric.getText().toString();
        String amountTransferred = etAmountTransferredGeneric.getText().toString();
        etUserNameGeneric.setHintTextColor(getResources().getColor(R.color.black));
        if (keyAddTransaction.equals(Tags.KEY_ADD_COLLECTION)) {
            Log.d(TAG, "validAsTransaction: rbCollectionOnline.isChecked() : " + rbCollectionOnline.isChecked() + " rbCollectionCash.isChecked() : " + rbCollectionCash.isChecked());
            if (rbCollectionOnline.isChecked()) {
                userName = rbCollectionOnline.getText().toString();
            } else if (rbCollectionCash.isChecked())
                userName = rbCollectionCash.getText().toString();
        }
        if (keyAddTransaction.equals(Tags.KEY_ADD_ENTRY_IN_KHATA) && !isValidKhata) {
            etUserNameGeneric.setText("");
            etUserNameGeneric.startAnimation(shakeAnimation);
            etUserNameGeneric.setHintTextColor(getResources().getColor(R.color.decreasing_red));
            return;
        }
        if (userName.equals("") || userName.equals("null")) {
            etUserNameGeneric.startAnimation(shakeAnimation);
            etUserNameGeneric.setHintTextColor(getResources().getColor(R.color.decreasing_red));
            return;
        }
        if (totalAmount.equals("") || totalAmount.equals("null")) {
            etTotalAmountGeneric.setHintTextColor(getResources().getColor(R.color.decreasing_red));
            etTotalAmountGeneric.startAnimation(shakeAnimation);
            return;
        }
        if (amountTransferred.equals("") || amountTransferred.equals("null")) {
            etAmountTransferredGeneric.setHintTextColor(getResources().getColor(R.color.decreasing_red));
            etAmountTransferredGeneric.startAnimation(shakeAnimation);
            return;
        }
        Integer amountTransfer = Integer.valueOf(amountTransferred);
        Integer totalAmountInt = Integer.valueOf(totalAmount);
        Integer balance = totalAmountInt - amountTransfer;

        String modeOfPayment = "online";
        addTransaction(userName, amountTransfer, totalAmountInt, balance, modeOfPayment, paymentType);

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
        transactionModel.setBackedUp(false);
        transactionModel.setUserId(SessionManagement.userId);
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
        rbCollectionOnline.setChecked(false);
        rbCollectionCash.setChecked(false);
        rbCollectionOther.setChecked(false);
//        txtKhataUserSerialNumber.setText("");
//        String serialNumber1 = String.valueOf(khataModelList.size());
//        txtKhataUserSerialNumber.setText(serialNumber1);
    }

    public void initialiseTransactionRecyclerView() {
//        Log.d(TAG, "initialiseTransactionRecyclerView: "+specificTransactionModelList.size());
        transactionAdapter = new TransactionAdapter(specificTransactionModelList, activity, Tags.KEY_SPECIFIC, new TransactionAdapter.OnButtonClick() {
            @Override
            public void onDeleteTransaction(TransactionModel transactionModel) {

            }

            @Override
            public void onRestoreTransaction(TransactionModel transactionModel) {

            }
        });
        rvRecents.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rvRecents.setAdapter(transactionAdapter);
    }

    private void rotateIcon() {
        // Create an ObjectAnimator to rotate the icon
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(ivOpenSuggestedKhata, "rotation", 0f, 180f);
        rotateAnimator.setDuration(300); // Set duration of animation in milliseconds
        rotateAnimator.start();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    void manipulatePaymentMode(String key) {
        if (key.equals(Tags.KEY_ONLINE)) {
            ivPaymentOnline.setAlpha(1f);
            ivPaymentOffline.setAlpha(0.3f);
            ivPaymentCheque.setAlpha(0.3f);
        } else if (key.equals(Tags.KEY_OFFLINE)) {
            ivPaymentOnline.setAlpha(0.3f);
            ivPaymentOffline.setAlpha(1f);
            ivPaymentCheque.setAlpha(0.3f);
        } else if (key.equals(Tags.KEY_CHEQUE)) {
            ivPaymentOnline.setAlpha(0.3f);
            ivPaymentOffline.setAlpha(0.3f);
            ivPaymentCheque.setAlpha(1f);
        }
    }

    void manipulateSalesMode() {

        rbSalesOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                rbSalesOffline.setChecked(false);
            }
        });
        rbSalesOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                rbSalesOnline.setChecked(false);
            }
        });
    }

    void manipulateKhataEntryMode() {

        rbKhataEntryGiven.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            }
        });
        rbKhataEntryTaken.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            }
        });
    }

    void manipulateCollectionMode() {
        rbCollectionCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                rbCollectionOther.setChecked(false);
                rbCollectionOnline.setChecked(false);
            }
        });
        rbCollectionOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                rbCollectionOther.setChecked(false);
                rbCollectionCash.setChecked(false);
            }
        });
        rbCollectionOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                rbCollectionCash.setChecked(false);
                rbCollectionOnline.setChecked(false);
            }
        });
    }
}