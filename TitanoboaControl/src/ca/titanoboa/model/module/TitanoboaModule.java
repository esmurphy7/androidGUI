package ca.titanoboa.model.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.widget.TextView;
import ca.titanoboa.network.TitanoboaPacketReader;
import ca.titanoboa.packet.HeadAndModuleGeneralPacket;
import ca.titanoboa.packet.Packet;

/**
 * Represents a single module (largest unit) of Titanoboa. Currently (Oct. 2012)
 * each module has 5 vertebrae. In addition to containing the vertebrae, each
 * module has its own battery level, motor speed, and pressure sensor value.
 * 
 * @author Graham
 * 
 */
public class TitanoboaModule implements Module {
	public static final int VERTEBRAE_PER_MODULE = 5;
    private static final int MAX_BATTERY_LEVEL = 25000; //10 bit voltage value

    private UUID lastPacketUuid;
    private final int moduleNumber;

	private int batteryLevel;
	private int motorSpeed;
    private int pressureSensorValue;

	private TextView batteryLevelView;
	private TextView motorSpeedView;
    private TextView pressureSensorView;

    public TitanoboaModule(int moduleNumber) {
        this.moduleNumber = moduleNumber;
    }

    @Override
    public UUID getLastPacketUuid() {
        return lastPacketUuid;
    }

    @Override
    public int getModuleNumber() {
        return moduleNumber;
    }

    @Override
	public int getBatteryLevel() {
		return batteryLevel;
	}

    @Override
	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

    public double getBatteryLevelAsPercent(){
        double percent = ((double)batteryLevel/(double)MAX_BATTERY_LEVEL)*100;
        return percent;
    }

    @Override
	public int getMotorSpeed() {
		return motorSpeed;
	}

    @Override
	public void setMotorSpeed(int motorSpeed) {
		this.motorSpeed = motorSpeed;
	}

    @Override
    public int getPressureSensorValue() {
        return pressureSensorValue;
    }

    @Override
    public void setPressureSensorValue(int pressureSensorValue) {
        this.pressureSensorValue = pressureSensorValue;
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
	 * @param packets The packets to update from.
	 */
    @Override
	public void updateData(Map<String, Packet> packets) {
        HeadAndModuleGeneralPacket headAndModuleGeneralPacket = (HeadAndModuleGeneralPacket) packets.get(TitanoboaPacketReader.HEAD_AND_MODULE_KEY);
        UUID currentPacketUuid = headAndModuleGeneralPacket.getUuid();

        // don't bother updating if the packet hasn't changed
        //if (!currentPacketUuid.equals(getLastPacketUuid())) {
            lastPacketUuid = currentPacketUuid;
            motorSpeed = headAndModuleGeneralPacket.getModuleMotorSpeed(getModuleNumber());
            batteryLevel = headAndModuleGeneralPacket.getModuleBatteryVoltage(getModuleNumber());
            pressureSensorValue = headAndModuleGeneralPacket.getModulePressureSensorValue(getModuleNumber());
        //}


	}


}
