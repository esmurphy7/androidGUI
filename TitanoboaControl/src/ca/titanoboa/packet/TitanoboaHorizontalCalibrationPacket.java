package ca.titanoboa.packet;

/**
 * Titanoboa horizontal calibration packet.
 *
 * @author Graham
 *         Date: 12/16/12
 *         Time: 10:52 AM
 */
public class TitanoboaHorizontalCalibrationPacket extends TitanoboaPacket implements HorizontalCalibrationPacket {

    // sizes in bytes of each module part and vertebra part of the packet, for calculating byte numbers
    private final int VERTEBRA_PART_SIZE = 4;
    private final int MODULE_PART_SIZE = VERTEBRA_PART_SIZE * TitanoboaPacket.VERTEBRAE_PER_MODULE;

    // first byte indices for different pieces of information
    private final int HORIZ_HIGH_CALIBRATION_FIRST_INDEX = 1;
    private final int HORIZ_LOW_CALIBRATION_FIRST_INDEX = 3;

    public TitanoboaHorizontalCalibrationPacket(byte[] rawPacket) {
        super(rawPacket);
        super.setModulePartSize(MODULE_PART_SIZE);
        super.setVertebraPartSize(VERTEBRA_PART_SIZE);
    }

    @Override
    public int getHorizontalHighCalibration(int module, int vertebra) {
        return getUnsignedShortForModuleAndVertebra(HORIZ_HIGH_CALIBRATION_FIRST_INDEX, module, vertebra);
    }

    @Override
    public int getHorizontalLowCalibration(int module, int vertebra) {
        return getUnsignedShortForModuleAndVertebra(HORIZ_LOW_CALIBRATION_FIRST_INDEX, module, vertebra);
    }
}
