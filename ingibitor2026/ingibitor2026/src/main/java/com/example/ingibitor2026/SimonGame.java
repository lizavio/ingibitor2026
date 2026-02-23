package com.example.ingibitor2026;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimonGame {
    private GraphicsContext gc;
    private Canvas canvas;
    private Controller2 controller;
    int points;
    Image car, tom, bak, cuc, farmer1;
    Rectangle targetData1, targetData2, targetData3, targetData4;
    List<String> titles;
    List<Rectangle> targets;
    Random rand;
    int r;
    int counter = 10; //счетчик игры
    int SIMON = counter;
    double alpha;
    int blinkIndex;
    private AnimationTimer timer;
    long time1;
    long time2;


    public SimonGame(Canvas canvas, Controller2 controller) {
        this.canvas = canvas;
        this.controller = controller;
        gc = canvas.getGraphicsContext2D();

        rand = new Random();

        titles = new ArrayList<>();
        titles.add("морковь");
        titles.add("помидор");
        titles.add("огурец");
        titles.add("баклажан");

        targetData1 = new Rectangle(150, 150, 150,150);
        targetData2 = new Rectangle(400, 400, 150,150);
        targetData3 = new Rectangle(400, 150, 150,150);
        targetData4 = new Rectangle(150, 400,150,150);
        targets = new ArrayList<>();
        targets.add(targetData1);
        targets.add(targetData2);
        targets.add(targetData3);
        targets.add(targetData4);

        Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 24 );
        gc.setFont( theFont );
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(1);

        car = new Image( getClass().getResourceAsStream("images/carrot.png") );
        tom = new Image( getClass().getResourceAsStream("images/tomat.png") );
        bak = new Image( getClass().getResourceAsStream("images/bakl.png") );
        cuc = new Image( getClass().getResourceAsStream("images/cucumber.png") );

        calculate();
        r = rand.nextInt(titles.size());
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
                        if ( targets.get(r).contains( e.getX(), e.getY() ) ) {
                            points++;
                        }
                        r = rand.nextInt(titles.size());
                        blinkIndex = rand.nextInt(targets.size());
                    }
                });
    }

    double a=0;
    private void draw(int r) {

                //очистка холста
                gc.setFill( new Color(0, 0.85, 0.2, 0.8) );
                gc.fillRect(0,0, canvas.getWidth(),canvas.getHeight());

                //картинки в прямоугольниках-целях
                //эффект мерцания
        alpha += 0.05;
        if (alpha > 1) alpha = 0.2;

        Image[] images = { car, tom, cuc, bak };
        for (int i = 0; i < targets.size(); i++) {
            if (i == blinkIndex) {
                gc.setGlobalAlpha(alpha);   // мерцающая
            } else {
                gc.setGlobalAlpha(1.0);     // обычные
            }

            Rectangle t = targets.get(i);
            gc.drawImage(images[i], t.getX(), t.getY());
        }

        gc.setGlobalAlpha(1);

                gc.setFill( Color.BLACK );
                String pointsText = "Собери: " + titles.get(r) + " "+points;
                gc.fillText( pointsText, 450, 50 );
                //gc.strokeText( pointsText, 450, 50 );

                gc.fillText("Меню", 50, 50);
                gc.fillText("Осталось собрать: "+counter, 150, 50);
                long t = (System.currentTimeMillis() - time1)/1000;
                gc.fillText("Прошло: "+t+" секунд", 720, 50);
        paintFarmer();
    }

    private void menuClick(double x, double y) {
        if (x > 50 && x < 120 && y > 30 && y < 50){
            timer.stop();
            controller.showMenu(); // возврат в меню
        }
    }

    private void paintFarmer(){
        farmer1 = new Image( getClass().getResourceAsStream("images/farmer1.png") );
        gc.drawImage(farmer1, 680, 230);
    }

    private void endGame(){
        if (counter == -1){
            time2 = System.currentTimeMillis();
            Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 18 );
            gc.setFont( theFont );
            String s = "";
            if (SIMON-points <= 2){
                s = "Теперь устроим \n праздник урожая!";
            }
            else{
                s = "Придется еще поработать.";
            }
            gc.fillText("«Ты собрал "+points+" овощей,\n" +
                    " и при этом "+(SIMON-points)+" ошибок! \n"+s, 750, 130);
            timer.stop();
        }
    }
}
