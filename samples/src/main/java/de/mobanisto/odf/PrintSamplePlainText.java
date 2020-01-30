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

import org.odftoolkit.odfdom.changes.TextContainingElement;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * Load a file from the resources directory and print its plain text content
 */
public class PrintSamplePlainText
{

	public static void main(String[] args) throws Exception
	{
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("test.odt");
		OdfTextDocument odt = OdfTextDocument.loadDocument(input);

		OfficeTextElement root = odt.getContentRoot();
		NodeList topChildren = root.getChildNodes();

		print(topChildren, 0);
	}

	private static void print(NodeList children, int level)
	{
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			String text = child.getTextContent();

			if (child instanceof TextContainingElement) {
				System.out.println(text);
			}

			NodeList grandchildren = child.getChildNodes();
			print(grandchildren, level++);
		}
	}

}
