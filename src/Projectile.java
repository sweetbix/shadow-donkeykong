/**
 * Parent class of all projectiles in the game. Projectiles are objects that are shot and travel a distance.
 * In this game, it is bananas and bullets which extend this class. This class handles the projectile travelling. It
 * extends GameObject to inherit basic properties
 */
public abstract class Projectile extends GameObject {
    private final double MAX_DISTANCE = 300;
    private double speed;
    private boolean right;
    private double currDistance;

    /**
     * Constructs a new projectile at a specified position with a specified direction
     * @param x The x coordinate of the projectile
     * @param y The y coordinate of the projectile
     * @param right True if the projectile is shot to the right, false if to the left
     */
    public Projectile(double x, double y, boolean right) {
        setX(x);
        setY(y);
        this.right = right;
        currDistance = 0;
        setDestroyed(false);
    }

    /**
     * Method which handles the projectiles travelling. Checks if they have travelled their maximum distance,
     * otherwise they continue travelling and the position is updated.
     * Assumption: we do not care if projectiles go outside window, as it won't hit anything anyway and will
     * be deleted after travelling the max distance
     */
    public void travel() {
        // check if projectile has reached the end of its path
        if (currDistance + speed > MAX_DISTANCE) {
            setDestroyed(true);
        } else {
            // check direction
            if (right) {
                setX(getX() + speed);
            } else {
                setX(getX() - speed);
            }

            currDistance += speed;
        }
    }

    /**
     * Reset state of a projectile. It will be destroyed
     */
    @Override
    public void reset() {
        setDestroyed(true);
    }

    /**
     * Set the speed of the projectile. This is used since bananas and bullets travel at different speeds and
     * will need to set it in their own classes.
     * @param speed The speed of the projectile (pixels / frame)
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
