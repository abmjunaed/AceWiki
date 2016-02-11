package importer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;



	public class VerbalizerWebserviceTest {

		private static final String OWL_TO_ACE_WS_URL = "http://attempto.ifi.uzh.ch/service/owl_verbalizer/owl_to_ace";
		private static final String OWL_TO_ACE_WS_URL_LOCALHOST = "http://localhost:5123";

		String owl ="<?xml version=\"1.0\"?>\n" +
				"<Ontology\n" +
				"	xmlns=\"http://www.w3.org/2002/07/owl#\"\n" +
				"	xml:base=\"http://org.semanticweb.ontologies/ont\"\n" +
				"	xmlns:xml=\"http://www.w3.org/XML/1998/namespace\">\n" +
				"	<Prefix name=\"t\" IRI=\"http://www.example.org/t.owl#\"/>\n" +
				"	<Prefix name=\"equipments2monuments\" IRI=\"http://localhost:9080/airbus/vocabularies/modus2place/A350/equipments2monuments/\"/> \n" +
				" 	<Declaration>\n" +
		        "		<DataProperty abbreviatedIRI=\"equipments2monuments:width\"/>\n" +
		        "	</Declaration> \n" +
				"	<SubClassOf>\n" +
				"		<Class IRI=\"/CCCA\"/>\n" +
				"		<Class IRI=\"/Monument\"/>\n" +
				"	</SubClassOf>\n" +
				"	<SubClassOf>\n" +
				"		<Class IRI=\"/TransversalGalley\"/>\n" +
				"		<Class IRI=\"/Galley\"/>\n" +				
				"	</SubClassOf>\n" +
				"<SubClassOf>\n" +
					"<Class IRI=\"/Lavatory-A\"/>\n" +
					"<ObjectIntersectionOf>\n" +
						"<DataHasValue>\n" +
							"<DataProperty abbreviatedIRI=\"equipments2monuments:depth\"/>\n" +
							"<Literal datatypeIRI=\"&xsd;float\">53.0</Literal>\n" +
						"</DataHasValue>\n" +
						"<DataHasValue>\n" +
							"<DataProperty abbreviatedIRI=\"equipments2monuments:width\"/>\n" +
							"<Literal datatypeIRI=\"&xsd;float\">41.0</Literal>\n" +
						"</DataHasValue>\n" +
					"</ObjectIntersectionOf>\n" +
				"</SubClassOf>" +
				"</Ontology>\n";
		
		private static final String IN = "<?xml version=\"1.0\"?>\n" +
		"<Ontology\n" +
		"	xmlns=\"http://www.w3.org/2002/07/owl#\"\n" +
		"	xml:base=\"http://org.semanticweb.ontologies/ont\"\n" +
		"	xmlns:xml=\"http://www.w3.org/XML/1998/namespace\">\n" +
		"	<Prefix name=\"t\" IRI=\"http://www.example.org/t.owl#\"/>\n" +
		"	<SubClassOf>\n" +
		"		<Class abbreviatedIRI=\"t:man\"/>\n" +
		"		<Class abbreviatedIRI=\"t:human\"/>\n" +
		"	</SubClassOf>\n" +
		"</Ontology>\n";

		private static final String IN_UNDEF_PREFIX = "<?xml version=\"1.0\"?>\n" +
		"<Ontology\n" +
		"	xmlns=\"http://www.w3.org/2002/07/owl#\"\n" +
		"	xml:base=\"http://org.semanticweb.ontologies/ont\"\n" +
		"	xmlns:xml=\"http://www.w3.org/XML/1998/namespace\">\n" +
		"	<SubClassOf>\n" +
		"		<Class abbreviatedIRI=\"undef:man\"/>\n" +
		"		<Class abbreviatedIRI=\"undef:human\"/>\n" +
		"	</SubClassOf>\n" +
		"</Ontology>\n";

		private static final String OUT = "Every man is a human.";

		private static final String OUT_UNDEF_PREFIX = "<error type=\"existence_error\">existence_error(variable,undef): system:nb_getval/2: </error>";

		private static final String OUT_CSV = "" +
		"ignored\tPrefix(t,http://www.example.org/t.owl#)\n\n" +
		"f\tEvery\n" +
		"cn_sg\thttp://www.example.org/t.owl#man\n" +
		"f\tis\n" +
		"f\ta\n" +
		"cn_sg\thttp://www.example.org/t.owl#human\n" +
		"f\t.\n\n";

		@Test
		public final void testCall() {
			Verbalizer verbalizer = new Verbalizer();
			verbalizer.setVerbalizerWebservice(OWL_TO_ACE_WS_URL_LOCALHOST);
			String response = verbalizer.call(IN);
			assertEquals(OUT, response.trim());
		}

		@Test
		public final void testCall1() {
			Verbalizer verbalizer = new Verbalizer();
			verbalizer.setVerbalizerWebservice(OWL_TO_ACE_WS_URL_LOCALHOST);
			String response = verbalizer.call(IN);
			assertEquals(OUT, response.trim());
		}

		@Test
		public final void testCall2() {
			Verbalizer verbalizer = new Verbalizer();
			verbalizer.setVerbalizerWebservice(OWL_TO_ACE_WS_URL_LOCALHOST);
			String response = verbalizer.call(IN_UNDEF_PREFIX);
			assertEquals(OUT_UNDEF_PREFIX, response.trim());
		}

		@Test
		public final void testCall3() {
			Verbalizer verbalizer = new Verbalizer();
			verbalizer.setVerbalizerWebservice(OWL_TO_ACE_WS_URL_LOCALHOST);
			String response = verbalizer.call(IN, OutputType.CSV);
			System.out.println(response);
			assertEquals(OUT_CSV, response);
		}
	}
