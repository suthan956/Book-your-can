package com.bookcan.deliver.productstore.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bookcan.deliver.productstore.activity.EmployeeActivity;
import com.bookcan.deliver.productstore.interfaces.LoginService;
import com.bookcan.deliver.productstore.model.Employee;
import com.bookcan.deliver.productstore.model.LoginRequest;
import com.bookcan.deliver.productstore.model.LoginResponse;
import com.bookcan.deliver.productstore.util.ApiClient;
import com.bookcan.deliver.productstore.util.ServiceGenerator;
import com.google.gson.Gson;
import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.activity.MainActivity;
import com.bookcan.deliver.productstore.model.User;
import com.bookcan.deliver.productstore.util.CustomToast;
import com.bookcan.deliver.productstore.util.Utils;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;
import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login_Fragment extends Fragment implements OnClickListener {
    private static View view;

    private static EditText emailid, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;

    ProgressDialog progressDialog;
    LocalStorage localStorage;
    String userString;
    User user;
    Employee employee;

    public Login_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout, container, false);
        initViews();
        setListeners();
        return view;
    }

    // Initiate Views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        emailid = view.findViewById(R.id.login_emailid);
        password = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.loginBtn);
        forgotPassword = view.findViewById(R.id.forgot_password);
        signUp = view.findViewById(R.id.createAccount);
        show_hide_password = view
                .findViewById(R.id.show_hide_password);
        loginLayout = view.findViewById(R.id.login_layout);
        progressDialog = new ProgressDialog(getContext());
        localStorage = new LocalStorage(getContext());
        String userString = localStorage.getUserLogin();
        Gson gson = new Gson();
        userString = localStorage.getUserLogin();
        user = gson.fromJson(userString, User.class);
        Log.d("User", userString);
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;

            case R.id.forgot_password:

                // Replace forgot password fragment with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer,
                                new ForgotPassword_Fragment(),
                                Utils.ForgotPassword_Fragment).commit();
                break;
            case R.id.createAccount:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new PincodeFragment(),
                                Utils.Pincode_Fragment).commit();
                break;
        }

    }

    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        final String getEmailId = emailid.getText().toString();
        final String getPassword = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter both credentials.");
            vibrate(200);
        }
        // Check if email id is valid or not
//        else if (!m.find()) {
//            new CustomToast().Show_Toast(getActivity(), view,
//                    "Your Email Id is Invalid.");
//            vibrate(200);
//            // Else do login and do your stuff
//        }
        else {

            progressDialog.setMessage("Please Wait....");
            progressDialog.show();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", getEmailId)
                    .addFormDataPart("password", getPassword)
                    .build();
            Call<LoginResponse> loginResponseCall = ApiClient.getUserService().userLogin(Utils.App_Authorization_Key,requestBody);
            loginResponseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(response.isSuccessful()){
                        LoginResponse loginResponse = response.body();
                        if (loginResponse.getResponse_code() != 101 ) {
                            JsonObject userObj = loginResponse.getUser_object();
                            localStorage.setUserAuthorization(loginResponse.getUser_authorization_1(),
                                    loginResponse.getUser_authorization_2());
                            if(userObj.get("role_value").getAsString().equalsIgnoreCase("employee")){
                                localStorage.setCustomerId(userObj.get("emp_code").getAsString());
                                employee = new Employee(userObj.get("emp_id").getAsString(),
                                        userObj.get("emp_code").getAsString(), userObj.get("first_name").getAsString(),
                                        userObj.get("last_name").getAsString(), userObj.get("emp_mob_no").getAsString(),
                                        userObj.get("role_value").getAsString());
                                Gson gson = new Gson();
                                String userString = gson.toJson(employee);
                                localStorage = new LocalStorage(requireContext());
                                localStorage.createUserLoginSession(userString);
                                progressDialog.dismiss();
                                localStorage.setCustomerOrEmployee("employee");
                                new CustomToast().Show_Toast(getActivity(), view,
                                        "Login Successful");
                                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getActivity(), EmployeeActivity.class));
                                getActivity().finish();
                                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            }else {
                                localStorage.setCustomerId(userObj.get("customer_id").getAsString());
                                user = new User(userObj.get("reg_id").getAsString(),
                                        userObj.get("customer_id").getAsString(), userObj.get("role_value").getAsString(),
                                        userObj.get("name").getAsString(), userObj.get("email").getAsString(),
                                        userObj.get("mobile").getAsString(), userObj.get("address").getAsString(),
                                        userObj.get("latitude").getAsString(), userObj.get("longtitude").getAsString(),
                                        userObj.get("house_flat_no").getAsString(), userObj.get("street_name").getAsString(),
                                        userObj.get("landmark").getAsString(), userObj.get("pincode").getAsString(),
                                        userObj.get("lift_availability").getAsString(), userObj.get("total_floors").getAsString(),
                                        userObj.get("updated_at").getAsString(), userObj.get("created_at").getAsString());
                                Gson gson = new Gson();
                                String userString = gson.toJson(user);
                                localStorage = new LocalStorage(requireContext());
                                localStorage.createUserLoginSession(userString);
                                progressDialog.dismiss();
                                localStorage.setCustomerOrEmployee("customer");
                                new CustomToast().Show_Toast(getActivity(), view,
                                        "Login Successful");
                                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            }
                        }else {
                            progressDialog.dismiss();
                            new CustomToast().Show_Toast(getActivity(), view,
                                    "Login Failed");
                            //Toast.makeText(requireContext(),loginResponse.getResponse(), Toast.LENGTH_LONG).show();
                        }
                    }else{
                        progressDialog.dismiss();
                        new CustomToast().Show_Toast(getActivity(), view,
                                "Login Failed");
                        //Toast.makeText(requireContext(),"Login Failed", Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    //Toast.makeText(requireContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Toast.makeText(requireContext(),"Login Failed", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    public void vibrate(int duration) {
        Vibrator vibs = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);
    }
}
