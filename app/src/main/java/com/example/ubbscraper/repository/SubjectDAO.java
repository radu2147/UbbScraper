package com.example.ubbscraper.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ubbscraper.models.Subject;

import java.util.List;

@Dao
public interface SubjectDAO {

    @Query("SELECT * from subjects")
    LiveData<List<Subject>> get_all();

    @Query("SELECT * from subjects")
    List<Subject> getAll();

    @Insert
    void insert(Subject obj);

    @Delete
    void delete(Subject obj);

    @Query("SELECT * from subjects WHERE id = :id")
    Subject get(int id);
}
