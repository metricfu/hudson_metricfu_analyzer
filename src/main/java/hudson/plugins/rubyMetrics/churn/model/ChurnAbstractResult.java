package hudson.plugins.rubyMetrics.churn.model;


public class ChurnAbstractResult {
	
	private String filePath;
	private String timesChanged;
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getTimesChanged() {
		return timesChanged;
	}
	public void setTimesChanged(String timesChanged) {
		this.timesChanged = timesChanged;
	}
	

}
