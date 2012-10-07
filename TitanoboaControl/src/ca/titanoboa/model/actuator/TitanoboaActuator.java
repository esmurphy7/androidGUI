package ca.titanoboa.model.actuator;

import ca.titanoboa.packet.Packet;
import android.widget.TextView;

/**
 * Represents a single actuator with orientation (H or V), current angle,
 * setpoint angle, raw sensor value, and sensor calibration high and low.
 * 
 * @Author Graham
 */
public class TitanoboaActuator implements Actuator {
	private int currentAngle;
	private int setpointAngle;
	private int rawSensorValue;
	private int sensorCalibrationHigh;
	private int sensorCalibrationLow;

	private TextView currentAngleView;
	private TextView setpointAngleView;
	private TextView rawSensorValueView;
	private TextView sensorCalibrationHighView;
	private TextView sensorCalibrationLowView;

	// 'H' for horizontal, 'V' for vertical
	private char actuatorOrientation;

	public char getActuatorOrientation() {
		return actuatorOrientation;
	}

	public void setActuatorOrientation(char actuatorOrientation) {
		this.actuatorOrientation = actuatorOrientation;
	}

	public TextView getCurrentAngleView() {
		return currentAngleView;
	}

	public void setCurrentAngleView(TextView currentAngleView) {
		this.currentAngleView = currentAngleView;
	}

	public TextView getSetpointAngleView() {
		return setpointAngleView;
	}

	public void setSetpointAngleView(TextView setpointAngleView) {
		this.setpointAngleView = setpointAngleView;
	}

	public TextView getRawSensorValueView() {
		return rawSensorValueView;
	}

	public void setRawSensorValueView(TextView rawSensorValueView) {
		this.rawSensorValueView = rawSensorValueView;
	}

	public TextView getSensorCalibrationHighView() {
		return sensorCalibrationHighView;
	}

	public void setSensorCalibrationHighView(TextView sensorCalibrationHighView) {
		this.sensorCalibrationHighView = sensorCalibrationHighView;
	}

	public TextView getSensorCalibrationLowView() {
		return sensorCalibrationLowView;
	}

	public void setSensorCalibrationLowView(TextView sensorCalibrationLowView) {
		this.sensorCalibrationLowView = sensorCalibrationLowView;
	}

	public int getCurrentAngle() {
		return currentAngle;
	}

	public void setCurrentAngle(int currentAngle) {
		this.currentAngle = currentAngle;
	}

	public int getSetpointAngle() {
		return setpointAngle;
	}

	public void setSetpointAngle(int setpointAngle) {
		this.setpointAngle = setpointAngle;
	}

	public int getRawSensorValue() {
		return rawSensorValue;
	}

	public void setRawSensorValue(int rawSensorValue) {
		this.rawSensorValue = rawSensorValue;
	}

	public int getSensorCalibrationHigh() {
		return sensorCalibrationHigh;
	}

	public void setSensorCalibrationHigh(int sensorCalibrationHigh) {
		this.sensorCalibrationHigh = sensorCalibrationHigh;
	}

	public int getSensorCalibrationLow() {
		return sensorCalibrationLow;
	}

	public void setSensorCalibrationLow(int sensorCalibrationLow) {
		this.sensorCalibrationLow = sensorCalibrationLow;
	}

	/**
	 * Update all the data for this actuator.
	 * 
	 * @param packet
	 *            The packet passed down from the vertebra.
	 * @param vertebraNumber
	 *            The vertebra number this actuator is in.
	 */
	@Override
	public void updateData(Packet packet, int vertebraNumber) {
		if (actuatorOrientation == 'H') {
			currentAngle = packet.getCurrentAngleH(vertebraNumber);
			setpointAngle = packet.getSetpointAngleH(vertebraNumber);
			rawSensorValue = packet.getRawSensorValueH(vertebraNumber);
			sensorCalibrationHigh = packet
					.getSensorCalibrationHighH(vertebraNumber);
			sensorCalibrationLow = packet
					.getSensorCalibrationLowH(vertebraNumber);
		} else if (actuatorOrientation == 'V') {
			currentAngle = packet.getCurrentAngleV(vertebraNumber);
			setpointAngle = packet.getSetpointAngleV(vertebraNumber);
			rawSensorValue = packet.getRawSensorValueV(vertebraNumber);
			sensorCalibrationHigh = packet
					.getSensorCalibrationHighV(vertebraNumber);
			sensorCalibrationLow = packet
					.getSensorCalibrationLowV(vertebraNumber);
		}
		currentAngleView.setText(Integer.toString(currentAngle));
		setpointAngleView.setText(Integer.toString(setpointAngle));
		rawSensorValueView.setText(Integer.toString(rawSensorValue));
		sensorCalibrationHighView.setText(Integer
				.toString(sensorCalibrationHigh));
		sensorCalibrationLowView
				.setText(Integer.toString(sensorCalibrationLow));
	}
}
