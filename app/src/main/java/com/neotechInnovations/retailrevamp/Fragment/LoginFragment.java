package com.neotechInnovations.retailrevamp.Fragment;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.neotechInnovations.retailrevamp.API.ResponseListener;
import com.neotechInnovations.retailrevamp.API.RevampRetrofit;
import com.neotechInnovations.retailrevamp.Activity.HomepageActivity;
import com.neotechInnovations.retailrevamp.Constant.Tags;
import com.neotechInnovations.retailrevamp.Model.UserModel;
import com.neotechInnovations.retailrevamp.R;
import com.neotechInnovations.retailrevamp.Utils.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;

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
    private static final String TAG = "LoginFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout llLoginContainer, llSignUpContainer;
    TextView txtLogin, txtSignUp;
    EditText etLoginUserName, etLoginUserPassword, etSignUpUserName, etSignUpUserEmail, etSignUpUserPassword, etSignUpUserConfirmPassword, etSignUpBusinessName;
    Button btnLogin, btnSignUp;
    ProgressBar pbSignup, pbLogin;
    HomepageActivity activity;
    ImageView ivBackBtn,ivBackBtn2, ivShowPassword, ivHidePassword, ivSignupShowPassword, ivSignupHidePassword;
    ScrollView svNotLoggedIn;
    LinearLayout llLoggedIn;
    TextView txtUserName, txtIntials;

    private boolean isPasswordVisible = true, isSignupPasswordVisible = true;
    Animation shakeAnimation;

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
        activity = (HomepageActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initialiseViews(view);
        manipulateViews();
        return view;
    }

    private void initialiseViews(View view) {
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        btnLogin = view.findViewById(R.id.btn_login);
        btnSignUp = view.findViewById(R.id.btn_signup);
        pbSignup = view.findViewById(R.id.pb_signup);
        pbLogin = view.findViewById(R.id.pb_login);
        etLoginUserName = view.findViewById(R.id.et_login_user_name);
        etLoginUserPassword = view.findViewById(R.id.et_login_user_password);
        etSignUpUserName = view.findViewById(R.id.et_signup_name);
        etSignUpUserEmail = view.findViewById(R.id.et_signup_email);
        etSignUpUserPassword = view.findViewById(R.id.et_signup_user_password);
        etSignUpUserConfirmPassword = view.findViewById(R.id.et_signup_user_confirm_password);
        etSignUpBusinessName = view.findViewById(R.id.et_signup_shop_name);
        txtLogin = view.findViewById(R.id.txt_login_in_signup);
        txtSignUp = view.findViewById(R.id.txt_signup_in_login);
        llLoginContainer = view.findViewById(R.id.ll_login_container);
        llSignUpContainer = view.findViewById(R.id.ll_signup_container);
        ivBackBtn = view.findViewById(R.id.iv_back_btn);
        ivBackBtn2 = view.findViewById(R.id.iv_back_btn_2);
        ivShowPassword = view.findViewById(R.id.iv_show_password);
        ivHidePassword = view.findViewById(R.id.iv_hide_password);
        ivSignupShowPassword = view.findViewById(R.id.iv_show_confirm_password_signup);
        ivSignupHidePassword = view.findViewById(R.id.iv_hide_confirm_password_signup);
        svNotLoggedIn = view.findViewById(R.id.sv_not_logged_in);
        llLoggedIn = view.findViewById(R.id.ll_logged_in);
        txtUserName = view.findViewById(R.id.txt_user_name);
        txtIntials = view.findViewById(R.id.txt_intials);
    }

    private void manipulateViews() {
        loggedInSuccessfully();
        manipulateContainers(Tags.STRING_LOGIN_CONTAINER);
        togglePasswordVisibility(Tags.STRING_LOGIN_CONTAINER);
        togglePasswordVisibility(Tags.STRING_SIGNUP_CONTAINER);
        //signUp logic
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call api
                signupUser();
            }
        });
        ivSignupShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility(Tags.STRING_SIGNUP_CONTAINER);
            }
        });
        ivSignupHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility(Tags.STRING_SIGNUP_CONTAINER);
            }
        });

        //login logic
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call api
                loginUser();
            }
        });
        ivShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility(Tags.STRING_LOGIN_CONTAINER);
            }
        });
        ivHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility(Tags.STRING_LOGIN_CONTAINER);
            }
        });
        ivBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomepageActivity) activity).onBackPressed();
            }
        });
        ivBackBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomepageActivity) activity).onBackPressed();
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

    private void loginUser() {
        String userName = etLoginUserName.getText().toString();
        String password = etLoginUserPassword.getText().toString();
        //check if email or password is empty or not
        if (userName.isEmpty()) {
            etLoginUserName.startAnimation(shakeAnimation);
            etLoginUserName.setHintTextColor(getResources().getColor(R.color.decreasing_red));
            return;
        }
        if (password.isEmpty()) {
            etLoginUserPassword.startAnimation(shakeAnimation);
            etLoginUserPassword.setHintTextColor(getResources().getColor(R.color.decreasing_red));
            return;
        }
        //call the api and check if it exist
        //if exist then login -> check password ,userName
        //if doesnot exist then send to signUp page

        //call the api and check if it exist
        //if exist then throw to login & say it already exist
        //if doesnot exist then create a user and login & say succesfully created account.

        pbLogin.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);
        RevampRetrofit revampRetrofit = new RevampRetrofit();

        String url = Tags.URL_LOGIN; // Replace with your endpoint
        HashMap<String, Object> data = new HashMap<>();  // Replace with your actual data object
        data.put(Tags.KEY_NAME, userName);
        data.put(Tags.KEY_PASSWORD, password);
        Log.d(TAG, " Loginuser userName :: " + userName);
        revampRetrofit.getData(url, data, new ResponseListener() {
            @Override
            public void onSuccess(ResponseBody responseBody) throws IOException {
                Log.d(TAG, " Loginuser Response: " + responseBody);
                UserModel userModel = UserModel.userResponseToUserModel(responseBody);
                Log.d(TAG, "onSuccess: Loginuser userModel : " + userModel.getName());
                SessionManagement sessionManagement = new SessionManagement(activity);
                SessionManagement.saveSession(userModel.getId(), userModel.getName(), userModel.getName());
                sessionManagement.getSession();
                Log.d(TAG, "onSuccess: Loginuser Successfully logged In : " + SessionManagement.userName);
                Toast.makeText(activity, "Welcome " + SessionManagement.userName, Toast.LENGTH_SHORT).show();
                loggedInSuccessfully();
                clearForm();

                pbLogin.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                ((HomepageActivity) activity).loggedIn();
                ((HomepageActivity) activity).restoreFromCloud();
            }

            @Override
            public void onFailure(ResponseBody responseBody) throws IOException {
                Log.e(TAG, "Failed: Loginuser " + responseBody);
                Toast.makeText(activity, responseBody.string(), Toast.LENGTH_SHORT).show();
                pbLogin.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestFailed(String message) {
                Log.e(TAG, "Request Failure: Loginuser " + message);
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                pbLogin.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
            }
        });


    }

    private void signupUser() {
        //check if email or password or name or business name is empty or not
        String userName = etSignUpUserName.getText().toString();
        String email = etSignUpUserEmail.getText().toString();
        String password = etSignUpUserPassword.getText().toString();
        String confirmPassword = etSignUpUserConfirmPassword.getText().toString();
        String businessName = etSignUpBusinessName.getText().toString();
        //check if email or password is empty or not
        if (userName.isEmpty()) {
            etSignUpUserName.startAnimation(shakeAnimation);
            etSignUpUserName.setHintTextColor(getResources().getColor(R.color.decreasing_red));
            return;
        }
        if (businessName.isEmpty()) {
            etSignUpBusinessName.startAnimation(shakeAnimation);
            etSignUpBusinessName.setHintTextColor(getResources().getColor(R.color.decreasing_red));
            return;
        }
        if (email.isEmpty()) {
            etSignUpUserEmail.startAnimation(shakeAnimation);
            etSignUpUserEmail.setHintTextColor(getResources().getColor(R.color.decreasing_red));
            return;
        }
        if (password.isEmpty()) {
            etSignUpUserPassword.startAnimation(shakeAnimation);
            etSignUpUserPassword.setHintTextColor(getResources().getColor(R.color.decreasing_red));
            return;
        }
        if (confirmPassword.isEmpty()) {
            etSignUpUserConfirmPassword.startAnimation(shakeAnimation);
            etSignUpUserConfirmPassword.setHintTextColor(getResources().getColor(R.color.decreasing_red));
            return;
        }
        if (!password.equals(confirmPassword)) {
            //password doesnot match
            Toast.makeText(getContext(), "Password doesnot match", Toast.LENGTH_SHORT).show();
        }
        //call the api and check if it exist
        //if exist then throw to login & say it already exist
        //if doesnot exist then create a user and login & say succesfully created account.
        pbSignup.setVisibility(View.VISIBLE);
        btnSignUp.setVisibility(View.GONE);
        RevampRetrofit revampRetrofit = new RevampRetrofit();

        String url = Tags.URL_SIGNUP;// Replace with your endpoint
        HashMap<String, Object> data = new HashMap<>();  // Replace with your actual data object
        data.put("name", userName);
        data.put("email", email);
        data.put("businessName", businessName);
        data.put("password", password);
        data.put("phoneNumber", userName);
        revampRetrofit.postData(url, data, new ResponseListener() {
            @Override
            public void onSuccess(ResponseBody responseBody) throws IOException, JSONException {
                String responseString = responseBody.string();
                JSONObject jsonObject = new JSONObject(responseString);
                Log.d(TAG, "Response: " + responseBody.string());
                UserModel userModel = UserModel.userJSONToUserModel(jsonObject);
                Log.d(TAG, "onSuccess: signupUser userModel : " + userModel.getName());
                SessionManagement sessionManagement = new SessionManagement(activity);
                SessionManagement.saveSession(userModel.getId(), userModel.getName(), userModel.getName());
                sessionManagement.getSession();
                Log.d(TAG, "onSuccess: Successfully logged In : " + SessionManagement.userName);
                Toast.makeText(activity, "Welcome " + SessionManagement.userName, Toast.LENGTH_SHORT).show();
                loggedInSuccessfully();
                clearForm();
                pbSignup.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.VISIBLE);
                ((HomepageActivity) activity).loggedIn();
            }

            @Override
            public void onFailure(ResponseBody responseBody) throws IOException {
                Log.e(TAG, "Failed: " + responseBody.string());
                Toast.makeText(activity, "Wrong credentials,Try again ", Toast.LENGTH_SHORT).show();
                pbSignup.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestFailed(String message) {
                Log.e(TAG, "Request Failure: " + message);
                Toast.makeText(activity, "Weak Internet ,Try again ", Toast.LENGTH_SHORT).show();
                pbSignup.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.VISIBLE);
            }
        });
    }

    private void clearForm() {
        etSignUpUserConfirmPassword.setText("");
        etSignUpUserPassword.setText("");
        etSignUpUserEmail.setText("");
        etSignUpUserName.setText("");
        etSignUpBusinessName.setText("");
        etLoginUserPassword.setText("");
        etLoginUserName.setText("");
    }

    private void manipulateContainers(String containerName) {
        if (containerName.equals(Tags.STRING_LOGIN_CONTAINER)) {
            llSignUpContainer.setVisibility(View.GONE);
            llLoginContainer.setVisibility(View.VISIBLE);
//            txtLogin.setBackgroundColor(getResources().getColor(R.color.black));
//            txtLogin.setTextColor(getResources().getColor(R.color.white));
//            txtSignUp.setBackgroundColor(getResources().getColor(R.color.white));
//            txtSignUp.setTextColor(getResources().getColor(R.color.black));
        } else if (containerName.equals(Tags.STRING_SIGNUP_CONTAINER)) {

            llSignUpContainer.setVisibility(View.VISIBLE);
            llLoginContainer.setVisibility(View.GONE);
//            txtLogin.setBackgroundColor(getResources().getColor(R.color.white));
//            txtLogin.setTextColor(getResources().getColor(R.color.black));
//            txtSignUp.setBackgroundColor(getResources().getColor(R.color.black));
//            txtSignUp.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void togglePasswordVisibility(String key) {
        if (key.equals(Tags.STRING_LOGIN_CONTAINER)) {
            if (isPasswordVisible) {
                // Hide password
                etLoginUserPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            togglePasswordButton.setText("Show");
                ivHidePassword.setVisibility(View.VISIBLE);
                ivShowPassword.setVisibility(View.GONE);
            } else {
                // Show password
                etLoginUserPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//            togglePasswordButton.setText("Hide");
                ivHidePassword.setVisibility(View.GONE);
                ivShowPassword.setVisibility(View.VISIBLE);
            }
            // Move cursor to the end of the text
            etLoginUserPassword.setSelection(etLoginUserPassword.getText().length());
            isPasswordVisible = !isPasswordVisible;
        } else if (key.equals(Tags.STRING_SIGNUP_CONTAINER)) {
            if (isSignupPasswordVisible) {
                // Hide password
                etSignUpUserConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            togglePasswordButton.setText("Show");
                ivSignupHidePassword.setVisibility(View.VISIBLE);
                ivSignupShowPassword.setVisibility(View.GONE);
            } else {
                // Show password
                etSignUpUserConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//            togglePasswordButton.setText("Hide");
                ivSignupHidePassword.setVisibility(View.GONE);
                ivSignupShowPassword.setVisibility(View.VISIBLE);
            }
            // Move cursor to the end of the text
            etSignUpUserConfirmPassword.setSelection(etSignUpUserConfirmPassword.getText().length());
            isSignupPasswordVisible = !isSignupPasswordVisible;
        }
    }

    public void loggedInSuccessfully() {
        if (SessionManagement.userId != null) {
            svNotLoggedIn.setVisibility(View.GONE);
            llLoggedIn.setVisibility(View.VISIBLE);
            String firstLetter = "";
            firstLetter += SessionManagement.userName.charAt(0);
            for (int i = 1; i < SessionManagement.userName.length(); i++) {
                if (SessionManagement.userName.charAt(i - 1) == ' ') {
                    firstLetter += SessionManagement.userName.charAt(i);
                }
            }
            txtIntials.setText(firstLetter);
            txtUserName.setText(SessionManagement.userName);
        } else {
            svNotLoggedIn.setVisibility(View.VISIBLE);
            llLoggedIn.setVisibility(View.GONE);
        }
    }
}