
public class Scorer implements Comparable<Scorer>{
	
	private String firstName;
	private String lastName;
	private String pennkey;
	private int score;
	
	public Scorer(String firstName, String lastName, String pennkey, int score) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.pennkey = pennkey;
		this.score = score;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getPennKey() {
		return pennkey;
	}
	
	public int getScore() {
		return score;
	}
	
	@Override
	public int compareTo(Scorer o) {
		if (o.equals(this)) {
			return 0;
		} else if (o.getScore() == this.getScore()) {
			return 1;
		} else if  (this.getScore() > o.getScore()) {
			return -1;
		} else {
			return 1;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Scorer)) {
			return false;
		}
		Scorer s = (Scorer) o;
		return s.getScore() == this.getScore()
			&& s.getPennKey().equals(getPennKey());
	}
	
	public String toString() {
		return "SCORE: " +score + " - " +firstName.toUpperCase() + " " 
				+lastName.toUpperCase() + " (" +pennkey +")";
	}
	
}
