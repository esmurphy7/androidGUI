package ca.titanoboa.views.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.widget.TextView;

import ca.titanoboa.model.module.Module;
import ca.titanoboa.model.module.TitanoboaModule;
import ca.titanoboa.model.vertebra.TitanoboaVertebra;
import ca.titanoboa.model.vertebra.Vertebra;
import ca.titanoboa.views.vertebra.TitanoboaVertebraViews;
import ca.titanoboa.network.TitanoboaPacketReader;
import ca.titanoboa.packet.HeadAndModuleGeneralPacket;
import ca.titanoboa.packet.Packet;
import ca.titanoboa.views.vertebra.VertebraViews;

/**
 * Represents the Android views of a single module (largest unit) of Titanoboa.
 * Currently (Sep 2013) each module has 5 vertebrae. In addition to containing
 * the view units for vertebrae, each module has its own battery level, motor
 * speed, and pressure sensor value.
 * 
 * @author Graham
 * 
 */
public class TitanoboaModuleViews implements ModuleViews {
	public static final int VERTEBRAE_PER_MODULE = 5;

    private final Module module;

    private final int moduleNumber;
	private final List<VertebraViews> vertebraViews;

	private TextView batteryLevelView;
	private TextView motorSpeedView;
    private TextView pressureSensorView;

    public TitanoboaModuleViews(int moduleNumber, Module module) {
        this.moduleNumber = moduleNumber;
        this.module = module;
        vertebraViews = new ArrayList<VertebraViews>();
        List<Vertebra> vertebrae = module.getVertebrae();
        for (int i = 1; i <= VERTEBRAE_PER_MODULE; i++) {
            VertebraViews vertebraView = new TitanoboaVertebraViews(getModuleNumber(), i, vertebrae.get(i-1));
            vertebraViews.add(vertebraView);
        }
    }

    @Override
    public int getModuleNumber() {
        return moduleNumber;
    }

    @Override
	public List<VertebraViews> getVertebraViews() {
		return vertebraViews;
	}

    @Override
	public TextView getMotorSpeedView() {
		return motorSpeedView;
	}

    @Override
	public void setMotorSpeedView(TextView motorSpeedView) {
		this.motorSpeedView = motorSpeedView;
	}

    @Override
	public TextView getBatteryLevelView() {
		return batteryLevelView;
	}

    @Override
	public void setBatteryLevelView(TextView batteryLevelView) {
		this.batteryLevelView = batteryLevelView;
	}

    @Override
    public TextView getPressureSensorView() {
        return pressureSensorView;
    }

    @Override
    public void setPressureSensorView(TextView pressureSensorView) {
        this.pressureSensorView = pressureSensorView;
    }

	/**
	 * Update views for this module. Tell each vertebra to update itself in turn.
	 */
    @Override
	public void updateViews() {
        // don't bother updating if the packet hasn't changed
        if (!module.isChangedSinceLastUpdate()) {
            return;
        }

		motorSpeedView.setText(Integer.toString(module.getMotorSpeed()));
		batteryLevelView.setText(Integer.toString(module.getBatteryLevel()));
        pressureSensorView.setText(Integer.toString(module.getPressureSensorValue()));

        for (VertebraViews views : vertebraViews) {
            views.updateViews();
        }
	}

    @Override
    public void setVertebraViews(List<List<TextView>> viewses) {
        int i = 0;
        for (List<TextView> views : viewses) {
            vertebraViews.get(i).setViews(views);
            i++;
        }
    }

}
