package csubb.news.ubbscraper.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import csubb.news.ubbscraper.models.NewsObject;

@Database(entities = {NewsObject.class}, version = 1, exportSchema = false)
@TypeConverters({NewsConverters.class})
public abstract class NewsDatabase extends RoomDatabase {
    private static final String DBNAME = "NewsDatabase";
    private static final Object lock = new Object();
    private static NewsDatabase sInstance;

    public static NewsDatabase getInstance(Context context){
        if(sInstance == null){
            synchronized (lock){
                sInstance = Room.databaseBuilder(context, NewsDatabase.class, DBNAME).build();
            }
        }
        return sInstance;
    }

    public abstract NewsDAO getDao();

}
