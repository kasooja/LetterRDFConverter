package converter;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gate.Annotation;
import gate.util.InvalidOffsetException;
import ontology.TurtleFormatRDFWriter;
import reader.GateAnnotationReader;

public class LetterXMLToRDFConverter {

	//	private Properties config = new Properties();
	private List<String> labelsToBeUsed = new ArrayList<String>();

	private GateAnnotationReader gateAnnoReader = new GateAnnotationReader();

	public LetterXMLToRDFConverter() {
	}

	//	private void loadConfig(String configFilePath){
	//		try {
	//			config.load(new FileInputStream(configFilePath));
	//		} catch (FileNotFoundException e) {
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}			
	//	}	

	//	private void setConfig(String propertyName, List<String> labelsToBeUsed){
	//		String labelsString = config.getProperty(propertyName);
	//		if(labelsString!=null && !"".equals(labelsString.trim())){
	//			String[] split = labelsString.trim().split(",");
	//			for(String label : split){
	//				label = label.trim();
	//				if(label.equalsIgnoreCase("all")){
	//					labelsToBeUsed = null;
	//					break;
	//				}
	//				labelsToBeUsed.add(label.trim());
	//			}
	//		}
	//	}

	public Map<String, String> createTurtleFile(String annotatedGateFile) {
	//	String turtleFileName = "src/main/resources/MashamtoLeibniz1.ttl";
	//	StringBuilder turtleFileContent = new StringBuilder();
		Map<String, String> map = new HashMap<String, String>();
		String annotationSetName = null;		
		gateAnnoReader.setDocument(annotatedGateFile);
		labelsToBeUsed = new ArrayList<String>();
		labelsToBeUsed.add("Argument");
		labelsToBeUsed.add("Charactarisation");	
		labelsToBeUsed.add("CitedWork");
		labelsToBeUsed.add("Closing");	
		labelsToBeUsed.add("Day");	labelsToBeUsed.add("Exchange");	
		labelsToBeUsed.add("IntellectualConcept");	labelsToBeUsed.add("IntellectualQuestion");
		labelsToBeUsed.add("Letter");	labelsToBeUsed.add("Location");	
		labelsToBeUsed.add("Month");	labelsToBeUsed.add("Opening");	
		labelsToBeUsed.add("Person");	labelsToBeUsed.add("Year");
		
		Map<String, List<Annotation>> annotations = gateAnnoReader.getGateAnnotationsLabelTagged(labelsToBeUsed, annotationSetName);
		TurtleFormatRDFWriter turtle = new TurtleFormatRDFWriter();
		String ontoUri = "http://www.semanticweb.org/ioanna/ontologies/2016/5/Intelcorrespondent";
		turtle.addBasePrefix(ontoUri);
		for(String annoType : annotations.keySet()) {			
			List<Annotation> annoTypeAnnotations = annotations.get(annoType);
			for(Annotation annotation : annoTypeAnnotations) {				
				try {
					String content = gateAnnoReader.getDocument().getContent().getContent(annotation.getStartNode().getOffset(), annotation.getEndNode().getOffset()).toString();
					String tag = ":" + annotation.getType();					
					turtle.addRDF(content, tag);
				} catch (InvalidOffsetException e) {
					e.printStackTrace();
				}
			}			
		}
		turtle.write("src/main/resources/MashamtoLeibniz1.ttl");
		gateAnnoReader.cleanUp();
		return map;
	}

	public static void main(String[] args) {
		String letterGateFile = "src/main/resources/MashamtoLeibniz1.xml";

		LetterXMLToRDFConverter reader = new LetterXMLToRDFConverter();
		reader.createTurtleFile(letterGateFile);		
	}

}
