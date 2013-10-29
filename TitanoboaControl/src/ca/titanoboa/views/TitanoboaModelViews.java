package ca.titanoboa.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.titanoboa.model.Model;
import ca.titanoboa.model.TitanoboaModel;
import ca.titanoboa.views.head.HeadViews;
import ca.titanoboa.views.head.TitanoboaHeadViews;
import ca.titanoboa.views.module.ModuleViews;
import ca.titanoboa.packet.*;
import ca.titanoboa.views.module.TitanoboaModuleViews;

/**
 * Represents the whole snake.
 * 
 * @author Graham
 * 
 */
public class TitanoboaModelViews implements ModelViews {

    private Model model;
	public static final int NUMBER_OF_MODULES = 4;
    private final HeadViews headViews;
	private final List<ModuleViews> moduleViewses;
	private int selectedModule;

    /**
     * Constructor. Sets up moduleViewses, which in turn set up vertebrae.
     */
    public TitanoboaModelViews(Model model) {
        this.model = model;
        headViews = new TitanoboaHeadViews();
        moduleViewses = new ArrayList<ModuleViews>();
        for (int i = 1; i <= NUMBER_OF_MODULES; i++) {
            ModuleViews moduleViews = new TitanoboaModuleViews(i, model.getModules().get(i-1));
            moduleViewses.add(moduleViews);
        }
    }

    /** {@inheritDoc} **/
    @Override
    public HeadViews getHeadViews() {
        return headViews;
    }

    /** {@inheritDoc} **/
	@Override
	public List<ModuleViews> getModuleViewses() {
		return moduleViewses;
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
    public void updateViewsAll() {
        for (ModuleViews moduleViews : moduleViewses) {
            moduleViews.updateViews();
        }
    }

    /** {@inheritDoc} **/
    public void updateViewsSelected(int selectedModule) {
        moduleViewses.get(selectedModule - 1).updateViews();
    }
}
