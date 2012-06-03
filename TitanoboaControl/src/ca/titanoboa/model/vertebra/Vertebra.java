package ca.titanoboa.model.vertebra;

import java.util.List;

import ca.titanoboa.model.actuator.Actuator;
import ca.titanoboa.packet.Packet;

public interface Vertebra {
	public int getVertebraNumber();

	public void setVertebraNumber(int vertebraNumber);
	
	public List<Actuator> getActuators();

	public void setActuators(List<Actuator> actuators);

	public void updateData(Packet packet);
}
