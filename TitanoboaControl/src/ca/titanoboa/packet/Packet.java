package ca.titanoboa.packet;

import java.util.UUID;

public interface Packet {
    UUID getUuid();

    byte[] getRawPacket();

    void setRawPacket(byte[] rawPacket);

    byte getByteByNumber(int byteNumber);

    int getByteAsIntByNumber(int byteNumber);

	int getPacketType();
}
