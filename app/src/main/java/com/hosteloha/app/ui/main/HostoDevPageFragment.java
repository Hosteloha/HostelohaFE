package com.hosteloha.app.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hosteloha.R;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.databinding.FragmentHostoDevpageBinding;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostoDevPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostoDevPageFragment extends Fragment {

    private FragmentHostoDevpageBinding mFgmtBinding = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HostoDevPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HostoDevPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HostoDevPageFragment newInstance(String param1, String param2) {
        HostoDevPageFragment fragment = new HostoDevPageFragment();
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
        mFgmtBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_hosto_devpage, container, false);


        initListener();
        // Inflate the layout for this fragment
        return mFgmtBinding.getRoot();
    }

    private void initListener() {
        // Profile - 1
        mFgmtBinding.devpageProfileSrinivas.devpageBtnGithubProfile1.setOnClickListener(mOnClickListener);
        mFgmtBinding.devpageProfileSrinivas.devpageBtnLinkedInProfile1.setOnClickListener(mOnClickListener);
        // Profile - 2
        mFgmtBinding.devpageProfileVinay.devpageBtnGithubProfile2.setOnClickListener(mOnClickListener);
        mFgmtBinding.devpageProfileVinay.devpageBtnLinkedInProfile2.setOnClickListener(mOnClickListener);
        // Profile - 3
        mFgmtBinding.devpageProfileSuhaas.devpageBtnGithubProfile3.setOnClickListener(mOnClickListener);
        mFgmtBinding.devpageProfileSuhaas.devpageBtnLinkedInProfile3.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String url = null;
            if (view.getId() == mFgmtBinding.devpageProfileSrinivas.devpageBtnLinkedInProfile1.getId()) {
                url = getResources().getString(R.string.dev_srinivas_github);
            } else if (view.getId() == mFgmtBinding.devpageProfileSrinivas.devpageBtnLinkedInProfile1.getId()) {
                url = getResources().getString(R.string.dev_srinivas_linkedin);
            } else if (view.getId() == mFgmtBinding.devpageProfileVinay.devpageBtnGithubProfile2.getId()) {
                url = getResources().getString(R.string.dev_vinay_github);
            } else if (view.getId() == mFgmtBinding.devpageProfileVinay.devpageBtnLinkedInProfile2.getId()) {
                url = getResources().getString(R.string.dev_vinay_linkedin);
            } else if (view.getId() == mFgmtBinding.devpageProfileSuhaas.devpageBtnGithubProfile3.getId()) {
                url = getResources().getString(R.string.dev_suhaas_github);
            } else if (view.getId() == mFgmtBinding.devpageProfileSuhaas.devpageBtnGithubProfile3.getId()) {
                url = getResources().getString(R.string.dev_suhaas_linkedin);
            }
            if (url != null) {
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        }
    };
}