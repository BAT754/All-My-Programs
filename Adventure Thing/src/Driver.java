import processing.core.PApplet;
import processing.core.PFont;

public class Driver extends PApplet {

    private String gameState;
    private String fightState;

    private PFont mainFont;
    private PFont textFont;

    private Character player;
    private Character enemy;

    private boolean beginCombat = true;
    private boolean showingText = false;
    private boolean newTextBox = true;

    // 1 = Attack, 0 = Defend

    private int waitTimer = 0;
    private int textCounter = 0;
    private int textSpeed = 2;
    private int textIndex = 0;
    private int textIncrease = 1;
    private int textWait = 0;
    private boolean textShown = false;

    private int hTrainCost = 30;
    private int sTrainCost = 50;
    private int dTrainCost = 50;
    private int cTrainCost = 100;

    private String[] battleTexts = {                                            // Condition      Index
            "An enemy appears from the wild!",                                  // Intro            0
            "You attacked, but the \nenemy defended.",                            // P - 1 | E - 0    1
            "The enemy attacked, \nbut you defended, reducing the damage!",       // P - 0 | E - 1    2
            "You attack the enemy, \nand they retaliate as well.",                // P - 1 | E - 1    3
            "You and the enemy both \ntake defensive stances."                    // P - 0 | E - 0    4
    };
    private String shownPhrase;



    // ----- FUNCTIONS ----------------------------------------- //


    public static void main(String[] args) {
        PApplet.main("Driver");
    }

    public void settings()
    {
        size(400, 500);
    }

    public void setup()
    {
        player = new Character();

        gameState = "menu";
        mainFont = createFont("Arial", 20);
        textFont = createFont("Monospaced", 20);
    }

    public void draw() {
        background(230);

        switch (gameState) {
            case "menu":
                drawMenuOptions();
                break;

            case "fight":
                performFight();
                break;

            case "train":
                drawTrain();
                break;

            case "stats":
                drawStats();
                break;
        }
    }

