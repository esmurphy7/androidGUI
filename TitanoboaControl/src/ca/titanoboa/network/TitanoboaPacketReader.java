package ca.titanoboa.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import ca.titanoboa.model.TitanoboaModel;
import ca.titanoboa.packet.Packet;
import ca.titanoboa.packet.TitanoboaPacket;

/**
 * Packet reader for Titanoboa - gets packets via UDP.
 * 
 * @author Graham
 * 
 */
public class TitanoboaPacketReader extends Thread implements PacketReader {

	private static final int PACKET_SIZE = 170;
	
	private List<Packet> packets;
	private DatagramSocket datagramSocket;
	private int port;

	public TitanoboaPacketReader() {
		packets = new ArrayList<Packet>();
		Packet emptyPacket = new TitanoboaPacket();
		for (int i = 0; i < TitanoboaModel.NUMBER_OF_MODULES; i++) {
			packets.add(emptyPacket);
		}
	}

	@Override
	public void finalize() {
		datagramSocket.close();
	}

	@Override
	public List<Packet> getPackets() {
		return packets;
	}

	/**
	 * Get packets from UDP. Each time, set the appropriate member of the
	 * 'packets' list (module number - 1) to the received packet.
	 */
	@Override
	public void run() {
		try {
			datagramSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			try {
				byte[] buf = new byte[PACKET_SIZE];
				DatagramPacket rawPacket = new DatagramPacket(buf, buf.length);
				datagramSocket.receive(rawPacket);
				Packet packet = new TitanoboaPacket(rawPacket.getData());
				packets.set((packet.getModuleNumber() - 1), packet);
				if (Thread.interrupted()) {
					datagramSocket.close();
					break;
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
