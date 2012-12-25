package ca.titanoboa.packet;

/**
 * Titanoboa vertical calibration packet.
 *
 * @author Graham
 *         Date: 12/16/12
 *         Time: 10:59 AM
 */
public class TitanoboaVerticalCalibrationPacket extends TitanoboaPacket implements VerticalCalibrationPacket {

    // sizes in bytes of each module part and vertebra part of the packet, for calculating byte numbers
    private final int VERTEBRA_PART_SIZE = 4;
    private final int MODULE_PART_SIZE = VERTEBRA_PART_SIZE * TitanoboaPacket.VERTEBRAE_PER_MODULE;

    // first byte indices for different pieces of information
    private final int VERT_HIGH_CALIBRATION_FIRST_INDEX = 1;
    private final int VERT_LOW_CALIBRATION_FIRST_INDEX = 3;

    public TitanoboaVerticalCalibrationPacket(byte[] rawPacket) {
        super(rawPacket);
        super.setModulePartSize(MODULE_PART_SIZE);
        super.setVertebraPartSize(VERTEBRA_PART_SIZE);
    }

    @Override
    public int getVerticalHighCalibration(int module, int vertebra) {
        return getUnsignedShortForModuleAndVertebra(VERT_HIGH_CALIBRATION_FIRST_INDEX, module, vertebra);
    }

    @Override
    public int getVerticalLowCalibration(int module, int vertebra) {
        return getUnsignedShortForModuleAndVertebra(VERT_LOW_CALIBRATION_FIRST_INDEX, module, vertebra);
    }
}
