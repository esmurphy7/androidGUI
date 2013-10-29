/**
 * 
 */
package ca.titanoboa.views.module;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.widget.TextView;

import ca.titanoboa.packet.Packet;
import ca.titanoboa.views.vertebra.VertebraViews;

/**
 * @author Graham
 * 
 */
public interface ModuleViews {
    public List<VertebraViews> getVertebraViews();

    int getModuleNumber();

	TextView getMotorSpeedView();

	void setMotorSpeedView(TextView motorSpeedView);

	TextView getBatteryLevelView();

	void setBatteryLevelView(TextView batteryLevelView);

    TextView getPressureSensorView();

    void setPressureSensorView(TextView pressureSensorView);
	
	void updateViews();

    void setVertebraViews(List<List<TextView>> viewses);
}
