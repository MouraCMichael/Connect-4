
public interface ConnectFourInterface
{ 
    public void setBlockingMove();
    public int nextPlayer(int pPlayer);
    public void undo();
    public void reset();
    public int selectCol (int pCol);
    public boolean isWinner();
    public boolean isWinner(int player);
    public boolean checkVerticalWin(int player); 
    public boolean checkHorizontalWin(int player);
    public boolean checkDiagonal1Win(int player);
    public boolean checkDiagonal2Win(int player);
    public int setDifficultyLevel(String dLevel);
    public int getTrackStrategy();
    public void setTrackStrategy(int strategy);
    public int smartMoves(int bRow, int bCol);
    public boolean checkSides(int bRow, int bCol);
    public boolean checkAboveBelow(int bRow, int bCol);
    public boolean checkUpperLeft(int bRow, int bCol);
    public boolean checkUpperRight(int bRow, int bCol);
    public boolean checkLowerLeft(int bRow, int bCol);
    public boolean checkLowerRight(int bRow, int bCol);
    public int randomMove();
}
