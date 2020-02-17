package com.hosteloha.app.ui.login;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.hosteloha.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainLoginFragment extends Fragment {

    Button mReuestOtpBtn;
    Button mSubmitOtpBtn;
    EditText mPhoneNumberEditText;
    LinearLayout mPhoneNumberView;
    LinearLayout mOtpView;
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
        mReuestOtpBtn = root.findViewById(R.id.btn_send_otp);
        mReuestOtpBtn.setOnClickListener(onClickListener);
        mSubmitOtpBtn = root.findViewById(R.id.btn_submit);
        mSubmitOtpBtn.setOnClickListener(onClickListener);
        otpPin = root.findViewById(R.id.pinView);

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

}
