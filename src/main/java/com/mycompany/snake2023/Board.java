/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.snake2023;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author victor
 */
public class Board extends javax.swing.JPanel {
 
    private Snake snake;
    public static final int NUM_ROWS = 20;
    public static final int NUM_COLS = 20;
    private Timer timer;
    private MyKeyAdapter keyAdapter;
    private Food food;
    private FoodFactory foodFactory;
    private List<Direction> movements;
    private ScoreInterface scoreInterface;
    private int hightscore;
    
    class MyKeyAdapter extends KeyAdapter {
        
        private boolean paused = false;

        public boolean isPaused() {
            return paused;
        }

        public void setPaused(boolean paused) {
            this.paused = paused;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (snake.getDirection() != Direction.RIGHT) {
                        snake.setDirection(Direction.LEFT);
                        movements.add(Direction.LEFT);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (snake.getDirection() != Direction.LEFT) {
                        snake.setDirection(Direction.RIGHT);
                         movements.add(Direction.RIGHT);
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (snake.getDirection() != Direction.DOWN) {
                        snake.setDirection(Direction.UP);
                         movements.add(Direction.UP);
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (snake.getDirection() != Direction.UP) {
                        snake.setDirection(Direction.DOWN);
                         movements.add(Direction.DOWN);
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    paused = !paused;
                    break;
                default:
                    break;
            }
            repaint();
        }
    }
    
    public Board() {
        initComponents();

        timer = new Timer(250, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!keyAdapter.isPaused()) {
                    tick();
                }
            }
        });
        
        setFocusable(true);
        keyAdapter = new MyKeyAdapter();
        foodFactory = new FoodFactory();
        hightscore = 0;
        initGame();
    }
    
    public void setScoreInterface(ScoreInterface scoreInterface) {
        this.scoreInterface = scoreInterface;
    }
    
    public void setIncrementer(ScoreInterface incrementer) {
        this.scoreInterface = incrementer;
    }
    
    private void initGame() {
        snake = new Snake(4);
        movements = new Vector<>(2);
        food = foodFactory.getFood(snake);
        addKeyListener(keyAdapter);
        if (scoreInterface != null) {
            scoreInterface.reset();
        }
        timer.start();
    }
    
    private void tick() {
        if(movements.size() != 0){
            Direction dir = movements.get(0);
            snake.setDirection(dir);
            movements.remove(0);
        }
        if (snake.canMove()) {
            snake.move();
            if (snake.eat(food)) {
                scoreInterface.increment(food.getPoints());
                food = foodFactory.getFood(snake);
            }
            if (food.hasToBeErased()) {
                food = foodFactory.getFood(snake);
            }        
        } else {
            processGameOver();
        }
        
        repaint();
        Toolkit.getDefaultToolkit().sync();
    }
    
    private void processGameOver() {
        timer.stop();
        removeKeyListener(keyAdapter);
        int score = scoreInterface.getScore();
        if(score> hightscore){
           hightscore = score;
       }
        int answer = JOptionPane.showConfirmDialog(this,  score 
                + " points\n" + hightscore + " hight score\nNew Game?",
                   "Game Over", JOptionPane.YES_NO_OPTION);
        if (answer == 0) {
            initGame();
        } else {
            System.exit(0);
        }
        // JFrame parentJFrame = (JFrame) SwingUtilities.getWindowAncestor(this); 
        // HighScoresDialog dialog = new HighScoresDialog(parentJFrame ,true);
        // dialog.setGetScorer(getScorer);
        // dialog.setVisible(true);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    int getSquareWidth() {
        return getWidth() / NUM_COLS;        
    }
    
    int getSquareHeight() {
        return getHeight() / NUM_ROWS;
    }
  
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        if (snake != null) {
            snake.paint(g, getSquareWidth(), getSquareHeight());
        }
        if (food != null) {
            food.paint(g, getSquareWidth(), getSquareHeight());
        }        
    }
    
    public void drawBackground(Graphics g) {        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, getWidth()/ NUM_COLS * NUM_COLS, 
                getHeight() / NUM_ROWS * NUM_ROWS);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
