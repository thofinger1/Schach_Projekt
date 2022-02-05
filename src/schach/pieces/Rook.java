package schach.pieces;

import schach.GameBoard;

public class Rook extends Piece {

    public Rook(GameBoard game, String picture, int x, int y) {
        super(game, picture, x, y);
    }
    
    @Override
    public boolean canMove(int targetX, int targetY) {
        return Math.abs(x - targetX) == 0 || Math.abs(y - targetY) == 0;
    }
}
