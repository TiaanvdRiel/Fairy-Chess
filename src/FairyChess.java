import java.util.List;
import java.awt.print.PrinterGraphics;
import java.io.FileWriter;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.channels.NetworkChannel;
import java.util.*;
import javax.sound.sampled.Line;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The driver class for the Fairy Chess project.
 */

public class FairyChess {
    // =============================================================================================================
    // ********************************************** MAIN *********************************************************
    //==============================================================================================================

    public static boolean GlobalAnimation = false;
    public static String GlobalStatusLine = "";
    public static String CurrScreen = "";
    public static String[][] GlobalArrPiecesInput = {
            {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
            {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"}
    };

    public static String[][] ArrPieces = {
            {"k", "r", "q", "n", "b", "p", "d", "f", "e", "a", "w"},
            {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"}
    };
    public static String[][] Board = new String[10][10];
    public static String NextPlayerMarker = "";
    public static int LineNum = 1;
    public static Scanner GuiScanner = null;
    public static int GameState = 0;
    public static String BuiltUpLine = "";
    public static String[] GUIArrPieceAccessableSpaces = new String[0];
    public static String[] GUIArrPieceCapturableSpaces = new String[0];
    public static boolean GUI;
    public static boolean IsMoveValid = true;
    public static boolean CurrPlayerKingInCheck = false;
    public static boolean OpposingKingInCheck = false;
    public static boolean GUIPieceFound = false;
    public static String GUIPieceAtSource;
    public static String PromtePieceTo;
    public static boolean ShouldPromote = false;
    public static ArrayList<String> ListSavedMoves = new ArrayList<String>();
    public static int FileNumber = (int) (Math.random() * 100000 + 1);
    public static boolean GOpposingKingInCheckMate = false;

    //=================================================================================================================
    //***************************************************** MAIN ******************************************************
    //=================================================================================================================
    public static void main(String[] args) {
        String StatusLine = "";
        // Get the board file's name, and initialize a File object to represent it
        // Read in from a file
        if (args.length < 1) {
            throw new IllegalArgumentException("Provide a file name as first argument");
        }

        String GUIMode = args[0];
        //StdOut.println(GUIMode);

        // TEXT MODE
        if (GUIMode.equals("T")) {
            ReadInputBoard();
            ReadMoveFile();
            GUI = false;

        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (GUIMode.equals("G")) {
            CurrScreen = "Start";
            // GameScreen();
            GUI = true;
            DisplayBackroung();
            AnimationLoop();


        }
    }


    //=================================================================================================================
    //********************************************** READ IN INPUT BOARD **********************************************
    //=================================================================================================================
    public static void ReadInputBoard() {
        // Get the Board File from the user
        int g = -1;
        Scanner boardScanner = null;

        // Get the board file form the user and make sure that it exists
        // Keep asking the user to enter a board until he/she enters a valid one
        while (g == -1) {
            String BoardFileName = (String) JOptionPane.showInputDialog(null, "Please enter an Inpput Board",
                    "Input Board", JOptionPane.QUESTION_MESSAGE, null, null, "valid1.board");

            String boardFilename = BoardFileName;
            File boardFile = new File(boardFilename);
            // Initialize the Scanner
            boardScanner = null;
            try {
                boardScanner = new Scanner(boardFile);
                g = 1;
            } catch (FileNotFoundException e) {
                // throw new IllegalArgumentException("Board file does not exist");
            }
        }

        // Read the file line by line, printing out as we go along
        LineNum = 1;
        // int that keeps track of what section of the text file is being validated
        // 1 - Piece allocation
        // 2 - Board config
        // 3 - Status line
        int CurrentSection = 1;     // Keeps track of what section we are validating
        int BoardRank = -1;         // Keeps track of what rank in the board we a reading in
        //
        while (boardScanner.hasNextLine()) {
            String line = boardScanner.nextLine();
            //----------------------------------------
            // Ignore comment lines
            //
            if (line.charAt(0) == '%') {
                // //StdOut.println("Skip" + line);
            } else {
                //----------------------------------------
                // 1 - Piece Allocation
                if (CurrentSection == 1) {
                    SectionOne(line, LineNum, ArrPieces);
                }
                //----------------------------------------
                // 2 - Board Config
                if (CurrentSection == 2) {
                    if (line.equals("")) {
                        BoardValidationErrors.illegalBoardDimension();
                    } else {
                        ++BoardRank;
                        SectionTwo(line, LineNum, Board, BoardRank, ArrPieces);
                    }
                }
                //----------------------------------------
                // 3 - StatusLine
                if (CurrentSection == 3) {
                    GlobalStatusLine = SectionThree(line, LineNum, Board, NextPlayerMarker);
                    //StdOut.println("Status Line: " + TempStatusLine);
                    NextPlayerMarker = GlobalStatusLine.substring(0, 1);
                    //StdOut.println("NEXT: " + NextPlayerMarker);
                }
                //----------------------------------------
                // checks for section separators
                if (line.equals("-----")) {
                    ++CurrentSection;
                }
                // Inc line number
                // ++LineNum;
            }// end else
            ++LineNum;
        } // end while board scanner has next line
        // end of input board file
        // the input board file is valid
        // all of the necessary parameters have been set
        LineNum = 0;
    }

    //=================================================================================================================
    //************************************************ READ IN MOVE FILE **********************************************
    //=================================================================================================================
    public static void ReadMoveFile() {

        // ************************************ READ IN MOVE FILE *******************************************
        // TODO Prompt the user for an input board !!!!!!!!!!!!!!!!!!!!!!!
        // Get the Board File from the user
        int g = -1;
        Scanner moveScanner = null;

        // Get the board file form the user and make sure that it exists
        // Keep asking the user to enter a board until he/she enters a valid one

        while (g == -1) {
            String MoveFileName = (String) JOptionPane.showInputDialog(null, "Please enter a Move File",
                    "Move File", JOptionPane.QUESTION_MESSAGE, null, null, "valid_move.moves");

            String moveFilename = MoveFileName;
            File moveFile = new File(moveFilename);
            // Initialize the Scanner
            moveScanner = null;
            try {
                moveScanner = new Scanner(moveFile);
                g = 1;
            } catch (FileNotFoundException e) {
                // throw new IllegalArgumentException("Board file does not exist");
            }
        }

        int MoveLineNum = 1;
        while (moveScanner.hasNextLine()) {
            String line = moveScanner.nextLine();
            //----------------------------------------
            // Ignore comment lines
            //
            if (line.charAt(0) == '%') {
                // //StdOut.println("Skip" + line);
            } else {
                //----------------------------------------
                //StdOut.println(line );
                // Send the line off for validation
                Board = ExecuteMove(line, NextPlayerMarker, MoveLineNum, Board, LineNum, ArrPieces, GlobalStatusLine);
                // Change Next Player
                if (NextPlayerMarker.equals("w")) {
                    NextPlayerMarker = "b";
                } else {
                    if (NextPlayerMarker.equals("b")) {
                        NextPlayerMarker = "w";
                    }
                }
            }// end not % line
            MoveLineNum++;
            //PrintBoard(Board, LineNum);
            //StdOut.println(GlobalStatusLine);
            //StdOut.println("=============================================================");
        }

        // PRINT OUT THE FINAL BOARD
        for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
            String CompareTemp = (GlobalArrPiecesInput[1][iSearchIndx]);
            ////StdOut.println( BoardTemp + ":" + CompareTemp  );
            if (!(CompareTemp.equals("0"))) {
                StdOut.println(GlobalArrPiecesInput[0][iSearchIndx] + ":" + GlobalArrPiecesInput[1][iSearchIndx]);
            }
        }// end for compare
        StdOut.println("-----");
        PrintBoard(Board, LineNum);
        StdOut.println("-----");
        StdOut.println(GlobalStatusLine);


    }


    // =============================================================================================================
    // ******************************************** EXECUTE MOVE ****************************************************
    //==============================================================================================================
    public static String[][] ExecuteMove(String line, String NextPlayerMarker, int MoveLineNum, String[][] Board, int BoardLineNum, String[][] ArrPieces, String StatusLine) {


        IsMoveValid = true;
        //StdOut.println(line + " (" + NextPlayerMarker + ")" + " (" + MoveLineNum + ")");
        String Source = "";
        String Destination = "";
        String PromotePieceTo = "";

        boolean Move = false;
        boolean Capture = false;
        boolean Promotion = false;
        boolean Check = false;
        boolean CheckMate = false;
        boolean CastlingQueenside = false;
        boolean CastlingKingsdide = false;

        int iRowSource = 0;
        int iColSource = 0;

        // Separate information in status line
        // Separates the line into the wanted variables ==========================================================
        //StdOut.println("========= STATUS LINE ============");
        String Temp = GlobalStatusLine;

        int col1 = Temp.indexOf(":");
        String NextPlayer = Temp.substring(0, col1);
        ////StdOut.println("Next Player: " + NextPlayerMarker);
        // Remove next player marker ========================
        Temp = Temp.substring(col1 + 1);
        // Find substring containing castling opportunities
        int col2 = Temp.indexOf(":");
        String CastlingOppertunities = Temp.substring(0, col2);
        ////StdOut.println("Caslting Oppertunities: " + CastlingOppertunities);
        // Remove Castling oppertunities ===================
        Temp = Temp.substring(col2 + 1);
        // Find Halfmove clock
        int col3 = Temp.indexOf(":");
        String HalfmoveCounter = Temp.substring(0, col3);
        //StdOut.println("Halfmove Counter: " + HalfmoveCounter);
        // Remove Halfmove Counter =========================
        Temp = Temp.substring(col3 + 1);
        String MoveCounter = Temp;
        //StdOut.println("Move Counter: " + MoveCounter);

        String PieceAtSource = "";
        //====================================================================================
        // DETERMINE WHAT KIND OF OPPERATIONS NEED TO BE PREFORMED
        // Castling
        if (line.equals("0-0-0+")) {
            CastlingQueenside = true;
            Check = true;
            //StdOut.println("Castle Kingside and check");
        } else if (line.equals("0-0+")) {
            CastlingKingsdide = true;
            Check = true;
            //StdOut.println("Castle Kingside and check");
        } else {
            if (line.equals("0-0-0")) {
                //StdOut.println("Castling Queenside");
                CastlingQueenside = true;
            } else if (line.equals("0-0")) {
                //StdOut.println("Castling Kingside");
                CastlingKingsdide = true;
            }
        }
        //
        // Move
        //StdOut.println("Else");
        if ((line.contains("-")) && (CastlingKingsdide == false) && (CastlingQueenside == false)) {
            Move = true;
            //StdOut.println("Move");
        }
        // Capture
        if (line.contains("x")) {
            Capture = true;
            //StdOut.println("Capture");
        }
        // Promotion
        if (line.contains("=")) {
            Promotion = true;
            //StdOut.println("Promotion");
        }
        // Check and Checkmate
        if (line.contains("++")) {
            CheckMate = true;
            //StdOut.println("Check Mate");

        } else if (line.contains("+")) {
            Check = true;
            //StdOut.println("CHECK");
            //StdOut.println("Check");
        }
        //===================================

        if ((CastlingKingsdide == false) && (CastlingQueenside == false)) {
            // SEPERATE THE LINE================================================
            String RestOfLine = "";
            // REMOVE SOURCE
            if (Move) {
                int dash = line.indexOf("-");
                Source = line.substring(0, dash);
                //StdOut.println("Source: " + Source);
                RestOfLine = line.substring(dash + 1, line.length());
                //StdOut.println(RestOfLine);
            }
            if (Capture) {
                int ex = line.indexOf("x");
                Source = line.substring(0, ex);
                //StdOut.println("Source: " + Source);
                RestOfLine = line.substring(ex + 1, line.length());
                //StdOut.println(RestOfLine);
            }
            // REMOVE CHECK NOTATIONS
            //remove checkmate notation
            if (CheckMate) {
                RestOfLine = RestOfLine.substring(0, RestOfLine.length() - 2);
                //StdOut.println(RestOfLine);
            }
            //remove check notation
            if (Check) {
                RestOfLine = RestOfLine.substring(0, RestOfLine.length() - 1);
                //StdOut.println(RestOfLine);
            }
            // REMOVE PROMOTION
            if (Promotion) {
                int equal = RestOfLine.indexOf("=");
                PromotePieceTo = RestOfLine.substring(equal + 1);
                //StdOut.println("Promote to: " + PromotePieceTo);
                RestOfLine = RestOfLine.substring(0, equal);
                //StdOut.println(RestOfLine);
            }
            // SET DESTINATION
            Destination = RestOfLine;

            // GET THE PIECE AT THE SOURCE
            char temp = Source.charAt(0);
            iRowSource = 10 - Integer.parseInt(Source.substring(1));
            iColSource = ((int) temp) - 97;
            PieceAtSource = Board[iRowSource][iColSource];
        }
        //==============================================================

        //===========================================================================================================
        //============================================ Castling  ====================================================
        //===========================================================================================================
        //"0-0-0" - QUEENSIDE (left)
        if (CastlingQueenside == true) {
            //====================
            // The player is not allowed to castle himself out of check
            //StdOut.println("Castle Queenside");
            CurrPlayerKingInCheck = CheckCheckCurrPlayer(NextPlayerMarker, Board, ArrPieces);
            //StdOut.println(CurrPlayerKingInCheck);
            if (CurrPlayerKingInCheck) {
                //StdOut.println("YOU CANNOR CASTLE OUT OF CHECK");
                MoveValidationErrors.illegalCastlingMove(MoveLineNum);
            }
            //====================
            Board = CastlingQueenside(NextPlayerMarker, HalfmoveCounter, MoveLineNum, Board, ArrPieces, CastlingOppertunities);

            // UPDATE THE CASTLING IN THE STATUS LINE
            if (NextPlayerMarker.equals("b")) {
                CastlingOppertunities = "--" + CastlingOppertunities.substring(2);
            }
            if (NextPlayerMarker.equals("w")) {
                CastlingOppertunities = CastlingOppertunities.substring(0, 2) + "--";
            }
        }
        // ============================================================
        //"0-0" - KINGSIDE (right)
        if (CastlingKingsdide == true) {
            //====================
            // The player is not allowed to castle himself out of check
            //StdOut.println("Castle Kingside");
            boolean CurrPlayerKingInCheck = CheckCheckCurrPlayer(NextPlayerMarker, Board, ArrPieces);
            //StdOut.println(CurrPlayerKingInCheck);
            if (CurrPlayerKingInCheck) {
                //StdOut.println("YOU CANNOR CASTLE OUT OF CHECK");
                MoveValidationErrors.illegalCastlingMove(MoveLineNum);
            }
            //====================
            Board = CastlingKingside(NextPlayerMarker, HalfmoveCounter, MoveLineNum, Board, ArrPieces, CastlingOppertunities);
            // UPDATE THE CASTLING IN THE STATUS LINE
            if (NextPlayerMarker.equals("b")) {
                CastlingOppertunities = "--" + CastlingOppertunities.substring(2);
            }
            if (NextPlayerMarker.equals("w")) {
                CastlingOppertunities = CastlingOppertunities.substring(0, 2) + "--";
            }
        }


        //===========================================================================================================
        //============================================ Movements  ===================================================
        //===========================================================================================================

        // MOVE THE PIECE
        if (Move == true) {
            Board = Move(NextPlayerMarker, MoveLineNum, Source, Destination, Board, BoardLineNum, ArrPieces);
        }
        //
        // CAPTURE PIECE
        if (Capture == true) {
            Board = Capture(NextPlayerMarker, MoveLineNum, Source, Destination, Board, BoardLineNum, ArrPieces);
        }
        //
        // PROMOTIONS
        if (Promotion == true) {
            Board = PromotePiece(NextPlayerMarker, MoveLineNum, Source, Destination, PromotePieceTo, Board, ArrPieces);
        }

        //===========================================================================================================
        //============================================ Checks  ======================================================
        //===========================================================================================================
        // Is opposing player now check
        // CheckCheckOpposingPlayer
        //StdOut.println(Move);
        OpposingKingInCheck = CheckCheckOpposingPlayer(NextPlayerMarker, Board, ArrPieces);
        StdOut.println("Opposing King in check");
        if (!GUI) {
            // 5.2.8) The move put the opposing king in capture but check notation was not used
            if (Check == false && OpposingKingInCheck == true && Move == true) {
                //StdOut.println("1");
                MoveValidationErrors.illegalMove(MoveLineNum);

            }
            // 5.3.7) The capture put the opposing king in check but check notation was not used
            if (Check == false && OpposingKingInCheck == true && Capture == true) {
                MoveValidationErrors.illegalCapture(MoveLineNum);
            }
            // 5.4.5) The castling put the opposing king in check but the suffix is not used
            if (Check == false && OpposingKingInCheck == true && (CastlingKingsdide == true || CastlingQueenside == true)) {
                MoveValidationErrors.illegalCastlingMove(MoveLineNum);
            }

            //====================================================================================
            // 5.6) The check extention is there but the opposing king is not in check
            if (Check == true && OpposingKingInCheck == false && Move == true) {
                MoveValidationErrors.illegalCheck(MoveLineNum);
            }
            // The capture put the opposing king in check but check notation was not used
            if (Check == true && OpposingKingInCheck == false && Capture == true) {
                MoveValidationErrors.illegalCheck(MoveLineNum);
            }
            // the check suffox os present but the castling did not put the opposing king in check
            if (Check == true && OpposingKingInCheck == false && (CastlingKingsdide == true || CastlingQueenside == true)) {
                MoveValidationErrors.illegalCheck(MoveLineNum);
            }
        }

        // Stays the same for GUI and text - not notation and can never move yourself into check
        //====================================================================================
        // is current player now in check
        // CheckCheckCurrPlayer
        CurrPlayerKingInCheck = CheckCheckCurrPlayer(NextPlayerMarker, Board, ArrPieces);
        StdOut.println(CurrPlayerKingInCheck);

        if (CurrPlayerKingInCheck == true && Move == true) {
            StdOut.println("YOU PUT YOURSELF IN CHECK");
            MoveValidationErrors.illegalMove(MoveLineNum);
        }

        if (CurrPlayerKingInCheck == true && Capture == true) {
            StdOut.println("YOU PUT YOURSELF IN CHECK");
            MoveValidationErrors.illegalCapture(MoveLineNum);
        }
        // 5.4.5) You cannot castle yourself into check
        if (CurrPlayerKingInCheck == true && (CastlingKingsdide == true || CastlingQueenside == true)) {
            MoveValidationErrors.illegalCastlingMove(MoveLineNum);
        }


        // ============================================================================================================
        // =========================================== CHECK MATE =====================================================
        // ============================================================================================================
        //TODO
        //Check Mate
        if (OpposingKingInCheck) {
            GOpposingKingInCheckMate = CheckCheckMate(NextPlayerMarker, Board, ArrPieces);
        }


        //===========================================================================================================
        //====================================== Update status line  ================================================
        //===========================================================================================================
        // UPDATE STATUS LINE

        // 5.2.6) If the halfmove clock equals 50 and the piece being moved is not a pawn or a drunk
        if (Move == true && HalfmoveCounter.equals("50")) {
            if (!(PieceAtSource.toUpperCase().equals("P")) && !(PieceAtSource.toUpperCase().equals("D"))) {
                //StdOut.println("HALFMOVE CLOCK EXCEEDED");
                MoveValidationErrors.illegalMove(MoveLineNum);
            }
        }
        StdOut.println("Is valid Move:" + IsMoveValid);
        if (IsMoveValid) {
            // Capturing and moves by a pawn or a drunken soldier or a pawn resets the halfmove clock
            if (Capture == true) {
                HalfmoveCounter = Integer.toString(0);
            }
            if ((PieceAtSource.toUpperCase().equals("P")) || (PieceAtSource.toUpperCase().equals("D"))) {
                HalfmoveCounter = Integer.toString(0);
            }

            // If the current move is not a capture or a move by a pawn or a drunk - INCREASE THE HALFMOVE COUNTER
            if (Capture == false) {
                if (!((PieceAtSource.toUpperCase().equals("P")) || (PieceAtSource.toUpperCase().equals("D")))) {
                    HalfmoveCounter = Integer.toString(Integer.parseInt(HalfmoveCounter) + 1);
                }
            }

            // Each time black moves - INCREASE THE MOVE COUNTER
            if (NextPlayerMarker.equals("b")) {
                MoveCounter = Integer.toString(Integer.parseInt(MoveCounter) + 1);
            }

            String DisplayNextPlayer = "";
            // Change Next Player
            if (NextPlayerMarker.equals("w")) {
                DisplayNextPlayer = "b";
            } else {
                if (NextPlayerMarker.equals("b")) {
                    DisplayNextPlayer = "w";
                }
            }
            //=========================
            // If the player moved his King form the staring position, then he loses both castling opportunities
            if (NextPlayerMarker.equals("w") && PieceAtSource.equals("K") && iRowSource == 9 && iColSource == 5) {
                //StdOut.println("Moved White King From Start");
                CastlingOppertunities = CastlingOppertunities.substring(0, 2) + "--";

            }
            if (NextPlayerMarker.equals("b") && PieceAtSource.equals("k") && iRowSource == 0 && iColSource == 5) {
                //StdOut.println("Moved Black King From Start");
                CastlingOppertunities = "--" + CastlingOppertunities.substring(2);
            }
            //=========================
            // If the player moves one of his rooks form staring position then he loses the castling opportunity on that side
            // WHITE QUEENSIDE
            if (NextPlayerMarker.equals("w") && PieceAtSource.equals("R") && iRowSource == 9 && iColSource == 0) {
                //StdOut.println("Moved White Rook QUEENSIDE From Start");
                CastlingOppertunities = CastlingOppertunities.substring(0, 2) + "-" + CastlingOppertunities.substring(3);
            }
            //WHITE KINGSIDE
            if (NextPlayerMarker.equals("w") && PieceAtSource.equals("R") && (iRowSource == 9 && iColSource == 9)) {
                //StdOut.println("Moved White Rook KINGSIDE From Start");
                CastlingOppertunities = CastlingOppertunities.substring(0, 3) + "-";
            }
            // BLACK QUEENSIDE
            if (NextPlayerMarker.equals("b") && PieceAtSource.equals("r") && iRowSource == 0 && iColSource == 0) {
                //StdOut.println("Moved Black Rook QUEENSIDE From Start");
                CastlingOppertunities = "-" + CastlingOppertunities.substring(1);
            }
            // BLACK KINGSIDE
            if (NextPlayerMarker.equals("b") && PieceAtSource.equals("r") && iRowSource == 0 && iColSource == 9) {
                //StdOut.println("Moved Black Rook KINGSIDE From Start");
                CastlingOppertunities = CastlingOppertunities.substring(0, 1) + "-" + CastlingOppertunities.substring(2);
            }

            GlobalStatusLine = DisplayNextPlayer + ":" + CastlingOppertunities + ":" + HalfmoveCounter + ":" + MoveCounter;
        }


        // StdOut.println("Updated Status Line: " + TempStatusLine);

        return Board;
    }

    // =============================================================================================================
    // ***********************(************ CASTLING - QUEENSIDE ***************************************************
    //==============================================================================================================
    public static String[][] CastlingQueenside(String NextPlayerMarker, String HalfmoveCounter, int MoveLineNum, String Board[][], String[][] ArrPieces, String CastlingOppertunities) {

        // 5.4.3 The halfmove clock equals 50
        if (HalfmoveCounter.equals("50")) {
            // StdOut.println("Halfmove Exceeded");
            MoveValidationErrors.illegalCastlingMove(MoveLineNum);
        }
        // BLACK QUEENSIDE
        if (NextPlayerMarker.equals("b")) {
            //StdOut.println("Castle - BLACK Queenside");
            //Is Black Queenside Caslting oppertunity still available
            if (CastlingOppertunities.substring(0, 1).equals("-")) {
                //StdOut.println("Castling Oppertunity not available");
                MoveValidationErrors.illegalCastlingMove(MoveLineNum);
            }
            // The Rook and King need to be at starting possiotions AND the spaces inbetween them need to be open
            if (!(Board[0][0].equals("r") && Board[0][1].equals(".") && Board[0][2].equals(".") && Board[0][3].equals(".") && Board[0][4].equals(".") && Board[0][5].equals("k"))) {
                // StdOut.println("Positions not correct/ Spaces not clear");
                MoveValidationErrors.illegalCastlingMove(MoveLineNum);
            }
            // Move the castle and the rook:
            if (IsMoveValid) {
                Board[0][0] = ".";
                Board[0][5] = ".";
                Board[0][2] = "k";
                Board[0][3] = "r";
            }
        }
        // WHITE QUEENSIDE
        if (NextPlayerMarker.equals("w")) {
            //StdOut.println("Castle - WHITE Queenside");
            //Is White Queenside Caslting oppertunity still available
            if (CastlingOppertunities.substring(2, 3).equals("-")) {
                //StdOut.println("Castling Oppertunity not available");
                MoveValidationErrors.illegalCastlingMove(MoveLineNum);
            }
            // The Rook and King need to be at starting possiotions AND the spaces inbetween them need to be open
            if (!(Board[9][0].equals("R") && Board[9][1].equals(".") && Board[9][2].equals(".") && Board[9][3].equals(".") && Board[9][4].equals(".") && Board[9][5].equals("K"))) {
                /// StdOut.println("Positions not correct/ Spaces not clear");
                MoveValidationErrors.illegalCastlingMove(MoveLineNum);
            }
            // Move the castle and the rook:
            if (IsMoveValid) {
                Board[9][0] = ".";
                Board[9][5] = ".";
                Board[9][2] = "K";
                Board[9][3] = "R";
            }
        }
        return Board;
    }

    // =============================================================================================================
    // ***********************(************ CASTLING - QUEENSIDE ***************************************************
    //==============================================================================================================
    public static String[][] CastlingKingside(String NextPlayerMarker, String HalfmoveCounter, int MoveLineNum, String Board[][], String[][] ArrPieces, String CastlingOppertunities) {

        // 5.4.3 The halfmove clock equals 50
        if (HalfmoveCounter.equals("50")) {
            // StdOut.println("Halfmove Exceeded");
            MoveValidationErrors.illegalCastlingMove(MoveLineNum);
        }
        // BLACK KINGSIDE
        if (NextPlayerMarker.equals("b")) {
            // StdOut.println("Castle - BLACK Kingside");
            //Is Black Queenside Caslting oppertunity still available
            if (CastlingOppertunities.substring(1, 2).equals("-")) {
                //StdOut.println("Castling Oppertunity not available");
                MoveValidationErrors.illegalCastlingMove(MoveLineNum);
            }
            // The Rook and King need to be at starting possiotions AND the spaces inbetween them need to be open
            if (!(Board[0][9].equals("r") && Board[0][8].equals(".") && Board[0][7].equals(".") && Board[0][6].equals(".") && Board[0][5].equals("k"))) {
                //StdOut.println("Positions not correct/ Spaces not clear");
                MoveValidationErrors.illegalCastlingMove(MoveLineNum);
            }
            if (IsMoveValid) {
                // Move the castle and the rook:
                Board[0][5] = ".";
                Board[0][9] = ".";
                Board[0][8] = "k";
                Board[0][7] = "r";
            }
        }
        // WHITE KINGSIDE
        if (NextPlayerMarker.equals("w")) {
            //StdOut.println("Castle - WHITE Kingside");
            //Is White Queenside Caslting oppertunity still available
            if (CastlingOppertunities.substring(3).equals("-")) {
                //StdOut.println("Castling Oppertunity not available");
                MoveValidationErrors.illegalCastlingMove(MoveLineNum);
            }
            // The Rook and King need to be at starting possiotions AND the spaces inbetween them need to be open
            if (!(Board[9][9].equals("R") && Board[9][8].equals(".") && Board[9][7].equals(".") && Board[9][6].equals(".") && Board[9][5].equals("K"))) {
                //StdOut.println("Positions not correct/ Spaces not clear");
                MoveValidationErrors.illegalCastlingMove(MoveLineNum);
            }
            // Move the castle and the rook:
            if (IsMoveValid) {
                Board[9][9] = ".";
                Board[9][5] = ".";
                Board[9][8] = "K";
                Board[9][7] = "R";
            }
        }
        return Board;
    }

    // =============================================================================================================
    // ******************************************* PROMOTION *******************************************************
    //==============================================================================================================
    public static String[][] PromotePiece(String NextPlayerMarker, int MoveLineNum, String Source, String Destination, String PromotePieceTo, String Board[][], String[][] ArrPieces) {
        //StdOut.println("***PROMOTION***");
        // Destination
        int iRowDestination;
        int iColDestination;
        //
        char temp = Destination.charAt(0);
        iRowDestination = 10 - Integer.parseInt(Destination.substring(1));
        iColDestination = ((int) temp) - 97;
        String DestinationCoords = iRowDestination + "," + iColDestination;
        //StdOut.println("Destination: " + iRowDestination + "," + iColDestination);
        String PieceToBePromoted = Board[iRowDestination][iColDestination];
        //StdOut.println("Piece to be promoted: " + PieceToBePromoted);
        //

        // 5.5.1) =====================================================
        // If the curr player is white his piece need to be in row 0
        if ((NextPlayerMarker.equals("w")) && (iRowDestination > 0)) {
            MoveValidationErrors.illegalPromotion(MoveLineNum);
        }
        // If the curr player is black his piece need to be in row 9
        if ((NextPlayerMarker.equals("b")) && (iRowDestination < 9)) {
            MoveValidationErrors.illegalPromotion(MoveLineNum);
        }

        //=============================================================
        // The piece can not be promoted to a
        String StringTemp = PromotePieceTo.toUpperCase();
        if (StringTemp.equals("P") || StringTemp.equals("D") || StringTemp.equals("K") || StringTemp.equals("E")) {
            MoveValidationErrors.illegalPromotion(MoveLineNum);
        }

        //=============================================================
        // If the player is white the piece that is promoted to must be a capital letter
        boolean PieceFound = false;
        // Search for a WHITE piece
        if (NextPlayerMarker.equals("w")) {
            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                String CompareTemp = (ArrPieces[0][iSearchIndx]).toUpperCase();
                //StdOut.println( BoardTemp + ":" + CompareTemp  );
                if (PromotePieceTo.equals(CompareTemp)) {
                    PieceFound = true;
                    //StdOut.println("Piece Found");
                }
            }// end for compare
            if (!(PieceFound)) {
                //StdOut.println("Piece not found");
                //StdOut.println( Character.toString((char)(97 + iCol)) + (10 - iRow));
                MoveValidationErrors.illegalPromotion(MoveLineNum);
            }
        }
        //If the player is black the piece that is promoted to must be a lowercase letter
        // Search for a BLACK piece
        if (NextPlayerMarker.equals("b")) {
            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                String CompareTemp = (ArrPieces[0][iSearchIndx]);
                //StdOut.println( BoardTemp + ":" + CompareTemp  );
                if (PromotePieceTo.equals(CompareTemp)) {
                    PieceFound = true;
                    //StdOut.println("Piece Found");
                }
            }// end for compare
            if (!(PieceFound)) {
                //StdOut.println("Piece not found");
                MoveValidationErrors.illegalPromotion(MoveLineNum);
            }
        }

        // 5.5.2) The piece promoted to does not appear at leat once in the piece allocation
        StringTemp = PromotePieceTo.toLowerCase();
        for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
            //StdOut.println("Array of pieces: " + ArrPieces[0][iSearchIndx] + ":" + ArrPieces[1][iSearchIndx]);
            String ArrPiece = ArrPieces[0][iSearchIndx];
            if (ArrPiece.equals(StringTemp)) {
                if (ArrPieces[1][iSearchIndx].equals("0")) {
                    //StdOut.println("Piece not allocated");
                    MoveValidationErrors.illegalPromotion(MoveLineNum);
                }
            }
        }

        // Promote the piece
        if (IsMoveValid) {
            Board[iRowDestination][iColDestination] = PromotePieceTo;
        }
        return Board;
    }


