/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.philips.bootcamp.analyzerweb.controller.StaticCodeAnalyzerController;
import com.philips.bootcamp.analyzerweb.utils.Values;

public class ControllerClassTest {

  @Test
  public void generateReport_Controller_PmdAnalyzer_ValidFilePath_GenerateWithOKSTatus() throws URISyntaxException, ClientProtocolException, IOException, InterruptedException {
    final StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController();
    final ResponseEntity<String> response  =  scac.genPmd(Values.TEST_VALID_FILE_PATH);
    assertEquals(HttpStatus.OK,response.getStatusCode());
  }

  /*
   * @Test public void
   * generateReport_Controller_CheckstyleAnalyzer_ValidFilePath_GenerateWithOKSTatus() throws
   * URISyntaxException, ClientProtocolException, IOException, InterruptedException { final
   * StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController(); final
   * ResponseEntity<StringBuilder> response = scac.genCheckstyle(Values.TEST_VALID_FILE_PATH);
   * assertEquals(HttpStatus.OK,response.getStatusCode()); }
   *
   * @Test public void
   * generateReport_Controller_SimilarityAnalyzer_ValidFilePath_GenerateWithOKSTatus() throws
   * URISyntaxException, ClientProtocolException, IOException, InterruptedException { final
   * StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController(); final
   * ResponseEntity<StringBuilder> response = scac.genSimian(Values.TEST_VALID_FILE_PATH);
   * assertEquals(HttpStatus.OK,response.getStatusCode()); }
   *
   * @Test public void generateReport_IntegratedAnalyzer_ValidFilePath_GeneratesReportSuccessfully()
   * throws FilePathNotValidException, IOException, InterruptedException { final
   * StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController(); final
   * ResponseEntity<StringBuilder> response = scac.genIntegratedReport(Values.TEST_VALID_FILE_PATH);
   * assertEquals(HttpStatus.OK,response.getStatusCode()); }
   *
   * @Test public void generateReport_Controller_PmdAnalyzer_InvalidFilePath_GenerateWith404STatus()
   * throws URISyntaxException, ClientProtocolException, IOException, InterruptedException { final
   * StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController(); final
   * ResponseEntity<StringBuilder> response = scac.genPmd(Values.TEST_INVALID_FILE_PATH);
   * assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode()); }
   *
   * @Test public void
   * generateReport_Controller_CheckstyleAnalyzer_InvalidFilePath_GenerateWith404STatus() throws
   * URISyntaxException, ClientProtocolException, IOException, InterruptedException { final
   * StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController(); final
   * ResponseEntity<StringBuilder> response = scac.genCheckstyle(Values.TEST_INVALID_FILE_PATH);
   * assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode()); }
   *
   * @Test public void
   * generateReport_Controller_SimilarityAnalyzer_InvalidFilePath_GenerateWith404STatus() throws
   * URISyntaxException, ClientProtocolException, IOException, InterruptedException { final
   * StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController(); final
   * ResponseEntity<StringBuilder> response = scac.genSimian(Values.TEST_INVALID_FILE_PATH);
   * assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode()); }
   *
   * @Test public void
   * generateReport_IntegratedAnalyzer_InValidFilePath_GeneratesReportSuccessfully() throws
   * FilePathNotValidException, IOException, InterruptedException { final
   * StaticCodeAnalyzerController scac = new StaticCodeAnalyzerController(); final
   * ResponseEntity<StringBuilder> response =
   * scac.genIntegratedReport(Values.TEST_INVALID_FILE_PATH);
   * assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode()); }
   */
}
