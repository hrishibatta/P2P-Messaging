import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.print.DocFlavor;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import static java.awt.Color.black;


public class Client{
    //declare needed variables and objects
    Color blue = new Color(10, 151, 255);
    Color green = new Color(36, 208, 84);
    BufferedReader in;
    PrintWriter out;
    JFrame myFrame = new JFrame("PeerChat");
    JLabel label = new JLabel();
    JLabel lbl1 = new JLabel();
    JTextField textField  = new JTextField(40);
    EmptyBorder noBorder = new EmptyBorder(new Insets(0, 0, 0, 0));
    JTextPane txt = new txtPane();
    JScrollPane scrllPane = new JScrollPane(txt);
    private int count = 0; //count variable counts the total number of lines typed
    //declare and design the txtPane
    private static class txtPane extends JTextPane {
        //constructor
        public txtPane() {
            super();
            setOpaque(false);
            setBackground(new Color(0,0,0,0));
        }
    }
    /**
     * private method that "appends" or displays the given text on the given text pane
     in the given color
     *
     * @param tp describes the text pane on which the text should be displayed
     * @param msg contains the actual string value that needs to be displayed
     * @param c describes the color in which the text should be displayed
     */
    private void appendToPane(JTextPane tp, String msg, Color c)
    {
//declare needed variables
        StyleContext stylContext = StyleContext.getDefaultStyleContext();
        AttributeSet attrSet = stylContext.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.Foreground, c);
//set the text's font, size, and alignment
        attrSet = stylContext.addAttribute(attrSet, StyleConstants.FontFamily, "Lucid Console");
                attrSet = stylContext.addAttribute(attrSet, StyleConstants.Alignment,
                        StyleConstants.ALIGN_JUSTIFIED);
//displays the text
        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(attrSet, false);
        tp.replaceSelection(msg);
    }
    //constructor
    public Client() {
//declare the needed variables
        textField.setEditable(false);
        label.setVisible(true);
        myFrame.getContentPane().add(textField, "South");
        scrllPane.setBorder(noBorder);
        txt.setMargin(new Insets(10, 10, 400, 0));
        myFrame.getContentPane().add(scrllPane);
        myFrame.pack();

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println((textField.getText()));
                textField.setText("");
            }
        });
    }

    private String getServerAddress(){

        return JOptionPane.showInputDialog(
                myFrame,
                "Enter the Server IP address:",
                "PeerChat Connect",
                JOptionPane.QUESTION_MESSAGE);
    }

    private String getName(){
    JTextField username = new JTextField(10);
    JTextField password = new JTextField(10);

    JPanel panel1 = new JPanel();
    panel1.add(new JLabel("Username: "));
    panel1.add(username);
    panel1.add(new JLabel("Password: "));
    panel1.add(password);

    int result = JOptionPane.showConfirmDialog(null, panel1, "Log in", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        String temp = username.getText() + password.getText();
        return temp;
    }
    return "why"; //checkkkkkkkkkkkkkkkkkkkkk
    }

    private void  run() throws IOException{
        setServer();

        while(true){
            String line = (in.readLine());
            if(line == null){
                continue;
            }if(line.length() > 50){
                appendToPane(txt, "Calm Down! This is a safe space. \n", black);
            }if(line.startsWith("LOGIN")){
                out.println(getName());
            }else if (line.startsWith("HANDSHAKE")){
                textField.setEditable(true);
            }else if(line.startsWith("STREAM")){
                if(count %2 == 0){
                  appendToPane(txt, line.substring(7)+"\n", green);

                }
                else{
                    appendToPane(txt, line.substring(7)+"\n", blue);
                }
                checkLines();
            }
        }
    }

    private  void checkLines(){
        if(count % 50 == 0 && count != 0){
            appendToPane(txt, "Check the clock, You've been here for a long time" + "\n", black);
        }
        count++;
    }

    private  void setServer() throws IOException{

        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 8888);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

    }

    public static void main(String[] args) throws Exception{
        Client client = new Client();
        client.myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.myFrame.setVisible(true);
        client.run();
    }
}
