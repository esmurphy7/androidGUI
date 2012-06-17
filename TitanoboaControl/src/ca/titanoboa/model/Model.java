package ca.titanoboa.model;

import java.util.List;

import ca.titanoboa.model.module.Module;
import ca.titanoboa.packet.Packet;

public interface Model {
	public List<Module> getModules();

	public void setModules(List<Module> modules);
	
	public void updateData(List<Packet> packets);
}
