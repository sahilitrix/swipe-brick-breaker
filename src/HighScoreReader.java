import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HighScoreReader implements Iterator<Scorer> {

	private BufferedReader reader;
	private Scorer nextScorer;

	public HighScoreReader(String filePath) {
		try {
			reader = new BufferedReader(new FileReader(filePath));
			nextScorer = parseLineToScorer(reader.readLine());
		} catch (FileNotFoundException e) {
			nextScorer = null;
			throw new IllegalArgumentException();
		} catch (IOException e) {
			nextScorer = null;
			e.printStackTrace();
		}
	}
	
	/**
	 * Given a line in the file with the correct arguments, this method creates 
	 * and returns a Scorer object.
	 * @param a line with firstName,lastName,pennkey,and score attributes 
	 * separated by dashes.
	 * @return a Scorer object with the attributes read in the file*/
	private Scorer parseLineToScorer (String line){
		if (line == null) {
			return  null;
		}
		String[] args = line.split("-");
		String firstName = args[0]; 
		String lastName = args[1]; 
		String pennkey = args[2]; 
		int score = Integer.parseInt(args[3]); 
		return new Scorer(firstName, lastName, pennkey, score);
	}
	
	@Override
	public boolean hasNext() {
		if (nextScorer == null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return nextScorer != null;
	}

	@Override
	public Scorer next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		Scorer currScorer = nextScorer;
		try {
			nextScorer = parseLineToScorer(reader.readLine());
		} catch (IOException e) {
			nextScorer = null;
			e.printStackTrace();
		}
		return currScorer;
	}

}
