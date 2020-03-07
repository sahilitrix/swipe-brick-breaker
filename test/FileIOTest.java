import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import org.junit.Test;

public class FileIOTest {
	
	@Test
	public void testCreateReaderValid() {
		HighScoreReader reader = new HighScoreReader("files/highscores.txt");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateReaderFileNotFound() {
		HighScoreReader reader = new HighScoreReader("files/random.txt");
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testReadHighScoreEmptyFile() {
		HighScoreReader reader = new HighScoreReader("files/empty.txt");
		assertFalse(reader.hasNext());
		reader.next();
	}
	
	@Test
	public void testReadHighScoreSingletonFile() {
		HighScoreReader reader = new HighScoreReader("files/single_score.txt");
		assertTrue(reader.hasNext());
		Scorer scorer = reader.next();
		assertEquals(scorer, new Scorer("Sahit", "Penmatcha", "sahitpen", 7));
		assertFalse(reader.hasNext());
	}
	
	@Test
	public void testReadHighScoreThreeScoresFile() {
		HighScoreReader reader = new HighScoreReader("files/three_scores.txt");
		for (int i = 0; i < 3; i++) {
			assertTrue(reader.hasNext());
		}
		Scorer scorer = reader.next();
		assertEquals(scorer, new Scorer("Sahit", "Penmatcha", "sahitpen", 10));
		scorer = reader.next();
		assertEquals(scorer, new Scorer("Jeff", "Penmatcha", "jeffpen", 8));
		scorer = reader.next();
		assertEquals(scorer, new Scorer("Owen", "Penmatcha", "owenpen", 6));
	}
	
	@Test
    public void testWriteHighScoreToEmptyFile() {
		File file = new File("files/write_to_empty.txt");
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HighScoreParser parser = new HighScoreParser("files/write_to_empty.txt");
		parser.writeHighScore(new Scorer("Jeff", "Bezos", "bezos", 100));
		TreeSet<Scorer> scores = parser.getHighScores();
		assertTrue(scores.size() == 1);
		assertEquals(scores.first(), new Scorer("Jeff", "Bezos", "bezos", 100));
    }
	
	@Test
    public void testWriteHighScoreToFileWithLessThanThreeScores() {
		HighScoreParser parser = new HighScoreParser("files/write_to_singleton.txt");
		parser.writeToFile("Sahit-Penmatcha-sahitpen-7");
		parser.writeHighScore(new Scorer("Jeff", "Bezos", "bezos", 100));
		TreeSet<Scorer> scores = parser.getHighScores();
		assertTrue(scores.size() == 2);
		assertEquals(scores.first(), new Scorer("Jeff", "Bezos", "bezos", 100));
    }
	
	@Test
    public void testWriteHighScoreToFileWithThreeScores() {
		HighScoreParser parser = new HighScoreParser("files/write_to_triple.txt");
		parser.writeToFile("Sahit-Penmatcha-sahitpen-10\nJeff-Penmatcha-jeffpen-8\nOwen-Penmatcha-owenpen-6");
		parser.writeHighScore(new Scorer("Jeff", "Bezos", "bezos", 100));
		TreeSet<Scorer> scores = parser.getHighScores();
		assertTrue(scores.size() == 3);
		assertEquals(scores.first(), new Scorer("Jeff", "Bezos", "bezos", 100));
    }
	
	@Test
    public void testWriteHighScoreNotHighScore() {
		HighScoreParser parser = new HighScoreParser("files/write_to_triple.txt");
		parser.writeToFile("Sahit-Penmatcha-sahitpen-10\nJeff-Penmatcha-jeffpen-8\nOwen-Penmatcha-owenpen-6");
		parser.writeHighScore(new Scorer("Jeff", "Bezos", "bezos", 1));
		TreeSet<Scorer> scores = parser.getHighScores();
		assertTrue(scores.size() == 3);
		assertFalse(scores.contains(new Scorer("Jeff", "Bezos", "bezos", 1)));
    }
	
	@Test
    public void testWriteHighScoreFirstPlace() {
		HighScoreParser parser = new HighScoreParser("files/write_to_triple.txt");
		parser.writeToFile("Sahit-Penmatcha-sahitpen-10\nJeff-Penmatcha-jeffpen-8\nOwen-Penmatcha-owenpen-6");
		parser.writeHighScore(new Scorer("Jeff", "Bezos", "bezos", 100));
		TreeSet<Scorer> scores = parser.getHighScores();
		assertTrue(scores.size() == 3);
		assertTrue(scores.contains(new Scorer("Jeff", "Bezos", "bezos", 100)));
		assertEquals(scores.first(), new Scorer("Jeff", "Bezos", "bezos", 100));
    }
	
	@Test
    public void testWriteHighScoreSecondPlace() {
		HighScoreParser parser = new HighScoreParser("files/write_to_triple.txt");
		parser.writeToFile("Sahit-Penmatcha-sahitpen-10\nJeff-Penmatcha-jeffpen-8\nOwen-Penmatcha-owenpen-6");
		parser.writeHighScore(new Scorer("Jeff", "Bezos", "bezos", 9));
		TreeSet<Scorer> scores = parser.getHighScores();
		assertTrue(scores.size() == 3);
		assertTrue(scores.contains(new Scorer("Jeff", "Bezos", "bezos", 9)));
		scores.pollFirst();
		assertEquals(scores.first(), new Scorer("Jeff", "Bezos", "bezos", 9));
    }
	
	@Test
    public void testWriteHighScoreThirdPlace() {
		HighScoreParser parser = new HighScoreParser("files/write_to_triple.txt");
		parser.writeToFile("Sahit-Penmatcha-sahitpen-10\nJeff-Penmatcha-jeffpen-8\nOwen-Penmatcha-owenpen-6");
		parser.writeHighScore(new Scorer("Jeff", "Bezos", "bezos", 7));
		TreeSet<Scorer> scores = parser.getHighScores();
		assertTrue(scores.size() == 3);
		assertTrue(scores.contains(new Scorer("Jeff", "Bezos", "bezos", 7)));
		scores.pollFirst();
		scores.pollFirst();
		assertEquals(scores.first(), new Scorer("Jeff", "Bezos", "bezos", 7));
    }
	
	@Test
    public void testWriteHighScoreSameUserDifferentScore() {
		HighScoreParser parser = new HighScoreParser("files/write_to_triple.txt");
		parser.writeToFile("Sahit-Penmatcha-sahitpen-10\nJeff-Penmatcha-jeffpen-8\nOwen-Penmatcha-owenpen-6");
		parser.writeHighScore(new Scorer("Sahit", "Penmatcha", "sahitpen", 9));
		TreeSet<Scorer> scores = parser.getHighScores();
		assertTrue(scores.size() == 3);
		assertTrue(scores.contains(new Scorer("Sahit", "Penmatcha", "sahitpen", 9)));
		assertEquals(scores.first(), new Scorer("Sahit", "Penmatcha", "sahitpen", 10));
		scores.pollFirst();
		assertEquals(scores.first(), new Scorer("Sahit", "Penmatcha", "sahitpen", 9));
    }
	
	@Test
    public void testWriteHighScoreSameUserSameScore() {
		HighScoreParser parser = new HighScoreParser("files/write_to_triple.txt");
		parser.writeToFile("Sahit-Penmatcha-sahitpen-10\nJeff-Penmatcha-jeffpen-8\nOwen-Penmatcha-owenpen-6");
		parser.writeHighScore(new Scorer("Sahit", "Penmatcha", "sahitpen", 10));
		TreeSet<Scorer> scores = parser.getHighScores();
		assertTrue(scores.size() == 3);
		assertTrue(scores.contains(new Scorer("Sahit", "Penmatcha", "sahitpen", 10)));
		assertEquals(scores.first(), new Scorer("Sahit", "Penmatcha", "sahitpen", 10));
		scores.pollFirst();
		assertEquals(scores.first(), new Scorer("Jeff", "Penmatcha", "jeffpen", 8));
    }
	
}
