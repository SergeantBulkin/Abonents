package by.bsuir.kulinka.abonents.model;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;

import com.bsuir.bottomapp.bar.abonents.R;
import com.bsuir.bottomapp.bar.abonents.databinding.ViewSearchBinding;
import com.google.android.material.circularreveal.CircularRevealCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SearchView extends FrameLayout
{
    ViewSearchBinding binding;
    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_search, this, true);
        binding = ViewSearchBinding.bind(view);
        binding.openSearchButton.setOnClickListener(v ->
        {
            openSearch();
        });
        binding.closeSearchButton.setOnClickListener(v ->
        {
            closeSearch();
        });
        binding.executeSearchButton.setOnClickListener(v ->
        {
            binding.searchInputText.setText("");
        });
    }

    private void openSearch()
    {
        binding.searchInputText.setText("");
        binding.searchOpenView.setVisibility(VISIBLE);
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                binding.searchOpenView,
                (binding.openSearchButton.getRight() + binding.openSearchButton.getLeft()) /2,
                (binding.openSearchButton.getTop() + binding.openSearchButton.getBottom()) /2,
                0f, getWidth());
        circularReveal.setDuration(200);
        circularReveal.start();
    }

    private void closeSearch()
    {
        Animator circularCancel = ViewAnimationUtils.createCircularReveal(
                binding.searchOpenView,
                (binding.openSearchButton.getRight() + binding.openSearchButton.getLeft()) / 2,
                (binding.openSearchButton.getTop() + binding.openSearchButton.getBottom()) / 2,
                getWidth(), 0f);
        circularCancel.setDuration(200);
        circularCancel.start();
        circularCancel.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                binding.searchOpenView.setVisibility(INVISIBLE);
                binding.searchInputText.setText("");
                circularCancel.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });
    }
}
