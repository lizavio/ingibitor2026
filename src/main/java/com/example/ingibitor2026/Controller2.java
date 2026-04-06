package com.example.ingibitor2026;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller2 {
    @FXML
    private Canvas gameCanvas;

    private GameMenu menu;
    HashMap<String, int[]> res;

    @FXML
    public void initialize() {
        res = new HashMap<>();
        res.put("simon", new int[]{0, 0, 0});
        res.put("stroop", new int[]{0, 0, 0});
        res.put("go", new int[]{0, 0, 0});
        res.put("flanker", new int[]{0, 0, 0});
        showMenu();
    }
    public void showMenu() {
        menu = new GameMenu(gameCanvas, this);
    }
}
