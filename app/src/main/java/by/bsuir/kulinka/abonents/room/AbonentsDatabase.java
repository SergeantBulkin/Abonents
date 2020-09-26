package by.bsuir.kulinka.abonents.room;

import android.util.Log;

import by.bsuir.kulinka.abonents.model.Abonent;
import by.bsuir.kulinka.abonents.model.DisposableManager;
import by.bsuir.kulinka.abonents.model.Plan;
import by.bsuir.kulinka.abonents.model.Service;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

@Database(entities = {Abonent.class, Plan.class, Service.class}, version = 1, exportSchema = false)
public abstract class AbonentsDatabase extends RoomDatabase
{
    public abstract AbonentDAO getAbonentDAO();

    public void insertAbonent(Abonent abonent)
    {
        DisposableManager.add(getAbonentDAO()
                .insertAbonent(abonent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver()
                {
                    @Override
                    public void onComplete()
                    {
                        addToLog("insertAbonent - inserted");
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        addToLog("insertAbonent - " + e.getMessage());
                    }
                })
        );
    }

    private void addToLog(String msg)
    {
        Log.d("LOG_TAG", msg);
    }
}
