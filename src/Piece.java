public interface Piece {

    //Methods
    public abstract void TracePath();


    // Getters
    public abstract String getCurrPlayer();
    public String[] getArrAllAccessableSpaces();
    public String[] getArrAllCapturableSpaces();
    public abstract int getiRow();
    public abstract int getiCol();
    public abstract String[][] getBoard();
    public abstract String[][] getArrPieces();
    public String[] getArrPieceAccessableSpaces();
    public String[] getArrPieceCapturableSpaces();

    // Setters
    public abstract void setiRow(int iRow);
    public abstract void setiCol(int iCol);
    public abstract void setCurrPlayer(String currPlayer);
    public abstract void setAllAccessableleSpaces(String[] AllAccessableleSpaces);
    public abstract void setAllCapturableSpaces(String[] allCapturableSpaces);
    public abstract void setBoard(String[][] Board);
    public abstract void setArrPieces(String[][] ArrPieces);


}
