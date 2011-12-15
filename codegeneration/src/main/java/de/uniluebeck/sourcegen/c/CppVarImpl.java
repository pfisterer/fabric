/**
 * Copyright (c) 2010, Dennis Pfisterer, Marco Wegner, Institute of Telematics, University of Luebeck
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
package de.uniluebeck.sourcegen.c;

import java.util.LinkedList;
import java.util.List;

class CppVarImpl extends CElemImpl implements CppVar {
    private enum Type {
        //DECL_STRING, LONG_STRING, LONG_CCOMPLEX_STRING, LONG_CPPCOMPLEX_STRING, TEMPLATE
    	TYPE_GENERATOR
    }

    private Long visability;

//    private String varDeclString;
    private Type type;
    private String initCode;
    private String varName;
//    private CComplexType cComplexType;
//    private CppComplexType cppComplexType;

    private CppTypeGenerator typeGenerator;
    private List<CppTemplateName> depTypes = new LinkedList<CppTemplateName>();

	private CComment comment = null;

//    private CppClass clazz;

	public CppVarImpl(CppTypeGenerator type, String varName) {
		this.type = Type.TYPE_GENERATOR;
		this.typeGenerator = type;
		this.varName = varName;
	}

	public CppVarImpl(long visibility, CppTypeGenerator type, String varName) {
		this(visibility, type, varName, null);
	}

	public CppVarImpl(long visibility, CppTypeGenerator type, String varName, String initCode) {
		this.type = Type.TYPE_GENERATOR;
		this.typeGenerator = type;
		this.varName = varName;
		this.visability = visibility;
		this.initCode = initCode;
	}

	/*
    public CppVarImpl(String varDeclString) {
        this.type = Type.DECL_STRING;
        this.varDeclString = varDeclString;
    }

    public CppVarImpl(long qualifiedType, String varName, String initCode) {
        this.type = Type.LONG_STRING;
        this.typeGenerator = new CppTypeGenerator(null, qualifiedType, null);
        this.varName = varName;
        this.initCode = initCode;
    }

    public CppVarImpl(String varName, CppTypeGenerator typeGen, String initCode) {
        this.type = Type.LONG_STRING;
        this.typeGenerator = typeGen;
        this.varName = varName;
        this.initCode = initCode;
    }

    public CppVarImpl(long qualifiedType, String varName, CppTemplateHelper template, CppTemplateName... dep) {
        this.type = Type.TEMPLATE;
        this.typeGenerator = new CppTypeGenerator(null, qualifiedType, template);
        this.varName = varName;
        this.initCode = "";
        this.addDependencies(dep);
    }

    public CppVarImpl(CppTypeGenerator type, String varName, CppTemplateHelper template, CppTemplateName... dep) {
        this.type = Type.TEMPLATE;
        this.typeGenerator = type;
        this.varName = varName;
        this.initCode = "";
        this.addDependencies(dep);
    }

    public CppVarImpl(long qualifier, CComplexType type, String varName, String initCode) {
        this.type = Type.LONG_CCOMPLEX_STRING;
        this.typeGenerator = new CppTypeGenerator(qualifier, null, null);
        this.cComplexType = type;
        this.varName = varName;
        this.initCode = initCode;
    }

    public CppVarImpl(long qualifier, CppComplexType type, String varName, String initCode) {
        this.type = Type.LONG_CPPCOMPLEX_STRING;
        this.typeGenerator = new CppTypeGenerator(qualifier, null, null);
        this.cppComplexType = type;
        this.varName = varName;
        this.initCode = initCode;
    }
    */

  public void addDependencies(CppTemplateName... dep) {
    for (CppTemplateName elem: dep) {
      this.depTypes.add(elem);
    }
  }

  @Override
  public String getInit() {
    if (initCode == null) {
      return null;
    }

    StringBuffer buffer = new StringBuffer();

    switch (type) {
      case TYPE_GENERATOR:
        buffer.append(varName + "(" + initCode + ")");
        break;
      default:
        buffer.append(Cpp.newline + "// NOTHING TO APPEND" + Cpp.newline);
    }

    return buffer.toString();
  }

  @Override
  public void toString(StringBuffer buffer, int tabCount) {
    indent(buffer, tabCount);

    // write comment if necessary
    if (comment != null) {
      buffer.append(Cpp.newline);
      comment.toString(buffer, tabCount);
    }

    switch (type) {
      case TYPE_GENERATOR:
        buffer.append(typeGenerator.toString() + " " + varName);
        break;
      default:
        buffer.append(Cpp.newline + "// NOTHING TO APPEND" + Cpp.newline);
    }

/*
        // Add all dependent types
        if (this.depTypes != null && this.depTypes.size() > 0) {
            int cnt = this.depTypes.size();
            for (int i = 0; i < cnt; i++) {
                buffer.append(this.depTypes.get(i).getName());
                buffer.append("::");
            }
        }
*/
/*
    	switch (type) {
        	case TYPE_GENERATOR:

//        		buffer.append(typeGenerator.toString() + " ");
//
//        		// is a class variable
//        		if(this.clazz != null && initCode != null) {
//        			buffer.append(getParents());
//        			buffer.append(this.clazz.getName() + "::");
//        		}
//

//
//        		// is an initialized class variable
        		if(initCode != null) {
            		buffer.append(varName);
        			buffer.append("(" + initCode + ")");
        		} else {
        			buffer.append(varName);
        		}

        		break;
*/
        		/*
        case DECL_STRING:
            buffer.append(varDeclString);
            break;
        case LONG_CCOMPLEX_STRING:
        case LONG_CPPCOMPLEX_STRING:
            String modString = typeGenerator.toString();
            buffer.append(modString.length() > 0 ? modString + " " : ""); // Add a space
            buffer.append(type == Type.LONG_CPPCOMPLEX_STRING ? this.cppComplexType.getTypeName() : this.cComplexType
                    .getTypeName());
            buffer.append(" " + varName);
            if (initCode != null) {
    			buffer.append(" = ");
    			buffer.append(initCode);
    			buffer.append(";");
    		}
            break;
        case LONG_STRING:
            buffer.append(typeGenerator.toString());
            buffer.append(" ");
            buffer.append(varName);
            if (initCode != null) {
    			buffer.append(" = ");
    			buffer.append(initCode);
    			buffer.append(";");
    		}
            break;
        case TEMPLATE:
            buffer.append(typeGenerator.toString());
            buffer.append(" ");
            buffer.append(varName);
            if (initCode != null) {
    			buffer.append(" = ");
    			buffer.append(initCode);
    			buffer.append(";");
    		}
            break;
        	default:
        		buffer.append(Cpp.newline + "// NOTHING TO APPEND" + Cpp.newline);
        }
        		 */
    }

  public Long getVisability() {
    return visability;
	}
  
  // TODO: Added
  public String getTypeName() {
    return type.name();
  }

	public String getVarName() {
    return varName;
	}

  public String getInitCode() {
    return initCode;
	}

  @Override
	public CppVar setComment(CComment comment) {
		this.comment = comment;
		return this;
	}

  /**
   * returns OUTER::NESTED1::NESTED2::...::NESTEDN
   *
   * @return
   */
/*
  private String getParents(){
    StringBuffer myParents = new StringBuffer();
    if(this.clazz != null) {
      for (CppClass p : this.clazz.getParents()) {
        myParents.append(p.getName()+ "::");
    }
    }
    return myParents.toString();
  }
*/
/*	@Override
	public CppVar setClass(CppClass clazz) {
		this.clazz = clazz;
		return this;
	}
*/

}
