package model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MusicStoreTest {

    // Test readMusicFile() to read in files and create albumMap (hashmap)
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
    void testSearchStoreByIncorrectInfo() throws IOException {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);
        assertTrue(store.searchSongByTitle("Nonexistent").isEmpty());
        assertTrue(store.searchSongByArtist("Unknown").isEmpty());
        assertTrue(store.searchAlbumByTitle("Unknown Album").isEmpty());
        assertTrue(store.searchAlbumByArtist("Unknown").isEmpty());
        assertNull(store.getSong("Nonexistent", "Adele"));
        assertNull(store.getSong("Rolling in the Deep", "Unknown"));
        assertNull(store.getAlbum("Unknown Album", "Adele"));
        assertNull(store.getAlbum("Rolling in the Deep", "Unknown"));
    }
    
    
    // 
    @Test
    void testSearchSongByTitle() throws IOException {
        MusicStore store = new MusicStore();
        populateAlbumMap(store); 
        List<Song> songsByTitle = store.searchSongByTitle("Rolling in the Deep");
        assertEquals(1, songsByTitle.size());
        assertEquals("Rolling in the Deep", songsByTitle.get(0).getSongTitle());
    }
    
    @Test
    void testSearchSongByArtist() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store); 
        List<Song> songsByArtist = store.searchSongByArtist("Adele");
        boolean songFound = false;
        for (Song song : songsByArtist) {
            if (song.getSongTitle().equals("Someone Like You")) {
                songFound = true;
                break;
            }
        }
        assertTrue(songFound);
    }
    
    @Test
    void testSearchAlbumByTitle() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store); 
        List<Album> albumsByTitle = store.searchAlbumByTitle("21");
        assertEquals(1, albumsByTitle.size());
        assertEquals("21", albumsByTitle.get(0).getAlbumTitle());
    }
    
    @Test
    void testSearchAlbumByArtist() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store); 
        List<Album> albumsByArtist = store.searchAlbumByArtist("Adele");
        assertEquals(2, albumsByArtist.size());
        assertEquals("Adele", albumsByArtist.get(0).getArtist());
    }
    
    @Test
    void testGetSong() {
        MusicStore store = new MusicStore();
        populateAlbumMap(store); 
        Song song = store.getSong("Rolling in the Deep", "Adele");
        assertEquals("Rolling in the Deep", song.getSongTitle());
        assertEquals("21", song.getAlbumTitle());
    }


    @Test
    void testGetAlbum()  {
        MusicStore store = new MusicStore();
        populateAlbumMap(store);      
        Album album = store.getAlbum("21", "Adele");
        assertNull(store.getAlbum("21", "Unknown"));
        assertNull(store.getAlbum("Nonexistent", "Adele")); 
        assertNull(store.getAlbum("Nonexistent", "Unknown")); 
        assertEquals("21", album.getAlbumTitle());
        assertEquals("Adele", album.getArtist());
        assertEquals("Pop", album.getGenre());
        assertEquals(12, album.getSongs().size()); 
    }
}
