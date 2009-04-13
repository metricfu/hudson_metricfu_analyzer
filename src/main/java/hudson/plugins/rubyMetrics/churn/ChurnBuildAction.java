package hudson.plugins.rubyMetrics.churn;

import hudson.model.AbstractBuild;
import hudson.model.HealthReport;
import hudson.plugins.rubyMetrics.AbstractRubyMetricsBuildAction;
import hudson.plugins.rubyMetrics.churn.model.ChurnResults;
import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class ChurnBuildAction extends AbstractRubyMetricsBuildAction {
	
	private final ChurnResults results;
	
	public ChurnBuildAction(AbstractBuild<?, ?> owner, ChurnResults results) {
		super(owner);
		this.results = results;
	}

	public HealthReport getBuildHealth() {
		return null;
	}

	public String getDisplayName() {
		return "Churn report";
	}

	public String getIconFileName() {
		return "graph.gif";
	}

	public String getUrlName() {
		return "churn";
	}

	public AbstractBuild<?, ?> getOwner() {
		return owner;
	}

	public ChurnResults getResults() {
		return results;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected DataSetBuilder<String, NumberOnlyBuildLabel> getDataSetBuilder() {
		DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> dsb = new DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel>();
		for (ChurnBuildAction a = this; a != null; a = a.getPreviousResult()) {
			ChartUtil.NumberOnlyBuildLabel label = new ChartUtil.NumberOnlyBuildLabel(a.owner);
			
			LinkedList<Map<String, String>> list = results.getResults();
			int count = 0;
			for (Map<String, String> map : list) {
				if(count < 5){
					dsb.add(new Integer(map.get("value")), map.get("file"), label);
				}
				count++;
			}
		}		
		
        return dsb;
	}
	
	@Override
	protected String getRangeAxisLabel() {
		return "";
	}    
}
