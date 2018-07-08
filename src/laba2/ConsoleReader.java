/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.awt.Graphics;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Asus
 */
public class ConsoleReader extends Thread {

    private Laba2 app;
    private PipedReader pr;
    private int code;

    public ConsoleReader(PipedWriter pw, Laba2 l2) {
        try {
            pr = new PipedReader(pw);
        } catch (IOException ex) {

        }
        app = l2;
    }

    @Override
    public void run() {

        while (true) {
            System.out.println("Runnin");
            try {
                code = (int) pr.read();
                int size = app.allAlbinoRabbits.size();
                System.out.println(size);
                float percent = code / 100;
                size = (int) (size * percent);
                System.out.println(size);

                Iterator iAlb = app.allAlbinoRabbits.iterator();
                int i = 0;
                try {
                    while (iAlb.hasNext() && (i < size)) {
                        AlbinoRabbit temp = (AlbinoRabbit) iAlb.next();
                        temp.setLenghtOfLife(0);
                        i++;
                    }
                } catch (ConcurrentModificationException e) {
                }
            } catch (IOException e) {
                System.out.println("Failed to read pipe");
            }
        }
    }
}
