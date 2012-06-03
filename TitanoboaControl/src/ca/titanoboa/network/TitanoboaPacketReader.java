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

public class TitanoboaPacketReader extends Thread implements PacketReader {

	private List<Packet> packets;
	private DatagramSocket datagramSocket;
	private String address;
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
				byte[] buf = new byte[170];
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
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
