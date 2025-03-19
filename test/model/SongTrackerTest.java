package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SongTrackerTest {
    private SongTracker songTracker;
    private Song[] songs;

    // Set up a SongTracker with 11 songs labeled A-K
    @BeforeEach
    void setUp() {
        songTracker = new SongTracker();
        songs = new Song[11];
        for (int i = 0; i < 11; i++) {
            char letter = (char) ('A' + i);
            songs[i] = new Song("Song" + letter, "Artist" + letter, "Album" + (i + 1), null, false);
        }
    }

    @Test
    void testPlaySongUpdatesPlayCount() {
        songTracker.playSong(songs[0]);
        assertEquals(1, songs[0].getPlayCount());
        songTracker.playSong(songs[0]);
        assertEquals(2, songs[0].getPlayCount());
    }

    @Test
    void testRecentSongsList() {
        songTracker.playSong(songs[0]);
        songTracker.playSong(songs[1]);
        songTracker.playSong(songs[0]);
        List<Song> recentList = songTracker.getRecentSongsPlaylist().getSongs();
        assertEquals(2, recentList.size());
        assertEquals(songs[0], recentList.get(0));
        assertEquals(songs[1], recentList.get(1));
    }

    @Test
    void testFrequentSongsList() {
        songTracker.playSong(songs[0]);
        songTracker.playSong(songs[0]);
        songTracker.playSong(songs[0]);
        songTracker.playSong(songs[1]);
        songTracker.playSong(songs[1]);
        songTracker.playSong(songs[2]);
        List<Song> frequentList = songTracker.getFrequentSongsPlaylist().getSongs();
        assertEquals(3, frequentList.size());
        assertEquals(songs[0], frequentList.get(0)); // 3 plays
        assertEquals(songs[1], frequentList.get(1)); // 2 plays
        assertEquals(songs[2], frequentList.get(2)); // 1 play
    }

    @Test
    void testRecentSongsOrder() {
        songTracker.playSong(songs[0]);
        songTracker.playSong(songs[1]);
        songTracker.playSong(songs[2]);
        songTracker.playSong(songs[1]);
        List<Song> recentList = songTracker.getRecentSongsPlaylist().getSongs();
        assertEquals(3, recentList.size());
        assertEquals(songs[1], recentList.get(0));
        assertEquals(songs[2], recentList.get(1));
        assertEquals(songs[0], recentList.get(2));
    }

    @Test
    void testFrequentSongsOrder() {
        songTracker.playSong(songs[2]);
        songTracker.playSong(songs[2]);
        songTracker.playSong(songs[2]);
        songTracker.playSong(songs[0]);
        songTracker.playSong(songs[0]);
        songTracker.playSong(songs[1]);
        List<Song> frequentList = songTracker.getFrequentSongsPlaylist().getSongs();
        assertEquals(3, frequentList.size());
        assertEquals(songs[2], frequentList.get(0)); // 3 plays
        assertEquals(songs[0], frequentList.get(1)); // 2 plays
        assertEquals(songs[1], frequentList.get(2)); // 1 play
    }

    @Test
    void testRecentSongsLimit() {
        for (int i = 0; i < 11; i++) {
            songTracker.playSong(songs[i]);
        }
        List<Song> recentList = songTracker.getRecentSongsPlaylist().getSongs();
        assertEquals(10, recentList.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(songs[10 - i], recentList.get(i));
        }
    }

    @Test
    void testFrequentSongsLimit() {
        for (int i = 0; i < 11; i++) {
            Song song = songs[i];
            for (int j = 0; j < 11 - i; j++) {
                songTracker.playSong(song);
            }
        }
        List<Song> frequentList = songTracker.getFrequentSongsPlaylist().getSongs();
        assertEquals(10, frequentList.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(songs[i], frequentList.get(i));
        }
    }

    @Test
    void testPlayingSameSongMultipleTimes() {
        for (int i = 0; i < 5; i++) {
            songTracker.playSong(songs[0]);
        }
        List<Song> recentList = songTracker.getRecentSongsPlaylist().getSongs();
        assertEquals(1, recentList.size());
        assertEquals(songs[0], recentList.get(0));
        
        List<Song> frequentList = songTracker.getFrequentSongsPlaylist().getSongs();
        assertEquals(1, frequentList.size());
        assertEquals(songs[0], frequentList.get(0));
        assertEquals(5, songs[0].getPlayCount());
    }
}