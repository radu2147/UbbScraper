package com.example.ubbscraper.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ubbscraper.models.Subject;

import java.util.List;

public class SubjectViewModel extends ViewModel {
    private LiveData<List<Subject>> lista;
    public LiveData<List<Subject>> get_live_data(final Context context){
        if(lista == null){

            lista = SubjectDatabase.newInstance(context).getDao().get_all();
        }
        return lista;
    }
}
