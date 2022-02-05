package schach.pieces;

import schach.GameBoard;

public class Knight extends Piece {

    public Knight(GameBoard game, String picture, int x, int y) {
        super(game, picture, x, y);
    }
    
    @Override
    public boolean canMove(int targetX, int targetY) {
        return (Math.abs(x - targetX) == 2 && Math.abs(y - targetY) == 1)
                || (Math.abs(x - targetX) == 1 && Math.abs(y - targetY) == 2);
    }
}
