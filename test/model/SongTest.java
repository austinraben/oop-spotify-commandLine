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
        Song song = new Song("Daydreamer", "Adele", "19", null, true);
        song.setRating(Rating.THREE);
        song.setFavorite(true);
        assertEquals("Song Title: Daydreamer, Artist: Adele, Album: 19, Rating: 3, Favorite", song.toString());
    }
    
    @Test
    void testToStringNoRatingOrFavorite() {
        Song song = new Song("Daydreamer", "Adele", "19", null, true);
        assertEquals("Song Title: Daydreamer, Artist: Adele, Album: 19, Favorite", song.toString());
    }
}