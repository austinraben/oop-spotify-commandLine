// Part 3:

package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist {
	private String name;
	private ArrayList<Song> songs;
	
	public Playlist(String name) {
		this.name = name;
		this.songs = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public List<Song> getSongs() {
        return new ArrayList<>(songs);  
    }
	
	// Used to update automatic Playlists in SongTracker
	public void getSongList(List<Song> songList) {
		songs.clear();
		songs.addAll(songList);
	}
	
	public List<Song> getShuffledSongs() {
    	List<Song> shuffled = new ArrayList<>(songs);
    	Collections.shuffle(shuffled);
    	return shuffled;
    }
	
	public void setName(String name) {
		if (!name.trim().isEmpty()) {
			this.name = name;
		}
	}

	public void addSong(Song song) {
		if (!songs.contains(song)) {
			songs.add(song);
		}
	}
	
	/*
	 * Overloaded methods for removing songs from a Playlist
	 */
	
	public void removeSong(Song song) {
		songs.remove(song);
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
        
        // check same playlist name
        Playlist other = (Playlist) o;
        return name.equalsIgnoreCase(other.name);    
     }
	
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(": ");
        for (int i = 0; i < songs.size(); i++) {
            sb.append(songs.get(i).getSongTitle());
            if (i < songs.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
