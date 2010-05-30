/*
 * Copyright (c) 2009, IETR/INSA of Rennes
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
package net.sf.orcc.ir;

import net.sf.orcc.OrccRuntimeException;
import net.sf.orcc.ir.expr.ExpressionEvaluator;

/**
 * This class represents a global variable. A global variable is a variable that
 * is not assignable, may have a value as an expression, which should be
 * constant when evaluated.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class GlobalVariable extends Variable {

	/**
	 * variable constant value.
	 */
	protected Object initialValue;

	/**
	 * Creates a new global variable from the given global variable.
	 * 
	 * @param variable
	 *            a global variable
	 */
	public GlobalVariable(GlobalVariable variable) {
		super(variable);
		this.initialValue = variable.initialValue;
	}

	/**
	 * Creates a new global variable with the given location, type, and name.
	 * The global variable created will have no value.
	 * 
	 * @param location
	 *            the global variable location
	 * @param type
	 *            the global variable type
	 * @param name
	 *            the global variable name
	 */
	public GlobalVariable(Location location, Type type, String name) {
		this(location, type, name, null);
	}

	/**
	 * Creates a new global variable with the given location, type, name, and
	 * initial value.
	 * 
	 * @param location
	 *            the global variable location
	 * @param type
	 *            the global variable type
	 * @param name
	 *            the global variable name
	 * @param value
	 *            the value of the global variable
	 */
	public GlobalVariable(Location location, Type type, String name,
			Expression value) {
		super(location, type, name, true, value);
	}

	/**
	 * Evaluates this variable's value to a constant.
	 * 
	 * @throws OrccRuntimeException
	 *             if the value could not be evaluated to a constant
	 */
	public void evaluate() {
		if (hasExpression()) {
			initialValue = getExpression().accept(new ExpressionEvaluator());
		}
	}

	/**
	 * Returns the initial value of this global variable, or <code>null</code>
	 * if this variable has no initial constant value. The initial value may be
	 * a boolean, an integer, a list or a string.
	 * 
	 * @return an object, or <code>null</code> if this variable has no constant
	 *         value
	 */
	public Object getConstantValue() {
		return initialValue;
	}

}
