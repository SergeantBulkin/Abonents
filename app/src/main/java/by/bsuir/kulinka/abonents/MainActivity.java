package by.bsuir.kulinka.abonents;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bsuir.bottomapp.bar.abonents.R;
import com.bsuir.bottomapp.bar.abonents.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import by.bsuir.kulinka.abonents.fragment.AbonentFragment;
import by.bsuir.kulinka.abonents.fragment.BaseFragment;
import by.bsuir.kulinka.abonents.fragment.PlanFragment;
import by.bsuir.kulinka.abonents.fragment.ServiceFragment;

public class MainActivity extends AppCompatActivity implements BaseFragment.ActivityInterface
{
    //----------------------------------------------------------------------------------------------
    //Переменная для биндинга
    ActivityMainBinding binding;
    private SearchView searchView;

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

        setSupportActionBar(binding.toolBar);
        //getSupportActionBar().setTitle(R.string.app_name);

        //Настройка BottomBar
        setUpBottomBar();

        //Инициализация списка фрагментов
        createFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.abonents_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    //Методы интерфейса BaseFragment
    @Override
    public void showBottomBar()
    {
        binding.bottomBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomBar()
    {
        binding.bottomBar.setVisibility(View.GONE);
    }
    //----------------------------------------------------------------------------------------------
}
