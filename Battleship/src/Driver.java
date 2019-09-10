import processing.core.PApplet;

public class Driver extends PApplet
{

    Grid[] grids;
    Ship[][] ships;

    enum GameState
    {
        SETUP, PLAYER1TURN, PLAYER2TURN, GETREADY
    }
    GameState state;

    int playerTurn;

    int mouseHoverX;
    int mouseHoverY;

    boolean goodClick;
    boolean showingConfirmButton = false;

    public static void main(String[] args)
    {
        PApplet.main("Driver");
    }

    public void settings()
    {
        size(1200, 750);
    }

    public void setup()
    {
        // Creates the grids
        grids = new Grid[2];

        grids[0] = new Grid(60, 40);
        grids[1] = new Grid(640, 40);

        // Creates the ships
        ships = new Ship[2][5];

        for (int x = 0; x < 2; x++)
        {
            for (int y = 0; y < 5; y++)
            {
                ships[x][y] = new Ship(y+1, x);
            }
        }

        // General Variable Set up
        state = GameState.SETUP;
        playerTurn = 0;
        mouseHoverX = -1;
        mouseHoverY = -1;

    }

    public void draw()
    {
        background(255, 255, 255);

        for (Grid grid : grids)
        {
            grid.drawGrid();
        }



        switch (state)
        {
            case SETUP:
                shipSelectMenu();

                // Draws Highlights for the board based on the player's turn
                for (int x = 0; x < 10; x++)
                {
                    for (int y = 0; y < 10; y++)
                    {
                        if (grids[playerTurn].gridZone[x][y].mouseOver())
                        {
                            grids[playerTurn].gridZone[x][y].highlight = true;
                            mouseHoverX = x;
                            mouseHoverY = y;
                        }
                        else
                        {
                            grids[playerTurn].gridZone[x][y].highlight = false;
                        }
                    }
                }

                for (int x = 0; x < 5; x++)
                {
                    if (ships[playerTurn][x].mini.selected)
                    {
                        ships[playerTurn][x].drawGhost();
                    }

                    if (ships[playerTurn][x].placed)
                    {
                        ships[playerTurn][x].drawShip();
                    }
                }


                break;

            case GETREADY:

                stroke(0);
                strokeWeight(3);
                fill(140, 209, 255);
                rect(530, 620, 150, 70, 10);

                fill(0);
                text("Ready", 580, 660);

                break;

            case PLAYER1TURN:


                break;
        }

    }

    private void shipSelectMenu()
    {
        strokeWeight(4);
        stroke(0);
        fill(150);
        rect(80, 580, 1060, 150, 10);

        for (int x = 0; x < 5; x++)
        {
            ships[playerTurn][x].drawMini();
        }

        int number = 0;
        for (int x = 0; x < 5; x++)
        {
            if (ships[playerTurn][x].placed)
                number += 1;

        }
        if (number == 5)
        {
            drawConfirmButton();
            showingConfirmButton = true;
        }
        else
            showingConfirmButton = false;


    }

    private void drawConfirmButton()
    {
        stroke(0);
        strokeWeight(3);
        fill(140, 209, 255);
        rect(700, 620, 150, 70, 10);

        fill(0);
        text("Confirm", 740, 650);
    }

    public void keyPressed()
    {
        switch (state)
        {
            case SETUP:

                for (int x = 0; x < 5; x++)
                {
                    if (ships[playerTurn][x].mini.selected)
                    {
                        if (key == 'd')
                        {
                            ships[playerTurn][x].direction += 1;

                            if (ships[playerTurn][x].direction > 1)
                                ships[playerTurn][x].direction = 0;
                        }
                    }
                }

                break;
        }
    }

    public void mousePressed()
    {
        switch (state)
        {
            case SETUP:

                for (int x = 0; x < 5; x++)
                {
                    if (grids[playerTurn].mouseOver())
                    {
                        if (ships[playerTurn][x].mini.selected && ships[playerTurn][x].canFit())
                        {
                            ships[playerTurn][x].xPos = grids[playerTurn].gridZone[mouseHoverX][mouseHoverY].xPos;
                            System.out.println(mouseHoverX);
                            ships[playerTurn][x].yPos = grids[playerTurn].gridZone[mouseHoverX][mouseHoverY].yPos;
                            System.out.println(mouseHoverY);
                            ships[playerTurn][x].mini.selected = false;
                            ships[playerTurn][x].placed = true;
                            ships[playerTurn][x].mini.placed = true;
                            ships[playerTurn][x].mini.c1 = 120;
                            ships[playerTurn][x].mini.c2 = 120;
                            ships[playerTurn][x].mini.c3 = 120;

                            grids[playerTurn].gridZone[mouseHoverX][mouseHoverY].highlight = false;
                            mouseHoverX = -1;
                            mouseHoverY = -1;
                        }
                    }
                    else
                    {
                        if (ships[playerTurn][x].mini.mouseOver())
                        {
                            ships[playerTurn][x].mini.c1 = 65;
                            ships[playerTurn][x].mini.c2 = 201;
                            ships[playerTurn][x].mini.c3 = 2;
                            ships[playerTurn][x].mini.selected = true;
                            ships[playerTurn][x].placed = false;
                        }
                        else
                        {
                            if (!ships[playerTurn][x].mini.placed)
                            {
                                ships[playerTurn][x].mini.c1 = 200;
                                ships[playerTurn][x].mini.c2 = 200;
                                ships[playerTurn][x].mini.c3 = 200;
                                ships[playerTurn][x].mini.selected = false;
                                mouseHoverX = -1;
                                mouseHoverY = -1;
                            }
                        }
                    }
                }

                if (showingConfirmButton)
                {
                    if (mouseX > 700 && mouseY >  620 && mouseX < 850 && mouseY < 690)
                    {
                        playerTurn += 1;

                        if (playerTurn >= 2)
                        {
                            state = GameState.GETREADY;
                        }
                    }
                }
                break;

            case GETREADY:

                if (mouseX > 530 && mouseY > 620 && mouseX < 680 && mouseY < 690)
                {
                    state = GameState.PLAYER1TURN;
                }

            case PLAYER1TURN:

                break;
        }
    }



