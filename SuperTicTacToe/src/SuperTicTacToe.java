import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class SuperTicTacToe extends PApplet
{
    private Board[][] boardArray;
    private PImage[] cats;

    private int playerTurn;
    private int winType;
    private int winner;
    private int winX;
    private int winY;
    private int fade;

    private float lineSpeed;
    private float linePos;

    private boolean spaceLeft;
    private boolean goAnywhere;
    private boolean canClick;
    private String gameState;
    private int xTurn;
    private int yTurn;

    PFont MAIN_FONT;
    PFont INFO_FONT;

    public static void main(String[] args)
    {
        PApplet.main("SuperTicTacToe");
    }
    public void settings()
    {
        size(850, 650);
    }
    public void setup()
    {
        // Setting up the board and grid
        boardArray = new Board[3][3];

        for (int r = 0; r < 3; r++)
        {
            for (int c = 0; c < 3; c++)
            {
                boardArray[r][c] = new Board(35 + r*200, 35 + c*200, 180, 60);
                boardArray[r][c].createGrid();
            }
        }

        // Font Variables
        MAIN_FONT = createFont("Comic Sans MS", 12, false);
        INFO_FONT = createFont("Arial", 12, false);

        // Starting Variables
        gameState = "Play";
        goAnywhere = true;
        spaceLeft = false;
        canClick = true;

        playerTurn = 0;
        fade = 0;

        cats = new PImage[5];

        // Cat array set up
        cats[0] = loadImage("images/cat1.png");
        cats[1] = loadImage("images/cat2.png");
        cats[2] = loadImage("images/cat3.png");
        cats[3] = loadImage("images/cat4.png");
        cats[4] = loadImage("images/cat5.jpg");

        // Resize pictures
        for (int cat = 0; cat < 5; cat++)
        {
            cats[cat].resize(160, 160);

        }
    }

    public void draw()
    {
        background(225);

        drawBoards();

        switch (gameState)
        {
            case "Play":

                showAllowed();
                canClick = true;

                break;

            case "Game Over":

                drawWinLine(winX, winY, winType);
                break;
        }
    }

    private void drawBoards()
    {
        drawMainBoard();

        for (int r = 0; r < 3; r++)
        {
            for (int c = 0; c < 3; c++)
            {
                boardArray[r][c].drawBoard();
            }
        }

        playerUI();
    }

    private void drawMainBoard()
    {
        strokeWeight(5);
        stroke(0);

        // Vertical Bars
        line(225, 25, 225, 625);
        line(425, 25, 425, 625);

        // Horizontal Bars
        line(25, 225, 625, 225);
        line(25, 425, 625, 425);
    }

    public void drawWinLine(int x, int y, int winType)
    {
        stroke(10, 240, 10, 150);
        strokeWeight(25);

        switch (winType)
        {
            case 1:
                line(125 + 200*x, 25, 125 + 200*x, 25+linePos);
                break;

            case 2:
                line(25, 125 + 200*y, 25 + linePos, 125 + 200*y);
                break;

            case 3:
                line(25, 25, 25 + linePos, 25 + linePos);
                break;

            case 4:
                line(625, 25, 625 - linePos, 25 + linePos);
                break;
        }

        linePos += lineSpeed;
        lineSpeed += 0.15;

        if (linePos >= 600)
        {
            lineSpeed = 0;
            linePos = 600;
        }
    }

    private void findWinner(int r, int c, int player)
    {
        if (player == 0 || player == 1)
        {
            int row = 0;
            int col = 0;
            int diag1 = 0;
            int diag2 = 0;

            for (int check = 0; check < 3; check++)
            {
                if (boardArray[r][check].boardState == player)
                    col++;
                if (boardArray[check][c].boardState == player)
                    row++;
                if (boardArray[check][check].boardState == player)
                    diag1++;
                if (boardArray[check][2 - check].boardState == player)
                    diag2++;
            }

            if (row == 3 || col == 3 || diag1 == 3 || diag2 == 3)
            {
                winType = -1;
                winner = player;
                gameState = "Game Over";
                canClick = false;

                if (col == 3)
                    winType = 1;
                else if (row == 3)
                    winType = 2;
                else if (diag1 == 3)
                    winType = 3;
                else if (diag2 == 3)
                    winType = 4;

                winX = r;
                winY = c;
            }
        }

        // Check for Draw
        for (int rr = 0; r < 3; r++)
        {
            for (int cc = 0; c < 3; c++)
            {
                if (boardArray[rr][cc].boardState == -1)
                    spaceLeft = true;
            }
        }

        if (!spaceLeft)
        {
            winner = 2;
            gameState = "Game Over";
            canClick = false;
        }
    }

    public void mousePressed()
    {
        if (canClick)
        {
            for (int r = 0; r < 3; r++)
            {
                for (int c = 0; c < 3; c++)
                {
                    if (boardArray[r][c].mouseOver() && boardArray[r][c].boardState == -1)
                    {
                        if (boardArray[r][c].allowedBoard || goAnywhere)
                        {
                            if (goAnywhere)
                                goAnywhere = false;

                            boardArray[r][c].clicked();

                            // make sure you can only click in the proper board
                            boardArray[r][c].allowedBoard = false;
                            boardArray[xTurn][yTurn].allowedBoard = true;

                            findWinner(r, c, boardArray[r][c].boardState);
                        }
                    }
                }
            }
        }

        // Mouse over restart button
        if (mouseX > 670 && mouseX < 830 && mouseY > 300 && mouseY < 360)
        {
            resetGame();
        }

        // Mouse over quit button
        if (mouseX > 670 && mouseX < 830 && mouseY > 380&& mouseY < 440)
        {
            System.exit(0);
        }
    }

    private void playerUI()
    {
        // Background
        noStroke();
        fill(100);
        rect(650, -5, 201, height + 10);

        // Little Design thing
        strokeWeight(5);
        stroke(75);

        int r = 0;
        while (r*35 + 670 < width)
        {
            int c = 0;
            while (c*35 -5 < height)
            {
                line(660 + r*35, -5 + c*35, 680 + r*35, 15 + c*35);
                line(697 + r*35, -22 + c*35, 677 + r*35, -2 + c*35);
                c += 1;
            }
            r += 1;
        }

        // Dividing Line
        strokeWeight(6);
        stroke(0);
        line(650, 1, 650, height - 1);

        // -------------------- Text -------------------- //
        stroke(50);
        strokeWeight(4);
        fill(200);
        rect(670, 30, 160, 85, 7);
        rect(710, 125, 75, 75, 7);

       switch (gameState)
       {
           case "Play":     // Shows who's turn it is

                fill(25);
                textFont(MAIN_FONT, 35);
                text("Player", 700, 65);
                text("Turn", 707, 103);

                if (playerTurn == 0)    // Draw X
                {
                    stroke(10, 10, 240);
                    strokeWeight(6);
                    line(730, 145, 765, 180);
                    line(765, 145, 730, 180);
                }
                if (playerTurn == 1)   // Draw O
                {
                    stroke(240, 10, 10);
                    strokeWeight(6);
                    noFill();
                    ellipse(747, 162, 40, 40);
                }
                break;

           case "Game Over":    // Shows who the winner is
               fill(25);
               textFont(MAIN_FONT, 38);
               text("Winner!", 680, 85);

               if (winner == 0)     // Draw X
               {
                    stroke(10, 10, 240);
                    strokeWeight(6);
                    line(730, 145, 765, 180);
                    line(765, 145, 730, 180);
               }
               if (winner == 1)     // Draw O
               {
                    stroke(240, 10, 10);
                    strokeWeight(6);
                    noFill();
                    ellipse(747, 162, 40, 40);
               }
               if (winner == 2)
               {
                   noWinner();
               }
               break;

        }   /* switch end */

        // ----------------------- SIDE BUTTONS ----------------------- //
        // Button One ------------- //
        stroke(50);
        strokeWeight(4);
        fill(200);
        rect(670, 300, 160, 60, 7);

        fill(200, 50, 50);
        rect(670, 300, 50, 60, 7);

        strokeWeight(7);
        fill(50);
        line(720, 302, 720, 358);

        textFont(INFO_FONT, 30);
        fill(0);
        text("Reset", 730, 340);


        // Button Two ------------- //
        stroke(50);
        strokeWeight(4);
        fill(200);
        rect(670, 380, 160, 60, 7);

        fill(110, 50, 200);
        rect(670, 380, 50, 60, 7);

        strokeWeight(7);
        fill(50);
        line(720, 382, 720, 438);

        textFont(INFO_FONT, 30);
        fill(0);
        text("Quit", 740, 420);

    }

    private void resetGame()
    {
        if (winner != 1 && winner != 0)
            playerTurn = (int)random(2);
        else
            playerTurn = winner;

        gameState = "Play";
        goAnywhere = true;
        spaceLeft = false;
        canClick = true;
        winner = -1;
        lineSpeed = 4;
        linePos = 0;
        fade = 0;

        for (int r = 0; r < 3; r++)
        {
            for (int c = 0; c < 3; c++)
            {
                boardArray[r][c].resetBoard();
            }
        }
    }

    private void showAllowed()
    {
        int xPos;
        int yPos;
        int size;

        if (boardArray[xTurn][yTurn].boardState != -1)
        {
            goAnywhere = true;
        }

        if (!goAnywhere)
        {
            boardArray[xTurn][yTurn].boxUI();

            xPos = 32 + xTurn*200;
            yPos = 32 + yTurn*200;
            size = 186;

            noFill();
            strokeWeight(10);
            stroke(250, 100, 30, 100);
            rect(xPos, yPos, size, size);
        }
        else
        {
            xPos = 32;
            yPos = 32;
            size = 186;

            for (int r = 0; r < 3; r++)
            {
                for (int c = 0; c < 3; c++)
                {
                    if (boardArray[r][c].boardState == -1)
                    {
                        noFill();
                        strokeWeight(10);
                        stroke(250, 100, 30, 100);
                        rect(xPos + r*200, yPos + c*200, size, size);
                        boardArray[r][c].boxUI();
                    }
                }
            }
        }
    }

    public void keyPressed()
    {
        if (key == 'r')
        {
            gameState = "Game Over";
            winX = 1;
            winY = 1;
            winType = 1;
        }

        for (int r = 0; r < 3; r++)
        {
            for (int c = 0; c < 3; c++)
            {
                if (boardArray[r][c].mouseOver())
                {
                    if (key == 'x')
                        boardArray[r][c].boardState = 0;

                    if (key == 'o')
                        boardArray[r][c].boardState = 1;

                    if (key == 'd')
                        boardArray[r][c].boardState = 2;

                    if (key == 'f')
                        findWinner(r, c, boardArray[r][c].boardState);

                }
            }
        }
    }

    public void noWinner()
    {
        fill(225, fade);
        noStroke();
        rect(0, 0, 645, 645);

        if (fade < 500)
            fade += 5;

        if (fade >= 500)
        {
            cats[4].resize(600, 600);
            image(cats[4], 25, 25);
        }
    }


    // BOARD CLASS ------------------------------------ //
    public class Board
    {
        // Instance Variables
        int xPos;
        int yPos;
        int length;
        int space;
        int boardState;

        // Grid stuff
        private GridSquare[][] board;

        // Overall Variables
        int winner = -1;
        int buffer = 35;
        int size;
        private int rCat;
        boolean allowedBoard = false;

        protected Board(int x, int y, int l, int s)
        {
            xPos = x;
            yPos = y;
            length = l;
            space = s;
            size = 3*space;

            rCat = (int)random(4);
            boardState = -1;
            board = new GridSquare[3][3];
        }

        private void drawBoard()
        {
            switch (boardState)
            {
                case -1:    // Draw Playable Board

                    strokeWeight(1);
                    stroke(0);

                    // Vertical Bars
                    line(xPos + space, yPos, xPos + space, yPos + length);
                    line(xPos + 2 * space, yPos, xPos + 2 * space, yPos + length);

                    // Horizontal Bars
                    line(xPos, yPos + space, xPos + length, yPos + space);
                    line(xPos, yPos + 2 * space, xPos + length, yPos + 2 * space);

                    drawMarks();
                    //boxUI();
                    break;

                case 0:     // Draw X

                    strokeWeight(12);
                    stroke(10, 10, 240);
                    line(xPos + buffer, yPos + buffer, xPos + size - buffer, yPos + size - buffer);
                    line(xPos + size - buffer, yPos + buffer, xPos + buffer, yPos + size - buffer);
                    break;

                case 1:     // Draw O

                    noFill();
                    strokeWeight(12);
                    stroke(240, 10, 10);
                    ellipse(xPos+size/2, yPos+size/2, size-2*buffer, size-2*buffer);

                    break;

                case 2:     // Draw Draw

                    image(cats[rCat], xPos + 10, yPos + 10);

                    break;
            }

        }

        private void boxUI()
        {
            for (int rCheck = 0; rCheck < 3; rCheck++)
            {
                for (int cCheck = 0; cCheck < 3; cCheck++)
                {
                    if (board[rCheck][cCheck].mouseOver())
                    {
                        // Draw highlight
                        noStroke();
                        fill(10, 240, 10, 90);
                        rect(board[rCheck][cCheck].x, board[rCheck][cCheck].y, board[rCheck][cCheck].w, board[rCheck][cCheck].h);
                    }
                }
            }
        }

        private void createGrid()
        {
            for (int r = 0; r < 3; r++)
            {
                for (int c = 0; c < 3; c++)
                {
                    board[r][c] = new GridSquare(xPos + r*space, yPos + c*space, space, space);
                }
            }
        }

        private boolean mouseOver()
        {
            if (mouseX > xPos && mouseX < xPos + 3*space && mouseY > yPos && mouseY < yPos + 3*space)
                return true;
            else
                return false;
        }

        private void clicked()
        {
            for (int x = 0; x < 3; x++)
            {
                for (int y = 0; y < 3; y++)
                {
                    if (board[x][y].state == -1 && board[x][y].mouseOver())
                    {
                        playerTurn = board[x][y].clicked(playerTurn);
                        checkWin(x, y, board[x][y].state);

                        xTurn = x;
                        yTurn = y;
                    }
                }
            }
        }

        private void drawMarks()
        {
            for (int r = 0; r < 3; r++)
            {
                for (int c = 0; c < 3; c++)
                {
                    board[r][c].drawTurn();
                }
            }
        }

        private void checkWin(int x, int y, int player)
        {
            int colWin = 0;
            int rowWin = 0;
            int d1Win = 0;
            int d2Win = 0;

            for (int next = 0; next < 3; next++)
            {
                if (board[x][next].state == player)
                    colWin++;
                if (board[next][y].state == player)
                    rowWin++;
                if (board[next][next].state == player)
                    d1Win++;
                if (board[next][2 - next].state == player)
                    d2Win++;
            }

            if (colWin == 3 || rowWin == 3 || d1Win == 3 || d2Win == 3)
            {
               winner = player;
               boardState = winner;
            }


            // If it's a draw
            if (winner == -1 && !spotsLeft())
            {
                winner = 2;
                boardState = 2;
            }
        }

        private void resetBoard()
        {
            allowedBoard = false;
            boardState = -1;
            winner = -1;
            rCat = (int)random(4);

            for (int r = 0; r < 3; r++)
            {
                for (int c = 0; c < 3; c++)
                {
                    board[r][c].state = -1;
                }
            }
        }

        private boolean spotsLeft()
        {
            boolean open = false;

            for (int r = 0; r < 3; r++)
            {
                for (int c = 0; c < 3; c++)
                {
                    if (board[r][c].state == -1)
                        open = true;
                }
            }

            return open;
        }
    }

    // GRID SQUARE CLASS ------------------------------ //
    public class GridSquare
    {
        private float x;
        private float y;
        private float w;
        private float h;
        private int state;
        int symbolBuffer = 15;

        public GridSquare(float x, float y, float w, float h)
        {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            state = -1;
        }

        private void drawTurn()
        {
            if (state == 0)
            {
                // Draw X
                strokeWeight(5);
                stroke(10, 10, 240);
                line(x+symbolBuffer, y+symbolBuffer, x+w-symbolBuffer, y+h-symbolBuffer);
                line(x+w-symbolBuffer, y+symbolBuffer, x+symbolBuffer, y+h-symbolBuffer);
            }

            if (state == 1)
            {
                // Draw O
                noFill();
                strokeWeight(5);
                stroke(240, 10, 10);
                ellipse(x+w/2, y+h/2, w-2*symbolBuffer, h-2*symbolBuffer);
            }
        }

        private int clicked(int turn)
        {
            state = turn;

            // Gets next turn
            turn += 1;
            if (turn == 2)
                turn = 0;

            return turn;
        }

        private boolean mouseOver()
        {
            if (mouseX > x && mouseY > y && mouseX < x+w && mouseY < y+h)
            {
                return true;
            }
            else
                return false;
        }
    }
}
