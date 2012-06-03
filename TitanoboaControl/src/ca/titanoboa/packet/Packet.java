package ca.titanoboa.packet;

public interface Packet {
	public byte[] getRawPacket();

	public void setRawPacket(byte[] rawPacket);

	public byte getByteByNumber(int byteNumber);

	public int getTitanoboaPacketType();

	public int getPacketVersion();

	public int getModuleNumber();

	public int getBatteryLevel();

	public int getMotorSpeed();

	public int getSetpointAngleH(int vertebra);

	public int getCurrentAngleH(int vertebra);

	public int getRawSensorValueH(int vertebra);

	public int getSensorCalibrationHighH(int vertebra);

	public int getSensorCalibrationLowH(int vertebra);

	public int getSetpointAngleV(int vertebra);

	public int getCurrentAngleV(int vertebra);

	public int getRawSensorValueV(int vertebra);

	public int getSensorCalibrationHighV(int vertebra);

	public int getSensorCalibrationLowV(int vertebra);
}
