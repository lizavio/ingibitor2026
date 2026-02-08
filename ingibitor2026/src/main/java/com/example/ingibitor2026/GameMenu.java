package com.example.ingibitor2026;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameMenu{

    private GraphicsContext gc;
    private Canvas canvas;
    Controller2 controller;

    public GameMenu(Canvas canvas, Controller2 controller) {
        this.canvas = canvas;
        this.controller = controller;
        gc = canvas.getGraphicsContext2D();

        drawMenu();
        canvas.setOnMouseClicked(e -> handleClick(e.getX(), e.getY()));
    }

    private void drawMenu() {
        gc.setFill(Color.PINK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(24));

        gc.fillText("ВЫБЕРИ ИГРУ:", 280, 100);

        gc.fillText("1. Игра Саймона \"Собери урожай\"", 300, 260);
        gc.fillText("2. Эффект Струпа \"Найди яйца\"", 300, 320);
    }

    private void handleClick(double x, double y) {

        if (y > 240 && y < 280) {
            new SimonGame(canvas, controller);
        }
        if (y > 300 && y < 340) {
            new StroopGame(canvas, controller);
        }
    }

}