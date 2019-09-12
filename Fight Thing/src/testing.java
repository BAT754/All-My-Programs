import java.util.Scanner;
import java.util.Random;

public class testing
{
    // Used to clear the left over "\n" that gets left when getting anything other than strings.
    public static String bufferClear;

    public static void main(String[] args)
    {
        Scanner playerInput = new Scanner(System.in);
        Character mainPlayer = new Character(15, 4, 4);

        System.out.println("Hello. What is your name?");
        mainPlayer.name = playerInput.nextLine();

        System.out.println("Welcome, " + mainPlayer.name + "\n\n");

        int menuChoice = -1;

        // Loops the game until the player chooses to leave the game
        while (menuChoice != 4)
        {

            System.out.println("You have 3 options:\n1. Fight\n2. Shop\n3. View Stats\n4. Leave\n");
            System.out.println("What do you want to do");
            System.out.print("CHOICE >> ");
            menuChoice = playerInput.nextInt();
            bufferClear = playerInput.nextLine();

            System.out.print("\n");

            // Switch statements act like if statements, just with different formatting
            // You pick one variable that you has several possible values, such as menuChoice
            // It then enters the case that represents the possible option.
            switch (menuChoice)
            {
                case 1:     // Fight

                    fight(mainPlayer);

                    if (mainPlayer.health < 1)
                        lostBattle(mainPlayer);

                    break;

                case 2:     // Shop

                    training(mainPlayer);

                    break;

                case 3:     // View Stats

                    mainPlayer.viewStats();

                    System.out.println("Do you want to heal for 20 gold?");
                    String heal = playerInput.nextLine();

                    if (heal.equalsIgnoreCase("yes"))
                    {
                        if (mainPlayer.coins >= 20)
                        {
                            System.out.println("You have been healed back to full health");
                            mainPlayer.health = mainPlayer.maxHealth;
                            mainPlayer.coins -= 20;
                        }
                        else
                        {
                            System.out.println("Sorry, you don't have enough money to be healed");
                            System.out.println("Better go win a fight");
                        }
                    }
                    else
                    {
                        System.out.println("Well good luck then\n");
                    }

                    break;

                case 4:     // Leave



                    break;

                case 5:

                    cheatCodeArea(mainPlayer);

                    break;

            }   /* End Switch */

        } /* End While */

    } /* End Main */


    // Fight method for the character
    // Parameter:   Character object
    // Returns:     Void

    public static void fight(Character player)
    {
        // Objects
        Scanner input = new Scanner(System.in);
        Character enemy = new Character(player.level);
        Random rNum = new Random();

        boolean fight = true;

        int damageDealt;
        int playerTurn = 1;
        int fightChoice;

        System.out.println(player.name + ", you enter the arena.");
        System.out.println("An enemy approaches.");

        while (fight)
        {
            if (playerTurn == 1)        // ------------  Player Turn  --------------  //
            {

                System.out.println("It is your turn. Do you: \n1. Attack\n2. Defend\n3. Conserve your energy\n");
                System.out.print("CHOICE >> ");
                fightChoice = input.nextInt();
                bufferClear = input.nextLine();


                switch (fightChoice)
                {
                    case 1:

                        damageDealt = player.calculateDamage(enemy);

                        System.out.println("You deal " + damageDealt + " damage to the enemy.\n");
                        enemy.health -= damageDealt;
                        player.defenseBonus = 5;

                        break;

                    case 2:

                        System.out.println("You take a defense stance, boosting your defense.\n");
                        player.conservePoints += 1;
                        player.defenseBonus = 5;

                        break;

                    case 3:

                        System.out.println("You conserve your energy, readying for your next attack.\n");
                        player.conservePoints += player.concentration;
                        player.defenseBonus = 0;

                        break;
                }

                if (enemy.health <= 0)
                {
                    System.out.println("Your attack finishes off the enemy.\nYou have won the battle!\n\n");
                    fight = false;
                    System.out.println("You gain " + enemy.coins + " gold from the enemy");
                    player.coins += enemy.coins;
                }
                else
                {
                    playerTurn = 2;
                }

            }
            else if (playerTurn == 2)   // ------------  Enemy Turn  --------------  //
            {
                // Get the enemy's choice of move
                fightChoice = rNum.nextInt(3) + 1;

                switch (fightChoice)
                {
                    case 1:

                        damageDealt = enemy.calculateDamage(player);

                        System.out.println("The enemy deals " + damageDealt + " damage to you.\n");
                        player.health -= damageDealt;
                        enemy.defenseBonus = 0;

                        break;

                    case 2:

                        System.out.println("The enemy takes a defense stance, boosting their defense.\n");
                        enemy.conservePoints += 1;
                        enemy.defenseBonus = 5;

                        break;

                    case 3:

                        System.out.println("The enemy conserves their energy, readying for their next attack.\n");
                        enemy.conservePoints += enemy.concentration;
                        enemy.defenseBonus = 0;

                        break;
                }

                if (player.health <= 0)
                {
                    System.out.println("The enemy's attack finishes you off.\nYou have lost this battle!\n\n");
                    fight = false;
                    System.out.println("You lose " + (player.coins / 4) + " as prize for the winner");
                    player.coins -= (player.coins / 4);
                }
                else
                {
                    playerTurn = 1;
                }

            }
        }

        // Closes the scanner
        //input.close();
    }

