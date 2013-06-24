package ca.titanoboa.model.vertebra;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.widget.TextView;
import ca.titanoboa.packet.Packet;

public interface Vertebra {
    UUID getLastHorizontalSetpointsAndPositionsPacketUuid();

    UUID getLastHorizontalCalibrationPacketUuid();

    UUID getLastVerticalCalibrationPacketUuid();

    int getParentModuleNumber();

    int getVertebraNumber();

    int getHorizontalSetpointAngle();

    void setHorizontalSetpointAngle(final int horizontalSetpointAngle);

    int getHorizontalAngle();

    void setHorizontalAngle(final int horizontalSensorValue);

    int getHorizontalHighCalibration();

    void setHorizontalHighCalibration(final int horizontalHighCalibration);

    int getHorizontalLowCalibration();

    void setHorizontalLowCalibration(final int horizontalLowCalibration);

    int getVerticalSetpointAngle();

    void setVerticalSetpointAngle(final int verticalSetpointAngle);

    int getVerticalAngle();

    void setVerticalAngle(final int verticalSensorValue);

    int getVerticalHighCalibration();

    void setVerticalHighCalibration(final int verticalHighCalibration);

    int getVerticalLowCalibration();

    void setVerticalLowCalibration(final int verticalLowCalibration);

    // Android views
    TextView getHorizontalSetpointAngleView();

    void setHorizontalSetpointAngleView(TextView horizontalSetpointAngleView);

    TextView getHorizontalAngleView();

    void setHorizontalAngleView(TextView horizontalSensorValueView);

    TextView getHorizontalHighCalibrationView();

    void setHorizontalHighCalibrationView(TextView horizontalHighCalibrationView);

    TextView getHorizontalLowCalibrationView();

    void setHorizontalLowCalibrationView(TextView horizontalLowCalibrationView);

    TextView getVerticalSetpointAngleView();

    void setVerticalSetpointAngleView(TextView verticalSetpointAngleView);

    TextView getVerticalAngleView();

    void setVerticalAngleView(TextView verticalSensorValueView);

    TextView getVerticalHighCalibrationView();

    void setVerticalHighCalibrationView(TextView verticalHighCalibrationView);

    TextView getVerticalLowCalibrationView();

    void setVerticalLowCalibrationView(TextView verticalLowCalibrationView);

	void updateData(Map<String, Packet> packets);

    void setViews(List<TextView> views);
}
