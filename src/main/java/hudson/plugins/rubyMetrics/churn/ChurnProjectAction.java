package hudson.plugins.rubyMetrics.churn;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.plugins.rubyMetrics.AbstractRubyMetricsProjectAction;

public class ChurnProjectAction extends AbstractRubyMetricsProjectAction {

	public ChurnProjectAction(AbstractProject<?, ?> project) {
		super(project);
	}
	
	public String getDisplayName() {
		return "Churn report";
	}

	public String getUrlName() {		
		return "churn";
	}
	
	public ChurnBuildAction getLastResult() {
		for (AbstractBuild<?, ?> b = project.getLastStableBuild(); b != null; b = b.getPreviousNotFailedBuild()) {
	        if (b.getResult() == Result.FAILURE)
	            continue;
	        ChurnBuildAction r = b.getAction(ChurnBuildAction.class);
	        if (r != null)
	            return r;
	    }
	    return null;
	}
	
	public Integer getLastResultBuild() {
		for (AbstractBuild<?, ?> b = project.getLastStableBuild(); b != null; b = b.getPreviousNotFailedBuild()) {
            if (b.getResult() == Result.FAILURE)
                continue;
            ChurnBuildAction r = b.getAction(ChurnBuildAction.class);
            if (r != null)
                return b.getNumber();
        }
        return null;
	}

}
