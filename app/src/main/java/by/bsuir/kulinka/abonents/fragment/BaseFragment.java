package by.bsuir.kulinka.abonents.fragment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment
{
    //----------------------------------------------------------------------------------------------
    public ActivityInterface activityInterface;
    //----------------------------------------------------------------------------------------------
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            activityInterface = (ActivityInterface) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement ActivityInterface");
        }
    }
    //----------------------------------------------------------------------------------------------
    public void hideBottomBar()
    {
        activityInterface.hideBottomBar();
    }
    public void showBottomBar()
    {
        activityInterface.showBottomBar();
    }
    public void addToLog(String msg)
    {
        Log.d("LOG_TAG", msg);
    }
    //----------------------------------------------------------------------------------------------
    public interface ActivityInterface
    {
        void showBottomBar();

        void hideBottomBar();
    }
    //----------------------------------------------------------------------------------------------
}
