package ca.titanoboa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.titanoboa.model.head.Head;
import ca.titanoboa.model.head.TitanoboaHead;
import ca.titanoboa.model.module.Module;
import ca.titanoboa.model.module.TitanoboaModule;
import ca.titanoboa.packet.*;

/**
 * Represents the whole snake.
 * 
 * @author Graham
 * 
 */
public class TitanoboaModel implements Model {

	public static final int NUMBER_OF_MODULES = 4;
    private final Head head;
	private final List<Module> modules;
	private int selectedModule;

    /**
     * Constructor. Sets up modules, which in turn set up vertebrae.
     */
    public TitanoboaModel() {
        head = new TitanoboaHead();
        modules = new ArrayList<Module>();
        for (int i = 1; i <= NUMBER_OF_MODULES; i++) {
            Module module = new TitanoboaModule(i);
            modules.add(module);
        }
    }

    /** {@inheritDoc} **/
    @Override
    public Head getHead() {
        return head;
    }

    /** {@inheritDoc} **/
	@Override
	public List<Module> getModules() {
		return modules;
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

//	/** {@inheritDoc} **/
//	@Override
//	public void updateDataAll(List<Packet> packets) {
//		for (int i = 0; i < NUMBER_OF_MODULES; i++)
//			if (packets.size() > i) {
//				Packet updatePacketForModule = packets.get(i);
//				modules.get(i).updateData(updatePacketForModule);
//			}
//	}
//
//	/** {@inheritDoc} **/
//	@Override
//	public void updateDataSelected(int selectedModule, List<Packet> packets) {
//		modules.get(selectedModule - 1).updateData(packet);
//	}

    /** {@inheritDoc} **/
    public void updateDataAll(Map<String, Packet> packets) {
        for (Module module : modules) {
            module.updateData(packets);
        }
    }

    /** {@inheritDoc} **/
    public void updateDataSelected(int selectedModule, Map<String, Packet> packets) {
        modules.get(selectedModule - 1).updateData(packets);
    }
}
