package com.example.projectkeep.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectkeep.fragments.ExpenseFragment
import com.example.projectkeep.fragments.ReportFragment


// viewpager adapter
class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ExpenseFragment()
            }
            1 -> {
                ReportFragment()
            }
            else -> {
                ExpenseFragment()
            }


        }
    }
}









