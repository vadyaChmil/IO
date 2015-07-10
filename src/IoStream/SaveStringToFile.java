package IoStream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveStringToFile {

	public static void main(String[] args) throws IOException {
		String testString = "10001;Joshua Bloch;Effective Java;2010;385";
		File testFile = new File("testFile.txt");
		try (BufferedWriter output = new BufferedWriter(new FileWriter(testFile))) {
			output.write(testString);
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}
}
