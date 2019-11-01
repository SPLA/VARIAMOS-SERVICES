package components.github;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GHCommit {
	String message = "";
	List<String> parents = new ArrayList<String>();
	String tree = "";
	
	public GHCommit() {
		
	}
	
	public GHCommit(String me, List<String> pa, String tr) {
		this.message=me;
		this.parents=pa;
		this.tree=tr;
	}
}
