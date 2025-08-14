// REMEMBER ALONDRA THIS IS THE MAIN PROGRAM LOOP
import java.util.Scanner;

public class HauntedRadio {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChannelManager channels = new ChannelManager();
        AsciiArt art = new AsciiArt();
        AudioPlayer audio = new AudioPlayer();
        MorseCode morse = new MorseCode();

        boolean ghostUnlocked = false;
        int channel = 1;

        outer:
        while (true) {
            art.clearScreen();
            boolean displayGlitched = channels.isDisplayGlitched();
            art.printRadio(channel, displayGlitched);

            // show thematic text
            if (channel >= 1 && channel <= 5) {
                System.out.println(channels.getPodcast(channel));
            } else if (channel == 666 && ghostUnlocked) {
                art.printGhostHeader();
                System.out.println(channels.getGhostPodcast());
            } else {
                System.out.println("Tuning... the static whispers.");
            }

            // choose and play a random spanish podcast each time
            String podcastPath = channels.getRandomSpanishPodcast();
            if (podcastPath != null) {
                audio.playHauntedAudio(podcastPath);
            } else {
                System.out.println("[No Spanish podcasts found. Playing static instead.]");
                audio.playHauntedAudio("audio/static.wav");
            }

            // channel 5 puzzle unlocks ghost channel 666
            if (channel == 5 && !ghostUnlocked) {
                boolean solved = channels.runPuzzle(sc);
                if (solved) {
                    ghostUnlocked = true;
                    System.out.println("\nA cold wind whispers: 'Try 666.'");
                } else {
                    System.out.println("\nSomething swallows your answer. The radio goes quiet.");
                }
            }

            // if user tuned to ghost channel and it's unlocked, play ghost extra (Morse)
            if (channel == 666 && ghostUnlocked) {
                // optional ghost broadcast audio (deep drone) if exists
                String ghostAudio = channels.getGhostAudio();
                if (ghostAudio != null) {
                    audio.playHauntedAudio(ghostAudio);
                }

                // then the morse transmission
                System.out.println("\n--- BEGIN MORSE TRANSMISSION ---");
                morse.playMessage("THIS IS DEFINITELY AN A");
                System.out.println("\n--- END TRANSMISSION ---");
            }

            // prompt for next tune
            System.out.print("\nTune to channel (1-5" + (ghostUnlocked ? ", 666" : "") + ") or 0 to quit: ");
            String input = sc.nextLine().trim();

            if (input.equals("0") || input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                System.out.println("The radio sighs. Goodbye.");
                break outer;
            }

            // parse channel
            try {
                int next = Integer.parseInt(input);
                if (next == 666 && !ghostUnlocked) {
                    System.out.println("That frequency is silent... maybe solve the haunted puzzle first.");
                    // keep current channel unchanged, loop again
                } else if ((next >= 1 && next <= 5) || (next == 666 && ghostUnlocked)) {
                    channel = next;
                } else {
                    System.out.println("Invalid channel. Defaulting to 1.");
                    channel = 1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Couldn't parse that. Defaulting to channel 1.");
                channel = 1;
            }

            // little pause before next redraw
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }

        sc.close();
    }
}

