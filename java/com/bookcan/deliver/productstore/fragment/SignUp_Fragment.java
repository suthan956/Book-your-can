package com.bookcan.deliver.productstore.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.bookcan.deliver.productstore.activity.CheckoutActivity;
import com.bookcan.deliver.productstore.model.CheckoutResponse;
import com.bookcan.deliver.productstore.model.LoginResponse;
import com.bookcan.deliver.productstore.model.SignUpResponse;
import com.bookcan.deliver.productstore.util.ApiClient;
import com.bookcan.deliver.productstore.util.GPSTracker;
import com.daimajia.easing.linear.Linear;
import com.google.gson.Gson;
import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.activity.LoginRegisterActivity;
import com.bookcan.deliver.productstore.activity.MainActivity;
import com.bookcan.deliver.productstore.model.User;
import com.bookcan.deliver.productstore.util.CustomToast;
import com.bookcan.deliver.productstore.util.Utils;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUp_Fragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText fullName, emailId, mobileNumber,
            password,delLoc,flatNo,street,landmark,pincode,floorNo;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    ProgressDialog progressDialog;
    User user;

    Gson gson;
    String stringLatitude = "0.0";
    String stringLongitude = "0.0";
    String liftAvail = "0";

    public SignUp_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        fullName = view.findViewById(R.id.fullName);
        emailId = view.findViewById(R.id.userEmailId);
        mobileNumber = view.findViewById(R.id.mobileNumber);

        password = view.findViewById(R.id.password);
        delLoc = view.findViewById(R.id.userLocation);
        flatNo = view.findViewById(R.id.flatNo);
        street = view.findViewById(R.id.streetName);
        landmark = view.findViewById(R.id.landmark);
        pincode = view.findViewById(R.id.pincode);
        floorNo = view.findViewById(R.id.floorNo);
        GPSTracker gpsTracker = new GPSTracker(requireContext());
        if (!gpsTracker.getIsGPSTrackingEnabled()) {
            gpsTracker.showSettingsAlert();
        }else {
            stringLatitude = String.valueOf(gpsTracker.latitude);
            stringLongitude = String.valueOf(gpsTracker.longitude);
        }
        LinearLayout gpsLayout = view.findViewById(R.id.gpsLayout);
        gpsLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker gpsTracker = new GPSTracker(requireContext());
                if (gpsTracker.getIsGPSTrackingEnabled())
                {
                    stringLatitude = String.valueOf(gpsTracker.latitude);

                    stringLongitude = String.valueOf(gpsTracker.longitude);

//                    String country = gpsTracker.getCountryName(requireContext());
//
//                    String city = gpsTracker.getLocality(requireContext());
//
//                    String postalCode = gpsTracker.getPostalCode(requireContext());

                    String addressLine = gpsTracker.getAddressLine(requireContext());
                    delLoc.setText(addressLine);
                    Log.i("gps",stringLatitude+stringLongitude+addressLine);
                }
                else
                {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gpsTracker.showSettingsAlert();
                }
            }
        });

        signUpButton = view.findViewById(R.id.signUpBtn);
        login = view.findViewById(R.id.already_user);
        terms_conditions = view.findViewById(R.id.terms_conditions);
        progressDialog = new ProgressDialog(getContext());

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }

        SwitchCompat liftSwitch = view.findViewById(R.id.liftAvail);
        liftSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    liftAvail = "1";
                }else {
                    liftAvail = "0";
                }
            }
        });


    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                // Replace login fragment
                new LoginRegisterActivity().replaceLoginFragment();
                break;
        }

    }

    // Check Validation Method
    private void checkValidation() {
        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getPassword = password.getText().toString();
        String getFlatno = flatNo.getText().toString();
        String getDelAdd = delLoc.getText().toString();
        String getStreet = street.getText().toString();
        String getFloorno = floorNo.getText().toString();
        String getPincode = pincode.getText().toString();
        String getLandmark = landmark.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);


        if (getFullName.length() == 0) {
            fullName.setError("Enter Your Name");
            fullName.requestFocus();
        } else if (getEmailId.length() == 0) {
            emailId.setError("Enter Your Email");
            emailId.requestFocus();
        } else if (!m.find()) {
            emailId.setError("Eneter Correct Email");
            emailId.requestFocus();
        } else if (getMobileNumber.length() == 0) {
            mobileNumber.setError("Enter Your Mobile Number");
            mobileNumber.requestFocus();
        } else if (getPassword.length() == 0) {
            password.setError("Enter Password");
            password.requestFocus();
        } else if (getFlatno.length() == 0) {
            flatNo.setError("Enter flatNo");
            flatNo.requestFocus();
        } else if (getStreet.length() == 0) {
            street.setError("Enter street");
            street.requestFocus();
        } else if (getPincode.length() == 0) {
            pincode.setError("Enter pincode");
            pincode.requestFocus();
        } else if (getDelAdd.length() == 0) {
            delLoc.setError("Enter Delivery Address");
            delLoc.requestFocus();
        }  else {
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            JSONObject obj = new JSONObject();
            try {
                obj.put("email", getEmailId);
                obj.put("name", getFullName);
                obj.put("mobile", getMobileNumber);
                obj.put("location", delLoc);
                obj.put("latitude", stringLatitude);
                obj.put("longitude", stringLongitude);
                obj.put("house_flat_no", getFlatno);
                obj.put("street_name", getStreet);
                obj.put("landmark", getLandmark);
                obj.put("password", getPassword);
                obj.put("pincode", getPincode);
                obj.put("lift_availability", liftAvail);
                obj.put("total_floors", getFloorno);
                obj.put("role_value", "customer");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LocalStorage localStorage = new LocalStorage(requireContext());
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
            Call<SignUpResponse> call = ApiClient.getSignUpService().signUpCall(Utils.App_Authorization_Key,
                    "NDQx", "Q1VTVDAwNDQxMTYzOTcyMzQ1MjIwMDQ5ODU3OTc=",body);
            call.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    progressDialog.dismiss();
                    if(response.body().getResponse().contains("OTP")){
                        String cus_id = response.body().getCustomer_id();
                        showOTPDialog(cus_id);
                    }else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                        alertDialog.setTitle("SignUp Status");
                        alertDialog.setMessage(response.body().getResponse());
                        alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new LoginRegisterActivity().replaceLoginFragment();
                            }
                        });
                        alertDialog.show();
                    }
                }
                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable throwable) {
                    //Log.e(TAG, throwable.toString());
                    progressDialog.dismiss();
                    Toast.makeText(requireContext(),"Signup Failed",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    private void showOTPDialog(String cus_id){
        final EditText input = new EditText(requireContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle("Enter OTP to Verify");
        alertDialog.setView(input);
        alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("otp", input.getText().toString())
                        .addFormDataPart("cus_id", cus_id)
                        .build();
                Call<SignUpResponse> loginResponseCall = ApiClient.getOtpService().userLoginOtp(Utils.App_Authorization_Key,requestBody);
                loginResponseCall.enqueue(new Callback<SignUpResponse>() {
                    @Override
                    public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                        if (response.body().getResponse().contains("Incorrect")){
                            showOTPDialog(cus_id);
                        }else {
                            new LoginRegisterActivity().replaceLoginFragment();
                        }
                    }

                    @Override
                    public void onFailure(Call<SignUpResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        //Toast.makeText(requireContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(requireContext(),"OTP Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        alertDialog.setNegativeButton("Cancel",null);
        alertDialog.show();
    }
}
