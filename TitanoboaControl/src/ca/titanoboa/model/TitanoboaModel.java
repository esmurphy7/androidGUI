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
	private int selectedModule;

	/** {@inheritDoc} **/
	@Override
	public List<Module> getModules() {
		return modules;
	}

	/** {@inheritDoc} **/
	@Override
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	/** {@inheritDoc} **/
	@Override
	public int getSelectedModule() {
		return selectedModule;
	}

	/** {@inheritDoc} **/
	@Override
	public void setSelectedModule(int selectedModule) {
		this.selectedModule = selectedModule;
	}

	/** {@inheritDoc} **/
	@Override
	public void updateDataAll(List<Packet> packets) {
		for (int i = 0; i < NUMBER_OF_MODULES; i++)
			if (packets.size() > i) {
				Packet updatePacketForModule = packets.get(i);
				modules.get(i).updateData(updatePacketForModule);
			}
	}
	
	/** {@inheritDoc} **/
	@Override
	public void updateDataSelected(Packet packet, int selectedModule) {
		modules.get(selectedModule - 1).updateData(packet);
	}
	
}
