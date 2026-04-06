package com.example.ingibitor2026;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameMenu{

    private GraphicsContext gc;
    private Canvas canvas;
    Controller2 controller;
    Image farmer, w_farmer, girl, boy;

    public GameMenu(Canvas canvas, Controller2 controller) {
        this.canvas = canvas;
        this.controller = controller;
        gc = canvas.getGraphicsContext2D();

        drawMenu();
        canvas.setOnMouseClicked(e -> handleClick(e.getX(), e.getY()));
    }

    private void drawMenu() {
        gc.setFill(new Color(0.4, 0.7, 0.2, 1));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(24));

        gc.fillText("Приветствуем, "+ Controller1.name, 280, 50);

        gc.fillText("ВЫБЕРИ ИГРУ:", 240, 200);
        gc.fillText("Время/Ошибки/Очки ", 520, 200);

        gc.fillText("1. Игра Саймона \"Собери урожай\"", 100, 260);
        int t = controller.res.get("simon")[0];
        int e = controller.res.get("simon")[1];
        int p = controller.res.get("simon")[2];
        gc.fillText(""+t+"/"+e+"/"+p, 520, 260);

        gc.fillText("2. Эффект Струпа \"Найди яйца\"", 100, 320);
        t = controller.res.get("stroop")[0];
        e = controller.res.get("stroop")[1];
        p = controller.res.get("stroop")[2];
        gc.fillText(""+t+"/"+e+"/"+p, 520, 320);

        gc.fillText("3. Игра Go/No-Go \"Посчитай овец\"", 100, 380);
        t = controller.res.get("go")[0];
        e = controller.res.get("go")[1];
        p = controller.res.get("go")[2];
        gc.fillText(""+t+"/"+e+"/"+p, 520, 380);

        gc.fillText("4. Игра Flanker \"Цыплята и утята\"", 100, 440);
        t = controller.res.get("flanker")[0];
        e = controller.res.get("flanker")[1];
        p = controller.res.get("flanker")[2];
        gc.fillText(""+t+"/"+e+"/"+p, 520, 440);

        paintFamily();
    }

    private void paintFamily() {
        farmer = new Image( getClass().getResourceAsStream("images/farmer1.png") );
        w_farmer = new Image( getClass().getResourceAsStream("images/w_farmer.png") );
        girl = new Image( getClass().getResourceAsStream("images/girl.png") );
        boy = new Image( getClass().getResourceAsStream("images/boy.png") );

        gc.drawImage(farmer, 750, 430, 135, 190);
        gc.drawImage(w_farmer, 660, 435, 110, 185);
        gc.drawImage(girl, 720, 470, 70, 150);
        gc.drawImage(boy, 760, 520, 60, 100);
    }

    private void handleClick(double x, double y) {

        if (y > 240 && y < 280) {
            new SimonGame(canvas, controller);
        }
        if (y > 300 && y < 340) {
            new StroopGame(canvas, controller);
        }
        if (y > 360 && y < 400) {
            new GoNoGame(canvas, controller);
        }
        if (y > 420 && y < 460) {
            new FlankerGame(canvas, controller);
        }

    }

}