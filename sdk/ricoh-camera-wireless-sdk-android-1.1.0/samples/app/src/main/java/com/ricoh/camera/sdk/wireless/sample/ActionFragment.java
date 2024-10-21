// Copyright (c) 2017 Ricoh Co., Ltd. All Rights Reserved.
package com.ricoh.camera.sdk.wireless.sample;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricoh.camera.sdk.wireless.api.CameraDevice;
import com.ricoh.camera.sdk.wireless.api.CameraEventListener;
import com.ricoh.camera.sdk.wireless.api.response.Response;
import com.ricoh.camera.sdk.wireless.api.response.Result;
import com.ricoh.camera.sdk.wireless.api.response.StartCaptureResponse;

import java.util.Arrays;


public class ActionFragment extends Fragment implements Nameable {

    private OnActionFragmentInteractionListener mListener;

    private Globals mGlobals;
    private LiveViewEventListener mLiveViewEventListener = new LiveViewEventListener();

    private ImageView mLiveViewImageView;
    private TextView mLiveViewStatusTextView;
    private TextView mActionStatusTextView;

    public ActionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_action, container, false);

        mLiveViewImageView = (ImageView) view.findViewById(R.id.image_view_liveview);
        mLiveViewStatusTextView = (TextView) view.findViewById(R.id.text_view_liveview_state);
        mActionStatusTextView = (TextView) view.findViewById(R.id.text_view_action_state);

        mGlobals = (Globals) getActivity().getApplication();
        if (mGlobals.mCameraDevice != null) {
            mGlobals.mCameraDevice.addEventListener(mLiveViewEventListener);
        }

        view.findViewById(R.id.button_liveview_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLiveView();
            }
        });

        view.findViewById(R.id.button_liveview_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLiveView();
            }
        });

        view.findViewById(R.id.button_shoot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCapture(false);
            }
        });

        view.findViewById(R.id.button_shoot_with_focus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCapture(true);
            }
        });

        view.findViewById(R.id.button_shoot_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCapture();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActionFragmentInteractionListener) {
            mListener = (OnActionFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnActionFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if (mGlobals.mCameraDevice != null) {
            mGlobals.mCameraDevice.removeEventListener(mLiveViewEventListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void startLiveView() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String actionName = getString(R.string.start_liveview);

                if (mGlobals.canUseCamera() == false) {
                    return getString(R.string.failed, actionName);
                }

                if (Arrays.asList(mGlobals.mCameraDevice.getEventListeners())
                    .contains(mLiveViewEventListener) == false) {
                    mGlobals.mCameraDevice.addEventListener(mLiveViewEventListener);
                }

                Response res = mGlobals.mCameraDevice.startLiveView();

                if (res.getResult() == Result.OK) {
                    return getString(R.string.succeed, actionName);
                } else {
                    return getString(R.string.failed, actionName);
                }
            }

            @Override
            protected void onPostExecute(String result) {
                mActionStatusTextView.setText(result);
            }
        }.execute();
    }

    private void stopLiveView() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String actionName = getString(R.string.stop_liveview);

                if (mGlobals.canUseCamera() == false) {
                    return getString(R.string.failed, actionName);
                }

                Response res = mGlobals.mCameraDevice.stopLiveView();
                if (res.getResult() == Result.OK) {
                    return getString(R.string.succeed, actionName);
                } else {
                    return getString(R.string.failed, actionName);
                }
            }

            @Override
            protected void onPostExecute(String result) {
                mActionStatusTextView.setText(result);
            }
        }.execute();
    }

    private void startCapture(final boolean withFocus) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String actionName = null;

                if (withFocus == false) {
                    actionName = getString(R.string.start_shoot);
                } else {
                    actionName = getString(R.string.start_shoot_with_focus);
                }

                if (mGlobals.canUseCamera() == false) {
                    return getString(R.string.failed, actionName);
                }

                StartCaptureResponse res = null;
                try {
                    res = mGlobals.mCameraDevice.startCapture(withFocus);
                } catch (UnsupportedOperationException e) {
                    return getString(R.string.unsupport, actionName +  " (focus =" + withFocus + ")");
                }

                if (res.getResult() == Result.OK) {
                    return getString(R.string.succeed, actionName + " (" + res.getCapture().getMethod() + ")");
                } else {
                    return getString(R.string.failed, actionName + " (" + res.getErrors().get(0).getCode() + ")");
                }
            }

            @Override
            protected void onPostExecute(String result) {
                mActionStatusTextView.setText(result);
            }
        }.execute();
    }

    private void stopCapture() {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String actionName = getString(R.string.stop_shoot);

                if (mGlobals.canUseCamera() == false) {
                    return getString(R.string.failed, actionName);
                }

                Response res = null;
                try {
                    res = mGlobals.mCameraDevice.stopCapture();
                } catch (UnsupportedOperationException e){
                    return getString(R.string.unsupport, actionName);
                }

                if (res.getResult() == Result.OK) {
                    return getString(R.string.succeed, actionName);
                } else {
                    return getString(R.string.failed, actionName);
                }

            }

            @Override
            protected void onPostExecute(String result) {
                mActionStatusTextView.setText(result);
            }
        }.execute();
    }

    @Override
    public String getName() {
        return "Action";
    }

    public interface OnActionFragmentInteractionListener {
        void showLiveView(Bitmap receivedImage, ImageView showView, TextView statusView);
    }

    public class LiveViewEventListener extends CameraEventListener {

        @Override
        public void liveViewFrameUpdated(CameraDevice sender, byte[] liveViewFrame) {

            if (mListener != null) {
                final Bitmap bitmap =
                    BitmapFactory.decodeByteArray(liveViewFrame, 0, liveViewFrame.length);
                mListener.showLiveView(bitmap, mLiveViewImageView, mLiveViewStatusTextView);
            }

        }
    }

}