    public static void cheatCodeArea(Character player)
    {
        Scanner input = new Scanner(System.in);

        System.out.println("Which stat do you wish to increase?");
        String stat = input.nextLine();

        switch (stat)
        {
            case "Strength":
                System.out.println("What do you want your new stength to be?");
                player.attack = input.nextInt();

                break;

            case "Health":

                System.out.println("what do you want your new health to be?");
                player.health = input.nextInt();

                break;

            case "Concentration":
                System.out.println("what do you want your new Concentration bonus to be?");
                player.concentration = input.nextInt();
                break;

            case "Defense":

                System.out.println("What do you want your new defense to be?");
                player.defense = input.nextInt();

                break;

            case "Money":

                System.out.println("What do you want your new balance to be?");
                player.coins = input.nextInt();

                break;
        }
    }


    // Shop method for the character
    // Parameter:   Character object
    // Returns:     Void

    public static void training(Character player)
    {
        Scanner input = new Scanner(System.in);
        boolean wrongChoice = false;
        String train;

        System.out.println("Welcome to the training grounds");
        System.out.println("What kind of trianing would you like to attempt today?");
        System.out.println("Strength\nDefense\nConcentration\nResolve");

        String choice = input.nextLine();
        do
        {
            switch (choice)
            {
                case "Strength":

                    wrongChoice = false;
                    System.out.println("Strength training will cost you 30 gold, and increase your attack by 1");
                    System.out.println("Do you still want to train?");
                    train = input.nextLine();

                    if (train.equalsIgnoreCase("yes"))
                    {
                        if (player.coins < 30)
                        {
                            System.out.println("Sorry, you don't have enough money for this training");
                        }
                        else
                        {
                            player.attack += 1;
                            player.coins -= 30;
                        }
                    }
                    else
                    {
                        System.out.println("Good day then.\n\n");
                    }

                    break;

                case "Defense":

                    wrongChoice = false;
                    System.out.println("Defense training will cost you 30 gold, and increase your defense by 1");
                    System.out.println("Do you still want to train?");
                    train = input.nextLine();

                    if (train.equalsIgnoreCase("yes"))
                    {
                        if (player.coins < 30)
                        {
                            System.out.println("Sorry, you don't have enough money for this training");
                        }
                        else
                        {
                            player.defense += 1;
                            player.coins -= 30;
                        }
                    }
                    else
                    {
                        System.out.println("Good day then.\n\n");
                    }

                    break;

                case "Concentration":

                    wrongChoice = false;
                    System.out.println("Concentration training will cost you 50 gold, and increase your concentration bonus by 1");
                    System.out.println("Do you still want to train?");
                    train = input.nextLine();

                    if (train.equalsIgnoreCase("yes"))
                    {
                        if (player.coins < 30)
                        {
                            System.out.println("Sorry, you don't have enough money for this training");
                        }
                        else
                        {
                            player.concentration += 1;
                            player.coins -= 50;
                        }
                    }
                    else
                    {
                        System.out.println("Good day then.\n\n");
                    }

                    break;

                case "Resolve":

                    wrongChoice = false;
                    System.out.println("Resolve training will cost you 100 gold, and increase your health by 5");
                    System.out.println("Do you still want to train?");
                    train = input.nextLine();

                    if (train.equalsIgnoreCase("yes"))
                    {
                        if (player.coins < 30)
                        {
                            System.out.println("Sorry, you don't have enough money for this training");
                        }
                        else
                        {
                            player.maxHealth += 5;
                            player.health = player.maxHealth;
                            player.coins -= 100;
                        }
                    }
                    else
                    {
                        System.out.println("Good day then.\n\n");
                    }

                    break;


                default:    // The default case catches everything else that isn't caught in the previous cases.

                    wrongChoice = true;

                    break;
            }
        } while (wrongChoice);


        //input.close();
    }

    // Method for when the character loses a battle
    // Parameter:   Character object
    // Returns:     Void


    public static void lostBattle(Character player)
    {
        System.out.println("It looks like you lost your battle. I recomend being a little more cautious in the future.");
        System.out.println("Don't forget, you can heal yourself by checking your stats, and you can boost your stats by training if you've got the coin.");
        System.out.println("I'll restore you back to full health, but it's going to cost quite a bit");
        System.out.println("You're restored back to full health, at the cost of 40 gold");

        player.health = player.maxHealth;

        if (player.coins < 40)
        {
            player.coins = 0;
        }
        else
        {
            player.coins -= 40;
        }

    }





} /* End class */
