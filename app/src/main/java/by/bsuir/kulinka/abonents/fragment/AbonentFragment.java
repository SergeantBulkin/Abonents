package by.bsuir.kulinka.abonents.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bsuir.bottomapp.bar.abonents.R;
import com.bsuir.bottomapp.bar.abonents.databinding.FragmentAbonentBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import by.bsuir.kulinka.abonents.adapter.AbonentsAdapter;
import by.bsuir.kulinka.abonents.model.Abonent;
import by.bsuir.kulinka.abonents.model.DisposableManager;
import by.bsuir.kulinka.abonents.retrofit.MyServerNetworkService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class AbonentFragment extends BaseFragment implements AbonentsAdapter.AbonentOnItemClickListener
{
    //----------------------------------------------------------------------------------------------
    FragmentAbonentBinding binding;
    List<Abonent> abonents;
    //----------------------------------------------------------------------------------------------
    public AbonentFragment()
    {
        // Required empty public constructor
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //Логи
        addToLog("onCreate - Метод открыт");

        super.onCreate(savedInstanceState);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Логи
        addToLog("onCreateView - Метод открыт");

        //Показать BottomBar
        showBottomBar();

        // Inflate the layout for this fragment
        binding = FragmentAbonentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        //Логи
        addToLog("onViewCreated - Метод открыт");

        super.onViewCreated(view, savedInstanceState);
        //Настройка вьюшек
        setUpViews();
        //Загрузить абонентов с сервера или БД
        getAbonents();
    }
    //----------------------------------------------------------------------------------------------
    private void setUpViews()
    {
        //Обработчик нажатия на FAB
        binding.abonentsFab.setOnClickListener(v ->
        {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, new CreateAbonentFragment())
                    .addToBackStack("abonentFragment")
                    .commit();
        });

        //Обработчик нажатия на кнопку "Повторить"
        binding.errorLoading.errorLoadingButton.setOnClickListener(v ->
        {
            //Спрятать сообщение об ошибке
            hideError();

            //Загрузить абонентов с сервера
            getAbonentsFromServer();
        });
    }
    //----------------------------------------------------------------------------------------------
    //Загрузка абонентов
    private void getAbonents()
    {
        abonents = new ArrayList<>();

        //database.insertAbonent(new Abonent(1,"Кулинка","Александр",18,"+375445548928",5,24.53F,"адрес", Date.valueOf("2020-10-23").toString()));
        //database.insertAbonent(new Abonent(2,"Кулинка","Александр",19,"+375445548928",1,12.53F,"адрес", Date.valueOf("2020-10-23").toString()));
        //database.insertAbonent(new Abonent(3,"Кулинка","Александр",20,"+375445548928",3,24.53F,"адрес", Date.valueOf("2020-10-23").toString()));
        //database.insertAbonent(new Abonent(4,"Кулинка","Александр",12,"+375445548928",8,24.53F,"адрес", Date.valueOf("2020-10-23").toString()));

        //Получить данные с сервера
        getAbonentsFromServer();
    }
    //----------------------------------------------------------------------------------------------
    //Получить абонентов с сервера
    private void getAbonentsFromServer()
    {
        //Показать ProgressBar
        showProgressBar();

        DisposableManager.add(MyServerNetworkService
                .getInstance()
                .getJSONApi()
                .getAbonents()
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::fromIterable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Abonent>()
                {
                    @Override
                    public void onNext(Abonent abonent)
                    {
                        abonents.add(abonent);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        //Логи
                        addToLog("getAbonentsFromServer - onError");

                        //Убрать ProgressBar
                        hideProgressBar();

                        //Убрать FAB
                        //hideFAB();

                        //Сообщение об ошибке
                        showError();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete()
                    {
                        //Убрать ProgressBar
                        hideProgressBar();

                        //Настройка RecyclerView
                        setUpRecyclerView();
                    }
                }));
    }
    //----------------------------------------------------------------------------------------------
    private void setUpRecyclerView()
    {
        AbonentsAdapter abonentsAdapter = new AbonentsAdapter(abonents, this);
        binding.abonentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.abonentsRecyclerView.setAdapter(abonentsAdapter);

        //Сделать видимой FAB
        showFAB();
    }
    //----------------------------------------------------------------------------------------------
    //Метод интерфейса AbonentAdapter
    @Override
    public void abonentItemClicked(Abonent abonent)
    {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, CreateAbonentFragment.newInstance(abonent))
                .addToBackStack("abonentFragment")
                .commit();
    }
    //----------------------------------------------------------------------------------------------
    //Проверка соединения с интернетом
    private boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    //----------------------------------------------------------------------------------------------
    //Вспомогательные методы
    private void hideError()
    {
        //Спрятать
        binding.errorLoading.errorLoadingIcon.setVisibility(View.GONE);
        binding.errorLoading.errorLoadingText.setVisibility(View.GONE);
        binding.errorLoading.errorLoadingButton.setVisibility(View.GONE);
    }
    private void showError()
    {
        //Показать сообщение об ошибке
        binding.errorLoading.errorLoadingIcon.setVisibility(View.VISIBLE);
        binding.errorLoading.errorLoadingText.setVisibility(View.VISIBLE);
        binding.errorLoading.errorLoadingButton.setVisibility(View.VISIBLE);
    }
    private void hideProgressBar()
    {
        //Убрать ProgressBar
        binding.abonentsProgressBar.setVisibility(View.GONE);
    }
    private void showProgressBar()
    {
        //Показать ProgressBar
        binding.abonentsProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideFAB()
    {
        //Убрать FAB
        binding.abonentsFab.setVisibility(View.GONE);
    }
    private void showFAB()
    {
        //Сделать видимой FAB
        binding.abonentsFab.setVisibility(View.VISIBLE);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        addToLog("onAttach");
    }
    @Override
    public void onStart()
    {
        super.onStart();
        addToLog("onStart");
    }
    @Override
    public void onResume()
    {
        super.onResume();
        addToLog("onResume");
    }
    @Override
    public void onPause()
    {
        super.onPause();
        addToLog("onPause");
    }
    @Override
    public void onStop()
    {
        super.onStop();
        addToLog("onStop");
    }
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        addToLog("onDestroyView");
    }
    @Override
    public void onDestroy()
    {
        //Логи
        addToLog("onDestroy - Метод открыт");

        super.onDestroy();

        //Отписка
        DisposableManager.dispose();
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        addToLog("onDetach");
    }
    //----------------------------------------------------------------------------------------------
}