import bagel.Image;
import bagel.Window;

import java.util.ArrayList;

/**
 * Represents mario, the main playable character of the game. Mario is able to move around, interact with objects,
 * collect weapons, and shoot bullets. This class handles most of marios interactions with other game objects.
 * It also implements Fallable which handles the falling physics for mario
 */
public class Mario extends Character implements Fallable {
    private final double GRAVITY = 0.2;
    private final double JUMP_VELOCITY = -5;
    private final double JUMP_HEIGHT = 65;
    private final double SPEED = 3.5;
    private boolean climbing;
    private boolean hammer;
    private boolean blaster;
    private Image marioL;
    private Image marioR;
    private Image marioLHammer;
    private Image marioRHammer;
    private Image marioLBlaster;
    private Image marioRBlaster;
    private int ammo;
    private boolean right;

    /**
     * Creates a new mario at the specified position (it will be his starting position) and loads in image files
     * and initialise state
     * @param x The starting x coordinate of mario
     * @param y The starting y coordinate of mario
     */
    public Mario(double x, double y) {
        marioL = new Image("res/mario_left.png");
        marioR = new Image("res/mario_right.png");
        marioLHammer = new Image("res/mario_hammer_left.png");
        marioRHammer = new Image("res/mario_hammer_right.png");
        marioLBlaster = new Image("res/mario_blaster_left.png");
        marioRBlaster = new Image("res/mario_blaster_right.png");

        setStartX(x);
        setStartY(y);
        setX(x);
        setY(y);
        setImg(marioR);
        setVelocity(0);
        climbing = false;
        hammer = false;
        blaster = false;
        ammo = 0;
        right = true;
        sethSpeed(SPEED);
        setvSpeed(SPEED);
    }


    // Below are methods used to handle ladder climbing

    /**
     * Method to test if mario is currently on (intersecting) a ladder, if so we accordingly
     * adjust the correct variables
     * @param ladders An array list of all the ladders in the level
     * @return True if mario is on a ladder, false if not
     */
    public boolean onLadder(ArrayList<Ladder> ladders) {
        for (Ladder l : ladders) {
            if (intersectingLadder(l)) {
                // Mario is on this ladder, set variables accordingly
                setVelocity(0);
                climbing = true;
                return true;
            }
        }
        climbing = false;
        return false;
    }

    /**
     * Test to see if the bottom of mario is above the very bottom pixel of the ladder.
     * Note that if he is on the bottom pixel, whilst he is still on the ladder, he should not be able to climb
     * down any further (otherwise he will climb down into a platform)
     * @param ladders An array list of all the platforms in the level
     * @return True of mario as above the bottom of the ladder, false if he is at the very bottom pixel
     */
    public boolean aboveLadderBottom(ArrayList<Ladder> ladders) {
        for (Ladder l : ladders) {
            // Find the ladder that mario is currently on (if he is on one)
            if (intersectingLadder(l)) {
                return getY() + getImg().getHeight() / 2.0 < l.getY() + l.getImg().getHeight() / 2.0;
            }
        }

        return false;
    }

    /**
     * Test to see if the bottom of mario is at the very top pixel of a ladder.
     * Note that if mario is on the top pixel, whilst he is still on the ladder, he should not be able to climb
     * up any further (otherwise he will climb off the top of the ladder)
     * @param ladders An array list of all ladders in the level
     * @return True if mario is at the very top pixel of a ladder
     */
    public boolean atLadderTop(ArrayList<Ladder> ladders) {
        for (Ladder l : ladders) {
            if (intersectingLadder(l)) {
                return getY() + getImg().getHeight() / 2.0 == l.getY() - l.getImg().getHeight() / 2.0;
            }
        }
        return false;
    }

    /**
     * Test to see if mario is "on" the ladder, which means testing to see if he is in between the middle bounds
     * @param l A single ladder
     * @return True if mario is on this ladder, false if not
     */
    private boolean intersectingLadder(Ladder l) {
        return getX() >= l.getX() - l.getImg().getWidth() / 2.0 && getX() <= l.getX() + l.getImg().getWidth() / 2.0
                && getY() + getImg().getHeight() / 2.0 >= l.getY() - l.getImg().getHeight() / 2.0 - 1
                && getY() <= l.getY() + l.getImg().getHeight() / 2.0;
    }

    /**
     * Override the inAir function from the Fallable interface to account for if mario is on a ladder. If he is,
     * then he is not in the air.
     * @param platforms An array list of all platforms in the level
     * @param gameObject The object to check
     * @return True if mario is midair, false if he is climbing a ladder or resting on a platform
     */
    @Override
    public boolean inAir(ArrayList<Platform> platforms, GameObject gameObject) {
        if (isClimbing()) {
            return false;
        }

        return Fallable.super.inAir(platforms, gameObject);
    }

