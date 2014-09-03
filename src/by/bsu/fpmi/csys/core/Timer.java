package by.bsu.fpmi.csys.core;

import by.bsu.fpmi.csys.application.Settings;

final class Timer {
    private double time;

    double getTimestamp(double dt) {
        return time + dt;
    }

    double getNextTic() {
        return time + Settings.getStepTimeMillis();
    }

    double getDtAndSetTime(double timestamp) {
        double dt = timestamp - time;
        time = timestamp;
        return dt;
    }
}
