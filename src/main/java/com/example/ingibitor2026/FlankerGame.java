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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlankerGame {
    private GraphicsContext gc;
    private Canvas canvas;
    private Controller2 controller;
    int points;
    Image duck, chiken, boy, left, right, green;
    Rectangle targetData1, targetData2;
    List<String> titles;
    List<Rectangle> targets;
    Random rand;
    int r1, r2;
    int counter = 30; //счетчик игры
    int SIMON = counter;
    int error;
    int step;
    int birdX1, birdX2, birdX3, birdX4, birdX5;
    private AnimationTimer timer;
    long time1;
    long time2;
    long t;


    public FlankerGame(Canvas canvas, Controller2 controller) {
        this.canvas = canvas;
        this.controller = controller;
        gc = canvas.getGraphicsContext2D();

        rand = new Random();
        r1 = rand.nextInt(2);
        r2 = rand.nextInt(2);

        targetData1 = new Rectangle(100, 420, 120,120);
        targetData2 = new Rectangle(750, 420, 120,120);

        Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 24 );
        gc.setFont( theFont );
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(1);

        duck = new Image( getClass().getResourceAsStream("images/duck.png") );
        chiken = new Image( getClass().getResourceAsStream("images/chiken.png") );
        green = new Image( getClass().getResourceAsStream("images/green.png") );
        left = new Image( getClass().getResourceAsStream("images/l2.png") );
        right = new Image( getClass().getResourceAsStream("images/l1.png") );

        birdX1 = 250;
        birdX2 = 350;
        birdX3 = 450;
        birdX4 = 550;
        birdX5 = 650;

        calculate();
        timer = new AnimationTimer()//бесконечный цикл 60 р/с
        {
            public void handle(long currentNanoTime) {
                draw();
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

                        if ( r1 == 0 && targetData1.contains(e.getX(), e.getY()) ||
                                r1 == 1 && targetData2.contains(e.getX(), e.getY())) {
                            points++;
                            step = 150;
                        }
                    }
                });
    }

    private void draw() {
        //очистка холста
        gc.setFill( new Color(0, 0.7, 0.2, 0.8) );
        gc.fillRect(0,0, canvas.getWidth(),canvas.getHeight());

        //картинки в прямоугольниках-целях
        gc.setFill( Color.BLACK );
        String pointsText = "Направь среднего птенца: "+points;
        gc.fillText( pointsText, 350, 50 );

        gc.drawImage(left, targetData1.getX(), targetData1.getY());
        gc.drawImage(right, targetData2.getX(), targetData2.getY());

        gc.fillText("Меню", 50, 50);
        gc.fillText("Осталось: "+counter, 150, 50);
        t = (System.currentTimeMillis() - time1)/1000;
        gc.fillText("Прошло: "+t+" секунд", 720, 50);
        paintFarmer();

        generateBirds();
    }

    private void generateBirds() {
        if (step >= 0 && step < 100){
            if(r1==1){
                gc.drawImage(chiken, birdX3, 200);
            }
            else{
                gc.drawImage(duck, birdX3, 200);
            }
            if(r2==1){
                gc.drawImage(chiken, birdX1, 200);
                gc.drawImage(chiken, birdX2, 200);
                gc.drawImage(chiken, birdX4, 200);
                gc.drawImage(chiken, birdX5, 200);
            }
            else{
                gc.drawImage(duck, birdX1, 200);
                gc.drawImage(duck, birdX2, 200);
                gc.drawImage(duck, birdX4, 200);
                gc.drawImage(duck, birdX5, 200);
            }
        }
        if (step >= 100 && step < 150){
            gc.drawImage(green, birdX1, 200);
            gc.drawImage(green, birdX2, 200);
            gc.drawImage(green, birdX3, 200);
            gc.drawImage(green, birdX4, 200);
            gc.drawImage(green, birdX5, 200);
        }
        if (step == 150 ){
            step = -1;
            r1 = rand.nextInt(2);
            r2 = rand.nextInt(2);
        }
        step++;

    }

    private void menuClick(double x, double y) {
        if (x > 50 && x < 120 && y > 30 && y < 50){
            timer.stop();
            controller.showMenu(); // возврат в меню
        }
    }

    private void paintFarmer(){
        boy = new Image( getClass().getResourceAsStream("images/boy.png") );
        gc.drawImage(boy, 460, 430);
    }

    private void endGame(){
        if (counter == -1){
            time2 = System.currentTimeMillis();
            Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 18 );
            gc.setFont( theFont );
            String s = "";
            if (SIMON-points <= 2){
                s = "Теперь все птенцы дома!";
            }
            else{
                s = "Птички потерялись! \n Придется их искать.";
            }
            error = SIMON-points;
            gc.fillText("«Ты помог "+points+" птенцам,\n" +
                    " и при этом "+(error)+" ошибок!", 260, 440);
            gc.fillText(s, 550, 440);

            int time = (int)t;
            controller.res.put("flanker", new int[]{time, error, points});

            Controller1.trainNum ++;
            timer.stop();
        }
    }
}
