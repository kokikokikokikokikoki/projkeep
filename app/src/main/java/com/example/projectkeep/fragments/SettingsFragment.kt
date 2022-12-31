package com.example.projectkeep.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.projectkeep.R
import com.example.projectkeep.adapter.ViewPagerAdapter

import com.example.projectkeep.databinding.LayoutTabBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator



class SettingsFragment : Fragment() {
        //connect the expense_tab_layout


    //connect with viewpager
   //private lateinit var viewPagerAdapter: ViewPagerAdapter
//    private lateinit var viewPager: ViewPager
//    private lateinit var tabLayout: TabLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)






    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = LayoutTabBinding.bind(view)

        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Expense"
                }
                1 -> {
                    tab.text = "Income"
                }
            }
        }.attach()




    }






}