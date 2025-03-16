package model;

public enum Rating {
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5);
	
    private final int ratingValue;

    Rating(int ratingValue) {
    	this.ratingValue = ratingValue;    }

    public int getRatingValue() {
        return ratingValue;
    }
}
