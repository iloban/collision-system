package by.bsu.fpmi.csys.core;

import by.bsu.fpmi.csys.application.Settings;
import by.bsu.fpmi.csys.model.Ball;
import by.bsu.fpmi.csys.model.WallType;
import javafx.geometry.Point2D;

import java.util.Collection;

public final class CollisionUtils {
    private CollisionUtils() {
    }

    public static boolean isCollisionDetected(Ball ball, Collection<Ball> balls) {
        for (Ball targetBall : balls) {
            if (isIntersected(ball, targetBall)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIntersected(Ball ball1, Ball ball2) {
        return distance(ball1.getCenterX(), ball1.getCenterY(), ball2.getCenterX(), ball2.getCenterY())
                <= ball1.getRadius() + ball2.getRadius();
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(distanceSq(x1, y1, x2, y2));
    }

    public static double distanceSq(double x1, double y1, double x2, double y2) {
        return Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0);
    }

    public static boolean isCollisionConsistency(double dt) {
        return !Double.isInfinite(dt) && dt < Settings.getPredictionTimeLimit();
    }

    public static double calcTimeToHitWall(Ball ball, WallType wallType) {
        double center;
        double radius = ball.getRadius();
        double velocity;
        double distance;

        if (wallType == WallType.VERTICAL) {
            center = ball.getCenterX();
            velocity = ball.getVelocityX();
            distance = Settings.getWidth();
        } else {
            center = ball.getCenterY();
            velocity = ball.getVelocityY();
            distance = Settings.getHeight();
        }

        if (velocity > 0) {
            return (distance - center - radius) / velocity;
        }
        if (velocity < 0) {
            return -(center - radius) / velocity;
        }
        return Double.POSITIVE_INFINITY;
    }

    public static double calcTimeToHitBall(Ball sourceBall, Ball targetBall) {
        if (sourceBall == targetBall) {
            return Double.POSITIVE_INFINITY;
        }

        double c = Math.pow(sourceBall.getRadius() + targetBall.getRadius(), 2.0);
        double a1 = sourceBall.getCenterX() - targetBall.getCenterX();
        double a2 = sourceBall.getVelocityX() - targetBall.getVelocityX();
        double b1 = sourceBall.getCenterY() - targetBall.getCenterY();
        double b2 = sourceBall.getVelocityY() - targetBall.getVelocityY();

        double a22b22 = Math.pow(a2, 2.0) + Math.pow(b2, 2.0);
        double dsq = a22b22 * c - Math.pow(a2 * b1 - a1 * b2, 2.0);
        if (dsq < 0) {
            return Double.POSITIVE_INFINITY;
        }
        double d = Math.sqrt(dsq);

        double a1a2b1b2 = a1 * a2 + b1 * b2;
        double t1 = (-a1a2b1b2 - d) / a22b22;
        if (t1 > 0) {
            return t1;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    public static void bounceOffWall(Ball ball, WallType wallType) {
        if (wallType == WallType.VERTICAL) {
            ball.inverseVelocityX();
        } else {
            ball.inverseVelocityY();
        }
    }

    public static void bounceOff(Ball sourceBall, Ball targetBall) {
        final Ball leftBall;
        final Ball rightBall;
        if (sourceBall.getCenterX() < targetBall.getCenterX()) {
            leftBall = sourceBall;
            rightBall = targetBall;
        } else {
            leftBall = targetBall;
            rightBall = sourceBall;
        }

        Point2D c2 = new Point2D(rightBall.getCenterX() - leftBall.getCenterX(),
          rightBall.getCenterY() - leftBall.getCenterY());
        Point2D v1 = new Point2D(leftBall.getVelocityX(), leftBall.getVelocityY());
        Point2D v2 = new Point2D(rightBall.getVelocityX(), rightBall.getVelocityY());
        double m1 = leftBall.getMass();
        double m2 = rightBall.getMass();

        double alpha = Math.atan(c2.getY() / c2.getX()); // From -Pi/2 to Pi/2
        double cosAlpha = Math.cos(alpha);
        double sinAlpha = Math.sin(alpha);

        Point2D nv1 = rotatePoint(v1, sinAlpha, cosAlpha);
        Point2D nv2 = rotatePoint(v2, sinAlpha, cosAlpha);

        Point2D nnv1 = calcAfterCollisionVelocity(m1, m2, nv1, nv2);
        Point2D nnv2 = calcAfterCollisionVelocity(m2, m1, nv2, nv1);

        Point2D nnnv1 = rotatePoint(nnv1, -sinAlpha, cosAlpha);
        Point2D nnnv2 = rotatePoint(nnv2, -sinAlpha, cosAlpha);

        leftBall.setVelocityX(nnnv1.getX());
        leftBall.setVelocityY(nnnv1.getY());
        rightBall.setVelocityX(nnnv2.getX());
        rightBall.setVelocityY(nnnv2.getY());
    }

    private static Point2D rotatePoint(Point2D v, double sinAlpha, double cosAlpha) {
        return new Point2D(v.getX() * cosAlpha + v.getY() * sinAlpha, -v.getX() * sinAlpha + v.getY() * cosAlpha);
    }

    private static Point2D calcAfterCollisionVelocity(double m1, double m2, Point2D v1, Point2D v2) {
        return new Point2D((m1 * v1.getX() - m2 * v1.getX() + 2.0 * m2 * v2.getX()) / (m1 + m2), v1.getY());
    }
}
