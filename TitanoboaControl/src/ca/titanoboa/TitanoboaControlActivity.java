package ca.titanoboa;

import java.util.ArrayList;
import java.util.List;
import ca.titanoboa.R;
import ca.titanoboa.model.Model;
import ca.titanoboa.model.TitanoboaModel;
import ca.titanoboa.model.actuator.Actuator;
import ca.titanoboa.model.actuator.TitanoboaActuator;
import ca.titanoboa.model.module.Module;
import ca.titanoboa.model.module.TitanoboaModule;
import ca.titanoboa.model.vertebra.TitanoboaVertebra;
import ca.titanoboa.model.vertebra.Vertebra;
import ca.titanoboa.network.PacketReader;
import ca.titanoboa.network.TitanoboaPacketReader;
import ca.titanoboa.packet.Packet;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.EditText;

/**
 * The base activity of the whole app!
 * 
 * @author Graham
 * 
 */
public class TitanoboaControlActivity extends Activity {

	private int selectedModule;
	private boolean screenSizeIsXLarge;

	/**
	 * A Runnable implementation for updating the UI every UPDATE_DELAY ms.
	 */
	private final class UIUpdateTask implements Runnable {
		@Override
		public void run() {
			List<Packet> packets = titanoboaPacketReader.getPackets();
			if (!packets.isEmpty()) {
				titanoboaModel.updateData(packets);
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
	 * On click listener for module buttons. Only used for phone version of app
	 * so far.
	 * 
	 * @author Graham
	 * 
	 */
	private final class ModuleButtonsOnClickListener implements OnClickListener {

		/**
		 * Switch selected module depending on which radio button was clicked.
		 */
		@Override
		public void onClick(View v) {

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

		}

	}

	private Model titanoboaModel;
	private PacketReader titanoboaPacketReader;
	private Thread packetReaderThread;
	private boolean packetReaderThreadStarted;
	private Handler uiUpdateHandler;
	private Runnable uiUpdateTask;

	// how often data updates, in ms
	public static final int UPDATE_DELAY = 1000;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		boolean screenSizeIsXLarge = getResources().getBoolean(
				R.bool.screen_xlarge);

		titanoboaModel = new TitanoboaModel();

		// initialize differently for a tablet (xlarge) than a phone
		if (screenSizeIsXLarge) {
			selectedModule = 0;
			setupTitanoboaModelXLarge();
		} else {
			selectedModule = 1;
			setupTitanoboaModelNormal();
		}

		titanoboaPacketReader = new TitanoboaPacketReader();
		// Packet reader is started via Connect button
		packetReaderThreadStarted = false;

		uiUpdateHandler = new Handler();
		uiUpdateTask = new UIUpdateTask();

		Button connectButton = ((Button) findViewById(R.id.connectButton));
		connectButton.setOnClickListener(new ConnectButtonOnClickListener());

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
	}

	/**
	 * Set up model with modules, vertebrae, and actuators. Normal version for
	 * phones.
	 */
	private void setupTitanoboaModelNormal() {
		// TODO: Set up model for normal screen. I think what should be done is
		// to set the same views for all modules, and just allow the model to
		// determine via selectedModule which module should be allowed to update
		// its values on the screen. With that in mind this method should be a
		// lot simpler than the XLarge setup - just loop over the list of 4
		// modules and set all the vertebrae and actuators up the same way.
	}

	/**
	 * Set up model with modules, vertebrae, and actuators. XLarge version for
	 * tablets.
	 */
	private void setupTitanoboaModelXLarge() {
		/*
		 * My apologies for this nasty-ass setup code. :| Much of the difficulty
		 * is in the fact that there's no particularly good way to cobble
		 * together the R.id.x string - there's a findViewByName which would
		 * make the code neater, but it's horribly inefficient compared to
		 * findViewById. Doing the TextView creation programmatically rather
		 * than in the layout XML might help. Pulling this out to its own class
		 * would be nice, too.
		 */

		titanoboaModel = new TitanoboaModel();

		TitanoboaModule module1 = new TitanoboaModule();
		List<Module> modules = new ArrayList<Module>();

		List<Vertebra> module1Vertebrae = new ArrayList<Vertebra>();
		module1.setBatteryLevelView((TextView) findViewById(R.id.m1_battery_level));
		module1.setMotorSpeedView((TextView) findViewById(R.id.m1_motor_speed));

		TitanoboaVertebra module1Vertebra0 = new TitanoboaVertebra();

		TitanoboaActuator m1v0HorizontalActuator = new TitanoboaActuator();
		m1v0HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m1_v0_h_setpoint_angle));
		m1v0HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m1_v0_h_current_angle));
		m1v0HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m1_v0_h_raw_sensor_value));
		m1v0HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m1_v0_h_sensor_calibration_high));
		m1v0HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m1_v0_h_sensor_calibration_low));
		m1v0HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m1v0VerticalActuator = new TitanoboaActuator();
		m1v0VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m1_v0_v_setpoint_angle));
		m1v0VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m1_v0_v_current_angle));
		m1v0VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m1_v0_v_raw_sensor_value));
		m1v0VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m1_v0_v_sensor_calibration_high));
		m1v0VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m1_v0_v_sensor_calibration_low));
		m1v0VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module1Vertebra0Actuators = new ArrayList<Actuator>();
		module1Vertebra0Actuators.add(m1v0HorizontalActuator);
		module1Vertebra0Actuators.add(m1v0VerticalActuator);
		module1Vertebra0.setActuators(module1Vertebra0Actuators);
		module1Vertebra0.setVertebraNumber(0);
		module1Vertebrae.add(module1Vertebra0);

		TitanoboaVertebra module1Vertebra1 = new TitanoboaVertebra();

		TitanoboaActuator m1v1HorizontalActuator = new TitanoboaActuator();
		m1v1HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m1_v1_h_setpoint_angle));
		m1v1HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m1_v1_h_current_angle));
		m1v1HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m1_v1_h_raw_sensor_value));
		m1v1HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m1_v1_h_sensor_calibration_high));
		m1v1HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m1_v1_h_sensor_calibration_low));
		m1v1HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m1v1VerticalActuator = new TitanoboaActuator();
		m1v1VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m1_v1_v_setpoint_angle));
		m1v1VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m1_v1_v_current_angle));
		m1v1VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m1_v1_v_raw_sensor_value));
		m1v1VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m1_v1_v_sensor_calibration_high));
		m1v1VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m1_v1_v_sensor_calibration_low));
		m1v1VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module1Vertebra1Actuators = new ArrayList<Actuator>();
		module1Vertebra1Actuators.add(m1v1HorizontalActuator);
		module1Vertebra1Actuators.add(m1v1VerticalActuator);
		module1Vertebra1.setActuators(module1Vertebra1Actuators);
		module1Vertebra1.setVertebraNumber(1);
		module1Vertebrae.add(module1Vertebra1);

		TitanoboaVertebra module1Vertebra2 = new TitanoboaVertebra();

		TitanoboaActuator m1v2HorizontalActuator = new TitanoboaActuator();
		m1v2HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m1_v2_h_setpoint_angle));
		m1v2HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m1_v2_h_current_angle));
		m1v2HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m1_v2_h_raw_sensor_value));
		m1v2HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m1_v2_h_sensor_calibration_high));
		m1v2HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m1_v2_h_sensor_calibration_low));
		m1v2HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m1v2VerticalActuator = new TitanoboaActuator();
		m1v2VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m1_v2_v_setpoint_angle));
		m1v2VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m1_v2_v_current_angle));
		m1v2VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m1_v2_v_raw_sensor_value));
		m1v2VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m1_v2_v_sensor_calibration_high));
		m1v2VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m1_v2_v_sensor_calibration_low));
		m1v2VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module1Vertebra2Actuators = new ArrayList<Actuator>();
		module1Vertebra2Actuators.add(m1v2HorizontalActuator);
		module1Vertebra2Actuators.add(m1v2VerticalActuator);
		module1Vertebra2.setActuators(module1Vertebra2Actuators);
		module1Vertebra2.setVertebraNumber(2);
		module1Vertebrae.add(module1Vertebra2);

		TitanoboaVertebra module1Vertebra3 = new TitanoboaVertebra();

		TitanoboaActuator m1v3HorizontalActuator = new TitanoboaActuator();
		m1v3HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m1_v3_h_setpoint_angle));
		m1v3HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m1_v3_h_current_angle));
		m1v3HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m1_v3_h_raw_sensor_value));
		m1v3HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m1_v3_h_sensor_calibration_high));
		m1v3HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m1_v3_h_sensor_calibration_low));
		m1v3HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m1v3VerticalActuator = new TitanoboaActuator();
		m1v3VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m1_v3_v_setpoint_angle));
		m1v3VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m1_v3_v_current_angle));
		m1v3VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m1_v3_v_raw_sensor_value));
		m1v3VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m1_v3_v_sensor_calibration_high));
		m1v3VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m1_v3_v_sensor_calibration_low));
		m1v3VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module1Vertebra3Actuators = new ArrayList<Actuator>();
		module1Vertebra3Actuators.add(m1v3HorizontalActuator);
		module1Vertebra3Actuators.add(m1v3VerticalActuator);
		module1Vertebra3.setActuators(module1Vertebra3Actuators);
		module1Vertebra3.setVertebraNumber(3);
		module1Vertebrae.add(module1Vertebra3);

		TitanoboaVertebra module1Vertebra4 = new TitanoboaVertebra();

		TitanoboaActuator m1v4HorizontalActuator = new TitanoboaActuator();
		m1v4HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m1_v4_h_setpoint_angle));
		m1v4HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m1_v4_h_current_angle));
		m1v4HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m1_v4_h_raw_sensor_value));
		m1v4HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m1_v4_h_sensor_calibration_high));
		m1v4HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m1_v4_h_sensor_calibration_low));
		m1v4HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m1v4VerticalActuator = new TitanoboaActuator();
		m1v4VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m1_v4_v_setpoint_angle));
		m1v4VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m1_v4_v_current_angle));
		m1v4VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m1_v4_v_raw_sensor_value));
		m1v4VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m1_v4_v_sensor_calibration_high));
		m1v4VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m1_v4_v_sensor_calibration_low));
		m1v4VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module1Vertebra4Actuators = new ArrayList<Actuator>();
		module1Vertebra4Actuators.add(m1v4HorizontalActuator);
		module1Vertebra4Actuators.add(m1v4VerticalActuator);
		module1Vertebra4.setActuators(module1Vertebra4Actuators);
		module1Vertebra4.setVertebraNumber(4);
		module1Vertebrae.add(module1Vertebra4);

		module1.setVertebrae(module1Vertebrae);

		modules.add(module1);

		TitanoboaModule module2 = new TitanoboaModule();

		List<Vertebra> module2Vertebrae = new ArrayList<Vertebra>();
		module2.setBatteryLevelView((TextView) findViewById(R.id.m2_battery_level));
		module2.setMotorSpeedView((TextView) findViewById(R.id.m2_motor_speed));

		TitanoboaVertebra module2Vertebra0 = new TitanoboaVertebra();

		TitanoboaActuator m2v0HorizontalActuator = new TitanoboaActuator();
		m2v0HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m2_v0_h_setpoint_angle));
		m2v0HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m2_v0_h_current_angle));
		m2v0HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m2_v0_h_raw_sensor_value));
		m2v0HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m2_v0_h_sensor_calibration_high));
		m2v0HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m2_v0_h_sensor_calibration_low));
		m2v0HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m2v0VerticalActuator = new TitanoboaActuator();
		m2v0VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m2_v0_v_setpoint_angle));
		m2v0VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m2_v0_v_current_angle));
		m2v0VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m2_v0_v_raw_sensor_value));
		m2v0VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m2_v0_v_sensor_calibration_high));
		m2v0VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m2_v0_v_sensor_calibration_low));
		m2v0VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module2Vertebra0Actuators = new ArrayList<Actuator>();
		module2Vertebra0Actuators.add(m2v0HorizontalActuator);
		module2Vertebra0Actuators.add(m2v0VerticalActuator);
		module2Vertebra0.setActuators(module2Vertebra0Actuators);
		module2Vertebra0.setVertebraNumber(0);
		module2Vertebrae.add(module2Vertebra0);

		TitanoboaVertebra module2Vertebra1 = new TitanoboaVertebra();

		TitanoboaActuator m2v1HorizontalActuator = new TitanoboaActuator();
		m2v1HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m2_v1_h_setpoint_angle));
		m2v1HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m2_v1_h_current_angle));
		m2v1HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m2_v1_h_raw_sensor_value));
		m2v1HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m2_v1_h_sensor_calibration_high));
		m2v1HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m2_v1_h_sensor_calibration_low));
		m2v1HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m2v1VerticalActuator = new TitanoboaActuator();
		m2v1VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m2_v1_v_setpoint_angle));
		m2v1VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m2_v1_v_current_angle));
		m2v1VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m2_v1_v_raw_sensor_value));
		m2v1VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m2_v1_v_sensor_calibration_high));
		m2v1VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m2_v1_v_sensor_calibration_low));
		m2v1VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module2Vertebra1Actuators = new ArrayList<Actuator>();
		module2Vertebra1Actuators.add(m2v1HorizontalActuator);
		module2Vertebra1Actuators.add(m2v1VerticalActuator);
		module2Vertebra1.setActuators(module2Vertebra1Actuators);
		module2Vertebra1.setVertebraNumber(1);
		module2Vertebrae.add(module2Vertebra1);

		TitanoboaVertebra module2Vertebra2 = new TitanoboaVertebra();

		TitanoboaActuator m2v2HorizontalActuator = new TitanoboaActuator();
		m2v2HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m2_v2_h_setpoint_angle));
		m2v2HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m2_v2_h_current_angle));
		m2v2HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m2_v2_h_raw_sensor_value));
		m2v2HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m2_v2_h_sensor_calibration_high));
		m2v2HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m2_v2_h_sensor_calibration_low));
		m2v2HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m2v2VerticalActuator = new TitanoboaActuator();
		m2v2VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m2_v2_v_setpoint_angle));
		m2v2VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m2_v2_v_current_angle));
		m2v2VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m2_v2_v_raw_sensor_value));
		m2v2VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m2_v2_v_sensor_calibration_high));
		m2v2VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m2_v2_v_sensor_calibration_low));
		m2v2VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module2Vertebra2Actuators = new ArrayList<Actuator>();
		module2Vertebra2Actuators.add(m2v2HorizontalActuator);
		module2Vertebra2Actuators.add(m2v2VerticalActuator);
		module2Vertebra2.setActuators(module2Vertebra2Actuators);
		module2Vertebra2.setVertebraNumber(2);
		module2Vertebrae.add(module2Vertebra2);

		TitanoboaVertebra module2Vertebra3 = new TitanoboaVertebra();

		TitanoboaActuator m2v3HorizontalActuator = new TitanoboaActuator();
		m2v3HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m2_v3_h_setpoint_angle));
		m2v3HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m2_v3_h_current_angle));
		m2v3HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m2_v3_h_raw_sensor_value));
		m2v3HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m2_v3_h_sensor_calibration_high));
		m2v3HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m2_v3_h_sensor_calibration_low));
		m2v3HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m2v3VerticalActuator = new TitanoboaActuator();
		m2v3VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m2_v3_v_setpoint_angle));
		m2v3VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m2_v3_v_current_angle));
		m2v3VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m2_v3_v_raw_sensor_value));
		m2v3VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m2_v3_v_sensor_calibration_high));
		m2v3VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m2_v3_v_sensor_calibration_low));
		m2v3VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module2Vertebra3Actuators = new ArrayList<Actuator>();
		module2Vertebra3Actuators.add(m2v3HorizontalActuator);
		module2Vertebra3Actuators.add(m2v3VerticalActuator);
		module2Vertebra3.setActuators(module2Vertebra3Actuators);
		module2Vertebra3.setVertebraNumber(3);
		module2Vertebrae.add(module2Vertebra3);

		TitanoboaVertebra module2Vertebra4 = new TitanoboaVertebra();

		TitanoboaActuator m2v4HorizontalActuator = new TitanoboaActuator();
		m2v4HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m2_v4_h_setpoint_angle));
		m2v4HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m2_v4_h_current_angle));
		m2v4HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m2_v4_h_raw_sensor_value));
		m2v4HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m2_v4_h_sensor_calibration_high));
		m2v4HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m2_v4_h_sensor_calibration_low));
		m2v4HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m2v4VerticalActuator = new TitanoboaActuator();
		m2v4VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m2_v4_v_setpoint_angle));
		m2v4VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m2_v4_v_current_angle));
		m2v4VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m2_v4_v_raw_sensor_value));
		m2v4VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m2_v4_v_sensor_calibration_high));
		m2v4VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m2_v4_v_sensor_calibration_low));
		m2v4VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module2Vertebra4Actuators = new ArrayList<Actuator>();
		module2Vertebra4Actuators.add(m2v4HorizontalActuator);
		module2Vertebra4Actuators.add(m2v4VerticalActuator);
		module2Vertebra4.setActuators(module2Vertebra4Actuators);
		module2Vertebra4.setVertebraNumber(4);
		module2Vertebrae.add(module2Vertebra4);

		module2.setVertebrae(module2Vertebrae);

		modules.add(module2);

		TitanoboaModule module3 = new TitanoboaModule();

		List<Vertebra> module3Vertebrae = new ArrayList<Vertebra>();
		module3.setBatteryLevelView((TextView) findViewById(R.id.m3_battery_level));
		module3.setMotorSpeedView((TextView) findViewById(R.id.m3_motor_speed));

		TitanoboaVertebra module3Vertebra0 = new TitanoboaVertebra();

		TitanoboaActuator m3v0HorizontalActuator = new TitanoboaActuator();
		m3v0HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m3_v0_h_setpoint_angle));
		m3v0HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m3_v0_h_current_angle));
		m3v0HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m3_v0_h_raw_sensor_value));
		m3v0HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m3_v0_h_sensor_calibration_high));
		m3v0HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m3_v0_h_sensor_calibration_low));
		m3v0HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m3v0VerticalActuator = new TitanoboaActuator();
		m3v0VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m3_v0_v_setpoint_angle));
		m3v0VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m3_v0_v_current_angle));
		m3v0VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m3_v0_v_raw_sensor_value));
		m3v0VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m3_v0_v_sensor_calibration_high));
		m3v0VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m3_v0_v_sensor_calibration_low));
		m3v0VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module3Vertebra0Actuators = new ArrayList<Actuator>();
		module3Vertebra0Actuators.add(m3v0HorizontalActuator);
		module3Vertebra0Actuators.add(m3v0VerticalActuator);
		module3Vertebra0.setActuators(module3Vertebra0Actuators);
		module3Vertebra0.setVertebraNumber(0);
		module3Vertebrae.add(module3Vertebra0);

		TitanoboaVertebra module3Vertebra1 = new TitanoboaVertebra();

		TitanoboaActuator m3v1HorizontalActuator = new TitanoboaActuator();
		m3v1HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m3_v1_h_setpoint_angle));
		m3v1HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m3_v1_h_current_angle));
		m3v1HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m3_v1_h_raw_sensor_value));
		m3v1HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m3_v1_h_sensor_calibration_high));
		m3v1HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m3_v1_h_sensor_calibration_low));
		m3v1HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m3v1VerticalActuator = new TitanoboaActuator();
		m3v1VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m3_v1_v_setpoint_angle));
		m3v1VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m3_v1_v_current_angle));
		m3v1VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m3_v1_v_raw_sensor_value));
		m3v1VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m3_v1_v_sensor_calibration_high));
		m3v1VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m3_v1_v_sensor_calibration_low));
		m3v1VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module3Vertebra1Actuators = new ArrayList<Actuator>();
		module3Vertebra1Actuators.add(m3v1HorizontalActuator);
		module3Vertebra1Actuators.add(m3v1VerticalActuator);
		module3Vertebra1.setActuators(module3Vertebra1Actuators);
		module3Vertebra1.setVertebraNumber(1);
		module3Vertebrae.add(module3Vertebra1);

		TitanoboaVertebra module3Vertebra2 = new TitanoboaVertebra();

		TitanoboaActuator m3v2HorizontalActuator = new TitanoboaActuator();
		m3v2HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m3_v2_h_setpoint_angle));
		m3v2HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m3_v2_h_current_angle));
		m3v2HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m3_v2_h_raw_sensor_value));
		m3v2HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m3_v2_h_sensor_calibration_high));
		m3v2HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m3_v2_h_sensor_calibration_low));
		m3v2HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m3v2VerticalActuator = new TitanoboaActuator();
		m3v2VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m3_v2_v_setpoint_angle));
		m3v2VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m3_v2_v_current_angle));
		m3v2VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m3_v2_v_raw_sensor_value));
		m3v2VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m3_v2_v_sensor_calibration_high));
		m3v2VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m3_v2_v_sensor_calibration_low));
		m3v2VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module3Vertebra2Actuators = new ArrayList<Actuator>();
		module3Vertebra2Actuators.add(m3v2HorizontalActuator);
		module3Vertebra2Actuators.add(m3v2VerticalActuator);
		module3Vertebra2.setActuators(module3Vertebra2Actuators);
		module3Vertebra2.setVertebraNumber(2);
		module3Vertebrae.add(module3Vertebra2);

		TitanoboaVertebra module3Vertebra3 = new TitanoboaVertebra();

		TitanoboaActuator m3v3HorizontalActuator = new TitanoboaActuator();
		m3v3HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m3_v3_h_setpoint_angle));
		m3v3HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m3_v3_h_current_angle));
		m3v3HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m3_v3_h_raw_sensor_value));
		m3v3HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m3_v3_h_sensor_calibration_high));
		m3v3HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m3_v3_h_sensor_calibration_low));
		m3v3HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m3v3VerticalActuator = new TitanoboaActuator();
		m3v3VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m3_v3_v_setpoint_angle));
		m3v3VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m3_v3_v_current_angle));
		m3v3VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m3_v3_v_raw_sensor_value));
		m3v3VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m3_v3_v_sensor_calibration_high));
		m3v3VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m3_v3_v_sensor_calibration_low));
		m3v3VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module3Vertebra3Actuators = new ArrayList<Actuator>();
		module3Vertebra3Actuators.add(m3v3HorizontalActuator);
		module3Vertebra3Actuators.add(m3v3VerticalActuator);
		module3Vertebra3.setActuators(module3Vertebra3Actuators);
		module3Vertebra3.setVertebraNumber(3);
		module3Vertebrae.add(module3Vertebra3);

		TitanoboaVertebra module3Vertebra4 = new TitanoboaVertebra();

		TitanoboaActuator m3v4HorizontalActuator = new TitanoboaActuator();
		m3v4HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m3_v4_h_setpoint_angle));
		m3v4HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m3_v4_h_current_angle));
		m3v4HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m3_v4_h_raw_sensor_value));
		m3v4HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m3_v4_h_sensor_calibration_high));
		m3v4HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m3_v4_h_sensor_calibration_low));
		m3v4HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m3v4VerticalActuator = new TitanoboaActuator();
		m3v4VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m3_v4_v_setpoint_angle));
		m3v4VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m3_v4_v_current_angle));
		m3v4VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m3_v4_v_raw_sensor_value));
		m3v4VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m3_v4_v_sensor_calibration_high));
		m3v4VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m3_v4_v_sensor_calibration_low));
		m3v4VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module3Vertebra4Actuators = new ArrayList<Actuator>();
		module3Vertebra4Actuators.add(m3v4HorizontalActuator);
		module3Vertebra4Actuators.add(m3v4VerticalActuator);
		module3Vertebra4.setActuators(module3Vertebra4Actuators);
		module3Vertebra4.setVertebraNumber(4);
		module3Vertebrae.add(module3Vertebra4);

		module3.setVertebrae(module3Vertebrae);

		modules.add(module3);

		TitanoboaModule module4 = new TitanoboaModule();

		List<Vertebra> module4Vertebrae = new ArrayList<Vertebra>();
		module4.setBatteryLevelView((TextView) findViewById(R.id.m4_battery_level));
		module4.setMotorSpeedView((TextView) findViewById(R.id.m4_motor_speed));

		TitanoboaVertebra module4Vertebra0 = new TitanoboaVertebra();

		TitanoboaActuator m4v0HorizontalActuator = new TitanoboaActuator();
		m4v0HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m4_v0_h_setpoint_angle));
		m4v0HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m4_v0_h_current_angle));
		m4v0HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m4_v0_h_raw_sensor_value));
		m4v0HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m4_v0_h_sensor_calibration_high));
		m4v0HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m4_v0_h_sensor_calibration_low));
		m4v0HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m4v0VerticalActuator = new TitanoboaActuator();
		m4v0VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m4_v0_v_setpoint_angle));
		m4v0VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m4_v0_v_current_angle));
		m4v0VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m4_v0_v_raw_sensor_value));
		m4v0VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m4_v0_v_sensor_calibration_high));
		m4v0VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m4_v0_v_sensor_calibration_low));
		m4v0VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module4Vertebra0Actuators = new ArrayList<Actuator>();
		module4Vertebra0Actuators.add(m4v0HorizontalActuator);
		module4Vertebra0Actuators.add(m4v0VerticalActuator);
		module4Vertebra0.setActuators(module4Vertebra0Actuators);
		module4Vertebra0.setVertebraNumber(0);
		module4Vertebrae.add(module4Vertebra0);

		TitanoboaVertebra module4Vertebra1 = new TitanoboaVertebra();

		TitanoboaActuator m4v1HorizontalActuator = new TitanoboaActuator();
		m4v1HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m4_v1_h_setpoint_angle));
		m4v1HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m4_v1_h_current_angle));
		m4v1HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m4_v1_h_raw_sensor_value));
		m4v1HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m4_v1_h_sensor_calibration_high));
		m4v1HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m4_v1_h_sensor_calibration_low));
		m4v1HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m4v1VerticalActuator = new TitanoboaActuator();
		m4v1VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m4_v1_v_setpoint_angle));
		m4v1VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m4_v1_v_current_angle));
		m4v1VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m4_v1_v_raw_sensor_value));
		m4v1VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m4_v1_v_sensor_calibration_high));
		m4v1VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m4_v1_v_sensor_calibration_low));
		m4v1VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module4Vertebra1Actuators = new ArrayList<Actuator>();
		module4Vertebra1Actuators.add(m4v1HorizontalActuator);
		module4Vertebra1Actuators.add(m4v1VerticalActuator);
		module4Vertebra1.setActuators(module4Vertebra1Actuators);
		module4Vertebra1.setVertebraNumber(1);
		module4Vertebrae.add(module4Vertebra1);

		TitanoboaVertebra module4Vertebra2 = new TitanoboaVertebra();

		TitanoboaActuator m4v2HorizontalActuator = new TitanoboaActuator();
		m4v2HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m4_v2_h_setpoint_angle));
		m4v2HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m4_v2_h_current_angle));
		m4v2HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m4_v2_h_raw_sensor_value));
		m4v2HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m4_v2_h_sensor_calibration_high));
		m4v2HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m4_v2_h_sensor_calibration_low));
		m4v2HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m4v2VerticalActuator = new TitanoboaActuator();
		m4v2VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m4_v2_v_setpoint_angle));
		m4v2VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m4_v2_v_current_angle));
		m4v2VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m4_v2_v_raw_sensor_value));
		m4v2VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m4_v2_v_sensor_calibration_high));
		m4v2VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m4_v2_v_sensor_calibration_low));
		m4v2VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module4Vertebra2Actuators = new ArrayList<Actuator>();
		module4Vertebra2Actuators.add(m4v2HorizontalActuator);
		module4Vertebra2Actuators.add(m4v2VerticalActuator);
		module4Vertebra2.setActuators(module4Vertebra2Actuators);
		module4Vertebra2.setVertebraNumber(2);
		module4Vertebrae.add(module4Vertebra2);

		TitanoboaVertebra module4Vertebra3 = new TitanoboaVertebra();

		TitanoboaActuator m4v3HorizontalActuator = new TitanoboaActuator();
		m4v3HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m4_v3_h_setpoint_angle));
		m4v3HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m4_v3_h_current_angle));
		m4v3HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m4_v3_h_raw_sensor_value));
		m4v3HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m4_v3_h_sensor_calibration_high));
		m4v3HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m4_v3_h_sensor_calibration_low));
		m4v3HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m4v3VerticalActuator = new TitanoboaActuator();
		m4v3VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m4_v3_v_setpoint_angle));
		m4v3VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m4_v3_v_current_angle));
		m4v3VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m4_v3_v_raw_sensor_value));
		m4v3VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m4_v3_v_sensor_calibration_high));
		m4v3VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m4_v3_v_sensor_calibration_low));
		m4v3VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module4Vertebra3Actuators = new ArrayList<Actuator>();
		module4Vertebra3Actuators.add(m4v3HorizontalActuator);
		module4Vertebra3Actuators.add(m4v3VerticalActuator);
		module4Vertebra3.setActuators(module4Vertebra3Actuators);
		module4Vertebra3.setVertebraNumber(3);
		module4Vertebrae.add(module4Vertebra3);

		TitanoboaVertebra module4Vertebra4 = new TitanoboaVertebra();

		TitanoboaActuator m4v4HorizontalActuator = new TitanoboaActuator();
		m4v4HorizontalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m4_v4_h_setpoint_angle));
		m4v4HorizontalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m4_v4_h_current_angle));
		m4v4HorizontalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m4_v4_h_raw_sensor_value));
		m4v4HorizontalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m4_v4_h_sensor_calibration_high));
		m4v4HorizontalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m4_v4_h_sensor_calibration_low));
		m4v4HorizontalActuator.setActuatorOrientation('H');

		TitanoboaActuator m4v4VerticalActuator = new TitanoboaActuator();
		m4v4VerticalActuator
				.setSetpointAngleView((TextView) findViewById(R.id.m4_v4_v_setpoint_angle));
		m4v4VerticalActuator
				.setCurrentAngleView((TextView) findViewById(R.id.m4_v4_v_current_angle));
		m4v4VerticalActuator
				.setRawSensorValueView((TextView) findViewById(R.id.m4_v4_v_raw_sensor_value));
		m4v4VerticalActuator
				.setSensorCalibrationHighView((TextView) findViewById(R.id.m4_v4_v_sensor_calibration_high));
		m4v4VerticalActuator
				.setSensorCalibrationLowView((TextView) findViewById(R.id.m4_v4_v_sensor_calibration_low));
		m4v4VerticalActuator.setActuatorOrientation('V');

		List<Actuator> module4Vertebra4Actuators = new ArrayList<Actuator>();
		module4Vertebra4Actuators.add(m4v4HorizontalActuator);
		module4Vertebra4Actuators.add(m4v4VerticalActuator);
		module4Vertebra4.setActuators(module4Vertebra4Actuators);
		module4Vertebra4.setVertebraNumber(4);
		module4Vertebrae.add(module4Vertebra4);

		module4.setVertebrae(module4Vertebrae);

		modules.add(module4);

		titanoboaModel.setModules(modules);

	}
}