# Haunted Radio — Console-Based Spooky Podcast Player
A console-based haunted radio experience with ASCII art, glitch effects, audio playback, puzzles, and a hidden ghost channel.

## 🎮 How to Play
- Tune channels `1–5` for spooky broadcasts.
- Channel `5` contains a riddle — solve it to unlock channel `666`.
- Channel `666` plays a ghost broadcast and sends a hidden Morse code message.
- Occasionally, the display glitches and static interrupts your listening.

## 📂 Project Structure
- **AsciiArt.java** — Prints the haunted radio and ghost ASCII art.
- **AudioPlayer.java** — Handles `.wav` playback, glitches, and jumpscares.
- **ChannelManager.java** — Manages podcasts, puzzles, and ghost channel logic.
- **HauntedRadio.java** — Main program loop (entry point).
- **MorseCode.java** — Generates and plays Morse code audio.
