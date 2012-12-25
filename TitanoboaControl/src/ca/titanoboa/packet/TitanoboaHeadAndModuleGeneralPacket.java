package ca.titanoboa.packet;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Graham
 *         Date: 12/16/12
 *         Time: 9:04 AM
 */
public class TitanoboaHeadAndModuleGeneralPacket extends TitanoboaPacket implements HeadAndModuleGeneralPacket {

    // size in bytes of the module part of the packet, for calculating byte numbers
    private final int MODULE_PART_SIZE = 6;

    // (first) byte indices for different pieces of information
    private final int NUMBER_OF_MODULES_CONNECTED_INDEX = 1;
    private final int HEAD_BATTERY_VOLTAGE_INDEX = 2;
    private final int MODULE_BATTERY_VOLTAGE_FIRST_INDEX = 4;
    private final int MODULE_MOTOR_SPEED_FIRST_INDEX = 6;
    private final int MODULE_PRESSURE_SENSOR_VALUE_FIRST_INDEX = 8;

    public TitanoboaHeadAndModuleGeneralPacket(byte[] rawPacket) {
        super(rawPacket);
        super.setModulePartSize(MODULE_PART_SIZE);
    }

    @Override
    public int getNumberOfModulesConnected() {
        return getByteAsIntByNumber(NUMBER_OF_MODULES_CONNECTED_INDEX);
    }

    @Override
    public int getHeadBatteryVoltage() {
        return getUnsignedShortByStartByte(HEAD_BATTERY_VOLTAGE_INDEX);
    }

    @Override
    public int getModuleBatteryVoltage(int module) {
        return getUnsignedShortForModule(MODULE_BATTERY_VOLTAGE_FIRST_INDEX, module);
    }

    @Override
    public int getModuleMotorSpeed(int module) {
        return getUnsignedShortForModule(MODULE_MOTOR_SPEED_FIRST_INDEX, module);
    }

    @Override
    public int getModulePressureSensorValue(int module) {
        return getUnsignedShortForModule(MODULE_PRESSURE_SENSOR_VALUE_FIRST_INDEX, module);
    }
}
