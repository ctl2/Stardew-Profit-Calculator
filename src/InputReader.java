import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public interface InputReader {

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	default String getInput() {
		try {
			return br.readLine().trim().toLowerCase();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(0);
			return null;
		}
	}
	
}
