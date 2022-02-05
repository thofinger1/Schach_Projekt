package schach.pieces;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import schach.GameBoard;
import schach.Tile;

public abstract class Piece extends Tile {
    private String picture;
    private boolean white;

    public Piece(GameBoard game, String picture, int x, int y) {
        super(game, x, y);
        this.picture = picture;
        this.white = picture.contains("white");
    }

    @Override
    protected Node setupNode() {        
        ImageView image = new ImageView(new Image(getClass().getResourceAsStream(picture)));
        image.setPreserveRatio(true);
        image.setFitWidth(100);
        
        Pane pane = new Pane(image);
        
        pane.setOnDragDetected((MouseEvent event) -> {
            Dragboard db = image.startDragAndDrop(TransferMode.ANY);
            
            ClipboardContent content = new ClipboardContent();
            content.putString(x + "," + y);
            db.setContent(content);
            
            event.consume();
        });
        
        return pane;
    }
    
    public abstract boolean canMove(int targetX, int targetY);

    public boolean isWhite() {
        return white;
    }
}
