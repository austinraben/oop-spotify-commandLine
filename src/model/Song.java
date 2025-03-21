package model;
import java.util.Comparator;
import java.util.Optional;

public class Song {
	private String songTitle;
	private String artist;
	private String albumTitle;
	private Optional<Rating> rating; // 1-5
	private boolean favorite;
	private int playCount;
	
	public Song(String songTitle, String artist, String albumTitle, Rating rating, boolean favorite) {
		this.songTitle = songTitle;
		this.artist = artist;
		this.albumTitle = albumTitle;
		this.rating = Optional.ofNullable(rating);
		this.favorite = favorite;
		this.playCount = 0;
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
	
	public int getPlayCount() {
		return playCount;
	}
	
	public void incrementPlayCount() {
		playCount++;
	}
	
	/*
	 * Comparator for sorting by song title, then by artist for ties
	 */
	public static Comparator<Song> titleFirstComparator() {
        return new Comparator<Song>() {
            public int compare(Song s1, Song s2) {
                int comp = s1.getSongTitle().compareTo(s2.getSongTitle());
                if (comp == 0) {
                    comp = s1.getArtist().compareTo(s2.getArtist());
                }
                return comp;
            }
        };
    }

    /*
     * Comparator for sorting by artist, then by song title for ties
     */
    public static Comparator<Song> artistFirstComparator() {
        return new Comparator<Song>() {
            public int compare(Song s1, Song s2) {
                int comp = s1.getArtist().compareTo(s2.getArtist());
                if (comp == 0) {
                    comp = s1.getSongTitle().compareTo(s2.getSongTitle());
                }
                return comp;
            }
        };
    }

    /*
     * Comparator for sorting by rating, then by song title for ties
     */
    public static Comparator<Song> ratingFirstComparator() {
        return new Comparator<Song>() {
            public int compare(Song s1, Song s2) {
                Optional<Rating> r1 = s1.getRating();
                Optional<Rating> r2 = s2.getRating();
                if (r1.isPresent() && r2.isPresent()) {
                    int ratingComp = Integer.compare(r1.get().getRatingValue(), r2.get().getRatingValue());
                    if (ratingComp != 0) {
                        return ratingComp;
                    } else {
                        return s1.getSongTitle().compareTo(s2.getSongTitle());
                    }
                } else if (r1.isPresent()) {
                    return -1; // s1 has a rating, s2 doesn’t, so s1 comes first
                } else if (r2.isPresent()) {
                    return 1; // s2 has a rating, s1 doesn’t, so s2 comes first
                } else {
                    // Both unrated, sort by title
                    return s1.getSongTitle().compareTo(s2.getSongTitle());
                }
            }
        };
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
        sb.append(songTitle).append(" by ");
        sb.append(artist).append(" from ");
        sb.append("\'").append(albumTitle).append("\'");
        rating.ifPresent(rating -> sb.append(", rated ").append(rating.getRatingValue()).append("/5"));

        if (favorite) {
            sb.append(", favorite!");
        }

        return sb.toString();
    }
}
