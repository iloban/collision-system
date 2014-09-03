package by.bsu.fpmi.csys.event;

public abstract class TimerEvent implements Comparable<TimerEvent> {
    private final double timestamp;

    protected TimerEvent(double timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(TimerEvent timerEvent) {
        return Double.compare(timestamp, timerEvent.timestamp);
    }

    public double getTimestamp() {
        return timestamp;
    }
}
