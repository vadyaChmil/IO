package http_servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadFileFromBrowserToPc extends HttpServlet {

	/**
	 * Vadya Zakusylo
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		byte[] repository;
		final int sizeTemporaryRepository = 1024 * 1024;
		byte[] temporaryRepository = new byte[sizeTemporaryRepository];

		response.setContentType("text/plain");

		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				(InputStream) request.getInputStream());

		int indexTemporaryRepository = 0;
		int contentInputStream;
		while ((contentInputStream = bufferedInputStream.read()) != -1) {
			temporaryRepository[indexTemporaryRepository] = (byte) contentInputStream;
			indexTemporaryRepository++;
		}

		repository = new byte[indexTemporaryRepository];
		for (int index = 0; index < indexTemporaryRepository; index++) {
			repository[index] = temporaryRepository[index];
		}
		temporaryRepository = null;

		String boundary = extractBoundary(request.getHeader("Content-Type"));

		ArrayList<ResponseBlock> responseComponents = divideRepositoryToBlocks(repository, boundary);

		for (ResponseBlock component : responseComponents)
			component.saveFile(repository,
					System.getProperty("user.dir") + System.getProperty("file.separator"));

	}

	private String extractBoundary(String header) {
		int indexBoundaryAttribute = header.lastIndexOf("boundary="); // 9 symbols
		int countBoundaryAttribute = 9;
		String boundary = header.substring(indexBoundaryAttribute + countBoundaryAttribute);
		// real boundary has extra two symbols '-'
		// in the real length than in header Content-Type
		boundary = "--" + boundary;
		return boundary;
	}

	private ArrayList<ResponseBlock> divideRepositoryToBlocks(byte[] repository, String boundary) {
		ArrayList<ResponseBlock> repositoryArray = new ArrayList<>();

		String repositoryString = new String(repository);
		int operation = 0;
		int indexOfBlock = 0;
		int previousIndexOfBlock = 0;

		// content and boundary are separated by {'\r','\n'}
		int boundarySeparator = 2;

		while ((indexOfBlock = repositoryString.indexOf(boundary, indexOfBlock)) != -1) {
			if (operation != 0) {
				ResponseBlock responseBlock = new ResponseBlock("NO_FILE", previousIndexOfBlock,
						indexOfBlock - boundarySeparator);
				pushDataToBlock(responseBlock,
						repositoryString.substring(previousIndexOfBlock, indexOfBlock));
				repositoryArray.add(responseBlock);
			}
			indexOfBlock += boundary.length();
			previousIndexOfBlock = indexOfBlock;
			operation++;
		}
		return repositoryArray;
	}

	private void pushDataToBlock(ResponseBlock responseBlock, String repositoryString) {
		char[] charSeparator = { '\r', '\n', '\r', '\n' };
		String stringSeparator = new String(charSeparator);
		String header;

		// content and header are separated by {'\r','\n','\r','\n'}
		int headerSeparator = 4;
		// content and boundary are separated by {'\r','\n'}
		int boundarySeparator = 2;

		int index = repositoryString.indexOf(stringSeparator, boundarySeparator);
		if (index != -1) {
			header = repositoryString.substring(0, index);
			responseBlock.nameBlock = getName(header);
			responseBlock.startIndex += index + headerSeparator;
		}
	}

	private String getName(String header) {
		String filename = "";
		header.toLowerCase();
		int indexHeaderAttribute = header.indexOf("filename="); // 9 symbols
		int countHeaderAttribute = 9;
		int invertedCommas = 1;
		if (indexHeaderAttribute != -1) {
			int up_index = header.indexOf((int) '"', indexHeaderAttribute + invertedCommas
					+ countHeaderAttribute);
			filename = header.substring(indexHeaderAttribute + countHeaderAttribute
					+ invertedCommas, up_index);
			indexHeaderAttribute = filename.lastIndexOf((int) '/');
			up_index = filename.lastIndexOf((int) '\\');
			filename = filename
					.substring(Math.max(indexHeaderAttribute, up_index) + invertedCommas);
		} else
			filename = "NO_FILE";
		return filename;
	}
}

class ResponseBlock {
	public String nameBlock;
	public int startIndex;
	public int endIndex;

	public ResponseBlock(String filename, int startIndex, int endIndex) {
		this.nameBlock = filename;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public void saveFile(byte[] repository, String direction) {
		if (!nameBlock.equals("NO_FILE")) {
			try (FileOutputStream fileOutputStream = new FileOutputStream(new File(direction
					+ nameBlock))) {
				fileOutputStream.write(repository, startIndex, endIndex - startIndex);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}
}