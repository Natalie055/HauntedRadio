//audio playback and glitches
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AudioPlayer {
    private final Random rand = new Random();
    private final String staticAudio;
    private final String jumpscareAudio;

    public AudioPlayer() {
        String projectRoot = getProjectRoot();
        if (projectRoot != null) {
            staticAudio = projectRoot + "/audio/static.wav";
            jumpscareAudio = projectRoot + "/audio/jumpscare.wav";
        } else {
            System.err.println("Audio folder not found! Using fallback paths.");
            staticAudio = "audio/static.wav";
            jumpscareAudio = "audio/jumpscare.wav";
        }
    }

    private String getProjectRoot() {
        try {
            String currentDir = new File(".").getCanonicalPath();
            File dir = new File(currentDir);
            while (dir != null) {
                File audioDir = new File(dir, "audio");
                if (audioDir.exists() && audioDir.isDirectory()) {
                    return dir.getAbsolutePath();
                }
                dir = dir.getParentFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void playHauntedAudio(String filePath) {
        try {
            if (rand.nextInt(100) < 10 && new File(jumpscareAudio).exists()) {
                System.out.println("\n!!! The frequency distorts suddenly !!!\n");
                playAudioSafe(jumpscareAudio);
                return;
            }

            playAudioSafe(filePath);

            if (rand.nextInt(100) < 20 && new File(staticAudio).exists()) {
                System.out.println("\n*** The signal shifts to STATIC ***\n");
                playAudioSafe(staticAudio);
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
                System.out.println("\n*** The original broadcast fades back in ***\n");
                playAudioSafe(filePath);
            }

        } catch (Exception e) {
            System.out.println("[Audio error: " + e.getMessage() + "]");
        }
    }

    public void playAudioSafe(String filePath) {
        File audioFile = new File(filePath);
        if (!audioFile.exists()) {
            System.out.println("[Missing audio: " + filePath + "]");
            return;
        }

        try (AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile)) {
            AudioFormat baseFormat = stream.getFormat();

            // Force standard: 44.1kHz, 16-bit PCM signed, mono/stereo preserved
            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    44100f,
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    44100f,
                    false
            );

            try (AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, stream)) {
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);

                if (!AudioSystem.isLineSupported(info)) {
                    System.out.println("[Skipping unsupported format: " + filePath + "]");
                    return;
                }

                try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                    line.open(decodedFormat);
                    line.start();

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = din.read(buffer, 0, buffer.length)) != -1) {
                        line.write(buffer, 0, bytesRead);
                    }

                    line.drain();
                }
            }

        } catch (UnsupportedAudioFileException e) {
            System.out.println("[Unsupported audio file: " + filePath + "]");
        } catch (IOException | LineUnavailableException e) {
            System.out.println("[Could not play audio: " + filePath + " | " + e.getMessage() + "]");
        }
    }
}
