import bagel.Image;
import java.util.ArrayList;

/**
 * Represents a platform in the game. Game objects and characters are able to rest on platforms, and if they aren't
 * resting on a platform, then they are falling (except mario climbing a ladder). Information on platforms
 * is read from the game properties file
 */
public class Platform extends GameObject {
    private final double COLLISION_ERROR = 4.5;

    /**
     * Creates a new platform at a specified position. It will not move from this position. Also reads in image
     * from res folder
     * @param x The x coordinate of the platform centre
     * @param y The y coordinate of the platform centre
     */
    public Platform(double x, double y) {
        this.setX(x);
        this.setY(y);
        this.setImg(new Image("res/platform.png"));
    }

    /**
     * Method to read in the platform positions from the app properties file and create an arraylist of these platforms
     * for a specified level
     * @param points A string from game properties containing platform positions in the format of xx,yy:xx,yy:xx,yy
     * @return A new array list containing all barrels for a specified level
     */
    public static ArrayList<Platform> readPlatforms(String points) {
        ArrayList<Platform> platforms = new ArrayList<Platform>();

        // Find number of platforms
        int n = 0;
        for (int i = 0; i < points.length(); i++) {
            if (points.charAt(i) == ';') {
                n++;
            }
        }

        // increment to account for last one
        n++;

        // Read each individual platform, creating a new platform at that position and storing it in platforms
        for (int i = 1; i <= n; i++) {
            if (i == n) {
                // special case for last co-ordinate
                platforms.add(
                        new Platform(
                                Double.parseDouble(points.substring(0, points.indexOf(","))),
                                Double.parseDouble(points.substring(points.indexOf(',') + 1))
                ));
            } else {
                platforms.add(new Platform(
                        Double.parseDouble(points.substring(0, points.indexOf(","))),
                        Double.parseDouble(points.substring(points.indexOf(',') + 1, points.indexOf(';')))
                ));
                points = points.substring(points.indexOf(";") + 1);
            }
        }
        return platforms;
    }



    /**
     * Method that determines if another entity intersects this platform, with an error for collision if the entity
     * is moving
     * @param gameObject Any object to check with this platform
     * @return True of the gameObject is making contact with the platform, false if not
     */
    public boolean platformIntersect(GameObject gameObject) {
        return (gameObject.getY() + gameObject.getImg().getHeight() / 2.0 >= this.getY() - this.getImg().getHeight() / 2.0 - COLLISION_ERROR
                && gameObject.getY() + gameObject.getImg().getHeight() / 2.0 <= this.getY() - this.getImg().getHeight() / 2.0 + COLLISION_ERROR
                && gameObject.getX() >= this.getX() - this.getImg().getWidth() / 2.0
                && gameObject.getX() <= this.getX() + this.getImg().getWidth() / 2.0);
    }

    /**
     * Special method used specifically to test if a ladder is intersecting a platform, with more room for error.
     * Used to check if the ladder positions need to be adjusted.
     * @param ladder An arraylist of all ladders in the game
     * @return True of the ladder is unintentionally intersecting with this platform
     */
    public boolean ladderIntersectsPlatform(Ladder ladder) {
        return (ladder.getX() + ladder.getImg().getWidth() / 2.0 <= this.getX() + this.getImg().getWidth() / 2.0
        && ladder.getX() - ladder.getImg().getWidth() / 2.0 >= this.getX() - this.getImg().getWidth() / 2.0
        && ladder.getY() + ladder.getImg().getHeight() / 2.0 - 10 >= this.getY() + this.getImg().getHeight() / 2.0
        && ladder.getY() - ladder.getImg().getHeight() / 2.0 + 10 <= this.getY() - this.getImg().getHeight() / 2.0);
    }

    /**
     * No implementation as platforms are not reset
     */
    public void reset() {

    }
}
