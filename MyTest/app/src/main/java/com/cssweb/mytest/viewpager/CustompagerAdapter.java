package com.cssweb.mytest.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * @author Moto
 * @ClassName ImageViewPagerAdapter
 * @Description TODO
 * @date 2014 2014-7-1
 */
public class CustompagerAdapter extends PagerAdapter {

    private ArrayList<View> mViewList;

    public CustompagerAdapter(ArrayList<View> pArrayList) {
        this.mViewList = pArrayList;
    }

    @Override
    public int getCount() {
        if (mViewList != null) {
            return mViewList.size();
        }
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        if (mViewList.size() != 0)
            container.removeView(mViewList.get(position));
//        else
        //            super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int pPosition) {
        //        if (mViewList.size() != 0) {
        container.addView(mViewList.get(pPosition), 0);
        return mViewList.get(pPosition);
        //        } else
        //            return super.instantiateItem(container, pPosition);
    }

    @Override
    public boolean isViewFromObject(View pView, Object pObject) {
        return (pView == pObject);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}