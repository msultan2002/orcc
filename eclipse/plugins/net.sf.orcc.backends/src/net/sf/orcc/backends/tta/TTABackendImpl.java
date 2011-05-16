/*
 * Copyright (c) 2011, IRISA
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
 *   * Neither the name of the IETR/INSA of Rennes nor the names of its
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
package net.sf.orcc.backends.tta;

import static net.sf.orcc.OrccLaunchConstants.DEBUG_MODE;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.orcc.OrccException;
import net.sf.orcc.backends.AbstractBackend;
import net.sf.orcc.backends.InstancePrinter;
import net.sf.orcc.backends.llvm.LLVMExpressionPrinter;
import net.sf.orcc.backends.llvm.LLVMTemplateData;
import net.sf.orcc.backends.llvm.LLVMTypePrinter;
import net.sf.orcc.backends.llvm.transformations.BoolToIntTransformation;
import net.sf.orcc.backends.llvm.transformations.GetElementPtrAdder;
import net.sf.orcc.backends.llvm.transformations.PrintlnTransformation;
import net.sf.orcc.backends.transformations.CastAdder;
import net.sf.orcc.backends.transformations.TypeSizeTransformation;
import net.sf.orcc.backends.transformations.tac.TacTransformation;
import net.sf.orcc.backends.xlim.transformations.InstPhiTransformation;
import net.sf.orcc.ir.Actor;
import net.sf.orcc.ir.transformations.BlockCombine;
import net.sf.orcc.ir.transformations.RenameTransformation;
import net.sf.orcc.ir.transformations.SSATransformation;
import net.sf.orcc.ir.util.ActorVisitor;
import net.sf.orcc.network.Instance;
import net.sf.orcc.network.Network;
import net.sf.orcc.network.serialize.XDFWriter;
import net.sf.orcc.network.transformations.NetworkClassifier;
import net.sf.orcc.tools.classifier.ActorClassifier;
import net.sf.orcc.tools.normalizer.ActorNormalizer;

import org.eclipse.core.resources.IFile;

/**
 * TTA back-end.
 * 
 * @author Herve Yviquel
 * 
 */
public class TTABackendImpl extends AbstractBackend {

	/**
	 * Backend options
	 */
	private boolean classify;

	private boolean debugMode;

	private boolean normalize;

	private final Map<String, String> transformations;

	/**
	 * Creates a new instance of the TTA back-end. Initializes the
	 * transformation hash map.
	 */
	public TTABackendImpl() {
		transformations = new HashMap<String, String>();
		transformations.put("abs", "abs_");
		transformations.put("getw", "getw_");
		transformations.put("index", "index_");
		transformations.put("min", "min_");
		transformations.put("max", "max_");
		transformations.put("select", "select_");
	}

	@Override
	public void doInitializeOptions() {
		classify = getAttribute("net.sf.orcc.backends.classify", false);
		normalize = getAttribute("net.sf.orcc.backends.normalize", false);
		debugMode = getAttribute(DEBUG_MODE, false);
	}

	@Override
	protected void doTransformActor(Actor actor) throws OrccException {
		if (classify) {
			if (!actor.getSimpleName().equals("BufferCurrPic")
					&& !actor.getSimpleName().equals("SplitSpsInfo")) {
				new ActorClassifier().doSwitch(actor);

				if (normalize) {
					new ActorNormalizer().visit(actor);
				}
			}
		}

		ActorVisitor<?>[] transformations = { new SSATransformation(),
				new TypeSizeTransformation(), new BoolToIntTransformation(),
				new PrintlnTransformation(),
				new RenameTransformation(this.transformations),
				new TacTransformation(true), new CastAdder(true),
				new InstPhiTransformation(), new GetElementPtrAdder(),
				new BlockCombine() };

		for (ActorVisitor<?> transformation : transformations) {
			transformation.doSwitch(actor);
		}

		// Organize medata information for the current actor
		actor.setTemplateData(new LLVMTemplateData(actor));
	}

	private void doTransformNetwork(Network network) throws OrccException {
		network.flatten();

		if (classify) {
			new NetworkClassifier().transform(network);
		}
	}

	@Override
	protected void doVtlCodeGeneration(List<IFile> files) throws OrccException {
		// do not generate a VTL
	}

	@Override
	protected void doXdfCodeGeneration(Network network) throws OrccException {
		doTransformNetwork(network);

		printInstances(network);

		// print network
		write("Printing network...\n");
		XDFWriter writer = new XDFWriter();
		writer.write(new File(path), network);
	}

	@Override
	protected boolean printInstance(Instance instance) throws OrccException {
		InstancePrinter printer = new InstancePrinter(project, "LLVM_actor", !debugMode);
		printer.setExpressionPrinter(new LLVMExpressionPrinter());
		printer.setTypePrinter(new LLVMTypePrinter());
		return printer.print(instance.getId() + ".bc", path, instance,
				"instance");
	}

}