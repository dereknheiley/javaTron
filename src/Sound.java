// imports used for the sound file
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Sound {

	// Buffer is used to load data where music can be played
	private static final int BUFFER_SIZE = 128000;
	private static File soundFile;
	private static AudioInputStream audioStream;
	private static AudioFormat audioFormat;
	private static SourceDataLine sourceLine;

	// used to play music in the game
	public static void music() {
		String strFilename = "Derezzed.wav";

		// try is used to check if the file exists in the following lines.
		// if file does not exist, will catch the exception error and exit.
		try {
			soundFile = new File(strFilename);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		// creates the audio stream with the designated file.
		try {
			audioStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		audioFormat = audioStream.getFormat(); // formats the audio stream for
												// use

		// sourceLine is created as a SourceDataLine.
		// Handles bytes, delivers to mixer to play the song.
		// bytes a read from a file and put into the source line.
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		try {
			sourceLine = (SourceDataLine) AudioSystem.getLine(info);
			sourceLine.open(audioFormat);
		} catch (LineUnavailableException e) { // checks for error
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace(); // checks for error
			System.exit(1);
		}

		// bytes are delivered to the mixer to play the sound
		sourceLine.start();

		int nBytesRead = 0;
		byte[] abData = new byte[BUFFER_SIZE];
		while (nBytesRead != -1) {
			try {
				nBytesRead = audioStream.read(abData, 0, abData.length);
			} catch (IOException e) { // checks for error
				e.printStackTrace();
			}
			if (nBytesRead >= 0) { // if there are bytes, play sound
				@SuppressWarnings("unused")
				int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
			}
		}

		// ends the sound
		sourceLine.drain();
		sourceLine.close();
		music();
	}
}