import javax.sound.sampled.*;

public class MorseCode {

    private static final float SAMPLE_RATE = 44100f;

    public static void playTone(double freqHz, int durationMs) {
        int samples = (int) ((durationMs / 1000.0) * SAMPLE_RATE);
        byte[] output = new byte[samples * 2]; // 16-bit PCM

        for (int i = 0; i < samples; i++) {
            short val = (short) (Math.sin(2 * Math.PI * i * freqHz / SAMPLE_RATE) * Short.MAX_VALUE);
            output[i * 2] = (byte) (val & 0xFF);
            output[i * 2 + 1] = (byte) ((val >> 8) & 0xFF);
        }

        AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("[Cannot play Morse tone: unsupported audio line]");
            return;
        }

        try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
            line.open(format);
            line.start();
            line.write(output, 0, output.length);
            line.drain();
        } catch (LineUnavailableException e) {
            System.out.println("[Error playing Morse tone: " + e.getMessage() + "]");
        }
    }

    public static void playMessage(String message, int dotDurationMs) {
        String morse = toMorse(message);
        for (char c : morse.toCharArray()) {
            switch (c) {
                case '.': playTone(800, dotDurationMs); break;
                case '-': playTone(800, dotDurationMs * 3); break;
                case ' ': try { Thread.sleep(dotDurationMs); } catch (InterruptedException ignored) {} break;
            }
            try { Thread.sleep(dotDurationMs); } catch (InterruptedException ignored) {}
        }
    }

    private static String toMorse(String msg) {
        msg = msg.toUpperCase();
        StringBuilder morse = new StringBuilder();
        String[] codes = {
                ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", // A-I
                ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", // J-R
                "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.." // S-Z
        };
        for (char ch : msg.toCharArray()) {
            if (ch >= 'A' && ch <= 'Z') {
                morse.append(codes[ch - 'A']).append(" ");
            } else if (ch == ' ') morse.append("  ");
        }
        return morse.toString();
    }
}
