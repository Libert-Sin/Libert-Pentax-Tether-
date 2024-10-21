// Copyright (c) 2017 Ricoh Co., Ltd. All Rights Reserved.
package com.ricoh.camera.sdk.wireless.sample;

import static android.content.ContentValues.TAG;
import static android.provider.Settings.System.DATE_FORMAT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import static com.ricoh.camera.sdk.wireless.sample.Globals.SDF;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.ricoh.camera.sdk.wireless.api.CameraDevice;
import com.ricoh.camera.sdk.wireless.api.CameraImage;
import com.ricoh.camera.sdk.wireless.api.ImageFormat;
import com.ricoh.camera.sdk.wireless.api.response.Response;
import com.ricoh.camera.sdk.wireless.api.response.Result;
import com.ricoh.camera.sdk.wireless.api.setting.camera.CameraDeviceSetting;
import com.ricoh.camera.sdk.wireless.api.setting.camera.CameraTime;
import com.ricoh.camera.sdk.wireless.api.setting.camera.Channel;
import com.ricoh.camera.sdk.wireless.api.setting.camera.Key;
import com.ricoh.camera.sdk.wireless.api.setting.camera.SSID;
import com.ricoh.camera.sdk.wireless.api.setting.capture.CaptureSetting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

public class PopupWindowFactory {

    private enum DOWNLOAD_TYPE{
        DATA, THUMB
    }

    private Fragment mFragment;
    private Globals mGlobals;

    public PopupWindowFactory(Fragment fragment){
        mFragment = fragment;
        mGlobals = (Globals) fragment.getActivity().getApplication();
    }

