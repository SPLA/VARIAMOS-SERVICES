package coffee.modelParsers.basicHLVLPackage;

import java.util.List;

public class CnfExpFactory implements HlvlBasicKeys{

	public String getCNF2expression(List<String> positives, List<String> negatives, int numId, String id) {
		String out= id+ (numId++) + COLON+  EXPRESSION+ OPEN_CALL;

		for(String element: negatives){
			out +=  NEG+ element+ SPACE+  L_OR + SPACE;
		}
		for(String element: positives){
			out +=  element+ SPACE+ L_OR + SPACE;
		}

		out = out.substring(0, out.length() -3)+ CLOSE_CALL + "\n";

		return out; 
	}
}
