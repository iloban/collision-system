package by.bsu.fpmi.csys.event;

import by.bsu.fpmi.csys.model.Ball;

public final class BallCollisionEvent extends CollisionEvent {
    private final Ball targetBall;
    private final int collisionCount;

    public BallCollisionEvent(double timestamp, Ball sourceBall, Ball targetBall) {
        super(timestamp, sourceBall);
        this.targetBall = targetBall;
        collisionCount = targetBall.getCollisionCount();
    }

    @Override
    public boolean isValid() {
        return super.isValid() && targetBall.getCollisionCount() == collisionCount;
    }

    public Ball getTargetBall() {
        return targetBall;
    }
}
