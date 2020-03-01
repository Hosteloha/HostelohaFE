package com.hosteloha.app.ui.login;


import android.content.DialogInterface;
import android.os.Bundle;
import android.security.keystore.UserNotAuthenticatedException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hosteloha.R;
import com.hosteloha.app.MainActivity;
import com.hosteloha.app.beans.AuthenticationTokenJWT;
import com.hosteloha.app.beans.UserAuthentication;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.utils.HostelohaUtils;
import com.hosteloha.databinding.FragmentLoginMainBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.collection.ArrayMap;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainLoginFragment extends Fragment {
    FragmentLoginMainBinding mFLMBinding;

    NavController navController;
    AlertDialog.Builder mBuilder;

    String phoneNum = "";
    private String mLOG_TAG = MainLoginFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBuilder = new AlertDialog.Builder(getContext());
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mFLMBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_main, container, false);
        initListener();
        return mFLMBinding.getRoot();
    }

    /**
     * To initialize the listener's such as {@link TextWatcher}, {@link android.view.View.OnKeyListener}
     */
    private void initListener() {
        mFLMBinding.etNumber.addTextChangedListener(mGeneralTextWatcher);
        mFLMBinding.etNumber.setOnKeyListener(mOnKeyListener);

        mFLMBinding.btnSendOtp.setOnClickListener(onClickListener);
        enableButton(mFLMBinding.btnSendOtp, false);

        mFLMBinding.btnSubmit.setOnClickListener(onClickListener);
        enableButton(mFLMBinding.btnSubmit, false);

        mFLMBinding.pinView.addTextChangedListener(mGeneralTextWatcher);
        mFLMBinding.pinView.setOnKeyListener(mOnKeyListener);

        mFLMBinding.webAuthenticationView.setOnClickListener(onClickListener);
        mFLMBinding.btnSubmitWebauthentication.setOnClickListener(onClickListener);
        mFLMBinding.etWebauthUsername.addTextChangedListener(mGeneralTextWatcher);
        mFLMBinding.etWebauthUsername.setOnKeyListener(mOnKeyListener);
        mFLMBinding.etWebauthPassword.addTextChangedListener(mGeneralTextWatcher);
        mFLMBinding.etWebauthPassword.setOnKeyListener(mOnKeyListener);

        enableButton(mFLMBinding.btnSubmitWebauthentication, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Added API to check whether user exist or not
    private boolean isUserExists(String phoneNum) {
        //ToDO   Check user exists or not in DataBase

        if (phoneNum.equals("0000000000"))
            return true;
        else
            return false;

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
                enableButton(mFLMBinding.btnSubmit, (inputPin.length() >= 4));
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
                if (mFLMBinding.etNumber != null)
                    phoneNum = mFLMBinding.etNumber.getText().toString();
                if (phoneNum.length() == 10) {
                    showOtherViews(View.GONE);
                    mFLMBinding.verifyOtpView.setVisibility(View.VISIBLE);
                    String OTPSentTo = getResources().getString(R.string.otp_sent_variable_phone, phoneNum);
                    mFLMBinding.tvOtpReceived.setText(OTPSentTo);
                } else {
                    Toast.makeText(getContext(), "Please Enter Valid Phone Number", Toast.LENGTH_LONG).show();
                }

            } else if (v.getId() == R.id.btn_submit) {
                if (isUserExists(phoneNum)) {
                    if (verifyOtp())
                        navController.navigate(R.id.action_mainLoginFragment_to_nav_login);
                    else
                        Toast.makeText(getContext(), "Please enter correct OTP", Toast.LENGTH_LONG).show();
                } else {
                    showSignUpAlertDilogue();
                }
            } else if (v.getId() == R.id.web_authentication_view) {
                showOtherViews(View.GONE);
                //Showing email authentication
                mFLMBinding.verifyWebauthenticationView.setVisibility(View.VISIBLE);
            } else if (v.getId() == R.id.btn_submit_webauthentication) {
                String userName = mFLMBinding.etWebauthUsername.getText().toString();
                String userPass = mFLMBinding.etWebauthPassword.getText().toString();
                UserAuthentication userAuthentication = new UserAuthentication(userName, userPass);
                getAuthenticationToken(userAuthentication);

            }
        }
    };

    private void getAuthenticationToken(UserAuthentication userAuthentication) {
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("username", userAuthentication.getUsername());
        jsonParams.put("password", userAuthentication.getPassword());
        Log.e(mLOG_TAG, "Sending data " + jsonParams.values());
        final RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams)).toString());

        ApiUtil.getServiceClass().getAuthenticationToken(body).enqueue(new Callback<AuthenticationTokenJWT>() {
            @Override
            public void onResponse(Call<AuthenticationTokenJWT> call, Response<AuthenticationTokenJWT> response) {
                if (response.isSuccessful()) {
                    AuthenticationTokenJWT mAuthenticationTokenJWT = response.body();
                    HostelohaUtils.setAuthenticationToken(mAuthenticationTokenJWT.getJwt());
                    Toast.makeText(getActivity(), mAuthenticationTokenJWT.getJwt()+"", Toast.LENGTH_SHORT).show();
                } else {
                    String mPopMessage = null;
                    try {
                        mPopMessage = "INVALID_DATA " + response.body();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getActivity(), mPopMessage, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AuthenticationTokenJWT> call, Throwable t) {
                Log.e(mLOG_TAG, "Unable to submit post to API.");
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
}
