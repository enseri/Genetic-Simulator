import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class All extends JPanel {
    private final Scanner s = new Scanner(System.in);
    private int dotCount = 1000, frames, updates, mostSurvived = 0;
    public static int numMovements = 250, safeZoneX = 475, safeZoneY = 475, safeZoneWidth = 50, safeZoneHeight = 50;
    private long fps = 1000 / 60, ups = 1000 / 30, lastFrame, lastUpdate, lastCall;

    public static ArrayList<Dot> dots = new ArrayList<>();
    public static void main(String[] args) {
        new All();
    }
    private All() {
        init();
        this.setPreferredSize(new Dimension(1000, 1000));
        JFrame frame = new JFrame();
        frame.setTitle("Genetic-Simulator");
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void init() {
        System.out.println("> Load Default (0) Custom (1)");
        System.out.print("< ");
        if(s.nextInt() == 1) {
            System.out.println("> Enter Num Dots");
            System.out.print("< ");
            dotCount = s.nextInt();
            System.out.println("> Enter FPS");
            System.out.print("< ");
            fps = 1000 / s.nextInt();
            System.out.println("> Enter UPS");
            System.out.print("< ");
            ups = 1000 / s.nextInt();
            System.out.println("> Enter Num Movements");
            System.out.print("< ");
            numMovements = s.nextInt();
            System.out.println("> Enter Safe Zone X");
            System.out.print("< ");
            safeZoneX = s.nextInt();
            System.out.println("> Enter Safe Zone Y");
            System.out.print("< ");
            safeZoneY = s.nextInt();
            System.out.println("> Enter Safe Zone Width");
            System.out.print("< ");
            safeZoneWidth = s.nextInt();
            System.out.println("> Enter Safe Zone Height");
            System.out.print("< ");
            safeZoneHeight = s.nextInt();
            if(ups > 0)
                System.out.println("*NOTE* They Will Live For " + (numMovements / ups) + " Seconds *NOTE*");
        }
        for(int i = 0; i < dotCount; i++) {
            int x = (int) (Math.random() * 990), y = (int) (Math.random() * 990);
            while(x + 10 >= safeZoneX && x <= safeZoneX + safeZoneWidth && y + 10 >= safeZoneY && y <= safeZoneY + safeZoneHeight) {
                x = (int) (Math.random() * 990);
                y = (int) (Math.random() * 990);
            }
            dots.add(new Dot(x,y));
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        boolean updating = true;
        while(updating) {
            if (System.currentTimeMillis() - lastFrame > fps) {
                lastFrame = System.currentTimeMillis();
                tick(g);
                updating = false;
                repaint();
                frames++;
            }
            if (System.currentTimeMillis() - lastUpdate > ups) {
                lastUpdate = System.currentTimeMillis();
                update();
                updates++;
            }
            if(System.currentTimeMillis() - lastCall > 1000) {
                System.out.println("> FPS: " + frames);
                System.out.println("> UPS: " + updates);
                lastCall = System.currentTimeMillis();
                frames = 0;
                updates = 0;
            }
        }
    }

    private void tick(Graphics g) {
        g.setColor(new Color(75, 75, 20, 222));
        g.fillRect(safeZoneX, safeZoneY, safeZoneWidth, safeZoneHeight);
        boolean allDead = true;
        for(Dot temp : dots) {
            if(temp.isAlive) {
                if(temp.movements < numMovements) {
                    allDead = false;
                }
                g.setColor(Color.green);
            } else {
                g.setColor(Color.red);
            }
            g.fillOval(temp.x, temp.y, 10, 10);
            g.setColor(Color.black);
            g.drawOval(temp.x, temp.y, 10, 10);
            if(temp.x + 10 >= safeZoneX && temp.x <= safeZoneX + safeZoneWidth && temp.y + 10 >= safeZoneY && temp.y <= safeZoneY + safeZoneHeight) {
                g.setColor(Color.MAGENTA);
                g.drawOval(temp.x, temp.y, 10, 10);
                g.setColor(Color.green);
            }
            int stackCount = 0;
            for(Dot temp2 : dots) {
                if(temp.x == temp2.x && temp.y == temp2.y && temp != temp2) {
                    stackCount++;
                }
            }
            if(stackCount > 0) {
                g.setColor(Color.black);
                g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
                g.drawString((stackCount + 1) + "", temp.x + 2, temp.y - 5);
            }
        }
        if(allDead)
            nextGeneration();
    }

    private void nextGeneration() {
        int survived = 0;
        ArrayList<Dot> tempDots = (ArrayList<Dot>) dots.clone();
        dots.clear();
        for(int i = 0; i < tempDots.size(); i++) {
            Dot temp = tempDots.get(i);
            if(temp.x + 10 >= safeZoneX && temp.x <= safeZoneX + safeZoneWidth && temp.y + 10 >= safeZoneY && temp.y <= safeZoneY + safeZoneHeight) {
                survived++;
                int x = (int) (Math.random() * 990), y = (int) (Math.random() * 990);
                while(x + 10 >= safeZoneX && x <= safeZoneX + safeZoneWidth && y + 10 >= safeZoneY && y <= safeZoneY + safeZoneHeight) {
                    x = (int) (Math.random() * 990);
                    y = (int) (Math.random() * 990);
                }
                dots.add(new Dot(x, y, temp));
            }
        }
        while(dots.size() > dotCount)
            dots.remove(0);
        if(dots.size() == 0)
            while(dots.size() < dotCount) {
                int x = (int) (Math.random() * 990), y = (int) (Math.random() * 990);
                while(x + 10 >= safeZoneX && x <= safeZoneX + safeZoneWidth && y + 10 >= safeZoneY && y <= safeZoneY + safeZoneHeight) {
                    x = (int) (Math.random() * 990);
                    y = (int) (Math.random() * 990);
                }
                dots.add(new Dot(x, y));
            }
        else {
            int movingIndex = 0;
            while (dots.size() < dotCount) {
                int x = (int) (Math.random() * 990), y = (int) (Math.random() * 990);
                while(x + 10 >= safeZoneX && x <= safeZoneX + safeZoneWidth && y + 10 >= safeZoneY && y <= safeZoneY + safeZoneHeight) {
                    x = (int) (Math.random() * 990);
                    y = (int) (Math.random() * 990);
                }
                dots.add(0, new Dot(x, y, dots.get((int) ((Math.random() * (dots.size() - movingIndex)) + movingIndex))));
                movingIndex++;
            }
        }
        if(survived > mostSurvived) {
            System.out.println("> Better Generation");
            mostSurvived = survived;
        }
    }

    private void update() {
        for (Dot temp : dots) {
            if (temp.isAlive)
                temp.update();
        }
    }
}

class Dot {
    public int x, y, movements;
    // Info
    // 0 data | 1 Impulse Needed | 2 Impulse Per Data | 3 Move
    int[] distanceFromNearestObject = new int[4], distanceFromNWall = new int[4], distanceFromEWall = new int[4], distanceFromSWall = new int[4], distanceFromWWall = new int[4];
    public boolean isAlive = true;
    public Dot(int x, int y) {
        init();
        this.x = x;
        this.y = y;
    }
    public Dot(int x, int y, Dot parent) {
        adapt(parent);
        this.x = x;
        this.y = y;
    }

    public void init() {
        distanceFromNearestObject[1] = (int) (Math.random() * 4) + 1;
        distanceFromWWall[1] = (int) (Math.random() * 4) + 1;
        distanceFromSWall[1] = (int) (Math.random() * 4) + 1;
        distanceFromEWall[1] = (int) (Math.random() * 4) + 1;
        distanceFromNWall[1] = (int) (Math.random() * 4) + 1;
        if((int) (Math.random() * 4) + 1 <= 2)
            distanceFromNearestObject[2] = (int) (Math.random() * .01) * 10000 ;
        if((int) (Math.random() * 4) + 1 <= 2)
            distanceFromWWall[2] = (int) ((Math.random() * .01) * 10000);
        if((int) (Math.random() * 4) + 1 <= 2)
            distanceFromSWall[2] = (int) ((Math.random() * .01) * 10000);
        if((int) (Math.random() * 4) + 1 <= 2)
            distanceFromEWall[2] = (int) ((Math.random() * .01) * 10000);
        if((int) (Math.random() * 4) + 1 <= 2)
            distanceFromNWall[2] = (int) ((Math.random() * .01) * 10000);
        distanceFromNearestObject[3] = (int) (Math.random() * 4) + 1;
        distanceFromWWall[3] = (int) (Math.random() * 4) + 1;
        distanceFromSWall[3] = (int) (Math.random() * 4) + 1;
        distanceFromEWall[3] = (int) (Math.random() * 4) + 1;
        distanceFromNWall[3] = (int) (Math.random() * 4) + 1;
    }
    public void update() {
        distanceFromNearestObject[0] = 1000;
        for(Dot temp : All.dots) {
            if((int) Math.sqrt(((temp.x - x) * (temp.x - x)) + ((temp.y - y)  * (temp.y - y))) < distanceFromNearestObject[0] && temp != this)
                distanceFromNearestObject[0] = (int) Math.sqrt(((temp.x - x) * (temp.x - x)) + ((temp.y - y)  * (temp.y - y)));
        }
        distanceFromNWall[0] = y;
        distanceFromEWall[0] = 990 - x;
        distanceFromSWall[0] = 990 - y;
        distanceFromWWall[0] = x;
        if(x + 10 >= All.safeZoneX && x <= All.safeZoneX + All.safeZoneWidth && y + 10 >= All.safeZoneY && y <= All.safeZoneY + All.safeZoneHeight)
                movements = All.numMovements;
        if (movements < All.numMovements){
            if((double) distanceFromNearestObject[0] * ((double) distanceFromNearestObject[2] / 10000) > distanceFromNearestObject[1]) {
                move(distanceFromNearestObject[3]);
            }
            else if((double) distanceFromNWall[0] * ((double) distanceFromNWall[2] / 10000) > distanceFromNWall[1]) {
                move(distanceFromNWall[3]);
            }
            else if((double) distanceFromEWall[0] * ((double) distanceFromEWall[2] / 10000) > distanceFromEWall[1]) {
                move(distanceFromEWall[3]);
            }
            else if((double) distanceFromSWall[0] * ((double) distanceFromSWall[2] / 10000) > distanceFromSWall[1]) {
                move(distanceFromSWall[3]);
            }
            else if((double) distanceFromWWall[0] * ((double) distanceFromWWall[2] / 10000) > distanceFromWWall[1]) {
                move(distanceFromWWall[3]);
            }
            movements++;
        }
    }
    public void adapt(Dot parent) {
        distanceFromNearestObject = parent.distanceFromNearestObject.clone();
        distanceFromWWall = parent.distanceFromNWall.clone();
        distanceFromSWall = parent.distanceFromNWall.clone();
        distanceFromEWall = parent.distanceFromNWall.clone();
        distanceFromNWall = parent.distanceFromNWall.clone();
        if((int) (Math.random() * 10) == 0)
            distanceFromNearestObject[1] = (int) (Math.random() * 4) + 1;
        if((int) (Math.random() * 10) == 0)
            distanceFromWWall[1] = (int) (Math.random() * 4) + 1;
        if((int) (Math.random() * 10) == 0)
            distanceFromSWall[1] = (int) (Math.random() * 4) + 1;
        if((int) (Math.random() * 10) == 0)
            distanceFromEWall[1] = (int) (Math.random() * 4) + 1;
        if((int) (Math.random() * 10) == 0)
            distanceFromNWall[1] = (int) (Math.random() * 4) + 1;
        if((int) (Math.random() * 10) == 0)
            if((int) (Math.random() * 4) + 1 <= 2)
                distanceFromNearestObject[2] = (int) (Math.random() * .01) * 10000 ;
        if((int) (Math.random() * 10) == 0)
            if((int) (Math.random() * 4) + 1 <= 2)
                distanceFromWWall[2] = (int) ((Math.random() * .01) * 10000);
        if((int) (Math.random() * 10) == 0)
            if((int) (Math.random() * 4) + 1 <= 2)
                distanceFromSWall[2] = (int) ((Math.random() * .01) * 10000);
        if((int) (Math.random() * 10) == 0)
            if((int) (Math.random() * 4) + 1 <= 2)
                distanceFromEWall[2] = (int) ((Math.random() * .01) * 10000);
        if((int) (Math.random() * 10) == 0)
            if((int) (Math.random() * 4) + 1 <= 2)
                distanceFromNWall[2] = (int) ((Math.random() * .01) * 10000);
        if((int) (Math.random() * 10) == 0)
            distanceFromNearestObject[3] = (int) (Math.random() * 4) + 1;
        if((int) (Math.random() * 10) == 0)
            distanceFromWWall[3] = (int) (Math.random() * 4) + 1;
        if((int) (Math.random() * 10) == 0)
            distanceFromSWall[3] = (int) (Math.random() * 4) + 1;
        if((int) (Math.random() * 10) == 0)
            distanceFromEWall[3] = (int) (Math.random() * 4) + 1;
        if((int) (Math.random() * 10) == 0)
            distanceFromNWall[3] = (int) (Math.random() * 4) + 1;
    }
    
    public void move(int move) {
        switch(move) {
            case 1:
                if(x - 10 >= 0)
                    x -= 10;
                break;
            case 2:
                if(y + 10 <= 990)
                    y += 10;
                break;
            case 3:
                if(x + 10 <= 990)
                    x += 10;
                break;
            case 4:
                if(y - 10 >= 0)
                    y -= 10;
                break;
            default:
                System.out.println("Invalid Move");
        }
    }
}
