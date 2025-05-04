package com.example.tamagochi;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DashboardAdopter extends FragmentStateAdapter {
    public DashboardAdopter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position ==0){
            return new Home();
        }
        else if(position == 1)
        {
            return new Shop();
        } else if (position == 2) {
            return new Profile();
        }
        return new Home();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
