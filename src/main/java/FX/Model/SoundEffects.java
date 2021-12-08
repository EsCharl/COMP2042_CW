package FX.Model;

import javafx.scene.media.AudioClip;

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

    public SoundEffects(){
        clayBrickCollisionSound1 = new AudioClip(getClass().getResource("/SoundEffects/ClayImpactSound1.mp3").toExternalForm());
        clayBrickCollisionSound2 = new AudioClip(getClass().getResource("/SoundEffects/ClayImpactSound2.mp3").toExternalForm());
        steelBrickCollisionSound1 = new AudioClip(getClass().getResource("/SoundEffects/SteelImpactSound1.mp3").toExternalForm());
        steelBrickCollisionSound2 = new AudioClip(getClass().getResource("/SoundEffects/SteelImpactSound2.mp3").toExternalForm());
        victorySound = new AudioClip(getClass().getResource("/SoundEffects/VictorySound.wav").toExternalForm());
        lostSound = new AudioClip(getClass().getResource("/SoundEffects/DefeatSound.wav").toExternalForm());
        gameWindowCollisionSound1 = new AudioClip(getClass().getResource("/SoundEffects/WindowImpact1.mp3").toExternalForm());
        gameWindowCollisionSound2 = new AudioClip(getClass().getResource("/SoundEffects/WindowImpact2.mp3").toExternalForm());
        ballPlayerCollisionSound1 = new AudioClip(getClass().getResource("/SoundEffects/BallPlayerImpactSound1.mp3").toExternalForm());
        ballPlayerCollisionSound2 = new AudioClip(getClass().getResource("/SoundEffects/BallPlayerImpactSound2.mp3").toExternalForm());
        cementBrickCollisionSound = new AudioClip(getClass().getResource("/SoundEffects/CementImpactSound.wav").toExternalForm());
        cementBrickDestroyedSound = new AudioClip(getClass().getResource("/SoundEffects/CementBreak.mp3").toExternalForm());
    }

    public void setClayBrickCollisionSound1(AudioClip clayBrickCollisionSound1) {
        this.clayBrickCollisionSound1 = clayBrickCollisionSound1;
    }

    public void setClayBrickCollisionSound2(AudioClip clayBrickCollisionSound2) {
        this.clayBrickCollisionSound2 = clayBrickCollisionSound2;
    }

    public void setGameWindowCollisionSound1(AudioClip gameWindowCollisionSound1) {
        this.gameWindowCollisionSound1 = gameWindowCollisionSound1;
    }

    public void setGameWindowCollisionSound2(AudioClip gameWindowCollisionSound2) {
        this.gameWindowCollisionSound2 = gameWindowCollisionSound2;
    }

    public void setBallPlayerCollisionSound1(AudioClip ballPlayerCollisionSound1) {
        this.ballPlayerCollisionSound1 = ballPlayerCollisionSound1;
    }

    public void setBallPlayerCollisionSound2(AudioClip ballPlayerCollisionSound2) {
        this.ballPlayerCollisionSound2 = ballPlayerCollisionSound2;
    }

    public void setSteelBrickCollisionSound1(AudioClip steelBrickCollisionSound1) {
        this.steelBrickCollisionSound1 = steelBrickCollisionSound1;
    }

    public void setSteelBrickCollisionSound2(AudioClip steelBrickCollisionSound2) {
        this.steelBrickCollisionSound2 = steelBrickCollisionSound2;
    }

    public void setCementBrickCollisionSound(AudioClip cementBrickCollisionSound) {
        this.cementBrickCollisionSound = cementBrickCollisionSound;
    }

    public void setCementBrickDestroyedSound(AudioClip cementBrickDestroyedSound) {
        this.cementBrickDestroyedSound = cementBrickDestroyedSound;
    }

    public void setVictorySound(AudioClip victorySound) {
        this.victorySound = victorySound;
    }

    public void setLostSound(AudioClip lostSound) {
        this.lostSound = lostSound;
    }

    public AudioClip getClayBrickCollisionSound1() {
        return clayBrickCollisionSound1;
    }

    public AudioClip getClayBrickCollisionSound2() {
        return clayBrickCollisionSound2;
    }

    public AudioClip getGameWindowCollisionSound1() {
        return gameWindowCollisionSound1;
    }

    public AudioClip getGameWindowCollisionSound2() {
        return gameWindowCollisionSound2;
    }

    public AudioClip getBallPlayerCollisionSound1() {
        return ballPlayerCollisionSound1;
    }

    public AudioClip getBallPlayerCollisionSound2() {
        return ballPlayerCollisionSound2;
    }

    public AudioClip getSteelBrickCollisionSound1() {
        return steelBrickCollisionSound1;
    }

    public AudioClip getSteelBrickCollisionSound2() {
        return steelBrickCollisionSound2;
    }

    public AudioClip getCementBrickCollisionSound() {
        return cementBrickCollisionSound;
    }

    public AudioClip getCementBrickDestroyedSound() {
        return cementBrickDestroyedSound;
    }

    public AudioClip getVictorySound() {
        return victorySound;
    }

    public AudioClip getLostSound() {
        return lostSound;
    }
}
