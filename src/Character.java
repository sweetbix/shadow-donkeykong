/**
 * Abstract parent class for all characters in the game (mario, donkey kong, monkeys)
 */
public abstract class Character extends GameObject {
    private double startX;
    private double startY;
    private double hSpeed;
    private double vSpeed;

    /**
     * Get the starting x coordinate of a character. Used to reset that character after it has moved away from this
     * position
     * @return The initial x coordinate of a character
     */
    public double getStartX() {
        return startX;
    }

    /**
     * Get the starting y coordinate of a character. Used to reset that character after it has moved away from this
     * position
     * @return The initial y coordinate of a character
     */
    public double getStartY() {
        return startY;
    }

    /**
     * Get the horizontal speed (pixels / frame) of a character. Used for character movement
     * @return The horizontal speed of a character in pixels / frame
     */
    public double gethSpeed() {
        return hSpeed;
    }

    /**
     * Get the vertical speed (pixels / frame) of a character. Used for character movement (up and down ladders)
     * @return The vertical speed of a character in pixels / frame
     */
    public double getvSpeed() {
        return vSpeed;
    }

    /**
     * Set the initial starting x coordinate of a character. Generally used in constructors
     * @param startX The starting x coordinate of a character
     */
    public void setStartX(double startX) {
        this.startX = startX;
    }

    /**
     * Set the initial starting y coordinate of a character. Generally used in constructors
     * @param startY The starting y coordinate of a character
     */
    public void setStartY(double startY) {
        this.startY = startY;
    }

    /**
     * Set the horizontal speed (pixels / frame). Generally used in constructors
     * @param hSpeed The horizontal speed (pixels / frame) of a character
     */
    public void sethSpeed(double hSpeed) {
        this.hSpeed = hSpeed;
    }

    /**
     * Set the vertical speed (pixels / frame). Generally used in constructors
     * @param vSpeed The vertical speed (pixels / frame) of a character
     */
    public void setvSpeed(double vSpeed) {
        this.vSpeed = vSpeed;
    }
}
