package view;

import model.*;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.List;

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
    private static final String BOLD = "\u001B[1m";    // bold text for main menu options

    public UserInterface(MusicStore musicStore, LibraryModel libraryModel) {
        this.musicStore = musicStore;
        this.libraryModel = libraryModel;
        this.scanner = new Scanner(System.in);
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException {
        MusicStore musicStore = new MusicStore();       

        // Populate MusicStore's hashMap with all albums' data
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
	                            System.out.println(GREEN + "Welcome to the Austin and Lisette's Spotify Program!" + RESET);
	                            System.out.println("\n\n");
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
	                            System.out.println(GREEN + "Welcome to the Austin and Lisette's Spotify Program!" + RESET);
	                            System.out.println("\n\n");
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
    
	    while (loggedIn && programRunning) {
	        displayMainMenu();
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
	        switch (choice) {
            case "0":
                System.out.println("Saving and logging out...");
                System.out.println('\n');
                if (currentUser != null) {
                    currentUser.save();
                }
                currentUser = null;
                libraryModel = null;
                loggedIn = false;
                System.out.println();
                break;
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
	                removeFromLibrary();
                    break;
                case "5":
	            	System.out.println();
	                viewLibraryLists();
	                break;
	            case "6":
	            	System.out.println();
	                managePlaylists();
	                break;
	            case "7":
	            	System.out.println();
	                manageSongs();
	                break;
	            case "8":
	            	System.out.println();
	                playSong();
	                break;
	            case "9":
	            	System.out.println();
	            	viewSortedSongs();
	            	break;
                case "10":
                    System.out.println();
                    shuffleSongs();
                    break;
	            default:
	                System.out.println(RED + "Invalid option. Please try again." + RESET);
	                break;
	        	}
	    	}
	    }
    }

    private void displayMainMenu() {
        System.out.println(GREEN + "=== Main Menu ===" + RESET);
        System.out.println(BOLD + "0. Save and Logout" + RESET);
        System.out.println(BOLD + "1. Search Music Store" + RESET);
        System.out.println(BOLD + "2. Search Library" + RESET);
        System.out.println(BOLD + "3. Add to Library" + RESET);
        System.out.println(BOLD + "4. Remove from Library" + RESET);       
        System.out.println(BOLD + "5. View Library Lists" + RESET);
        System.out.println(BOLD + "6. Manage Playlists" + RESET);
        System.out.println(BOLD + "7. Manage Songs" + RESET);
        System.out.println(BOLD + "8. Play Song" + RESET);
        System.out.println(BOLD + "9. View Sorted Songs" + RESET);
        System.out.println(BOLD + "10. Shuffle Songs" + RESET);           
        System.out.print("Enter your choice (0-10): ");
    }

    // Main Menu -> 1. Search Music Store
    private void searchMusicStore() {
        boolean searching = true;
        while (searching) {
            System.out.println(BLUE + "=== Search Music Store ===" + RESET);
            System.out.println("a. Song by Title");
            System.out.println("b. Song by Artist");
            System.out.println("c. Album by Title");
            System.out.println("d. Album by Artist");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice (a-d, 0): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            switch (choice) {
            case "a":
                System.out.print("Enter song title: ");
                System.out.print(CYAN); String title = scanner.nextLine(); System.out.print(RESET);
                List<Song> songsByTitle = musicStore.searchSongByTitle(title);
                displaySongList(songsByTitle, "Songs Found");
                if (!songsByTitle.isEmpty()) {
                    System.out.print("Do you want to see album information for a song? (y/n): ");
                    System.out.print(CYAN); String albumChoice = scanner.nextLine(); System.out.print(RESET);
                    if (albumChoice.equalsIgnoreCase("y")) {
                        System.out.print("Enter the song number: ");
                        System.out.print(CYAN); int songIndex = Integer.parseInt(scanner.nextLine()) - 1; System.out.print(RESET);
                        if (songIndex >= 0 && songIndex < songsByTitle.size()) {
                            Song selectedSong = songsByTitle.get(songIndex);
                            displayAlbumInformation(selectedSong);
                        } else {
                            System.out.println(RED + "Invalid song number." + RESET);
                        }
                    }
                }
                break;
                case "b":
                    System.out.print("Enter artist: ");
    	            System.out.print(CYAN); String artist = scanner.nextLine(); System.out.print(RESET);
                    List<Song> songsByArtist = musicStore.searchSongByArtist(artist);
                    displaySongList(songsByArtist, "Songs Found");
                    break;
                case "c":
                    System.out.print("Enter album title: ");
    	            System.out.print(CYAN); String albumTitle = scanner.nextLine(); System.out.print(RESET);
                    List<Album> albumsByTitle = musicStore.searchAlbumByTitle(albumTitle);
                    displayAlbumList(albumsByTitle, "Albums Found");
                    break;
                case "d":
                    System.out.print("Enter artist: ");
    	            System.out.print(CYAN); String albumArtist = scanner.nextLine(); System.out.print(RESET);
                    List<Album> albumsByArtist = musicStore.searchAlbumByArtist(albumArtist);
                    displayAlbumList(albumsByArtist, "Albums Found");
                    break;
                case "0":
                	System.out.println();
                    searching = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    // Main Menu -> 2. Search Library
    private void searchLibrary() {
        boolean searching = true;
        while (searching) {
            System.out.println(BLUE + "=== Search Library ===" + RESET);
            System.out.println("a. Song by Title");
            System.out.println("b. Song by Artist");
            System.out.println("c. Album by Title");
            System.out.println("d. Album by Artist");
            System.out.println("e. Playlist by Name");
            System.out.println("f. Songs by Genre");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice (a-f, 0): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            switch (choice) {
                case "a":
                    System.out.print("Enter song title: ");
                    System.out.print(CYAN); String title = scanner.nextLine(); System.out.print(RESET);
                    List<Song> songsByTitle = libraryModel.searchSongByTitle(title);
                    displaySongList(songsByTitle, "Songs Found in Library");
                    break;
                case "b":
                    System.out.print("Enter artist: ");
                    System.out.print(CYAN); String artist = scanner.nextLine(); System.out.print(RESET);
                    List<Song> songsByArtist = libraryModel.searchSongByArtist(artist);
                    displaySongList(songsByArtist, "Songs Found in Library");
                    break;
                case "c":
                    System.out.print("Enter album title: ");
                    System.out.print(CYAN); String albumTitle = scanner.nextLine(); System.out.print(RESET);
                    List<Album> albumsByTitle = libraryModel.searchAlbumByTitle(albumTitle);
                    displayAlbumList(albumsByTitle, "Albums Found in Library");
                    break;
                case "d":
                    System.out.print("Enter artist: ");
                    System.out.print(CYAN); String albumArtist = scanner.nextLine(); System.out.print(RESET);
                    List<Album> albumsByArtist = libraryModel.searchAlbumByArtist(albumArtist);
                    displayAlbumList(albumsByArtist, "Albums Found in Library");
                    break;
                case "e":
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
                case "f":
                    System.out.print("Enter genre: ");
                    System.out.print(CYAN); String genre = scanner.nextLine(); System.out.print(RESET);
                    List<Song> songsByGenre = libraryModel.searchSongsByGenre(genre);
                    displaySongList(songsByGenre, "Songs Found in Genre: " + genre);
                    break;
                case "0":
                	System.out.println();
                    searching = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    // Main Menu -> 3. Add to Library
    private void addToLibrary() {
        boolean adding = true;
        while (adding) {
            System.out.println(BLUE + "=== Add to Library ===" + RESET);
            System.out.println("a. Add Song");
            System.out.println("b. Add Album");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice (a-b, 0): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            switch (choice) {
                case "a":
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
                case "b":
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
                case "0":
                	System.out.println();
                    adding = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    // Main Menu -> 4. Remove from Library
    private void removeFromLibrary() {
        boolean removing = true;
        while (removing) {
            System.out.println(BLUE + "=== Remove from Library ===" + RESET);
            System.out.println("a. Remove Song");
            System.out.println("b. Remove Album");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice (a-b, 0): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            switch (choice) {
                case "a":
                    System.out.print("Enter song title: ");
                    System.out.print(CYAN); String songTitle = scanner.nextLine(); System.out.print(RESET);
                    System.out.print("Enter artist: ");
                    System.out.print(CYAN); String songArtist = scanner.nextLine(); System.out.print(RESET);
                    Song songToRemove = musicStore.getSong(songTitle, songArtist);
                    
                    if (songToRemove != null && libraryModel.getSongs().contains(songToRemove)) {
                        libraryModel.removeSong(songToRemove);
                        System.out.println(GREEN + "Song removed from library successfully." + RESET);
                    } else {
                        System.out.println(YELLOW + "Song not found in the library." + RESET);
                    }
                    break;
                    
                case "b":
                    System.out.print("Enter album title: ");
                    System.out.print(CYAN); String albumTitle = scanner.nextLine(); System.out.print(RESET);
                    System.out.print("Enter artist: ");
                    System.out.print(CYAN); String albumArtist = scanner.nextLine(); System.out.print(RESET);
                    Album albumToRemove = musicStore.getAlbum(albumTitle, albumArtist);
                    if (albumToRemove != null && libraryModel.getAlbums().contains(albumToRemove)) {
                        libraryModel.removeAlbum(albumToRemove);
                        System.out.println(GREEN + "Album removed from library successfully." + RESET);
                    } else {
                        System.out.println(YELLOW + "Album not found in the library." + RESET);
                    }
                    break;
                    
                case "0":
                    System.out.println();
                    removing = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    // Main Menu -> 5. View Library Lists
    private void viewLibraryLists() {
        boolean viewing = true;
        while (viewing) {
            System.out.println(BLUE + "=== View Library Lists ===" + RESET);
            System.out.println("a. Song Titles");
            System.out.println("b. Artists");
            System.out.println("c. Albums");
            System.out.println("d. Playlists");
            System.out.println("e. Favorite Songs");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice (a-e, 0): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            switch (choice) {
                case "a":
                    List<String> songTitles = libraryModel.getSongTitles();
                    displayList(songTitles, "Song Titles in Library");
                    break;
                case "b":
                    List<String> artists = libraryModel.getArtists();
                    displayList(artists, "Artists in Library");
                    break;
                case "c":
                    List<String> albumTitles = libraryModel.getAlbumTitles();
                    displayList(albumTitles, "Albums in Library");
                    break;
                case "d":
                    viewPlaylists();
                    break;
                case "e":
                    List<String> favoriteSongs = libraryModel.getFavoriteSongs();
                    displayList(favoriteSongs, "Favorite Songs in Library");
                    break;
                case "0":
                	System.out.println();
                    viewing = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    // Main Menu -> 6. Manage Playlists
    private void managePlaylists() {
        boolean managing = true;
        while (managing) {
            System.out.println(BLUE + "=== Manage Playlists ===" + RESET);
            System.out.println("a. Create New Playlist");
            System.out.println("b. Add Song to Playlist");
            System.out.println("c. Remove Song from Playlist");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice (a-c, 0): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            
            switch (choice) {
                case "a":
                    System.out.print("Enter playlist name: ");
                    System.out.print(CYAN); String playlistName = scanner.nextLine(); System.out.print(RESET);
                    if (playlistName.equalsIgnoreCase("Recent Songs") || playlistName.equalsIgnoreCase("Frequent Songs")) {
                        System.out.println(YELLOW + "Cannot create a playlist with this name. It is reserved for automatic playlists." + RESET);
                        break;
                    }
                    Playlist newPlaylist = new Playlist(playlistName);
                    boolean playlistAdded = libraryModel.addPlaylist(newPlaylist);
                    if (playlistAdded) {
                        System.out.println(GREEN + "Playlist created successfully." + RESET);
                    } else {
                        System.out.println(YELLOW + "Playlist with that name already exists." + RESET);
                    }
                    break;
                    
                case "b":
                    System.out.print("Enter playlist name: ");
                    System.out.print(CYAN); String addPlaylistName = scanner.nextLine(); System.out.print(RESET);
                    if (addPlaylistName.equalsIgnoreCase("Recent Songs") || addPlaylistName.equalsIgnoreCase("Frequent Songs")) {
                        System.out.println(YELLOW + "Cannot modify automatic playlists (Recent Songs or Frequent Songs)." + RESET);
                        break;
                    }
                    
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
                    
                case "c":
                    System.out.print("Enter playlist name: ");
                    System.out.print(CYAN); String removePlaylistName = scanner.nextLine(); System.out.print(RESET);
                    if (removePlaylistName.equalsIgnoreCase("Recent Songs") || removePlaylistName.equalsIgnoreCase("Frequent Songs")) {
                        System.out.println(YELLOW + "Cannot modify automatic playlists (Recent Songs or Frequent Songs)." + RESET);
                        break;
                    }
                    
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
                case "0":
                    System.out.println();
                    managing = false;
                    break;
                    
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    // Main Menu -> 7. Manage Songs
    private void manageSongs() {
        boolean managing = true;
        while (managing) {
            System.out.println(BLUE + "=== Manage Songs ===" + RESET);
            System.out.println("a. Mark Song as Favorite");
            System.out.println("b. Unmark Song as Favorite");
            System.out.println("c. Rate Song");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice (a-c, 0): ");
            System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);
            switch (choice) {
                case "a":
                    markSongAsFavorite(true);
                    break;
                case "b":
                    markSongAsFavorite(false);
                    break;
                case "c":
                    rateSong();
                    break;
                case "0":
                	System.out.println();
                    managing = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    // Main Menu -> 8. Play Song
    private void playSong() {
        System.out.println(BLUE + "=== Play Song ===" + RESET);
        System.out.print("Enter song title: ");
        System.out.print(CYAN); String songTitle = scanner.nextLine(); System.out.print(RESET);
        System.out.print("Enter artist: ");
        System.out.print(CYAN); String artist = scanner.nextLine(); System.out.print(RESET);

        List<Song> songs = libraryModel.searchSongByTitle(songTitle);
        Song songToPlay = null;
        for (Song song : songs) {
            if (song.getArtist().equalsIgnoreCase(artist)) {
                songToPlay = song;
                break;
            }
        }

        if (songToPlay != null) {
            libraryModel.getSongTracker().playSong(songToPlay);
            System.out.println(GREEN + "Playing: " + songToPlay.getSongTitle() + " by " + songToPlay.getArtist() + RESET);
        } else {
            System.out.println(YELLOW + "Song not found in library." + RESET);
        }
    }

    // Main Menu -> 9. View Sorted Songs
    private void viewSortedSongs() {
        boolean sorting = true;
        while (sorting) {
            System.out.println(BLUE + "=== View Sorted Songs ===" + RESET);
            System.out.println("a. Sort by Title");
            System.out.println("b. Sort by Artist");
            System.out.println("c. Sort by Rating");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice (a-c, 0): ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "a":
                    List<Song> songsByTitle = libraryModel.getSongsSortedByTitle();
                    displaySortedSongList(songsByTitle, "Songs Sorted by Title");
                    break;
                case "b":
                    List<Song> songsByArtist = libraryModel.getSongsSortedByArtist();
                    displaySortedSongList(songsByArtist, "Songs Sorted by Artist");
                    break;
                case "c":
                    List<Song> songsByRating = libraryModel.getSongsSortedByRating();
                    displaySortedSongList(songsByRating, "Songs Sorted by Rating");
                    break;
                case "0":
                    System.out.println();
                    sorting = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
                    break;
            }
        }
    }

    // Main Menu -> 10. Shuffle Songs
    private void shuffleSongs() {
        System.out.println(BLUE + "=== Shuffle Songs ===" + RESET);
        System.out.println("a. Shuffle All Songs in Library");
        System.out.println("b. Shuffle Songs in a Playlist");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice (a-b, 0): ");
        System.out.print(CYAN); String choice = scanner.nextLine(); System.out.print(RESET);

        switch (choice) {
            case "a":
                List<Song> librarySongs = libraryModel.getSongs();
                if (librarySongs.isEmpty()) {
                    System.out.println(YELLOW + "No songs in the library to shuffle." + RESET);
                } else {
                    System.out.println(MAGENTA + "=== Original Library Song List ===" + RESET);
                    for (int i = 0; i < librarySongs.size(); i++) {
                        System.out.println((i + 1) + ". " + librarySongs.get(i).getSongTitle() + " by " + librarySongs.get(i).getArtist());
                    }
                    List<Song> shuffledLibrarySongs = libraryModel.getShuffledSongs();
                    System.out.println(MAGENTA + "=== Shuffled Library Song List ===" + RESET);
                    for (int i = 0; i < shuffledLibrarySongs.size(); i++) {
                        System.out.println((i + 1) + ". " + shuffledLibrarySongs.get(i).getSongTitle() + " by " + shuffledLibrarySongs.get(i).getArtist());
                    }
                }
                break;

            case "b":
                System.out.print("Enter playlist name: ");
                System.out.print(CYAN); String playlistName = scanner.nextLine(); System.out.print(RESET);
                Playlist playlist = libraryModel.searchPlaylistByName(playlistName);
                if (playlist == null) {
                    System.out.println(YELLOW + "Playlist not found." + RESET);
                } else {
                    List<Song> playlistSongs = playlist.getSongs();
                    if (playlistSongs.isEmpty()) {
                        System.out.println(YELLOW + "No songs in the playlist to shuffle." + RESET);
                    } else {
                        System.out.println(MAGENTA + "=== Original Playlist: " + playlist.getName() + " ===" + RESET);
                        for (int i = 0; i < playlistSongs.size(); i++) {
                            System.out.println((i + 1) + ". " + playlistSongs.get(i).getSongTitle() + " by " + playlistSongs.get(i).getArtist());
                        }
                        List<Song> shuffledPlaylistSongs = playlist.getShuffledSongs();
                        System.out.println(MAGENTA + "=== Shuffled Playlist: " + playlist.getName() + " ===" + RESET);
                        for (int i = 0; i < shuffledPlaylistSongs.size(); i++) {
                            System.out.println((i + 1) + ". " + shuffledPlaylistSongs.get(i).getSongTitle() + " by " + shuffledPlaylistSongs.get(i).getArtist());
                        }
                    }
                }
                break;

            case "0":
                System.out.println();
                break;

            default:
                System.out.println(RED + "Invalid option. Please try again." + RESET);
                break;
        }
    }

    // Main Menu -> 5. View Library Lists -> d. Playlists
    private void viewPlaylists() {
        List<Playlist> allPlaylists = libraryModel.getAllPlaylistsForDisplay();
        if (allPlaylists.isEmpty()) {
            System.out.println(YELLOW + "No playlists found." + RESET);
            
        } else {
            System.out.println(MAGENTA + "=== Playlists in Library ===" + RESET);
            for (int i = 0; i < allPlaylists.size(); i++) {
                System.out.println((i + 1) + ". " + allPlaylists.get(i).getName());
            }
            
            System.out.print("Select a playlist to view (1-" + allPlaylists.size() + ") or 0 to go back: ");
            System.out.print(CYAN); String playlistChoice = scanner.nextLine(); System.out.print(RESET);
            
            try {
                int index = Integer.parseInt(playlistChoice);
                if (index >= 1 && index <= allPlaylists.size()) {
                    Playlist selectedPlaylist = allPlaylists.get(index - 1);
                    
                    System.out.println(MAGENTA + "=== Playlist: " + selectedPlaylist.getName() + " ===" + RESET);
                    List<Song> songs = selectedPlaylist.getSongs();
                    for (int j = 0; j < songs.size(); j++) {
                        System.out.println((j + 1) + ". " + songs.get(j).getSongTitle() + " by " + songs.get(j).getArtist());
                    }
                    
                } else if (index != 0) {
                    System.out.println(RED + "Invalid selection." + RESET);
                }
                
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid input. Please enter a number." + RESET);
            }
        }
    }

    // Main Menu -> 7. Manage Songs -> a. Mark Song as Favorite or b. Unmark Song as Favorite
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

    // Main Menu -> 7. Manage Songs -> c. Rate Song
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

    // Used by: searchMusicStore(), searchLibrary()
    private void displaySongList(List<Song> songs, String header) {
        if (songs.isEmpty()) {
            System.out.println(YELLOW + "No songs found." + RESET);
        } else {
            System.out.println(MAGENTA + "=== " + header + " ===" + RESET);
            int i = 1;
            for (Song song : songs) {
                System.out.println(i + ". " + song.toString());
                i++;
            }
        }
    }

    // Used by: searchMusicStore(), searchLibrary()
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

    // Used by: viewLibraryLists()
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
    
    // Used by: searchMusicStore()
    private void displayAlbumInformation(Song song) {
        Album album = libraryModel.getAlbumFromSong(song);
        if (album != null) {
            System.out.println(MAGENTA + "=== Album Information for '" + song.getSongTitle() + "' ===" + RESET);
            System.out.println(album.toString());
            if (libraryModel.getAlbums().contains(album)) {
                System.out.println(GREEN + "This album is already in your library." + RESET);
            } else {
                System.out.println(YELLOW + "This album is not in your library." + RESET);
            }
        } else {
            System.out.println(YELLOW + "Album information not found for this song." + RESET);
        }
    }

    // Used by: viewSortedSongs()
    private void displaySortedSongList(List<Song> songs, String header) {
        System.out.println(MAGENTA + "=== " + header + " ===" + RESET);
        if (songs.isEmpty()) {
            System.out.println("No songs available.");
        } else {
            for (int i = 0; i < songs.size(); i++) {
                System.out.println((i + 1) + ". " + songs.get(i));
            }
        }
        System.out.println();
    }
}