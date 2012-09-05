package com.tencent.eclipse.nior.actions;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.tencent.eclipse.nior.Activator;
import com.tencent.eclipse.nior.Messages;
import com.tencent.eclipse.nior.util.StringUtil;

public class ReplayDialog extends TitleAreaDialog {
	private ReplayInfo replayInfo;

	/**
	 * replayTimesText widget.
	 */
	private Text replayTimesText;
	private String replayTimesValue;

	private Button screenAnalysisBtn;
	private boolean screenAnalysis;

	private Button packetsAnalysisBtn;
	private boolean packetsAnalysis;

	private Button memoryAnalysisBtn;
	private boolean memoryAnalysis;
	

	private Button cpuAnalysisBtn;
	private boolean cpuAnalysis;

	private Label memCpuPackageLabel;

	private Text memCpuPackageText;
	private String memCpuPkgName;

	private Button okButton;

	protected ReplayDialog(Shell parentShell) {
		super(parentShell);
	}

	public ReplayInfo getReplayInfo() {
		return replayInfo;
	}

	public void setReplayInfo(ReplayInfo replayInfo) {
		this.replayInfo = replayInfo;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(Messages.TITLE_REPLAY_INFO);
		Composite container = (Composite) super.createDialogArea(parent);
		Composite composite = new Composite(container, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setFont(container.getFont());

		// create message
		Label label = new Label(composite, SWT.WRAP);
		label.setText(Messages.INFO_ENTER_REPLAY_TIMES);
		GridData data = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		label.setLayoutData(data);
		label.setFont(parent.getFont());
		replayTimesText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		replayTimesText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		replayTimesText.setText("1");
		replayTimesValue="1";
		replayTimesText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				replayTimesValue = replayTimesText.getText();
				if (!StringUtil.isNumeric(replayTimesValue)) {
					setErrorMessage(Messages.ALERT_NUMBER_REQUIRED);
				} else {
					setErrorMessage(null);
				}
			}
		});

		screenAnalysisBtn = new Button(composite, SWT.CHECK);
		screenAnalysisBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 2, 1));
		screenAnalysisBtn.setText(Messages.LABEL_SCREEN_ANALYSIS);
		screenAnalysisBtn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 校验是否设置了截屏工具目录以及设置replayinfo中的值
				screenAnalysis=screenAnalysisBtn.getSelection();
				if (screenAnalysis) {
					String screenAnalysisDir = Activator.getDefault().getPreferenceStore().getString(Activator.PREF_SCREEN_ANALYZER_DIR);
					if (StringUtil.isBlank(screenAnalysisDir)) {
						setErrorMessage(Messages.ALERT_SCREEN_ANALYZER_DIR_REQUIRED);
					}
				} else {
					setErrorMessage(null);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		packetsAnalysisBtn = new Button(composite, SWT.CHECK);
		packetsAnalysisBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 2, 1));
		packetsAnalysisBtn.setText(Messages.LABEL_PACKETS_ANALYSIS);
		packetsAnalysisBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// 校验是否设置了tcpdump数据存放目录以及设置replayinfo中的值
				packetsAnalysis = packetsAnalysisBtn.getSelection();
				if (packetsAnalysis) {
					String packetsAnalysisDir = Activator.getDefault()
							.getPreferenceStore()
							.getString(Activator.PREF_PACKETS_ANALYZER_DIR);
					if (StringUtil.isBlank(packetsAnalysisDir)) {
						setErrorMessage(Messages.ALERT_PACKETS_ANALYZER_DIR_REQUIRED);
					}
				} else {
					setErrorMessage(null);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		memoryAnalysisBtn = new Button(composite, SWT.CHECK);
		memoryAnalysisBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 2, 1));
		memoryAnalysisBtn.setText(Messages.LABEL_MEMORY_ANALYSIS);
		memoryAnalysisBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				memoryAnalysis = memoryAnalysisBtn.getSelection();
				if (memoryAnalysis) {
					String memoryAnalysisDir = Activator.getDefault()
							.getPreferenceStore()
							.getString(Activator.PREF_MEMEORY_ANALYZER_DIR);
					if (StringUtil.isBlank(memoryAnalysisDir)) {
						setErrorMessage(Messages.ALERT_MEMEORY_ANALYZER_DIR_REQUIRED);
						return;
					}
				} else {
					setErrorMessage(null);
				}
				boolean isSelected = memoryAnalysis;
				setUpMemCpuPackage(isSelected);

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		
		//cpu analysis currently not supportted
		cpuAnalysisBtn = new Button(composite, SWT.CHECK);
		cpuAnalysisBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 2, 1));
		cpuAnalysisBtn.setText(Messages.LABEL_CPU_ANALYSIS);
		cpuAnalysisBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				cpuAnalysis = cpuAnalysisBtn.getSelection();
				if (cpuAnalysis) {
					String cpuAnalysisDir = Activator.getDefault()
							.getPreferenceStore()
							.getString(Activator.PREF_CPU_ANALYZER_DIR);
					if (StringUtil.isBlank(cpuAnalysisDir)) {
						setErrorMessage(Messages.ALERT_CPU_ANALYZER_DIR_REQUIRED);
						return;
					}
				} else {
					setErrorMessage(null);
				}
				boolean isSelected = cpuAnalysis;
				setUpMemCpuPackage(isSelected);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		memCpuPackageLabel = new Label(composite, SWT.NONE);
		memCpuPackageLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		memCpuPackageLabel.setText(Messages.LABEL_MEMEORY_PKGNAME);

		memCpuPackageText = new Text(composite, SWT.BORDER);
		memCpuPackageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		setUpMemCpuPackage(false);
		memCpuPackageText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (StringUtil.isBlank(memCpuPackageText.getText())) {
					setErrorMessage(Messages.ALERT_PKGNAME_REQUIRED);
				} else {
					setErrorMessage(null);
					memCpuPkgName=memCpuPackageText.getText().trim();
				}
			}
		});

		return composite;
	}

	private void setUpMemCpuPackage(boolean enabled) {
		memCpuPackageLabel.setEnabled(enabled);
		memCpuPackageText.setEnabled(enabled);
		memCpuPackageText.forceFocus();
		if (enabled){
			if(StringUtil.isBlank(memCpuPackageText.getText())) {
				setErrorMessage(Messages.ALERT_PKGNAME_REQUIRED);
			} else {
				setErrorMessage(null);
			}
		}
			
	}

	@Override
	public void setErrorMessage(String newErrorMessage) {
		super.setErrorMessage(newErrorMessage);
		if (null != newErrorMessage) {
			okButton.setEnabled(false);
		}else{
			okButton.setEnabled(true);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		super.okPressed();
		// set up replayinfo
		replayInfo = new ReplayInfo();
		replayInfo.setReplayTimes(Integer.parseInt(replayTimesValue));
		replayInfo.setCpuInfoAnalysis(cpuAnalysis);
		replayInfo.setMemoryInfoAnalysis(memoryAnalysis);
		replayInfo.setMemPkgName(memCpuPkgName);
		replayInfo.setPacketsAnalysis(packetsAnalysis);
		replayInfo.setScreenAnalysis(screenAnalysis);
	}

}
