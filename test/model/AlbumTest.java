package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;;

public class AlbumTest {
	
	@Test
	void testAlbumTitle() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("Daydreamer", "Adele", "19", null, false));
        Album album = new Album("19", "Adele", "Pop", "2008", songs);
        assertEquals("19", album.getAlbumTitle());
	}
	
	@Test
	void testArtist() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("Daydreamer", "Adele", "19", null, false));
        Album album = new Album("19", "Adele", "Pop", "2008", songs);
        assertEquals("Adele", album.getArtist());
	}
	
	@Test
	void testGenre() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("Daydreamer", "Adele", "19", null, false));
        Album album = new Album("19", "Adele", "Pop", "2008", songs);
		assertEquals("Pop", album.getGenre());
	}
	
	@Test
	void testYear() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("Daydreamer", "Adele", "19", null, false));
        Album album = new Album("19", "Adele", "Pop", "2008", songs);
		assertEquals(2008, album.getYear());
	}
	
	@Test
	void testSongs() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("Daydreamer", "Adele", "19", null, false));
        songs.add(new Song("Best for Last", "Adele", "19", null, false));
        Album album = new Album("19", "Adele", "Pop", "2008", songs);
		assertEquals("Daydreamer", album.getSongs().get(0).getSongTitle());
		assertEquals("Best for Last", album.getSongs().get(1).getSongTitle());
	}
	
    @Test
    void testToString() {
       List<Song> songs = new ArrayList<>();
       songs.add(new Song("Daydreamer", "Adele", "19", null, false));
       songs.add(new Song("Best for Last", "Adele", "19", null, false));
       Album album = new Album("19", "Adele", "Pop", "2008", songs);

       String expectedToString = "Album: 19\n" +
               "Artist: Adele\n" +
               "Genre: Pop\n" +
               "Year: 2008\n" +
               "Songs:\n" +
               "- Song Title: Daydreamer, Artist: Adele, Album: 19\n" +
               "- Song Title: Best for Last, Artist: Adele, Album: 19\n";

       assertEquals(expectedToString, album.toString());
   }
}