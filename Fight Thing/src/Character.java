import java.util.Random;

public class Character
{
    public int maxHealth;
    public int health;

    public int attack;
    public int defense;
    public int concentration;
    public int conservePoints;
    public int defenseBonus;

    public int coins;
    public int level;

    public String name;

    private Random randNum = new Random();

    public Character(int level)
    {
        maxHealth = randNum.nextInt(10) + 9 + level;
        health = maxHealth;

        attack  = randNum.nextInt(5) + level;
        defense = randNum.nextInt(5) + level;
        coins = randNum.nextInt(20)*level + 10;
        name  = "Enemy";
    }

    public Character(int hlth, int atk, int dfns)
    {
        maxHealth = hlth;
        health = maxHealth;
        attack = atk;
        defense = dfns;

        concentration = 3;
        coins = 0;
        level = 1;
        name = "Ricky Bobby";
    }

    public void viewStats()
    {
        System.out.println("||  YOUR STATS  ||");
        System.out.println("Current Health: " + health + " / " + maxHealth);
        System.out.println("Attack: " + attack);
        System.out.println("Defense: " + defense);
        System.out.println("Money: " + coins);
    }

    public int calculateDamage(Character enemy)
    {
        int totalDefense;
        int totalAttack;
        int damage;

        totalDefense = enemy.defense + enemy.defenseBonus;
        totalAttack = attack + conservePoints;

        conservePoints = 0;
        enemy.defenseBonus = 0;

        damage = totalAttack - totalDefense;

        if (damage < 1)
        {
            damage = 1;
        }

        return damage;


    }


}
