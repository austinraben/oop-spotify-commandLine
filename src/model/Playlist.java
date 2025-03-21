// Part 3:

package model;

import java.util.ArrayList;
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
	
	/*
	 * @pre: name != null
	 */
	public void setName(String name) {
		if (!name.trim().isEmpty()) {
			this.name = name;
		}
	}
	
	/*
	 * @pre: song != null
	 */
	public void addSong(Song song) {
		if (!songs.contains(song)) {
			songs.add(song);
		}
	}
	
	public void removeSong(Song song) {
		songs.remove(song);
	}
	public void removeSongFromLibrary(String title, String artist) {
		for(int i = 0; i< songs.size();i++) {
			Song newSong = songs.get(i);
			if(newSong.getSongTitle().equalsIgnoreCase(title) && newSong.getArtist().equalsIgnoreCase(artist)) {
				songs.remove(i);
				i--;	
			}
		}
	}
	
	public List<Song> getSongs() {
        return new ArrayList<>(songs);  
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
	
	public void getSongList(List<Song> songList) {
		songs.clear();
		songs.addAll(songList);
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
