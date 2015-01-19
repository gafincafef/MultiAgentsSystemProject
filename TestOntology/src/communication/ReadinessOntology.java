package communication;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;
import tasks.ReadyStatus;

public class ReadinessOntology extends Ontology {

	private static final long serialVersionUID = -8068647241230541126L;

	public static final String READINESS_ONTOLOGY_NAME = "secondary-agent-readiness-ontology";
	
	public static final String READINESS = "Readiness";
	public static final String READINESS_AGENT_NAME = "agentName";
	public static final String READINESS_READY_STATUS = "readyStatus";
	
	private static Ontology mInstance = new ReadinessOntology();
	
	public static Ontology getInstance() {
		return mInstance;
	}
	
	private ReadinessOntology() {
		super(READINESS_ONTOLOGY_NAME, BasicOntology.getInstance());
		try {
			add(new PredicateSchema(READINESS), ReadyStatus.class);
			PredicateSchema predicateSchema = (PredicateSchema) getSchema(READINESS);
			predicateSchema.add(READINESS_AGENT_NAME, (PrimitiveSchema)getSchema(BasicOntology.STRING));
			predicateSchema.add(READINESS_READY_STATUS, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));

		} catch (OntologyException e) {
			e.printStackTrace();
		}
	}

}
