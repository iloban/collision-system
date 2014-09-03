package by.bsu.fpmi.csys.event;

import by.bsu.fpmi.csys.model.Ball;

public abstract class CollisionEvent extends TimerEvent {
    private final Ball sourceBall;
    private final int collisionCount;

    public CollisionEvent(double timestamp, Ball sourceBall) {
        super(timestamp);
        this.sourceBall = sourceBall;
        collisionCount = sourceBall.getCollisionCount();
    }

    public boolean isValid() {
        return sourceBall.getCollisionCount() == collisionCount;
    }

    public Ball getSourceBall() {
        return sourceBall;
    }
}
