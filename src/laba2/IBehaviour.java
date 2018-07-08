/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.awt.Graphics;

/**
 *
 * @author Asus
 */
public interface IBehaviour {

    boolean ShouldBeBorn(double percentage);

    boolean ShouldBeBorn();

    void DrawARabbit(Graphics g, int xCoord, int yCoord);
}
