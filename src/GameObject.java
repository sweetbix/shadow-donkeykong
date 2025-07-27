import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Abstract parent class for all game objects in the game.
 * Provides common properties such as position in terms of (x,y) coordinates (in pixels), vertical velocity, and
 * tracks whether the object is destroyed or not
 */
public abstract class GameObject {
    private final Properties GAME_PROPS = IOUtils.readPropertiesFile("res/app.properties");
    private Image img;
    private double x;
    private double y;
    private double velocity;
    private boolean destroyed;

    /**
     * Resets the object to it's initial state. Used to reset levels. Needs to be implemented by concrete subclasses
     */
    public abstract void reset();

    /**
     * Method that can be called by other classes to see if a game object intersects with that object (by comparing
     * their images)
     * @param gameObject The object we are test to see if it intersects with this object
     * @return True if the two objects are intersecting, otherwise false.
     */
    public boolean intersects(GameObject gameObject) {
        Rectangle r1 = new Rectangle(new Point(gameObject.getX() - getImg().getWidth() / 2.0, gameObject.getY() - gameObject.getImg().getHeight() / 2.0), gameObject.getImg().getWidth(), gameObject.getImg().getHeight());
        Rectangle r2 = new Rectangle(new Point(this.x - this.img.getWidth() / 2.0, this.y - this.img.getHeight() / 2.0), this.img.getWidth(), this.img.getHeight());
        return r1.intersects(r2);
    }

    /**
     * Set an object's image. Generally a new instance of an Image (bagel class) read from the res folder
     * @param img A new image with a path file in the res folder
     */
    public void setImg(Image img) {
        this.img = img;
    }

    /**
     * Update an objects x coordinate. Used to update its position in the game
     * @param x The new x coordinate of the object
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Update an objects y coordinate. Used to update its position in the game
     * @param y The new y coordinate of the object
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Set the vertical velocity of an object. Generally used for falling physics.
     * @param velocity The new vertical velocity of this object
     */
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    /**
     * Change the state of if the object is destroyed or not. Can be used to destroy an object after interaction,
     * or reset the objects state by setting destroyed as false.
     * @param destroyed True if the object is now destroyed, otherwise false.
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    /**
     * Get the Image (bagel class) of this object. Used to draw objects with the .draw method, and to test for
     * intersections with other objects
     * @return An Image (bagel class) representing the image of the object
     */
    public Image getImg() {
        return this.img;
    }

    /**
     * Get the x coordinate of the object
     * @return The x coordinate of the object in pixels
     */
    public double getX() {
        return this.x;
    }

    /**
     * Get the y coordinate of the object
     * @return The y coordinate of the object in pixels
     */
    public double getY() {
        return this.y;
    }

    /**
     * Get the vertical velocity of the object
     * @return The vertical velocity of the object
     */
    public double getVelocity() {
        return this.velocity;
    }

    /**
     * Check to see whether the object is destroyed or not. Can be used to see if the object
     * should be rendered or not (destroyed objects are not rendered)
     * @return True if the current object is destroyed, false if not.
     */
    public boolean isDestroyed() {
        return this.destroyed;
    }
}
