package ca.titanoboa.model.head;

/**
 * Titanoboa head.
 *
 * @author Graham
 *         Date: 12/16/12
 *         Time: 2:24 PM
 */
public class TitanoboaHead implements Head {

    private int numberOfModulesConnected;
    private int batteryVoltage;

    @Override
    public int getNumberOfModulesConnected() {
        return numberOfModulesConnected;
    }

    @Override
    public int getBatteryVoltage() {
        return batteryVoltage;
    }
}
