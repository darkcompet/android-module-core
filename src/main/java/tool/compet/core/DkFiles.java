/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class, provides common basic operations on file, directory.
 */
public class DkFiles {
	public static boolean exists(String filePath) {
		return new File(filePath).exists();
	}

	public static boolean createFile(String filePath) {
		return createFile(new File(filePath));
	}

	/**
	 * Create file deeply (include its parent directory if not exist) if not exist.
	 *
	 * @return true if file was existed, or new file was created. Otherwise false.
	 */
	public static boolean createFile(File file) {
		try {
			if (file.exists()) {
				return true;
			}
			File parent = file.getParentFile();
			if (parent != null && ! parent.exists() && ! parent.mkdirs()) {
				return false;
			}
			return file.createNewFile();
		}
		catch (Exception e) {
			DkLogs.error(DkFiles.class, e, "Could not create new file: " + file.getPath());
			return false;
		}
	}

	public static boolean createDir(String dirPath) {
		return createDir(new File(dirPath));
	}

	/**
	 * Create directory deeply (include its parent directory if not exist) if not exist.
	 *
	 * @return true if new directory was created or given directory was existed, otherwise false.
	 */
	public static boolean createDir(File dir) {
		try {
			if (dir.exists()) {
				return true;
			}
			File parent = dir.getParentFile();
			if (parent != null && ! parent.exists() && ! parent.mkdirs()) {
				return false;
			}
			return dir.mkdir();
		}
		catch (Exception e) {
			DkLogs.error(DkFiles.class, e, "Could not create new dir: " + dir.getPath());
			return false;
		}
	}

	public static boolean delete(String filePath) {
		return delete(new File(filePath));
	}

	/**
	 * Delete file or directory. Note that, Java does not delete dirty folder,
	 * so first, we need delete dirs on the parent path of this file.
	 * @return true if file not exist or file was deleted successful. Otherwise false.
	 */
	public static boolean delete(File file) {
		if (file == null || ! file.exists()) {
			return true;
		}
		if (file.isFile()) {
			return file.delete();
		}

		File[] children = file.listFiles();

		if (children != null) {
			// Rule: delete all children files before delete directory
			for (File child : children) {
				delete(child);
			}
		}
		return file.delete();
	}

	public static void save(String utf8Chars, File file, boolean append) throws IOException {
		save(utf8Chars == null ? "".getBytes() : utf8Chars.getBytes(), file.getAbsolutePath(), append);
	}

	public static void save(String utf8Chars, String filePath, boolean append) throws IOException {
		save(utf8Chars == null ? "".getBytes() : utf8Chars.getBytes(), filePath, append);
	}

	public static void save(byte[] data, File file, boolean append) throws IOException {
		save(data, file.getPath(), append);
	}

	/**
	 * Save (write) data to the file.
	 */
	public static void save(byte[] data, String filePath, boolean append) throws IOException {
		createFile(filePath);

		OutputStream os = new FileOutputStream(filePath, append);
		os.write(data);
		os.close();
	}

	/**
	 * Load (read) data from given file.
	 */
	public static String loadAsString(String filePath) throws IOException {
		createFile(filePath);

		String line;
		StringBuilder sb = new StringBuilder(256);
		BufferedReader br = new BufferedReader(new FileReader(filePath));

		while ((line = br.readLine()) != null) {
			sb.append(line).append(DkConst.LS);
		}

		br.close();

		return sb.toString();
	}

	/**
	 * Load (read) data as UTF-8 from given file.
	 */
	public static List<String> loadAsUtf8Lines(File file) throws Exception {
		List<String> lines = new ArrayList<>();
		BufferedReader reader = newUtf8Reader(file);
		String readLine;

		while ((readLine = reader.readLine()) != null) {
			lines.add(readLine);
		}

		reader.close();

		return lines;
	}

	/**
	 * Load (read) data from given file and result as byte[].
	 *
	 * @return Null if file not found. Otherwise byte array.
	 */
	public static byte[] loadAsBytes(String filePath) {
		try {
			if (! exists(filePath)) {
				return null;
			}
			return loadAsBytes(new FileInputStream(filePath));
		}
		catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * Load (read) data from given file.
	 *
	 * @return Null if file not found. Otherwise byte array.
	 */
	public static byte[] loadAsBytes(InputStream is) {
		try {
			int capacity = 2 << 12;
			DkByteArrayList result = new DkByteArrayList(capacity);
			byte[] buffer = new byte[capacity];
			int readCount;

			while ((readCount = is.read(buffer)) != -1) {
				result.addRange(buffer, 0, readCount);
			}

			return result.toArray();
		}
		catch (Exception e) {
			return null;
		}
	}

	public static BufferedReader newUtf8Reader(File file) throws Exception {
		return new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
	}

	public static BufferedWriter newUtf8Writer(File file) throws Exception {
		return newUtf8Writer(file, false);
	}

	public static BufferedWriter newUtf8Writer(File file, boolean append) throws Exception {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8));
	}

	public static List<File> collectFilesRecursively(File dir) {
		List<File> result = new ArrayList<>();
		File[] chilren = dir.listFiles();

		if (chilren != null) {
			for (File child : chilren) {
				if (child.isFile()) {
					result.add(child);
				}
				else {
					result.addAll(collectFilesRecursively(child));
				}
			}
		}

		return result;
	}

	/**
	 * @param file A file, for eg,. welcome.bk.txt
	 * @return Name of this file without extension, for eg,. welcome.bk
	 */
	public static String filename(File file) {
		String fileName = file.getName();
		int endIndex = fileName.lastIndexOf(".");

		return endIndex < 0 ? fileName : fileName.substring(0, endIndex);
	}

	public static String makePath(String... names) {
		if (names == null) {
			return DkConst.EMPTY_STRING;
		}

		StringBuilder sb = new StringBuilder(64);
		boolean first = true;

		for (String name : names) {
			if (first) {
				first = false;
				sb.append(name);
			}
			else {
				sb.append(File.separatorChar).append(name);
			}
		}

		return sb.toString();
	}
}
