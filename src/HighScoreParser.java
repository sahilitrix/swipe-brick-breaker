import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;

public class HighScoreParser {
	
	private String filePath = "files/highscores.txt";
	private BufferedWriter writer;
	private TreeSet<Scorer> highscores;
	
	public HighScoreParser(String filePath) {
		highscores = new TreeSet<Scorer>();
		readHighScores();
		this.filePath = filePath;
	}
	
	//Get the 3 high-scores (or less) written in the .txt file separated by dash
	private void readHighScores() {
		highscores.clear();
		HighScoreReader reader = new HighScoreReader(filePath);
		while (reader.hasNext()) {
			highscores.add(reader.next()); 
		}
	}
	
	public TreeSet<Scorer> getHighScores(){
		readHighScores();
		TreeSet<Scorer> copy  = new TreeSet<Scorer>();
		for  (Scorer s : highscores) {
			copy.add(new Scorer(s.getFirstName(), s.getLastName(), s.getPennKey(), s.getScore()));
		}
		return copy;
	}
	
	/**This method only adds the highscore if there are less than 3 scores currently 
	 * in the file or if the highscore is greater than any of the current 3 present. 
	 * @return true if writing highscore was success, 
	 * false if score was not higher than any of current highscores
	 */
	public boolean writeHighScore(Scorer s) {
		readHighScores();
		highscores.add(s);
		if (highscores.size() > 3) {
			if (s == highscores.pollLast()) {
				return false;
			}
		}
		try {
			writer = new BufferedWriter(new FileWriter(filePath, false));
			for (Scorer o : highscores) {
				writer.write(o.getFirstName() +"-" +o.getLastName() +"-" +o.getPennKey() +"-" +o.getScore());
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public void writeToFile(String lines) {
		try {
			writer = new BufferedWriter(new FileWriter(filePath, false));
			writer.write(lines);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}
