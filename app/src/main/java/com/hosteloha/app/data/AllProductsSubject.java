package com.hosteloha.app.data;

import com.hosteloha.app.beans.ProductObject;

import java.util.ArrayList;
import java.util.List;

public class AllProductsSubject {

    private static AllProductsSubject mAllProductsSubject = new AllProductsSubject();
    private List<ProductObject> mProductsList = new ArrayList<>();
    private List<ProductsObserver> observers = new ArrayList<>();

    private AllProductsSubject() {
    } //Singleton class

    public static AllProductsSubject getAllProductsSubject() {
        return mAllProductsSubject;
    }

    public List<ProductObject> getProductsList() {
        return mProductsList;
    }

    public void setProductsList(List<ProductObject> mProductsList) {
        this.mProductsList = mProductsList;
        notifyObservers();

    }

    public void detachObservers(ProductsObserver o) {
        if (observers.contains(o) == true) {
            observers.remove(o);
        }
    }

    public void attachObservers(ProductsObserver o) {
        if (observers.contains(o) == false) {
            observers.add(o);
        }
    }

    public void notifyObservers() {
        for (ProductsObserver o : observers) {
            o.onProductsChanged(mProductsList);
        }
    }

    public interface ProductsObserver {
        void onProductsChanged(List<ProductObject> arrylist);
    }
}
