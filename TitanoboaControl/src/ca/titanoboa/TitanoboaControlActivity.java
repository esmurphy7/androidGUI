package ca.titanoboa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import ca.titanoboa.model.module.TitanoboaModule;
import ca.titanoboa.network.PacketReader;
import ca.titanoboa.network.TitanoboaPacketReader;
import ca.titanoboa.packet.*;

/**
 * The base activity of the whole app!
 * 
 * @author Graham
 * 
 */
public class TitanoboaControlActivity extends Activity {

    //=========== Constants =======================
    public static final int NUMBER_OF_MODULES = 5;
    public static final int UPDATE_DELAY = 1; // how often data updates, in ms

    //=========== Attributes =======================
	private ArrayList<TitanoboaModule> modules;
	private PacketReader titanoboaPacketReader;
	private Thread packetReaderThread;
	private boolean packetReaderThreadStarted;
	private Handler uiUpdateHandler;
	private Runnable uiUpdateTask;
	private int selectedModule;
	private boolean screenSizeIsXLarge;
    private boolean isSimulation;
    private Simulation simulation;

    //=========== Views =======================
	private TextView batteryLevelView;
    //TODO: include view for battery image


    //============ Getters & Setters ==============
	public int getSelectedModule() {
		return selectedModule;
	}
	public void setSelectedModule(int selectedModule) {
		this.selectedModule = selectedModule;
	}

    public TextView getBatteryLevelView() {
        return batteryLevelView;
    }
    public void setBatteryLevelView(TextView batteryLevelView) {
        this.batteryLevelView = batteryLevelView;
    }

    //============ Methods =======================
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		screenSizeIsXLarge = getResources().getBoolean(R.bool.screen_xlarge);
        modules = new ArrayList<TitanoboaModule>();
		// initialize modules (i starts at 1 since module constructor cannot handle values of 0 for i
        for(int i=1; i<NUMBER_OF_MODULES; i++)
        {
            TitanoboaModule module = new TitanoboaModule(i);
            modules.add(module);
        }

