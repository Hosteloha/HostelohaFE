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
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.hosteloha.R;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.QueryResponse;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.ui.seller.adapter.CustomPageAdapter;
import com.hosteloha.app.utils.HostelohaUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.hosteloha.app.utils.Define.REQUEST_CODE_ACTION_GET_CONTENT;
import static com.hosteloha.app.utils.Define.REQUEST_CODE_ACTION_IMAGE_CAPTURE;
import static com.hosteloha.app.utils.Define.REQUEST_CODE_CAMERA_PERMISSION;
import static com.hosteloha.app.utils.Define.REQUEST_CODE_READ_STORAGE_PERMISSION;

public class SellerFragment extends Fragment {

    private SellerViewModel sellerViewModel;

    EditText mProductTitleText, mProductSpecificTags;
    Button mNextBtn;
    Button mPrevBtn;
    ImageButton mUploadPhotoesBtn;
    LinearLayout mLL_uploadPhotoes;
    AutoCompleteTextView mProductCategoriesDropDown, mProductConditionDropDown;
    ChipGroup mProductTagsChipGroup, mProductCategoriesChipGroup;

    ViewPager mViewPager;
    View mView_Page1, mView_Page2, mView_Page3;
    private CustomPageAdapter pageadapter;
    private ProgressDialog mProgress;
    private String mLOG_TAG = SellerFragment.class.getSimpleName();


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
        mNextBtn.setAlpha(.1f);
        mPrevBtn = (Button) root.findViewById(R.id.seller_prev_btn);

        mViewPager = root.findViewById(R.id.seller_viewpager);
        pageadapter = new CustomPageAdapter(inflater);

        mView_Page1 = inflater.inflate(R.layout.seller_page1, null);
        mView_Page2 = inflater.inflate(R.layout.seller_page2, null);
        mView_Page3 = inflater.inflate(R.layout.seller_page3, null);
        pageadapter.insertView(mView_Page1);
/*
        Commented to avoid swipe left and right functionality when view switching
        pageadapter.insertView(mView_Page2);
        pageadapter.insertView(mView_Page3);*/

        mViewPager.setAdapter(pageadapter);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        //issue - #10 added below changes to remain 3 pages of view pager to be alive
        mViewPager.setOffscreenPageLimit(2);

        mLL_uploadPhotoes = mView_Page1.findViewById(R.id.page1_ll_upload_photoes);
        mUploadPhotoesBtn = mView_Page1.findViewById(R.id.page1_ib_upload_photoes);
        mProductTitleText = mView_Page1.findViewById(R.id.page1_et_title);
        mProductCategoriesDropDown = mView_Page1.findViewById(R.id.page1_dropdown_product_categories);
        mProductConditionDropDown = mView_Page1.findViewById(R.id.page2_dropdown_product_condition);
        mProductSpecificTags = mView_Page1.findViewById(R.id.page1_et_product_tags);
        mProductTagsChipGroup = mView_Page1.findViewById(R.id.chipGroupProductTags);
        mProductCategoriesChipGroup = mView_Page1.findViewById(R.id.chipGroupProductCategories);
        mProductSpecificTags.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if ((textView != null) && (textView.getText().length() > 0)) {
                        addChipToGroup(textView.getText(), mProductTagsChipGroup, false);
                    }
                    textView.setText(null);
                }
                return false;
            }
        });

        mProductCategoriesDropDown.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if ((textView != null) && (textView.getText().length() > 0)) {
                        CharSequence enteredText = textView.getText();
                        if (checkifEnteredItemsIsPresentInDropdown(mProductCategoriesDropDown.getAdapter(), enteredText.toString())) {
                            addChipToGroup(textView.getText(), mProductCategoriesChipGroup, true);
                        }
                    }
                    textView.setText(null);
                }
                return false;
            }
        });
        // TODO :: Adding default chip for testing purpose, later remove it
        addChipToGroup("#handmade", mProductTagsChipGroup, false);


        mPrevBtn.setOnClickListener(mOnClickListener);
        mNextBtn.setOnClickListener(mOnClickListener);
        mUploadPhotoesBtn.setOnClickListener(mOnClickListener);

        String[] PRODUCT_CATEGORIES = getContext().getResources().getStringArray(R.array.product_categories);
        addDropDownData(mProductCategoriesDropDown, PRODUCT_CATEGORIES);
        //To dismiss the dropdown after user selects
        mProductCategoriesDropDown.post(new Runnable() {
            @Override
            public void run() {
                mProductCategoriesDropDown.dismissDropDown();
            }
        });
        String[] PRODUCT_CONDITION = getContext().getResources().getStringArray(R.array.product_condition);
        addDropDownData(mProductConditionDropDown, PRODUCT_CONDITION);

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

    private void addChipToGroup(CharSequence text, final ChipGroup chipGroup, boolean isHeirarchyEnabled) {
        final Chip chip = new Chip(getContext());
        chip.setText(text);
        chip.setCloseIconVisible(true);
        chip.setTextAppearance(R.style.modifiedEditText);
        chip.setChipIconTintResource(R.color.greyish);
        if (isHeirarchyEnabled) {
            if (chipGroup != null) {
                if (chipGroup.getChildCount() == 0) {
                    //then no root icon
                } else if (chipGroup.getChildCount() > 0) {
                    // then add subroot dir icon
                    chip.setChipIcon(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_subdirectory_arrow_right_black_24dp));
                }
            }
        }
        // necessary to get single selection working
        chip.setClickable(false);
        chip.setCheckable(false);
        chipGroup.addView(chip);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup.removeView(chip);
            }
        });
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.seller_next_btn:
                    if (mNextBtn.getText().equals("Submit")) {
                        final String mProductTitle = mProductTitleText.getText() + "_" + HostelohaUtils.getCurrentDateTime();

                        final ProductObject mProductObject = new ProductObject();
                        mProductObject.setSubtitle("test product");
                        mProductObject.setDescription("test Description");
                        mProductObject.setUsers_id(2);
                        mProductObject.setCondition_id(1);
                        mProductObject.setCategory_id(1);
                        mProductObject.setDelivery_format_id(1);
                        mProductObject.setPayment_option_id(1);
                        mProductObject.setSelling_format_id(1);

                        final Map<String, Object> jsonParams = new ArrayMap<>();

                        jsonParams.put("subtitle", mProductObject.getSubtitle());
                        jsonParams.put("description", mProductObject.getDescription());
                        jsonParams.put("category", "Electronics");
                        jsonParams.put("subcategory1", "Mobiles");
                        jsonParams.put("subcategory2", "Samsung");
                        jsonParams.put("users_id", 2);
                        jsonParams.put("condition_id", mProductObject.getCondition_id());
                        jsonParams.put("delivery_format_id", mProductObject.getDelivery_format_id());
                        jsonParams.put("payment_option_id", mProductObject.getPayment_option_id());
                        jsonParams.put("selling_format_id", mProductObject.getSelling_format_id());

                        final RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                                (new JSONObject(jsonParams)).toString());
                        try {
                            final RequestBody copy = body;
                            final Buffer buffer = new Buffer();
                            copy.writeTo(buffer);

                            Log.d("Suhaas", " body :: " + buffer.readUtf8());
                        } catch (final IOException e) {

                        }

                        new AlertDialog.Builder(getContext())
                                .setTitle(mProductTitle)
                                .setMessage("Are you sure you want to upload the product ? " + mProductTitle)
                                .setIcon(android.R.drawable.ic_menu_info_details)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        mProgress.show();
                                        sendPost(body);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    }

