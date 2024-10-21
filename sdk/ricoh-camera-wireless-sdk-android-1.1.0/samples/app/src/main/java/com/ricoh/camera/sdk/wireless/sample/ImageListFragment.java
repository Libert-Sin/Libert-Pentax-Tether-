// Copyright (c) 2017 Ricoh Co., Ltd. All Rights Reserved.
package com.ricoh.camera.sdk.wireless.sample;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ricoh.camera.sdk.wireless.api.CameraImage;
import com.ricoh.camera.sdk.wireless.api.response.Response;
import com.ricoh.camera.sdk.wireless.api.response.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ImageListFragment extends Fragment implements Nameable {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnImageListFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageListRecyclerViewAdapter mImageListRecyclerViewAdapter;

    private Globals mGlobals;
    private List<CameraImage> mImages;

    private Timer mTimer = null;
    private static final int INTERVAL_MS = 500;

    public ImageListFragment() {
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

        mSwipeRefreshLayout =
            (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_images, container, false);
        final Context context = mSwipeRefreshLayout.getContext();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (mGlobals.canUseCamera() == false) {
                    Toast.makeText(context, getString(R.string.not_find), Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }

                new AsyncTask<Void, Void, Response>() {
                    @Override
                    protected Response doInBackground(Void... params) {
                        return mGlobals.mCameraDevice.updateImages();
                    }

                    @Override
                    protected void onPostExecute(Response result) {
                        if (result.getResult() == Result.ERROR) {
                            Toast.makeText(context, getString(R.string.failed, "update images"), Toast.LENGTH_LONG).show();
                            mSwipeRefreshLayout.setRefreshing(false);
                            return;
                        }

                        ImageListFragment.this.updateList();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }.execute();

            }
        });

        RecyclerView recyclerView = (RecyclerView) mSwipeRefreshLayout.findViewById(R.id.fragment_images);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        mImages = new ArrayList<CameraImage>();
        mImageListRecyclerViewAdapter = new ImageListRecyclerViewAdapter(mImages, mListener);
        recyclerView.setAdapter(mImageListRecyclerViewAdapter);

        return mSwipeRefreshLayout;
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

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.destroyDrawingCache();
            mSwipeRefreshLayout.clearAnimation();
        }

        mTimer.cancel();
        mTimer = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImageListFragmentInteractionListener) {
            mListener = (OnImageListFragmentInteractionListener) context;
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
        return "Image List";
    }

    private void updateList(){
        if (mGlobals.mCameraDevice == null) {
            return;
        }

        mImages.clear();
        mImages.addAll(mGlobals.mCameraDevice.getImages());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mImageListRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public interface OnImageListFragmentInteractionListener {

        enum OPTION{
            SHOW_DATA, SHOW_INFO
        }

        void onImageListFragmentInteraction(CameraImage item, OPTION option);
    }

    class UpdateTimerTask extends TimerTask{
        @Override
        public void run() {
            ImageListFragment.this.updateList();
        }
    }

}
