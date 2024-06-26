package com.neotechInnovations.retailrevamp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neotechInnovations.retailrevamp.Activity.HomepageActivity;
import com.neotechInnovations.retailrevamp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SideBarMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SideBarMenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SideBarMenuFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    HomepageActivity activity;
//    LinearLayout llCreateKhata,llViewAllTransaction,llBackup,llLastBackup,llSettings,llRefer;

    public SideBarMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SideBarMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SideBarMenuFragment newInstance(String param1, String param2) {
        SideBarMenuFragment fragment = new SideBarMenuFragment();
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
        View view= inflater.inflate(R.layout.fragment_side_bar_menu, container, false);
        initialiseViews(view);
//        manipulateViews();
        return view;
    }
    private void initialiseViews(View view) {
//        llCreateKhata=view.findViewById(R.id.ll_create_khata);
//        llBackup=view.findViewById(R.id.ll_back_up);
//        llLastBackup=view.findViewById(R.id.ll_last_back_up);
//        llSettings=view.findViewById(R.id.ll_settings);
//        llRefer=view.findViewById(R.id.ll_refer);
//        llViewAllTransaction=view.findViewById(R.id.ll_view_all_transaction);

    }

//    @SuppressLint("ClickableViewAccessibility")
//    private void manipulateViews() {
//        llCreateKhata.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d(TAG, "onTouch: llCreateKhata:: "+motionEvent.getAction());
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                }
//                return true;
//            }
//        });
//        llSettings.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d(TAG, "onTouch: llSettings:: "+motionEvent.getAction());
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                }
//                return true;
//            }
//        });
//        llRefer.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d(TAG, "onTouch: llRefer:: "+motionEvent.getAction());
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                }
//                return true;
//            }
//        });
//        llBackup.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d(TAG, "onTouch: llBackup:: "+motionEvent.getAction());
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                }
//                return true;
//            }
//        });
//        llLastBackup.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d(TAG, "onTouch: llLastBackup:: "+motionEvent.getAction());
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                }
//                return true;
//            }
//        });
//        llViewAllTransaction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                llViewAllTransaction.setBackground(ContextCompat.getDrawable(activity, R.color.black_10));
//            }
//        });
////        llViewAllTransaction.setOnTouchListener(new View.OnTouchListener() {
////            @Override
////            public boolean onTouch(View view, MotionEvent motionEvent) {
////                Log.d(TAG, "onTouch: llViewAllTransaction:: "+motionEvent.getAction());
////                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
////                    llViewAllTransaction.setBackground(ContextCompat.getDrawable(activity, R.color.black_10));
////                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
////                    llViewAllTransaction.setBackground(ContextCompat.getDrawable(activity, R.color.transparent));
////                }
////                return false;
////            }
////        });
//    }
}