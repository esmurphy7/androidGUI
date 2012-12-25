/**
 * 
 */
package ca.titanoboa.model.module;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.widget.TextView;

import ca.titanoboa.model.vertebra.Vertebra;
import ca.titanoboa.packet.Packet;

/**
 * @author Graham
 * 
 */
public interface Module {
	List<Vertebra> getVertebrae();

    UUID getLastPacketUuid();

    int getModuleNumber();

	int getBatteryLevel();

	void setBatteryLevel(int batteryLevel);

	int getMotorSpeed();

	void setMotorSpeed(int motorSpeed);

    int getPressureSensorValue();

    void setPressureSensorValue(int pressureSensorValue);

	TextView getMotorSpeedView();

	void setMotorSpeedView(TextView motorSpeedView);

	TextView getBatteryLevelView();

	void setBatteryLevelView(TextView batteryLevelView);

    TextView getPressureSensorView();

    void setPressureSensorView(TextView pressureSensorView);
	
	void updateData(Map<String, Packet> packets);

    void setVertebraeViews(List<List<TextView>> vertebraeViews);
}
