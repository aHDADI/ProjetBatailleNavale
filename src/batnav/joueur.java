package batnav;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
 
public class joueur { 
    final int FIELD_SIZE = 10;
    private serveur server;
    private Socket clientSocket = null;
    private int[][] field;
    public OutputStream outS;
    public InputStream inputS;
    public ObjectOutputStream out;
    public ObjectInputStream input;
    private final int[] shipsPattern = {1,2,3};
    private Random random;
    public joueur(Socket socket) { // déclaration du joueur
        try {
            this.server = server;
            this.clientSocket = socket;
            init();
            out = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (Exception ex) {
        }
    }
    void init() { // initialisation de la grille
       field = new int[FIELD_SIZE][FIELD_SIZE];
        random = new Random();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = 0;
            }
        }
        generateShips();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println("");
        }
    }
    public boolean isFree(int x, int y) {// vérifier si la case est libre
        boolean check;
        int[][] Array = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
        int dx = 0;
        int dy = 0;
        if ((x >= 0) && (x < 10) && (y >= 0) && (y < 10) && (field[x][y] == 0)) {
            for (int i = 0; i < 8; i++) {
                dx = x + Array[i][0];
                dy = y + Array[i][1];
                if ((dx >= 0) && (dx < 10) && (dy >= 0) && (dy < 10) && (field[dx][dy] == 2)) {
                    check = false;
                    return check;
                }
            }
            check = true;
        } else {
            check = false;
        }
        return check;
    }
    public void generateShips() {// génération des bateaux
        int x, y, kx, ky;
        boolean check;
        for (int i = 3; i >= 0; i--) {
            for (int j = 0; j <= 3 - i; j++) {
                do {
                    x = random.nextInt(10);
                    y = random.nextInt(10);
                    kx = random.nextInt(2);
                    if (kx == 0) {
                        ky = 1;
                    } else {
                        ky = 0;
                    }
                    check = false;
                    for (int k = 0; k <= i; k++) {
                        if (!(isFree(x + kx * k, y + ky * k))) {
                            check = true;
                            break;
                        }
                    }
                    if (!check) {
                        for (int k = 0; k <= i; k++) {
                            field[x + kx * k][y + ky * k] = 2;
                        }
                    }
                } while (check);
            }
        }
    }
    public int[][] getField() {
        return field;
    }
    public int[][] encryptField() {
        int[][] encryptedField = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (field[i][j] != 2) {
                    encryptedField[i][j] = field[i][j];
                } else {
                    encryptedField[i][j] = 0;
                }
            }
        }
        return encryptedField;
    }
    public boolean checkDefeat() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (field[i][j] == 2) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean checkHit(int x, int y) {// vérifier si l'attaque a touché
        if ((field[x][y] == 2)) {
            return true;
        } else {
            return false;
        }
    }
    public boolean hitSamePlace(int x, int y) {// vérifier si l'un des joueurs a toucher la meme place
        if ((field[x][y] == 1) || (field[x][y] == 3)) {
            return true;
        } else {
            return false;
        }
    }   
    public boolean CheckForEmptinessCell(int x, int y) {
        if (field[x][y] == 0) {
            return true;
        } else {
            return false;
        }
    }
    public void shotInCell(int x, int y) {
        int cell = field[x][y];
        switch (cell) {
            case 0:
                field[x][y] = 1;
                break;
            case 1:
                field[x][y] = 2;
                break;
            case 2:
                field[x][y] = 3;
                break;
        }
    }
}