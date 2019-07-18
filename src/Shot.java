public class Shot extends Sprite {

    private final int H_SPACE = 6;
    private final int V_SPACE = 1;

    public Shot(int x, int y) {
      initShot(x, y);
    }

    public Shot(){}

    private void initShot(int x, int y) {
      setX(x + H_SPACE);
      setY(y - V_SPACE);
    }
}
