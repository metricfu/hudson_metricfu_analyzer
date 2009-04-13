package hudson.plugins.rubyMetrics.churn.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ChurnResults {
	
	private LinkedList<Map<String, String>> results = new LinkedList<Map<String,String>>();
	
	public void addResult(String file, String value) {
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("file", file);
		result.put("value", value);
		results.add(result);
	}
	
	public LinkedList<Map<String, String>> getResults() {
		return results;
	}
}
