package com.terryyessfung.whatsins.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.terryyessfung.whatsins.Activities.PostsDetailActivity;
import com.terryyessfung.whatsins.Adapters.SquareRecyclerAdapter;
import com.terryyessfung.whatsins.DataManager;
import com.terryyessfung.whatsins.Helper;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.Model.PostsList;
import com.terryyessfung.whatsins.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SquareFragment extends Fragment {
    private RecyclerView mstaggeredRv;
    private SquareRecyclerAdapter madapter;
    private StaggeredGridLayoutManager mmanager;

    private SensorManager mSensorManager;
    private Vibrator mVibrator;
    private final int mShakeValue = 15;

    private SoundPool mSoundPool = null;
    private AudioManager maudioManager;
    private HashMap<Integer,Integer> soundPoolMap;

    private List<Post> mPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_square,container,false);

        // set sensor
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        // init sound


        maudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        mstaggeredRv = view.findViewById(R.id.square_recycView);
        mmanager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mstaggeredRv.setLayoutManager(mmanager);
        mPosts = new ArrayList<>();
        madapter = new SquareRecyclerAdapter(getContext(),mPosts);
        mstaggeredRv.setAdapter(madapter);

        fetchImage();

        return view;
    }

    private void fetchImage() {
        Call<PostsList> call = DataManager.getInstance().getAPIService().getAllPosts();
        call.enqueue(new Callback<PostsList>() {
            @Override
            public void onResponse(Call<PostsList> call, Response<PostsList> response) {
                if(response.isSuccessful()){
                    for(Post post: response.body().getPosts()){
                        mPosts.add(post);
                    }
                    madapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PostsList> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSensorManager != null) {
            mSensorManager.registerListener(
                    mSensorEventListener,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(mSensorEventListener);
        }
    }

    /**
     * Shake one Shake function
     **/
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (Helper.getInstance().isShaking(event.values, mShakeValue)) {
                //mVibrator.vibrate(200);
                mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
                mSoundPool.load(getActivity().getApplicationContext(), R.raw.shake, 1);
                mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        if (mPosts.size() > 0) {
                            mSoundPool.play(R.raw.shake, 1, 1, 0, 0, 1);
                            int r = new Random().nextInt(mPosts.size());
                            Post post = mPosts.get(r);
                            Intent intent = new Intent(getContext(), PostsDetailActivity.class);
                            intent.putExtra(PostsDetailActivity.POST_ID, post.get_id());
                            intent.putExtra(PostsDetailActivity.PUBLISHER_ID, post.getPublisher());
                            startActivity(intent);
                        }
                    }
                });
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
