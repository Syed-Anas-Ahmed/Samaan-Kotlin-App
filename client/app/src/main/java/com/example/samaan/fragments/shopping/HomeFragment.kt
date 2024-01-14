package com.example.samaan.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.samaan.R
import com.example.samaan.adapters.HomeViewpagerAdapter
import com.example.samaan.databinding.FragmentHomeBinding
import com.example.samaan.fragments.categories.AccesssoryFragment
import com.example.samaan.fragments.categories.ChairFragment
import com.example.samaan.fragments.categories.CupboardFragment
import com.example.samaan.fragments.categories.FurnitureFragment
import com.example.samaan.fragments.categories.MainCategoryFragment
import com.example.samaan.fragments.categories.TableFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.common.collect.Table

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            FurnitureFragment(),
            CupboardFragment(),
            TableFragment(),
            AccesssoryFragment(),

            )

        val viewpagerAdapter =
            HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewpagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Main"
                }

                1 -> {
                    tab.text = "Chairs"
                }

                2 -> {
                    tab.text = "Cupboards"
                }

                3 -> {
                    tab.text = "Table"
                }

                4 -> {
                    tab.text = "Accessories"
                }

                5 -> {
                    tab.text = "Furniture"
                }
            }
        }.attach()
    }
}
