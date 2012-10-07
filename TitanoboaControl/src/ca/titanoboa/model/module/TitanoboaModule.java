package ca.titanoboa.model.module;

import java.util.List;

import android.widget.TextView;

import ca.titanoboa.model.vertebra.Vertebra;
import ca.titanoboa.packet.Packet;

/**
 * Represents a single module (largest unit) of Titanoboa. Currently (Oct. 2012)
 * each module has 5 vertebrae. In addition to containing the vertebrae, each
 * module has its own battery level and motor speed.
 * 
 * @author Graham
 * 
 */
public class TitanoboaModule implements Module {
	public static final int VERTEBRAE_PER_MODULE = 5;

	private List<Vertebra> vertebrae;
	private int batteryLevel;
	private int motorSpeed;

	private TextView batteryLevelView;
	private TextView motorSpeedView;

	public List<Vertebra> getVertebrae() {
		return vertebrae;
	}

	public void setVertebrae(List<Vertebra> vertebrae) {
		this.vertebrae = vertebrae;
	}

	public int getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public int getMotorSpeed() {
		return motorSpeed;
	}

	public void setMotorSpeed(int motorSpeed) {
		this.motorSpeed = motorSpeed;
	}

	public TextView getMotorSpeedView() {
		return motorSpeedView;
	}

	public void setMotorSpeedView(TextView motorSpeedView) {
		this.motorSpeedView = motorSpeedView;
	}

	public TextView getBatteryLevelView() {
		return batteryLevelView;
	}

	public void setBatteryLevelView(TextView batteryLevelView) {
		this.batteryLevelView = batteryLevelView;
	}

	/**
	 * Update data for this module. Tell each vertebra to update itself in turn,
	 * passing it the packet.
	 * @param packet The packet for this module.
	 */
	public void updateData(Packet packet) {
		motorSpeed = packet.getMotorSpeed();
		batteryLevel = packet.getBatteryLevel();

		motorSpeedView.setText(Integer.toString(motorSpeed));
		batteryLevelView.setText(Integer.toString(batteryLevel));

		for (Vertebra vertebra : vertebrae) {
			vertebra.updateData(packet);
		}
	}

}
