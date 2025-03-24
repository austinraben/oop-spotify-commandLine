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
    @Test
    void testLibraryShuffle(){
    	MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);

        library.addSong("The Scientist","Coldplay");
        library.addSong("Rolling in the Deep", "Adele");
        library.addSong("Simple Things", "The Heavy");
      
        List<Song> originalSongs = library.getSongs();
        List<Song> shuffledSongs = library.getShuffledSongs();
        
        assertEquals(originalSongs.size(), shuffledSongs.size());
        assertTrue(shuffledSongs.containsAll(originalSongs));
        
        // line below might sometimes fail due to shuffling odds
        // assertFalse(originalSongs.equals(shuffledSongs));
    }

    @Test
    void testPlaylistShuffle() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        Playlist playlist = new Playlist("TestPlaylist");

        playlist.addSong(store.getSong("The Scientist", "Coldplay"));
        playlist.addSong(store.getSong("Rolling in the Deep", "Adele"));
        playlist.addSong(new Song("Simple Things", "The Heavy", "The House That Dirt Built", null, false));

        List<Song> originalSongs = playlist.getSongs();
        List<Song> shuffledSongs = playlist.getShuffledSongs();
        
        assertEquals(originalSongs.size(), shuffledSongs.size());
        assertTrue(shuffledSongs.containsAll(originalSongs));
        assertFalse(originalSongs.equals(shuffledSongs));
    }
    
    @Test
    void testGenrePlaylists() {
    	MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);
        
        library.addAlbum("Sons", "The Heavy");
        library.addSong("Rolling in the Deep", "Adele");

        List<Playlist> genrePlaylists = library.getGenrePlaylists();
        assertEquals(1, genrePlaylists.size());
        assertEquals("ROCK Playlist", genrePlaylists.get(0).getName());
        assertEquals(11, genrePlaylists.get(0).getSongs().size());

        boolean hasPopPlaylist = false;
        for (Playlist playlist : genrePlaylists) {
            if (playlist.getName().equals("POP Playlist")) {
                hasPopPlaylist = true;
                break;
            }
        }
        assertFalse(hasPopPlaylist);
    }
    
    @Test
    public void testAlbumIsInLibrary() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);

        library.addAlbum("21", "Adele");
        Album album21 = store.getAlbum("21", "Adele");
        assertTrue(library.albumIsInLibrary(album21));

        Album album25 = store.getAlbum("25", "Adele");
        if (album25 != null) {
            assertFalse(library.albumIsInLibrary(album25));
        }
    }
    
    @Test
    public void testGetFavoriteSongsPlaylist() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);

        library.addSong("Lovesong", "Adele");
        library.addSong("Someone Like You", "Adele");
        library.addSong("Rolling in the Deep", "Adele");

        List<Song> librarySongs = library.getSongs();
        for (Song s : librarySongs) {
            if (s.getSongTitle().equalsIgnoreCase("Lovesong")) {
                s.setFavorite(true);
            } else if (s.getSongTitle().equalsIgnoreCase("Someone Like You")) {
                s.setRating(Rating.FIVE);
            }
        }

        Playlist favorites = library.getFavoriteSongsPlaylist();
        List<Song> favoriteSongs = favorites.getSongs();
        assertEquals("Favorite Songs", favorites.getName());
        
        assertTrue(favoriteSongs.stream().anyMatch(s -> s.getSongTitle().equalsIgnoreCase("Lovesong")));
        assertTrue(favoriteSongs.stream().anyMatch(s -> s.getSongTitle().equalsIgnoreCase("Someone Like You")));
        assertFalse(favoriteSongs.stream().anyMatch(s -> s.getSongTitle().equalsIgnoreCase("Rolling in the Deep")));
    }
    
    @Test
    public void testGetPlaylists() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);

        Playlist p1 = new Playlist("Playlist1");
        Playlist p2 = new Playlist("Playlist2");
        library.addPlaylist(p1);
        library.addPlaylist(p2);

        List<Playlist> playlists = library.getPlaylists();
        assertEquals(2, playlists.size());
        assertTrue(playlists.contains(p1));
        assertTrue(playlists.contains(p2));
    }
    
    @Test
    public void testGetSongTracker() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);

        SongTracker tracker = library.getSongTracker();
        assertNotNull(tracker);
    }
    
    @Test
    public void testGetTopRatedPlaylist() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);

        library.addSong("Lovesong", "Adele");
        library.addSong("Someone Like You", "Adele");
        library.addSong("Rolling in the Deep", "Adele");

        List<Song> librarySongs = library.getSongs();
        for (Song s : librarySongs) {
            if (s.getSongTitle().equalsIgnoreCase("Lovesong")) {
                s.setRating(Rating.FOUR);
            } else if (s.getSongTitle().equalsIgnoreCase("Someone Like You")) {
                s.setRating(Rating.FIVE);
            }
        }

        Playlist topRated = library.getTopRatedPlaylist();
        List<Song> topSongs = topRated.getSongs();
        assertEquals("Top Rated", topRated.getName(), "Playlist name should be 'Top Rated'");
        assertTrue(topSongs.stream().anyMatch(s -> s.getSongTitle().equalsIgnoreCase("Lovesong"))); // rated 4
        assertTrue(topSongs.stream().anyMatch(s -> s.getSongTitle().equalsIgnoreCase("Someone Like You"))); // rated 5
        assertFalse(topSongs.stream().anyMatch(s -> s.getSongTitle().equalsIgnoreCase("Rolling in the Deep"))); // unrated
    }
    
    @Test
    public void testSetAlbums() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);

        // Create and set album list
        List<Album> testAlbums = new ArrayList<>();
        testAlbums.add(store.getAlbum("19", "Adele")); 
        testAlbums.add(store.getAlbum("21", "Adele"));
        library.setAlbums(testAlbums);

        assertEquals(testAlbums, library.getAlbums());
    }
    
    
    @Test
    public void testSetPlaylists() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);

        List<Playlist> testPlaylists = new ArrayList<>();
        Playlist p1 = new Playlist("Playlist1");
        Playlist p2 = new Playlist("Playlist2");
        testPlaylists.add(p1);
        testPlaylists.add(p2);
        library.setPlaylists(testPlaylists);

        assertEquals(testPlaylists, library.getPlaylists());
    }
    
    @Test
    public void testSetSongs() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);

        List<Song> testSongs = new ArrayList<>();
        testSongs.add(store.getSong("Lovesong", "Adele"));
        testSongs.add(store.getSong("Someone Like You", "Adele"));
        library.setSongs(testSongs);

        assertEquals(testSongs, library.getSongs());
    }

    @Test
    public void testSearchAlbumByTitle() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);

        library.addAlbum("19", "Adele");
        library.addAlbum("21", "Adele");

        // Search and verify
        List<Album> result1 = library.searchAlbumByTitle("21");
        assertEquals(1, result1.size(), "Should find one album titled '21'");
        assertEquals("21", result1.get(0).getAlbumTitle());

        List<Album> result2 = library.searchAlbumByTitle("Nonexistent Album");
        assertTrue(result2.isEmpty());
    }
    
    @Test
    public void testGetAlbumInformation() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        LibraryModel library = new LibraryModel(store);

        Song lovesong = store.getSong("Lovesong", "Adele");
        Album album21 = library.getAlbumInformation(lovesong);
        assertNotNull(album21, "Album should not be null for 'Lovesong'");
        assertEquals("21", album21.getAlbumTitle(), "Should return album '21' for 'Lovesong'");
        assertEquals("Adele", album21.getArtist(), "Artist should be 'Adele'");

        Song songWithNullAlbum = new Song("Test Song", "Test Artist", null, null, false);
        Album nullAlbumResult = library.getAlbumInformation(songWithNullAlbum);
        assertNull(nullAlbumResult);

        Album nullSongResult = library.getAlbumInformation(null);
        assertNull(nullSongResult);

        Song songWithInvalidAlbum = new Song("Invalid Song", "Artist", "NonExistentAlbum", null, false);
        Album invalidAlbumResult = library.getAlbumInformation(songWithInvalidAlbum);
        assertNull(invalidAlbumResult);
    }
}
