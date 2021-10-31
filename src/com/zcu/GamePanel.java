package com.zcu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import static com.zcu.GVAR.*;

public class GamePanel extends JPanel implements ActionListener {

    boolean didChange=false;
    int x[] = new int[GAME_UNITS];
    int y[] = new int[GAME_UNITS];
    int bodyParts = 3;
    int applesEaten =0;
    int appleX;
    int appleY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;
    GamePanel(){
        random = new Random();
        timer = new Timer(DELAY, this);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        StartGame();

    }
    public void StartGame(){

        NewApple();
        bodyParts=3;
        direction='D';
        x[0]=0;
        y[0]=0;
        running = true;
        timer.start();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Draw(g);
    }
    public void Draw(Graphics g){
        for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_WIDTH);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_HEIGHT, i*UNIT_SIZE);

        }
        g.setColor(Color.RED);
        g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        for (int i = 0; i < bodyParts; i++) {
            if (i==0){
                g.setColor(Color.GREEN);
            } else {
                g.setColor(new Color(45, 180, 0));
            }
            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

        }
    }
    public void NewApple(){
        appleX = random.nextInt((int) ((SCREEN_WIDTH/UNIT_SIZE)-2))*UNIT_SIZE+UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT/UNIT_SIZE)-2)*UNIT_SIZE+UNIT_SIZE;
    }
    public void Move(){
        didChange= false;
        for (int i = bodyParts; i>0 ; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case 'W':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'S':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'A':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'D':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void AddBodyOnApple(){
        if (CheckCollisionsApple()) {
            bodyParts++;
            //DELAY-=5;
            timer.setDelay(DELAY);
            NewApple();
        }
    }
    public boolean CheckCollisionsApple(){
        if (x[0]==appleX&&y[0]==appleY){
            return true;
        }
        return false;
    }
    public boolean CollisionWithBody(){
        boolean collided = false;
        for (int i = 0; i < bodyParts; i++) {
            for (int j = 0; j < bodyParts; j++) {
                if ((x[i]==x[j]&&y[i]==y[j])&&x[i]!=0&&y[i]!=0&&i!=j) {
                    collided=true;
                }
            }
        }
        return collided;
    }
    public void GameOver(){
        if (x[0] == SCREEN_WIDTH || y[0] == SCREEN_HEIGHT||x[0] <0|| y[0] < 0) {
            running = false;
        }
        if (CollisionWithBody()){
            running = false;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //running=GAME_STARTED;
        GameOver();
        if (running){
            Toolkit.getDefaultToolkit().sync();
            AddBodyOnApple();
            Move();
            this.repaint();
        }
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){

            switch (e.getKeyCode()){
                case KeyEvent.VK_A:
                    if (direction != 'D'&& direction!='A'&&!didChange){
                        direction = 'A';
                        didChange=true;
                    }
                    break;
                case KeyEvent.VK_D:
                    if (direction != 'A'&& direction!='D'&&!didChange){
                        direction = 'D';
                        didChange=true;
                    }
                    break;
                case KeyEvent.VK_W:
                    if (direction != 'S'&& direction!='W'&&!didChange){
                        direction = 'W';
                        didChange=true;
                    }
                    break;
                case KeyEvent.VK_S:
                    if (direction != 'W'&& direction!='S'&&!didChange){
                        direction = 'S';
                        didChange=true;
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    StartGame();
            }
        }
    }
}
