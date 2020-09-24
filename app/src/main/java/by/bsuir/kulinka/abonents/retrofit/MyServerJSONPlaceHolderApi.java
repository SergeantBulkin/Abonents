package by.bsuir.kulinka.abonents.retrofit;

import by.bsuir.kulinka.abonents.model.Abonent;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MyServerJSONPlaceHolderApi
{
    //Получить всех абонентов
    @GET("getAbons")
    Observable<List<Abonent>> getAbonents();
}
