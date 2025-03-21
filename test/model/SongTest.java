package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SongTest {

    @Test
    void testGetSongTitle() {
        Song song = new Song("Daydreamer", "Adele", "19",null,  false);
        assertEquals("Daydreamer", song.getSongTitle());
    }

    @Test
    void testGetArtist() {
        Song song = new Song("Daydreamer", "Adele", "19", null, false);
        assertEquals("Adele", song.getArtist());
    }

    @Test
    void testGetAlbumTitle() {
        Song song = new Song("Daydreamer", "Adele", "19", null, false);
        assertEquals("19", song.getAlbumTitle());
    }

    @Test
    void testSongRating() {
        Song song = new Song("Daydreamer", "Adele", "19", null, false);
        song.setRating(Rating.FIVE);
        assertTrue(song.getRating().isPresent());
        assertEquals(5, song.getRating().get().getRatingValue());
    }

    @Test
    void testIsFavorite() {
        Song song = new Song("Daydreamer", "Adele", "19", null, false);
        song.setFavorite(true);
        assertTrue(song.isFavorite());

        song.setFavorite(false);
        assertFalse(song.isFavorite());
    }

    @Test
    void testToString() {
        Song song1 = new Song("Daydreamer", "Adele", "19", null, true);
        song1.setRating(Rating.THREE);
        song1.setFavorite(true);
        assertEquals("Daydreamer by Adele from '19', rated 3/5, favorite!", song1.toString());
        
        Song song2 = new Song("Lovesong", "Adele", "21", null, false);
        assertEquals("Lovesong by Adele from '21'", song2.toString());
    }
}