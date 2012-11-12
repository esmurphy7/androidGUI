package ca.titanoboa.model;

import java.util.List;

import ca.titanoboa.model.module.Module;
import ca.titanoboa.packet.Packet;

/**
 * Represents the whole snake.
 * 
 * @author Graham
 * 
 */
public class TitanoboaModel implements Model {

	public static final int NUMBER_OF_MODULES = 4;
	private List<Module> modules;

	@Override
	public List<Module> getModules() {
		return modules;
	}

	@Override
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	/**
	 * Update the data for the entire model, module by module.
	 * @param packets The list of packets.
	 */
	@Override
	public void updateData(List<Packet> packets) {
		for (int i = 0; i < NUMBER_OF_MODULES; i++)
			if (packets.size() > i) {
				Packet updatePacketForModule = packets.get(i);
				modules.get(i).updateData(updatePacketForModule);
			}

	}
}
