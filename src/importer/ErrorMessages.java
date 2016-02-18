package importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import ch.uzh.ifi.attempto.ape.OutputType;

/**
 * Stores the error messages returned by owl-verbalizer. It gives following error messages
 * unsupported, ignored, comment/* BUG:
 * 
 * @author rmajunae
 *
 */
public class ErrorMessages {
	
	/**
	 * 
	 */
	public static Set<String>errorMsgTypes;
	public static Map<String,ArrayList<String>> errorMessages = new HashMap<String, ArrayList<String>>();//store error message for each errorMsgTypes
	static {
		errorMsgTypes= Sets.newHashSet("unsupported","ignored","comment","others");
		// create arraylist for each errorMsgTypes, to store the error messages
		for (String type : errorMsgTypes) {
			errorMessages.put(type, new ArrayList<String>());
		}
	}
	
}
