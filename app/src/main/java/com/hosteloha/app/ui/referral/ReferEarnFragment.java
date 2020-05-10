package com.hosteloha.app.ui.referral;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.BuildConfig;
import com.hosteloha.R;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.utils.HostelohaUtils;
import com.hosteloha.databinding.FragmentReferEarnBinding;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReferEarnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReferEarnFragment extends Fragment {
    private FragmentReferEarnBinding mFragmentBinding = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReferEarnFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReferEarnFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReferEarnFragment newInstance(String param1, String param2) {
        ReferEarnFragment fragment = new ReferEarnFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_refer_earn, container, false);
        mFragmentBinding.shareNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hosto");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                    HostelohaUtils.showSnackBarNotification(getActivity(),
                            "PlayStore is not installed");
                    HostelohaLog.debugOut("Playstore not installed");
                }
            }
        });
        return mFragmentBinding.getRoot();
    }
}
