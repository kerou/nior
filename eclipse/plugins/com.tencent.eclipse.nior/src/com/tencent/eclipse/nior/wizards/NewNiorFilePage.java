package com.tencent.eclipse.nior.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import com.tencent.eclipse.nior.Messages;

public class NewNiorFilePage extends WizardNewFileCreationPage {

	public NewNiorFilePage(IStructuredSelection selection) {
		super("new record page", selection);
		setTitle(Messages.NEW_NIOR_RECORD_PAGE_TITLE);
		setDescription(Messages.NEW_NIOR_RECORD_PAGE_DESCRIPTION);
		setFileExtension("nior");
	}
	

}
