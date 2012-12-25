package ca.titanoboa.model;

import java.util.List;
import java.util.Map;

import ca.titanoboa.model.head.Head;
import ca.titanoboa.model.module.Module;
import ca.titanoboa.packet.Packet;

/**
 * Represents the whole physical object.
 * @author Graham
 *
 */
public interface Model {
    /**
     * Get the head.
     * @return the {@link Head}
     */
    Head getHead();

    /**
	 * Get the list of modules.
	 * @return a {@link java.util.List} of Modules
	 */
	List<Module> getModules();
	
	/**
	 * Update the data for the entire model, module by module.
	 * 
	 * @param packets
	 *            The list of packets.
	 */
	void updateDataAll(Map<String, Packet> packets);
	
	/**
	 * Update the data for just the selected module.
	 *
     * @param selectedModule the selected module
	 * @param packets The list of packets.
	 */
	void updateDataSelected(int selectedModule, Map<String, Packet> packets);

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
