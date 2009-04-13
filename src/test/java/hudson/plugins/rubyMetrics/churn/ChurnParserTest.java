package hudson.plugins.rubyMetrics.churn;

import hudson.plugins.rubyMetrics.churn.model.ChurnResults;

import java.io.InputStream;
import java.util.Map;

import junit.framework.TestCase;

public class ChurnParserTest extends TestCase {
	
	public void testParse() throws Exception {
		
		InputStream input = this.getClass().getResourceAsStream("index.html");
		
		ChurnParser churnParser = new ChurnParser();
		ChurnResults results = churnParser.parse(input);
		
		assertNotNull(results);
		
		assertTrue(results.getResults().size() > 0);
		
		for (Map<String, String> result : results.getResults()) {
			assertNotNull(result.get("file"));
			assertNotNull(result.get("value"));
			assertNotSame("", result.get("file").trim());
			assertNotSame("", result.get("value").trim());
		}
		
	}
}
