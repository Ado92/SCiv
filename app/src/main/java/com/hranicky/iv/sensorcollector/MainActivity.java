package com.hranicky.iv.sensorcollector;

import android.app.ProgressDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {


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

    //TextView:
    private TextView m1a=null,m2a=null,m3a=null,m4a=null,m5a=null,m6a=null,m7a=null,m8a=null,m9a=null,m10a=null,m11a=null,m12a=null,m13a=null,m14a=null,m15a=null,m15aa=null,m16a=null,m17a=null,m18a=null,m19a=null,m20a=null,m21a=null,m22a=null,m23a=null,m24a=null;
    private TextView m1b=null,m2b=null,m3b=null,m4b=null,m5b=null,m6b=null,m7b=null,m8b=null,m9b=null,m10b=null,m11b=null,m12b=null,m13b=null,m14b=null,m15b=null,m15bb=null,m16b=null,m17b=null,m18b=null,m19b=null,m20b=null,m21b=null,m22b=null,m23b=null,m24b=null;

    //Temperature
    private float temperature= (float) -500.0;

    private boolean phase1=true;
    private boolean phase2=true;
    private boolean phase3=true;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.textView2);
        tv1.setVisibility(View.GONE);

        tv2 = (TextView) findViewById(R.id.textView3);
        tv2.setVisibility(View.GONE);
/*
        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
*/
        initializeTextViews();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mList= mSensorManager.getSensorList(Sensor.TYPE_ALL);

        initializeSensors();
        tv2.setVisibility(View.VISIBLE);
        tv2.setText("Senzorov:" + mList.size() );
        for (int i = 0; i < mList.size(); i++) {
            tv2.append("\n" + mList.get(i).getName());
        }

