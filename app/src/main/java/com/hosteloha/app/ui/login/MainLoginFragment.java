package com.hosteloha.app.ui.login;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hosteloha.R;
import com.hosteloha.app.beans.AuthenticationTokenJWT;
import com.hosteloha.app.beans.UserAuthentication;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.service.HostelohaService;
import com.hosteloha.app.utils.AppSharedPrefs;
import com.hosteloha.app.utils.Define;
import com.hosteloha.app.utils.HostelohaUtils;
import com.hosteloha.databinding.FragmentLoginMainBinding;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainLoginFragment extends Fragment {

    private HostelohaService mHostelohaService = null;

    FragmentLoginMainBinding mFLMBinding;
    private ProgressDialog mProgressDialog;
    NavController navController;
    AlertDialog.Builder mBuilder;

    String phoneNum = "";
    private String mLOG_TAG = MainLoginFragment.class.getSimpleName();

    //Google Auth
    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 8231;

    //FIRE_BASE AUTH & PHONE_AUTH
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mPhoneAuthCallbacks;
    FirebaseAuth mFireBaseAuth;
    private PhoneAuthProvider mPhoneAuthProvider;
    private String mVerificationId = null;
    PhoneAuthProvider.ForceResendingToken mForceResendingToken = null;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            HostelohaLog.debugOut("  onReceive :: SMS RECEIVER ::  " + intent.getAction());

            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                HostelohaUtils.showSnackBarNotification(getActivity(), "SMS :::received");
                Bundle extras = intent.getExtras();
                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        HostelohaUtils.showSnackBarNotification(getActivity(), "SMS :::" + message);
                        // Extract one-time code from the message
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Waiting for SMS timed out (5 minutes)
                        break;
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mHostelohaService = HostelohaUtils.getHostelohaService(getContext());
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Processing...");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCanceledOnTouchOutside(false);

        mBuilder = new AlertDialog.Builder(getContext());
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mFLMBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_main, container, false);


        mGoogleSignInClient = HostelohaUtils.getGoogleSignInClient(getActivity());

        mFireBaseAuth = FirebaseAuth.getInstance();
        mPhoneAuthProvider = PhoneAuthProvider.getInstance();

        initListener();

        SmsRetrieverClient client = SmsRetriever.getClient(getActivity());
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                HostelohaLog.debugOut("  onSuccess :: TASK ::  SMS");

            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                HostelohaLog.debugOut("  onSuccess :: TASK ::  SMS");

            }
        });

        // SMS retreival code.
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, mIntentFilter);
//        getActivity().registerReceiver(mBroadcastReceiver, mIntentFilter);


        return mFLMBinding.getRoot();
    }


    private void showProgressDialog(String title, String message) {
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void dismissProgressDialog(String dismissMessage) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            if (dismissMessage != null && dismissMessage.length() > 0) {
                HostelohaUtils.showSnackBarNotification(getActivity(), dismissMessage);
            }
            mProgressDialog.dismiss();
        }
    }

    /**
     * To initialize the listener's such as {@link TextWatcher}, {@link android.view.View.OnKeyListener}
     */
    private void initListener() {
        mFLMBinding.etNumber.addTextChangedListener(mGeneralTextWatcher);
        mFLMBinding.etNumber.setOnKeyListener(mOnKeyListener);

        mFLMBinding.btnSendOtp.setOnClickListener(onClickListener);
        enableButton(mFLMBinding.btnSendOtp, false);

        mFLMBinding.btnSubmitOtp.setOnClickListener(onClickListener);
        enableButton(mFLMBinding.btnSubmitOtp, false);

        mFLMBinding.pinView.addTextChangedListener(mGeneralTextWatcher);
        mFLMBinding.pinView.setOnKeyListener(mOnKeyListener);

        mFLMBinding.webAuthenticationView.setOnClickListener(onClickListener);
        mFLMBinding.btnSubmitWebauthentication.setOnClickListener(onClickListener);
        mFLMBinding.etWebauthUsername.addTextChangedListener(mGeneralTextWatcher);
        mFLMBinding.etWebauthUsername.setOnKeyListener(mOnKeyListener);
        mFLMBinding.etWebauthPassword.addTextChangedListener(mGeneralTextWatcher);
        mFLMBinding.etWebauthPassword.setOnKeyListener(mOnKeyListener);
        enableButton(mFLMBinding.btnSubmitWebauthentication, false);

        mFLMBinding.signInButton.setOnClickListener(onClickListener);
        registerCallBacksForPhoneAuth();

    }

    private void registerCallBacksForPhoneAuth() {
        mPhoneAuthCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                HostelohaUtils.showSnackBarNotification(getActivity(), " Verfied " + phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                HostelohaLog.debugOut(" Exception " + e.getMessage() + " StackTrace" + e.getStackTrace());
                HostelohaUtils.showSnackBarNotification(getActivity(), " Verfication Failed ");
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = verificationId;
                mForceResendingToken = forceResendingToken;
                super.onCodeSent(verificationId, forceResendingToken);
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Added API to check whether user exist or not
    private boolean isUserExists(String phoneNum) {
        //ToDO   Check user exists or not in DataBase

        return phoneNum.equals("0000000000");

    }

    private boolean verifyOtp() {
        // ToDO verify OTP
        boolean isCorrect = false;
        // Below code is temporary testing purpose
        isCorrect = mFLMBinding.pinView.getText().toString().equals(phoneNum.subSequence(phoneNum.length() - 4, phoneNum.length()));
        return isCorrect;
    }

    private void showSignUpAlertDilogue() {

        mBuilder.setMessage("New Phone Number found. Do you want to sign Up ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        navController.navigate(R.id.action_mainLoginFragment_to_signUpFragment);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mFLMBinding.sendOtpView.setVisibility(View.VISIBLE);
                        mFLMBinding.verifyOtpView.setVisibility(View.GONE);
                    }
                });
        //Creating dialog box
        AlertDialog alert = mBuilder.create();
        //Setting the title manually
        alert.setTitle("SignUp");
        alert.show();
    }

    /**
     * To enable the button request OTP button if the text length is 10.
     */
    private void processButtonByTextLength(int editTextId) {
        switch (editTextId) {
            case R.id.et_number:
                String inputText = mFLMBinding.etNumber.getText().toString();
                enableButton(mFLMBinding.btnSendOtp, (inputText.length() >= 10));
                break;
            case R.id.pinView:
                String inputPin = mFLMBinding.pinView.getText().toString();
                enableButton(mFLMBinding.btnSubmitOtp, (inputPin.length() == 6));
                break;
            case R.id.et_webauth_password:
            case R.id.et_webauth_username:
                if (mFLMBinding.etWebauthUsername.getText() != null &&
                        mFLMBinding.etWebauthPassword.getText() != null) {
                    if (mFLMBinding.etWebauthUsername.getText().length() > 0 &&
                            mFLMBinding.etWebauthPassword.getText().length() > 0) {
                        enableButton(mFLMBinding.btnSubmitWebauthentication, true);
                    } else {
                        enableButton(mFLMBinding.btnSubmitWebauthentication, false);
                    }
                }

                break;

        }
    }

    public void enableButton(Button button, boolean isEnable) {
        button.setEnabled(isEnable);
        if (isEnable) {
            button.setAlpha(1f);
        } else {
            button.setAlpha(0.2f);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_send_otp) {
                if (mFLMBinding.etNumber.getText() != null) {
                    phoneNum = mFLMBinding.etNumber.getText().toString();
                    if (phoneNum.length() == 10) {
                        //Add extension
                        phoneNum = "+91" + phoneNum;
                        mPhoneAuthProvider.verifyPhoneNumber(
                                phoneNum,
                                60,
                                TimeUnit.SECONDS,
                                getActivity(),
                                mPhoneAuthCallbacks);
                        showOtherViews(View.GONE);
                        mFLMBinding.verifyOtpView.setVisibility(View.VISIBLE);
                        String OTPSentTo = getResources().getString(R.string.otp_sent_variable_phone, phoneNum);
                        mFLMBinding.tvOtpReceived.setText(OTPSentTo);
                    } else {
                        Toast.makeText(getContext(), "Please Enter Valid Phone Number", Toast.LENGTH_LONG).show();
                    }
                }


            } else if (v.getId() == R.id.btn_submit_otp) {
                String mOTPIs = mFLMBinding.pinView.getText().toString();
                if (mVerificationId != null) {
                    mFireBaseAuth.signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, mOTPIs))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        HostelohaUtils.showSnackBarNotification(getActivity(), " OTP Success LOGIN");
                                        navigateToHomeScreen(AppSharedPrefs.getPreviousViewType(getContext()));
                                    } else {
                                        HostelohaUtils.showSnackBarNotification(getActivity(), "Incorrect OTP");
                                    }
                                }
                            });
                }