    private void drawEnvironment()
    {
        strokeWeight(5);
        stroke(0, 140, 0);
        fill(0, 180, 0);
        ellipse(100,225, 175, 75);
        ellipse(300,100, 175, 75);
    }
    private void drawFightButtons()
    {
        strokeWeight(3);
        stroke(0);
        fill(150);
        rect(25, 425, 150, 50, 5) ;
        rect(225, 425, 150, 50, 5);

        textFont(mainFont, 24);
        fill(0);
        text("Attack", 65, 457);
        text("Defend", 265, 457);
    }
    private void drawHealthBars()
    {
        //  ---------------- ENEMY STATUS BAR ------------------------ //

        // background border
        strokeWeight(2);
        stroke(0);
        fill(180);
        rect(10, 10, 130, 70, 5);

        // Character Name
        textFont(mainFont, 12);
        fill(0);
        text(enemy.name, 25, 30);

        // Background bar
        fill(100);
        stroke(100);
        rect(20, 40, 100, 10);

        enemy.healthBarSize = (int)((((double)enemy.health / (double)enemy.maxHealth) * 100) % 101);

        // Health Bar
        fill(255, 0,0);
        noStroke();
        rect(20, 40, enemy.healthBarSize, 10);

        if (enemy.health <= 0)
        {
            enemy.health = 0;
            enemy.healthBarSize = 0;
        }

        // Health Border
        noFill();
        strokeWeight(2);
        stroke(0);
        rect(19, 39,102, 12, 2);

        // Level and health
        fill(0);
        textFont(mainFont,10);
        text("Lvl: " + enemy.level, 22, 70);
        text("HP: " + enemy.health + " / " + enemy.maxHealth, 65, 70);

        //  -------------- PLAYER STATUS BAR ---------------------------------------  //

        // background border
        strokeWeight(2);
        stroke(0);
        fill(180);
        rect(250, 190, 130, 70, 5);

        // Player's name
        textFont(mainFont, 12);
        fill(0);
        text(player.name, 265, 210);

        // Background bar
        fill(100);
        stroke(100);
        rect(260, 220, 100, 10);

        player.healthBarSize = (int)((((double)player.health / (double)player.maxHealth) * 100) % 101);

        // Health Bar
        fill(255, 0,0);
        noStroke();
        rect(260, 220, player.healthBarSize, 10);

        if (player.health <= 0)
        {
            player.health = 0;
            player.healthBarSize = 0;
        }

        // Health Border
        noFill();
        strokeWeight(2);
        stroke(0);
        rect(259, 219,102, 12, 2);

        // Level and health
        fill(0);
        textFont(mainFont,10);
        text("Lvl: " + player.level, 260, 250);
        text("HP: " + player.health + " / " + player.maxHealth, 310, 250);
    }
    private void drawMenuOptions()
    {
        strokeWeight(3);
        stroke(0);
        fill(170);
        rect(25, 425, 100, 50,4);
        rect(150, 425, 100,50,4);
        rect(275, 425, 100, 50,4);

        fill(0);
        textFont(mainFont, 16);
        text("Fight", 60, 455);
        text("Train", 185, 455);
        text("View Stats", 290, 455);

        textFont(mainFont, 20);
        text("Choose an option", 120, 50);
    }
    private void drawStats()
    {
        // Main Box
        strokeWeight(5);
        stroke(0);
        fill(170, 100);
        rect(75, 50, 250, 325, 5);

        fill(0);
        textFont(mainFont, 24);
        text("-- Your Stats --", 120, 85);

        textFont(mainFont, 16);
        text("Maximum Health:", 85, 115);
        text("Current Health:  ", 85, 140);
        text("Strength:", 85, 165);
        text("Defense:" , 85, 190);
        text("Money:", 85, 215);

        text(player.maxHealth, 250, 115);
        text(player.health, 250, 140);
        text(player.strength, 250, 165);
        text(player.defense, 250, 190);
        text("$" + player.money, 250, 215);

        // Buttons
        strokeWeight(3);
        fill(170, 255);
        rect(50, 425, 100, 50,4);
        rect(250, 425, 100, 50, 4);

        fill(0);
        textFont(mainFont, 16);
        text("Heal", 80, 445);
        text("$10", 83, 465);
        text("Back", 280, 455);

    }
    private void drawTextBox(String phrase)
    {
        if (newTextBox)
        {
            textIndex = 0;
            textCounter = 0;
            textIncrease = 1;
            textWait = 0;
            newTextBox = false;
            textShown = false;
        }

        fill(0);
        strokeWeight(3);
        stroke(255);
        rect(50, 350, 300, 75, 10);

        fill(255);
        textFont(textFont, 16);
        text(phrase.substring(0, textIndex), 60, 375);

        if (textCounter % textSpeed == 0)
        {
            textIndex += 1;
        }

        textCounter += textIncrease;

        if (textIndex >= phrase.length())
        {
            textIncrease = 0;
            textWait += 1;
        }

        if (textWait >= 50)
        {
            textShown = true;
            textWait = 0;
        }

    }
    private void drawTrain()
    {
        strokeWeight(4);
        stroke(0);
        fill(170);
        rect(35, 100, 155, 125, 5);
        rect(210, 100, 155, 125, 5);
        rect(35, 245, 155, 125, 5);
        rect(210, 245, 155, 125, 5);
        strokeWeight(2);
        rect(130, 20, 150, 50, 5);

        fill(0);
        textFont(mainFont, 26);
        text("Health", 75, 165);
        text("$" + hTrainCost, 90, 200);

        text("Attack", 255, 165);
        text("$" + sTrainCost, 265, 200);

        text("Defense", 65, 310);
        text("$" + dTrainCost, 90, 345);

        text("Surprise", 240, 310);
        text("$" + cTrainCost, 250, 345);

        textFont(textFont, 24);
        text("$" + player.money, 140, 53);

        strokeWeight(3);
        fill(170, 255);
        rect(250, 425, 100, 50, 4);

        fill(0);
        textFont(mainFont, 16);
        text("Back", 280, 455);
    }



