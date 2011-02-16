/*
 * Copyright (c) 2010, IETR/INSA of Rennes
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
package net.sf.orcc.tools.merger2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.orcc.network.Connection;
import net.sf.orcc.network.Network;
import net.sf.orcc.network.Vertex;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * This class find the possible static path between two vertex of a graph.
 * 
 * 
 * @author J�r�me Gorin
 * 
 */
public class StaticRegion {
	private DirectedGraph<Vertex, Connection> dynamicGraph;
	Map<Vertex, Integer> repetitionVector;
	private DirectedGraph<Vertex, DefaultWeightedEdge> staticGraph;
	Set<Vertex> vertices;
	
	public StaticRegion(Network network, DirectedGraph<Vertex, DefaultWeightedEdge> staticGraph, Set<Vertex> region){
		this.dynamicGraph = network.getGraph();
		this.staticGraph = staticGraph;
		this.vertices = region;
	}
	
	public Boolean isValid(){
		List<Vertex> candidates = new ArrayList<Vertex>(vertices);
		
		while (candidates.size() > 1){
			Vertex candidate1 = candidates.get(0);
			for(DefaultWeightedEdge edge : staticGraph.outgoingEdgesOf(candidate1)){
				Vertex candidate2 = staticGraph.getEdgeTarget(edge);
				
			}
		}
		
		return true;
	}
}
