/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

/**
 *
 * @author Asus
 */
public class AlbinoRabbit extends Rabbit implements Serializable{

    double percentageOfPresence = -1;
    Color AColor = Color.lightGray;

    public AlbinoRabbit(double t, int l) {
        this.setTimeOfBorn(t);
        this.setLenghtOfLife(l);
    }

    ;
    @Override
    public boolean ShouldBeBorn(double percentage) {
        boolean becameBorn = false;
        if (percentageOfPresence > percentage) {
            becameBorn = true;
        }
        return becameBorn;
    }

    public void setPercentageOfPresence(double p) {
        this.percentageOfPresence = p;
    }

    public void DrawARabbit(Graphics g, int xCoord, int yCoord) {
        g.setColor(AColor);
        super.DrawARabbit(g, xCoord, yCoord);
        g.setColor(Color.red);
        g.fillOval(xCoord + 3, yCoord + 7, 2, 3);
        g.fillOval(xCoord + 12, yCoord + 7, 2, 3);
    }
}
