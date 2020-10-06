package by.bsuir.kulinka.abonents;

import android.os.Bundle;

import com.bsuir.bottomapp.bar.abonents.R;
import com.bsuir.bottomapp.bar.abonents.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import by.bsuir.kulinka.abonents.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.ActivityInterface
{
    //----------------------------------------------------------------------------------------------
    //Переменная для биндинга
    ActivityMainBinding binding;
    private SearchView searchView;

    //Фрагменты BottomNavigation
    final MainFragment baseFragment = new MainFragment();
    final FragmentManager fm = getSupportFragmentManager();
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Показать основной фрагмент
        showBaseFragment();
    }
    //----------------------------------------------------------------------------------------------
    //Инициализация списка фрагментов
    private void showBaseFragment()
    {
        fm.beginTransaction().add(R.id.main_container, baseFragment, "baseFragment").commit();
    }
    //----------------------------------------------------------------------------------------------
    //Методы интерфейса MainFragment
    @Override
    public void loadFragment(Fragment fragment, String fragmentTAG)
    {
        fm.beginTransaction().replace(R.id.main_container, fragment)
                .addToBackStack(fragmentTAG).commit();
    }
    //----------------------------------------------------------------------------------------------
}
