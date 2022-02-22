import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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

        // "PLAY" - Knappen
        JButton play = new JButton("PLAY");
        play.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
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
        //Properties configProperties;
        JButton create = new JButton("CREATE SERVER");
        create.setFont(new Font("Yu Gothic UI Semibold",Font.PLAIN,36));
        create.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            	Properties configProperties = createProperty("inst.properties");
                try {
                    // vi vill inte skapa två servrar på samma klient
                    
                    // IP-adress, port, antal spelare
                    String port = "8989";    //Denna behöver för tillfället vara 8989
                    String nbrOfPlayers = "";
                    int a;
                    a = 0;
                    while(a > 10 || a < 2){
                        nbrOfPlayers = JOptionPane.showInputDialog(null, "Please enter the amount of players in the game (2-10):", configProperties.getProperty("nrPlayers"));
                        if(nbrOfPlayers == null){
                            break;
                        }
                        try {
                            a = Integer.parseInt(nbrOfPlayers);
                            if(a >= 2 && a <=10){
                                break;
                            }
                        } catch (NumberFormatException z) {
                            Toolkit.getDefaultToolkit().beep();
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
        //Properties configProperties = createProperty("inst.properties");
        JButton join = new JButton("JOIN SERVER");
        join.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
        join.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            	Properties configProperties = createProperty("inst.properties");
                try {
                    String port = "8989";
                    String nickname = JOptionPane.showInputDialog(null, "Please Enter your nickname:", configProperties.getProperty("nickname"));
                    if(nickname == null || nickname.isEmpty()){
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "You need to enter a nickname to be able to play!");
                    } else {
                        clientProcess.add(Runtime.getRuntime().exec("java ChatClient localhost " + port + " " + nickname));
                    }                 
                } catch (Exception y) {
                    JOptionPane.showInputDialog(null, y + "");
                }
            }
        });

        
      //Change settings
        //File configFile = new File("config.properties"); //Open config file
        JButton config = new JButton("Change settings");
        config.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
        config.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            	configWindow();
            }
        });
        
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        // "UNO" - texten
        JLabel text = new JLabel("UNO");
        text.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
        p.add(text, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.add(Box.createRigidArea(new Dimension(0,window_height / 10)));
        buttons.add(play,gbc);
        buttons.add(Box.createRigidArea(new Dimension(0,window_height / 10)));
        buttons.add(exit,gbc);
        buttons.add(Box.createRigidArea(new Dimension(0,window_height / 10)));
        buttons.add(create,gbc);
        buttons.add(Box.createRigidArea(new Dimension(0,window_height / 10)));
        buttons.add(join,gbc);
        buttons.add(Box.createRigidArea(new Dimension(0,window_height / 10)));
        buttons.add(config,gbc);
        gbc.weighty = 1;
        p.add(buttons,gbc);
        add(p);
        WindowListener exitListener = new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                int confirm = JOptionPane.showOptionDialog(
                    null, "Are You Sure to Close Application?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if(confirm == 0){
                    System.exit(0);
                }
            }
        };
        this.addWindowListener(exitListener);
        setVisible(true);
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run() {
                // stäng ner alla processer
                for (Process p : clientProcess) {
                    p.destroy();
                }
                if(serverProcess != null)
                    serverProcess.destroy();
            }
        });
    }
    
    public static void configWindow() {
		/*Properties properties = new Properties();
	    try (FileInputStream fileInputStream = new FileInputStream("inst.properties")) {
	    	properties.load(fileInputStream);
	    } catch (Exception ex) {
	        System.out.println("exeption " + ex.getMessage());
	    }*/
    	Properties properties = createProperty("inst.properties");
	    JTextField nicknameField = new JTextField(properties.getProperty("nickname"));
	    JTextField nrPlayersField = new JTextField(properties.getProperty("nrPlayers"));

	    JPanel configPanel = new JPanel();
	    configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
	    configPanel.add(new JLabel("Nickname:"));
	    configPanel.add(nicknameField);
	    configPanel.add(Box.createVerticalStrut(15));
	    configPanel.add(new JLabel("Nr of players:"));
	    configPanel.add(nrPlayersField);

	    int result = JOptionPane.showConfirmDialog(null, configPanel, "Configuration", JOptionPane.OK_CANCEL_OPTION);
	    
	    if(nicknameField.getText() == null || nicknameField.getText().isEmpty() || nrPlayersField.getText() == null || nrPlayersField.getText().isEmpty()){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "You need to enter a nickname and number of players!");
            return;
        }
	    if(Integer.parseInt(nrPlayersField.getText()) > 10 || Integer.parseInt(nrPlayersField.getText()) < 2) {
	    	Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "The amount of players needs to be between 2 and 10!");
            return;
	    }
	    
	    if (result == JOptionPane.OK_OPTION) {
	      try {
	    	  File configFile = new File("inst.properties"); //Open configuration file
		      Properties props = new Properties();
		      props.setProperty("nickname", nicknameField.getText());
		      props.setProperty("nrPlayers", nrPlayersField.getText());
		      FileWriter fileWriter = new FileWriter(configFile); //io exception
		      props.store(fileWriter, "Configuration file"); //har exceptions
		      fileWriter.close(); //io exception
	      }catch(ClassCastException e) {
	    	  System.out.println(e.getMessage() + "contains keys or values that are not Strings");
	      }catch(Exception y) {
	    	  System.out.println(y.getMessage());
	      }
	    }
  }
    
    public static Properties createProperty(String filename) {
    	Properties properties = new Properties();
	    try (FileInputStream fileInputStream = new FileInputStream(filename)) {
	    	properties.load(fileInputStream);
	    } catch (Exception ex) {
	        System.out.println("exeption " + ex.getMessage());
	    }
	    return properties;
    }

    public static void main(String[] args){
        Menu m = new Menu();
    }
}
