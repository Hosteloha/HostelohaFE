package com.hosteloha.app.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hosteloha.app.beans.ProductImageUpload;
import com.hosteloha.app.log.HostelohaLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class AppFireDB {
    public void tobeimplemented() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("products");
        ArrayList<String> urlList = new ArrayList<String>() {{
            add("URL_1");
            add("URL_2");
        }};
        final ProductImageUpload upload = new ProductImageUpload("productID_" + System.currentTimeMillis(),
                urlList);
        String P_id = upload.getId();
        Map<String, Object> map = new HashMap<>();
        map.put(upload.getId(), urlList);
        DatabaseReference pIdinDb = myRef.child("productID_1587496307049");
        if (pIdinDb.getKey() != null) {
            HostelohaLog.debugOut(" pIdinDb :: exist " + pIdinDb.getKey().toString());
            // Change URL_index and url link
            pIdinDb.child("0").setValue("URL_3");
        }

//        myRef.updateChildren(map);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HostelohaLog.debugOut("Total children :: " + dataSnapshot.getChildrenCount());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot :
                            dataSnapshot.getChildren()) {
                        String productID = postSnapshot.getKey();
                        ArrayList<String> urlList = (ArrayList<String>) postSnapshot.getValue();
                        HostelohaLog.debugOut("Product ID :: " + productID + " \n List :: " + urlList.size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
