/**
 * 
 */
package ca.titanoboa.model.module;

import java.util.List;

import android.widget.TextView;

import ca.titanoboa.model.vertebra.Vertebra;
import ca.titanoboa.packet.Packet;

/**
 * @author Graham
 * 
 */
public interface Module {
	public List<Vertebra> getVertebrae();

	public void setVertebrae(List<Vertebra> vertebrae);

	public int getBatteryLevel();

	public void setBatteryLevel(int batteryLevel);

	public int getMotorSpeed();

	public void setMotorSpeed(int motorSpeed);

	public TextView getMotorSpeedView();

	public void setMotorSpeedView(TextView motorSpeedView);

	public TextView getBatteryLevelView();

	public void setBatteryLevelView(TextView batteryLevelView);
	
	public void updateData(Packet packet);

}
