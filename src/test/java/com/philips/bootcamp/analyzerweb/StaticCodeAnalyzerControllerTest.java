/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.philips.bootcamp.analyzerweb.controller.StaticCodeAnalyzerController;
import com.philips.bootcamp.analyzerweb.exceptions.FilePathNotValidException;
import com.philips.bootcamp.analyzerweb.service.CheckstyleAnalyzer;
import com.philips.bootcamp.analyzerweb.service.IntegratedAnalyzer;
import com.philips.bootcamp.analyzerweb.service.PmdAnalyzer;
import com.philips.bootcamp.analyzerweb.service.SimilarityAnalyzer;
import com.philips.bootcamp.analyzerweb.utils.CloneGit;
import com.philips.bootcamp.analyzerweb.utils.JavaFileLister;
import com.philips.bootcamp.analyzerweb.utils.Values;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StaticCodeAnalyzerControllerTest {

  @Test
  public void generateReportCheckstyleAnalyzerValidFilePathGeneratesReportSuccessfully() throws InterruptedException, FilePathNotValidException, IOException {
    final CheckstyleAnalyzer checkstyleTool = new CheckstyleAnalyzer(Values.TEST_VALID_FILE_PATH, Values.CHECKSTYLE_PATH,
        Values.CHECKSTYLE_RULESET);
    StringBuilder output = null;
    output = checkstyleTool.generateReport();
    assertTrue(output.length() > 0);
  }

  @Test
  public void generateReportPmdAnalyzerValidFilePathGeneratesReportSuccessfully() throws FilePathNotValidException, IOException, InterruptedException {
    final PmdAnalyzer pmdTool = new PmdAnalyzer(Values.TEST_VALID_FILE_PATH, Values.PMD_RULESET);
    StringBuilder output = null;
    output = pmdTool.generateReport();
    assertTrue(output.length() > 0);
  }

  @Test
  public void generateReportSimilarityAnalyzerValidFilePathGeneratesReportSuccessfully() throws FilePathNotValidException, IOException, InterruptedException {
    final SimilarityAnalyzer simianTool = new SimilarityAnalyzer(Values.TEST_VALID_FILE_PATH, Values.SIMIAN_PATH);
    StringBuilder output = null;
    output = simianTool.generateReport();
    assertTrue(output.length() > 0);
  }

  @Test
  public void generateReportIntegratedAnalyzerValidFilePathGeneratesReportSuccessfully() throws FilePathNotValidException, IOException, InterruptedException {
    final PmdAnalyzer pmdTool = new PmdAnalyzer(Values.TEST_VALID_FILE_PATH, Values.PMD_RULESET);
    final CheckstyleAnalyzer checkstyleTool = new CheckstyleAnalyzer(Values.TEST_VALID_FILE_PATH, Values.CHECKSTYLE_PATH,
        Values.CHECKSTYLE_RULESET);
    final SimilarityAnalyzer simAnalyzer = new SimilarityAnalyzer(Values.TEST_VALID_FILE_PATH, Values.SIMIAN_PATH);
    final IntegratedAnalyzer integratedAnalyzer = new IntegratedAnalyzer(Values.TEST_VALID_FILE_PATH);
    integratedAnalyzer.add(pmdTool);
    integratedAnalyzer.add(checkstyleTool);
    integratedAnalyzer.add(simAnalyzer);
    StringBuilder output = null;
    output = integratedAnalyzer.generateReport();
    assertTrue(output.length() > 0);
  }

  @Test(expected = FilePathNotValidException.class)
  public void generateReportCheckstyleAnalyzerInvalidFilePathExceptionThrown() throws FilePathNotValidException, IOException, InterruptedException {
    final CheckstyleAnalyzer checkstyleTool = new CheckstyleAnalyzer(Values.TEST_INVALID_FILE_PATH, Values.CHECKSTYLE_PATH,
        Values.CHECKSTYLE_RULESET);
    checkstyleTool.generateReport();
  }

  @Test(expected = FilePathNotValidException.class)
  public void generateReportPmdAnalyzerInvalidFilePathExceptionThrown() throws FilePathNotValidException, IOException, InterruptedException {
    final PmdAnalyzer pmdTool = new PmdAnalyzer(Values.TEST_INVALID_FILE_PATH, Values.PMD_RULESET);
    pmdTool.generateReport();
  }

  @Test(expected = FilePathNotValidException.class)
  public void generateReportSimilarityAnalyzerInvalidFilePathExceptionThrown() throws FilePathNotValidException, IOException, InterruptedException {
    final SimilarityAnalyzer simianTool = new SimilarityAnalyzer(Values.TEST_INVALID_FILE_PATH, Values.SIMIAN_PATH);
    simianTool.generateReport();
  }

  @Test(expected = FilePathNotValidException.class)
  public void generateReportIntegratedAnalyzerTestInvalidFilePathExceptionThrown() throws FilePathNotValidException, IOException, InterruptedException {
    final PmdAnalyzer pmdTool = new PmdAnalyzer(Values.TEST_INVALID_FILE_PATH, Values.PMD_RULESET);
    final CheckstyleAnalyzer checkstyleTool = new CheckstyleAnalyzer(Values.TEST_INVALID_FILE_PATH, Values.CHECKSTYLE_PATH,
        Values.CHECKSTYLE_RULESET);
    final SimilarityAnalyzer simianTool = new SimilarityAnalyzer(Values.TEST_VALID_FILE_PATH, Values.SIMIAN_PATH);
    final IntegratedAnalyzer integratedAnalyzer = new IntegratedAnalyzer(Values.TEST_INVALID_FILE_PATH);
    integratedAnalyzer.add(checkstyleTool);
    integratedAnalyzer.add(pmdTool);
    integratedAnalyzer.add(simianTool);
    integratedAnalyzer.generateReport();
  }
  @Test
  public void getJavaFileListTestValidDirectory() throws Exception{
    final JavaFileLister jfl = new JavaFileLister();
    final List<String> fileList=jfl.javaFilefilter(Values.TEST_VALID_FILE_PATH);
    assertTrue(fileList.size()>0);
  }

  @Test(expected = NullPointerException.class)
  public void getJavaFileListTestInvalidDirectory() throws Exception{
    final JavaFileLister jfl = new JavaFileLister();
    final List<String> fileList=jfl.javaFilefilter(Values.TEST_INVALID_FILE_PATH);
    assertTrue(fileList.size()== 0);
  }
  @Test
  public void genCheckstyleGitRepoTestPass() throws FilePathNotValidException, IOException, InterruptedException {
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final CheckstyleAnalyzer csa = Mockito.mock(CheckstyleAnalyzer.class);
    Mockito.when(csa.generateReport()).thenReturn(new StringBuilder("report"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setCheckstyleAnalyzer(csa);

    final ResponseEntity<String> response = scac.genCheckstyleGitRepo("test");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"data\":\"report\"}", response.getBody());
  }

  @Test
  public void genCheckstyleGitRepoTestFail() throws FilePathNotValidException, IOException, InterruptedException {
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final CheckstyleAnalyzer csa = Mockito.mock(CheckstyleAnalyzer.class);
    Mockito.when(csa.generateReport()).thenThrow(new FilePathNotValidException("hey"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setCheckstyleAnalyzer(csa);

    final ResponseEntity<String> response = scac.genCheckstyleGitRepo("test");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("{\"data\":\"Error: File not found\"}", response.getBody());
  }

  @Test
  public void genPmdTestPass() throws IOException, InterruptedException, FilePathNotValidException{
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final PmdAnalyzer pmda = Mockito.mock(PmdAnalyzer.class);
    Mockito.when(pmda.generateReport()).thenReturn(new StringBuilder("report"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setPmdAnalyzer(pmda);

    final ResponseEntity<String> response = scac.genPmd("test");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"data\":\"report\"}", response.getBody());

  }

  @Test
  public void genPmdTestFail() throws FilePathNotValidException, IOException, InterruptedException {
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final PmdAnalyzer pmda = Mockito.mock(PmdAnalyzer.class);
    Mockito.when(pmda.generateReport()).thenThrow(new FilePathNotValidException("hey"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setPmdAnalyzer(pmda);

    final ResponseEntity<String> response = scac.genPmd("test");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("{\"data\":\"Error: File not found\"}", response.getBody());
  }


  @Test
  public void genSimianTestPass() throws IOException, InterruptedException, FilePathNotValidException{
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final SimilarityAnalyzer sima = Mockito.mock(SimilarityAnalyzer.class);
    Mockito.when(sima.generateReport()).thenReturn(new StringBuilder("report"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setSimian(sima);

    final ResponseEntity<String> response = scac.genSimianCodeRepo("test");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"data\":\"GO\\nCount of Issues = 0\\r\\nreport\"}", response.getBody());

  }

  @Test
  public void genSimianTestFail() throws FilePathNotValidException, IOException, InterruptedException {
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final SimilarityAnalyzer sima = Mockito.mock(SimilarityAnalyzer.class);
    Mockito.when(sima.generateReport()).thenThrow(new FilePathNotValidException("hey"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setSimian(sima);

    final ResponseEntity<String> response = scac.genSimianCodeRepo("test");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("{\"data\":\"Error: File not found\"}", response.getBody());
  }

  @Test
  public void genIntegratedReportTestFail() throws FilePathNotValidException, IOException, InterruptedException{
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final CheckstyleAnalyzer csa = Mockito.mock(CheckstyleAnalyzer.class);
    final PmdAnalyzer pmd = Mockito.mock(PmdAnalyzer.class);
    final SimilarityAnalyzer simian = Mockito.mock(SimilarityAnalyzer.class);

    final IntegratedAnalyzer inta = Mockito.mock(IntegratedAnalyzer.class);
    Mockito.when(inta.generateReport()).thenThrow(new FilePathNotValidException("message"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setSimian(simian);
    scac.setCheckstyleAnalyzer(csa);
    scac.setPmdAnalyzer(pmd);
    scac.setIntegratedAnalyzer(inta);

    final ResponseEntity<String> response = scac.genIntegratedReport("test");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("{\"data\":\"Error: File not found\"}", response.getBody());

  }

  @Test
  public void genIntegratedReportTestPass1() throws FilePathNotValidException, IOException, InterruptedException{
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final CheckstyleAnalyzer csa = Mockito.mock(CheckstyleAnalyzer.class);
    final PmdAnalyzer pmd = Mockito.mock(PmdAnalyzer.class);
    final SimilarityAnalyzer simian = Mockito.mock(SimilarityAnalyzer.class);

    final IntegratedAnalyzer inta = Mockito.mock(IntegratedAnalyzer.class);
    Mockito.when(inta.generateReport()).thenReturn(new StringBuilder("message"));
    Mockito.when(inta.getIssueCount()).thenReturn(0);

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setSimian(simian);
    scac.setCheckstyleAnalyzer(csa);
    scac.setPmdAnalyzer(pmd);
    scac.setIntegratedAnalyzer(inta);

    final ResponseEntity<String> response = scac.genIntegratedReport("test");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"data\":\"GO\\nCount of Issues = 0\\r\\nmessage\"}", response.getBody());

  }

  @Test
  public void genIntegratedReportTestPass2() throws FilePathNotValidException, IOException, InterruptedException{
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final CheckstyleAnalyzer csa = Mockito.mock(CheckstyleAnalyzer.class);
    final PmdAnalyzer pmd = Mockito.mock(PmdAnalyzer.class);
    final SimilarityAnalyzer simian = Mockito.mock(SimilarityAnalyzer.class);

    final IntegratedAnalyzer inta = Mockito.mock(IntegratedAnalyzer.class);
    Mockito.when(inta.generateReport()).thenReturn(new StringBuilder("message"));
    Mockito.when(inta.getIssueCount()).thenReturn(1);

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setSimian(simian);
    scac.setCheckstyleAnalyzer(csa);
    scac.setPmdAnalyzer(pmd);
    scac.setIntegratedAnalyzer(inta);

    final ResponseEntity<String> response = scac.genIntegratedReport("test");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"data\":\"NO-GO\\nCount of Issues = 1\\r\\nmessage\"}", response.getBody());

  }

  @Test
  public void genIntegratedRepoForCodeTestPass1() throws FilePathNotValidException, IOException, InterruptedException{
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final CheckstyleAnalyzer csa = Mockito.mock(CheckstyleAnalyzer.class);
    final PmdAnalyzer pmd = Mockito.mock(PmdAnalyzer.class);
    final SimilarityAnalyzer simian = Mockito.mock(SimilarityAnalyzer.class);

    final IntegratedAnalyzer inta = Mockito.mock(IntegratedAnalyzer.class);
    Mockito.when(inta.generateReport()).thenReturn(new StringBuilder("message"));
    Mockito.when(inta.getIssueCount()).thenReturn(0);

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setSimian(simian);
    scac.setCheckstyleAnalyzer(csa);
    scac.setPmdAnalyzer(pmd);
    scac.setIntegratedAnalyzer(inta);

    final ResponseEntity<String> response = scac.genIntegratedRepoForCode("test");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"data\":\"GO\\nCount of Issues = 0\\r\\nmessage\"}", response.getBody());

  }

  @Test
  public void genIntegratedRepoForCodeTestPass2() throws FilePathNotValidException, IOException, InterruptedException{
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final CheckstyleAnalyzer csa = Mockito.mock(CheckstyleAnalyzer.class);
    final PmdAnalyzer pmd = Mockito.mock(PmdAnalyzer.class);
    final SimilarityAnalyzer simian = Mockito.mock(SimilarityAnalyzer.class);

    final IntegratedAnalyzer inta = Mockito.mock(IntegratedAnalyzer.class);
    Mockito.when(inta.generateReport()).thenReturn(new StringBuilder("message"));
    Mockito.when(inta.getIssueCount()).thenReturn(1);

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setSimian(simian);
    scac.setCheckstyleAnalyzer(csa);
    scac.setPmdAnalyzer(pmd);
    scac.setIntegratedAnalyzer(inta);

    final ResponseEntity<String> response = scac.genIntegratedRepoForCode("test");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"data\":\"NO-GO\\nCount of Issues = 1\\r\\nmessage\"}", response.getBody());

  }

  @Test
  public void genIntegratedRepoForCodeTestFail() throws FilePathNotValidException, IOException, InterruptedException{
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final CheckstyleAnalyzer csa = Mockito.mock(CheckstyleAnalyzer.class);
    final PmdAnalyzer pmd = Mockito.mock(PmdAnalyzer.class);
    final SimilarityAnalyzer simian = Mockito.mock(SimilarityAnalyzer.class);

    final IntegratedAnalyzer inta = Mockito.mock(IntegratedAnalyzer.class);
    Mockito.when(inta.generateReport()).thenThrow(new FilePathNotValidException("message"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setSimian(simian);
    scac.setCheckstyleAnalyzer(csa);
    scac.setPmdAnalyzer(pmd);
    scac.setIntegratedAnalyzer(inta);

    final ResponseEntity<String> response = scac.genIntegratedRepoForCode("test");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("{\"data\":\"Error: File not found\"}", response.getBody());

  }

  @Test
  public void genCheckstyleCodeRepoTestPass() throws FilePathNotValidException, IOException, InterruptedException {
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final CheckstyleAnalyzer csa = Mockito.mock(CheckstyleAnalyzer.class);
    Mockito.when(csa.generateReport()).thenReturn(new StringBuilder("report"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setCheckstyleAnalyzer(csa);

    final ResponseEntity<String> response = scac.genCheckstyleCodeRepo("test");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"data\":\"GO\\nCount of Issues = 0\\r\\nreport\"}", response.getBody());
  }

  @Test
  public void genCheckstyleCodeRepoTestFail() throws FilePathNotValidException, IOException, InterruptedException {
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final CheckstyleAnalyzer csa = Mockito.mock(CheckstyleAnalyzer.class);
    Mockito.when(csa.generateReport()).thenThrow(new FilePathNotValidException("hey"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setCheckstyleAnalyzer(csa);

    final ResponseEntity<String> response = scac.genCheckstyleCodeRepo("test");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("{\"data\":\"Error: File not found\"}", response.getBody());
  }

  @Test
  public void genPmdCodeRepoTestPass() throws IOException, InterruptedException, FilePathNotValidException{
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final PmdAnalyzer pmda = Mockito.mock(PmdAnalyzer.class);
    Mockito.when(pmda.generateReport()).thenReturn(new StringBuilder("report"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setPmdAnalyzer(pmda);

    final ResponseEntity<String> response = scac.genPmdCodeRepo("test");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"data\":\"GO\\nCount of Issues = 0\\r\\nreport\"}", response.getBody());

  }

  @Test
  public void genPmdCodeRepoTestFail() throws FilePathNotValidException, IOException, InterruptedException {
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final PmdAnalyzer pmda = Mockito.mock(PmdAnalyzer.class);
    Mockito.when(pmda.generateReport()).thenThrow(new FilePathNotValidException("hey"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setPmdAnalyzer(pmda);

    final ResponseEntity<String> response = scac.genPmdCodeRepo("test");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("{\"data\":\"Error: File not found\"}", response.getBody());
  }


  @Test
  public void genSimianCodeRepoTestPass() throws IOException, InterruptedException, FilePathNotValidException{
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final SimilarityAnalyzer sima = Mockito.mock(SimilarityAnalyzer.class);
    Mockito.when(sima.generateReport()).thenReturn(new StringBuilder("report"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setSimian(sima);

    final ResponseEntity<String> response = scac.genSimianCodeRepo("test");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"data\":\"GO\\nCount of Issues = 0\\r\\nreport\"}", response.getBody());

  }

  @Test
  public void genSimianCodeRepoTestFail() throws FilePathNotValidException, IOException, InterruptedException {
    final CloneGit cg = Mockito.mock(CloneGit.class);
    Mockito.when(cg.cloneRepo("test")).thenReturn("filepath");

    final SimilarityAnalyzer sima = Mockito.mock(SimilarityAnalyzer.class);
    Mockito.when(sima.generateReport()).thenThrow(new FilePathNotValidException("hey"));

    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    scac.setCloneGit(cg);
    scac.setSimian(sima);

    final ResponseEntity<String> response = scac.genSimianCodeRepo("test");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("{\"data\":\"Error: File not found\"}", response.getBody());
  }



}
