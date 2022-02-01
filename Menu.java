import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
public class Menu extends JFrame{
    
    private final int window_width = 1920;
    private final int window_height = 1080;

    public Menu(){
        setTitle("GameLauncher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(window_width,window_height); //Kan komma att ändras
        JPanel p = new JPanel();
        p.setLayout(null);
        // "Uno" - texten
        JLabel label = new JLabel();
        label.setText("UNO");
        label.setFont(new Font("Serif", Font.PLAIN, 36));
        label.setHorizontalAlignment(JLabel.CENTER);
        Dimension size = label.getPreferredSize();
        label.setBounds(window_width/2, 20, size.width, size.height);
        // "PLAY" - Knappen
        JButton play = new JButton("PLAY");
        play.setFont(new Font("Serif", Font.PLAIN, 17));
        play.setBounds(window_width/2, window_height/2 - 100, size.width, size.height);
        //play.setSize(87, 60);
        // "EXIT" - Knappen
        JButton exit = new JButton("EXIT");
        exit.setFont(new Font("Serif", Font.PLAIN, 17));
        exit.setBounds(window_width/2, window_height/2 + 50, size.width, size.height);
        exit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        p.add(label);
        p.add(play);
        p.add(exit);
        add(p);
        setVisible(true);
    }

    public static void main(String[] args){
        Menu m = new Menu();
    }
}
