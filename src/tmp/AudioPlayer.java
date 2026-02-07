package tmp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

    public static void playSound(String link) {
        Clip player = null;

        try {
            //Retrieve sound from provided link
            InputStream audioSrc = AudioPlayer.class.getResourceAsStream(link);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);

            if (audioSrc == null) {
                throw new IOException("Audio file not found: " + link);
            }

            AudioInputStream gameSound = AudioSystem.getAudioInputStream(bufferedIn);
            player = AudioSystem.getClip();
            player.open(gameSound);

            //Play sound and account for game volume
            FloatControl volume = (FloatControl) player.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(20f * (float) Math.log10(50.0 / 100.0));
            player.loop(0);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public static void playRandomNeutralPenguinSound() {
        Random random = new Random(System.currentTimeMillis());
        double odds = random.nextDouble();

        if(odds < 0.25) {
            playSound("/penguin_neutral_1.wav");
        } else if(odds < 0.5) {
            playSound("/penguin_neutral_2.wav");
        } else if(odds < 0.75) {
            playSound("/penguin_neutral_3.wav");
        } else {
            playSound("/penguin_neutral_4.wav");
        }

    }
}
