package ca.titanoboa.network;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;

import ca.titanoboa.model.TitanoboaModel;
import ca.titanoboa.packet.*;

/**
 * Packet reader for Titanoboa - gets packets via UDP.
 *
 * @author Graham
 */
public class TitanoboaPacketReader extends Thread implements PacketReader {

    // map keys for packet types
    public static final String HEAD_AND_MODULE_KEY = "headAndModule";
    public static final String HORIZONTAL_SETPOINTS_AND_POSITIONS_KEY = "horizontalSetpointsAndPositions";
    public static final String VERTICAL_SETPOINTS_AND_POSITIONS_KEY = "verticalSetpointsAndPositions";
    public static final String HORIZONTAL_CALIBRATION_KEY = "horizontalCalibration";
    public static final String VERTICAL_CALIBRATION_KEY = "verticalCalibration";

    // location of the packet type byte in the packet
    private final int PACKET_TYPE_INDEX = 0;

    private Map<String, Packet> packets;

    private DatagramSocket datagramSocket;
    private int port;

    public TitanoboaPacketReader() {
        byte[] emptyBytes = new byte[TitanoboaPacket.PACKET_SIZE];
        packets = new HashMap<String, Packet>();

        packets.put(HEAD_AND_MODULE_KEY, new TitanoboaHeadAndModuleGeneralPacket(emptyBytes));
        packets.put(HORIZONTAL_SETPOINTS_AND_POSITIONS_KEY, new TitanoboaHorizontalSetpointsAndPositionsPacket(emptyBytes));
        packets.put(VERTICAL_SETPOINTS_AND_POSITIONS_KEY, new TitanoboaVerticalSetpointsAndPositionsPacket(emptyBytes));
        packets.put(HORIZONTAL_CALIBRATION_KEY, new TitanoboaHorizontalCalibrationPacket(emptyBytes));
        packets.put(VERTICAL_CALIBRATION_KEY, new TitanoboaVerticalCalibrationPacket(emptyBytes));
    }

    public Map<String, Packet> getPackets() {
        return packets;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void finalize() {
        if (datagramSocket != null) {
            datagramSocket.close();
        }
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
            e.printStackTrace();
        }
        while (true) {
            try {
                byte[] buf = new byte[TitanoboaPacket.PACKET_SIZE];
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                datagramSocket.receive(datagramPacket);
                byte[] rawPacket = datagramPacket.getData();
                createIdentifiedPacket(rawPacket);

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

    private void createIdentifiedPacket(byte[] rawPacket) {
        int packetType = (rawPacket[PACKET_TYPE_INDEX] & 0xff);
        switch (packetType) {
            case 1:
                packets.put(HEAD_AND_MODULE_KEY, new TitanoboaHeadAndModuleGeneralPacket(rawPacket));
                break;
            case 2:
                packets.put(HORIZONTAL_SETPOINTS_AND_POSITIONS_KEY, new TitanoboaHorizontalSetpointsAndPositionsPacket(rawPacket));
                break;
            case 3:
                packets.put(VERTICAL_SETPOINTS_AND_POSITIONS_KEY, new TitanoboaVerticalSetpointsAndPositionsPacket(rawPacket));
                break;
            case 4:
                packets.put(HORIZONTAL_CALIBRATION_KEY, new TitanoboaHorizontalCalibrationPacket(rawPacket));
                break;
            case 5:
                packets.put(VERTICAL_CALIBRATION_KEY, new TitanoboaVerticalCalibrationPacket(rawPacket));
                break;
            default:
                throw new IllegalArgumentException("Unknown packet type");
        }
    }
}
