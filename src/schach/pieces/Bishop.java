package schach.pieces;

import schach.GameBoard;

public class Bishop extends Piece {

    public Bishop(GameBoard game, String picture, int x, int y) {
        super(game, picture, x, y);
    }
    
    @Override
    public boolean canMove(int targetX, int targetY) {
        return Math.abs(x - targetX) == Math.abs(y - targetY);
    }
}
