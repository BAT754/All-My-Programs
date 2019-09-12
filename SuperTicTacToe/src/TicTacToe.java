// Need these import statements to use the graphics needed for this program
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;


/* In order to use the processing package in your program, you need to add it to the project. You can do this by:
    - Click file in the top left corner.
    - Go to "Project Structure"
    - Click libraries on the left side
    - Click the + symbol near the top, then click "java" in the menu that drops down
    - Navigate through your folders till you find the processing folder
    - Inside the processing folder, navigate to Java/core/library
    - Click on core.jar, and then click add in the bottom right corner.
    - Click OK on the window that pops up, and you should now see "core" listed under the + symbol you clicked earlier
    - Click the final OK button, and now you have added the processing library to your project
*/



public class TicTacToe extends PApplet
{
    int COLS = 3;
    int ROWS = 3;
    int h;
    int w;

    boolean canPlay;
    int symbolBuffer = 15;
    int boardBuffer = 50;

    GridSquare[][] board;

    int playerTurn = 0;    // X goes first
    String gameState;

    PFont MAIN_FONT;
    PFont INFO_FONT;
    PImage[] cats;

    // Winner Variables
    int winX;
    int winY;
    int winType;
    int winner;
    float linePos;
    float lineSpeed;
    int rCat;


    public static void main(String[] args)
    {
        PApplet.main("TicTacToe");
    }

    public void setup()
    {
        w = 100;
        h = 100;

        MAIN_FONT = createFont("Comic Sans MS", 12, false);
        INFO_FONT = createFont("Arial", 12, false);

        gameState = "Play";
        canPlay = true;
        winner = -1;
        winType = -1;
        linePos = 0;
        lineSpeed = 1;
        rCat = (int)random(4);

        board = new GridSquare[ROWS][COLS];

        for (int r = 0; r < ROWS; r++)
        {
            for (int c = 0; c < COLS; c++)
            {
                board[r][c] = new GridSquare(boardBuffer + r*w, boardBuffer + c*h, w, h);
            }
        }

        // Images
        cats = new PImage[5];
        cats[0] = loadImage("images/cat1.png");
        cats[1] = loadImage("images/cat2.png");
        cats[2] = loadImage("images/cat3.png");
        cats[3] = loadImage("images/cat4.png");
        cats[4] = loadImage("images/cat5.jpg");

        // Resize the images
        for (int x = 0; x < 5; x++)
            cats[x].resize(30,30);

    }

    public void settings()
    {
        size(400,400);
    }

    public void draw()
    {
        background(255);
        drawBoard();
        boxUI();


        // Draws the x's and o's
        for (int i = 0; i < ROWS; i++)
        {
            for (int j = 0; j < COLS; j++)
            {
                board[i][j].drawTurn();
            }
        }

        switch (gameState)
        {
            case "Play":
                canPlay = true;
                playerUI();
                break;

            case "Game Over":
                canPlay = false;
                drawWinLine(winX, winY, winType);
                winUI();
                break;
        }
    }

    public void drawBoard()
    {
        // Draws the Board
        strokeWeight(1);
        stroke(0);

        // 2 vertical lines
        line(boardBuffer + w, boardBuffer, boardBuffer + w, boardBuffer + 300);
        line(boardBuffer + 2*w, boardBuffer, boardBuffer + 2*w, boardBuffer + 300);

        // 2 Horizontal Lines
        line(boardBuffer, boardBuffer + h, boardBuffer + 300, boardBuffer + h);
        line(boardBuffer, boardBuffer + 2*h, boardBuffer + 300, boardBuffer + 2*h);
    }

    public void playerUI()
    {
        // Draw the Border
        noFill();
        stroke(0);
        strokeWeight(2);
        rect(25, -5, 350, 45);

        // Text
        textFont(MAIN_FONT, 20);
        fill(50);
        text("Player's Turn: ", 115, 30);

        // Player Symbol
        if (playerTurn == 0)
        {
            stroke(10, 10, 240);
            strokeWeight(3);

            line(260, 10, 280, 30);
            line(260, 30, 280, 10);
        }

        if (playerTurn == 1)
        {
            stroke(245, 10, 10);
            strokeWeight(3);
            noFill();

            ellipse(265, 20, 20,20);
        }
    }

    public void boxUI()
    {
        for (int rCheck = 0; rCheck < ROWS; rCheck++)
        {
            for (int cCheck = 0; cCheck < COLS; cCheck++)
            {
                if (board[rCheck][cCheck].mouseOver())
                {
                    // Draw highlight
                    noStroke();
                    fill(10, 240, 10, 35);
                    rect(board[rCheck][cCheck].x, board[rCheck][cCheck].y, board[rCheck][cCheck].w,board[rCheck][cCheck].h);


                    // Gives info about the square that the mouse is hovering over
                    /*
                    // Draw Info Box
                    fill(0);
                    strokeWeight(1);
                    stroke(0);
                    rect(50, 360, 300, 45);

                    // Draw Info
                    textFont(INFO_FONT, 12);

                    fill(255);
                    text("Box X: " + board[rCheck][cCheck].x, 55, 370);
                    text("Box Y: " + board[rCheck][cCheck].y, 55, 380);
                    text("Box State: " + board[rCheck][cCheck].state, 55, 390);

                    text("Box X Num: " + rCheck, 150, 370);
                    text("Box Y Num: " + cCheck, 150, 380);

                    text("Player Turn: " + playerTurn, 250, 370);
                    */
                }

            }
        }
    }

