/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class CloneGit {

  String param="C:/Project_Material/GithubProjects/";
  String cloneDirectoryPath3=param;

  public CloneGit() {
    //constructor with no parameter

  }

  public String cloneRepo(String gitRepopath) {

    final File directory=new File(cloneDirectoryPath3);
    try {
      FileUtils.cleanDirectory(directory);
    } catch (final IOException e) {
      System.out.println(e.getMessage());
    }

    try {
      final Git clone=Git.cloneRepository()
          .setURI(gitRepopath)
          .setDirectory(Paths.get(cloneDirectoryPath3).toFile())
          .call();
      clone.close();
    } catch (final GitAPIException e) {
      System.out.println("ERROR IN CLONING REPO FROM GIT"+e.getMessage());
    }

    return cloneDirectoryPath3;
  }
}
