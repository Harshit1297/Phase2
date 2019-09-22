/*
cha * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.philips.bootcamp.analyzerweb.exceptions.FilePathNotValidException;
import com.philips.bootcamp.analyzerweb.service.CheckstyleAnalyzer;
import com.philips.bootcamp.analyzerweb.service.IntegratedAnalyzer;
import com.philips.bootcamp.analyzerweb.service.PmdAnalyzer;
import com.philips.bootcamp.analyzerweb.service.SimilarityAnalyzer;
import com.philips.bootcamp.analyzerweb.utils.CloneGit;
import com.philips.bootcamp.analyzerweb.utils.FileUtils;
import com.philips.bootcamp.analyzerweb.utils.JavaFileLister;
import com.philips.bootcamp.analyzerweb.utils.Values;

@RestController
public class StaticCodeAnalyzerController {

  CloneGit cg = new CloneGit();

  CheckstyleAnalyzer csa = new CheckstyleAnalyzer(Values.CHECKSTYLE_PATH, Values.CHECKSTYLE_RULESET);
  PmdAnalyzer pmd = new PmdAnalyzer(Values.PMD_RULESET);
  SimilarityAnalyzer simian = new SimilarityAnalyzer(Values.SIMIAN_PATH);

  IntegratedAnalyzer iAnalyzer = new IntegratedAnalyzer();

  public void setCloneGit(CloneGit cg) { this.cg = cg; }
  public void setCheckstyleAnalyzer(CheckstyleAnalyzer csa) { this.csa = csa; }
  public void setPmdAnalyzer(PmdAnalyzer pmd) { this.pmd = pmd; }
  public void setSimian(SimilarityAnalyzer simian) { this.simian = simian; }
  public void setIntegratedAnalyzer(IntegratedAnalyzer iAnalyzer) { this.iAnalyzer = iAnalyzer; }

  @PostMapping(value = "/api/cs")
  public ResponseEntity<String> genCheckstyleGitRepo(@RequestBody String gitfilepath)
      throws IOException, InterruptedException {
    final String filepath = cg.cloneRepo(gitfilepath);
    csa.setFilepath(filepath);

    final JSONObject object = new JSONObject();

    try {
      object.put("data", csa.generateReport().toString());
      System.out.println(object);
      return ResponseEntity.ok().body(object.toString());
    } catch (final FilePathNotValidException e) {
      object.put("data", Values.ERROR_FILE_NOT_FOUND);
      System.out.println(object);
      return new ResponseEntity<>(object.toString(), HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping(value = "/api/code/cs")
  public ResponseEntity<String> genCheckstyleCodeRepo(@RequestBody String source)
      throws IOException, InterruptedException {

    final File parent = FileUtils.createDirectory("source");
    final File sourceFile = new File(parent, "Test.java");
    FileUtils.writeFileContents(sourceFile, source);

    csa.setFilepath(parent.getAbsolutePath());

    final JSONObject object = new JSONObject();

    try {
      object.put("data", csa.generateReport().toString());

      final int countOfIssues = csa.getIssueCount();
      final StringBuilder output = csa.generateReport();
      object.put("data", output.insert(0, String.format("Count of Issues = %d%n", countOfIssues)));
      if (countOfIssues == 0) {
        object.put("data", output.insert(0, "GO\n"));
      } else {
        object.put("data", output.insert(0, "NO-GO\n"));
      }
      FileUtils.deleteDirectoryRecursion(parent.toPath());
      return ResponseEntity.ok().body(object.toString());
    } catch (final FilePathNotValidException e) {
      object.put("data", Values.ERROR_FILE_NOT_FOUND);
      System.out.println(object);
      return new ResponseEntity<>(object.toString(), HttpStatus.NOT_FOUND);
    }

  }

  @PostMapping(value = "/api/code/pmd")
  public ResponseEntity<String> genPmdCodeRepo(@RequestBody String source) throws IOException, InterruptedException {

    final File parent = FileUtils.createDirectory("source");
    final File sourceFile = new File(parent, "Test.java");
    FileUtils.writeFileContents(sourceFile, source);

    pmd.setFilepath(parent.getAbsolutePath());

    final JSONObject object = new JSONObject();

    try {
      object.put("data", pmd.generateReport().toString());

      System.out.println(object);
      final int countOfIssues = pmd.getIssueCount();
      final StringBuilder output = pmd.generateReport();
      object.put("data", output.insert(0, String.format("Count of Issues = %d%n", countOfIssues)));
      if (countOfIssues == 0) {
        object.put("data", output.insert(0, "GO\n"));
      } else {
        object.put("data", output.insert(0, "NO-GO\n"));
      }
      FileUtils.deleteDirectoryRecursion(parent.toPath());
      return ResponseEntity.ok().body(object.toString());
    } catch (final FilePathNotValidException e) {
      object.put("data", Values.ERROR_FILE_NOT_FOUND);
      System.out.println(object);
      return new ResponseEntity<>(object.toString(), HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping(value = "/api/pmd")
  public ResponseEntity<String> genPmd(@RequestBody String gitfilepath) throws IOException, InterruptedException {
    final String filepath = cg.cloneRepo(gitfilepath);

    pmd.setFilepath(filepath);

    final JSONObject object = new JSONObject();

    try {
      object.put("data", pmd.generateReport().toString());
      System.out.println(object);
      return ResponseEntity.ok().body(object.toString());
    } catch (final FilePathNotValidException e) {
      object.put("data", Values.ERROR_FILE_NOT_FOUND);
      System.out.println(object);
      return new ResponseEntity<>(object.toString(), HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping(value = "/api/sim")
  public ResponseEntity<String> genSimian(@RequestBody String gitfilepath) throws IOException, InterruptedException {
    final String filepath = cg.cloneRepo(gitfilepath);
    simian.setFilepath(filepath);

    final JSONObject object = new JSONObject();

    try {
      object.put("data", simian.generateReport().toString());
      return ResponseEntity.ok().body(object.toString());
    } catch (final FilePathNotValidException e) {
      object.put("data", Values.ERROR_FILE_NOT_FOUND);
      return new ResponseEntity<>(object.toString(), HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping(value = "/api/code/sim")
  public ResponseEntity<String> genSimianCodeRepo(@RequestBody String source) throws IOException, InterruptedException {
    final File parent = FileUtils.createDirectory("source");
    final File sourceFile = new File(parent, "Test.java");
    FileUtils.writeFileContents(sourceFile, source);

    simian.setFilepath(parent.getAbsolutePath());

    final JSONObject object = new JSONObject();

    try {
      object.put("data", simian.generateReport().toString());
      final int countOfIssues = simian.getIssueCount();
      final StringBuilder output = simian.generateReport();
      object.put("data", output.insert(0, String.format("Count of Issues = %d%n", countOfIssues)));
      if (countOfIssues == 0) {
        object.put("data", output.insert(0, "GO\n"));
      } else {
        object.put("data", output.insert(0, "NO-GO\n"));
      }
      FileUtils.deleteDirectoryRecursion(parent.toPath());
      return ResponseEntity.ok().body(object.toString());
    } catch (final FilePathNotValidException e) {
      object.put("data", Values.ERROR_FILE_NOT_FOUND);
      return new ResponseEntity<>(object.toString(), HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping(value = "/api/all")
  public ResponseEntity<String> genIntegratedReport(@RequestBody String gitfilepath)
      throws IOException, InterruptedException {

    final IntegratedAnalyzer integratedAnalyzer;
    final String filepath = cg.cloneRepo(gitfilepath);

    csa.setFilepath(filepath);
    pmd.setFilepath(filepath);
    simian.setFilepath(filepath);

    iAnalyzer.setFilepath(filepath);

    iAnalyzer.add(csa);
    iAnalyzer.add(pmd);
    iAnalyzer.add(simian);

    final JSONObject object = new JSONObject();
    try {
      object.put("data", iAnalyzer.generateReport().toString());
      System.out.println(object);
      final int countOfIssues = iAnalyzer.getIssueCount();
      final StringBuilder output = iAnalyzer.generateReport();
      object.put("data", output.insert(0, String.format("Count of Issues = %d%n", countOfIssues)));
      if (countOfIssues == 0) {
        object.put("data", output.insert(0, "GO\n"));
      } else {
        object.put("data", output.insert(0, "NO-GO\n"));
      }
      return ResponseEntity.ok().body(object.toString());
    } catch (final FilePathNotValidException e) {
      object.put("data", Values.ERROR_FILE_NOT_FOUND);
      System.out.println(object);
      return new ResponseEntity<>(object.toString(), HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping(value = "/api/code/all")
  public ResponseEntity<String> genIntegratedRepoForCode(@RequestBody String source)
      throws IOException, InterruptedException {
    final File parent = FileUtils.createDirectory("source");
    final File sourceFile = new File(parent, "Test.java");
    FileUtils.writeFileContents(sourceFile, source);

    simian.setFilepath(parent.getAbsolutePath());
    pmd.setFilepath(parent.getAbsolutePath());
    csa.setFilepath(parent.getAbsolutePath());

    iAnalyzer.add(csa);
    iAnalyzer.add(simian);
    iAnalyzer.add(pmd);

    final JSONObject object = new JSONObject();
    try {
      object.put("data", iAnalyzer.generateReport().toString());
      System.out.println(object);
      final int countOfIssues = iAnalyzer.getIssueCount();
      final StringBuilder output = iAnalyzer.generateReport();
      object.put("data", output.insert(0, String.format("Count of Issues = %d%n", countOfIssues)));
      if (countOfIssues == 0) {
        object.put("data", output.insert(0, "GO\n"));
      } else {
        object.put("data", output.insert(0, "NO-GO\n"));
      }
      FileUtils.deleteDirectoryRecursion(parent.toPath());
      return ResponseEntity.ok().body(object.toString());
    } catch (final FilePathNotValidException e) {
      object.put("data", Values.ERROR_FILE_NOT_FOUND);
      System.out.println(object);
      return new ResponseEntity<>(object.toString(), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping(value = "/api")
  public ModelAndView getFiles(@RequestParam("path") String path) throws IOException {
    final JavaFileLister listFiles = new JavaFileLister();
    final ModelAndView model = new ModelAndView("index");
    final String filepath = java.net.URLDecoder.decode(path, StandardCharsets.UTF_8.toString());
    model.addObject("lists", listFiles.javaFilefilter(filepath));
    return model;
  }
}
