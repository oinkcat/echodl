package ru.softcat.echodl;
import java.util.*;
import java.util.regex.*;

public class ProgramListParser
{
	private final String PATTERN = "data-(title|q[0-9])=\"([^\"]+)\"";
	
	private List<Program> programs;
	
	private String tempProgramTitle;
	
	private Program currentProgram;
	
	public List<Program> parse(String pageContent) {
		Pattern regex = Pattern.compile(PATTERN, Pattern.MULTILINE);
		Matcher match = regex.matcher(pageContent);
		
		programs = new ArrayList<>();
		while(match.find()) {
			parseMatchData(match);
		}
		
		return programs;
	}
	
	private void parseMatchData(MatchResult match) {
		String dataValue = match.group(2);
		
		if(match.group(1).equals("title")) {
			if(currentProgram != null) {
				currentProgram = null;
			}
			
			tempProgramTitle = dataValue;
		} else if(currentProgram != null) {
			currentProgram.addLink(dataValue);
		} else {
			currentProgram = new Program(tempProgramTitle);
			currentProgram.addLink(dataValue);
			programs.add(currentProgram);
		}
	}
}
