package hudson.plugins.rubyMetrics.churn;

import hudson.plugins.rubyMetrics.churn.model.ChurnResults;
import hudson.util.IOException2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class ChurnParser {
	
	private static final String TABLE_TAG_NAME = "table";
	private static final String TR_TAG_NAME = "tr";

	
    public ChurnResults parse(File file) throws IOException {        
        return parse(new FileInputStream(file));
    }
    
    public ChurnResults parse(InputStream input) throws IOException {
    	try {            
    		ChurnResults result = new ChurnResults();
    		
        	Parser parser = initParser(getHtml(input));        	
        	TableTag report = getReportTable(parser);
        	
        	for(int i=0;i<report.getRowCount();i++) {
        		//row at 0 is the header row, so we have to get the row greater than 0
        		if(i>0){
        			TableRow totalRow = report.getRow(i);
        			result.addResult(getColumnByNumber(totalRow, TR_TAG_NAME, 0), getColumnByNumber(totalRow, TR_TAG_NAME, 1));
        		}
        	}        	
        	
        	return result;
        } catch (Exception e) {
            throw new IOException2("cannot parse churn report file", e);
        }        
    }
    
    private String getHtml(InputStream input) throws IOException {
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	
    	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    	String line;
    	while ((line = reader.readLine()) != null) {
    		pw.write(line);
    	}
    	return sw.toString();
    }
    
    private Parser initParser(String html) throws ParserException {
    	final Parser htmlParser = new Parser();
    	htmlParser.setInputHTML(html);
    	return htmlParser;
    }
    
    private TableTag getReportTable(Parser htmlParser) throws ParserException {
    	NodeList reportNode = htmlParser.extractAllNodesThatMatch(new TagNameFilter(TABLE_TAG_NAME));
    	if (!(reportNode != null && reportNode.size() > 0)) {
    		throw new ParserException("cannot parse churn report file, report element wasn't found");
    	}
    	return (TableTag) reportNode.elements().nextNode();
    }
    
    private String getColumnByNumber(TableRow row, String tagName, int number) {
    	NodeList nodeList = new NodeList();
	    row.collectInto(nodeList, new TagNameFilter(tagName));
	    
	    String text = null;
	    
	    if (nodeList.size() > 0) {
	    	NodeList columns = nodeList.elementAt(0).getChildren();
	    	Node n = columns.elementAt(number);
	    	text = n.getFirstChild().getText();
	    }
	    return text;
    }
    
}
