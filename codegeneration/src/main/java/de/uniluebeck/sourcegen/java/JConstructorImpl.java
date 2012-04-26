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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import de.uniluebeck.sourcegen.exceptions.JConflictingModifierException;
import de.uniluebeck.sourcegen.exceptions.JInvalidModifierException;


class JConstructorImpl extends JElemImpl implements JConstructor {

    private static final ResourceBundle res = ResourceBundle.getBundle(JConstructorImpl.class.getCanonicalName());

    private JMethodBodyImpl body;

    private JComplexType parent;

    private int modifiers;

    private JMethodSignatureImpl signature;

    private JConstructorComment comment = null;

    /**
     * This constructor's list of Java annotations.
     */
    private List<JConstructorAnnotation> annotations = new ArrayList<JConstructorAnnotation>();

    public JConstructorImpl(JComplexType parent, int modifiers,
                            JMethodSignature signature, String... source)
            throws JConflictingModifierException, JInvalidModifierException {

        this.modifiers = modifiers;
        this.parent = parent;
        this.signature = (signature != null) ? (JMethodSignatureImpl) signature :
                JMethodSignature.factory.createEmptySignature();
        this.body = (source != null) ? new JMethodBodyImpl(source) : JMethodBody.factory.createEmpty();

        validateModifiers();

    }

    public boolean equals(JConstructorImpl other) {
        return parent.equals(other.parent)
                && signature.equals(other.signature);
    }

    public JMethodBody getBody() {
        return body;
    }

    public JComplexType getParent() {
        return parent;
    }

    public JMethodSignature getSignature() {
        return signature;
    }

    private void validateModifiers() throws JInvalidModifierException, JConflictingModifierException {

        // allowed:
        //		public, protected, private
        // unallowed:
        // 		abstract, final, interface
        // 		native, static, strict,
        // 		synchronized, transient, volatile

        boolean invalid =
                JModifier.isAbstract(modifiers) ||
                        JModifier.isFinal(modifiers) ||
                        JModifier.isInterface(modifiers) ||
                        JModifier.isNative(modifiers) ||
                        JModifier.isStatic(modifiers) ||
                        JModifier.isStrict(modifiers) ||
                        JModifier.isSynchronized(modifiers) ||
                        JModifier.isTransient(modifiers) ||
                        JModifier.isVolatile(modifiers);

        if (invalid)
            throw new JInvalidModifierException(
                    res.getString("exception.modifier.invalid") + //$NON-NLS-1$
                            JModifier.toString(modifiers)
            );

        if (JModifier.isConflict(modifiers))
            throw new JConflictingModifierException(
                    res.getString("exception.modifier.conflict") //$NON-NLS-1$
            );


    }

    @Override
    public void toString(StringBuffer buffer, int tabCount) {

        // write comment if necessary
        if (comment != null) {
            comment.toString(buffer, tabCount);
        }

        // write annotations if there are any
        for (JConstructorAnnotation ann : this.annotations) {
            ann.toString(buffer, tabCount);
        }

        if (toStringModifiers(buffer, tabCount, modifiers))
            buffer.append(" ");
        buffer.append(parent.getName());
        signature.toString(buffer, 0);
        buffer.append(" {\n");
        body.toString(buffer, tabCount + 1);
        buffer.append("\n");
        indent(buffer, tabCount);
        buffer.append("}");
    }

    public static void main(String[] args) throws Exception {
        JClass clazz = JClass.factory.create(
                "TheConstructorTestClass"
        );
        JConstructor constructor = JConstructor.factory.create(
                clazz,
                Modifier.PRIVATE,
                JMethodSignature.factory.create(
                        JParameter.factory.create(JModifier.NONE, "String", "theTestString")
                ),
                "hello world\n",
                "my darling"
        );
        System.out.print(constructor.toString(1));
    }

    /* (non-Javadoc)
         * @see de.uniluebeck.sourcegen.JConstructor#setComment(de.uniluebeck.sourcegen.JConstructorComment)
         */
    public JConstructor setComment(JConstructorComment comment) {
        this.comment = comment;
        return this;
    }

    /**
     * @see de.uniluebeck.sourcegen.java.JConstructor#addAnnotation(de.uniluebeck.sourcegen.java.JConstructorAnnotation[])
     */
    public JConstructor addAnnotation(JConstructorAnnotation... annotations) {
        for (JConstructorAnnotation ann : annotations) {
            this.annotations.add(ann);
        }
        return this;
    }

}
