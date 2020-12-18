package batnav;


import java.net.Socket;
import java.net.ServerSocket;
import java.io.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

 
public class serveur implements Serializable {
    static int port = 1500;
    private ArrayList<joueur> players = new ArrayList<joueur>();
    private boolean gameOver = false;
    private boolean firstPlayerAction;
    public static void main(String[] args) {
        new serveur();
    }
    serveur() {
        Socket clientSocket = null;
        ServerSocket serverSocket = null;
        joueur playerAction;
        joueur enemy;
        boolean isMissed = false;
        int x, y;
        String message;
        int event;
        try {
            Random random = new Random();
            serverSocket = new ServerSocket(port);
            System.out.println("Le serveur est connecté");
            while (players.size() < 2) {
                clientSocket = serverSocket.accept();
                joueur player = new joueur(clientSocket);
                players.add(player);
                System.out.println("Le client est connecté");
            }
            System.out.println("Début du jeu");
            joueur player1 = players.get(0);
            joueur player2 = players.get(1);
            player1.out.writeObject(player1.getField());
            player1.out.writeObject(player2.encryptField());
            player2.out.writeObject(player2.getField());
            player2.out.writeObject(player1.encryptField());
            while (!gameOver) {
                playerAction = (joueur) (firstPlayerAction ? players.get(0) : players.get(1));
                enemy = (joueur) (firstPlayerAction ? players.get(1) : players.get(0));
                firstPlayerAction = !firstPlayerAction;
                isMissed = false;
                while (!isMissed) {
                    playerAction.out.writeBoolean(true);//Your turn 1-2
                    playerAction.out.flush();
                    enemy.out.writeBoolean(false);//enemy turn; //2-1
                    enemy.out.flush();
                    x = playerAction.input.readInt(); //1-3
                    y = playerAction.input.readInt(); //1-4
                    event = playerAction.input.readInt(); //1-5
                    System.out.println(x + " ; " + y + " | " + event);
                    if (event == 1) 
                    {
                        if (!enemy.hitSamePlace(x, y)) {   //si la personne n'a pas encore atteint ce point
                            if (enemy.checkHit(x, y)) { //tu as atteint la cible
                                enemy.shotInCell(x, y);
                                message = "Vous avez toucher votre cible";
                                if (enemy.checkDefeat()) {    //pas de navires ennemis survivants
                                    message = "Vous avez gagner!";
                                    gameOver = true;
                                }
                            } else if (enemy.CheckForEmptinessCell(x, y)) {
                                enemy.shotInCell(x, y);
                                isMissed = true;
                                message = "Vous avez rater votre adversaire";
                            }
                        }
                    }
                    playerAction.out.writeBoolean(gameOver);
                    enemy.out.writeBoolean(gameOver);
 
                    playerAction.out.writeObject(playerAction.getField());
                    playerAction.out.writeObject(enemy.encryptField());
 
                    enemy.out.writeObject(enemy.getField());
                    enemy.out.writeObject(playerAction.encryptField());
                    System.out.println(isMissed);
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
