package ca.titanoboa.packet;

/**
 * Interface for Titanoboa horizontal calibration packet.
 *
 * @author Graham
 * Date: 12/16/12
 * Time: 8:33 AM
 */
public interface HorizontalCalibrationPacket extends Packet {
    public int getHorizontalHighCalibration(int module, int vertebra);

    public int getHorizontalLowCalibration(int module, int vertebra);
}