    private void getInPosition()
    {
        if (!player.inPosition)
        {
            player.xPos += 4;

            if (player.xPos >= 100)
            {
                player.xPos = 100;
                player.inPosition = true;
            }
        }

        if (player.inPosition && !enemy.inPosition)
        {
            enemy.xPos -= 4;

            if (enemy.xPos <= 300)
            {
                enemy.xPos = 300;
                enemy.inPosition = true;
            }
        }
    }
    public void keyPressed()
    {
        switch(gameState)
        {
            case "fight":
                if (key == 'd')
                {
                    enemy.health -= 1;
                }
                break;
        }
    }
    public void mousePressed()
    {
        switch (gameState)
        {
            case "menu":
                // FIGHT BOX
                if (mouseX > 25 && mouseY > 425 && mouseX < 125 && mouseY < 475)
                {
                    gameState = "fight";
                    fightState = "intro";
                    beginCombat = true;
                    enemy = new Character((int)random(4) + 1, (int)random(4) + 1, (int)random(11)+ 10, (int)random(15) + 5, 1);
                }

                // TRAIN BOX
                if (mouseX > 150 && mouseY > 425 && mouseX < 250 && mouseY < 475)
                {
                    gameState = "train";
                }

                // VIEW STATS BOX
                if (mouseX > 275 && mouseY > 425 && mouseX < 375 && mouseY < 475)
                {
                    gameState = "stats";
                }
                break;

            case "stats":

                if (mouseX > 50 && mouseY > 425 && mouseX < 150 && mouseY < 475)
                {
                    // Do healing stuffs
                    player.health = player.maxHealth;
                    player.money -= 10;
                }

                if (mouseX > 250 && mouseY > 425 && mouseX < 350 && mouseY < 475)
                {
                    gameState = "menu";
                }

                break;

            case "train":

                fill(170);
                rect(35, 100, 155, 125, 5);
                rect(210, 100, 155, 125, 5);
                rect(35, 245, 155, 125, 5);
                rect(210, 245, 155, 125, 5);

                // Health      ----------------------------------------------- //
                if (mouseX > 35 && mouseY > 100 && mouseX < 190 && mouseY < 225 )
                {
                    if (player.money >= hTrainCost)
                    {
                        player.money -= hTrainCost;
                        player.maxHealth += 5;
                    }
                }

                // Attack      ----------------------------------------------- //
                if (mouseX > 210 && mouseY > 100 && mouseX < 365 && mouseY < 225)
                {
                    if (player.money >= sTrainCost)
                    {
                        player.money -= sTrainCost;
                        player.strength += 1;
                    }
                }

                // Defense    ----------------------------------------------- //
                if (mouseX > 35 && mouseY > 245 && mouseX < 190 && mouseY < 370)
                {
                    if (player.money >= dTrainCost)
                    {
                        player.money -= dTrainCost;
                        player.defense += 1;
                    }
                }

                // Surprise    ----------------------------------------------- //
                if (mouseX > 210 && mouseY > 245 && mouseX < 365 && mouseY < 370)
                {
                    System.out.println("4");
                }

                // Back Button ----------------------------------------------- //
                if (mouseX > 250 && mouseY > 425 && mouseX < 350 && mouseY < 475)
                {
                    gameState = "menu";
                }
                break;

            case "fight":

                // Attack Button
                if (mouseX > 25 && mouseY > 425 && mouseX < 175 && mouseY < 475)
                {
                    player.action = 1;
                }

                // Defend Button
                if (mouseX > 225 && mouseY > 425 && mouseX < 375 && mouseY < 475)
                {
                    player.action = 0;
                }
                break;
        }
    }

