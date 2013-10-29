package ca.titanoboa.views.vertebra;

import java.util.List;

import android.widget.TextView;
import ca.titanoboa.model.vertebra.Vertebra;

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
public class TitanoboaVertebraViews implements VertebraViews {

    private final Vertebra vertebra;

    private final int parentModuleNumber;
	private final int vertebraNumber;

    private TextView horizontalSetpointAngleView;
    private TextView horizontalSensorValueView;
    private TextView horizontalHighCalibrationView;
    private TextView horizontalLowCalibrationView;
    private TextView verticalSetpointAngleView;
    private TextView verticalSensorValueView;
    private TextView verticalHighCalibrationView;
    private TextView verticalLowCalibrationView;

    public TitanoboaVertebraViews(final int parentModuleNumber, final int vertebraNumber,
                                  final Vertebra vertebra) {
        this.parentModuleNumber = parentModuleNumber;
        this.vertebraNumber = vertebraNumber;
        this.vertebra = vertebra;
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
    public TextView getHorizontalSetpointAngleView() {
        return horizontalSetpointAngleView;
    }

    @Override
    public void setHorizontalSetpointAngleView(TextView horizontalSetpointAngleView) {
        this.horizontalSetpointAngleView = horizontalSetpointAngleView;
    }

    @Override
    public TextView getHorizontalSensorValueView() {
        return horizontalSensorValueView;
    }

    @Override
    public void setHorizontalSensorValueView(TextView horizontalSensorValueView) {
        this.horizontalSensorValueView = horizontalSensorValueView;
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
    public TextView getVerticalSensorValueView() {
        return verticalSensorValueView;
    }

    @Override
    public void setVerticalSensorValueView(TextView verticalSensorValueView) {
        this.verticalSensorValueView = verticalSensorValueView;
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
	 * Tell all views to update themselves.
	 */
	@Override
	public void updateViews() {
        // don't bother updating if the packets haven't changed
        if (!vertebra.isChangedSinceLastUpdate()) {
            return;
        }

        getHorizontalSetpointAngleView().setText(Integer.toString(vertebra.getHorizontalSetpointAngle()));
        getHorizontalSensorValueView().setText(Integer.toString(vertebra.getHorizontalSensorValue()));

        getVerticalSetpointAngleView().setText(Integer.toString(vertebra.getVerticalSetpointAngle()));
        getVerticalSensorValueView().setText(Integer.toString(vertebra.getVerticalSensorValue()));

        getHorizontalHighCalibrationView().setText(Integer.toString(vertebra.getHorizontalHighCalibration()));
        getHorizontalLowCalibrationView().setText(Integer.toString(vertebra.getHorizontalLowCalibration()));

        getVerticalHighCalibrationView().setText(Integer.toString(vertebra.getVerticalHighCalibration()));
        getVerticalLowCalibrationView().setText(Integer.toString(vertebra.getVerticalLowCalibration()));

        vertebra.setChangedSinceLastUpdate(false);
	}

    /**
     * Set the views. This is dependent on order and generally pretty icky.
     * @param views The views to set
     */
    @Override
    public void setViews(List<TextView> views) {
        setHorizontalSetpointAngleView(views.get(0));
        setHorizontalSensorValueView(views.get(1));
        setHorizontalHighCalibrationView(views.get(2));
        setHorizontalLowCalibrationView(views.get(3));
        setVerticalSetpointAngleView(views.get(4));
        setVerticalSensorValueView(views.get(5));
        setVerticalHighCalibrationView(views.get(6));
        setVerticalLowCalibrationView(views.get(7));
    }
}
