package Controller;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import View.*;
import javax.swing.*;

public class Menu extends JFrame {

    private int window_width = 1920;
    private int window_height = 1080;
    private Process serverProcess;
    private ArrayList<Process> clientProcess;

    public Menu() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        clientProcess = new ArrayList<Process>();

        setTitle("GameLauncher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(window_width, window_height);

        //Create buttons and add to list
        ArrayList<JButton> buttonList = new ArrayList<JButton>();
        buttonList.add(createServerButton());
        buttonList.add(createJoinButton());
        buttonList.add(createConfigButton());
        buttonList.add(createExitButton());
        createMenuPanel(buttonList); //create panel
        

        // Shutdownhook to ensure that no process created in runtime are left running
        // once the application is closed
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Shutdown all processes
                if (clientProcess.size() != 0) {
                    for (Process p : clientProcess) {
                        p.destroy();
                    }
                }
                if (serverProcess != null)
                    serverProcess.destroy();
            }
        });
    }
    
    private JButton createServerButton() {
    	JButton create = new JButton("CREATE SERVER");
        create.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Properties configProperties = createProperty("inst.properties");
                try {
                    String port = "8989"; // All communication is run on port 8989
                    String nbrOfPlayers = "";
                    int a = 0;
                    while (a > 10 || a < 2) {
                        nbrOfPlayers = JOptionPane.showInputDialog(null,
                                "Please enter the amount of players in the game (2-10):",
                                configProperties.getProperty("nrPlayers"));
                        if (nbrOfPlayers == null)
                            break;

                        try {
                            a = Integer.parseInt(nbrOfPlayers);
                            if (a >= 2 && a <= 10) {
                                break;
                            }
                        } catch (NumberFormatException z) {
                            Toolkit.getDefaultToolkit().beep();
                            JOptionPane.showMessageDialog(null, "The amount of players needs to be between 2 and 10!");
                        }
                    }
                    String open_server = "java Model/Server " + port + " " + nbrOfPlayers;
                    if (serverProcess != null)
                        serverProcess.destroy();
                    serverProcess = Runtime.getRuntime().exec(open_server);
                    ServerOutputView sov = new ServerOutputView();
                    sov.setProcess(serverProcess);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
        return create;
    }
    
    private JButton createJoinButton() {
    	JButton join = new JButton("JOIN SERVER");
        join.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
        join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Properties configProperties = createProperty("inst.properties");
                try {
                    String port = "8989";
                    String nickname = JOptionPane.showInputDialog(null, "Please Enter your nickname:",
                            configProperties.getProperty("nickname"));
                    String ip = JOptionPane.showInputDialog(null, "Please Enter the host's ip-adress:",
                            "localhost");
                    if (nickname == null || nickname.isEmpty()) {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Invalid nickname");
                    } else if (ip == null || ip.isEmpty()) {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Invalid host ip-adress");
                    } else {
                        clientProcess.add(
                                Runtime.getRuntime()
                                        .exec("java Model/ChatClient" + " " + ip + " " + port + " " + nickname));
                    }
                } catch (Exception y) {
                    JOptionPane.showInputDialog(null, y + "");
                }
            }
        });
        return join;
    }
    
    private JButton createConfigButton() {
        JButton config = new JButton("SETTINGS");
        config.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
        config.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configWindow();
            }
        });
        return config;
    }

    private JButton createExitButton() {
    	JButton exit = new JButton("EXIT");
        exit.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close all processes
                if (clientProcess.size() != 0) {
                    for (Process p : clientProcess) {
                        p.destroy();
                    }
                }
                if (serverProcess != null)
                    serverProcess.destroy();
                System.exit(0);
            }
        });
        return exit;
    }
    
    private JPanel createMenuPanel(ArrayList<JButton> buttonList) {
    	JPanel p = new JPanel();
    	p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        // Title
        JLabel text = new JLabel("UNO");
        text.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 36));
        p.add(text, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttons = new JPanel(new GridBagLayout());
        
        for (JButton button : buttonList) {
        	buttons.add(Box.createRigidArea(new Dimension(0, window_height / 10)));
            buttons.add(button, gbc);
        }
        
        gbc.weighty = 1;
        p.add(buttons, gbc);
        add(p);
        setVisible(true);
        return p;
    }
    
    private void configWindow() {
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

        if (nicknameField.getText() == null || nicknameField.getText().isEmpty() || nrPlayersField.getText() == null
                || nrPlayersField.getText().isEmpty()) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "You need to enter a nickname and number of players!");
            return;
        }
        
        try {
        if (Integer.parseInt(nrPlayersField.getText()) > 10 || Integer.parseInt(nrPlayersField.getText()) < 2) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "The amount of players needs to be between 2 and 10!");
            return;
        }} catch(NumberFormatException e) {
        	Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "The amount of players needs to be a number");
            return;
        }

        if (result == JOptionPane.OK_OPTION) {
            try {
                File configFile = new File("inst.properties"); // Open configuration file
                Properties props = new Properties();
                props.setProperty("nickname", nicknameField.getText());
                props.setProperty("nrPlayers", nrPlayersField.getText());
                FileWriter fileWriter = new FileWriter(configFile); // io exception
                props.store(fileWriter, "Configuration file"); // har exceptions
                fileWriter.close(); // io exception
            } catch (ClassCastException e) {
                System.out.println(e.getMessage() + "contains keys or values that are not Strings");
            } catch (Exception y) {
                System.out.println(y.getMessage());
            }
        }
    }

    /* Returns property for filename*/
    static Properties createProperty(String filename) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            properties.load(fileInputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return properties;
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
    }
}
