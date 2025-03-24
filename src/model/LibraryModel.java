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
	 * Added to addSong so that It can also return album information
	 */
	
	public boolean addSong(String songTitle, String artist) {
		Song song = musicStore.getSong(songTitle, artist);
		if (song != null && !songs.contains(song)) {
			songs.add(song);
			String albumTitle = song.getAlbumTitle();
			if (albumTitle != null) {
				Album libraryAlbum = libraryAlbumByTitle(albumTitle);
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
	
	private Album libraryAlbumByTitle(String title) {
		for(Album album: albums) {
			if(album.getAlbumTitle().equalsIgnoreCase(title)) {
				return album;
			}
		}
		return null; 
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
	 * NEW searchSongsByGenre method for part f
	 */
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
     *  Code for shuffle
     */
    public List<Song> getShuffledSongs() {
    	List<Song> shuffled = new ArrayList<>(songs);
    	Collections.shuffle(shuffled);
    	return shuffled;
    	
    }
    /*
     * Code for Album Information when searching a song
     */
    public Album getAlbumInformation(Song song) {
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
    
    public boolean albumIsInLibrary(Album album) {
    	return albums.contains(album);
    }
    
    /*
     * NEW 3 methods creating playlists for favorite song, genre, and top rated
     */
    
    public Playlist favoriteSongsPlaylist(){
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
     * NEW: Sorting methods 
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
