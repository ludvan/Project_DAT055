import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
public class Menu extends JFrame{
    
    private final int window_width = 1920;
    private final int window_height = 1080;
    private Server server; // lagra server

    public Menu(){
        setTitle("GameLauncher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(window_width,window_height); //Kan komma att ändras
        JPanel p = new JPanel();
        p.setLayout(null);
        // "Uno" - texten
        JLabel label = new JLabel();
        label.setText("UNO");
        label.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
        label.setHorizontalAlignment(JLabel.CENTER);
        Dimension size = label.getPreferredSize();
        label.setBounds(window_width/2, 20, size.width, size.height);
        // "PLAY" - Knappen
        JButton play = new JButton("PLAY");
        play.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
        Dimension play_size = play.getPreferredSize();
        play.setBounds(window_width/2 - 20, window_height/5, play_size.width, play_size.height);
        //play.setSize(87, 60);
        play.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //TODO: När man stänger ner spelet så stängs även launchern ner
                try {
                    UnoWindow w = new UnoWindow();
                    w.setVisible(true);
                } catch (Exception y) {
                    JOptionPane.showMessageDialog(null, y + "");
                }
            }
        });
        // "EXIT" - Knappen
        JButton exit = new JButton("EXIT");
        exit.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36)); //17
        exit.setBounds(window_width/2 - 10, 7*window_height/15, play_size.width, play_size.height);
        exit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        // Create Server button
        JButton create = new JButton("CREATE SERVER");
        create.setFont(new Font("Yu Gothic UI Semibold",Font.PLAIN,36));
        Dimension server_size = create.getPreferredSize();
        create.setBounds(window_width/2 - 100, 2*window_height/7, server_size.width, server_size.height);
        create.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    server = new Server(8989);
                    server.start();
                } catch (Exception y) {
                    JOptionPane.showInputDialog(null, y + "");
                }
            }
        });
        // Join Server button
        JButton join = new JButton("JOIN SERVER");
        join.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
        join.setBounds(window_width/2 - 100, 3*window_height/8, server_size.width, server_size.height);
        join.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    ChatClient c = new ChatClient("hostname",8989,"liam");
                } catch (Exception y) {
                    JOptionPane.showInputDialog(null, y + "");
                }
            }
        });
        p.add(label);
        p.add(play);
        p.add(exit);
        p.add(create);
        p.add(join);
        add(p);
        setVisible(true);
    }

    public static void main(String[] args){
        Menu m = new Menu();
    }
}
