package ca.titanoboa.model.actuator;

import ca.titanoboa.packet.Packet;
import android.widget.TextView;

public interface Actuator {
	public int getCurrentAngle();

	public void setCurrentAngle(int currentAngle);

	public int getSetpointAngle();

	public void setSetpointAngle(int setpointAngle);

	public int getRawSensorValue();

	public void setRawSensorValue(int rawSensorValue);

	public int getSensorCalibrationHigh();

	public void setSensorCalibrationHigh(int sensorCalibrationHigh);

	public int getSensorCalibrationLow();

	public void setSensorCalibrationLow(int sensorCalibrationLow);

	public void setCurrentAngleView(TextView currentAngleView);

	public TextView getSetpointAngleView();

	public void setSetpointAngleView(TextView setpointAngleView);

	public TextView getRawSensorValueView();

	public void setRawSensorValueView(TextView rawSensorValueView);

	public TextView getSensorCalibrationHighView();

	public void setSensorCalibrationHighView(TextView sensorCalibrationHighView);

	public TextView getSensorCalibrationLowView();

	public void setSensorCalibrationLowView(TextView sensorCalibrationLowView);
	
	public char getActuatorOrientation();

	public void setActuatorOrientation(char actuatorOrientation);
	
	public void updateData(Packet packet, int vertebraNumber);
}
