package ml.w568w.musicplayer;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import ml.w568w.musicplayer.util.FileScanner;
import ml.w568w.musicplayer.util.FileScanner.PermissionCallBack;
import ml.w568w.musicplayer.util.FileScanner.ScanCallBack;
import ml.w568w.musicplayer.util.Toast;
import ml.w568w.musicplayer.util.Vectors;
import ml.w568w.musicplayer.util.Vectors.Process;

public class MainActivity extends MIDlet {
	public static final Command OK = new Command("OK", Command.OK, 1);
	public static final Command BACK = new Command("����", Command.BACK, 1);
	List mainMenu = new List("Music Player", List.IMPLICIT);
	List songlist = new List("�����б�", List.IMPLICIT);
	Form scanProgress = new Form("");
	Vector playList = new Vector();
	private Display display = Display.getDisplay(this);
	private static final String[] menu = { "ȫ������", "����", "ɨ��", "�˳�" };

	public MainActivity() {
		int len = menu.length, i = 0;
		// �����ļ�Ȩ��
		FileScanner.requestPermission(new PermissionCallBack() {

			public void onSuccess() {
				System.out.println("File permission accessed");
				Toast.showText(mainMenu, "�ļ�Ȩ�޻�óɹ�!");
			}

			public void onFailed() {
			}
		});
		// ��ʼ���˵�
		for (; i < len; i++) {
			mainMenu.append(menu[i], null);
		}
		mainMenu.setCommandListener(new CommandListener() {

			public void commandAction(Command command, Displayable arg1) {
				if (command.equals(List.SELECT_COMMAND)) {
					switch (mainMenu.getSelectedIndex()) {
					case 0:
						reloadMusicList();
						display.setCurrent(songlist);
						break;
					case 1:

						break;
					case 2:
						scanProgress.deleteAll();
						scanProgress.removeCommand(OK);
						scanProgress.append("ɨ��������...");
						display.setCurrent(scanProgress);
						FileScanner.scanFile(".mp3", new ScanCallBack() {

							public void onScanFinished(Vector result) {
								// ɨ�����
								scanProgress.addCommand(OK);
								scanProgress.setTitle("����");
								scanProgress.deleteAll();
								scanProgress.append("�ҵ�������:" + result.size());
								scanProgress.setCommandListener(new CommandListener() {

											public void commandAction(
													Command arg0,
													Displayable arg1) {
												if (arg0.equals(OK)) {
													display.setCurrent(mainMenu);
												}
											}
										});
								MusicUtil.�����б�(result);
							}

							public void onScanPath(String path) {
								scanProgress.setTitle(path);
								super.onScanPath(path);
							}

						});
						break;
					case 3:
						try {
							destroyApp(true);
							notifyDestroyed();
						} catch (MIDletStateChangeException e) {
						}
					}
				}

			}
		});
		songlist.addCommand(BACK);
		songlist.setCommandListener(new CommandListener() {

			public void commandAction(Command arg0, Displayable arg1) {
				if (arg0.equals(BACK))
					display.setCurrent(mainMenu);
				else if (arg0.equals(List.SELECT_COMMAND)) {
					Song song = (Song) playList.elementAt(songlist
							.getSelectedIndex());
					try {
						System.out.println(song.path);

						Player p = Manager.createPlayer(FileScanner
								.openConnection(song.path).openInputStream(),
								"audio/mp3");
						p.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println(e.getMessage());
					} catch (MediaException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		display.setCurrent(mainMenu);
		String[] types = Manager.getSupportedContentTypes("file");
		for (int i = 0; i < types.length; i++) {
			System.out.println(types[i]);
		}
	}

	public void reloadMusicList() {
		songlist.deleteAll();
		playList = MusicUtil.��ȡ�б�();
		Vectors.foreach(playList, new Process() {

			public void processOn(Object obj) {
				songlist.append(((Song) obj).name, null);
			}
		});
	}

}
