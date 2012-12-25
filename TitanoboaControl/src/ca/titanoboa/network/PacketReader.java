package ca.titanoboa.network;

import java.util.List;
import java.util.Map;

import ca.titanoboa.packet.*;

public interface PacketReader extends Runnable {
	public void run();

	public int getPort();

    public Map<String, Packet> getPackets();

	public void setPort(int port);
}
