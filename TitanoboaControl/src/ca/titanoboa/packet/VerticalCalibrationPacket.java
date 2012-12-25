package ca.titanoboa.packet;

/**
 * Interface for Titanoboa vertical calibration packet.
 *
 * @author Graham
 * Date: 12/16/12
 * Time: 8:33 AM
 */
public interface VerticalCalibrationPacket extends Packet {
    public int getVerticalHighCalibration(int module, int vertebra);

    public int getVerticalLowCalibration(int module, int vertebra);
}
