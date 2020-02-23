package com.hosteloha.app.ui.login;


import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.hosteloha.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainLoginFragment extends Fragment {

    Button mReuestOtpBtn;
    Button mSubmitOtpBtn;
    TextView mOTPSentVariableNumber;
    EditText mPhoneNumberEditText;
    CardView mPhoneNumberView;
    CardView mOtpView;
    PinView otpPin;

    NavController navController;
    AlertDialog.Builder mBuilder;

    String phoneNum = "";
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_send_otp) {
                if (mPhoneNumberEditText != null)
                    phoneNum = mPhoneNumberEditText.getText().toString();
                if (phoneNum.length() == 10) {
                    mPhoneNumberView.setVisibility(View.GONE);
                    mOtpView.setVisibility(View.VISIBLE);
                    String OTPSentTo = getResources().getString(R.string.otp_sent_variable_phone, phoneNum);
                    mOTPSentVariableNumber.setText(OTPSentTo);
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
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login_main, container, false);
        mPhoneNumberView = root.findViewById(R.id.send_otp_view);
        mOtpView = root.findViewById(R.id.verify_otp_view);
        mPhoneNumberEditText = root.findViewById(R.id.et_number);
        mOTPSentVariableNumber = root.findViewById(R.id.tv_otp_received);
        mPhoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                processButtonByTextLength(mPhoneNumberEditText.getId());
            }
        });
        mPhoneNumberEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                // Get key action, up or down.
                int action = keyEvent.getAction();
                if (action == KeyEvent.ACTION_UP) {
                    processButtonByTextLength(mPhoneNumberEditText.getId());
                }
                return false;
            }
        });

        mReuestOtpBtn = root.findViewById(R.id.btn_send_otp);
        mReuestOtpBtn.setOnClickListener(onClickListener);
        enableButton(mReuestOtpBtn, false);
        mSubmitOtpBtn = root.findViewById(R.id.btn_submit);
        mSubmitOtpBtn.setOnClickListener(onClickListener);
        enableButton(mSubmitOtpBtn, false);
        otpPin = root.findViewById(R.id.pinView);
        otpPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                processButtonByTextLength(otpPin.getId());
            }
        });
        otpPin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                // Get key action, up or down.
                int action = keyEvent.getAction();
                if (action == KeyEvent.ACTION_UP) {
                    processButtonByTextLength(otpPin.getId());
                }
                return false;
            }
        });
        mBuilder = new AlertDialog.Builder(getContext());
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        return root;
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
        isCorrect = otpPin.getText().toString().equals(phoneNum.subSequence(phoneNum.length() - 4, phoneNum.length()));
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
                        mPhoneNumberView.setVisibility(View.VISIBLE);
                        mOtpView.setVisibility(View.GONE);
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
                String inputText = mPhoneNumberEditText.getText().toString();
                enableButton(mReuestOtpBtn, (inputText.length() >= 10));
                break;
            case R.id.pinView:
                String inputPin = otpPin.getText().toString();
                enableButton(mSubmitOtpBtn, (inputPin.length() >= 4));
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
}
