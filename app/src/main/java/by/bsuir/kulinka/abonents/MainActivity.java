package by.bsuir.kulinka.abonents;

import android.os.Bundle;

import com.bsuir.bottomapp.bar.abonents.R;

import by.bsuir.kulinka.abonents.fragment.AbonentFragment;
import by.bsuir.kulinka.abonents.fragment.PlanFragment;
import by.bsuir.kulinka.abonents.fragment.ServiceFragment;
import com.bsuir.bottomapp.bar.abonents.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //Переменная для биндинга
    ActivityMainBinding binding;
    List<Fragment> fragmentList;
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Инициализация списка фрагментов
        createFragments();
        //Настройка BottomBar
        setUpBottomBar();
        //Установка первого фрагмента
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, fragmentList.get(0)).commit();
    }
    //----------------------------------------------------------------------------------------------
    //Инициализация списка фрагментов
    private void createFragments()
    {
        fragmentList = new ArrayList<>();
        fragmentList.add(new AbonentFragment());
        fragmentList.add(new PlanFragment());
        fragmentList.add(new ServiceFragment());
    }
    //----------------------------------------------------------------------------------------------
    //Настройка BottomBar
    private void setUpBottomBar()
    {
        binding.bottomBar.setOnItemSelectedListener(i ->
        {
            loadFragment(i);
            return true;
        });
    }
    //----------------------------------------------------------------------------------------------
    //Заменить фрагмент в контейнере
    private void loadFragment(int i)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragmentList.get(i)).commit();
    }
    //----------------------------------------------------------------------------------------------
}
