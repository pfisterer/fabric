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

public abstract class Cpp {

	public static String newline = System.getProperty("line.separator");
  public static String tab = "\t";

  public static final long ARRAY 		= 1<<0;
	public static final long ARRAY2D	= 1<<1;
	public static final long ARRAY3D	= 1<<2;
	public static final long BOOL			= 1<<3;
	public static final long CHAR 		= 1<<4;
	public static final long CONST		= 1<<5;
	public static final long DOUBLE		= 1<<6;
	public static final long EXTERN		= 1<<7;
	public static final long FLOAT		= 1<<8;
	public static final long FRIEND		= 1<<9;
	public static final long INLINE		= 1<<10;
	public static final long INT			= 1<<11;
	public static final long LONG			= 1<<12;
	public static final long LONGDOUBLE = 1<<13;
	public static final long MUTABLE	= 1<<14;
	public static final long NONE			= 1<<15;
	public static final long POINTER	= 1<<16;
	public static final long PRIVATE	= 1<<17;
	public static final long PROTECTED	= 1<<18;
	public static final long PUBLIC		= 1<<19;
	public static final long REFERENCE 	= 1<<20;
	public static final long REGISTER	= 1<<21;
	public static final long SHORT 		= 1<<22;
	public static final long SIGNED		= 1<<23;
	public static final long STATIC		= 1<<24;
	public static final long THIS			= 1<<25;
	public static final long TYPEDEF	= 1<<26;
	public static final long UNSIGNED	= 1<<27;
	public static final long VIRTUAL	= 1<<28;
	public static final long VOID			= 1<<29;
	public static final long VOLATILE	= 1<<30;
	public static final long WCHAR_T	= 1<<31;

	public static boolean isArray(long mod) {
		return (mod & ARRAY) != 0;
	}

	public static boolean isArray2D(long mod) {
		return (mod & ARRAY2D) != 0;
	}

	public static boolean isArray3D(long mod) {
		return (mod & ARRAY3D) != 0;
	}

	public static boolean isBool(long mod) {
		return (mod & BOOL) != 0;
	}

	public static boolean isChar(long mod) {
		return (mod & CHAR) != 0;
	}

	public static boolean isConst(long mod) {
		return (mod & CONST) != 0;
	}

	public static boolean isDouble(long mod) {
		return (mod & DOUBLE) != 0;
	}

	public static boolean isExtern(long mod) {
		return (mod & EXTERN) != 0;
	}

	public static boolean isFloat(long mod) {
		return (mod & FLOAT) != 0;
	}

	public static boolean isFriend(long mod) {
		return (mod & FRIEND) != 0;
	}

	public static boolean isInline(long mod) {
		return (mod & INLINE) != 0;
	}

	public static boolean isInt(long mod) {
		return (mod & INT) != 0;
	}

	public static boolean isLong(long mod) {
		return (mod & LONG) != 0;
	}

	public static boolean isLongDouble(long mod) {
		return (mod & LONGDOUBLE) != 0;
	}

	public static boolean isMutable(long mod) {
		return (mod & MUTABLE) != 0;
	}

	public static boolean isNone(long mod) {
		return (mod & NONE) != 0;
	}

	public static boolean isPointer(long mod) {
		return (mod & POINTER) != 0;
	}

	public static boolean isPrivate(long mod) {
		return (mod & PRIVATE) != 0;
	}

	public static boolean isProtected(long mod) {
		return (mod & PROTECTED) != 0;
	}

	public static boolean isPublic(long mod) {
		return (mod & PUBLIC) != 0;
	}

	public static boolean isReference(long mod) {
		return (mod & REFERENCE) != 0;
	}

	public static boolean isRegister(long mod) {
		return (mod & REGISTER) != 0;
	}

	public static boolean isShort(long mod) {
		return (mod & SHORT) != 0;
	}

	public static boolean isSigned(long mod) {
		return (mod & SIGNED) != 0;
	}

	public static boolean isStatic(long mod) {
		return (mod & STATIC) != 0;
	}

	public static boolean isThis(long mod) {
		return (mod & THIS) != 0;
	}

	public static boolean isTypedef(long mod) {
		return (mod & TYPEDEF) != 0;
	}

	public static boolean isUnsigned(long mod) {
		return (mod & UNSIGNED) != 0;
	}

	public static boolean isVirtual(long mod) {
		return (mod & VIRTUAL) != 0;
	}

	public static boolean isVoid(long mod) {
		return (mod & VOID) != 0;
	}

	public static boolean isVolatile(long mod) {
		return (mod & VOLATILE) != 0;
	}

	public static boolean isWchar_t(long mod) {
		return (mod & WCHAR_T) != 0;
	}

	public static String toString(long mod) {
		StringBuffer sb = new StringBuffer();
		// int len;

		if (isConst(mod)) 		sb.append("const ");
		if (isExtern(mod)) 		sb.append("extern ");
		if (isFriend(mod)) 		sb.append("friend ");
		if (isInline(mod)) 		sb.append("inline ");
		if (isMutable(mod)) 	sb.append("mutable ");
		if (isRegister(mod)) 	sb.append("register ");
		if (isStatic(mod)) 		sb.append("static ");
		if (isSigned(mod)) 		sb.append("signed ");
		if (isTypedef(mod)) 	sb.append("typedef ");
		if (isUnsigned(mod))	sb.append("unsigned ");
		if (isVirtual(mod)) 	sb.append("virtual ");
		if (isVolatile(mod)) 	sb.append("volatile ");

		if (isPrivate(mod)) 	sb.append("private ");
		if (isProtected(mod))	sb.append("protected ");
		if (isPublic(mod)) 		sb.append("public ");

		if (isBool(mod)) 		sb.append("bool ");
		if (isChar(mod)) 		sb.append("char ");
		if (isDouble(mod)) 		sb.append("double ");
		if (isFloat(mod)) 		sb.append("float ");
		if (isInt(mod)) 		sb.append("int ");
		if (isLong(mod)) 		sb.append("long ");
		if (isLongDouble(mod))	sb.append("long double ");
		if (isShort(mod)) 		sb.append("short ");
		if (isWchar_t(mod)) 	sb.append("wchar_t ");

		if (isArray(mod)) 		sb.append("[]");
		if (isArray2D(mod)) 	sb.append("[][]");
		if (isArray3D(mod))		sb.append("[][][]");
		if (isPointer(mod)) 	sb.append("*");
		if (isReference(mod)) 	sb.append("&");

		if (isNone(mod)) 		sb.append(" ");
		if (isThis(mod)) 		sb.append("this ");
		if (isVoid(mod)) 		sb.append("void ");

		/*
		if ((len = sb.length()) > 0)
		    return sb.toString().substring(0, len-1);
		*/
		return sb.toString().trim();
	}

	public static void main(String args[]) {
		System.out.println(Long.toBinaryString(1) + " & " + Long.toBinaryString(1<<5) + " : " +Long.toBinaryString((1 & (1<<5))));
		System.out.println(Long.toBinaryString(STATIC) + " & " + Long.toBinaryString(STATIC) + " : " +Long.toBinaryString((STATIC & (STATIC))));
	}

}
