package ca.titanoboa.network;

import java.util.List;

import ca.titanoboa.packet.Packet;

public interface PacketReader extends Runnable {
	public List<Packet> getPackets();

	public void run();

	public String getAddress();

	public void setAddress(String address);

	public int getPort();

	public void setPort(int port);
}
