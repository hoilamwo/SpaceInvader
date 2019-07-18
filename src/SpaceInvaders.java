import java.awt.EventQueue;
import javax.swing.JFrame;

public class SpaceInvaders extends JFrame implements Commons{

    public SpaceInvaders() {
      initUI();
    }

    private void initUI() {
       add(new Board());
       setSize(BOARD_WIDTH, BOARD_HEIGHT);
       setTitle("Space Invaders");
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setLocationRelativeTo(null);
       this.setResizable(false);
   }

   public static void main(String[] args) {
     EventQueue.invokeLater(() -> {
            SpaceInvaders ex = new SpaceInvaders();
            ex.setVisible(true);
        });
   }
}
