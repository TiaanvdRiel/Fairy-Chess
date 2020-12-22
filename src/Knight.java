import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Knight implements Piece {


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

    public Knight(String CurrPlayer, int iRow, int iCol, String[][] Board, String[][] ArrPieces, String[] ArrAllAccessableleSpaces, String[] ArrAllCapturableSpaces) {
        this.CurrPlayer = CurrPlayer;
        this.iRow = iRow;
        this.iCol = iCol;
        this.ArrAllAccessableleSpaces = ArrAllAccessableleSpaces;
        this.ArrAllCapturableSpaces = ArrAllCapturableSpaces;
        this.Board = Board;
        this.ArrPieces = ArrPieces;
    }

    public void TracePath() {

        //StdOut.println("***KNIGHT***");

        // Create an ArrayList object so that things can be continously added to it
        ArrayList<String> PieceAccessableSpaces = new ArrayList<String>();
        ArrayList<String> PieceCapturabelSpaces = new ArrayList<String>();

        // Convert Arrays to array list so that unspecified amount of elements can be added
        ArrayList<String> AllAccessableSpaces = new ArrayList<String>();
        Collections.addAll(AllAccessableSpaces, ArrAllAccessableleSpaces);
        ArrayList<String> AllCapturableSpaces = new ArrayList<String>();
        Collections.addAll(AllCapturableSpaces, ArrAllCapturableSpaces);

        //StdOut.println("CURRENT PLAYER: " + CurrPlayer);

        // KNIGHT
        int NewRow = 0;
        int NewCol = 0;

        //====================================
        // 1.) UP RIGHT (-2,+1)
        NewRow = iRow - 2;
        NewCol = iCol + 1;
        // Check that new position is on board
        if ((NewRow >= 0) && (NewCol <= 9)) {
            //Check is space is occupied
            //If occupied
            if (!((Board[NewRow][NewCol]).equals("."))) {
                //
                //StdOut.println("Piece at: " + NewRow + ":" + NewCol);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx].equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == w
                //
                //Blavk can only captue White
                if (CurrPlayer.equals("b")) {
                    // If piece is white (uppercase) it can be captured
                    // search throuh all capturable white pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if ((ArrPieces[0][iSearchIndx].toUpperCase()).equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == b
                // end of next space not being empty - containing a piece
            } else {
                //If unoccupied
                //StdOut.println(NewRow + "," + NewCol);
                PieceAccessableSpaces.add(NewRow + "," + NewCol);
                AllAccessableSpaces.add(NewRow + "," + NewCol);
            }// end else
        }

        //====================================
        // 2.) RIGHT UP (-1,+2)
        NewRow = iRow - 1;
        NewCol = iCol + 2;
        // Check that new position is on board
        if ((NewRow >= 0) && (NewCol <= 9)) {
            //Check is space is occupied
            //If occupied
            if (!((Board[NewRow][NewCol]).equals("."))) {
                //
                //StdOut.println("Piece at: " + NewRow + ":" + NewCol);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx].equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == w
                //
                //Blavk can only captue White
                if (CurrPlayer.equals("b")) {
                    // If piece is white (uppercase) it can be captured
                    // search throuh all capturable white pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if ((ArrPieces[0][iSearchIndx].toUpperCase()).equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == b
                // end of next space not being empty - containing a piece
            } else {
                //If unoccupied
                //StdOut.println(NewRow + "," + NewCol);
                PieceAccessableSpaces.add(NewRow + "," + NewCol);
                AllAccessableSpaces.add(NewRow + "," + NewCol);
            }// end else
        }

        //====================================
        // 3.) RIGHT DOWN (+1,+2)
        NewRow = iRow + 1;
        NewCol = iCol + 2;
        // Check that new position is on board
        if ((NewRow <= 9) && (NewCol <= 9)) {
            //Check is space is occupied
            //If occupied
            if (!((Board[NewRow][NewCol]).equals("."))) {
                //
                //StdOut.println("Piece at: " + NewRow + ":" + NewCol);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx].equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == w
                //
                //Blavk can only captue White
                if (CurrPlayer.equals("b")) {
                    // If piece is white (uppercase) it can be captured
                    // search throuh all capturable white pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if ((ArrPieces[0][iSearchIndx].toUpperCase()).equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == b
                // end of next space not being empty - containing a piece
            } else {
                //If unoccupied
                //StdOut.println(NewRow + "," + NewCol);
                PieceAccessableSpaces.add(NewRow + "," + NewCol);
                AllAccessableSpaces.add(NewRow + "," + NewCol);
            }// end else
        }

        //====================================
        // 4.) DOWN RIGHT (+2,+1)
        NewRow = iRow + 2;
        NewCol = iCol + 1;
        // Check that new position is on board
        if ((NewRow <= 9) && (NewCol <= 9)) {
            //Check is space is occupied
            //If occupied
            if (!((Board[NewRow][NewCol]).equals("."))) {
                //
                //StdOut.println("Piece at: " + NewRow + ":" + NewCol);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx].equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == w
                //
                //Blavk can only captue White
                if (CurrPlayer.equals("b")) {
                    // If piece is white (uppercase) it can be captured
                    // search throuh all capturable white pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if ((ArrPieces[0][iSearchIndx].toUpperCase()).equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == b
                // end of next space not being empty - containing a piece
            } else {
                //If unoccupied
                //StdOut.println(NewRow + "," + NewCol);
                PieceAccessableSpaces.add(NewRow + "," + NewCol);
                AllAccessableSpaces.add(NewRow + "," + NewCol);
            }// end else
        }

        //====================================
        // 5.) DOWN LEFT (+2,-1)
        NewRow = iRow + 2;
        NewCol = iCol - 1;
        // Check that new position is on board
        if ((NewRow <= 9) && (NewCol >= 0 )) {
            //Check is space is occupied
            //If occupied
            if (!((Board[NewRow][NewCol]).equals("."))) {
                //
                //StdOut.println("Piece at: " + NewRow + ":" + NewCol);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx].equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == w
                //
                //Blavk can only captue White
                if (CurrPlayer.equals("b")) {
                    // If piece is white (uppercase) it can be captured
                    // search throuh all capturable white pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if ((ArrPieces[0][iSearchIndx].toUpperCase()).equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == b
                // end of next space not being empty - containing a piece
            } else {
                //If unoccupied
                //StdOut.println(NewRow + "," + NewCol);
                PieceAccessableSpaces.add(NewRow + "," + NewCol);
                AllAccessableSpaces.add(NewRow + "," + NewCol);
            }// end else
        }

        //====================================
        // 6.) LEFT DOWN (+1,-2)
        NewRow = iRow + 1;
        NewCol = iCol - 2;
        // Check that new position is on board
        if ((NewRow <= 9) && (NewCol >= 0)) {
            //Check is space is occupied
            //If occupied
            if (!((Board[NewRow][NewCol]).equals("."))) {
                //
                //StdOut.println("Piece at: " + NewRow + ":" + NewCol);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx].equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == w
                //
                //Blavk can only captue White
                if (CurrPlayer.equals("b")) {
                    // If piece is white (uppercase) it can be captured
                    // search throuh all capturable white pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if ((ArrPieces[0][iSearchIndx].toUpperCase()).equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == b
                // end of next space not being empty - containing a piece
            } else {
                //If unoccupied
                //StdOut.println(NewRow + "," + NewCol);
                PieceAccessableSpaces.add(NewRow + "," + NewCol);
                AllAccessableSpaces.add(NewRow + "," + NewCol);
            }// end else
        }

        //====================================
        // 7.) LEFT UP (-1,-2)
        NewRow = iRow - 1;
        NewCol = iCol - 2;
        // Check that new position is on board
        if ((NewRow >= 0) && (NewCol >=0 )) {
            //Check is space is occupied
            //If occupied
            if (!((Board[NewRow][NewCol]).equals("."))) {
                //
                //StdOut.println("Piece at: " + NewRow + ":" + NewCol);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx].equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == w
                //
                //Blavk can only captue White
                if (CurrPlayer.equals("b")) {
                    // If piece is white (uppercase) it can be captured
                    // search throuh all capturable white pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if ((ArrPieces[0][iSearchIndx].toUpperCase()).equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == b
                // end of next space not being empty - containing a piece
            } else {
                //If unoccupied
                //StdOut.println(NewRow + "," + NewCol);
                PieceAccessableSpaces.add(NewRow + "," + NewCol);
                AllAccessableSpaces.add(NewRow + "," + NewCol);
            }// end else
        }

        //====================================
        // 8.) UP LEFT (-2,-1)
        NewRow = iRow - 2;
        NewCol = iCol - 1;
        // Check that new position is on board
        if ((NewRow >= 0) && (NewCol >= 0)) {
            //Check is space is occupied
            //If occupied
            if (!((Board[NewRow][NewCol]).equals("."))) {
                //
                //StdOut.println("Piece at: " + NewRow + ":" + NewCol);
                //
                // White can only captue Black
                if (CurrPlayer.equals("w")) {
                    // If piece is black (lowercase) it can be captured
                    // search throuh all capturable black pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if (ArrPieces[0][iSearchIndx].equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == w
                //
                //Blavk can only captue White
                if (CurrPlayer.equals("b")) {
                    // If piece is white (uppercase) it can be captured
                    // search throuh all capturable white pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        if ((ArrPieces[0][iSearchIndx].toUpperCase()).equals(Board[NewRow][NewCol])) {
                            PieceCapturabelSpaces.add(NewRow + "," + NewCol);
                            AllCapturableSpaces.add(NewRow + "," + NewCol);
                            //StdOut.println("Capturable Piece at: " + NewRow + ":" + NewCol);
                        }
                    }
                }// if curr player == b
                // end of next space not being empty - containing a piece
            } else {
                //If unoccupied
                //StdOut.println(NewRow + "," + NewCol);
                PieceAccessableSpaces.add(NewRow + "," + NewCol);
                AllAccessableSpaces.add(NewRow + "," + NewCol);
            }// end else
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
