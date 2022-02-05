package schach;

import schach.pieces.Piece;
import schach.pieces.Pawn;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import schach.pieces.*;

public class GameBoard implements Initializable {

    @FXML
    private GridPane grid;

    @FXML
    private TextField ipField;

    private Tile[][] board;

    private ServerSocket serverSocket;
    private PrintWriter socketWriter;
    private BufferedReader socketReader;

    private boolean whiteTurn = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupBoard();
    }

    @FXML
    private void buttonReset() {
        removeBoard();

        try {
            if (socketWriter != null) {
                socketWriter.close();
                socketWriter = null;
            }
            if (socketReader != null) {
                socketReader.close();
                socketReader = null;
            }
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        setupBoard();
    }

    @FXML
    private void buttonConnect() {
        buttonReset();
        try {
            Socket server = new Socket(ipField.getText(), 1337);
            handleConnection(server);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void buttonListen() {
        buttonReset();
        try {
            serverSocket = new ServerSocket(1337);
            Socket client = serverSocket.accept();
            handleConnection(client);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void handleConnection(Socket socket) {
        try {
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        new Thread(() -> {
            try {
                String line = socketReader.readLine();
                while (line != null) {
                    final String innerLine = line;
                    Platform.runLater(() -> {
                        attemptMove(innerLine, true);
                    });
                    line = socketReader.readLine();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void setupBoard() {
        board = new Tile[8][8];

        for (int i = 0; i < 8; i++) {
            addChessPiece(new Pawn(this, "/Pawn-black.png", i, 1));
        }

        for (int i = 0; i < 8; i++) {
            addChessPiece(new Pawn(this, "/Pawn-white.png", i, 6));
        }

        addChessPiece(new Rook(this, "/Rook-black.png", 0, 0));
        addChessPiece(new Rook(this, "/Rook-black.png", 7, 0));
        addChessPiece(new Rook(this, "/Rook-white.png", 0, 7));
        addChessPiece(new Rook(this, "/Rook-white.png", 7, 7));

        addChessPiece(new Knight(this, "/Knight-black.png", 1, 0));
        addChessPiece(new Knight(this, "/Knight-black.png", 6, 0));
        addChessPiece(new Knight(this, "/Knight-white.png", 1, 7));
        addChessPiece(new Knight(this, "/Knight-white.png", 6, 7));

        addChessPiece(new Bishop(this, "/Bishop-black.png", 2, 0));
        addChessPiece(new Bishop(this, "/Bishop-black.png", 5, 0));
        addChessPiece(new Bishop(this, "/Bishop-white.png", 2, 7));
        addChessPiece(new Bishop(this, "/Bishop-white.png", 5, 7));

        addChessPiece(new King(this, "/King-black.png", 4, 0));
        addChessPiece(new Queen(this, "/Queen-black.png", 3, 0));
        addChessPiece(new King(this, "/King-white.png", 4, 7));
        addChessPiece(new Queen(this, "/Queen-white.png", 3, 7));

        for (int y = 2; y < 6; y++) {
            for (int x = 0; x < 8; x++) {
                board[x][y] = new Tile(this, x, y);
                board[x][y].addToGrid();
            }
        }
    }

    private void removeBoard() {
        for (Tile[] tiles : board) {
            for (Tile tile : tiles) {
                tile.removeFromGrid();
            }
        }
        board = null;
    }

    private void addChessPiece(Tile tile) {
        board[tile.x][tile.y] = tile;
        board[tile.x][tile.y].addToGrid();
    }

    public void attemptMove(String content, boolean remote) {
        String[] targets = content.split(";");
        String[] source = targets[0].split(",");
        String[] target = targets[1].split(",");

        int sourceX = Integer.parseInt(source[0]);
        int sourceY = Integer.parseInt(source[1]);
        int targetX = Integer.parseInt(target[0]);
        int targetY = Integer.parseInt(target[1]);

        Tile sourceTile = board[sourceX][sourceY];
        Tile targetTile = board[targetX][targetY];

        if (!(sourceTile instanceof Piece)) {
            return;
        }

        Piece sourcePiece = (Piece) sourceTile;

        // right turn
        boolean canMove = sourcePiece.isWhite() == whiteTurn;
        // if connected: check if right player
        canMove &= socketWriter == null || (serverSocket == null && !whiteTurn) || (serverSocket != null && whiteTurn);
        // can't capture own piece
        canMove &= !(targetTile instanceof Piece && ((Piece) targetTile).isWhite() == sourcePiece.isWhite());
        // piece has to move
        canMove &= !(sourceX == targetX && sourceY == targetY);
        // individual rules
        canMove &= sourcePiece.canMove(targetX, targetY);

        if (remote || canMove) {
            move(sourceX, sourceY, targetX, targetY, remote);
        }
    }

    private void move(int sourceX, int sourceY, int targetX, int targetY, boolean remote) {
        if (socketWriter != null && !remote) {
            socketWriter.println(sourceX + "," + sourceY + ";" + targetX + "," + targetY);
        }

        whiteTurn = !whiteTurn;

        Tile sourceTile = board[sourceX][sourceY];
        Tile targetTile = board[targetX][targetY];

        sourceTile.removeFromGrid();
        targetTile.removeFromGrid();

        board[targetX][targetY] = sourceTile;
        sourceTile.setX(targetX);
        sourceTile.setY(targetY);
        sourceTile.addToGrid();

        board[sourceX][sourceY] = new Tile(this, sourceX, sourceY);
        board[sourceX][sourceY].addToGrid();

        if (targetTile instanceof King) {
            King targetPiece = (King) targetTile;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Game ended!");
            alert.setHeaderText("Game ended!");
            alert.setContentText(targetPiece.isWhite() ? "Black has won!" : "White has won!");
            alert.showAndWait();
            buttonReset();
        }
    }

    public GridPane getGrid() {
        return grid;
    }

    public Tile[][] getBoard() {
        return board;
    }
}
