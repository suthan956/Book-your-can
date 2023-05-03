package com.bookcan.deliver.productstore.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.activity.LoginRegisterActivity;
import com.bookcan.deliver.productstore.activity.MainActivity;
import com.bookcan.deliver.productstore.model.ForgetResponse;
import com.bookcan.deliver.productstore.model.LoginResponse;
import com.bookcan.deliver.productstore.model.User;
import com.bookcan.deliver.productstore.util.ApiClient;
import com.bookcan.deliver.productstore.util.CustomToast;
import com.bookcan.deliver.productstore.util.Utils;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPassword_Fragment extends Fragment implements
        OnClickListener {
    private static View view;

    private static EditText emailId;
    private static TextView submit, back;
    ProgressDialog progressDialog;
    public ForgotPassword_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.forgotpassword_layout, container,
                false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize the views
    private void initViews() {
        emailId = view.findViewById(R.id.registered_emailid);
        submit = view.findViewById(R.id.forgot_button);
        back = view.findViewById(R.id.backToLoginBtn);
        progressDialog = new ProgressDialog(getContext());
        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            back.setTextColor(csl);
            submit.setTextColor(csl);

        } catch (Exception e) {
        }

    }

    // Set Listeners over buttons
    private void setListeners() {
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backToLoginBtn:

                // Replace Login Fragment on Back Presses
                new LoginRegisterActivity().replaceLoginFragment();
                break;

            case R.id.forgot_button:

                // Call Submit button task
                submitButtonTask();
                break;

        }

    }

    private void submitButtonTask() {
        progressDialog.setMessage("Please Wait....");
        progressDialog.show();
        String getEmailId = emailId.getText().toString();

        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.regEx);

        // Match the pattern
        Matcher m = p.matcher(getEmailId);

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0) {
            progressDialog.dismiss();
            new CustomToast().Show_Toast(getActivity(), view,
                    "Please enter your Email Id.");

            // Check if email id is valid or not
        }
        else if (!m.find()) {
            progressDialog.dismiss();
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");

            // Else submit email id and fetch passwod or do your stuff
        }
        else {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", getEmailId)
                    .build();
            Call<ForgetResponse> forgetResponseCall = ApiClient.getForgetService().forgetPasswordService(Utils.App_Authorization_Key, requestBody);
            forgetResponseCall.enqueue(new Callback<ForgetResponse>() {
                @Override
                public void onResponse(Call<ForgetResponse> call, Response<ForgetResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        ForgetResponse forgetResponse = response.body();
                        new CustomToast().Show_Toast(getActivity(), view,
                                forgetResponse.getResponse());
                    } else {
                        new CustomToast().Show_Toast(getActivity(), view,
                                "Invalid Email Id");
                    }

                }

                @Override
                public void onFailure(Call<ForgetResponse> call, Throwable t) {
                    //Toast.makeText(requireContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    new CustomToast().Show_Toast(getActivity(), view,
                            "Invalid Email Id");
                }
            });
        }
    }
}