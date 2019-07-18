import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JPanel;

public class Board extends JPanel implements Runnable, Commons {

    private Dimension d;
    private Player player;
    private ArrayList<Shot> shots;
    private ArrayList<Minion> minions;
    private int minionAlive = 15;

    private int direction = -1;
    private boolean ingame = true;
    private Thread animator;
    private String message = "Game Over";

    public Board() {

        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);

        gameInit();
        setDoubleBuffered(true);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    public void gameInit() {
        player = new Player();
        shots = new ArrayList<>();
        minions = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            for(int j = 0; j <5; j++) {
                Minion minion = new Minion(MINION_INIT_X + 80 * j, MINION_INIT_Y + 40 * i);
                minions.add(minion);
            }
        }

        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawPlayer(Graphics g) {
        if(player.isVisible()) {
            g.drawRect(player.getX(), player.getY(), PLAYER_WIDTH, PLAYER_HEIGHT);
            g.fillRect(player.getX(), player.getY(), PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        if(player.isDying()) {
            ingame = false;
            player.die();
        }
    }

    public void drawHealthBar(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRect(50, 450, player.getHealth()*4, 10);
        g.setColor(Color.PINK);
        g.fillRect(50, 450, player.getHealth()*4, 10);
    }

    public void drawShot(Graphics g) {
        for(Shot s: shots) {
            if (s.isVisible()) {
                g.drawRect(s.getX(), s.getY(), 3, 5);
                g.fillRect(s.getX(), s.getY(), 3, 5);
            }
        }
    }

    public void drawMinion(Graphics g) {
        for(Minion m: minions) {
            if(m.isVisible()) {
                g.drawRect(m.getX(), m.getY(), MINION_WIDTH, MINION_HEIGHT);
                g.fillRect(m.getX(), m.getY(), MINION_WIDTH, MINION_HEIGHT);
            }
            if(m.isDying()) {
                m.die();
            }
        }
    }

    public void drawAttack(Graphics g) {
        for (Minion m: minions) {
            Minion.Attack a = m.getAttack();

            if(!a.isDestroyed()) {
                g.drawRect(a.getX(),a.getY(), 2, 2);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.pink);

        if (ingame) {
            drawPlayer(g);
            drawHealthBar(g);
            drawShot(g);
            g.setColor(Color.darkGray);
            drawMinion(g);
            drawAttack(g);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void animationCycle() {

        // player
        player.move();
        if(player.getHealth()<=0){
            player.setDying(true);
        }

        //Shot
        for(Iterator<Shot> it = shots.iterator(); it.hasNext(); ){
            Shot shot = it.next();
            if(shot.isVisible()) {
                int shotX = shot.getX();
                int shotY = shot.getY();

                for(Minion m: minions){
                    int mX = m.getX();
                    int mY = m.getY();

                    //Collision Detect
                    if(m.isVisible() && shot.isVisible()){
                        if(shotX >= mX && shotX <= mX+MINION_WIDTH) {
                            if(shotY >= mY && shotY <= mY+MINION_HEIGHT) {
                                m.setDying(true);
                                shot.die();
                                minionAlive--;
                                //check number of minion alive
                                if(minionAlive <= 0) {
                                    ingame = false;
                                    message = "You Won!";
                                }
                            }
                        }
                    }
                }

                //Shot velocity
                int y = shot.getY();
                y -= 5;

                if(y < 0) {
                    shot.die();
                } else {
                    shot.setY(y);
                }
            }
        }
        //Remove shot from shots
        shots.removeIf(shot -> !shot.isVisible());

        //Minions
        for(Minion m: minions) {
            int x = m.getX();

            //Change horizontal direction && go down
            if(x >= BOARD_WIDTH-MINION_WIDTH*2 && direction != -1){
                direction = -1;
                Iterator i1 = minions.iterator();

                while (i1.hasNext()) {
                    Minion m2 = (Minion) i1.next();
                    m2.setY(m2.getY() + GO_DOWN);
                }
            }

            if(x <= 5 && direction != 1) {
                direction = 1;
                Iterator i1 = minions.iterator();

                while (i1.hasNext()) {
                    Minion m2 = (Minion) i1.next();
                    m2.setY(m2.getY() + GO_DOWN);
                }
            }

            if (m.isVisible()) {
                m.move(direction);
                if(m.getY()+MINION_HEIGHT >= 410){
                    ingame = false;
                }
            }
        }

        //Minion Attack
        Random generator = new Random();

        for(Minion m: minions) {
            int a = generator.nextInt(100);
            Minion.Attack attack = m.getAttack();

            if(a == 1 && m.isVisible() && attack.isDestroyed()) {
                attack.setDestroyed(false);
                attack.setX(m.getX());
                attack.setY(m.getY());
            }

            int aX = attack.getX();
            int aY = attack.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if(player.isVisible() && !attack.isDestroyed()) {
                if(aX >= playerX && aX <= playerX + PLAYER_WIDTH){
                    if (aY >= playerY && aY <= playerY + PLAYER_HEIGHT) {
                        player.setHealth(-10);
                        System.out.println(player.getHealth());
                        attack.setDestroyed(true);
                    }
                }
            }

            if(!attack.isDestroyed()) {
                attack.setY(attack.getY() + 5);
                if(attack.getY() >= 435) {
                    attack.setDestroyed(true);
                }
            }
        }
    }

    public void gameOver() {
        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        Font small = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2, 100);
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (ingame) {

            repaint();
            animationCycle();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = 17 - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }

            beforeTime = System.currentTimeMillis();
        }
        gameOver();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                if (ingame) {
                    if (shots.size() < 3) {
                        Shot shot = new Shot(x, y);
                        shots.add(shot);
                    }
                }
            }
        }
    }
}
