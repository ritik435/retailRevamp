package com.neotechInnovations.retailrevamp.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.neotechInnovations.retailrevamp.API.ResponseListener;
import com.neotechInnovations.retailrevamp.API.RevampRetrofit;
import com.neotechInnovations.retailrevamp.Adapter.StatsticsInfoAdapter;
import com.neotechInnovations.retailrevamp.Adapter.TransactionAdapter;
import com.neotechInnovations.retailrevamp.Constant.Tags;
import com.neotechInnovations.retailrevamp.Fragment.AddTransactionFragment;
import com.neotechInnovations.retailrevamp.Fragment.AllTransactionsFragment;
import com.neotechInnovations.retailrevamp.Fragment.CreateKhataFragment;
import com.neotechInnovations.retailrevamp.Fragment.HistoryFragment;
import com.neotechInnovations.retailrevamp.Fragment.KhataFragment;
import com.neotechInnovations.retailrevamp.Fragment.LoginFragment;
import com.neotechInnovations.retailrevamp.Model.KhataModel;
import com.neotechInnovations.retailrevamp.Model.StatsticsInfoModel;
import com.neotechInnovations.retailrevamp.Model.TransactionModel;
import com.neotechInnovations.retailrevamp.R;
import com.neotechInnovations.retailrevamp.Utils.GoogleSheetsManager;
import com.neotechInnovations.retailrevamp.Utils.LocaleHelper;
import com.neotechInnovations.retailrevamp.Utils.SessionManagement;
import com.neotechInnovations.retailrevamp.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;

public class HomepageActivity extends AppCompatActivity {

    private static final String TAG = "HomepageActivity";
    public static String spreadsheetLink;
    public static String spreadsheetId;
    SharedPreference sharedPreference;
    CardView cvAddPayments, cvAddSales, cvAddEntryInKhata, cvAppLock, cvAddCollection;
    LinearLayout llLanguageChange;
    TextView txtAllTransaction, txtLanguage;
    SwitchCompat materialSwitch;
    RecyclerView rvStatsInfo, rvRecents;
    FrameLayout flAddTransaction, flAddTransactionBg, flAllTransaction, flLoginFragment, flCreateKhataFragment, flKhataFragment, flHistoryFragment;
    DrawerLayout drawerLayout;
    NavigationView nvSidebarMenu;
    LinearLayout llSidebarBg;
    MaterialToolbar topAppBar;
    private static boolean isAddTransactionFragmentOpened, isAllTransactionFragmentOpened, isCreateKhataFragmentOpened, isLoginFragmentOpened, isKhataFragmentOpened, isHistoryFragmentOpened;
    AddTransactionFragment addTransactionFragment;
    AllTransactionsFragment allTransactionsFragment;
    CreateKhataFragment createKhataFragment;
    KhataFragment khataFragment;
    LoginFragment loginFragment;
    HistoryFragment historyFragment;
    ImageView ivMenu;
    BottomSheetBehavior addTransactionBottomSheetBehavior;
    List<StatsticsInfoModel> statsticsInfoModelList = new ArrayList<>();
    private static boolean isProMode, isEasyMode;
    public static boolean toRefreshListInHomepage = false;
    HashMap<String, List<Integer>> hmStatsInfo = new HashMap<>();
    public static List<TransactionModel> transactionModelList = new ArrayList<>();
    public static List<TransactionModel> salesTransactionModelList = new ArrayList<>();
    public static List<TransactionModel> collectionTransactionModelList = new ArrayList<>();
    public static List<TransactionModel> paymentTransactionModelList = new ArrayList<>();
    public static List<TransactionModel> khataTransactionModelList = new ArrayList<>();
    public static List<TransactionModel> specificTransactionModelsList = new ArrayList<>();
    public static List<KhataModel> newKhataList = new ArrayList<>();
    public static List<String> suggestedKhataList = new ArrayList<>();
    boolean needForceBackedUp = true;
    int backupCountTrans = 0, backedUpSuccessTrans = 0;
    int backupCountKhata = 0, backedUpSuccessKhata = 0;
    StatsticsInfoAdapter statsticsInfoAdapter;
    TransactionAdapter transactionAdapter;
    Integer language;
    CardView signUpButton, cvHistory;
    LinearLayout llSidepanelDetails, llSidepanelSignUp;
    TextView txtSidepanelPhoneNumber, txtSidepanelUserName, txtSidepanelUserEmail, txtUserName, txtSidepanelProfile;
    public static boolean isDataBackedUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        sharedPreference = new SharedPreference(this);
        initialiseViews();
        manipulateViews();

    }

    private void initialiseViews() {
        cvAddPayments = findViewById(R.id.cv_add_payments);
        cvAddSales = findViewById(R.id.cv_add_sales);
        cvAddCollection = findViewById(R.id.cv_add_collection);
        cvAddEntryInKhata = findViewById(R.id.cv_add_entry_in_khata);
        cvAppLock = findViewById(R.id.cv_app_lock);
        ivMenu = findViewById(R.id.iv_hamburger_menu);
        llLanguageChange = findViewById(R.id.ll_language_change);
        txtAllTransaction = findViewById(R.id.txt_all_transactions);
        rvStatsInfo = findViewById(R.id.rv_main_info);
        rvRecents = findViewById(R.id.rv_recents);
        flAddTransaction = findViewById(R.id.fl_add_transaction);
        flAddTransactionBg = findViewById(R.id.fl_add_transaction_bg);
        flAllTransaction = findViewById(R.id.fl_all_transaction);
        drawerLayout = findViewById(R.id.drawerLayout);
        topAppBar = findViewById(R.id.topAppBar);
        nvSidebarMenu = findViewById(R.id.nv_sidebar_menu);
        llSidebarBg = findViewById(R.id.ll_sidebar_bg);
        flLoginFragment = findViewById(R.id.fl_login);
        flCreateKhataFragment = findViewById(R.id.fl_create_khata);
        flKhataFragment = findViewById(R.id.fl_khata);
        txtLanguage = findViewById(R.id.txt_language);
        materialSwitch = findViewById(R.id.material_switch);
        txtUserName = findViewById(R.id.txt_user_name);
        cvHistory = findViewById(R.id.cv_history);
        flHistoryFragment = findViewById(R.id.fl_history);

        // Access the header layout
        View headerView = nvSidebarMenu.getHeaderView(0);
// Find the "Sign Up" button in the header and set a click listener
        signUpButton = headerView.findViewById(R.id.cv_sign_up);
        llSidepanelDetails = headerView.findViewById(R.id.ll_sidepanel_details);
        llSidepanelSignUp = headerView.findViewById(R.id.ll_sidepanel_sign_up);
        txtSidepanelPhoneNumber = headerView.findViewById(R.id.txt_sidepanel_phone_number);
        txtSidepanelUserName = headerView.findViewById(R.id.txt_sidepanel_user_name);

        txtSidepanelUserEmail = headerView.findViewById(R.id.txt_sidepanel_user_email);
        txtSidepanelProfile = headerView.findViewById(R.id.txt_sidepanel_profile);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void manipulateViews() {

        isProMode = false;
        isEasyMode = true;
        isAddTransactionFragmentOpened = false;
        isAllTransactionFragmentOpened = false;
        isCreateKhataFragmentOpened = false;
        isLoginFragmentOpened = false;
        isKhataFragmentOpened = false;
        spreadsheetLink = "";
        language = 0;

        setSupportActionBar(topAppBar);
        NavClick();
        changeLanguageManipulateViews(language);
        navigationLayoutAdjustment();
        loggedIn();

        nvSidebarMenu.setVisibility(View.VISIBLE);
        drawerLayout.closeDrawer(GravityCompat.START);
        llSidebarBg.setVisibility(View.GONE);
        cvHistory.setOnClickListener(view -> {
            manipulateHistoryFragment(true);
        });
        txtSidepanelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nvSidebarMenu.setVisibility(View.GONE);
                drawerLayout.closeDrawer(GravityCompat.START);
                llSidebarBg.setVisibility(View.GONE);
                // Handle the sign-up button click here
                Toast.makeText(getApplicationContext(), "Sign Up clicked", Toast.LENGTH_SHORT).show();
                manipulateLoginFragment(true);
                // Or navigate to the sign-up screen
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nvSidebarMenu.setVisibility(View.GONE);
                drawerLayout.closeDrawer(GravityCompat.START);
                llSidebarBg.setVisibility(View.GONE);
                // Handle the sign-up button click here
                Toast.makeText(getApplicationContext(), "Sign Up clicked", Toast.LENGTH_SHORT).show();
                manipulateLoginFragment(true);
                // Or navigate to the sign-up screen
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
//                Log.d(TAG, "onDrawerSlide: " + slideOffset);
                llSidebarBg.setAlpha(slideOffset);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                llSidebarBg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                llSidebarBg.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        if (isEasyMode && !isProMode) {
            cvAddSales.setVisibility(View.GONE);
            cvAddCollection.setVisibility(View.VISIBLE);
        } else if (!isEasyMode && isProMode) {
            cvAddSales.setVisibility(View.GONE);
            cvAddCollection.setVisibility(View.VISIBLE);
        }

        cvAppLock.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    cvAppLock.setAlpha(0.3f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        cvAppLock.setAlpha(1f);
                        Log.d(TAG, "onTouch: calcAllStatsInfo : to call ");
                        calcAllStatsInfo();
//                    initialiseStatsInfoRecyclerView();
                        Log.d(TAG, "onTouch: hmStatsInfo.get(\"28 April 24\") : " + hmStatsInfo.get("28 April 24"));

                        //save data in sheets
                        saveDataInSheets();
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        cvAppLock.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return true;
            }
        });
        llLanguageChange.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    llLanguageChange.setAlpha(0.3f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        llLanguageChange.setAlpha(1f);
                        Log.d(TAG, "onTouch: changeLanguage : to call ");
                        if (language == 0) {
                            language = 1;
                        } else {
                            language = 0;
                        }
                        changeLanguageManipulateViews(language);
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        llLanguageChange.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return true;
            }
        });
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nvSidebarMenu.setVisibility(View.VISIBLE);
                drawerLayout.openDrawer(GravityCompat.START);
                llSidebarBg.setVisibility(View.VISIBLE);
            }
        });


