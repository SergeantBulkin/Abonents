package by.bsuir.kulinka.abonents.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.bsuir.bottomapp.bar.abonents.R;
import com.bsuir.bottomapp.bar.abonents.databinding.FragmentCreatePlanBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import by.bsuir.kulinka.abonents.adapter.PlanInfoServiceAdapter;
import by.bsuir.kulinka.abonents.model.DisposableManager;
import by.bsuir.kulinka.abonents.model.Plan;
import by.bsuir.kulinka.abonents.model.PlanInfo;
import by.bsuir.kulinka.abonents.model.Service;
import by.bsuir.kulinka.abonents.retrofit.MyServerNetworkService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CreatePlanFragment extends Fragment implements PlanInfoServiceAdapter.ServiceItemRemoveClickListener
{
    //----------------------------------------------------------------------------------------------
    private FragmentCreatePlanBinding binding;
    private PlanInfo planInfo;
    private PlanInfoServiceAdapter adapter;
    private List<Service> currentPlanServices;
    private List<Service> allPlanServices;
    private ArrayList<MultiSelectModel> servicesDialogList;
    private boolean isPlan = false;
    private CreatePlanFragmentInterface listener;
    //----------------------------------------------------------------------------------------------
    public static CreatePlanFragment newInstance(PlanInfo planInfo, ArrayList<Service> services, CreatePlanFragmentInterface listener)
    {
        Bundle args = new Bundle();
        args.putParcelable("planInfo", planInfo);
        args.putParcelableArrayList("services", services);
        CreatePlanFragment fragment = new CreatePlanFragment(listener);
        fragment.setArguments(args);
        return fragment;
    }

    public CreatePlanFragment(CreatePlanFragmentInterface listener)
    {
        this.listener = listener;
    }

    public CreatePlanFragment(ArrayList<Service> services, CreatePlanFragmentInterface listener)
    {
        this.allPlanServices = services;
        this.listener = listener;
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

        //Если есть выбранный план, то отобразить инфу о нём
        if (getArguments() != null && getArguments().containsKey("planInfo") && getArguments().containsKey("services"))
        {
            planInfo = getArguments().getParcelable("planInfo");
            if (planInfo != null)
            {
                isPlan = true;

                //Присвоить переменной список выбранного плана
                currentPlanServices = planInfo.getServices();

                fillPlanFields();
            }

            allPlanServices = getArguments().getParcelableArrayList("services");
        } else
        {
            //Инициализировать список
            currentPlanServices = new ArrayList<>();

            //Убрать кнопку "Удалить"
            binding.planDeleteButton.setVisibility(View.GONE);
            binding.planConfirmButton.setText("Создать");

            //Сделать недоступной кнопку "Удалить"
            binding.planDeleteButton.setEnabled(false);
        }

        //Иниициализировать адаптер
        adapter = new PlanInfoServiceAdapter(currentPlanServices, this);

        binding.recyclerViewPlanServices.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewPlanServices.setItemAnimator(new DefaultItemAnimator());

        //Установить адаптер
        binding.recyclerViewPlanServices.setAdapter(adapter);

        //Инициализировать списки для диалога
        initListsForDialog();
    }
    //----------------------------------------------------------------------------------------------
    //Инициализировать списки для диалога
    private void initListsForDialog()
    {
        servicesDialogList = new ArrayList<>();

        //Заполнить списки для диалогов всеми услугами
        for (Service service : allPlanServices)
        {
            servicesDialogList.add(new MultiSelectModel(service.getId(), service.getService_name()));
        }

        //Удалить уже выбранные услуги в плане
        if (isPlan)
        {
            for (Service service : currentPlanServices)
            {
                for (int i = 0; i < servicesDialogList.size(); i++)
                {
                    if (service.getId() == servicesDialogList.get(i).getId())
                    {
                        servicesDialogList.remove(i);
                        i--;
                    }
                }
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    //Установка обработчиков
    private void setUpListeners()
    {
        //Обработка нажатия на кнопку "Добавить"
        binding.planAddButton.setOnClickListener(v ->
        {
            MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                    .title(getResources().getString(R.string.plan_services))
                    .titleSize(25)
                    .positiveText("Done")
                    .negativeText("Cancel")
                    .setMinSelectionLimit(1)
                    .multiSelectList(servicesDialogList)
                    .onSubmit(new MultiSelectDialog.SubmitCallbackListener()
                    {
                        @Override
                        public void onSelected(ArrayList<Integer> arrayListInt, ArrayList<String> arrayListString, String s)
                        {
                            //Сортировка выбранных индексов
                            Collections.sort(arrayListInt);
                            addToLog(arrayListInt.toString());

                            for (Integer id : arrayListInt)
                            {
                                //Убрать выбранные услуги из списка для диалога
                                for (int i = 0; i < servicesDialogList.size(); i++)
                                {
                                    if (id == servicesDialogList.get(i).getId())
                                    {
                                        servicesDialogList.remove(i);
                                        i--;
                                    }
                                }

                                //Добавить в текущие услуги
                                for (Service service : allPlanServices)
                                {
                                    if (id == service.getId())
                                    {
                                        currentPlanServices.add(service);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();

                            //Пересчитать стоимость
                            calculateCost();
                        }

                        @Override
                        public void onCancel()
                        {

                        }
                    });
            multiSelectDialog.show(requireActivity().getSupportFragmentManager(), "multiSelectDialog");
        });

        //Обработка нажатия на кнопку "Удалить"
        binding.planDeleteButton.setOnClickListener(v ->
        {
            //Показать ProgressBar
            showTopProgressBar();

            //Заблокировать всё
            disableAllViews();

            //Удалить план
            deletePlan();
        });

        //Обработка нажатия на кнопку "Применить"/"Создать"
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
                    //Обновить план
                    updatePlan();
                } else
                {
                    //Создать план
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
        if (currentPlanServices.size() == 0)
        {
            binding.textInputLayoutPlanCost.setError("Выберите услуги");
            flag = false;
        }
        return flag;
    }
    //----------------------------------------------------------------------------------------------
    //Удалить план
    private void deletePlan()
    {
        DisposableManager.add(MyServerNetworkService.getInstance()
                .getJSONApi()
                .deletePlan(planInfo.getPlan_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Integer>()
                {
                    @Override
                    public void onSuccess(Integer integer)
                    {
                        //Если вернулось -1, значит произошла ошибка
                        if (integer == -1)
                        {
                            //Скрыть ProgressBar
                            hideTopProgressBar();

                            //Snackbar
                            makeSnackBar("Ошибка удаления");

                            //Разблокировать всё
                            enableAllViews();
                        } else
                        {
                            //Скрыть ProgressBar
                            hideTopProgressBar();

                            //Snackbar
                            makeSnackBar("План удалён");

                            //Обновить список планов и закрыть фрагмент
                            listener.deletePlanList(planInfo.getPlan_id());
                            closeFragment();
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        //Скрыть ProgressBar
                        hideTopProgressBar();

                        //Snackbar
                        makeSnackBar("Ошибка удаления");

                        //Разблокировать всё
                        enableAllViews();
                    }
                }));
    }
    //Создать план
    private void createPlan()
    {
        DisposableManager.add(MyServerNetworkService.getInstance()
            .getJSONApi()
            .createPlan(getUpdatedPlan())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<PlanInfo>()
            {
                @Override
                public void onSuccess(PlanInfo planInfo)
                {
                    makeSnackBar("План создан");

                    //Обновить список планов и закрыть фрагмент
                    listener.createPlanList(planInfo);
                    closeFragment();
                }

                @Override
                public void onError(Throwable e)
                {
                    //Скрыть ProgressBar
                    hideTopProgressBar();

                    e.printStackTrace();
                    //Snackbar
                    makeSnackBar("Ошибка создания");

                    //Разблокировать всё
                    enableAllViews();

                }
            }));
    }
    //Обновить план
    private void updatePlan()
    {
        DisposableManager.add(MyServerNetworkService.getInstance()
            .getJSONApi()
            .updatePlan(getUpdatedPlan())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<PlanInfo>()
            {
                @Override
                public void onSuccess(PlanInfo planInfo)
                {
                    makeSnackBar("План обновлён");

                    //Обновить список планов и закрыть фрагмент
                    listener.updatePlanList(planInfo);
                    closeFragment();
                }

                @Override
                public void onError(Throwable e)
                {
                    //Скрыть ProgressBar
                    hideTopProgressBar();

                    e.printStackTrace();
                    //Snackbar
                    makeSnackBar("Ошибка создания");

                    //Разблокировать всё
                    enableAllViews();
                }
            }));
    }
    //----------------------------------------------------------------------------------------------
    //Собрать данные из полей и предоставить готоывй объект
    private PlanInfo getUpdatedPlan()
    {
        //Запомнить введенные данные
        String name = Objects.requireNonNull(binding.textInputEditTextPlanName.getText()).toString();
        float cost = Float.parseFloat(Objects.requireNonNull(binding.textInputEditTextPlanCost.getText()).toString());

        //Если мы редактируем информацию о плане, то обновляем его
        if (isPlan)
        {
            planInfo.setName(name);
            planInfo.setServices(currentPlanServices);
            planInfo.setCost(cost);
            return planInfo;
        } else
        {
            //Иначе создаём нового
            return new PlanInfo(name, currentPlanServices, cost);
        }
    }
    //----------------------------------------------------------------------------------------------
    //Вывести информацию об абоненте
    private void fillPlanFields()
    {
        binding.textInputEditTextPlanName.setText(planInfo.getName());
        binding.textInputEditTextPlanCost.setText(String.valueOf(planInfo.getCost()));
    }
    //----------------------------------------------------------------------------------------------
    //Пересчитать стоимость
    private void calculateCost()
    {
        float cost = 0f;

        for (Service service : currentPlanServices)
        {
            cost +=service.getCost() ;
        }

        binding.textInputEditTextPlanCost.setText(String.valueOf(cost));
    }
    //----------------------------------------------------------------------------------------------
    //Вспомогательные методы
    private void makeSnackBar(String msg)
    {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
    }
    //Закрыть фрагмент
    private void closeFragment()
    {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
    private void showTopProgressBar()
    {
        binding.createPlanProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideTopProgressBar()
    {
        binding.createPlanProgressBar.setVisibility(View.GONE);
    }
    public void addToLog(String msg)
    {
        Log.d("LOG_TAG", getClass().getName() + " - " + msg);
    }
    //Заблокировать всё
    private void disableAllViews()
    {
        binding.textInputLayoutPlanName.setEnabled(false);
        binding.planAddButton.setEnabled(false);
        binding.recyclerViewPlanServices.setEnabled(false);
        binding.textInputLayoutPlanCost.setEnabled(false);
        binding.planDeleteButton.setEnabled(false);
        binding.planConfirmButton.setEnabled(false);
    }
    //Разблокировать всё
    private void enableAllViews()
    {
        binding.textInputLayoutPlanName.setEnabled(true);
        binding.planAddButton.setEnabled(true);
        binding.recyclerViewPlanServices.setEnabled(true);
        binding.textInputLayoutPlanCost.setEnabled(true);
        binding.planDeleteButton.setEnabled(true);
        binding.planConfirmButton.setEnabled(true);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void itemServiceRemoveClick(int position)
    {
        addToLog("Позиция - " + position);
        try
        {
            servicesDialogList.add(new MultiSelectModel(currentPlanServices.get(position).getId(), currentPlanServices.get(position).getService_name()));
            currentPlanServices.remove(position);
            adapter.notifyDataSetChanged();

            //Пересчитать стоимость
            calculateCost();

        } catch (IndexOutOfBoundsException index)
        {
            makeSnackBar("Ошибка удаления - "+ position);
        }
    }
    //----------------------------------------------------------------------------------------------
    public interface CreatePlanFragmentInterface
    {
        void deletePlanList(int deletedServiceID);

        void createPlanList(PlanInfo planInfo);

        void updatePlanList(PlanInfo planInfo);
    }
    //----------------------------------------------------------------------------------------------
}