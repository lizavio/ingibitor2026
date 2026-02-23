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

public class StroopGame {
    private GraphicsContext gc;
    private Canvas canvas;
    private Controller2 controller;
    int points;
    Image yellow, green, blue, red, w_farmer;
    Rectangle targetData1, targetData2, targetData3, targetData4;
    List<String> titles;
    List<Rectangle> targets;
    Random rand;
    int r, r1;
    int counter = 30; //счетчик игры
    int SIMON = counter;
    double alpha;
    int blinkIndex;
    private AnimationTimer timer;
    long time1;
    long time2;


    public StroopGame(Canvas canvas, Controller2 controller) {
        this.canvas = canvas;
        this.controller = controller;
        gc = canvas.getGraphicsContext2D();

        rand = new Random();

        titles = new ArrayList<>();
        titles.add("желтое");
        titles.add("зеленое");
        titles.add("синее");
        titles.add("красное");

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

        yellow = new Image( getClass().getResourceAsStream("images/egg1.png") );
        green = new Image( getClass().getResourceAsStream("images/egg2.png") );
        blue = new Image( getClass().getResourceAsStream("images/egg3.png") );
        red = new Image( getClass().getResourceAsStream("images/egg4.png") );

        calculate();
        r = rand.nextInt(titles.size());
        r1 = rand.nextInt(titles.size());
        timer = new AnimationTimer()//бесконечный цикл 60 р/с
        {
            public void handle(long currentNanoTime) {
                draw(r, r1);
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
                        if ( targets.get(r1).contains( e.getX(), e.getY() ) ) {
                            points++;
                        }
                        r = rand.nextInt(titles.size());
                        r1 = rand.nextInt(titles.size());
                    }
                });
    }

    private void draw(int r, int r1) {
        //очистка холста
        gc.setFill( new Color(0.5, 0.8, 0.5, 1) );
        gc.fillRect(0,0, canvas.getWidth(),canvas.getHeight());

        //картинки в прямоугольниках-целях
        Image[] images = { yellow, green, blue, red };
        for (int i = 0; i < targets.size(); i++) {
            Rectangle t = targets.get(i);
            gc.drawImage(images[i], t.getX(), t.getY());
        }

        Color[] cols = {Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED};
        gc.setFill(cols[r1]);
        String pointsText = "Найди : " + titles.get(r) + " яйцо "+points;
        gc.fillText( pointsText, 420, 50 );

        gc.setFill( Color.BLACK );
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
        w_farmer = new Image( getClass().getResourceAsStream("images/w_farmer.png") );
        gc.drawImage(w_farmer, 700, 230);
    }

    private void endGame(){
        if (counter == -1){
            time2 = System.currentTimeMillis();
            Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 18 );
            gc.setFont( theFont );
            String s = "";
            if (SIMON-points <= 2){
                s = "Теперь идем есть куличи!";
            }
            else{
                s = "Придется еще поискать.";
            }
            gc.fillText("«Ты нашел "+points+" яиц,\n" +
                    " и при этом "+(SIMON-points)+" ошибок! \n"+s, 720, 170);
            timer.stop();
        }
    }
}