package model;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AlbumTest.class,
    SongTest.class,
    PlaylistTest.class,
    MusicStoreTest.class,
    LibraryModelTest.class,
    UserTest.class,
    SongTrackerTest.class
})
public class AllTests {
	// Annotations above handle all tests
}