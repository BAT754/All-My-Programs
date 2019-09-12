import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Driver extends PApplet
{
    Row[] rows;
    MasterRow masterRow;

    Peg[] options;
    Peg selectedPeg;

    boolean validClick;
    boolean fillMaster;
    boolean coveredUp;

    int rowOn;
    int players;
    char theme;

    enum GameState {
        MASTER_TURN, GUESSER_TURN, COVER_SHIELD, CALCULATE,
        WIN, FAIL, RESTART, MAIN_MENU, COMPUTER_TURN
    }
    GameState gameState;

    PFont mainFont;

    PImage[] dogPics;
    PImage[] catPics;
    PImage[] swPics;
    PImage[] hPics;

    public static void main(String[] args)
    {
        PApplet.main("Driver");
    }

    public void settings()
    {
        size(1300, 650);
    }

    public void setup()
    {
        rows = new Row[10];
        masterRow = new MasterRow();
        options = new Peg[7];
        validClick = false;
        fillMaster = true;
        rowOn = 0;
        theme = '~';
        gameState = GameState.MAIN_MENU;

        for (int x = 0; x < 10; x++)
        {
            rows[x] = new Row(x);
        }

        options[0] = new Peg(100, 90, 0);
        options[1] = new Peg(200, 90, 1);
        options[2] = new Peg(300, 90, 2);
        options[3] = new Peg(400, 90, 3);
        options[4] = new Peg(500, 90, 4);
        options[5] = new Peg(600, 90, 5);
        options[6] = new Peg(700, 90, -1);

        mainFont = createFont("monospaced", 20, false);


        // Theme Pictures

        dogPics = new PImage[11];
        catPics = new PImage[11];
        swPics = new PImage[11];
        hPics = new PImage[11];

        for (int x = 0; x < 11; x++)
        {
            dogPics[x] = loadImage("Images/Dog" + (x + 1) + ".jpg");
        }

        for (int x = 0; x < 11; x++)
        {
            catPics[x] = loadImage("Images/cat" + (x + 1) + ".jpg");
        }

        for (int x = 0; x < 11; x++)
        {
            swPics[x] = loadImage("Images/sw" + (x + 1) + ".jpg");
        }

        for (int x = 0; x < 11; x++)
        {
            hPics[x] = loadImage("Images/h" + (x + 1) + ".jpg");
        }
    }

    public void draw()
    {
        background(220, 220, 220);

        textFont(mainFont, 12);
        fill(0);
        //text("x " + mouseX, 20, 20);
        //text("y " + mouseY, 20, 40);
        text("State: " + gameState, 20, 60);

        if (gameState != GameState.MAIN_MENU)
        {
            drawBoard();
        }

        switch (gameState)
        {
            case MAIN_MENU:

                drawMainMenu();

                break;

            case COMPUTER_TURN:

                if (masterRow.isCovered())
                {
                    for (int x = 0; x < 4; x++)
                    {
                        masterRow.pegs[x].type = (int)random(6);
                    }
                    gameState = GameState.GUESSER_TURN;
                }
                else
                {
                    masterRow.speed = 3;
                    masterRow.moveShield();
                }

                break;

            case MASTER_TURN:

                if (masterRow.cover > 0)
                    masterRow.moveShield();
                else
                    masterRow.cover = 0;

                if (masterRow.rowFull())
                {
                    drawConfirmButton();
                }

                break;

            case COVER_SHIELD:

                masterRow.speed = 3;
                masterRow.moveShield();

                if (masterRow.cover >= 150)
                {
                    masterRow.cover = 150;

                    gameState = GameState.GUESSER_TURN;
                }

                break;


            case GUESSER_TURN:

                if (rows[rowOn].cover > 0)
                {
                    rows[rowOn].moveShield();
                }
                else
                {
                    rows[rowOn].cover = 0;
                }

                if (rows[rowOn].rowFull())
                {
                    drawConfirmButton();
                }
                break;

            case CALCULATE:
                calculateRow();

                break;

            case WIN:

                textFont(mainFont, 48);
                fill(0);
                text("You win!", 600, 50);

                if (masterRow.cover > 0)
                {
                    masterRow.moveShield();
                }
                else
                    masterRow.cover = 0;

                drawRestartButton();
                break;

            case FAIL:

                textFont(mainFont, 48);
                fill(0);
                text("You lose!", 600, 50);

                if (masterRow.cover > 0)
                {
                    masterRow.moveShield();
                }
                else
                    masterRow.cover = 0;

                drawRestartButton();


                break;

            case RESTART:

                // Covers the master row back up
                if (masterRow.cover < 150)
                {
                    masterRow.moveShield();
                }
                else
                    masterRow.cover = 150;

                // Covers each row back up. Once they're covered, then the restart happens
                for (int x = 0; x < 10; x++)
                {
                    if (!rows[x].isCovered())
                    {
                        rows[x].speed = 10;
                        rows[x].moveShield();


                    }
                }

                if (rows[0].isCovered() && masterRow.isCovered())
                {
                    coveredUp = true;
                }

                if (coveredUp)
                {
                    // Resets all guessing rows
                    for (int x = 0; x < 10; x++)
                    {
                        for (int y = 0; y < 4; y++)
                        {
                            rows[x].pegs[y].type = -1;
                            rows[x].speed = -10;
                            rows[x].rightSpot = 0;
                            rows[x].rightColor = 0;
                            rows[x].pegs[y].foundMatch = false;
                        }
                    }

                    // Resets Master Row
                    for (int x = 0; x < 4; x++)
                    {
                        masterRow.pegs[x].type = -1;
                        masterRow.pegs[x].foundMatch = false;
                        masterRow.speed = -3;
                    }


                    // Resets other variables
                    rowOn = 0;
                    coveredUp = false;

                    if (players == 1)
                        gameState = GameState.COMPUTER_TURN;
                    else if (players == 2)
                        gameState = GameState.MASTER_TURN;
                }
                break;
        }

    }


    private void configureTheme()
    {
        switch (theme)
        {
            case '1':     // Dog Theme

                for (int x = 0; x < 10; x++)
                {
                    rows[x].coverImage = dogPics[x];
                    rows[x].holdImage = "Images/Dog"+ (x + 1) + ".jpg";
                    rows[x].coverImage.resize(90, 440);
                }

                masterRow.coverImage = dogPics[10];
                masterRow.holdImage = "Images/Dog11.jpg";
                masterRow.coverImage.resize(150, 440);
                break;

            case '2':     // cat theme

                for (int x = 0; x < 10; x++)
                {
                    rows[x].coverImage = catPics[x];
                    rows[x].holdImage = "Images/cat"+ (x + 1) + ".jpg";
                    rows[x].coverImage.resize(90, 440);
                }

                masterRow.coverImage = catPics[10];
                masterRow.holdImage = "Images/cat11.jpg";
                masterRow.coverImage.resize(150, 440);

                break;

            case '3':     // Video game theme

                break;

            case '4':     // Star wars theme

                for (int x = 0; x < 10; x++)
                {
                    rows[x].coverImage = swPics[x];
                    rows[x].holdImage = "Images/sw"+ (x + 1) + ".jpg";
                    rows[x].coverImage.resize(90, 440);
                }

                masterRow.coverImage = swPics[10];
                masterRow.holdImage = "Images/sw11.jpg";
                masterRow.coverImage.resize(150, 440);

                break;

            case '5':     // Surprise theme

                break;

            case '@':

                for (int x = 0; x < 10; x++)
                {
                    rows[x].coverImage = hPics[x];
                    rows[x].holdImage = "Images/h"+ (x + 1) + ".jpg";
                    rows[x].coverImage.resize(90, 440);
                }

                masterRow.coverImage = hPics[10];
                masterRow.holdImage = "Images/h11.jpg";
                masterRow.coverImage.resize(150, 440);
                break;
        }
    }

    private void calculateRow()
    {

        // Check for exact matches
        for (int x = 0; x < 4; x++)
        {
            if (rows[rowOn].pegs[x].type == masterRow.pegs[x].type)
            {
                rows[rowOn].rightSpot += 1;
                masterRow.pegs[x].foundMatch = true;
                rows[rowOn].pegs[x].foundMatch = true;

            }
        }

        // Check for right color in wrong spot
        for (int x = 0; x < 4; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (rows[rowOn].pegs[x].type == masterRow.pegs[y].type && !rows[rowOn].pegs[x].foundMatch &&!masterRow.pegs[y].foundMatch)
                {
                    rows[rowOn].rightColor += 1;
                    masterRow.pegs[y].foundMatch = true;
                    rows[rowOn].pegs[x].foundMatch = true;
                    y = 4;
                }
            }
        }

        if (rows[rowOn].rightSpot == 4)
        {
            masterRow.speed = -masterRow.speed;
            gameState = GameState.WIN;

        }
        else
        {
            rowOn += 1;

            if (rowOn == 10)
            {
                rowOn = 9;
                gameState = GameState.FAIL;
                masterRow.speed = -3;
            }
            else
            {
                gameState = GameState.GUESSER_TURN;

                for (int x = 0; x < 4; x++)
                {
                    masterRow.pegs[x].foundMatch = false;
                }
            }
        }
    }

    private void drawBoard()
    {
        stroke(0);
        strokeWeight(4);
        line(25, 80, 1275, 80);
        line(25, 160, 1275, 160);

        for (int x = 0; x < 10; x++)
        {
            rows[x].drawRow();
        }

        masterRow.drawMasterRow();
        for (int x = 0; x < 7; x++)
        {
            options[x].drawPeg();
        }
    }

    private void drawConfirmButton()
    {
        stroke(0);
        strokeWeight(2);
        fill(138, 196, 255);
        rect(900, 85, 200, 70, 10);

        textFont(mainFont, 32);
        fill(0);
        text("Confirm", 925, 130);
    }

    private void drawMainMenu()
    {
        background(104, 161, 252);

        // Title
        fill(200, 0, 0);
        textFont(mainFont, 48);
        text("Master Mind", 500, 100);

        // 1-player Button
        strokeWeight(4);
        stroke(0);
        fill(223, 72, 250);
        rect(390, 400, 250, 100, 10);

        fill(0);
        textFont(mainFont, 32);
        text("1 Player", 440, 460);

        // 2-player Button
        strokeWeight(4);
        stroke(0);
        fill(223, 72, 250);
        rect(660, 400, 250, 100, 10);

        fill(0);
        textFont(mainFont, 32);
        text("2 Players", 700, 460);
    }

    private void drawRestartButton()
    {
        stroke(0);
        strokeWeight(2);
        fill(138, 196, 255);
        rect(900, 85, 200, 70, 10);

        textFont(mainFont, 32);
        fill(0);
        text("Restart", 925, 130);
    }

    public void keyPressed()
    {
        if (gameState == GameState.MAIN_MENU)
        {
            theme = key;

        }

        if (key == 'r')
            gameState = GameState.MAIN_MENU;

        if (key == 'g')
        {
            for (int x = 0; x < 4; x++)
            {
                rows[rowOn].pegs[x].type = 0;
            }
        }
    }

    public void mousePressed()
    {
        switch (gameState)
        {
            case MAIN_MENU:

                if (mouseX > 390 && mouseY > 400 && mouseX < 640 && mouseY < 500)
                {
                    players = 1;
                    gameState = GameState.COMPUTER_TURN;
                    configureTheme();
                }

                if (mouseX > 660 && mouseY > 400 && mouseX < 810 && mouseY < 500)
                {
                    players = 2;
                    gameState = GameState.MASTER_TURN;
                    configureTheme();
                }

                break;

            case MASTER_TURN:

                for (int x = 0; x < 4; x++)
                {
                    if (selectedPeg != null && masterRow.pegs[x].mouseOver() && fillMaster)
                    {
                        masterRow.pegs[x].type = selectedPeg.type;
                        validClick = true;
                    }
                }

                if (masterRow.rowFull())
                {
                    if (mouseX > 900 && mouseY > 85 && mouseX < 1100 && mouseY < 155)
                    {
                        gameState = GameState.COVER_SHIELD;
                    }
                }

                break;

            case GUESSER_TURN:

                for (int x = 0; x < 10; x++)
                {
                    for (int y = 0; y < 4; y++)
                    {
                        if (selectedPeg != null && rows[x].pegs[y].mouseOver() && x == rowOn)
                        {
                            rows[x].pegs[y].type = selectedPeg.type;
                            validClick = true;
                        }
                    }
                }

                if (masterRow.rowFull())
                {
                    if (mouseX > 900 && mouseY > 85 && mouseX < 1100 && mouseY < 155)
                    {
                        gameState = GameState.CALCULATE;
                    }
                }

                break;

            case FAIL:
            case WIN:

                if (mouseX > 900 && mouseY > 85 && mouseX < 1100 && mouseY < 155)
                {
                    gameState = GameState.RESTART;
                    masterRow.speed = 3;
                }

                break;
        }


        // Pick which color peg you want. Allowed in both master and guesser options
        for (int x = 0; x < 7; x++)
        {
            if (options[x].mouseOver())
            {
                if (selectedPeg != null)
                {
                    selectedPeg.selected = false;
                    selectedPeg = null;
                }

                selectedPeg = options[x];
                selectedPeg.selected = true;
                validClick = true;
            }
        }

        // Checks if they clicked anywhere else on screen
        if (!validClick)
        {
            if (selectedPeg != null)
            {
                selectedPeg.selected = false;
                selectedPeg = null;
            }
        }
        else
        {
            validClick = false;
        }
    }   // End Mouse Click

    // -------------------  CLASSES  ----------------------- //

    public class Peg
    {
        int type;
        int yPos;
        int xPos;
        boolean options;
        boolean selected = false;
        boolean foundMatch = false;

        Peg(int yPos, int xPos)
        {
            type = -1;
            this.yPos = yPos;
            this.xPos = xPos;
            options = false;
        }

        Peg(int xPos, int yPos, int type)
        {
            this.xPos = xPos;
            this.yPos = yPos;
            this.type = type;
            options = true;
        }

        private void drawPeg()
        {
            switch (type)
            {
                case -1:    // clear

                    ellipseMode(CORNER);
                    fill(255, 50);
                    stroke(0);
                    strokeWeight(3);
                    ellipse(xPos, yPos, 60, 60);

                    stroke(200, 0, 0);
                    strokeWeight(3);
                    line(xPos + 20, yPos + 20, xPos + 40, yPos + 40);
                    line(xPos + 40, yPos + 20, xPos + 20, yPos + 40);

                    break;

                case 0:     // white

                    ellipseMode(CORNER);
                    fill(200, 200, 200);
                    stroke(255);
                    strokeWeight(3);
                    ellipse(xPos, yPos, 60, 60);
                    break;

                case 1:     // red
                    ellipseMode(CORNER);
                    fill(200, 0, 0);
                    stroke(255, 0, 0);
                    strokeWeight(3);
                    ellipse(xPos, yPos, 60, 60);

                    break;

                case 2:     // blue
                    ellipseMode(CORNER);
                    fill(0, 0, 200);
                    stroke(0, 0, 255);
                    strokeWeight(3);
                    ellipse(xPos, yPos, 60, 60);

                    break;

                case 3:     // purple
                    ellipseMode(CORNER);
                    fill(143, 1, 138);
                    stroke(255, 0, 246);
                    strokeWeight(3);
                    ellipse(xPos, yPos, 60, 60);

                    break;

                case 4:     // green
                    ellipseMode(CORNER);
                    fill(0, 200, 0);
                    stroke(0, 255, 0);
                    strokeWeight(3);
                    ellipse(xPos, yPos, 60, 60);

                    break;

                case 5:     // orange
                    ellipseMode(CORNER);
                    fill(255, 149, 0);
                    stroke(255, 184, 43);
                    strokeWeight(3);
                    ellipse(xPos, yPos, 60, 60);

                    break;
            }

            if (selected)
            {
                noStroke();
                fill(250, 255, 245, 100);
                ellipse(xPos - 5, yPos - 5, 70, 70);
            }

        }

        private boolean mouseOver()
        {
            if (mouseX > xPos && mouseY > yPos && mouseX < xPos + 60 && mouseY < yPos + 60)
            {
                return true;

            }

            return false;
        }
    }

    public class Row
    {
        Peg[] pegs;
        PImage coverImage;
        String holdImage;

        int position;

        int cover = 440;
        int speed = -10;
        int rightSpot;
        int rightColor;

        int c1, c2, c3;

        Row(int num)
        {
            pegs = new Peg[4];
            position = num;

            for (int x = 0; x < 4; x++)
            {
                pegs[x] = new Peg(280 + x*90, 35 + position*110);
            }

            rightSpot = 0;
            rightColor = 0;

            c1 = (int)random(256);
            c2 = (int)random(256);
            c3 =(int)random(256);

            System.out.println("Row is created");
        }

        private void drawHints()
        {
            // Draw Red Spots (Right color in the right spot)
            for (int x = 0; x < rightSpot; x++)
            {
                stroke(190, 0, 0);
                strokeWeight(1);
                fill(255, 0, 0);
                ellipse(30 + position*110 + x*20, 210, 10, 10);
            }

            // Draw White Spots (Right color in the wrong spot)
            for (int x = 0; x < rightColor; x++)
            {
                stroke(190, 190, 190);
                strokeWeight(1);
                fill(255, 255, 255);
                ellipse(30 + position*110 + x*20, 235, 10, 10);
            }
        }

        private void drawRow()
        {
            fill(130);
            stroke(0);
            strokeWeight(4);

            rect(20 + position*110, 200, 90, 440, 10);
            line(20 + position*110, 260, 20 + position*110 + 90, 260);

            for (int x = 0; x < 4; x++)
            {
                pegs[x].drawPeg();
            }

            drawHints();



            if (theme == '~')
            {
                fill(c1, c2, c3);
                noStroke();
                rect(20 + position*110, 200, 90, cover, 10);

            }
            else if (cover != 0)
            {
                image(coverImage, 20 + position*110, 200);

            }

            noFill();
            stroke(0);
            strokeWeight(4);

            rect(20 + position * 110, 200, 90, 440, 10);
        }

        private void moveShield()
        {
            cover += speed;

            if (theme != '~')
            {
                coverImage = loadImage(holdImage);
                coverImage.resize(90, cover);
            }
        }

        private boolean isCovered()
        {
            if (cover == 440)
                return true;

            else
                return false;
        }

        private boolean rowFull()
        {
            for (int x = 0; x < 4; x++)
            {
                if (pegs[x].type == -1)
                {
                    return false;
                }
            }

            return true;
        }
    }

    public class MasterRow
    {
        Peg[] pegs;
        PImage coverImage;
        String holdImage;

        int cover;
        int speed = 5;

        int c1, c2, c3;

        MasterRow()
        {
            cover = 0;
            pegs = new Peg[4];

            for (int x = 0; x < 4; x++)
            {
                pegs[x] = new Peg(280 + x*90, 1175);
            }

            c1 = (int)random(256);
            c2 = (int)random(256);
            c3 = (int)random(256);
        }

        private void drawMasterRow()
        {
            fill(180);
            stroke(0);
            strokeWeight(4);

            rect(1130, 200, 150, 440, 10);

            for (int x = 0; x < 4; x++)
            {
                pegs[x].drawPeg();
            }



            if (theme == '~')
            {
                noStroke();
                fill(c1, c2, c3);
                rect(1130, 200, cover, 440, 10);

            }
            else if (cover != 0)
            {
                image(coverImage, 1130, 200);

            }

            noFill();
            stroke(0);
            strokeWeight(4);

            rect(1130, 200, 150, 440, 10);
        }

        private void moveShield()
        {
            cover += speed;

            if (theme != '~')
            {

                coverImage = loadImage(holdImage);
                coverImage.resize(cover, 440);
            }

        }

        private boolean rowFull()
        {
            for (int x = 0; x < 4; x++)
            {
                if (pegs[x].type == -1)
                {
                    return false;
                }
            }

            return true;
        }

        private boolean isCovered()
        {
            return cover == 150;
        }
    }

}
