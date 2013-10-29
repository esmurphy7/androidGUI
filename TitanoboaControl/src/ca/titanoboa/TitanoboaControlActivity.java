package ca.titanoboa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TextView;
import ca.titanoboa.model.Model;
import ca.titanoboa.model.TitanoboaModel;
import ca.titanoboa.views.ModelViews;
import ca.titanoboa.views.TitanoboaModelViews;
import ca.titanoboa.views.module.ModuleViews;
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

    private Model titanoboaModel;
    private ModelViews titanoboaModelViews;
    private PacketReader titanoboaPacketReader;
    private Thread packetReaderThread;
    private boolean packetReaderThreadStarted;
    private Handler uiUpdateHandler;
    private Runnable uiUpdateTask;
    private int selectedModule;
    private boolean screenSizeIsXLarge;

    // how often data updates, in ms
    public static final int UPDATE_DELAY = 1;

    /**
     * Get the selected module.
     *
     * @return the selected module
     */
    public int getSelectedModule() {
        return selectedModule;
    }

    /**
     * Set the selected module.
     *
     * @param selectedModule the selected module to set
     */
    public void setSelectedModule(int selectedModule) {
        this.selectedModule = selectedModule;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        screenSizeIsXLarge = getResources().getBoolean(R.bool.screen_xlarge);

        titanoboaModel = new TitanoboaModel();

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

        titanoboaPacketReader = new TitanoboaPacketReader();
        // Packet reader is started via Connect button
        packetReaderThreadStarted = false;

        uiUpdateHandler = new Handler();
        uiUpdateTask = new UIUpdateTask();

        if (!screenSizeIsXLarge) {
            OnClickListener moduleButtonsOnClickListener = new ModuleButtonsOnClickListener();
            RadioButton module1Radio = ((RadioButton) findViewById(R.id.module1Radio));
            module1Radio.setOnClickListener(moduleButtonsOnClickListener);
            module1Radio.setSelected(true);
            RadioButton module2Radio = ((RadioButton) findViewById(R.id.module2Radio));
            module2Radio.setOnClickListener(moduleButtonsOnClickListener);
            RadioButton module3Radio = ((RadioButton) findViewById(R.id.module3Radio));
            module3Radio.setOnClickListener(moduleButtonsOnClickListener);
            RadioButton module4Radio = ((RadioButton) findViewById(R.id.module4Radio));
            module4Radio.setOnClickListener(moduleButtonsOnClickListener);
        }

        startListening();
    }

    /**
     * Set up model with modules, vertebrae, and actuators. Normal version for
     * phones.
     */
    private void setupTitanoboaModelNormal() {

        titanoboaModelViews = new TitanoboaModelViews(titanoboaModel);

        for (ModuleViews moduleViews : titanoboaModelViews.getModuleViewses()) {
            moduleViews.setBatteryLevelView((TextView) findViewById(R.id.battery_level));
            moduleViews.setMotorSpeedView((TextView) findViewById(R.id.motor_speed));
            moduleViews.setPressureSensorView((TextView) findViewById(R.id.pressure));

            // TODO: My view IDs are zero-based but the vertebra ID numbers are 1..n, which should be fixed.
            // TODO: Kill the setVertebraeViews thing and just set the views directly. It doesn't really gain anything and it makes the code less clear.
            // TODO: The old "current angle" row is now "sensor value" so these layout IDs should be updated to match.
            List<List<TextView>> vertebraeViews = new ArrayList<List<TextView>>();

            List<TextView> vertebra0Views = new ArrayList<TextView>();
            vertebra0Views.add((TextView) findViewById(R.id.v0_h_setpoint_angle));
            vertebra0Views.add((TextView) findViewById(R.id.v0_h_current_angle));
            vertebra0Views.add((TextView) findViewById(R.id.v0_h_sensor_calibration_high));
            vertebra0Views.add((TextView) findViewById(R.id.v0_h_sensor_calibration_low));
            vertebra0Views.add((TextView) findViewById(R.id.v0_v_setpoint_angle));
            vertebra0Views.add((TextView) findViewById(R.id.v0_v_current_angle));
            vertebra0Views.add((TextView) findViewById(R.id.v0_v_sensor_calibration_high));
            vertebra0Views.add((TextView) findViewById(R.id.v0_v_sensor_calibration_low));
            vertebraeViews.add(vertebra0Views);

            List<TextView> vertebra1Views = new ArrayList<TextView>();
            vertebra1Views.add((TextView) findViewById(R.id.v1_h_setpoint_angle));
            vertebra1Views.add((TextView) findViewById(R.id.v1_h_current_angle));
            vertebra1Views.add((TextView) findViewById(R.id.v1_h_sensor_calibration_high));
            vertebra1Views.add((TextView) findViewById(R.id.v1_h_sensor_calibration_low));
            vertebra1Views.add((TextView) findViewById(R.id.v1_v_setpoint_angle));
            vertebra1Views.add((TextView) findViewById(R.id.v1_v_current_angle));
            vertebra1Views.add((TextView) findViewById(R.id.v1_v_sensor_calibration_high));
            vertebra1Views.add((TextView) findViewById(R.id.v1_v_sensor_calibration_low));
            vertebraeViews.add(vertebra1Views);

            List<TextView> vertebra2Views = new ArrayList<TextView>();
            vertebra2Views.add((TextView) findViewById(R.id.v2_h_setpoint_angle));
            vertebra2Views.add((TextView) findViewById(R.id.v2_h_current_angle));
            vertebra2Views.add((TextView) findViewById(R.id.v2_h_sensor_calibration_high));
            vertebra2Views.add((TextView) findViewById(R.id.v2_h_sensor_calibration_low));
            vertebra2Views.add((TextView) findViewById(R.id.v2_v_setpoint_angle));
            vertebra2Views.add((TextView) findViewById(R.id.v2_v_current_angle));
            vertebra2Views.add((TextView) findViewById(R.id.v2_v_sensor_calibration_high));
            vertebra2Views.add((TextView) findViewById(R.id.v2_v_sensor_calibration_low));
            vertebraeViews.add(vertebra2Views);

            List<TextView> vertebra3Views = new ArrayList<TextView>();
            vertebra3Views.add((TextView) findViewById(R.id.v3_h_setpoint_angle));
            vertebra3Views.add((TextView) findViewById(R.id.v3_h_current_angle));
            vertebra3Views.add((TextView) findViewById(R.id.v3_h_sensor_calibration_high));
            vertebra3Views.add((TextView) findViewById(R.id.v3_h_sensor_calibration_low));
            vertebra3Views.add((TextView) findViewById(R.id.v3_v_setpoint_angle));
            vertebra3Views.add((TextView) findViewById(R.id.v3_v_current_angle));
            vertebra3Views.add((TextView) findViewById(R.id.v3_v_sensor_calibration_high));
            vertebra3Views.add((TextView) findViewById(R.id.v3_v_sensor_calibration_low));
            vertebraeViews.add(vertebra3Views);

            List<TextView> vertebra4Views = new ArrayList<TextView>();
            vertebra4Views.add((TextView) findViewById(R.id.v4_h_setpoint_angle));
            vertebra4Views.add((TextView) findViewById(R.id.v4_h_current_angle));
            vertebra4Views.add((TextView) findViewById(R.id.v4_h_sensor_calibration_high));
            vertebra4Views.add((TextView) findViewById(R.id.v4_h_sensor_calibration_low));
            vertebra4Views.add((TextView) findViewById(R.id.v4_v_setpoint_angle));
            vertebra4Views.add((TextView) findViewById(R.id.v4_v_current_angle));
            vertebra4Views.add((TextView) findViewById(R.id.v4_v_sensor_calibration_high));
            vertebra4Views.add((TextView) findViewById(R.id.v4_v_sensor_calibration_low));
            vertebraeViews.add(vertebra4Views);

            moduleViews.setVertebraViews(vertebraeViews);
        }
    }

    /**
     * Set up model with modules, vertebrae, and actuators. XLarge version for
     * tablets.
     */
    private void setupTitanoboaModelXLarge() {
        /*
		 * This is less nasty than the last version, but I still don't like it. The problem is there's no good way to
		 * dynamically generate the part after R.id. - there's a findViewByName but its efficiency apparently sucks.
		 * Most of the improvement from the last version is from having made the model automatically set up its modules
		 * which in turn set up their vertebrae, and from eliminating the actuator objects.
		 */

        titanoboaModelViews = new TitanoboaModelViews(titanoboaModel);

        ModuleViews moduleViews1 = titanoboaModelViews.getModuleViewses().get(0);

        moduleViews1.setBatteryLevelView((TextView) findViewById(R.id.m1_battery_level));
        moduleViews1.setMotorSpeedView((TextView) findViewById(R.id.m1_motor_speed));
        moduleViews1.setPressureSensorView((TextView) findViewById(R.id.m1_pressure));

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

        moduleViews1.setVertebraViews(module1VertebraeViews);

        ModuleViews moduleViews2 = titanoboaModelViews.getModuleViewses().get(1);

        moduleViews2.setBatteryLevelView((TextView) findViewById(R.id.m2_battery_level));
        moduleViews2.setMotorSpeedView((TextView) findViewById(R.id.m2_motor_speed));
        moduleViews2.setPressureSensorView((TextView) findViewById(R.id.m2_pressure));

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

        moduleViews2.setVertebraViews(module2VertebraeViews);

        ModuleViews moduleViews3 = titanoboaModelViews.getModuleViewses().get(2);

        moduleViews3.setBatteryLevelView((TextView) findViewById(R.id.m3_battery_level));
        moduleViews3.setMotorSpeedView((TextView) findViewById(R.id.m3_motor_speed));
        moduleViews3.setPressureSensorView((TextView) findViewById(R.id.m3_pressure));

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

        moduleViews3.setVertebraViews(module3VertebraeViews);

        ModuleViews moduleViews4 = titanoboaModelViews.getModuleViewses().get(3);

        moduleViews4.setBatteryLevelView((TextView) findViewById(R.id.m4_battery_level));
        moduleViews4.setMotorSpeedView((TextView) findViewById(R.id.m4_motor_speed));
        moduleViews4.setPressureSensorView((TextView) findViewById(R.id.m4_pressure));

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

        moduleViews4.setVertebraViews(module4VertebraeViews);
    }

    // TODO: Move these classes to their own files? Would require some reworking
    // since they use some Activity methods.

    /**
     * A Runnable implementation for updating the UI every UPDATE_DELAY ms.
     */
    private final class UIUpdateTask implements Runnable {
        @Override
        public void run() {
            Map<String, Packet> packets = titanoboaPacketReader.getPackets();

            if ((packets != null) && (!packets.isEmpty())) {
                if (screenSizeIsXLarge) {
                    titanoboaModel.updateDataAll(packets);
                    titanoboaModelViews.updateViewsAll();
                } else {
                    titanoboaModel.updateDataSelected(getSelectedModule(), packets);
                    titanoboaModelViews.updateViewsSelected(getSelectedModule());
                }
            }

            uiUpdateHandler.postAtTime(this, SystemClock.uptimeMillis()
                    + UPDATE_DELAY);
        }
    }

    /**
     * Starts the packet listening thread.
     *
     * @author Kevin
     */
    public void startListening() {
        // toggle packet reader/UI updater state and button label
        if (!packetReaderThreadStarted) {
            titanoboaPacketReader.setPort(12345);

            // start packet reader
            packetReaderThread = new Thread(titanoboaPacketReader);
            packetReaderThreadStarted = true;
            packetReaderThread.start();

            // start UI updater
            uiUpdateHandler.removeCallbacks(uiUpdateTask);
            uiUpdateHandler.post(uiUpdateTask);
        }
    }

    /**
     * Stops the packet listening thread.
     *
     * @author Kevin
     */
    public void stopListening() {
        if (!packetReaderThreadStarted) {
            // stop packet reader
            packetReaderThreadStarted = false;
            packetReaderThread.interrupt();

            // stop UI updater
            uiUpdateHandler.removeCallbacks(uiUpdateTask);
        }
    }

    /**
     * On click listener for module buttons. Only used for phone version of app
     * so far.
     *
     * @author Graham
     */
    private final class ModuleButtonsOnClickListener implements OnClickListener {

        /**
         * Switch selected module depending on which radio button was clicked.
         */
        @Override
        public void onClick(View v) {
            // change module based on button clicked
            int buttonId = v.getId();
            switch (buttonId) {
                case R.id.module1Radio:
                    selectedModule = 1;
                    break;
                case R.id.module2Radio:
                    selectedModule = 2;
                    break;
                case R.id.module3Radio:
                    selectedModule = 3;
                    break;
                case R.id.module4Radio:
                    selectedModule = 4;
                    break;
            }
            // update immediately so it doesn't ever show the wrong data
            titanoboaModel.updateDataSelected(selectedModule, titanoboaPacketReader.getPackets());
            titanoboaModelViews.updateViewsSelected(selectedModule);

            // change header label to appropriate module
            ((TextView) findViewById(R.id.moduleHeader))
                    .setText(getString(R.string.module) + " " + selectedModule);
        }

    }
}
