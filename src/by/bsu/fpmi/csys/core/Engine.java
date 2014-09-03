package by.bsu.fpmi.csys.core;

import by.bsu.fpmi.csys.event.BallCollisionEvent;
import by.bsu.fpmi.csys.event.BreakEvent;
import by.bsu.fpmi.csys.event.CollisionEvent;
import by.bsu.fpmi.csys.event.TimerEvent;
import by.bsu.fpmi.csys.event.WallCollisionEvent;
import by.bsu.fpmi.csys.model.Ball;
import by.bsu.fpmi.csys.model.WallType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.PriorityQueue;

public final class Engine implements EventHandler<ActionEvent> {
    private static final Engine ENGINE = new Engine();

    public static Engine getInstance() {
        return ENGINE;
    }

    private Timer timer = new Timer();
    private ObjectContainer objectContainer = ObjectContainer.getInstance();
    private PriorityQueue<TimerEvent> queue = new PriorityQueue<>();

    private Engine() {
    }

    public void init() {
        for (Ball ball : objectContainer.getBalls()) {
            predictCollisions(ball);
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        queue.add(new BreakEvent(timer.getNextTic()));
        while (!queue.isEmpty()) {
            TimerEvent event = queue.poll();

            if (event instanceof BreakEvent) {
                moveBalls(timer.getDtAndSetTime(event.getTimestamp()));
                break;
            }

            CollisionEvent collisionEvent = (CollisionEvent) event;
            if (collisionEvent.isValid()) {
                moveBalls(timer.getDtAndSetTime(event.getTimestamp()));
                handleCollisions(collisionEvent);
            }
        }
    }

    private void predictCollisions(Ball sourceBall) {
        double dt = CollisionUtils.calcTimeToHitWall(sourceBall, WallType.HORIZONTAL);
        if (CollisionUtils.isCollisionConsistency(dt)) {
            queue.add(new WallCollisionEvent(timer.getTimestamp(dt), sourceBall, WallType.HORIZONTAL));
        }

        dt = CollisionUtils.calcTimeToHitWall(sourceBall, WallType.VERTICAL);
        if (CollisionUtils.isCollisionConsistency(dt)) {
            queue.add(new WallCollisionEvent(timer.getTimestamp(dt), sourceBall, WallType.VERTICAL));
        }

        for (Ball targetBall : objectContainer.getBalls()) {
            dt = CollisionUtils.calcTimeToHitBall(sourceBall, targetBall);
            if (CollisionUtils.isCollisionConsistency(dt)) {
                queue.add(new BallCollisionEvent(timer.getTimestamp(dt), sourceBall, targetBall));
            }
        }
    }

    private void moveBalls(double dt) {
        for (Ball ball : objectContainer.getBalls()) {
            ball.move(dt);
        }
    }

    private void handleCollisions(CollisionEvent event) {
        if (event instanceof WallCollisionEvent) {
            handleWallCollision((WallCollisionEvent) event);
        } else {
            handleBallCollision((BallCollisionEvent) event);
        }
    }

    private void handleWallCollision(WallCollisionEvent event) {
        CollisionUtils.bounceOffWall(event.getSourceBall(), event.getWallType());
        event.getSourceBall().incrementCollisionCount();
        predictCollisions(event.getSourceBall());
    }

    private void handleBallCollision(BallCollisionEvent event) {
        CollisionUtils.bounceOff(event.getSourceBall(), event.getTargetBall());
        event.getSourceBall().incrementCollisionCount();
        event.getTargetBall().incrementCollisionCount();
        predictCollisions(event.getSourceBall());
        predictCollisions(event.getTargetBall());
    }
}