    /**
     * Method used when the space-bar is pressed, which initiates a jump by giving mario a velocity upwards
     */
    public void startJump() {
        // check to see mario is currently grounded (not in a jump or in the air)
        if (getVelocity() == 0) {
            setVelocity(JUMP_VELOCITY);
            setY(getY() + getVelocity());
        }
    }

    // Below are methods used for interaction with barrels

    /**
     * Method to check if mario is intersecting any barrels. If he is holding the hammer while doing so,
     * we destroy that barrel.
     * @param barrels Array list of all barrels in the level
     * @return True if mario is touching a barrel, false if not
     */
    public boolean touchBarrel(ArrayList<Barrel> barrels) {
        for (Barrel b : barrels) {
            if (b.intersects(this) && !b.isDestroyed()) {
                if (hammer) {
                    b.setDestroyed(true);
                }
                return true;
            }
        }
        return false;
    }



    /**
     * Test if mario passes above the top of a barrel (as long as he passes lower than the maximum jump height), this
     * way we know that mario has jumped over a barrel. passBarrelR is for when mario is travelling to the right.
     * @param barrels Array list of all barrels in the level
     * @return True if he has passed the top of a barrel, to the right
     */
    public boolean passBarrelR(ArrayList<Barrel> barrels) {
        for (Barrel b : barrels) {
            if (!b.isDestroyed()
                    && getX() < b.getX() && getX() + gethSpeed() >= b.getX()
                    && getY() < b.getY() && b.getY() - b.getImg().getHeight() / 2.0 - getY() <= JUMP_HEIGHT) {
                return true;
            }
        }
        return false;
    }

