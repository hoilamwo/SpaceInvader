public class Minion extends Sprite {

  private Attack attack;

  public Minion(int x, int y) {
    initMinion(x, y);
  }

  private void initMinion(int x, int y) {
    this.x = x;
    this.y = y;

    attack = new Attack(x, y);
  }

  //Position Minion in horizontal direction
  public void move(int direction) {
    this.x += direction;
  }

  //called when Minion is about to attack
  public Attack getAttack() {
    return attack;
  }

  //Inner Attack class
  public class Attack extends Sprite {
    private boolean destroyed;

    public Attack(int x, int y) {
      initAttack(x, y);
    }

    public void initAttack(int x, int y) {
      setDestroyed(true);
      this.x = x;
      this.y = y;
    }

    public void setDestroyed(boolean destroyed) {
      this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
      return destroyed;
    }
  }

}
