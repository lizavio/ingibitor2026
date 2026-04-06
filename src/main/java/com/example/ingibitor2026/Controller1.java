package com.example.ingibitor2026;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.*;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDate;

public class Controller1 {
    @FXML
    WebView wv;
    WebEngine engine;
    Timer timer;
    String lastResult = "";
    @FXML
    Label l;
    static String name;
    @FXML
    TextArea resArea;
    String result;
    LocalDate currentDate;
    @FXML
    Button saveButton;
    static int trainNum;

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
        showDialog();
    }

    public void showDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Приветствие");
        dialog.setHeaderText("Как тебя зовут?");
        dialog.getEditor().setPrefWidth(300);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            name = result.get().toString();
            load();
        }
        else{
            name = "Гость";
        }
    }

    public void load() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("save.ser");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Saved saved = null;
        try {
            saved = (Saved) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        result = saved.getRes();
        resArea.setText(result);
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
                    if (times.length == 2 && !times[0].equals("00:00")){
                        onNewResult(times[0], times[1]);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("bad script");
        }
    }
    private void onNewResult(String currentTime, String recordTime) {
        //l.setText("Обновление результатов:");
        //l.setText("Рекорд: " + recordTime);
            l.setText("Текущий результат: " + currentTime);
            currentDate = LocalDate.now();
            resArea.appendText(currentDate + " " + currentTime +", Тренировок:"+ trainNum + "\n");

    }

    public void save(javafx.event.ActionEvent actionEvent) throws IOException {
        result = resArea.getText();
        Saved saved = new Saved(result);
        FileOutputStream os = new FileOutputStream("save.ser");
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(saved);
        oos.close();
    }

    /////////////////КОНЕЦ ПАРСИНГА///////////////////////

}
