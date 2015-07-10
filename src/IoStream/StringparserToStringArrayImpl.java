package IoStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StringparserToStringArrayImpl {

	public static void main(String[] args) throws IOException {
		 StringparserToStringArray unit = new StringparserToStringArray();
		 byte[] data = "123,456,789,123,456,789".getBytes();
		 InputStream input = new ByteArrayInputStream(data);
		 unit.read(input);
		 System.out.println(unit.tokens);
	}

}
