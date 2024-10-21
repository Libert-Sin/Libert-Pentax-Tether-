// Copyright (c) 2017 Ricoh Co., Ltd. All Rights Reserved.
package com.ricoh.camera.sdk.wireless.sample;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ricoh.camera.sdk.wireless.api.CameraDevice;
import com.ricoh.camera.sdk.wireless.api.CameraDeviceDetector;
import com.ricoh.camera.sdk.wireless.api.CameraEventListener;
import com.ricoh.camera.sdk.wireless.api.CameraImage;
import com.ricoh.camera.sdk.wireless.api.CameraStorage;
import com.ricoh.camera.sdk.wireless.api.Capture;
import com.ricoh.camera.sdk.wireless.api.DeviceInterface;
import com.ricoh.camera.sdk.wireless.api.response.Response;
import com.ricoh.camera.sdk.wireless.api.response.Result;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class DeviceFragment extends Fragment implements Nameable {

    private OnDeviceFragmentInteractionListener mListener;

    private Globals mGlobals;

    private TextView mManufactureTextView;
    private TextView mModelTextView;
    private TextView mFirmwareVersionTextView;
    private TextView mSerialNumberTextView;
    private TextView mBatteryLevelTextView;
    private TextView mStorageTextView;
    private TextView mEventTextView;

    private TextView mStateView;

    private EventListenerForShow mEventListenerForShow = new EventListenerForShow();

    private Timer mTimer = null;
    private static final int INTERVAL_MS = 500;

    public DeviceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mGlobals = (Globals) getActivity().getApplication();

        View view = inflater.inflate(R.layout.fragment_device, container, false);

        initViews(view);
        mListener.showEvent(mGlobals.mEventTextBuilder.toString(), mEventTextView);

        showViewInfo();

        view.findViewById(R.id.button_connect).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        connect();
                    }
                }
        );

        view.findViewById(R.id.button_disconnect).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disconnect();
                    }
                }
        );

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDeviceFragmentInteractionListener) {
            mListener = (OnDeviceFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDeviceFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    private void connect() {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                if (mGlobals.mCameraDevice == null) {
                    List<CameraDevice> detectedList = CameraDeviceDetector.detect(DeviceInterface.WLAN);
                    if (detectedList.isEmpty() == true) {
                        return getString(R.string.not_find);
                    }

                    mGlobals.mCameraDevice = addEventListenerIfNeed(detectedList.get(0));
                } else if (mGlobals.mCameraDevice.isConnected(DeviceInterface.WLAN) == false) {
                    List<CameraDevice> detectedList = CameraDeviceDetector.detect(DeviceInterface.WLAN);
                    if (detectedList.isEmpty() == false) {
                        mGlobals.mCameraDevice = addEventListenerIfNeed(detectedList.get(0));
                    }
                }

                Response response = mGlobals.mCameraDevice.connect(DeviceInterface.WLAN);
                if (response.getResult() == Result.OK) {
                    mGlobals.addedEventTextBuffer("connected!");
                    mListener.showEvent(mGlobals.mEventTextBuilder.toString(), mEventTextView);
                    retrieveViewInfo();

                    return getString(R.string.succeed, getString(R.string.connect));
                } else {
                    return getString(R.string.failed, getString(R.string.connect));
                }
            }

            @Override
            protected void onPostExecute(String result) {
                mStateView.setText(result);
                showViewInfo();
            }

        }.execute();
    }

    private CameraDevice addEventListenerIfNeed(CameraDevice cameraDevice){

        boolean isContainEventListenerForShow = false;
        for (CameraEventListener listener: cameraDevice.getEventListeners()) {
            if (listener instanceof EventListenerForShow) {
                isContainEventListenerForShow = true;
            }
        }

        if (isContainEventListenerForShow == false) {
            cameraDevice.addEventListener(mEventListenerForShow);
        }

        return cameraDevice;
    }

    private void disconnect() {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                if (mGlobals.mCameraDevice == null) {
                    return getString(R.string.not_find);
                }

                Response response = mGlobals.mCameraDevice.disconnect(DeviceInterface.WLAN);
                if (response.getResult() == Result.OK) {
                    mGlobals.addedEventTextBuffer("disconnected!");
                    mListener.showEvent(mGlobals.mEventTextBuilder.toString(), mEventTextView);

                    return getString(R.string.succeed, getString(R.string.disconnect));
                } else {
                    return getString(R.string.failed, getString(R.string.disconnect));
                }

            }

            @Override
            protected void onPostExecute(String result) {
                mStateView.setText(result);
                showViewInfo();
            }

        }.execute();
    }

    private void initViews(View view){
        mManufactureTextView = (TextView) view.findViewById(R.id.text_view_manufacture);
        mModelTextView = (TextView) view.findViewById(R.id.text_view_model);
        mFirmwareVersionTextView = (TextView) view.findViewById(R.id.text_view_firmware);
        mSerialNumberTextView = (TextView) view.findViewById(R.id.text_view_serial_number);
        mBatteryLevelTextView = (TextView) view.findViewById(R.id.text_view_battery_level);
        mStorageTextView = (TextView) view.findViewById(R.id.text_view_storage);
        mEventTextView = (TextView) view.findViewById(R.id.text_view_event);
        mStateView = (TextView) view.findViewById(R.id.text_view_connection_state);
    }

    private void retrieveViewInfo(){

        mGlobals.mDeviceInfoMap.put("manufacturer", mGlobals.mCameraDevice.getManufacturer());
        mGlobals.mDeviceInfoMap.put("model", mGlobals.mCameraDevice.getModel());
        mGlobals.mDeviceInfoMap.put("firmwareVersion", mGlobals.mCameraDevice.getFirmwareVersion());
        mGlobals.mDeviceInfoMap.put("serialNumber", mGlobals.mCameraDevice.getSerialNumber());

        String batteryLevel = String.valueOf(mGlobals.mCameraDevice.getStatus().getBatteryLevel());
        mGlobals.mDeviceInfoMap.put("batteryLevel", batteryLevel);

        StringBuilder builder = new StringBuilder();
        for (CameraStorage cs: mGlobals.mCameraDevice.getStorages()) {
            builder.append("\n\t\t[" + cs.getId() + "]");
            builder.append(" available:" + cs.isAvailable());
            builder.append(", remain:" + cs.getRemainingPictures());
            builder.append(", type:" + cs.getType());
            builder.append(", permission:" + cs.getPermission());
            builder.append(", state:" + cs.getListImagesState());
        }
        mGlobals.mDeviceInfoMap.put("storage", builder.toString());
    }

    private void showViewInfo(){

        if( mGlobals.mDeviceInfoMap.isEmpty() == true) {
            return;
        }

        mManufactureTextView.setText(mGlobals.mDeviceInfoMap.get("manufacturer"));
        mModelTextView.setText(mGlobals.mDeviceInfoMap.get("model"));
        mFirmwareVersionTextView.setText(mGlobals.mDeviceInfoMap.get("firmwareVersion"));
        mSerialNumberTextView.setText(mGlobals.mDeviceInfoMap.get("serialNumber"));
        mBatteryLevelTextView.setText(mGlobals.mDeviceInfoMap.get("batteryLevel"));
        mStorageTextView.setText(mGlobals.mDeviceInfoMap.get("storage"));
    }

    @Override
    public String getName() {
        return "Device";
    }

    public interface OnDeviceFragmentInteractionListener {
        void showEvent(String content, TextView eventView);
    }

    public class EventListenerForShow extends CameraEventListener {
        @Override
        public void imageStored(CameraDevice sender, CameraImage image) {
            if (isAdded() == true) {
                mGlobals.addedEventTextBuffer(getString(R.string.event_image_stored, image.getName()));
            } else {
                mGlobals.addedEventTextBuffer("ImageStored " + image.getName());
            }
        }

        @Override
        public void captureComplete(CameraDevice sender, Capture capture) {
            if (isAdded() == true) {
                mGlobals.addedEventTextBuffer(getString(R.string.event_capture_complete, capture.getId()));
            } else {
                mGlobals.addedEventTextBuffer("CaptureComplete " + capture.getId());
            }
        }

        @Override
        public void deviceDisconnected(CameraDevice sender) {
            if (isAdded() == true) {
                mGlobals.addedEventTextBuffer(getString(R.string.event_device_disconnected));
            }else {
                mGlobals.addedEventTextBuffer("DeviceDisconnected");
            }
        }

    }

    private class UpdateTimerTask extends TimerTask {

        @Override
        public void run() {
            if (mGlobals.mCameraDevice == null) {
                return;
            }

            retrieveViewInfo();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showViewInfo();
                }
            });

            mListener.showEvent(mGlobals.mEventTextBuilder.toString(), mEventTextView);
        }
    }

}
