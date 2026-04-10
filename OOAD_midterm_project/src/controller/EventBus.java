package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EventBus {
    private static EventBus instance;

    // 原有的模式切換監聽器 (接收 String)
    private List<Consumer<String>> modeListeners = new ArrayList<>();

    // 新增：處理 Use Case A 拖曳放開的監聽器 (接收 String 和 Point)
    private List<BiConsumer<String, Point>> releaseListeners = new ArrayList<>();

    private EventBus() {}

    public static EventBus getInstance() {
        if (instance == null) instance = new EventBus();
        return instance;
    }

    // --- 原有方法：用於切換工具列狀態 ---
    public void publishModeChange(String modeName) {
        for (Consumer<String> listener : modeListeners) {
            listener.accept(modeName);
        }
    }

    public void subscribeModeChange(Consumer<String> listener) {
        modeListeners.add(listener);
    }

    // --- 新增方法：用於 Use Case A 的拖曳建立功能 ---

    /**
     * 當使用者在按鈕上放開滑鼠時呼叫
     * @param type "rect" 或 "oval"
     * @param screenPoint 滑鼠放開時在螢幕上的絕對座標
     */
    public void publishMouseReleased(String type, Point screenPoint) {
        for (BiConsumer<String, Point> listener : releaseListeners) {
            listener.accept(type, screenPoint);
        }
    }

    /**
     * 由 MainFrame 或 Canvas 訂閱，收到後執行建立物件的動作
     */
    public void subscribeMouseReleased(BiConsumer<String, Point> listener) {
        releaseListeners.add(listener);
    }
}