//        addElementsInStatsInfo();
//        addElementsInRecents();
        retrieveDataFromLocal();
        initialiseStatsInfoRecyclerView();
        calcAllStatsInfo();
        refineKhataIds();
        addTransactionBottomSheetBehavior = BottomSheetBehavior.from(flAddTransaction);
        addTransactionBottomSheetBehavior.setHideable(true);
        addTransactionBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        // callback for do something
        addTransactionBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        manipulateAddTransactionFragment("", false, "");
                    }
                    break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        txtAllTransaction.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    txtAllTransaction.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        txtAllTransaction.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAllTransactionFragment : to call ");
                        manipulateAllTransactionFragment(true);
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        txtAllTransaction.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return true;
            }
        });
        cvAddCollection.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    cvAddCollection.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        cvAddCollection.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment : cvAddCollection to call ");
                        manipulateAddTransactionFragment(Tags.KEY_ADD_COLLECTION, true, "");
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        cvAddCollection.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return false;
            }
        });


        cvAddPayments.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    cvAddPayments.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        cvAddPayments.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment : to call ");
                        manipulateAddTransactionFragment(Tags.KEY_ADD_PAYMENTS, true, "");
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        cvAddPayments.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return false;
            }
        });

        cvAddSales.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    cvAddSales.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        cvAddSales.setAlpha(1f);
                        manipulateAddTransactionFragment(Tags.KEY_ADD_SALES, true, "");
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        cvAddSales.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return true;
            }
        });

        cvAddEntryInKhata.setOnTouchListener(new View.OnTouchListener() {
            private boolean inArea = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea = true;
                    cvAddEntryInKhata.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        cvAddEntryInKhata.setAlpha(1f);
                        manipulateAddTransactionFragment(Tags.KEY_ADD_ENTRY_IN_KHATA, true, "");
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD ");
                    if (!isInsideButtonArea(view, motionEvent)) {
                        // Set alpha back to 1 when the finger moves outside the button area
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in outside :" + inArea);
                        cvAddEntryInKhata.setAlpha(1f);
                        // Dispatch a custom ACTION_CANCEL event
                        inArea = false;
                    }
                    Log.d(TAG, "onTouch: manipulateAddTransactionFragment MOVEDD in areaaa " + inArea);
                }
                return true;
            }
        });
