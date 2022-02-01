import java.awt.Font;

import javax.swing.*;
public class Menu extends JFrame{
    
    public Menu(){
        setTitle("GameLauncher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(1920,1080); //Kan komma att ändras
        JPanel p = new JPanel();
        JTextField textField = new JTextField();
        textField.setText("UNO");
        textField.setFont(new Font("Serif", Font.PLAIN, 36));
        p.add(textField);
        add(p);
    }
    public static void main(String[] args){
        Menu m = new Menu();
    }
}
