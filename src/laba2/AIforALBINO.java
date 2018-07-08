/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Asus
 */
public class AIforALBINO extends BaseAI {

    int range;
    int newXCoord;

    public AIforALBINO(Vector vAlbino, int AppletWidth) {
        vAlb = vAlbino;
        range = AppletWidth;
    }

    @Override
    public void count() {
        Iterator it = vAlb.iterator();
        while (it.hasNext()) {
            AlbinoRabbit temp = (AlbinoRabbit) it.next();
            if (temp.xPos < 550) {
                newXCoord = temp.xPos + V;
                temp.xPos = newXCoord;
            }
        }
    }

}