    public void performFight()
    {
        // Shows the scene
        drawEnvironment();
        player.drawCharacter(195);
        enemy.drawCharacter(65);

        switch (fightState) {
            case "intro":

                if (beginCombat)
                {
                    enemy = new Character((int)random(4) + 2, (int)random(4) + 2, (int)random(21)+ 5, (int)random(15) + 5, 1);

                    System.out.println(enemy.health);
                    System.out.println(enemy.strength);
                    System.out.println(enemy.defense);

                    beginCombat = false;
                    player.xPos = -100;
                    enemy.xPos = 500;
                    enemy.name = "Enemy";
                    enemy.alpha = 255;
                    player.inPosition = false;
                    enemy.inPosition = false;
                }


                if (player.inPosition && enemy.inPosition) {

                    fightState = "player choice";

                }

                getInPosition();

                break;

            case "player choice":
                // Monitors Actions
                drawFightButtons();
                drawHealthBars();

                if (player.action != -1) {
                    fightState = "computer choice";
                }

                break;

            case "computer choice":
                drawHealthBars();
                // Shows the scene
                enemy.action = (int) random(2);
                fightState = "wait";
                break;

            case "wait":

                drawHealthBars();
                waitTimer += 1;

                if (waitTimer >= 50) {
                    fightState = "action";
                    waitTimer = 0;
                    showingText = true;
                    newTextBox = true;
                }

                break;

            case "action":
                drawHealthBars();

                if (showingText)
                {
                    if (player.action == 1)
                    {
                        player.defending = false;

                        if (enemy.action == 1)
                        {
                            shownPhrase = battleTexts[3];
                            enemy.defending = false;
                        }
                        else
                        {
                            shownPhrase = battleTexts[1];
                            enemy.defending = true;
                        }
                    }
                    else
                    {
                        player.defending = true;

                        if (enemy.action == 1)
                        {
                            shownPhrase = battleTexts[2];
                            enemy.defending = false;
                        }
                        else
                        {
                            shownPhrase = battleTexts[4];
                            enemy.defending = true;
                        }
                    }

                    drawTextBox(shownPhrase);

                    if (textShown)
                    {
                        showingText = false;
                    }
                }
                else
                {
                    // Deal Damage
                    if (!player.defending)
                    {
                        enemy.takeDamage(player);
                    }

                    if (!enemy.defending)
                    {
                        player.takeDamage(enemy);
                    }

                    fightState = "player choice";
                    player.action = -1;
                    enemy.action = -1;
                    waitTimer = 0;

                }

                if (player.health <= 0 || enemy.health <= 0)
                {
                    fightState = "end";
                    newTextBox = true;
                }

                break;

            case "end":

                if (enemy.health <= 0)
                {
                    if (enemy.alpha >= 0)
                        enemy.alpha -= 5;
                    else
                    {
                        drawTextBox("You defeated the enemy!\nYou won " + enemy.money + " gold!");

                        if (textShown)
                        {
                            if (player.xPos > -100)
                                player.xPos -= 4;
                            else
                            {
                                gameState = "menu";
                                player.money += enemy.money;
                            }
                        }
                    }

                }
                else
                {
                    drawTextBox("You got oofed.\nYou lost 10 gold.");

                    if (textShown)
                    {
                        player.money -= 10;
                        gameState = "menu";
                    }
                }



                break;
        }
    }


    /*
        Character Class
        Manages the Character's visuals and their actions
     */

    public class Character
    {
        int strength;
        int defense;
        int money;
        int maxHealth;
        int health;
        int xPos = -200;
        int alpha;
        int action = -1;
        String name;

        int healthBarSize;
        boolean inPosition = false;
        boolean defending = false;
        int level;

        int c1, c2, c3;
        int s1, s2, s3;

            /* Add leveling system later

            int xp;
            int[] levelChart = new int[10];
            */

        public Character() {
            strength = 5;
            defense = 5;
            money = 0;
            maxHealth = 20;
            health = maxHealth;
            name = "Player";
            level = 1;
            alpha = 255;

            c1 = c2 = 100;
            c3 = 255;
            s1 = s2 = 0;
            s3 = 180;
        }

        Character(int str, int def, int mon, int hth, int lvl) {
            strength = str;
            defense = def;
            maxHealth = hth;
            health = maxHealth;
            money = mon;
            name = "Enemy";
            level = lvl;

            c1 = 240;
            c2 = c2 = 80;

            s1 = 170;
            s2 = s3 = 0;
        }

        public void takeDamage(Character enemy)
        {
            int damageTaken;

            if (defending)
                damageTaken = enemy.strength - defense;
            else
                damageTaken = enemy.strength;

            if (damageTaken < 1)
                damageTaken = 1;

            health -= damageTaken;
        }

        public void drawCharacter(int yPos)
        {
            strokeWeight(2);
            stroke(s1, s2, s3 ,alpha);
            fill(c1, c2, c3, alpha);
            ellipse(xPos, yPos, 100, 100);
        }
    }
}
