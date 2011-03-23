/**
 * Copyright (c) 2010, Institute of Telematics, University of Luebeck
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

public abstract class Cpp {
	
	public static final long ARRAY 		= 1<<0;
	public static final long ARRAY2D	= 1<<1;
	public static final long ARRAY3D	= 1<<2;
	public static final long BOOL		= 1<<3;
	public static final long CHAR 		= 1<<4;
	public static final long CONST		= 1<<5;
	public static final long DOUBLE		= 1<<6;
	public static final long EXTERN		= 1<<7;
	public static final long FLOAT		= 1<<8;
	public static final long FRIEND		= 1<<9;
	public static final long INLINE		= 1<<10;
	public static final long INT		= 1<<11;
	public static final long LONG		= 1<<12;
	public static final long LONGDOUBLE = 1<<13;
	public static final long MUTABLE	= 1<<14;
	public static final long NONE		= 1<<15;
	public static final long POINTER	= 1<<16;
	public static final long PRIVATE	= 1<<17;
	public static final long PROTECTED	= 1<<18;
	public static final long PUBLIC		= 1<<19;
	public static final long REFERENCE 	= 1<<20;
	public static final long REGISTER	= 1<<21;
	public static final long SHORT 		= 1<<22;
	public static final long SIGNED		= 1<<23;
	public static final long STATIC		= 1<<24;
	public static final long THIS		= 1<<25;
	public static final long TYPEDEF	= 1<<26;
	public static final long UNSIGNED	= 1<<27;
	public static final long VIRTUAL	= 1<<28;
	public static final long VOID		= 1<<29;
	public static final long VOLATILE	= 1<<30;
	public static final long WCHAR_T	= 1<<31;
	
	public boolean isArray(long mod) {
		return (mod & ARRAY) != 0;
	}
	
	public boolean isArray2D(long mod) {
		return (mod & ARRAY2D) != 0;
	}
	
	public boolean isArray3D(long mod) {
		return (mod & ARRAY3D) != 0;
		
	}
	
	public boolean isBool(long mod) {
		return (mod & BOOL) != 0;
		
	}
	
	public boolean isChar(long mod) {
		return (mod & CHAR) != 0;
		
	}
	
	public boolean isConst(long mod) {
		return (mod & CONST) != 0;
		
	}
	
	public boolean isDouble(long mod) {
		return (mod & DOUBLE) != 0;
		
	}
	
	public boolean isExtern(long mod) {
		return (mod & EXTERN) != 0;
		
	}
	
	public boolean isFloat(long mod) {
		return (mod & FLOAT) != 0;
		
	}
	
	public boolean isFriend(long mod) {
		return (mod & FRIEND) != 0;
		
	}
	
	public boolean isInline(long mod) {
		return (mod & INLINE) != 0;
		
	}
	
	public boolean isInt(long mod) {
		return (mod & INT) != 0;
	}
	
	public boolean isLong(long mod) {
		return (mod & LONG) != 0;
	}
	
	public boolean isLongDouble(long mod) {
		return (mod & LONGDOUBLE) != 0;
	}
	
	public boolean isMutable(long mod) {
		return (mod & MUTABLE) != 0;
	}
	
	public boolean isNone(long mod) {
		return (mod & NONE) != 0;
	}
	
	public boolean isPointer(long mod) {
		return (mod & POINTER) != 0;
	}
	
	public boolean isPrivate(long mod) {
		return (mod & PRIVATE) != 0;
	}
	
	public boolean isProtected(long mod) {
		return (mod & PROTECTED) != 0;
	}
	
	public boolean isPublic(long mod) {
		return (mod & PUBLIC) != 0;
	}
	
	public boolean isReference(long mod) {
		return (mod & REFERENCE) != 0;
	}
	
	public boolean isRegister(long mod) {
		return (mod & REGISTER) != 0;
	}
	
	public boolean isShort(long mod) {
		return (mod & SHORT) != 0;
	}
	
	public boolean isSigned(long mod) {
		return (mod & SIGNED) != 0;
	}
	
	public boolean isStatic(long mod) {
		return (mod & STATIC) != 0;
	}
	
	public boolean isThis(long mod) {
		return (mod & THIS) != 0;
	}
	
	public boolean isTypedef(long mod) {
		return (mod & TYPEDEF) != 0;
	}
	
	public boolean isUnsigned(long mod) {
		return (mod & UNSIGNED) != 0;
	}
	
	public boolean isVirtual(long mod) {
		return (mod & VIRTUAL) != 0;
	}
	
	public boolean isVoid(long mod) {
		return (mod & VOID) != 0;
	}
	
	public boolean isVolatile(long mod) {
		return (mod & VOLATILE) != 0;
	}
	
	public boolean isWchar_t(long mod) {
		return (mod & WCHAR_T) != 0;
	}
	
	public static String toString(long mod) {
		StringBuffer sb = new StringBuffer();
		int len;
		
		if ((mod & CONST) != 0) 	sb.append("const ");
		if ((mod & EXTERN) != 0) 	sb.append("extern ");
		if ((mod & FRIEND) != 0) 	sb.append("friend ");
		if ((mod & INLINE) != 0) 	sb.append("inline ");
		if ((mod & MUTABLE) != 0) 	sb.append("mutable ");
		if ((mod & REGISTER) != 0) 	sb.append("register ");
		if ((mod & STATIC) != 0) 	sb.append("static ");
		if ((mod & SIGNED) != 0) 	sb.append("signed ");
		if ((mod & TYPEDEF) != 0) 	sb.append("typedef ");
		if ((mod & UNSIGNED) != 0)	sb.append("unsigned ");
		if ((mod & VIRTUAL) != 0) 	sb.append("virtual ");
		if ((mod & VOLATILE) != 0) 	sb.append("volatile ");

		if ((mod & PRIVATE) != 0) 	sb.append("private ");
		if ((mod & PROTECTED) != 0)	sb.append("protected ");
		if ((mod & PUBLIC) != 0) 	sb.append("public ");

		if ((mod & BOOL) != 0) 		sb.append("bool ");
		if ((mod & CHAR) != 0) 		sb.append("char ");
		if ((mod & DOUBLE) != 0) 	sb.append("double ");
		if ((mod & FLOAT) != 0) 	sb.append("float ");
		if ((mod & INT) != 0) 		sb.append("int ");
		if ((mod & LONG) != 0) 		sb.append("long ");
		if ((mod & LONGDOUBLE) != 0)sb.append("long double ");
		if ((mod & SHORT) != 0) 	sb.append("short ");
		if ((mod & WCHAR_T) != 0) 	sb.append("wchar_t ");

		if ((mod & ARRAY) != 0) 	sb.append("[]");
		if ((mod & ARRAY2D) != 0) 	sb.append("[][]");
		if ((mod & ARRAY3D) != 0)	sb.append("[][][]");
		if ((mod & POINTER) != 0) 	sb.append("*");
		if ((mod & REFERENCE) != 0) sb.append("&");

		if ((mod & NONE) != 0) 		sb.append(" ");
		if ((mod & THIS) != 0) 		sb.append("this ");
		if ((mod & VOID) != 0) 		sb.append("void ");
		
		if ((len = sb.length()) > 0)
		    return sb.toString().substring(0, len-1);
		return sb.toString();
	}
	
	public static void main(String args[]) {
		System.out.println(Long.toBinaryString(1) + " & " + Long.toBinaryString(1<<5) + " : " +Long.toBinaryString((1 & (1<<5))));
		System.out.println(Long.toBinaryString(STATIC) + " & " + Long.toBinaryString(STATIC) + " : " +Long.toBinaryString((STATIC & (STATIC))));
		
	}

}
