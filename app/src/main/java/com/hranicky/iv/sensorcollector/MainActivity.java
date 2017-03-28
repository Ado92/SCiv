package com.hranicky.iv.sensorcollector;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.util.EntityUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.LogRecord;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//import it.sauronsoftware.ftp4j.FTPClient;
//import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {


    TextView tv1 = null;
    TextView tv2 = null;

    private volatile SensorManager mSensorManager;
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

    private Sensor mORIENTATION;
    private Sensor mPOSE_6DOF;
    private Sensor mPRESSURE;
    private Sensor mPROXIMITY;
    private Sensor mRELATIVE_HUMIDITY;

    private Sensor mROTATION_VECTOR;
    private Sensor mSIGNIFICANT_MOTION;
    private Sensor mSTATIONARY_DETECT;
    private Sensor mSTEP_COUNTER;
    private Sensor mSTEP_DETECTOR;

    private volatile LocationManager mGPS;
    private volatile String GPSData = "null;null;null;null;null";

    //TextView:
    private volatile TextView m1a = null, m2a = null, m3a = null, m4a = null, m5a = null, m6a = null, m7a = null, m8a = null, m9a = null, m10a = null, m11a = null, m12a = null, m13a = null, m13aCompass = null, m14a = null, m15a = null, m15aa = null, m16a = null, m17a = null, m18a = null, m19a = null, m20a = null, m21a = null, m22a = null, m23a = null, m24a = null, m25a = null;
    private volatile TextView m1b = null, m2b = null, m3b = null, m4b = null, m5b = null, m6b = null, m7b = null, m8b = null, m9b = null, m10b = null, m11b = null, m12b = null, m13b = null, m13bCompass = null, m14b = null, m15b = null, m15bb = null, m16b = null, m17b = null, m18b = null, m19b = null, m20b = null, m21b = null, m22b = null, m23b = null, m24b = null, m25b = null;
    private volatile ImageView m13cCompass = null;
    private volatile LinearLayout m13dCompass = null;
    private float[] mCompass = new float[9];
    private float[] mCompassOrientation = new float[3];
    private float mCompassCurrentDegree = 0f;
    private float[] mCompassAccelerometer = new float[3];
    private float[] mCompassMagnetometer = new float[3];
    //Temperature
    private float temperature = (float) -500.0;

    private boolean phase1 = true;
    private boolean phase2 = true;
    private boolean phase3 = true;

    private int actualCount = 0;
    private int maxCount = 10;

    //Start Button
    private Button start;

    //CheckBoxes
    private CheckBox ph1;
    private CheckBox ph2;
    private CheckBox ph3;

    //EditText
    private EditText et;

    //SQLite Database
    private volatile SQLiteDatabase sc;

    //Thread for sensors
    private volatile boolean running = true;
    private AsyncTask sensorsRunning;

    private LinearLayout testingMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.textView2);
        tv1.setVisibility(View.GONE);

        tv2 = (TextView) findViewById(R.id.textView3);
        tv2.setVisibility(View.GONE);

        testingMenu = (LinearLayout) findViewById(R.id.testingMenu);