/*
        mList= mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 1; i < mList.size(); i++) {
            tv1.setVisibility(View.VISIBLE);
            tv1.append("\n" + mList.get(i).getName() + "\n" + mList.get(i).getVendor() + "\n" + mList.get(i).getVersion() + "\n\n");
        }
*/

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

            et.setText(maxCount);
            ph1.setChecked(phase1);
            ph2.setChecked(phase2);
            ph3.setChecked(phase3);
        }

        start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(this);

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

        savedInstanceState.putInt("maxCount",maxCount);
        savedInstanceState.putInt("actualCount",actualCount);
        savedInstanceState.putBoolean("phase1",phase1);
        savedInstanceState.putBoolean("phase2",phase2);
        savedInstanceState.putBoolean("phase3",phase3);

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

        et.setText(maxCount);
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


    float[] gravity = new float[3];
    float[] linear_acceleration = new float[3];

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
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == event.sensor){
            m1b.setText("Hodnoty (m/s^2):\n");
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

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            writeValues("x",m1b,linear_acceleration[0]);
            writeValues("y",m1b,linear_acceleration[1]);
            writeValues("z",m1b,linear_acceleration[2]);
            m1b.append("Neupravené dáta zo senzoru (m/s^2):\n");
            writeValues("x",m1b,event.values[0]);
            writeValues("y",m1b,event.values[1]);
            writeValues("z",m1b,event.values[2]);

        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) == event.sensor){
            m2b.setText("Hodnota (°C):\n");
            writeValues("Teplota okolia:",m2b,event.values[0]);
            temperature = event.values[0];
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_DEVICE_PRIVATE_BASE) == event.sensor){
            m3b.setText("Hodnota:\n");
                writeValues("Device Private Base",m3b,event.values[0]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) == event.sensor){
            m4b.setText("Hodnoty (bez ohľadu na geomagnetické pole Zeme):\n");
            String value="";
            for(int i=0; i < event.values.length; i++){
                if(i==0) {
                    value="x*sin(θ/2)";
                }
                else if(i==1) {
                    value="y*sin(θ/2)";
                }
                else if(i==2) {
                    value="z*sin(θ/2)";
                }
                else if(i==3) {
                    value="cos(θ/2)";
                }
                else if(i==4) {
                    value="Odhadovaná presnosť (rad, -1 ak nie je k disp.)";
                }
                writeValues(value,m4b,event.values[i]);
            }
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) == event.sensor){
            m5b.setText("Hodnoty (bez použitia gyroskopu):\n");
            String value="";
            for(int i=0; i < event.values.length; i++){
                if(i==0) {
                    value="x*sin(θ/2)";
                }
                else if(i==1) {
                    value="y*sin(θ/2)";
                }
                else if(i==2) {
                    value="z*sin(θ/2)";
                }
                else if(i==3) {
                    value="cos(θ/2)";
                }
                else if(i==4) {
                    value="Odhadovaná presnosť (rad, -1 ak nie je k disp.)";
                }
                writeValues(value,m5b,event.values[i]);
            }
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) == event.sensor){
            m6b.setText("Hodnoty (m/s^2):\n");
            /*
            The gravity vector components are reported in m/s^2 in the x, y and z fields of sensors_event_t.acceleration.
            When the device is at rest, the output of the gravity sensor should be identical to that of the accelerometer. On Earth, the magnitude is around 9.8 m/s^2.
             */

            writeValues("x",m6b,event.values[0]);
            writeValues("y",m6b,event.values[1]);
            writeValues("z",m6b,event.values[2]);

        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == event.sensor){
            m7b.setText("Hodnoty rýchlostí otáčania okolo os (rad/s):\n");
            writeValues("x",m7b,event.values[0]);
            writeValues("y",m7b,event.values[1]);
            writeValues("z",m7b,event.values[2]);

        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED) == event.sensor){
            m8b.setText("Hodnoty rýchlostí otáčania okolo os bez driftovej kompenzácie (rad/s):\n");
            writeValues("x",m8b,event.values[0]);
            writeValues("y",m8b,event.values[1]);
            writeValues("z",m8b,event.values[2]);
            m8b.append("Odhadovaný drift okolo os (rad/s):\n");
            writeValues("x",m8b,event.values[3]);
            writeValues("y",m8b,event.values[4]);
            writeValues("z",m8b,event.values[5]);

        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT) == event.sensor){
            m9b.setText("Hodnota (0.0 - pri veľkej nepresnosti, 1.0 pri veľkej presnosti):\n");
            writeValues("Zaznamenanie úderu srdca",m9b,event.values[0]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) == event.sensor){
            m10b.setText("Hodnota (bpm - počet úderov za min.):\n");
            writeValues("Rýchlosť úderov srdca",m10b,event.values[0]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == event.sensor){
            m11b.setText("Hodnota (lux):\n");
            writeValues("Okolité osvetlenie",m11b,event.values[0]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) == event.sensor){
            m12b.setText("Hodnoty:\n");
            for(int i=0; i < event.values.length; i++){
                writeValues("x",m12b,event.values[i]);
            }

            m12b.setText("Hodnoty akceleračnej rýchlosti okolo os bez gravitácie (m/s^2):\n");
            writeValues("x",m8b,event.values[0]);
            writeValues("y",m8b,event.values[1]);
            writeValues("z",m8b,event.values[2]);

        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == event.sensor){
            m13b.setText("Hodnoty okolitého magnetického poľa v osiach (uT):\n");
            writeValues("x",m13b,event.values[0]);
            writeValues("y",m13b,event.values[1]);
            writeValues("z",m13b,event.values[2]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) == event.sensor){
            m14b.setText("Hodnoty okolitého magnetického poľa v osiach bez kalibrácie ťažkými kovmi (uT):\n");
            writeValues("x",m14b,event.values[0]);
            writeValues("y",m14b,event.values[1]);
            writeValues("z",m14b,event.values[2]);
            m14b.append("Ovplyvnenie os ťažkými kovmi (uT):\n");
            writeValues("x",m14b,event.values[3]);
            writeValues("y",m14b,event.values[4]);
            writeValues("z",m14b,event.values[5]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT) == event.sensor){
            m15b.setText("Hodnota (N/A, 1.0 pri pohnutí zariadením po dobu aspoň 5s):\n");
            writeValues("Pohyb:",m15b,event.values[0]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) == event.sensor){
            m15bb.setText("Hodnoty (°):\n");
            writeValues("Uhol zdvihu (uhol okolo osi x)",m15bb,event.values[1]);
            writeValues("Bočný náklon (uhol okolo osi y)",m15bb,event.values[2]);
            writeValues("Azimut (uhol okolo osi z)",m15bb,event.values[0]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF) == event.sensor){
            m16b.setText("Hodnoty:\n");
            writeValues("x*sin(θ/2)",m16b,event.values[0]);
            writeValues("y*sin(θ/2)",m16b,event.values[1]);
            writeValues("z*sin(θ/2)",m16b,event.values[2]);
            writeValues("cos(θ/2)",m16b,event.values[3]);
            m16b.append("Posun po osiach od ľubovoľného počiatku:\n");
            writeValues("x",m16b,event.values[4]);
            writeValues("y",m16b,event.values[5]);
            writeValues("z",m16b,event.values[6]);
            m16b.append("Delta rotácia kvaterniónu:\n");
            writeValues("x*sin(θ/2)",m16b,event.values[7]);
            writeValues("y*sin(θ/2)",m16b,event.values[8]);
            writeValues("z*sin(θ/2)",m16b,event.values[9]);
            writeValues("cos(θ/2)",m16b,event.values[10]);
            m16b.append("Delta posun po osiach:\n");
            writeValues("x",m16b,event.values[11]);
            writeValues("y",m16b,event.values[12]);
            writeValues("z",m16b,event.values[13]);
            writeValues("Sekvenčné číslo",m16b,event.values[14]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) == event.sensor){
            m17b.setText("Hodnota (hPa):\n");
            writeValues("Atmosferický tlak",m17b,event.values[0]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == event.sensor){
            m18b.setText("Hodnota (cm):\n");
            writeValues("Blízkosť",m18b,event.values[0]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) == event.sensor){
            m19b.setText("Hodnoty (%):\n");
            writeValues("Relatívna vlhkosť okolitého vzduchu",m19b,event.values[0]);
            if(temperature > -499) {
                float dewPoint;
                float h = (float) (Math.log(event.values[0] / 100.0) + (17.62 * temperature) / (243.12 + temperature));
                dewPoint = (float) (243.12 * h / (17.62 - h));
                float absoluteHumidity;
                absoluteHumidity = (float) (216.7 * (event.values[0] / 100.0 * 6.112 * Math.exp(17.62 * temperature / (243.12 + temperature)) / (273.15 + temperature)));
                m19b.append("Teplota potrebná na zkondenzovanie vzduchu (°C):\n");
                writeValues("Rosný bod", m19b, dewPoint);
                m19a.append("Množstvo vodnej pary v určitom množstvo suchého vzduchu (g/m^3)");
                writeValues("Absolútna vlhkosť", m19b, absoluteHumidity);

            }
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == event.sensor){
            m20b.setText("Hodnoty (vzhľadom na geomagnetické pole Zeme):\n");
            String value="";
            for(int i=0; i < event.values.length; i++){
                if(i==0) {
                    value="x*sin(θ/2)";
                }
                else if(i==1) {
                    value="y*sin(θ/2)";
                }
                else if(i==2) {
                    value="z*sin(θ/2)";
                }
                else if(i==3) {
                    value="cos(θ/2)";
                }
                else if(i==4) {
                    value="Odhadovaná presnosť (rad, -1 ak nie je k disp.)";
                }
                writeValues(value,m20b,event.values[i]);
            }
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION) == event.sensor){
            m21b.setText("Bez hodnôt:\n");
            writeValues("(1):",m21b,event.values[0]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT) == event.sensor){
            m22b.setText("Hodnota (N/A, 1.0 pri nepohnutí zariadením po dobu aspoň 5s):\n");
            writeValues("Bez pohybu:",m22b,event.values[0]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) == event.sensor){
            m23b.setText("Hodnota (kroky):\n");
            writeValues("Počet krokov od posledného rebootu",m23b,event.values[0]);
        }
        else
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) == event.sensor){
            m24b.setText("Hodnota (N/A, 1.0 pri kroku):\n");
            writeValues("Krok",m24b,event.values[0]);

        }




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


    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    //Run all checked phases
    public void startPhases() {
        maxCount = Integer.parseInt(( (EditText) findViewById(R.id.pocetVzoriek)).getText().toString());
        actualCount = 0;

        start.setText("Stop\n("+(actualCount+1)+"/"+maxCount+")");
        //Execute phases
        while (actualCount < maxCount) {
            actualCount++;
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            Date date = new Date(mYear - 1900, mMonth, mDay);

            final String name = DateFormat.format("dd.MM.yyyy", date).toString();
            final String desg = String.valueOf(actualCount + 1);
            final String sal = String.valueOf(maxCount);

            class AddEmployee extends AsyncTask<Void, Void, String> {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(MainActivity.this, "Adding...", "Wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Config.KEY_EMP_NAME, name);
                    params.put(Config.KEY_EMP_DESG, desg);
                    params.put(Config.KEY_EMP_SAL, sal);

                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.URL_ADD, params);
                    return res;
                }
            }

            AddEmployee ae = new AddEmployee();
            ae.execute();
        }
        //Koniec cyklu
        start.setText("Štart");

    }

    //CheckBoxes
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.phase1:
                if (checked)
                {
                    phase1 = true;
                }
                else
                {
                    phase1 = false;
                }
                break;
            case R.id.phase2:
                if (checked)
                {
                    phase2 = true;
                }
                else
                {
                    phase2 = false;
                }
                break;
            case R.id.phase3:
                if (checked)
                {
                    phase3 = true;
                }
                else
                {
                    phase3 = false;
                }
                break;
        }
    }


    private void initializeSensors() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            // Success! There's a pressure sensor.
            mACCELEROMETER = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            setListener(mACCELEROMETER);
            m1a.setVisibility(View.VISIBLE);
            m1b.setVisibility(View.VISIBLE);

            m1a.append("\nNázov: " + mACCELEROMETER.getName() + "\nPredajca: " + mACCELEROMETER.getVendor() + "\nVerzia: " + mACCELEROMETER.getVersion() + "\n");

        }
        else {
            // Failure! No pressure sensor.
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            // Success! There's a pressure sensor.
            mAMBIENT_TEMPERATURE = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            setListener(mAMBIENT_TEMPERATURE);
            m2a.setVisibility(View.VISIBLE);
            m2b.setVisibility(View.VISIBLE);

            m2a.append("\nNázov: " + mAMBIENT_TEMPERATURE.getName() + "\nPredajca: " + mAMBIENT_TEMPERATURE.getVendor() + "\nVerzia: " + mAMBIENT_TEMPERATURE.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_DEVICE_PRIVATE_BASE) != null){
            // Success! There's a pressure sensor.
            mDEVICE_PRIVATE_BASE = mSensorManager.getDefaultSensor(Sensor.TYPE_DEVICE_PRIVATE_BASE);
            setListener(mDEVICE_PRIVATE_BASE);
            m3a.setVisibility(View.VISIBLE);
            m3b.setVisibility(View.VISIBLE);

            m3a.append("\nNázov: " + mDEVICE_PRIVATE_BASE.getName() + "\nPredajca: " + mDEVICE_PRIVATE_BASE.getVendor() + "\nVerzia: " + mDEVICE_PRIVATE_BASE.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) != null){
            // Success! There's a pressure sensor.
            mGAME_ROTATION_VECTOR = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
            setListener(mGAME_ROTATION_VECTOR);
            m4a.setVisibility(View.VISIBLE);
            m4b.setVisibility(View.VISIBLE);

            m4a.append("\nNázov: " + mGAME_ROTATION_VECTOR.getName() + "\nPredajca: " + mGAME_ROTATION_VECTOR.getVendor() + "\nVerzia: " + mGAME_ROTATION_VECTOR.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) != null){
            // Success! There's a pressure sensor.
            mGEOMAGNETIC_ROTATION_VECTOR = mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
            setListener(mGEOMAGNETIC_ROTATION_VECTOR);
            m5a.setVisibility(View.VISIBLE);
            m5b.setVisibility(View.VISIBLE);

            m5a.append("\nNázov: " + mGEOMAGNETIC_ROTATION_VECTOR.getName() + "\nPredajca: " + mGEOMAGNETIC_ROTATION_VECTOR.getVendor() + "\nVerzia: " + mGEOMAGNETIC_ROTATION_VECTOR.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null){
            // Success! There's a pressure sensor.
            mGRAVITY = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            setListener(mGRAVITY);
            m6a.setVisibility(View.VISIBLE);
            m6b.setVisibility(View.VISIBLE);

            m6a.append("\nNázov: " + mGRAVITY.getName() + "\nPredajca: " + mGRAVITY.getVendor() + "\nVerzia: " + mGRAVITY.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){
            // Success! There's a pressure sensor.
            mGYROSCOPE = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            setListener(mGYROSCOPE);
            m7a.setVisibility(View.VISIBLE);
            m7b.setVisibility(View.VISIBLE);

            m7a.append("\nNázov: " + mGRAVITY.getName() + "\nPredajca: " + mGRAVITY.getVendor() + "\nVerzia: " + mGRAVITY.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED) != null){
            // Success! There's a pressure sensor.
            mGYROSCOPE_UNCALIBRATED = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
            setListener(mGYROSCOPE_UNCALIBRATED);
            m8a.setVisibility(View.VISIBLE);
            m8b.setVisibility(View.VISIBLE);

            m8a.append("\nNázov: " + mGYROSCOPE_UNCALIBRATED.getName() + "\nPredajca: " + mGYROSCOPE_UNCALIBRATED.getVendor() + "\nVerzia: " + mGYROSCOPE_UNCALIBRATED.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT) != null){
            // Success! There's a pressure sensor.
            mHEART_BEAT = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT);
            setListener(mHEART_BEAT);
            m9a.setVisibility(View.VISIBLE);
            m9b.setVisibility(View.VISIBLE);

            m9a.append("\nNázov: " + mHEART_BEAT.getName() + "\nPredajca: " + mHEART_BEAT.getVendor() + "\nVerzia: " + mHEART_BEAT.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null){
            // Success! There's a pressure sensor.
            mHEART_RATE = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
            setListener(mHEART_RATE);
            m10a.setVisibility(View.VISIBLE);
            m10b.setVisibility(View.VISIBLE);

            m10a.append("\nNázov: " + mHEART_RATE.getName() + "\nPredajca: " + mHEART_RATE.getVendor() + "\nVerzia: " + mHEART_RATE.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            // Success! There's a pressure sensor.
            mLIGHT = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            setListener(mLIGHT);
            m11a.setVisibility(View.VISIBLE);
            m11b.setVisibility(View.VISIBLE);

            m11a.append("\nNázov: " + mLIGHT.getName() + "\nPredajca: " + mLIGHT.getVendor() + "\nVerzia: " + mLIGHT.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null){
            // Success! There's a pressure sensor.
            mLINEAR_ACCELERATION = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            setListener(mLINEAR_ACCELERATION);
            m12a.setVisibility(View.VISIBLE);
            m12b.setVisibility(View.VISIBLE);

            m12a.append("\nNázov: " + mLINEAR_ACCELERATION.getName() + "\nPredajca: " + mLINEAR_ACCELERATION.getVendor() + "\nVerzia: " + mLINEAR_ACCELERATION.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            // Success! There's a pressure sensor.
            mMAGNETIC_FIELD = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            setListener(mMAGNETIC_FIELD);
            m13a.setVisibility(View.VISIBLE);
            m13b.setVisibility(View.VISIBLE);

            m13a.append("\nNázov: " + mMAGNETIC_FIELD.getName() + "\nPredajca: " + mMAGNETIC_FIELD.getVendor() + "\nVerzia: " + mMAGNETIC_FIELD.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) != null){
            // Success! There's a pressure sensor.
            mMAGNETIC_FIELD_UNCALIBRATED = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
            setListener(mMAGNETIC_FIELD_UNCALIBRATED);
            m14a.setVisibility(View.VISIBLE);
            m14b.setVisibility(View.VISIBLE);

            m14a.append("\nNázov: " + mMAGNETIC_FIELD_UNCALIBRATED.getName() + "\nPredajca: " + mMAGNETIC_FIELD_UNCALIBRATED.getVendor() + "\nVerzia: " + mMAGNETIC_FIELD_UNCALIBRATED.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT) != null){
            // Success! There's a pressure sensor.
            mMOTION_DETECT = mSensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT);
            setListener(mMOTION_DETECT);
            m15a.setVisibility(View.VISIBLE);
            m15b.setVisibility(View.VISIBLE);

            m15a.append("\nNázov: " + mMOTION_DETECT.getName() + "\nPredajca: " + mMOTION_DETECT.getVendor() + "\nVerzia: " + mMOTION_DETECT.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null){
            // Success! There's a pressure sensor.
            mORIENTATION = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            setListener(mORIENTATION);
            m15aa.setVisibility(View.VISIBLE);
            m15bb.setVisibility(View.VISIBLE);

            m15aa.append("\nNázov: " + mORIENTATION.getName() + "\nPredajca: " + mORIENTATION.getVendor() + "\nVerzia: " + mORIENTATION.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF) != null){
            // Success! There's a pressure sensor.
            mPOSE_6DOF = mSensorManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF);
            setListener(mPOSE_6DOF);
            m16a.setVisibility(View.VISIBLE);
            m16b.setVisibility(View.VISIBLE);

            m16a.append("\nNázov: " + mPOSE_6DOF.getName() + "\nPredajca: " + mPOSE_6DOF.getVendor() + "\nVerzia: " + mPOSE_6DOF.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null){
            // Success! There's a pressure sensor.
            mPRESSURE = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            setListener(mPRESSURE);
            m17a.setVisibility(View.VISIBLE);
            m17b.setVisibility(View.VISIBLE);

            m17a.append("\nNázov: " + mPRESSURE.getName() + "\nPredajca: " + mPRESSURE.getVendor() + "\nVerzia: " + mPRESSURE.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){
            // Success! There's a pressure sensor.
            mPROXIMITY = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            setListener(mPROXIMITY);
            m18a.setVisibility(View.VISIBLE);
            m18b.setVisibility(View.VISIBLE);

            m18a.append("\nNázov: " + mPROXIMITY.getName() + "\nPredajca: " + mPROXIMITY.getVendor() + "\nVerzia: " + mPROXIMITY.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null){
            // Success! There's a pressure sensor.
            mRELATIVE_HUMIDITY = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
            setListener(mRELATIVE_HUMIDITY);
            m19a.setVisibility(View.VISIBLE);
            m19b.setVisibility(View.VISIBLE);

            m19a.append("\nNázov: " + mRELATIVE_HUMIDITY.getName() + "\nPredajca: " + mRELATIVE_HUMIDITY.getVendor() + "\nVerzia: " + mRELATIVE_HUMIDITY.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null){
            // Success! There's a pressure sensor.
            mROTATION_VECTOR = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            setListener(mROTATION_VECTOR);
            m20a.setVisibility(View.VISIBLE);
            m20b.setVisibility(View.VISIBLE);

            m20a.append("\nNázov: " + mROTATION_VECTOR.getName() + "\nPredajca: " + mROTATION_VECTOR.getVendor() + "\nVerzia: " + mROTATION_VECTOR.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION) != null){
            // Success! There's a pressure sensor.
            mSIGNIFICANT_MOTION = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
            setListener(mSIGNIFICANT_MOTION);
            m21a.setVisibility(View.VISIBLE);
            m21b.setVisibility(View.VISIBLE);

            m21a.append("\nNázov: " + mSIGNIFICANT_MOTION.getName() + "\nPredajca: " + mSIGNIFICANT_MOTION.getVendor() + "\nVerzia: " + mSIGNIFICANT_MOTION.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT) != null){
            // Success! There's a pressure sensor.
            mSTATIONARY_DETECT = mSensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT);
            setListener(mSTATIONARY_DETECT);
            m22a.setVisibility(View.VISIBLE);
            m22b.setVisibility(View.VISIBLE);

            m22a.append("\nNázov: " + mSTATIONARY_DETECT.getName() + "\nPredajca: " + mSTATIONARY_DETECT.getVendor() + "\nVerzia: " + mSTATIONARY_DETECT.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            // Success! There's a pressure sensor.
            mSTEP_COUNTER = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            setListener(mSTEP_COUNTER);
            m23a.setVisibility(View.VISIBLE);
            m23b.setVisibility(View.VISIBLE);

            m23a.append("\nNázov: " + mSTEP_COUNTER.getName() + "\nPredajca: " + mSTEP_COUNTER.getVendor() + "\nVerzia: " + mSTEP_COUNTER.getVersion() + "\n");

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            // Success! There's a pressure sensor.
            mSTEP_DETECTOR = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            setListener(mSTEP_DETECTOR);
            m24a.setVisibility(View.VISIBLE);
            m24b.setVisibility(View.VISIBLE);

            m24a.append("\nNázov: " + mSTEP_DETECTOR.getName() + "\nPredajca: " + mSTEP_DETECTOR.getVendor() + "\nVerzia: " + mSTEP_DETECTOR.getVersion() + "\n");

        }

    }


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
            startPhases();
        }
    }
}
