package batnav;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.net.*;
import javax.swing.*;
import java.awt.*;

 
 
public class client extends JFrame {
    int FIELD_SIZE = 100;
    static int port = 1500;
    static String adress = "127.0.0.1";
    private ArrayList<joueur> players = new ArrayList<joueur>();
    private boolean gameOver = false;
    private OutputStream outS;
    private InputStream inputS;
    private ObjectOutputStream out;
    private ObjectInputStream input;
    Canvas rivalPanel;
    Canvas playerPanel;
    JTextArea board;
    int[][] playerField;
    int[][] enemyField;
    public static void main(String[] args) {
        new client();
    }
    client() {
        try {
            InetAddress ipAdress = InetAddress.getByName(adress);
            Socket socket = new Socket(ipAdress, port);
            inputS = socket.getInputStream();
            outS = socket.getOutputStream();
            input = new ObjectInputStream(inputS);
            out = new ObjectOutputStream(outS);
            playerField = (int[][]) input.readObject();
            enemyField = (int[][]) input.readObject();
            setTitle("battaille navale");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);
            rivalPanel = new Canvas();
            rivalPanel.setPreferredSize(new Dimension(FIELD_SIZE, FIELD_SIZE));
            rivalPanel.setBackground(Color.black);
        }catch (Exception ex) {};
            playerPanel = new Canvas(); 
            JButton init = new JButton("Lancer la partie");
            JButton exit = new JButton("Quitter la partie");
            board = new JTextArea();
            board.setEditable(false);
            JScrollPane scroll = new JScrollPane(board); 
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout());
            buttonPanel.add(init);
            buttonPanel.add(exit);
            JPanel container = new JPanel();
            container.setLayout(new BorderLayout());
            container.add(playerPanel, BorderLayout.NORTH);
            container.add(scroll, BorderLayout.CENTER);
            container.add(buttonPanel, BorderLayout.SOUTH);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
            add(rivalPanel);
            add(playerPanel);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
    }
   
    }