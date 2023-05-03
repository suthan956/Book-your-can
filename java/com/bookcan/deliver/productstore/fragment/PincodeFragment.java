package com.bookcan.deliver.productstore.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.activity.LoginRegisterActivity;
import com.bookcan.deliver.productstore.model.ForgetResponse;
import com.bookcan.deliver.productstore.model.PinCodeResponse;
import com.bookcan.deliver.productstore.util.ApiClient;
import com.bookcan.deliver.productstore.util.CustomToast;
import com.bookcan.deliver.productstore.util.Utils;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PincodeFragment extends Fragment implements
        View.OnClickListener {
    private static View view;

    private static EditText emailId;
    private static TextView submit, back;
    ProgressDialog progressDialog;
    private static FragmentManager fragmentManager;
    public PincodeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pincode, container,
                false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize the views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();
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

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0) {
            progressDialog.dismiss();
            new CustomToast().Show_Toast(getActivity(), view,
                    "Please enter Pincode");

            // Check if email id is valid or not
        } else {
            LocalStorage localStorage = new LocalStorage(requireContext());
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("pincode", getEmailId)
                    .build();
            Call<PinCodeResponse> forgetResponseCall = ApiClient.getPincodeService().pinCOdeAvail(Utils.App_Authorization_Key,
                    "NDQx", "Q1VTVDAwNDQxMTYzOTcyMzQ1MjIwMDQ5ODU3OTc=",requestBody);
            forgetResponseCall.enqueue(new Callback<PinCodeResponse>() {
                @Override
                public void onResponse(Call<PinCodeResponse> call, Response<PinCodeResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        PinCodeResponse pinCodeResponse = response.body();
                        new CustomToast().Show_Toast(getActivity(), view, pinCodeResponse.getMessage());
                        if (pinCodeResponse.getStatus() == 1) {
                            fragmentManager
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                                    .replace(R.id.frameContainer, new SignUp_Fragment(),
                                            Utils.SignUp_Fragment).commit();
                        }
                    }else {
                        new CustomToast().Show_Toast(getActivity(), view, "Not Available");
                    }
                }

                @Override
                public void onFailure(Call<PinCodeResponse> call, Throwable t) {
                    //Toast.makeText(requireContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    new CustomToast().Show_Toast(getActivity(), view, "Not Available");
                }
            });
        }
    }
}