/*
                    To add the functionality to previous and next button
                    if (mViewPager != null)
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
*/

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

    /**
     * To add the dropdown data, we pass the particular dropdown and the data to be filled.
     *
     * @param editTextFilledExposedDropdown
     * @param dropDownData
     */
    private void addDropDownData(AutoCompleteTextView editTextFilledExposedDropdown, String[] dropDownData) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, dropDownData);
        editTextFilledExposedDropdown.setAdapter(adapter);

    }

    private boolean checkifEnteredItemsIsPresentInDropdown(ListAdapter adapter, String enteredText) {
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (enteredText.equalsIgnoreCase(adapter.getItem(i).toString())) {
                    return true;
                }
            }
        }
        Toast.makeText(getActivity(), "Not found in list", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void showUploadPhotoesAlertDilogue() {

        CharSequence[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose from ..");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                dialogInterface.dismiss();
                switch (position) {
                    case 0:
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            if (PermissionChecker.checkSelfPermission(getContext(),
                                    (Manifest.permission.CAMERA)) == PermissionChecker.PERMISSION_DENIED) {
                                String[] permissions = {Manifest.permission.CAMERA};
                                requestPermissions(permissions, REQUEST_CODE_CAMERA_PERMISSION);
                            } else {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, REQUEST_CODE_ACTION_IMAGE_CAPTURE);
                            }
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, REQUEST_CODE_ACTION_IMAGE_CAPTURE);
                        }
                        break;
                    case 1:
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            if (PermissionChecker.checkSelfPermission(getContext(),
                                    Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED) {
                                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                                requestPermissions(permissions, REQUEST_CODE_READ_STORAGE_PERMISSION);
                            } else {
                                pickImageFromGallery();
                            }
                        } else {
                            pickImageFromGallery();
                        }
                        break;
                }
            }
        });

        builder.show();

    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_CODE_ACTION_GET_CONTENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ACTION_GET_CONTENT) {
            if (data.getData() != null) {
                ImageView imgView = new ImageView(getContext());
                imgView.setLayoutParams(mUploadPhotoesBtn.getLayoutParams());
                imgView.setImageURI(data.getData());
                imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                mLL_uploadPhotoes.addView(imgView);
            } else {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ImageView imgView = new ImageView(getContext());
                    imgView.setLayoutParams(mUploadPhotoesBtn.getLayoutParams());
                    imgView.setImageURI(clipData.getItemAt(i).getUri());
                    imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    mLL_uploadPhotoes.addView(imgView);
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ACTION_IMAGE_CAPTURE) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            ImageView imgView = new ImageView(getContext());
            imgView.setLayoutParams(mUploadPhotoesBtn.getLayoutParams());
            imgView.setImageBitmap(bitmap);
            imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mLL_uploadPhotoes.addView(imgView);
        }
    }

    private void sendPost(RequestBody body) {
        ApiUtil.getServiceClass().uploadProduct(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mProgress.dismiss();
                if (response.isSuccessful()) {
                    String getmQueryStatus = null;
                    try {
                        getmQueryStatus = response.body().string();
                        String getmQueryError = response.body().string();
                        Toast.makeText(getActivity(), getmQueryStatus + " : " + getmQueryError, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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