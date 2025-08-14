//Channel and Puzzle logic
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ChannelManager {
    private final List<String> spanishPodcasts = new ArrayList<>();
    private final Random rand = new Random();
    private final String projectRoot;

    private final String[] podcasts = {
        "Una estática profunda... algo respira detrás del muro.",
        "Una voz antigua cuenta la historia de la casa sin puertas.",
        "Susurros: '¿Me escuchas... aún?'",
        "Un golpeteo lejano, después silencio.",
        "El ruido se curva en una pregunta..."
    };

    private final String ghostPodcast = "El aire se enfría. Una letanía antigua pronuncia tu nombre...";
    private final String ghostAudioPath;

    public ChannelManager() {
        projectRoot = getProjectRoot();
        ghostAudioPath = projectRoot != null ? projectRoot + "/audio/ghost_broadcast.wav" : "audio/ghost_broadcast.wav";
        loadSpanishPodcasts();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadSpanishPodcasts() {
        if (projectRoot == null) return;
        File folder = new File(projectRoot + "/audio/spanish_podcasts");
        if (!folder.exists() || !folder.isDirectory()) return;

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));
        if (files == null) return;
        for (File f : files) {
            spanishPodcasts.add(f.getPath());
        }
    }

    public String getRandomSpanishPodcast() {
        if (spanishPodcasts.isEmpty()) return null;
        return spanishPodcasts.get(rand.nextInt(spanishPodcasts.size()));
    }

    public String getPodcast(int channel) {
        if (channel >= 1 && channel <= podcasts.length) return podcasts[channel - 1];
        return "";
    }

    public boolean runPuzzle(Scanner sc) {
        System.out.println("\nUna voz susurra: 'Hablo sin boca, escucho sin oídos. ¿Qué soy?'");
        System.out.print("Tu respuesta: ");
        String answer = sc.nextLine().trim().toLowerCase();
        return answer.contains("eco") || answer.contains("echo");
    }

    public boolean isDisplayGlitched() {
        return rand.nextInt(100) < 20;
    }

    public String getGhostPodcast() {
        return ghostPodcast;
    }

    public String getGhostAudio() {
        File f = new File(ghostAudioPath);
        return f.exists() ? ghostAudioPath : null;
    }
}
