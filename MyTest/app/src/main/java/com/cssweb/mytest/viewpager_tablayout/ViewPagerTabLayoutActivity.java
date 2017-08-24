package com.cssweb.mytest.viewpager_tablayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.cssweb.mytest.R;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/7/20.
 */

public class ViewPagerTabLayoutActivity extends FragmentActivity {
    private static final String TAG = "VPTabLayoutActivity";
    TabLayout mTabLayout;
    private ViewPager mViewPager;
    Fragment mFragmentAll;
    Fragment mFragmentUnPay;
    Fragment mFragmentUnFinish;
    Fragment mFragmentFinished;
    private List<Fragment> list;
    private MyAdapter adapter;
    private String[] titles = {"全部", "未支付", "未完成", "已完成"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] mIconUnselectIds = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private int[] mIconSelectIds = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    CommonTabLayout tabLayout_2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_viewpager_tablayout);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //        mTabLayout.addTab(mTabLayout.newTab().setText("全部"));
        //        mTabLayout.addTab(mTabLayout.newTab().setText("未支付"));
        //        mTabLayout.addTab(mTabLayout.newTab().setText("未完成"));
        //        mTabLayout.addTab(mTabLayout.newTab().setText("已完成"));

        list = new ArrayList<>();

        mFragmentAll = TabFragment.getInstance("全部");
        mFragmentUnPay = TabFragment.getInstance("未支付");
        mFragmentUnFinish = TabFragment.getInstance("未完成 ");
        mFragmentFinished = TabFragment.getInstance("已完成");
        list.add(mFragmentAll);
        list.add(mFragmentUnPay);
        list.add(mFragmentUnFinish);
        list.add(mFragmentFinished);

        //ViewPager的适配器
        adapter = new MyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        //绑定
        //        mTabLayout.setupWithViewPager(mViewPager);

        //----------------------

        View decorView = getWindow().getDecorView();
        SlidingTabLayout tabLayout_2 = ViewFindUtils.find(decorView, R.id.tablayout2);
        tabLayout_2.setViewPager(mViewPager);


        //----------
        //        for (int i = 0; i < titles.length; i++) {
        //            mTabEntities.add(new TabEntity(titles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        //        }
        //        View decorView = getWindow().getDecorView();
        //        tabLayout_2 = ViewFindUtils.find(decorView, R.id.tablayout2);
        //        //        tabLayout_2.setViewPager(mViewPager);
        //        tabLayout_2.setTabData(mTabEntities);
        //        tabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
        //            @Override
        //            public void onTabSelect(int position) {
        //                mViewPager.setCurrentItem(position);
        //            }
        //
        //            @Override
        //            public void onTabReselect(int position) {
        //
        //            }
        //        });
        //
        //
        //        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        //            @Override
        //            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //
        //            }
        //
        //            @Override
        //            public void onPageSelected(int position) {
        //                tabLayout_2.setCurrentTab(position);
        //            }
        //
        //            @Override
        //            public void onPageScrollStateChanged(int state) {
        //
        //            }
        //        });
        //
        //        mViewPager.setCurrentItem(1);


    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


}
