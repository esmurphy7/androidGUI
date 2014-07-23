package ca.titanoboa.packet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;

public class PacketStructureTest {

    private final int PACKET_SIZE=125;
    private final int HEAD_BATTERY_VOLTAGE_INDEX = 2;
    //part size 6 for module general packet
    private final int MODULE_PART_SIZE = 6;
    private final int MODULE_NUMBER = 3;
    byte[] rawPacket;

    public PacketStructureTest(){
        rawPacket = new byte[PACKET_SIZE];
        new Random().nextBytes(rawPacket);
        //System.out.println("packet length: "+rawPacket.length);
        for(int i=0; i<rawPacket.length; i++){
            if(i%2==0){
                //System.out.println("Packet: "+rawPacket[i]);
            }
        }

        int unsignedShort = getUnsignedShortForModuleAndVertebra(HEAD_BATTERY_VOLTAGE_INDEX, MODULE_NUMBER );
        System.out.printf("Unsignedshort for module %d: %d", MODULE_NUMBER, unsignedShort);
    }

    protected int getUnsignedShortForModuleAndVertebra(int firstByteNumber, int module) {
        int startByte = calculateStartByte(firstByteNumber, module);
        System.out.println("Startbyte: "+startByte);
        return ((getByteByNumber(startByte) & 0xff) << 8) | (getByteByNumber(++startByte) & 0xff);
    }

    public byte getByteByNumber(int byteNumber){
        return rawPacket[byteNumber];
    }

    private int calculateStartByte(int firstByteNumber, int module) {
        int startByte = firstByteNumber + (MODULE_PART_SIZE  * (module - 1));
        return startByte;
    }

    public static void main(String args[]){
        PacketStructureTest reader = new PacketStructureTest();
    }
}