//        postAllIsBacked();
//        syncLists();
    }

    private void refineKhataIds() {
        Log.d(TAG, "refineKhataIds: REFINEKHATA : newKhataList : " + newKhataList.size());
        for (int i = 0; i < newKhataList.size(); i++) {
            KhataModel khataModel = newKhataList.get(i);
            if (khataModel.getKhataUserIdString() == null) {
                String khatauserIdString = "";
                khatauserIdString += "(#";
                khatauserIdString += khataModel.getKhataSerialNumber();
                khatauserIdString += ") ";
                khatauserIdString += khataModel.getKhataUserName();
                newKhataList.get(i).setKhataUserIdString(khatauserIdString);
                suggestedKhataList.add(khatauserIdString);
            } else {
                suggestedKhataList.add(khataModel.getKhataUserIdString());
            }
        }
        Log.d(TAG, "refineKhataIds: REFINEKHATA : suggestedKhataList : " + suggestedKhataList.size());
    }

    private void navigationLayoutAdjustment() {
        nvSidebarMenu.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Convert 350dp to pixels
                        int maxWidthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics());

                        // Get the current height of the NavigationView
                        int currentWidth = nvSidebarMenu.getWidth();

                        // Set width to match parent
                        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) nvSidebarMenu.getLayoutParams();
                        params.height = DrawerLayout.LayoutParams.MATCH_PARENT;

                        // If the current height exceeds 350dp, set it to 350dp
                        if (currentWidth > maxWidthPx) {
                            params.width = maxWidthPx;
                        }

                        nvSidebarMenu.setLayoutParams(params);

                        // Remove the listener to prevent it from being called multiple times
                        nvSidebarMenu.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    public static boolean isInsideButtonArea(View v, MotionEvent event) {
        // Check if the touch event coordinates are within the button's bounds
        return event.getX() >= 0 && event.getX() <= v.getWidth() &&
                event.getY() >= 0 && event.getY() <= v.getHeight();
    }

    private void NavClick() {
        nvSidebarMenu.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.view_all_transaction) {
                Log.d(TAG, "NavClick: 1");
                manipulateAllTransactionFragment(true);
            } else if (item.getItemId() == R.id.back_up_on_account) {
                Log.d(TAG, "NavClick: 2");
                if (SessionManagement.userId != null) {
//                    backupAllData();
                    forceBackup();
                } else {
                    Toast.makeText(getApplicationContext(), "Login or signUp before back up", Toast.LENGTH_SHORT).show();
                    manipulateLoginFragment(true);
                }
            } else if (item.getItemId() == R.id.logout) {
                Log.d(TAG, "NavClick: 3");
                logout();
                manipulateLoginFragment(true);
                loggedIn();
            } else if (item.getItemId() == R.id.create_khata) {
                Log.d(TAG, "NavClick: 4");
                manipulateCreateKhataFragment(true, Tags.KEY_CREATE_KHATA);
            } else if (item.getItemId() == R.id.view_all_khata) {
                Log.d(TAG, "NavClick: 4a");
                manipulateCreateKhataFragment(true, Tags.KEY_VIEW_KHATA);
            } else if (item.getItemId() == R.id.erase_all_data) {
                Log.d(TAG, "NavClick: 5");
                eraseAllData();
            } else if (item.getItemId() == R.id.transfer_all_data) {
                retrieveFromSheet();
            } else if (item.getItemId() == R.id.restore_all_data) {
                if (SessionManagement.userId != null) {
//                    backupAllData();
                    restoreFromCloud();
                } else {
                    Toast.makeText(getApplicationContext(), "Login or signUp before restoring", Toast.LENGTH_SHORT).show();
                    manipulateLoginFragment(true);
                }
//                restoreAllTransactions();
            } else if (item.getItemId() == R.id.upload_on_excel) {
                calcAllStatsInfo();
//                    initialiseStatsInfoRecyclerView();
                Log.d(TAG, "onTouch: hmStatsInfo.get(\"28 April 24\") : " + hmStatsInfo.get("28 April 24"));

                //save data in sheets
                saveDataInSheets();
            }
            nvSidebarMenu.setVisibility(View.GONE);
            drawerLayout.closeDrawer(GravityCompat.START);
            llSidebarBg.setVisibility(View.GONE);
            return false;
        });
    }

    private void eraseAllData() {
        Log.d(TAG, "eraseAllData: entereddd ");
        SharedPreference.clearLists();
        SharedPreference.clearNewKhataList();
        hmStatsInfo.clear();
        newKhataList.clear();
        statsticsInfoAdapter.hmStatsInfo = hmStatsInfo;
        statsticsInfoAdapter.notifyDataSetChanged();
        retrieveDataFromLocal();
    }

    private void logout() {
        if (SessionManagement.userId != null) {
            SessionManagement sessionManagement = new SessionManagement(this);
            sessionManagement.clearSession();
        }
    }

    public void loggedIn() {
        SessionManagement sessionManagement = new SessionManagement(this);
        sessionManagement.getSession();
        if (SessionManagement.userId != null) {
            llSidepanelDetails.setVisibility(View.VISIBLE);
            llSidepanelSignUp.setVisibility(View.GONE);
            txtSidepanelPhoneNumber.setText(SessionManagement.userId);
            txtSidepanelUserName.setText(SessionManagement.userName);
            txtSidepanelUserEmail.setText(SessionManagement.userImage);
            txtUserName.setText("Hello , " + SessionManagement.userName);
        } else {
            llSidepanelDetails.setVisibility(View.GONE);
            llSidepanelSignUp.setVisibility(View.VISIBLE);
            txtUserName.setText("Hello , Guest");
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.ll_close_add_transaction) {
            addTransactionBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: is pressed" + isAddTransactionFragmentOpened + " : " + isAllTransactionFragmentOpened + " : " + isCreateKhataFragmentOpened + " : " + isLoginFragmentOpened);
        if (isAddTransactionFragmentOpened) {
            manipulateAddTransactionFragment("", false, "");
        } else if (isAllTransactionFragmentOpened) {
            manipulateAllTransactionFragment(false);
        } else if (isKhataFragmentOpened) {
            manipulateKhataFragment(false, null);
        } else if (isCreateKhataFragmentOpened) {
            manipulateCreateKhataFragment(false, Tags.KEY_VIEW_KHATA);
        } else if (isLoginFragmentOpened) {
            manipulateLoginFragment(false);
        } else if (isHistoryFragmentOpened) {
            manipulateHistoryFragment(false);
        }
    }

    public void manipulateAddTransactionFragment(String keyAddTransaction, boolean toOpen, String entryKhataName) {
        Log.d(TAG, "manipulateAddTransactionFragment: entered... : " + toOpen + " : " + keyAddTransaction);
        if (toOpen) {
            flAddTransaction.setVisibility(View.VISIBLE);
            flAddTransactionBg.setVisibility(View.VISIBLE);
            isAddTransactionFragmentOpened = true;
            addTransactionBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            addTransactionFragment = AddTransactionFragment.newInstance(keyAddTransaction, entryKhataName);
            getSupportFragmentManager().beginTransaction().add(flAddTransaction.getId(), addTransactionFragment).addToBackStack(null).commit();

        } else {
            getSupportFragmentManager().beginTransaction().remove(addTransactionFragment).addToBackStack(null).commit();
            flAddTransaction.setVisibility(View.GONE);
            flAddTransactionBg.setVisibility(View.GONE);
            isAddTransactionFragmentOpened = false;
            if (isKhataFragmentOpened) {
                khataFragment.addElementsInRecents();
            }
        }
    }

    public void manipulateAllTransactionFragment(boolean toOpen) {
        if (toOpen) {
            isAllTransactionFragmentOpened = true;
            flAllTransaction.setVisibility(View.VISIBLE);
            allTransactionsFragment = AllTransactionsFragment.newInstance("", "");
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).add(flAllTransaction.getId(), allTransactionsFragment).addToBackStack(null).commit();
        } else {
            isAllTransactionFragmentOpened = false;
            flAllTransaction.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().remove(allTransactionsFragment).addToBackStack(null).commit();
        }
    }

    public void manipulateHistoryFragment(boolean toOpen) {
        if (toOpen) {
            flHistoryFragment.setVisibility(View.VISIBLE);
            historyFragment = HistoryFragment.newInstance("", "");
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).add(flHistoryFragment.getId(), historyFragment).addToBackStack(null).commit();
            isHistoryFragmentOpened = true;
        } else {
            if (toRefreshListInHomepage) {
                toRefreshListInHomepage = false;
                transactionAdapter.transactionModelList = transactionModelList;
                transactionAdapter.notifyDataSetChanged();
            }
            getSupportFragmentManager().beginTransaction().remove(historyFragment).addToBackStack(null).commit();
            flHistoryFragment.setVisibility(View.GONE);
            isHistoryFragmentOpened = false;
        }
    }

    public void manipulateLoginFragment(boolean toOpen) {
        if (toOpen) {
            flLoginFragment.setVisibility(View.VISIBLE);
            loginFragment = LoginFragment.newInstance("", "");
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).add(flLoginFragment.getId(), loginFragment).addToBackStack(null).commit();
            isLoginFragmentOpened = true;
        } else {
            getSupportFragmentManager().beginTransaction().remove(loginFragment).addToBackStack(null).commit();
            flLoginFragment.setVisibility(View.GONE);
            isLoginFragmentOpened = false;
        }
    }

    public void manipulateCreateKhataFragment(boolean toOpen, String khataOpening) {
        if (toOpen) {
            createKhataFragment = CreateKhataFragment.newInstance(khataOpening, "");
            flCreateKhataFragment.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
//                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                    .add(flCreateKhataFragment.getId(), createKhataFragment).addToBackStack(null).commit();
            isCreateKhataFragmentOpened = true;
        } else {
            flCreateKhataFragment.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().remove(createKhataFragment).addToBackStack(null).commit();
            isCreateKhataFragmentOpened = false;
        }
    }

    public void manipulateKhataFragment(boolean toOpen, KhataModel khataModel) {
        if (toOpen) {
            flKhataFragment.setVisibility(View.VISIBLE);
            khataFragment = KhataFragment.newInstance(khataModel, "");
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).add(flKhataFragment.getId(), khataFragment).addToBackStack(null).commit();
            isKhataFragmentOpened = true;
        } else {
            flKhataFragment.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().remove(khataFragment).addToBackStack(null).commit();
            isKhataFragmentOpened = false;
        }
    }

    private void addElementsInStatsInfo() {
        StatsticsInfoModel statsticsInfoModel = new StatsticsInfoModel();
        statsticsInfoModel.setBottomLine(1500);
        statsticsInfoModel.setTotalSales(30000);
        statsticsInfoModel.setStartDate(new Timestamp(3000));
        statsticsInfoModel.setEndDate(new Timestamp(10000));
        for (int i = 0; i < 3; i++) {
            statsticsInfoModelList.add(statsticsInfoModel);
        }
    }

