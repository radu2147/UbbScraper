package csubb.news.ubbscraper.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import csubb.news.ubbscraper.models.NewsObject;

import java.util.List;

@Dao
public interface NewsDAO {
    @Query("SELECT * from News")
    LiveData<List<NewsObject>> get_all();

    @Insert
    void insert(NewsObject obj);

    @Delete
    void delete(NewsObject obj);

    @Query("SELECT * from News WHERE id = :id")
    NewsObject get(int id);
}
