package com.birdeveloper.androidutils

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

fun AppCompatActivity.setupTabs(tabLayout: TabLayout?, viewPager: ViewPager?, tabs: List<Tab>) {
    viewPager?.adapter = SectionsPagerAdapter(this, supportFragmentManager, tabs)
    tabLayout?.setupWithViewPager(viewPager)

    // setup Tab Icons
    if (tabs[0].iconRes != null) // if the first tab has icon then the rest will have one
        tabs.forEachIndexed { index, tab -> tabLayout?.getTabAt(index)?.setIcon(tab.iconRes!!) }
}

fun Fragment.setupTabs(tabLayout: TabLayout?, viewPager: ViewPager?, tabs: List<Tab>) {
    context?.let { context ->
        viewPager?.adapter = SectionsPagerAdapter(context, childFragmentManager, tabs)
        tabLayout?.setupWithViewPager(viewPager)

        // setup Tab Icons
        if (tabs[0].iconRes != null) // if the first tab has icon then the rest will have one
            tabs.forEachIndexed { index, tab -> tabLayout?.getTabAt(index)?.setIcon(tab.iconRes!!) }
    }
}

data class Tab(val fragment: Fragment, @StringRes val titleRes: Int? = null, @DrawableRes val iconRes: Int? = null)

class SectionsPagerAdapter(val context: Context, fm: FragmentManager, private val tabs: List<Tab>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = tabs[position].fragment
    override fun getPageTitle(position: Int): CharSequence? = tabs[position].titleRes?.let { context.getString(it) }
    override fun getCount() = tabs.size
}
