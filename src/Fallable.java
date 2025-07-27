import java.util.ArrayList;

/**
 * An interface for objects which can fall subject to gravity.
 * Provides default implementations to detect if an object is currently in the air or grounded,
 * and a method to accelerate an object without exceeding the terminal velocity
 */
public interface Fallable {
    double MAX_VELOCITY = 10;


    /**
     * Method which compares an objects position to all platforms in the level to check if the object is
     * in the air, or grounded
     * @param platforms An array list of all platforms in the level
     * @param gameObject The object to check
     * @return True of the object is in the air, otherwise false if it is grounded on a platform
     */
    default boolean inAir(ArrayList<Platform> platforms, GameObject gameObject) {
        for (Platform p : platforms) {

            // Check if the object is in contact with this platform, with room for a collision error
            if (p.platformIntersect(gameObject)) {

                // Adjust the position of the entity to align with the platform, due to us having a collision error
                gameObject.setY(p.getY() - p.getImg().getHeight() / 2.0 - gameObject.getImg().getHeight() / 2.0);

                // If entity connects with a platform, then it should stop falling
                gameObject.setVelocity(0);

                return false;
            }
        }
        return true;
    }


    /**
     * Applies acceleration to a falling object, ensuring it does not exceed the terminal velocity. Updates
     * the objects position.
     * @param acceleration The rate of which the object is accelerating (generally it will be gravity)
     * @param gameObject The object to accelerate
     */
    default void accelerate(double acceleration, GameObject gameObject) {
        gameObject.setVelocity(Math.min(gameObject.getVelocity() + acceleration, MAX_VELOCITY));
        gameObject.setY(gameObject.getY() + gameObject.getVelocity());
    }
}
