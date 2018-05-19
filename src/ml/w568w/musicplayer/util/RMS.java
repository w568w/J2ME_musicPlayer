package ml.w568w.musicplayer.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class RMS {

	private RecordStore mStore;

	private RMS(RecordStore rs) {
		mStore = rs;
	}

	public RMS putString(String key, String value)
			throws RecordStoreNotOpenException, RecordStoreFullException,
			RecordStoreException, IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeUTF(key);
		dos.writeUTF(value);
		byte[] b = baos.toByteArray();
		dos.close();
		baos.close();
		int id;
		if ((id = findString(key)) == -1) {
			mStore.addRecord(b, 0, b.length);
		} else {
			mStore.setRecord(id, b, 0, b.length);
		}

		return this;
	}

	public String getString(final String key)
			throws RecordStoreNotOpenException, RecordStoreFullException,
			RecordStoreException, IOException {
		int id;
		if ((id = findString(key)) == -1) {
			return null;
		} else {
			ByteArrayInputStream bais = new ByteArrayInputStream(
					mStore.getRecord(id));
			DataInputStream dis = new DataInputStream(bais);
			dis.readUTF();
			return dis.readUTF();
		}
	}

	private int findString(final String key)
			throws RecordStoreNotOpenException, RecordStoreFullException,
			RecordStoreException, IOException {
		RecordEnumeration re = mStore.enumerateRecords(new RecordFilter() {

			public boolean matches(byte[] arg0) {
				ByteArrayInputStream bais = new ByteArrayInputStream(arg0);
				DataInputStream dis = new DataInputStream(bais);
				try {
					String key = dis.readUTF();
					if (key.equals(key)) {
						return true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						bais.close();
						dis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return false;
			}
		}, null, false);
		if (re.numRecords() > 0) {
			return re.nextRecordId();
		} else
			return -1;
	}

	public static RMS open(String name) throws RecordStoreFullException,
			RecordStoreNotFoundException, RecordStoreException {
		return new RMS(RecordStore.openRecordStore(name, true));
	}

}
