package net.trevize.galatee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.commons.lang.ArrayUtils;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SystemCommandHandler2.java - Nov 25, 2009
 */

public class SystemCommandHandler2 {

	public static final String ESCAPED_DOUBLE_QUOTE_CHARACTER = "\"";
	
	class SyncPipe implements Runnable {
		private final OutputStream ostrm_;
		private final InputStream istrm_;

		 public SyncPipe(InputStream istrm, OutputStream ostrm) {
			istrm_ = istrm;
			ostrm_ = ostrm;
		}

		public void run() {
			try {
				final byte[] buffer = new byte[1024];
				for (int length = 0; (length = istrm_.read(buffer)) != -1;) {
					ostrm_.write(buffer, 0, length);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class PidReader implements Runnable {
		private InputStream istrm;
		private SystemCommandHandler2 sch2;
		private int pid;

		public PidReader(SystemCommandHandler2 sch2, InputStream istrm) {
			this.sch2 = sch2;
			this.istrm = istrm;
		}

		public void run() {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						istrm));
				StringBuffer sb = new StringBuffer();
				sb.append(br.readLine());
				pid = Integer.parseInt(sb.toString());
				sch2.setPid(pid);
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private int pid;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		//System.out.println("SystemCommandHander2.setPid(): pid=" + pid);
		this.pid = pid;
	}

	/**************************************************************************/

	public SystemCommandHandler2() {
		this.pid = 0;
	}

	public void exec(String[] command) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("sh");
		} catch (IOException e) {
			e.printStackTrace();
		}

		SystemCommandHandler2.PidReader outputStream = new PidReader(this,
				process.getInputStream());

		new Thread(new SyncPipe(process.getErrorStream(), System.err)).start();
		//new Thread(new SyncPipe(process.getInputStream(), System.out)).start();
		new Thread(outputStream).start();

		Writer writer = null;
		try {
			writer = new OutputStreamWriter(process.getOutputStream(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String[] commands = (String[]) ArrayUtils.addAll(
				new String[] { "echo $$" }, command);
		try {
			for (String c : commands) {
				writer.write(c);
				writer.write("\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
