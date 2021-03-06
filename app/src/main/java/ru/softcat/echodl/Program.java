package ru.softcat.echodl;
import java.util.*;

public class Program
{
	private final int DEFAULT_LINK_IDX = 1;
	private final int MAX_LINKS = 2;
	
	private final char NAME_DELIMITER = '—';
	
	private String name;
	
	private String details;
	
	private List<String> links;
	
	public Program(String name) {
		links = new ArrayList<>();
		parseProgramName(name);
	}
	
	private void parseProgramName(String fullName) {
		int nameDelimPos = fullName.lastIndexOf(NAME_DELIMITER);
		
		if(nameDelimPos > -1) {
			name = fullName.substring(nameDelimPos + 2);
			details = fullName.substring(0, nameDelimPos - 1);
		} else {
			name = "???";
			details = fullName;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getDetails() {
		return details;
	}
	
	public String getLink(int index) {
		if(isDone() && (index >= 0) && (links.size() > index)) {
			return links.get(index);
		} else {
			return null;
		}
	}
	
	public Boolean isDone() {
		return name != null && links.size() >= MAX_LINKS;
	}
	
	public String getDefaultLink() {
		return getLink(DEFAULT_LINK_IDX);
	}
	
	public void addLink(String link) {
		links.add(link);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
