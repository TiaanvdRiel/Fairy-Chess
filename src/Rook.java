
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Rook implements Piece {


    public String CurrPlayer;
    public String[] ArrAllAccessableleSpaces;
    public String[] ArrAllCapturableSpaces;
    public String[][] Board;
    public String[][] ArrPieces;
    public int iRow;
    public int iCol;
    //
    public String[] ArrPieceAccessableSpaces;
    public String[] ArrPieceCapturableSpaces;



    public Rook(String CurrPlayer, int iRow, int iCol, String[][] Board, String[][] ArrPieces, String[] ArrAllAccessableleSpaces, String[] ArrAllCapturableSpaces) {
        this.CurrPlayer = CurrPlayer;
        this.iRow = iRow;
        this.iCol = iCol;
        this.ArrAllAccessableleSpaces = ArrAllAccessableleSpaces;
        this.ArrAllCapturableSpaces = ArrAllCapturableSpaces;
        this.Board = Board;
        this.ArrPieces = ArrPieces;
    }


    public void TracePath() {

        //StdOut.println("***ROOK***");
        // Create an ArrayList object so that things can be continously added to it
        ArrayList<String> PieceAccessableSpaces = new ArrayList<String>();
        ArrayList<String> PieceCapturabelSpaces = new ArrayList<String>();

        // Convert Arrays to array list so that unspecified amount of elements can be added
        ArrayList<String> AllAccessableSpaces = new ArrayList<String>();
        Collections.addAll(AllAccessableSpaces, ArrAllAccessableleSpaces);
        ArrayList<String> AllCapturableSpaces = new ArrayList<String>();
        Collections.addAll(AllCapturableSpaces, ArrAllCapturableSpaces);

        //StdOut.println("CURRENT PLAYER: " + CurrPlayer);


        // UP
        //StdOut.println("UP");
        for (int rowIndx = iRow - 1; rowIndx >= 0; rowIndx--) {
            if (!((Board[rowIndx][iCol]).equals("."))) {
                //
                //StdOut.println("Piece at: " + rowIndx + ":" + iCol);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx].equals(Board[rowIndx][iCol])) {
                            PieceCapturabelSpaces.add(rowIndx + "," + iCol);
                            AllCapturableSpaces.add(rowIndx + "," + iCol);
                            //StdOut.println("Capturable Piece at: " + rowIndx + ":" + iCol);
                            break;
                        }
                    }
                }// if curr player == w
                //
                //Black can only captue White
                if (CurrPlayer.equals("b")) {
                    // If piece is white (uppercase) it can be captured
                    // search throuh all capturable white pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if ((ArrPieces[0][iSearchIndx].toUpperCase()).equals(Board[rowIndx][iCol])) {
                            PieceCapturabelSpaces.add(rowIndx + "," + iCol);
                            AllCapturableSpaces.add(rowIndx + "," + iCol);
                            //StdOut.println("Capturable Piece at: " + rowIndx + ":" + iCol);
                            break;
                        }
                    }
                }// if curr player == b
                //
                break;
            }// end of next space not being empty - containing a piece
            //StdOut.println(rowIndx + "," + iCol);
            PieceAccessableSpaces.add(rowIndx + "," + iCol);
            AllAccessableSpaces.add(rowIndx + "," + iCol);

        }

        //DOWN
        //StdOut.println("DOWN");
        for (int rowIndx = iRow + 1; rowIndx <= 9; rowIndx++) {
            if (!((Board[rowIndx][iCol]).equals("."))) {
                //
                //StdOut.println("Piece at: " + rowIndx + ":" + iCol);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx].equals(Board[rowIndx][iCol])) {
                            PieceCapturabelSpaces.add(rowIndx + "," + iCol);
                            AllCapturableSpaces.add(rowIndx + "," + iCol);
                            //StdOut.println("Capturable Piece at: " + rowIndx + ":" + iCol);
                            break;
                        }
                    }
                }// if curr player == w
                //
                //Blavk can only captue White
                if (CurrPlayer.equals("b")) {
                    // If piece is white (uppercase) it can be captured
                    // search throuh all capturable white pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if ((ArrPieces[0][iSearchIndx].toUpperCase()).equals(Board[rowIndx][iCol])) {
                            PieceCapturabelSpaces.add(rowIndx + "," + iCol);
                            AllCapturableSpaces.add(rowIndx + "," + iCol);
                            //StdOut.println("Capturable Piece at: " + rowIndx + ":" + iCol);
                            break;
                        }
                    }
                }// if curr player == b
                //
                break;
            }// end of next space not being empty - containing a piece
            //StdOut.println(rowIndx + "," + iCol);
            PieceAccessableSpaces.add(rowIndx + "," + iCol);
            AllAccessableSpaces.add(rowIndx + "," + iCol);
        }


        //RIGHT
        //StdOut.println("RIGHT");
        for (int colIndx = iCol + 1; colIndx <= 9; colIndx++) {
            if (!((Board[iRow][colIndx]).equals(".")))  {
                //
                //StdOut.println("Piece at: " + iRow + ":" + colIndx);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx ].equals(Board[iRow][colIndx])){
                            PieceCapturabelSpaces.add(iRow + "," + colIndx);
                            AllCapturableSpaces.add( iRow + "," + colIndx);
                            //StdOut.println("Capturable Piece at: " + iRow + ":" + colIndx);
                            break;
                        }
                    }
                }// if curr player == w
                //
                //Blavk can only captue White
                if (CurrPlayer.equals("b")) {
                    // If piece is white (uppercase) it can be captured
                    // search throuh all capturable white pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (  (ArrPieces[0][iSearchIndx ].toUpperCase()).equals(Board[iRow][colIndx])){
                            PieceCapturabelSpaces.add(iRow + "," + colIndx);
                            AllCapturableSpaces.add( iRow + "," + colIndx);
                            //StdOut.println("Capturable Piece at: " + iRow + ":" + colIndx);
                            break;
                        }
                    }
                }// if curr player == b
                //
                break;
            }// end of next space not being empty - containing a piece
            //StdOut.println(iRow + "," + colIndx);
            PieceAccessableSpaces.add(iRow + "," + colIndx);
            AllAccessableSpaces.add(iRow + "," + colIndx);
        }


        // LEFT
        //StdOut.println("LEFT");
        for (int colIndx = iCol - 1; colIndx >= 0; colIndx--) {
            if (!((Board[iRow][colIndx]).equals(".")))  {
                //
                //StdOut.println("Piece at: " + iRow + ":" + colIndx);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx ].equals(Board[iRow][colIndx])){
                            PieceCapturabelSpaces.add(iRow + "," + colIndx);
                            AllCapturableSpaces.add( iRow + "," + colIndx);
                            //StdOut.println("Capturable Piece at: " + iRow + ":" + colIndx);
                            break;
                        }
                    }
                }// if curr player == w
                //
                //Blavk can only captue White
                if (CurrPlayer.equals("b")) {
                    // If piece is white (uppercase) it can be captured
                    // search throuh all capturable white pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (  (ArrPieces[0][iSearchIndx ].toUpperCase()).equals(Board[iRow][colIndx])){
                            PieceCapturabelSpaces.add(iRow + "," + colIndx);
                            AllCapturableSpaces.add( iRow + "," + colIndx);
                            //StdOut.println("Capturable Piece at: " + iRow + ":" + colIndx);
                            break;
                        }
                    }
                }// if curr player == b
                //
                break;
            }// end of next space not being empty - containing a piece
            //StdOut.println(iRow + "," + colIndx);
            PieceAccessableSpaces.add(iRow + "," + colIndx);
            AllAccessableSpaces.add(iRow + "," + colIndx);
        }

        // Piece Specific ========================================================================
        //MOVABLE SPACES
        // Convert Array List to a normal Array
        this.ArrPieceAccessableSpaces = new String[PieceAccessableSpaces.size()];
        this.ArrPieceAccessableSpaces = PieceAccessableSpaces.toArray(ArrPieceAccessableSpaces);

        //CAPTURABLE SPACES
        // Convert Array List to a normal Array
        this.ArrPieceCapturableSpaces = new String[PieceCapturabelSpaces.size()];
        this.ArrPieceCapturableSpaces = PieceCapturabelSpaces.toArray(ArrPieceCapturableSpaces);


        // All Pieces array =====================================================================
        this.ArrAllAccessableleSpaces = AllAccessableSpaces.toArray(ArrAllAccessableleSpaces);
        this.ArrAllCapturableSpaces = AllCapturableSpaces.toArray(ArrAllCapturableSpaces);

    }


    // Getters
    public final String getCurrPlayer() {
        return this.CurrPlayer;
    }

    public String[] getArrAllAccessableSpaces() {
        return this.ArrAllAccessableleSpaces;
    }

    public String[] getArrAllCapturableSpaces() {
        return this.ArrAllCapturableSpaces;
    }

    public final int getiRow() {
        return this.iRow;
    }

    public final int getiCol() {
        return this.iCol;
    }

    public final String[][] getBoard() {
        return this.Board;
    }

    public final String[][] getArrPieces() {
        return this.ArrPieces;
    }

    //============================================
    public String[] getArrPieceAccessableSpaces() {
        return ArrPieceAccessableSpaces;
    }

    public String[] getArrPieceCapturableSpaces() {
        return ArrPieceCapturableSpaces;
    }
    //============================================

    // Setters
    public final void setiRow(int iRow) {
        this.iRow = iRow;
    }

    public final void setiCol(int iCol) {
        this.iCol = iCol;
    }

    public final void setCurrPlayer(String currPlayer) {
        this.CurrPlayer = currPlayer;
    }

    public final void setAllAccessableleSpaces(String[] AllAccessableleSpaces) {
        this.ArrAllAccessableleSpaces = AllAccessableleSpaces;
    }

    public final void setAllCapturableSpaces(String[] allCapturableSpaces) {
        this.ArrAllCapturableSpaces = allCapturableSpaces;
    }

    public final void setBoard(String[][] Board) {
        this.Board = Board;
    }

    public final void setArrPieces(String[][] ArrPieces) {
        this.ArrPieces = ArrPieces;
    }
}



