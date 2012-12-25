package ca.titanoboa.packet;

/**
 * Titanoboa vertical setpoints and positions packet.
 *
 * @author Graham
 * Date: 12/16/12
 * Time: 10:50 AM
 */
public class TitanoboaVerticalSetpointsAndPositionsPacket extends TitanoboaPacket implements VerticalSetpointsAndPositionsPacket {

    // sizes in bytes of each module part and vertebra part of the packet, for calculating byte numbers
    private final int VERTEBRA_PART_SIZE = 3;
    private final int MODULE_PART_SIZE = VERTEBRA_PART_SIZE * TitanoboaPacket.VERTEBRAE_PER_MODULE;

    // first byte indices for different pieces of information
    private final int VERT_SETPOINT_ANGLE_FIRST_INDEX = 1;
    private final int VERT_SENSOR_READING_FIRST_INDEX = 2;

    public TitanoboaVerticalSetpointsAndPositionsPacket(byte[] rawPacket) {
        super(rawPacket);
        super.setModulePartSize(MODULE_PART_SIZE);
        super.setVertebraPartSize(VERTEBRA_PART_SIZE);
    }

    @Override
    public int getVerticalSetpointAngle(int module, int vertebra) {
        return getByteAsIntForModuleAndVertebra(VERT_SETPOINT_ANGLE_FIRST_INDEX, module, vertebra);
    }

    @Override
    public int getVerticalSensorValue(int module, int vertebra) {
        return getUnsignedShortForModuleAndVertebra(VERT_SENSOR_READING_FIRST_INDEX, module, vertebra);
    }
}
