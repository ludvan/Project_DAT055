import java.awt.Font;

import javax.swing.*;
public class Menu extends JFrame{
    
    public Menu(){
        setTitle("GameLauncher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920,1080); //Kan komma att ändras
        JPanel p = new JPanel();
        JLabel label = new JLabel();
        label.setText("UNO");
        label.setFont(new Font("Serif", Font.PLAIN, 36));
        p.add(label);
        add(p);
        setVisible(true);
    }
    public static void main(String[] args){
        Menu m = new Menu();
    }
}
