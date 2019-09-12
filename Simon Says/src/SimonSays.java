import processing.core.PApplet;
import processing.core.PFont;
import java.util.ArrayList;


public class SimonSays extends PApplet
{
    private Button[] bList;
    private ArrayList<Integer> sequence;

    private PFont main;

    private int score = 0;
    private int toFlash;
    private int flashCounter = 0;
    private int guess;
    private int guessing = 0;
    private int showing = 0;
    private int waitTimer = 0;

    private boolean startTurn = true;
    private boolean flashing = false;
    private boolean readyToFlash = false;
    private boolean madeGuess = false;
    private boolean restart = false;


    // Player turn or Computer Turn
    private String gameState;

    public static void main(String[] args)
    {
        PApplet.main("SimonSays");
    }

    public void settings()
    {
        size(600, 700);
    }

    public void setup()
    {

        gameState = "start";

        main = createFont("Arial", 20);
        bList = new Button[4];
        bList[0] = new Button(0, 100, 1);
        bList[1] = new Button(300, 100, 2);
        bList[2] = new Button(0, 400, 3);
        bList[3] = new Button(300, 400, 4);

        sequence = new ArrayList<>();
    }

    public void draw()
    {
        background(255);
        drawHUD();

        for (int x = 0; x < 4; x++)
        {
            bList[x].drawButton();
        }

        switch (gameState)
        {
            case "start":


                break;

            case "player":

                fill(0);
                textFont(main);

                if (madeGuess)
                {
                    System.out.println("You guessed " + guess + ", and the next in sequence was " + sequence.get(guessing));
                    if(guess == (sequence.get(guessing)))
                    {
                        System.out.println("Good job");
                        madeGuess = false;
                        guessing += 1;

                        if (guessing >= sequence.size())
                        {
                            score += 1;
                            gameState = "wait";
                            guessing = 0;
                        }
                    }
                    else
                    {
                        System.out.println("You messed up");
                        gameState = "game over";
                    }
                }


                break;

            case "computer":

                if (startTurn)
                {
                    sequence.add((int) random(4));
                    startTurn = false;
                    flashing = false;
                    readyToFlash = true;
                }


                    if (!flashing && readyToFlash)
                    {
                        flashing = true;
                        toFlash = sequence.get(showing);
                        bList[toFlash].flash = true;
                        readyToFlash = false;
                    }
                    else if (!flashing)
                    {
                        flashCounter += 1;

                        if (flashCounter >= 50)
                        {
                            readyToFlash = true;
                            flashCounter = 0;

                            if (showing == sequence.size()-1)
                            {
                                gameState = "player";
                                showing = 0;
                            }
                            else
                                showing += 1;
                        }
                    }

                break;

            case "wait":
                waitTimer += 1;

                if (waitTimer >= 50)
                {
                    gameState = "computer";
                    waitTimer = 0;
                    startTurn = true;
                }


                break;

            case "game over":

                if (restart)
                {
                    for (int x = sequence.size() - 1; x >= 0; x--)
                    {
                        sequence.remove(x);
                    }

                    showing = 0;
                    guessing = 0;
                    score = 0;
                    madeGuess = false;
                    restart = false;

                    gameState = "wait";
                }

                break;
        }

        // Flash Color
        for (int x = 0; x < 4; x++)
        {
            if (bList[x].flash)
            {
                bList[x].flash();
            }
        }
    }

    public void mousePressed()
    {
        if (!madeGuess)
        {
            for (int x = 0; x < 4; x++)
            {
                if (bList[x].mouseOver())
                {
                    flashing = true;
                    toFlash = bList[x].type - 1;
                    readyToFlash = false;
                    bList[x].flash = true;

                    madeGuess = true;
                    guess = bList[x].type - 1;
                }
            }
        }


    }

    public void keyPressed()
    {
        if (!madeGuess)
        {
            if (key == '1')
            {
                flashing = true;
                toFlash = 0;
                readyToFlash = false;
                bList[toFlash].flash = true;

                guess = 0;
                madeGuess = true;
            }
            if (key == '2')
            {
                flashing = true;
                toFlash = 1;
                readyToFlash = false;
                bList[toFlash].flash = true;

                guess = 1;
                madeGuess = true;
            }
            if (key == '3')
            {
                flashing = true;
                toFlash = 2;
                readyToFlash = false;
                bList[toFlash].flash = true;

                guess = 2;
                madeGuess = true;
            }
            if (key == '4')
            {
                flashing = true;
                toFlash = 3;
                readyToFlash = false;
                bList[toFlash].flash = true;

                guess = 3;
                madeGuess = true;
            }

        }   /*  End !madeGuess if  */

        if (key == ENTER && gameState.equals("start"))
        {
            gameState = "wait";
        }

        if (key == ENTER && gameState.equals("game over"))
        {
            restart = true;
        }
    }

    private  void drawHUD()
    {
        fill(100, 100, 180);
        rect(20, 20, 540, 100);


        fill(0);
        textFont(main, 20);
        text("Score: " + score, 50, 50);


        switch (gameState)
        {
            case "start":

                text("Press ENTER to start", 400, 50);

                break;

            case "player":

                text("Your turn", 400, 50);

                break;

            case "wait":
            case "computer":

                text("Wait for pattern", 400, 50);

                break;

            case "game over":

                text("Game Over", 400, 50);

                break;
        }

         // SHOW THE SEQUENCE

//            text("Sequence: ", 10, 80);
//            for (int x = 0; x < sequence.size(); x++)
//            {
//                text(sequence.get(x) + 1, 120 + 20*x, 80);
//            }


    }

    public class Button     /* --------------------  Button class  ---------------------------- */
    {
        int x;
        int y;
        int c1, c2, c3;
        int size = 300;
        int type;
        int alpha = 105;
        int flashAmount = 20;

        boolean flash = false;

        public Button(int x, int y, int type)
        {
            this.x = x;
            this.y = y;
            this.type = type;


            switch (type)
            {
                case 1:     // Purple
                    c1 = 255;
                    c2 = 0;
                    c3 = 255;
                    break;

                case 2:     // Red
                    c1 = 255;
                    c2 = 0;
                    c3 = 0;
                    break;

                case 3:     // Blue
                    c1 = 0;
                    c2 = 0;
                    c3 = 255;
                    break;

                case 4:     // Green
                    c1 = 0;
                    c2 = 150;
                    c3 = 0;
                    break;

                default:

                    c1 = 0;
                    c2 = 0;
                    c3 = 0;
            }
        }   /* End constructor */

        public void drawButton()
        {
            strokeWeight(5);
            stroke(0);
            fill(c1, c2, c3, alpha);
            rect(x, y, size, size);

            textFont(main, 72);
            fill(c1 - 100, c2 - 100, c3 - 100);
            text(type, x + 120, y + 170);
        }

        public boolean mouseOver()
        {
            if (mouseX > x && mouseY > y && mouseX < x + size && mouseY < y + size)
            {
                return true;
            }
            return false;
        }

        public void flash()
        {
            if (flash)
            {
                alpha += flashAmount;

                if (alpha >= 255)
                {
                    flashAmount = -flashAmount;
                }

                if (alpha <=100)
                {
                    flashAmount = -flashAmount;
                    flash = false;
                    flashing = false;
                    alpha = 105;

                }

            }
        }
    }
}
