import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatSystem extends JPanel
{
    private ChatClient client;
    private ArrayList<String> messages;
    private JTextArea chatArea;
    private JTextField messageArea;

    public ChatSystem(ChatClient client)
    {
        this.client = client;
        messages = new ArrayList<String>();
        new JPanel();
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        messageArea = new JTextField(30);
        add(chatArea, BorderLayout.CENTER);
        add(messageArea, BorderLayout.SOUTH);
        JButton send = new JButton("Send");
        send.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(messageArea.getText().length() != 0)
                {
                    sendMessage(messageArea.getText());
                    messageArea.setText("");
                }
            }
        });
        add(send, BorderLayout.SOUTH);
        setVisible(true);
    }

    public void addMessage(String message)
    {
        messages.add(message);
        messageArea.setText(messageArea.getText() + "\n" + message);
        validate();
        repaint();
    }

    public void sendMessage(String message)
    {
        client.sendToServer(message);
    }
}