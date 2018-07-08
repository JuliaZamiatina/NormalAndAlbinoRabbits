/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.JOptionPane;

/**
 *
 * @author Asus
 */
public abstract class Rabbit implements IBehaviour, Serializable {

    int xPos = 0;
    int yPos = 0;
    private int timeOfWaiting;
    private double timeOfBorn;
    private int lengthOfLife;
    long code;

    @Override
    public boolean ShouldBeBorn(double percentage) {
        return false;
    }

    @Override
    public String toString() {
        return "Кролик №" + code;
    }

    ;
    @Override
    public boolean ShouldBeBorn() {
        return false;
    }

    ;
    @Override
    public void DrawARabbit(Graphics g, int xCoord, int yCoord) {
        xPos = xCoord;
        yPos = yCoord;
        g.fillOval(xCoord, yCoord + 5, 20, 10);
        g.fillOval(xCoord + 3, yCoord, 5, 10);
        g.fillOval(xCoord + 12, yCoord, 5, 10);
    }

    public void setTimeOfWaiting(int t) {
        try {
            if ((t <= 0) || (t > 10)) {
                throw new ArithmeticException();
            } else {
                this.timeOfWaiting = t;
            }
        } catch (ArithmeticException e) {
            JOptionPane.showMessageDialog(null, "Введенное значение выходит за границы допустимого, времени ожидания присвоено стандартное значение '1'", "Ошибка в значении", JOptionPane.WARNING_MESSAGE);
            this.timeOfWaiting = 1;
        }
    }

    public void exterminate(Graphics g) {
        g.fillRect(xPos, yPos, xPos + 15, yPos + 20);
    }

    public int getTimeOfWaiting() {
        return this.timeOfWaiting;
    }

    public void setTimeOfBorn(double time) {
        this.timeOfBorn = time;
    }

    public double getTimeOfBorn() {
        return this.timeOfBorn;
    }

    public int getLengthOfLife() {
        return this.lengthOfLife;
    }

    public void setLenghtOfLife(int l) {
        this.lengthOfLife = l;
    }

}
