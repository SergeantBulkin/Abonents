package by.bsuir.kulinka.abonents.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bsuir.bottomapp.bar.abonents.databinding.FragmentBaseBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import by.bsuir.kulinka.abonents.adapter.AbonentsAdapter;
import by.bsuir.kulinka.abonents.adapter.PlansInfoAdapter;
import by.bsuir.kulinka.abonents.adapter.ServiceAdapter;
import by.bsuir.kulinka.abonents.model.Abonent;
import by.bsuir.kulinka.abonents.model.DisposableManager;
import by.bsuir.kulinka.abonents.model.PlanInfo;
import by.bsuir.kulinka.abonents.model.Service;
import by.bsuir.kulinka.abonents.retrofit.MyServerNetworkService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class BaseFragment extends Fragment implements AbonentsAdapter.AbonentOnItemClickListener, PlansInfoAdapter.PlanOnItemClickListener, ServiceAdapter.ServiceOnItemClickListener
{
    //----------------------------------------------------------------------------------------------
    //Объект интерфейса MainActivity
    public ActivityInterface activityInterface;

    //Биндинг
    private FragmentBaseBinding binding;

    //Выбранная вкладка
    private int chosenTab = 0;

    //Списки объектов
    private List<Abonent> abonents;
    private AbonentsAdapter abonentsAdapter;
    private List<PlanInfo> plansInfo;
    private PlansInfoAdapter plansInfoAdapter;
    private List<Service> services;
    private ServiceAdapter serviceAdapter;
    //----------------------------------------------------------------------------------------------
    //Обязуем MainActivity имплементировать интерфейс
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            activityInterface = (ActivityInterface) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement ActivityInterface");
        }
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Инициализировать списки и адаптеры
        abonents = new ArrayList<>();
        abonentsAdapter = new AbonentsAdapter(this);
        plansInfo = new ArrayList<>();
        plansInfoAdapter = new PlansInfoAdapter(this);
        services = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(this);
    }
    //----------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        addToLog("onCreateView");
        // Inflate the layout for this fragment
        binding = FragmentBaseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Настройка вьюшек
        setUpViews();

        //Установка слушателя на BottomBar
        setUpBottomBar();

        //При старте показывать абонентов
        showAbonents();
    }
    //----------------------------------------------------------------------------------------------
    private void setUpViews()
    {
        //Обработчик нажатия на FAB1
        binding.materialDesignFloatingActionMenuItem1.setOnClickListener(v ->
        {
            //Заменить фрагмент
            activityInterface.loadFragment(new CreateAbonentFragment(), "baseFragment");
        });
        //Обработчик нажатия на FAB2
        binding.materialDesignFloatingActionMenuItem2.setOnClickListener(v ->
        {
            //Заменить фрагмент

        });
        //Обработчик нажатия на FAB3
        binding.materialDesignFloatingActionMenuItem3.setOnClickListener(v ->
        {
            //Заменить фрагмент

        });

        //Обработчик нажатия на кнопку "Повторить"
        binding.errorLoading.errorLoadingButton.setOnClickListener(v ->
        {
            //Спрятать сообщение об ошибке
            hideError();

            switch (chosenTab)
            {
                case 0:
                    clearAdapter();
                    showAbonents();
                    break;
                case 1:
                    clearAdapter();
                    showPlans();
                    break;
                case 2:
                    clearAdapter();
                    showServices();
                    break;
            }
        });
    }
    //----------------------------------------------------------------------------------------------
    //Настройка BottomBar
    private void setUpBottomBar()
    {
        //Обработка выбора фрагмента
        binding.bottomBar.setOnItemSelectedListener(i ->
        {
            switch (i)
            {
                case 0:
                    chosenTab = 0;
                    clearAdapter();
                    showAbonents();
                    return true;
                case 1:
                    chosenTab = 1;
                    clearAdapter();
                    showPlans();
                    return true;
                case 2:
                    chosenTab = 2;
                    clearAdapter();
                    showServices();
                    return true;
            }
            return true;
        });
    }
    //----------------------------------------------------------------------------------------------
    //Отобразить абонентов
    private void showAbonents()
    {
        //Показать ProgressBar
        showProgressBar();


        if (abonents.size() == 0)
        {
            //Спрятать FAB
            hideFAB();

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
                            //Убрать ProgressBar
                            hideProgressBar();

                            //Сообщение об ошибке
                            showError();
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete()
                        {
                            //Установить загруженный список в адаптер
                            abonentsAdapter.setAbonents(abonents);

                            //Убрать ProgressBar
                            hideProgressBar();

                            //Настройка RecyclerView
                            setUpRecyclerView(0);
                        }
                    }));
        }
        else
        {
            setUpRecyclerView(0);
        }
    }
    //Отобразить планы
    private void showPlans()
    {
        //Показать ProgressBar
        showProgressBar();

        if (plansInfo.size() == 0)
        {
            //Спрятать FAB
            hideFAB();

            DisposableManager.add(MyServerNetworkService
                    .getInstance()
                    .getJSONApi()
                    .getPlansInfo()
                    .subscribeOn(Schedulers.io())
                    .flatMap(Observable::fromIterable)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<PlanInfo>()
                    {
                        @Override
                        public void onNext(PlanInfo planInfo)
                        {
                            plansInfo.add(planInfo);
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            //Убрать ProgressBar
                            hideProgressBar();

                            //Сообщение об ошибке
                            showError();
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete()
                        {
                            //Установить загруженный список в адаптер
                            plansInfoAdapter.setPlansInfo(plansInfo);

                            //Убрать ProgressBar
                            hideProgressBar();

                            //Настройка RecyclerView
                            setUpRecyclerView(1);
                        }
                    }));
        }
        else
        {
            setUpRecyclerView(1);
        }
    }
    //Отобразить услуги
    private void showServices()
    {
        //Показать ProgressBar
        showProgressBar();

        if (services.size() == 0)
        {
            //Спрятать FAB
            hideFAB();

            DisposableManager.add(MyServerNetworkService
                .getInstance()
                .getJSONApi()
                .getServices()
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::fromIterable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Service>()
                {
                    @Override
                    public void onNext(Service service)
                    {
                        services.add(service);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        //Убрать ProgressBar
                        hideProgressBar();

                        //Сообщение об ошибке
                        showError();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete()
                    {
                        //Установить загруженный список в адаптер
                        serviceAdapter.setServices(services);

                        //Убрать ProgressBar
                        hideProgressBar();

                        //Настройка RecyclerView
                        setUpRecyclerView(2);
                    }
                }));
        } else
        {
            setUpRecyclerView(2);
        }
    }
    //----------------------------------------------------------------------------------------------
    //Настройка RecyclerView
    private void setUpRecyclerView(int flag)
    //Очистить адаптер RecyclerView
    {
        switch (flag)
        {
            case 0:
                binding.abonentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.abonentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                binding.abonentsRecyclerView.setAdapter(abonentsAdapter);

                //Спрятать ProgressBar
                hideProgressBar();

                //Сделать видимой FAB
                showFAB();
                break;
            case 1:
                binding.abonentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.abonentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                binding.abonentsRecyclerView.setAdapter(plansInfoAdapter);

                //Спрятать ProgressBar
                hideProgressBar();

                //Сделать видимой FAB
                showFAB();
                break;
            case 2:
                binding.abonentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.abonentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                binding.abonentsRecyclerView.setAdapter(serviceAdapter);

                //Спрятать ProgressBar
                hideProgressBar();

                //Сделать видимой FAB
                showFAB();
                break;
        }
    }
    private void clearAdapter()
    {
        binding.abonentsRecyclerView.setAdapter(null);
    }
    //----------------------------------------------------------------------------------------------
    //Вспомогательные методы
    private void hideError()
    {
        //Спрятать сообщение об ошибке
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
        binding.floatingActionMenu.hideMenu(true);
    }
    private void showFAB()
    {
        //Сделать видимой FAB
        binding.floatingActionMenu.showMenu(true);
    }
    public void addToLog(String msg)
    {
        Log.d("LOG_TAG", getClass().getName() + " - " + msg);
    }
    //----------------------------------------------------------------------------------------------
    //Методы адаптеров
    @Override
    public void abonentItemClicked(Abonent abonent)
    {
        activityInterface.loadFragment(CreateAbonentFragment.newInstance(abonent), "abonentFragment");
    }

    @Override
    public void planItemClicked(PlanInfo planInfo)
    {

    }

    @Override
    public void serviceItemClicked(Service service)
    {

    }
    //----------------------------------------------------------------------------------------------
    //Интерфейс для связи с MainActivity
    public interface ActivityInterface
    {
        void loadFragment(Fragment fragment, String fragmentTAG);
    }
    //----------------------------------------------------------------------------------------------
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
