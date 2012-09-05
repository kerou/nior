package com.tencent.eclipse.nior.rr.operations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import com.tencent.eclipse.nior.Activator;

/**
 * @author kaipi
 *
 */
public class MemCpuUtils {

	public static boolean booStarted = false;
	public static String strNativeSize = null;
	public static String strDalvikSize = null;
	public static String strNativeAllocatedSize = null;
	public static String strDalvikAllocatedSize = null;
	public static String strNativePrivdirty = null;
	public static String strDalvikPrivdirty = null;
	public static String strNativePSS = null;
	public static String strDalvikPSS = null;
	public static String strTotalPSS = null;
	private static String strCPU = null;

	public static File deleteAndCreateFile(String strPath) {
		File fileResult = null;

		try {
			fileResult = new File(strPath);

			fileResult.deleteOnExit(); // 删除过往记录

			fileResult.createNewFile();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileResult;
	}

	public static void writeLog(String resultFile, List<String> alResult, boolean isMem, String strPackageName) {
		if (resultFile == null ) {
			Activator.logError(null, "one of parameters of writelog is wrong");
			Activator.logError(null, "resultFile is : "+resultFile);
			Activator.logError(null, "output is : "+alResult);
			return;
		}
		
		// Write All Lines
		if (!booStarted) {
			strNativeSize = "NativeSize:";
			strDalvikSize = "DalvikSize:";
			strNativeAllocatedSize = "NativeAllocatedSize:";
			strDalvikAllocatedSize = "DalvikAllocatedSize:";
			strNativePrivdirty = "NativePrivdirty:";
			strDalvikPrivdirty = "DalvikPrivdirty:";
			strNativePSS = "NativePSS:";
			strDalvikPSS = "DalvikPSS:";
			strTotalPSS = "TotalPSS:";
			strCPU = "CPU:";
			booStarted = true;
		}

		FileWriter fwResult = null;
		BufferedWriter bwResult = null;
		try {
			// get all values:
			String nValue;
			String strLine;
			
			fwResult = new FileWriter(resultFile);
			bwResult = new BufferedWriter(fwResult);
			int nStartLocation = 0;

			if (isMem) {
				if( alResult.size() < 11 )
				{
					Activator.logError(null, String.format("line number:%d", alResult.size()));
					for( int i = 0; i < alResult.size(); i++ )
						Activator.logError(null, alResult.get(i));
					
					return;
				}
				
				String strResult = alResult.get(5); // 获取第六行数值即size值
				nStartLocation = strResult.indexOf(':') + 1;
				strLine = strResult.substring(nStartLocation + 0 * 9, nStartLocation + 0 * 9 + 9); // 获取native
																			// size
				nValue = strLine.replaceAll(" ", "");
				strNativeSize += nValue + ",";
				strLine = strResult.substring(nStartLocation + 1 * 9, nStartLocation + 1 * 9 + 9); // 获取dalvik
																			// size
				nValue = strLine.replaceAll(" ", "");
				strDalvikSize += nValue + ","; // 获取dalvik size

				strResult = alResult.get(6); // 获取第七行数值即Allocated值
				nStartLocation = strResult.indexOf(':') + 1;
				strLine = strResult.substring(nStartLocation + 0 * 9, nStartLocation + 0 * 9 + 9); // 获取native
																			// allocated
				nValue = strLine.replaceAll(" ", "");
				strNativeAllocatedSize += nValue + ",";
				strLine = strResult.substring(nStartLocation + 1 * 9, nStartLocation + 1 * 9 + 9); // 获取dalvik
																			// allocated
				nValue = strLine.replaceAll(" ", "");
				strDalvikAllocatedSize += nValue + ","; // 获取dalvik allocated

				strResult = alResult.get(8); // 获取第九行数值即Allocated值
				nStartLocation = strResult.indexOf(':') + 1;
				strLine = strResult.substring(nStartLocation + 0 * 9, nStartLocation + 0 * 9 + 9); // 获取native
																			// pss
				nValue = strLine.replaceAll(" ", "");
				strNativePSS += nValue + ",";
				strLine = strResult.substring(nStartLocation + 1 * 9, nStartLocation + 1 * 9 + 9); // 获取dalvik
																			// pss
				nValue = strLine.replaceAll(" ", "");
				strDalvikPSS += nValue + ","; // 获取dalvik PSS
				strLine = strResult.substring(nStartLocation + 3 * 9, nStartLocation + 3 * 9 + 9); // 获取dalvik
																			// pss
				nValue = strLine.replaceAll(" ", "");
				strTotalPSS += nValue + ","; // 获取total PSS

				strResult = alResult.get(10); // 获取第六行数值即Allocated值
				nStartLocation = strResult.indexOf(':') + 1;
				strLine = strResult.substring(nStartLocation + 0 * 9, nStartLocation + 0 * 9 + 9); // 获取native
																			// private
																			// dirty
				nValue = strLine.replaceAll(" ", "");
				strNativePrivdirty += nValue + ",";
				strLine = strResult.substring(nStartLocation + 1 * 9, nStartLocation + 1 * 9 + 9); // 获取dalvik
																			// private
																			// dirty
				nValue = strLine.replaceAll(" ", "");
				strDalvikPrivdirty += nValue + ",";

				bwResult.write(strNativeSize + "\n");
				bwResult.write(strDalvikSize + "\n");
				bwResult.write(strNativeAllocatedSize + "\n");
				bwResult.write(strDalvikAllocatedSize + "\n");
				bwResult.write(strNativePrivdirty + "\n");
				bwResult.write(strDalvikPrivdirty + "\n");
				bwResult.write(strNativePSS + "\n");
				bwResult.write(strDalvikPSS + "\n");
				bwResult.write(strTotalPSS + "\n");
				bwResult.flush();
			} else {
				
				String strLog;
				int nLocation;
				int nPercentLocation;	
				String[] strCpus = null;
				for( int i = 0; i < alResult.size(); i++ )
				{
					strLog = alResult.get(i);
					
					nPercentLocation = strLog.indexOf(":push");
					if( nPercentLocation != -1 )
					{
						continue;
					}
					
					nLocation = strLog.indexOf('%');
					if( nLocation == -1 )   //如果不包含%，继续下一循环
					{
						continue;
					}
					else
					{		
						Activator.logInfo("strLog:" + strLog + "\n");
						nLocation = strLog.indexOf('\t');//如果分隔符是制表符，则使用制表符作为分隔符将strLog分开
						if( nLocation != -1 )
						{
							strCpus = strLog.split("\t");
						}
						
						nLocation = strLog.indexOf(' ');//如果分隔符是空格，则使用空格作为分隔符将strLog分开
						if( nLocation != -1 )
						{
							strCpus = strLog.split(" ");  
						}
						
						if( nLocation == -1 )
						{
							continue;  //无空格符，无制表符，继续循环
						}
											
						Activator.logInfo("strCpus[0]:" + strCpus[0] + "strCpus[1]:" + strCpus[1] + "strCpus[2]:" + strCpus[2] + "\n");
						strCPU += strCpus[2] + ",";
						Activator.logInfo("strCPU:" + strCPU + "\n");
						break;
					
					}
				}

				bwResult.write(strCPU + "\n");
				bwResult.flush();
			}

			bwResult.close();
			fwResult.close();
		} catch (Exception e) {
			Activator.logError(e, "dump mem or cpu info failed");
		}
	}

}
