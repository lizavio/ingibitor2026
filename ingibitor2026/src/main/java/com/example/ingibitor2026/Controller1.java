package com.example.ingibitor2026;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.Timer;
import java.util.TimerTask;

public class Controller1 {
    @FXML
    WebView wv;
    WebEngine engine;
    Timer timer;
    String lastResult = "";

    @FXML
    public void initialize() {
        engine = wv.getEngine();
        engine.load("https://schultetable.ru/training/");

        wv.setZoom(0.7);

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Страница загружена, запускаем мониторинг...");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            startMonitoring();
                        });
                    }
                }, 2000);
            }
        });
    }
    ///////////////////ПАРСИНГ///////////////////////////////////////
    private void startMonitoring() {
        engine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                checkForResults();
            }
        });
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    checkForResults();
                });
            }
        }, 3000, 3000); // первая проверка через 5 сек, потом каждые 3 сек
    }
    private void checkForResults() {
        String script = "var d=document.getElementById('result')," +
                "t=d?d.querySelectorAll('.time'):[];" +
                "t.length>1?t[0].textContent+'|'+t[1].textContent:'ERROR_NEED_2_FOUND_'+t.length";
        try {
            Object result = engine.executeScript(script);
            if (result != null) {
                String currentResult = result.toString();
                if (!currentResult.equals(lastResult) && !currentResult.isEmpty()) {
                    lastResult = currentResult;
                    String[] times = currentResult.split("\\|");
                    if (times.length == 2) {
                        onNewResult(times[0], times[1]);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("bad script");
        }
    }
    private void onNewResult(String currentTime, String recordTime) {
        System.out.println("Обновление результатов:");
        System.out.println("Текущее: " + currentTime);
        System.out.println("Рекорд: " + recordTime);
        Platform.runLater(() -> {
            // update UI

        });
    }
    /////////////////КОНЕЦ ПАРСИНГА///////////////////////

}
