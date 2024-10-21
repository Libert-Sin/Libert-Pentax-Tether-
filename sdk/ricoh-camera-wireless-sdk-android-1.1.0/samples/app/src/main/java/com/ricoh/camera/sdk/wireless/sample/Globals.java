// Copyright (c) 2017 Ricoh Co., Ltd. All Rights Reserved.
package com.ricoh.camera.sdk.wireless.sample;

import android.app.Application;

import com.ricoh.camera.sdk.wireless.api.CameraDevice;
import com.ricoh.camera.sdk.wireless.api.DeviceInterface;
import com.ricoh.camera.sdk.wireless.api.setting.camera.CameraDeviceSetting;
import com.ricoh.camera.sdk.wireless.api.setting.camera.CameraTime;
import com.ricoh.camera.sdk.wireless.api.setting.camera.Channel;
import com.ricoh.camera.sdk.wireless.api.setting.camera.DualCardSlotsMode;
import com.ricoh.camera.sdk.wireless.api.setting.camera.Key;
import com.ricoh.camera.sdk.wireless.api.setting.camera.SSID;
import com.ricoh.camera.sdk.wireless.api.setting.capture.CaptureMethod;
import com.ricoh.camera.sdk.wireless.api.setting.capture.CaptureSetting;
import com.ricoh.camera.sdk.wireless.api.setting.capture.CustomImage;
import com.ricoh.camera.sdk.wireless.api.setting.capture.DigitalFilter;
import com.ricoh.camera.sdk.wireless.api.setting.capture.ExposureCompensation;
import com.ricoh.camera.sdk.wireless.api.setting.capture.FNumber;
import com.ricoh.camera.sdk.wireless.api.setting.capture.ISO;
import com.ricoh.camera.sdk.wireless.api.setting.capture.MovieCaptureFormat;
import com.ricoh.camera.sdk.wireless.api.setting.capture.StillImageQuality;
import com.ricoh.camera.sdk.wireless.api.setting.capture.ShutterSpeed;
import com.ricoh.camera.sdk.wireless.api.setting.capture.StillImageCaptureFormat;
import com.ricoh.camera.sdk.wireless.api.setting.capture.WhiteBalance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Globals extends Application {

    public static final SimpleDateFormat SDF = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public CameraDevice mCameraDevice;

    public Map<String, String> mDeviceInfoMap = new HashMap<String, String>();

    public StringBuilder mEventTextBuilder = new StringBuilder();

    public static final List<CaptureSetting> getAllCaptureSettings(){

        List<CaptureSetting> settings = new ArrayList<CaptureSetting>();

        settings.add(new FNumber());
        settings.add(new ExposureCompensation());
        settings.add(new ISO());
        settings.add(new WhiteBalance());
        settings.add(new ShutterSpeed());
        settings.add(new StillImageCaptureFormat());
        settings.add(new StillImageQuality());
        settings.add(new MovieCaptureFormat());
        settings.add(new CustomImage());
        settings.add(new DigitalFilter());
        settings.add(new CaptureMethod());

        return settings;
    }

    public static final List<CameraDeviceSetting> getAllCameraSettings(){

        List<CameraDeviceSetting> settings = new ArrayList<CameraDeviceSetting>();

        settings.add(new CameraTime());
        settings.add(new DualCardSlotsMode());
        settings.add(new SSID());
        settings.add(new Key());
        settings.add(new Channel());

        return settings;
    }

    public boolean canUseCamera(){

        if (mCameraDevice == null) {
            return false;
        }

        return mCameraDevice.isConnected(DeviceInterface.WLAN);
    }

    public void addedEventTextBuffer(String message){
        mEventTextBuilder.insert(0, "[" + Globals.SDF.format(new Date()) + "] " + message + "\n");
    }

}
