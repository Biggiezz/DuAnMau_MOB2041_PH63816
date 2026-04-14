package com.example.DuAnMau_PH63816.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProductSearchViewModel extends ViewModel {

    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query == null ? "" : query);
    }
}
