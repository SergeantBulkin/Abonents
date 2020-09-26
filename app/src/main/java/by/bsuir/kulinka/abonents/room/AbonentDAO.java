package by.bsuir.kulinka.abonents.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import by.bsuir.kulinka.abonents.model.Abonent;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface AbonentDAO
{
    @Insert
    Completable insertAbonent(Abonent abonent);

    @Delete
    Completable deleteAbonent(Abonent abonent);

    @Query("SELECT * FROM abonent")
    Single<List<Abonent>> getAllAbonents();

    @Query("SELECT * FROM abonent WHERE id = :id")
    Abonent getAbonentByID(int id);
}