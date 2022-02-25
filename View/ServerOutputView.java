package View;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.*;
public class ServerOutputView extends Thread {
    private Process serverProcess;
    private BufferedReader reader;
    private JTextArea outputText;
    private JFrame frame;

    public ServerOutputView(){
        outputText = new JTextArea();
        outputText.setEditable(false);
        outputText.setText("reading server...");
        JScrollPane scrollArea = new JScrollPane(outputText);
        frame = new JFrame("Server log");
        frame.setSize(200, 300);
        frame.add(scrollArea);
        frame.setVisible(true);
    }

    public void setProcess(Process serverProcess)
    {
        this.serverProcess = serverProcess;
        this.reader = new BufferedReader(new InputStreamReader(serverProcess.getInputStream()));
        start();
    }

    public void run()
    {
        String text = "";
        while(true)
        {
            String line;
            if(reader != null)
            {
                try {
                    while((line = reader.readLine()) != null)
                    {
                        text += line + "\n";
                        outputText.setText(text);
                        frame.repaint();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){

    }
}
