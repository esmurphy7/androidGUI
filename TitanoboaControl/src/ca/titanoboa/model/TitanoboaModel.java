package ca.titanoboa.model;

import java.util.List;

import ca.titanoboa.model.module.Module;
import ca.titanoboa.packet.Packet;

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

	@Override
	public void updateData(List<Packet> packets) {
		// have to subtract 1 because module numbers are 1, 2, 3, 4, but our
		// list indices are 0, 1, 2, 3
		for (int i = 0; i < NUMBER_OF_MODULES; i++)
			if (packets.size() > i) {
				Packet updatePacketForModule = packets.get(i);
				modules.get(i).updateData(updatePacketForModule);
			}
			
	}
}
