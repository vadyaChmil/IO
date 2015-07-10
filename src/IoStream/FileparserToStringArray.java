package IoStream;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileparserToStringArray {

	public static void main(String[] args) throws IOException {
		List<String> tokens = new ArrayList<String>();
		String strg = "test.txt";
		File file = new File("d://" + strg);
		if (!file.exists()) {
			file.createNewFile();
		}

		try (FileReader input = new FileReader(file)) {
			StringBuilder builder = new StringBuilder();
			int data = input.read();
			while (data != -1) {
				if (((char) data) != ';' && ((char) data) != 0x0d) {
					builder.append((char) data);
				} else {
					if (!builder.toString().trim().isEmpty()) {
						tokens.add(builder.toString().trim());
					}
					builder.delete(0, builder.length());
				}
				data = input.read();
			}
			if (!builder.toString().trim().isEmpty()) {
				tokens.add(builder.toString().trim());
				builder.delete(0, builder.length());
			}
			System.out.println(tokens);
		}
	}
}
