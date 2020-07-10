package info.devram.dainikhatabook.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import info.devram.dainikhatabook.ViewModel.MainActivityViewModel;
import info.devram.dainikhatabook.ui.ExpensePageFragment;
import info.devram.dainikhatabook.ui.IncomePageFragment;
import info.devram.dainikhatabook.ui.TodayPageFragment;

public class MainActivityPagerAdapter extends FragmentStateAdapter {
    private MainActivityViewModel mainActivityViewModel;

    public MainActivityPagerAdapter(@NonNull FragmentManager fragmentManager,
                                    @NonNull Lifecycle lifecycle,
                                    MainActivityViewModel viewModel) {
        super(fragmentManager, lifecycle);
        this.mainActivityViewModel = viewModel;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TodayPageFragment(mainActivityViewModel);
            case 1:
                return new ExpensePageFragment(mainActivityViewModel);
            case 2:
                return new IncomePageFragment(mainActivityViewModel);
            default:
                return null;

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
