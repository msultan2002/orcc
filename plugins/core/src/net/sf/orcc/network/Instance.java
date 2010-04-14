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
package net.sf.orcc.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.orcc.OrccException;
import net.sf.orcc.classes.IClass;
import net.sf.orcc.ir.Actor;
import net.sf.orcc.ir.Expression;
import net.sf.orcc.ir.serialize.IRParser;
import net.sf.orcc.network.attributes.IAttribute;
import net.sf.orcc.network.attributes.IAttributeContainer;
import net.sf.orcc.network.serialize.XDFParser;

/**
 * This class defines an instance. An instance has an id, a class, parameters
 * and attributes. The class of the instance points to an actor or a network.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class Instance implements Comparable<Instance>, IAttributeContainer {

	/**
	 * the actor referenced by this instance. May be <code>null</code> if this
	 * instance references a network.
	 */
	private Actor actor;

	/**
	 * attributes
	 */
	private Map<String, IAttribute> attributes;

	/**
	 * the class of this instance
	 */
	private String clasz;

	/**
	 * the absolute path this instance is defined in
	 */
	private File file;

	/**
	 * the id of this instance
	 */
	private String id;

	private boolean isBroadcast;

	private boolean isWrapper;

	/**
	 * the network referenced by this instance. May be <code>null</code> if this
	 * instance references a actor.
	 */
	private Network network;

	/**
	 * the parameters of this instance
	 */
	private Map<String, Expression> parameters;

	/**
	 * Creates a new virtual instance. Only used by subclass Broadcast.
	 * 
	 * @param id
	 *            the instance id
	 * @param clasz
	 *            the instance class
	 * @param parameters
	 *            parameters of this instance
	 */
	protected Instance(String id, String clasz,
			Map<String, Expression> parameters) {
		this.attributes = new HashMap<String, IAttribute>();
		this.clasz = clasz;
		this.id = id;
		this.isBroadcast = true;
		this.parameters = parameters;
	}

	/**
	 * Creates a new virtual instance. Only used by subclass Wrapper in the C++
	 * backend.
	 * 
	 * @param id
	 *            the instance id
	 * @param clasz
	 *            the instance class
	 * @param parameters
	 *            parameters of this instance
	 */
	protected Instance(String id, String clasz,
			Map<String, Expression> parameters,
			Map<String, IAttribute> attributes) {
		this.clasz = clasz;
		this.id = id;
		this.isWrapper = true;
		this.parameters = parameters;
		this.attributes = attributes;
	}

	
	public Instance(String id, Network network) {
		this.id = id;
		this.network = network;
		this.parameters = new HashMap<String, Expression>();
		this.attributes = new HashMap<String, IAttribute>();
	}

	/**
	 * Creates a new instance, and try to load it. The path indicates the path
	 * in which files should be searched.
	 * 
	 * @param path
	 *            the path in which we should look for files
	 * @param id
	 *            the instance id
	 * @param clasz
	 *            the instance class
	 * @param parameters
	 *            parameters of this instance
	 * @param attributes
	 *            a map of attributes
	 * @throws OrccException
	 */
	public Instance(String path, String id, String clasz,
			Map<String, Expression> parameters,
			Map<String, IAttribute> attributes) throws OrccException {
		this.attributes = attributes;
		this.id = id;
		this.parameters = parameters;

		file = new File(path, clasz + ".xdf");
		if (file.exists()) {
			// cool, we got a network
			XDFParser parser = new XDFParser(file.getAbsolutePath());
			network = parser.parseNetwork();

			// update the class from the network declared name
			this.clasz = network.getName();
		} else {
			// not a network => will load later when the instantiate method is
			// called
			this.clasz = clasz;
		}
	}

	@Override
	public int compareTo(Instance instance) {
		return id.compareTo(instance.id);
	}

	/**
	 * Returns the actor referenced by this instance.
	 * 
	 * @return the actor referenced by this instance, or <code>null</code> if
	 *         this instance references a network.
	 */
	public Actor getActor() {
		return actor;
	}

	@Override
	public IAttribute getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public Map<String, IAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * Returns the class of this instance.
	 * 
	 * @return the class of this instance
	 */
	public String getClasz() {
		return clasz;
	}

	/**
	 * Returns the file in which this instance is defined.
	 * 
	 * @return the file in which this instance is defined
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns the identifier of this instance.
	 * 
	 * @return the identifier of this instance
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the network referenced by this instance.
	 * 
	 * @return the network referenced by this instance, or <code>null</code> if
	 *         this instance references an actor.
	 */
	public Network getNetwork() {
		return network;
	}

	/**
	 * Returns the parameters of this instance. This is a reference, not a copy.
	 * 
	 * @return the parameters of this instance
	 */
	public Map<String, Expression> getParameters() {
		return parameters;
	}
	
	/**
	 * Returns the classification class of the instance.
	 * 
	 * @return the classification class of this instance
	 */
	public IClass getContentClass() {
		IClass clasz = null;
		
		if(isActor()) {
			clasz = actor.getActorClass();
		} else {
			clasz = network.getNetworkClass();
		}
		
		return clasz;
	}

	@Override
	public int hashCode() {
		// the hash code of an instance is the hash code of its identifier
		return id.hashCode();
	}

	/**
	 * Tries to load this instance as an actor.
	 * 
	 * @param path
	 *            the path where actors should be looked up
	 * 
	 * @throws OrccException
	 */
	public void instantiate(String path) throws OrccException {
		String className = new File(clasz).getName();
		actor = Network.getActorFromPool(className);
		if (actor == null) {
			// try and load the actor
			file = new File(path, className + ".json");
			try {
				InputStream in = new FileInputStream(file);
				actor = new IRParser().parseActor(in);
				Network.putActorInPool(className, actor);
			} catch (OrccException e) {
				throw new OrccException("Could not parse instance \"" + id
						+ "\" because: " + e.getLocalizedMessage(), e);
			} catch (FileNotFoundException e) {
				throw new OrccException("I/O error when parsing \"" + id + "\"", e);
			}
		}
	}

	/**
	 * Returns true if this instance references an actor.
	 * 
	 * @return true if this instance references an actor.
	 */
	public boolean isActor() {
		return (actor != null);
	}

	/**
	 * Returns true if this instance is a broadcast.
	 * 
	 * @return true if this instance is a broadcast
	 */
	public boolean isBroadcast() {
		return isBroadcast;
	}

	/**
	 * Returns true if this instance references a network.
	 * 
	 * @return true if this instance references a network.
	 */
	public boolean isNetwork() {
		return (network != null);
	}

	/**
	 * Returns true if this instance is a wrapper.
	 * 
	 * @return true if this instance is a wrapper
	 */
	public boolean isWrapper() {
		return isWrapper;
	}

	public void setClasz(String clasz) {
		this.clasz = clasz;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "\"" + id + "\" instance of \"" + clasz + "\"";
	}

}
