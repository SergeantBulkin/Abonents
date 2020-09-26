package by.bsuir.kulinka.abonents;

import android.app.Application;

import androidx.room.Room;
import by.bsuir.kulinka.abonents.room.AbonentsDatabase;

public class App extends Application
{
    private static App instance;

    private AbonentsDatabase database;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AbonentsDatabase.class, "abonents")
                .build();
    }

    public static App getInstance()
    {
        return instance;
    }

    public AbonentsDatabase getDatabase()
    {
        return database;
    }
}
