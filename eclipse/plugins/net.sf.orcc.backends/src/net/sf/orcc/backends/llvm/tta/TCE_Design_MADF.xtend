/*
 * Copyright (c) 2012, IRISA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of IRISA nor the names of its
 *     contributors may be used to endorse or promote products derived from this
 *     software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
 
 package net.sf.orcc.backends.llvm.tta

import net.sf.orcc.backends.llvm.tta.architecture.Design
import net.sf.orcc.backends.llvm.tta.architecture.util.ArchitectureVisitor
import net.sf.orcc.backends.llvm.tta.architecture.Processor

/*
 * The template to print the Multiprocessor Architecture Description File.
 *  
 * @author Herve Yviquel
 * 
 */
class TCE_Design_MADF extends ArchitectureVisitor<CharSequence> {
	
	override caseDesign(Design design){
		'''
		<?xml version="1.0" encoding="UTF-8" standalone="no" ?>
		<madf version="0.1">
			«FOR processor:design.processors»
			«doSwitch(processor)»
			«ENDFOR»
		</madf>
		'''
	}
	
	override caseProcessor(Processor processor){
		'''
		<processor name="«processor.name»" >
			<adf path="processor_«processor.name»/" filename="processor_«processor.name».adf" />
			<idf path="processor_«processor.name»/" filename="processor_«processor.name».idf" />
			<tpef path="processor_«processor.name»/" filename="processor_«processor.name».tpef" />
		</processor>
		'''
	}
	
	
}
