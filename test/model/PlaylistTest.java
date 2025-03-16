package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlaylistTest {

    @Test
    void testSetName() {
        Playlist playlist1 = new Playlist("OldName");
        playlist1.setName("NewName");
        assertEquals("NewName", playlist1.getName());
        
        Playlist playlist2 = new Playlist("OldName");
        playlist2.setName(" ");  // empty playlist names don't work
        assertEquals("OldName", playlist2.getName());  
    }

    @Test
    void testAddSong() {
        Playlist playlist = new Playlist("TestPlaylist");
        Song song1 = new Song("Daydreamer", "Adele", "19", null, false);
        playlist.addSong(song1);
        assertEquals(1, playlist.getSongs().size());
        
        Song song2 = new Song("Rolling in the Deep", "Adele", "21", null, false);
        playlist.addSong(song2);
        assertEquals(2, playlist.getSongs().size());
        
        Song dupSong = new Song("Rolling in the Deep", "Adele", "21", null, false);
        playlist.addSong(dupSong);
        assertEquals(2, playlist.getSongs().size());
        
        playlist.addSong(dupSong);
        assertEquals(2, playlist.getSongs().size());
    }

    @Test
    void testRemoveSong() {
        Playlist playlist = new Playlist("TestPlaylist");
        Song song1 = new Song("Daydreamer", "Adele", "19", null, false);
        playlist.addSong(song1);
        playlist.removeSong(song1);
        assertTrue(playlist.getSongs().isEmpty());
        
        Song song2 = new Song("Rolling in the Deep", "Adele", "21", null, false);
        playlist.addSong(song1);
        playlist.removeSong(song2);  // removing song not in playlist
        assertEquals(1, playlist.getSongs().size());
    }

    @Test
    void testEquals_SameInstance() {
        Playlist playlist = new Playlist("TestPlaylist");
        assertTrue(playlist.equals(playlist));
    }

    @Test
    void testEquals_NullOrDifferentType() {
        Playlist playlist = new Playlist("TestPlaylist");
        assertFalse(playlist.equals(null));
        assertFalse(playlist.equals(new Object()));
    }

    @Test
    void testEquals_SameName() {
        Playlist playlist1 = new Playlist("TestPlaylist");
        Playlist playlist2 = new Playlist("TESTPLAYLIST"); 
        assertTrue(playlist1.equals(playlist2));
        
        Playlist playlist3 = new Playlist("TestPlaylist");
        Playlist playlist4 = new Playlist("OtherPlaylist");
        assertFalse(playlist3.equals(playlist4));
    }
}