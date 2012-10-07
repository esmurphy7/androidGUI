package ca.titanoboa.packet;

/**
 * Represents a packet. Each packet contains all the data for a particular
 * module. Most of the get* methods convert the relevant bits to an int for the
 * app. TODO: Add the packet structure.
 * 
 * @author Graham
 * 
 */
public class TitanoboaPacket implements Packet {

	private byte[] rawPacket;

	public TitanoboaPacket() {
		this.rawPacket = new byte[170];
	}

	public TitanoboaPacket(byte[] rawPacket) {
		this.rawPacket = rawPacket;
	}

	@Override
	public byte[] getRawPacket() {
		return rawPacket;
	}

	@Override
	public void setRawPacket(byte[] rawPacket) {
		this.rawPacket = rawPacket;
	}

	@Override
	public byte getByteByNumber(int byteNumber) {
		return rawPacket[byteNumber];
	}

	@Override
	public int getTitanoboaPacketType() {
		return (int) (rawPacket[0] & 0xff);
	}

	@Override
	public int getPacketVersion() {
		return (int) (rawPacket[1] & 0xff);
	}

	@Override
	public int getModuleNumber() {
		return (int) (rawPacket[2] & 0xff);
	}

	@Override
	public int getBatteryLevel() {
		return (int) (((rawPacket[3] & 0xff) << 2) | ((rawPacket[4] & 0xff) >> 6));
	}

	@Override
	public int getMotorSpeed() {
		return (int) (((rawPacket[5] & 0xff) << 2) | ((rawPacket[6] & 0xff) >> 6));
	}

	@Override
	public int getSetpointAngleH(int vertebra) {
		return (int) (rawPacket[20 + (vertebra * 30)] & 0xff);
	}

	@Override
	public int getCurrentAngleH(int vertebra) {
		return (int) (rawPacket[21 + (vertebra * 30)] & 0xff);
	}

	@Override
	public int getRawSensorValueH(int vertebra) {
		return (int) (((rawPacket[22 + (vertebra * 30)] & 0xff) << 2) | ((rawPacket[23 + (vertebra * 30)] & 0xff) >> 6));
	}

	@Override
	public int getSensorCalibrationHighH(int vertebra) {
		return (int) (((rawPacket[24 + (vertebra * 30)] & 0xff) << 2) | ((rawPacket[25 + (vertebra * 30)] & 0xff) >> 6));
	}

	@Override
	public int getSensorCalibrationLowH(int vertebra) {
		return (int) (((rawPacket[26 + (vertebra * 30)] & 0xff) << 2) | ((rawPacket[27 + (vertebra * 30)] & 0xff) >> 6));
	}

	@Override
	public int getSetpointAngleV(int vertebra) {
		return (int) (rawPacket[28 + (vertebra * 30)] & 0xff);
	}

	@Override
	public int getCurrentAngleV(int vertebra) {
		return (int) (rawPacket[29 + (vertebra * 30)] & 0xff);
	}

	@Override
	public int getRawSensorValueV(int vertebra) {
		return (int) (((rawPacket[30 + (vertebra * 30)] & 0xff) << 2) | ((rawPacket[31 + (vertebra * 30)] & 0xff) >> 6));
	}

	@Override
	public int getSensorCalibrationHighV(int vertebra) {
		return (int) (((rawPacket[32 + (vertebra * 30)] & 0xff) << 2) | ((rawPacket[33 + (vertebra * 30)] & 0xff) >> 6));
	}

	@Override
	public int getSensorCalibrationLowV(int vertebra) {
		return (int) (((rawPacket[34 + (vertebra * 30)] & 0xff) << 2) | ((rawPacket[35 + (vertebra * 30)] & 0xff) >> 6));
	}

}
