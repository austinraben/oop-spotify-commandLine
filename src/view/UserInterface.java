package view;

import model.*;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private MusicStore musicStore;
    private User currentUser;
    private LibraryModel libraryModel;
    private Scanner scanner;

    // ANSI color codes for enhanced UI (AI generated)
    private static final String RESET = "\u001B[0m";  // default text color
    private static final String CYAN = "\u001B[36m";  // input text
    private static final String BLUE = "\u001B[34m";  // Sub-menu header 
    private static final String MAGENTA = "\u001B[35m";  // list header 
    private static final String GREEN = "\u001B[32m";  // success operation and login/main menus
    private static final String YELLOW = "\u001B[33m";  // unsuccessful operation 
    private static final String RED = "\u001B[31m";  // invalid input 

    public UserInterface(MusicStore musicStore, LibraryModel libraryModel) {
        this.musicStore = musicStore;
        this.libraryModel = libraryModel;
        this.scanner = new Scanner(System.in);
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException {
        MusicStore musicStore = new MusicStore();       

        // Populate MusicStore with files
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
            
        // Start UI with populated MusicStore
        LibraryModel libraryModel = new LibraryModel(musicStore); 
        UserInterface ui = new UserInterface(musicStore, libraryModel);
        ui.start();
    }

    /*
	 * Landing page for user to login or create new account
     */
    public void start() throws NoSuchAlgorithmException {
    	boolean programRunning = true; 
        boolean loggedIn = false;
        
        while (programRunning) {
        	while (!loggedIn) {
	            System.out.println(GREEN + "=== Login Menu ===" + RESET);
	            System.out.println("1. Login");
	            System.out.println("2. Create new account");
	            System.out.println("3. Exit");
	            System.out.print("Enter your choice (1-3): ");
	            
	            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
	            
	            switch (choice) {
	                case "1":
	                    System.out.print("Enter username: ");
	    	            System.out.print(CYAN); String username = scanner.nextLine(); System.out.print(RESET);
	                    System.out.print("Enter password: ");
	    	            System.out.print(CYAN); String password = scanner.nextLine(); System.out.print(RESET);
	                    File userFile = new File("users/" + username + ".txt");
	                    if (userFile.exists()) {
	                        User user = User.load(username, musicStore);
	                        if (user != null && user.verifyPassword(password)) {
	                            currentUser = user;
	                            libraryModel = user.getLibraryModel();
	                            loggedIn = true;
	                            System.out.println(GREEN + "Login successful!" + RESET);
	                            System.out.println("\n");
	                        } else {
	                            System.out.println(YELLOW + "Invalid username or password." + RESET);
	                        }
	                    } else {
	                        System.out.println(YELLOW + "Username not found." + RESET);
	                    }
	                    break;
	                    
	                case "2":
	                    System.out.print("Enter new username: ");
	    	            System.out.print(CYAN); String newUsername = scanner.nextLine(); System.out.print(RESET);
	                    File newUserFile = new File("users/" + newUsername + ".txt");
	                    if (newUserFile.exists()) {
	                        System.out.println(YELLOW + "Username already exists." + RESET);
	                    } else {
	                        System.out.print("Enter new password: ");
		    	            System.out.print(CYAN); String newPassword = scanner.nextLine(); System.out.print(RESET);
	                        try {
	                            User newUser = new User(newUsername, newPassword, musicStore);
	                            currentUser = newUser;
	                            libraryModel = newUser.getLibraryModel();
	                            newUser.save();
	                            loggedIn = true;
	                            System.out.println(GREEN + "Account created and logged in successfully." + RESET);
	                        } catch (NoSuchAlgorithmException e) {
	                            System.out.println("Error creating user: " + e.getMessage());
	                        }
	                    }
	                    break;
	                    
	                case "3":
	                    System.out.println("Exiting...");
	                    if (currentUser != null) {
	                        currentUser.save();
	                    }
	                    return;
	                default:
	                    System.out.println(RED + "Invalid option. Please try again." + RESET);
	            }
        	}
    
	    /*
	    * Display the main menu and provide the user 7 options
	    * Each input will call a function to prompt further options
	    * The user can return to this Main Menu at any time
	    */
	    while (loggedIn && programRunning) {
	        displayMainMenu();
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
	        switch (choice) {
	            case "1":
	            	System.out.println();
	                searchMusicStore();
	                break;
	            case "2":
	            	System.out.println();
	                searchLibrary();
	                break;
	            case "3":
	            	System.out.println();
	                addToLibrary();
	                break;
	            case "4":
	            	System.out.println();
	                viewLibraryLists();
	                break;
	            case "5":
	            	System.out.println();
	                managePlaylists();
	                break;
	            case "6":
	            	System.out.println();
	                manageSongs();
	                break;
	            case "7":
	                System.out.println("Saving...");
	                System.out.println('\n');
	                if (currentUser != null) {
	                    currentUser.save();
	                }
	                currentUser = null;
	                libraryModel = null;
	                loggedIn = false;
	                break;
	            default:
	                System.out.println(RED + "Invalid option. Please try again." + RESET);
	                break;
	        	}
	    	}
	    }
    }

    private void displayMainMenu() {
        System.out.println(GREEN + "Welcome to the Music Library System!" + RESET);
        System.out.println(GREEN + "=== Main Menu ===" + RESET);
        System.out.println("1. Search Music Store");
        System.out.println("2. Search Library");
        System.out.println("3. Add to Library");
        System.out.println("4. View Library Lists");
        System.out.println("5. Manage Playlists");
        System.out.println("6. Manage Songs");
        System.out.println("7. Save and Logout");
        System.out.print("Enter your choice (1-7): ");
    }

    /*
     * Option 1 from Main Menu
     */
    private void searchMusicStore() {
        boolean searching = true;
        while (searching) {
            System.out.println(BLUE + "=== Search Music Store ===" + RESET);
            System.out.println("1. Song by Title");
            System.out.println("2. Song by Artist");
            System.out.println("3. Album by Title");
            System.out.println("4. Album by Artist");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice (1-5): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            switch (choice) {
                case "1":
                    System.out.print("Enter song title: ");
    	            System.out.print(CYAN); String title = scanner.nextLine(); System.out.print(RESET);
                    List<Song> songsByTitle = musicStore.searchSongByTitle(title);
                    displaySongList(songsByTitle, "Songs Found");
                    break;
                case "2":
                    System.out.print("Enter artist: ");
    	            System.out.print(CYAN); String artist = scanner.nextLine(); System.out.print(RESET);
                    List<Song> songsByArtist = musicStore.searchSongByArtist(artist);
                    displaySongList(songsByArtist, "Songs Found");
                    break;
                case "3":
                    System.out.print("Enter album title: ");
    	            System.out.print(CYAN); String albumTitle = scanner.nextLine(); System.out.print(RESET);
                    List<Album> albumsByTitle = musicStore.searchAlbumByTitle(albumTitle);
                    displayAlbumList(albumsByTitle, "Albums Found");
                    break;
                case "4":
                    System.out.print("Enter artist: ");
    	            System.out.print(CYAN); String albumArtist = scanner.nextLine(); System.out.print(RESET);
                    List<Album> albumsByArtist = musicStore.searchAlbumByArtist(albumArtist);
                    displayAlbumList(albumsByArtist, "Albums Found");
                    break;
                case "5":
                	System.out.println();
                    searching = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    /*
     * Option 2 from Main Menu
     */
    private void searchLibrary() {
        boolean searching = true;
        while (searching) {
            System.out.println(BLUE + "=== Search Library ===" + RESET);
            System.out.println("1. Song by Title");
            System.out.println("2. Song by Artist");
            System.out.println("3. Album by Title");
            System.out.println("4. Album by Artist");
            System.out.println("5. Playlist by Name");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice (1-6): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            switch (choice) {
                case "1":
                    System.out.print("Enter song title: ");
                    System.out.print(CYAN); String title = scanner.nextLine(); System.out.print(RESET);
                    List<Song> songsByTitle = libraryModel.searchSongByTitle(title);
                    displaySongList(songsByTitle, "Songs Found in Library");
                    break;
                case "2":
                    System.out.print("Enter artist: ");
                    System.out.print(CYAN); String artist = scanner.nextLine(); System.out.print(RESET);
                    List<Song> songsByArtist = libraryModel.searchSongByArtist(artist);
                    displaySongList(songsByArtist, "Songs Found in Library");
                    break;
                case "3":
                    System.out.print("Enter album title: ");
                    System.out.print(CYAN); String albumTitle = scanner.nextLine(); System.out.print(RESET);
                    List<Album> albumsByTitle = libraryModel.searchAlbumByTitle(albumTitle);
                    displayAlbumList(albumsByTitle, "Albums Found in Library");
                    break;
                case "4":
                    System.out.print("Enter artist: ");
                    System.out.print(CYAN); String albumArtist = scanner.nextLine(); System.out.print(RESET);
                    List<Album> albumsByArtist = libraryModel.searchAlbumByArtist(albumArtist);
                    displayAlbumList(albumsByArtist, "Albums Found in Library");
                    break;
                case "5":
                    System.out.print("Enter playlist name: ");
                    System.out.print(CYAN); String playlistName = scanner.nextLine(); System.out.print(RESET);
                    Playlist playlist = libraryModel.searchPlaylistByName(playlistName);
                    if (playlist != null) {
                        System.out.println(MAGENTA + "=== Playlist: " + playlist.getName() + " ===" + RESET);
                        List<Song> songs = playlist.getSongs();
                        for (int i = 0; i < songs.size(); i++) {
                            System.out.println((i + 1) + ". " + songs.get(i).getSongTitle() + " by " + songs.get(i).getArtist());
                        }
                    } else {
                        System.out.println(YELLOW + "Playlist not found." + RESET);
                    }
                    break;
                case "6":
                	System.out.println();
                    searching = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    /*
     * Option 3 from Main Menu
     */
    private void addToLibrary() {
        boolean adding = true;
        while (adding) {
            System.out.println(BLUE + "=== Add to Library ===" + RESET);
            System.out.println("1. Add Song");
            System.out.println("2. Add Album");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice (1-3): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            switch (choice) {
                case "1":
                    System.out.print("Enter song title: ");
                    System.out.print(CYAN); String songTitle = scanner.nextLine(); System.out.print(RESET);
                    System.out.print("Enter artist: ");
                    System.out.print(CYAN); String songArtist = scanner.nextLine(); System.out.print(RESET);
                    boolean songAdded = libraryModel.addSong(songTitle, songArtist);
                    if (songAdded) {
                        System.out.println(GREEN + "Song added to library successfully." + RESET);
                    } else {
                        System.out.println(YELLOW + "Song not found in the store or already in the library." + RESET);
                    }
                    break;
                case "2":
                    System.out.print("Enter album title: ");
                    System.out.print(CYAN); String albumTitle = scanner.nextLine(); System.out.print(RESET);
                    System.out.print("Enter artist: ");
                    System.out.print(CYAN); String albumArtist = scanner.nextLine(); System.out.print(RESET);
                    boolean albumAdded = libraryModel.addAlbum(albumTitle, albumArtist);
                    if (albumAdded) {
                        System.out.println(GREEN + "Album added to library successfully." + RESET);
                    } else {
                        System.out.println(YELLOW + "Album not found in the store or already in the library." + RESET);
                    }
                    break;
                case "3":
                	System.out.println();
                    adding = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    /*
     * Option 4 from Main Menu
     */
    private void viewLibraryLists() {
        boolean viewing = true;
        while (viewing) {
            System.out.println(BLUE + "=== View Library Lists ===" + RESET);
            System.out.println("1. Song Titles");
            System.out.println("2. Artists");
            System.out.println("3. Albums");
            System.out.println("4. Playlists");
            System.out.println("5. Favorite Songs");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice (1-6): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            switch (choice) {
                case "1":
                    List<String> songTitles = libraryModel.getSongTitles();
                    displayList(songTitles, "Song Titles in Library");
                    break;
                case "2":
                    List<String> artists = libraryModel.getArtists();
                    displayList(artists, "Artists in Library");
                    break;
                case "3":
                    List<String> albumTitles = libraryModel.getAlbumTitles();
                    displayList(albumTitles, "Albums in Library");
                    break;
                case "4":
                    List<String> playlistNames = libraryModel.getPlaylistNames();
                    displayList(playlistNames, "Playlists in Library");
                    break;
                case "5":
                    List<String> favoriteSongs = libraryModel.getFavoriteSongs();
                    displayList(favoriteSongs, "Favorite Songs in Library");
                    break;
                case "6":
                	System.out.println();
                    viewing = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    /*
     * Option 5 from Main Menu
     */
    private void managePlaylists() {
        boolean managing = true;
        while (managing) {
            System.out.println(BLUE + "=== Manage Playlists ===" + RESET);
            System.out.println("1. Create New Playlist");
            System.out.println("2. Add Song to Playlist");
            System.out.println("3. Remove Song from Playlist");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice (1-4): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            switch (choice) {
                case "1":
                    System.out.print("Enter playlist name: ");
                    System.out.print(CYAN); String playlistName = scanner.nextLine(); System.out.print(RESET);
                    Playlist newPlaylist = new Playlist(playlistName);
                    boolean playlistAdded = libraryModel.addPlaylist(newPlaylist);
                    if (playlistAdded) {
                        System.out.println(GREEN + "Playlist created successfully." + RESET);
                    } else {
                        System.out.println(YELLOW + "Playlist with that name already exists." + RESET);
                    }
                    break;
                case "2":
                    System.out.print("Enter playlist name: ");
                    System.out.print(CYAN); String addPlaylistName = scanner.nextLine(); System.out.print(RESET);
                    Playlist addPlaylist = libraryModel.searchPlaylistByName(addPlaylistName);
                    if (addPlaylist != null) {
                        System.out.print("Enter song title: ");
                        System.out.print(CYAN); String addSongTitle = scanner.nextLine(); System.out.print(RESET);
                        System.out.print("Enter artist: ");
                        System.out.print(CYAN); String addSongArtist = scanner.nextLine(); System.out.print(RESET);
                        List<Song> songsToAdd = libraryModel.searchSongByTitle(addSongTitle);
                        
                        Song songToAdd = null;
                        
                        for (Song s : songsToAdd) {
                            if (s.getArtist().equalsIgnoreCase(addSongArtist)) {
                                songToAdd = s;
                                break; 
                            }
                        }
                        
                        if (songToAdd != null) {
                            addPlaylist.addSong(songToAdd);
                            System.out.println(GREEN + "Song added to playlist successfully." + RESET);
                        } else {
                            System.out.println(YELLOW + "Song not found in library." + RESET);
                        }
                    } else {
                        System.out.println(YELLOW + "Playlist not found." + RESET);
                    }
                    break;
                case "3":
                    System.out.print("Enter playlist name: ");
                    System.out.print(CYAN); String removePlaylistName = scanner.nextLine(); System.out.print(RESET);
                    Playlist removePlaylist = libraryModel.searchPlaylistByName(removePlaylistName);
                    if (removePlaylist != null) {
                        System.out.print("Enter song title: ");
                        System.out.print(CYAN); String removeSongTitle = scanner.nextLine(); System.out.print(RESET);
                        System.out.print("Enter artist: ");
                        System.out.print(CYAN); String removeSongArtist = scanner.nextLine(); System.out.print(RESET);
                        List<Song> songsToRemove = libraryModel.searchSongByTitle(removeSongTitle);
                        
                        Song songToRemove = null;
                        for (Song s : songsToRemove) {
                            if (s.getArtist().equalsIgnoreCase(removeSongArtist)) {
                                songToRemove = s;
                                break; 
                            }
                        }
                        
                        if (songToRemove != null) {
                            removePlaylist.removeSong(songToRemove);
                            System.out.println(GREEN + "Song removed from playlist successfully." + RESET);
                        } else {
                            System.out.println(YELLOW + "Song not found in playlist." + RESET);
                        }
                    } else {
                        System.out.println(YELLOW + "Playlist not found." + RESET);
                    }
                    break;
                case "4":
                	System.out.println();
                    managing = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    /*
     * Option 6 from Main Menu
     */
    private void manageSongs() {
        boolean managing = true;
        while (managing) {
            System.out.println(BLUE + "=== Manage Songs ===" + RESET);
            System.out.println("1. Mark Song as Favorite");
            System.out.println("2. Unmark Song as Favorite");
            System.out.println("3. Rate Song");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice (1-4): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            switch (choice) {
                case "1":
                    markSongAsFavorite(true);
                    break;
                case "2":
                    markSongAsFavorite(false);
                    break;
                case "3":
                    rateSong();
                    break;
                case "4":
                	System.out.println();
                    managing = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    /*
     * Step 1) Option 6 from Main Menu (Manage Songs)
     * Step 2) Option 1 or 2
     */
    private void markSongAsFavorite(boolean favorite) {
        System.out.print("Enter song title: ");
        System.out.print(CYAN); String title = scanner.nextLine(); System.out.print(RESET);
        System.out.print("Enter artist: ");
        System.out.print(CYAN); String artist = scanner.nextLine(); System.out.print(RESET);
        List<Song> songs = libraryModel.searchSongByTitle(title);
        
        Song song = null;
        for (Song s : songs) {
            if (s.getArtist().equalsIgnoreCase(artist)) {
                song = s;
                break; 
            }
        }
        
        if (song != null) {
            song.setFavorite(favorite);
            System.out.println(GREEN + "Song " + (favorite ? "marked" : "unmarked") + " as favorite successfully." + RESET);
        } else {
            System.out.println(YELLOW + "Song not found in library." + RESET);
        }
    }

    /*
     * Step 1) Option 6 from Main Menu (Manage Songs)
     * Step 2) Option 3
     */
    private void rateSong() {
        System.out.print("Enter song title: ");
        System.out.print(CYAN); String title = scanner.nextLine(); System.out.print(RESET);
        System.out.print("Enter artist: ");
        System.out.print(CYAN); String artist = scanner.nextLine(); System.out.print(RESET);
        List<Song> songs = libraryModel.searchSongByTitle(title);
        Song song = null;
      
        for (Song s : songs) {
            if (s.getArtist().equalsIgnoreCase(artist)) {
                song = s;
                break; 
            }
        }
        
        if (song != null) {
            System.out.print("Enter rating (1-5): ");
            try {
            	System.out.print(CYAN);
                int ratingValue = Integer.parseInt(scanner.nextLine());
                System.out.print(RESET);
                if (ratingValue >= 1 && ratingValue <= 5) {
                    Rating rating = Rating.values()[ratingValue - 1];
                    song.setRating(rating);
                    if (ratingValue == 5) {
                        song.setFavorite(true);
                    }
                    System.out.println(GREEN + "Song rated successfully." + RESET);
                } else {
                    System.out.println(RED + "Invalid rating. Please enter a number between 1 and 5." + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid input. Please enter a number." + RESET);
            }
        } else {
            System.out.println(YELLOW + "Song not found in library." + RESET);
        }
    }

    /*
     * Step 1) Option 1 or 2 from Main Menu (Search Music Store or Library)
     * Step 2) Option 1 or 2 (Search for Song(s) by song title or artist)
     */
    private void displaySongList(List<Song> songs, String header) {
        if (songs.isEmpty()) {
            System.out.println(YELLOW + "No songs found." + RESET);
        } else {
            System.out.println(MAGENTA + "=== " + header + " ===" + RESET);
            for (Song song : songs) {
                System.out.println(song.toString());
            }
        }
    }

    /*
     * Step 1) Option 1 or 2 from Main Menu (Search Music Store or Library)
     * Step 2) Option 3 or 4 (Search for Album by album title or artist)
     */
    private void displayAlbumList(List<Album> albums, String header) {
        if (albums.isEmpty()) {
            System.out.println(YELLOW + "No albums found." + RESET);
        } else {
            System.out.println(MAGENTA + "=== " + header + " ===" + RESET);
            for (Album album : albums) {
                System.out.println(album.toString());
            }
        }
    }

    /*
     * Step 1) Option 4 from Main Menu (View Library Lists)
     * Step 2) Option 1 - 5 ((1) song titles, (2) artists, (3) albums, (4) playlists, (5) favorite songs)
     */
    private void displayList(List<String> items, String header) {
        if (items.isEmpty()) {
            System.out.println(YELLOW + "No items found." + RESET);
        } else {
            System.out.println(MAGENTA + "=== " + header + " ===" + RESET);
            for (String item : items) {
                System.out.println(item);
            }
        }
    }
}
