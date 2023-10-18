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

public class CreateRequisitionJSONTest {

	/**
	 * ip address add 172.20.0.200/24 dev eth0
	 * 
	 */

	@Test
	public void test() {

		String lowerAddress = "172.20.0.50";
		String upperAddress = "172.20.0.254";


		String requisitionFileName = "./target/stresstest1.json";
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

			String str = "{\n"
					   + " \"foreign-source\": \"testrequisition\",\n"
					   + " \"node\": [\n";
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
			Iterator<? extends IPAddress> addressiterator = range.iterator();
			while(addressiterator.hasNext()) {
				IPAddress address = addressiterator.next();
				count++;
				    str = "   {\n"
						+ "    \"location\": \"default\",\n"
						+ "    \"foreign-id\": \"router_"+count+ "\",\n"
						+ "    \"node-label\": \"router_"+count+ "\",\n"
						+ "    \"interface\": [\n"
						+ "        {\n"
						+ "            \"monitored-service\": [\n"
						+ "                {\n"
						+ "                    \"category\": [],\n"
						+ "                    \"meta-data\": [],\n"
						+ "                    \"service-name\": \"ICMP\"\n"
						+ "                },\n"
						+ "                {\n"
						+ "                    \"category\": [],\n"
						+ "                    \"meta-data\": [],\n"
						+ "                    \"service-name\": \"SNMP\"\n"
						+ "                }\n"
						+ "            ],\n"
						+ "            \"category\": [],\n"
						+ "            \"meta-data\": [],\n"
						+ "            \"descr\": null,\n"
						+ "            \"ip-addr\": \""+address	+ "\",\n"
						+ "            \"managed\": null,\n"
						+ "            \"status\": 1,\n"
						+ "            \"snmp-primary\": \"P\"\n"
						+ "        }\n"
						+ "    ],\n"
						+ "    \"category\": [\n"
						+ "        {\n"
						+ "            \"name\": \"CUST\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"Production\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"SWITCH\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"ONEACCESS\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"Model_xxx\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"DGC\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"CUSTOMERID:xxx\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"OFS\"\n"
						+ "        }\n"
						+ "    ],\n"
						+ "    \"asset\": [\n"
						+ "        {\n"
						+ "            \"name\": \"assetNumber\",\n"
						+ "            \"value\": \"xxx\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"building\",\n"
						+ "            \"value\": \"xxx\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"latitude\",\n"
						+ "            \"value\": \"50.9105\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"longitude\",\n"
						+ "            \"value\": \"1.4049\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"manufacturer\",\n"
						+ "            \"value\": \"xxx\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"modelNumber\",\n"
						+ "            \"value\": \"xxx\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"category\",\n"
						+ "            \"value\": \"SWITCH\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"displayCategory\",\n"
						+ "            \"value\": \"CUST\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"pollerCategory\",\n"
						+ "            \"value\": \"CUST\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"thresholdCategory\",\n"
						+ "            \"value\": \"CUST\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"dateInstalled\",\n"
						+ "            \"value\": \"07/09/2020\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"lastModifiedDate\",\n"
						+ "            \"value\": \"10/16/2023\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"lastModifiedBy\",\n"
						+ "            \"value\": \"xxx\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"comment\",\n"
						+ "            \"value\": \"xxx, (xxx)\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"description\",\n"
						+ "            \"value\": \"CUST\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"address1\",\n"
						+ "            \"value\": \"xxx\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"address2\",\n"
						+ "            \"value\": \"xxx\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"zip\",\n"
						+ "            \"value\": \"xxx\"\n"
						+ "        },\n"
						+ "        {\n"
						+ "            \"name\": \"city\",\n"
						+ "            \"value\": \"xxx\"\n"
						+ "        }\n"
						+ "    ],\n"
						+ "    \"meta-data\": [],\n"
						+ "    \"building\": \"xxx\",\n"
						+ "    \"city\": null,\n"
						+ "    \"parent-foreign-source\": null,\n"
						+ "    \"parent-foreign-id\": null,\n"
						+ "    \"parent-node-label\": null\n"
						+ "   }";
				if (addressiterator.hasNext()) {
					str = str+ ",";
				}
					str = str+ "\n";

				System.out.print(str);
				requisitionWriter.append(str);
				
				scriptStr= "ip address add "+address
						+ "/24 dev eth0\n";
				ipscriptWriter.append(scriptStr);
			}

			// finish requisition
			str =   "   ]\n"
					+ "}\n";
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
