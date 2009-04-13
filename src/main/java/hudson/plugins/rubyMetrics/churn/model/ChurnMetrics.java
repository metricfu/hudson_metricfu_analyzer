package hudson.plugins.rubyMetrics.churn.model;

import java.util.Arrays;
import java.util.Comparator;

public enum ChurnMetrics {
	LINES, LOC, CLASSES, METHODS, M_C, LOC_M;
	
	public String prettyPrint() {
		switch (this) {			
			case LOC:
				return this.toString();
			case M_C:
				return slashedPrint();
			case LOC_M:
				return slashedPrint();				
		}
		return defaultPrettyPrint();
	}
	
	private String defaultPrettyPrint() {
		String prettyString = this.toString().toLowerCase();
		return prettyString.substring(0, 1).toUpperCase() + prettyString.substring(1);
	}
	
	private String slashedPrint() {
		return this.toString().replaceAll("_", "/");
	}
	
	public static ChurnMetrics toChurnMetrics(String name) {
		try {
			return ChurnMetrics.valueOf(name.toUpperCase().replaceAll("/", "_"));
		} catch (Exception e) {
			return null;
		}
	}
	
	public int getOrder() {
		return Arrays.asList(ChurnMetrics.values()).indexOf(this);
	}
	
	public static class COMPARATOR implements Comparator<ChurnMetrics> {
		public int compare(ChurnMetrics o1, ChurnMetrics o2) {
			return new Integer(o1.getOrder()).compareTo(new Integer(o2.getOrder()));
		}
		
	}
}
