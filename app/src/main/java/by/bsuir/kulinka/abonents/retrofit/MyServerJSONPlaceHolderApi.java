package by.bsuir.kulinka.abonents.retrofit;

import by.bsuir.kulinka.abonents.model.Abonent;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MyServerJSONPlaceHolderApi
{
    //Получить всех абонентов
    @GET("abonents")
    Observable<List<Abonent>> getAbonents();

    //Создать абонента
    @POST("abonent/create")
    Single<Abonent> createAbonent(@Body Abonent abonent);

    //Обновить абоненте
    @PUT("abonent/update")
    Single<Abonent> updateAbonent(@Body Abonent abonent);

    //Удалить абонента
    @DELETE("abonent/delete")
    Single<Integer> deleteAbonent(@Query(value = "abonid") int abonid);

    //Получить все планы
    @GET("plans")
    Observable<List<Plan>> getPlans();
}
