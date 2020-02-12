package com.hosteloha.app.ui.login;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.hosteloha.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class SignUpFragment extends Fragment {
    EditText date;
    Button submit;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_signup) {
                // ToDo Validate details and send to server
                final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_signUpFragment_to_nav_login);
            }
        }
    };
    private DatePickerDialog datePickerDialog;
    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.getId() == R.id.date && hasFocus)
                datePickerDialog.show();
        }
    };
    private SimpleDateFormat dateFormatter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);
        submit = root.findViewById(R.id.btn_signup);
        submit.setOnClickListener(onClickListener);

        date = (EditText) root.findViewById(R.id.date);
        date.setOnFocusChangeListener(onFocusChangeListener);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setDateTimeField();
        return root;
    }

    private void setDateTimeField() {
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
            }

        }, 1995, 6, Calendar.getInstance().get(Calendar.DATE));

    }
}