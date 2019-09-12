import processing.core.PFont;
import processing.core.PApplet;

public class DisplayFonts extends PApplet
{

    private String[] fontList = new String[1000];
    private PFont test;

    private int xPos = 5;
    private int yPos = 0;
    private int fontNum = 0;

    private int timer;

    public static void main(String[] args)
    {
        PApplet.main("DisplayFonts");

    }

    public void setup()
    {
        fontList = PFont.list();
        fontNum = 0;

        timer = 0;

        for (int x = 0; x < 549; x++)
        {
            System.out.println(fontList[x]);
        }

    }

    public void settings()
    {
        size(700, 400);
    }

    public void draw()
    {
        background(255);

        test = createFont(fontList[fontNum], 16);

        fill(0);
        textFont(test, 16);
        text("Test sentance with " + fontList[fontNum], 20, 200);

        timer += 1;

        if (timer >= 60)
        {
            timer = 0;
            fontNum += 1;

            if (fontNum == 999)
                fontNum = 0;
        }


    }

}
