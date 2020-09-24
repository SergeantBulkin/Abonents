package by.bsuir.kulinka.abonents;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bsuir.bottomapp.bar.abonents.R;
import com.bsuir.bottomapp.bar.abonents.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import by.bsuir.kulinka.abonents.fragment.AbonentFragment;
import by.bsuir.kulinka.abonents.fragment.PlanFragment;
import by.bsuir.kulinka.abonents.fragment.ServiceFragment;

public class MainActivity extends AppCompatActivity
{
    //----------------------------------------------------------------------------------------------
    //Переменная для биндинга
    ActivityMainBinding binding;

    //Фрагменты BottomNavigationView
    final Fragment abonents = new AbonentFragment();
    final Fragment plans = new PlanFragment();
    final Fragment services = new ServiceFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = abonents;
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Настройка BottomBar
        setUpBottomBar();

        //Инициализация списка фрагментов
        createFragments();
    }
    //----------------------------------------------------------------------------------------------
    //Инициализация списка фрагментов
    private void createFragments()
    {
        fm.beginTransaction().add(R.id.main_container, services, "services").hide(services).commit();
        fm.beginTransaction().add(R.id.main_container, plans, "plans").hide(plans).commit();
        fm.beginTransaction().add(R.id.main_container, abonents, "abonents").commit();
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
                    fm.beginTransaction().hide(active).show(abonents).commit();
                    active = abonents;
                    return true;
                case 1:
                    fm.beginTransaction().hide(active).show(plans).commit();
                    active = plans;
                    return true;
                case 2:
                    fm.beginTransaction().hide(active).show(services).commit();
                    active = services;
                    return true;
            }
            return true;
        });
    }
    //----------------------------------------------------------------------------------------------
}
