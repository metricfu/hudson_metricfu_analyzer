package hudson.plugins.rubyMetrics;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.Publisher;

public abstract class RubyMetricsPublisher extends Publisher {
	
	protected boolean fail(Build<?, ?> build, BuildListener listener, String message) {
    	listener.getLogger().println(message);
        build.setResult(Result.FAILURE);
    	return true;
    }
	
	public static class IndexFilenameFilter implements FilenameFilter {
		
		private String fileName = null;
		
		public IndexFilenameFilter() {}
		public IndexFilenameFilter(String fileName) {
			this.fileName = fileName;
		}
		
        public boolean accept(File dir, String name) {
        	if(fileName != null) {
        		return name.equalsIgnoreCase(this.fileName);
        	} else {
        		return name.equalsIgnoreCase("index.html");
        	}
        }
        
    }
	
	public static class IndexFileFilter implements FileFilter {
		public boolean accept(File pathname) {
			if(pathname.isFile()){
				return pathname.getName().equalsIgnoreCase("index.html");
			}
			return false;
		}
		
	}
	
}
