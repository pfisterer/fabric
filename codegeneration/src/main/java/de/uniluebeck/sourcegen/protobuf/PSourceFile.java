package de.uniluebeck.sourcegen.protobuf;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.protobuf.types.PAbstractElem;

public class PSourceFile extends PAbstractElem implements SourceFile {
	private File relativeFileName;

	private String packageName;

	private Collection<PAbstractElem> elements = new LinkedList<PAbstractElem>();

	public PSourceFile(String fileName) {
		this.relativeFileName = new File(fileName);
	}

	public void add(PAbstractElem element) {
		this.elements.add(element);
	}

	@Override
	public String getFileName() {
		return relativeFileName.getPath();
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		if (packageName != null && !"".equals(packageName))
			addLine(buffer, tabCount, "package " + packageName + ";");

		for(PAbstractElem element : elements)
			element.toString(buffer, tabCount+1);
	}

}