//                if (isUserExists(phoneNum)) {
//                    if (verifyOtp())
//                        navigateToHomeScreen(HostelohaUtils.getPreviousViewType(getContext()));
//                    else
//                        Toast.makeText(getContext(), "Please enter correct OTP", Toast.LENGTH_LONG).show();
//                } else {
//                    showSignUpAlertDilogue();
//                }
            } else if (v.getId() == R.id.web_authentication_view) {
                showOtherViews(View.GONE);
                //Showing email authentication
                mFLMBinding.verifyWebauthenticationView.setVisibility(View.VISIBLE);
            } else if (v.getId() == R.id.btn_submit_webauthentication) {
                showProgressDialog("Verifying Credentials", "Please wait...");
                String userName = mFLMBinding.etWebauthUsername.getText().toString();
                String userPass = mFLMBinding.etWebauthPassword.getText().toString();
                UserAuthentication userAuthentication = new UserAuthentication(userName, userPass);
                getAuthenticationToken(userAuthentication);
            } else if (v.getId() == R.id.sign_in_button) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        }
    };

    private void navigateToHomeScreen(String viewType) {
        if(Define.isPreviousViewCheckEnabled){
            boolean isPreviousViewBuyer = (viewType.equals(Define.VIEW_BUYER));
            if (isPreviousViewBuyer) {
                navController.navigate(MainLoginFragmentDirections.actionMainLoginFragmentToNavBuyer());
            } else {
                navController.navigate(MainLoginFragmentDirections.actionMainLoginFragmentToNavSeller());
            }
        }else{
            navController.navigate(MainLoginFragmentDirections.actionMainLoginFragmentToNavHome());
        }
    }

    private void getAuthenticationToken(UserAuthentication userAuthentication) {

        ApiUtil.getServiceClass().getAuthenticationToken(userAuthentication).enqueue(new Callback<AuthenticationTokenJWT>() {
            @Override
            public void onResponse(Call<AuthenticationTokenJWT> call, Response<AuthenticationTokenJWT> response) {
                if (response.isSuccessful()) {
                    dismissProgressDialog("Verfied Successfully");
                    AuthenticationTokenJWT mAuthenticationTokenJWT = response.body();
                    HostelohaUtils.setAuthenticationToken(mAuthenticationTokenJWT.getJwt());
                    if (mHostelohaService != null)
                        mHostelohaService.getSplashData();
                    AppSharedPrefs.storeUserLoginInfo(getContext(), true, HostelohaUtils.AUTHENTICATION_TOKEN);
                    navigateToHomeScreen(AppSharedPrefs.getPreviousViewType(getContext()));
                    HostelohaLog.debugOut("user id :  " + mAuthenticationTokenJWT.getUserId() + "  JWT " + mAuthenticationTokenJWT.getJwt());
                } else {
                    String mPopMessage = null;
                    try {
                        mPopMessage = "INVALID_DATA " + response.body();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getActivity(), mPopMessage, Toast.LENGTH_SHORT).show();
                    dismissProgressDialog("Failed to login");
                    showOtherViews(View.GONE);
                    mFLMBinding.sendOtpView.setVisibility(View.VISIBLE);
                    mFLMBinding.webAuthenticationView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<AuthenticationTokenJWT> call, Throwable t) {
                dismissProgressDialog("Host unreachable");
                HostelohaLog.debugOut("Unable to submit post to API.");
            }
        });
    }

    private void showOtherViews(int isVisible) {
        mFLMBinding.sendOtpView.setVisibility(isVisible);
        mFLMBinding.verifyWebauthenticationView.setVisibility(isVisible);
        mFLMBinding.webAuthenticationView.setVisibility(isVisible);
    }

    TextWatcher mGeneralTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.hashCode() == mFLMBinding.etNumber.getText().hashCode()) {
                processButtonByTextLength(mFLMBinding.etNumber.getId());
            }
            if (editable.hashCode() == mFLMBinding.pinView.getText().hashCode()) {
                processButtonByTextLength(mFLMBinding.pinView.getId());
            }
            if (editable.hashCode() == mFLMBinding.etWebauthPassword.getText().hashCode()) {
                processButtonByTextLength(mFLMBinding.etWebauthPassword.getId());
            }
            if (editable.hashCode() == mFLMBinding.etWebauthUsername.getText().hashCode()) {
                processButtonByTextLength(mFLMBinding.etWebauthUsername.getId());
            }

        }
    };

    View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            int action = keyEvent.getAction();
            if (action == KeyEvent.ACTION_UP) {
                if (view.getId() == mFLMBinding.etNumber.getId()) {
                    processButtonByTextLength(mFLMBinding.etNumber.getId());
                }
                if (view.getId() == mFLMBinding.pinView.getId()) {
                    processButtonByTextLength(mFLMBinding.pinView.getId());
                }
                if (view.getId() == mFLMBinding.etWebauthUsername.getId()) {
                    processButtonByTextLength(mFLMBinding.etWebauthUsername.getId());
                }
                if (view.getId() == mFLMBinding.etWebauthPassword.getId()) {
                    processButtonByTextLength(mFLMBinding.etWebauthUsername.getId());
                }
            }
            return false;
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            navigateToHomeScreen(AppSharedPrefs.getPreviousViewType(getContext()));
            String userNameIs = account.getDisplayName();
            String userEmailIs = account.getEmail();
            HostelohaUtils.showSnackBarNotification(getActivity(), userNameIs + " - " + userEmailIs);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            HostelohaUtils.showSnackBarNotification(getActivity(), "Google Sign IN :: " + e.getStatusCode());
        }
    }
}
