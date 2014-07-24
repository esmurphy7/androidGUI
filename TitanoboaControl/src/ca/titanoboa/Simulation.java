package ca.titanoboa;

import java.util.*;

import ca.titanoboa.network.TitanoboaPacketReader;
import ca.titanoboa.packet.HeadAndModuleGeneralPacket;
import ca.titanoboa.packet.Packet;
import ca.titanoboa.packet.TitanoboaHeadAndModuleGeneralPacket;

/**
 * Created by Evan Murphy on 7/22/2014.
 */
public class Simulation extends Thread implements Runnable{

    public static final String HEAD_AND_MODULE_KEY = "headAndModule";
    private final int MODULE_BATTERY_VOLTAGE_FIRST_INDEX = 4;
    private final int PACKET_SIZE = 125;
    private Map<String, Packet> packets;
    private Battery battery;

    public Simulation(){
        packets = new HashMap<String, Packet>();
        battery = new Battery();
    }

    public Map<String, Packet> getPackets(){
        return packets;
    }

    @Override
    public void run(){
        //randomize mock packet
        byte[] rawPacket = new byte[PACKET_SIZE];
        new Random().nextBytes(rawPacket);
        TitanoboaHeadAndModuleGeneralPacket generalPacket = new TitanoboaHeadAndModuleGeneralPacket(rawPacket);
        //battery.decay();
        //generalPacket.setBatteryLevel(battery.batteryLevel);
        packets.put(HEAD_AND_MODULE_KEY, new TitanoboaHeadAndModuleGeneralPacket(rawPacket));
    }

    private class Battery {
        private int MAX_VOLTAGE = 25000;
        private double DECAY_RATE = 0.9; //20% decay per call

        private int batteryLevel;

        public Battery(){
            batteryLevel = MAX_VOLTAGE;
        }

        public void decay(){
            batteryLevel = (int)(batteryLevel*DECAY_RATE);
        }
    }

}
