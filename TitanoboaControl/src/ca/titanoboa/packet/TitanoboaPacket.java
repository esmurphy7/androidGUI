package ca.titanoboa.packet;

import java.util.UUID;

/**
 * Generic packet methods. Extended by all specific packet implementations.
 * 
 * @author Graham
 */
public class TitanoboaPacket implements Packet {

    public static final int VERTEBRAE_PER_MODULE = 5;
    public static final int PACKET_SIZE = 125;

    private final UUID uuid;
	private byte[] rawPacket;
    private int modulePartSize;
    private int vertebraPartSize;

    /**
     * Constructor - create empty packet.
     */
	public TitanoboaPacket() {
		this(new byte[PACKET_SIZE]);
	}

    /**
     * Constructor - create with contents of rawPacket.
     * @param rawPacket the rawPacket contents to set
     */
	public TitanoboaPacket(byte[] rawPacket) {
		this.rawPacket = rawPacket;
        this.uuid = UUID.randomUUID();
	}

    @Override
    public UUID getUuid() {
        return uuid;
    }

	@Override
	public byte[] getRawPacket() {
		return rawPacket;
	}

	@Override
	public void setRawPacket(byte[] rawPacket) {
		this.rawPacket = rawPacket;
	}

	@Override
	public byte getByteByNumber(int byteNumber) {
		return rawPacket[byteNumber];
	}

    @Override
    public int getByteAsIntByNumber(int byteNumber) {
        return getByteByNumber(byteNumber) & 0xff;
    }

	@Override
	public int getPacketType() {
		return (getByteByNumber(0) & 0xff);
	}

    protected int getModulePartSize() {
        return modulePartSize;
    }

    protected void setModulePartSize(int modulePartSize) {
        this.modulePartSize = modulePartSize;
    }

    protected int getVertebraPartSize() {
        return vertebraPartSize;
    }

    protected void setVertebraPartSize(int vertebraPartSize) {
        this.vertebraPartSize = vertebraPartSize;
    }

    protected int getByteAsIntForModuleAndVertebra(int byteNumber, int module, int vertebra) {
        return getByteByNumber(calculateStartByte(byteNumber, module, vertebra)) & 0xff;
    }

    protected int getByteAsIntForModule(int byteNumber, int module) {
        return getByteAsIntForModuleAndVertebra(byteNumber, module, 0);
    }

    protected int getUnsignedShortForModuleAndVertebra(int firstByteNumber, int module, int vertebra) {
        int startByte = calculateStartByte(firstByteNumber, module, vertebra);
        return ((getByteByNumber(startByte) & 0xff) << 8) | (getByteByNumber(++startByte) & 0xff);
    }

    protected int getUnsignedShortForModule(int startByte, int module) {
        return getUnsignedShortForModuleAndVertebra(startByte, module, 0);
    }

    protected int getUnsignedShortByStartByte(int startByte) {
        return ((getByteByNumber(startByte) & 0xff) << 8) | (getByteByNumber(++startByte) & 0xff);
    }

    private int calculateStartByte(int firstByteNumber, int module, int vertebra) {
        int startByte = firstByteNumber + (getModulePartSize() * (module - 1));
        if (vertebra > 0) {
            startByte += (getVertebraPartSize() * (vertebra - 1));
        }
        return startByte;
    }
}
