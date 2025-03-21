// Part 3:

package model;

import java.util.ArrayList;
import java.util.List;

public class Album {
	private String albumTitle;
	private String artist;
	private String genre;
	private String year;
	private List<Song> songs;
	
	public Album(String albumTitle, String artist, String genre, String year, List<Song> songs) {
		this.albumTitle = albumTitle;
		this.artist = artist;
		this.genre = genre;
		this.year = year;
		this.songs = songs;
	}

	public String getAlbumTitle() {
		return albumTitle;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public int getYear() {
		return Integer.parseInt(year);
	}
	
	public List<Song> getSongs() {
		return new ArrayList<>(songs);
	}
	
	public void removeSong(String title, String artist) {
		for(int i = 0; i< songs.size();i++) {
			Song newSong = songs.get(i);
			if(newSong.getSongTitle().equalsIgnoreCase(title) && newSong.getArtist().equalsIgnoreCase(artist)) {
				songs.remove(i);
				i--;	
			}
		}
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
        
        // check same album title and artist
        Album other = (Album) o;
        return albumTitle.equalsIgnoreCase(other.albumTitle) &&
               artist.equalsIgnoreCase(other.artist);
    }
	
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Album: ").append(albumTitle).append("\n");
        sb.append("Artist: ").append(artist).append("\n");
        sb.append("Genre: ").append(genre).append("\n");
        sb.append("Year: ").append(year).append("\n");
        sb.append("Songs:\n");
        for (Song song : songs) {
            sb.append("- ").append(song.toString()).append("\n"); 
        }
        return sb.toString();
    }
}
