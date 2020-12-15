package by.bsuir.kulinka.abonents.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyServerNetworkService
{
    //----------------------------------------------------------------------------------------------
    //Singleton объект
    private static MyServerNetworkService instance;

    //Постоянная часть ссылки
    private static final String BASE_URL = "http://192.168.1.3:8080/";
    private static Retrofit retrofit;
    //----------------------------------------------------------------------------------------------
    //Конструктор
    private MyServerNetworkService()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    //----------------------------------------------------------------------------------------------
    //Создаст объект только, если он еще не существует
    public static MyServerNetworkService getInstance()
    {
        if (instance == null)
            instance = new MyServerNetworkService();
        return instance;
    }
    //----------------------------------------------------------------------------------------------
    //Объект интерфейса
    public MyServerJSONPlaceHolderApi getJSONApi()
    {
        return retrofit.create(MyServerJSONPlaceHolderApi.class);
    }
    //----------------------------------------------------------------------------------------------
}
