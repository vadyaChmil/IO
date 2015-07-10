package IoStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StringparserToStringArray {
	protected List<String> tokens = new ArrayList<String>();

	public void read(InputStream input) throws IOException {
		StringBuilder builder = new StringBuilder();

		int data = input.read();
		while (data != -1) {
			if (((char) data) != ',') {
				builder.append((char) data);
			} else {
				tokens.add(builder.toString().trim());
				builder.delete(0, builder.length());
			}
			data = input.read();
		}
		if (!builder.toString().trim().isEmpty()) {
			tokens.add(builder.toString().trim());
			builder.delete(0, builder.length());
		}
	}
}
