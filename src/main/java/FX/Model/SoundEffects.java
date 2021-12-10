package FX.Model;

import FX.Model.Entities.Brick.Brick;
import javafx.scene.media.AudioClip;

import java.util.Random;

/**
 * this class contains all the sound effects for the gameplay.
 */
public class SoundEffects {
    private AudioClip clayBrickCollisionSound1;
    private AudioClip clayBrickCollisionSound2;
    private AudioClip gameWindowCollisionSound1;
    private AudioClip gameWindowCollisionSound2;
    private AudioClip ballPlayerCollisionSound1;
    private AudioClip ballPlayerCollisionSound2;
    private AudioClip steelBrickCollisionSound1;
    private AudioClip steelBrickCollisionSound2;
    private AudioClip cementBrickCollisionSound;
    private AudioClip cementBrickDestroyedSound;

    private AudioClip victorySound;
    private AudioClip lostSound;

    private Random rnd = new Random();

    /**
     * this constructor is used to set all the sound effects into a variable for future reference.
     */
    public SoundEffects(){
        setClayBrickCollisionSound1(new AudioClip(getClass().getResource("/SoundEffects/ClayImpactSound1.mp3").toExternalForm()));
        setClayBrickCollisionSound2(new AudioClip(getClass().getResource("/SoundEffects/ClayImpactSound2.mp3").toExternalForm()));
        setSteelBrickCollisionSound1(new AudioClip(getClass().getResource("/SoundEffects/SteelImpactSound1.mp3").toExternalForm()));
        setSteelBrickCollisionSound2(new AudioClip(getClass().getResource("/SoundEffects/SteelImpactSound2.mp3").toExternalForm()));
        setVictorySound(new AudioClip(getClass().getResource("/SoundEffects/VictorySound.wav").toExternalForm()));
        setLostSound(new AudioClip(getClass().getResource("/SoundEffects/DefeatSound.wav").toExternalForm()));
        setGameWindowCollisionSound1(new AudioClip(getClass().getResource("/SoundEffects/WindowImpact1.mp3").toExternalForm()));
        setGameWindowCollisionSound2(new AudioClip(getClass().getResource("/SoundEffects/WindowImpact2.mp3").toExternalForm()));
        setBallPlayerCollisionSound1(new AudioClip(getClass().getResource("/SoundEffects/BallPlayerImpactSound1.mp3").toExternalForm()));
        setBallPlayerCollisionSound2(new AudioClip(getClass().getResource("/SoundEffects/BallPlayerImpactSound2.mp3").toExternalForm()));
        setCementBrickCollisionSound(new AudioClip(getClass().getResource("/SoundEffects/CementImpactSound.wav").toExternalForm()));
        setCementBrickDestroyedSound(new AudioClip(getClass().getResource("/SoundEffects/CementBreak.mp3").toExternalForm()));
    }

    /**
     * this method is used to set one of the clay brick collision sound effect which is caused when the ball collides with the ball.
     *
     * @param clayBrickCollisionSound1 this is the audio clip used to set into a variable.
     */
    public void setClayBrickCollisionSound1(AudioClip clayBrickCollisionSound1) {
        this.clayBrickCollisionSound1 = clayBrickCollisionSound1;
    }

    /**
     * this method is used to set the other clay brick collision sound effect which is caused when the ball collides with the ball.
     *
     * @param clayBrickCollisionSound2 this is the audio clip used to set into a variable.
     */
    public void setClayBrickCollisionSound2(AudioClip clayBrickCollisionSound2) {
        this.clayBrickCollisionSound2 = clayBrickCollisionSound2;
    }

    /**
     * this method is used to set one of the audio clip used for window collision between the ball and the window.
     *
     * @param gameWindowCollisionSound1 this is the audio clip used to set into a variable.
     */
    public void setGameWindowCollisionSound1(AudioClip gameWindowCollisionSound1) {
        this.gameWindowCollisionSound1 = gameWindowCollisionSound1;
    }

    /**
     * this method is used to set the other sound audio clip for the window collision between the ball and the window.
     *
     * @param gameWindowCollisionSound2 this is the audio clip used to set into a variable.
     */
    public void setGameWindowCollisionSound2(AudioClip gameWindowCollisionSound2) {
        this.gameWindowCollisionSound2 = gameWindowCollisionSound2;
    }

    /**
     * this method is used to set one of the audio clip for the player (paddle) ball collision.
     *
     * @param ballPlayerCollisionSound1 this is the audio clip used to set into a variable.
     */
    public void setBallPlayerCollisionSound1(AudioClip ballPlayerCollisionSound1) {
        this.ballPlayerCollisionSound1 = ballPlayerCollisionSound1;
    }

    /**
     * this method is used to set the other audio clip for the player (paddle) ball collision
     *
     * @param ballPlayerCollisionSound2 this is the audio clip used to set into a variable.
     */
    public void setBallPlayerCollisionSound2(AudioClip ballPlayerCollisionSound2) {
        this.ballPlayerCollisionSound2 = ballPlayerCollisionSound2;
    }

    /**
     * this method is used to set the audio clip used for the collision between the steel brick or the reinforced steel brick and the ball.
     *
     * @param steelBrickCollisionSound1 this is the audio clip used to set into a variable.
     */
    public void setSteelBrickCollisionSound1(AudioClip steelBrickCollisionSound1) {
        this.steelBrickCollisionSound1 = steelBrickCollisionSound1;
    }

    /**
     * this method is used to set the other steel brick sound collision between the ball and the steel brick
     *
     * @param steelBrickCollisionSound2 this is the audio clip used to set into a variable.
     */
    public void setSteelBrickCollisionSound2(AudioClip steelBrickCollisionSound2) {
        this.steelBrickCollisionSound2 = steelBrickCollisionSound2;
    }

