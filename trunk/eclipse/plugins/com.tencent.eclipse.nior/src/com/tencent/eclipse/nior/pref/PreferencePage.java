package com.tencent.eclipse.nior.pref;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tencent.eclipse.nior.Activator;
import com.tencent.eclipse.nior.Messages;

public class PreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	private NiorPrefFieldEditor screenAnalyzerDirField;
	private NiorPrefFieldEditor packetsAnalyzerDirField;
	private NiorPrefFieldEditor memoryAnalyzerDirField;
	private NiorPrefFieldEditor cpuAnalyzerDirField;
	public PreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.TITLE_PREFERENCE_PAGE);
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createFieldEditors() {

		screenAnalyzerDirField = new NiorPrefFieldEditor(
				Activator.PREF_SCREEN_ANALYZER_DIR,
				Messages.LABEL_SCREEN_ANALYZER_LOCATION, getFieldEditorParent());
		packetsAnalyzerDirField=new NiorPrefFieldEditor(
				Activator.PREF_PACKETS_ANALYZER_DIR,
				Messages.LABEL_PACKETS_ANALYZER_LOCATION, getFieldEditorParent());
		memoryAnalyzerDirField=new NiorPrefFieldEditor(
				Activator.PREF_MEMEORY_ANALYZER_DIR,
				Messages.LABEL_MEMEORY_ANALYZER_LOCATION, getFieldEditorParent());
		addField(screenAnalyzerDirField);
		addField(packetsAnalyzerDirField);
		addField(memoryAnalyzerDirField);
//		cpuAnalyzerDirField=new NiorPrefFieldEditor(
//				Activator.PREF_CPU_ANALYZER_DIR,
//				Messages.LABEL_CPU_ANALYZER_LOCATION, getFieldEditorParent());
//		addField(cpuAnalyzerDirField);

	}

	@Override
	public void dispose() {
		super.dispose();

		if (screenAnalyzerDirField != null) {
			screenAnalyzerDirField.dispose();
			screenAnalyzerDirField = null;
		}
		if (packetsAnalyzerDirField != null) {
			packetsAnalyzerDirField.dispose();
			packetsAnalyzerDirField = null;
		}
		if (memoryAnalyzerDirField != null) {
			memoryAnalyzerDirField.dispose();
			memoryAnalyzerDirField = null;
		}
		if (cpuAnalyzerDirField != null) {
			cpuAnalyzerDirField.dispose();
			cpuAnalyzerDirField = null;
		}
	}

	private static class NiorPrefFieldEditor extends DirectoryFieldEditor {
		public NiorPrefFieldEditor(String name, String labelText,
				Composite parent) {
			super(name, labelText, parent);
			setEmptyStringAllowed(false);
		}
	}

}
