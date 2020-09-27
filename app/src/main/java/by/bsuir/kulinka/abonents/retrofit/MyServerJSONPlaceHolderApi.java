package by.bsuir.kulinka.abonents.retrofit;

import by.bsuir.kulinka.abonents.model.Abonent;

import java.util.List;

import by.bsuir.kulinka.abonents.model.Plan;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

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
