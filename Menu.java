import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import javax.swing.*;
public class Menu extends JFrame{
    
    private final int window_width = 1920;
    private final int window_height = 1080;
    private Process serverProcess;
    private ArrayList<Process> clientProcess;

    public Menu(){
        clientProcess = new ArrayList<Process>();

        setTitle("GameLauncher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(window_width,window_height); //Kan komma att ändras
        JPanel p = new JPanel();
        p.setBorder(new EmptyBorder(10,10,10,10));
        p.setLayout(new GridBagLayout());
        
        // "Uno" - texten
        JLabel label = new JLabel();
        label.setText("UNO");
        label.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
        
        //Dimension size = label.getPreferredSize();
        //label.setBounds(window_width/2, 20, size.width, size.height);

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
                // stäng ner alla processer
                for (Process p : clientProcess) {
                    p.destroy();
                }
                serverProcess.destroy();
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
                    // vi vill inte skapa två servrar på samma klient
                    
                    // IP-adress, port, antal spelare
                    String port = "8989";    //Denna behöver för tillfället vara 8989
                    String nbrOfPlayers = "";
                    int a;
                    a = 0;
                    while(a > 10 || a < 2){
                        nbrOfPlayers = JOptionPane.showInputDialog("Please enter the amount of players in the game (2-10):");
                        if(nbrOfPlayers == null){
                            break;
                        }
                        try {
                            a = Integer.parseInt(nbrOfPlayers);
                            if(a >= 2 && a <=10){
                                break;
                            }
                        } catch (NumberFormatException z) {
                            JOptionPane.showMessageDialog(null, "The amount of players needs to be between 2 and 10!");
                        }
                    }
                    String open_server = "java Server " + port + " " + nbrOfPlayers;
                    if(serverProcess != null)
                        serverProcess.destroy();
                    serverProcess = Runtime.getRuntime().exec(open_server); 
                } catch (IOException y) {
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
                    String port = "8989";
                    String nickname = JOptionPane.showInputDialog("Please Enter your nickname:");
                    while(nickname == null || nickname.isEmpty()){
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "You need to enter a nickname to be able to play!");
                        break;
                    }
                    clientProcess.add(Runtime.getRuntime().exec("java ChatClient localhost " + port + " " + nickname));                 
                } catch (Exception y) {
                    JOptionPane.showInputDialog(null, y + "");
                }
            }
        });

        
      //Change settings
        File configFile = new File("config.properties"); //Open config file
        JButton config = new JButton("Change settings");
        config.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36)); //17
        config.setBounds(window_width/2 - 100, 3*window_height/10, play_size.width, play_size.height);
        config.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            	String port = JOptionPane.showInputDialog("Please Enter port:");;
                String nickname = JOptionPane.showInputDialog("Please Enter your nickname:");
                System.out.println("port " + port + " :: name " + nickname);
                
                try {
                    Properties props = new Properties();
                    props.setProperty("port", port);
                    props.setProperty("nickname", nickname);
                    FileWriter writer = new FileWriter(configFile);
                    props.store(writer, "host settings");
                    writer.close();
                } catch (FileNotFoundException ex) {
                    // file does not exist
                } catch (IOException ex) {
                    // I/O error
                }
            }
        });
        
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        p.add(label);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttons = new JPanel(new GridBagLayout());
        //p.add(Box.createRigidArea(new Dimension(0,window_height / 10)));
        buttons.add(play,gbc);
        //p.add(Box.createRigidArea(new Dimension(0,window_height / 50)));
        buttons.add(exit,gbc);
        //p.add(Box.createRigidArea(new Dimension(0,window_height / 50)));
        buttons.add(create,gbc);
        //p.add(Box.createRigidArea(new Dimension(0,window_height / 50)));
        buttons.add(join,gbc);
        //p.add(Box.createRigidArea(new Dimension(0,window_height / 50)));
        buttons.add(config,gbc);
        gbc.weighty = 1;
        p.add(buttons,gbc);
        add(p);
        setVisible(true);
    }

    public static void main(String[] args){
        Menu m = new Menu();
    }
}
