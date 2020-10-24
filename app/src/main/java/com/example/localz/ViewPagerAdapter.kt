package com.example.localz

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) :FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {

        when (position) {

            0 ->{
                return Favorites()
            }
            1 ->{
                return Reviews()
            }

        }
        return Favorites()
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0-> return "Favorites"
            1-> return "Reviews"
        }
        return null
    }
}