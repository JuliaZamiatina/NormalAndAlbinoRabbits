/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedWriter;
import static java.lang.Thread.*;
import java.util.*;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Laba2 extends JApplet {

    private Timer m_timer;
    private Updater upd;
    private boolean m_runViaFrame = false;
    double temp_time = 0;
    double m_time = 0;
    int lolNormal;
    int lolAlbino;
    NormalRabbit firstNormalRabbit = new NormalRabbit(0, 0);
    AlbinoRabbit firstAlbinoRabbit = new AlbinoRabbit(0, 0);
    Vector allNormalRabbits = new Vector();
    Vector allAlbinoRabbits = new Vector();
    static JFrame frame = new JFrame("Rabbits");
    static JButton startButton = new JButton("Старт");
    static JButton stopButton = new JButton("Стоп");
    static JButton againButton = new JButton("<html>Начать симуляцию<p>с новыми параметрами</html>");
    static JButton currentButton = new JButton("Текущие объекты");
    static JButton startMovingButton = new JButton("Начать движение");
    static JButton stopMovingButton = new JButton("Остановить движение");
    static Label labelForThreadPriority = new Label("Приоритет потоков");
    static Laba2 applet = new Laba2(true);
    static JCheckBox showInf = new JCheckBox("Показать информацию");
    static JComboBox threadPriority;
    static JRadioButton showTimerRadioButton = new JRadioButton("Показать таймер");
    static JRadioButton hideTimerRadioButton = new JRadioButton("Скрыть таймер");
    static JMenuBar mainMenu = new JMenuBar();
    TreeSet tnormal, talbino;
    HashMap hnormal, halbino;
    AIforNORMAL nAI;
    AIforALBINO aAI;
    ConsoleReader cReader;
    boolean IsWaiting = true;
    ConfigManager cnfgManager = new ConfigManager();
    int towNormal, towAlbino;
    int ctbNormal, popAlbino, nlolNormal, nlolAlbino;
    firstMessageBox f;

    private class Updater extends TimerTask {

        private Laba2 m_applet = null;
        // Первый ли запуск метода run()?
        private boolean m_firstRun = true;
        // Время начала
        private long m_startTime = 0;
        // Время последнего обновления
        private long m_lastTime = 0;

        public Updater(Laba2 applet) {
            m_applet = applet;
        }

        @Override
        public void run() {
            if (m_firstRun) {
                m_startTime = System.currentTimeMillis();
                m_lastTime = m_startTime;
                m_firstRun = false;
            }
            long currentTime = System.currentTimeMillis();
            // Время, прошедшее от начала, в секундах
            int elapsed = (int) (currentTime - m_startTime) / 1000;
            // Время, прошедшее с последнего обновления, в секундах
            int frameTime = (int) (m_lastTime - m_startTime) / 1000;
            // Вызываем обновление
            m_applet.Update(elapsed, frameTime);
            m_lastTime = currentTime;
        }
    }

    public Laba2() {
        Init();
    }

    public Laba2(boolean viaFrame) {
        m_runViaFrame = viaFrame;
        Init();
    }

    class MessageBox extends JDialog {

        JTextArea a;
        JButton ok;
        JButton cansel;

        public MessageBox(JFrame owner, String s) {
            super(owner, "Rabbits", true);

            this.setLayout(new FlowLayout());
            setSize(400, 400);

            a = new JTextArea(s, 5, 30);
            a.setEditable(false);
            this.add(a);

            MessageBox t = this;
            JPanel panel = new JPanel();

            ok = new JButton("OK");
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showInf.setSelected(false);
                    t.dispose();
                }
            });
            this.add(ok);

            cansel = new JButton("Cansel");
            cansel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showInf.setSelected(false);
                    t.dispose();
                    startButton.doClick();
                }
            });
            this.add(cansel);

        }
    }

    class ConsoleBox extends JDialog {

        private PipedWriter pw;
        private TextField input;
        private TextArea output;
        private JPanel panel;
        private ConsoleBox mB = this;
        int code;

        public ConsoleBox(JFrame p, String t, boolean modal) {
            super(p, t, modal);
            this.setLocationRelativeTo(p);
            setSize(300, 300);
            pw = new PipedWriter();
            cReader = new ConsoleReader(pw, applet);
            cReader.start();
            panel = new JPanel();
            input = new TextField(36);
            input.setEditable(true);
            panel.add(input);
            output = new TextArea(13, 36);
            output.setEditable(true);
            panel.add(output);
            add(panel);
            input.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String command = input.getText();
                    output.append(input.getText() + "\n");
                    if (command.startsWith("reduce")) {
                        String percent = command.replaceFirst("\\D*", "");
                        System.out.println("Проценты" + percent);
                        code = Integer.valueOf(percent);
                        if ((code <= 0) || (code > 100)) {
                            output.append("Неверные данные!" + '\n');
                        } else {
                            try {
                                pw.write(code);
                                System.out.println("Код" + code);
                                output.setText(output.getText() + "Done\n");
                            } catch (IOException ex) {
                                System.out.println("Failed to pipe");
                            }
                        }
                    } else {
                        output.append("Неизвестная команда!" + '\n');
                    }
                    input.setText("");
                }
            });
        }
    }

    class firstMessageBox extends JDialog {

        JTextArea towNormalController;
        JTextArea towAlbinoController;
        JComboBox ctbNormalController;
        JComboBox popAlbinoController;
        JComboBox lolNormalController;
        JComboBox lolAlbinoController;
        JButton okButton;
        Label l;

        private void SetupConfigData() {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            int[] conf = cnfgManager.GetConfig();

            for (int i = 0; i < 6; i++) {
                System.out.println(conf[i]);
            }
            towNormalController.setText(String.valueOf(conf[0]));
            towAlbinoController.setText(String.valueOf(conf[1]));

            if (conf[2] > 9 || conf[2] < 0) {
                conf[2] = 3;
            }
            ctbNormalController.setSelectedIndex(conf[2]);

            if (conf[3] > 9 || conf[3] < 0) {
                conf[3] = 3;
            }
            popAlbinoController.setSelectedIndex(conf[3]);

            if (conf[4] > 5 || conf[4] < 0) {
                conf[4] = 3;
            }
            lolNormalController.setSelectedIndex(conf[4]);

            if (conf[5] > 5 || conf[5] < 0) {
                conf[5] = 3;
            }
            lolAlbinoController.setSelectedIndex(conf[5]);
        }

        public firstMessageBox(JFrame owner) {
            super(owner, "Запуск", true);

            this.setLayout(new BorderLayout());
            setSize(400, 400);

            l = new Label("Для начала введите данные:");
            l.setFont(new Font("Courier", Font.BOLD, 16));
            this.add(l, BorderLayout.NORTH);

            JPanel controlsPanel = new JPanel();
            controlsPanel.setLayout(new FlowLayout());

            towNormalController = new JTextArea();
            Label l1 = new Label("Ожидание для нормальных кроликов:");
            l1.setFont(new Font("Courier", Font.PLAIN, 14));
            towNormalController.setPreferredSize(new Dimension(20, 16));
            controlsPanel.add(l1);
            controlsPanel.add(towNormalController);

            towAlbinoController = new JTextArea();
            Label l2 = new Label("Ожидание для кроликов-альбиносов:");
            l2.setFont(new Font("Courier", Font.PLAIN, 14));
            towAlbinoController.setPreferredSize(new Dimension(20, 16));
            controlsPanel.add(l2);
            controlsPanel.add(towAlbinoController);

            Double[] items = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
            ctbNormalController = new JComboBox(items);
            Label l3 = new Label("Вер. появления нормальных кроликов:");
            l3.setFont(new Font("Courier", Font.PLAIN, 14));
            controlsPanel.add(l3);
            controlsPanel.add(ctbNormalController);

            popAlbinoController = new JComboBox(items);
            Label l4 = new Label("Относ. вер. появления альбиносов:");
            l4.setFont(new Font("Courier", Font.PLAIN, 14));
            controlsPanel.add(l4);
            controlsPanel.add(popAlbinoController);

            Integer[] items2 = {10, 5, 4, 3, 2, 1};
            lolNormalController = new JComboBox(items2);
            Label l5 = new Label("Время жизни нормальных кроликов:");
            l5.setFont(new Font("Courier", Font.PLAIN, 14));
            controlsPanel.add(l5);
            controlsPanel.add(lolNormalController);

            lolAlbinoController = new JComboBox(items2);
            Label l6 = new Label("Время жизни кроликов-альбиносов:");
            l6.setFont(new Font("Courier", Font.PLAIN, 14));
            controlsPanel.add(l6);
            controlsPanel.add(lolAlbinoController);

            this.add(controlsPanel);

            okButton = new JButton("OK");

            firstMessageBox t = this;
            SetupConfigData();

            okButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = towNormalController.getText();
                    towNormal = Integer.valueOf(s);
                    try {
                        if (s == null) {
                            throw new NullPointerException();
                        } else if (!s.matches("\\d+")) {
                            throw new NumberFormatException();
                        } else {
                            firstNormalRabbit.setTimeOfWaiting(new Integer(s));
                        }
                    } catch (NullPointerException exp) {
                        JOptionPane.showMessageDialog(null, "Вы не ввели значение, времени ожидания присвоено стандартное значение '1'", "Ошибка в значении", JOptionPane.WARNING_MESSAGE);
                        firstNormalRabbit.setTimeOfWaiting(1);
                        towNormalController.setText("1");
                    } catch (NumberFormatException exp) {
                        JOptionPane.showMessageDialog(null, "Время ожидания может быть только числом, времени ожидания присвоено стандартное значение '1'", "Ошибка в значении", JOptionPane.WARNING_MESSAGE);
                        firstNormalRabbit.setTimeOfWaiting(1);
                        towNormalController.setText("1");
                    }

                    s = towAlbinoController.getText();
                    towAlbino = Integer.valueOf(s);
                    try {
                        if (s == null) {
                            throw new NullPointerException();
                        } else if (!s.matches("\\d+")) {
                            throw new NumberFormatException();
                        } else {
                            firstAlbinoRabbit.setTimeOfWaiting(new Integer(s));
                        }
                    } catch (NullPointerException exp) {
                        JOptionPane.showMessageDialog(null, "Вы не ввели значение, времени ожидания присвоено стандартное значение '1'", "Ошибка в значении", JOptionPane.WARNING_MESSAGE);
                        firstAlbinoRabbit.setTimeOfWaiting(1);
                        towAlbinoController.setText("1");
                    } catch (NumberFormatException exp) {
                        JOptionPane.showMessageDialog(null, "Время ожидания может быть только числом, времени ожидания присвоено стандартное значение '1'", "Ошибка в значении", JOptionPane.WARNING_MESSAGE);
                        firstAlbinoRabbit.setTimeOfWaiting(1);
                        towAlbinoController.setText("1");
                    }

                    firstNormalRabbit.setChanceToBorn((double) ctbNormalController.getSelectedItem());
                    ctbNormal = ctbNormalController.getSelectedIndex();
                    firstAlbinoRabbit.setPercentageOfPresence((double) popAlbinoController.getSelectedItem());
                    popAlbino = popAlbinoController.getSelectedIndex();
                    lolNormal = (int) lolNormalController.getSelectedItem();
                    nlolNormal = lolNormalController.getSelectedIndex();
                    lolAlbino = (int) lolAlbinoController.getSelectedItem();
                    nlolAlbino = lolAlbinoController.getSelectedIndex();

                    t.dispose();
                }
            });
            this.add(okButton, BorderLayout.SOUTH);
        }
    }

    class CurrentMessegeBox extends JDialog {

        JTextArea current;
        Label l;

        public CurrentMessegeBox(JFrame owner) {
            super(owner, "Текущие", Dialog.ModalityType.DOCUMENT_MODAL);

            this.setLayout(new BorderLayout());
            setSize(400, 400);

            l = new Label("Текущие объекты:");
            l.setFont(new Font("Courier", Font.BOLD, 16));
            this.add(l, BorderLayout.NORTH);

            current = new JTextArea();
            String s = "Нормальные кролики:\n";
            Iterator it1 = hnormal.entrySet().iterator();
            while (it1.hasNext()) {
                s += it1.next().toString() + "\n";
            }

            s += "\nАльбиносы:\n";
            Iterator it2 = halbino.entrySet().iterator();
            while (it2.hasNext()) {
                s += it2.next().toString() + "\n";
            }
            current.setText(s);
            current.setEditable(false);
            this.add(current, BorderLayout.CENTER);
        }
    }

    class RabbitComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            return -1;
        }

    }

    private void Init() {
        f = new firstMessageBox(frame);
        f.setLocationRelativeTo(frame);
        f.setVisible(true);
        String[] items = {"Основное поток", "Поток расчетов"};
        threadPriority = new JComboBox(items);

        tnormal = new TreeSet(new RabbitComparator());
        talbino = new TreeSet(new RabbitComparator());

        hnormal = new HashMap();
        halbino = new HashMap();

        nAI = new AIforNORMAL(allNormalRabbits, applet);
        nAI.start();
        //nAI.suspend();

        aAI = new AIforALBINO(allAlbinoRabbits, this.getWidth());
        aAI.start();
        //aAI.suspend();

        frame.addWindowListener(new WindowListener() {

            public void windowClosing(WindowEvent event) {
                cnfgManager.SaveConfig(towNormal, towAlbino, ctbNormal, popAlbino, nlolNormal, nlolAlbino);
                System.exit(0);
            }

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopButton.setEnabled(true);
                startButton.setEnabled(false);
                applet.StartSimulation();
                repaint();
            }
        });
        againButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (stopButton.isEnabled()) {
                    stopButton.doClick();
                }
                temp_time = 0;
                allNormalRabbits.clear();
                allAlbinoRabbits.clear();
                Graphics g = applet.getGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, applet.getWidth(), applet.getHeight());
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(0, 20, applet.getWidth(), 20);
                repaint();

                firstMessageBox m = new firstMessageBox(frame);
                m.show();
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
                applet.CancelSimulation();
                repaint();
            }
        });
        currentButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentMessegeBox m = new CurrentMessegeBox(frame);
                m.show();
            }
        });

        startMovingButton.addActionListener(new ActionListener() {

            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                startMovingButton.setEnabled(false);
                stopMovingButton.setEnabled(true);
                nAI.flag = false;
                aAI.flag = false;
                synchronized (nAI) {
                    nAI.notifyAll();
                }
                synchronized (aAI) {
                    aAI.notifyAll();
                }
            }
        });

        stopMovingButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nAI.flag = true;
                aAI.flag = true;
                startMovingButton.setEnabled(true);
                stopMovingButton.setEnabled(false);
            }
        });

        threadPriority.setFocusable(false);

        threadPriority.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (threadPriority.getSelectedIndex() == 0) {
                    aAI.setPriority(MIN_PRIORITY);
                    nAI.setPriority(MIN_PRIORITY);
                    System.out.println("низкий приоритет");
                } else {
                    aAI.setPriority(MAX_PRIORITY);
                    nAI.setPriority(MAX_PRIORITY);
                    System.out.println("высокий приоритет");
                }
            }
        });

        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e);
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_B:
                        if (startButton.isEnabled()) {
                            startButton.doClick();
                        }
                        break;
                    case KeyEvent.VK_E:
                        if (stopButton.isEnabled()) {
                            stopButton.doClick();
                        }
                        break;
                    case KeyEvent.VK_T:
                        if (showTimerRadioButton.isSelected()) {
                            hideTimerRadioButton.setSelected(true);
                        } else {
                            showTimerRadioButton.setSelected(true);
                        }
                        repaint();
                        break;
                }
            }
        });
    }

    public String ShowStatistic() {
        String str = "Количество обычных кроликов = " + Integer.toString(allNormalRabbits.size());
        str += '\n' + "Количество альбиносов = " + Integer.toString(allAlbinoRabbits.size());
        str += '\n' + "Время работы = " + temp_time;
        return str;
    }

    public void StartSimulation() {
        m_timer = new Timer();
        upd = new Updater(applet);
        m_timer.schedule(upd, 0, 1000);
        showInf.setEnabled(true);
    }

    public void CancelSimulation() {
        temp_time += m_time;
        m_timer.cancel();
        if (showInf.isSelected()) {
            MessageBox m = new MessageBox(frame, this.ShowStatistic());
            m.setVisible(true);
        }
    }

    public void TimerController() {
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        String str = "Time = " + Double.toString(temp_time + m_time);
        g.drawString(str, 15, 15);
    }

    public synchronized void Update(int elapsedTime, int frameTime) {
        m_time = elapsedTime;
        this.repaint();
    }

    private void Serialize() {
        stopButton.doClick();

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:\\normalRabbits.ser"));
            oos.writeObject(allNormalRabbits);
            oos.flush();            
            oos.close();
        } catch (IOException e) {
            System.out.println("Failed to serialize!");
        }
        
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:\\albinoRabbits.ser"));       
            oos.writeObject(allAlbinoRabbits);
            oos.flush();            
            oos.close();
        } catch (IOException e) {
            System.out.println("Failed to serialize!");
        }
        
        startButton.doClick();
    }

    private void Deserialize() {
        stopButton.doClick();
        
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("D:\\normalRabbits.ser"));
            
            Vector temp = (Vector) ois.readObject();
            allNormalRabbits.clear();
            allNormalRabbits.addAll(temp);            
            ois.close();
        } catch (IOException e) {
            System.out.println("Failed to deserialize!IO1");
        } catch (ClassNotFoundException ex) {
            System.out.println("Failed to deserialize!");
        }
        
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("D:\\albinoRabbits.ser"));
            
            Vector temp = (Vector) ois.readObject();
            allAlbinoRabbits.clear();
            allAlbinoRabbits.addAll(temp);
            ois.close();
        } catch (IOException e) {
            System.out.println("Failed to deserialize!IO2");
        } catch (ClassNotFoundException ex) {
            System.out.println("Failed to deserialize!");
        }     
        
        repaint();
        temp_time = 0;
        startButton.doClick();
    }

    public long RandomGenerator(TreeSet t) {
        Random rand = new Random();

        long num = Math.abs(rand.nextInt());
        Iterator it = t.iterator();
        while (t.contains(num)) {
            num = Math.abs(rand.nextInt());
        }
        return num;
    }

    public int CreateCoords(int range) {
        Random rand = new Random();
        int coord = rand.nextInt(range) + 20;

        Iterator iNorm = allNormalRabbits.iterator();
        while (iNorm.hasNext()) {
            NormalRabbit temp = (NormalRabbit) iNorm.next();

            while ((Math.abs(temp.xPos - coord) < 20) || (Math.abs(temp.yPos - coord) < 20)) {
                coord = rand.nextInt(range) + 20;
            }
        }

        Iterator iAlb = allAlbinoRabbits.iterator();
        while (iAlb.hasNext()) {
            AlbinoRabbit temp = (AlbinoRabbit) iAlb.next();

            while ((Math.abs(temp.xPos - coord) < 20) || (Math.abs(temp.yPos - coord) < 20)) {
                coord = rand.nextInt(range) + 20;
            }
        }

        return coord;
    }

    @Override
    public void paint(Graphics g) {

        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        Iterator iNorm = allNormalRabbits.iterator();
        while (iNorm.hasNext()) {
            NormalRabbit temp = (NormalRabbit) iNorm.next();

            if (m_time + temp_time - temp.getTimeOfBorn() + 1 >= temp.getLengthOfLife()) {
                tnormal.remove(temp.code);
                hnormal.remove(temp.getTimeOfBorn());
                iNorm.remove();
            } else {
                temp.DrawARabbit(g, temp.xPos, temp.yPos);
            }

        }

        Iterator iAlb = allAlbinoRabbits.iterator();
        while (iAlb.hasNext()) {
            AlbinoRabbit temp = (AlbinoRabbit) iAlb.next();

            if (m_time + temp_time - temp.getTimeOfBorn() >= temp.getLengthOfLife()) {
                talbino.remove(temp.code);
                halbino.remove(temp.getTimeOfBorn());
                iAlb.remove();
            } else if (nAI.going) {
                temp.DrawARabbit(g, temp.xPos, temp.yPos);
            }
        }

        if (stopButton.isEnabled()) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 100, 15);
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(0, 20, this.getWidth(), 20);
            if (showTimerRadioButton.isSelected()) {
                System.out.println(m_time);
                this.TimerController();
            }
            if (m_time == 0) {
                return;
            }
            int[] Coordinates = new int[2];
            if ((m_time % firstNormalRabbit.getTimeOfWaiting()) == 0) {
                if (firstNormalRabbit.ShouldBeBorn()) {
                    NormalRabbit temp = new NormalRabbit(m_time + temp_time, lolNormal);
                    Coordinates[0] = CreateCoords(this.getWidth() - 100);
                    Coordinates[1] = CreateCoords(this.getHeight() - 100);
                    temp.DrawARabbit(g, Coordinates[0], Coordinates[1]);
                    temp.code = RandomGenerator(tnormal);
                    allNormalRabbits.add(temp);
                    tnormal.add(temp.code);
                    hnormal.put(temp.getTimeOfBorn(), temp);
                }
            }
            if ((m_time % firstAlbinoRabbit.getTimeOfWaiting()) == 0) {
                if (firstAlbinoRabbit.ShouldBeBorn((double) allAlbinoRabbits.size() / (double) allNormalRabbits.size())) {
                    AlbinoRabbit temp = new AlbinoRabbit(m_time + temp_time, lolAlbino);
                    Coordinates[0] = CreateCoords(this.getWidth() - 20);
                    Coordinates[1] = CreateCoords((this.getHeight() - 20) + 20);
                    temp.DrawARabbit(g, Coordinates[0], Coordinates[1]);
                    temp.code = RandomGenerator(talbino);
                    allAlbinoRabbits.add(temp);
                    talbino.add(temp.code);
                    halbino.put(temp.getTimeOfBorn(), temp);
                }
            }
        }
    }

    public void startConsole() {
        ConsoleBox cmb = new ConsoleBox(frame, "Консоль", false);
        cmb.setVisible(true);
        cmb.setFocusable(false);
    }

    public static void main(String[] args) {
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        ButtonGroup gr = new ButtonGroup();
        gr.add(showTimerRadioButton);
        gr.add(hideTimerRadioButton);
        hideTimerRadioButton.setSelected(true);
        JPanel buttons = new JPanel();
        buttons.setPreferredSize(new Dimension(200, frame.getHeight() - 40));
        stopButton.setEnabled(false);
        buttons.setLayout(new FlowLayout());
        buttons.add(startButton);
        buttons.add(stopButton);
        showInf.setEnabled(false);
        buttons.add(showInf);
        buttons.add(showTimerRadioButton);
        buttons.add(hideTimerRadioButton);
        buttons.add(currentButton);
        buttons.add(startMovingButton);
        stopMovingButton.setEnabled(false);
        buttons.add(stopMovingButton);
        buttons.add(labelForThreadPriority);
        buttons.add(threadPriority);
        buttons.add(againButton);

        applet.setPreferredSize(new Dimension(580, frame.getHeight() - 40));
        frame.add(applet, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.EAST);

        // menu
        JMenu startMenu = new JMenu("Simulation");
        JMenuItem start = new JMenuItem("Начать");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.doClick();
            }
        });
        startMenu.add(start);
        startMenu.addSeparator();
        JMenuItem stop = new JMenuItem("Остановить");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopButton.doClick();
            }
        });
        startMenu.add(stop);
        startMenu.addSeparator();
        
        JMenuItem console = new JMenuItem("Открыть консоль");
        console.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applet.startConsole();
            }
        });
        startMenu.add(console);
        startMenu.addSeparator();
        
        JMenuItem seri = new JMenuItem("Сохранить объекты");
        seri.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applet.Serialize();
            }
        });
        startMenu.add(seri);
        startMenu.addSeparator();
        
        JMenuItem deseri = new JMenuItem("Загрузить объекты");
        deseri.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applet.Deserialize();
            }
        });
        startMenu.add(deseri);
        startMenu.addSeparator();
        
        JMenuItem exit = new JMenuItem("Выход");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        startMenu.add(exit);

        JMenu timerMenu = new JMenu("Timer");
        JMenuItem show = new JMenuItem("Показать");
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTimerRadioButton.setSelected(true);
            }
        });
        timerMenu.add(show);
        timerMenu.addSeparator();
        JMenuItem hide = new JMenuItem("Скрыть");
        hide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideTimerRadioButton.setSelected(true);
            }
        });
        timerMenu.add(hide);

        mainMenu.add(startMenu);
        mainMenu.add(timerMenu);
        frame.setJMenuBar(mainMenu);
        frame.pack();
    }
}
