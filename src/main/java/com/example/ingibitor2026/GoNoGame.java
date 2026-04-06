package com.example.ingibitor2026;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Random;

public class GoNoGame {
    private GraphicsContext gc;
    private Canvas canvas;
    private Controller2 controller;
    int points;
    Image sheep_go, sheep_no, girl, img, black;
    Rectangle target;
    Random rand;
    int r;
    int counter = 30; //счетчик игры
    int GONO = counter;
    private AnimationTimer timer;
    long time1;
    long time2;
    int step = 0;
    long t;
    int error;

    public GoNoGame(Canvas canvas, Controller2 controller) {
        this.canvas = canvas;
        this.controller = controller;
        gc = canvas.getGraphicsContext2D();

        rand = new Random();

        target = new Rectangle(200, 200, 200,200);

        Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 24 );
        gc.setFont( theFont );
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(1);

        sheep_go = new Image( getClass().getResourceAsStream("images/sheep_go.png") );
        sheep_no = new Image( getClass().getResourceAsStream("images/sheep_no.png") );
        black = new Image(getClass().getResourceAsStream("images/black.png"));

        calculate();
        timer = new AnimationTimer()//бесконечный цикл 60 р/с
        {
            public void handle(long currentNanoTime) {
                draw(r);
            }
        };
        timer.start();
        time1 = System.currentTimeMillis();
    }

    private void calculate() {
        canvas.setOnMouseClicked(
                new EventHandler<MouseEvent>()
                {
                    public void handle(MouseEvent e)
                    {
                        menuClick(e.getX(), e.getY());
                        counter--;
                        endGame();
                        //условие получения очков
                        if ( target.contains( e.getX(), e.getY() ) ) {
                            r = rand.nextInt(1000);
                            if (step >= 0 && step < 50){
                                points++;
                                step = 50;
                            }
                        }
                    }
                });
    }

    private void draw(int к) {
        //очистка холста
        gc.setFill( new Color(0.1, 0.1, 0.3, 1) );
        gc.fillRect(0,0, canvas.getWidth(),canvas.getHeight());

        t = (System.currentTimeMillis() - time1)/1000;

        //картинки в прямоугольниках-целях
        if (step == 0){
            img = sheep_go;
        }
        if (step == 50 || step == 125){
            img = black;
        }
        if (step == 75 && r <= 200){
            img = sheep_no;
        }
        if (step == 75 && r > 200){
            step = -1;
        }
        if (step == 150 ){
            step = -1;
        }
        step++;

        gc.drawImage(img, target.getX(), target.getY());

        gc.setFill( Color.WHITE );
        String pointsText = "Считай только спящих овец!";
        gc.fillText( pointsText, 400, 50 );

        gc.fillText("Меню", 40, 50);
        gc.fillText("Осталось посчитать: "+(counter), 125, 50);
        gc.fillText("Прошло: "+t+" секунд", 757, 50);
        paintFarmer();
    }

    private void menuClick(double x, double y) {
        if (x > 40 && x < 110 && y > 30 && y < 50){
            timer.stop();
            controller.showMenu(); // возврат в меню
        }
    }

    private void paintFarmer(){
        girl = new Image( getClass().getResourceAsStream("images/girl.png") );
        gc.drawImage(girl, 700, 230);
    }

    private void endGame(){
        if (counter == -1){
            time2 = System.currentTimeMillis();
            Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 18 );
            gc.setFont( theFont );
            String s = "";
            if (points >= GONO -2){
                s = "Спокойной ночи!";
            }
            else{
                s = "Опять не спится...";
            }
            error = GONO-points;
            gc.fillText("«Ты насчитал "+points+" овец,\n" +
                    " и при этом "+(error)+" ошибок! \n"+s, 700, 150);
            int time = (int)t;
            controller.res.put("go", new int[]{time, error, points});
            Controller1.trainNum ++;
            timer.stop();
        }
    }
}

