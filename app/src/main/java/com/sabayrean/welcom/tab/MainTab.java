package com.sabayrean.welcom.tab;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sabayrean.welcom.R;
import com.sabayrean.welcom.find.FindFriends;
import com.sabayrean.welcom.world.Feed;


public class MainTab extends Fragment {

    SlidingTabLayout mSlidingTabLayout;
    ViewPager mViewPager;

    private LinearLayout ll;
    private FragmentActivity fa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fa = super.getActivity();
        ll = (LinearLayout) inflater.inflate(R.layout.tab_layout, container, false);



        mViewPager = (ViewPager) ll.findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2); // tabcachesize (=tabcount for better performance)


        mSlidingTabLayout = (SlidingTabLayout) ll.findViewById(R.id.sliding_tabs);

        // use own style rules for tab layout
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);

        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(res.getColor(R.color.colorPrimaryDark));
        mSlidingTabLayout.setDistributeEvenly(true);
        mViewPager.setAdapter(new MainTabs(getChildFragmentManager()));

        mSlidingTabLayout.setViewPager(mViewPager);

        // Tab events
        if (mSlidingTabLayout != null) {
            mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        return ll;
    }

    /**
     * The {@link PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    class MainTabs extends FragmentPagerAdapter {

        SparseArray<View> views = new SparseArray<View>();
        public MainTabs(FragmentManager fm) {
            super(fm);
        }

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Feed";
                default:
                    return "World";
            }
        }


        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            Fragment fragment = null;
            if(position==0){
                fragment = new FindFriends();
            }else{
                fragment = new Feed();
            }
            return fragment;
        }

    }
}
