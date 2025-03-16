package model;
import java.util.Optional;

public class Song {
	private String songTitle;
	private String artist;
	private String albumTitle;
	private Optional<Rating> rating; // 1-5
	private boolean favorite;
	
	public Song(String songTitle, String artist, String albumTitle, Rating rating, boolean favorite) {
		this.songTitle = songTitle;
		this.artist = artist;
		this.albumTitle = albumTitle;
		this.rating = Optional.ofNullable(rating);
		this.favorite = false;
	}

	public String getSongTitle() {
		return songTitle;
	}

	public String getArtist() {
		return artist;
	}

	public String getAlbumTitle() {
		return albumTitle;
	}

	public Optional<Rating> getRating() {
		return rating;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setRating(Rating rating) {
		this.rating = Optional.ofNullable(rating);
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	
	@Override
    public boolean equals(Object o) {
		// check same instance
        if (this == o) { 
        	return true;  
        }
        
        // check object type
        if (o == null || getClass() != o.getClass()) {
        	return false;  
        }
        
        // check same song title and artist
        Song other = (Song) o;
        return songTitle.equalsIgnoreCase(other.songTitle) &&
               artist.equalsIgnoreCase(other.artist);
    }
	
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Song Title: ").append(songTitle).append(", ");
        sb.append("Artist: ").append(artist).append(", ");
        sb.append("Album: ").append(albumTitle);

        rating.ifPresent(rating -> sb.append(", Rating: ").append(rating.getRatingValue())); 

        if (favorite) {
            sb.append(", Favorite");
        }

        return sb.toString();
    }
}