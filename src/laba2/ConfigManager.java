/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

/**
 *
 * @author Asus
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConfigManager {

    FileInputStream fi;
    FileOutputStream fo;

    private final String PATH = "D:\\config.txt";

    public ConfigManager() {
    }

    public int[] GetConfig() {
        int[] configData = new int[6];
        try {
            fi = new FileInputStream(PATH);
            DataInputStream dis = new DataInputStream(fi);
            for (int i=0; i<6; i++){
                configData[i] = dis.readInt();
            }
            dis.close();
            fi.close();

        } catch (FileNotFoundException ex) {
            System.out.println("InputFile not found");
        } catch (IOException ex) {
            System.out.println("Cant read from file");
        }

        return configData;
    }

    public void SaveConfig(int towNormal, int towAlbino, int ctbNormal, int popAlbino, int nlolNormal, int nlolAlbino) {

        try {
            fo = new FileOutputStream(PATH, false);
        } catch (IOException ex) {
            System.out.println("OutputFile not found");
        }

        DataOutputStream dos = new DataOutputStream(fo);

        try {
            dos.writeInt(towNormal);
            dos.writeInt(towAlbino);
            dos.writeInt(ctbNormal);
            dos.writeInt(popAlbino);
            dos.writeInt(nlolNormal);
            dos.writeInt(nlolAlbino);
            dos.flush();
            dos.close();
            fo.flush();
            fo.close();
        } catch (IOException ex) {
            System.out.println("Cant write to file");
        }

    }
}