    /**
     * Same as above method, but for when mario is travelling to the left
     * @param barrels Array list of all barrels in the level
     * @return True if he has passed the top of a barrel, to the left
     */
    public boolean passBarrelL(ArrayList<Barrel> barrels) {
        for (Barrel b : barrels) {
            if (!b.isDestroyed()
                    && getX() > b.getX() && getX() - gethSpeed() <= b.getX()
                    && getY() < b.getY() && b.getY() - b.getImg().getHeight() / 2.0 - getY() <= JUMP_HEIGHT) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to test if mario has made contact with donkey kong
     * @param donkey Donkey kong in the game
     * @return True if mario has made contact with donkey kong, false if not
     */
    public boolean touchDonkey(Donkey donkey) {
        return this.intersects(donkey);
    }

    /**
     * Method to test if mario is collecting a hammer. Handles the logic for hammer collection.
     * @param hammers All hammers existing in the current level
     * @return True if mario has collected a hammer, false if not
     */
    public boolean touchHammer(ArrayList<Hammer> hammers) {
        for (Hammer h : hammers) {
            if (!h.isDestroyed() && this.intersects(h)) {

                // Adjust image
                if (right) {
                     setImg(marioRHammer);
                } else {
                    setImg(marioLHammer);
                }

                // Hammer needs to be destroyed upon collecting it
                h.setDestroyed(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Method to test if mario is collecting a blaster. Handles the logic for blaster collection.
     * @param blasters All hammers existing in the current level
     * @return True if mario has collected a blaster, false if not
     */
    public boolean touchBlaster(ArrayList<Blaster> blasters) {
        for (Blaster b : blasters) {
            if (!b.isDestroyed() && this.intersects(b)) {

                // Adjust image
                if (right) {
                    setImg(marioRBlaster);
                } else {
                    setImg(marioLBlaster);
                }

                // Blaster is destroyed upon collection
                b.setDestroyed(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Method to check if a banana (shot by intelligent monkeys) has connected with mario
     * @param bananas An array list of all bananas existing in the level
     * @return True if a banana has connected with mario, otherwise false.
     */
    public boolean touchBanana(ArrayList<Banana> bananas) {
        for (Banana b : bananas) {
            if (this.intersects(b)) {
                return true;
                // we don't need to destroy the banana because the game will end anyway
            }
        }

        return false;
    }

    /**
     * Method to check if mario has made contact with normal monkeys OR intelligent monkeys. If mario has a hammer,
     * the monkeys are destroyed within this method as well.
     * @param normMonkeys All normal monkeys in the level
     * @param intelMonkeys A intelligent monkeys in the level
     * @return True if mario has made contact with a monkey, false if not
     */
    public boolean touchMonkeys(ArrayList<NormMonkey> normMonkeys, ArrayList<IntelMonkey> intelMonkeys) {
        for (NormMonkey m : normMonkeys) {
            if (this.intersects(m) && !m.isDestroyed()) {

                // For if mario has a hammer
                if (hammer) {
                    m.setDestroyed(true);
                }
                return true;
            }
        }

        for (IntelMonkey m : intelMonkeys) {
            if (this.intersects(m) && !m.isDestroyed()) {

                // For if mario has a hammer
                if (hammer) {
                    m.setDestroyed(true);
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Method which handles shooting logic for mario. Does a check to see if mario has ammo to shoot, and if so,
     * a bullet is created. If mario runs out of bullets, this method will also update the logic by removing the
     * blater and updating the mario image
     * @param bullets Array of all bullet in the game. This array will be updated with the new bullet (if shot)
     * @return Updated array with the newly shot bullet, otherwise if no bullet is shot this array will
     * be the same as the input array
     */
    public ArrayList<Bullet> shoot(ArrayList<Bullet> bullets) {
        if (ammo == 0) {
            // no ammo, can't shoot
            return bullets;
        }

        // create the bullet
        Bullet bullet = new Bullet(getX(), getY(), right);
        bullets.add(bullet);
        ammo--;

        // case for if we just shot the last bullet
        if (ammo == 0) {
            blaster = false;
            if (right) {
                setImg(marioR);
            } else {
                setImg(marioL);
            }
        }

        return bullets;
    }

    // Below are methods for movement, adjusting the image of mario when necessary

    /**
     * Method for mario to move left, updating his position. Activated by left arrow on the keyboard input.
     * Handles logic to ensure mario does not walk past screen boundaries, and updated directional variable 'right'.
     */
    public void left() {
        setX(Math.max(getX() - gethSpeed(), 0));
        if (hammer) {
            setImg(marioLHammer);
        } else if (blaster) {
            setImg(marioLBlaster);
        } else {
            setImg(marioL);
        }
        right = false;
    }

    /**
     * Method for mario to move right, updating his position. Activated by right arrow on the keyboard input.
     * Handles logic to ensure mario does not walk past screen boundaries, and updated directional variable 'right'.
     */
    public void right() {
        setX(Math.min(getX() + gethSpeed(), Window.getWidth()));
        if (hammer) {
            setImg(marioRHammer);
        } else if (blaster) {
            setImg(marioRBlaster);
        } else {
            setImg(marioR);
        }
        right = true;
    }

    /**
     * Method for mario to climb down ladders, updating his position
     */
    public void down() {
        setY(getY() + getvSpeed());
    }

    /**
     * Method for mario to climb up ladders, updating his position
     */
    public void up() {
        setY(getY() - getvSpeed());
    }

    /**
     * Reset state of mario. Used when resetting a level, or the entire game. All variables are reset to as they
     * were originally, and mario returns to his starting spawn position
     */
    @Override
    public void reset() {
        setX(getStartX());
        setY(getStartY());
        setVelocity(0);
        setImg(marioR);
        climbing = false;
        hammer = false;
        blaster = false;
        ammo = 0;
        right = true;
    }

    /**
     * Get the gravity constant affecting mario. Used for falling physics
     * @return The gravity constant for mario
     */
    public double getGRAVITY() {
        return GRAVITY;
    }

    /**
     * Check if mario is currently climbing a ladder or not
     * @return True if mario is climbing a ladder, false if not
     */
    public boolean isClimbing() {
        return climbing;
    }

    /**
     * Check if mario currently has a hammer or not
     * @return True if mario has a hammer, false if not
     */
    public boolean hasHammer() {
        return hammer;
    }

    /**
     * Change the state of if mario has a hammer. Used when he equips a blaster (hammer is removed)
     * @param hammer New hammer state
     */
    public void setHammer(boolean hammer) {
        this.hammer = hammer;
    }

    /**
     * Change the state of if mario has a blaster. Used when he equips a blaster (blaster is removed)
     * @param blaster New blaster state
     */
    public void setBlaster(boolean blaster) {
        this.blaster = blaster;
    }

    /**
     * Check if mario currently has a blaster or not
     * @return True if mario currently has a blaster, false if not
     */
    public boolean hasBlaster() {
        return blaster;
    }

    /**
     * Method to change the amount of ammo mario has. Used when he picks up a blaster, or if a blaster is removed
     * @param ammo A new amount of ammo mario has
     */
    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    /**
     * Get the amount of ammo mario currently has. Used to render this information in the top right of the screen
     * @return The amount of ammo mario currently has
     */
    public int getAmmo() {
        return ammo;
    }
}