    /**
     * this method is used to set the cement brick collision between the ball and the cement brick.
     *
     * @param cementBrickCollisionSound this is the audio clip used to set into a variable.
     */
    public void setCementBrickCollisionSound(AudioClip cementBrickCollisionSound) {
        this.cementBrickCollisionSound = cementBrickCollisionSound;
    }

    /**
     * this method is used to set the breaking of the cement brick.
     *
     * @param cementBrickDestroyedSound this is the audio clip used to set into a variable.
     */
    public void setCementBrickDestroyedSound(AudioClip cementBrickDestroyedSound) {
        this.cementBrickDestroyedSound = cementBrickDestroyedSound;
    }

    /**
     * this method is used to set the victory sound effect when the player successfully beat a level.
     *
     * @param victorySound this is the audio clip used to set into a variable.
     */
    public void setVictorySound(AudioClip victorySound) {
        this.victorySound = victorySound;
    }

    /**
     * this method is used to set the defeat sound effect when the player failed to beat a level.
     *
     * @param lostSound this is the audio clip used to set into a variable.
     */
    public void setLostSound(AudioClip lostSound) {
        this.lostSound = lostSound;
    }

    /**
     * this method is used to get the audio clip used when the ball collides with the clay brick.
     *
     * @return this is the audio clip being returned to be used to indicate that the clay brick is being collided.
     */
    public AudioClip getClayBrickCollisionSound1() {
        return clayBrickCollisionSound1;
    }

    /**
     * this method is used to get the audio clip used when the ball collides with the clay brick.
     *
     * @return this is the audio clip being returned to be used to indicate that the clay brick is collided.
     */
    public AudioClip getClayBrickCollisionSound2() {
        return clayBrickCollisionSound2;
    }

    /**
     * this method is used to get the audio clip used when the ball collides with the game window side.
     *
     * @return this is the audio clip being returned to be used to indicate that the game window have been impacted.
     */
    public AudioClip getGameWindowCollisionSound1() {
        return gameWindowCollisionSound1;
    }

    /**
     * this method is used to get the audio clip used when the ball collides with the game window side.
     *
     * @return this is the audio clip being returned to be used to indicate that the game window have been impacted.
     */
    public AudioClip getGameWindowCollisionSound2() {
        return gameWindowCollisionSound2;
    }

    /**
     * this method is used to get the audio clip used when the ball collides with the player paddle.
     *
     * @return this is the audio clip being returned to be used to indicate that the player paddle have been impacted.
     */
    public AudioClip getBallPlayerCollisionSound1() {
        return ballPlayerCollisionSound1;
    }

    /**
     * this method is used to get the audio clip used when the ball collides with the player paddle.
     *
     * @return this is the audio clip being returned to be used to indicate that the player paddle have been impacted.
     */
    public AudioClip getBallPlayerCollisionSound2() {
        return ballPlayerCollisionSound2;
    }

    /**
     * this method is used to get the audio clip used when the steel brick gotten collided with the steel brick.
     *
     * @return this is the audio clip being returned to be used to indicate that the steel brick or the reinforced steel brick is collided.
     */
    public AudioClip getSteelBrickCollisionSound1() {
        return steelBrickCollisionSound1;
    }

    /**
     * this method is used to get the audio clip used when the steel brick gotten collided with the steel brick.
     *
     * @return this is the audio clip being returned to be used to indicate that the steel brick or the reinforced steel brick is collided.
     */
    public AudioClip getSteelBrickCollisionSound2() {
        return steelBrickCollisionSound2;
    }

    /**
     * this method is used to get the audio clip used when the cement brick is getting collided.
     *
     * @return this is the audio clip used when the cement brick is being collided.
     */
    public AudioClip getCementBrickCollisionSound() {
        return cementBrickCollisionSound;
    }

    /**
     * this method is used to get the audio clip used when the cement brick is broken.
     *
     * @return this is the audio clip used when the cement brick is broken.
     */
    public AudioClip getCementBrickDestroyedSound() {
        return cementBrickDestroyedSound;
    }

    /**
     * this method is used to get the victory audio clip when the user successfully beat a level.
     *
     * @return this is the audio clip used when the user successfully beat a level.
     */
    public AudioClip getVictorySound() {
        return victorySound;
    }

    /**
     * this method is used to get a defeat audio clip when the user failed to beat a level.
     *
     * @return this is the audio clip used when the user failed to beat a level.
     */
    public AudioClip getLostSound() {
        return lostSound;
    }

    /**
     * this method is used to play a random sound effect when there is a collision from the ball.
     *  @param soundEffect1 this is one of the sound effect which might be selected based on a random probability
     * @param soundEffect2 this is the other sound effect which might be selected based on a random probability
     */
    public void ballCollisionRandomSound(AudioClip soundEffect1, AudioClip soundEffect2) {
        if (rnd.nextBoolean())
            soundEffect1.play();
        else
            soundEffect2.play();
    }

    /**
     * this method is used to play a sound effect when the brick collides with a brick.
     *
     * @param b this is the brick which is being collided by the ball.
     */
    public void playBrickSoundEffect(Brick b){
        switch (b.getBrickName()){
            case "Clay Brick":
                ballCollisionRandomSound(getClayBrickCollisionSound1(), getClayBrickCollisionSound2());
                break;
            case "Reinforced Steel Brick":
            case "Steel Brick":
                ballCollisionRandomSound(getSteelBrickCollisionSound1(), getSteelBrickCollisionSound2());
                break;
            case "Cement Brick":
                if(b.getCurrentStrength() == b.getMaxStrength())
                    getCementBrickCollisionSound().play();
                else
                    getCementBrickDestroyedSound().play();
                break;
            default:
        }
    }
}
