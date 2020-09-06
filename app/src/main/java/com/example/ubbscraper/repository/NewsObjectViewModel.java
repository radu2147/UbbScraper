package com.example.ubbscraper.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ubbscraper.models.NewsObject;

import java.util.List;

public class NewsObjectViewModel extends ViewModel {
    private LiveData<List<NewsObject>> instance;

    public LiveData<List<NewsObject>> getInstance(Context context) {
        if(instance == null){
            instance = NewsDatabase.getInstance(context).getDao().get_all();
        }
        return instance;
    }
}
