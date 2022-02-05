package schach.pieces;

import schach.GameBoard;
import schach.Tile;

public class Pawn extends Piece {

    public Pawn(GameBoard game, String picture, int x, int y) {
        super(game, picture, x, y);
    }

    @Override
    public boolean canMove(int targetX, int targetY) {
        int direction = isWhite() ? -1 : 1;
        boolean isInitialPosition = (isWhite() && y == 6) || (!isWhite() && y == 1);
        boolean isSingleMove = targetY == y + direction;
        boolean isDoubleMove = isInitialPosition && targetY == y + direction * 2;
            Tile targetTile = game.getBoard()[targetX][targetY];
            
        if (x != targetX) {
            if(targetX != x + 1 && targetX != x - 1) return false;
            
            if (!(targetTile instanceof Piece)) {
                return false;
            }
            Piece targetPiece = (Piece) targetTile;
            
            return targetPiece.isWhite() != isWhite();
        } else {
            return (isSingleMove || isDoubleMove) && !(targetTile instanceof Piece);
        }
    }
}
