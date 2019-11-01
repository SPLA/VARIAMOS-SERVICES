package components.github;

public class GHRepo {
	String name = "";
	String description = "";
	String homepage = "";
	Boolean auto_init = true;
	
	public GHRepo(){
		
	}
	
	public GHRepo(String na, String desc, String hm){
		name=na;
		description=desc;
		homepage=hm;
	}
	
}
