package Connect4;

public class PlayerP {
	
	private String playerName;
	private String playerSymbol;
	private int numGames;
	private int numWins;
	private int numLosses;
	
	// constructor
	public PlayerP() {
		
		playerName = "n";
		playerSymbol = "s";
		numGames = 0;
		numWins = 0;
		numLosses = 0;
	}
	
	// overloaded constructor
	public PlayerP (String playerName, String playerSymbol) {
		
		// using this.etc.. due to String name being the same as variable name
		this.playerName = playerName;
		this.playerSymbol = playerSymbol;
		numLosses = 0;
		numWins = 0;
		numGames = 0;
		
	}
	
	// getters
	public String getName () {
		return playerName;
	}
	public String getSymbol() {
		return playerSymbol;
	}
	public int getWins() {
		return numWins;
	}
	public int getLosses() {
		return numLosses;
	}
	public int getGames() {
		return numGames;
	}
	
	// setters
	public void setName (String playerName) {
		this.playerName = playerName;
	}
	public void setSymbol (String playerSymbol) {
		this.playerSymbol = playerSymbol;
	}
	
	// methods
	public void addWins() {
		numWins++;
		numGames++;
	}
	public void addLosses() {
		numLosses++;
		numGames++;
	}
	public void addDraws() {
		numGames++;
	}
}
