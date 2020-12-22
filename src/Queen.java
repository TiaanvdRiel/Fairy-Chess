import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Queen implements Piece {


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

    public Queen(String CurrPlayer, int iRow, int iCol, String[][] Board, String[][] ArrPieces, String[] ArrAllAccessableleSpaces, String[] ArrAllCapturableSpaces) {
        this.CurrPlayer = CurrPlayer;
        this.iRow = iRow;
        this.iCol = iCol;
        this.ArrAllAccessableleSpaces = ArrAllAccessableleSpaces;
        this.ArrAllCapturableSpaces = ArrAllCapturableSpaces;
        this.Board = Board;
        this.ArrPieces = ArrPieces;
    }


    public void TracePath() {

        //StdOut.println("***QUEEN***");
        // Create an ArrayList object so that things can be continously added to it
        ArrayList<String> PieceAccessableSpaces = new ArrayList<String>();
        ArrayList<String> PieceCapturabelSpaces = new ArrayList<String>();

        // Convert Arrays to array list so that unspecified amount of elements can be added
        ArrayList<String> AllAccessableSpaces = new ArrayList<String>();
        Collections.addAll(AllAccessableSpaces, ArrAllAccessableleSpaces);
        ArrayList<String> AllCapturableSpaces = new ArrayList<String>();
        Collections.addAll(AllCapturableSpaces, ArrAllCapturableSpaces);

        //StdOut.println("CURRENT PLAYER: " + CurrPlayer);

        //=====================================================
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

        //=====================================================
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


        //=====================================================
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


        //=====================================================
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


        //=====================================================
        // 1.) DIAGONAL - RIGHT UP
        //StdOut.println("DIAGONAL - RIGHT UP");
        // Moves one row up and one column right simultaneously
        for (int rowIndx = iRow - 1, colIndx = iCol + 1; (rowIndx >= 0 && colIndx <= 9); rowIndx--, colIndx++) {
            if (!((Board[rowIndx][colIndx]).equals(".")))  {
                //
                //StdOut.println("Piece at: " + rowIndx + ":" + colIndx);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx ].equals(Board[rowIndx][colIndx])){
                            PieceCapturabelSpaces.add(rowIndx + "," + colIndx);
                            AllCapturableSpaces.add(rowIndx + "," + colIndx);
                            //StdOut.println("Capturable Piece at: " + rowIndx + ":" + colIndx);
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
                        if (  (ArrPieces[0][iSearchIndx ].toUpperCase()).equals(Board[rowIndx][colIndx])){
                            PieceCapturabelSpaces.add(rowIndx + "," + colIndx);
                            AllCapturableSpaces.add(rowIndx + "," + colIndx);
                            //StdOut.println("Capturable Piece at: " + rowIndx + ":" + colIndx);
                            break;
                        }
                    }
                }// if curr player == b
                //
                break;
            }// end of next space not being empty - containing a piece
            //StdOut.println(rowIndx + "," + colIndx);
            PieceAccessableSpaces.add(rowIndx + "," + colIndx);
            AllAccessableSpaces.add(rowIndx + "," + colIndx);
        }

        //=====================================================
        // 2.)  DIAGONAL - LEFT UP
        //StdOut.println("DIAGONAL - LEFT UP");
        // Moves one row up and one column right simultaneously
        for (int rowIndx = iRow - 1, colIndx = iCol - 1; (rowIndx >= 0 && colIndx >= 0); rowIndx--, colIndx--) {
            if (!((Board[rowIndx][colIndx]).equals(".")))  {
                //
                //StdOut.println("Piece at: " + rowIndx + ":" + colIndx);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx ].equals(Board[rowIndx][colIndx])){
                            PieceCapturabelSpaces.add(rowIndx + "," + colIndx);
                            AllCapturableSpaces.add(rowIndx + "," + colIndx);
                            //StdOut.println("Capturable Piece at: " + rowIndx + ":" + colIndx);
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
                        if (  (ArrPieces[0][iSearchIndx ].toUpperCase()).equals(Board[rowIndx][colIndx])){
                            PieceCapturabelSpaces.add(rowIndx + "," + colIndx);
                            AllCapturableSpaces.add(rowIndx + "," + colIndx);
                            //StdOut.println("Capturable Piece at: " + rowIndx + ":" + colIndx);
                            break;
                        }
                    }
                }// if curr player == b
                //
                break;
            }// end of next space not being empty - containing a piece
            //StdOut.println(rowIndx + "," + colIndx);
            PieceAccessableSpaces.add(rowIndx + "," + colIndx);
            AllAccessableSpaces.add(rowIndx + "," + colIndx);
        }

        //=====================================================
        // 3.) DIAGONAL - RIGHT DOWN
        //StdOut.println("DIAGONAL - RIGHT DOWN");
        // Moves one row up and one column right simultaneously
        for (int rowIndx = iRow + 1, colIndx = iCol + 1; (rowIndx <= 9 && colIndx <= 9); rowIndx++, colIndx++) {
            if (!((Board[rowIndx][colIndx]).equals(".")))  {
                //
                //StdOut.println("Piece at: " + rowIndx + ":" + colIndx);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx ].equals(Board[rowIndx][colIndx])){
                            PieceCapturabelSpaces.add(rowIndx + "," + colIndx);
                            AllCapturableSpaces.add(rowIndx + "," + colIndx);
                            //StdOut.println("Capturable Piece at: " + rowIndx + ":" + colIndx);
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
                        if (  (ArrPieces[0][iSearchIndx ].toUpperCase()).equals(Board[rowIndx][colIndx])){
                            PieceCapturabelSpaces.add(rowIndx + "," + colIndx);
                            AllCapturableSpaces.add(rowIndx + "," + colIndx);
                            //StdOut.println("Capturable Piece at: " + rowIndx + ":" + colIndx);
                            break;
                        }
                    }
                }// if curr player == b
                //
                break;
            }// end of next space not being empty - containing a piece
            //StdOut.println(rowIndx + "," + colIndx);
            PieceAccessableSpaces.add(rowIndx + "," + colIndx);
            AllAccessableSpaces.add(rowIndx + "," + colIndx);
        }

        //=====================================================
        // 4.) DIAGONAL - LEFT DOWN
        //StdOut.println("DIAGONAL - LEFT DOWN");
        // Moves one row up and one column right simultaneously
        for (int rowIndx = iRow + 1, colIndx = iCol - 1; (rowIndx <= 9 && colIndx >= 0); rowIndx++, colIndx--) {
            if (!((Board[rowIndx][colIndx]).equals(".")))  {
                //
                //StdOut.println("Piece at: " + rowIndx + ":" + colIndx);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx ].equals(Board[rowIndx][colIndx])){
                            PieceCapturabelSpaces.add(rowIndx + "," + colIndx);
                            AllCapturableSpaces.add(rowIndx + "," + colIndx);
                            //StdOut.println("Capturable Piece at: " + rowIndx + ":" + colIndx);
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
                        if (  (ArrPieces[0][iSearchIndx ].toUpperCase()).equals(Board[rowIndx][colIndx])){
                            PieceCapturabelSpaces.add(rowIndx + "," + colIndx);
                            AllCapturableSpaces.add(rowIndx + "," + colIndx);
                            //StdOut.println("Capturable Piece at: " + rowIndx + ":" + colIndx);
                            break;
                        }
                    }
                }// if curr player == b
                //
                break;
            }// end of next space not being empty - containing a piece
            //StdOut.println(rowIndx + "," + colIndx);
            PieceAccessableSpaces.add(rowIndx + "," + colIndx);
            AllAccessableSpaces.add(rowIndx + "," + colIndx);
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