    public void createPopupWindowForSettingHasAvailableList(final Object item) {

        if (item instanceof CameraDeviceSetting == false && item instanceof CaptureSetting == false) {
            return;
        }

        LayoutInflater layoutInflater = mFragment.getActivity().getLayoutInflater();
        LinearLayout popLayout = (LinearLayout) layoutInflater.inflate(R.layout.popup_setting, null);

        initPopupWindow(popLayout);

        final TextView textView = (TextView) popLayout.findViewById(R.id.text_view_setting_name);
        final Spinner spinner = (Spinner) popLayout.findViewById(R.id.spinner_available_settings);
        final TextView stateView = (TextView) popLayout.findViewById(R.id.text_view_setting_state);

        if (item instanceof CameraDeviceSetting == true) {
            textView.setText(((CameraDeviceSetting)item).getName());
        } else if(item instanceof CaptureSetting == true) {
            textView.setText(((CaptureSetting)item).getName());
        }

        setAdapterWithAvailableSettings(spinner, item);

        Button button = (Button) popLayout.findViewById(R.id.button_apply_setting);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Object selectedItem = (Object) spinner.getSelectedItem();

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {

                        Response res = null;

                        CameraDevice cameraDevice = mGlobals.mCameraDevice;
                        if (selectedItem instanceof CameraDeviceSetting == true) {
                            res = cameraDevice.setCameraDeviceSettings(Arrays.asList(
                                new CameraDeviceSetting[]{(CameraDeviceSetting) selectedItem}));
                            if (res.getResult() == Result.OK) {
                                return mFragment.getString(R.string.succeed, "set " + ((CameraDeviceSetting) selectedItem).getValue());
                            } else {
                                return mFragment.getString(R.string.failed, "set " + ((CameraDeviceSetting) selectedItem).getValue() + " (" + res.getErrors().get(0).getCode() + ")");
                            }
                        } else if(selectedItem instanceof CaptureSetting == true) {
                            res = cameraDevice.setCaptureSettings(Arrays.asList(
                                new CaptureSetting[]{(CaptureSetting) selectedItem}));
                            if (res.getResult() == Result.OK) {
                                return mFragment.getString(R.string.succeed, "set " + ((CaptureSetting) selectedItem).getValue());
                            } else {
                                return mFragment.getString(R.string.failed, "set " + ((CaptureSetting) selectedItem).getValue() + " (" + res.getErrors().get(0).getCode() + ")");
                            }
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        stateView.setText(result);
                    }

                }.execute();

            }
        });

    }

    public void createPopupWindowForCameraTime(final CameraDeviceSetting item) {

        LayoutInflater layoutInflater = mFragment.getActivity().getLayoutInflater();
        LinearLayout popLayout = (LinearLayout) layoutInflater.inflate(R.layout.popup_setting_datetime, null);

        initPopupWindow(popLayout);

        final TextView textView = (TextView) popLayout.findViewById(R.id.text_view_setting_name);
        final EditText editText = (EditText) popLayout.findViewById(R.id.edit_text_datetime);
        final TextView stateView = (TextView) popLayout.findViewById(R.id.text_view_setting_state);

        textView.setText(item.getName());

        Button button = (Button) popLayout.findViewById(R.id.button_apply_setting);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String datetime = editText.getText().toString();

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        Date date = null;
                        try {
                            date = SDF.parse(datetime);
                        }catch (ParseException e) {
                            return mFragment.getString(R.string.format_err, DATE_FORMAT);
                        }

                        Response res = mGlobals.mCameraDevice.setCameraDeviceSettings(
                            Arrays.asList(new CameraDeviceSetting[]{new CameraTime(date)}));
                        if (res.getResult() == Result.OK) {
                            return mFragment.getString(R.string.succeed, "set " + datetime);
                        } else {
                            return mFragment.getString(R.string.failed, "set " + datetime + " (" + res.getErrors().get(0).getCode() + ")");
                        }
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        stateView.setText(result);
                    }

                }.execute();

            }
        });

    }

    public void createPopupWindowForWifiSettings(final CameraDeviceSetting item) {

        LayoutInflater layoutInflater = mFragment.getActivity().getLayoutInflater();
        LinearLayout popLayout = (LinearLayout) layoutInflater.inflate(R.layout.popup_setting_wifi, null);

        initPopupWindow(popLayout);

        TextView textView = (TextView) popLayout.findViewById(R.id.text_view_setting_name);
        textView.setText(new SSID().getName() + "/" + new Key().getName() + "/" + Channel.N0.getName());

        final EditText ssidText = (EditText) popLayout.findViewById(R.id.edit_text_view_ssid);
        final EditText keyText = (EditText) popLayout.findViewById(R.id.edit_text_view_key);
        final Spinner spinner = (Spinner) popLayout.findViewById(R.id.spinner_channel);
        final TextView stateView = (TextView) popLayout.findViewById(R.id.text_view_setting_state);

        final Channel channel = new Channel();
        mGlobals.mCameraDevice.getCameraDeviceSettings(Arrays.asList(new CameraDeviceSetting[]{channel}));

        setAdapterWithAvailableSettings(spinner, channel);

        Button button = (Button) popLayout.findViewById(R.id.button_apply_setting);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String ssid = ssidText.getText().toString();
                final String key = keyText.getText().toString();
                final CameraDeviceSetting selectedItem = (CameraDeviceSetting) spinner.getSelectedItem();

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        Response res = mGlobals.mCameraDevice.setCameraDeviceSettings(
                                Arrays.asList(new CameraDeviceSetting[]{new SSID(ssid), new Key(key), selectedItem}));
                        if (res.getResult() == Result.OK) {
                            return mFragment.getString(R.string.succeed, "set SSID:" + ssid + " Key:" + key + " " + selectedItem);
                        } else {
                            return mFragment.getString(R.string.failed, "set SSID:" + ssid + " Key:" + key + " " + selectedItem);
                        }
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        stateView.setText(result);
                    }

                }.execute();

            }
        });

    }

    public void createPopupWindowForImageInfo(final CameraImage item) {

        LayoutInflater layoutInflater = mFragment.getActivity().getLayoutInflater();
        LinearLayout popLayout = (LinearLayout) layoutInflater.inflate(R.layout.popup_image_info, null);

        initPopupWindow(popLayout);

        final ImageView thumbView = (ImageView) popLayout.findViewById(R.id.image_view_thumb);
        final TextView nameView = (TextView) popLayout.findViewById(R.id.text_view_file_name);
        final TextView typeView = (TextView) popLayout.findViewById(R.id.text_view_file_type);
        final TextView formatView = (TextView) popLayout.findViewById(R.id.text_view_file_format);
        final TextView datetimeView = (TextView) popLayout.findViewById(R.id.text_view_file_datetime);
        final TextView hasThumbView = (TextView) popLayout.findViewById(R.id.text_view_has_thumb);
        final TextView storageView = (TextView) popLayout.findViewById(R.id.text_view_file_storage);

        nameView.setText(item.getName());
        typeView.setText(item.getType().toString());
        formatView.setText(item.getFormat().toString());
        if (item.getDateTime() != null) {
            datetimeView.setText(Globals.SDF.format(item.getDateTime()));
        }
        hasThumbView.setText(String.valueOf(item.hasThumbnail()));
        storageView.setText(item.getStorage().getId());

        if (item.hasThumbnail() == false) {
            return;
        }

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                return download(item, DOWNLOAD_TYPE.THUMB);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                if (result == null) {
                    return;
                }

                thumbView.setImageBitmap(result);
            }

        }.execute();

    }

    public void createPopupWindowForImageData(final CameraImage item) {

        LayoutInflater layoutInflater = mFragment.getActivity().getLayoutInflater();
        LinearLayout popLayout = (LinearLayout) layoutInflater.inflate(R.layout.popup_image_data, null);

        initPopupWindow(popLayout);

        final ImageView imageView = (ImageView) popLayout.findViewById(R.id.image_view_download_image);
        final TextView textView = (TextView) popLayout.findViewById(R.id.text_view_download_state);

        if (item.getFormat() != ImageFormat.JPEG) {
            textView.setText(mFragment.getString(R.string.only_format, "JPEG"));
            return;
        }

        textView.setText(R.string.downloading);
        final long start = System.currentTimeMillis();
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                return download(item, DOWNLOAD_TYPE.DATA);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                long end = System.currentTimeMillis();

                if (result == null) {
                    textView.setText(mFragment.getString(R.string.failed, "show image"));

                    return;
                }

                imageView.setImageBitmap(result);
                textView.setText(item.getName() + " (time = " + (end - start) + " [ms])");
            }

        }.execute();

    }

    private void initPopupWindow(LinearLayout popLayout){

        final PopupWindow popupWin = new PopupWindow(mFragment.getView(), MATCH_PARENT, WRAP_CONTENT, true);
        popupWin.setContentView(popLayout);
        popupWin.setOutsideTouchable(true);
        popupWin.setFocusable(true);
        popupWin.showAtLocation(mFragment.getView(), Gravity.CENTER, 0, 0);

        Button closeButton = (Button) popLayout.findViewById(R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWin.dismiss();
            }
        });
    }

    private void setAdapterWithAvailableSettings(Spinner spinner, Object setting){

        final ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(mFragment.getActivity(), android.R.layout.simple_spinner_item);
        if (setting instanceof CameraDeviceSetting == true) {
            adapter.addAll(((CameraDeviceSetting)setting).getAvailableSettings());
        }
        if (setting instanceof CaptureSetting == true) {
            adapter.addAll(((CaptureSetting)setting).getAvailableSettings());
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private Bitmap download(CameraImage target, DOWNLOAD_TYPE type){

        OutputStream out = null;
        File outputFile = null;
        try {
            File outputDir = mFragment.getActivity().getCacheDir();
            outputFile = File.createTempFile("prefix", "extension", outputDir);
            out = new FileOutputStream(outputFile);

        } catch (Exception e) {
            Log.e(TAG, "Failed to create temp file", e);
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    //do nothing
                }
            }
            return null;
        }

        Response res = null;
        try {
            if(type == DOWNLOAD_TYPE.DATA) {
                res = target.getData(out);
            } else if(type == DOWNLOAD_TYPE.THUMB) {
                res = target.getThumbnail(out);
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to download", e);
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
        }

        if (res.getResult() == Result.ERROR) {
            Log.e(TAG, "Failed to download (" + res.getErrors().get(0).getCode().toString() + ")");
            return null;
        }

        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(new FileInputStream(outputFile));
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Failed to write stream", e);
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
        }

        return bmp;
    }

}
