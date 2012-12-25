package ca.titanoboa.packet;

/**
 * Interface for Titanoboa vertical setpoints and positions packet.
 *
 * @author Graham
 * Date: 12/16/12
 * Time: 8:33 AM
 */
public interface VerticalSetpointsAndPositionsPacket extends Packet {
    public int getVerticalSetpointAngle(int module, int vertebra);

    public int getVerticalSensorValue(int module, int vertebra);
}
