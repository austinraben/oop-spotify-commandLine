// Part 3:
// 

package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryModel {
	private List<Song> songs;
	private List<Album> albums;
	private List<Playlist> playlists;
	private MusicStore musicStore;
	private SongTracker songTracker;
	
	public LibraryModel(MusicStore musicStore) {
		this.songs = new ArrayList<>();
		this.albums = new ArrayList<>();
		this.playlists = new ArrayList<>();
		this.musicStore = musicStore;
		this.songTracker = new SongTracker();
	}
	
	/*
	 * Add methods to add song/album/playlist to Library
	 */
	
	public boolean addSong(String songTitle, String artist) {
		Song song = musicStore.getSong(songTitle, artist);
		if (song != null && !songs.contains(song)) {
			songs.add(song);
			return true;
		}
		return false;
	}
	
	public boolean addAlbum(String albumTitle, String artist) {
		Album album = musicStore.getAlbum(albumTitle, artist);
		if (album != null && !albums.contains(album)) {
			albums.add(album);
		
			for (Song song : album.getSongs()) {
				if (!songs.contains(song)) {
					songs.add(song);
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean addPlaylist(Playlist playlist) {
	    if (playlist != null && !playlists.contains(playlist)) {
	        playlists.add(playlist);
	        return true;
	    }
	    return false;
	}
	
	/*
	 * Four search methods below to search Library for song/album by song/artist
	 * One additional search method to search Playlist by name
	 */
	
	public List<Song> searchSongByTitle(String title) {
		return songs.stream()
				.filter(song -> song.getSongTitle().equalsIgnoreCase(title))
				.collect(Collectors.toList());
	}
	
	public List<Album> searchAlbumByTitle(String title) {
		return albums.stream()
				.filter(album -> album.getAlbumTitle().equalsIgnoreCase(title))
				.collect(Collectors.toList());
	}
	
	public List<Song> searchSongByArtist(String artist) {
		return songs.stream()
				.filter(song -> song.getArtist().equalsIgnoreCase(artist))
				.collect(Collectors.toList());
	}
	
	public List<Album> searchAlbumByArtist(String artist) {
		return albums.stream()
				.filter(album -> album.getArtist().equalsIgnoreCase(artist))
				.collect(Collectors.toList());
	}
	
	public Playlist searchPlaylistByName(String name) {
	    for (Playlist playlist : playlists) {
	        if (playlist.getName().equalsIgnoreCase(name)) {
	            return playlist;
	        }
	    }
	    return null;
	}
	
	/*
	 * Getter methods to get information from Library data structures
	 */
	
	public SongTracker getSongTracker() {
		return songTracker;
	}
	
	public List<String> getSongTitles() {
		List<String> songTitles = new ArrayList<>();
		for (Song song : songs) {
			songTitles.add(song.getSongTitle());
			}
		return songTitles;
	}
	
	public List<Song> getSongs() {
	    return new ArrayList<>(songs);
	}
	
    public List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }

    public List<Playlist> getPlaylists() {
        return new ArrayList<>(playlists);
    }
	
	public List<String> getAlbumTitles() {
		List<String> albumTitles = new ArrayList<>();
		for (Album album : albums) {
			albumTitles.add(album.getAlbumTitle());
		}
		return albumTitles;
	}
	
	public List<String> getArtists() {
		List<String> artists = new ArrayList<>();
		for (Song song : songs) {
			String artist = song.getArtist();
			if (!artists.contains(artist)) {
				artists.add(artist);
			}
		}
		return artists;
	}
	
	public List<String> getPlaylistNames() {
		List<String> playlistNames = new ArrayList<>();
		for (Playlist playlist : playlists) {
			playlistNames.add(playlist.getName());
		}
		return playlistNames;
	}
	
	public List<String> getFavoriteSongs() {
		List<String> favoriteSongs = new ArrayList<>();
		for (Song song : songs) {
			if (song.isFavorite()) {
				favoriteSongs.add(song.getSongTitle());
			}
		}
		return favoriteSongs;
	}
	
	public List<Playlist> getAllPlaylistsForDisplay() {
	    List<Playlist> allPlaylists = new ArrayList<>(playlists); 
	    allPlaylists.add(songTracker.getRecentSongsPlaylist());   
	    allPlaylists.add(songTracker.getFrequentSongsPlaylist());
	    return allPlaylists;
	}
	
    public List<Song> getSongsSortedByTitle() {
        List<Song> sortedList = new ArrayList<>(songs);
        Collections.sort(sortedList, Song.titleFirstComparator());
        return sortedList;
    }

    public List<Song> getSongsSortedByArtist() {
        List<Song> sortedList = new ArrayList<>(songs);
        Collections.sort(sortedList, Song.artistFirstComparator());
        return sortedList;
    }

    public List<Song> getSongsSortedByRating() {
        List<Song> sortedList = new ArrayList<>(songs);
        Collections.sort(sortedList, Song.ratingFirstComparator());
        return sortedList;
    }
	
	/*
	 * Setters for loading User information (User.load())
	 */
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
    
    /*
     * Removes Song or Album from library 
     */
    public void removeSong(Song song) {
    	for(int i = 0; i < songs.size(); i++) {
    		Song newSong = songs.get(i);
    		if (newSong.getSongTitle().equalsIgnoreCase(song.getSongTitle()) && 
    		newSong.getArtist().equalsIgnoreCase(song.getArtist())){
    			songs.remove(i);
    			i--;
    		}
    	}
    	for (Playlist playlist : playlists) {
            playlist.removeSongFromLibrary(song.getSongTitle(), song.getArtist());
        }
    	for (Album album : albums) {
            album.removeSong(song.getSongTitle(), song.getArtist());
        }
    }
    
    public void removeAlbum(Album album) {
    	albums.remove(album);
    	for(Song song: album.getSongs()) {
    		removeSong(song);
    	}
    }
	/*
	 *  code for song shuffling
	 */
	public List<Song> getShuffledSongs() {
    	List<Song> shuffled = new ArrayList<>(songs);
    	Collections.shuffle(shuffled);
    	return shuffled;
    	
    }
}
