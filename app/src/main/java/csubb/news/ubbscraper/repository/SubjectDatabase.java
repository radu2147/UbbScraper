package csubb.news.ubbscraper.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import csubb.news.ubbscraper.models.Subject;

@Database(entities = {Subject.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class SubjectDatabase extends RoomDatabase {

    private static final String dbName = "Subject_table";
    public static final Object LOCK = new Object();
    private static SubjectDatabase sInstance;

    public static SubjectDatabase newInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context, SubjectDatabase.class, dbName)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract SubjectDAO getDao();
}
