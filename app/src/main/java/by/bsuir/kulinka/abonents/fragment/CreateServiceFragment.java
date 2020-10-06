package by.bsuir.kulinka.abonents.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bsuir.bottomapp.bar.abonents.databinding.DialogFragmentCreateServiceBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import by.bsuir.kulinka.abonents.model.DisposableManager;
import by.bsuir.kulinka.abonents.model.Service;
import by.bsuir.kulinka.abonents.retrofit.MyServerNetworkService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CreateServiceFragment extends BottomSheetDialogFragment
{
    //----------------------------------------------------------------------------------------------
    private DialogFragmentCreateServiceBinding binding;
    private Service service;
    private boolean isService = false;
    private CreateServiceDialogInterface listener;
    //----------------------------------------------------------------------------------------------
    public static CreateServiceFragment newInstance(Service service, CreateServiceDialogInterface listener)
    {
        Bundle args = new Bundle();
        args.putParcelable("service", service);
        CreateServiceFragment fragment = new CreateServiceFragment(listener);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateServiceFragment(CreateServiceDialogInterface listener)
    {
        this.listener = listener;
    }
    //----------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = DialogFragmentCreateServiceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Настройка Views
        //setUpViews(view);
        setUpListeners();
        setTextWatchers();

        //Если есть выбранная услуга, то отобразить инфу о ней
        if (getArguments() != null && getArguments().containsKey("service"))
        {
            service = getArguments().getParcelable("service");
            if (service != null)
            {
                isService = true;

                fillServiceFields();
            }
        } else
        {
            //Убрать кнопку "Удалить"
            binding.serviceDeleteButton.setVisibility(View.GONE);
            binding.serviceConfirmButton.setText("Создать");
        }
    }
    //----------------------------------------------------------------------------------------------
    //Вывести информацию об услуге
    private void fillServiceFields()
    {
        binding.textInputEditTextServiceName.setText(service.getService_name());
        binding.textInputEditTextServiceCost.setText(String.valueOf(service.getCost()));
    }
    //----------------------------------------------------------------------------------------------
    private void setUpListeners()
    {
        //Обработка нажатия на кнопку "Применить"/"Создать"
        binding.serviceConfirmButton.setOnClickListener(v ->
        {
            if (checkViews())
            {
                //Показать Progress
                showTopProgressBar();

                //Заблокировать всё
                disableAllViews();

                if (isService)
                {
                    //Обновить план
                    updateService();
                } else
                {
                    //Создать план
                    createService();
                }
            }
        });

        //Обработка нажатия на кнопку "Удалить"
        binding.serviceDeleteButton.setOnClickListener(v ->
        {
            //Показать ProgressBar
            showTopProgressBar();

            //Заблокировать всё
            disableAllViews();

            //Удалить план
            deleteService();
        });
    }
    //----------------------------------------------------------------------------------------------
    //Установка слушателей ввода текста
    private void setTextWatchers()
    {
        //Имя
        binding.textInputEditTextServiceName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Убрать ошибку
                binding.textInputLayoutServiceName.setError("");
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        //Стоимость
        binding.textInputEditTextServiceCost.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //Убрать ошибку
                binding.textInputLayoutServiceCost.setError("");
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
        if (Objects.requireNonNull(binding.textInputEditTextServiceName.getText()).toString().equals(""))
        {
            binding.textInputLayoutServiceName.setError("Введите название");
            flag = false;
        }

        //Проверить стоимость
        if (Objects.requireNonNull(binding.textInputEditTextServiceCost.getText()).toString().equals(""))
        {
            binding.textInputLayoutServiceCost.setError("Введите стоимость");
            flag = false;
        }

        return flag;
    }
    //----------------------------------------------------------------------------------------------
    //Собрать данные из полей и предоставить готоывй объект
    private Service getUpdatedService()
    {
        //Запомнить введенные данные
        String name = Objects.requireNonNull(binding.textInputEditTextServiceName.getText()).toString();
        float cost = Float.parseFloat(Objects.requireNonNull(binding.textInputEditTextServiceCost.getText()).toString());

        //Если мы редактируем информацию об услуге, то обновляем её
        if (isService)
        {
            service.setService_name(name);
            service.setCost(cost);
            return service;
        } else
        {
            //Иначе создаём новоую
            return new Service(name, cost);
        }
    }
    //----------------------------------------------------------------------------------------------
    //Удалить услугу
    private void deleteService()
    {
        DisposableManager.add(MyServerNetworkService.getInstance()
            .getJSONApi()
            .deleteService(service.getId())
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
                        makeSnackBar("Услуга удалена");

                        //Обновить список услуг и закрыть диалог
                        listener.deleteServicesList(service.getId());
                        dismiss();
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

    //Создать услугу
    private void createService()
    {
        addToLog("createService");
        DisposableManager.add(MyServerNetworkService.getInstance()
                .getJSONApi()
                .createService(getUpdatedService())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Service>()
                {
                    @Override
                    public void onSuccess(Service service)
                    {
                        makeSnackBar("Услуга создана");

                        //Обновить список услуг и закрыть диалог
                        listener.createServiceList(service);
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        //Скрыть ProgressBar
                        hideTopProgressBar();

                        //Snackbar
                        makeSnackBar("Ошибка удаления");

                        e.printStackTrace();
                        //Разблокировать всё
                        enableAllViews();
                    }
                }));
    }

    //Обновить услугу
    private void updateService()
    {
        DisposableManager.add(MyServerNetworkService.getInstance()
            .getJSONApi()
            .updateService(getUpdatedService())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<Service>()
            {
                @Override
                public void onSuccess(Service service)
                {
                    makeSnackBar("Услуга обновлена");

                    //Обновить список услуг и закрыть диалог
                    listener.updateServiceList(service);
                    dismiss();
                }

                @Override
                public void onError(Throwable e)
                {
                    //Скрыть ProgressBar
                    hideTopProgressBar();

                    //Snackbar
                    makeSnackBar("Ошибка удаления");

                    e.printStackTrace();

                    e.printStackTrace();
                    //Разблокировать всё
                    enableAllViews();
                }
            }));
    }
    //----------------------------------------------------------------------------------------------
    //Вспомогательные методы
    private void makeSnackBar(String msg)
    {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show();
    }
    private void showTopProgressBar()
    {
        binding.createServiceProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideTopProgressBar()
    {
        binding.createServiceProgressBar.setVisibility(View.GONE);
    }
    public void addToLog(String msg)
    {
        Log.d("LOG_TAG", getClass().getName() + " - " + msg);
    }
    //Заблокировать всё
    private void disableAllViews()
    {
        binding.textInputLayoutServiceName.setEnabled(false);
        binding.textInputLayoutServiceCost.setEnabled(false);
        binding.serviceConfirmButton.setEnabled(false);
        binding.serviceDeleteButton.setEnabled(false);
    }
    //Разблокировать всё
    private void enableAllViews()
    {
        binding.textInputLayoutServiceName.setEnabled(true);
        binding.textInputLayoutServiceCost.setEnabled(true);
        binding.serviceConfirmButton.setEnabled(true);
        binding.serviceDeleteButton.setEnabled(true);
    }
    //----------------------------------------------------------------------------------------------
    public interface CreateServiceDialogInterface
    {
        void deleteServicesList(int deletedServiceID);

        void createServiceList(Service service);

        void updateServiceList(Service service);
    }
    //----------------------------------------------------------------------------------------------
}