//
//    private void addElementsInRecents() {
//        TransactionModel transactionModel1 = new TransactionModel();
//        transactionModel1.setMode("Online");
//        transactionModel1.setTotalAmount(30000);
//
//        transactionModel1.setPaymentType("Debit");
//        transactionModel1.setAmountTransferred(29900 + 1);
//        String w = String.valueOf(Integer.valueOf(1));
//        transactionModel1.setUserName(w);
//        transactionModel1.setBalance(1 + 100);
//
//        salesTransactionModelList.add(transactionModel1);
//        khataTransactionModelList.add(transactionModel1);
//        paymentTransactionModelList.add(transactionModel1);
//        for (int i = 0; i < 1; i++) {
//            TransactionModel transactionModel = new TransactionModel();
//            transactionModel.setMode("Online");
//            transactionModel.setTotalAmount(30000);
//
//            transactionModel.setPaymentType("Debit");
//            transactionModel.setAmountTransferred(29900 + i);
//            String s = String.valueOf(Integer.valueOf(i));
//            transactionModel.setUserName(s);
//            transactionModel.setBalance(i + 100);
//            transactionModelList.add(transactionModel);
//            collectionTransactionModelList.add(transactionModel);
//        }
//    }

    public void initialiseTransactionRecyclerView() {
        transactionAdapter = new TransactionAdapter(transactionModelList, this, Tags.KEY_HOME, new TransactionAdapter.OnButtonClick() {
            @Override
            public void onDeleteTransaction(TransactionModel transactionModel) {
                new Thread(() -> {
                    boolean isTransactionDeleted=false;
                    for (int i = 0; i < transactionModelList.size(); i++) {
                        if (transactionModel.getId() != null
                                && transactionModelList.get(i).getId() != null
                                && transactionModel.getId().equals(transactionModelList.get(i).getId())) {
                            isTransactionDeleted=true;
                            transactionModelList.get(i).setDeleted(true);
                            break;
                        }
                    }
                    // Notify dataset changes on the main thread
                    if (isTransactionDeleted)
                        runOnUiThread(() -> transactionChanged());
                }).start();
            }
//                for (int i=0;i<transactionModelList.size();i++){
//                    if (transactionModel.getId()!=null && transactionModelList.get(i).getId()!=null && transactionModel.getId().equals(transactionModelList.get(i).getId())){
//                        transactionModelList.get(i).setDeleted(true);
//                        break;
//                    }
//                }
//                //delete the date too if doesnot have any transaction in it .
//                //notify dataset changed
//                transactionChanged();
//            }

            @Override
            public void onRestoreTransaction(TransactionModel transactionModel) {

            }
        });
        rvRecents.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvRecents.setAdapter(transactionAdapter);
        Log.d(TAG, "initialiseTransactionRecyclerView: ....");
    }

    public void initialiseStatsInfoRecyclerView() {
        Log.d(TAG, "initialiseStatsInfoRecyclerView: hmStatsInfo.get(\"28 April 24\") : " + hmStatsInfo.get("1 May 24"));
        statsticsInfoAdapter = new StatsticsInfoAdapter(hmStatsInfo, this);
        rvStatsInfo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvStatsInfo.setAdapter(statsticsInfoAdapter);
        try {
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(rvStatsInfo);
        } catch (Exception e) {
            Log.d(TAG, "initialiseStatsInfoRecyclerView: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void changeLanguageManipulateViews(int language) {
        Log.d(TAG, "changeLanguageManipulateViews: changeLanguage : " + language);
        Context context = null;
        Resources resources = null;
        if (language == 0) {
            context = LocaleHelper.setLocale(this, "en");
            resources = context.getResources();
        } else if (language == 1) {
            context = LocaleHelper.setLocale(this, "hi");
            resources = context.getResources();
        }
        if (resources != null) {
            Log.d(TAG, "changeLanguageManipulateViews: changeLanguage : " + resources.getString(R.string.all_transaction));
            txtAllTransaction.setText(resources.getString(R.string.view_all));
            txtLanguage.setText(resources.getString(R.string.current_language));
        }
    }

    public void syncLists() {
        Log.d(TAG, "syncLists: forceBackUp if ");
        addTransactionInList(transactionModelList, transactionModelList, "");

    }

    public void addTransactionInList(List<TransactionModel> transactionList, List<TransactionModel> specifyModelList, String keyAddTransaction) {
        transactionModelList = transactionList;
        if (transactionModelList.size() > 2) {
            transactionAdapter.notifyDataSetChanged();
        } else
            initialiseTransactionRecyclerView();
        if (keyAddTransaction.equals(Tags.KEY_ADD_COLLECTION)) {
            collectionTransactionModelList = specifyModelList;
        } else if (keyAddTransaction.equals(Tags.KEY_ADD_SALES)) {
            salesTransactionModelList = specifyModelList;
        } else if (keyAddTransaction.equals(Tags.KEY_ADD_ENTRY_IN_KHATA)) {
            khataTransactionModelList = specifyModelList;
        } else if (keyAddTransaction.equals(Tags.KEY_ADD_PAYMENTS)) {
            paymentTransactionModelList = specifyModelList;
        }
        saveInLocal(transactionList);
        calcAllStatsInfo();
    }

    private void saveInLocal(List<TransactionModel> transactionList) {
        // Save the list to SharedPreferences
        SharedPreference.saveLists(transactionModelList, collectionTransactionModelList, paymentTransactionModelList, salesTransactionModelList, khataTransactionModelList);
    }

    private void retrieveDataFromLocal() {
        //user session
        SharedPreference.getUserSession();
        spreadsheetId = SharedPreference.spreadSheetId;
        if (spreadsheetId == null || spreadsheetId.equals("") || spreadsheetId.equals("null")) {
            Log.d(TAG, "retrieveDataFromLocal: spreadsheetID GoogleSheetsManager: is " + spreadsheetId);
        } else {
            spreadsheetLink = Tags.PREFIX_SPREADSHEET_LINK + spreadsheetId;
        }
        Log.d(TAG, "retrieveDataFromLocal: spreadsheetLink GoogleSheetsManager: " + spreadsheetLink);
        // Retrieve the list from SharedPreferences
        transactionModelList = SharedPreference.getTransactionList();
        collectionTransactionModelList = SharedPreference.getCollectionList();
        salesTransactionModelList = SharedPreference.getSalesList();
        paymentTransactionModelList = SharedPreference.getPaymentList();
        khataTransactionModelList = SharedPreference.getKhataEntryList();
        newKhataList = SharedPreference.getNewKhataList();

        if (transactionModelList == null) {
            transactionModelList = new ArrayList<>();
        }
        if (collectionTransactionModelList == null) {
            collectionTransactionModelList = new ArrayList<>();
        }
        if (salesTransactionModelList == null) {
            salesTransactionModelList = new ArrayList<>();
        }
        if (paymentTransactionModelList == null) {
            paymentTransactionModelList = new ArrayList<>();
        }
        if (khataTransactionModelList == null) {
            khataTransactionModelList = new ArrayList<>();
        }
        if (newKhataList == null) {
            newKhataList = new ArrayList<>();
        }
        Log.d(TAG, "retrieveDataFromLocal: REFINEKHATA : newKhataList : " + newKhataList.size());

        initialiseTransactionRecyclerView();
    }

    private void saveDataInSheets() {
        // Create a new list to hold objects
        Log.d(TAG, "saveDataInSheets: GoogleSheetsManager entereed... " + hmStatsInfo.get("01 May 24"));
        if (spreadsheetLink.equals("")) {
            // Define your strings
            String[] strings = {"Date", "collection", "initial", "payments", "sales", "khata", "total Sales(Easy)", "profit/loss(Easy)"};

            // Populate the resultList
            List<List<Object>> resultList = new ArrayList<>();
            List<Object> row = new ArrayList<>();
            for (String str : strings) {
                row.add(str);
            }
            resultList.add(row);

            GoogleSheetsManager googleSheetsManager = new GoogleSheetsManager(this);
            GoogleSheetsManager.addListsToGoogleSheets(resultList, true, new GoogleSheetsManager.GoogleSheetsCallback() {
                @Override
                public void onSheetCreated(String sheetLink) {
                    Log.d(TAG, "onSheetCreated: sheet is GoogleSheetsManager : created : ---- " + sheetLink);
                    spreadsheetLink = sheetLink;
                    SharedPreference.saveUserSession(spreadsheetId);
                }

                @Override
                public void onDataCalculated(Map<String, List<Integer>> hmStatsRetrieve) {
                    if (hmStatsRetrieve != null) {
                        Log.d(TAG, "onDataCalculated: GoogleSheetsManager hmStatsRetrieve: " + hmStatsRetrieve.get("02 May 24"));
                    } else {
                        Log.d(TAG, "onDataCalculated: GoogleSheetsManager hmStatsRetrieve is null ");
                    }
                }

                @Override
                public void onSheetCreationFailed(Exception e) {
                    Log.d(TAG, "onSheetCreationFailed: GoogleSheetsManager: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            return;
        }

        // Convert HashMap to a List<Map.Entry<String, List<Integer>>>
        List<Map.Entry<String, List<Integer>>> entryList = new ArrayList<>(hmStatsInfo.entrySet());

        // Sort the list of entries based on the date string in the keys
        Collections.sort(entryList, new Comparator<Map.Entry<String, List<Integer>>>() {
            @Override
            public int compare(Map.Entry<String, List<Integer>> entry1, Map.Entry<String, List<Integer>> entry2) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yy", Locale.ENGLISH);
                try {
                    Date date1 = sdf.parse(entry1.getKey());
                    Date date2 = sdf.parse(entry2.getKey());
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        List<List<Object>> resultList = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : hmStatsInfo.entrySet()) {
            Log.d(TAG, "saveDataInSheets: GoogleSheetsManager enter loop ");
            List<Object> tempList = new ArrayList<>();
            tempList.add(entry.getKey()); // Add key as first element
            tempList.addAll(entry.getValue()); // Add list of integers
            resultList.add(tempList); // Add to result list
        }

        Log.d(TAG, "saveDataInSheets: GoogleSheetsManager entereed... 1 : ");

        GoogleSheetsManager googleSheetsManager = new GoogleSheetsManager(this);
        GoogleSheetsManager.addListsToGoogleSheets(resultList, false, new GoogleSheetsManager.GoogleSheetsCallback() {
            @Override
            public void onSheetCreated(String sheetLink) {
                Log.d(TAG, "onSheetCreated: sheet is GoogleSheetsManager : created : ---- " + sheetLink);
                Toast.makeText(getApplicationContext(), sheetLink, Toast.LENGTH_LONG).show();
                spreadsheetLink = sheetLink;
            }

            @Override
            public void onDataCalculated(Map<String, List<Integer>> hmStatsRetrieve) {
                if (hmStatsRetrieve != null) {
                    Log.d(TAG, "onDataCalculated: GoogleSheetsManager hmStatsRetrieve: " + hmStatsRetrieve.get("02 May 24"));
                } else {
                    Log.d(TAG, "onDataCalculated: GoogleSheetsManager hmStatsRetrieve is null ");
                }
            }

            @Override
            public void onSheetCreationFailed(Exception e) {
                Toast.makeText(getApplicationContext(), "There is some issue creating excel sheet ", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onSheetCreationFailed: GoogleSheetsManager: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void retrieveFromSheet() {
        GoogleSheetsManager googleSheetsManager = new GoogleSheetsManager(this);
        SharedPreference.getUserSession();
        String spreadSheetId1 = SharedPreference.spreadSheetId;

        GoogleSheetsManager.retrieveDataFromGoogleSheets(spreadSheetId1, new GoogleSheetsManager.GoogleSheetsCallback() {
            @Override
            public void onSheetCreated(String sheetLink) {
                String message = "Sheet has been created: " + sheetLink;
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onSheetCreated: sheet is GoogleSheetsManager : created : ---- " + sheetLink);
            }

            @Override
            public void onDataCalculated(Map<String, List<Integer>> hmStatsRetrieve) {
                if (hmStatsRetrieve != null) {
                    Log.d(TAG, "onDataCalculated: GoogleSheetsManager hmStatsRetrieve: " + hmStatsRetrieve.get("02 May 24"));
                    hmStatsInfo = (HashMap<String, List<Integer>>) hmStatsRetrieve;
                    statsticsInfoAdapter.hmStatsInfo = hmStatsInfo;
                    statsticsInfoAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onDataCalculated: GoogleSheetsManager hmStatsRetrieve is null ");
                }
            }

            @Override
            public void onSheetCreationFailed(Exception e) {
                Toast.makeText(getApplicationContext(), "There is some issue creating excel sheet ", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onSheetCreationFailed: GoogleSheetsManager: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void calcAllStatsInfo() {
        Log.d(TAG, "calcAllStatsInfo: entereddd ... ");
        hmStatsInfo = new HashMap<>();
        String dateString = "";
        //all payments of start to end date
        //all collection of start to end date
        //initial
        int transactionListSize = transactionModelList.size();
        for (int i = transactionListSize - 1; i >= 0; i--) {
            TransactionModel transactionModel = transactionModelList.get(i);
            if (transactionModel.isDeleted()) {
                continue;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(Tags.DATE_FORMAT, Locale.getDefault());

            // Convert Timestamp to Date object
            Date date1 = new Date(transactionModel.getDate().getTime());

            // Format the Date object into a string
            dateString = dateFormat.format(date1);

            Log.d(TAG, "calcAllStatsInfo: transactionModel.getKey() : " + transactionModel.getKey() + " dateString : " + dateString);
            if (transactionModel.getKey() == null) {
                continue;
            }
            //extract data
            List<Integer> infoList;
            if (hmStatsInfo.containsKey(dateString)) {
                infoList = hmStatsInfo.get(dateString);
                Log.d(TAG, "calcAllStatsInfo: entered if already present ::: ");
            } else {
                infoList = new ArrayList<>();
                infoList.add(0);
                infoList.add(0);
                infoList.add(0);
                infoList.add(0);
                infoList.add(0);
                Log.d(TAG, "calcAllStatsInfo: entered if not already present ");
            }
            //make changes to the list

            if (transactionModel.getKey().equals(Tags.KEY_ADD_PAYMENTS)) {
                Log.d(TAG, "calcAllStatsInfo:  payment2 " + infoList.get(2));
                Integer payment = infoList.get(2);
                payment += transactionModel.getAmountTransferred();
                infoList.set(2, payment);
            } else if (transactionModel.getKey().equals(Tags.KEY_ADD_COLLECTION)) {
                Log.d(TAG, "calcAllStatsInfo:  collection0 " + infoList.get(0));
                Integer collection = infoList.get(0);
                collection += transactionModel.getAmountTransferred();
                infoList.set(0, collection);
            } else if (transactionModel.getKey().equals(Tags.KEY_ADD_SALES)) {
                Log.d(TAG, "calcAllStatsInfo:  sales3 " + infoList.get(3));
                Integer sales = infoList.get(3);
                sales += transactionModel.getAmountTransferred();
                infoList.set(3, sales);
            } else if (transactionModel.getKey().equals(Tags.KEY_ADD_ENTRY_IN_KHATA)) {
                Log.d(TAG, "calcAllStatsInfo: khata4 " + infoList.get(4));
                Integer entryInKhata = infoList.get(4);
                entryInKhata += transactionModel.getAmountTransferred();
                infoList.set(4, entryInKhata);
            } else if (transactionModel.getKey().equals(Tags.KEY_ADD_INITIAL)) {
                Log.d(TAG, "calcAllStatsInfo: initial1 " + infoList.get(1));
                Integer intial = infoList.get(1);
                intial += transactionModel.getAmountTransferred();
                infoList.set(1, intial);
            }
            //put data in the hashmap
            hmStatsInfo.put(dateString, infoList);
            Log.d(TAG, "calcAllStatsInfo: transactionModel.getDate() : " + transactionModel.getDate() + " info:  " + infoList.get(0) + " : " + infoList.get(1) + " : " + infoList.get(2) + " : " + infoList.get(3) + " : " + infoList.get(4));
//            if (hmStatsInfo.containsKey(transactionModel.getDate())){
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    hmStatsInfo.replace(transactionModel.getDate(),infoList);
//                }
////                else{
////                    hmStatsInfo.put(transactionModel.getDate(),infoList);
////                }
//            }else{
//                hmStatsInfo.put(transactionModel.getDate(),infoList);
//            }

        }
        statsticsInfoAdapter.hmStatsInfo = hmStatsInfo;
        statsticsInfoAdapter.notifyDataSetChanged();
        Log.d(TAG, "calcAllStatsInfo: hmStatsInfo.get(Current) : " + dateString + " : " + hmStatsInfo.get(dateString));

    }

    public void forceBackup() {
        if (transactionModelList.isEmpty()) {
            Toast.makeText(this, "Nothing is there to backup", Toast.LENGTH_SHORT).show();
            return;
        }
        //delete all transactions from db
        deleteAllTransactions();
        //delete all khata from db
        deleteAllKhatas();
        //put all transactions as isbackedUp=false
        postAllIsBacked();
        //syncLists
        syncLists();
        //sync new Khata list
        SharedPreference.savenNewKhataLists(newKhataList);
        //backup again
        backupAllData();
    }

    public void backupAllData() {
        if (transactionModelList.isEmpty()) {
            Toast.makeText(this, "Nothing is there to backup", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "backupAllTransactions: forceBackUp entered if");
        needForceBackedUp = true;
        backupCountTrans = 0;
        backedUpSuccessTrans = 0;
        backupCountKhata = 0;
        backedUpSuccessKhata = 0;
        Log.d(TAG, "backupAllTransactions: transactionModelList.size()" + transactionModelList.size());
        Log.d(TAG, "backupAllTransactions: salesTransactionModelList.size()" + salesTransactionModelList.size());
        Log.d(TAG, "backupAllTransactions: khataTransactionModelList.size()" + khataTransactionModelList.size());
        Log.d(TAG, "backupAllTransactions: collectionTransactionModelList.size()" + collectionTransactionModelList.size());
        Log.d(TAG, "backupAllTransactions: paymentTransactionModelList.size()" + paymentTransactionModelList.size());
        for (int i = transactionModelList.size() - 1; i >= 0; i--) {
            if (!transactionModelList.get(i).isBackedUp() && transactionModelList.get(i).getTransaction()) {
                backupCountTrans++;
                needForceBackedUp = false;
                Log.d(TAG, "backupAllTransactions: which is not backed : " + transactionModelList.get(i).getUserName());
                backupTransactionOnCloud(transactionModelList.get(i), i);
                break;
            }
        }
        for (int i = newKhataList.size() - 1; i >= 0; i--) {
            KhataModel khataModel = newKhataList.get(i);
            khataModel.setUserId(SessionManagement.userId);
            if (!khataModel.isBackedUp()) {
                backupCountKhata++;
                needForceBackedUp = false;
                Log.d(TAG, "backupAllTransactions::: which is not backed : " + khataModel.getKhataSerialNumber());
                backupKhataOnCloud(khataModel, i);
                break;
            }
        }
        //show a dialog box for force backup.
        if (needForceBackedUp) {
            showBackupConfirmationDialog();
        }
    }

    private void backupTransactionOnCloud(TransactionModel transactionModel, int pos) {
        final Integer[] position = {pos};
        if (!transactionModel.getTransaction()) {
            position[0]--;
            if (position[0] < 0) {
                Toast.makeText(getApplicationContext(), "Backed up transaction successfully: " + backedUpSuccessTrans, Toast.LENGTH_SHORT).show();
                syncLists();
                return;
            }
            backupTransactionOnCloud(transactionModelList.get(position[0]), position[0]);
            return;
        }
        RevampRetrofit revampRetrofit = new RevampRetrofit();
        String url = Tags.URL_ADD_TRANSACTION; // Replace with your endpoint
        HashMap<String, Object> data = new HashMap<>();  // Replace with your actual data object
        data.put(Tags.KEY_USERNAME, transactionModel.getUserName());
        data.put(Tags.KEY_NAME, transactionModel.getUserName());
//        data.put(Tags.KEY_PASSWORD, password);
        Log.d(TAG, " getTransaction backupTransactionOnCloud BACKUPLOGICTEST :: " + pos + "  : " + transactionModel.getUserName() + " : " + transactionModel.getDate());
        revampRetrofit.postDataTransaction(url, transactionModel, new ResponseListener() {
            @Override
            public void onSuccess(ResponseBody responseBody) throws IOException, JSONException {
                String responseString = responseBody.string();
                JSONObject jsonObject = new JSONObject(responseString);
                Log.d(TAG, "onSuccess : Response: Transaction is done : " + jsonObject.toString());
                TransactionModel transactionModel1 = TransactionModel.transactionJSONToTransactionModel(jsonObject);
                Log.d(TAG, "onSuccess: backupTransactionOnCloud transaction is : " + transactionModel1.getTransaction());
                backedUpSuccessTrans++;
                Log.d(TAG, "onSuccess: transaction is : pos" + backedUpSuccessTrans + " :: " + backupCountTrans);
                transactionModelList.get(pos).setBackedUp(true);
                position[0]--;
                Log.d(TAG, "onSuccess: BACKUPDONE transactionModelList.size() : " + transactionModelList.size() + " : position[0] : " + position[0]);
                if (position[0] < 0) {
                    Toast.makeText(getApplicationContext(), "Backed up transaction successfully : " + backedUpSuccessTrans, Toast.LENGTH_SHORT).show();
                    syncLists();
                } else {
                    backupTransactionOnCloud(transactionModelList.get(position[0]), position[0]);
                }
//                if (backedUpSuccessTrans == backupCountTrans) {
//                    Toast.makeText(getApplicationContext(), "Backed up successfully", Toast.LENGTH_SHORT).show();
//                    syncLists();
//                }
//                if ()
//                pos++;
//                    backupTransactionOnCloud(transactionModelList.get(pos), pos++);
            }

            @Override
            public void onFailure(ResponseBody responseBody) throws IOException, JSONException {
                Log.e(TAG, "onFailure Failed: " + responseBody);
                Toast.makeText(getApplicationContext(), "Failed to add transaction", Toast.LENGTH_SHORT).show();
//                String responseString = responseBody.string();
//                JSONObject jsonObject = new JSONObject(responseString);
            }

            @Override
            public void onRequestFailed(String message) {
                Log.e(TAG, "onRequestFailed : Request Failure: " + message);
                Toast.makeText(getApplicationContext(), "Failed to add transaction on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void backupKhataOnCloud(KhataModel khataModel, int pos) {
        final Integer[] position = {pos};
        RevampRetrofit revampRetrofit = new RevampRetrofit();

        String url = Tags.URL_ADD_KHATA; // Replace with your endpoint
        Log.d(TAG, "backupKhataOnCloud: BACKUPLOGICTEST :: " + khataModel.getKhataBalance() + " : " + khataModel.getKhataSerialNumber() + " : " + khataModel.getKhataUserIdString() + " : " + khataModel.getKhataUserName() + " : " + khataModel.getKhataUserPhone() + " : " + SessionManagement.userId);
        revampRetrofit.postDataKhata(url, khataModel, new ResponseListener() {
            @Override
            public void onSuccess(ResponseBody responseBody) throws IOException, JSONException {
                String responseString = responseBody.string();
                JSONObject jsonObject = new JSONObject(responseString);
                Log.d(TAG, "onSuccess : Response: Transaction is done : " + jsonObject.toString());
                KhataModel khataModel1 = KhataModel.khataJSONToKhataModel(jsonObject);
                Log.d(TAG, "onSuccess: transaction is : " + khataModel1.getKhataSerialNumber());
                backedUpSuccessKhata++;
                Log.d(TAG, "onSuccess: transaction is : pos" + backedUpSuccessKhata + " :: " + backupCountKhata);
                newKhataList.get(pos).setBackedUp(true);
                position[0]--;
                Log.d(TAG, "onSuccess: BACKUPDONE KHATA newKhataList.size() : " + newKhataList.size() + " : position[0] : " + position[0]);
                if (position[0] < 0) {
                    Toast.makeText(getApplicationContext(), "Backed up khata successfully : " + backedUpSuccessKhata, Toast.LENGTH_SHORT).show();
                    SharedPreference.savenNewKhataLists(newKhataList);
                } else {
                    backupKhataOnCloud(newKhataList.get(position[0]), position[0]);
                }
            }

            @Override
            public void onFailure(ResponseBody responseBody) throws IOException, JSONException {
                Log.e(TAG, "onFailure Failed: " + responseBody);
                Toast.makeText(getApplicationContext(), "Failed to back up khata", Toast.LENGTH_SHORT).show();
//                String responseString = responseBody.string();
//                JSONObject jsonObject = new JSONObject(responseString);
            }

            @Override
            public void onRequestFailed(String message) {
                Log.e(TAG, "onRequestFailed : Request Failure: " + message);
                Toast.makeText(getApplicationContext(), "Failed to back up khata on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void restoreFromCloud() {
        eraseAllData();
        restoreAllTransactions();
        restoreAllKhatas();
        if (newKhataList.size() > 0 || transactionModelList.size() > 0) {
            isDataBackedUp = true;
        }
    }

    private void divideTheList(String key, List<TransactionModel> transactionModelList, List<KhataModel> khataModelList) {
        if (key.equals(Tags.KEY_TRANSACTIONS)) {
//            this.transactionModelList = transactionModelList;
            sortListOut(transactionModelList);
            saveInLocal(transactionModelList);
        } else if (key.equals(Tags.KEY_KHATA)) {
            SharedPreference.savenNewKhataLists(khataModelList);
        }
        calcAllStatsInfo();
    }

    private void sortListOut(List<TransactionModel> transactionList) {
        Log.d(TAG, "sortListOut: Entered checkAnotherTransaction " + transactionList.size());
        transactionModelList = new ArrayList<>();
        paymentTransactionModelList = new ArrayList<>();
        collectionTransactionModelList = new ArrayList<>();
        salesTransactionModelList = new ArrayList<>();
        khataTransactionModelList = new ArrayList<>();
        for (int i = transactionList.size() - 1; i >= 0; i--) {
            TransactionModel transactionModel = transactionList.get(i);
            Log.d(TAG, "sortListOut: sortListOut : GETDATEINSORT() : " + transactionModel.getDate().toString());
            specificTransactionModelsList = null;
            if (transactionModel.getTransaction()) {
                Log.d(TAG, "sortListOut: checkAnotherTransaction: addedIn :: getKey :: " + transactionModel.getKey());
                if (transactionModel.getKey().equals(Tags.KEY_ADD_PAYMENTS)) {
                    specificTransactionModelsList = paymentTransactionModelList;
                } else if (transactionModel.getKey().equals(Tags.KEY_ADD_COLLECTION)) {
                    specificTransactionModelsList = collectionTransactionModelList;
                } else if (transactionModel.getKey().equals(Tags.KEY_ADD_SALES)) {
                    specificTransactionModelsList = salesTransactionModelList;
                } else if (transactionModel.getKey().equals(Tags.KEY_ADD_ENTRY_IN_KHATA)) {
                    specificTransactionModelsList = khataTransactionModelList;
                }
                boolean is0thElementAdded = checkAnotherTransaction(transactionModel);
                if (specificTransactionModelsList != null) {
                    specificTransactionModelsList.add(1, transactionModel);
                }
            }
        }
        for (int i = transactionList.size() - 1; i >= 0; i--) {
            TransactionModel transactionModel = transactionList.get(i);
            Log.d(TAG, "sortListOut: sortListOut : GETDATEINSORT() : " + transactionModel.getDate().toString());
            specificTransactionModelsList = null;
            if (transactionModel.getTransaction()) {
                Log.d(TAG, "sortListOut: checkAnotherTransaction: addedIn :: getKey :: " + transactionModel.getKey());
                specificTransactionModelsList = transactionModelList;
                boolean is0thElementAdded = checkAnotherTransaction(transactionModel);
                if (specificTransactionModelsList != null) {
                    specificTransactionModelsList.add(1, transactionModel);
                }
            }
        }
    }

    private void restoreAllTransactions() {
        Log.d(TAG, "deleteAllTransactions: restoreFromCloud restoreAllKhatas entered forceRestore");
        RevampRetrofit revampRetrofit = new RevampRetrofit();
        String url = Tags.URL_GET_TRANSACTIONS; // Replace with your endpoint
        HashMap<String, Object> data = new HashMap<>();  // Replace with your actual data object
        data.put(Tags.KEY_USERID, SessionManagement.userId);
        revampRetrofit.getData(url, data, new ResponseListener() {
            @Override
            public void onSuccess(ResponseBody responseBody) throws IOException, JSONException {
                String responseString = responseBody.string();
                JSONArray transactionArray = new JSONArray(responseString);
                Log.d(TAG, "onSuccess : Response: Transaction is done : " + transactionArray.toString());
                List<TransactionModel> transactionModelList1 = TransactionModel.transactionResponseToTransactionModelList(transactionArray);
                Log.d(TAG, "onSuccess: restoreFromCloud transactionModelList1.size() " + transactionModelList1.size());
                if (transactionModelList1.size() > 0) {
                    Log.d(TAG, "onSuccess: GETDATEINSORT :: " + transactionModelList1.get(0).getDate());
                }
                Collections.reverse(transactionModelList1);
//                transactionModelList = transactionModelList1;
                divideTheList(Tags.KEY_TRANSACTIONS, transactionModelList1, new ArrayList<>());
                alreadyBackedUp(transactionModelList);
                initialiseTransactionRecyclerView();
            }

            @Override
            public void onFailure(ResponseBody responseBody) throws IOException, JSONException {
                Log.e(TAG, "onFailure restoreFromCloud Failed: " + responseBody);
                Toast.makeText(getApplicationContext(), "Unable to restore transactions", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestFailed(String message) {
                Log.e(TAG, "onRequestFailed : restoreFromCloud Request Failure: " + message);
                Toast.makeText(getApplicationContext(), "Unable to restore transactions on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void alreadyBackedUp(List<TransactionModel> transactionModelList) {
        for (int i = 0; i < transactionModelList.size(); i++) {
            TransactionModel transactionModel = transactionModelList.get(i);
            transactionModel.setBackedUp(true);
        }
    }

    private void restoreAllKhatas() {
        Log.d(TAG, "deleteAllTransactions: restoreFromCloud restoreAllKhatas entered forceRestore");
        RevampRetrofit revampRetrofit = new RevampRetrofit();
        String url = Tags.URL_GET_ALL_KHATA; // Replace with your endpoint
        HashMap<String, Object> data = new HashMap<>();  // Replace with your actual data object
        data.put(Tags.KEY_USERID, SessionManagement.userId);
        revampRetrofit.getData(url, data, new ResponseListener() {
            @Override
            public void onSuccess(ResponseBody responseBody) throws IOException, JSONException {
                String responseString = responseBody.string();
                JSONArray khataArray = new JSONArray(responseString);
                Log.d(TAG, "onSuccess : Response: Transaction is done : " + khataArray.toString());
                List<KhataModel> khataModelList1 = KhataModel.khataResponseToKhataModelList(khataArray);
                Log.d(TAG, "onSuccess: restoreFromCloud khataModelList1.size() " + khataModelList1.size());
                Collections.reverse(khataModelList1);
                newKhataList = khataModelList1;
                divideTheList(Tags.KEY_KHATA, new ArrayList<>(), khataModelList1);
            }

            @Override
            public void onFailure(ResponseBody responseBody) throws IOException, JSONException {
                Log.e(TAG, "onFailure Failed: restoreFromCloud : : " + responseBody);
                Toast.makeText(getApplicationContext(), "Unable to restore Khatas", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestFailed(String message) {
                Log.e(TAG, "onRequestFailed : Request Failure: restoreFromCloud :: " + message);
                Toast.makeText(getApplicationContext(), "Unable to restore Khatas on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAllKhatas() {
        Log.d(TAG, "deleteAllTransactions: entered forceBackUp");
        RevampRetrofit revampRetrofit = new RevampRetrofit();
        String url = Tags.URL_DELETE_KHATAS; // Replace with your endpoint
        HashMap<String, Object> data = new HashMap<>();  // Replace with your actual data object
        data.put(Tags.KEY_USERID, SessionManagement.userId);
        revampRetrofit.deleteData(url, data, new ResponseListener() {
            @Override
            public void onSuccess(ResponseBody responseBody) throws IOException, JSONException {
                String responseString = responseBody.string();
                JSONObject jsonObject = new JSONObject(responseString);
                Log.d(TAG, "onSuccess : Response: Transaction is done : " + jsonObject.toString());
            }

            @Override
            public void onFailure(ResponseBody responseBody) throws IOException, JSONException {
                Log.e(TAG, "onFailure Failed: " + responseBody);
                Toast.makeText(getApplicationContext(), "Unable to delete transactions", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestFailed(String message) {
                Log.e(TAG, "onRequestFailed : Request Failure: " + message);
                Toast.makeText(getApplicationContext(), "Unable to delete transactions on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAllTransactions() {
        Log.d(TAG, "deleteAllTransactions: entered forceBackUp");
        RevampRetrofit revampRetrofit = new RevampRetrofit();
        String url = Tags.URL_DELETE_TRANSACTIONS; // Replace with your endpoint
        HashMap<String, Object> data = new HashMap<>();  // Replace with your actual data object
        data.put(Tags.KEY_USERID, SessionManagement.userId);
        revampRetrofit.deleteData(url, data, new ResponseListener() {
            @Override
            public void onSuccess(ResponseBody responseBody) throws IOException, JSONException {
                String responseString = responseBody.string();
                JSONObject jsonObject = new JSONObject(responseString);
                Log.d(TAG, "onSuccess : Response: Transaction is done : " + jsonObject.toString());
            }

            @Override
            public void onFailure(ResponseBody responseBody) throws IOException, JSONException {
                Log.e(TAG, "onFailure Failed: " + responseBody);
                Toast.makeText(getApplicationContext(), "Unable to delete transactions", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestFailed(String message) {
                Log.e(TAG, "onRequestFailed : Request Failure: " + message);
                Toast.makeText(getApplicationContext(), "Unable to delete transactions on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postAllIsBacked() {
        Log.d(TAG, "postAllIsBacked: forceBackUp entered");
        for (int i = 0; i < transactionModelList.size(); i++) {
//            if (transactionModelList.get(i).getUserId()==null){
            transactionModelList.get(i).setUserId(SessionManagement.userId);
//            }
            transactionModelList.get(i).setBackedUp(false);
        }
        for (int i = 0; i < newKhataList.size(); i++) {
//            if (transactionModelList.get(i).getUserId()==null){
            newKhataList.get(i).setUserId(SessionManagement.userId);
//            }
            newKhataList.get(i).setBackedUp(false);
        }
    }

    // Method to show confirmation dialog
    public void showBackupConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to do a force back up?");

        // "Yes" button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the "Yes" action
                forceBackup();
            }
        });

        // "No, cancel" button
        builder.setNegativeButton("No, cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(this.getColor(R.color.increasing_green)); // Red color for "Yes"

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(this.getColor(R.color.decreasing_red)); // Blue color for "No, cancel"

    }

    public boolean checkAnotherTransaction(TransactionModel specificTransaction) {
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
            Log.d(TAG, "checkAnotherTransaction 1 a): " + specificTransactionModelsList.get(0).getDate().toString());
            TransactionModel specificTransactionModel = specificTransactionModelsList.get(0);
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
        Log.d(TAG, "checkAnotherTransaction COMPARINGDATES 3 a) : currentDateString" + currentdateString + " latestEntrySpecificDateString : " + latestEntrySpecificDateString);

        if ((specificTransactionModelsList != null && !specificTransactionModelsList.isEmpty()) && currentdateString.equals(latestEntrySpecificDateString)) { //&& !specificTransactionModelsList.get(0).getTransaction()
            //then do nothing
            Log.d(TAG, "checkAnotherTransaction: FALSEEEE addedIn which list : " + specificTransaction.getUserName());
            return false;
        } else {
            Log.d(TAG, "checkAnotherTransaction: TRUEEE addedIn which list : " + specificTransaction.getUserName());
            //if not then make new entry of date sequence at 0th element.
            if (specificTransactionModelsList == null) {
                specificTransactionModelsList = new ArrayList<>();
            }
            specificTransactionModelsList.add(0, transactionModel);
            Log.d(TAG, "checkAnotherTransaction: is added in transaction and specific Transaction ... specificTransactionModelList: " + specificTransactionModelsList.size());
            return true;
        }
//        Log.d(TAG, "checkAnotherTransaction 3 b ) : currentDateString" + currentdateString + " latestEntryDateString : " + latestEntryDateString);
    }

    public void transactionChanged() {
        initialiseTransactionRecyclerView();
        calcAllStatsInfo();
        initialiseStatsInfoRecyclerView();
        syncLists();
    }
}
