import bagel.Image;
import bagel.Window;

import java.util.ArrayList;

/**
 * Parent class representing a monkey in the game. Has common properties such as speed, images, and variables
 * to handle path travelling.
 * Implements Fallable since monkeys will fall subject to gravity for the opening animation. Extends Character
 * to inherit basic character properties
 */
public abstract class Monkey extends Character implements Fallable {
    private final double GRAVITY = 0.4;
    private final double SPEED = 0.5;
    private Image rightImg;
    private Image leftImg;
    private boolean right;

    // Used to track how much of the current path section the monkey has walked
    private double currDistance;
    private ArrayList<Integer> path;

    // Used to track the index of the path which the monkey is currently walking
    private int step;
    private final double COLLISION_ERROR = 2;

    /**
     * Creates a new monkey at a specified posiiton, given a specified path. This information is read from the
     * game properties file, using another method.
     * @param x The starting x coordinate of the monkey
     * @param y The starting y coordinate of the monkey
     * @param right True if the monkey begins facing right, otherwise false
     * @param path An array list of integers which is the path which this monkey will travel
     */
    public Monkey(double x, double y, boolean right, ArrayList<Integer> path) {
        setStartX(x);
        setStartY(y);
        setX(x);
        setY(y);
        sethSpeed(SPEED);
        currDistance = 0;
        step = 0;
        this.right = right;
        this.path = path;
    }

    /**
     * Method which handles path travelling logic. First checks if the current path cannot be travelled anymore,
     * otherwise it continues travelling the current path and the position is updated
     * @param platforms An array list of every platform in the level
     */
    public void travelPath(ArrayList<Platform> platforms) {
        // step loops around to 0 if needed
        step = step % path.size();

        double currentPath = path.get(step);

        // checks the cases for if the monkey should switch direction and walk the next path
        if (currDistance + gethSpeed() > currentPath || atPlatformEdge(platforms) || atScreenEdge()) {
            // I have made an assumption here that if the remaining path is less than our speed, we will skip this.
            // i.e. if 0.25 to walk, but speed is 0.5, therefore we will skip
            step += 1;

            // change direction
            right = !right;
            currDistance = 0;
        } else {
            // we can keep travelling this path
            if (right) {
                setImg(rightImg);
                setX(getX() + gethSpeed());
            } else {
                setImg(leftImg);
                setX(getX() - gethSpeed());
            }

            currDistance += gethSpeed();
        }


    }

    /**
     * Helper method for path travelling. Detects if a monkey is at the edge of it's current platform (with room for
     * collision error.
     * @param platforms An array list of all platforms in the level
     * @return Boolean value true if the monkey is at a platform edge, and false if not
     */
    private boolean atPlatformEdge(ArrayList<Platform> platforms) {
        for (Platform p : platforms) {
            if (onPlatform(p)) {
                // we know that the monkey is at the same y value as this platform (allowing for a small error)

                // Values representing the x coordinate of the right edge of the platform, and the left edge
                double platformRight = p.getX() + p.getImg().getWidth() / 2.0;
                double platformLeft = p.getX() - p.getImg().getWidth() / 2.0;

                // Check if the monkey is close to either of these edges
                if (right && (Math.abs(getX() - (platformRight - getImg().getWidth() / 2.0)) <= COLLISION_ERROR)) {
                    return true;
                } else if (!right && Math.abs(getX() - (platformLeft + getImg().getWidth() / 2.0)) <= COLLISION_ERROR) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Helper method for the platform edge detection method. Checks if a monkey is on the current platform,
     * by comparing y values, with room for collision error.
     * @param p The platform to be compared with
     * @return True of the monkey is on this platform, false if not
     */
    private boolean onPlatform(Platform p) {
        double difference = (Math.abs(getY() + getImg().getHeight() / 2.0 - (p.getY() - p.getImg().getHeight() / 2.0)));
        return (difference < COLLISION_ERROR);
    }

    /**
     * Helper method for the path travelling. Checks if the monkey is at the edge of the screen on the x-axis. We make
     * the assumption that there will always be platforms blocking the bottom edge, and the top edge is never reached.
     * @return True if the monkey is at the edge of the screen, false if not
     */
    private boolean atScreenEdge() {
        if (right) {
            return getX() + getImg().getWidth() / 2.0 >= Window.getWidth();
        } else {
            return getX() - getImg().getWidth() / 2.0 <= 0;
        }
    }

    /**
     * Method to check if a bullet shot by mario has connected with this monkey. If so, the bullet is destroyed, and
     * this monkey is also destroyed (handled in the main class of the game)
     * @param bullets An arraylist of all bullets currently in the level
     * @return True of this monkey has been shot, false if not
     */
    public boolean touchBullet(ArrayList<Bullet> bullets) {
        for (Bullet b : bullets) {
            if (this.intersects(b)) {
                // bullet is destroyed upon contact
                b.setDestroyed(true);
                return true;
            }
        }

        return false;
    }

    /**
     * Helper method to read the path a monkey will follow.
     * @param points A substring from the game properties file, in the form of xx,yy,zz containing the path
     *               of the intelligent monkey
     * @return An array list of integers representing this path
     */
    public static ArrayList<Integer> readPath(String points) {
        ArrayList<Integer> path = new ArrayList<Integer>();

        while (points.contains(",")) {
            path.add(Integer.parseInt(
                    points.substring(0, points.indexOf((",")))
            ));
            points = points.substring(points.indexOf(",") + 1);
        }

        // last one
        path.add(Integer.parseInt(points));

        return path;
    }


    /**
     * Get the gravity constant affecting this monkey. Used for falling physics
     * @return Gravity constant affecting this monkey
     */
    public double getGRAVITY() {
        return GRAVITY;
    }

    /**
     * Method to reset the state of a monkey. Used to reset level 2
     */
    public void reset() {
        setX(getStartX());
        setY(getStartY());
        step = 0;
        currDistance = 0;
        right = true;
        setDestroyed(false);
    }

    /**
     * Method to check which direction the monkey is currently facing
     * @return True if the monkey is facing to the right, false if left
     */
    public boolean isRight() {
        return right;
    }

    /**
     * Edit the left image of the monkey. Used in constructors of concrete child classes
     * @param leftImg The image to be shown when a monkey is travelling to the left
     */
    public void setLeftImg(Image leftImg) {
        this.leftImg = leftImg;
    }

    /**
     * Edit the right image of the monkey. Used in constructors of concrete child classes
     * @param rightImg The image to be shown when a monkey is travelling to the right
     */
    public void setRightImg(Image rightImg) {
        this.rightImg = rightImg;
    }

    /**
     * Get the left image of the monkey
     * @return The image to be shown when the monkey is travelling left
     */
    public Image getLeftImg() {
        return leftImg;
    }

    /**
     * Get the right image of the monkey
     * @return The image to be shown when the monkey is travelling right
     */
    public Image getRightImg() {
        return rightImg;
    }
}