/*
        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
*/
        initializeTextViews();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        initializeSensors();

        tv2.setVisibility(View.VISIBLE);
        tv2.setText("Senzorov:" + mList.size());
        for (int i = 0; i < mList.size(); i++) {
            tv2.append("\n" + mList.get(i).getName());
        }

        ph1 = (CheckBox) findViewById(R.id.phase1);
        ph2 = (CheckBox) findViewById(R.id.phase2);
        ph3 = (CheckBox) findViewById(R.id.phase3);

        et = (EditText) findViewById(R.id.pocetVzoriek);

        if (savedInstanceState != null) {
            maxCount = savedInstanceState.getInt("maxCount");
            actualCount = savedInstanceState.getInt("actualCount");
            phase1 = savedInstanceState.getBoolean("phase1");
            phase2 = savedInstanceState.getBoolean("phase2");
            phase3 = savedInstanceState.getBoolean("phase3");

            et.setText(maxCount + "");
            ph1.setChecked(phase1);
            ph2.setChecked(phase2);
            ph3.setChecked(phase3);
        }


        sc = openOrCreateDatabase("SenCol", Context.MODE_APPEND, null);

        start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(this);


    }

    class SensorsDataTask extends AsyncTask <Object, Void, Void> {
        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Object... params) {
            startPhases();
            return null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putBoolean("MyBoolean", true);
        savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putString("MyString", "Welcome back to Android");
        // etc.

        savedInstanceState.putInt("maxCount", maxCount);
        savedInstanceState.putInt("actualCount", actualCount);
        savedInstanceState.putBoolean("phase1", phase1);
        savedInstanceState.putBoolean("phase2", phase2);
        savedInstanceState.putBoolean("phase3", phase3);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
        double myDouble = savedInstanceState.getDouble("myDouble");
        int myInt = savedInstanceState.getInt("MyInt");
        String myString = savedInstanceState.getString("MyString");


        maxCount = savedInstanceState.getInt("maxCount");
        actualCount = savedInstanceState.getInt("actualCount");
        phase1 = savedInstanceState.getBoolean("phase1");
        phase2 = savedInstanceState.getBoolean("phase2");
        phase3 = savedInstanceState.getBoolean("phase3");

        //  et.setText(maxCount + "");
        ph1.setChecked(phase1);
        ph2.setChecked(phase2);
        ph3.setChecked(phase3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private boolean testingMenuSet = true;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (testingMenuSet) {
            testingMenu.setVisibility(View.GONE);
            testingMenuSet = false;
        }
        else {
            testingMenu.setVisibility(View.VISIBLE);
            testingMenuSet = true;
        }

        return super.onOptionsItemSelected(item);
    }


    float[] gravity = new float[3];
    float[] linear_acceleration = new float[3];
    volatile float[] allSC = new float[78];

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
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == event.sensor) {
            //Kalmanov Filter
            xKF = kalmanFilterUpdateDimensionData( event.values[0],1);
            yKF = kalmanFilterUpdateDimensionData( event.values[1],2);
            zKF = kalmanFilterUpdateDimensionData( event.values[2],3);

            mCompassAccelerometer[0] = xKF;
            mCompassAccelerometer[1] = yKF;
            mCompassAccelerometer[2] = zKF;

            m1b.setText("Hodnoty s použitím Kalmanovho filtra (m/s^2):\n");

            /*
            When the device lies flat on a table and is pushed on its left side toward the right, the x acceleration value is positive.
            When the device lies flat on a table, the acceleration value is +9.81, which correspond to the acceleration of the device (0 m/s^2) minus the force of gravity (-9.81 m/s^2).
            When the device lies flat on a table and is pushed toward the sky with an acceleration of A m/s^2, the acceleration value is equal to A+9.81 which correspond to the acceleration of the device (+A m/s^2) minus the force of gravity (-9.81 m/s^2).
            */
            // alpha is calculated as t / (t + dT)
            // with t, the low-pass filter's time-constant
            // and dT, the event delivery rate
            float alpha = (float) 0.8;

            /*
                float[] gravity = new float[3];
                float[] linear_acceleration = new float[3];
             */

            gravity[0] = alpha * gravity[0] + (1 - alpha) * xKF;
            gravity[1] = alpha * gravity[1] + (1 - alpha) * yKF;
            gravity[2] = alpha * gravity[2] + (1 - alpha) * zKF;

            linear_acceleration[0] = xKF - gravity[0];
            linear_acceleration[1] = yKF - gravity[1];
            linear_acceleration[2] = zKF - gravity[2];

            //Write values
            writeValues("x", m1b, linear_acceleration[0]);
            writeValues("y", m1b, linear_acceleration[1]);
            writeValues("z", m1b, linear_acceleration[2]);


            m1b.append("Dáta upravené kalmanovým filtrom (m/s^2):\n");
            writeValues("x", m1b, (float) xKF);
            writeValues("y", m1b, (float) yKF);
            writeValues("z", m1b, (float) zKF);

            m1b.append("Neupravené dáta zo senzoru (m/s^2):\n");
            writeValues("x", m1b, event.values[0]);
            writeValues("y", m1b, event.values[1]);
            writeValues("z", m1b, event.values[2]);


            allSC[0] = linear_acceleration[0];
            allSC[1] = linear_acceleration[1];
            allSC[2] = linear_acceleration[2];

            allSC[3] = event.values[0];
            allSC[4] = event.values[1];
            allSC[5] = event.values[2];
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) == event.sensor) {
            m2b.setText("Hodnota (°C):\n");
            writeValues("Teplota okolia:", m2b, event.values[0]);
            temperature = event.values[0];
            allSC[6] = event.values[0];

        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_DEVICE_PRIVATE_BASE) == event.sensor) {
            m3b.setText("Hodnota:\n");
            writeValues("Device Private Base", m3b, event.values[0]);
            allSC[7] = event.values[0];

        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) == event.sensor) {
            m4b.setText("Hodnoty (bez ohľadu na geomagnetické pole Zeme):\n");
            String value = "";
            for (int i = 0; i < event.values.length; i++) {
                if (i == 0) {
                    value = "x*sin(θ/2)";
                    allSC[8] = event.values[0];
                } else if (i == 1) {
                    value = "y*sin(θ/2)";
                    allSC[9] = event.values[1];
                } else if (i == 2) {
                    value = "z*sin(θ/2)";
                    allSC[10] = event.values[2];
                } else if (i == 3) {
                    value = "cos(θ/2)";
                    allSC[11] = event.values[3];
                } else if (i == 4) {
                    value = "Odhadovaná presnosť (rad, -1 ak nie je k disp.)";
                    allSC[12] = event.values[4];
                }
                writeValues(value, m4b, event.values[i]);
            }
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) == event.sensor) {
            m5b.setText("Hodnoty (bez použitia gyroskopu):\n");
            String value = "";
            for (int i = 0; i < event.values.length; i++) {
                if (i == 0) {
                    value = "x*sin(θ/2)";
                    allSC[13] = event.values[0];
                } else if (i == 1) {
                    value = "y*sin(θ/2)";
                    allSC[14] = event.values[1];
                } else if (i == 2) {
                    value = "z*sin(θ/2)";
                    allSC[15] = event.values[2];
                } else if (i == 3) {
                    value = "cos(θ/2)";
                    allSC[16] = event.values[3];
                } else if (i == 4) {
                    value = "Odhadovaná presnosť (rad, -1 ak nie je k disp.)";
                    allSC[17] = event.values[4];
                }
                writeValues(value, m5b, event.values[i]);
            }
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) == event.sensor) {
            m6b.setText("Hodnoty (m/s^2):\n");
            /*
            The gravity vector components are reported in m/s^2 in the x, y and z fields of sensors_event_t.acceleration.
            When the device is at rest, the output of the gravity sensor should be identical to that of the accelerometer. On Earth, the magnitude is around 9.8 m/s^2.
             */

            writeValues("x", m6b, event.values[0]);
            writeValues("y", m6b, event.values[1]);
            writeValues("z", m6b, event.values[2]);

            allSC[18] = event.values[0];
            allSC[19] = event.values[1];
            allSC[20] = event.values[2];

        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == event.sensor) {
            m7b.setText("Hodnoty rýchlostí otáčania okolo os (rad/s):\n");
            writeValues("x", m7b, event.values[0]);
            writeValues("y", m7b, event.values[1]);
            writeValues("z", m7b, event.values[2]);

            allSC[21] = event.values[0];
            allSC[22] = event.values[1];
            allSC[23] = event.values[2];

        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED) == event.sensor) {
            m8b.setText("Hodnoty rýchlostí otáčania okolo os bez driftovej kompenzácie (rad/s):\n");
            writeValues("x", m8b, event.values[0]);
            writeValues("y", m8b, event.values[1]);
            writeValues("z", m8b, event.values[2]);
            m8b.append("Odhadovaný drift okolo os (rad/s):\n");
            writeValues("x", m8b, event.values[3]);
            writeValues("y", m8b, event.values[4]);
            writeValues("z", m8b, event.values[5]);

            allSC[24] = event.values[0];
            allSC[25] = event.values[1];
            allSC[26] = event.values[2];
            allSC[27] = event.values[3];
            allSC[28] = event.values[4];
            allSC[29] = event.values[5];
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT) == event.sensor) {
            m9b.setText("Hodnota (0.0 - pri veľkej nepresnosti, 1.0 pri veľkej presnosti):\n");
            writeValues("Zaznamenanie úderu srdca", m9b, event.values[0]);
            allSC[30] = event.values[0];
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) == event.sensor) {
            m10b.setText("Hodnota (bpm - počet úderov za min.):\n");
            writeValues("Rýchlosť úderov srdca", m10b, event.values[0]);
            allSC[31] = event.values[0];
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == event.sensor) {
            m11b.setText("Hodnota (lux):\n");
            writeValues("Okolité osvetlenie", m11b, event.values[0]);
            allSC[32] = event.values[0];
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) == event.sensor) {
            m12b.setText("Hodnoty akceleračnej rýchlosti okolo os bez gravitácie (m/s^2):\n");
            writeValues("x", m12b, event.values[0]);
            writeValues("y", m12b, event.values[1]);
            writeValues("z", m12b, event.values[2]);

            allSC[33] = event.values[0];
            allSC[34] = event.values[1];
            allSC[35] = event.values[2];

        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == event.sensor) {
            m13b.setText("Hodnoty okolitého magnetického poľa v osiach (uT):\n");
            writeValues("x", m13b, event.values[0]);
            writeValues("y", m13b, event.values[1]);
            writeValues("z", m13b, event.values[2]);

            allSC[36] = event.values[0];
            allSC[37] = event.values[1];
            allSC[38] = event.values[2];

            xmKF = kalmanFilterUpdateDimensionData( event.values[0],4);
            ymKF = kalmanFilterUpdateDimensionData( event.values[1],5);
            zmKF = kalmanFilterUpdateDimensionData( event.values[2],6);

            m13b.append("Hodnoty okolitého magnetického poľa upravené Kalmanovým filtrom (uT):\n");
            writeValues("x", m13b, xmKF);
            writeValues("y", m13b, ymKF);
            writeValues("z", m13b, zmKF);

            //Data Fusion: Compass
            if(kalmanSet3) {

                mCompassMagnetometer[0] = xmKF;
                mCompassMagnetometer[1] = ymKF;
                mCompassMagnetometer[2] = zmKF;

                SensorManager.getRotationMatrix(mCompass, null, mCompassAccelerometer, mCompassMagnetometer);
                SensorManager.getOrientation(mCompass, mCompassOrientation);
                float azimuthInRadians = mCompassOrientation[0];
                float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
                RotateAnimation compassPointer = new RotateAnimation(
                        mCompassCurrentDegree,
                        -azimuthInDegress,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

                compassPointer.setDuration(250);

                compassPointer.setFillAfter(true);

                m13cCompass.startAnimation(compassPointer);
                mCompassCurrentDegree = -azimuthInDegress;
            }

        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) == event.sensor) {
            m14b.setText("Hodnoty okolitého magnetického poľa v osiach bez kalibrácie ťažkými kovmi (uT):\n");
            writeValues("x", m14b, event.values[0]);
            writeValues("y", m14b, event.values[1]);
            writeValues("z", m14b, event.values[2]);
            m14b.append("Ovplyvnenie os ťažkými kovmi (uT):\n");
            writeValues("x", m14b, event.values[3]);
            writeValues("y", m14b, event.values[4]);
            writeValues("z", m14b, event.values[5]);

            allSC[39] = event.values[0];
            allSC[40] = event.values[1];
            allSC[41] = event.values[2];
            allSC[42] = event.values[3];
            allSC[43] = event.values[4];
            allSC[44] = event.values[5];

        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT) == event.sensor) {
            m15b.setText("Hodnota (N/A, 1.0 pri pohnutí zariadením po dobu aspoň 5s):\n");
            writeValues("Pohyb:", m15b, event.values[0]);
            allSC[45] = event.values[0];
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) == event.sensor) {
            m15bb.setText("Hodnoty (°):\n");
            writeValues("Uhol zdvihu (uhol okolo osi x)", m15bb, event.values[1]);
            writeValues("Bočný náklon (uhol okolo osi y)", m15bb, event.values[2]);
            writeValues("Azimut (uhol okolo osi z)", m15bb, event.values[0]);

            allSC[46] = event.values[1];
            allSC[47] = event.values[2];
            allSC[48] = event.values[0];

        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF) == event.sensor) {
            m16b.setText("Hodnoty:\n");
            writeValues("x*sin(θ/2)", m16b, event.values[0]);
            writeValues("y*sin(θ/2)", m16b, event.values[1]);
            writeValues("z*sin(θ/2)", m16b, event.values[2]);
            writeValues("cos(θ/2)", m16b, event.values[3]);
            m16b.append("Posun po osiach od ľubovoľného počiatku:\n");
            writeValues("x", m16b, event.values[4]);
            writeValues("y", m16b, event.values[5]);
            writeValues("z", m16b, event.values[6]);
            m16b.append("Delta rotácia kvaterniónu:\n");
            writeValues("x*sin(θ/2)", m16b, event.values[7]);
            writeValues("y*sin(θ/2)", m16b, event.values[8]);
            writeValues("z*sin(θ/2)", m16b, event.values[9]);
            writeValues("cos(θ/2)", m16b, event.values[10]);
            m16b.append("Delta posun po osiach:\n");
            writeValues("x", m16b, event.values[11]);
            writeValues("y", m16b, event.values[12]);
            writeValues("z", m16b, event.values[13]);
            writeValues("Sekvenčné číslo", m16b, event.values[14]);

            allSC[49] = event.values[0];
            allSC[50] = event.values[1];
            allSC[51] = event.values[2];
            allSC[52] = event.values[3];
            allSC[53] = event.values[4];

            allSC[54] = event.values[5];
            allSC[55] = event.values[6];
            allSC[56] = event.values[7];
            allSC[57] = event.values[8];
            allSC[58] = event.values[9];

            allSC[59] = event.values[10];
            allSC[60] = event.values[11];
            allSC[61] = event.values[12];
            allSC[62] = event.values[13];
            allSC[63] = event.values[14];

        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) == event.sensor) {
            m17b.setText("Hodnota (hPa):\n");
            writeValues("Atmosferický tlak", m17b, event.values[0]);
            allSC[64] = event.values[0];
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == event.sensor) {
            m18b.setText("Hodnota (cm):\n");
            writeValues("Blízkosť", m18b, event.values[0]);
            allSC[65] = event.values[0];
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) == event.sensor) {
            m19b.setText("Hodnoty (%):\n");
            writeValues("Relatívna vlhkosť okolitého vzduchu", m19b, event.values[0]);
            allSC[66] = event.values[0];
            if (temperature > -499) {
                float dewPoint;
                float h = (float) (Math.log(event.values[0] / 100.0) + (17.62 * temperature) / (243.12 + temperature));
                dewPoint = (float) (243.12 * h / (17.62 - h));
                float absoluteHumidity;
                absoluteHumidity = (float) (216.7 * (event.values[0] / 100.0 * 6.112 * Math.exp(17.62 * temperature / (243.12 + temperature)) / (273.15 + temperature)));
                m19b.append("Teplota potrebná na zkondenzovanie vzduchu (°C):\n");
                writeValues("Rosný bod", m19b, dewPoint);
                m19a.append("Množstvo vodnej pary v určitom množstvo suchého vzduchu (g/m^3)");
                writeValues("Absolútna vlhkosť", m19b, absoluteHumidity);

                allSC[67] = dewPoint;
                allSC[68] = absoluteHumidity;

            }
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == event.sensor) {
            m20b.setText("Hodnoty (vzhľadom na geomagnetické pole Zeme):\n");
            String value = "";
            for (int i = 0; i < event.values.length; i++) {
                if (i == 0) {
                    value = "x*sin(θ/2)";
                    allSC[69] = event.values[0];
                } else if (i == 1) {
                    value = "y*sin(θ/2)";
                    allSC[70] = event.values[1];
                } else if (i == 2) {
                    value = "z*sin(θ/2)";
                    allSC[71] = event.values[2];
                } else if (i == 3) {
                    value = "cos(θ/2)";
                    allSC[72] = event.values[3];
                } else if (i == 4) {
                    value = "Odhadovaná presnosť (rad, -1 ak nie je k disp.)";
                    allSC[73] = event.values[4];
                }
                writeValues(value, m20b, event.values[i]);
            }
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION) == event.sensor) {
            m21b.setText("Bez hodnôt:\n");
            writeValues("(1):", m21b, event.values[0]);
            allSC[74] = event.values[0];

        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT) == event.sensor) {
            m22b.setText("Hodnota (N/A, 1.0 pri nepohnutí zariadením po dobu aspoň 5s):\n");
            writeValues("Bez pohybu:", m22b, event.values[0]);
            allSC[75] = event.values[0];
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) == event.sensor) {
            m23b.setText("Hodnota (kroky):\n");
            writeValues("Počet krokov od posledného rebootu", m23b, event.values[0]);
            allSC[76] = event.values[0];
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) == event.sensor) {
            m24b.setText("Hodnota (N/A, 1.0 pri kroku):\n");
            writeValues("Krok", m24b, event.values[0]);
            allSC[77] = event.values[0];
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
           // return;
        }
        mGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void writeValues(String s, TextView tv, Float f) {
        tv.append(s + ": " + f + "\n");
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

        for (int i = 0; i < mList.size(); i++) {
            mSensorManager.registerListener(this, mList.get(i), SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
        mGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);



    }

    @Override
    protected void onPause() {
        super.onPause();
        //sensorsRunning SensorsDataTask set to STOP
        running = false;
        mSensorManager.unregisterListener(this);
        // Remove the listener you previously added
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
        mGPS.removeUpdates(locationListener);
    }

    //Toast pri vytvarani suboru
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.arg1 == 1) {
                Toast.makeText(getApplicationContext(), "Vytváram JSON.", Toast.LENGTH_LONG).show();
            } else if (msg.arg1 == 2) {
                Toast.makeText(getApplicationContext(), "Vytváram CSV.", Toast.LENGTH_LONG).show();
            } else if (msg.arg1 == 3) {
                Toast.makeText(getApplicationContext(), "Vytváram DUMP.", Toast.LENGTH_LONG).show();
            }
        }
    };

    //Run all checked phases
    public void startPhases() {
        maxCount = Integer.parseInt(((EditText) findViewById(R.id.pocetVzoriek)).getText().toString());

        //Save data to SQLite (if phase is set to true
        if (phase1 == true || phase2 == true || phase3 == true) {
            data2sql(maxCount);
        }

        Message msg;

        //Execute phases
        int faza = 1;
        for (faza = 1; faza <= 3; faza++) {
            if ((faza == 1 && phase1 == true) || (faza == 2 && phase2 == true) || (faza == 3 && phase3 == true)) {

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                Date date = new Date(mYear - 1900, mMonth, mDay);

                Pair<String, String> zaznamyZPrenosu;
                String velkost = "ivanka";
                String cas = "milisekundy";

                final String typ;
                if (faza == 1) {
                    msg = handler.obtainMessage();
                    msg.arg1 = 1;
                    handler.sendMessage(msg);
//                    Toast.makeText(MainActivity.this, "Vytváram JSON.", Toast.LENGTH_SHORT).show();
                    typ = "json";
                    zaznamyZPrenosu = json(maxCount);
                    cas = zaznamyZPrenosu.first;
                    velkost = zaznamyZPrenosu.second;
                } else if (faza == 2) {
                    msg = handler.obtainMessage();
                    msg.arg1 = 2;
                    handler.sendMessage(msg);
//                    Toast.makeText(MainActivity.this, "Vytváram CSV.", Toast.LENGTH_SHORT).show();
                    typ = "csv";
                    zaznamyZPrenosu = csv(maxCount);
                    cas = zaznamyZPrenosu.first;
                    velkost = zaznamyZPrenosu.second;
                } else {
                    msg = handler.obtainMessage();
                    msg.arg1 = 3;
                    handler.sendMessage(msg);
//                    Toast.makeText(MainActivity.this, "Vytváram DUMP.", Toast.LENGTH_SHORT).show();
                    typ = "dump";
                    zaznamyZPrenosu = dump(maxCount);
                    cas = zaznamyZPrenosu.first;
                    velkost = zaznamyZPrenosu.second;
                }
                final String suborov = String.valueOf(maxCount);
                final String datum = DateFormat.format("dd.MM.yyyy", date).toString();


                final String finalVelkost = velkost;
                final String finalCas = cas;
                class AddCollection extends AsyncTask<Void, Void, String> {

                    ProgressDialog loading;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                     //   loading = ProgressDialog.show(MainActivity.this, "Adding...", "Wait...", false, false);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                       // loading.dismiss();
                        Toast.makeText(MainActivity.this, "Pridávam " + (suborov) + "", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected String doInBackground(Void... v) {
                        HashMap<String, String> params = new HashMap<>();
                        params.put(Config.KEY_TYP, typ);
                        params.put(Config.KEY_SUBOROV, suborov);
                        params.put(Config.KEY_VELKOST, finalVelkost);
                        params.put(Config.KEY_CAS, finalCas);
                        params.put(Config.KEY_DATUM, datum);

                        RequestHandler rh = new RequestHandler();
                        String res = rh.sendPostRequest(Config.URL_ADD, params);
                        return res;
                    }
                }

                AddCollection ac = new AddCollection();
                ac.execute();
            }
        }


    }

    public Pair<String, String> json(int max) {
        String casPrenosu = "";
        String velkostSuboru = "";
        long zaciatokPrenosu = 0;
        long koniecPrenosu = 0;
        //Začiatok vytvárania JSONu
        zaciatokPrenosu = System.currentTimeMillis();
        JSONArray jsonArr = new JSONArray();
        try {
            Cursor cursor;
            for (int i = 1; i <= max; i++) {
                cursor = sc.rawQuery("SELECT * FROM 'scData' WHERE id = '" + i + "'", null);
                cursor.moveToFirst();
                JSONObject jsonOb = new JSONObject();
                jsonOb.put("'datum'", cursor.getString(1));
                jsonOb.put("'accelerometer'", cursor.getString(2));
                jsonOb.put("'ambient_temperature'", cursor.getString(3));
                jsonOb.put("'device_private_base'", cursor.getString(4));
                jsonOb.put("'game_rotation_vector'", cursor.getString(5));
                jsonOb.put("'geomagnetic_rotation_vector'", cursor.getString(6));
                jsonOb.put("'gravity'", cursor.getString(7));
                jsonOb.put("'gyroscope'", cursor.getString(8));
                jsonOb.put("'gyroscope_uncalibrated'", cursor.getString(9));
                jsonOb.put("'heart_beat'", cursor.getString(10));
                jsonOb.put("'heart_rate'", cursor.getString(11));
                jsonOb.put("'light'", cursor.getString(12));
                jsonOb.put("'linear_acceleration'", cursor.getString(13));
                jsonOb.put("'magnetic_field'", cursor.getString(14));
                jsonOb.put("'magnetic_field_uncalibrated'", cursor.getString(15));
                jsonOb.put("'motion_detect'", cursor.getString(16));
                jsonOb.put("'orientation'", cursor.getString(17));
                jsonOb.put("'pose_6dof'", cursor.getString(18));
                jsonOb.put("'pressure'", cursor.getString(19));
                jsonOb.put("'proximity'", cursor.getString(20));
                jsonOb.put("'relative_humidity'", cursor.getString(21));
                jsonOb.put("'rotation_vector'", cursor.getString(22));
                jsonOb.put("'significant_motion'", cursor.getString(23));
                jsonOb.put("'stationary_detect'", cursor.getString(24));
                jsonOb.put("'step_counter'", cursor.getString(25));
                jsonOb.put("'step_detector'", cursor.getString(26));
                jsonOb.put("'gps'",cursor.getString(27));
                jsonArr.put(jsonOb);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        String fileName = "sc.json";
        //OutputStreamWriter jsonFile;

        try {
            File path = getFilesDir();
/*
            ContextWrapper cw = new ContextWrapper(this);
            File path = cw.getDir("SCdir", Context.MODE_PRIVATE);
            if (!path.exists()){
                path.createNewFile();
                path.mkdir();
            }
*/
            File file = new File(path, fileName);
            // jsonFile =  new OutputStreamWriter(file);
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(jsonArr.toString().getBytes());
            stream.close();


            //  jsonFile.write(jsonArr.toString().getBytes());

            System.out.println("*********************************************************");
            System.out.println(path.toString());
            System.out.println(jsonArr.toString());
            System.out.println("*********************************************************");
            String pathToOurFile = "/data/data/com.hranicky.iv.sensorcollector/files/sc.json";
//            zaciatokPrenosu = System.currentTimeMillis();
            sendFile(pathToOurFile);
            koniecPrenosu = System.currentTimeMillis();
            casPrenosu = Objects.toString(koniecPrenosu - zaciatokPrenosu) + "ms";
            velkostSuboru = Objects.toString(file.length()) + "B";
            System.out.println("***************velkost suboru:" + velkostSuboru + " cas prenosu" + casPrenosu + "*******************");

            //  jsonFile.flush();
            // jsonFile.close();
            //Transfer json file to server

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new Pair<>(casPrenosu, velkostSuboru);
    }


    public Pair<String, String> csv(int max) {
        String casPrenosu = "";
        String velkostSuboru = "";
        long zaciatokPrenosu = 0;
        long koniecPrenosu = 0;
        //Zaciatok vytvárania CSV
        zaciatokPrenosu = System.currentTimeMillis();
        String csvArr = "";
        try {
            Cursor cursor;
            csvArr = csvArr + "'datum';'accelerometer';'ambient_temperature';'device_private_base';'game_rotation_vector';'geomagnetic_rotation_vector';" +
                    "'gravity';'gyroscope';'gyroscope_uncalibrated';'heart_beat';'heart_rate';" +
                    "'light';'linear_acceleration';'magnetic_field';'magnetic_field_uncalibrated';'motion_detect';" +
                    "'orientation';'pose_6dof';'pressure';'proximity';'relative_humidity';" +
                    "'rotation_vector';'significant_motion';'stationary_detect';'step_counter';'step_detector';'gps'\n";
            for (int i = 1; i <= max; i++) {
                cursor = sc.rawQuery("SELECT * FROM 'scData' WHERE id = '" + i + "'", null);
                cursor.moveToFirst();
                csvArr = csvArr + "\"" + cursor.getString(1) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(2) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(3) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(4) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(5) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(6) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(7) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(8) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(9) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(10) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(11) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(12) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(13) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(14) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(15) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(16) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(17) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(18) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(19) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(20) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(21) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(22) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(23) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(24) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(25) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(26) + "\";";
                csvArr = csvArr + "\"" + cursor.getString(27) + "\"";
                if (i < max) csvArr = csvArr + "\n";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String fileName = "sc.csv";

        try {
            File path = getFilesDir();
            File file = new File(path, fileName);
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(csvArr.getBytes());
            stream.close();

            System.out.println("*********************************************************");
            System.out.println(path.toString());
            System.out.println(csvArr.toString());
            System.out.println("*********************************************************");
            String pathToOurFile = "/data/data/com.hranicky.iv.sensorcollector/files/sc.csv";
//            zaciatokPrenosu = System.currentTimeMillis();
            sendFile(pathToOurFile);
            koniecPrenosu = System.currentTimeMillis();
            casPrenosu = Objects.toString(koniecPrenosu - zaciatokPrenosu) + "ms";
            velkostSuboru = Objects.toString(file.length()) + "B";
            System.out.println("***************velkost suboru:" + velkostSuboru + " cas prenosu" + casPrenosu + "*******************");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new Pair<>(casPrenosu, velkostSuboru);
    }

    public Pair<String, String> dump(int max) {
        String casPrenosu = "";
        String velkostSuboru = "";
        long zaciatokPrenosu = 0;
        long koniecPrenosu = 0;
        //Zaciatok vytvárania DUMPu
        zaciatokPrenosu = System.currentTimeMillis();
        String fileName = "sc.db";
        int BUFFER = 2048;
        byte[] buffer = new byte[2048];
        try {

            String currentDBPath = "/data/data/" + getPackageName() + "/databases/SenCol";
            String backupDBPath = fileName;
            File currentDB = new File(currentDBPath);
            File backupDB = new File(getFilesDir(), backupDBPath);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
            String pathToOurFile = "/data/data/com.hranicky.iv.sensorcollector/files/sc.db.zip";
            File file = new File(pathToOurFile);
            try {

                FileOutputStream fos = new FileOutputStream(pathToOurFile);
                ZipOutputStream zos = new ZipOutputStream(fos);
                ZipEntry ze = new ZipEntry("sc.db");
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream("/data/data/com.hranicky.iv.sensorcollector/files/sc.db");

                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }

                in.close();
                zos.closeEntry();

                //remember close it
                zos.close();

                System.out.println("Done");

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            System.out.println("*********************************************************");
            // System.out.println(path.toString());
            // System.out.println(csvArr.toString());
            System.out.println("*********************************************************");
//            zaciatokPrenosu = System.currentTimeMillis();
            sendFile(pathToOurFile);
            koniecPrenosu = System.currentTimeMillis();
            casPrenosu = Objects.toString(koniecPrenosu - zaciatokPrenosu) + "ms";
            velkostSuboru = Objects.toString(file.length()) + "B";
            System.out.println("***************velkost suboru:" + velkostSuboru + " cas prenosu" + casPrenosu + "*******************");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new Pair<>(casPrenosu, velkostSuboru);
    }

    public void sendFile(String pathToOurFile) {
        String url2 = "http://147.175.98.76:443/~xhranicky/mysql/CRUD/Upload/handle_upload.php";

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url2);


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.setBoundary("-------------" + System.currentTimeMillis());

            if (pathToOurFile != null) {
                entityBuilder.addPart("uploadedfile", new FileBody(new File(pathToOurFile)));
            }

            HttpEntity entity = entityBuilder.build();
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity);
            Log.v("result", result);

            System.out.println("***********************-----------------**********************************");
            System.out.println(response);
            System.out.println("************************------------------*********************************");

        } catch (Exception e) {
          //  System.out.println("---------------------- ERROR *************************************************");

            e.printStackTrace();
        }
    }

    public void data2sql(int max) {
        sc.execSQL("DROP TABLE IF EXISTS 'scData'");
        sc.execSQL("CREATE TABLE 'scData' ('id' int(11) NOT NULL," +
                "'datum' varchar(45) NOT NULL," +
                "'accelerometer' varchar," +
                "'ambient_temperature' varchar," +
                "'device_private_base' varchar," +
                "'game_rotation_vector' varchar," +
                "'geomagnetic_rotation_vector' varchar," +
                "'gravity' varchar," +
                "'gyroscope' varchar," +
                "'gyroscope_uncalibrated' varchar," +
                "'heart_beat' varchar," +
                "'heart_rate' varchar," +
                "'light' varchar," +
                "'linear_acceleration' varchar," +
                "'magnetic_field' varchar," +
                "'magnetic_field_uncalibrated' varchar," +
                "'motion_detect' varchar," +
                "'orientation' varchar," +
                "'pose_6dof' varchar," +
                "'pressure' varchar," +
                "'proximity' varchar," +
                "'relative_humidity' varchar," +
                "'rotation_vector' varchar," +
                "'significant_motion' varchar," +
                "'stationary_detect' varchar," +
                "'step_counter' varchar," +
                "'step_detector' varchar," +
                "'gps' varchar," +
                "PRIMARY KEY ('id'));");

        int actual = 0;

        while (actual < max) {
            actual++;

            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            Date date = new Date(mYear - 1900, mMonth, mDay);
            String datum = DateFormat.format("dd.MM.yyyy", date).toString();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            sc.execSQL("INSERT INTO 'scData' VALUES(" + actual + "," +
                    "'" + datum + "'," +
                    "'" + allSC[0] + ";" + allSC[1] + ";" + allSC[2] + ";" + xKF + ";" + yKF + ";" + zKF + ";" + allSC[3] + ";" + allSC[4] + ";" + allSC[5] + "'," +
                    "'" + allSC[6] + "'," +
                    "'" + allSC[7] + "'," +
                    "'" + allSC[8] + ";" + allSC[9] + ";" + allSC[10] + ";" + allSC[11] + ";" + allSC[12] + "'," +
                    "'" + allSC[13] + ";" + allSC[14] + ";" + allSC[15] + ";" + allSC[16] + ";" + allSC[17] + "'," +
                    "'" + allSC[18] + ";" + allSC[19] + ";" + allSC[20] + "'," +
                    "'" + allSC[21] + ";" + allSC[22] + ";" + allSC[23] + "'," +
                    "'" + allSC[24] + ";" + allSC[25] + ";" + allSC[26] + ";" + allSC[27] + ";" + allSC[28] + ";" + allSC[29] + "'," +
                    "'" + allSC[30] + "'," +
                    "'" + allSC[31] + "'," +
                    "'" + allSC[32] + "'," +
                    "'" + allSC[33] + ";" + allSC[34] + ";" + allSC[35] + "'," +
                    "'" + allSC[36] + ";" + allSC[37] + ";" + allSC[38] + ";" + xmKF + ";" + ymKF + ";" + zmKF + "'," +
                    "'" + allSC[39] + ";" + allSC[40] + ";" + allSC[41] + ";" + allSC[42] + ";" + allSC[43] + ";" + allSC[44] + "'," +
                    "'" + allSC[45] + "'," +
                    "'" + allSC[46] + ";" + allSC[47] + ";" + allSC[48] + "'," +
                    "'" + allSC[49] + ";" + allSC[50] + ";" + allSC[51] + ";" + allSC[52] + ";" + allSC[53] + ";" + allSC[54] + ";" + allSC[55] + ";" + allSC[56] + ";" + allSC[57] + ";" + allSC[58] + ";" + allSC[59] + ";" + allSC[60] + ";" + allSC[61] + ";" + allSC[62] + ";" + allSC[63] + "'," +
                    "'" + allSC[64] + "'," +
                    "'" + allSC[65] + "'," +
                    "'" + allSC[66] + ";" + allSC[67] + ";" + allSC[68] + "'," +
                    "'" + allSC[69] + ";" + allSC[70] + ";" + allSC[71] + ";" + allSC[72] + ";" + allSC[73] + "'," +
                    "'" + allSC[74] + "'," +
                    "'" + allSC[75] + "'," +
                    "'" + allSC[76] + "'," +
                    "'" + allSC[77] + "'," +
                    "'" + GPSData + "');");
        }

    }

    //CheckBoxes
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.phase1:
                if (checked) {
                    phase1 = true;
                } else {
                    phase1 = false;
                }
                break;
            case R.id.phase2:
                if (checked) {
                    phase2 = true;
                } else {
                    phase2 = false;
                }
                break;
            case R.id.phase3:
                if (checked) {
                    phase3 = true;
                } else {
                    phase3 = false;
                }
                break;
        }
    }


    private void initializeSensors() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // Success! There's a pressure sensor.
            mACCELEROMETER = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            setListener(mACCELEROMETER);
            m1a.setVisibility(View.VISIBLE);
            m1b.setVisibility(View.VISIBLE);

            m1a.append("\nNázov: " + mACCELEROMETER.getName() + "\nPredajca: " + mACCELEROMETER.getVendor() + "\nVerzia: " + mACCELEROMETER.getVersion() + "\n");

        } else {
            // Failure! No pressure sensor.
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            // Success! There's a pressure sensor.
            mAMBIENT_TEMPERATURE = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            setListener(mAMBIENT_TEMPERATURE);
            m2a.setVisibility(View.VISIBLE);
            m2b.setVisibility(View.VISIBLE);

            m2a.append("\nNázov: " + mAMBIENT_TEMPERATURE.getName() + "\nPredajca: " + mAMBIENT_TEMPERATURE.getVendor() + "\nVerzia: " + mAMBIENT_TEMPERATURE.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_DEVICE_PRIVATE_BASE) != null) {
            // Success! There's a pressure sensor.
            mDEVICE_PRIVATE_BASE = mSensorManager.getDefaultSensor(Sensor.TYPE_DEVICE_PRIVATE_BASE);
            setListener(mDEVICE_PRIVATE_BASE);
            m3a.setVisibility(View.VISIBLE);
            m3b.setVisibility(View.VISIBLE);

            m3a.append("\nNázov: " + mDEVICE_PRIVATE_BASE.getName() + "\nPredajca: " + mDEVICE_PRIVATE_BASE.getVendor() + "\nVerzia: " + mDEVICE_PRIVATE_BASE.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) != null) {
            // Success! There's a pressure sensor.
            mGAME_ROTATION_VECTOR = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
            setListener(mGAME_ROTATION_VECTOR);
            m4a.setVisibility(View.VISIBLE);
            m4b.setVisibility(View.VISIBLE);

            m4a.append("\nNázov: " + mGAME_ROTATION_VECTOR.getName() + "\nPredajca: " + mGAME_ROTATION_VECTOR.getVendor() + "\nVerzia: " + mGAME_ROTATION_VECTOR.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) != null) {
            // Success! There's a pressure sensor.
            mGEOMAGNETIC_ROTATION_VECTOR = mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
            setListener(mGEOMAGNETIC_ROTATION_VECTOR);
            m5a.setVisibility(View.VISIBLE);
            m5b.setVisibility(View.VISIBLE);

            m5a.append("\nNázov: " + mGEOMAGNETIC_ROTATION_VECTOR.getName() + "\nPredajca: " + mGEOMAGNETIC_ROTATION_VECTOR.getVendor() + "\nVerzia: " + mGEOMAGNETIC_ROTATION_VECTOR.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            // Success! There's a pressure sensor.
            mGRAVITY = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            setListener(mGRAVITY);
            m6a.setVisibility(View.VISIBLE);
            m6b.setVisibility(View.VISIBLE);

            m6a.append("\nNázov: " + mGRAVITY.getName() + "\nPredajca: " + mGRAVITY.getVendor() + "\nVerzia: " + mGRAVITY.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            // Success! There's a pressure sensor.
            mGYROSCOPE = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            setListener(mGYROSCOPE);
            m7a.setVisibility(View.VISIBLE);
            m7b.setVisibility(View.VISIBLE);

            m7a.append("\nNázov: " + mGRAVITY.getName() + "\nPredajca: " + mGRAVITY.getVendor() + "\nVerzia: " + mGRAVITY.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED) != null) {
            // Success! There's a pressure sensor.
            mGYROSCOPE_UNCALIBRATED = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
            setListener(mGYROSCOPE_UNCALIBRATED);
            m8a.setVisibility(View.VISIBLE);
            m8b.setVisibility(View.VISIBLE);

            m8a.append("\nNázov: " + mGYROSCOPE_UNCALIBRATED.getName() + "\nPredajca: " + mGYROSCOPE_UNCALIBRATED.getVendor() + "\nVerzia: " + mGYROSCOPE_UNCALIBRATED.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT) != null) {
            // Success! There's a pressure sensor.
            mHEART_BEAT = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT);
            setListener(mHEART_BEAT);
            m9a.setVisibility(View.VISIBLE);
            m9b.setVisibility(View.VISIBLE);

            m9a.append("\nNázov: " + mHEART_BEAT.getName() + "\nPredajca: " + mHEART_BEAT.getVendor() + "\nVerzia: " + mHEART_BEAT.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null) {
            // Success! There's a pressure sensor.
            mHEART_RATE = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
            setListener(mHEART_RATE);
            m10a.setVisibility(View.VISIBLE);
            m10b.setVisibility(View.VISIBLE);

            m10a.append("\nNázov: " + mHEART_RATE.getName() + "\nPredajca: " + mHEART_RATE.getVendor() + "\nVerzia: " + mHEART_RATE.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            // Success! There's a pressure sensor.
            mLIGHT = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            setListener(mLIGHT);
            m11a.setVisibility(View.VISIBLE);
            m11b.setVisibility(View.VISIBLE);

            m11a.append("\nNázov: " + mLIGHT.getName() + "\nPredajca: " + mLIGHT.getVendor() + "\nVerzia: " + mLIGHT.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null) {
            // Success! There's a pressure sensor.
            mLINEAR_ACCELERATION = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            setListener(mLINEAR_ACCELERATION);
            m12a.setVisibility(View.VISIBLE);
            m12b.setVisibility(View.VISIBLE);

            m12a.append("\nNázov: " + mLINEAR_ACCELERATION.getName() + "\nPredajca: " + mLINEAR_ACCELERATION.getVendor() + "\nVerzia: " + mLINEAR_ACCELERATION.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            // Success! There's a pressure sensor.
            mMAGNETIC_FIELD = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            setListener(mMAGNETIC_FIELD);
            m13a.setVisibility(View.VISIBLE);
            m13b.setVisibility(View.VISIBLE);

            m13a.append("\nNázov: " + mMAGNETIC_FIELD.getName() + "\nPredajca: " + mMAGNETIC_FIELD.getVendor() + "\nVerzia: " + mMAGNETIC_FIELD.getVersion() + "\n");

        }

        m13aCompass.setVisibility(View.VISIBLE);
        m13bCompass.setVisibility(View.VISIBLE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) != null) {
            // Success! There's a pressure sensor.
            mMAGNETIC_FIELD_UNCALIBRATED = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
            setListener(mMAGNETIC_FIELD_UNCALIBRATED);
            m14a.setVisibility(View.VISIBLE);
            m14b.setVisibility(View.VISIBLE);

            m14a.append("\nNázov: " + mMAGNETIC_FIELD_UNCALIBRATED.getName() + "\nPredajca: " + mMAGNETIC_FIELD_UNCALIBRATED.getVendor() + "\nVerzia: " + mMAGNETIC_FIELD_UNCALIBRATED.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT) != null) {
            // Success! There's a pressure sensor.
            mMOTION_DETECT = mSensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT);
            setListener(mMOTION_DETECT);
            m15a.setVisibility(View.VISIBLE);
            m15b.setVisibility(View.VISIBLE);

            m15a.append("\nNázov: " + mMOTION_DETECT.getName() + "\nPredajca: " + mMOTION_DETECT.getVendor() + "\nVerzia: " + mMOTION_DETECT.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null) {
            // Success! There's a pressure sensor.
            mORIENTATION = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            setListener(mORIENTATION);
            m15aa.setVisibility(View.VISIBLE);
            m15bb.setVisibility(View.VISIBLE);

            m15aa.append("\nNázov: " + mORIENTATION.getName() + "\nPredajca: " + mORIENTATION.getVendor() + "\nVerzia: " + mORIENTATION.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF) != null) {
            // Success! There's a pressure sensor.
            mPOSE_6DOF = mSensorManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF);
            setListener(mPOSE_6DOF);
            m16a.setVisibility(View.VISIBLE);
            m16b.setVisibility(View.VISIBLE);

            m16a.append("\nNázov: " + mPOSE_6DOF.getName() + "\nPredajca: " + mPOSE_6DOF.getVendor() + "\nVerzia: " + mPOSE_6DOF.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            // Success! There's a pressure sensor.
            mPRESSURE = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            setListener(mPRESSURE);
            m17a.setVisibility(View.VISIBLE);
            m17b.setVisibility(View.VISIBLE);

            m17a.append("\nNázov: " + mPRESSURE.getName() + "\nPredajca: " + mPRESSURE.getVendor() + "\nVerzia: " + mPRESSURE.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            // Success! There's a pressure sensor.
            mPROXIMITY = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            setListener(mPROXIMITY);
            m18a.setVisibility(View.VISIBLE);
            m18b.setVisibility(View.VISIBLE);

            m18a.append("\nNázov: " + mPROXIMITY.getName() + "\nPredajca: " + mPROXIMITY.getVendor() + "\nVerzia: " + mPROXIMITY.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
            // Success! There's a pressure sensor.
            mRELATIVE_HUMIDITY = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
            setListener(mRELATIVE_HUMIDITY);
            m19a.setVisibility(View.VISIBLE);
            m19b.setVisibility(View.VISIBLE);

            m19a.append("\nNázov: " + mRELATIVE_HUMIDITY.getName() + "\nPredajca: " + mRELATIVE_HUMIDITY.getVendor() + "\nVerzia: " + mRELATIVE_HUMIDITY.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null) {
            // Success! There's a pressure sensor.
            mROTATION_VECTOR = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            setListener(mROTATION_VECTOR);
            m20a.setVisibility(View.VISIBLE);
            m20b.setVisibility(View.VISIBLE);

            m20a.append("\nNázov: " + mROTATION_VECTOR.getName() + "\nPredajca: " + mROTATION_VECTOR.getVendor() + "\nVerzia: " + mROTATION_VECTOR.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION) != null) {
            // Success! There's a pressure sensor.
            mSIGNIFICANT_MOTION = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
            setListener(mSIGNIFICANT_MOTION);
            m21a.setVisibility(View.VISIBLE);
            m21b.setVisibility(View.VISIBLE);

            m21a.append("\nNázov: " + mSIGNIFICANT_MOTION.getName() + "\nPredajca: " + mSIGNIFICANT_MOTION.getVendor() + "\nVerzia: " + mSIGNIFICANT_MOTION.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT) != null) {
            // Success! There's a pressure sensor.
            mSTATIONARY_DETECT = mSensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT);
            setListener(mSTATIONARY_DETECT);
            m22a.setVisibility(View.VISIBLE);
            m22b.setVisibility(View.VISIBLE);

            m22a.append("\nNázov: " + mSTATIONARY_DETECT.getName() + "\nPredajca: " + mSTATIONARY_DETECT.getVendor() + "\nVerzia: " + mSTATIONARY_DETECT.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            // Success! There's a pressure sensor.
            mSTEP_COUNTER = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            setListener(mSTEP_COUNTER);
            m23a.setVisibility(View.VISIBLE);
            m23b.setVisibility(View.VISIBLE);

            m23a.append("\nNázov: " + mSTEP_COUNTER.getName() + "\nPredajca: " + mSTEP_COUNTER.getVendor() + "\nVerzia: " + mSTEP_COUNTER.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            // Success! There's a pressure sensor.
            mSTEP_DETECTOR = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            setListener(mSTEP_DETECTOR);
            m24a.setVisibility(View.VISIBLE);
            m24b.setVisibility(View.VISIBLE);

            m24a.append("\nNázov: " + mSTEP_DETECTOR.getName() + "\nPredajca: " + mSTEP_DETECTOR.getVendor() + "\nVerzia: " + mSTEP_DETECTOR.getVersion() + "\n");

        }

        //GPS
        // Acquire a reference to the system Location Manager
        mGPS = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
           // return;
        }
        // Register the listener with the Location Manager to receive location updates
        mGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the gps location provider.
            //makeUseOfNewLocation(location);

            String zemDlzka = "" + location.getLongitude();
            String zemSirka = "" + location.getLatitude();
            String nadVyska = "" + location.getAltitude();
            String presnost = "" + location.getAccuracy();
            String cas = "" + location.getTime();

            m25a.setText("GPS:\n Aktívne\n");
            m25b.setText("Hodnoty:\nZemepisná dĺžka: " + zemDlzka + "\nZemepisná šírka: " + zemSirka + "\nNadmorská výška: " + nadVyska + "\nPresnosť: " + presnost + "\nČas: " + cas +"\n");
            GPSData = zemDlzka + ";" + zemSirka + ";" + nadVyska + ";" + presnost + ";" + cas;


          //  System.out.println("---------------------------------\n\n\n\nDATA PROV EN\n" + GPSData + "\n\n\n------------------------");

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
            m25a.setText("GPS:\n Aktívne\n");
            m25b.setText("Hodnoty:\n Zisťujem polohu!\n");
//                GPSData = "null;null;null;null;null";

         //   System.out.println("---------------------------------\n\n\n\nPROVIDER ENABLED\n\n\n\n------------------------");

        }

        public void onProviderDisabled(String provider) {
            m25a.setText("GPS:\n Neaktívne\n");
            m25b.setText("Hodnoty:\n Potrebné aktivovať GPS!\n(Aktivácia môže trvať niekoľko sekúnd)\n");
            GPSData = "null;null;null;null;null";

          //  System.out.println("---------------------------------\n\n\n\nPROVIDER\n\n\n\n------------------------");

        }
    };


    private void initializeTextViews() {
        m1a=(TextView) findViewById(R.id.textViewM1a);
        m2a=(TextView) findViewById(R.id.textViewM2a);
        m3a=(TextView) findViewById(R.id.textViewM3a);
        m4a=(TextView) findViewById(R.id.textViewM4a);
        m5a=(TextView) findViewById(R.id.textViewM5a);
        m6a=(TextView) findViewById(R.id.textViewM6a);
        m7a=(TextView) findViewById(R.id.textViewM7a);
        m8a=(TextView) findViewById(R.id.textViewM8a);
        m9a=(TextView) findViewById(R.id.textViewM9a);
        m10a=(TextView) findViewById(R.id.textViewM10a);
        m11a=(TextView) findViewById(R.id.textViewM11a);
        m12a=(TextView) findViewById(R.id.textViewM12a);
        m13a=(TextView) findViewById(R.id.textViewM13a);
        m13aCompass=(TextView) findViewById(R.id.textViewM13aCompass);
        m14a=(TextView) findViewById(R.id.textViewM14a);
        m15a=(TextView) findViewById(R.id.textViewM15a);
        m15aa=(TextView) findViewById(R.id.textViewM15aa);
        m16a=(TextView) findViewById(R.id.textViewM16a);
        m17a=(TextView) findViewById(R.id.textViewM17a);
        m18a=(TextView) findViewById(R.id.textViewM18a);
        m19a=(TextView) findViewById(R.id.textViewM19a);
        m20a=(TextView) findViewById(R.id.textViewM20a);
        m21a=(TextView) findViewById(R.id.textViewM21a);
        m22a=(TextView) findViewById(R.id.textViewM22a);
        m23a=(TextView) findViewById(R.id.textViewM23a);
        m24a=(TextView) findViewById(R.id.textViewM24a);
        m25a=(TextView) findViewById(R.id.textViewM25a);

        m1b=(TextView) findViewById(R.id.textViewM1b);
        m2b=(TextView) findViewById(R.id.textViewM2b);
        m3b=(TextView) findViewById(R.id.textViewM3b);
        m4b=(TextView) findViewById(R.id.textViewM4b);
        m5b=(TextView) findViewById(R.id.textViewM5b);
        m6b=(TextView) findViewById(R.id.textViewM6b);
        m7b=(TextView) findViewById(R.id.textViewM7b);
        m8b=(TextView) findViewById(R.id.textViewM8b);
        m9b=(TextView) findViewById(R.id.textViewM9b);
        m10b=(TextView) findViewById(R.id.textViewM10b);
        m11b=(TextView) findViewById(R.id.textViewM11b);
        m12b=(TextView) findViewById(R.id.textViewM12b);
        m13b=(TextView) findViewById(R.id.textViewM13b);
        m13bCompass=(TextView) findViewById(R.id.textViewM13bCompass);
        m14b=(TextView) findViewById(R.id.textViewM14b);
        m15b=(TextView) findViewById(R.id.textViewM15b);
        m15bb=(TextView) findViewById(R.id.textViewM15bb);
        m16b=(TextView) findViewById(R.id.textViewM16b);
        m17b=(TextView) findViewById(R.id.textViewM17b);
        m18b=(TextView) findViewById(R.id.textViewM18b);
        m19b=(TextView) findViewById(R.id.textViewM19b);
        m20b=(TextView) findViewById(R.id.textViewM20b);
        m21b=(TextView) findViewById(R.id.textViewM21b);
        m22b=(TextView) findViewById(R.id.textViewM22b);
        m23b=(TextView) findViewById(R.id.textViewM23b);
        m24b=(TextView) findViewById(R.id.textViewM24b);
        m25b=(TextView) findViewById(R.id.textViewM25b);

        m13cCompass=(ImageView) findViewById(R.id.textViewM13cCompass);
        m13dCompass=(LinearLayout) findViewById(R.id.textViewM13dCompass);
    }

    public void setListener(Sensor s){
        mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v == start){
            Toast.makeText(MainActivity.this, "Počkajte prosím, záznamy sa spracúvajú.", Toast.LENGTH_SHORT).show();
            sensorsRunning = new SensorsDataTask();
            sensorsRunning.execute();
            /*MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    startPhases();
                }
            });*/
        }

    }

    //Kalman filter for 1-3 dimensions
    private float xKF = 0;
    private float yKF = 0;
    private float zKF = 0;

    private float xmKF = 0;
    private float ymKF = 0;
    private float zmKF = 0;

    private float _q[] = new float[6]; // = 0; //process noise covariance
    private float _r[] = new float[6]; // = 1; //measurement noise covariance
    private float _x[] = new float[6]; // = 0; //value
    private float _y[] = new float[6]; // = 0; //value
    private float _p[] = new float[6]; // = 1; //estimation error covariance
    private float _k[] = new float[6]; // = (float) 0.5; //kalman gain
    private boolean kalmanSet1 = false;
    private boolean kalmanSet2 = false;
    private boolean kalmanSet3 = false;
    private boolean kalmanSet4 = false;
    private boolean kalmanSet5 = false;
    private boolean kalmanSet6 = false;

    public float kalmanFilterUpdateDimensionData (float measurement, int i){
        //prediction update
        float qstart = (float) 0.125;
        float rpstart = (float) 32;
        float kstart = (float) 0.5;

        float qstartM = (float) 4;
        float rpstartM = (float) 32;
        float kstartM = (float) 0.5;
        //omit _x = _x
        if ((!kalmanSet1) && (i == 1)) {
            kalmanSet1 = true;
            _x[i-1] = measurement;
            _q[i-1] = qstart;
            _r[i-1] = rpstart;
            _p[i-1] = rpstart;
            _k[i-1] = kstart;
        }

        if ((!kalmanSet2) && (i == 2)) {
            kalmanSet2 = true;
            _x[i-1] = measurement;
            _q[i-1] = qstart;
            _r[i-1] = rpstart;
            _p[i-1] = rpstart;
            _k[i-1] = kstart;
        }

        if ((!kalmanSet3) && (i == 3)) {
            kalmanSet3 = true;
            _x[i-1] = measurement;
            _q[i-1] = qstart;
            _r[i-1] = rpstart;
            _p[i-1] = rpstart;
            _k[i-1] = kstart;
        }

        if ((!kalmanSet4) && (i == 4)) {
            kalmanSet4 = true;
            _x[i-1] = measurement;
            _q[i-1] = qstartM;
            _r[i-1] = rpstartM;
            _p[i-1] = rpstartM;
            _k[i-1] = kstartM;
        }

        if ((!kalmanSet5) && (i == 5)) {
            kalmanSet5 = true;
            _x[i-1] = measurement;
            _q[i-1] = qstartM;
            _r[i-1] = rpstartM;
            _p[i-1] = rpstartM;
            _k[i-1] = kstartM;
        }

        if ((!kalmanSet6) && (i == 6)) {
            kalmanSet6 = true;
            _x[i-1] = measurement;
            _q[i-1] = qstartM;
            _r[i-1] = rpstartM;
            _p[i-1] = rpstartM;
            _k[i-1] = kstartM;
        }

        _p[i-1] = _p[i-1] + _q[i-1];

        //measurement update
        _k[i-1] = _p[i-1] / (_p[i-1] + _r[i-1]);
        _y[i-1] = (measurement - _x[i-1]);
        _x[i-1] = _x[i-1] + _k[i-1] * _y[i-1];
        _p[i-1] = (1 - _k[i-1]) * _p[i-1];

        return _x[i-1];
    }
}
