package by.bsu.fpmi.csys.core;

import by.bsu.fpmi.csys.model.Ball;

import java.util.ArrayList;
import java.util.List;

public final class ObjectContainer {
    private static final ObjectContainer OBJECT_CONTAINER = new ObjectContainer();

    public static ObjectContainer getInstance() {
        return OBJECT_CONTAINER;
    }

    private List<Ball> balls = new ArrayList<>();

    private ObjectContainer() {
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public void setBalls(List<Ball> balls) {
        this.balls = balls;
    }
}
