package ca.titanoboa;

import java.util.*;

import ca.titanoboa.packet.Packet;
import ca.titanoboa.packet.TitanoboaHeadAndModuleGeneralPacket;

/**
 * Created by Evan Murphy on 7/22/2014.
 */
public class Simulation extends Thread implements Runnable{

    public static final String HEAD_AND_MODULE_KEY = "headAndModule";
    private final int PACKET_SIZE = 125;
    private Map<String, Packet> packets;

    public Simulation(){
        packets = new HashMap<String, Packet>();
    }

    @Override
    public void run(){
        //randomize mock packets
        byte[] rawPacket = new byte[125];

        packets.put(HEAD_AND_MODULE_KEY, new TitanoboaHeadAndModuleGeneralPacket(rawPacket));
    }

    private class Battery {
        private int MAX_VOLTAGE;
        private int DECAY_RATE;

        private int batteryLevel;

        public Battery(){
            //generate random byte
        }

        public void decay(){
            batteryLevel = batteryLevel*DECAY_RATE;
        }
    }

}
