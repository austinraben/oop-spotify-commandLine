/*
 * LA2--User.java to create users and load user information
 * 
 * A User has the following data structures:
 *    - String username
 *    - byte[] for salt and hashedPassword
 *        - byte arrays are compatiable with crytographic algorithms 
 *        - generateSalt() uses SecureRandom() to generate a random, secure 16 byte array
 *        - hashPassowrd(password, salt) uses MessageDigest's API to create a hashed password 
 *           - This hashed password is secure and almost impossible to convert back to the user's password
 */

package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
	private String username;
	private byte[] salt;
	private byte[] hashedPassword;
	private LibraryModel libraryModel;
	
	// Constructor for new user
	public User(String username, String password, MusicStore musicStore) throws NoSuchAlgorithmException {
		this.username = username;
		this.salt = generateSalt();
		this.hashedPassword = hashPassword(password, salt);
		this.libraryModel = new LibraryModel(musicStore);
	}
	
	// Overloaded constructor for loading existing user
	private User(String username, byte[] salt, byte[] hashedPassword, MusicStore musicStore) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
        this.libraryModel = new LibraryModel(musicStore);
    }

    public String getUsername() {
        return username;
    }
    
	public LibraryModel getLibraryModel() {
        return libraryModel;
    }
	
	public byte[] getSalt() {
        return salt.clone();
    }

    public byte[] getHashedPassword() {
        return hashedPassword.clone();
    }
	
	/*
	 * Generate random 16 byte salt for passwords
	 */
	private byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
	
	/*
	 * Use MessageDigest API to hash password + salt
	 */
	private byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(salt);					
		md.update(password.getBytes());		
		return md.digest();
	}
	
	/*
	 * Verify that a given password is a match to the hashed password
	 */
	public boolean verifyPassword(String password) throws NoSuchAlgorithmException {
        byte[] testHash = hashPassword(password, salt);
        return Arrays.equals(testHash, hashedPassword);
    }

	/*
	 * Save user data to a .txt file
	 */
	public void save() {
        File dir = new File("users");
        if (!dir.exists()) dir.mkdir();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("users/" + username + ".txt"))) {
            writer.println("[username]");
            writer.println(username);
            writer.println("[salt]");
            writer.println(Base64.getEncoder().encodeToString(salt));
            writer.println("[hashed_password]");
            writer.println(Base64.getEncoder().encodeToString(hashedPassword));
            
            writer.println("[songs]");
            for (Song song : libraryModel.getSongs()) {
                String ratingStr = song.getRating().isPresent() ? String.valueOf(song.getRating().get().getRatingValue()) : "";
                writer.println(song.getSongTitle() + "|" + song.getArtist() + "|" + song.getAlbumTitle() + "|" + 
                              ratingStr + "|" + song.isFavorite());
            }
            
            writer.println("[albums]");
            for (Album album : libraryModel.getAlbums()) {
                StringBuilder songList = new StringBuilder();
                for (Song song : album.getSongs()) {
                    songList.append(song.getSongTitle()).append(",");
                }
                writer.println(album.getAlbumTitle() + "|" + album.getArtist() + "|" + album.getGenre() + "|" + 
                              album.getYear() + "|" + songList.toString());
            }
            
            writer.println("[playlists]");
            for (Playlist playlist : libraryModel.getPlaylists()) {
                StringBuilder songList = new StringBuilder();
                for (Song song : playlist.getSongs()) {
                    songList.append(song.getSongTitle()).append(",").append(song.getArtist()).append(";");
                }
                writer.println(playlist.getName() + "|" + songList.toString());
            }
            
            writer.println("[song_tracker]");
            SongTracker songTracker = libraryModel.getSongTracker();

            writer.println("[recent_songs]");
            for (Song song : songTracker.getRecentSongsPlaylist().getSongs()) {
                writer.println(song.getSongTitle() + "|" + song.getArtist());
            }
            
            writer.println("[play_counts]");
            for (Map.Entry<Song, Integer> entry : songTracker.getCountMap().entrySet()) {
                Song song = entry.getKey();
                int count = entry.getValue();
                writer.println(song.getSongTitle() + "|" + song.getArtist() + "|" + count);
            }
            
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }
	
	/* Load user data from .txt file given the username
	 * 
	 */
	public static User load(String username, MusicStore musicStore) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users/" + username + ".txt"))) {
            String line;
            byte[] salt = null;
            byte[] hashedPassword = null;
            Map<String, Song> songMap = new HashMap<>(); 
            List<Song> songs = new ArrayList<>();
            List<Album> albums = new ArrayList<>();
            List<Playlist> playlists = new ArrayList<>();
            List<Song> recentSongs = new ArrayList<>();
            Map<Song, Integer> playCounts = new HashMap<>();
            
            String section = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("[")) {
                    section = line.substring(1, line.length() - 1);
                } else if (section != null && !line.trim().isEmpty()) {
                    switch (section) {
                        case "username":
                            if (!line.equals(username)) throw new IOException("Username mismatch");
                            break;
                        case "salt":
                            salt = Base64.getDecoder().decode(line);
                            break;
                        case "hashed_password":
                            hashedPassword = Base64.getDecoder().decode(line);
                            break;
                            
                        case "songs":
                            String[] songParts = line.split("\\|");
                            String songTitle = songParts[0];
                            String artist = songParts[1];
                            String albumTitleFromSong = songParts[2];
                            Rating rating = songParts[3].isEmpty() ? null : Rating.values()[Integer.parseInt(songParts[3]) - 1];
                            boolean favorite = Boolean.parseBoolean(songParts[4]);
                            Song song = new Song(songTitle, artist, albumTitleFromSong, rating, favorite);
                            songMap.put(songTitle + "|" + artist, song); 
                            songs.add(song);
                            break;
                            
                        case "albums":
                            String[] albumParts = line.split("\\|");
                            String albumTitle = albumParts[0];
                            String albumArtist = albumParts[1];
                            String genre = albumParts[2];
                            String year = albumParts[3];
                            List<Song> albumSongs = new ArrayList<>();
                            String[] aSongs = albumParts[4].split(",");
                            for (String title : aSongs) {
                                if (!title.isEmpty()) {
                                    Song s = songMap.get(title + "|" + albumArtist); 
                                    if (s != null) {
                                        albumSongs.add(s);
                                    }
                                }
                            }
                            Album album = new Album(albumTitle, albumArtist, genre, year, albumSongs);
                            albums.add(album);
                            break;
                            
                        case "playlists":
                            String[] playlistParts = line.split("\\|");
                            String playlistName = playlistParts[0];
                            Playlist playlist = new Playlist(playlistName);
                            if (playlistParts.length > 1 && !playlistParts[1].isEmpty()) {
                                String[] pSongPairs = playlistParts[1].split(";");
                                for (String pair : pSongPairs) {
                                    if (!pair.isEmpty()) {
                                        String[] songInfo = pair.split(",");
                                        String sTitle = songInfo[0];
                                        String sArtist = songInfo[1];
                                        Song s = songMap.get(sTitle + "|" + sArtist);
                                        if (s != null) {
                                            playlist.addSong(s);
                                        }
                                    }
                                }
                            }
                            playlists.add(playlist);
                            break;
                            
                        case "song_tracker":
                            break;
                        case "recent_songs":
                            String[] recentParts = line.split("\\|");
                            String recentTitle = recentParts[0];
                            String recentArtist = recentParts[1];
                            Song recentSong = songMap.get(recentTitle + "|" + recentArtist);
                            if (recentSong != null) {
                                recentSongs.add(recentSong);
                            }
                            break;
                        case "play_counts":
                            String[] countParts = line.split("\\|");
                            String countTitle = countParts[0];
                            String countArtist = countParts[1];
                            int countValue = Integer.parseInt(countParts[2]);
                            Song countSong = songMap.get(countTitle + "|" + countArtist);
                            if (countSong != null) {
                                playCounts.put(countSong, countValue);
                            }
                    }
                }
            }
            if (salt == null || hashedPassword == null) {
                throw new IOException("Missing salt or hashed password");
            }
            
            User user = new User(username, salt, hashedPassword, musicStore);
            user.libraryModel.setSongs(songs);
            user.libraryModel.setAlbums(albums);
            user.libraryModel.setPlaylists(playlists);
            
            SongTracker songTracker = user.libraryModel.getSongTracker();
            songTracker.setRecentSongs(recentSongs);
            songTracker.setPlayCounts(playCounts);
            songTracker.updatePlaylists();
            return user;
        } catch (IOException e) {
            System.out.println("Error loading user data: " + e.getMessage());
            return null;
        }
    }
}