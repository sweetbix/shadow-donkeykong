import bagel.Image;

import java.util.ArrayList;

/**
 * Represents donkey kong, the main boss in the game. He has lives which can be lost to mario's bullets. He is subject
 * to gravity and can fall. Donkey kong does not move.
 * Implements Fallable which handles falling physics
 */
public class Donkey extends Character implements Fallable {
    private double GRAVITY = 0.4;
    // Initial starting health of donkey kong
    private final int START_HP = 5;
    private int health;

    /**
     * Constructs a donkey kong at the specified position. He will not move from this initial position at all. Loads
     * the image from the res folder too.
     * @param x The x coordinate of donkey kong
     * @param y The y coordinate of donkey kong
     */
    public Donkey(double x, double y) {
        setX(x);
        setY(y);
        health = START_HP;
        setImg(new Image("res/donkey_kong.png"));
    }

    /**
     * Method used to check if a bullet has connected with donkey kong. If so, he loses a health point.
     * @param bullets An array list containing all the bullet existing in the game at any present moment
     */
    public void touchBullet(ArrayList<Bullet> bullets) {
        for (Bullet b : bullets) {
            if (this.intersects(b)) {
                // bullet is destroyed upon contact
                health--;
                b.setDestroyed(true);
            }
        }
    }

    /**
     * Reset the state of donkey kong. Used to reset levels.
     */
    @Override
    public void reset() {
        health = START_HP;
    }

    /**
     * Get the gravity constant affecting donkey kong
     * @return The gravity constant affecting donkey kong
     */
    public double getGRAVITY() {
        return GRAVITY;
    }

    /**
     * Get the current health of donkey kong. Used to load this information in the level stats prompts
     * @return The current health points of donkey kong
     */
    public int getHealth() {
        return health;
    }

    /**
     * Set the health of donkey to a value. Used to set his health to 0 if mario destroys donkey
     * @param health The new health points of donkey kong
     */
    public void setHealth(int health) {
        this.health = health;
    }
}
