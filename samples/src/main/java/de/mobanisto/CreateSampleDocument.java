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

package de.mobanisto;

import java.nio.file.Files;
import java.nio.file.Path;

import org.odftoolkit.odfdom.doc.OdfTextDocument;

/*
 * The sample from the odftoolkit website
 */
public class CreateSampleDocument
{

	public static void main(String[] args) throws Exception
	{
		OdfTextDocument odt = OdfTextDocument.newTextDocument();
		odt.addText("This is my very first ODF test");

		Files.createDirectories(Util.getDirForSampleFiles());
		Path file = Util.getDirForSampleFiles().resolve("test.odt");
		odt.save(file.toFile());
	}

}
