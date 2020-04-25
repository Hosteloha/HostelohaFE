package com.hosteloha.app.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hosteloha.app.log.HostelohaLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class AppFireDataBase {

    private static String PRODUCTS_LIST = "products";
    private static FirebaseDatabase mFireDataBase = null;
    private static DatabaseReference mProductsRef = null;

    public static FirebaseDatabase getFireBaseDBInstance() {
        if (mFireDataBase == null) {
            mFireDataBase = FirebaseDatabase.getInstance();
        }
        return mFireDataBase;
    }

    public static DatabaseReference getProductsReference() {
        if (mProductsRef == null) {
            mProductsRef = getFireBaseDBInstance().getReference(PRODUCTS_LIST);

        }
        return mProductsRef;
    }

    /**
     * editUrlList :: To change the already present URL list.
     *
     * @param productID
     * @param URlList
     */
    public static void editUrlList(String productID, ArrayList<String> URlList) {
        DatabaseReference productItem = getProductsReference().child(productID);
        if (productItem.getKey() != null) {
            String productIDFetched = productItem.getKey().toString();
            HostelohaLog.debugOut(" editUrlList :: product Images list :: " + productIDFetched);
        } else {
            HostelohaLog.debugOut(" No matching product list found - Creating NEW");
            addUrlList(productID, URlList);
        }
    }

    /**
     * To add the new URL list to the product ID.
     *
     * @param productID
     * @param URlList
     */
    public static void addUrlList(String productID, ArrayList<String> URlList) {
        HostelohaLog.debugOut(" addUrlList :: NEW URL list for PRODUCT :: " + productID +" URL's : "+URlList.size());
        Map<String, Object> productImagesUrl = new HashMap<>();
        productImagesUrl.put(productID, URlList);
        getProductsReference().updateChildren(productImagesUrl);
    }

    private static Map<String, ArrayList<String>> productImageList = new HashMap<>();

    /**
     * getProductImagesMap :: to get the whole products map with List of URL
     * @return Map<String, ArrayList < String>> with product ID and its list of URL's
     */
    public static Map<String, ArrayList<String>> getProductImagesMap() {
        getProductsReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productImageList = (Map<String, ArrayList<String>>) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                HostelohaLog.debugOut("ValueChanged onCancelled :: ");
            }
        });

        return productImageList;
    }

    private static ArrayList<String> returnUrlList = new ArrayList<>();

    /**
     * To get the URL list for specific product.
     *
     * @param productID
     * @return
     */
    public static ArrayList<String> getUrlList(final String productID) {
        getProductsReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot :
                            dataSnapshot.getChildren()) {
                        String fetchedProductID = postSnapshot.getKey();
                        if (fetchedProductID != null && fetchedProductID.equals(productID)) {
                            ArrayList<String> localUrlList = (ArrayList<String>) postSnapshot.getValue();
                            if (localUrlList != null && localUrlList.size() > 0) {
                                returnUrlList = new ArrayList<>(localUrlList);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                HostelohaLog.debugOut("ValueChanged onCancelled :: ");
            }
        });
        return returnUrlList;
    }
}
