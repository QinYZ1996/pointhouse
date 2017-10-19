package com.pointhouse.chiguan.k1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by ljj on 2017/7/3.
 */

public class CourseFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 2;
    private CourseClassifiCationActivity fragment1 = null;
    private PhTeacherListActivity fragment2 = null;

    public CourseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragment1 = new CourseClassifiCationActivity();
        fragment2 = new PhTeacherListActivity();
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case CourseClassifiCationMainActivity.PAGE_ONE:
                fragment = fragment1;
                break;
            case CourseClassifiCationMainActivity.PAGE_TWO:
                fragment = fragment2;
                break;
        }
        return fragment;
    }

}
