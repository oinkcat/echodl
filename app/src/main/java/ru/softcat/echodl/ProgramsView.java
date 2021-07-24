package ru.softcat.echodl;
import java.util.List;

public interface ProgramsView
{
	void setProgramList(List<Program> programs);
	
	void showProgress(String actionName);
	
	void hideProgress();
	
	void showError(String errorMessage);
}
