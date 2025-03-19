package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

public class UserTest {
    private static final String USERS_DIR = "users";
    private MusicStore musicStore;

    // Helper method to create a user file with username and all user content
    private void createUserFile(String username, String content) throws IOException {
        File dir = new File(USERS_DIR);
        if (!dir.exists()) dir.mkdir();
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_DIR + "/" + username + ".txt"))) {
            writer.println(content);
        }
    }

    // Populate Music Store before each test case runs
    @BeforeEach
    void setUp() {
        musicStore = new MusicStore();
        try {
            String folderPath = "src/model.albums/";
            File folder = new File(folderPath);
            File[] files = folder.listFiles();
            for (File file : files) {
                musicStore.readMusicFile(file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Delete User directory after test case runs
    @AfterEach
    void tearDown() {
        File dir = new File(USERS_DIR);
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                file.delete();
            }
            dir.delete();
        }
    }

    @Test
    void testGetUsername() throws NoSuchAlgorithmException {
        User user = new User("testuser", "password", musicStore);
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void testGetSalt() throws NoSuchAlgorithmException {
        User user = new User("testuser", "password123", new MusicStore());
        byte[] salt = user.getSalt();
        assertNotNull(salt);
        assertEquals(16, salt.length);
    }

    @Test
    void testGetHashedPassword() throws NoSuchAlgorithmException {
        User user = new User("testuser", "password123", new MusicStore());
        byte[] hashedPassword = user.getHashedPassword();
        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.length > 0);
    }
    
    @Test
    void testGetLibraryModel() throws NoSuchAlgorithmException {
        User user = new User("testuser", "password", musicStore);
        assertNotNull(user.getLibraryModel());
    }

    @Test
    void testVerifyPasswordCorrect() throws NoSuchAlgorithmException {
        User user = new User("testuser", "password123", musicStore);
        assertTrue(user.verifyPassword("password123"));
    }

    @Test
    void testVerifyPasswordIncorrect() throws NoSuchAlgorithmException {
        User user = new User("testuser", "password123", musicStore);
        assertFalse(user.verifyPassword("wrongpassword"));
    }

    /*
     * Tests for load()
     */
    
    @Test
    void testLoadUserNoMusic() throws IOException, NoSuchAlgorithmException {
        String username = "testuser";
        String content = "[username]\n" + username + 
        				 "\n[salt]\n" + Base64.getEncoder().encodeToString(new byte[16]) + 
        				 "\n[hashed_password]\n" + Base64.getEncoder().encodeToString(new byte[16]);
        
        createUserFile(username, content);
        
        User user = User.load(username, musicStore);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertTrue(user.getLibraryModel().getSongs().isEmpty());
        assertTrue(user.getLibraryModel().getAlbums().isEmpty());
        assertTrue(user.getLibraryModel().getPlaylists().isEmpty());
    }

    @Test
    void testLoadUserWithSongs() throws IOException, NoSuchAlgorithmException {
        String username = "testuser";
        String content = "[username]\n" + 
        					username + 
        				 "\n[salt]\n" + 
        				 	Base64.getEncoder().encodeToString(new byte[16]) + 
        				 "\n[hashed_password]\n" + 
        				 	Base64.getEncoder().encodeToString(new byte[16]) + 
        				 "\n[songs]\n" + 
        				 	"Song1|Artist1|Album1|3|true\n" +
        				 	"Song2|Artist2|Album2||false";
        
        createUserFile(username, content);
        
        User user = User.load(username, musicStore);
        assertNotNull(user);
        assertEquals(2, user.getLibraryModel().getSongs().size());
        
        Song song1 = user.getLibraryModel().getSongs().get(0);
        assertEquals("Song1", song1.getSongTitle());
        assertTrue(song1.getRating().isPresent());
        assertEquals(3, song1.getRating().get().getRatingValue());
        assertTrue(song1.isFavorite());
        
        Song song2 = user.getLibraryModel().getSongs().get(1);
        assertEquals("Song2", song2.getSongTitle());
        assertFalse(song2.getRating().isPresent());
        assertFalse(song2.isFavorite());
    }

    @Test
    void testLoadUserWithAlbums() throws IOException, NoSuchAlgorithmException {
        String username = "testuser";
        String content = "[username]\n" + 
        					username + 
        				 "\n[salt]\n" +
                         	Base64.getEncoder().encodeToString(new byte[16]) + 
                         "\n[hashed_password]\n" +
                         	Base64.getEncoder().encodeToString(new byte[16]) + 
                         "\n[songs]\n" +
                         	"Song1|Artist1|Album1|3|true\n" +
                         	"Song2|Artist1|Album1||false\n" +
                         "[albums]\n" +
                         	"Album1|Artist1|Pop|2020|Song1,Song2";
        
        createUserFile(username, content);
        
        User user = User.load(username, musicStore);
        assertNotNull(user);
        assertEquals(1, user.getLibraryModel().getAlbums().size());
        
        Album album = user.getLibraryModel().getAlbums().get(0);
        assertEquals("Album1", album.getAlbumTitle());
        assertEquals("Artist1", album.getArtist());
        assertEquals("Pop", album.getGenre());
        assertEquals(2020, album.getYear());
        assertEquals(2, album.getSongs().size());
    }

    @Test
    void testLoadUserWithPlaylists() throws IOException, NoSuchAlgorithmException {
        String username = "testuser";
        String content = "[username]\n" + 
        					username + 
        				 "\n[salt]\n" +
                         	Base64.getEncoder().encodeToString(new byte[16]) + 
                         "\n[hashed_password]\n" +
                         	Base64.getEncoder().encodeToString(new byte[16]) + 
                         "\n[songs]\n" +
                         	"Song1|Artist1|Album1|3|true\n" +
                         	"Song2|Artist2|Album2||false\n" +
                         "[playlists]\n" +
                         	"MyPlaylist|Song1,Artist1;Song2,Artist2";
        
        createUserFile(username, content);
        
        User user = User.load(username, musicStore);
        assertNotNull(user);
        assertEquals(1, user.getLibraryModel().getPlaylists().size());
        
        Playlist playlist = user.getLibraryModel().getPlaylists().get(0);
        assertEquals("MyPlaylist", playlist.getName());
        assertEquals(2, playlist.getSongs().size());
    }
    
    /*
     * Tests for save()
     */
    @Test
    void testSaveUserWithSongs() throws NoSuchAlgorithmException, IOException {
        User user = new User("testuser", "password123", musicStore);
        
        user.getLibraryModel().addSong("Rolling in the Deep", "Adele");
        user.getLibraryModel().addSong("The Scientist", "Coldplay");
        
        List<Song> songs = user.getLibraryModel().getSongs();
        Song rollingInTheDeep = null;
        Song theScientist = null;
        for (Song song : songs) {
            if (song.getSongTitle().equals("Rolling in the Deep") && song.getArtist().equals("Adele")) {
                rollingInTheDeep = song;
            } else if (song.getSongTitle().equals("The Scientist") && song.getArtist().equals("Coldplay")) {
                theScientist = song;
            }
        }
        rollingInTheDeep.setFavorite(true);
        theScientist.setRating(Rating.ONE);
        
        user.save();

        File userFile = new File(USERS_DIR + "/testuser.txt");
        assertTrue(userFile.exists());

        String fileContent = new String(Files.readAllBytes(Paths.get(USERS_DIR + "/testuser.txt")));
        assertTrue(fileContent.contains("Rolling in the Deep|Adele|21||true"));
        assertTrue(fileContent.contains("The Scientist|Coldplay|A Rush of Blood to the Head|1|false"));
    }

    @Test
    void testSaveUserWithAlbums() throws NoSuchAlgorithmException, IOException {
        User user = new User("testuser", "password123", musicStore);
        
        user.getLibraryModel().addAlbum("19", "Adele");
        user.getLibraryModel().addAlbum("21", "Adele");
        
        List<Album> albums = user.getLibraryModel().getAlbums();
        Album ninteen = null;
        Album twentyOne = null;
        
        for (Album album : albums) {
            if (album.getAlbumTitle().equals("19") && album.getArtist().equals("Adele")) {
                ninteen = album;
            }
            if (album.getAlbumTitle().equals("21") && album.getArtist().equals("Adele")) {
                twentyOne = album;
            }
        }
        
        assertNotNull(ninteen);
        assertNotNull(twentyOne);
    
        // Modify a song within the album
	    List<Song> ninteenSongs = ninteen.getSongs();
	    Song daydreamerSong = null;
	    for (Song song : ninteenSongs) {
	        if (song.getSongTitle().equals("Daydreamer")) {
	            daydreamerSong = song;
	            break;
	        }
	    }
	    assertNotNull(daydreamerSong);
	    daydreamerSong.setFavorite(true);
	    daydreamerSong.setRating(Rating.FOUR);
	    
	    user.save();
	
	    File userFile = new File(USERS_DIR + "/testuser.txt");
	    assertTrue(userFile.exists());
	
	    String fileContent = new String(Files.readAllBytes(Paths.get(USERS_DIR + "/testuser.txt")));
	    assertTrue(fileContent.contains("19|Adele|Pop|2008|Daydreamer,Best for Last,"));
	    assertTrue(fileContent.contains("21|Adele|Pop|2011|Rolling in the Deep,"));
        assertTrue(fileContent.contains("Daydreamer|Adele|19|4|true"));
    }
    
    @Test
    void testSaveUserWithPlaylists() throws NoSuchAlgorithmException, IOException {
        User user = new User("testuser", "password123", musicStore);
        
        user.getLibraryModel().addSong("Daydreamer", "Adele"); 
        user.getLibraryModel().addSong("Lovesong", "Adele");
        
        // Retrieve the songs from libraryModel.getSongs() to modify them
        List<Song> songs = user.getLibraryModel().getSongs();
        Song daydreamerSong = null;
        Song lovesongSong = null;
        for (Song song : songs) {
            if (song.getSongTitle().equals("Daydreamer") && song.getArtist().equals("Adele")) {
                daydreamerSong = song;
            } else if (song.getSongTitle().equals("Lovesong") && song.getArtist().equals("Adele")) {
                lovesongSong = song;
            }
        }

        daydreamerSong.setRating(Rating.FIVE);
        daydreamerSong.setFavorite(true);
        
        lovesongSong.setRating(Rating.THREE);
        lovesongSong.setFavorite(true);
        
        // Add songs to the playlist
        Playlist playlist = new Playlist("MyPlaylist");
        playlist.addSong(daydreamerSong);
        playlist.addSong(lovesongSong);
        user.getLibraryModel().addPlaylist(playlist);
        
        List<Playlist> playlists = user.getLibraryModel().getPlaylists();
        Playlist myPlaylist = null;
        for (Playlist pl : playlists) {
            if (pl.getName().equals("MyPlaylist")) {
                myPlaylist = pl;
            }
        }
        assertNotNull(myPlaylist);
        
        // Modify Lovesong after adding to the playlist
        assertNotNull(lovesongSong);
        lovesongSong.setRating(Rating.ONE);
        lovesongSong.setFavorite(false);
        
        user.save();

        File userFile = new File(USERS_DIR + "/testuser.txt");
        assertTrue(userFile.exists());

        String fileContent = new String(Files.readAllBytes(Paths.get(USERS_DIR + "/testuser.txt")));
        assertTrue(fileContent.contains("MyPlaylist|Daydreamer,Adele;Lovesong,Adele"));
        assertTrue(fileContent.contains("Daydreamer|Adele|19|5|true"));
        assertTrue(fileContent.contains("Lovesong|Adele|21|1|false"));
    }

}