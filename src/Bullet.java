import bagel.Image;

/**
 * Represents a bullet object in the game that can be shot by mario, and travel
 * for up to a maximum distance before being destroyed, and have a set travel speed.
 * It is a subclass of Projectile, which handles the projectile path travelling.
 * In contrast to bananas, bullets have a different image for when it is travelling left or right
 */
public class Bullet extends Projectile {
    private final double SPEED = 3.8;
    private Image leftImg;
    private Image rightImg;

    /**
     * Creates a bullet at a specified position with a specified direction. Also reads in the images from
     * res folder and stores them
     * @param x x coordinate of the bullet
     * @param y y coordinate of the bullet
     * @param right direction the bullet is travelling
     */
    public Bullet(double x, double y, boolean right) {
        super(x, y, right);
        leftImg = new Image("res/bullet_left.png");
        rightImg = new Image("res/bullet_right.png");

        // Initialise the correct image according to its travel direction
        if (right) {
            setImg(rightImg);
        } else {
            setImg(leftImg);
        }
        setSpeed(SPEED);

    }

    /**
     * Method to reset the state of a bullet
     */
    @Override
    public void reset() {
        super.reset();
    }
}
