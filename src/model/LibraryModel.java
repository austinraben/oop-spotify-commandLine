/*
 * A User has a LibraryModel
 * A LibraryModel has the following data structures:
 *    - List<Song>, List<Album>, List<Playlist>
 *          - Ensures encapsulation so that internal data structures are hidden
 *    - ArrayList<>:
 *          - Resizable array implemantion of list for data changes
 */

package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
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
	 * Add Song to LibraryModel given title and artist
	 *   - Returns: true if successful, false is not found in MusicStore or already added
	 * NEW: Add the song's album to the Album list
	 */
	public boolean addSong(String songTitle, String artist) {
		Song song = musicStore.getSong(songTitle, artist);
		if (song != null && !songs.contains(song)) {
			songs.add(song);
			String albumTitle = song.getAlbumTitle();
			if (albumTitle != null) {
				Album libraryAlbum = getLibraryAlbumByTitle(albumTitle);
	            if (libraryAlbum == null) {
	  
	                List<Song> albumSongs = new ArrayList<>();
	                albumSongs.add(song);
	                Album originalAlbum = musicStore.getAlbum(albumTitle, artist);
	                if (originalAlbum != null) {
	                    libraryAlbum = new Album(originalAlbum.getAlbumTitle(), originalAlbum.getArtist(),originalAlbum.getGenre(), String.valueOf(originalAlbum.getYear()), albumSongs);
	                    albums.add(libraryAlbum);
	                }
	            } 
	            else {
	            	
	                if (!libraryAlbum.getSongs().contains(song)) {
	                    libraryAlbum.getSongs().add(song);
	                }
	            }
			}
			return true;
		}
		return false;
	}
	
	private Album getLibraryAlbumByTitle(String title) {
		for(Album album: albums) {
			if(album.getAlbumTitle().equalsIgnoreCase(title)) {
				return album;
			}
		}
		return null; 
	}
	
	/*
	 * Add Album to LibraryModel given album title and artist
	 * Also add all of the Songs in the corresponding album
	 *   - Returns: true if successful, false is not found in MusicStore or already added
	 */
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
	
	/*
	 * Add Playlist to LibraryModel given playlist
	 *   - Returns: true if successful, false is already added
	 */
	public boolean addPlaylist(Playlist playlist) {
	    if (playlist != null && !playlists.contains(playlist)) {
	        playlists.add(playlist);
	        return true;
	    }
	    return false;
	}
	
	/*
	 * Methods to search the LibraryModel for data information
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
	public List<Song> searchSongsByGenre(String genre){
		List<Song> genreList = new ArrayList<>();
		String genreTitle = genre.toUpperCase();
		for(Song song : songs) {
			String albumTitle = song.getAlbumTitle();
			if(albumTitle != null) {
				Album album = musicStore.getAlbum(albumTitle, song.getArtist());
				if (album != null) {
					String songGenre = album.getGenre().toUpperCase();
				
                    switch (genreTitle) {
                        case "POP":
                            if (songGenre.equals("POP")) {
                                genreList.add(song);
                            }
                            break;
                        case "ALTERNATIVE":
                            if (songGenre.equals("ALTERNATIVE")) {
                                genreList.add(song);
                            }
                            break;
                        case "COUNTRY":
                            if (songGenre.equals("COUNTRY")) {
                                genreList.add(song);
                            }
                            break;
                        case "LATIN":
                            if (songGenre.equals("LATIN")) {
                                genreList.add(song);
                            }
                            break;
                        case "ROCK":
                            if (songGenre.equals("ROCK")) {
                                genreList.add(song);
                            }
                            break;
                        case "SINGER/SONGWRITER":
                            if (songGenre.equals("SINGER/SONGWRITER")) {
                                genreList.add(song);
                            }
                            break;
                       default:
                    	   break;
                    }
				}
			}
		}
		return genreList;
	}
	
	/*
	 * Getter methods to get data from the LibraryModel
	 *    - Maintains encapsulation by returning copies of data structures
	 */
	public List<Song> getSongs() {
	    return new ArrayList<>(songs);
	}
	
    public List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }
    
    public List<Playlist> getPlaylists() {
        return new ArrayList<>(playlists);
    }
    
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
	
    public List<Song> getShuffledSongs() {
    	List<Song> shuffled = new ArrayList<>(songs);
    	Collections.shuffle(shuffled);
    	return shuffled;
    }
	
	public List<Playlist> getAllPlaylistsForDisplay() {
	    List<Playlist> allPlaylists = new ArrayList<>(playlists); 
	    allPlaylists.add(songTracker.getRecentSongsPlaylist());   
	    allPlaylists.add(songTracker.getFrequentSongsPlaylist());
	    allPlaylists.add(getFavoriteSongsPlaylist());
	    allPlaylists.add(getTopRatedPlaylist());
	    List<Playlist> genrePlaylists = getGenrePlaylists();
	    if (!genrePlaylists.isEmpty()) {
	        allPlaylists.addAll(genrePlaylists);
	    }	    
	    return allPlaylists;
	}
	
    public Album getAlbumFromSong(Song song) {
    	if(song == null || song.getAlbumTitle() == null) {
    		return null;
    	}
    	List<Album> albumFromSong = musicStore.searchAlbumByTitle(song.getAlbumTitle());
    	if (albumFromSong.isEmpty()) {
    		return null;
    	}
    	else {
    		return albumFromSong.get(0);
    	}
    }
	
	/*
	 * Setters for LibraryModel data structures
	 *    - Used in User's load() method
	 *    - Maintains encapsulation by returning copies of data structures
	 */
    public void setSongs(List<Song> songs) {
        this.songs = new ArrayList<>(songs);
    }

    public void setAlbums(List<Album> albums) {
        this.albums = new ArrayList<>(albums);
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = new ArrayList<>(playlists);
    }
    
    /*
     * Remove Song from LibraryModel and its data structrues
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
            playlist.removeSong(song.getSongTitle(), song.getArtist());
        }
    	for (Album album : albums) {
            album.removeSong(song.getSongTitle(), song.getArtist());
        }
    }
    
    /*
     * Remove Album and its Songs from LibraryModel and its data structrues
     */
    public void removeAlbum(Album album) {
    	albums.remove(album);
    	for(Song song: album.getSongs()) {
    		removeSong(song);
    	}
    }
    
    /*
     * LA2: 
     * Methods to create automatic playlists for favorite songs, genre (if >=10), and top rated (4-5)
     */
    public Playlist getFavoriteSongsPlaylist(){
    	Playlist favorites = new Playlist("Favorite Songs");
    	for(Song song: songs) {
    		boolean isRatedFive = song.getRating().isPresent() && song.getRating().get().getRatingValue() == 5;
    		if (song.isFavorite() || isRatedFive) {
                favorites.addSong(song);
            }
        }
        return favorites;
    }
    
    public Playlist getTopRatedPlaylist() {
        Playlist topRated = new Playlist("Top Rated");
        for (Song song : songs) {
            if (song.getRating().isPresent()) {
                int ratingValue = song.getRating().get().getRatingValue();
                if (ratingValue >= 4) {
                    topRated.addSong(song);
                }
            }
        }
        return topRated;
    }
    
    public List<Playlist> getGenrePlaylists() {
        List<Playlist> genrePlaylists = new ArrayList<>();
        String[] genres = {"POP", "ALTERNATIVE", "COUNTRY", "LATIN", "ROCK", "SINGER/SONGWRITER"};
        for (String genre : genres) {
            List<Song> genreSongs = searchSongsByGenre(genre);
            if (genreSongs.size() >= 10) {
                Playlist genrePlaylist = new Playlist(genre + " Playlist");
                for (Song song : genreSongs) {
                    genrePlaylist.addSong(song);
                }
                genrePlaylists.add(genrePlaylist);
            }
        }
        return genrePlaylists;
    }
    
    /*
     * LA2: 
     * Methods to get sorted List of Songs by Song's characteristics
     */
    
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
}
