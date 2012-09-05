package com.tencent.eclipse.nior.util;

import java.io.IOException;
import java.io.OutputStream;

public class CMDUtil {
	public static void runCMDLine(OutputStream out, String cmd)
			throws IOException {
		cmd += "\n";
		out.write(cmd.getBytes());
		out.flush();
	}
}
