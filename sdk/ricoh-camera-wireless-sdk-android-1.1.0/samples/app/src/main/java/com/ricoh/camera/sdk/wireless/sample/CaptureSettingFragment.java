// Copyright (c) 2017 Ricoh Co., Ltd. All Rights Reserved.
package com.ricoh.camera.sdk.wireless.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ricoh.camera.sdk.wireless.api.setting.capture.CaptureSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CaptureSettingFragment extends Fragment implements Nameable {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnCaptureSettingFragmentInteractionListener mListener;

    private Globals mGlobals;
    private CaptureSettingRecyclerViewAdapter mCaptureSettingRecyclerViewAdapter;

    private List<CaptureSetting> mCaptureSettings = new ArrayList<CaptureSetting>();

    private Timer mTimer = null;
    private static final int INTERVAL_MS = 500;

    public CaptureSettingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mGlobals = (Globals) getActivity().getApplication();

        View view = inflater.inflate(R.layout.fragment_capture_setting, container, false);

        if (view instanceof RecyclerView == false) {
            return view;
        }

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        mCaptureSettingRecyclerViewAdapter =
            new CaptureSettingRecyclerViewAdapter(mCaptureSettings, mListener);
        recyclerView.setAdapter(mCaptureSettingRecyclerViewAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Timer();
        mTimer.schedule(new UpdateTimerTask(), 0, INTERVAL_MS);
    }

    @Override
    public void onPause() {
        super.onPause();
        mTimer.cancel();
        mTimer = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCaptureSettingFragmentInteractionListener) {
            mListener = (OnCaptureSettingFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCaptureSettingFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public String getName() {
        return "Capture Setting";
    }

    public interface OnCaptureSettingFragmentInteractionListener {
        void onCaptureSettingFragmentInteraction(CaptureSetting item);
    }

    private class UpdateTimerTask extends TimerTask{
        @Override
        public void run() {
            if (mGlobals.mCameraDevice == null) {
                return;
            }

            List<CaptureSetting> currentSettings = new ArrayList<>(Globals.getAllCaptureSettings());
            mGlobals.mCameraDevice.getCaptureSettings(currentSettings);
            mCaptureSettings.clear();
            for (CaptureSetting cs: currentSettings) {
                if (cs.getValue() != null) {
                    mCaptureSettings.add(cs);
                }
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCaptureSettingRecyclerViewAdapter.notifyDataSetChanged();
                }
            });
        }
    }

}
