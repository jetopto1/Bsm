package com.jetopto.bsm.custom.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jetopto.bsm.R;

public class SensorLevelView extends LinearLayout {

    private static final String TAG = SensorLevelView.class.getSimpleName();

//    private ImageView mEmergentImageView;
//    private ImageView mUrgentImageView;
//    private ImageView mSafetyImageView;
    private ImageView mStatusImageView;

    public SensorLevelView(Context context) {
        super(context);
    }

    public SensorLevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.stick_sensor_status_view, this, true);
        mStatusImageView = findViewById(R.id.stick_status_view);
//        mEmergentImageView = findViewById(R.id.emergent);
//        mUrgentImageView = findViewById(R.id.urgent);
//        mSafetyImageView = findViewById(R.id.safety);
    }

    public void updateSensorLevel(String level) {
        //TODO change imageview state.
        Log.d(TAG, "Level " + level);
        switch (level) {
            case "safety":
//                mSafetyImageView.setEnabled(true);
//                mUrgentImageView.setEnabled(false);
//                mEmergentImageView.setEnabled(false);
                mStatusImageView.setEnabled(true);
                break;
            case "urgent":
//                mSafetyImageView.setEnabled(true);
//                mUrgentImageView.setEnabled(true);
//                mEmergentImageView.setEnabled(false);
                break;
            case "emergent":
                mStatusImageView.setEnabled(false);
                break;
        }
    }

}
