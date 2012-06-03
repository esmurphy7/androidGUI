package ca.titanoboa.model.module;

import java.util.List;

import android.widget.TextView;

import ca.titanoboa.model.vertebra.Vertebra;
import ca.titanoboa.packet.Packet;

public class TitanoboaModule implements Module {
	public static final int VERTEBRAE_PER_MODULE = 5;
	public static final int VERTEBRA_VALUES_SIZE = 10;

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

//	@Override
//	public void updateViews() {
//		motorSpeedView.setText(Integer.toString(motorSpeed));
//		batteryLevelView.setText(Integer.toString(batteryLevel));
//
//		for (Vertebra vertebra : vertebrae) {
//			vertebra.updateViews();
//		}
//
//	}
	
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
