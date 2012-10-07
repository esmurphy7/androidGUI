package ca.titanoboa.model.vertebra;

import java.util.List;

import ca.titanoboa.model.actuator.Actuator;
import ca.titanoboa.packet.Packet;

/**
 * Represents a single vertebra. As of Oct 2012 each vertebra contains 2
 * actuators, horizontal (0) and vertical (1). It might make more conceptual
 * sense to use a Map instead of a List, but I don't think it's a big deal,
 * particularly given the getHorizontalActuator and getVerticalActuator
 * shortcuts.
 * 
 * @author Graham
 * 
 */
public class TitanoboaVertebra implements Vertebra {

	public static int ACTUATORS_PER_VERTEBRA = 2;

	private List<Actuator> actuators;
	private int vertebraNumber;

	public int getVertebraNumber() {
		return vertebraNumber;
	}

	public void setVertebraNumber(int vertebraNumber) {
		this.vertebraNumber = vertebraNumber;
	}

	@Override
	public List<Actuator> getActuators() {
		return actuators;
	}

	@Override
	public void setActuators(List<Actuator> actuators) {
		this.actuators = actuators;
	}

	public Actuator getHorizontalActuator() {
		return actuators.get(0);
	}

	public void setHorizontalActuator(Actuator actuator) {
		actuators.set(0, actuator);
	}

	public Actuator getVerticalActuator() {
		return actuators.get(1);
	}

	public void setVerticalActuator(Actuator actuator) {
		actuators.set(1, actuator);
	}

	/**
	 * Tell all actuators in this vertebra to update themselves.
	 * 
	 * @param packet
	 *            The packet passed down from the module.
	 */
	@Override
	public void updateData(Packet packet) {
		for (Actuator actuator : actuators) {
			actuator.updateData(packet, vertebraNumber);
		}

	}
}
