package com.hosteloha.app.ui.seller;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hosteloha.R;
import com.hosteloha.app.beans.QueryResponse;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.ui.seller.adapter.CustomPageAdapter;
import com.hosteloha.app.utils.HostelohaUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class SellerFragment extends Fragment {

    private SellerViewModel sellerViewModel;

    EditText mProductTitleText;
    Button mNextBtn;
    Button mPrevBtn;
    ImageButton mUploadPhotoesBtn;
    LinearLayout mLL_uploadPhotoes;

    ViewPager mViewPager;
    View mView_Page1;
    View mView_Page2;
    View mView_Page3;
    private CustomPageAdapter pageadapter;
    private ProgressDialog mProgress;
    private String mLOG_TAG = SellerFragment.class.getSimpleName();

    private final int REQUEST_CODE_CAMERA_PERMISSION = 100;
    private final int REQUEST_CODE_READ_STORAGE_PERMISSION = REQUEST_CODE_CAMERA_PERMISSION + 1;

    private final int REQUEST_CODE_ACTION_IMAGE_CAPTURE = 1001;
    private final int REQUEST_CODE_ACTION_GET_CONTENT = REQUEST_CODE_ACTION_IMAGE_CAPTURE + 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sellerViewModel =
                ViewModelProviders.of(this).get(SellerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_seller, container, false);
        final TextView textView = root.findViewById(R.id.text_seller);
        sellerViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mNextBtn = (Button) root.findViewById(R.id.seller_next_btn);
        mPrevBtn = (Button) root.findViewById(R.id.seller_prev_btn);

        mViewPager = root.findViewById(R.id.seller_viewpager);
        pageadapter = new CustomPageAdapter(inflater);

        mView_Page1 = inflater.inflate(R.layout.seller_page1,null);
        mView_Page2 = inflater.inflate(R.layout.seller_page2,null);
        mView_Page3 = inflater.inflate(R.layout.seller_page3,null);
        pageadapter.insertView(mView_Page1);
        pageadapter.insertView(mView_Page2);
        pageadapter.insertView(mView_Page3);

        mViewPager.setAdapter(pageadapter);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        //issue - #10 added below changes to remain 3 pages of view pager to be alive
        mViewPager.setOffscreenPageLimit(2);

        mLL_uploadPhotoes = (LinearLayout) mView_Page1.findViewById(R.id.page1_ll_upload_photoes);
        mUploadPhotoesBtn = (ImageButton) mView_Page1.findViewById(R.id.page1_ib_upload_photoes);
        mProductTitleText = (EditText) mView_Page1.findViewById(R.id.page1_et_title);

        mPrevBtn.setOnClickListener(mOnClickListener);
        mNextBtn.setOnClickListener(mOnClickListener);
        mUploadPhotoesBtn.setOnClickListener(mOnClickListener);

        //Progress dialog
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.setCanceledOnTouchOutside(true);
        mProgress.setCancelable(true);

        return root;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.seller_next_btn:
                    if (mNextBtn.getText().equals("Submit")) {
                        final String mProductTitle = mProductTitleText.getText() + "_" + HostelohaUtils.getCurrentDateTime();

                        new AlertDialog.Builder(getContext())
                                .setTitle(mProductTitle)
                                .setMessage("Are you sure you want to upload the product ?")
                                .setIcon(android.R.drawable.ic_menu_info_details)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        mProgress.show();
                                        sendPost(mProductTitle);
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                    }

                    if (mViewPager != null)
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);

                    break;
                case R.id.seller_prev_btn:
                    if (mViewPager != null)
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                    break;
                case R.id.page1_ib_upload_photoes:
                    showUploadPhotoesAlertDilogue();
                    break;
            }
        }
    };

    private void showUploadPhotoesAlertDilogue(){

        CharSequence[] items = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose from ..");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                dialogInterface.dismiss();
                switch (position){
                    case 0:
                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            if (PermissionChecker.checkSelfPermission(getContext(),
                                    (Manifest.permission.CAMERA)) == PermissionChecker.PERMISSION_DENIED) {
                                String[] permissions = {Manifest.permission.CAMERA};
                                requestPermissions(permissions, REQUEST_CODE_CAMERA_PERMISSION);
                            } else {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent,REQUEST_CODE_ACTION_IMAGE_CAPTURE);
                            }
                        }else{
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent,REQUEST_CODE_ACTION_IMAGE_CAPTURE);
                        }
                        break;
                    case 1:
                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            if (PermissionChecker.checkSelfPermission(getContext(),
                                    Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED) {
                                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                                requestPermissions(permissions, REQUEST_CODE_READ_STORAGE_PERMISSION);
                            } else {
                                pickImageFromGallery();
                            }
                        }else{
                            pickImageFromGallery();
                        }
                        break;
                }
            }
        });

        builder.show();

    }

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent,REQUEST_CODE_ACTION_GET_CONTENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_ACTION_GET_CONTENT){
            if(data.getData() != null){
                ImageView imgView = new ImageView(getContext());
                imgView.setLayoutParams(mUploadPhotoesBtn.getLayoutParams());
                imgView.setImageURI(data.getData());
                imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                mLL_uploadPhotoes.addView(imgView);
            } else {
                ClipData clipData = data.getClipData();
                for(int i=0; i < clipData.getItemCount();i++){
                    ImageView imgView = new ImageView(getContext());
                    imgView.setLayoutParams(mUploadPhotoesBtn.getLayoutParams());
                    imgView.setImageURI(clipData.getItemAt(i).getUri());
                    imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    mLL_uploadPhotoes.addView(imgView);
                }
            }
        }else if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_ACTION_IMAGE_CAPTURE){
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            ImageView imgView = new ImageView(getContext());
            imgView.setLayoutParams(mUploadPhotoesBtn.getLayoutParams());
            imgView.setImageBitmap(bitmap);
            imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mLL_uploadPhotoes.addView(imgView);
        }
    }

    private void sendPost(String mProductTitle) {
        ApiUtil.getServiceClass().uploadProduct(mProductTitle).enqueue(new Callback<QueryResponse>() {
            @Override
            public void onResponse(Call<QueryResponse> call, Response<QueryResponse> response) {
                mProgress.dismiss();
                if (response.isSuccessful()) {
                    String getmQueryStatus = response.body().getmQueryStatus();
                    String getmQueryError = response.body().getmQueryError();
                    Toast.makeText(getActivity(), getmQueryStatus + " : " + getmQueryError, Toast.LENGTH_SHORT).show();

                } else {
                    String mPopMessage = "INVALID_DATA";
                    Toast.makeText(getActivity(), mPopMessage, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<QueryResponse> call, Throwable t) {
                Log.e(mLOG_TAG, "Unable to submit post to API.");
            }
        });
    }

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mPrevBtn != null)
                mPrevBtn.setEnabled((position == 0) ? false : true);

            if (mNextBtn != null)
                mNextBtn.setText((position == 2) ? "Submit" : "Next");
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}