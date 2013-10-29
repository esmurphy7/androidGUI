package ca.titanoboa.views.vertebra;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.widget.TextView;
import ca.titanoboa.packet.Packet;

public interface VertebraViews {

    int getParentModuleNumber();

    int getVertebraNumber();

    // Android views
    TextView getHorizontalSetpointAngleView();

    void setHorizontalSetpointAngleView(TextView horizontalSetpointAngleView);

    TextView getHorizontalSensorValueView();

    void setHorizontalSensorValueView(TextView horizontalSensorValueView);

    TextView getHorizontalHighCalibrationView();

    void setHorizontalHighCalibrationView(TextView horizontalHighCalibrationView);

    TextView getHorizontalLowCalibrationView();

    void setHorizontalLowCalibrationView(TextView horizontalLowCalibrationView);

    TextView getVerticalSetpointAngleView();

    void setVerticalSetpointAngleView(TextView verticalSetpointAngleView);

    TextView getVerticalSensorValueView();

    void setVerticalSensorValueView(TextView verticalSensorValueView);

    TextView getVerticalHighCalibrationView();

    void setVerticalHighCalibrationView(TextView verticalHighCalibrationView);

    TextView getVerticalLowCalibrationView();

    void setVerticalLowCalibrationView(TextView verticalLowCalibrationView);

	void updateViews();

    void setViews(List<TextView> views);
}
