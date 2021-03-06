package edu.rits.ma.jade.communication;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

public class AgentTrackingOntology extends Ontology {

	private static final long serialVersionUID = -1303696500126979766L;

	public static final String ONTOLOGY_NAME = "tracking-agent-state-ontology";

	public static final String PREDICATE_TRACKING = "Tracking";
	public static final String PREDICATE_TRACKING_AGENT_NAME = "agentName";
	public static final String PREDICATE_TRACKING_STATE = "state";
	
	private static Ontology mInstance = new AgentTrackingOntology();

	public static Ontology getInstance() {
		return mInstance;
	}

	private AgentTrackingOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try {
			//Use predicate schema
			add(new PredicateSchema(PREDICATE_TRACKING), AgentState.class);
			
			//The predicate schema contents (secondary) agent name and its state (ready, not ready, done task...)
			PredicateSchema predicateSchema = (PredicateSchema) getSchema(PREDICATE_TRACKING);
			predicateSchema.add(PREDICATE_TRACKING_AGENT_NAME, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			predicateSchema.add(PREDICATE_TRACKING_STATE, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));

		} catch (OntologyException e) {
			e.printStackTrace();
		}
	}

}
