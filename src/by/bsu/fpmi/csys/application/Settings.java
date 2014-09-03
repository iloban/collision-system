package by.bsu.fpmi.csys.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Settings {
    private static final Map<String, Object> settingsCache = new HashMap<>();
    private static final Map<String, String> defaultSettings = new HashMap<String, String>() {{
        put("ballCount", "10");
        put("skipLimit", "5000");
        put("minRadius", "8");
        put("maxRadius", "40");
        put("minVelocity", "0.01");
        put("maxVelocity", "0.25");
        put("width", "800");
        put("height", "600");
        put("predictionTimeLimit", String.valueOf((800 + 600) / 0.01));
        put("stepTimeMillis", "20");
    }};

    private static Map<String, String> userSettings = Collections.emptyMap();

    public static void setUserSettings(Map<String, String> userSettings) {
        Settings.userSettings = userSettings;
    }

    public static int getBallCount() {
        return getParameterAsInt("ballCount");
    }

    public static int getSkipLimit() {
        return getParameterAsInt("skipLimit");
    }

    public static double getMinRadius() {
        return getParameterAsDouble("minRadius");
    }

    public static double getMaxRadius() {
        return getParameterAsDouble("maxRadius");
    }

    public static double getMinVelocity() {
        return getParameterAsDouble("minVelocity");
    }

    public static double getMaxVelocity() {
        return getParameterAsDouble("maxVelocity");
    }

    public static double getWidth() {
        return getParameterAsDouble("width");
    }

    public static double getHeight() {
        return getParameterAsDouble("height");
    }

    public static double getPredictionTimeLimit() {
        return getParameterAsDouble("predictionTimeLimit");
    }

    public static double getStepTimeMillis() {
        return getParameterAsDouble("stepTimeMillis");
    }

    private static int getParameterAsInt(String key) {
        if (!settingsCache.containsKey(key)) {
            settingsCache.put(key, Integer.valueOf(getParameter(key)));
        }
        return (int) settingsCache.get(key);
    }

    private static double getParameterAsDouble(String key) {
        if (!settingsCache.containsKey(key)) {
            settingsCache.put(key, Double.valueOf(getParameter(key)));
        }
        return (double) settingsCache.get(key);
    }

    private static String getParameter(String key) {
        return userSettings.containsKey(key) ? userSettings.get(key) : defaultSettings.get(key);
    }
}
