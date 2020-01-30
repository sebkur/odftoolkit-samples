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

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.odftoolkit.odfdom.changes.TextContainingElement;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * Load a file from the resources directory,
 * then replace a few variables
 * and write the file to a temporary location.
 */
public class MailMerge
{

	public static void main(String[] args) throws Exception
	{
		Map<String, String> variables = new HashMap<>();
		variables.put("salutation", "Mrs.");
		variables.put("first name", "Janice");
		variables.put("name", "Doe");
		variables.put("address line1", "Castle Road 45");
		variables.put("address line2", "12345 Old Berlin");

		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("letter.odt");
		OdfTextDocument odt = OdfTextDocument.loadDocument(input);

		OfficeTextElement root = odt.getContentRoot();
		NodeList topChildren = root.getChildNodes();

		replaceVariables(topChildren, variables, 0);

		Files.createDirectories(Util.getDirForSampleFiles());
		Path file = Util.getDirForSampleFiles().resolve("mailmerge.odt");
		odt.save(file.toFile());
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
