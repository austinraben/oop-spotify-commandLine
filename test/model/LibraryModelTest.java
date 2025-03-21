package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibraryModelTest {

    private void populateAlbumMap(MusicStore store) {
        try {
            String folderPath = "src/model.albums/";
            File folder = new File(folderPath);
            File[] files = folder.listFiles();
            for (File file : files) {
                store.readMusicFile(file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    @Test
    void testAddSongs_Existing() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        boolean result = library.addSong("Rolling in the Deep", "Adele");
        assertTrue(result);
        
        List<Song> foundSongs = library.searchSongByTitle("Rolling in the Deep");
        assertEquals(1, foundSongs.size());
    }

    @Test
    void testAddSongs_Duplicate() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addSong("Rolling in the Deep", "Adele");
        boolean result = library.addSong("Rolling in the Deep", "Adele");
        assertFalse(result);
        
        List<Song> foundSongs = library.searchSongByTitle("Rolling in the Deep");
        assertEquals(1, foundSongs.size());
    }

    @Test
    void testAddSongs_NonExistent() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        boolean result = library.addSong("Nonexistent", "Adele");
        assertFalse(result);
    }

    @Test
    void testAddAlbum_Existing() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        boolean result = library.addAlbum("21", "Adele");
        assertTrue(result);
        
        List<Album> foundAlbums = library.searchAlbumByTitle("21");
        assertEquals(1, foundAlbums.size());
        
        List<Song> foundSongs = library.searchSongByTitle("Someone Like You");
        assertEquals(1, foundSongs.size());
    }

    @Test
    void testAddAlbum_Duplicate() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addAlbum("21", "Adele");
        boolean result = library.addAlbum("21", "Adele");
        assertFalse(result);
        
        List<Album> foundAlbums = library.searchAlbumByTitle("21");
        assertEquals(1, foundAlbums.size());
    }

    @Test
    void testAddAlbum_NonExistent() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        boolean result = library.addAlbum("Nonexistent", "Adele");
        assertFalse(result);
    }

    @Test
    void testSearchSongByTitle_NotFound() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        List<Song> foundSongs = library.searchSongByTitle("Nonexistent");
        assertTrue(foundSongs.isEmpty());
    }


    @Test
    void testSearchAlbumByTitle_NotFound() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        List<Album> foundAlbums = library.searchAlbumByTitle("Nonexistent");
        assertTrue(foundAlbums.isEmpty());
    }

    @Test
    void testSearchSongByArtist_Found() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addSong("Rolling in the Deep", "Adele");
        List<Song> foundSongs = library.searchSongByArtist("Adele");
        assertEquals(1, foundSongs.size());
    }

    @Test
    void testSearchSongByArtist_NotFound() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        List<Song> foundSongs = library.searchSongByArtist("Unknown");
        assertTrue(foundSongs.isEmpty());
    }

    @Test
    void testSearchAlbumByArtist_Found() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addAlbum("21", "Adele");
        List<Album> foundAlbums = library.searchAlbumByArtist("Adele");
        assertEquals(1, foundAlbums.size());
    }

    @Test
    void testSearchAlbumByArtist_NotFound() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        List<Album> foundAlbums = library.searchAlbumByArtist("Unknown");
        assertTrue(foundAlbums.isEmpty());
    }

    @Test
    void testSearchPlaylistByName() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        Playlist playlist = new Playlist("MyPlaylist");
        
        Song song1 = store.getSong("Rolling in the Deep", "Adele");
        Song song2 = store.getSong("Someone Like You", "Adele");
        
        playlist.addSong(song1);
        playlist.addSong(song2);
        library.addPlaylist(playlist);
        
        Playlist found = library.searchPlaylistByName("MyPlaylist");
        assertNotNull(found);
        
        Playlist notFound = library.searchPlaylistByName("Nonexistent");
        assertNull(notFound);
    }

    @Test
    void testGetSongTitles() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addSong("Rolling in the Deep", "Adele");
        library.addSong("Someone Like You", "Adele");
        
        List<String> titles = library.getSongTitles();
        assertEquals(2, titles.size());
        
        assertTrue(titles.contains("Rolling in the Deep"));
        assertTrue(titles.contains("Someone Like You"));
    }

    @Test
    void testGetAlbumTitles() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addAlbum("21", "Adele");
        List<String> titles = library.getAlbumTitles();
        assertEquals(1, titles.size());
        assertTrue(titles.contains("21"));
    }
    
    @Test
    void testGetArtists() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addSong("Rolling in the Deep", "Adele");
        library.addSong("Someone Like You", "Adele");
        List<String> artists = library.getArtists();
        assertEquals(1, artists.size());
        assertTrue(artists.contains("Adele"));
        
        library.addSong("The Cave", "Mumford & Sons");
        artists = library.getArtists();
        assertEquals(2, artists.size());
        assertTrue(artists.contains("Mumford & Sons"));

    }

    @Test
    void testGetPlaylistNames() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addPlaylist(new Playlist("Playlist1"));
        library.addPlaylist(new Playlist("Playlist2"));
        library.addPlaylist(new Playlist("DupPlaylist"));
        library.addPlaylist(new Playlist("DupPlaylist"));
        
        List<String> names = library.getPlaylistNames();
        
        assertEquals(3, names.size());
        assertTrue(names.contains("Playlist1"));
        assertTrue(names.contains("Playlist2"));
        assertTrue(names.contains("DupPlaylist"));
    }

    @Test
    void testGetFavoriteSongs() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addSong("Rolling in the Deep", "Adele");
        library.addSong("Someone Like You", "Adele");
        library.addSong("The Cave", "Mumford & Sons");
        
        List<Song> allSongs = library.getSongs();
        
        Song song1 = allSongs.get(0);  // rolling in the deep
        Song song2 = allSongs.get(1);  // someone like you
        Song song3 = allSongs.get(2);  // Mumford & Sons
        
        song1.setFavorite(true);
        song2.setFavorite(true);
        
        List<String> favorites = library.getFavoriteSongs();
        assertEquals(2, favorites.size());
        
        assertTrue(favorites.contains("Rolling in the Deep"));
        assertTrue(song1.isFavorite());
        
        assertTrue(favorites.contains("Someone Like You"));
        assertTrue(song2.isFavorite());
        
        assertFalse(favorites.contains("The Cave"));
        assertFalse(song3.isFavorite());
    }
    
    @Test
    void testRemoveSong() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addSong("Rolling in the Deep", "Adele");
        library.addAlbum("21", "Adele");
        List<Song> librarySongs = library.searchSongByTitle("Rolling in the Deep"); 
        
        Song songToBeRemoved = librarySongs.get(0);
        
        Playlist playlist = new Playlist("New Playlist");
        playlist.addSong(songToBeRemoved);
        
        library.addPlaylist(playlist);
        library.removeSong(songToBeRemoved);
        
        assertTrue(library.searchSongByTitle("Rolling in the Deep").isEmpty()); 
        
        assertFalse(playlist.getSongs().contains(songToBeRemoved));
        
        List<Album> albumInfo = library.searchAlbumByTitle("21");
        assertFalse(albumInfo.get(0).getSongs().contains(songToBeRemoved)); 
        
    }
    @Test
    void testRemoveSongLibrary() {
        MusicStore store = new MusicStore(); 
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        Song song = new Song("Rolling in the Deep", "Adele", "21", null, false);
        library.removeSong(song);
        List<Song> songs = library.getSongs(); 
        assertEquals(0, songs.size());
    }
    
    @Test
    void testRemoveAlbum() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addAlbum("21", "Adele");
        List<Album> initialAlbums = library.searchAlbumByTitle("21");
        assertEquals(1, initialAlbums.size());
        Album albumToRemove = initialAlbums.get(0);
        Playlist playlist = new Playlist("New Playlist");
        List<Song> allSongs = albumToRemove.getSongs();
        for (Song song : allSongs) {
            playlist.addSong(song);
        }
        library.addPlaylist(playlist);
        assertEquals(allSongs.size(), library.getSongs().size()); 
        assertEquals(allSongs.size(), playlist.getSongs().size());
        library.removeAlbum(albumToRemove);
        List<Album> albumList = library.searchAlbumByTitle("21");
        assertTrue(albumList.isEmpty());
        List<Song> songList = library.getSongs();
        assertTrue(songList.isEmpty());
        assertTrue(playlist.getSongs().isEmpty());
    }
    	
    @Test
    void testRemoveAlbumLibrary() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("Rolling in the Deep", "Adele", "21", null, false));
        Album albumToRemove = new Album("21", "Adele", "pop", "2011", songs);
        library.removeAlbum(albumToRemove);
        List<Album> albums = library.getAlbums();
        assertEquals(0, albums.size());
    }

    @Test
    void testGetAllPlaylistsForDisplay() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        List<Playlist> allPlaylists = library.getAllPlaylistsForDisplay();
        assertEquals(2, allPlaylists.size());
        assertEquals("Recent Songs", allPlaylists.get(0).getName());
        assertEquals("Frequent Songs", allPlaylists.get(1).getName());
    }
    
    @Test
    void testGetSongsSortedByTitle() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addSong("Rolling in the Deep", "Adele");
        library.addSong("Lovesong", "Adele");
        library.addSong("The Scientist", "Coldplay");

        List<Song> sortedByTitle = library.getSongsSortedByTitle();
        assertEquals("Lovesong", sortedByTitle.get(0).getSongTitle());
        assertEquals("Rolling in the Deep", sortedByTitle.get(1).getSongTitle());
        assertEquals("The Scientist", sortedByTitle.get(2).getSongTitle());
    }

    @Test
    void testGetSongsSortedByArtist() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addSong("Rolling in the Deep", "Adele");
        library.addSong("Lovesong", "Adele");
        library.addSong("The Scientist", "Coldplay");

        List<Song> sortedByArtist = library.getSongsSortedByArtist();
        assertEquals("Adele", sortedByArtist.get(0).getArtist());
        assertEquals("Adele", sortedByArtist.get(1).getArtist());
        assertEquals("Coldplay", sortedByArtist.get(2).getArtist());
    }

    @Test
    void testGetSongsSortedByRating() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);

        library.addSong("Rolling in the Deep", "Adele");
        library.addSong("Lovesong", "Adele");
        library.addSong("The Scientist", "Coldplay");

        List<Song> songs = library.getSongs();
        Song rollingInTheDeep = null;
        Song lovesong = null;
        for (Song song : songs) {
            if (song.getSongTitle().equals("Rolling in the Deep")) {
                rollingInTheDeep = song;
            } else if (song.getSongTitle().equals("Lovesong")) {
                lovesong = song;
            }
        }

        rollingInTheDeep.setRating(Rating.FIVE); 
        lovesong.setRating(Rating.FOUR); 

        List<Song> sortedByRating = library.getSongsSortedByRating();
        assertEquals("Lovesong", sortedByRating.get(0).getSongTitle()); 
        assertEquals("Rolling in the Deep", sortedByRating.get(1).getSongTitle()); 
        assertEquals("The Scientist", sortedByRating.get(2).getSongTitle());      
    }
}
