/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philps.bootcamp.analyzerweb.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import org.junit.Test;
import com.philips.bootcamp.analyzerweb.utils.CloneGit;

public class CloneGitTest {
  @Test
  public void cloneRepoTestPass() {
    final CloneGit cg = new CloneGit();
    final String out = cg.cloneRepo("https://github.com/sidd397/MyRepo.git");
    assertEquals("C:/Project_Material/GithubProjects/", out);
    final File file = new File("C:/Project_Material/GithubProjects/");
    assertTrue((file.isDirectory() && file.list().length > 0));
  }

  @Test
  public void cloneRepoTestFail() {
    final CloneGit cg = new CloneGit();
    final String out = cg.cloneRepo("blah");
    assertEquals("C:/Project_Material/GithubProjects/", out);
    final File file = new File("C:/Project_Material/GithubProjects/");
    assertTrue((file.isDirectory() && file.list().length == 0));
  }

}