    //  ----------------------  Classes  -----------------------------  //

    public class Grid
    {

        int xPos;
        int yPos;
        GridSquare[][] gridZone;

        Grid(int xPos, int yPos)
        {

            this.xPos = xPos;
            this.yPos = yPos;
            gridZone = new GridSquare[10][10];

            for (int x = 0; x < 10; x++)
            {
                for (int y = 0; y < 10; y++)
                {
                    gridZone[x][y] = new GridSquare(this.xPos + (50*x), this.yPos + (50*y));
                }
            }
        }

        private void drawGrid()
        {
            fill(44, 231, 245);
            stroke(0);
            strokeWeight(2);
            rect(xPos - 20, yPos - 20, 540, 540, 10);

            for (int x = 0; x < 10; x++)
            {
                for (int y = 0; y < 10; y++)
                {
                    gridZone[x][y].drawGridSquare();
                }
            }
        }

        private boolean mouseOver()
        {
            return (mouseX > xPos && mouseY > yPos && mouseX < xPos + 500 && mouseY < yPos + 500);
        }
    }

    public class GridSquare
    {
        int type;
        int xPos;
        int yPos;
        int size = 50;

        boolean highlight = false;


        GridSquare(int xPos, int yPos)
        {
            this.xPos = xPos;
            this.yPos = yPos;
            type = 0;
        }

        private void drawGridSquare()
        {
            if (highlight)
                fill(29, 109, 173, 100);
            else
                noFill();

            stroke(5, 157, 168, 50);
            strokeWeight(1);
            rect(xPos, yPos, size, size, 3);
        }

        private boolean mouseOver()
        {
            if (mouseX > xPos && mouseX < xPos + size && mouseY > yPos && mouseY < yPos + size)
            {
                fill(0);
                text(type, 20, 20);
                return true;
            }
            return false;
        }

    }

    public class Ship
    {
        int size;
        int direction;
        int hits;
        int player;

        int xPos;
        int yPos;

        ShipMini mini;

        boolean placed;

        Ship(int size, int player)
        {
            this.size = size;
            this.player = player;
            direction = 0;
            xPos = -1;
            yPos = -1;

            mini = new ShipMini(this, 90 + 60*(size-1), 590);

        }

        private void drawShip()
        {
            ellipseMode(CORNER);
            stroke(0);
            strokeWeight(2);

            if (player == 0)
                fill(180, 0, 0);
            else
                fill(34, 240, 89);

            if (direction == 0)
                ellipse(xPos+5, yPos+5, 40, 40 + (size - 1)*50);
            else
                ellipse(xPos+5, yPos+5, 40 + (size - 1)*50,40);

        }

        private void drawGhost()
        {
            ellipseMode(CORNER);
            stroke(0, 150);
            strokeWeight(2);

            if (player == 0)
                fill(180, 0, 0, 150);
            else
                fill(34, 240, 89, 150);

            if (mouseHoverX != -1 && mouseHoverY != -1 && canFit())
            {

                int xVar = grids[playerTurn].gridZone[mouseHoverX][mouseHoverY].xPos;
                int yVar = grids[playerTurn].gridZone[mouseHoverX][mouseHoverY].yPos;

                if (direction == 0)
                    ellipse(xVar + 5, yVar + 5, 40, 40 + (size - 1) * 50);
                else
                    ellipse(xVar + 5, yVar + 5, 40 + (size - 1) * 50, 40);
            }
        }

        private boolean canFit()
        {
            if (mouseHoverX != -1 && mouseHoverY != -1)
            {
                int xVar = grids[playerTurn].gridZone[mouseHoverX][mouseHoverY].xPos;
                int yVar = grids[playerTurn].gridZone[mouseHoverX][mouseHoverY].yPos;

                if (direction == 0)
                {
                    if (yVar + 40 + (size - 1) * 50 > grids[playerTurn].yPos + 500)
                        return false;
                    else
                        return true;
                }
                else
                {
                    if (xVar + 40 + (size - 1) * 50 > grids[playerTurn].xPos + 500)
                        return false;
                    else
                        return true;
                }
            }
            else
                return false;
        }

        private void drawMini()
        {
            mini.drawShipMini();
        }
    }

    public class ShipMini
    {
        int c1, c2, c3;
        int xPos, yPos;
        Ship ship;
        boolean selected;
        boolean placed = false;


        ShipMini(Ship ship, int xPos, int yPos)
        {
            this.xPos = xPos;
            this.yPos = yPos;
            this.ship = ship;
            c1 = c2 = c3 = 200;
            selected = false;
        }

        private void drawShipMini()
        {
            strokeWeight(2);
            stroke(0);
            fill(c1, c2, c3);
            rect(xPos, yPos, 50, 130, 10);

            ellipseMode(CORNER);

            if (ship.player == 0)
                fill(180, 0, 0);
            else
                fill(34, 240, 89);

            stroke(0);
            strokeWeight(1);
            ellipse(xPos + 10, yPos + 10, 30, 30);

            fill(0);
            text("Size", xPos + 12, yPos+ 90);
            text(ship.size, xPos + 20, yPos + 110);
        }

        private boolean mouseOver()
        {
            return (mouseX > xPos && mouseY > yPos && mouseX < xPos + 50 && mouseY < yPos + 130);
        }
    }




}
