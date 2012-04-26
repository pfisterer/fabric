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
package de.uniluebeck.sourcegen.java;

import java.util.LinkedList;

import de.uniluebeck.sourcegen.exceptions.JDuplicateException;

// TODO change to package private. it is public because of Workspace::getJSourceFile (new JSourceFileImpl(packageName,
// fileName);)
public class JSourceFileImpl extends JElemImpl implements JSourceFile {

	private LinkedList<JComplexTypeImpl> types = new LinkedList<JComplexTypeImpl>();

	private LinkedList<String> imports = new LinkedList<String>();

	private LinkedList<JComplexTypeImpl> importsComplex = new LinkedList<JComplexTypeImpl>();

	private String fileName;

	private String packageName;

	public String getFileName() {
		return fileName;
	}

	public JSourceFileImpl(String packageName, String fileName) {
		this.fileName = fileName;
		this.packageName = packageName;
	}

	public JSourceFile add(JComplexType... typeObject) throws JDuplicateException {
		for (JComplexType to : typeObject) {
			if (to.getSourceFile() == this)
				throw new JDuplicateException("Complex SimpleType already added to JSourceFile");
			if (to.getSourceFile() != null)
				throw new JDuplicateException("Complex SimpleType is already added to some other source file");
			if (contains(to))
				throw new JDuplicateException("Duplicate Java Complex SimpleType " + typeObject);
			types.add((JComplexTypeImpl) to);
			((JComplexTypeImpl) to).setSourceFile(this);
		}
		return this;
	}

	public boolean contains(JComplexType typeObject) {
		for (JComplexTypeImpl type : types)
			if (type.equals(typeObject))
				return true;
		return false;
	}

	public JSourceFile addImport(JComplexType... typeObjects) throws JDuplicateException {
		for (JComplexType to : typeObjects) {
			if (containsImport(to))
				throw new JDuplicateException("Duplicate import " + to.getFullyQualifiedName());
			importsComplex.add((JComplexTypeImpl) to);
		}
		return this;
	}

	public boolean containsImport(JComplexType typeObject) {
		for (JComplexType to : importsComplex)
			if (to.equals(typeObject))
				return true;
		return false;
	}

	public JSourceFile addImport(String... importTypes) throws JDuplicateException {
		for (String imp : importTypes) {
			if (containsImport(imp))
				throw new JDuplicateException("Duplicate import " + imp);
			imports.add(imp);
		}
		return this;
	}

	public boolean containsImport(String imp) {
		for (String s : imports)
			if (s.equals(imp))
				return true;
		return false;
	}

	public JSourceFile addImport(Class<?>... imp) throws JDuplicateException {
		for (Class<?> c : imp) {
			if (containsImport(c.getCanonicalName()))
				throw new JDuplicateException("Duplicate import " + c.getCanonicalName());
			imports.add(c.getCanonicalName());
		}
		return this;
	}

	public boolean containsImport(Class<?> imp) {
		return containsImport(imp.getCanonicalName());
	}

	@Override
	public void toString(StringBuffer buffer, int tabCount) {
		if (appendPackageString(buffer, tabCount))
			buffer.append("\n\n");
		if (appendImportBlock(buffer, tabCount))
			buffer.append("\n\n");
		toString(buffer, tabCount, types, "", "\n\n");
	}

	boolean appendPackageString(StringBuffer buffer, int tabCount) {
		boolean hasPackageName = getPackageName() != null && !"".equals(getPackageName());

		// TODO this might not work anymore after refactoring because it used to rely on Workspace.getJPackagePrefix
		if (hasPackageName) {
			indent(buffer, tabCount);
			buffer.append("package ");
			buffer.append(getPackageName());
			buffer.append(";");
			return true;
		}

		return false;
	}

	boolean appendImportBlock(StringBuffer buffer, int tabCount) {

		if (importsComplex.size() > 0 || imports.size() > 0) {
			for (JComplexTypeImpl imp : importsComplex) {
				indent(buffer, tabCount);
				buffer.append("import ");
				// TODO this might not work anymore after refactoring because it used to rely on
				// Workspace.getJPackagePrefix
				buffer.append(imp.getFullyQualifiedName());
				buffer.append(";\n");
			}
			
			if (importsComplex.size() > 0)
				buffer.append("\n");
			
			for (String imp : imports) {
				indent(buffer, tabCount);
				buffer.append("import ");
				buffer.append(imp);
				buffer.append(";");
				if (!imp.equals(imports.getLast()))
					buffer.append("\n");
			}
			return true;
		}
		return false;
	}

	public String getPackageName() {
		return packageName;
	}

	public JSourceFile setPackageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	public JClass getClassByName(String className) {
		for (JComplexTypeImpl c : types)
			if (c instanceof JClassImpl && ((JClassImpl) c).getPackageName().equals(packageName)
					&& ((JClassImpl) c).getName().equals(className))
				return (JClass) c;
		return null;
	}

    public boolean equals(Object o) {
        boolean ret = false;

        if(o instanceof JSourceFileImpl) {
            JSourceFileImpl sf = (JSourceFileImpl) o;

            // Check file name
            ret = fileName.equals(sf.fileName);

            // Check package name
            ret = ret && packageName.equals(sf.packageName);

            // Check imports (ignoring order)
            ret = ret && imports.containsAll(sf.imports) && sf.imports.containsAll(imports);

            // Check complex imports (ignoring order)
            ret = ret && importsComplex.containsAll(sf.importsComplex) && sf.importsComplex.containsAll(importsComplex);

            // Check types (ignoring order)
            ret = ret && types.containsAll(sf.types) && sf.types.containsAll(types);
        }

        return ret;
    }

}
