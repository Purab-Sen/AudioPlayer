package project;
//file libraries
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

//audio system libraries
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;

public class AudioPlayer {
	static JFileChooser chooseDirectory;
	static boolean loopState = false;
	static String audioFileName;
	static File myaudio;
	static Clip clip;
	static boolean audiostate = false;
	static String Path;
	//setup audio_file
	AudioPlayer(String filename)throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		
		myaudio = new File(Path+"/"+filename+".wav");
		audioFileName = filename;
		AudioInputStream as = AudioSystem.getAudioInputStream(myaudio);
		//clips
		clip = AudioSystem.getClip();
		clip.open(as);//as ==> audio stream
		
	}
	static void Play(){
		clip.start();
		audiostate = true;
	}
	static void Pause() {
		clip.stop();
		audiostate = false;
	}
	static void Reset() {
		clip.setMicrosecondPosition(0);
	}
	static void setPosition(long x) {
		clip.setMicrosecondPosition(x);
	}
	static void close() {
		if(clip != null)
		clip.close();
		audiostate = false;
	}
	static boolean setPath() {
		chooseDirectory = new JFileChooser();
		chooseDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = chooseDirectory.showOpenDialog(GUI.jframe);
		if(result == 0) {
			File folder = new File(chooseDirectory.getSelectedFile().getAbsolutePath());
			Path = folder.getAbsolutePath();
			writePath(Path);
			return true;
		}
		else {
			return false;
		}
	}
	static void readPath() {
		String data;
		try {
		      File myObj = new File("./src/path.txt");
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        data = myReader.nextLine();
		        Path = new String(data);
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred while reading file");
		      e.printStackTrace();
		    }
	}
	static void writePath(String data) {
	    try {
	        FileWriter myWriter = new FileWriter("./src/path.txt");
	        myWriter.write(data);
	        myWriter.close();
	      } catch (IOException e) {
	        System.out.println("An error occurred while writing file");
	        e.printStackTrace();
	      }

	}
}