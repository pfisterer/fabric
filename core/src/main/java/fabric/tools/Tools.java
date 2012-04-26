/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fabric.tools;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;

import org.slf4j.LoggerFactory;

public class Tools {
	 private static final org.slf4j.Logger log = LoggerFactory.getLogger(Tools.class);

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static Vector<String> getExternalHostIps() {
		HashSet<String> ips = new HashSet<String>();
		Vector<String> external = new Vector<String>();

		try {
			InetAddress i;
			NetworkInterface iface = null;

			for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces
					.hasMoreElements();) {
				iface = ifaces.nextElement();
				for (Enumeration<InetAddress> ifaceips = iface.getInetAddresses(); ifaceips.hasMoreElements();) {
					i = ifaceips.nextElement();
					if (i instanceof Inet4Address)
						ips.add(i.getHostAddress());
				}
			}

		} catch (Throwable t) {
			log.error("Unable to retrieve external ips: " + t, t);

			try {
				log.debug("Trying different lookup scheme");

				InetAddress li = InetAddress.getLocalHost();
				String strli = li.getHostName();
				InetAddress ia[] = InetAddress.getAllByName(strli);
				for (int i = 0; i < ia.length; i++)
					ips.add(ia[i].getHostAddress());
			} catch (Throwable t2) {
				log.error("Also unable to retrieve external ips: " + t2, t2);
			}

		}

		for (String ip : ips)
			if (isExternalIp(ip)) {
				log.debug("Found external ip: " + ip);
				external.add(ip);
			}

		return external;
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String getFirstLocalIp() {
		Vector<String> ips = getExternalHostIps();
		String localHost = "127.0.0.1";

		for (String ip : ips)
			if (!"127.0.0.1".equals(ip))
				localHost = new String(ip);

		return localHost;
	}

	//-------------------------------------------------------------------------
	/**
	 * 127.0.0.0		  -   127.255.255.255 (localhost)
	 * 10.0.0.0        -   10.255.255.255  (10/8 prefix)
	 * 172.16.0.0      -   172.31.255.255  (172.16/12 prefix)
	 * 192.168.0.0     -   192.168.255.255 (192.168/16 prefix)		
	 */
	public static boolean isExternalIp(String ip) {
		boolean external = true;

		if (ip == null)
			external = false;
		else if (ip.startsWith("127."))
			external = false;
		else if (ip.startsWith("10."))
			external = false;
		else if (ip.startsWith("192.168."))
			external = false;

		for (int i = 16; i <= 31; ++i)
			if (ip.startsWith("172." + i + "."))
				external = false;

		log.debug("IP " + ip + " is an " + (external ? "external" : "internal") + " address");
		return external;
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 * @param millis
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Throwable e) {
			/**/
		}
	}

	//	-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toString(Collection<?> l) {
		return toString(l, ", ");
	}

	//	-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toString(short[] l, int offset, int length) {
		LinkedList<Short> ll = new LinkedList<Short>();
		for (int i = offset; i < offset + length; ++i)
			ll.add(l[i]);

		return toString(ll, ", ");
	}

	//	-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toString(byte[] l) {
		if (l == null)
			return "";
		return toString(l, 0, l.length);
	}

	//	-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toString(byte[] l, int offset, int length) {
		if (l == null)
			return "";

		LinkedList<String> ll = new LinkedList<String>();
		for (int i = offset; i < offset + length; ++i)
			ll.add(toHexString(l[i]));

		return toString(ll, ", ");
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toString(Collection<?> l, String divider) {
		StringBuffer b = new StringBuffer();

		if (l == null)
			return "<null>";

		for (Object o : l) {
			String t = o != null ? o.toString() : "{null}";
			if (b.length() > 0)
				b.append(divider);

			b.append(t);
		}

		return b.toString().trim();
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toHexString(char tmp) {
		return toHexString((byte) tmp);
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toHexString(byte tmp) {
		return "0x" + Integer.toHexString(tmp & 0xFF);
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toHexString(byte[] tmp) {
		return toHexString(tmp, 0, tmp.length);
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toHexString(int i) {
		String tmp = "";
		if (i > 0xFF)
			tmp += toHexString((byte) (i >> 8 & 0xFF)) + " ";
		else
			tmp += "    ";

		tmp += toHexString((byte) (i & 0xFF));

		return tmp;
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toHexString(long i) {
		String tmp = "";

		if (i > 0xFF)
			tmp += toHexString((byte) (i >> 24 & 0xFF)) + ":";
		if (i > 0xFF)
			tmp += toHexString((byte) (i >> 16 & 0xFF)) + ":";
		if (i > 0xFF)
			tmp += toHexString((byte) (i >> 8 & 0xFF)) + ":";

		tmp += toHexString((byte) (i & 0xFF));

		return tmp;
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toHexString(byte[] tmp, int offset) {
		return toHexString(tmp, offset, tmp.length - offset);
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toHexString(byte[] tmp, int offset, int length) {
		StringBuffer s = new StringBuffer();
		for (int i = offset; i < offset + length; ++i) {
			if (s.length() > 0)
				s.append(' ');
			s.append("0x");
			s.append(Integer.toHexString(tmp[i] & 0xFF));
		}
		return s.toString();
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String toString(HashMap<?, ?> m) {
		if (m == null || m.size() == 0)
			return "";

		StringBuffer s = new StringBuffer();
		for (Object o : m.keySet()) {
			if (s.length() > 0)
				s.append(", ");
			s.append(o.toString());
			s.append("=");
			s.append(m.get(o));
		}
		return s.toString();
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static int toInteger(String s, int defaultValue) {
		int retVal = defaultValue;

		try {
			retVal = Integer.parseInt(s);
		} catch (Throwable t) {
			/**/
		}

		return retVal;
	}

	//-------------------------------------------------------------------------
	/**
	 * @return 
	 * 
	 */
	public static StackTraceElement[] getStackTrace() {
		Throwable ex = new Throwable();
		return ex.getStackTrace();
	}

	//-------------------------------------------------------------------------
	/**
	 * @return 
	 * 
	 */
	public static String toString(StackTraceElement[] trace) {
		StringBuffer s = new StringBuffer();
		for (StackTraceElement t : trace) {
			s.append(t.toString());
			s.append('\n');
		}
		return s.toString();
	}

	//-------------------------------------------------------------------------
	/**
	 * 
	 */
	public static String uppercaseFirst(String s) {
		if (s == null || s.length() <= 0)
			return "";

		String result = s.substring(0, 1).toUpperCase();

		if (s.length() > 1)
			result += s.substring(1);

		return result;
	}



}

