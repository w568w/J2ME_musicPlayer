package ml.w568w.musicplayer.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

public final class FileScanner {
	public static void requestPermission(final PermissionCallBack callback) {
		new Thread(new Runnable() {
			public void run() {

				Enumeration iter = FileSystemRegistry.listRoots();
				boolean hasRoot = false;
				String rootPath = "";
				while (iter.hasMoreElements()) {
					rootPath = (String) iter.nextElement();
					System.out.println(rootPath);
					hasRoot = true;
				}
				if (!hasRoot) {
					callback.onFailed();
					return;
				}
				try {
					FileConnection conn = openConnection(rootPath);
					if (!conn.canRead()) {
						throw new IOException("Unreadable");
					}
					callback.onSuccess();
				} catch (IOException e) {
					e.printStackTrace();
					callback.onFailed();
					return;
				}

			}
		}).start();
	}

	public static void scanFile(final String fileName,
			final ScanCallBack callback) {
		new Thread(new Runnable() {
			public void run() {
				Vector v = new Vector();
				Enumeration iter = FileSystemRegistry.listRoots();
				while (iter.hasMoreElements()) {
					String rootPath = (String) iter.nextElement();
					try {
						Vector vec = scanRev(openConnection(rootPath),
								fileName, new ScanThreadCallBack() {

									public void onScanPath(String path) {
										callback.onScanPath(path);
									}
								});
						addAll(v, vec);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				callback.onScanFinished(v);
			}
		}).start();
	}

	private static void addAll(Vector vec, Vector addition) {
		Enumeration enumeration = addition.elements();
		while (enumeration.hasMoreElements()) {
			vec.addElement(enumeration.nextElement());
		}
	}

	private static Vector scanRev(FileConnection path, String fileName,
			ScanThreadCallBack callback) throws IOException {
		Enumeration files = path.list();
		Vector f = new Vector();
		while (files.hasMoreElements()) {
			String str = (String) files.nextElement();

			callback.onScanPath(path.getPath() + path.getName() + "/" + str);
			FileConnection conn = openConnection(path.getPath()
					+ path.getName() + "/" + str);
			if (conn.isDirectory()) {
				Vector subFiles = scanRev(conn, fileName, callback);
				addAll(f, subFiles);
			} else if (conn.getName().indexOf(fileName) != -1) {
				f.addElement(conn.getPath() + str);
			}
			conn.close();
		}
		path.close();
		return f;
	}

	public interface PermissionCallBack {
		void onSuccess();

		void onFailed();
	}

	public static abstract class ScanCallBack {
		public void onScanPath(String path) {
		}

		public abstract void onScanFinished(Vector result);
	}

	private interface ScanThreadCallBack {
		void onScanPath(String path);
	}

	public static FileConnection openConnection(String path) throws IOException {
		
		if (path.startsWith("/"))
			return (FileConnection) Connector.open("file://" + path,
					Connector.READ_WRITE);
		else
			return (FileConnection) Connector.open("file:///" + path,
					Connector.READ_WRITE);
	}
}