		// initialize differently for a tablet (xlarge) than a phone
		if (screenSizeIsXLarge) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setSelectedModule(0);
			setupTitanoboaModelXLarge();
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setSelectedModule(1);
			setupTitanoboaModelNormal();
		}

        //receive the intent passed by the configPromptActivity to check if this should be treated as a simulation environment
        Intent intent = getIntent();
        isSimulation = intent.getBooleanExtra(ConfigPromptActivity.IS_SIMULATION, false);

        //run simulation thread if simulation environment has been selected, otherwise run standard reader thread
        if(isSimulation){
            simulation = new Simulation();
            simulation.start();
            uiUpdateHandler = new Handler();
            uiUpdateTask = new UIUpdateTask();
            uiUpdateHandler.removeCallbacks(uiUpdateTask);
            uiUpdateHandler.post(uiUpdateTask);
        }else {
            titanoboaPacketReader = new TitanoboaPacketReader();
            // Packet reader is started via Connect button
            packetReaderThreadStarted = false;

            uiUpdateHandler = new Handler();
            uiUpdateTask = new UIUpdateTask();

            Button connectButton = ((Button) findViewById(R.id.connectButton));
            connectButton.setOnClickListener(new ConnectButtonOnClickListener());
        }

		if (!screenSizeIsXLarge) {
			//TODO: init listview dynamically and attach listener, see http://www.mkyong.com/android/android-spinner-drop-down-list-example/
		}
	}


	/**
	 * Set up model with modules, vertebrae, and actuators. Normal version for
	 * phones.
	 */
	private void setupTitanoboaModelNormal() {

        // initialize modules (i starts at 1 since module constructor cannot handle values of 0 for i
        for(int i=1; i<NUMBER_OF_MODULES; i++)
        {
            TitanoboaModule module = new TitanoboaModule(i);
            modules.add(module);
        }

        setBatteryLevelView((TextView) findViewById(R.id.battery_level));
        //module.setMotorSpeedView((TextView) findViewById(R.id.motor_speed));
        //module.setPressureSensorView((TextView) findViewById(R.id.pressure));

        // TODO: My view IDs are zero-based but the vertebra ID numbers are 1..n, which should be fixed.
        // TODO: Kill the setVertebraeViews thing and just set the views directly. It doesn't really gain anything and it makes the code less clear.
        // TODO: The old "current angle" row is now "sensor value" so these layout IDs should be updated to match.
    }

	/**
	 * Set up model with modules, vertebrae, and actuators. XLarge version for
	 * tablets.
	 */

	private void setupTitanoboaModelXLarge() {

		 //This is less nasty than the last version, but I still don't like it. The problem is there's no good way to
		 //dynamically generate the part after R.id. - there's a findViewByName but its efficiency apparently sucks.
		 //Most of the improvement from the last version is from having made the model automatically set up its modules
		// which in turn set up their vertebrae, and from eliminating the actuator objects.

        /*
		titanoboaModel = new TitanoboaModel();

		Module module1 = titanoboaModel.getModules().get(0);

		module1.setBatteryLevelView((TextView) findViewById(R.id.m1_battery_level));
		module1.setMotorSpeedView((TextView) findViewById(R.id.m1_motor_speed));
        module1.setPressureSensorView((TextView) findViewById(R.id.m1_pressure));

        List<List<TextView>> module1VertebraeViews = new ArrayList<List<TextView>>();

        List<TextView> module1Vertebra0Views = new ArrayList<TextView>();        
        module1Vertebra0Views.add((TextView) findViewById(R.id.m1_v0_h_setpoint_angle));
        module1Vertebra0Views.add((TextView) findViewById(R.id.m1_v0_h_current_angle));
        module1Vertebra0Views.add((TextView) findViewById(R.id.m1_v0_h_sensor_calibration_high));
        module1Vertebra0Views.add((TextView) findViewById(R.id.m1_v0_h_sensor_calibration_low));
        module1Vertebra0Views.add((TextView) findViewById(R.id.m1_v0_v_setpoint_angle));
        module1Vertebra0Views.add((TextView) findViewById(R.id.m1_v0_v_current_angle));
        module1Vertebra0Views.add((TextView) findViewById(R.id.m1_v0_v_sensor_calibration_high));
        module1Vertebra0Views.add((TextView) findViewById(R.id.m1_v0_v_sensor_calibration_low));
        module1VertebraeViews.add(module1Vertebra0Views);

        List<TextView> module1Vertebra1Views = new ArrayList<TextView>();
        module1Vertebra1Views.add((TextView) findViewById(R.id.m1_v1_h_setpoint_angle));
        module1Vertebra1Views.add((TextView) findViewById(R.id.m1_v1_h_current_angle));
        module1Vertebra1Views.add((TextView) findViewById(R.id.m1_v1_h_sensor_calibration_high));
        module1Vertebra1Views.add((TextView) findViewById(R.id.m1_v1_h_sensor_calibration_low));
        module1Vertebra1Views.add((TextView) findViewById(R.id.m1_v1_v_setpoint_angle));
        module1Vertebra1Views.add((TextView) findViewById(R.id.m1_v1_v_current_angle));
        module1Vertebra1Views.add((TextView) findViewById(R.id.m1_v1_v_sensor_calibration_high));
        module1Vertebra1Views.add((TextView) findViewById(R.id.m1_v1_v_sensor_calibration_low));
        module1VertebraeViews.add(module1Vertebra1Views);

        List<TextView> module1Vertebra2Views = new ArrayList<TextView>();
        module1Vertebra2Views.add((TextView) findViewById(R.id.m1_v2_h_setpoint_angle));
        module1Vertebra2Views.add((TextView) findViewById(R.id.m1_v2_h_current_angle));
        module1Vertebra2Views.add((TextView) findViewById(R.id.m1_v2_h_sensor_calibration_high));
        module1Vertebra2Views.add((TextView) findViewById(R.id.m1_v2_h_sensor_calibration_low));
        module1Vertebra2Views.add((TextView) findViewById(R.id.m1_v2_v_setpoint_angle));
        module1Vertebra2Views.add((TextView) findViewById(R.id.m1_v2_v_current_angle));
        module1Vertebra2Views.add((TextView) findViewById(R.id.m1_v2_v_sensor_calibration_high));
        module1Vertebra2Views.add((TextView) findViewById(R.id.m1_v2_v_sensor_calibration_low));
        module1VertebraeViews.add(module1Vertebra2Views);

        List<TextView> module1Vertebra3Views = new ArrayList<TextView>();
        module1Vertebra3Views.add((TextView) findViewById(R.id.m1_v3_h_setpoint_angle));
        module1Vertebra3Views.add((TextView) findViewById(R.id.m1_v3_h_current_angle));
        module1Vertebra3Views.add((TextView) findViewById(R.id.m1_v3_h_sensor_calibration_high));
        module1Vertebra3Views.add((TextView) findViewById(R.id.m1_v3_h_sensor_calibration_low));
        module1Vertebra3Views.add((TextView) findViewById(R.id.m1_v3_v_setpoint_angle));
        module1Vertebra3Views.add((TextView) findViewById(R.id.m1_v3_v_current_angle));
        module1Vertebra3Views.add((TextView) findViewById(R.id.m1_v3_v_sensor_calibration_high));
        module1Vertebra3Views.add((TextView) findViewById(R.id.m1_v3_v_sensor_calibration_low));
        module1VertebraeViews.add(module1Vertebra3Views);

        List<TextView> module1Vertebra4Views = new ArrayList<TextView>();
        module1Vertebra4Views.add((TextView) findViewById(R.id.m1_v4_h_setpoint_angle));
        module1Vertebra4Views.add((TextView) findViewById(R.id.m1_v4_h_current_angle));
        module1Vertebra4Views.add((TextView) findViewById(R.id.m1_v4_h_sensor_calibration_high));
        module1Vertebra4Views.add((TextView) findViewById(R.id.m1_v4_h_sensor_calibration_low));
        module1Vertebra4Views.add((TextView) findViewById(R.id.m1_v4_v_setpoint_angle));
        module1Vertebra4Views.add((TextView) findViewById(R.id.m1_v4_v_current_angle));
        module1Vertebra4Views.add((TextView) findViewById(R.id.m1_v4_v_sensor_calibration_high));
        module1Vertebra4Views.add((TextView) findViewById(R.id.m1_v4_v_sensor_calibration_low));
        module1VertebraeViews.add(module1Vertebra4Views);

        module1.setVertebraeViews(module1VertebraeViews);

        Module module2 = titanoboaModel.getModules().get(1);

        module2.setBatteryLevelView((TextView) findViewById(R.id.m2_battery_level));
        module2.setMotorSpeedView((TextView) findViewById(R.id.m2_motor_speed));
        module2.setPressureSensorView((TextView) findViewById(R.id.m2_pressure));

        List<List<TextView>> module2VertebraeViews = new ArrayList<List<TextView>>();

        List<TextView> module2Vertebra0Views = new ArrayList<TextView>();
        module2Vertebra0Views.add((TextView) findViewById(R.id.m2_v0_h_setpoint_angle));
        module2Vertebra0Views.add((TextView) findViewById(R.id.m2_v0_h_current_angle));
        module2Vertebra0Views.add((TextView) findViewById(R.id.m2_v0_h_sensor_calibration_high));
        module2Vertebra0Views.add((TextView) findViewById(R.id.m2_v0_h_sensor_calibration_low));
        module2Vertebra0Views.add((TextView) findViewById(R.id.m2_v0_v_setpoint_angle));
        module2Vertebra0Views.add((TextView) findViewById(R.id.m2_v0_v_current_angle));
        module2Vertebra0Views.add((TextView) findViewById(R.id.m2_v0_v_sensor_calibration_high));
        module2Vertebra0Views.add((TextView) findViewById(R.id.m2_v0_v_sensor_calibration_low));
        module2VertebraeViews.add(module2Vertebra0Views);

        List<TextView> module2Vertebra1Views = new ArrayList<TextView>();
        module2Vertebra1Views.add((TextView) findViewById(R.id.m2_v1_h_setpoint_angle));
        module2Vertebra1Views.add((TextView) findViewById(R.id.m2_v1_h_current_angle));
        module2Vertebra1Views.add((TextView) findViewById(R.id.m2_v1_h_sensor_calibration_high));
        module2Vertebra1Views.add((TextView) findViewById(R.id.m2_v1_h_sensor_calibration_low));
        module2Vertebra1Views.add((TextView) findViewById(R.id.m2_v1_v_setpoint_angle));
        module2Vertebra1Views.add((TextView) findViewById(R.id.m2_v1_v_current_angle));
        module2Vertebra1Views.add((TextView) findViewById(R.id.m2_v1_v_sensor_calibration_high));
        module2Vertebra1Views.add((TextView) findViewById(R.id.m2_v1_v_sensor_calibration_low));
        module2VertebraeViews.add(module2Vertebra1Views);

        List<TextView> module2Vertebra2Views = new ArrayList<TextView>();
        module2Vertebra2Views.add((TextView) findViewById(R.id.m2_v2_h_setpoint_angle));
        module2Vertebra2Views.add((TextView) findViewById(R.id.m2_v2_h_current_angle));
        module2Vertebra2Views.add((TextView) findViewById(R.id.m2_v2_h_sensor_calibration_high));
        module2Vertebra2Views.add((TextView) findViewById(R.id.m2_v2_h_sensor_calibration_low));
        module2Vertebra2Views.add((TextView) findViewById(R.id.m2_v2_v_setpoint_angle));
        module2Vertebra2Views.add((TextView) findViewById(R.id.m2_v2_v_current_angle));
        module2Vertebra2Views.add((TextView) findViewById(R.id.m2_v2_v_sensor_calibration_high));
        module2Vertebra2Views.add((TextView) findViewById(R.id.m2_v2_v_sensor_calibration_low));
        module2VertebraeViews.add(module2Vertebra2Views);

        List<TextView> module2Vertebra3Views = new ArrayList<TextView>();
        module2Vertebra3Views.add((TextView) findViewById(R.id.m2_v3_h_setpoint_angle));
        module2Vertebra3Views.add((TextView) findViewById(R.id.m2_v3_h_current_angle));
        module2Vertebra3Views.add((TextView) findViewById(R.id.m2_v3_h_sensor_calibration_high));
        module2Vertebra3Views.add((TextView) findViewById(R.id.m2_v3_h_sensor_calibration_low));
        module2Vertebra3Views.add((TextView) findViewById(R.id.m2_v3_v_setpoint_angle));
        module2Vertebra3Views.add((TextView) findViewById(R.id.m2_v3_v_current_angle));
        module2Vertebra3Views.add((TextView) findViewById(R.id.m2_v3_v_sensor_calibration_high));
        module2Vertebra3Views.add((TextView) findViewById(R.id.m2_v3_v_sensor_calibration_low));
        module2VertebraeViews.add(module2Vertebra3Views);

        List<TextView> module2Vertebra4Views = new ArrayList<TextView>();
        module2Vertebra4Views.add((TextView) findViewById(R.id.m2_v4_h_setpoint_angle));
        module2Vertebra4Views.add((TextView) findViewById(R.id.m2_v4_h_current_angle));
        module2Vertebra4Views.add((TextView) findViewById(R.id.m2_v4_h_sensor_calibration_high));
        module2Vertebra4Views.add((TextView) findViewById(R.id.m2_v4_h_sensor_calibration_low));
        module2Vertebra4Views.add((TextView) findViewById(R.id.m2_v4_v_setpoint_angle));
        module2Vertebra4Views.add((TextView) findViewById(R.id.m2_v4_v_current_angle));
        module2Vertebra4Views.add((TextView) findViewById(R.id.m2_v4_v_sensor_calibration_high));
        module2Vertebra4Views.add((TextView) findViewById(R.id.m2_v4_v_sensor_calibration_low));
        module2VertebraeViews.add(module2Vertebra4Views);

        module2.setVertebraeViews(module2VertebraeViews);

        Module module3 = titanoboaModel.getModules().get(2);

        module3.setBatteryLevelView((TextView) findViewById(R.id.m3_battery_level));
        module3.setMotorSpeedView((TextView) findViewById(R.id.m3_motor_speed));
        module3.setPressureSensorView((TextView) findViewById(R.id.m3_pressure));

        List<List<TextView>> module3VertebraeViews = new ArrayList<List<TextView>>();

        List<TextView> module3Vertebra0Views = new ArrayList<TextView>();
        module3Vertebra0Views.add((TextView) findViewById(R.id.m3_v0_h_setpoint_angle));
        module3Vertebra0Views.add((TextView) findViewById(R.id.m3_v0_h_current_angle));
        module3Vertebra0Views.add((TextView) findViewById(R.id.m3_v0_h_sensor_calibration_high));
        module3Vertebra0Views.add((TextView) findViewById(R.id.m3_v0_h_sensor_calibration_low));
        module3Vertebra0Views.add((TextView) findViewById(R.id.m3_v0_v_setpoint_angle));
        module3Vertebra0Views.add((TextView) findViewById(R.id.m3_v0_v_current_angle));
        module3Vertebra0Views.add((TextView) findViewById(R.id.m3_v0_v_sensor_calibration_high));
        module3Vertebra0Views.add((TextView) findViewById(R.id.m3_v0_v_sensor_calibration_low));
        module3VertebraeViews.add(module3Vertebra0Views);

        List<TextView> module3Vertebra1Views = new ArrayList<TextView>();
        module3Vertebra1Views.add((TextView) findViewById(R.id.m3_v1_h_setpoint_angle));
        module3Vertebra1Views.add((TextView) findViewById(R.id.m3_v1_h_current_angle));
        module3Vertebra1Views.add((TextView) findViewById(R.id.m3_v1_h_sensor_calibration_high));
        module3Vertebra1Views.add((TextView) findViewById(R.id.m3_v1_h_sensor_calibration_low));
        module3Vertebra1Views.add((TextView) findViewById(R.id.m3_v1_v_setpoint_angle));
        module3Vertebra1Views.add((TextView) findViewById(R.id.m3_v1_v_current_angle));
        module3Vertebra1Views.add((TextView) findViewById(R.id.m3_v1_v_sensor_calibration_high));
        module3Vertebra1Views.add((TextView) findViewById(R.id.m3_v1_v_sensor_calibration_low));
        module3VertebraeViews.add(module3Vertebra1Views);

        List<TextView> module3Vertebra2Views = new ArrayList<TextView>();
        module3Vertebra2Views.add((TextView) findViewById(R.id.m3_v2_h_setpoint_angle));
        module3Vertebra2Views.add((TextView) findViewById(R.id.m3_v2_h_current_angle));
        module3Vertebra2Views.add((TextView) findViewById(R.id.m3_v2_h_sensor_calibration_high));
        module3Vertebra2Views.add((TextView) findViewById(R.id.m3_v2_h_sensor_calibration_low));
        module3Vertebra2Views.add((TextView) findViewById(R.id.m3_v2_v_setpoint_angle));
        module3Vertebra2Views.add((TextView) findViewById(R.id.m3_v2_v_current_angle));
        module3Vertebra2Views.add((TextView) findViewById(R.id.m3_v2_v_sensor_calibration_high));
        module3Vertebra2Views.add((TextView) findViewById(R.id.m3_v2_v_sensor_calibration_low));
        module3VertebraeViews.add(module3Vertebra2Views);

        List<TextView> module3Vertebra3Views = new ArrayList<TextView>();
        module3Vertebra3Views.add((TextView) findViewById(R.id.m3_v3_h_setpoint_angle));
        module3Vertebra3Views.add((TextView) findViewById(R.id.m3_v3_h_current_angle));
        module3Vertebra3Views.add((TextView) findViewById(R.id.m3_v3_h_sensor_calibration_high));
        module3Vertebra3Views.add((TextView) findViewById(R.id.m3_v3_h_sensor_calibration_low));
        module3Vertebra3Views.add((TextView) findViewById(R.id.m3_v3_v_setpoint_angle));
        module3Vertebra3Views.add((TextView) findViewById(R.id.m3_v3_v_current_angle));
        module3Vertebra3Views.add((TextView) findViewById(R.id.m3_v3_v_sensor_calibration_high));
        module3Vertebra3Views.add((TextView) findViewById(R.id.m3_v3_v_sensor_calibration_low));
        module3VertebraeViews.add(module3Vertebra3Views);

        List<TextView> module3Vertebra4Views = new ArrayList<TextView>();
        module3Vertebra4Views.add((TextView) findViewById(R.id.m3_v4_h_setpoint_angle));
        module3Vertebra4Views.add((TextView) findViewById(R.id.m3_v4_h_current_angle));
        module3Vertebra4Views.add((TextView) findViewById(R.id.m3_v4_h_sensor_calibration_high));
        module3Vertebra4Views.add((TextView) findViewById(R.id.m3_v4_h_sensor_calibration_low));
        module3Vertebra4Views.add((TextView) findViewById(R.id.m3_v4_v_setpoint_angle));
        module3Vertebra4Views.add((TextView) findViewById(R.id.m3_v4_v_current_angle));
        module3Vertebra4Views.add((TextView) findViewById(R.id.m3_v4_v_sensor_calibration_high));
        module3Vertebra4Views.add((TextView) findViewById(R.id.m3_v4_v_sensor_calibration_low));
        module3VertebraeViews.add(module3Vertebra4Views);

        module3.setVertebraeViews(module3VertebraeViews);

        Module module4 = titanoboaModel.getModules().get(3);

        module4.setBatteryLevelView((TextView) findViewById(R.id.m4_battery_level));
        module4.setMotorSpeedView((TextView) findViewById(R.id.m4_motor_speed));
        module4.setPressureSensorView((TextView) findViewById(R.id.m4_pressure));

        List<List<TextView>> module4VertebraeViews = new ArrayList<List<TextView>>();

        List<TextView> module4Vertebra0Views = new ArrayList<TextView>();
        module4Vertebra0Views.add((TextView) findViewById(R.id.m4_v0_h_setpoint_angle));
        module4Vertebra0Views.add((TextView) findViewById(R.id.m4_v0_h_current_angle));
        module4Vertebra0Views.add((TextView) findViewById(R.id.m4_v0_h_sensor_calibration_high));
        module4Vertebra0Views.add((TextView) findViewById(R.id.m4_v0_h_sensor_calibration_low));
        module4Vertebra0Views.add((TextView) findViewById(R.id.m4_v0_v_setpoint_angle));
        module4Vertebra0Views.add((TextView) findViewById(R.id.m4_v0_v_current_angle));
        module4Vertebra0Views.add((TextView) findViewById(R.id.m4_v0_v_sensor_calibration_high));
        module4Vertebra0Views.add((TextView) findViewById(R.id.m4_v0_v_sensor_calibration_low));
        module4VertebraeViews.add(module4Vertebra0Views);

        List<TextView> module4Vertebra1Views = new ArrayList<TextView>();
        module4Vertebra1Views.add((TextView) findViewById(R.id.m4_v1_h_setpoint_angle));
        module4Vertebra1Views.add((TextView) findViewById(R.id.m4_v1_h_current_angle));
        module4Vertebra1Views.add((TextView) findViewById(R.id.m4_v1_h_sensor_calibration_high));
        module4Vertebra1Views.add((TextView) findViewById(R.id.m4_v1_h_sensor_calibration_low));
        module4Vertebra1Views.add((TextView) findViewById(R.id.m4_v1_v_setpoint_angle));
        module4Vertebra1Views.add((TextView) findViewById(R.id.m4_v1_v_current_angle));
        module4Vertebra1Views.add((TextView) findViewById(R.id.m4_v1_v_sensor_calibration_high));
        module4Vertebra1Views.add((TextView) findViewById(R.id.m4_v1_v_sensor_calibration_low));
        module4VertebraeViews.add(module4Vertebra1Views);

        List<TextView> module4Vertebra2Views = new ArrayList<TextView>();
        module4Vertebra2Views.add((TextView) findViewById(R.id.m4_v2_h_setpoint_angle));
        module4Vertebra2Views.add((TextView) findViewById(R.id.m4_v2_h_current_angle));
        module4Vertebra2Views.add((TextView) findViewById(R.id.m4_v2_h_sensor_calibration_high));
        module4Vertebra2Views.add((TextView) findViewById(R.id.m4_v2_h_sensor_calibration_low));
        module4Vertebra2Views.add((TextView) findViewById(R.id.m4_v2_v_setpoint_angle));
        module4Vertebra2Views.add((TextView) findViewById(R.id.m4_v2_v_current_angle));
        module4Vertebra2Views.add((TextView) findViewById(R.id.m4_v2_v_sensor_calibration_high));
        module4Vertebra2Views.add((TextView) findViewById(R.id.m4_v2_v_sensor_calibration_low));
        module4VertebraeViews.add(module4Vertebra2Views);

        List<TextView> module4Vertebra3Views = new ArrayList<TextView>();
        module4Vertebra3Views.add((TextView) findViewById(R.id.m4_v3_h_setpoint_angle));
        module4Vertebra3Views.add((TextView) findViewById(R.id.m4_v3_h_current_angle));
        module4Vertebra3Views.add((TextView) findViewById(R.id.m4_v3_h_sensor_calibration_high));
        module4Vertebra3Views.add((TextView) findViewById(R.id.m4_v3_h_sensor_calibration_low));
        module4Vertebra3Views.add((TextView) findViewById(R.id.m4_v3_v_setpoint_angle));
        module4Vertebra3Views.add((TextView) findViewById(R.id.m4_v3_v_current_angle));
        module4Vertebra3Views.add((TextView) findViewById(R.id.m4_v3_v_sensor_calibration_high));
        module4Vertebra3Views.add((TextView) findViewById(R.id.m4_v3_v_sensor_calibration_low));
        module4VertebraeViews.add(module4Vertebra3Views);

        List<TextView> module4Vertebra4Views = new ArrayList<TextView>();
        module4Vertebra4Views.add((TextView) findViewById(R.id.m4_v4_h_setpoint_angle));
        module4Vertebra4Views.add((TextView) findViewById(R.id.m4_v4_h_current_angle));
        module4Vertebra4Views.add((TextView) findViewById(R.id.m4_v4_h_sensor_calibration_high));
        module4Vertebra4Views.add((TextView) findViewById(R.id.m4_v4_h_sensor_calibration_low));
        module4Vertebra4Views.add((TextView) findViewById(R.id.m4_v4_v_setpoint_angle));
        module4Vertebra4Views.add((TextView) findViewById(R.id.m4_v4_v_current_angle));
        module4Vertebra4Views.add((TextView) findViewById(R.id.m4_v4_v_sensor_calibration_high));
        module4Vertebra4Views.add((TextView) findViewById(R.id.m4_v4_v_sensor_calibration_low));
        module4VertebraeViews.add(module4Vertebra4Views);

        module4.setVertebraeViews(module4VertebraeViews);
        */
	}


    // TODO: Move these classes to their own files? Would require some reworking
	// since they use some Activity methods.

	/**
	 * A Runnable implementation for updating the UI every UPDATE_DELAY ms.
	 */
	private final class UIUpdateTask implements Runnable {
		@Override
		public void run() {
            Map<String, Packet> packets = new HashMap<String, Packet>();
            if(isSimulation){
                packets = simulation.getPackets();
            }else {
                packets = titanoboaPacketReader.getPackets();
            }

            if ((packets != null) && (!packets.isEmpty())) {
                if (screenSizeIsXLarge) {
                    //titanoboaModel.updateDataAll(packets);
                } else {
                    updateBatteryLevels(packets);
                    updateBatteryLevelViews();
                    //TODO: update battery image view
                }
            }

			uiUpdateHandler.postAtTime(this, SystemClock.uptimeMillis()
					+ UPDATE_DELAY);
		}
	}

	/**
	 * Listener for the connect button. Starts and stops the packet reader and
	 * UI updater, and toggles the button label between Connect and Disconnect.
	 */
	private final class ConnectButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// toggle packet reader/UI updater state and button label
			if (!packetReaderThreadStarted) {
				titanoboaPacketReader.setPort(Integer
						.parseInt(((EditText) findViewById(R.id.portEditText))
								.getText().toString()));

				// start packet reader
				packetReaderThread = new Thread(titanoboaPacketReader);
				packetReaderThreadStarted = true;
				packetReaderThread.start();

				// start UI updater
				uiUpdateHandler.removeCallbacks(uiUpdateTask);
				uiUpdateHandler.post(uiUpdateTask);

				// toggle button label to Disconnect
				Button connectButton = ((Button) v);
				connectButton.setText(getResources().getString(
						R.string.disconnect_button_label));

			} else {
				// stop packet reader
				packetReaderThreadStarted = false;
				packetReaderThread.interrupt();

				// stop UI updater
				uiUpdateHandler.removeCallbacks(uiUpdateTask);

				// toggle button label to Connect
				Button connectButton = ((Button) v);
				connectButton.setText(getResources().getString(
						R.string.connect_button_label));
			}

		}
	}

	/**
	 * On item selected listener for module spinner. Only used for phone version of app
	 * so far.
	 *
	 * @author Graham
	 *
	 */
	private final class moduleSpinnerItemSelected implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            updateBatteryLevels(titanoboaPacketReader.getPackets());
            updateBatteryLevelViews();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            updateBatteryLevels(titanoboaPacketReader.getPackets());
            updateBatteryLevelViews();
        }
    }
    /**
    Update battery levels of all modules
     */
    public void updateBatteryLevels(Map<String, Packet> packets){
        for(TitanoboaModule module : modules){
            module.updateData(packets);
        }
    }
    /**
    Update the batteryLevelView based on the currently selected module
     */
    public void updateBatteryLevelViews(){
        //TitanoboaModule selectedModule = modules.get(getSelectedModule()-1);
        //batteryLevelView.setText(Integer.toString(selectedModule.getBatteryLevel()));
        ListView listView = (ListView) findViewById(R.id.moduleList);
        //for every module in the listView, set its text to the corresponding module's battery level
    }

}