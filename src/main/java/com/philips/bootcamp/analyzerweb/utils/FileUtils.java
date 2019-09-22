package com.philips.bootcamp.analyzerweb.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class FileUtils {

  public static File createDirectory(String folderName) {
    final File directory = new File(folderName);
    directory.mkdir();
    return directory;
  }

  public static void writeFileContents(File file, String contents) {
    try {
      final String name = file.getName();
      if (!file.exists() && name != null && name.length() > 0) {
        file.createNewFile();
      }
    } catch (final IOException ioe) {
      throw new RuntimeException("[ERROR] Something went wrong during file creation!");
    }

    if (file != null && file.isFile()) {
      if (contents == null) {
        contents = "";
      }
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        writer.write(contents);
      } catch (final IOException ioe) {
        System.out.println("[ERROR] Couldn't write to file");
      }
    } else {
      throw new RuntimeException("[ERROR] Invalid file provided!");
    }
  }

  public static void deleteDirectoryRecursion(Path path) throws IOException {
    if (path != null && path.toFile().exists()) {
      if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
        try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
          for (final Path entry : entries) {
            deleteDirectoryRecursion(entry);
          }
        }
      }
      Files.delete(path);
    }
  }
}