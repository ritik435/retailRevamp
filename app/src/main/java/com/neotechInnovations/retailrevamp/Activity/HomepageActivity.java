package com.neotechInnovations.retailrevamp.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import com.neotechInnovations.retailrevamp.Adapter.StatsticsInfoAdapter;
import com.neotechInnovations.retailrevamp.Adapter.TransactionAdapter;
import com.neotechInnovations.retailrevamp.Constant.Tags;
import com.neotechInnovations.retailrevamp.Fragment.AddTransactionFragment;
import com.neotechInnovations.retailrevamp.Fragment.AllTransactionsFragment;
import com.neotechInnovations.retailrevamp.Fragment.CreateKhataFragment;
import com.neotechInnovations.retailrevamp.Fragment.KhataFragment;
import com.neotechInnovations.retailrevamp.Fragment.LoginFragment;
import com.neotechInnovations.retailrevamp.Model.KhataModel;
import com.neotechInnovations.retailrevamp.Model.StatsticsInfoModel;
import com.neotechInnovations.retailrevamp.Model.TransactionModel;
import com.neotechInnovations.retailrevamp.R;
import com.neotechInnovations.retailrevamp.Utils.GoogleSheetsManager;
import com.neotechInnovations.retailrevamp.Utils.LocaleHelper;
import com.neotechInnovations.retailrevamp.Utils.SharedPreference;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

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
    FrameLayout flAddTransaction, flAddTransactionBg, flAllTransaction, flLoginFragment, flCreateKhataFragment, flKhataFragment;
    DrawerLayout drawerLayout;
    NavigationView nvSidebarMenu;
    LinearLayout llSidebarBg;
    MaterialToolbar topAppBar;
    private static boolean isAddTransactionFragmentOpened, isAllTransactionFragmentOpened, isCreateKhataFragmentOpened, isLoginFragmentOpened, isKhataFragmentOpened;
    AddTransactionFragment addTransactionFragment;
    AllTransactionsFragment allTransactionsFragment;
    CreateKhataFragment createKhataFragment;
    KhataFragment khataFragment;
    LoginFragment loginFragment;
    ImageView ivMenu;
    BottomSheetBehavior addTransactionBottomSheetBehavior;
    List<StatsticsInfoModel> statsticsInfoModelList = new ArrayList<>();
    private static boolean isProMode, isEasyMode;
    HashMap<String, List<Integer>> hmStatsInfo = new HashMap<>();
    public static List<TransactionModel> transactionModelList = new ArrayList<>();
    public static List<TransactionModel> salesTransactionModelList = new ArrayList<>();
    public static List<TransactionModel> collectionTransactionModelList = new ArrayList<>();
    public static List<TransactionModel> paymentTransactionModelList = new ArrayList<>();
    public static List<TransactionModel> khataTransactionModelList = new ArrayList<>();
    public static List<KhataModel> newKhataList = new ArrayList<>();
    public static List<String> suggestedKhataList=new ArrayList<>();
    StatsticsInfoAdapter statsticsInfoAdapter;
    TransactionAdapter transactionAdapter;
    Integer language;

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

        nvSidebarMenu.setVisibility(View.VISIBLE);
        drawerLayout.closeDrawer(GravityCompat.START);
        llSidebarBg.setVisibility(View.GONE);


        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                Log.d(TAG, "onDrawerSlide: " + slideOffset);
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
            private boolean inArea=false;
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    inArea=true;
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
                }else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
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
                    inArea=true;
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
                        manipulateAddTransactionFragment("", false,"");
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
                    inArea=true;
                    txtAllTransaction.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        txtAllTransaction.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAllTransactionFragment : to call ");
                        manipulateAllTransactionFragment(true);
                    }
                }else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
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
                        manipulateAddTransactionFragment(Tags.KEY_ADD_COLLECTION, true,"");
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
                    inArea=true;
                    cvAddPayments.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        cvAddPayments.setAlpha(1f);
                        Log.d(TAG, "onTouch: manipulateAddTransactionFragment : to call ");
                        manipulateAddTransactionFragment(Tags.KEY_ADD_PAYMENTS, true,"");
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
                    inArea=true;
                    cvAddSales.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        cvAddSales.setAlpha(1f);
                        manipulateAddTransactionFragment(Tags.KEY_ADD_SALES, true,"");
                    }
                }else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
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
                    inArea=true;
                    cvAddEntryInKhata.setAlpha(0.5f);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (inArea) {
                        cvAddEntryInKhata.setAlpha(1f);
                        manipulateAddTransactionFragment(Tags.KEY_ADD_ENTRY_IN_KHATA, true,"");
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
    }

    private void refineKhataIds() {
        Log.d(TAG, "refineKhataIds: REFINEKHATA : newKhataList : "+newKhataList.size());
        for (int i=0;i<newKhataList.size();i++){
            KhataModel khataModel=newKhataList.get(i);
            if (khataModel.getKhataUserIdString()==null){
                String khatauserIdString="";
                khatauserIdString+="(#";
                khatauserIdString+=khataModel.getKhataSerialNumber();
                khatauserIdString+=") ";
                khatauserIdString+=khataModel.getKhataUserName();
                newKhataList.get(i).setKhataUserIdString(khatauserIdString);
                suggestedKhataList.add(khatauserIdString);
            }else {
                suggestedKhataList.add(khataModel.getKhataUserIdString());
            }
        }
        Log.d(TAG, "refineKhataIds: REFINEKHATA : suggestedKhataList : "+suggestedKhataList.size());
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
                manipulateLoginFragment(true);
            } else if (item.getItemId() == R.id.logout) {
                Log.d(TAG, "NavClick: 3");
                manipulateLoginFragment(true);
            } else if (item.getItemId() == R.id.create_khata) {
                Log.d(TAG, "NavClick: 4");
                manipulateCreateKhataFragment(true,Tags.KEY_CREATE_KHATA);
            } else if (item.getItemId() == R.id.view_all_khata) {
                Log.d(TAG, "NavClick: 4a");
                manipulateCreateKhataFragment(true,Tags.KEY_VIEW_KHATA);
            } else if (item.getItemId() == R.id.erase_all_data) {
                Log.d(TAG, "NavClick: 5");
                SharedPreference.clearLists();
                hmStatsInfo.clear();
                statsticsInfoAdapter.hmStatsInfo = hmStatsInfo;
                statsticsInfoAdapter.notifyDataSetChanged();
                retrieveDataFromLocal();
            } else if (item.getItemId() == R.id.transfer_all_data) {
                retrieveFromSheet();
            }
            nvSidebarMenu.setVisibility(View.GONE);
            drawerLayout.closeDrawer(GravityCompat.START);
            llSidebarBg.setVisibility(View.GONE);
            return false;
        });
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
            manipulateAddTransactionFragment("", false,"");
        } else if (isAllTransactionFragmentOpened) {
            manipulateAllTransactionFragment(false);
        } else if (isKhataFragmentOpened) {
            manipulateKhataFragment(false,null);
        } else if (isCreateKhataFragmentOpened) {
            manipulateCreateKhataFragment(false,Tags.KEY_VIEW_KHATA);
        } else if (isLoginFragmentOpened) {
            manipulateLoginFragment(false);
        }
    }

    public void manipulateAddTransactionFragment(String keyAddTransaction, boolean toOpen , String entryKhataName) {
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
            if (isKhataFragmentOpened){
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

    public void manipulateCreateKhataFragment(boolean toOpen , String khataOpening) {
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

    public void manipulateKhataFragment(boolean toOpen,KhataModel khataModel) {
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
        transactionAdapter = new TransactionAdapter(transactionModelList, this);
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
        saveInLocal(transactionList, specifyModelList, keyAddTransaction);
        calcAllStatsInfo();
    }

    private void saveInLocal(List<TransactionModel> transactionList, List<TransactionModel> specifyModelList, String keyAddTransaction) {
        // Save the list to SharedPreferences
        SharedPreference.saveLists(transactionList, collectionTransactionModelList, paymentTransactionModelList, salesTransactionModelList, khataTransactionModelList);
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
        Log.d(TAG, "retrieveDataFromLocal: REFINEKHATA : newKhataList : "+newKhataList.size());

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
//    private void getDataFromAPI() {
//
//        // creating a string variable for URL.
//        String url = "https://spreadsheets.google.com/feeds/list/1AOOaz-5PhVgIvfROammZsdUs92PdYhEUgGoDrYlGGhc/od6/public/values?alt=json";
//        // creating a new variable for our request queue
//        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//
//        // creating a variable for our JSON object request and passing our URL to it.
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                loadingPB.setVisibility(View.GONE);
//                try {
//                    JSONObject feedObj = response.getJSONObject("feed");
//                    JSONArray entryArray = feedObj.getJSONArray("entry");
//                    for(int i=0; i<entryArray.length(); i++){
//                        JSONObject entryObj = entryArray.getJSONObject(i);
//                        String firstName = entryObj.getJSONObject("gsx$firstname").getString("$t");
//                        String lastName = entryObj.getJSONObject("gsx$lastname").getString("$t");
//                        String email = entryObj.getJSONObject("gsx$email").getString("$t");
//                        String avatar = entryObj.getJSONObject("gsx$avatar").getString("$t");
//                        userModalArrayList.add(new UserModal(firstName, lastName, email, avatar));
//
//                        // passing array list to our adapter class.
//                        userRVAdapter = new UserRVAdapter(userModalArrayList, MainActivity.this);
//
//                        // setting layout manager to our recycler view.
//                        userRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//
//                        // setting adapter to our recycler view.
//                        userRV.setAdapter(userRVAdapter);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // handling on error listener method.
//                Toast.makeText(HomepageActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
//            }
//        });
//        // calling a request queue method
//        // and passing our json object
//        queue.add(jsonObjectRequest);
//    }
}
