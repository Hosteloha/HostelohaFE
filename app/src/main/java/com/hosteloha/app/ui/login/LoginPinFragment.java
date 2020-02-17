package com.hosteloha.app.ui.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hosteloha.R;
import com.hosteloha.app.MainActivity;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class LoginPinFragment extends Fragment {

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    private Button mSendOTPButton, mVerifyOTPButton;
    private EditText mPhoneNumberInput, mOTPInput;
    private LoginViewModel loginViewModel;
    private String TAG = LoginViewModel.class.getSimpleName();
    private String verificationCode;
    private Activity mActivity;
    private LinearLayout mGetOTPLayout, mVerifyOTPLayout;
    private ProgressDialog mProgressDialog;
    private PhoneAuthProvider mPhoneAuthProvider;
    private int RC_SIGN_IN = 8231;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loginpage_sendOTPButton:

                    Log.d("Suhaas", " current user :: " + auth.getCurrentUser() + " UID " + auth.getUid());
                    Log.d("Suhaas", " Firebase instance:: " + FirebaseAuth.getInstance().getCurrentUser() + " UID " + FirebaseAuth.getInstance().getCurrentUser());

                    mGetOTPLayout.setVisibility(View.GONE);
                    mVerifyOTPLayout.setVisibility(View.VISIBLE);
                    String phoneNumberIs = mPhoneNumberInput.getText().toString();
                    Log.d("Suhaas", "Phone Number is :: " + phoneNumberIs);

                    if (phoneNumberIs != null && phoneNumberIs.length() > 0) {
                        mPhoneAuthProvider.verifyPhoneNumber(
                                phoneNumberIs,
                                60,
                                TimeUnit.SECONDS,
                                mActivity,
                                mCallbacks);
                    }
                    mProgressDialog.setMessage("Sending verification code...");
                    mProgressDialog.show();
                    break;
                case R.id.loginpage_verifyOtpBtn:
                    String OTP = mOTPInput.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, OTP);
                    SigninWithPhone(credential);
                    break;
                case R.id.sign_in_button:
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                    break;

            }}};

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

        StartFirebaseLogin();

        mSendOTPButton.setOnClickListener(mOnClickListener);
        mVerifyOTPButton.setOnClickListener(mOnClickListener);
//        if (auth != null) {
//            Log.d("Suhaas", "In ONcreate current user :: " + auth.getCurrentUser() + " UID " + auth.getUid() +
//                    " phone Number :: " + auth.getCurrentUser() + " FireBase :: "+FirebaseAuth.getInstance().getCurrentUser());
//
//            auth.signOut();
//            FirebaseAuth.getInstance().signOut();
//        }


        /**
         * Google SignIN
         */
        root.findViewById(R.id.sign_in_button).setOnClickListener(mOnClickListener);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this.getActivity(), gso);


        return root;
    }

    public void onComplete (String pin){
        Log.d(TAG, "Pin complete: " + pin + " current pin :: " + HostelohaUtils.getCurrentTimePin());
        // Added 4560 as default pin.
        if (pin.equals("4560")) {
            final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_nav_login_to_nav_home);
        } else {
            Snackbar.make(getView(), "Invalid pin", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void StartFirebaseLogin () {

        auth = FirebaseAuth.getInstance();
        mPhoneAuthProvider = PhoneAuthProvider.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                mProgressDialog.dismiss();
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d(TAG, "verification failed:" + e);

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                mProgressDialog.dismiss();
                Toast.makeText(mActivity, "Code sent", Toast.LENGTH_SHORT).show();
            }
        };
    }


    private void SigninWithPhone (PhoneAuthCredential credential){
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mActivity, "Success OTP LOGIN", Toast.LENGTH_SHORT).show();
                            switchToHostfragment();

                        } else {
                            Toast.makeText(mActivity, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void switchToHostfragment () {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_nav_login_to_nav_home);
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult (Task < GoogleSignInAccount > completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI (GoogleSignInAccount account){
        if (account != null) {
            Toast.makeText(getActivity(), " User ::" + account.getDisplayName() + " Email :: " + account.getEmail() + " is logged IN", Toast.LENGTH_SHORT).show();
            switchToHostfragment();
        } else {
            Toast.makeText(getActivity(), " User :: NULL", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOutIfGoogleExisting () {

        //signing out even the phone numbers
        FirebaseAuth instance = FirebaseAuth.getInstance();
        if (instance != null) {
            instance.signOut();
        }


        // Firebase sign out
        if (auth != null) {
            auth.signOut();
        }

        // Google sign out
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut().addOnCompleteListener(mActivity,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateUI(null);
                        }
                    });
        }

    }

}


