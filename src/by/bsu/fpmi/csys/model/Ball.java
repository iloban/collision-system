package by.bsu.fpmi.csys.model;

import javafx.scene.shape.Circle;

public final class Ball extends Circle {
    private final double mass;

    private int collisionCount;
    private double velocityX;
    private double velocityY;

    public Ball(double centerX, double centerY, double radius, double velocityX, double velocityY) {
        super(centerX, centerY, radius);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        mass = Math.PI * Math.pow(radius, 2.0); // Density and height equals to 1
    }

    public void move(double dt) {
        setCenterX(getCenterX() + velocityX * dt);
        setCenterY(getCenterY() + velocityY * dt);
    }

    public void inverseVelocityX() {
        velocityX = -velocityX;
    }

    public void inverseVelocityY() {
        velocityY = -velocityY;
    }

    public int getCollisionCount() {
        return collisionCount;
    }

    public void incrementCollisionCount() {
        collisionCount++;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getVelocity() {
        return Math.sqrt(Math.pow(velocityX, 2.0) + Math.pow(velocityY, 2.0));
    }

    public double getMass() {
        return mass;
    }
}
