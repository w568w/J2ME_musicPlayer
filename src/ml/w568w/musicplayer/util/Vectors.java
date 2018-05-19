package ml.w568w.musicplayer.util;

import java.util.Enumeration;
import java.util.Vector;

public class Vectors {
	public interface Process {
		void processOn(Object obj);
	}

	public static void foreach(Vector vec, Process process) {
		if (vec != null) {
			Enumeration enumeration = vec.elements();
			while (enumeration.hasMoreElements()) {
				process.processOn(enumeration.nextElement());
			}
		}
	}
}
