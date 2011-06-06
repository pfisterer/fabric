/**
 * Copyright (c) 2010, Dennis Pfisterer, Marco Wegner, Institute of Telematics, University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *      - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 *        disclaimer.
 *      - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *        following disclaimer in the documentation and/or other materials provided with the distribution.
 *      - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 *        products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uniluebeck.sourcegen.echo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.uniluebeck.sourcegen.SourceFile;
import de.uniluebeck.sourcegen.dot.DGraphNode;

public class EchoFile extends EchoElement implements SourceFile {
    /**
     * This echo source file's path relative to the workspace.
     */
    private final File echoFile;
    
    private final List<String> labels = new ArrayList<String>();
    
    
    /**
     * Constructs a new echo source file with the specified file name.
     * 
     * @param fileName The new source file's path relative to the workspace.
     */
    public EchoFile(String fileName) {
        this.echoFile = new File(fileName);
    }

    @Override
    public String getFileName() {
        return echoFile.getPath();
    }
    
    @Override
    public void toString(StringBuffer buffer, int tabCount) {
        addLine(buffer, tabCount, "Found xsd elements");
       for(String s : labels){
    	   addLine(buffer,tabCount, s);
       }
        addLine(buffer, tabCount, "This should be all");
    }
    
    public void add(String label) {
        this.labels.add(label);
    }
    
}
