package Model;

import java.util.Random;

public class Dice {
    private final Random random = new Random();
    private final String type;

    public Dice(String type) {
        this.type = type;
    }

    public int roll() {
        switch (type) {
            case "REGULAR":
                return random.nextInt(6) + 1; // 1 to 6
            case "QUESTION":
                return random.nextInt(3) + 1; // 1: EASY, 2: MEDIUM, 3: HARD
            case "ENHANCED":
                return random.nextInt(10) - 3; // -3 to 6
            default:
                throw new IllegalArgumentException("Invalid dice type");
        }
    }

	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}
}
