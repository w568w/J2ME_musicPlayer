package ml.w568w.musicplayer;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotOpenException;

import com.sun.j2me.global.SerializableResource;

import ml.w568w.musicplayer.util.RMS;
import ml.w568w.musicplayer.util.StringUtils;
import ml.w568w.musicplayer.util.Vectors;
import ml.w568w.musicplayer.util.Vectors.Process;

public class MusicUtil{
	private static RMS mRms;
	static {
		try {
			mRms = RMS.open("playlist");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Vector 读取列表() {
		try {
			String listRaw = mRms.getString("list");
			if (listRaw == null)
				return null;
			else {
				String[] songs = StringUtils.split(listRaw, ";");
				int len = songs.length;
				Vector vec = new Vector();
				for (int i = 0; i < len; i++) {
					if (songs[i].trim().equals(""))
						continue;
					vec.addElement(new Song(songs[i]));
				}
				return vec;
			}
		} catch (RecordStoreNotOpenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void 保存列表(Vector vec){
		final StringBuffer buffer = new StringBuffer();
		Vectors.foreach(vec, new Process() {
			
			public void processOn(Object obj) {
				buffer.append((String)obj);
				buffer.append(";");	
			}
		});
		try {
			mRms.putString("list",
					buffer.toString());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
