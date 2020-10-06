package by.bsuir.kulinka.abonents.retrofit;

import java.util.List;

import by.bsuir.kulinka.abonents.model.Abonent;
import by.bsuir.kulinka.abonents.model.Plan;
import by.bsuir.kulinka.abonents.model.PlanInfo;
import by.bsuir.kulinka.abonents.model.Service;
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
    //----------------------------------------------------------------------------------------------
    //Получить всех абонентов
    @GET("abonents")
    Observable<List<Abonent>> getAbonents();
    //----------------------------------------------------------------------------------------------
    //Создать абонента
    @POST("abonent/create")
    Single<Abonent> createAbonent(@Body Abonent abonent);
    //----------------------------------------------------------------------------------------------
    //Обновить абоненте
    @PUT("abonent/update")
    Single<Abonent> updateAbonent(@Body Abonent abonent);
    //----------------------------------------------------------------------------------------------
    //Удалить абонента
    @DELETE("abonent/delete")
    Single<Integer> deleteAbonent(@Query(value = "abonid") int abonid);
    //----------------------------------------------------------------------------------------------
    //Получить все планы
    @GET("plans")
    Observable<List<Plan>> getPlans();
    //----------------------------------------------------------------------------------------------
    //Получить все планы с полной информацией
    @GET("plans/info")
    Observable<List<PlanInfo>> getPlansInfo();
    //----------------------------------------------------------------------------------------------
    //Создать новый план
    @POST("plan/add")
    Single<PlanInfo> createPlan(@Body PlanInfo planInfo);
    //----------------------------------------------------------------------------------------------
    //Обновить план
    @PUT("plan/update")
    Single<PlanInfo> updatePlan(@Body PlanInfo planInfo);
    //----------------------------------------------------------------------------------------------
    //Удалить план
    @DELETE("plan/delete")
    Single<Integer> deletePlan(@Query(value = "planid") int planid);
    //----------------------------------------------------------------------------------------------
    //Получить все услуги
    @GET("services")
    Observable<List<Service>> getServices();
    //----------------------------------------------------------------------------------------------
    //Создать новую услугу
    @POST("service/create/body")
    Single<Service> createService(@Body Service service);
    //----------------------------------------------------------------------------------------------
    //Удалить услугу
    @DELETE("service/delete")
    Single<Integer> deleteService(@Query(value = "serviceid") int serviceid);
    //----------------------------------------------------------------------------------------------
    //Обновить услугу
    @PUT("service/update")
    Single<Service> updateService(@Body Service service);
    //----------------------------------------------------------------------------------------------
}
