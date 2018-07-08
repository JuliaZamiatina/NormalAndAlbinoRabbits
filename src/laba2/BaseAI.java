/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asus
 */
public abstract class BaseAI extends Thread {

    boolean going = true;
    Vector vNorm;
    Vector vAlb;
    int V = 1;
    boolean flag = true;

    public abstract void count();

    @Override
    public synchronized void run() {
        while (going) {
            if (flag) {
                try {
                    this.wait();
                } catch (InterruptedException ex) {

                }
            }
            count();
            try {
                this.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
    }

}