    public void winUI()
    {
        // Draw the Border
        noFill();
        stroke(0);
        strokeWeight(2);
        rect(25, -5, 350, 45);

        // Text
        textFont(MAIN_FONT, 20);
        fill(50);
        text("Winner: ", 35, 27);

        // Player Symbol
        if (winner == 0)
        {
            stroke(10, 10, 240);
            strokeWeight(3);

            line(120, 10, 140, 30);
            line(120, 30, 140, 10);
        }

        if (winner == 1)
        {
            stroke(245, 10, 10);
            strokeWeight(3);
            noFill();

            ellipse(130, 20, 20,20);
        }

        if (winner == 2)
        {
            image(cats[rCat], 120, 5);
        }

        // REPLAY or QUIT OPTIONS --------------------- //

        // Two Boxes
        stroke(0);
        fill(0);
        strokeWeight(1);
        rect(225, 5, 50, 25);
        rect(295, 5, 50, 25);

        // Text
        textFont(INFO_FONT, 12);
        fill(255);
        text("Replay", 230, 22);
        text("Quit", 308,22);
    }

    public void mousePressed()
    {
        if (canPlay)
        {
            for (int x = 0; x < COLS; x++)
            {
                for (int y = 0; y < ROWS; y++)
                {
                    if (board[x][y].state == -1 && board[x][y].mouseOver())
                    {
                        playerTurn = board[x][y].onClick(mouseX, mouseY, playerTurn);
                        checkWin(x, y, board[x][y].state);
                    }
                }
            }
        }
        else
        {
            // Check if mouse is over Redo button
            if (mouseX > 225 && mouseX < 275 && mouseY > 5 && mouseY < 30)
            {
                resetGame();
            }
            // Check if mouse is over Quit button
            if (mouseX > 295 && mouseX < 345 && mouseY > 5 && mouseY < 30)
            {
                System.exit(0);
            }
        }
    }

    // Resets all the variables so the game start over

    public void resetGame()
    {
        // If the game tied, chose a random person to start the next game.
        // else, the winner gets to start the next game.
        if (winner == 2)
            playerTurn = (int)(random(2));
        else
            playerTurn = winner;


        gameState = "Play";
        canPlay = true;
        winner = -1;
        linePos = 0;
        lineSpeed = 3;
        rCat = (int)random(4);

        for (int r = 0; r < ROWS; r++)
        {
            for (int c = 0; c < COLS; c++)
            {
                board[r][c].state = -1;
            }
        }


    }

    // Algorithm to check with player won
    public void checkWin(int x, int y, int state)
    {
        int colWin = 0;
        int rowWin = 0;
        int d1Win = 0;
        int d2Win = 0;

        for (int next = 0; next < 3; next++)
        {
            if (board[x][next].state == state)
                colWin++;
            if (board[next][y].state == state)
                rowWin++;
            if (board[next][next].state == state)
                d1Win++;
            if (board[next][2 - next].state == state)
                d2Win++;
        }

        if (colWin == 3 || rowWin == 3 || d1Win == 3 || d2Win == 3)
        {
            winType = -1;
            winner = state;
            gameState = "Game Over";

            if (colWin == 3)
                winType = 1;
            else if (rowWin == 3)
                winType = 2;
            else if (d1Win == 3)
                winType = 3;
            else if (d2Win == 3)
                winType = 4;

            winX = x;
            winY = y;
        }

        // If it's a draw
        if (winner == -1 && !spotsLeft())
        {
            winner = 2;
            winType = -1;
            gameState = "Game Over";
        }
    }

    // Checks to see if there are any open spots left.
    public boolean spotsLeft()
    {
        boolean open = false;

        for (int r = 0; r < ROWS; r++)
        {
            for (int c = 0; c < COLS; c++)
            {
                if (board[r][c].state == -1)
                    open = true;
            }
        }

        return open;
    }


    // Draws the line through the winning move
    public void drawWinLine(int x, int y, int winType)
    {
        stroke(10, 240, 10, 150);
        strokeWeight(10);

        switch (winType)
        {
            case 1:
                line(100 + 100*x, 50, 100 + 100*x, 50+linePos);
                break;

            case 2:
                line(50, 100 + 100*y, 50 + linePos, 100 + 100*y);
                break;

            case 3:
                line(50, 50, 50 + linePos, 50 + linePos);
                break;

            case 4:
                line(350, 50, 350 - linePos, 50 + linePos);
                break;
        }

        linePos += lineSpeed;
        lineSpeed += 0.08;

        if (linePos >= 300)
        {
            lineSpeed = 0;
            linePos = 300;
        }
    }

    // Grid square class, which holds the position of the square, and checks to see
    // if the mouse is over it, and knows whether it's an X, O, or empty
    public class GridSquare
    {
        public float x;
        public float y;
        public float w;
        public float h;
        public int state;

        public GridSquare(float x, float y, float w, float h)
        {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            state = -1;
        }

        public void drawTurn()
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

        int onClick(int clickedX, int clickedY, int turn)
        {
            state = turn;

            // Gets next turn
            turn += 1;
            if (turn == 2)
                turn = 0;

            return turn;
        }

        public boolean mouseOver()
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
