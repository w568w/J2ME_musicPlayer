package ml.w568w.musicplayer.util;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Screen;
import javax.microedition.lcdui.Ticker;

public class Toast {
	public static void showText(final Screen screen,String toast){
		Ticker ticker=new Ticker(toast);
		screen.setTicker(ticker);
		new Timer().schedule(new TimerTask() {
			
			public void run() {
				screen.setTicker(null);
				
			}
		}, 3000);
	}
}
