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
public class NormalRabbit extends Rabbit implements Serializable{

    double chanceToBorn;
    Color Ncolor = Color.BLACK;

    public NormalRabbit(double t, int l) {
        this.setTimeOfBorn(t);
        this.setLenghtOfLife(l);
    }

    ;
    @Override
    public boolean ShouldBeBorn() {
        boolean BecameBorn = false;
        double temp = (double) Math.random();
        if (temp <= chanceToBorn) {
            BecameBorn = true;
        }
        return BecameBorn;
    }

    public void setChanceToBorn(double c) {
        this.chanceToBorn = c;
    }

    public void DrawARabbit(Graphics g, int xCoord, int yCoord) {
        g.setColor(Ncolor);
        super.DrawARabbit(g, xCoord, yCoord);
        g.setColor(Color.WHITE);
        g.fillOval(xCoord + 3, yCoord + 7, 2, 3);
        g.fillOval(xCoord + 12, yCoord + 7, 2, 3);
    }
}
