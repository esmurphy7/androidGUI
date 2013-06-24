package ca.titanoboa.model.vertebra;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.widget.TextView;
import ca.titanoboa.network.TitanoboaPacketReader;
import ca.titanoboa.packet.*;

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

    private UUID lastHorizontalSetpointsAndPositionsPacketUuid;
    private UUID lastHorizontalCalibrationPacketUuid;
    private UUID lastVerticalCalibrationPacketUuid;

    private final int parentModuleNumber;
	private final int vertebraNumber;
    private int horizontalSetpointAngle;
    private int horizontalAngle;
    private int horizontalHighCalibration;
    private int horizontalLowCalibration;
    private int verticalSetpointAngle;
    private int verticalAngle;
    private int verticalHighCalibration;
    private int verticalLowCalibration;

    private TextView horizontalSetpointAngleView;
    private TextView horizontalAngleView;
    private TextView horizontalHighCalibrationView;
    private TextView horizontalLowCalibrationView;
    private TextView verticalSetpointAngleView;
    private TextView verticalAngleView;
    private TextView verticalHighCalibrationView;
    private TextView verticalLowCalibrationView;

    public TitanoboaVertebra(final int parentModuleNumber, final int vertebraNumber) {
        this.parentModuleNumber = parentModuleNumber;
        this.vertebraNumber = vertebraNumber;
    }

    @Override
    public UUID getLastHorizontalSetpointsAndPositionsPacketUuid() {
        return lastHorizontalSetpointsAndPositionsPacketUuid;
    }

    @Override
    public UUID getLastHorizontalCalibrationPacketUuid() {
        return lastHorizontalCalibrationPacketUuid;
    }

    @Override
    public UUID getLastVerticalCalibrationPacketUuid() {
        return lastVerticalCalibrationPacketUuid;
    }

    @Override
    public int getParentModuleNumber() {
        return parentModuleNumber;
    }

    @Override
    public int getVertebraNumber() {
        return vertebraNumber;
    }

    @Override
    public int getHorizontalSetpointAngle() {
        return horizontalSetpointAngle;
    }

    @Override
    public void setHorizontalSetpointAngle(int horizontalSetpointAngle) {
        this.horizontalSetpointAngle = horizontalSetpointAngle;
    }

    @Override
    public int getHorizontalAngle() {
        return horizontalAngle;
    }

    @Override
    public void setHorizontalAngle(int horizontalAngle) {
        this.horizontalAngle = horizontalAngle;
    }

    @Override
    public int getHorizontalHighCalibration() {
        return horizontalHighCalibration;
    }

    @Override
    public void setHorizontalHighCalibration(int horizontalHighCalibration) {
        this.horizontalHighCalibration = horizontalHighCalibration;
    }

    @Override
    public int getHorizontalLowCalibration() {
        return horizontalLowCalibration;
    }

    @Override
    public void setHorizontalLowCalibration(int horizontalLowCalibration) {
        this.horizontalLowCalibration = horizontalLowCalibration;
    }

    @Override
    public int getVerticalSetpointAngle() {
        return verticalSetpointAngle;
    }

    @Override
    public void setVerticalSetpointAngle(int verticalSetpointAngle) {
        this.verticalSetpointAngle = verticalSetpointAngle;
    }

    @Override
    public int getVerticalAngle() {
        return verticalAngle;
    }

    @Override
    public void setVerticalAngle(int verticalAngle) {
        this.verticalAngle = verticalAngle;
    }

    @Override
    public int getVerticalHighCalibration() {
        return verticalHighCalibration;
    }

    @Override
    public void setVerticalHighCalibration(int verticalHighCalibration) {
        this.verticalHighCalibration = verticalHighCalibration;
    }

    @Override
    public int getVerticalLowCalibration() {
        return verticalLowCalibration;
    }

    @Override
    public void setVerticalLowCalibration(int verticalLowCalibration) {
        this.verticalLowCalibration = verticalLowCalibration;
    }

    @Override
    public TextView getHorizontalSetpointAngleView() {
        return horizontalSetpointAngleView;
    }

    @Override
    public void setHorizontalSetpointAngleView(TextView horizontalSetpointAngleView) {
        this.horizontalSetpointAngleView = horizontalSetpointAngleView;
    }

    @Override
    public TextView getHorizontalAngleView() {
        return horizontalAngleView;
    }

    @Override
    public void setHorizontalAngleView(TextView horizontalAngleView) {
        this.horizontalAngleView = horizontalAngleView;
    }

    @Override
    public TextView getHorizontalHighCalibrationView() {
        return horizontalHighCalibrationView;
    }

    @Override
    public void setHorizontalHighCalibrationView(TextView horizontalHighCalibrationView) {
        this.horizontalHighCalibrationView = horizontalHighCalibrationView;
    }

    @Override
    public TextView getHorizontalLowCalibrationView() {
        return horizontalLowCalibrationView;
    }

    @Override
    public void setHorizontalLowCalibrationView(TextView horizontalLowCalibrationView) {
        this.horizontalLowCalibrationView = horizontalLowCalibrationView;
    }

    @Override
    public TextView getVerticalSetpointAngleView() {
        return verticalSetpointAngleView;
    }

    @Override
    public void setVerticalSetpointAngleView(TextView verticalSetpointAngleView) {
        this.verticalSetpointAngleView = verticalSetpointAngleView;
    }

    @Override
    public TextView getVerticalAngleView() {
        return verticalAngleView;
    }

    @Override
    public void setVerticalAngleView(TextView verticalAngleView) {
        this.verticalAngleView = verticalAngleView;
    }

    @Override
    public TextView getVerticalHighCalibrationView() {
        return verticalHighCalibrationView;
    }

    @Override
    public void setVerticalHighCalibrationView(TextView verticalHighCalibrationView) {
        this.verticalHighCalibrationView = verticalHighCalibrationView;
    }

    @Override
    public TextView getVerticalLowCalibrationView() {
        return verticalLowCalibrationView;
    }

    @Override
    public void setVerticalLowCalibrationView(TextView verticalLowCalibrationView) {
        this.verticalLowCalibrationView = verticalLowCalibrationView;
    }

	/**
	 * Tell all actuators in this vertebra to update themselves.
	 * 
	 * @param packets
	 *            The packets passed down from the module.
	 */
	@Override
	public void updateData(Map<String, Packet> packets) {
        SetpointsAndPositionsPacket setpointsAndPositionsPacket = (SetpointsAndPositionsPacket) packets.get(TitanoboaPacketReader.SETPOINTS_AND_POSITIONS_KEY);
        HorizontalCalibrationPacket horizontalCalibrationPacket = (HorizontalCalibrationPacket) packets.get(TitanoboaPacketReader.HORIZONTAL_CALIBRATION_KEY);
        VerticalCalibrationPacket verticalCalibrationPacket = (VerticalCalibrationPacket) packets.get(TitanoboaPacketReader.VERTICAL_CALIBRATION_KEY);

        UUID currentSetpointsAndPositionsPacketUuid = setpointsAndPositionsPacket.getUuid();
        UUID currentHorizontalCalibrationPacketUuid = horizontalCalibrationPacket.getUuid();
        UUID currentVerticalCalibrationPacketUuid = verticalCalibrationPacket.getUuid();

        // don't bother updating if the packets haven't changed
        if (!currentSetpointsAndPositionsPacketUuid.equals(getLastHorizontalSetpointsAndPositionsPacketUuid())) {
            lastHorizontalSetpointsAndPositionsPacketUuid = currentSetpointsAndPositionsPacketUuid;

            setHorizontalSetpointAngle(setpointsAndPositionsPacket.getHorizontalSetpointAngle(getParentModuleNumber(), getVertebraNumber()));
            getHorizontalSetpointAngleView().setText(Integer.toString(getHorizontalSetpointAngle()));
            setHorizontalAngle(setpointsAndPositionsPacket.getHorizontalAngle(getParentModuleNumber(), getVertebraNumber()));
            getHorizontalAngleView().setText(Integer.toString(getHorizontalAngle()));

            setVerticalSetpointAngle(setpointsAndPositionsPacket.getVerticalSetpointAngle(getParentModuleNumber(), getVertebraNumber()));
            getVerticalSetpointAngleView().setText(Integer.toString(getVerticalSetpointAngle()));
            setVerticalAngle(setpointsAndPositionsPacket.getVerticalAngle(getParentModuleNumber(), getVertebraNumber()));
            getVerticalAngleView().setText(Integer.toString(getVerticalAngle()));
        }

        if (!currentHorizontalCalibrationPacketUuid.equals(getLastHorizontalCalibrationPacketUuid())) {
            lastHorizontalCalibrationPacketUuid = currentHorizontalCalibrationPacketUuid;

            setHorizontalHighCalibration(horizontalCalibrationPacket.getHorizontalHighCalibration(getParentModuleNumber(), getVertebraNumber()));
            getHorizontalHighCalibrationView().setText(Integer.toString(getHorizontalHighCalibration()));
            setHorizontalLowCalibration(horizontalCalibrationPacket.getHorizontalLowCalibration(getParentModuleNumber(), getVertebraNumber()));
            getHorizontalLowCalibrationView().setText(Integer.toString(getHorizontalLowCalibration()));
        }

        if (!currentVerticalCalibrationPacketUuid.equals(getLastVerticalCalibrationPacketUuid())) {
            lastVerticalCalibrationPacketUuid = currentVerticalCalibrationPacketUuid;

            setVerticalHighCalibration(verticalCalibrationPacket.getVerticalHighCalibration(getParentModuleNumber(), getVertebraNumber()));
            getVerticalHighCalibrationView().setText(Integer.toString(getVerticalHighCalibration()));
            setVerticalLowCalibration(verticalCalibrationPacket.getVerticalLowCalibration(getParentModuleNumber(), getVertebraNumber()));
            getVerticalLowCalibrationView().setText(Integer.toString(getVerticalLowCalibration()));
        }
	}

    /**
     * Set the views. This is dependent on order and generally pretty icky.
     * @param views The views to set
     */
    @Override
    public void setViews(List<TextView> views) {
        setHorizontalSetpointAngleView(views.get(0));
        setHorizontalAngleView(views.get(1));
        setHorizontalHighCalibrationView(views.get(2));
        setHorizontalLowCalibrationView(views.get(3));
        setVerticalSetpointAngleView(views.get(4));
        setVerticalAngleView(views.get(5));
        setVerticalHighCalibrationView(views.get(6));
        setVerticalLowCalibrationView(views.get(7));
    }
}
