package by.bsuir.kulinka.abonents.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bsuir.bottomapp.bar.abonents.R;

public class PlanFragment extends BaseFragment
{
    public PlanFragment()
    {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }
}