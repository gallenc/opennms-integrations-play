package stresstestprovisioning;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import inet.ipaddr.IPAddressSeqRange;

import org.junit.Test;

public class CreateRequisitionXMLTest {

	/**
	 * ip address add 172.20.0.200/24 dev eth0
	 * 
	 */

	@Test
	public void test() {

		String lowerAddress = "172.20.0.50";
		String upperAddress = "172.20.3.50";


		String requisitionFileName = "./target/stresstest1.xml";
		String ipscriptName = "./target/ipaddrscript.sh";
		BufferedWriter requisitionWriter = null;
		BufferedWriter ipscriptWriter =null;
		try {
			File requistionFile = new File(requisitionFileName);
			if (requistionFile.exists())
				requistionFile.delete();
			
			File ipscriptFile = new File(ipscriptName);
			if (ipscriptFile.exists())
				ipscriptFile.delete();
			

			ipscriptWriter = new BufferedWriter(new FileWriter(ipscriptFile, true));

			String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<model-import date-stamp=\"2022-03-03T16:51:00.968Z\" foreign-source=\"stresstest1\" last-import=\"2022-03-03T16:51:01.031Z\">\n"
					+ "" + "";
			System.out.print(str);

			requisitionWriter = new BufferedWriter(new FileWriter(requistionFile, true));
			requisitionWriter.append(str);
			
			String scriptStr="#!/bin/sh\n"
					+ "# script to provision additional ip addresses in docker image. Note     cap_add: - NET_ADMIN should be set\n";
			ipscriptWriter.append(scriptStr);

			// see https://stackoverflow.com/questions/69417370/loop-through-ip-addresses-in-range
			
			IPAddress lower = new IPAddressString(lowerAddress).toAddress();
			IPAddress upper = new IPAddressString(upperAddress).toAddress();
			IPAddressSeqRange range = lower.toSequentialRange(upper);
			int count = 0;
			for (IPAddress address : range.getIterable()) {
				count++;
				str = "<node foreign-id=\"Router" + count + "\" node-label=\"Router" + count
						+ "\"><interface ip-addr=\"" + address + "\" status=\"1\" snmp-primary=\"P\"/></node>\n";

				System.out.print(str);
				requisitionWriter.append(str);
				
				scriptStr= "ip address add "+address
						+ "/24 dev eth0\n";
				ipscriptWriter.append(scriptStr);
			}

			// finish requisition
			str = "</model-import>\n";
			System.out.print(str);
			requisitionWriter.append(str);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

			if (requisitionWriter != null)
				try {
					requisitionWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (ipscriptWriter != null)
				try {
					ipscriptWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
		}
	}

	public void generate(String lowerAddress, String upperAddress) {

	}

}
