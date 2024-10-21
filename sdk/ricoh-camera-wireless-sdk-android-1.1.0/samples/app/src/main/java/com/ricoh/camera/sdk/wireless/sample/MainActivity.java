// Copyright (c) 2017 Ricoh Co., Ltd. All Rights Reserved.
package com.ricoh.camera.sdk.wireless.sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricoh.camera.sdk.wireless.api.CameraImage;
import com.ricoh.camera.sdk.wireless.api.setting.camera.CameraDeviceSetting;
import com.ricoh.camera.sdk.wireless.api.setting.capture.CaptureSetting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        DeviceFragment.OnDeviceFragmentInteractionListener,
        ActionFragment.OnActionFragmentInteractionListener,
        ImageListFragment.OnImageListFragmentInteractionListener,
        CaptureSettingFragment.OnCaptureSettingFragmentInteractionListener,
        CameraSettingFragment.OnCameraSettingFragmentInteractionListener {

    private DeviceFragment mDeviceFragment = new DeviceFragment();
    private ActionFragment mActionFragment = new ActionFragment();
    private ImageListFragment mImageListFragment = new ImageListFragment();
    private CaptureSettingFragment captureSettingFragment = new CaptureSettingFragment();
    private CameraSettingFragment mCameraSettingFragment = new CameraSettingFragment();

    private final List<Fragment> mFragments = new ArrayList<Fragment>();
    {
        mFragments.add(mDeviceFragment);
        mFragments.add(mActionFragment);
        mFragments.add(mImageListFragment);
        mFragments.add(captureSettingFragment);
        mFragments.add(mCameraSettingFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        FragmentManager manager = getSupportFragmentManager();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(manager);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void showEvent(final String content, final TextView eventView){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                eventView.setText(content);
            }
        });
    }

    @Override
    public void showLiveView(final Bitmap receivedImage, final ImageView showView, final TextView statusView){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showView.setImageBitmap(receivedImage);
                statusView.setText(getString(R.string.updated) + Globals.SDF.format(new Date()));
            }
        });
    }

    @Override
    public void onImageListFragmentInteraction(CameraImage item, ImageListFragment.OnImageListFragmentInteractionListener.OPTION option) {
        if (option == ImageListFragment.OnImageListFragmentInteractionListener.OPTION.SHOW_INFO) {
            new PopupWindowFactory(mImageListFragment).createPopupWindowForImageInfo(item);
        } else if (option == ImageListFragment.OnImageListFragmentInteractionListener.OPTION.SHOW_DATA) {
            new PopupWindowFactory(mImageListFragment).createPopupWindowForImageData(item);
        }
    }

    @Override
    public void onCaptureSettingFragmentInteraction(CaptureSetting item) {
        new PopupWindowFactory(captureSettingFragment).createPopupWindowForSettingHasAvailableList(item);
    }

    @Override
    public void onCameraSettingFragmentInteraction(CameraDeviceSetting item, CameraSettingFragment.OnCameraSettingFragmentInteractionListener.OPTION option) {
        if (option == CameraSettingFragment.OnCameraSettingFragmentInteractionListener.OPTION.AVAILABLE) {
            new PopupWindowFactory(mCameraSettingFragment).createPopupWindowForSettingHasAvailableList(item);
        } else if (option == CameraSettingFragment.OnCameraSettingFragmentInteractionListener.OPTION.DATETIME) {
            new PopupWindowFactory(mCameraSettingFragment).createPopupWindowForCameraTime(item);
        } else if(option == CameraSettingFragment.OnCameraSettingFragmentInteractionListener.OPTION.WIFI) {
            new PopupWindowFactory(mCameraSettingFragment).createPopupWindowForWifiSettings(item);
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragments.get(position);

        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Fragment fragment = mFragments.get(position);
            if (fragment instanceof Nameable) {
                return ((Nameable) fragment).getName();
            } else {
                return "Tab" + (position + 1);
            }
        }
    }
}
