package com.hosteloha.app.ui.seller;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hosteloha.R;
import com.hosteloha.app.datarepository.beans.ProductObject;
import com.hosteloha.app.datarepository.beans.SellerProductObject;
import com.hosteloha.app.datarepository.retroapi.ApiUtil;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.service.HostelohaService;
import com.hosteloha.app.utils.AppSharedPrefs;
import com.hosteloha.app.utils.Define;
import com.hosteloha.app.utils.HostelohaUtils;
import com.hosteloha.app.utils.ImageWindow;
import com.hosteloha.databinding.FragmentSellerBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
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
    private FragmentSellerBinding mSellerBinding;
    private Activity mActivity = getActivity();
    private HostelohaService mHostelohaService = null;
    private String LOG_TAG = SellerFragment.class.getSimpleName();

    private EditText mProductTitleText, mProductSubTitleText, mProductSpecificTags, mProductDescriptionText, mCostPrice, mSelleingPrice;
    private Button mNextBtn;
    private ImageButton mUploadPhotosBtn;
    private LinearLayout mLL_uploadPhotos;

    private AutoCompleteTextView mProductCategoriesDropDown, mProductConditionDropDown;
    private ChipGroup mProductTagsChipGroup, mProductCategoriesChipGroup;

    private ProgressDialog mProgress;

    private Map<String, Set<String>> mAllCategoriesMap = null;
    private String mLOG_TAG = SellerFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mSellerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller, container, false);

        AppSharedPrefs.storeCurrentViewTypeInPrefs(mActivity, Define.VIEW_SELLER);

        sellerViewModel = ViewModelProviders.of(getActivity()).get(SellerViewModel.class);
        View root = mSellerBinding.sellerRoot;

        mNextBtn = root.findViewById(R.id.seller_next_btn);
        mLL_uploadPhotos = root.findViewById(R.id.page1_ll_upload_photos);
        mUploadPhotosBtn = root.findViewById(R.id.page1_ib_upload_photos);
        mProductTitleText = root.findViewById(R.id.page1_et_title);
        mProductSubTitleText = root.findViewById(R.id.page1_et_subtitle);
        mProductDescriptionText = root.findViewById(R.id.page2_et_description);
        mCostPrice = root.findViewById(R.id.page2_et_product_cost);
        mSelleingPrice = root.findViewById(R.id.page2_et_selling_cost);
        mProductCategoriesDropDown = root.findViewById(R.id.page1_dropdown_product_categories);
        mProductConditionDropDown = root.findViewById(R.id.page2_dropdown_product_condition);
        mProductSpecificTags = root.findViewById(R.id.page1_et_product_tags);
        mProductTagsChipGroup = root.findViewById(R.id.chipGroupProductTags);
        mProductCategoriesChipGroup = root.findViewById(R.id.chipGroupProductCategories);
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

        mProductCategoriesDropDown.setOnClickListener(mOnClickListener);
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
                            refreshProductCategoryDropDown();
                        }
                    }
                }
                return false;
            }
        });
        // TODO :: Adding default chip for testing purpose, later remove it
        addChipToGroup("#handmade", mProductTagsChipGroup, false);

        mNextBtn.setOnClickListener(mOnClickListener);
        mUploadPhotosBtn.setOnClickListener(mOnClickListener);

        mProductCategoriesDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String) adapterView.getItemAtPosition(i);
                if (checkifEnteredItemsIsPresentInDropdown(mProductCategoriesDropDown.getAdapter(), selection)) {
                    addChipToGroup(selection, mProductCategoriesChipGroup, true);
                    refreshProductCategoryDropDown();
                }
            }
        });

        if (mActivity != null) {
            String[] PRODUCT_CONDITION = mActivity.getResources().getStringArray(R.array.product_condition);
            addDropDownData(mProductConditionDropDown, PRODUCT_CONDITION);
        }

        //Progress dialog
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.setCanceledOnTouchOutside(true);
        mProgress.setCancelable(true);

        sellerViewModel.getCategoriesListLiveData().observe(getViewLifecycleOwner(), new Observer<Map<String, Set<String>>>() {
            @Override
            public void onChanged(Map<String, Set<String>> categoriesMap) {
                mAllCategoriesMap = categoriesMap;
                fetchSubCategory("root");
            }
        });
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        AppSharedPrefs.storeCurrentViewTypeInPrefs(getContext(), Define.VIEW_SELLER);
    }

    private void fetchSubCategory(String category) {

        String[] categoriesList = new String[0];
        if (mAllCategoriesMap != null) {
            categoriesList = mAllCategoriesMap.get(category).toArray(new String[0]);
            addDropDownData(mProductCategoriesDropDown, categoriesList);
        } else if (sellerViewModel != null)
            sellerViewModel.req_getCategoryMapList();
    }

    private void refreshProductCategoryDropDown() {
        //Here setting the adapter to null, so that the old data does not persist and cant be entered
        mProductCategoriesDropDown.setAdapter(null);
        int chipGroupSize = mProductCategoriesChipGroup.getChildCount();
        if (chipGroupSize > 0 && chipGroupSize < 3) {
            //Get more subcategories
            Chip lastAddedChip = (Chip) mProductCategoriesChipGroup.getChildAt(chipGroupSize - 1);
            fetchSubCategory(lastAddedChip.getText().toString());
            mProductCategoriesDropDown.setText(null);
            mProductCategoriesDropDown.setHint("Choose SubCategory");
        } else if (chipGroupSize >= 3) {
            mProductCategoriesDropDown.setHint("limit reached");
            mProductCategoriesDropDown.setEnabled(false);
        } else if (chipGroupSize == 0) {
            fetchSubCategory("root");
        }
    }

    private void addChipToGroup(CharSequence text, final ChipGroup chipGroup, boolean isHeirarchyEnabled) {
        final Chip chip = new Chip(getContext());
        chip.setText(text);
        chip.setCloseIconVisible(true);
        chip.setTextAppearance(R.style.modifiedEditText);
        chip.setChipIconTintResource(R.color.primary500);
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
                int position = -1;
                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    Chip eachChip = (Chip) chipGroup.getChildAt(i);
                    if (eachChip.equals(chip)) {
                        position = i;
                        break;
                    }
                }

                if (position != -1) {
                    chipGroup.removeViews(position, chipGroup.getChildCount() - position);
                }

                refreshProductCategoryDropDown();
            }
        });
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.seller_next_btn:
                    if (mNextBtn.getText().equals("Submit")) {
                        final SellerProductObject productObject = getProductDataToUpload();
                        Log.d(LOG_TAG, "  Submit button onClick  ------------->   " + productObject.toString());
                        new AlertDialog.Builder(getContext())
                                .setTitle(productObject.getTitle())
                                .setMessage("Are you sure you want to upload the product ? " + productObject.getTitle())
                                .setIcon(android.R.drawable.ic_menu_info_details)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        mProgress.show();
                                        sendPost(productObject);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    }

                    break;
                case R.id.page1_ib_upload_photos:
                    showUploadPhotoesAlertDilogue();
                    break;

                case R.id.page1_dropdown_product_categories:
                    HostelohaLog.debugOut(" ProductCategoriesDropDown  clicked ...!");
                    if (mProductCategoriesDropDown != null && mProductCategoriesDropDown.getAdapter() == null
                            || (mProductCategoriesDropDown.getAdapter() != null && mProductCategoriesDropDown.getAdapter().getCount() == 0))
                        refreshProductCategoryDropDown();
                    break;
            }
        }
    };

    /**
     * To validate the field of the data between different form fields.
     * So that the user doesn't upload incorrect or other publicly bad language
     *
     * @param mViewType
     * @param inputFieldPosition
     * @return
     */
    private boolean validateDataBeforeUpload(Object mViewType, int inputFieldPosition) {
        if (mViewType != null) {
            if (mViewType instanceof EditText) {
                if (((EditText) mViewType).getText().length() > 0) {
                    return true;
                }
            }

            if (mViewType instanceof Chip) {
                // TODO :: Check if is
            }
        }
        HostelohaUtils.showSnackBarNotification(mActivity, "FIELD INVALID :: " + inputFieldPosition);
        return false;
    }

    private SellerProductObject getProductDataToUpload() {
        final SellerProductObject mProductObject = new SellerProductObject();

        final int SELLER_PRODUCT_FIELD_TITLE = 1;
        final int SELLER_PRODUCT_FIELD_SUBTITLE = SELLER_PRODUCT_FIELD_TITLE + 1;
        final int SELLER_PRODUCT_FIELD_DESCRIPTION = SELLER_PRODUCT_FIELD_SUBTITLE + 1;

        if (validateDataBeforeUpload(mProductTitleText, SELLER_PRODUCT_FIELD_TITLE)) {
            mProductObject.setTitle(mProductTitleText.getText().toString());
        }

        if (validateDataBeforeUpload(mProductSubTitleText, SELLER_PRODUCT_FIELD_SUBTITLE)) {
            mProductObject.setSubtitle(mProductSubTitleText.getText().toString());
        }

        if (validateDataBeforeUpload(mProductDescriptionText, SELLER_PRODUCT_FIELD_DESCRIPTION)) {
            mProductObject.setDescription(mProductDescriptionText.getText().toString());
        }

        if (mProductCategoriesChipGroup != null && mProductCategoriesChipGroup.getChildCount() > 0) {
            for (int i = 0; i < mProductCategoriesChipGroup.getChildCount(); i++) {
                Chip eachChip = (Chip) mProductCategoriesChipGroup.getChildAt(i);
                String chipText = eachChip.getText().toString();
                if (i == 0) {
                    mProductObject.setMainCategory(chipText);
                } else if (i == 1) {
                    mProductObject.setSubCategory1(chipText);
                } else if (i == 2) {
                    mProductObject.setSubCategory2(chipText);
                }
            }
        } else {
            // Category not chosen
            // TODO:: to add the product in default category. (CATEGORY :: OTHERS)
        }

        /**
         * TODO :: Yet to configure for these fields, since data is not correct.
         */
        mProductObject.setUsers_id(61);
        mProductObject.setCondition_id(1);
        mProductObject.setDelivery_format_id(1);
        mProductObject.setPayment_option_id(1);
        mProductObject.setSelling_format_id(1);
        int cp = 0;
        if (!"".equals(mCostPrice.getText().toString())) {
            cp = Integer.parseInt(mCostPrice.getText().toString());
        }
        mProductObject.setCostPrice(cp);

        int sp = 0;
        if (!"".equals(mSelleingPrice.getText().toString())) {
            sp = Integer.parseInt(mSelleingPrice.getText().toString());
        }
        mProductObject.setSellingPrice(sp);

        return mProductObject;
    }

    /**
     * To add the dropdown data, we pass the particular dropdown and the data to be filled.
     *
     * @param editTextFilledExposedDropdown
     * @param dropDownData
     */
    private void addDropDownData(AutoCompleteTextView editTextFilledExposedDropdown, String[] dropDownData) {
        if (dropDownData != null && dropDownData.length > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_menu_popup_item, dropDownData);
            editTextFilledExposedDropdown.setAdapter(adapter);
        }
    }

    private boolean checkifEnteredItemsIsPresentInDropdown(ListAdapter adapter, String enteredText) {
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (enteredText.equalsIgnoreCase(adapter.getItem(i).toString())) {
                    return true;
                }
            }
        }
        HostelohaUtils.showSnackBarNotification(mActivity, "Item not found in list");
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
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ACTION_GET_CONTENT && data != null
                && data.getData() != null) {
            Uri fileURI = data.getData();
            if (fileURI != null) {
                addViewToUploadPhotosViewer(fileURI);
            } else {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    int clipCount = clipData.getItemCount();
                    for (int i = 0; i < clipCount; i++) {
                        fileURI = clipData.getItemAt(i).getUri();
                        addViewToUploadPhotosViewer(fileURI);
                    }
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ACTION_IMAGE_CAPTURE && data != null) {
            //TODO :: Store image in the directory and send the URI to the firebase (@Contact :: Suhaas)
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            ImageView imgView = new ImageView(getContext());
            imgView.setLayoutParams(mUploadPhotosBtn.getLayoutParams());
            imgView.setImageBitmap(bitmap);
            imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mLL_uploadPhotos.addView(imgView);
        }
    }

    private void addViewToUploadPhotosViewer(Uri fileURI) {
        if (fileURI != null) {
            final ImageWindow imgView = new ImageWindow(new ContextThemeWrapper(getContext(), R.style.image_window_style), null, 0);
//                imgView.setLayoutParams(mUploadPhotosBtn.getLayoutParams());
            imgView.getImageView().setImageURI(fileURI);
            // Storing the image path so that we can upload while upload file to FireBase
            imgView.setTag(fileURI);
            imgView.setOnCloseListener(new ImageWindow.OnCloseListener() {
                @Override
                public void onCloseClick(final View imageWindow) {
                    imageWindow.animate().scaleY(0).scaleX(0).setDuration(200).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            ((ViewGroup) imgView.getParent()).removeView(imgView);
                        }
                    }).start();
                }
            });
            mLL_uploadPhotos.addView(imgView);
        }
    }

    private void sendPost(SellerProductObject productObject) {
        ApiUtil.getServiceClass().uploadProduct(productObject, HostelohaUtils.getAuthenticationToken()).enqueue(new Callback<ProductObject>() {
            @Override
            public void onResponse(Call<ProductObject> call, Response<ProductObject> response) {
                mProgress.dismiss();
                if (response.isSuccessful()) {
                    ProductObject productObject = response.body();
                    if (productObject != null) {
                        int productID = productObject.getProductId();
                        String productTitle = productObject.getTitle();
                        String dialogMessage = "Hurrayy!!! Your product " + productTitle + " - " + productID
                                + " is uploaded";
                        showDialogPopUpProductUploaded(dialogMessage);
                        // To upload images with the product id
                        uploadPhotosInBackGround(productID);
                    }
                } else {
                    String mPopMessage = null;
                    try {
                        mPopMessage = "INVALID_DATA " + response.body();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    HostelohaUtils.showSnackBarNotification(getActivity(), mPopMessage);
                }
            }

            @Override
            public void onFailure(Call<ProductObject> call, Throwable t) {
                Log.e(mLOG_TAG, "Unable to submit post to API.");
            }
        });
    }

    /**
     * To get the file paths and upload
     *
     * @param productID
     */
    private void uploadPhotosInBackGround(int productID) {
        List<Uri> filesURI = new ArrayList<>();
        for (int i = 0; i < mLL_uploadPhotos.getChildCount(); i++) {
            View view = mLL_uploadPhotos.getChildAt(i);
            Uri fileURI = (Uri) view.getTag();
            HostelohaLog.debugOut("File fileURI :: " + fileURI);
            filesURI.add(fileURI);
        }

        // Upload the list
        if (mHostelohaService != null) {
            mHostelohaService.uploadProductImagesToFire(filesURI, productID);
        } else {
            HostelohaLog.debugOut(" uploadPhotosInBackGround :: service NULL ");
        }

    }

    private void showDialogPopUpProductUploaded(String message) {
        new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogTheme)
                .setTitle("Product Uploaded")
                .setMessage(message)
                .setPositiveButton("Upload new product", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Navigation.findNavController(getView()).navigate(R.id.nav_seller);
                    }
                })
                .setNeutralButton("Preview Product", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Navigation.findNavController(getView()).navigate(R.id.nav_buyer);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Keep the values same
                    }
                })
                .setCancelable(false)
                .show();

    }
}