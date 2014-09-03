package by.bsu.fpmi.csys.application;

import by.bsu.fpmi.csys.core.CollisionUtils;
import by.bsu.fpmi.csys.core.Engine;
import by.bsu.fpmi.csys.core.ObjectContainer;
import by.bsu.fpmi.csys.model.Ball;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradientBuilder;
import javafx.scene.paint.RadialGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class CollisionSystem extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        initSettings();
        createModel();
        createUI(stage);
        simulate();
    }

    private void initSettings() {
        Settings.setUserSettings(getParameters().getNamed());
    }

    private void createModel() {
        Random random = new Random();
        List<Ball> balls = new ArrayList<>();
        BALL_GENERATOR:
        for (int i = 0, j = 0; i < Settings.getBallCount(); i++, j = 0) {
            Ball ball;
            do {
                if (j++ >= Settings.getSkipLimit()) {
                    break BALL_GENERATOR;
                }
                ball = createRandomBall(random);
            } while (CollisionUtils.isCollisionDetected(ball, balls));
            balls.add(ball);
        }
        ObjectContainer.getInstance().setBalls(balls);
    }

    private Ball createRandomBall(Random random) {
        double radius =
                Settings.getMinRadius() + (Settings.getMaxRadius() - Settings.getMinRadius()) * random.nextDouble();
        double centerX = radius + (Settings.getWidth() - 2 * radius) * random.nextDouble();
        double centerY = radius + (Settings.getHeight() - 2 * radius) * random.nextDouble();
        double velocityX = Settings.getMinVelocity() + (Settings.getMaxVelocity() - Settings.getMinVelocity()) * random
                .nextDouble();
        if (random.nextBoolean()) {
            velocityX = -velocityX;
        }
        double velocityY = Settings.getMinVelocity() + (Settings.getMaxVelocity() - Settings.getMinVelocity()) * random
                .nextDouble();
        if (random.nextBoolean()) {
            velocityY = -velocityY;
        }
        return new Ball(centerX, centerY, radius, velocityX, velocityY);
    }

    private void createUI(Stage stage) {
        List<Ball> balls = ObjectContainer.getInstance().getBalls();
        for (Ball ball : balls) {
            ball.setFill(Color.web("white", 0.5));
            ball.setStroke(RadialGradientBuilder.create().centerX(0.5).centerY(0.5).radius(2.0).proportional(true)
                    .cycleMethod(CycleMethod.NO_CYCLE)
                    .stops(new Stop(0, Color.web("white", 0.15)), new Stop(0.63, Color.web("white", 0))).build());
            ball.setStrokeType(StrokeType.OUTSIDE);
            ball.setStrokeWidth(ball.getRadius() * 2.0);
        }
        Group ballGroup = new Group();
        ballGroup.setEffect(new BoxBlur(2.0, 2.0, 2)); // ballGroup.setEffect(new GaussianBlur(2));
        ballGroup.getChildren().addAll(balls);

        Rectangle colors = new Rectangle(Settings.getWidth(), Settings.getHeight(),
                LinearGradientBuilder.create().startX(0.0).startY(1.0).endX(1.0).endY(0.0).proportional(true)
                        .cycleMethod(CycleMethod.NO_CYCLE)
                        .stops(new Stop(0, Color.web("#f8bd55")), new Stop(0.14, Color.web("#c0fe56")),
                                new Stop(0.28, Color.web("#5df9c1")), new Stop(0.43, Color.web("#64c2f8")),
                                new Stop(0.57, Color.web("#be4af7")), new Stop(0.71, Color.web("#ed5fc2")),
                                new Stop(0.85, Color.web("#ef504c")), new Stop(1, Color.web("#f2660f"))).build());
        colors.setBlendMode(BlendMode.OVERLAY);

        Group group =
                new Group(new Rectangle(Settings.getWidth(), Settings.getHeight(), Color.BLACK), ballGroup, colors);
        stage.setScene(new Scene(group, Settings.getWidth(), Settings.getHeight()));
        stage.setTitle("Collision System");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public void simulate() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        Engine engine = Engine.getInstance();
        engine.init();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Settings.getStepTimeMillis()), engine));
        timeline.play();
    }
}
