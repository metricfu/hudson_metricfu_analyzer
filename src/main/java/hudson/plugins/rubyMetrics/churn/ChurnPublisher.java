package hudson.plugins.rubyMetrics.churn;

import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.Action;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Project;
import hudson.model.Result;
import hudson.plugins.rubyMetrics.RubyMetricsPublisher;
import hudson.plugins.rubyMetrics.churn.model.ChurnResults;
import hudson.tasks.Publisher;

import java.io.File;
import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;


@SuppressWarnings("unchecked")
public class ChurnPublisher extends RubyMetricsPublisher {

	private final String reportDir;
	
	@DataBoundConstructor
	public ChurnPublisher(String reportDir) {
		this.reportDir = reportDir;				
	}
	
	public String getReportDir() {
		return reportDir;
	}
	
	/**
     * {@inheritDoc}
     */
    public boolean perform(Build<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
    	if (!Result.SUCCESS.equals(build.getResult())) {
    		listener.getLogger().println("Build wasn't successful, skipping churn report");
    		return true;
    	}
    	listener.getLogger().println("Publishing Rcov report...");
    	    	    	
        final Project<?, ?> project = build.getParent();        
        final FilePath workspace = project.getModuleRoot();        
        
        if (!isRailsProject(workspace)) {
        	return fail(build, listener, "This is not a rails app directory: " + workspace.getName());
        }
		
        boolean copied = moveFileIndexToBuildRootDir(workspace, build, listener);
        if (!copied) {
        	return fail(build, listener, "Churn report directory wasn't found using the pattern '" + reportDir + "'.");
        }
		
        final IndexFilenameFilter indexFilter = new IndexFilenameFilter("churn.html");
        
        File[] files = build.getRootDir().listFiles(indexFilter);
        if (!(files != null && files.length > 0)) {
        	return fail(build, listener, "Churn report index file wasn't found");
        }
        
        ChurnParser parser = new ChurnParser();
        ChurnResults results = parser.parse(files[0]);
    	
        ChurnBuildAction action = new ChurnBuildAction(build, results);        
        build.getActions().add(action);
		
    	return true;
    }
    
    private boolean moveFileIndexToBuildRootDir(FilePath workspace, Build<?, ?> build, BuildListener listener) throws InterruptedException {    	
        try {        	
        	FilePath dir = workspace.child(reportDir);
        	if (!dir.exists()) {
        		listener.getLogger().println("file not found: " + dir);
        		return false;
        	}
        	dir.list(new IndexFileFilter()).get(0).copyTo(new FilePath( new File(build.getRootDir().getAbsolutePath()+File.separator+"churn.html") ));
            return true;
        } catch (IOException e) {
            Util.displayIOException(e, listener);
            e.printStackTrace(listener.fatalError("Unable to find churn results"));
            build.setResult(Result.FAILURE);
        }
        return false;
    }
    
	private boolean isRailsProject(FilePath workspace) {
		try { //relaxed rails app schema
			return workspace.isDirectory()
				&& workspace.list("app") != null && workspace.list("config") != null
				&& workspace.list("db") != null && workspace.list("test") != null;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	@Override
	public Action getProjectAction(Project project) {
		return new ChurnProjectAction(project);
	}
	
	/**
     * Descriptor should be singleton.
     */
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    public static final class DescriptorImpl extends Descriptor<Publisher> {
    	
		protected DescriptorImpl() {
			super(ChurnPublisher.class);
		}

		@Override
		public String getDisplayName() {
			return "Publish Churn report";
		}
		
    }

	public Descriptor<Publisher> getDescriptor() {
		return DESCRIPTOR;
	}
}
