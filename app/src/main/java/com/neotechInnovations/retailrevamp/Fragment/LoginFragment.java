package com.neotechInnovations.retailrevamp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.neotechInnovations.retailrevamp.Activity.HomepageActivity;
import com.neotechInnovations.retailrevamp.Constant.Tags;
import com.neotechInnovations.retailrevamp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout llLoginContainer,llSignUpContainer;
    TextView txtLogin, txtSignUp;
    EditText etLoginUserName,etLoginUserPassword, etSignUpUserName,etSignUpUserEmail, etSignUpUserPassword ,etSignUpUserConfirmPassword;
    Button btnLogin,btnSignUp;
    HomepageActivity activity;
    ImageView ivBackBtn;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        activity=(HomepageActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        initialiseViews(view);
        manipulateViews();
        return view;
    }

    private void initialiseViews(View view) {
        btnLogin=view.findViewById(R.id.btn_login);
        btnSignUp=view.findViewById(R.id.btn_signup);
        etLoginUserName=view.findViewById(R.id.et_login_user_name);
        etLoginUserPassword=view.findViewById(R.id.et_login_user_password);
        etSignUpUserName=view.findViewById(R.id.et_signup_name);
        etSignUpUserEmail=view.findViewById(R.id.et_signup_email);
        etSignUpUserPassword=view.findViewById(R.id.et_signup_user_password);
        etSignUpUserConfirmPassword=view.findViewById(R.id.et_signup_user_confirm_password);
        txtLogin=view.findViewById(R.id.txt_login_in_signup);
        txtSignUp=view.findViewById(R.id.txt_signup_in_login);
        llLoginContainer=view.findViewById(R.id.ll_login_container);
        llSignUpContainer=view.findViewById(R.id.ll_signup_container);
        ivBackBtn=view.findViewById(R.id.iv_back_btn);
    }

    private void manipulateViews() {
        manipulateContainers(Tags.STRING_LOGIN_CONTAINER);

        ivBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomepageActivity)activity).onBackPressed();
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manipulateContainers(Tags.STRING_LOGIN_CONTAINER);
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manipulateContainers(Tags.STRING_SIGNUP_CONTAINER);
            }
        });
    }
    private void manipulateContainers(String containerName){
        if(containerName.equals(Tags.STRING_LOGIN_CONTAINER)){
            llSignUpContainer.setVisibility(View.GONE);
            llLoginContainer.setVisibility(View.VISIBLE);
//            txtLogin.setBackgroundColor(getResources().getColor(R.color.black));
//            txtLogin.setTextColor(getResources().getColor(R.color.white));
//            txtSignUp.setBackgroundColor(getResources().getColor(R.color.white));
//            txtSignUp.setTextColor(getResources().getColor(R.color.black));
        }
        else if(containerName.equals(Tags.STRING_SIGNUP_CONTAINER)){

            llSignUpContainer.setVisibility(View.VISIBLE);
            llLoginContainer.setVisibility(View.GONE);
//            txtLogin.setBackgroundColor(getResources().getColor(R.color.white));
//            txtLogin.setTextColor(getResources().getColor(R.color.black));
//            txtSignUp.setBackgroundColor(getResources().getColor(R.color.black));
//            txtSignUp.setTextColor(getResources().getColor(R.color.white));
        }
    }
}