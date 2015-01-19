package agent;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import tasks.SimpleTaskImpl;
import behaviour.OntologySenderBehaviour;

import communication.ReadinessOntology;

public class OntologySenderAgent extends Agent {

	private static final long serialVersionUID = 3212214772605221070L;

	private Codec mCodec = new SLCodec();
	private Ontology mOntology = ReadinessOntology.getInstance();
	
	@Override
	public void setup() {
		super.setup();
		getContentManager().registerLanguage(mCodec);
		getContentManager().registerOntology(mOntology);
		
		System.out.println(this.getName() + " setup");
		addBehaviour(new OntologySenderBehaviour(this, new SimpleTaskImpl()));
	}

}
