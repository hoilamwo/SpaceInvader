import java.awt.event.KeyEvent;

public class Player extends Sprite implements Commons{

  private final int START_X = (BOARD_WIDTH-PLAYER_WIDTH)/2;
  private final int START_Y = 410;
  private int health = 100;

  public Player() {
    initPlayer();
  }

  private void initPlayer() {
    setX(START_X);
    setY(START_Y);
  }

  public void move() {
    x += dx;

    if (x <= 5) {
      x = 5;
    }
    if (x >= BOARD_WIDTH-PLAYER_WIDTH*2-5) {
      x = BOARD_WIDTH-PLAYER_WIDTH*2-5;
    }
  }

  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();

    if (key == KeyEvent.VK_LEFT) {
        dx = -3;
    }
    if (key == KeyEvent.VK_RIGHT) {
        dx = 3;
    }
  }

  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();

    if (key == KeyEvent.VK_LEFT) {
        dx = 0;
    }
    if (key == KeyEvent.VK_RIGHT) {
        dx = 0;
    }
  }

  public int getHealth() {
    return this.health;
  }

  public void setHealth(int y) {
    this.health += y;
  }
}
