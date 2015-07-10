package IoStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Scanner;

public class SaveDataFromInet {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		System.out.println("Input http:");
		String str = new Scanner(System.in).nextLine();
		System.out.println("Input name:");
		String strg = new Scanner(System.in).nextLine();
		long timeStart = System.currentTimeMillis();
		InputStream src = new BufferedInputStream(new URL(str).openStream(), 1024);
		OutputStream dst = new BufferedOutputStream(new FileOutputStream("d://" + strg), 1024);
		copy(src, dst);
		System.out.println(System.currentTimeMillis() - timeStart);
	}

	private static void copy(InputStream src, OutputStream dst) throws IOException {
		while (true) {
			byte[] buff = new byte[300 * 1024 * 1024];
			int count = src.read(buff);
			if (count == -1) {
				return;
			} else {
				dst.write(buff, 0, count);
			}
		}
	}
}
