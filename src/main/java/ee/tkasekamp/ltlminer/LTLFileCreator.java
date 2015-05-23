package ee.tkasekamp.ltlminer;

import java.util.ArrayList;

public class LTLFileCreator {
	/**
	 * Creates a proper LTL model from all the input rules. Adds necessary stuff
	 * to the front.
	 * 
	 * @param rules
	 * @return LTL model ready to be analysed
	 */
	public static String createLTLModel(ArrayList<String> rules) {
		StringBuilder model = new StringBuilder();
		// Add more if necessary
		model.append("set ate.EventType; set ate.WorkflowModelElement;");
		model.append("rename ate.EventType as eventtype;");
		model.append("rename ate.WorkflowModelElement as activity; \n");
		for (String string : rules) {
			model.append(string);
			model.append("\n");
		}
		return model.toString();
	}
}
