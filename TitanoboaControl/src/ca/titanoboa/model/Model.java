package ca.titanoboa.model;

import java.util.List;

import ca.titanoboa.model.module.Module;
import ca.titanoboa.packet.Packet;

/**
 * Represents the whole physical object.
 * @author Graham
 *
 */
public interface Model {
	/**
	 * Get the list of modules.
	 * @return a {@link java.util.List} of Modules
	 */
	List<Module> getModules();

	/**
	 * Set the list of modules.
	 * @param modules a {@link java.util.list} of Modules to set
	 */
	void setModules(List<Module> modules);
	
	/**
	 * Update the data for the entire model, module by module.
	 * 
	 * @param packets
	 *            The list of packets.
	 */
	void updateData(List<Packet> packets);

	/**
	 * Get the selected module.
	 * @return the selected module
	 */
	int getSelectedModule();

	/**
	 * Set the selected module.
	 * @param selectedModule the selected module to set
	 */
	void setSelectedModule(int selectedModule);
}
