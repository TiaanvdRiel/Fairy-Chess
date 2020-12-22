import java.awt.*;

public class MoveValidationErrors {

    /**
     * To be called in case of 5.2
     *
     * @param line The line number of the illegal move
     */
    public static void illegalMove(int line) {
       StdOut.println("Illegal move");
        if (!(FairyChess.GUI)) {
            System.out.println("ERROR: Illegal move on line " + line);
            System.exit(11);
        } else if (FairyChess.GUI){
            FairyChess.IsMoveValid = false;
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(1020, 635, 70, 25);
            StdDraw.setPenColor(StdDraw.WHITE);
            Font font = new Font("Consolas", Font.PLAIN, 14);
            StdDraw.setFont(font);
            String temp1 = "ERROR: Illegal";
            String temp2 = "move on line " + line ;
            String temp3 = "foolish human";
            StdDraw.text(1020, 650, temp1 );
            StdDraw.text(1025, 633, temp2);
            StdDraw.text(1025, 616, temp3);

        }
    }

    /**
     * To be called in case of 5.3
     *
     * @param line The line number of the illegal capture
     */
    public static void illegalCapture(int line) {
        if (!(FairyChess.GUI)) {
            System.out.println("ERROR: Illegal capture on line " + line);
            System.exit(12);
        } else if (FairyChess.GUI){
            FairyChess.IsMoveValid = false;
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(1020, 635, 70, 25);
            StdDraw.setPenColor(StdDraw.WHITE);
            Font font = new Font("Consolas", Font.PLAIN, 14);
            StdDraw.setFont(font);
            String temp1 = "ERROR: Illegal";
            String temp2 = "capture on line " + line ;
            String temp3 = "foolish human";
            StdDraw.text(1020, 650, temp1 );
            StdDraw.text(1025, 633, temp2);
            StdDraw.text(1025, 616, temp3);
        }

    }

    /**
     * To be called in case of 5.4
     *
     * @param line The line number of the illegal castling move
     */
    public static void illegalCastlingMove(int line) {
        if (!(FairyChess.GUI)) {
            System.out.println("ERROR: Illegal castling move on line " + line);
            System.exit(13);
        } else if (FairyChess.GUI){
            FairyChess.IsMoveValid = false;
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(1020, 635, 70, 25);
            StdDraw.setPenColor(StdDraw.WHITE);
            Font font = new Font("Consolas", Font.PLAIN, 14);
            StdDraw.setFont(font);
            String temp1 = "ERROR: Illegal";
            String temp2 = "castle on line " + line ;
            String temp3 = "foolish human";
            StdDraw.text(1020, 650, temp1 );
            StdDraw.text(1025, 633, temp2);
            StdDraw.text(1025, 616, temp3);
        }

    }

    /**
     * To be called in case of 5.5
     *
     * @param line The line number of the illegal promotion
     */
    public static void illegalPromotion(int line) {
        if (!(FairyChess.GUI)) {
            System.out.println("ERROR: Illegal promotion on line " + line);
            System.exit(14);
        } else if (FairyChess.GUI){
            FairyChess.IsMoveValid = false;
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(1020, 635, 70, 25);
            StdDraw.setPenColor(StdDraw.WHITE);
            Font font = new Font("Consolas", Font.PLAIN, 14);
            StdDraw.setFont(font);
            String temp1 = "ERROR: Illegal";
            String temp2 = "promotion on line " + line ;
            String temp3 = "foolish human";
            StdDraw.text(1020, 650, temp1 );
            StdDraw.text(1025, 633, temp2);
            StdDraw.text(1025, 616, temp3);
        }

    }

    /**
     * To be called in case of 5.6
     *
     * @param line The line number of the move with the illegal check suffix
     */
    public static void illegalCheck(int line) {
        if (!(FairyChess.GUI)) {
            System.out.println("ERROR: Illegal check suffix on line " + line);
            System.exit(15);
        } else if (FairyChess.GUI){
            FairyChess.IsMoveValid = false;
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(1020, 635, 70, 25);
            StdDraw.setPenColor(StdDraw.WHITE);
            Font font = new Font("Consolas", Font.PLAIN, 14);
            StdDraw.setFont(font);
            String temp1 = "ERROR: Illegal";
            String temp2 = "check on line " + line ;
            String temp3 = "foolish human";
            StdDraw.text(1020, 650, temp1 );
            StdDraw.text(1025, 633, temp2);
            StdDraw.text(1025, 616, temp3);
        }

    }

    /**
     * To be called in case of 5.7
     *
     * @param line The line number of the move with the illegal checkmate suffix
     */
    public static void illegalCheckmate(int line) {
        if (!(FairyChess.GUI)) {
            System.out.println("ERROR: Illegal checkmate suffix on line " + line);
            System.exit(16);
        } else if (FairyChess.GUI){
            FairyChess.IsMoveValid = false;
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(1020, 635, 70, 25);
            StdDraw.setPenColor(StdDraw.WHITE);
            Font font = new Font("Consolas", Font.PLAIN, 14);
            StdDraw.setFont(font);
            String temp1 = "ERROR: Illegal";
            String temp2 = "checkmate on line " + line ;
            String temp3 = "foolish human";
            StdDraw.text(1020, 650, temp1 );
            StdDraw.text(1025, 633, temp2);
            StdDraw.text(1025, 616, temp3);
        }

    }
}