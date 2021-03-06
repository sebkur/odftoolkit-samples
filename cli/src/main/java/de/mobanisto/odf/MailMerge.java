// Copyright 2020 Sebastian Kuerten
//
// This file is part of odftoolkit-samples.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package de.mobanisto.odf;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.odftoolkit.odfdom.changes.TextContainingElement;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MailMerge
{

	public static void main(String[] args) throws Exception
	{
		if (args.length != 3) {
			System.out.println(String.format(
					"usage: %s <input odt> <csv file> <output odt>",
					MailMerge.class.getSimpleName()));
			System.exit(1);
		}

		String filenameInput = args[0];
		String filenameCsv = args[1];
		String filenameOutput = args[2];

		Path fileInput = Paths.get(filenameInput);
		Path fileCsv = Paths.get(filenameCsv);
		Path fileOutput = Paths.get(filenameOutput);

		String outputFilename = fileOutput.getFileName().toString();
		String filePattern = determineFilePattern(outputFilename);

		List<Map<String, String>> datasets = readCsv(fileCsv);

		for (int i = 0; i < datasets.size(); i++) {
			Map<String, String> variables = datasets.get(i);

			String filenameN = String.format(filePattern, i + 1);
			Path fileOutputN = fileOutput.resolveSibling(filenameN);

			mailMerge(fileInput, variables, fileOutputN);
		}
	}

	private static String determineFilePattern(String outputFilename)
	{
		if (!outputFilename.contains(".")) {
			return outputFilename + "-%d";
		}
		int pos = outputFilename.lastIndexOf(".");
		String namePart = outputFilename.substring(0, pos);
		String extensionPart = outputFilename.substring(pos);
		return namePart + "-%d" + extensionPart;
	}

	private static List<Map<String, String>> readCsv(Path file)
			throws IOException
	{
		List<Map<String, String>> datasets = new ArrayList<>();

		BufferedReader reader = Files.newBufferedReader(file);
		try (ICsvMapReader mapReader = new CsvMapReader(reader,
				CsvPreference.STANDARD_PREFERENCE)) {

			final String[] header = mapReader.getHeader(true);

			Map<String, String> values;
			while ((values = mapReader.read(header)) != null) {
				datasets.add(values);
			}
		}

		return datasets;
	}

	private static void mailMerge(Path fileInput, Map<String, String> variables,
			Path fileOutput) throws Exception
	{
		OdfTextDocument odt = OdfTextDocument.loadDocument(fileInput.toFile());

		OfficeTextElement root = odt.getContentRoot();
		NodeList topChildren = root.getChildNodes();

		replaceVariables(topChildren, variables, 0);
		odt.save(fileOutput.toFile());
	}

	private static void replaceVariables(NodeList children,
			Map<String, String> variables, int level)
	{
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			String text = child.getTextContent();

			if (child instanceof TextContainingElement) {
				TextContainingElement element = (TextContainingElement) child;
				String newText = replacesVariables(text, variables);
				element.setTextContent(newText);
			}

			NodeList grandchildren = child.getChildNodes();
			replaceVariables(grandchildren, variables, level++);
		}
	}

	private static String replacesVariables(String text,
			Map<String, String> variables)
	{
		String current = text;
		for (String variable : variables.keySet()) {
			String value = variables.get(variable);
			String pattern = String.format("\\$\\{%s\\}", variable);
			current = current.replaceAll(pattern, value);
		}
		return current;
	}

}
