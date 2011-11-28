/*
 * Copyright (c) 2009-2011, IETR/INSA of Rennes
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
package net.sf.orcc.df.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.orcc.df.Actor;
import net.sf.orcc.df.Argument;
import net.sf.orcc.df.Attribute;
import net.sf.orcc.df.Broadcast;
import net.sf.orcc.df.DfPackage;
import net.sf.orcc.df.Entity;
import net.sf.orcc.df.Instance;
import net.sf.orcc.df.Network;
import net.sf.orcc.df.SerDes;
import net.sf.orcc.moc.MoC;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * This class defines an instance. An instance has an id, a class, parameters
 * and attributes. The class of the instance points to an actor or a network.
 * 
 * @author Matthieu Wipliez
 * @generated
 */
public class InstanceImpl extends VertexImpl implements Instance {

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getArguments() <em>Arguments</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArguments()
	 * @generated
	 * @ordered
	 */
	protected EList<Argument> arguments;

	/**
	 * The cached value of the '{@link #getAttributes() <em>Attributes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributes()
	 * @generated
	 * @ordered
	 */
	protected EList<Attribute> attributes;

	/**
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected Entity entity;

	/**
	 * The cached value of the '{@link #getHierarchy() <em>Hierarchy</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHierarchy()
	 * @generated
	 * @ordered
	 */
	protected EList<Instance> hierarchy;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entity basicGetEntity() {
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DfPackage.INSTANCE__ATTRIBUTES:
				return getAttributes();
			case DfPackage.INSTANCE__ID:
				return getId();
			case DfPackage.INSTANCE__ARGUMENTS:
				return getArguments();
			case DfPackage.INSTANCE__HIERARCHY:
				return getHierarchy();
			case DfPackage.INSTANCE__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DfPackage.INSTANCE__ATTRIBUTES:
				return ((InternalEList<?>)getAttributes()).basicRemove(otherEnd, msgs);
			case DfPackage.INSTANCE__ARGUMENTS:
				return ((InternalEList<?>)getArguments()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DfPackage.INSTANCE__ATTRIBUTES:
				return attributes != null && !attributes.isEmpty();
			case DfPackage.INSTANCE__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case DfPackage.INSTANCE__ARGUMENTS:
				return arguments != null && !arguments.isEmpty();
			case DfPackage.INSTANCE__HIERARCHY:
				return hierarchy != null && !hierarchy.isEmpty();
			case DfPackage.INSTANCE__ENTITY:
				return entity != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DfPackage.INSTANCE__ATTRIBUTES:
				getAttributes().clear();
				getAttributes().addAll((Collection<? extends Attribute>)newValue);
				return;
			case DfPackage.INSTANCE__ID:
				setId((String)newValue);
				return;
			case DfPackage.INSTANCE__ARGUMENTS:
				getArguments().clear();
				getArguments().addAll((Collection<? extends Argument>)newValue);
				return;
			case DfPackage.INSTANCE__HIERARCHY:
				getHierarchy().clear();
				getHierarchy().addAll((Collection<? extends Instance>)newValue);
				return;
			case DfPackage.INSTANCE__ENTITY:
				setEntity((Entity)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DfPackage.Literals.INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DfPackage.INSTANCE__ATTRIBUTES:
				getAttributes().clear();
				return;
			case DfPackage.INSTANCE__ID:
				setId(ID_EDEFAULT);
				return;
			case DfPackage.INSTANCE__ARGUMENTS:
				getArguments().clear();
				return;
			case DfPackage.INSTANCE__HIERARCHY:
				getHierarchy().clear();
				return;
			case DfPackage.INSTANCE__ENTITY:
				setEntity((Entity)null);
				return;
		}
		super.eUnset(featureID);
	}

	@Override
	public Actor getActor() {
		return (Actor) getEntity();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Argument> getArguments() {
		if (arguments == null) {
			arguments = new EObjectContainmentEList<Argument>(Argument.class, this, DfPackage.INSTANCE__ARGUMENTS);
		}
		return arguments;
	}

	@Override
	public Attribute getAttribute(String name) {
		for (Attribute attribute : getAttributes()) {
			if (name.equals(attribute.getName())) {
				return attribute;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Attribute> getAttributes() {
		if (attributes == null) {
			attributes = new EObjectContainmentEList<Attribute>(Attribute.class, this, DfPackage.INSTANCE__ATTRIBUTES);
		}
		return attributes;
	}

	@Override
	public Broadcast getBroadcast() {
		return (Broadcast) getEntity();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entity getEntity() {
		if (entity != null && entity.eIsProxy()) {
			InternalEObject oldEntity = (InternalEObject)entity;
			entity = (Entity)eResolveProxy(oldEntity);
			if (entity != oldEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DfPackage.INSTANCE__ENTITY, oldEntity, entity));
			}
		}
		return entity;
	}

	/**
	 * Returns the path of identifiers from the top-level to this instance.
	 * 
	 * @return the path of identifiers from the top-level to this instance
	 */
	public List<String> getHierarchicalId() {
		List<String> ids = new ArrayList<String>();
		for (Instance instance : getHierarchy()) {
			ids.add(instance.getId());
		}
		ids.add(getId());
		return ids;
	}

	@Override
	public String getHierarchicalPath() {
		StringBuilder builder = new StringBuilder();
		for (String id : getHierarchicalId()) {
			builder.append('/');
			builder.append(id);
		}

		return builder.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Instance> getHierarchy() {
		if (hierarchy == null) {
			hierarchy = new EObjectResolvingEList<Instance>(Instance.class, this, DfPackage.INSTANCE__HIERARCHY);
		}
		return hierarchy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	@Override
	public MoC getMoC() {
		if (isActor()) {
			return getActor().getMoC();
		} else if (isNetwork()) {
			return getNetwork().getMoC();
		} else {
			return null;
		}
	}

	@Override
	public Network getNetwork() {
		return (Network) getEntity();
	}

	@Override
	public SerDes getWrapper() {
		return (SerDes) getEntity();
	}

	@Override
	public boolean isActor() {
		return (getEntity() instanceof Actor);
	}

	@Override
	public boolean isBroadcast() {
		return (getEntity() instanceof Broadcast);
	}

	@Override
	public boolean isInstance() {
		return true;
	}

	@Override
	public boolean isNetwork() {
		return (getEntity() instanceof Network);
	}

	@Override
	public boolean isWrapper() {
		return (getEntity() instanceof SerDes);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntity(Entity newEntity) {
		Entity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DfPackage.INSTANCE__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DfPackage.INSTANCE__ID, oldId, id));
	}

	@Override
	public String toString() {
		return id + ": " + getEntity();
	}

}
