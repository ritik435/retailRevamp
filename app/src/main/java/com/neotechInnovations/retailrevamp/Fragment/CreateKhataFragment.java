package com.neotechInnovations.retailrevamp.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neotechInnovations.retailrevamp.Activity.HomepageActivity;
import com.neotechInnovations.retailrevamp.Adapter.KhataAdapter;
import com.neotechInnovations.retailrevamp.Constant.Tags;
import com.neotechInnovations.retailrevamp.Model.KhataModel;
import com.neotechInnovations.retailrevamp.R;
import com.neotechInnovations.retailrevamp.Utils.SessionManagement;
import com.neotechInnovations.retailrevamp.Utils.SharedPreference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateKhataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateKhataFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CreateKhataFragment";

    // TODO: Rename and change types of parameters
    private String khataOpeningType;
    private String mParam2;
    HomepageActivity activity;
    KhataAdapter allKhataAdapter;
    RecyclerView rvAllKhata;
    ImageView ivBackBtn;
    EditText etKhataUserName, etKhataUserPhoneNumber;
    TextView txtKhataUserSerialNumber, txtDeleteKhata;
    NestedScrollView nsvCreateKhata;
    CardView cvCreateKhata;
    ImageView ivKhataUserImage;
    Animation shakeAnimation;
    List<KhataModel> khataModelList = new ArrayList<>();
    TextView txtCreateHeading;
    LinearLayout llCreateMainContainer, llAllKhata;


    public CreateKhataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateKhataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateKhataFragment newInstance(String param1, String param2) {
        CreateKhataFragment fragment = new CreateKhataFragment();
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
            khataOpeningType = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        activity = (HomepageActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_khata, container, false);
        initialiseviews(view);
        manipulateViews();
        return view;
    }

    private void initialiseviews(View view) {
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        rvAllKhata = view.findViewById(R.id.rv_all_khata);
        ivBackBtn = view.findViewById(R.id.iv_back_btn);
        etKhataUserName = view.findViewById(R.id.et_khata_user_name);
        etKhataUserPhoneNumber = view.findViewById(R.id.et_khata_user_phone_number);
        ivKhataUserImage = view.findViewById(R.id.iv_khata_user_image);
        txtKhataUserSerialNumber = view.findViewById(R.id.txt_khata_serial_number);
        cvCreateKhata = view.findViewById(R.id.cv_create_khata);
        txtDeleteKhata = view.findViewById(R.id.txt_delete_khata);
        nsvCreateKhata = view.findViewById(R.id.nsv_create_khata);
        txtCreateHeading = view.findViewById(R.id.txt_create_heading);
        llCreateMainContainer = view.findViewById(R.id.ll_create_main_container);
        llAllKhata = view.findViewById(R.id.ll_all_khata);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void manipulateViews() {

        if (khataOpeningType.equals(Tags.KEY_VIEW_KHATA)) {
            llAllKhata.setVisibility(View.GONE);
            llCreateMainContainer.setVisibility(View.GONE);
            txtCreateHeading.setText("Khata Book");
        }else if (khataOpeningType.equals(Tags.KEY_CREATE_KHATA)) {
            llAllKhata.setVisibility(View.VISIBLE);
            llCreateMainContainer.setVisibility(View.VISIBLE);
            txtCreateHeading.setText("Create a Khata");
        }

        addElementInKhata();
        initialiseKhataEntryRecyclerView();
        resetCreateKhata();
        txtDeleteKhata.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    txtDeleteKhata.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        txtDeleteKhata.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment : cvAddCollection to call ");
                        SharedPreference sharedPreference = new SharedPreference(activity);
                        SharedPreference.clearNewKhataList();
                        khataModelList = new ArrayList<>();
                        HomepageActivity.newKhataList = khataModelList;
                        allKhataAdapter.khataModelList = khataModelList;
                        allKhataAdapter.notifyDataSetChanged();
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!HomepageActivity.isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        txtDeleteKhata.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return false;
            }
        });
        cvCreateKhata.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    cvCreateKhata.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        cvCreateKhata.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment : cvAddCollection to call ");
                        createKhata();
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!HomepageActivity.isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        cvCreateKhata.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return true;
            }
        });
        ivBackBtn.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    ivBackBtn.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        ivBackBtn.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment : cvAddCollection to call ");
                        ((HomepageActivity) activity).onBackPressed();
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!HomepageActivity.isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        ivBackBtn.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return true;
            }
        });

    }

    private void addElementInKhata() {
        khataModelList = HomepageActivity.newKhataList;
    }

    private void saveInLocal(List<KhataModel> newKhataList) {
        // Save the list to SharedPreferences
        SharedPreference.savenNewKhataLists(newKhataList);
    }

    private void initialiseKhataEntryRecyclerView() {
        Log.d(TAG, "initialiseKhataEntryRecyclerView: " + khataModelList);
        allKhataAdapter = new KhataAdapter(khataModelList, activity);
        rvAllKhata.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rvAllKhata.setAdapter(allKhataAdapter);
    }

    private void createKhata() {
        String userName = etKhataUserName.getText().toString();
        String phoneNumber = etKhataUserPhoneNumber.getText().toString();
        String serialNumber = txtKhataUserSerialNumber.getText().toString();
        etKhataUserName.setHintTextColor(resolveColorAttribute(activity, com.google.android.material.R.attr.colorPrimaryVariant));

        if (userName.equals("") || userName.equals("null")) {
            etKhataUserName.setHintTextColor(resolveColorAttribute(activity, com.google.android.material.R.attr.colorOnSurfaceInverse));
            etKhataUserName.startAnimation(shakeAnimation);
            return;
        }
        if (phoneNumber.equals("") || phoneNumber.equals("null")) {
            etKhataUserPhoneNumber.setHintTextColor(resolveColorAttribute(activity, com.google.android.material.R.attr.colorOnSurfaceInverse));
            etKhataUserPhoneNumber.startAnimation(shakeAnimation);
            return;
        }
        hideKeyboard(activity);
        KhataModel khataModel = new KhataModel();
        khataModel.setKhataSerialNumber(serialNumber);
        khataModel.setKhataUserName(userName);
        khataModel.setKhataUserPhone(phoneNumber);
//        khataModel.setKhataUserId(UUID.randomUUID());
        khataModel.setUserId(SessionManagement.userId);

        String khatauserIdString = "";
        khatauserIdString += "(#";
        khatauserIdString += serialNumber;
        khatauserIdString += ") ";
        khatauserIdString += userName;
        khataModel.setKhataUserIdString(khatauserIdString);
        khataModel.setUserId(SessionManagement.userId);

        khataModelList.add(khataModel);
        HomepageActivity.suggestedKhataList.add(khataModel.getKhataUserIdString());
        allKhataAdapter.notifyItemInserted(khataModelList.size() - 1);
        Log.d(TAG, "createKhata: rvAllKhata.getChildAt(khataModelList.size() - 1) ::: " + rvAllKhata.getChildAt(khataModelList.size() - 2));
        try {
            final float y = rvAllKhata.getChildAt(khataModelList.size() - 2).getY();
            nsvCreateKhata.post(new Runnable() {
                @Override
                public void run() {
                    nsvCreateKhata.fling(0);
                    nsvCreateKhata.smoothScrollTo(0, (int) y);
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "createKhata: exception in scrolling::::: AddEntryInKhata: " + e.getMessage());
        }
//        rvAllKhata.scrollToPosition(khataModelList.size() - 1);
//        rvAllKhata.setNestedScrollingEnabled(true);
//        rvAllKhata.scrollToPosition(khataModelList.size() - 1);
//        rvAllKhata.postDelayed(()->{
//            rvAllKhata.setNestedScrollingEnabled(false);
//        },5000);
//        ((HomepageActivity)activity).backupKhataOnCloud(khataModel);
        HomepageActivity.newKhataList = khataModelList;
        saveInLocal(khataModelList);
        resetCreateKhata();
    }

    @ColorInt
    private int resolveColorAttribute(Context mContext, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    public void resetCreateKhata() {
        //clear the form
        etKhataUserName.setText("");
        etKhataUserPhoneNumber.setText("");
//        etKhataUserName.setFocusable(false);
//        etKhataUserPhoneNumber.setFocusable(false);
//
//        etKhataUserName.setFocusable(true);
//        etKhataUserPhoneNumber.setFocusable(true);

        txtKhataUserSerialNumber.setText("");
        int size = khataModelList.size();
        size++;
        String serialNumber1 = String.valueOf(size);
        txtKhataUserSerialNumber.setText(serialNumber1);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}