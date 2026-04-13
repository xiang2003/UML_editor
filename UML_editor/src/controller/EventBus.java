package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EventBus {
    private static EventBus instance;

    private List<Consumer<String>> modeListeners = new ArrayList<>();

    private List<BiConsumer<String, Point>> releaseListeners = new ArrayList<>();

    private EventBus() {}

    public static EventBus getInstance() {
        if (instance == null) instance = new EventBus();
        return instance;
    }

    public void publishModeChange(String modeName) {
        for (Consumer<String> listener : modeListeners) {
            listener.accept(modeName);
        }
    }

    public void subscribeModeChange(Consumer<String> listener) {
        modeListeners.add(listener);
    }

    public void publishMouseReleased(String type, Point screenPoint) {
        for (BiConsumer<String, Point> listener : releaseListeners) {
            listener.accept(type, screenPoint);
        }
    }

    public void subscribeMouseReleased(BiConsumer<String, Point> listener) {
        releaseListeners.add(listener);
    }
}