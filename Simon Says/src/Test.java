import processing.core.PApplet;

public class Test extends PApplet
{
    boolean pause = false;
    int yPos = 200;

    public static void main(String[] args)
    {
        PApplet.main("Test");
    }

    public void settings()
    {
        size (400, 400);

    }

    public void setup()
    {

    }

    public void draw()
    {
        ellipse(200, yPos, 60, 60);

        while (pause)
        {

        }

        yPos += 2;

        if (yPos > 400)
            yPos = 0;
    }

    public void keyPressed()
    {
        if (key == 'f')
        {
            pause = true;
        }

        if (key == 'j')
        {
            pause = false;
        }
    }
}
