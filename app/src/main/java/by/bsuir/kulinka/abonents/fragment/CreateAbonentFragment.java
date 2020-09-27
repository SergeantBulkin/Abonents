package by.bsuir.kulinka.abonents.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bsuir.bottomapp.bar.abonents.databinding.FragmentCreateAbonentBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import by.bsuir.kulinka.abonents.model.Abonent;
import by.bsuir.kulinka.abonents.model.DisposableManager;
import by.bsuir.kulinka.abonents.model.Plan;
import by.bsuir.kulinka.abonents.retrofit.MyServerNetworkService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CreateAbonentFragment extends BaseFragment
{
    //----------------------------------------------------------------------------------------------
    private FragmentCreateAbonentBinding binding;
    private Abonent abonent;
    private List<Plan> plans;
    private int chosenPlanID;
    private boolean isAbonent = false;
    //----------------------------------------------------------------------------------------------
    public static CreateAbonentFragment newInstance(Abonent abonent)
    {
        Bundle args = new Bundle();
        args.putParcelable("abonent", abonent);
        CreateAbonentFragment fragment = new CreateAbonentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CreateAbonentFragment()
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
        //Спрятать BottomBar
        hideBottomBar();

        // Inflate the layout for this fragment
        binding = FragmentCreateAbonentBinding.inflate(getLayoutInflater(), container, false);
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
        if (getArguments() != null && getArguments().containsKey("abonent"))
        {
            abonent = getArguments().getParcelable("abonent");
            if (abonent != null)
            {
                isAbonent = true;

                //Запомнить план выбранного абонента
                chosenPlanID = abonent.getPlan_id();

                //Загрузить планы и установить план абонента в поле
                loadPlans(chosenPlanID);

                fillAbonentFields();
            }
        } else
        {
            //Убрать кнопку "Удалить"
            binding.abonentDeleteButton.setVisibility(View.GONE);
            binding.abonentConfirmButton.setText("Создать");

            //Загрузить планы
            loadPlans(-1);

            //Сделать недоступной кнопку "Удалить"
            binding.abonentDeleteButton.setEnabled(false);
        }
    }
    //----------------------------------------------------------------------------------------------
    //Установка обработчиков
    private void setUpListeners()
    {
        //Обработка нажатия на EditTextDate
        binding.textInputEditTextAbonentCreateDate.setOnClickListener(v ->
        {
            DatePicker picker = new DatePicker(binding.textInputEditTextAbonentCreateDate);
            picker.show(requireActivity().getSupportFragmentManager(), "datepicker");
        });

        //Обработка нажатия на кнопку "Удалить"
        binding.abonentDeleteButton.setOnClickListener(v ->
        {
            //Показать ProgressBar
            showTopProgressBar();

            //Заблокировать всё
            disableAllViews();

            deleteAbonent();
        });

        //Обработка нажатия на кнопку "Применить"
        binding.abonentConfirmButton.setOnClickListener(v ->
        {
            if (checkViews())
            {
                //Показать Progress
                showTopProgressBar();

                //Заблокировать всё
                disableAllViews();

                if (isAbonent)
                {
                    updateAbonent();
                } else
                {
                    createAbonent();
                }
            }
        });
    }
    //----------------------------------------------------------------------------------------------
    //Установка слушателей ввода текста
    private void setTextWatchers()
    {
        //Имя
        binding.textInputEditTextAbonentName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Убрать ошибку
                binding.textInputLayoutAbonentName.setError("");
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        //Фамилия
        binding.textInputEditTextAbonentLastname.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Убрать ошибку
                binding.textInputLayoutAbonentLastname.setError("");
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        //Возраст
        binding.textInputEditTextAbonentAge.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Убрать ошибку
                binding.textInputLayoutAbonentAge.setError("");
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        //Номер
        binding.textInputEditTextAbonentMobileNumber.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Убрать ошибку
                binding.textInputLayoutAbonentMobileNumber.setError("");
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        //Баланс
        binding.textInputEditTextAbonentBalance.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Убрать ошибку
                binding.textInputLayoutAbonentBalance.setError("");
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        //Адрес
        binding.textInputEditTextAbonentAddress.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Убрать ошибку
                binding.textInputLayoutAbonentAddress.setError("");
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        //Дата
        binding.textInputEditTextAbonentCreateDate.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Убрать ошибку
                binding.textInputLayoutAbonentCreateDate.setError("");
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }
    //----------------------------------------------------------------------------------------------
    //Загрузить планы
    private void loadPlans(int planID)
    {
        //Показать ProgressBar загрузки планов
        binding.plansProgressBar.setVisibility(View.VISIBLE);

        //Заблокировать выбор плана, пока они не загрузятся
        binding.textInputLayoutAbonentPlan.setEnabled(false);

        //Инициализировать список
        plans = new ArrayList<>();

        DisposableManager.add(MyServerNetworkService.getInstance()
                .getJSONApi()
                .getPlans()
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::fromIterable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Plan>()
                {
                    @Override
                    public void onNext(Plan plan)
                    {
                        plans.add(plan);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        //Показать ошибку
                        binding.textInputLayoutAbonentPlan.setError("Ошибка загрузки");

                        //Спрятать ProgressBar загрузки планов
                        binding.plansProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete()
                    {
                        //Если смотрим инфу об абоненте, то отобразить его тариф
                        if(planID != -1)
                        {
                            for (Plan plan : plans)
                            {
                                if (plan.getId() == planID)
                                {
                                    binding.textFieldAutocompleteAbonentPlan.setText(plan.getPlan_name());
                                }
                            }
                        }

                        //Установить адаптер
                        setPlansAdapter();
                    }
                }));
    }
    //----------------------------------------------------------------------------------------------
    //Установить адаптер выбора факультета
    private void setPlansAdapter()
    {
        //Спрятать ProgressBar загрузки планов
        binding.plansProgressBar.setVisibility(View.GONE);

        //Создать список имён для адаптера
        List<String> planNames = new ArrayList<>();
        for (Plan plan : plans)
        {
            planNames.add(plan.getPlan_name());
        }

        //Установить адаптер для выбора факультета
        binding.textFieldAutocompleteAbonentPlan.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                planNames));

        //Разблокировать выбор плана
        binding.textInputLayoutAbonentPlan.setEnabled(true);

        //Обработка выбора факультета
        binding.textFieldAutocompleteAbonentPlan.setOnItemClickListener((parent, view, position, id) ->
        {
            //Убрать ошибку
            binding.textInputLayoutAbonentPlan.setError("");

            //Запомнить выбранный план
            chosenPlanID = plans.get(position).getId();
        });
    }
    //----------------------------------------------------------------------------------------------
    //Проверка введенных данных
    private boolean checkViews()
    {
        boolean flag = true;

        //Проверить имя
        if (Objects.requireNonNull(binding.textInputEditTextAbonentName.getText()).toString().equals(""))
        {
            binding.textInputLayoutAbonentName.setError("Введите имя");
            flag = false;
        }
        //Проверить фамилию
        if (Objects.requireNonNull(binding.textInputEditTextAbonentLastname.getText()).toString().equals(""))
        {
            binding.textInputLayoutAbonentLastname.setError("Введите фамилию");
            flag = false;
        }
        //Проверить возраст
        int age;
        if (Objects.requireNonNull(binding.textInputEditTextAbonentAge.getText()).toString().equals(""))
        {
            binding.textInputLayoutAbonentAge.setError("Введите");
            flag = false;
        } else
        {
            try
            {
                age = Integer.parseInt(Objects.requireNonNull(binding.textInputEditTextAbonentAge.getText()).toString());
                if (age > 255)
                {
                    binding.textInputLayoutAbonentAge.setError("Ошибка");
                    flag = false;
                }
            } catch (NumberFormatException nfe)
            {
                binding.textInputLayoutAbonentAge.setError("Некорректный возраст");
            }
        }
        //Проверить номер
        Pattern pattern = Pattern.compile("[+]375(29|25|44)\\d{7}");
        Matcher matcher = pattern.matcher(Objects.requireNonNull(binding.textInputEditTextAbonentMobileNumber.getText()).toString());
        if (!matcher.matches())
        {
            binding.textInputLayoutAbonentMobileNumber.setError("Неверный формат");
            flag = false;
        }
        if (Objects.requireNonNull(binding.textInputEditTextAbonentMobileNumber.getText()).toString().equals(""))
        {
            binding.textInputLayoutAbonentMobileNumber.setError("Введите номер");
            flag = false;
        }
        //Проверить баланс
        if (Objects.requireNonNull(binding.textInputEditTextAbonentBalance.getText()).toString().equals(""))
        {
            binding.textInputLayoutAbonentBalance.setError("Введите");
            flag = false;
        }
        //Проверить тарифный план
        if (Objects.requireNonNull(binding.textFieldAutocompleteAbonentPlan.getText()).toString().equals(""))
        {
            binding.textInputLayoutAbonentPlan.setError("Выберите план");
            flag = false;
        }
        //Проверить адрес
        if (Objects.requireNonNull(binding.textInputEditTextAbonentAddress.getText()).toString().equals(""))
        {
            binding.textInputLayoutAbonentAddress.setError("Введите адрес");
            flag = false;
        }
        //Проверить дату
        if (Objects.requireNonNull(binding.textInputEditTextAbonentCreateDate.getText()).toString().equals(""))
        {
            binding.textInputLayoutAbonentCreateDate.setError("Выберите дату");
            flag = false;
        }

        return flag;
    }
    //----------------------------------------------------------------------------------------------
    //Удалить абонента
    private void deleteAbonent()
    {
        DisposableManager.add(MyServerNetworkService.getInstance()
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
                }));
    }
    //----------------------------------------------------------------------------------------------
    //Создать абонента
    private void createAbonent()
    {
        DisposableManager.add(MyServerNetworkService.getInstance()
            .getJSONApi()
            .createAbonent(getUpdatedAbonent())
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
            }));
    }
    //----------------------------------------------------------------------------------------------
    //Обновить абонента
    private void updateAbonent()
    {
        DisposableManager.add(MyServerNetworkService.getInstance()
            .getJSONApi()
            .updateAbonent(getUpdatedAbonent())
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
            }));
    }
    //----------------------------------------------------------------------------------------------
    //Собрать данные из полей
    private Abonent getUpdatedAbonent()
    {
        //Запомнить введенные данные
        String name = Objects.requireNonNull(binding.textInputEditTextAbonentName.getText()).toString();
        String lastname = Objects.requireNonNull(binding.textInputEditTextAbonentLastname.getText()).toString();
        int age = Integer.parseInt(Objects.requireNonNull(binding.textInputEditTextAbonentAge.getText()).toString());
        String number = Objects.requireNonNull(binding.textInputEditTextAbonentMobileNumber.getText()).toString();
        float balance = Float.parseFloat(Objects.requireNonNull(binding.textInputEditTextAbonentBalance.getText()).toString());
        String address = Objects.requireNonNull(binding.textInputEditTextAbonentAddress.getText()).toString();
        String date = Objects.requireNonNull(toSQLDate(Objects.requireNonNull(binding.textInputEditTextAbonentCreateDate.getText()).toString()));
        //Если мы редактируем информацию об абоненте, то обновляем его
        if (isAbonent)
        {
            abonent.setName(name);
            abonent.setLastname(lastname);
            abonent.setAge(age);
            abonent.setMobile_number(number);
            abonent.setBalance(balance);
            abonent.setPlan_id(chosenPlanID);
            abonent.setAddress(address);
            abonent.setCreate_date(date);
            return abonent;
        } else
        {
            //Иначе создаём нового
            return new Abonent(name, lastname, age, number, chosenPlanID, balance, address, date);
        }
    }
    //----------------------------------------------------------------------------------------------
    //Заблокировать всё
    private void disableAllViews()
    {
        binding.textInputLayoutAbonentName.setEnabled(false);
        binding.textInputLayoutAbonentLastname.setEnabled(false);
        binding.textInputLayoutAbonentAge.setEnabled(false);
        binding.textInputLayoutAbonentMobileNumber.setEnabled(false);
        binding.textInputLayoutAbonentBalance.setEnabled(false);
        binding.textInputLayoutAbonentPlan.setEnabled(false);
        binding.textInputLayoutAbonentAddress.setEnabled(false);
        binding.textInputLayoutAbonentCreateDate.setEnabled(false);
        binding.abonentConfirmButton.setEnabled(false);
        binding.abonentDeleteButton.setEnabled(false);
    }
    //Разблокировать всё
    private void enableAllViews()
    {
        binding.textInputLayoutAbonentName.setEnabled(true);
        binding.textInputLayoutAbonentLastname.setEnabled(true);
        binding.textInputLayoutAbonentAge.setEnabled(true);
        binding.textInputLayoutAbonentMobileNumber.setEnabled(true);
        binding.textInputLayoutAbonentBalance.setEnabled(true);
        binding.textInputLayoutAbonentPlan.setEnabled(true);
        binding.textInputLayoutAbonentAddress.setEnabled(true);
        binding.textInputLayoutAbonentCreateDate.setEnabled(true);
        binding.abonentConfirmButton.setEnabled(true);
        binding.abonentDeleteButton.setEnabled(true);
    }
    //----------------------------------------------------------------------------------------------
    //Вывести информацию об абоненте
    private void fillAbonentFields()
    {
        //Сделать недоступным выбор плана
        binding.textInputLayoutAbonentPlan.setEnabled(false);

        binding.textInputEditTextAbonentLastname.setText(abonent.getLastname());
        binding.textInputEditTextAbonentName.setText(abonent.getName());
        binding.textInputEditTextAbonentAge.setText(String.valueOf(abonent.getAge()));
        binding.textInputEditTextAbonentMobileNumber.setText(abonent.getMobile_number());
        binding.textInputEditTextAbonentBalance.setText(String.valueOf(abonent.getBalance()));
        binding.textInputEditTextAbonentAddress.setText(abonent.getAddress());
        binding.textInputEditTextAbonentCreateDate.setText(fromSQLDate(abonent.getCreate_date()));
    }
    //----------------------------------------------------------------------------------------------
    //Закрыть фрагмент
    private void closeFragment()
    {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
    //----------------------------------------------------------------------------------------------
    //Методы конвертации даты
    private String fromSQLDate(String sqlDate)
    {
        String[] mas = sqlDate.split("-");
        return mas[2] + "." + mas[1] + "." + mas[0];
    }
    private String toSQLDate(String normalDate)
    {
        String[] mas = normalDate.split("[.]");
        addToLog(Arrays.toString(mas));
        return mas[2] + "-" + mas[1] + "-" + mas[0];
    }
    //----------------------------------------------------------------------------------------------
    //Вспомогательные методы
    private void makeSnackBar(String msg)
    {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
    }
    private void showTopProgressBar()
    {
        binding.createAbonentProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideTopProgressBar()
    {
        binding.createAbonentProgressBar.setVisibility(View.GONE);
    }
    //----------------------------------------------------------------------------------------------
}