package ca.titanoboa.packet;

/**
 * Interface for Titanoboa head and module general info packet.
 *
 * @author Graham
 * Date: 12/16/12
 * Time: 8:32 AM
 */
public interface HeadAndModuleGeneralPacket extends Packet {

    public int getNumberOfModulesConnected();

    public int getHeadBatteryVoltage();

    public int getModuleBatteryVoltage(int module);

    public int getModuleMotorSpeed(int module);

    public int getModulePressureSensorValue(int module);
}
