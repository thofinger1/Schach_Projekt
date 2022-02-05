package schach;

import javafx.scene.Node;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

public class Tile {
    protected GameBoard game;
    protected int x;
    protected int y;
    private Node node;

    public Tile(GameBoard game, int x, int y) {
        this.game = game;
        this.x = x;
        this.y = y;
    }
    
    protected Node setupNode() {
        Pane pane = new Pane();
        return pane;
    }
    
    public void addToGrid() {
        node = setupNode();
        
        node.setOnDragOver(event -> {
            if(event.getGestureSource() != node && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            
            event.consume();
        });
        
        node.setOnDragDropped(event -> {
            if(event.getDragboard().hasString()) {
                String content = event.getDragboard().getString();
                content = content + ";" + x + "," + y;
                game.attemptMove(content, false);
            }
            event.setDropCompleted(true);
            event.consume();
        });
        
        game.getGrid().add(node, x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public void removeFromGrid() {
        game.getGrid().getChildren().remove(node);
    }
}
