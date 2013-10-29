package ca.titanoboa.views;

import java.util.List;
import java.util.Map;

import ca.titanoboa.views.head.HeadViews;
import ca.titanoboa.views.module.ModuleViews;
import ca.titanoboa.packet.Packet;

/**
 * Represents the whole physical object.
 * @author Graham
 *
 */
public interface ModelViews {
    /**
     * Get the head.
     * @return the {@link ca.titanoboa.views.head.HeadViews}
     */
    HeadViews getHeadViews();

    /**
	 * Get the list of modules.
	 * @return a {@link java.util.List} of Modules
	 */
	List<ModuleViews> getModuleViewses();
	
	/**
	 * Update the views for the entire model, module by module.
	 */
	void updateViewsAll();
	
	/**
	 * Update the views for just the selected module.
	 *
     * @param selectedModule the selected module
	 */
	void updateViewsSelected(int selectedModule);

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
