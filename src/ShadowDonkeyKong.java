import bagel.*;

import java.util.ArrayList;
import java.util.Properties;

/**
 * The main class for the Shadow Donkey Kong game.
 * This class extends {@code AbstractGame} and is responsible for managing game initialization,
 * updates, rendering, and handling user input.
 *
 * It sets up the game world, initializes characters, platforms, ladders, and other game objects,
 * and runs the game loop to ensure smooth gameplay.
 */
public class ShadowDonkeyKong extends AbstractGame {
    // Constants
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private final Image BACKGROUND_IMG = new Image("res/background.png");
    private final int BLASTER_AMMO = 5;

    // Score handling
    private final int TIME_BONUS_FACTOR = 3;
    private final int BARREL_JUMP = 30;
    private final int BARREL_DESTROY = 100;
    private final int MONKEY_DESTROY = 100;

    // Screens
    StartScreen startScreen = new StartScreen();
    EndScreen endScreen = new EndScreen();
    Level1 level1 = new Level1();
    Level2 level2 = new Level2();

    // Boolean variables to handle navigation between screens
    private boolean gameStart = false;
    private boolean nextLevel = false;
    private boolean won = false;
    private boolean lost = false;

    private int score;

    // Stores all game objects (note separate arraylists are used for each level)
    private ArrayList<Platform> platforms1;
    private ArrayList<Platform> platforms2;

    private ArrayList<Barrel> barrels1;
    private ArrayList<Barrel> barrels2;

    private ArrayList<Ladder> ladders1;
    private ArrayList<Ladder> ladders2;

    private ArrayList<Hammer> hammers1;
    private ArrayList<Hammer> hammers2;

    private ArrayList<Blaster> blasters2;

    // Projectiles
    private ArrayList<Banana> bananas2;
    private ArrayList<Bullet> bullets2;

    // Characters
    private ArrayList<NormMonkey> normMonkeys2;
    private ArrayList<IntelMonkey> intelMonkeys2;

    // Singular objects (no need for an array list to store multiple)
    private Mario mario1;
    private Mario mario2;

    private Donkey donkey1;
    private Donkey donkey2;

    /**
     * Constructor for ShadowDonkeyKong game. All variables are initialised here, and arraylists are populated
     * using relevant static methods
     * @param gameProps The game properties file which information is read from
     * @param messageProps The message properties file which information is read from
     */
    public ShadowDonkeyKong(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("home.title"));

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;

        String point;

        score = 0;

        // Initialise all array lists using class static methods which read information directly from
        // the game properties file
        platforms1 = Platform.readPlatforms(gameProps.getProperty("platforms.level1"));
        platforms2 = Platform.readPlatforms(gameProps.getProperty("platforms.level2"));

        barrels1 = Barrel.readBarrels(1, gameProps);
        barrels2 = Barrel.readBarrels(2, gameProps);

        ladders1 = Ladder.readLadders(1, gameProps);
        ladders2 = Ladder.readLadders(2, gameProps);

        hammers1 = Hammer.readHammers(1, gameProps);
        hammers2 = Hammer.readHammers(2, gameProps);

        blasters2 = Blaster.readBlasters(2, gameProps);

        bananas2 = new ArrayList<Banana>();
        bullets2 = new ArrayList<Bullet>();

        // Read in starting positions of characters, by directly manipulating the strings from the game properties file
        point = gameProps.getProperty("mario.level1");
        mario1 = new Mario(Double.parseDouble(point.substring(0, point.indexOf(","))),
                Double.parseDouble(point.substring(point.indexOf(",") + 1)));

        point = gameProps.getProperty("mario.level2");
        mario2 = new Mario(Double.parseDouble(point.substring(0, point.indexOf(","))),
                Double.parseDouble(point.substring(point.indexOf(",") + 1)));

        point = gameProps.getProperty("donkey.level1");
        donkey1 = new Donkey(Double.parseDouble(point.substring(0, point.indexOf(","))),
                Double.parseDouble(point.substring(point.indexOf(",") + 1)));

        point = gameProps.getProperty("donkey.level2");
        donkey2 = new Donkey(Double.parseDouble(point.substring(0, point.indexOf(","))),
                Double.parseDouble(point.substring(point.indexOf(",") + 1)));

