/**
 * Represents the second level of the game. Renders score, time, donkey health, and mario ammo information.
 * Extends the Level parent class which provides common level properties
 */
public class Level2 extends Level {
    private double donkeyHpX;
    private double donkeyHpY;

    /**
     * Creates a new level 2 instance. Reads in the display coordinates of Donkey HP and ammo
     */
    public Level2() {
        super();

        String coords = this.getGAME_PROPS().getProperty("gamePlay.donkeyhealth.coords");
        donkeyHpX = Double.parseDouble(coords.substring(0, coords.indexOf(",")));
        donkeyHpY = Double.parseDouble(coords.substring(coords.indexOf(",") + 1));
    }

    /**
     * Render the score, time left, donkey HP and ammo at the specified position
     * @param score Player score
     * @param donkeyHp Donkey kong remaining health
     * @param ammo Player remaining ammo
     */
    public void render(int score, int donkeyHp, int ammo) {
        this.getScoreFont().drawString(String.format("Score %d\n" + "Time Left %d", score, getCurrentFrame() / getFPS()),
                getScoreX(), getScoreY());
        this.setCurrentFrame(getCurrentFrame() - 1);

        this.getScoreFont().drawString(String.format("Donkey Health %d\n" + "Bullet %d", donkeyHp, ammo),
                donkeyHpX, donkeyHpY);
    }
}
