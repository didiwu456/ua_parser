package com.fi.athena.ua_parser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class App {
	private static final String INPUT_FILE = "./user_agent_list.csv";
	private static final String OUTPUT_FILE = "./ua_parser_output.csv";

	private static final String DEVICE_CLASS = "DeviceClass";
	private static final String OS_NAME_VERSION = "OperatingSystemNameVersion";
	private static final String AGENT_NAME_VERSION_MAJOR = "AgentNameVersionMajor";

	private static UserAgentAnalyzer uaa = (UserAgentAnalyzer) UserAgentAnalyzer
			.newBuilder()
			.withField(DEVICE_CLASS)
			.withField(OS_NAME_VERSION)
			.withField(AGENT_NAME_VERSION_MAJOR)
			.build();

	public static void main(String[] args) throws IOException {
		PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));

		StringBuffer csvHeader = new StringBuffer("");
		StringBuffer csvData = new StringBuffer("");
		csvHeader.append(
				"User Agent,DeviceClass,OperatingSystemNameVersion,AgentNameVersionMajor,Request Count,IP Count\n");

		Scanner scanner = new Scanner(new File(INPUT_FILE));
		scanner.useDelimiter("\n");

		// write header
		pw.write(csvHeader.toString());

		scanner.nextLine(); // Skip the headers
		int i = 1;
		while (scanner.hasNext()) {
			String[] line = scanner.next().split("\"\"\"");
			System.out.println(i++ + ": " + line[1]);
			analyzeUserAgent(line, csvData);
		}
		pw.write(csvData.toString());
		pw.close();
		scanner.close();
	}

	private static void analyzeUserAgent(String[] userAgent, StringBuffer csvData) throws IOException {
		UserAgent agent = uaa.parse(userAgent[1]);

		String[] counts = userAgent[2].split(",");

		System.out.println("	UserAgent [deviceClass=" + agent.getValue(DEVICE_CLASS)
				+ ", operatingSystemNameVersion=" + agent.getValue(OS_NAME_VERSION)
				+ ", agentNameVersionMajor=" + agent.getValue(AGENT_NAME_VERSION_MAJOR) + "]");

		csvData.append("\"" + agent.getUserAgentString() + "\"");
		csvData.append(",");
		csvData.append(agent.getValue(DEVICE_CLASS));
		csvData.append(",");
		csvData.append(agent.getValue(OS_NAME_VERSION));
		csvData.append(",");
		csvData.append(agent.getValue(AGENT_NAME_VERSION_MAJOR));
		csvData.append(",");
		csvData.append(counts[1].replaceAll("^\"|\"$", ""));
		csvData.append(",");
		csvData.append(counts[2].replaceAll("^\"|\"$", ""));
		csvData.append("\n");
	}
}
