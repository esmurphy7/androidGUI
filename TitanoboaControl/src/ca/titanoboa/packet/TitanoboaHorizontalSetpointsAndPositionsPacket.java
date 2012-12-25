package ca.titanoboa.packet;

/**
 * Titanoboa horizontal setpoints and positions packet.
 *
 * @author Graham
 *         Date: 12/16/12
 *         Time: 9:46 AM
 */
public class TitanoboaHorizontalSetpointsAndPositionsPacket extends TitanoboaPacket implements HorizontalSetpointsAndPositionsPacket {

    // sizes in bytes of each module part and vertebra part of the packet, for calculating byte numbers
    private final int VERTEBRA_PART_SIZE = 3;
    private final int MODULE_PART_SIZE = VERTEBRA_PART_SIZE * TitanoboaPacket.VERTEBRAE_PER_MODULE;

    // first byte indices for different pieces of information
    private final int HORIZ_SETPOINT_ANGLE_FIRST_INDEX = 1;
    private final int HORIZ_SENSOR_READING_FIRST_INDEX = 2;

    public TitanoboaHorizontalSetpointsAndPositionsPacket(byte[] rawPacket) {
        super(rawPacket);
        super.setModulePartSize(MODULE_PART_SIZE);
        super.setVertebraPartSize(VERTEBRA_PART_SIZE);
    }

    @Override
    public int getHorizontalSetpointAngle(int module, int vertebra) {
        return getByteAsIntForModuleAndVertebra(HORIZ_SETPOINT_ANGLE_FIRST_INDEX, module, vertebra);
    }

    @Override
    public int getHorizontalSensorValue(int module, int vertebra) {
        return getUnsignedShortForModuleAndVertebra(HORIZ_SENSOR_READING_FIRST_INDEX, module, vertebra);
    }
}