        normMonkeys2 = NormMonkey.readNormMonkeys(normMonkeys2, gameProps);
        intelMonkeys2 = IntelMonkey.readIntelMonkeys(gameProps);
    }


    /**
     * Render the relevant screen based on the keyboard input given by the user and the status of the gameplay.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        BACKGROUND_IMG.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        if (input.wasPressed(Keys.ESCAPE)) {
            // Close the game
            Window.close();

        } else if (!gameStart && input.wasPressed(Keys.ENTER)) {
            // Start the game
            gameStart = true;

        } else if (!gameStart && !nextLevel && input.wasPressed(Keys.NUM_2)) {
            // Skip to level 2
            gameStart = true;
            nextLevel = true;
        }


        if (!gameStart) {
            // Home screen
            startScreen.render();

        } else if (gameStart && !nextLevel && !lost && !won) {
            // Level 1
            level1.render(score);

            // This handles all object interactions and rendering
            objectRenderLevel1();

            // This handles all character interactions and rendering
            charRenderLevel1();


            // Handle mario movement with keyboard input
            if (input.isDown(Keys.RIGHT)) {
                if (mario1.passBarrelR(barrels1)) {
                    score += BARREL_JUMP;
                }
                mario1.right();
            }

            if (input.isDown(Keys.LEFT)) {
                if (mario1.passBarrelL(barrels1)) {
                    score += BARREL_JUMP;
                }
                mario1.left();
            }

            if (input.isDown(Keys.UP) && mario1.onLadder(ladders1) && !mario1.atLadderTop(ladders1)) {
                mario1.up();
            }

            if (input.isDown(Keys.DOWN) && mario1.onLadder(ladders1) && mario1.aboveLadderBottom(ladders1)) {
                mario1.down();
            }

            // We can not jump while we are midair, or if we are climbing a ladder
            if (input.wasPressed(Keys.SPACE) && !mario1.inAir(platforms1, mario1) && (!mario1.isClimbing() || mario1.isClimbing() && mario1.atLadderTop(ladders1))) {
                mario1.startJump();
            }

            // check conditions to lose game
            if (level1.getCurrentFrame() == 0 || (!mario1.hasHammer() && mario1.touchBarrel(barrels1)) ||
                    !mario1.hasHammer() && mario1.touchDonkey(donkey1)) {
                lost = true;
                won = false;
            }

            // condition to win level
            if (mario1.hasHammer() && mario1.touchDonkey(donkey1)) {
                nextLevel = true;
            }

            // Handle win loss and advancing level (on a win)
            if (won || lost) {
                if (lost) {
                    score = 0;
                } else {
                    nextLevel = true;
                }
            }


        } else if (gameStart && nextLevel && !lost && !won) {

            // Level 2 (time will reset from level 1)
            level2.render(score, donkey2.getHealth(), mario2.getAmmo());

            // This handles all object interactions and rendering
            objectRenderLevel2();

            // This handles all character interactions and rendering
            charRenderLevel2();


            // Handle mario movement with keyboard input
            if (input.isDown(Keys.RIGHT)) {
                if (mario2.passBarrelR(barrels2)) {
                    score += BARREL_JUMP;
                }
                mario2.right();
            }

            if (input.isDown(Keys.LEFT)) {
                if (mario2.passBarrelL(barrels2)) {
                    score += BARREL_JUMP;
                }
                mario2.left();
            }

            if (input.isDown(Keys.UP) && mario2.onLadder(ladders2) && !mario2.atLadderTop(ladders2)) {
                mario2.up();
            }

            if (input.isDown(Keys.DOWN) && mario2.onLadder(ladders2) && mario2.aboveLadderBottom(ladders2)) {
                mario2.down();
            }

            // We can not jump while we are midair, or if we are climbing a ladder
            if (input.wasPressed(Keys.SPACE) && !mario2.inAir(platforms2, mario2) && (!mario2.isClimbing() || mario2.isClimbing() && mario2.atLadderTop(ladders2))) {
                mario2.startJump();
            }

            // Handle blaster shooting
            if (input.wasPressed(Keys.S)) {
                mario2.shoot(bullets2);
            }

            // check conditions to lose game
            if (checkLoseConditions()) {
                lost = true;
                won = false;
            }

            // Check conditions to win level
            if (mario2.hasHammer() && mario2.touchDonkey(donkey2) || donkey2.getHealth() == 0) {
                donkey2.setHealth(0);
                won = true;
                lost = false;

                // Add time bonus to score
                score += TIME_BONUS_FACTOR * level2.getCurrentFrame() / level2.getFPS();
            }


        } else {

            // Score is reset on a loss
            if (lost) {
                score = 0;
            }

            endScreen.render(won, score);

            if (input.wasPressed(Keys.SPACE)) {

                // Reset all variables for a new start
                resetLevel1();
                resetLevel2();

            }
        }
    }

    /**
     * Method used to render all level 1 objects onto the game screen by drawing their images
     * Checks are performed for destroyable objects to see if it is destroyed or not, before rendering them
     * Object positions are also adjusted using another method, in case they are intersecting with platforms
     */
    private void objectRenderLevel1() {

        // Render platforms
        for (Platform p : platforms1) {
            p.getImg().draw(p.getX(), p.getY());
        }

        // Render (non-destroyed) barrels
        for (Barrel b : barrels1) {
            if (!b.isDestroyed()) {
                b.adjustPosition(platforms1);
                b.getImg().draw(b.getX(), b.getY());
            }
        }

        // Render ladders
        for (Ladder l : ladders1) {
            l.adjustPosition(platforms1);
            l.getImg().draw(l.getX(), l.getY());
        }

        // Render (non-destroyed) hammers
        for (Hammer h : hammers1) {
            if (!h.isDestroyed()) {
                h.getImg().draw(h.getX(), h.getY());
            }
        }

        // Falling animations
        for (Barrel b : barrels1) {
            if (b.inAir(platforms1, b)) {
                b.accelerate(b.getGRAVITY(), b);
            }
        }

        for (Ladder l : ladders1) {
            if (l.inAir(platforms1, l)) {
                l.accelerate(l.getGRAVITY(), l);
            }
        }

    }

    /**
     * Method used to render all game characters in level 1. Also handles interaction between mario and other objects
     * such as hammers and barrels.
     */
    private void charRenderLevel1() {
        donkey1.getImg().draw(donkey1.getX(), donkey1.getY());
        mario1.getImg().draw(mario1.getX(), mario1.getY());

        // Opening falling animation for donkey kong
        if (donkey1.inAir(platforms1, donkey1)) {
            donkey1.accelerate(donkey1.getGRAVITY(), donkey1);
        }

        // Check if mario has collected a hammer
        if (mario1.touchHammer(hammers1)) {
            mario1.setHammer(true);
        }

        // Handle case if mario has walked over ladder
        mario1.onLadder(ladders1);

        // Handle mario falling physics
        if (mario1.inAir(platforms1, mario1)) {
            mario1.accelerate(mario1.getGRAVITY(), mario1);
        }

        // Points gain
        if (mario1.hasHammer() && mario1.touchBarrel(barrels1)) {
            score += BARREL_DESTROY;
        }

    }

    /**
     * Renders all in game objects for level 2. Same format as objectRenderLevel1.
     * Additions to level 2 include blasters, and also bananas and bullets. Both projectiles are handled in the same
     * way.
     */
    private void objectRenderLevel2() {
        // Render platforms
        for (Platform p : platforms2) {
            p.getImg().draw(p.getX(), p.getY());
        }

        // Render (non-destroyed) barrels
        for (Barrel b : barrels2) {
            if (!b.isDestroyed()) {
                b.adjustPosition(platforms2);
                b.getImg().draw(b.getX(), b.getY());
            }
        }

        // Render ladders
        for (Ladder l : ladders2) {
            l.adjustPosition(platforms2);
            l.getImg().draw(l.getX(), l.getY());
        }

        // Render (non-destroyed) hammers
        for (Hammer h : hammers2) {
            if (!h.isDestroyed()) {
                h.getImg().draw(h.getX(), h.getY());
            }
        }

        // Render (non-destroyed) blasters
        for (Blaster b : blasters2) {
            if (!b.isDestroyed()) {
                b.getImg().draw(b.getX(), b.getY());
            }
        }

        // Handle (non-destroyed) banana travelling and rendering
        for (int i = 0; i < bananas2.size(); i++) {
            Banana b = bananas2.get(i);

            if (b.isDestroyed()) {
                bananas2.remove(i);
                i--;
            } else {
                b.travel();
                b.getImg().draw(b.getX(), b.getY());
            }
        }

        // Handle (non-destroyed) bullet travelling and rendering
        for (int i = 0; i < bullets2.size(); i++) {
            Bullet b = bullets2.get(i);

            if (b.isDestroyed()) {
                bullets2.remove(i);
                i--;
            } else {
                b.travel();
                b.getImg().draw(b.getX(), b.getY());
            }
        }

        // Falling animations
        for (Barrel b : barrels2) {
            if (b.inAir(platforms2, b)) {
                b.accelerate(b.getGRAVITY(), b);
            }
        }

        for (Ladder l : ladders2) {
            if (l.inAir(platforms2, l)) {
                l.accelerate(l.getGRAVITY(), l);
            }
        }


    }

    /**
     * Render all characters on level 2. Handles the movement of Monkeys, interaction between mario, blasters and
     * hammers, ammo gain, mario interaction with barrels and monkeys, and monkey interaction with bullets
     */
    private void charRenderLevel2() {
        donkey2.getImg().draw(donkey2.getX(), donkey2.getY());
        mario2.getImg().draw(mario2.getX(), mario2.getY());

        // Render normal monkeys
        for (NormMonkey m : normMonkeys2) {
            // Skip destroyed monkeys
            if (m.isDestroyed()) {
                continue;
            }

            // Opening animation for falling monkey
            if (m.inAir(platforms2, m)) {
                m.accelerate(m.getGRAVITY(), m);
            }

            // Handle path travelling and rendering
            m.travelPath(platforms2);
            m.getImg().draw(m.getX(), m.getY());

            // Check if a bullet has connected with the monkey
            if (m.touchBullet(bullets2)) {
                m.setDestroyed(true);
                score += MONKEY_DESTROY;
            }
        }

        // Same as above, but for intelligent monkeys, but this also handles banana shooting
        for (IntelMonkey m : intelMonkeys2) {
            // Skip destroyed monkeys
            if (m.isDestroyed()) {
                continue;
            }

            if (m.inAir(platforms2, m)) {
                m.accelerate(m.getGRAVITY(), m);
            } else {
                // only start shooting when we have landed
                m.updateTime(level2.getFPS());
                m.shoot(bananas2);
            }
            m.travelPath(platforms2);
            m.getImg().draw(m.getX(), m.getY());

            // Check if a bullet has connected with the monkey
            if (m.touchBullet(bullets2)) {
                m.setDestroyed(true);
                score += MONKEY_DESTROY;
            }
        }

        // Donkey opening falling animation
        if (donkey2.inAir(platforms2, donkey2)) {
            donkey2.accelerate(donkey2.getGRAVITY(), donkey2);
        }

        // Handle mario falling physics
        if (mario2.inAir(platforms2, mario2)) {
            mario2.accelerate(mario2.getGRAVITY(), mario2);
        }

        // Mario interaction with hammers
        if (mario2.touchHammer(hammers2)) {
            mario2.setHammer(true);
            mario2.setBlaster(false);
            mario2.setAmmo(0);
        }

        // Mario interaction with blasters
        if (mario2.touchBlaster(blasters2)) {
            if (mario2.hasBlaster()) {
                // add ammo to his current count
                mario2.setAmmo(mario2.getAmmo() + BLASTER_AMMO);
            } else {
                mario2.setAmmo(BLASTER_AMMO);
            }
            mario2.setBlaster(true);
            mario2.setHammer(false);
        }

        // Handle case if mario has walked over ladder
        mario2.onLadder(ladders2);

        // Points gain
        if (mario2.hasHammer() && mario2.touchBarrel(barrels2)) {
            score += BARREL_DESTROY;
        }

        if (mario2.hasHammer() && mario2.touchMonkeys(normMonkeys2, intelMonkeys2)) {
            score += MONKEY_DESTROY;
        }

        // Check if a bullet has connected with donkey kong and handle if so
        donkey2.touchBullet(bullets2);
    }

    /**
     * Helper method to check the conditions to lose level 2.
     * Checks if time runs out, if mario touches a barrel, or donkey kong, or is touched by a banana
     * or if a monkey touches mario (all with mario having no hammer)
     */
    private boolean checkLoseConditions() {
        return (level2.getCurrentFrame() == 0 || (!mario2.hasHammer() && mario2.touchBarrel(barrels2))
                || (!mario2.hasHammer() && mario2.touchDonkey(donkey2)) || mario2.touchBanana(bananas2)
                || (!mario2.hasHammer() && mario2.touchMonkeys(normMonkeys2, intelMonkeys2)));
    }

    /**
     * Helper method to reset all variables for level 1
     */
    private void resetLevel1() {
        score = 0;
        gameStart = false;
        won = false;
        lost = false;
        nextLevel = false;
        mario1.reset();
        level1.reset();

        for (Barrel b : barrels1) {
            b.reset();
        }

        for (Hammer h : hammers1) {
            h.reset();
        }
    }

    /**
     * Helper method to reset all variables for level 1 and 2
     */
    private void resetLevel2() {
        resetLevel1();
        mario2.reset();
        level2.reset();
        donkey2.reset();

        for (Barrel b : barrels2) {
            b.reset();
        }

        for (Hammer h : hammers2) {
            h.reset();
        }

        for (Blaster b : blasters2) {
            b.reset();
        }

        for (NormMonkey m : normMonkeys2) {
            m.reset();
        }

        for (IntelMonkey m : intelMonkeys2) {
            m.reset();
        }

        bananas2 = new ArrayList<Banana>();
        bullets2 = new ArrayList<Bullet>();

    }

    /**
     * The main entry point of the Shadow Donkey Kong game.
     *
     * This method loads the game properties and message files, initializes the game,
     * and starts the game loop.
     *
     * @param args Command-line arguments (not used in this game).
     */
    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile("res/app.properties");
        Properties messageProps = IOUtils.readPropertiesFile("res/message.properties");
        ShadowDonkeyKong game = new ShadowDonkeyKong(gameProps, messageProps);
        game.run();
    }


}
