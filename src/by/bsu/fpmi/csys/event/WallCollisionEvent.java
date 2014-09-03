package by.bsu.fpmi.csys.event;

import by.bsu.fpmi.csys.model.Ball;
import by.bsu.fpmi.csys.model.WallType;

public final class WallCollisionEvent extends CollisionEvent {
    private final WallType wallType;

    public WallCollisionEvent(double timestamp, Ball sourceBall, WallType wallType) {
        super(timestamp, sourceBall);
        this.wallType = wallType;
    }

    public WallType getWallType() {
        return wallType;
    }
}
