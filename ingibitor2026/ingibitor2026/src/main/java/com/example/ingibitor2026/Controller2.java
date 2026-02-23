package com.example.ingibitor2026;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class Controller2 {
    @FXML
    private Canvas gameCanvas;

    private GameMenu menu;

    @FXML
    public void initialize() {
        showMenu();
    }
    public void showMenu() {
        menu = new GameMenu(gameCanvas, this);
    }
}
