import bagel.Image;

/**
 * Represents a banana object in the game that can be shot by intelligent monkeys, and travel
 * for up to a maximum distance before being destroyed, and have a set travel speed.
 * It is a subclass of Projectile, which handles the projectile path travelling
 */
public class Banana extends Projectile {
    private final double SPEED = 1.8;

    /**
     * Constructs a new banana at a specified position, with a specified direction.
     * @param x This is the x value the banana originates from
     * @param y The y value the banana originates from
     * @param right A boolean to see if the banana is shot to the rightward direction or not.
     */
    public Banana(double x, double y, boolean right) {
        super(x, y, right);
        setImg(new Image("res/banana.png"));
        setSpeed(SPEED);
    }

    /**
     * This method resets the banana by setting it as destroyed
     */
    @Override
    public void reset() {
        super.reset();
    }
}
