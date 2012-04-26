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
package fabric.core.filegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.AttributeContainer.Restriction;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for simpleType_whiteSpace.xsd
 */
public class ST_WhiteSpace_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public ST_WhiteSpace_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
     * Generates the JComplexType objects corresponding to the test XSD.
     */
    @Override void generateClasses() throws Exception {
        /**
         * Address1Type
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
	Restriction address1TypeRestriction = new Restriction();
	address1TypeRestriction.whiteSpace = "preserve";
    	JClass address1Type = ((JClass) AttributeContainer.newBuilder()
            .setName("Address1Type")
            .addElement("String", "value", address1TypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(address1Type, strategy.getRequiredDependencies());

        /**
         * Address2Type
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        Restriction address2TypeRestriction = new Restriction();
        address2TypeRestriction.whiteSpace = "replace";
        JClass address2Type = ((JClass) AttributeContainer.newBuilder()
            .setName("Address2Type")
            .addElement("String", "value", address2TypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(address2Type, strategy.getRequiredDependencies());

        /**
         * Address3Type
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        Restriction address3TypeRestriction = new Restriction();
        address3TypeRestriction.whiteSpace = "collapse";
        JClass address3Type = ((JClass) AttributeContainer.newBuilder()
            .setName("Address3Type")
            .addElement("String", "value", address3TypeRestriction)
            .build()
            .asClassObject(strategy));
        types.put(address3Type, strategy.getRequiredDependencies());

        /**
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        JClass root = ((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("Address1Type", "Address1")
            .addElement("Address2Type", "Address2")
            .addElement("Address3Type", "Address3")
            .build()
            .asClassObject(strategy));
        types.put(root, strategy.getRequiredDependencies());
    }
}
