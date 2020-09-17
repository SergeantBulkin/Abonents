package com.example.bottomapp.bar.test;

import android.os.Bundle;

import com.example.bottomapp.bar.test.databinding.ActivityMainBinding;
import com.example.bottomapp.bar.test.fragment.AbonentFragment;
import com.example.bottomapp.bar.test.fragment.PlanFragment;
import com.example.bottomapp.bar.test.fragment.ServiceFragment;

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
        //Настройка боттом бара
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
    //Настройка боттом бара
    private void setUpBottomBar()
    {
        binding.bottomBar.setOnItemSelectedListener(i ->
        {
            loadFragment(i);
            return true;
        });
    }
    //----------------------------------------------------------------------------------------------
    private void loadFragment(int i)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragmentList.get(i)).commit();
    }
    //----------------------------------------------------------------------------------------------
}
