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
package fabric.wsdlschemaparser.schema;

import java.util.HashSet;

/**
 * 
 * @author Richard Mietz
 *
 */
public class ReservedNames {

	private static ReservedNames instance = null;
	
	private final String RES_NAME_JAVA_POSTFIX = "_resJava";
	private final String RES_NAME_C_POSTFIX = "_resC"; 
	
	private HashSet<String> reservedCNames = new HashSet<String>();
	private HashSet<String> reservedJavaNames = new HashSet<String>();
	
	public ReservedNames() {
		addCNames();
		addJavaNames();
	}
	
	public final static synchronized ReservedNames instance() {
		if (instance == null)
			instance = new ReservedNames();
		return instance;
	}
	
	private void addCNames()
	{
		reservedCNames.add("break");
		reservedCNames.add("case");
		reservedCNames.add("char");
		reservedCNames.add("const");
		reservedCNames.add("continue");
		reservedCNames.add("default");
		reservedCNames.add("do");
		reservedCNames.add("double");
		reservedCNames.add("else");
		reservedCNames.add("enum");
		reservedCNames.add("extern");
		reservedCNames.add("float");
		reservedCNames.add("for");
		reservedCNames.add("goto");
		reservedCNames.add("if");
		reservedCNames.add("int");
		reservedCNames.add("long");
		reservedCNames.add("register");
		reservedCNames.add("return");
		reservedCNames.add("short");
		reservedCNames.add("signed");
		reservedCNames.add("sizeof");
		reservedCNames.add("static");
		reservedCNames.add("struct");
		reservedCNames.add("switch");
		reservedCNames.add("typedef");
		reservedCNames.add("union");
		reservedCNames.add("unsigned");
		reservedCNames.add("void");
		reservedCNames.add("while");
	}
	
	private void addJavaNames()
	{
		reservedJavaNames.add("abstract");
		reservedJavaNames.add("assert");
		reservedJavaNames.add("boolean");
		reservedJavaNames.add("break");
		reservedJavaNames.add("byte");
		reservedJavaNames.add("case");
		reservedJavaNames.add("catch");
		reservedJavaNames.add("char");
		reservedJavaNames.add("class");
		reservedJavaNames.add("const");
		reservedJavaNames.add("continue");
		reservedJavaNames.add("default");
		reservedJavaNames.add("do");
		reservedJavaNames.add("double");
		reservedJavaNames.add("else");
		reservedJavaNames.add("enum");
		reservedJavaNames.add("extends");
		reservedJavaNames.add("false");
		reservedJavaNames.add("final");
		reservedJavaNames.add("finally");
		reservedJavaNames.add("float");
		reservedJavaNames.add("for");
		reservedJavaNames.add("goto");
		reservedJavaNames.add("if");
		reservedJavaNames.add("implements");
		reservedJavaNames.add("import");
		reservedJavaNames.add("instanceof");
		reservedJavaNames.add("int");
		reservedJavaNames.add("interface");
		reservedJavaNames.add("long");
		reservedJavaNames.add("native");
		reservedJavaNames.add("new");
		reservedJavaNames.add("null");
		reservedJavaNames.add("package");
		reservedJavaNames.add("private");
		reservedJavaNames.add("protected");
		reservedJavaNames.add("public");
		reservedJavaNames.add("return");
		reservedJavaNames.add("short");
		reservedJavaNames.add("static");
		reservedJavaNames.add("super");
		reservedJavaNames.add("strictfp");
		reservedJavaNames.add("switch");
		reservedJavaNames.add("synchronized");
		reservedJavaNames.add("this");
		reservedJavaNames.add("throw");
		reservedJavaNames.add("throws");
		reservedJavaNames.add("transient");
		reservedJavaNames.add("true");
		reservedJavaNames.add("try");
		reservedJavaNames.add("void");
		reservedJavaNames.add("volatile");
		reservedJavaNames.add("while");
	}

	public boolean isJavaResName(String name)
	{
		return reservedJavaNames.contains(name);
	}
	
	public boolean isCResName(String name)
	{
		return reservedCNames.contains(name);
	}
	
	public boolean isResName(String name)
	{
		return isCResName(name) || isJavaResName(name);
	}
	
	private String getNewJavaName(String name)
	{
		return name + RES_NAME_JAVA_POSTFIX;
	}
	
	private String getNewCName(String name)
	{
		return name + RES_NAME_C_POSTFIX;
	}
	
	public String getNewName(String name)
	{
		if(isJavaResName(name) && !isCResName(name))
		{
			return getNewJavaName(name);
		}
		else if(!isJavaResName(name) && isCResName(name))
		{
			return getNewCName(name);
		}
		else if(isJavaResName(name) && isCResName(name))
		{
			return name + RES_NAME_JAVA_POSTFIX + RES_NAME_C_POSTFIX;
		}
		return name;
	}
}
