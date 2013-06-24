package ca.titanoboa.packet;

/**
 * Titanoboa horizontal setpoints and positions packet.
 *
 * @author Graham
 *         Date: 12/16/12
 *         Time: 9:46 AM
 */
public class TitanoboaSetpointsAndPositionsPacket extends TitanoboaPacket implements SetpointsAndPositionsPacket {

    // sizes in bytes of each module part and vertebra part of the packet, for calculating byte numbers
    private final int VERTEBRA_PART_SIZE = 4;
    private final int MODULE_PART_SIZE = VERTEBRA_PART_SIZE * TitanoboaPacket.VERTEBRAE_PER_MODULE;

    // first byte indices for different pieces of information
    private final int HORIZ_SETPOINT_ANGLE_FIRST_INDEX = 7;
    private final int HORIZ_ANGLE_FIRST_INDEX = 8;
    private final int VERTICAL_SETPOINT_ANGLE_FIRST_INDEX = 9;
    private final int VERTICAL_ANGLE_FIRST_INDEX = 10;

    public TitanoboaSetpointsAndPositionsPacket(byte[] rawPacket) {
        super(rawPacket);
        super.setModulePartSize(MODULE_PART_SIZE);
        super.setVertebraPartSize(VERTEBRA_PART_SIZE);
    }

    @Override
    public int getHorizontalSetpointAngle(int module, int vertebra) {
        return getByteAsIntForModuleAndVertebra(HORIZ_SETPOINT_ANGLE_FIRST_INDEX, module, vertebra);
    }

    @Override
    public int getHorizontalAngle(int module, int vertebra) {
        return getByteAsIntForModuleAndVertebra(HORIZ_ANGLE_FIRST_INDEX, module, vertebra);
    }

    @Override
    public int getVerticalSetpointAngle(int module, int vertebra) {
        return getByteAsIntForModuleAndVertebra(VERTICAL_SETPOINT_ANGLE_FIRST_INDEX, module, vertebra);
    }

    @Override
    public int getVerticalAngle(int module, int vertebra) {
        return getByteAsIntForModuleAndVertebra(VERTICAL_ANGLE_FIRST_INDEX, module, vertebra);
    }
}
