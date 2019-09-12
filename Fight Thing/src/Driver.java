import processing.core.PApplet;
import processing.core.PFont;

public class Driver extends PApplet
{

    private String gameState;

    private PFont MAIN_MENU;
    private PFont BASIC_FONT;


    public static void main(String[] args)
    {
        PApplet.main("Driver");
    }

    public void setup()
    {
        gameState = "Main Menu";

        // ------ Fonts ------- //
        MAIN_MENU = createFont("Herculanum", 30);
        BASIC_FONT = createFont("Arial-Black", 16);

    }

    public void settings()
    {
        size(1200, 750);
    }

    public void draw()
    {
        background(255);

        switch (gameState)
        {
            case "Main Menu":

                drawMainMenu();



                break;

            case "Game":

                drawGame();

                break;

        }

    }

    public void drawGame()
    {
        background(30, 30, 190);

        fill(120, 210, 70);
        strokeWeight(5);
        stroke(0);
        rect(400, 600, 400, 100, 5);

        fill(0);
        text("Play", 560, 660);

    }

    public void drawMainMenu()
    {
        background(100);

        textFont(MAIN_MENU, 32);
        text("Welcome to the game", 440, 100);


        drawMainMenuButtons();

    }

    public void drawMainMenuButtons()
    {
        // play button
        fill(120, 10, 10);
        strokeWeight(5);
        stroke(0);
        rect(400, 600, 400, 100, 5);

        fill(0);
        text("Play", 560, 660);
    }

    public void mousePressed()
    {
        switch (gameState)
        {
            case "Main Menu":

                if (mouseX > 400 && mouseY < 800 && mouseY > 600 && mouseY < 700)
                {
                    gameState = "Game";
                }

                break;

            case "Game":

                if (mouseX > 400 && mouseY < 800 && mouseY > 600 && mouseY < 700)
                {
                    gameState = "Main Menu";
                }

                break;
        }
    }
}
