package com.spark.demo.thirdparty.shake;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.spark.demo.thirdparty.R;
import com.spark.demo.thirdparty.zxing.KJAnimations;


/**
 * 摇一摇界面
 * @author spark
 */
public  class ShakeShowActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager = null;
    private Sensor sensor;
    private Vibrator vibrator = null;
    private boolean isRequest = false;
    private float lastX;
    private float lastY;
    private float lastZ;
    private long lastUpdateTime;
    private static final int SPEED_SHRESHOLD = 25;// 这个值越大需要越大的力气来摇晃手机
    private static final int UPTATE_INTERVAL_TIME = 50;

    private ImageView mImgShake;
    private ProgressBar mProgress;
    private RelativeLayout mLayoutBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shake);
        setTitle(getString(R.string.text_title_shake));
        mImgShake = (ImageView) findViewById(R.id.shake_img_y);
        mProgress = (ProgressBar) findViewById(R.id.progress_shaky);
        mLayoutBottom = (RelativeLayout) findViewById(R.id.rl_show);
        initData();
    }


    /**
     * 上移
     */
    private void animUp() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mImgShake, "translationY", -300);
        animator.setDuration(200);
        animator.start();
        mLayoutBottom.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    /**
     * 复位
     */
    private void animReset() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mImgShake, "translationY", 0);
        animator.setDuration(200);
        animator.start();
    }

    /**
     * 摇动手机成功后调用
     */
    private void onShake() {
//        isRequest = true;
        mProgress.setVisibility(View.VISIBLE);
        final Animation anim = KJAnimations.shakeAnimation(mImgShake.getLeft());
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                animReset();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 摇一摇完成后 要做的操作  【如：网络请求】
                shakeFinished();
            }
        });

        mImgShake.startAnimation(anim);
    }

    /**
     * 摇一摇结束
     */
    public void shakeFinished (){
        // TODO: 摇一摇结束后要做的操作
        Toast.makeText(ShakeShowActivity.this, "摇一摇结束，现在获取数据...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            if (sensor != null) {
                sensorManager.registerListener(this, sensor,
                        SensorManager.SENSOR_DELAY_GAME);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //  Log.i("spark--", "onSensorChanged");
        long currentUpdateTime = System.currentTimeMillis();
        long timeInterval = currentUpdateTime - lastUpdateTime;
        if (timeInterval < UPTATE_INTERVAL_TIME) {
            return;
        }
        lastUpdateTime = currentUpdateTime;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        lastX = x;
        lastY = y;
        lastZ = z;

        double speed = (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                * deltaZ) / timeInterval) * 80;
        if (speed >= SPEED_SHRESHOLD && !isRequest) {
            mLayoutBottom.setVisibility(View.GONE);
            vibrator.vibrate(300);
            onShake();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void initData() {
        sensorManager = (SensorManager) this
                .getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
    }

}
