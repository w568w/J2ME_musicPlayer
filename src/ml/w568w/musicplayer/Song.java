package ml.w568w.musicplayer;

public class Song {
	public String name;
	public String path;
	public Song(String rawPath){
		name=rawPath.substring(rawPath.lastIndexOf('/')+1);
		path=rawPath;
	}
}
