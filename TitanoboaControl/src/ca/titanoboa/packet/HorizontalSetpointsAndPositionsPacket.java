package ca.titanoboa.packet;

/**
 * Interface for Titanoboa horizontal setpoints and positions packet.
 *
 * @author Graham
 * Date: 12/16/12
 * Time: 8:33 AM
 */
public interface HorizontalSetpointsAndPositionsPacket extends Packet {
    public int getHorizontalSetpointAngle(int module, int vertebra);

    public int getHorizontalSensorValue(int module, int vertebra);
}
