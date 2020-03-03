package com.hosteloha.app.ui.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hosteloha.R;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class LoginPinFragment extends Fragment {

    private Button mSendOTPButton, mVerifyOTPButton;
    private EditText mPhoneNumberInput, mOTPInput;
    private LoginViewModel loginViewModel;
    private String TAG = LoginViewModel.class.getSimpleName();
    private String verificationCode;
    private Activity mActivity;
    private LinearLayout mGetOTPLayout, mVerifyOTPLayout;
    private ProgressDialog mProgressDialog;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loginpage_sendOTPButton:

                    Log.d("Suhaas", " Firebase instance:: " + FirebaseAuth.getInstance().getCurrentUser() + " UID " + FirebaseAuth.getInstance().getCurrentUser());

                    mGetOTPLayout.setVisibility(View.GONE);
                    mVerifyOTPLayout.setVisibility(View.VISIBLE);
                    String phoneNumberIs = mPhoneNumberInput.getText().toString();
                    Log.d("Suhaas", "Phone Number is :: " + phoneNumberIs);

                    if (phoneNumberIs != null && phoneNumberIs.length() > 0) {

                    }
                    mProgressDialog.setMessage("Sending verification code...");
                    mProgressDialog.show();
                    break;
                case R.id.loginpage_verifyOtpBtn:
                    String OTP = mOTPInput.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, OTP);

                    break;

            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        signOutIfGoogleExisting();
        mProgressDialog = new ProgressDialog(getActivity());

        loginViewModel =
                ViewModelProviders.of(this).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        final TextView textView = root.findViewById(R.id.text_login);
        mSendOTPButton = root.findViewById(R.id.loginpage_sendOTPButton);
        mVerifyOTPButton = root.findViewById(R.id.loginpage_verifyOtpBtn);
        mPhoneNumberInput = root.findViewById(R.id.loginpage_phoneNo);
        mOTPInput = root.findViewById(R.id.loginpage_otpPhoneVerify);
        mGetOTPLayout = root.findViewById(R.id.loginpage_phoneNoEnterLayout);
        mVerifyOTPLayout = root.findViewById(R.id.loginpage_phoneNoVerifyLayout);
        mActivity = this.getActivity();
        loginViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        mSendOTPButton.setOnClickListener(mOnClickListener);
        mVerifyOTPButton.setOnClickListener(mOnClickListener);
//        if (auth != null) {
//            Log.d("Suhaas", "In ONcreate current user :: " + auth.getCurrentUser() + " UID " + auth.getUid() +
//                    " phone Number :: " + auth.getCurrentUser() + " FireBase :: "+FirebaseAuth.getInstance().getCurrentUser());
//
//            auth.signOut();
//            FirebaseAuth.getInstance().signOut();
//        }


        return root;
    }

    public void onComplete(String pin) {
        Log.d(TAG, "Pin complete: " + pin + " current pin :: " + HostelohaUtils.getCurrentTimePin());
        // Added 4560 as default pin.
        if (pin.equals("4560")) {
            final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_nav_login_to_nav_home);
        } else {
            Snackbar.make(getView(), "Invalid pin", Snackbar.LENGTH_SHORT).show();
        }
    }


    private void switchToHostfragment() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_nav_login_to_nav_home);
    }


    private void signOutIfGoogleExisting() {


    }

}


