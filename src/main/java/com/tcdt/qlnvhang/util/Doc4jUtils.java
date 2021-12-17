package com.tcdt.qlnvhang.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.docx4j.Docx4J;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tr;
import org.springframework.core.io.ClassPathResource;

public class Doc4jUtils {
	public static void generateDoc(String template, HashMap<String, String> mapHeader,
			List<Map<String, Object>> lstMapDetail, OutputStream dataOutput)
			throws Docx4JException, JAXBException, IOException {
		WordprocessingMLPackage wordMLPackage;
		wordMLPackage = WordprocessingMLPackage.load(new ClassPathResource(template).getFile());
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
		// Data to construct a circular list
		ClassFinder find = new ClassFinder(Tbl.class);
		new TraversalUtil(wordMLPackage.getMainDocumentPart().getContent(), find);

		// TODO: Chinh sua them doi voi template co nhieu table
		if (lstMapDetail != null && lstMapDetail.size() > 0) {
			Tbl table = (Tbl) find.results.get(0);// Get the first table element
			// The second line is agreed as a template, and the content of the second line
			// is obtained
			Tr dynamicTr = (Tr) table.getContent().get(1);
			// Get the xml data of the template row
			String dynamicTrXml = XmlUtils.marshaltoString(dynamicTr);

			Map<String, Object> map;
			for (int i = 0; i < lstMapDetail.size(); i++) {
				map = lstMapDetail.get(i);
				Tr newTr = (Tr) XmlUtils.unmarshallFromTemplate(dynamicTrXml, map);
				table.getContent().add(newTr);
			}
			// Delete the placeholder row of the template row
			table.getContent().remove(1);
		}
		// Replace bien
		documentPart.variableReplace(mapHeader);

		// save the docs
		Docx4J.save(wordMLPackage, dataOutput);
	}
}
