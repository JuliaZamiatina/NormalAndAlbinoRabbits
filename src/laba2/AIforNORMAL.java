/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.util.*;
import javax.swing.JFrame;

/**
 *
 * @author Asus
 */
public class AIforNORMAL extends BaseAI {

    int dx;
    int dy;
    int newXCoord;
    int newYCoord;
    Random rand = new Random();
    Laba2 parent;

    public AIforNORMAL(Vector vNormal, Laba2 parentFrame) {
        vNorm = vNormal;
        parent = parentFrame;
    }

    @Override
    public void count() {
        Iterator it = vNorm.iterator();
        while (it.hasNext()) {
            NormalRabbit temp = (NormalRabbit) it.next();

            dx = rand.nextInt() % 4;
            dy = rand.nextInt() % 4;

            newXCoord = temp.xPos + dx * V;
            newYCoord = temp.yPos + dy * V;
            temp.xPos = newXCoord;
            temp.yPos = newYCoord;
        }
    }
}
