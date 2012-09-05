package com.tencent.eclipse.nior.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.ui.console.MessageConsoleStream;

import com.tencent.eclipse.nior.console.ConsoleFactory;

public class StreamDrainer implements Runnable {
		private InputStream ins;

		public StreamDrainer(InputStream ins) {
			this.ins = ins;
		}

		@Override
		public void run() {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(ins));
				String line = null;
				MessageConsoleStream  printer =ConsoleFactory.console.newMessageStream();
				ConsoleFactory.showConsole();
				while ((line = reader.readLine()) != null) {
					printer.println(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
