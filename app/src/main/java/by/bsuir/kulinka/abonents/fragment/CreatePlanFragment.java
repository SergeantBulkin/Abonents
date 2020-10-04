package by.bsuir.kulinka.abonents.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bsuir.bottomapp.bar.abonents.databinding.FragmentCreatePlanBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import by.bsuir.kulinka.abonents.adapter.PlanInfoServiceAdapter;
import by.bsuir.kulinka.abonents.model.PlanInfo;
import by.bsuir.kulinka.abonents.model.Service;

public class CreatePlanFragment extends Fragment implements PlanInfoServiceAdapter.ServiceItemRemoveClickListener
{
    //----------------------------------------------------------------------------------------------
    private FragmentCreatePlanBinding binding;
    private PlanInfo planInfo;
    private PlanInfoServiceAdapter adapter;
    private List<Service> planServices;
    private boolean isPlan = false;
    //----------------------------------------------------------------------------------------------
    public static CreatePlanFragment newInstance(PlanInfo planInfo)
    {
        Bundle args = new Bundle();
        args.putParcelable("planInfo", planInfo);
        CreatePlanFragment fragment = new CreatePlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CreatePlanFragment()
    {
        // Required empty public constructor
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = FragmentCreatePlanBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Настройка Views
        setUpListeners();
        setTextWatchers();

        //Если есть выбранный абонент, то отобразить инфу о нём
        if (getArguments() != null && getArguments().containsKey("planInfo"))
        {
            planInfo = getArguments().getParcelable("planInfo");
            if (planInfo != null)
            {
                isPlan = true;

                //Присвоить переменной список выбранного плана
                planServices = planInfo.getServices();

                fillPlanFields();
            }
        } else
        {
            //Инициализировать список
            planServices = new ArrayList<>();

            //Убрать кнопку "Удалить"
            binding.planDeleteButton.setVisibility(View.GONE);
            binding.planConfirmButton.setText("Создать");

            //Сделать недоступной кнопку "Удалить"
            binding.planDeleteButton.setEnabled(false);
        }

        //Иниициализировать адаптер
        adapter = new PlanInfoServiceAdapter(planServices, this);

        binding.recyclerViewPlanServices.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewPlanServices.setItemAnimator(new DefaultItemAnimator());

        //Установить адаптер
        binding.recyclerViewPlanServices.setAdapter(adapter);
    }
    //----------------------------------------------------------------------------------------------
    //Установка обработчиков
    private void setUpListeners()
    {
        //Обработка нажатия на кнопку "Удалить"
        binding.planDeleteButton.setOnClickListener(v ->
        {
            //Показать ProgressBar
            showTopProgressBar();

            //Заблокировать всё
            disableAllViews();

            deletePlan();
        });

        //Обработка нажатия на кнопку "Применить"
        binding.planConfirmButton.setOnClickListener(v ->
        {
            if (checkViews())
            {
                //Показать Progress
                showTopProgressBar();

                //Заблокировать всё
                disableAllViews();

                if (isPlan)
                {
                    updatePlan();
                } else
                {
                    createPlan();
                }
            }
        });
    }
    //----------------------------------------------------------------------------------------------
    //Установка слушателей ввода текста
    private void setTextWatchers()
    {
        //Имя
        binding.textInputEditTextPlanName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Убрать ошибку
                binding.textInputLayoutPlanName.setError("");
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }
    //----------------------------------------------------------------------------------------------
    //Проверка введенных данных
    private boolean checkViews()
    {
        boolean flag = true;

        //Проверить имя
        if (Objects.requireNonNull(binding.textInputEditTextPlanName.getText()).toString().equals(""))
        {
            binding.textInputLayoutPlanName.setError("Введите имя");
            flag = false;
        }
        //Проверить выбраны ли услуги
        if (planServices.size() == 0)
        {
            binding.textInputLayoutPlanCost.setError("Выберите услуги");
            flag = false;
        }
        return flag;
    }
    //----------------------------------------------------------------------------------------------
    //Удалить абонента
    private void deletePlan()
    {
        /*DisposableManager.add(MyServerNetworkService.getInstance()
                .getJSONApi()
                .deleteAbonent(abonent.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Integer>()
                {
                    @Override
                    public void onSuccess(Integer integer)
                    {
                        //Скрыть ProgressBar
                        hideTopProgressBar();

                        //Snackbar
                        makeSnackBar("Абонент удалён");

                        //Закрыть фрагмент
                        closeFragment();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        //Скрыть ProgressBar
                        hideTopProgressBar();

                        //Snackbar
                        makeSnackBar("Ошибка удаления");

                        //Закрыть фрагмент
                        closeFragment();
                    }
                }));*/
    }
    //----------------------------------------------------------------------------------------------
    //Создать абонента
    private void createPlan()
    {
        /*DisposableManager.add(MyServerNetworkService.getInstance()
            .getJSONApi()
            .createAbonent(getUpdatedPlan())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<Abonent>()
            {
                @Override
                public void onSuccess(Abonent abonent)
                {
                    makeSnackBar("Абонент создан");
                    closeFragment();
                }

                @Override
                public void onError(Throwable e)
                {
                    makeSnackBar("Ошибка создания");

                    //Разблокировать всё и убрать ProgressBar
                    enableAllViews();
                    hideTopProgressBar();
                }
            }));*/
    }
    //----------------------------------------------------------------------------------------------
    //Обновить абонента
    private void updatePlan()
    {
        /*DisposableManager.add(MyServerNetworkService.getInstance()
            .getJSONApi()
            .updateAbonent(getUpdatedPlan())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<Abonent>()
            {
                @Override
                public void onSuccess(Abonent abonent)
                {
                    makeSnackBar("Абонент обновлён");
                    closeFragment();
                }

                @Override
                public void onError(Throwable e)
                {
                    makeSnackBar("Ошибка обновления");

                    //Разблокировать всё и убрать ProgressBar
                    enableAllViews();
                    hideTopProgressBar();
                }
            }));*/
    }
    //----------------------------------------------------------------------------------------------
    //Собрать данные из полей
    private PlanInfo getUpdatedPlan()
    {
        //Запомнить введенные данные
        String name = Objects.requireNonNull(binding.textInputEditTextPlanName.getText()).toString();
        float cost = Float.parseFloat(Objects.requireNonNull(binding.textInputEditTextPlanCost.getText()).toString());

        //Если мы редактируем информацию о плане, то обновляем его
        if (isPlan)
        {
            planInfo.setName(name);
            planInfo.setServices(planServices);
            planInfo.setCost(cost);
            return planInfo;
        } else
        {
            //Иначе создаём нового
            return new PlanInfo(name, planServices, cost);
        }
    }
    //----------------------------------------------------------------------------------------------
    //Заблокировать всё
    private void disableAllViews()
    {
        binding.textInputLayoutPlanName.setEnabled(false);
        binding.planAddButton.setEnabled(false);
        binding.textInputLayoutPlanCost.setEnabled(false);
        binding.planDeleteButton.setEnabled(false);
        binding.planConfirmButton.setEnabled(false);
    }
    //Разблокировать всё
    private void enableAllViews()
    {
        binding.textInputLayoutPlanName.setEnabled(true);
        binding.planAddButton.setEnabled(true);
        binding.textInputLayoutPlanCost.setEnabled(true);
        binding.planDeleteButton.setEnabled(true);
        binding.planConfirmButton.setEnabled(true);
    }
    //----------------------------------------------------------------------------------------------
    //Вывести информацию об абоненте
    private void fillPlanFields()
    {
        binding.textInputEditTextPlanName.setText(planInfo.getName());
        binding.textInputEditTextPlanCost.setText(String.valueOf(planInfo.getCost()));
    }
    //----------------------------------------------------------------------------------------------
    //Закрыть фрагмент
    private void closeFragment()
    {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
    //----------------------------------------------------------------------------------------------
    //Вспомогательные методы
    private void makeSnackBar(String msg)
    {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
    }
    private void showTopProgressBar()
    {
        binding.createPlanProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideTopProgressBar()
    {
        binding.createPlanProgressBar.setVisibility(View.GONE);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void itemServiceRemoveClick(int position)
    {
        planServices.remove(position);
        adapter.notifyItemRemoved(position);
    }
    //----------------------------------------------------------------------------------------------
}