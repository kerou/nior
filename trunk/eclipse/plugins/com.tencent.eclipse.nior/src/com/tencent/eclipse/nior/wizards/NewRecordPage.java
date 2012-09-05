package com.tencent.eclipse.nior.wizards;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDebugBridgeChangeListener;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;
import com.tencent.eclipse.nior.Activator;
import com.tencent.eclipse.nior.Messages;
import com.tencent.eclipse.nior.rr.NiorRecorder;
import com.tencent.eclipse.nior.rr.Recordable;
import com.tencent.eclipse.nior.rr.nativelib.NativeRecorder;

/**
 * @author rainliwang
 * 
 */
public class NewRecordPage extends WizardPage implements IDebugBridgeChangeListener,IDeviceChangeListener {

	private IDevice selectedDevice;

	private String recordFileName;

	private Combo deviceSelectionList;

	private List<IDevice> devices=new ArrayList<IDevice>();

	private Button recordBtn;;
	
	protected NewRecordPage() {
		super("Nior");
		setTitle("Nior");
		//device choose list
		AndroidDebugBridge.addDebugBridgeChangeListener(this);
		AndroidDebugBridge.addDeviceChangeListener(this);
	}
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		
		//table content
		Label label = new Label(container, SWT.NULL);
		label.setText(Messages.LABEL_CHOOSE_DEVICE);
		deviceSelectionList = new Combo(container, SWT.SINGLE | SWT.BORDER|SWT.READ_ONLY);
		deviceSelectionList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				updateSelection();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				super.widgetDefaultSelected(e);
				widgetSelected(e);
			}
		});
		fillDevicesCombo(deviceSelectionList);
		
		recordBtn = new Button(container,  SWT.NULL);
		recordBtn.setText(Messages.RECORD);
		recordBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleRecord();
			}
		});
		
		Text notice = new Text(container, SWT.WRAP |SWT.MULTI|SWT.READ_ONLY);
		notice.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 3, 1));
		notice.setText(Messages.NOTICE);
		setControl(container);
	}
	protected void handleRecord() {
		if (selectedDevice == null) {
			// 提示用户选择设备
			updataErrorMessage(Messages.ALERT_NO_DEVICE_CHOOSEN);
			return;
		}
		if(Messages.RECORD.equals(recordBtn.getText())){
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			recordFileName = simpleDateFormat.format(date)+ ".rcd";
			final NiorRecorder recordable = new NativeRecorder(selectedDevice,
					recordFileName);
			RecordThread recordThread = new RecordThread(recordable,"recordThread");
			recordBtn.setText(Messages.STOP);
			recordThread.start();
			setErrorMessage(null);
			setMessage(Messages.RECORDING);
			setPageComplete(false);
			return;
		}else if(Messages.STOP.equals(recordBtn.getText())){
			NativeRecorder recorder = new NativeRecorder(selectedDevice,
					recordFileName);
			if(recorder.stop()){
				recordBtn.setText(Messages.RECORD);
				setMessage(Messages.RECORD_FINISHED);
				setPageComplete(true);
			}
		}
		
	}
	class RecordThread extends Thread{
		public RecordThread(final Recordable recordable,String name) {
			super(new Runnable() {
				@Override
				public void run() {
					recordable.record();
				}
			}, name);
		}
		
	}
	
	protected void updataErrorMessage(String msg) {
		setErrorMessage(msg);
		setPageComplete(msg==null);
	}
	protected void updataInfoMessage(String msg) {
		setErrorMessage(null);
		setMessage(msg);
		setPageComplete(msg==null);
	}
	public IDevice getSelectedDevice() {
		return selectedDevice;
	}

	public void setSelectedDevice(IDevice selectedDevice) {
		this.selectedDevice = selectedDevice;
	}

	public String getRecordFileName() {
		return recordFileName;
	}



	public void setRecordFileName(String recordFileName) {
		this.recordFileName = recordFileName;
	}
	@Override
	public void deviceConnected(IDevice device) {
		devices.add(device);
		final String serialNumber = device.getSerialNumber();
		Display.getDefault().asyncExec(new  Runnable() {
			@Override
			public void run() {
				if(!deviceSelectionList.isDisposed()){
					deviceSelectionList.add(serialNumber,devices.size()-1);				
					if(devices.size()==1){
						deviceSelectionList.select(0);
						updateSelection();
					}
				}
			}
		});
		
	}
	@Override
	public void deviceDisconnected(IDevice device) {
		Activator.logInfo("device disconnected. serialNo:"+device.getSerialNumber());
		devices.clear();
		AndroidDebugBridge bridge = AndroidDebugBridge.getBridge();
		refreshDeviceCombo(bridge);
	}
	@Override
	public void deviceChanged(IDevice device, int changeMask) {
		// TODO Auto-generated method stub
		System.out.println(1);
	}
	@Override
	public void bridgeChanged(AndroidDebugBridge bridge) {
		refreshDeviceCombo(bridge);
	}
	private void refreshDeviceCombo(AndroidDebugBridge bridge) {
		Collections.addAll(devices, bridge.getDevices());
		if(devices.size()==0){
			Collections.addAll(devices, bridge.getDevices());
		}
		if(deviceSelectionList==null){
			return;
		}
		Display.getDefault().asyncExec(new  Runnable() {
			@Override
			public void run() {
				fillDevicesCombo(deviceSelectionList);
			}
		});
	}
	
	/**
	 * fill all the selectable devices for a combo
	 * @param devicesCombo
	 */
	private void fillDevicesCombo(Combo devicesCombo) {
		if (!devicesCombo.isDisposed()) {
			devicesCombo.removeAll();
			int size = devices.size();
			if(size==0){
				updataErrorMessage(Messages.ALERT_DEVICE_NOT_EXISTS);
				return;
			}
			for (int i = 0; i < size; i++) {
				devicesCombo.add(devices.get(i).getSerialNumber(), i);
			}
			if (size >= 1) {
				devicesCombo.select(0);
				updateSelection();
			}
		}
	}
	private void updateSelection() {
		selectedDevice=devices.get(deviceSelectionList.getSelectionIndex());
		updataInfoMessage(Messages.INFO_CHOOSEN_DEVICE+selectedDevice.getSerialNumber());
	}
}
