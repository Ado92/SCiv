package com.hranicky.iv.sensorcollector;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    TextView tv1=null;
    TextView tv2=null;

    private SensorManager mSensorManager;
    private List<Sensor> mList;
    //Senzory:
    private Sensor mACCELEROMETER;
    private Sensor mAMBIENT_TEMPERATURE;
    private Sensor mDEVICE_PRIVATE_BASE;
    private Sensor mGAME_ROTATION_VECTOR;
    private Sensor mGEOMAGNETIC_ROTATION_VECTOR;

    private Sensor mGRAVITY;
    private Sensor mGYROSCOPE;
    private Sensor mGYROSCOPE_UNCALIBRATED;
    private Sensor mHEART_BEAT;
    private Sensor mHEART_RATE;

    private Sensor mLIGHT;
    private Sensor mLINEAR_ACCELERATION;
    private Sensor mMAGNETIC_FIELD;
    private Sensor mMAGNETIC_FIELD_UNCALIBRATED;
    private Sensor mMOTION_DETECT;

    private Sensor mPOSE_6DOF;
    private Sensor mPRESSURE;
    private Sensor mPROXIMITY;
    private Sensor mRELATIVE_HUMIDITY;
    private Sensor mROTATION_VECTOR;

    private Sensor mSIGNIFICANT_MOTION;
    private Sensor mSTATIONARY_DETECT;
    private Sensor mSTEP_COUNTER;
    private Sensor mSTEP_DETECTOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv2 = (TextView) findViewById(R.id.textView3);
        tv2.setVisibility(View.GONE);

        tv1 = (TextView) findViewById(R.id.textView2);
        tv1.setVisibility(View.GONE);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
/*
        mList= mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 1; i < mList.size(); i++) {
            tv1.setVisibility(View.VISIBLE);
            tv1.append("\n" + mList.get(i).getName() + "\n" + mList.get(i).getVendor() + "\n" + mList.get(i).getVersion() + "\n\n");
        }
*/

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            // Success! There's a pressure sensor.
            mACCELEROMETER = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        else {
            // Failure! No pressure sensor.
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            // Success! There's a pressure sensor.
            mAMBIENT_TEMPERATURE = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_DEVICE_PRIVATE_BASE) != null){
            // Success! There's a pressure sensor.
            mDEVICE_PRIVATE_BASE = mSensorManager.getDefaultSensor(Sensor.TYPE_DEVICE_PRIVATE_BASE);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) != null){
            // Success! There's a pressure sensor.
            mGAME_ROTATION_VECTOR = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) != null){
            // Success! There's a pressure sensor.
            mGEOMAGNETIC_ROTATION_VECTOR = mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null){
            // Success! There's a pressure sensor.
            mGRAVITY = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){
            // Success! There's a pressure sensor.
            mGYROSCOPE = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED) != null){
            // Success! There's a pressure sensor.
            mGYROSCOPE_UNCALIBRATED = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT) != null){
            // Success! There's a pressure sensor.
            mHEART_BEAT = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null){
            // Success! There's a pressure sensor.
            mHEART_RATE = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            // Success! There's a pressure sensor.
            mLIGHT = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null){
            // Success! There's a pressure sensor.
            mLINEAR_ACCELERATION = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            // Success! There's a pressure sensor.
            mMAGNETIC_FIELD = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) != null){
            // Success! There's a pressure sensor.
            mMAGNETIC_FIELD_UNCALIBRATED = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT) != null){
            // Success! There's a pressure sensor.
            mMOTION_DETECT = mSensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF) != null){
            // Success! There's a pressure sensor.
            mPOSE_6DOF = mSensorManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null){
            // Success! There's a pressure sensor.
            mPRESSURE = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){
            // Success! There's a pressure sensor.
            mPROXIMITY = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null){
            // Success! There's a pressure sensor.
            mRELATIVE_HUMIDITY = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null){
            // Success! There's a pressure sensor.
            mROTATION_VECTOR = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION) != null){
            // Success! There's a pressure sensor.
            mSIGNIFICANT_MOTION = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT) != null){
            // Success! There's a pressure sensor.
            mSTATIONARY_DETECT = mSensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            // Success! There's a pressure sensor.
            mSTEP_COUNTER = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            // Success! There's a pressure sensor.
            mSTEP_DETECTOR = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when there is a new sensor event.  Note that "on changed"
     * is somewhat of a misnomer, as this will also be called if we have a
     * new reading from a sensor with the exact same sensor values (but a
     * newer timestamp).
     * <p/>
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p/>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
  /*
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        float lux = event.values[0];
        // Do something with this sensor value.
        tv2.setVisibility(View.VISIBLE);
        tv2.append("\n" + lux + "\n\n");
*/
    }

    /**
     * Called when the accuracy of the registered sensor has changed.  Unlike
     * onSensorChanged(), this is only called when this accuracy value changes.
     * <p/>
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    protected void onResume() {
        super.onResume();

/*
        for (int i = 1; i < mList.size(); i++) {
            mSensorManager.registerListener(this, mList.get(i), SensorManager.SENSOR_DELAY_NORMAL);
        }
 */

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
