package ca.titanoboa.packet;

/**
 * Interface for Titanoboa horizontal setpoints and positions packet.
 *
 * @author Graham
 * Date: 12/16/12
 * Time: 8:33 AM
 */
public interface SetpointsAndPositionsPacket extends Packet {
    public int getHorizontalSetpointAngle(int module, int vertebra);

    public int getHorizontalAngle(int module, int vertebra);

    public int getVerticalSetpointAngle(int module, int vertebra);

    public int getVerticalAngle(int module, int vertebra);
}
