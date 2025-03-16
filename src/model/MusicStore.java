/*
 * PART 1 -- MusicStore.java
 * Purpose: Read in all album files and build a HashMap (albumMap) with Album information
 *	Also, there are public methods to:
 * (a) search the MusicStore by song/album for song/artist and
 * (b) getters for Song/Album objects
 * Authors: Austin Raben, Lisette Robles
 * 
 */
package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.String;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MusicStore {
	private static HashMap<String[], ArrayList<String>> albumMap;
	

	public MusicStore() {
		// key: album, artist (as an array)
		// value: artist, genre, year, all song names (as an ArrayList)
		albumMap = new HashMap<>();
	}
	
	public void readMusicFile(String filename) throws IOException {		
	        
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			
			
			String line;
			
			// data structures for each (key,value) in albumMap
			String[] albumAndArtist = null;
			ArrayList<String> albumInfo = null;
		
			// loop through each file
			while ((line = reader.readLine()) != null) {
				
				// ignore albums.txt
				if (line.split(",").length == 2) {
					break;
				}
						
				// first line
				if (line.contains(",")) {
					String[] parts = line.split(",");
					albumAndArtist = new String[2];
					albumAndArtist[0] = parts[0];  // album
					albumAndArtist[1] = parts[1];  // artist
					
					albumInfo = new ArrayList<>();
					albumInfo.add(parts[1]); // artist 
					albumInfo.add(parts[2]); // genre
					albumInfo.add(parts[3]); // year'
					
				} else {
					albumInfo.add(line); // songs
				}
				
				// put each iteration in HashMap
				albumMap.put(albumAndArtist, albumInfo);
		
				}
				
			}
		catch (IOException e) {
			e.printStackTrace();
		} 

	}
	
	/*
	 * Four search methods below to search MusicStore for song/album by song/artist
	 */
	
	public List<Song> searchSongByTitle(String sTitle) {
		List<Song> result = new ArrayList<>();
		for(Map.Entry<String[], ArrayList<String>> entry : albumMap.entrySet()) {
			String[] key = entry.getKey();
			String albumTitle = key[0];
			String artist = key[1];
			List<String> songTitles = entry.getValue().subList(3, entry.getValue().size());
			for(String songTitle: songTitles) {
				if (songTitle.equalsIgnoreCase(sTitle)) {
					result.add(new Song(songTitle, artist, albumTitle, null, false));
				}
			}
			
 		}
		return result;
		
	}
	
	public List<Song> searchSongByArtist(String artist) {
		List<Song> result = new ArrayList<>();
		for(Map.Entry<String[], ArrayList<String>> entry : albumMap.entrySet()) {
			String[] key = entry.getKey();
			if(key[1].equalsIgnoreCase(artist)) {
				String albumTitle = key[0];
				List<String> songTitles = entry.getValue().subList(3, entry.getValue().size());
				for(String songTitle: songTitles) {
					result.add(new Song(songTitle, artist, albumTitle, null, false));
				}
			}
 		}
		return result;
		
	}
	
	public List<Album> searchAlbumByTitle(String aTitle) {
		List<Album> result = new ArrayList<>();
		for(Map.Entry<String[], ArrayList<String>> entry : albumMap.entrySet()) {
			String[] key = entry.getKey();
			if (key[0].equalsIgnoreCase(aTitle)) {
				String artist = key[1];
				ArrayList<String> value = entry.getValue();
				String genre = value.get(1);
				String year = value.get(2);
				List<String> songTitles = value.subList(3, value.size());
				List<Song> songs = songTitles.stream()
						.map(songTitle -> new Song(songTitle, artist, aTitle, null, false))
						.collect(Collectors.toList());
				result.add(new Album(aTitle, artist, genre, year, songs));
				
			}
			
 		}
		return result;
		
	}
	
	public List<Album> searchAlbumByArtist(String artist) {
		List<Album> result = new ArrayList<>();
		for(Map.Entry<String[], ArrayList<String>> entry : albumMap.entrySet()) {
			String[] key = entry.getKey();
			if (key[1].equalsIgnoreCase(artist)) {
				String albumTitle = key[0];
				ArrayList<String> value = entry.getValue();
				String genre = value.get(1);
				String year = value.get(2);
				List<String> songTitles = value.subList(3, value.size());
				List<Song> songs = songTitles.stream()
						.map(songTitle -> new Song(songTitle, artist, albumTitle, null, false))
						.collect(Collectors.toList());
				result.add(new Album(albumTitle, artist, genre, year, songs));
				
			}
			
 		}
		return result;
		
	}
	
	/*
	 * Getters to return Song/Album objects if they exist in MusicStore, otherwise null
	 */
	
	public Song getSong(String songTitle, String artist) {
		for(Map.Entry<String[], ArrayList<String>> entry : albumMap.entrySet()) {
			String[] key = entry.getKey();
			if(key[1].equalsIgnoreCase(artist)) {
				String albumTitle = key[0];
				List<String> songTitles = entry.getValue().subList(3, entry.getValue().size());
				if(songTitles.contains(songTitle)) {
					return new Song(songTitle, artist, albumTitle, null, false);
				}
			}
		}
		return null;
	}
	
	public Album getAlbum(String albumTitle, String artist) {
		for(Map.Entry<String[], ArrayList<String>> entry : albumMap.entrySet()) {
			String[] key = entry.getKey();
			if(key[0].equalsIgnoreCase(albumTitle) && key[1].equalsIgnoreCase(artist)) {
				ArrayList<String> albumInfo = entry.getValue();
				String genre = albumInfo.get(1);
				String year = albumInfo.get(2);
				List<String> songTitles = albumInfo.subList(3, albumInfo.size());
				List<Song> songs = songTitles.stream()
						.map(songTitle -> new Song(songTitle, key[1], key[0], null, false))
						.collect(Collectors.toList());
				return new Album(key[0], key[1], genre, year, songs);
			}
		}
		return null;
	}
}