    // =============================================================================================================
    // ********************************************** MOVE *********************************************************
    //==============================================================================================================
    public static String[][] Move(String NextPlayerMarker, int MoveLineNum, String Source, String Destination, String[][] Board, int BoardLineNum, String[][] ArrPieces) {
        int iRowSource;
        int iColSource;
        //
        int iRowDestination;
        int iColDestination;
        //
        String SourcePiece;
        String DestinitionPiece;

        char temp = Source.charAt(0);
        iRowSource = 10 - Integer.parseInt(Source.substring(1));
        iColSource = ((int) temp) - 97;
        temp = Destination.charAt(0);
        iRowDestination = 10 - Integer.parseInt(Destination.substring(1));
        iColDestination = ((int) temp) - 97;

        String SourceCoords = iRowSource + "," + iColSource;
        String DestinationCoords = iRowDestination + "," + iColDestination;

        //StdOut.println("Source: " + iRowSource + "," + iColSource);
        //StdOut.println("Destination: " + iRowDestination + "," + iColDestination);

        // Get Source Piece
        SourcePiece = Board[iRowSource][iColSource];
        //StdOut.println("Source Piece: " + SourcePiece);
        // Get Destination Piece
        DestinitionPiece = Board[iRowDestination][iColDestination];
        //StdOut.println("Destination Piece: " + DestinitionPiece);

        // 5.2.1) The source square is not validated by a piece of the current player =================================
        boolean PieceFound = false;
        // search for the piece on the board in the array of known pieces

        // Search for a WHITE piece
        if (NextPlayerMarker.equals("w")) {
            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                String CompareTemp = (ArrPieces[0][iSearchIndx]).toUpperCase();
                //StdOut.println( BoardTemp + ":" + CompareTemp  );
                if (SourcePiece.equals(CompareTemp)) {
                    PieceFound = true;
                    //StdOut.println("Piece Found");
                }
            }// end for compare
            if (!(PieceFound)) {
                MoveValidationErrors.illegalMove(MoveLineNum);

            }
        }
        // Search for a BLACK piece
        if (NextPlayerMarker.equals("b")) {
            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                String CompareTemp = (ArrPieces[0][iSearchIndx]);
                //StdOut.println( BoardTemp + ":" + CompareTemp  );
                if (SourcePiece.equals(CompareTemp)) {
                    PieceFound = true;
                    //StdOut.println("Piece Found");
                }
            }// end for compare
            if (!(PieceFound)) {
                MoveValidationErrors.illegalMove(MoveLineNum);
            }
        }
        // ============================================================================================================
        // TRACE THE PATH OF THE APPROPRIATE PIECE
        String[] ArrAllAccessableSpaces = new String[0];
        String[] ArrAllCapturableSpaces = new String[0];
        String[] ArrPieceAccessableSpaces = new String[0];
        String[] ArrPieceCapturableSpaces = new String[0];

        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("P")) {
            // PAWN
            Piece pawn = new Pawn(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            pawn.TracePath();
            ArrPieceAccessableSpaces = pawn.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = pawn.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("B")) {
            // BISHOP
            Piece bishop = new Bishop(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            bishop.TracePath();
            ArrPieceAccessableSpaces = bishop.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = bishop.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("R")) {
            // ROOK
            Piece rook = new Rook(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            rook.TracePath();
            ArrPieceAccessableSpaces = rook.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = rook.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("N")) {
            // KNIGHT
            Piece knight = new Knight(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            knight.TracePath();
            ArrPieceAccessableSpaces = knight.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = knight.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("Q")) {
            // QUEEN
            Piece queen = new Queen(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            queen.TracePath();
            ArrPieceAccessableSpaces = queen.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = queen.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("K")) {
            // KING
            Piece king = new King(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            king.TracePath();
            ArrPieceAccessableSpaces = king.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = king.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("D")) {
            // DRUNKEN SOLDIER
            Piece drunksoldier = new DrunkenSoldier(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            drunksoldier.TracePath();
            ArrPieceAccessableSpaces = drunksoldier.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = drunksoldier.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("E")) {
            // ELEPHANT
            Piece elephant = new Elephant(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            elephant.TracePath();
            ArrPieceAccessableSpaces = elephant.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = elephant.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("F")) {
            // FLYING DRAGON
            Piece flyingdragon = new FlyingDragon(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            flyingdragon.TracePath();
            ArrPieceAccessableSpaces = flyingdragon.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = flyingdragon.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("W")) {
            // PRINCESS
            Piece princess = new Princess(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            princess.TracePath();
            ArrPieceAccessableSpaces = princess.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = princess.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("A")) {
            // AMAZON
            Piece amazon = new Amazon(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            amazon.TracePath();
            ArrPieceAccessableSpaces = amazon.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = amazon.getArrPieceCapturableSpaces();
        }

//        StdOut.println("Array of PIECE Accessable Spaces");
//        for (int x = 0; x <= ArrPieceAccessableSpaces.length - 1; x++) {
//          StdOut.println(ArrPieceAccessableSpaces[x]);
//        }
//        StdOut.println("Array of PIECE Capturable Spaces");
//        for (int x = 0; x <= ArrPieceCapturableSpaces.length - 1; x++) {
//            StdOut.println(ArrPieceCapturableSpaces[x]);
//        }

        // ============================================================================================================
        // 5.2.2) The destination square is occupied
        if (!(DestinitionPiece.equals("."))) {
            // StdOut.println("6");
            MoveValidationErrors.illegalMove(MoveLineNum);

        }

        // ============================================================================================================
        // 5.2.3) The piece can`t move to the destination square
        // Search for DestinationPiece in the array of accessible spaces
        PieceFound = false;
        for (int iSearchIndx = 0; iSearchIndx <= ArrPieceAccessableSpaces.length - 1; iSearchIndx++) {
            //StdOut.println("i:" + iSearchIndx);
            String CompareTemp = (ArrPieceAccessableSpaces[iSearchIndx]);
            if (DestinationCoords.equals(CompareTemp)) {
                PieceFound = true;
                //StdOut.println("Piece Found");
            }
        }// end for compare
        if (!(PieceFound)) {
            MoveValidationErrors.illegalMove(MoveLineNum);
        }
        // ============================================================================================================
        // MOVE THE PIECE AND UPDATE THE BOARD

        if (IsMoveValid) {
            Board[iRowSource][iColSource] = ".";
            Board[iRowDestination][iColDestination] = SourcePiece;
        }
        return Board;
    }

    // =============================================================================================================
    // ********************************************** CAPTURE ******************************************************
    //==============================================================================================================
    public static String[][] Capture(String NextPlayerMarker, int MoveLineNum, String Source, String Destination, String[][] Board, int BoardLineNum, String[][] ArrPieces) {
        int iRowSource;
        int iColSource;
        //
        int iRowDestination;
        int iColDestination;
        //
        String SourcePiece;
        String DestinitionPiece;

        char temp = Source.charAt(0);
        iRowSource = 10 - Integer.parseInt(Source.substring(1));
        iColSource = ((int) temp) - 97;
        temp = Destination.charAt(0);
        iRowDestination = 10 - Integer.parseInt(Destination.substring(1));
        iColDestination = ((int) temp) - 97;

        String SourceCoords = iRowSource + "," + iColSource;
        String DestinationCoords = iRowDestination + "," + iColDestination;

        // Get Source Piece
        SourcePiece = Board[iRowSource][iColSource];
        // Get Destination Piece
        DestinitionPiece = Board[iRowDestination][iColDestination];

        // 5.3.1) The source square is not occupied by a piece of the current player =================================
        boolean PieceFound = false;
        // search for the piece on the board in the array of known pieces

        // Search for a WHITE piece
        if (NextPlayerMarker.equals("w")) {
            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                String CompareTemp = (ArrPieces[0][iSearchIndx]).toUpperCase();
                //StdOut.println( BoardTemp + ":" + CompareTemp  );
                if (SourcePiece.equals(CompareTemp)) {
                    PieceFound = true;
                    //StdOut.println("Piece Found");
                }
            }// end for compare
            if (!(PieceFound)) {
                MoveValidationErrors.illegalCapture(MoveLineNum);
            }
        }
        // Search for a BLACK piece
        if (NextPlayerMarker.equals("b")) {
            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                String CompareTemp = (ArrPieces[0][iSearchIndx]);
                //StdOut.println( BoardTemp + ":" + CompareTemp  );
                if (SourcePiece.equals(CompareTemp)) {
                    PieceFound = true;
                    //StdOut.println("Piece Found");
                }
            }// end for compare
            if (!(PieceFound)) {
                MoveValidationErrors.illegalCapture(MoveLineNum);
            }
        }
        // ============================================================================================================
        // TRACE THE PATH OF THE APPROPRIATE PIECE ====================================================================

        String[] ArrAllAccessableSpaces = new String[0];
        String[] ArrAllCapturableSpaces = new String[0];
        String[] ArrPieceAccessableSpaces = new String[0];
        String[] ArrPieceCapturableSpaces = new String[0];

        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("P")) {
            // PAWN
            Piece pawn = new Pawn(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            pawn.TracePath();
            ArrPieceAccessableSpaces = pawn.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = pawn.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("B")) {
            // BISHOP
            Piece bishop = new Bishop(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            bishop.TracePath();
            ArrPieceAccessableSpaces = bishop.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = bishop.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("R")) {
            // ROOK
            Piece rook = new Rook(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            rook.TracePath();
            ArrPieceAccessableSpaces = rook.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = rook.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("N")) {
            // KNIGHT
            Piece knight = new Knight(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            knight.TracePath();
            ArrPieceAccessableSpaces = knight.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = knight.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("Q")) {
            // QUEEN
            Piece queen = new Queen(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            queen.TracePath();
            ArrPieceAccessableSpaces = queen.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = queen.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("K")) {
            // KING
            Piece king = new King(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            king.TracePath();
            ArrPieceAccessableSpaces = king.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = king.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("D")) {
            // DRUNKEN SOLDIER
            Piece drunksoldier = new DrunkenSoldier(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            drunksoldier.TracePath();
            ArrPieceAccessableSpaces = drunksoldier.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = drunksoldier.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("E")) {
            // ELEPHANT
            Piece elephant = new Elephant(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            elephant.TracePath();
            ArrPieceAccessableSpaces = elephant.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = elephant.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("F")) {
            // FLYING DRAGON
            Piece flyingdragon = new FlyingDragon(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            flyingdragon.TracePath();
            ArrPieceAccessableSpaces = flyingdragon.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = flyingdragon.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("W")) {
            // PRINCESS
            Piece princess = new Princess(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            princess.TracePath();
            ArrPieceAccessableSpaces = princess.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = princess.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("A")) {
            // AMAZON
            Piece amazon = new Amazon(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            amazon.TracePath();
            ArrPieceAccessableSpaces = amazon.getArrPieceAccessableSpaces();
            ArrPieceCapturableSpaces = amazon.getArrPieceCapturableSpaces();
        }

//        StdOut.println("Array of PIECE Accessable Spaces");
//        for (int x = 0; x <= ArrPieceAccessableSpaces.length - 1; x++) {
//            StdOut.println(ArrPieceAccessableSpaces[x]);
//        }
//        StdOut.println("Array of PIECE Capturable Spaces");
//        for (int x = 0; x <= ArrPieceCapturableSpaces.length - 1; x++) {
//            StdOut.println(ArrPieceCapturableSpaces[x]);
//        }

        // ============================================================================================================
        // 5.2.2) The destination square is not occupied  by the opposing player`s piece ==============================
        boolean OpposingPieceFound = false;
        // search for the piece on the board in the array of known pieces
        // Search for a WHITE piece - Curr Player is Black
        if (NextPlayerMarker.equals("b")) {
            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                String CompareTemp = (ArrPieces[0][iSearchIndx]).toUpperCase();
                //StdOut.println( BoardTemp + ":" + CompareTemp  );
                if (SourcePiece.equals(CompareTemp)) {
                    PieceFound = true;
                    //StdOut.println("Piece Found");
                }
            }// end for compare
            if (!(PieceFound)) {
                MoveValidationErrors.illegalCapture(MoveLineNum);
            }
        }
        // Search for a BLACK piece - Curr Player is white
        if (NextPlayerMarker.equals("w")) {
            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                String CompareTemp = (ArrPieces[0][iSearchIndx]);
                //StdOut.println( BoardTemp + ":" + CompareTemp  );
                if (SourcePiece.equals(CompareTemp)) {
                    PieceFound = true;
                    //StdOut.println("Piece Found");
                }
            }// end for compare
            if (!(PieceFound)) {
                MoveValidationErrors.illegalCapture(MoveLineNum);
            }
        }

        // ============================================================================================================
        // 5.2.3) The piece can`t move to the destination square ======================================================
        // Search for DestinationPiece in the array of accessible spaces
        PieceFound = false;
        for (int iSearchIndx = 0; iSearchIndx <= ArrPieceCapturableSpaces.length - 1; iSearchIndx++) {
            //StdOut.println("i:" + iSearchIndx);
            String CompareTemp = (ArrPieceCapturableSpaces[iSearchIndx]);
            //StdOut.println("Compare: " + CompareTemp + " + " + ArrPieceCapturableSpaces[iSearchIndx]);
            if (DestinationCoords.equals(CompareTemp)) {
                PieceFound = true;
                //StdOut.println("Piece Found");
            }
        }// end for compare
        if (!(PieceFound)) {
            //StdOut.println("Piece not found");
            //StdOut.println("Cant move there");
            //StdOut.println("12");
            MoveValidationErrors.illegalCapture(MoveLineNum);
        }

        // ============================================================================================================
        // MOVE THE PIECE AND UPDATE THE BOARD
        if (IsMoveValid) {
            Board[iRowSource][iColSource] = ".";
            Board[iRowDestination][iColDestination] = SourcePiece;

        }
        return Board;
    }

    // =============================================================================================================
    // ************************************** CHECK - OPPOSING PLAYER **********************************************
    //==============================================================================================================
    public static boolean CheckCheckOpposingPlayer(String NextPlayerMarker, String[][] Board, String[][] ArrPieces) {

        String OpposingKingCoords = "";
        // Locate the Opposing king ==========================
        for (int iRow = 0; iRow < 10; iRow++) {
            for (int iCol = 0; iCol < 10; iCol++) {
                // If player is white - search for black king
                if ((Board[iRow][iCol].equals("k")) && (NextPlayerMarker.equals("w"))) {
                    OpposingKingCoords = iRow + "," + iCol;
                    //StdOut.println("Opposing King:" + OpposingKingCoords);
                }
                if ((Board[iRow][iCol].equals("K")) && (NextPlayerMarker.equals("b"))) {
                    OpposingKingCoords = iRow + "," + iCol;
                    //StdOut.println("Opposing King:" + OpposingKingCoords);
                }
            } // end for col
        }// end for row

        //============================================================================================================
        // TRACE THE PATH OF EVERY ONE OF THE CURR PLAYER`S PIECES
        int iRowSource = 0;
        int iColSource = 0;
        String TypeOfPieceToTrace = "";
        String temp = "";
        String[] ArrAllAccessableSpaces = new String[0];
        String[] ArrAllCapturableSpaces = new String[0];
        String[] ArrPieceAccessableSpaces = new String[0];
        String[] ArrPieceCapturableSpaces = new String[0];

        //StdOut.println("=============== CHECK OPPOSING PLAYER ===================");
        boolean PieceFound = false;
        for (int iRow = 0; iRow < 10; iRow++) {
            for (int iCol = 0; iCol < 10; iCol++) {

                temp = Board[iRow][iCol];
                PieceFound = false;
                // If player is WHITE - search for UPPERCASE Pieces
                if (NextPlayerMarker.equals("w")) {
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        String CompareTemp = (ArrPieces[0][iSearchIndx].toUpperCase());
                        //StdOut.println( BoardTemp + ":" + CompareTemp  );
                        if (temp.equals(CompareTemp)) {
                            iRowSource = iRow;
                            iColSource = iCol;
                            TypeOfPieceToTrace = Board[iRow][iCol];
                            //StdOut.println("***Piece found: " + TypeOfPieceToTrace + " at: " + iRowSource + "," + iColSource);
                            PieceFound = true;
                        }
                    }// end for compare
                }

                // If player is WHITE - search for UPPERCASE Pieces
                if (NextPlayerMarker.equals("b")) {
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        String CompareTemp = (ArrPieces[0][iSearchIndx]);
                        //StdOut.println( BoardTemp + ":" + CompareTemp  );
                        if (temp.equals(CompareTemp)) {
                            iRowSource = iRow;
                            iColSource = iCol;
                            TypeOfPieceToTrace = Board[iRow][iCol];
                            //StdOut.println("***Piece found: " + TypeOfPieceToTrace + " at: " + iRowSource + "," + iColSource);
                            PieceFound = true;
                        }
                    }// end for compare
                }

                // TRACE THE PATH OF EACH APPROPRIATE PIECE
                if (PieceFound) {
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("P")) {
                        // PAWN
                        Piece pawn = new Pawn(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        pawn.TracePath();
                        ArrPieceAccessableSpaces = pawn.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = pawn.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = pawn.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = pawn.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("B")) {
                        // BISHOP
                        Piece bishop = new Bishop(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        bishop.TracePath();
                        ArrPieceAccessableSpaces = bishop.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = bishop.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = bishop.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = bishop.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("R")) {
                        // ROOK
                        Piece rook = new Rook(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        rook.TracePath();
                        ArrPieceAccessableSpaces = rook.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = rook.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = rook.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = rook.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("N")) {
                        // KNIGHT
                        Piece knight = new Knight(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        knight.TracePath();
                        ArrPieceAccessableSpaces = knight.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = knight.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = knight.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = knight.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("Q")) {
                        // QUEEN
                        Piece queen = new Queen(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        queen.TracePath();
                        ArrPieceAccessableSpaces = queen.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = queen.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = queen.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = queen.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("K")) {
                        // KING
                        Piece king = new King(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        king.TracePath();
                        ArrPieceAccessableSpaces = king.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = king.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = king.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = king.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("D")) {
                        // DRUNKEN SOLDIER
                        Piece drunksoldier = new DrunkenSoldier(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        drunksoldier.TracePath();
                        ArrPieceAccessableSpaces = drunksoldier.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = drunksoldier.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = drunksoldier.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = drunksoldier.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("E")) {
                        // ELEPHANT
                        Piece elephant = new Elephant(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        elephant.TracePath();
                        ArrPieceAccessableSpaces = elephant.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = elephant.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = elephant.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = elephant.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("F")) {
                        // FLYING DRAGON
                        Piece flyingdragon = new FlyingDragon(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        flyingdragon.TracePath();
                        ArrPieceAccessableSpaces = flyingdragon.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = flyingdragon.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = flyingdragon.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = flyingdragon.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("W")) {
                        // PRINCESS
                        Piece princess = new Princess(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        princess.TracePath();
                        ArrPieceAccessableSpaces = princess.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = princess.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = princess.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = princess.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("A")) {
                        // AMAZON
                        Piece amazon = new Amazon(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        amazon.TracePath();
                        ArrPieceAccessableSpaces = amazon.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = amazon.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = amazon.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = amazon.getArrAllCapturableSpaces();
                    }
                    //StdOut.println("Array of PIECE Accessable Spaces");
                    for (int x = 0; x <= ArrPieceAccessableSpaces.length - 1; x++) {
                        //StdOut.println(ArrPieceAccessableSpaces[x]);
                    }
                    //StdOut.println("Array of PIECE Capturable Spaces");
                    for (int x = 0; x <= ArrPieceCapturableSpaces.length - 1; x++) {
                        //StdOut.println(ArrPieceCapturableSpaces[x]);
                    }

                    //StdOut.println("Array of ALL Capturable Spaces");
                    for (int x = 0; x <= ArrAllCapturableSpaces.length - 1; x++) {
                        //StdOut.println(ArrAllCapturableSpaces[x]);
                    }
                } // end for col
            }// end for row
        }

//        StdOut.println("****************************************************************");
//        StdOut.println("Array of FINAL ALL Capturable Spaces");
//        for (int x = 0; x <= ArrAllCapturableSpaces.length - 1; x++) {
//            StdOut.println(ArrAllCapturableSpaces[x]);
//        }
//        StdOut.println("****************************************************************");

        boolean OpposingKingInCheck = false;

        for (int iSearchIndx = 0; iSearchIndx <= ArrAllCapturableSpaces.length - 1; iSearchIndx++) {
            String CompareTemp = (ArrAllCapturableSpaces[iSearchIndx]);
            //StdOut.println( BoardTemp + ":" + CompareTemp  );
            if (OpposingKingCoords.equals(CompareTemp)) {
                //StdOut.println("### OPPOSING KING IS IN CHECK ###");
                OpposingKingInCheck = true;
            }
        }// end for compare

        return OpposingKingInCheck;
    }

    // =============================================================================================================
    // ************************************** CHECK - CURRENT PLAYER **********************************************
    //==============================================================================================================
    public static boolean CheckCheckCurrPlayer(String NextPlayerMarker, String[][] Board, String[][] ArrPieces) {

        String OpposingKingCoords = "";

        if (NextPlayerMarker.equals("w")) {
            NextPlayerMarker = "b";
        } else if (NextPlayerMarker.equals("b")) {
            NextPlayerMarker = "w";
        }
        // Locate the Opposing king ==========================
        for (int iRow = 0; iRow < 10; iRow++) {
            for (int iCol = 0; iCol < 10; iCol++) {
                // If player is white - search for black king
                if ((Board[iRow][iCol].equals("k")) && (NextPlayerMarker.equals("w"))) {
                    OpposingKingCoords = iRow + "," + iCol;
                    //StdOut.println("Opposing King:" + OpposingKingCoords);
                }
                if ((Board[iRow][iCol].equals("K")) && (NextPlayerMarker.equals("b"))) {
                    OpposingKingCoords = iRow + "," + iCol;
                    //StdOut.println("Opposing King:" + OpposingKingCoords);
                }
            } // end for col
        }// end for row

        //============================================================================================================
        // TRACE THE PATH OF EVERY ONE OF THE CURR PLAYER`S PIECES
        int iRowSource = 0;
        int iColSource = 0;
        String TypeOfPieceToTrace = "";
        String temp = "";
        String[] ArrAllAccessableSpaces = new String[0];
        String[] ArrAllCapturableSpaces = new String[0];
        String[] ArrPieceAccessableSpaces = new String[0];
        String[] ArrPieceCapturableSpaces = new String[0];

        //StdOut.println("=============== CHECK IF YOU ARE IN CHECK ===================");
        boolean PieceFound = false;
        for (int iRow = 0; iRow < 10; iRow++) {
            for (int iCol = 0; iCol < 10; iCol++) {

                temp = Board[iRow][iCol];
                PieceFound = false;
                // If player is WHITE - search for UPPERCASE Pieces
                if (NextPlayerMarker.equals("w")) {
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        String CompareTemp = (ArrPieces[0][iSearchIndx].toUpperCase());
                        //StdOut.println( BoardTemp + ":" + CompareTemp  );
                        if (temp.equals(CompareTemp)) {
                            iRowSource = iRow;
                            iColSource = iCol;
                            TypeOfPieceToTrace = Board[iRow][iCol];
                            //StdOut.println("***Piece found: " + TypeOfPieceToTrace + " at: " + iRowSource + "," + iColSource);
                            PieceFound = true;
                        }
                    }// end for compare
                }

                // If player is WHITE - search for UPPERCASE Pieces
                if (NextPlayerMarker.equals("b")) {
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        String CompareTemp = (ArrPieces[0][iSearchIndx]);
                        //StdOut.println( BoardTemp + ":" + CompareTemp  );
                        if (temp.equals(CompareTemp)) {
                            iRowSource = iRow;
                            iColSource = iCol;
                            TypeOfPieceToTrace = Board[iRow][iCol];
                            //StdOut.println("***Piece found: " + TypeOfPieceToTrace + " at: " + iRowSource + "," + iColSource);
                            PieceFound = true;
                        }
                    }// end for compare
                }

                // TRACE THE PATH OF EACH APPROPRIATE PIECE
                if (PieceFound) {
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("P")) {
                        // PAWN
                        Piece pawn = new Pawn(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        pawn.TracePath();
                        ArrPieceAccessableSpaces = pawn.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = pawn.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = pawn.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = pawn.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("B")) {
                        // BISHOP
                        Piece bishop = new Bishop(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        bishop.TracePath();
                        ArrPieceAccessableSpaces = bishop.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = bishop.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = bishop.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = bishop.getArrAllCapturableSpaces();

                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("R")) {
                        // ROOK
                        Piece rook = new Rook(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        rook.TracePath();
                        ArrPieceAccessableSpaces = rook.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = rook.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = rook.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = rook.getArrAllCapturableSpaces();

                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("N")) {
                        // KNIGHT
                        Piece knight = new Knight(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        knight.TracePath();
                        ArrPieceAccessableSpaces = knight.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = knight.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = knight.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = knight.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("Q")) {
                        // QUEEN
                        Piece queen = new Queen(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        queen.TracePath();
                        ArrPieceAccessableSpaces = queen.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = queen.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = queen.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = queen.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("K")) {
                        // KING
                        Piece king = new King(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        king.TracePath();
                        ArrPieceAccessableSpaces = king.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = king.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = king.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = king.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("D")) {
                        // DRUNKEN SOLDIER
                        Piece drunksoldier = new DrunkenSoldier(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        drunksoldier.TracePath();
                        ArrPieceAccessableSpaces = drunksoldier.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = drunksoldier.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = drunksoldier.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = drunksoldier.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("E")) {
                        // ELEPHANT
                        Piece elephant = new Elephant(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        elephant.TracePath();
                        ArrPieceAccessableSpaces = elephant.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = elephant.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = elephant.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = elephant.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("F")) {
                        // FLYING DRAGON
                        Piece flyingdragon = new FlyingDragon(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        flyingdragon.TracePath();
                        ArrPieceAccessableSpaces = flyingdragon.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = flyingdragon.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = flyingdragon.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = flyingdragon.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("W")) {
                        // PRINCESS
                        Piece princess = new Princess(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        princess.TracePath();
                        ArrPieceAccessableSpaces = princess.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = princess.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = princess.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = princess.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("A")) {
                        // AMAZON
                        Piece amazon = new Amazon(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        amazon.TracePath();
                        ArrPieceAccessableSpaces = amazon.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = amazon.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = amazon.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = amazon.getArrAllCapturableSpaces();
                    }

//                    StdOut.println("Array of PIECE Accessable Spaces");
//                    for (int x = 0; x <= ArrPieceAccessableSpaces.length - 1; x++) {
//                        StdOut.println(ArrPieceAccessableSpaces[x]);
//                    }
//                    StdOut.println("Array of PIECE Capturable Spaces");
//                    for (int x = 0; x <= ArrPieceCapturableSpaces.length - 1; x++) {
//                        StdOut.println(ArrPieceCapturableSpaces[x]);
//                    }
//                    StdOut.println("Array of ALL Capturable Spaces");
//                    for (int x = 0; x <= ArrAllCapturableSpaces.length - 1; x++) {
//                        StdOut.println(ArrAllCapturableSpaces[x]);
//                    }

                } // end for col
            }// end for row
        }

        //StdOut.println("****************************************************************");
        //StdOut.println("Array of FINAL ALL THREATNING ENEMY Capturable Spaces");
        for (int x = 0; x <= ArrAllCapturableSpaces.length - 1; x++) {
            //StdOut.println(ArrAllCapturableSpaces[x]);
        }
        //StdOut.println("****************************************************************");
        boolean CurrPlayerKingInCheck = false;
        for (int iSearchIndx = 0; iSearchIndx <= ArrAllCapturableSpaces.length - 1; iSearchIndx++) {
            String CompareTemp = (ArrAllCapturableSpaces[iSearchIndx]);
            ////StdOut.println( BoardTemp + ":" + CompareTemp  );
            if (OpposingKingCoords.equals(CompareTemp)) {
                //StdOut.println("### YOUR KING IS IN CHECK ###");
                CurrPlayerKingInCheck = true;
            }
        }// end for compare
        return CurrPlayerKingInCheck;
    }


    // =============================================================================================================
    // ******************************************** CHECKMATE *******************************************************
    //==============================================================================================================
    public static boolean CheckCheckMate(String NextPlayerMarker, String[][] Board, String[][] ArrPieces) {

        // Trace the opposing kings all possible paths and captures
        // Trace all of the currents players paths and captures for all of his pieces
        // Eliminate all of the pieces in the opposing kings possible moveablespaces and capturable spaces if they
        // accessable or capturable by the opposing player
        // If the array of accessable and capturable spaces for the opposing king is 0 (emtpy), he can move nowhere
        // and is thus in checkmate
        // Don`t check castling - can`t castle out of check

        String[] KingArrAllAccessableSpaces = new String[0];
        String[] KingArrAllCapturableSpaces = new String[0];
        String[] KingArrPieceAccessableSpaces = new String[0];
        String[] KingArrPieceCapturableSpaces = new String[0];
        String OpposingKingCoords = "";
        // Locate the Opposing king ==========================
        for (int iRow = 0; iRow < 10; iRow++) {
            for (int iCol = 0; iCol < 10; iCol++) {
                // If player is white - search for black king
                if ((Board[iRow][iCol].equals("k")) && (NextPlayerMarker.equals("w"))) {
                    OpposingKingCoords = iRow + "," + iCol;
                    StdOut.println("Opposing King:" + OpposingKingCoords);
                    Piece king = new King(NextPlayerMarker, iRow, iCol, Board, ArrPieces, KingArrAllAccessableSpaces, KingArrAllCapturableSpaces);
                    king.TracePath();
                    KingArrPieceAccessableSpaces = king.getArrPieceAccessableSpaces();
                    KingArrPieceCapturableSpaces = king.getArrPieceCapturableSpaces();
                    KingArrAllAccessableSpaces = king.getArrAllAccessableSpaces();
                    KingArrAllCapturableSpaces = king.getArrAllCapturableSpaces();
                }
                if ((Board[iRow][iCol].equals("K")) && (NextPlayerMarker.equals("b"))) {
                    OpposingKingCoords = iRow + "," + iCol;
                    StdOut.println("Opposing King:" + OpposingKingCoords);
                    Piece king = new King(NextPlayerMarker, iRow, iCol, Board, ArrPieces, KingArrAllAccessableSpaces, KingArrAllCapturableSpaces);
                    king.TracePath();
                    KingArrPieceAccessableSpaces = king.getArrPieceAccessableSpaces();
                    KingArrPieceCapturableSpaces = king.getArrPieceCapturableSpaces();
                    KingArrAllAccessableSpaces = king.getArrAllAccessableSpaces();
                    KingArrAllCapturableSpaces = king.getArrAllCapturableSpaces();

                }
            } // end for col
        }// end for row

        //============================================================================================================
        // TRACE THE PATH OF EVERY ONE OF THE CURR PLAYER`S PIECES
        int iRowSource = 0;
        int iColSource = 0;
        String TypeOfPieceToTrace = "";
        String temp = "";
        String[] ArrAllAccessableSpaces = new String[0];
        String[] ArrAllCapturableSpaces = new String[0];
        String[] ArrPieceAccessableSpaces = new String[0];
        String[] ArrPieceCapturableSpaces = new String[0];

        //StdOut.println("=============== CHECK OPPOSING PLAYER ===================");
        boolean PieceFound = false;
        for (int iRow = 0; iRow < 10; iRow++) {
            for (int iCol = 0; iCol < 10; iCol++) {

                temp = Board[iRow][iCol];
                PieceFound = false;
                // If player is WHITE - search for UPPERCASE Pieces
                if (NextPlayerMarker.equals("w")) {
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        String CompareTemp = (ArrPieces[0][iSearchIndx].toUpperCase());
                        //StdOut.println( BoardTemp + ":" + CompareTemp  );
                        if (temp.equals(CompareTemp)) {
                            iRowSource = iRow;
                            iColSource = iCol;
                            TypeOfPieceToTrace = Board[iRow][iCol];
                            //StdOut.println("***Piece found: " + TypeOfPieceToTrace + " at: " + iRowSource + "," + iColSource);
                            PieceFound = true;
                        }
                    }// end for compare
                }

                // If player is WHITE - search for UPPERCASE Pieces
                if (NextPlayerMarker.equals("b")) {
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        String CompareTemp = (ArrPieces[0][iSearchIndx]);
                        //StdOut.println( BoardTemp + ":" + CompareTemp  );
                        if (temp.equals(CompareTemp)) {
                            iRowSource = iRow;
                            iColSource = iCol;
                            TypeOfPieceToTrace = Board[iRow][iCol];
                            //StdOut.println("***Piece found: " + TypeOfPieceToTrace + " at: " + iRowSource + "," + iColSource);
                            PieceFound = true;
                        }
                    }// end for compare
                }

                // TRACE THE PATH OF EACH APPROPRIATE PIECE
                if (PieceFound) {
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("P")) {
                        // PAWN
                        Piece pawn = new Pawn(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        pawn.TracePath();
                        ArrPieceAccessableSpaces = pawn.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = pawn.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = pawn.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = pawn.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("B")) {
                        // BISHOP
                        Piece bishop = new Bishop(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        bishop.TracePath();
                        ArrPieceAccessableSpaces = bishop.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = bishop.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = bishop.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = bishop.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("R")) {
                        // ROOK
                        Piece rook = new Rook(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        rook.TracePath();
                        ArrPieceAccessableSpaces = rook.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = rook.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = rook.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = rook.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("N")) {
                        // KNIGHT
                        Piece knight = new Knight(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        knight.TracePath();
                        ArrPieceAccessableSpaces = knight.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = knight.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = knight.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = knight.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("Q")) {
                        // QUEEN
                        Piece queen = new Queen(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        queen.TracePath();
                        ArrPieceAccessableSpaces = queen.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = queen.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = queen.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = queen.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("K")) {
                        // KING
                        Piece king = new King(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        king.TracePath();
                        ArrPieceAccessableSpaces = king.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = king.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = king.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = king.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("D")) {
                        // DRUNKEN SOLDIER
                        Piece drunksoldier = new DrunkenSoldier(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        drunksoldier.TracePath();
                        ArrPieceAccessableSpaces = drunksoldier.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = drunksoldier.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = drunksoldier.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = drunksoldier.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("E")) {
                        // ELEPHANT
                        Piece elephant = new Elephant(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        elephant.TracePath();
                        ArrPieceAccessableSpaces = elephant.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = elephant.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = elephant.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = elephant.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("F")) {
                        // FLYING DRAGON
                        Piece flyingdragon = new FlyingDragon(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        flyingdragon.TracePath();
                        ArrPieceAccessableSpaces = flyingdragon.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = flyingdragon.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = flyingdragon.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = flyingdragon.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("W")) {
                        // PRINCESS
                        Piece princess = new Princess(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        princess.TracePath();
                        ArrPieceAccessableSpaces = princess.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = princess.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = princess.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = princess.getArrAllCapturableSpaces();
                    }
                    // *****************************************************************************************************************
                    if (TypeOfPieceToTrace.toUpperCase().equals("A")) {
                        // AMAZON
                        Piece amazon = new Amazon(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
                        amazon.TracePath();
                        ArrPieceAccessableSpaces = amazon.getArrPieceAccessableSpaces();
                        ArrPieceCapturableSpaces = amazon.getArrPieceCapturableSpaces();
                        ArrAllAccessableSpaces = amazon.getArrAllAccessableSpaces();
                        ArrAllCapturableSpaces = amazon.getArrAllCapturableSpaces();
                    }
                    //StdOut.println("Array of PIECE Accessable Spaces");
                    for (int x = 0; x <= ArrPieceAccessableSpaces.length - 1; x++) {
                        //StdOut.println(ArrPieceAccessableSpaces[x]);
                    }
                    //StdOut.println("Array of PIECE Capturable Spaces");
                    for (int x = 0; x <= ArrPieceCapturableSpaces.length - 1; x++) {
                        //StdOut.println(ArrPieceCapturableSpaces[x]);
                    }

                    //StdOut.println("Array of ALL Capturable Spaces");
                    for (int x = 0; x <= ArrAllCapturableSpaces.length - 1; x++) {
                        //StdOut.println(ArrAllCapturableSpaces[x]);
                    }
                } // end for col
            }// end for row
        }

        // ================================= Curr players Pieces =====================================================
        StdOut.println("****************************************************************");
        StdOut.println("Array of FINAL ALL Capturable Spaces");
        for (int x = 0; x <= ArrAllCapturableSpaces.length - 1; x++) {
            StdOut.println(ArrAllCapturableSpaces[x]);
        }
        StdOut.println("****************************************************************");
        StdOut.println("****************************************************************");
        StdOut.println("Array of FINAL ALL Accessable Spaces");
        for (int x = 0; x <= ArrAllAccessableSpaces.length - 1; x++) {
            StdOut.println(ArrAllAccessableSpaces[x]);
        }
        StdOut.println("****************************************************************");

        // ================================ Oppoisng Players King =====================================================
        StdOut.println("****************************************************************");
        StdOut.println("KING Array of FINAL ALL ACCESSABLE Spaces");
        for (int x = 0; x <= KingArrAllAccessableSpaces.length - 1; x++) {
            StdOut.println(KingArrAllAccessableSpaces[x]);
        }
        StdOut.println("****************************************************************");


        // Convert the array of all accessable spaces by the king to a list
        List<String> list = new ArrayList<>(Arrays.asList(KingArrAllAccessableSpaces));

        // *** Remove all opposing accessable spaces from the King`s available move spaces
        String compare = "";
        int IndexComp;
        for (int i = 0; i < ArrAllAccessableSpaces.length; i++) {
            compare = ArrAllAccessableSpaces[i];
            if (list.contains(compare)) {
                IndexComp = list.indexOf(compare);
                StdOut.println("Match found in lists: " + compare + ", at: " + IndexComp);
                list.remove(IndexComp);
            }
        }

        // *** Remove all opposing capturable spaces from the King`s available move spaces
        for (int i = 0; i < ArrAllCapturableSpaces.length; i++) {
            compare = ArrAllCapturableSpaces[i];
            if (list.contains(compare)) {
                IndexComp = list.indexOf(compare);
                StdOut.println("Match found in lists: " + compare + ", at: " + IndexComp);
                list.remove(IndexComp);
            }
        }

        // Convert the remaining spaces back into an array
        String[] ArrSpacesLeft;
        ArrSpacesLeft = new String[list.size()];
        ArrSpacesLeft = list.toArray(ArrSpacesLeft);

        StdOut.println("****************************************************************");
        StdOut.println("Spaces Still Accessabel by oppossing KING");
        for (int x = 0; x <= ArrSpacesLeft.length - 1; x++) {
            StdOut.println(ArrSpacesLeft[x]);
        }
        StdOut.println("****************************************************************");


        boolean OpposingKingInCheckMate = false;
        if (ArrSpacesLeft.length == 0) {
            StdOut.println("CheckMate");
            OpposingKingInCheckMate = true;
        }

        return OpposingKingInCheckMate;
    }


    // =============================================================================================================
    // ******************************************** SECTION ONE ****************************************************
    //==============================================================================================================
    public static void SectionOne(String line, int LineNum, String[][] ArrPieces) {
        //
        int TempAdd = 0;
        // If end of section - Check that requirements are met
        if (line.equals("-----")) {
            // ============= End of section check that all criteria are met =======================
            //3.2.1 b) If the number of pawns are less than 10 at the first section divider use line of first section divider
            if (!((Integer.parseInt(ArrPieces[1][5]) + Integer.parseInt(ArrPieces[1][6])) == 10)) {
                //System.out.println(Integer.parseInt(ArrPieces[1][5]) + Integer.parseInt(ArrPieces[1][6]));
                BoardValidationErrors.illegalPieceAllocation(LineNum);
            }
            //=====================================================================================
            // 3.2.2. b) Check that there are 1 King and at least 2 Rooks
            if ((!(Integer.parseInt(ArrPieces[1][0]) == 1)) || (!(Integer.parseInt(ArrPieces[1][1]) == 2))) {
                BoardValidationErrors.illegalPieceAllocation(LineNum);
            }

        } else {
            // Not the end of the section - run checks
            // Validate the section divider ===================================
            if (line.length() > 5) {
                // //StdOut.println(line.substring(0, 5));
                if (line.substring(0, 5).equals("-----")) {
                    BoardValidationErrors.illegalBoardDimension();
                }
            }
            int col = line.indexOf(":");
            // if there is no colon the piece is wrong
            if (col == -1) {
                BoardValidationErrors.illegalPieceAllocation(LineNum);
            }
            //==================================================================
            String PieceType = line.substring(0, col);
            ////StdOut.println(PieceType);
            //=========================================================================================
            // Search for piece in array
            // 3.2.1 a) Check to see wheter or not he sum of the pawn and the drunk have been exceeded
            // THROWS EXCEPTION AS SOON AS EXCEEDED
            // //StdOut.println(PieceType);
            int SumDrunkPawn = 0;
            String NumToAdd = line.substring(col + 1, line.length());

            // Check that the number is in fact a number
            try {
                TempAdd = Integer.parseInt(line.substring(col + 1, line.length()));
            } catch (Exception e) {
                BoardValidationErrors.illegalPieceAllocation(LineNum);
            }

            if ((PieceType.equals("p")) || (PieceType.equals("d"))) {
                TempAdd = Integer.parseInt(line.substring(col + 1, line.length()));
                //StdOut.println("TempAdd" + TempAdd);
                for (int i = 0; i < 11; i++) {
                    if ((ArrPieces[0][i].equals("p")) || (ArrPieces[0][i].equals("d"))) {
                        SumDrunkPawn = SumDrunkPawn + Integer.parseInt(ArrPieces[1][i]);
                        ////StdOut.println("SumDP" + SumDrunkPawn);
                    }
                }
                ////StdOut.println("Curr Num DP: " + SumDrunkPawn);
                if (((SumDrunkPawn + TempAdd) > 10)) {
                    BoardValidationErrors.illegalPieceAllocation(LineNum);
                }
            }
            //=========================================================================================
            // Search for piece in array
            // 3.2.2 a) Check to see whether or not the number of officers have been exceeded,
            // if so call an error with the line number - AS SOON AS OFFICERS EXCEEDED
            int NumOfficers = 0;
            // //StdOut.println(PieceType);
            if (!((PieceType.equals("p")) || (PieceType.equals("d")))) {
                TempAdd = Integer.parseInt(line.substring(col + 1, line.length()));
                for (int i = 0; i < 11; i++) {
                    if (!((ArrPieces[0][i].equals("p")) || (ArrPieces[0][i].equals("d")))) {
                        NumOfficers = NumOfficers + Integer.parseInt(ArrPieces[1][i]);
                    }
                }
                ////StdOut.println("Curr Num Officers: " + NumOfficers);
                if (((NumOfficers + TempAdd) > 10)) {
                    BoardValidationErrors.illegalPieceAllocation(LineNum);
                }
            }
            // =====================================================================================================
            // If num of the specified pieces have not been exceeded, add the number to the array

            for (int i = 0; i < 11; i++) {
                // Piece found, add the num up in the array
                if (PieceType.equals(ArrPieces[0][i])) {
                    TempAdd = Integer.parseInt(line.substring(col + 1, line.length()));
                    //System.out.println("TempAdd: " + TempAdd);
                    ArrPieces[1][i] = Integer.toString(Integer.parseInt(ArrPieces[1][i] + TempAdd));
                }
            }

            //======================================================================================================
            // Create an array that is correct according to their formatting
            for (int iSearchIndex = 0; iSearchIndex < 11; iSearchIndex++) {
                if (GlobalArrPiecesInput[1][iSearchIndex].equals("0")) {
                    //StdOut.println(GlobalArrPiecesInput[1][iSearchIndex] + "," + iSearchIndex);
                    //StdOut.println(PieceType + "," + TempAdd);
                    TempAdd = Integer.parseInt(line.substring(col + 1, line.length()));
                    GlobalArrPiecesInput[0][iSearchIndex] = PieceType;
                    GlobalArrPiecesInput[1][iSearchIndex] = Integer.toString(Integer.parseInt(GlobalArrPiecesInput[1][iSearchIndex] + TempAdd));
                    break;
                }
            }
        }
    }

    // =============================================================================================================
    // ******************************************** SECTION TWO ****************************************************
    //==============================================================================================================
    public static void SectionTwo(String line, int LineNum, String[][] Board, int BoardRank, String[][] ArrPieces) {

        // Run end of section checks
        if (line.equals("-----")) {
            char file = ' ';
            int rank = 0;
            // 3.3.4) Checks that the read in board has 10 ranks
            if (!(BoardRank == 10)) {
                BoardValidationErrors.illegalBoardDimension();
            }
            // System.out.println(line + " " + "(" + LineNum + ")" + " EndOfSec_Two");
            //====================================================================================================
            // Check that each color has only one king
            // 3.3.1) Checks that the character is a valid chess piece
            int NumWhiteKingsFound = 0;
            int NumBlackKingsFound = 0;
            for (int iRow = 0; iRow < 10; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    if (Board[iRow][iCol].equals("k")) {
                        NumBlackKingsFound = NumBlackKingsFound + 1;
                    }
                    if (Board[iRow][iCol].equals("K")) {
                        NumWhiteKingsFound = NumWhiteKingsFound + 1;
                    }
                    if ((NumBlackKingsFound > 1) || (NumWhiteKingsFound > 1)) {
                        file = (char) (97 + iCol);
                        rank = (10 - iRow);
                        BoardValidationErrors.officerAllocationExceeded(file, rank);
                    }
                } // end for col
            }// end for row
            //====================================================================================================
            // 3.3.1) Checks that the character is a valid chess piece
            for (int iRow = 0; iRow < 10; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    boolean PieceFound = false;
                    // search for the piece on the board in the array of known pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        // //StdOut.println("Search: " + (Board[iRow][iCol]).toUpperCase());
                        String BoardTemp = (Board[iRow][iCol]).toUpperCase();
                        String CompareTemp = (ArrPieces[0][iSearchIndx]).toUpperCase();
                        ////StdOut.println( BoardTemp + ":" + CompareTemp  );
                        if (BoardTemp.equals(CompareTemp) || (Board[iRow][iCol]).equals(".")) {
                            PieceFound = true;
                        }
                    }// end for compare
                    if (!(PieceFound)) {
                        // //StdOut.println("Piece not found");
                        // //StdOut.println( Character.toString((char)(97 + iCol)) + (10 - iRow));
                        file = (char) (97 + iCol);
                        rank = (10 - iRow);
                        BoardValidationErrors.illegalPiece(file, rank);
                    }
                } // end for col
            }// end for row
            //====================================================================================================
            // 3.3.1) Checks that the character has been allocated
            for (int iRow = 0; iRow < 10; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    // search for the piece on the board in the array of known pieces
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        String BoardTemp = (Board[iRow][iCol]).toUpperCase();
                        String CompareTemp = (ArrPieces[0][iSearchIndx]).toUpperCase();
                        ////StdOut.println( BoardTemp + ":" + CompareTemp  );
                        if (BoardTemp.equals(CompareTemp)) {
                            //Piece on board found in table of allocated pieces
                            //  //StdOut.println( (Board[iRow][iCol]).toUpperCase() + ":" + (ArrPieces[0][iSearchIndx]).toUpperCase()  );
                            if (ArrPieces[1][iSearchIndx].equals("0")) {
                                ////StdOut.println("Not allocated");
                                file = (char) (97 + iCol);
                                rank = (10 - iRow);
                                BoardValidationErrors.illegalPiece(file, rank);
                            }
                        }
                    }// end for compare
                } // end for col
            }// end for row
            //====================================================================================================
            // 3.3.2) Exceeding Pawn and Drunken Pawn allocation
            int iNumPawnAllocated = Integer.valueOf(ArrPieces[1][5]);
            int iNumDrunkAllocated = Integer.valueOf(ArrPieces[1][6]);
            // Lowercase - Black
            int BLACKNumPawnFound = 0;
            int BLACKNumDrunkFound = 0;
            // Uppercase - White
            int WHITENumPawnFound = 0;
            int WHITENumDrunkFound = 0;

            for (int iRow = 0; iRow < 10; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    // Black Pawn
                    if (Board[iRow][iCol].equals("p")) {
                        BLACKNumPawnFound++;
                    }
                    // Black Drunk
                    if (Board[iRow][iCol].equals("d")) {
                        BLACKNumDrunkFound++;
                    }
                    // White Pawn
                    if (Board[iRow][iCol].equals("P")) {
                        WHITENumPawnFound++;
                    }
                    // White Drunk
                    if (Board[iRow][iCol].equals("D")) {
                        WHITENumDrunkFound++;
                    }
                    if ((BLACKNumPawnFound > iNumPawnAllocated) || (BLACKNumDrunkFound > iNumDrunkAllocated) || (WHITENumPawnFound > iNumPawnAllocated) || (WHITENumDrunkFound > iNumDrunkAllocated)) {
                        ////StdOut.println("Too Many P or D");
                        file = (char) (97 + iCol);
                        rank = (10 - iRow);
                        BoardValidationErrors.pawnAllocationExceeded(file, rank);
                    }
                } // end for col
            }// end for row
            //=========================================================================================================
            // 3.3.3) Exceeding Officer Allocation
            // n = i + 10 - (p + d)
            // Officer is any piece that is not a Pawn or a Drunk (10)
            // Lowercase - Black
            int pB = 0; // Black Pawn on board
            int dB = 0; // Black Drunk on board
            // Uppercase - White
            int pW = 0; // White Pawn on board
            int dW = 0; // White Drunk on board
            // Get pB
            for (int iRow = 0; iRow < 10; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    if (Board[iRow][iCol].equals("p")) {
                        pB++;
                    }
                } // end for col
            }// end for row
            // Get dB
            for (int iRow = 0; iRow < 10; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    if (Board[iRow][iCol].equals("d")) {
                        dB++;
                    }
                } // end for col
            }// end for row
            // Get pW
            for (int iRow = 0; iRow < 10; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    if (Board[iRow][iCol].equals("P")) {
                        pW++;
                    }
                } // end for col
            }// end for row
            // Get dW
            for (int iRow = 0; iRow < 10; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    if (Board[iRow][iCol].equals("D")) {
                        dW++;
                    }
                } // end for col
            }// end for row

            // Get n for each officer type, Max num of each officer type
            String[][] ArrNBlack = {
                    {"k", "r", "q", "n", "b", "p", "d", "f", "e", "a", "w"},
                    {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"}
            };
            String[][] ArrNWhite = {
                    {"K", "R", "Q", "N", "B", "P", "D", "F", "E", "A", "W"},
                    {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"}
            };

            // n for Black Officers
            for (int k = 0; k < 11; k++) {
                if (!((ArrPieces[0][k].equals("p")) || (ArrPieces[0][k].equals("d")))) {
                    int tempB = 0;
                    tempB = Integer.parseInt(ArrPieces[1][k]) + 10 - (pB + dB);
                    ArrNBlack[1][k] = Integer.toString(tempB);
                    ////StdOut.println(ArrNBlack[0][k] + ": " + ArrNBlack[1][k]);
                }
            }

            // n for White Officers
            for (int k = 0; k < 11; k++) {
                if (!((ArrPieces[0][k].equals("p")) || (ArrPieces[0][k].equals("d")))) {
                    int tempW = 0;
                    tempW = Integer.parseInt(ArrPieces[1][k]) + 10 - (pW + dW);
                    ArrNWhite[1][k] = Integer.toString(tempW);
                    ////StdOut.println(ArrNWhite[0][k] + ": " + ArrNWhite[1][k]);
                }
            }

            // //StdOut.println("----------------------------------------------------------");
            String[][] numBlackFound = {
                    {"k", "r", "q", "n", "b", "p", "d", "f", "e", "a", "w"},
                    {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"}
            };
            String[][] numWhiteFound = {
                    {"K", "R", "Q", "N", "B", "P", "D", "F", "E", "A", "W"},
                    {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"}
            };

            // Totals =================================================
            int TotalNumBlackOfficersFound = 0;
            int TotalNumWhiteOfficersFound = 0;
            // Get i
            int i = 0; // sum of officers allocated during piece allocation - both same
            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                if (!((ArrPieces[0][iSearchIndx].equals("p")) || (ArrPieces[0][iSearchIndx].equals("d")))) {
                    i = i + Integer.valueOf(ArrPieces[1][iSearchIndx]);
                }
            }
            int nW; // Max Num White officers;
            int nB; // Max Num Black officers
            nB = i + 10 - (pB + dB);
            nW = i + 10 - (pW + dW);
            // ========================================================
            for (int iRow = 0; iRow < 10; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    // search for the piece on the board in the array of known pieces
                    String BoardTemp = (Board[iRow][iCol]);
                    // Find board piece in black or white array and inc the number of that piece
                    for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                        // Black ================================================
                        String CompareTempBlack = (numBlackFound[0][iSearchIndx]);
                        if ((BoardTemp.equals(CompareTempBlack) && (!(BoardTemp.equals("p"))) && (!(BoardTemp.equals("d"))))) {
                            //Piece on board found in table of allocated pieces
                            int tempB = Integer.parseInt(numBlackFound[1][iSearchIndx]) + 1;
                            numBlackFound[1][iSearchIndx] = Integer.toString(tempB);
                            // //StdOut.println(numBlackFound[0][iSearchIndx] + ": " + numBlackFound[1][iSearchIndx]);
                            TotalNumBlackOfficersFound++;
                            ////StdOut.println("TotalNumBlackOfficersFound: " + TotalNumBlackOfficersFound);
                            // Specific
                            if (Integer.parseInt(numBlackFound[1][iSearchIndx]) > Integer.parseInt(ArrNBlack[1][iSearchIndx])) {
                                file = (char) (97 + iCol);
                                rank = (10 - iRow);
                                BoardValidationErrors.officerAllocationExceeded(file, rank);
                            }
                            // Total
                            if (TotalNumBlackOfficersFound > nB) {
                                file = (char) (97 + iCol);
                                rank = (10 - iRow);
                                BoardValidationErrors.officerAllocationExceeded(file, rank);
                            }
                        }
                        // White =================================================
                        String CompareTempWhite = (numWhiteFound[0][iSearchIndx]);
                        if ((BoardTemp.equals(CompareTempWhite) && (!(BoardTemp.equals("P"))) && (!(BoardTemp.equals("D"))))) {
                            //Piece on board found in table of allocated pieces
                            int tempW = Integer.parseInt(numWhiteFound[1][iSearchIndx]) + 1;
                            numWhiteFound[1][iSearchIndx] = Integer.toString(tempW);
                            ////StdOut.println(numWhiteFound[0][iSearchIndx] + ": " + numWhiteFound[1][iSearchIndx]);
                            TotalNumWhiteOfficersFound++;
                            ////StdOut.println("TotalNumWhiteOfficersFound: " + TotalNumWhiteOfficersFound);
                            // Specific
                            if (Integer.parseInt(numWhiteFound[1][iSearchIndx]) > Integer.parseInt(ArrNWhite[1][iSearchIndx])) {
                                file = (char) (97 + iCol);
                                rank = (10 - iRow);
                                BoardValidationErrors.officerAllocationExceeded(file, rank);
                            }
                            // Total
                            if (TotalNumWhiteOfficersFound > nW) {
                                file = (char) (97 + iCol);
                                rank = (10 - iRow);
                                BoardValidationErrors.officerAllocationExceeded(file, rank);
                            }
                        }
                    }// end for compare
                } // end for col
            }// end for row


            //====================================================================================================
            // 3.3.5) Illegal Pawn position
            // First row - Row 0
            for (int iRow = 0; iRow < 1; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    String BoardTemp = (Board[iRow][iCol]).toUpperCase();
                    if ((BoardTemp.equals("P")) || (BoardTemp.equals("D"))) {
                        // Pawn or Drunk found in first row
                        file = (char) (97 + iCol);
                        rank = (10 - iRow);
                        BoardValidationErrors.illegalPawnPosition(file, rank);
                    }
                } // end for col
            }// end for row
            // Last row - Row 9
            for (int iRow = 9; iRow < 10; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    String BoardTemp = (Board[iRow][iCol]).toUpperCase();
                    if ((BoardTemp.equals("P")) || (BoardTemp.equals("D"))) {
                        // Pawn or Drunk found in last row
                        file = (char) (97 + iCol);
                        rank = (10 - iRow);
                        BoardValidationErrors.illegalPawnPosition(file, rank);
                    }
                } // end for col
            }// end for row
            //====================================================================================================
            // 3.3.6) Illegal Elephant Position
            // White (uppercase) elephant found on Black side (10-6)
            for (int iRow = 0; iRow < 5; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    String BoardTemp = (Board[iRow][iCol]);
                    if (BoardTemp.equals("E")) {
                        // Uppercase E found on black side found
                        file = (char) (97 + iCol);
                        rank = (10 - iRow);
                        BoardValidationErrors.illegalElephantPosition(file, rank);
                    }
                } // end for col
            }// end for row

            // Black (lowercase) elephant found on White side (1-5)
            for (int iRow = 5; iRow < 10; iRow++) {
                for (int iCol = 0; iCol < 10; iCol++) {
                    String BoardTemp = (Board[iRow][iCol]);
                    if (BoardTemp.equals("e")) {
                        // Lowercase e found on white side found
                        file = (char) (97 + iCol);
                        rank = (10 - iRow);
                        BoardValidationErrors.illegalElephantPosition(file, rank);
                    }
                } // end for col
            }// end for row
        } else {
            // Else validate the line
            // Validate the section divider ===================================
            ////StdOut.println(line);
            if (line.contains("-")) {
                if (line.length() > 5) {
                    // //StdOut.println(line.substring(0, 5));
                    if (line.contains("------")) {
                        BoardValidationErrors.illegalNextPlayerMarker(LineNum);
                    }
                } else {
                    BoardValidationErrors.illegalBoardDimension();
                }
            }
            //==================================================================
            // Validate that the row is the correct length
            // Read in the row into the 2D array, skipping the spaces,
            // and counting how many characters where found
            //==============================
            // Keeps track of how many non space characters there are in the row
            // this is to avoid rows that are the correct length but just empty spaces
            int NumCharcatersFound = 0;
            int LineLentgh = line.length();
            int SpaceIndx = 0;
            //=============================
            //Space index will always be 1, since the line is copied from the start to the space,
            //trowing the space away, eg: a_b_c becomes b_c after a get read in
            // IF space index = -1 then there is no spaces left, eg: c, thus the are at the last character
            // IF space index = 0 after the character has been copied, there was one too many spaces
            // eg: a__b_c becomes _b_c, with the index of the next space being 0, not 1 as expected

            while (SpaceIndx > -1) {

                if (NumCharcatersFound == 10) {
                    ////StdOut.println("Exceeded");
                    BoardValidationErrors.illegalBoardDimension();
                }
                SpaceIndx = line.indexOf(" ");
                //LineIndx = LineIndx + SpaceIndx;
                // //StdOut.println("Space Index: " + SpaceIndx);
                String Character = "";

                //Too many consecutive spaces
                if (SpaceIndx == 0) {
                    ////StdOut.println(NumCharcatersFound + ":" + LineNum);
                    char file = (char) (97 + NumCharcatersFound);
                    int rank = (10 - BoardRank);
                    BoardValidationErrors.illegalPiece(file, rank);
                }
                // End of line, no more spaces
                if (SpaceIndx == -1) {
                    Character = line;
                    ++NumCharcatersFound;
                    ////StdOut.println("'" + Character + "' " + NumCharcatersFound);
                    if (Character.contains(" ")) {
                        //StdOut.println("Space in " + "'" + Character + "'");
                    }
                    Board[BoardRank][NumCharcatersFound - 1] = Character;
                    line = line.substring(SpaceIndx + 1, line.length());
                } else {
                    Character = line.substring(0, SpaceIndx);
                    ++NumCharcatersFound;
                    Board[BoardRank][NumCharcatersFound - 1] = Character;
                    line = line.substring(SpaceIndx + 1, line.length());
                }
            }
            // If 10 "characters were not found, the board`s number of ranks are incorrect
            ////StdOut.println(NumCharcatersFound);
            if (!(NumCharcatersFound == 10)) {
                BoardValidationErrors.illegalBoardDimension();
            }
            // System.out.println(line + " " + "(" + LineNum + ")" + "Sec2");
        }
    }

    // =============================================================================================================
    // ****************************************** SECTION THREE ****************************************************
    //==============================================================================================================
    public static String SectionThree(String line, int LineNum, String[][] Board, String NextPlayerMarker) {

        String StatusLine = line;
        // Separates the line into the wanted variables ==========================================================
        // Find the next player marker in the string
        int col1 = line.indexOf(":");
        String NextPlayer = line.substring(0, col1);
        ////StdOut.println("Next Player: " + NextPlayer);
        NextPlayerMarker = NextPlayer;
        // Remove next player marker
        line = line.substring(col1 + 1);
        // Find substring containing castling opportunities
        int col2 = line.indexOf(":");
        String CastlingOppertunities = line.substring(0, col2);
        ////StdOut.println("Caslting Oppertunities: " + CastlingOppertunities);

        // Remove Castling oppertunities
        line = line.substring(col2 + 1);
        // Find Halfmove clock
        int col3 = line.indexOf(":");
        String HalfmoveCounter = line.substring(0, col3);
        ////StdOut.println("Halfmove Counter: " + HalfmoveCounter);

        // Remove Halfmove Counter
        line = line.substring(col3 + 1);
        String MoveCounter = line;
        ////StdOut.println("Move Counter: " + MoveCounter);
        // =======================================================================================================
        // 3.4.1) Illegal Next Player
        if (!((NextPlayer.equals("w")) || (NextPlayer.equals("b")))) {
            BoardValidationErrors.illegalNextPlayerMarker(LineNum);
        }
        // =======================================================================================================
        // 3.4.2) Illegal Castling Oppertunities
        // Black Queen Side
        if (CastlingOppertunities.substring(0, 1).equals("+")) {
            ////StdOut.println("Black Queen Side");
            if (!((Board[0][0].equals("r")) && (Board[0][5].equals("k")))) {
                BoardValidationErrors.illegalCastlingOpportunity(LineNum, 0);
            }
        }
        // Black King Side
        if (CastlingOppertunities.substring(1, 2).equals("+")) {
            ////StdOut.println("Black King Side");
            if (!((Board[0][9].equals("r")) && (Board[0][5].equals("k")))) {
                BoardValidationErrors.illegalCastlingOpportunity(LineNum, 1);
            }
        }
        // White Queen Side
        if (CastlingOppertunities.substring(2, 3).equals("+")) {
            ////StdOut.println("White Queen Side");
            if (!((Board[9][0].equals("R")) && (Board[9][5].equals("K")))) {
                BoardValidationErrors.illegalCastlingOpportunity(LineNum, 2);
            }
        }
        // White King Side
        if (CastlingOppertunities.substring(3).equals("+")) {
            ////StdOut.println("White King Side");
            if (!((Board[9][9].equals("R")) && (Board[9][5].equals("K")))) {
                BoardValidationErrors.illegalCastlingOpportunity(LineNum, 3);
            }
        }
        // =======================================================================================================
        // 3.4.3) Illegal Halfmove Counter
        // Test if Halfmove Counter is an int
        try {
            Integer.parseInt(HalfmoveCounter);
        } catch (Exception e) {
            BoardValidationErrors.illegalHalfmoveClock(LineNum);
        }

        int iHalfMove = Integer.parseInt(HalfmoveCounter);
        ////StdOut.println(iHalfMove);

        if (!((iHalfMove >= 0) && (iHalfMove <= 50))) {
            BoardValidationErrors.illegalHalfmoveClock(LineNum);
        }

        // =======================================================================================================
        // 3.4.3) Illegal Move Counter
        // Test if Move Counter is an int
        try {
            Integer.parseInt(MoveCounter);
        } catch (Exception e) {
            BoardValidationErrors.illegalMoveCounter(LineNum);
        }
        int iMove = Integer.parseInt(MoveCounter);
        // //StdOut.println(iMove);
        if (!((iMove >= 0))) {
            BoardValidationErrors.illegalMoveCounter(LineNum);
        }

        // Status line is needed for moving pieces
        return StatusLine;
    }

    // =============================================================================================================
    // ****************************************** PRINT BOARD ******************************************************
    //==============================================================================================================
    public static void PrintBoard(String[][] Board, int LineNum) {
        LineNum = LineNum - 11;
        String PrintLine = "";
        for (int iRow = 0; iRow < 10; iRow++) {
            PrintLine = "";
            for (int iCol = 0; iCol < 10; iCol++) {
                PrintLine = PrintLine + " " + Board[iRow][iCol];
            } // end for col
            System.out.println(PrintLine.substring(1)); // + " " + "(" + LineNum + ")" + "Sec2");
            LineNum++;
        }// end for row
    }


    //==================================================================================================================
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //******************************************* CODE FOR GUI *********************************************************
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //==================================================================================================================

    // =============================================================================================================
    // *************************************** PREFROM ACTION ******************************************************
    //==============================================================================================================
    public static void PreformAction(double MouseX, double MouseY) {

        GlobalAnimation = false;
        // Watch for when the user clicks the start Button
        StdOut.println("Do something: " + MouseX + "," + MouseY);
        //============================================================================================================
        //*********************************
        // User Clicks on the Start Screen
        //*********************************
        if (CurrScreen.equals("Start")) {
            //StdDraw.pause(300);
            LineNum = 1;
            GameState = 1;
            StdOut.println("StartButton");
            ReadInputBoard();
            //ReadMoveFile();
            CurrScreen = "Game";
            DisplayBackroung();
            DrawBoard();
            ShowErrorBox();
            ShowMoveCounters();
            ShowNextPlayer();
            UpdateCastlingDisplay();
            AttackAnimation();

            // ======================================== CLOSE BUTTON =================================================
            // Listens for when the window closes
            StdDraw.frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    ExitGame();
                }
            });
            //=========================================================================================================

            Font font = new Font("Old English Text MT", Font.BOLD, 20);
            StdDraw.setFont(font);
            StdDraw.setPenColor(149, 109, 85);
            //===========================
            // PRINT OUT PIECE ALLOCATIONS
            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                String CompareTemp = (GlobalArrPiecesInput[1][iSearchIndx]);
                ////StdOut.println( BoardTemp + ":" + CompareTemp  );
                if (!(CompareTemp.equals("0"))) {
                    String Line = GlobalArrPiecesInput[0][iSearchIndx] + " : " + GlobalArrPiecesInput[1][iSearchIndx];
                    StdDraw.text(870, 220 + (27 * iSearchIndx), Line);
                }
            }// end for compare
            //==========================
            // Thread to play the music in the background


            Thread t1 = new Thread(new Runnable() {
                public void run() {
                    while (GlobalAnimation == true) {
                        int BUFFER_SIZE = 128000; // audio buffer for 128kb sound
                        AudioInputStream audioStream = null;
                        AudioFormat audioFormat = null;
                        SourceDataLine sourceLine = null; // resets source data
                        //=====================================================
                        // This piece of code uses a try except block to try and construct a filepath to the audio file based
                        // on the given filename
                        try {
                            audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("Background.wav")));
                            // File can`t be located
                        } catch (Exception e) {
                            e.printStackTrace();
                            //System.exit(1);
                            StdOut.println("Audio File not found.");
                        }
                        // Gets the audio format
                        audioFormat = audioStream.getFormat();
                        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                        try {
                            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                            sourceLine.open(audioFormat);
                        } catch (LineUnavailableException e) {
                            e.printStackTrace();
                            //System.exit(1);
                            StdOut.println("Audio File not found.");
                        } catch (Exception e) {
                            e.printStackTrace();
                            //System.exit(1);
                            StdOut.println("Audio File not found.");
                        }
                        //=====================
                        // Plays the audio file
                        sourceLine.start();
                        int nBytesRead = 0;
                        byte[] abData = new byte[BUFFER_SIZE];
                        while (nBytesRead != -1) {
                            try {
                                nBytesRead = audioStream.read(abData, 0, abData.length);

                            } catch (IOException e) {
                                e.printStackTrace();
                                StdOut.println("Audio File not found.");
                            }
                            if (nBytesRead >= 0) {
                                @SuppressWarnings("unused")
                                int nBytesWritten =
                                        sourceLine.write(abData, 0, nBytesRead);
                            }
                        }
                        // Resets
                        sourceLine.drain();
                        sourceLine.close();
                    }
                }
            });
            t1.start();


            //===================================
            AnimationLoop();

        }

        //============================================================================================================
        //*********************************
        // User clicks on the Game Screen
        //********************************
        // HANDELS ALL INPUT FORM GAME SCREEN
        if (CurrScreen.equals("Game")) {
            // StdDraw.pause(300);
            StdOut.println("Clicked on Game Screen");

            // =========================================================================================================
            // 1.)
            ///////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////// CHESS BOARD /////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////
            StdOut.println("Do Import stuff here");
            if (MouseX >= 25 && MouseX <= 725 && MouseY >= 5 && MouseY <= 585) {
                StdOut.println("User clicked on a piece");

                // Figure out the Mouse`s X and Y

                // X-Coordinate:
                String SelectedSquare = "";
                if (MouseX >= 25 && MouseX < 95) {
                    SelectedSquare = SelectedSquare + "a";
                }
                if (MouseX >= 95 && MouseX < 165) {
                    SelectedSquare = SelectedSquare + "b";
                }
                if (MouseX >= 165 && MouseX < 235) {
                    SelectedSquare = SelectedSquare + "c";
                }
                if (MouseX >= 235 && MouseX < 305) {
                    SelectedSquare = SelectedSquare + "d";
                }
                if (MouseX >= 305 && MouseX < 375) {
                    SelectedSquare = SelectedSquare + "e";
                }
                if (MouseX >= 375 && MouseX < 445) {
                    SelectedSquare = SelectedSquare + "f";
                }
                if (MouseX >= 445 && MouseX < 515) {
                    SelectedSquare = SelectedSquare + "g";
                }
                if (MouseX >= 515 && MouseX < 585) {
                    SelectedSquare = SelectedSquare + "h";
                }
                if (MouseX >= 585 && MouseX < 655) {
                    SelectedSquare = SelectedSquare + "i";
                }
                if (MouseX >= 655 && MouseX < 725) {
                    SelectedSquare = SelectedSquare + "j";
                }

                // Y-Coordinate
                if (MouseY >= 5 && MouseY < 63) {
                    SelectedSquare = SelectedSquare + "1";
                }
                if (MouseY >= 63 && MouseY < 121) {
                    SelectedSquare = SelectedSquare + "2";
                }
                if (MouseY >= 121 && MouseY < 179) {
                    SelectedSquare = SelectedSquare + "3";
                }
                if (MouseY >= 179 && MouseY < 237) {
                    SelectedSquare = SelectedSquare + "4";
                }
                if (MouseY >= 237 && MouseY < 295) {
                    SelectedSquare = SelectedSquare + "5";
                }
                if (MouseY >= 295 && MouseY < 353) {
                    SelectedSquare = SelectedSquare + "6";
                }
                if (MouseY >= 353 && MouseY < 411) {
                    SelectedSquare = SelectedSquare + "7";
                }
                if (MouseY >= 411 && MouseY < 469) {
                    SelectedSquare = SelectedSquare + "8";
                }
                if (MouseY >= 469 && MouseY < 527) {
                    SelectedSquare = SelectedSquare + "9";
                }
                if (MouseY >= 527 && MouseY < 585) {
                    SelectedSquare = SelectedSquare + "10";
                }
                StdOut.println("=========================================");
                StdOut.println(SelectedSquare);


                // Game State = 1 : User Selects the Source Piece
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////// SOURCE /////////////////////////////////////////////////////
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
                if (GameState == 1) {
                    StdOut.println("GameState: " + GameState);
                    String Source = SelectedSquare;
                    char temp = Source.charAt(0);
                    int iRowSource = 10 - Integer.parseInt(Source.substring(1));
                    int iColSource = ((int) temp) - 97;
                    String PieceAtSource = Board[iRowSource][iColSource];
                    GUIPieceAtSource = PieceAtSource;
                    //
                    // Wrong square selected
                    // CHECK THAT USER SELECTED ONE OF HIS OWN PIECES =========================
                    // Check that user selected to move an empty square =======================
                    if (PieceAtSource.equals(".")) {
                        StdOut.println("Please select a square with a piece on it");
                    } else {
                        // User chooses a appropriate square  ================================
                        //Check That the user selctes one of his own pieces
                        // Search for a WHITE piece
                        GUIPieceFound = false;
                        if (NextPlayerMarker.equals("w")) {
                            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                                String CompareTemp = (ArrPieces[0][iSearchIndx]).toUpperCase();
                                //StdOut.println( BoardTemp + ":" + CompareTemp  );
                                if (PieceAtSource.equals(CompareTemp)) {
                                    GUIPieceFound = true;
                                    //StdOut.println("Piece Found");
                                }
                            }// end for compare
                        }
                        // Search for a BLACK piece
                        if (NextPlayerMarker.equals("b")) {
                            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                                String CompareTemp = (ArrPieces[0][iSearchIndx]);
                                //StdOut.println( BoardTemp + ":" + CompareTemp  );
                                if (PieceAtSource.equals(CompareTemp)) {
                                    GUIPieceFound = true;
                                    //StdOut.println("Piece Found");
                                }
                            }// end for compare
                        }
                        StdOut.println("Game State: " + GameState);
                        StdOut.println("Source Coorinates: " + iRowSource + ":" + iColSource);
                        StdOut.println("Piece At Source: " + PieceAtSource);
                        StdOut.println("Next Player: " + NextPlayerMarker);
                        BuiltUpLine = SelectedSquare;
                        // Trace Path
                        if (GUIPieceFound) {
                            TracePiecePath(PieceAtSource, iRowSource, iColSource);
                        }
                        //ShowErrorBox();
                        DrawBoard();
                        GameState = 2;
                    }


                } else if (GameState == 2) {
                    StdOut.println("GameState: " + GameState);
                    // Game State = 2 : User Selects the Destination Piece
                    ////////////////////////////////////////////////////////////////////////////////////////////////
                    //////////////////////////////////// Destination ///////////////////////////////////////////////
                    ////////////////////////////////////////////////////////////////////////////////////////////////
                    // Get destination piece
                    String Destination = SelectedSquare;
                    char temp = Destination.charAt(0);
                    int iRowDestination = 10 - Integer.parseInt(Destination.substring(1));
                    int iColDestination = ((int) temp) - 97;
                    String PieceAtDestination = Board[iRowDestination][iColDestination];
                    StdOut.println("Piece At Source:" + GUIPieceAtSource);
                    ShouldPromote = false;
                    // ============================= Execute Move as a promotion ==================================
                    if ((iRowDestination == 0 && (GUIPieceAtSource.equals("P") || GUIPieceAtSource.equals("D"))) || (iRowDestination == 9 && (GUIPieceAtSource.equals("p") || GUIPieceAtSource.equals("d")))) {
                        StdOut.println("PROMOTION:");
                        PromtePieceTo = (String) JOptionPane.showInputDialog(null, "Please enter the Piece you want to promote to (**Uppercase for White, Lowercase for Black)",
                                "Input Board", JOptionPane.QUESTION_MESSAGE, null, null, "");
                        PromtePieceTo = "=" + PromtePieceTo;
                        ShouldPromote = true;
                        //
                    }

                    if (PieceAtDestination.equals(".")) {
                        StdOut.println("MOVE TO SQUARE:");
                        //
                        // ============================= Execute Move as a move ==================================
                        StdOut.println("Game State: " + GameState);
                        StdOut.println("Source Coorinates: " + iRowDestination + ":" + iColDestination);
                        StdOut.println("Piece At Source: " + PieceAtDestination);
                        BuiltUpLine = BuiltUpLine + "-" + SelectedSquare;
                        GameState = 1;
                        // Add Promotion suffix if needed
                        if (ShouldPromote) {
                            BuiltUpLine = BuiltUpLine + PromtePieceTo;
                        }
                        // Adds the check suffix for saving
                        ExecuteMove(BuiltUpLine, NextPlayerMarker, LineNum, Board, LineNum, ArrPieces, GlobalStatusLine);
                        if (OpposingKingInCheck) {
                            BuiltUpLine = BuiltUpLine + "+";
                        }
                        if (GOpposingKingInCheckMate) {
                            BuiltUpLine = BuiltUpLine + "+";
                        }
                        if (IsMoveValid) {
                            ListSavedMoves.add(BuiltUpLine);
                        }
                        // If a player won trigger the end of the game
                        if (IsMoveValid && GOpposingKingInCheckMate) {
                            ExitGame();
                        }

                        // Change Next Player
                        if (NextPlayerMarker.equals("w") && IsMoveValid) {
                            NextPlayerMarker = "b";
                        } else {
                            if (NextPlayerMarker.equals("b") && IsMoveValid) {
                                NextPlayerMarker = "w";
                            }
                        }

                        StdOut.println("////////////");
                        StdOut.println(BuiltUpLine);
                        StdOut.println("////////////");

                        // Save the executed move if game mode is in GUI mode
                        LineNum++;
                        ShowNextPlayer();
                        UpdateCastlingDisplay();
                        // **** Custom message for check
                        if (IsMoveValid && OpposingKingInCheck) {
                            if (NextPlayerMarker.equals("b")) {
                                StdDraw.setPenColor(StdDraw.BLACK);
                                StdDraw.filledRectangle(1020, 635, 70, 25);
                                StdDraw.setPenColor(StdDraw.WHITE);
                                Font font = new Font("Consolas", Font.PLAIN, 14);
                                StdDraw.setFont(font);
                                String temp1 = "Drat !!!";
                                String temp2 = "I am in check";
                                StdDraw.text(1020, 650, temp1);
                                StdDraw.text(1025, 633, temp2);
                            } else if (NextPlayerMarker.equals("w")) {
                                StdDraw.setPenColor(StdDraw.BLACK);
                                StdDraw.filledRectangle(1020, 635, 70, 25);
                                StdDraw.setPenColor(StdDraw.WHITE);
                                Font font = new Font("Consolas", Font.PLAIN, 14);
                                StdDraw.setFont(font);
                                String temp1 = "Mwha ha ha";
                                String temp2 = "You are in check";
                                StdDraw.text(1020, 650, temp1);
                                StdDraw.text(1025, 633, temp2);
                            }
                        }
                        // Piece wasn`t found so user clicked on his own piece, so we switch one extra time so that the curr user plays again
                        StdOut.println("Piece Found: " + GUIPieceFound);
                        // Show generic monester message - not check or error message
                        if (!OpposingKingInCheck && IsMoveValid) {
                            ShowErrorBox();
                        }

                        ShowMoveCounters();
                        DrawBoard();
                        ShowNextPlayer();

                        //
                    } else {
                        StdOut.println("CAPTURE PIECE:");
                        //
                        // ======================== Execute Move as a capture ================================
                        StdOut.println("Game State: " + GameState);
                        StdOut.println("Source Coorinates: " + iRowDestination + ":" + iColDestination);
                        StdOut.println("Piece At Source: " + PieceAtDestination);
                        BuiltUpLine = BuiltUpLine + "x" + SelectedSquare;
                        StdOut.println("Next Player: " + NextPlayerMarker);
                        GameState = 1;
                        ExecuteMove(BuiltUpLine, NextPlayerMarker, LineNum, Board, LineNum, ArrPieces, GlobalStatusLine);
                        // If a player won trigger the end of the game
                        if (OpposingKingInCheck) {
                            BuiltUpLine = BuiltUpLine + "+";
                        }
                        if (GOpposingKingInCheckMate) {
                            BuiltUpLine = BuiltUpLine + "+";
                        }
                        if (IsMoveValid) {
                            ListSavedMoves.add(BuiltUpLine);
                        }
                        // Plays Attack Animation
                        if (IsMoveValid) {
                            AttackAnimation();
                        }
                        // If a player won trigger the end of the game
                        if (IsMoveValid && GOpposingKingInCheckMate) {
                            ExitGame();
                        }
                        // Change Next Player
                        StdOut.println("change 1");
                        if (NextPlayerMarker.equals("w") && IsMoveValid) {
                            NextPlayerMarker = "b";
                        } else {
                            if (NextPlayerMarker.equals("b") && IsMoveValid) {
                                NextPlayerMarker = "w";
                            }
                        }
                        StdOut.println("////////////");
                        StdOut.println(BuiltUpLine);
                        StdOut.println("////////////");
                        LineNum++;
                        ShowNextPlayer();
                        UpdateCastlingDisplay();
                        // **** Custom message for check
                        if (IsMoveValid && OpposingKingInCheck) {
                            if (NextPlayerMarker.equals("b")) {
                                StdDraw.setPenColor(StdDraw.BLACK);
                                StdDraw.filledRectangle(1020, 635, 70, 25);
                                StdDraw.setPenColor(StdDraw.WHITE);
                                Font font = new Font("Consolas", Font.PLAIN, 14);
                                StdDraw.setFont(font);
                                String temp1 = "Drat !!!";
                                String temp2 = "I am in check";
                                StdDraw.text(1020, 650, temp1);
                                StdDraw.text(1025, 633, temp2);
                            } else if (NextPlayerMarker.equals("w")) {
                                StdDraw.setPenColor(StdDraw.BLACK);
                                StdDraw.filledRectangle(1020, 635, 70, 25);
                                StdDraw.setPenColor(StdDraw.WHITE);
                                Font font = new Font("Consolas", Font.PLAIN, 14);
                                StdDraw.setFont(font);
                                String temp1 = "Mwha ha ha";
                                String temp2 = "You are in check";
                                StdDraw.text(1020, 650, temp1);
                                StdDraw.text(1025, 633, temp2);
                            }
                        }
                        // Show generic monester message - not check or error message
                        if (!OpposingKingInCheck && IsMoveValid) {
                            ShowErrorBox();
                        }
                        ShowMoveCounters();
                        DrawBoard();
                        ShowNextPlayer();
                    }

                    //TODO
                    // Check for promotion
                    // if iRowDestination == 0 or 9


                }

                // ====================================================================================================
                // 2.)
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////// TEXT INPUT /////////////////////////////////////////////////
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
            } else if (MouseX >= 655 && MouseX <= 840 && MouseY >= 600 && MouseY <= 640) {
                // User Clicks the Text Input Button
                LineNum++;
                IsMoveValid = true;
                StdOut.println("User clicks on text inout");
                String line = (String) JOptionPane.showInputDialog(null, "Please enter a Move",
                        "Input Board", JOptionPane.QUESTION_MESSAGE, null, null, "");
                Board = ExecuteMove(line, NextPlayerMarker, LineNum, Board, LineNum, ArrPieces, GlobalStatusLine);
                if (OpposingKingInCheck) {
                    line = line + "+";
                }
                if (GOpposingKingInCheckMate) {
                    line = line + "+";
                }
                if (IsMoveValid) {
                    ListSavedMoves.add(line);
                }
                // If a player won trigger the end of the game
                if (IsMoveValid && GOpposingKingInCheckMate) {
                    ExitGame();
                }
                // Change Next Player
                if (NextPlayerMarker.equals("w") && IsMoveValid) {
                    NextPlayerMarker = "b";
                } else {
                    if (NextPlayerMarker.equals("b") && IsMoveValid) {
                        NextPlayerMarker = "w";
                    }
                }
                ShowNextPlayer();
                // ShowErrorBox();
                if (IsMoveValid) {
                    ShowErrorBox();
                    ShowMoveCounters();
                }
                UpdateCastlingDisplay();
                DrawBoard();

                // ====================================================================================================
                // 3.)
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
                ///////////////////////////////////////// SELECT FILE /////////////////////////////////////////////////
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
            } else if (MouseX >= 1045 && MouseX <= 1157 && MouseY >= 490 && MouseY <= 530) {
                // User Clicks on the select file button
                StdOut.println("Select File Button");
                int g = -1;
                //Scanner moveScanner = null;
                while (g == -1) {
                    String MoveFileName = (String) JOptionPane.showInputDialog(null, "Please enter a Move File",
                            "Move File", JOptionPane.QUESTION_MESSAGE, null, null, "valid_move.moves");

                    String moveFilename = MoveFileName;
                    File moveFile = new File(moveFilename);
                    // Initialize the Scanner
                    GuiScanner = null;
                    try {
                        GuiScanner = new Scanner(moveFile);
                        g = 1;
                    } catch (FileNotFoundException e) {
                        // throw new IllegalArgumentException("Board file does not exist");
                    }
                }
                DrawBoard();
                ShowNextPlayer();
                //ShowErrorBox();
                UpdateCastlingDisplay();
                LineNum = 0;

                // ====================================================================================================
                // 4.)
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////// NEXT MOVE //////////////////////////////////////////////////
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
            } else if (MouseX >= 1170 && MouseX <= 1280 && MouseY >= 490 && MouseY <= 530) {
                // User clicks the next move button
                StdOut.println("Next Move Button");
                IsMoveValid = true;
                String line = GuiScanner.nextLine();
                StdOut.println(line);
                // Ignore comment lines
                //

                if (!(GuiScanner.hasNextLine())) {
                    Font font = new Font("Old English Text MT", Font.BOLD, 20);
                    StdDraw.setFont(font);
                    StdDraw.setPenColor(149, 109, 85);
                    String end = "~ End of file ~";
                    StdDraw.text(1160, 470 + (-27 * (LineNum + 1)), end);
                    GuiScanner.close();
                }

                if (line.charAt(0) == '%') {
                    StdOut.println(line);
                } else {
                    Board = ExecuteMove(line, NextPlayerMarker, LineNum, Board, LineNum, ArrPieces, GlobalStatusLine);
                    if (OpposingKingInCheck) {
                        line = line + "+";
                    }
                    if (GOpposingKingInCheckMate) {
                        line = line + "+";
                    }
                    if (IsMoveValid) {
                        ListSavedMoves.add(line);
                    }
                    // If a player won trigger the end of the game
                    if (IsMoveValid && GOpposingKingInCheckMate) {
                        ExitGame();
                    }
                    // Change Next Player
                    if (NextPlayerMarker.equals("w") && IsMoveValid) {
                        NextPlayerMarker = "b";
                    } else {
                        if (NextPlayerMarker.equals("b") && IsMoveValid) {
                            NextPlayerMarker = "w";
                        }
                    }

                    Font font = new Font("Old English Text MT", Font.BOLD, 20);
                    StdDraw.setFont(font);
                    StdDraw.setPenColor(149, 109, 85);
                    StdOut.println(line);
                    StdDraw.text(1160, 470 + (-27 * LineNum), line);
                    LineNum++;
                }

                StdOut.println("-----");
                PrintBoard(Board, LineNum);
                StdOut.println("-----");
                StdOut.println(GlobalStatusLine);
                StdOut.println(NextPlayerMarker);

                ShowNextPlayer();
                UpdateCastlingDisplay();
                if (IsMoveValid) {
                    ShowErrorBox();
                    ShowMoveCounters();
                }
                DrawBoard();

                // ====================================================================================================
                //5.)
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////// CASTLING ///////////////////////////////////////////////////
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
            } else if (MouseX >= 430 && MouseX <= 622 && MouseY >= 598 && MouseY <= 696) {
                StdOut.println("Castling");
                String line = "";
                if (NextPlayerMarker.equals("b")) {
                    if (MouseX >= 430 && MouseX <= 475 && MouseY > 647 && MouseY <= 696) {
                        StdOut.println("Castling - Black Kingside");
                        ExecuteMove("0-0", NextPlayerMarker, LineNum, Board, LineNum, ArrPieces, GlobalStatusLine);
                        line = "0-0";
                        if (OpposingKingInCheck) {
                            line = line + "+";
                        }
                        if (GOpposingKingInCheckMate) {
                            line = line + "+";
                        }
                        if (IsMoveValid) {
                            ListSavedMoves.add(line);
                        }
                        // If a player won trigger the end of the game
                        if (IsMoveValid && GOpposingKingInCheckMate) {
                            ExitGame();
                        }
                    }
                    if (MouseX >= 430 && MouseX <= 475 && MouseY < 647 && MouseY >= 598) {
                        StdOut.println("Castling - Black Gueenside");
                        ExecuteMove("0-0-0", NextPlayerMarker, LineNum, Board, LineNum, ArrPieces, GlobalStatusLine);
                        line = "0-0-0";
                        if (OpposingKingInCheck) {
                            line = line + "+";
                        }
                        if (GOpposingKingInCheckMate) {
                            line = line + "+";
                        }
                        if (IsMoveValid) {
                            ListSavedMoves.add(line);
                        }
                        // If a player won trigger the end of the game
                        if (IsMoveValid && GOpposingKingInCheckMate) {
                            ExitGame();
                        }

                    }
                    // Switch player if successful castling
                    if (IsMoveValid) {
                        NextPlayerMarker = "w";
                    }
                } else {
                    if (NextPlayerMarker.equals("w")) {
                        if (MouseX >= 578 && MouseX <= 622 && MouseY > 647 && MouseY <= 696) {
                            StdOut.println("Castling - White Kinsgide");
                            ExecuteMove("0-0", NextPlayerMarker, LineNum, Board, LineNum, ArrPieces, GlobalStatusLine);
                            line = "0-0";
                            if (OpposingKingInCheck) {
                                line = line + "+";
                            }
                            if (GOpposingKingInCheckMate) {
                                line = line + "+";
                            }
                            if (IsMoveValid) {
                                ListSavedMoves.add(line);
                            }
                            // If a player won trigger the end of the game
                            if (IsMoveValid && GOpposingKingInCheckMate) {
                                ExitGame();
                            }
                        }
                        if (MouseX >= 578 && MouseX <= 622 && MouseY < 647 && MouseY > 598) {
                            StdOut.println("Castling - White Queenside");
                            ExecuteMove("0-0-0", NextPlayerMarker, LineNum, Board, LineNum, ArrPieces, GlobalStatusLine);
                            line = "0-0-0";
                            if (OpposingKingInCheck) {
                                line = line + "+";
                            }
                            if (GOpposingKingInCheckMate) {
                                line = line + "+";
                            }
                            if (IsMoveValid) {
                                ListSavedMoves.add(BuiltUpLine);
                            }
                            // If a player won trigger the end of the game
                            if (IsMoveValid && GOpposingKingInCheckMate) {
                                ExitGame();
                            }
                        }
                        // Switch player if successful castling
                        if (IsMoveValid) {
                            NextPlayerMarker = "b";
                        }
                    }
                }
                // Display updated information
                ShowNextPlayer();
                UpdateCastlingDisplay();
                if (IsMoveValid) {
                    ShowErrorBox();
                    ShowMoveCounters();
                }
                DrawBoard();

                // ====================================================================================================
                //5.)
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
                ///////////////////////////////////// CLICKS ON NOTHING ///////////////////////////////////////////////
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
            } else {
                AttackAnimation();
            }

            GlobalAnimation = true;

            AnimationLoop();

        } else {
            StdOut.println("else");
            GlobalAnimation = true;
            AnimationLoop();
        }


    }


    // =============================================================================================================
    // ************************************* TRACE PIECE PATH ******************************************************
    //==============================================================================================================
    public static void TracePiecePath(String SourcePiece, int iRowSource, int iColSource) {

        // TRACE THE PATH OF THE APPROPRIATE PIECE
        String[] ArrAllAccessableSpaces = new String[0];
        String[] ArrAllCapturableSpaces = new String[0];


        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("P")) {
            // PAWN
            Piece pawn = new Pawn(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            pawn.TracePath();
            GUIArrPieceAccessableSpaces = pawn.getArrPieceAccessableSpaces();
            GUIArrPieceCapturableSpaces = pawn.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("B")) {
            // BISHOP
            Piece bishop = new Bishop(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            bishop.TracePath();
            GUIArrPieceAccessableSpaces = bishop.getArrPieceAccessableSpaces();
            GUIArrPieceCapturableSpaces = bishop.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("R")) {
            // ROOK
            Piece rook = new Rook(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            rook.TracePath();
            GUIArrPieceAccessableSpaces = rook.getArrPieceAccessableSpaces();
            GUIArrPieceCapturableSpaces = rook.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("N")) {
            // KNIGHT
            Piece knight = new Knight(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            knight.TracePath();
            GUIArrPieceAccessableSpaces = knight.getArrPieceAccessableSpaces();
            GUIArrPieceCapturableSpaces = knight.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("Q")) {
            // QUEEN
            Piece queen = new Queen(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            queen.TracePath();
            GUIArrPieceAccessableSpaces = queen.getArrPieceAccessableSpaces();
            GUIArrPieceCapturableSpaces = queen.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("K")) {
            // KING
            Piece king = new King(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            king.TracePath();
            GUIArrPieceAccessableSpaces = king.getArrPieceAccessableSpaces();
            GUIArrPieceCapturableSpaces = king.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("D")) {
            // DRUNKEN SOLDIER
            Piece drunksoldier = new DrunkenSoldier(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            drunksoldier.TracePath();
            GUIArrPieceAccessableSpaces = drunksoldier.getArrPieceAccessableSpaces();
            GUIArrPieceCapturableSpaces = drunksoldier.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("E")) {
            // ELEPHANT
            Piece elephant = new Elephant(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            elephant.TracePath();
            GUIArrPieceAccessableSpaces = elephant.getArrPieceAccessableSpaces();
            GUIArrPieceCapturableSpaces = elephant.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("F")) {
            // FLYING DRAGON
            Piece flyingdragon = new FlyingDragon(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            flyingdragon.TracePath();
            GUIArrPieceAccessableSpaces = flyingdragon.getArrPieceAccessableSpaces();
            GUIArrPieceCapturableSpaces = flyingdragon.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("W")) {
            // PRINCESS
            Piece princess = new Princess(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            princess.TracePath();
            GUIArrPieceAccessableSpaces = princess.getArrPieceAccessableSpaces();
            GUIArrPieceCapturableSpaces = princess.getArrPieceCapturableSpaces();
        }
        // *****************************************************************************************************************
        if (SourcePiece.toUpperCase().equals("A")) {
            // AMAZON
            Piece amazon = new Amazon(NextPlayerMarker, iRowSource, iColSource, Board, ArrPieces, ArrAllAccessableSpaces, ArrAllCapturableSpaces);
            amazon.TracePath();
            GUIArrPieceAccessableSpaces = amazon.getArrPieceAccessableSpaces();
            GUIArrPieceCapturableSpaces = amazon.getArrPieceCapturableSpaces();
        }

        StdOut.println("Array of PIECE Accessable Spaces");
        for (int x = 0; x <= GUIArrPieceAccessableSpaces.length - 1; x++) {
            StdOut.println(GUIArrPieceAccessableSpaces[x]);
        }
        StdOut.println("Array of PIECE Capturable Spaces");
        for (int x = 0; x <= GUIArrPieceCapturableSpaces.length - 1; x++) {
            StdOut.println(GUIArrPieceCapturableSpaces[x]);
        }


    }


    // =============================================================================================================
    // *************************************** ANIMATION LOOP ******************************************************
    //==============================================================================================================
    public static void AnimationLoop() {
        String FilePathName = "";
        StdOut.println(GlobalAnimation);
        StdOut.println(CurrScreen);
        boolean MousePressed = false;
        // ================== START SCREEN ANIMATION LOOP ==================================
        if (CurrScreen.equals("Start")) {
            while (GlobalAnimation == true) {
                int BUFFER_SIZE = 128000; // audio buffer for 128kb sound
                AudioInputStream audioStream = null;
                AudioFormat audioFormat = null;
                SourceDataLine sourceLine = null; // resets source data
                //=====================================================
                // This piece of code uses a try except block to try and construct a filepath to the audio file based
                // on the given filename
                try {
                    audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("Organ.wav")));
                    // File can`t be located
                } catch (Exception e) {
                    e.printStackTrace();
                    //System.exit(1);
                    StdOut.println("Audio File not found.");
                }
                // Gets the audio format
                audioFormat = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                try {
                    sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                    sourceLine.open(audioFormat);
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                    //System.exit(1);
                    StdOut.println("Audio File not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                    //System.exit(1);
                    StdOut.println("Audio File not found.");
                }
                //=====================
                // Plays the audio file
                sourceLine.start();
                int nBytesRead = 0;
                byte[] abData = new byte[BUFFER_SIZE];
                while (nBytesRead != -1) {
                    try {
                        nBytesRead = audioStream.read(abData, 0, abData.length);
                        StdDraw.pause(50);
                        // Whatch for mouse clicks
                        if (StdDraw.isMousePressed()) {
                            PlayClick();
                            //GlobalAnimation = false;
                            MousePressed = true;
                            if (MousePressed == true) {
                                double MouseX = StdDraw.mouseX();
                                double MouseY = StdDraw.mouseY();
                                if ((MouseX >= 0.396 && MouseX <= 0.601) && (MouseY >= 0.334 && MouseY <= 0.372)) {
                                    StdDraw.pause(400);
                                    System.out.println("MousePress");
                                    GlobalAnimation = false;
                                    MousePressed = false;
                                    PreformAction(MouseX, MouseY);
                                    break;
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        StdOut.println("Audio File not found.");
                    }
                    if (nBytesRead >= 0) {
                        @SuppressWarnings("unused")
                        int nBytesWritten =
                                sourceLine.write(abData, 0, nBytesRead);
                    }
                }
                // Resets
                sourceLine.drain();
                sourceLine.close();
            }
        }


        //============================================================================================================

        if (CurrScreen.equals("Game")) {
            while (GlobalAnimation == true) {
                for (int i = 1; i <= 6; i++) {
                    // ====== Interrupts the animation loop so that the users action can be preformed =====
                    if (StdDraw.isMousePressed()) {
                        MousePressed = true;
                        PlayClick();
                        //GlobalAnimation = false;
                        if (MousePressed == true) {
                            double MouseX = StdDraw.mouseX();
                            double MouseY = StdDraw.mouseY();
                            StdDraw.pause(200);
                            System.out.println("MousePress");
                            GlobalAnimation = false;
                            MousePressed = false;
                            PreformAction(MouseX, MouseY);
                            break;
                        }
                    }
                    FilePathName = "Demon" + Integer.toString(i) + ".png";
                    try {
                        StdDraw.enableDoubleBuffering();
                        //StdDraw.setPenColor(Color.blue);
                        StdDraw.setPenColor(39, 38, 56);
                        StdDraw.filledSquare(1000, 780, 100);
                        StdDraw.picture(1000, 780, FilePathName, 290, 220);
                        StdDraw.show();
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

    }


    // =============================================================================================================
    // ********************************************** PLAY CLICK ***************************************************
    //==============================================================================================================
    public static void PlayClick() {
        Thread t2 = new Thread(new Runnable() {
            public void run() {

                int BUFFER_SIZE = 128000; // audio buffer for 128kb sound
                AudioInputStream audioStream = null;
                AudioFormat audioFormat = null;
                SourceDataLine sourceLine = null; // resets source data
                //=====================================================
                // This piece of code uses a try except block to try and construct a filepath to the audio file based
                // on the given filename
                try {
                    audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("Click.wav")));
                    // File can`t be located
                } catch (Exception e) {
                    e.printStackTrace();
                    //System.exit(1);
                    StdOut.println("Audio File not found.");
                }
                // Gets the audio format
                audioFormat = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                try {
                    sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                    sourceLine.open(audioFormat);
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                    //System.exit(1);
                    StdOut.println("Audio File not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                    //System.exit(1);
                    StdOut.println("Audio File not found.");
                }
                //=====================
                // Plays the audio file
                sourceLine.start();
                int nBytesRead = 0;
                byte[] abData = new byte[BUFFER_SIZE];
                while (nBytesRead != -1) {
                    try {
                        nBytesRead = audioStream.read(abData, 0, abData.length);

                    } catch (IOException e) {
                        e.printStackTrace();
                        StdOut.println("Audio File not found.");
                    }
                    if (nBytesRead >= 0) {
                        @SuppressWarnings("unused")
                        int nBytesWritten =
                                sourceLine.write(abData, 0, nBytesRead);
                    }
                }
                // Resets
                sourceLine.drain();
                sourceLine.close();
            }

        });
        t2.start();
    }


    // =============================================================================================================
    // ***************************************** SHOW MOVE COUNTERS ************************************************
    //==============================================================================================================
    public static void ShowMoveCounters() {

        StdDraw.setPenColor(56, 49, 97);
        //StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledRectangle(870, 45, 100, 25);
        // Separate information in status line
        // Separates the line into the wanted variables ==========================================================
        //StdOut.println("========= STATUS LINE ============");
        String Temp = GlobalStatusLine;
        int col1 = Temp.indexOf(":");
        String NextPlayer = Temp.substring(0, col1);
        ////StdOut.println("Next Player: " + NextPlayerMarker);
        // Remove next player marker ========================
        Temp = Temp.substring(col1 + 1);
        // Find substring containing castling opportunities
        int col2 = Temp.indexOf(":");
        String CastlingOppertunities = Temp.substring(0, col2);
        ////StdOut.println("Caslting Oppertunities: " + CastlingOppertunities);
        // Remove Castling oppertunities ===================
        Temp = Temp.substring(col2 + 1);
        // Find Halfmove clock
        int col3 = Temp.indexOf(":");
        String HalfmoveCounter = Temp.substring(0, col3);
        //StdOut.println("Halfmove Counter: " + HalfmoveCounter);
        // Remove Halfmove Counter =========================
        Temp = Temp.substring(col3 + 1);
        String MoveCounter = Temp;


        Font font = new Font("Old English Text MT", Font.PLAIN, 17);
        StdDraw.setFont(font);
        StdDraw.setPenColor(149, 109, 85);
        String line = "Move Counter: " + MoveCounter;
        StdDraw.text(870, 55, line);
        line = "Halfmove Counter: " + HalfmoveCounter;
        StdDraw.text(870, 35, line);


    }


    // =============================================================================================================
    // ************************************** DISPLAY BACKGROUND ***************************************************
    //==============================================================================================================
    public static void DisplayBackroung() {
        GlobalAnimation = false;
        StdOut.println("Curr Screen: " + CurrScreen);
        if (CurrScreen.equals("Start")) {
            StdDraw.setCanvasSize(950, 985);
            StdDraw.setXscale(0, 1);
            StdDraw.setYscale(0, 1);
            StdDraw.enableDoubleBuffering();
            StdDraw.picture(0.5, 0.5, "StartScreen.png", 1, 1);
            StdDraw.show();
            GlobalAnimation = true;
        }
        if (CurrScreen.equals("Game")) {
            StdDraw.setCanvasSize(1300, 980);
            StdDraw.setXscale(0, 1300);
            StdDraw.setYscale(0, 980);
            StdDraw.enableDoubleBuffering();
            StdDraw.picture(650, 490, "GameScreen.png", 1300, 980);
            StdDraw.show();
            GlobalAnimation = true;
        }

    }

    // =============================================================================================================
    // *************************************** ATTACK ANIMATION ****************************************************
    //==============================================================================================================
    public static void AttackAnimation() {
        if (CurrScreen.equals("Game")) {
            for (int i = 1; i <= 18; i++) {
                String FilePathName = "Attack" + Integer.toString(i) + ".png";
                try {
                    StdDraw.enableDoubleBuffering();
                    StdDraw.setPenColor(39, 38, 56);
                    //StdDraw.setPenColor(StdDraw.YELLOW);
                    StdDraw.filledRectangle(1000, 770, 112, 100);
                    StdDraw.picture(1010, 775, FilePathName, 310, 240);
                    StdDraw.show();
                    Thread.sleep(120);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // =============================================================================================================
    // *************************************** CASTLING DISPLAY **************************************************
    //==============================================================================================================
    public static void UpdateCastlingDisplay() {

        // WHIPE OLD BOARD
        //StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenColor(56, 49, 97);
        StdDraw.filledRectangle(448, 651, 20, 48);
        StdDraw.filledRectangle(594, 651, 20, 48);

        // PLACE BEAKERS
        StdOut.println(GlobalStatusLine);
        // Separate information in status line
        // Separates the line into the wanted variables ==========================================================
        //StdOut.println("========= STATUS LINE ============");
        String Temp = GlobalStatusLine;
        int col1 = Temp.indexOf(":");
        String NextPlayer = Temp.substring(0, col1);
        ////StdOut.println("Next Player: " + NextPlayerMarker);
        // Remove next player marker ========================
        Temp = Temp.substring(col1 + 1);
        // Find substring containing castling opportunities
        int col2 = Temp.indexOf(":");
        String CastlingOppertunities = Temp.substring(0, col2);
        ////StdOut.println("Caslting Oppertunities: " + CastlingOppertunities);
        // Remove Castling oppertunities ===================
        Temp = Temp.substring(col2 + 1);
        // Find Halfmove clock
        int col3 = Temp.indexOf(":");
        String HalfmoveCounter = Temp.substring(0, col3);
        //StdOut.println("Halfmove Counter: " + HalfmoveCounter);
        // Remove Halfmove Counter =========================
        Temp = Temp.substring(col3 + 1);
        String MoveCounter = Temp;

        // Black Queenside
        if (CastlingOppertunities.substring(0, 1).equals("+")) {
            StdDraw.picture(448, 674, "BlueBeaker.png", 40, 40);
        } else {
            StdDraw.picture(448, 674, "ClearBeaker.png", 40, 40);
        }
        // Black Kingside
        if (CastlingOppertunities.substring(1, 2).equals("+")) {
            StdDraw.picture(448, 628, "BlueBeaker.png", 40, 40);
        } else {
            StdDraw.picture(448, 628, "ClearBeaker.png", 40, 40);
        }
        // White Kingside
        if (CastlingOppertunities.substring(2, 3).equals("+")) {
            StdDraw.picture(594, 674, "WhiteBeaker.png", 40, 40);
        } else {
            StdDraw.picture(594, 674, "ClearBeaker.png", 40, 40);
        }
        // White Queenside
        if (CastlingOppertunities.substring(3).equals("+")) {
            StdDraw.picture(594, 628, "WhiteBeaker.png", 40, 40);
        } else {
            StdDraw.picture(594, 628, "ClearBeaker.png", 40, 40);
        }


    }


    // =============================================================================================================
    // *************************************** NEXT PLAYER MARKER **************************************************
    //==============================================================================================================
    public static void ShowNextPlayer() {
        if (NextPlayerMarker.equals("b")) {
            // Erase old one
            //StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenColor(39, 38, 56);
            StdDraw.filledSquare(663.8, 678, 10.5);
            StdDraw.filledRectangle(847, 678, 12, 20.5);
            // Draw new one
            StdDraw.picture(660, 718, "SwordLeft.png", 30, 40);
            StdDraw.picture(850, 718, "SwordRight.png", 30, 40);
        }
        if (NextPlayerMarker.equals("w")) {
            // Erase old one
            //StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenColor(39, 38, 56);
            StdDraw.filledSquare(663.8, 718, 10.5);
            StdDraw.filledRectangle(847, 718, 12, 20.5);
            // Draw new one
            StdDraw.picture(660, 675, "SwordLeft.png", 30, 40);
            StdDraw.picture(850, 675, "SwordRight.png", 30, 40);
        }

    }


    // =============================================================================================================
    // *************************************** SHOW ERROR BOX ******************************************************
    //==============================================================================================================
    public static void ShowErrorBox() {
        StdOut.println("Show Error Box");
        StdDraw.picture(985, 635, "ErrorBox.png", 230, 67);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(1020, 635, 70, 25);
        StdDraw.setPenColor(StdDraw.WHITE);
        Font font = new Font("Consolas", Font.PLAIN, 14);
        StdDraw.setFont(font);
        StdDraw.text(1020, 645, "You will never");
        StdDraw.text(1025, 625, "defeat me mortal");
    }


    // =============================================================================================================
    // *************************************** DRAW BOARD **********************************************************
    //==============================================================================================================
    public static void DrawBoard() {
        // ============================ DRAWS SQUARES ON THE BOARD =================================================
        StdDraw.enableDoubleBuffering();
        //StdDraw.setPenColor(39, 38, 56);
        StdDraw.setPenColor(149, 109, 85);
        StdDraw.filledRectangle(370, 295, 354, 294);
        for (int i = 1; i <= 10; i++) {
            // Odd Row
            if (i % 2 > 0) {
                for (int j = 1; j <= 10; j++) {

                    if (j % 2 > 0) {
                        StdDraw.setPenColor(229, 246, 152); // Black

                    } else {
                        StdDraw.setPenColor(21, 100, 93);// White

                    }
                    int yCenter = (i * (-58) + 614);
                    int xCenter = (j * (70) - 15);
                    StdDraw.filledRectangle(xCenter, yCenter, 35, 29);
                }
            }
            // Even Row
            if (i % 2 == 0) {
                for (int j = 1; j <= 10; j++) {
                    if (j % 2 > 0) {
                        StdDraw.setPenColor(21, 100, 93); // White

                    } else {
                        StdDraw.setPenColor(229, 246, 152); // Black

                    }
                    int yCenter = (i * (-58) + 614);
                    int xCenter = (j * (70) - 15);
                    StdDraw.filledRectangle(xCenter, yCenter, 35, 29);
                }
            }
        }

        // =============================== ACCESSABLE SPACES - IF PIECE IS SELECTED ===================================
        if (GUIArrPieceAccessableSpaces.length > 0) {
            if (!(GUIArrPieceAccessableSpaces[0].equals(""))) {
                for (int i = 1; i <= 10; i++) {
                    // Check if in ArrAccessable
                    // Odd Row
                    if (i % 2 > 0) {
                        for (int j = 1; j <= 10; j++) {
                            for (int m = 0; m < GUIArrPieceAccessableSpaces.length; m++) {
                                String currSpace = Integer.toString(i - 1) + "," + Integer.toString(j - 1);

                                if (currSpace.equals(GUIArrPieceAccessableSpaces[m])) {
                                    if (j % 2 > 0) {
                                        StdDraw.setPenColor(164, 222, 2);// White
                                    } else {
                                        StdDraw.setPenColor(76, 154, 42); // Black

                                    }
                                    int yCenter = (i * (-58) + 614);
                                    int xCenter = (j * (70) - 15);
                                    StdDraw.filledRectangle(xCenter, yCenter, 35, 29);
                                }

                            }

                        }
                    }
                    // Even Row
                    if (i % 2 == 0) {
                        for (int j = 1; j <= 10; j++) {
                            for (int m = 0; m < GUIArrPieceAccessableSpaces.length; m++) {
                                String currSpace = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                                if (currSpace.equals(GUIArrPieceAccessableSpaces[m])) {
                                    if (j % 2 > 0) {
                                        StdDraw.setPenColor(76, 154, 42); // Black
                                    } else {

                                        StdDraw.setPenColor(164, 222, 2);// White
                                    }
                                    int yCenter = (i * (-58) + 614);
                                    int xCenter = (j * (70) - 15);
                                    StdDraw.filledRectangle(xCenter, yCenter, 35, 29);
                                }

                            }
                        }
                    }
                }

                // Empty the array so that it isn`t show twice on the board
                for (int i = 0; i < GUIArrPieceAccessableSpaces.length; i++) {
                    GUIArrPieceAccessableSpaces[i] = "";
                }
            }
        }

        // ================================== CAPTURABLE SPACES - IF PIECE IS SELECTED ================================
        if (GUIArrPieceCapturableSpaces.length > 0) {
            if (!(GUIArrPieceCapturableSpaces[0].equals(""))) {
                for (int i = 1; i <= 10; i++) {
                    // Check if in ArrAccessable
                    // Odd Row
                    if (i % 2 > 0) {
                        for (int j = 1; j <= 10; j++) {
                            for (int m = 0; m < GUIArrPieceCapturableSpaces.length; m++) {
                                String currSpace = Integer.toString(i - 1) + "," + Integer.toString(j - 1);

                                if (currSpace.equals(GUIArrPieceCapturableSpaces[m])) {
                                    StdDraw.setPenColor(104, 1, 1);
                                    int yCenter = (i * (-58) + 614);
                                    int xCenter = (j * (70) - 15);
                                    StdDraw.filledRectangle(xCenter, yCenter, 35, 29);
                                }

                            }

                        }
                    }
                    // Even Row
                    if (i % 2 == 0) {
                        for (int j = 1; j <= 10; j++) {
                            for (int m = 0; m < GUIArrPieceCapturableSpaces.length; m++) {
                                String currSpace = Integer.toString(i - 1) + "," + Integer.toString(j - 1);
                                if (currSpace.equals(GUIArrPieceCapturableSpaces[m])) {
                                    StdDraw.setPenColor(104, 1, 1);
                                    int yCenter = (i * (-58) + 614);
                                    int xCenter = (j * (70) - 15);
                                    StdDraw.filledRectangle(xCenter, yCenter, 35, 29);
                                }

                            }
                        }
                    }
                }
                // Empty the array so that it isn`t show twice on the board
                for (int i = 0; i < GUIArrPieceCapturableSpaces.length; i++) {
                    GUIArrPieceCapturableSpaces[i] = "";
                }

            }
        }
        // ========================================= PLACES THE PIECES ================================================
        StdDraw.show();
        // Adds the pieces to the board as specified by the board[][] array
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 9; j++) {
                //StdOut.println(Board[i][j]);
                if (!(Board[i][j].equals("."))) {
                    //StdOut.println(Board[i][j]);
                    int yCenter = (i * (-58) + 614 - 58);
                    int xCenter = (j * (70) - 15 + 70);

                    if (Board[i][j].equals("P")) {
                        StdDraw.picture(xCenter, yCenter, "PawnWhite.png", 55, 55);
                    }
                    if (Board[i][j].equals("p")) {
                        StdDraw.picture(xCenter, yCenter, "PawnBlack.png", 55, 55);
                    }
                    if (Board[i][j].equals("K")) {
                        StdDraw.picture(xCenter, yCenter, "KingWhite.png", 55, 55);
                    }
                    if (Board[i][j].equals("k")) {
                        StdDraw.picture(xCenter, yCenter, "KingBlack.png", 55, 55);
                    }
                    if (Board[i][j].equals("R")) {
                        StdDraw.picture(xCenter, yCenter, "RookWhite.png", 55, 55);
                    }
                    if (Board[i][j].equals("r")) {
                        StdDraw.picture(xCenter, yCenter, "RookBlack.png", 55, 55);
                    }
                    if (Board[i][j].equals("Q")) {
                        StdDraw.picture(xCenter, yCenter, "QueenWhite.png", 55, 55);
                    }
                    if (Board[i][j].equals("q")) {
                        StdDraw.picture(xCenter, yCenter, "QueenBlack.png", 55, 55);
                    }
                    if (Board[i][j].equals("N")) {
                        StdDraw.picture(xCenter, yCenter, "KnightWhite.png", 55, 55);
                    }
                    if (Board[i][j].equals("n")) {
                        StdDraw.picture(xCenter, yCenter, "KnightBlack.png", 55, 55);
                    }
                    if (Board[i][j].equals("B")) {
                        StdDraw.picture(xCenter, yCenter, "BishopWhite.png", 55, 55);
                    }
                    if (Board[i][j].equals("b")) {
                        StdDraw.picture(xCenter, yCenter, "BishopBlack.png", 55, 55);
                    }
                    if (Board[i][j].equals("D")) {
                        StdDraw.picture(xCenter, yCenter, "DrunkWhite.png", 55, 55);
                    }
                    if (Board[i][j].equals("d")) {
                        StdDraw.picture(xCenter, yCenter, "DrunkBlack.png", 55, 55);
                    }
                    if (Board[i][j].equals("F")) {
                        StdDraw.picture(xCenter, yCenter, "DragonWhite.png", 55, 55);
                    }
                    if (Board[i][j].equals("f")) {
                        StdDraw.picture(xCenter, yCenter, "DragonBlack.png", 55, 55);
                    }
                    if (Board[i][j].equals("E")) {
                        StdDraw.picture(xCenter, yCenter, "ElephantWhite.png", 55, 55);
                    }
                    if (Board[i][j].equals("e")) {
                        StdDraw.picture(xCenter, yCenter, "ElephantBlack.png", 55, 55);
                    }
                    if (Board[i][j].equals("A")) {
                        StdDraw.picture(xCenter, yCenter, "AmazonWhite.png", 55, 55);
                    }
                    if (Board[i][j].equals("a")) {
                        StdDraw.picture(xCenter, yCenter, "AmazonBlack.png", 55, 55);
                    }
                    if (Board[i][j].equals("W")) {
                        StdDraw.picture(xCenter, yCenter, "PrincessWhite.png", 55, 55);
                    }
                    if (Board[i][j].equals("w")) {
                        StdDraw.picture(xCenter, yCenter, "PrincessBlack.png", 55, 55);
                    }

                }
            }
        }


    }


    // =============================================================================================================
    // *************************************** SAVE MOVES **********************************************************
    //==============================================================================================================
    public static void SaveMoves() {
        String[] ArrSavedMoves = new String[ListSavedMoves.size()];
        ArrSavedMoves = ListSavedMoves.toArray(ArrSavedMoves);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String DateTime = (dateFormat.format(date));

        StdOut.println("Trying to Saving Board");
        String FileName = "SavedMoves" + Integer.toString(FileNumber) + ".moves";
        try (PrintStream SaveMoveOut = new PrintStream(new FileOutputStream(FileName))) {
            SaveMoveOut.println("%Game played on:" + DateTime);
            for (int i = 0; i < ArrSavedMoves.length; i++) {
                SaveMoveOut.println(ArrSavedMoves[i]);
            }
            SaveMoveOut.close();
            JOptionPane.showMessageDialog(StdDraw.frame,
                    "Successfully saved your moves as: " + FileName, "Save Successfull",
                    JOptionPane.OK_OPTION);

        } catch (Exception e) {

        }
    }

    // =============================================================================================================
    // *************************************** SAVE BOARD **********************************************************
    //==============================================================================================================
    public static void SaveBoard() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String DateTime = (dateFormat.format(date));

        StdOut.println("Trying to Saving Board");
        String FileName = "SavedBoard" + Integer.toString(FileNumber) + ".board";
        try (PrintStream SaveBoardOut = new PrintStream(new FileOutputStream(FileName))) {
            SaveBoardOut.println("%Game played on:" + DateTime);

            // PRINT OUT PIECE ALLOCATIONS
            for (int iSearchIndx = 0; iSearchIndx < 11; iSearchIndx++) {
                String CompareTemp = (GlobalArrPiecesInput[1][iSearchIndx]);
                ////StdOut.println( BoardTemp + ":" + CompareTemp  );
                if (!(CompareTemp.equals("0"))) {
                    String Line = GlobalArrPiecesInput[0][iSearchIndx] + ":" + GlobalArrPiecesInput[1][iSearchIndx];
                    SaveBoardOut.println(Line);
                }
            }// end for compare
            SaveBoardOut.println("-----");
            //==========================
            // PRINT OUT BOARD
            String PrintLine = "";
            for (int iRow = 0; iRow < 10; iRow++) {
                PrintLine = "";
                for (int iCol = 0; iCol < 10; iCol++) {
                    PrintLine = PrintLine + " " + Board[iRow][iCol];
                } // end for col
                SaveBoardOut.println(PrintLine.substring(1)); // + " " + "(" + LineNum + ")" + "Sec2");
            }// end for row
            //==========================
            // PRINT OUT STATUS LINE
            SaveBoardOut.println("-----");
            SaveBoardOut.println(GlobalStatusLine);

            SaveBoardOut.close();
            JOptionPane.showMessageDialog(StdDraw.frame,
                    "Successfully saved your board as: " + FileName, "Save Successfull",
                    JOptionPane.OK_OPTION);

        } catch (Exception e) {

        }
    }

    // =============================================================================================================
    // ******************************************** EXIT GAME ******************************************************
    //==============================================================================================================
    public static void ExitGame() {

        if (GOpposingKingInCheckMate) {
            if (NextPlayerMarker.equals("b")) {
                JOptionPane.showMessageDialog(StdDraw.frame,
                        "Congratulations BLACK you have won !", "Black Wins",
                        JOptionPane.OK_OPTION);
                ListSavedMoves.add("%BLACK wins with a checkmate");
            }

        }
        if (GOpposingKingInCheckMate) {
            if (NextPlayerMarker.equals("w")) {
                JOptionPane.showMessageDialog(StdDraw.frame,
                        "Congratulations WHITE you have won !", "WHITE Wins",
                        JOptionPane.OK_OPTION);
                ListSavedMoves.add("%WHITE wins with a checkmate");
            }


        }


        if (JOptionPane.showConfirmDialog(StdDraw.frame,
                "Do you want to save your game ?", "Save ?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            SaveMoves();
            SaveBoard();
            System.exit(0);
        } else return;

    }
}




