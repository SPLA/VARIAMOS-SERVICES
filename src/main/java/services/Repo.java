package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import com.google.gson.JsonParser;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import components.github.GHCommit;
import components.github.GHRepo;
import components.github.GHTree;

@Controller
public class Repo {
	
	@CrossOrigin
	@RequestMapping(value="/Repo/test", method=RequestMethod.GET, produces="text/plain")
	@ResponseBody
	public String home() {
		String token = "GITHUB-TOKEN"; 
		//String url = "https://api.github.com/repos/danielgara/test/commits";
		//getHttp(token, url);
		
		//Create repo
		String url2 = "https://api.github.com/user/repos";
		createRepo(token,url2);
		
		//Create commit
		//createCommit(token, "danielgara", "test3", "master");
		
		return "Repo created properly...";
	}
	
	public String getHttp(String token, String url) {
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = getHeaders(token);
	    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	    ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	    return  result.getBody();
	}
	
	public void createRepo(String token, String url){
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = getHeaders(token);
	    
	    GHRepo gh = new GHRepo("test3","first repo","http://danielgara.com");
	    String json = new Gson().toJson(gh);

	    HttpEntity<String> entity = new HttpEntity<String>(json, headers);
	    String result = restTemplate.postForObject(url, entity, String.class);
	    System.out.println(result);
	}
	
	public void createCommit(String token, String user, String repo, String branch){
		//Get branch sha
		String url ="https://api.github.com/repos/"+user+"/"+repo+"/branches/"+branch;
		String res = getHttp(token, url);
		JsonParser parser = new JsonParser();
		JsonObject js_rootObj = parser.parse(res).getAsJsonObject();
		JsonElement js_commit = js_rootObj.get("commit");
		JsonObject js_commitobj = js_commit.getAsJsonObject();
		JsonElement js_sha = js_commitobj.get("sha");
		//parent sha
	    String sha = js_sha.getAsString();
	    
	    //Create tree
	    Resource resource_derived = new ClassPathResource("/uploads/component_pool2/");
	    Map<String, List<GHTree>> hm = GHTree.getTrees(resource_derived);
	    String json = new Gson().toJson(hm); 
	    String url2 ="https://api.github.com/repos/"+user+"/"+repo+"/git/trees";
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = getHeaders(token);
	    HttpEntity<String> entity = new HttpEntity<String>(json, headers);
	    String res2 = restTemplate.postForObject(url2, entity, String.class);
	    JsonObject js_rootTree = parser.parse(res2).getAsJsonObject();
	    JsonElement js_shaTree = js_rootTree.get("sha");
	    //tree sha
	    String shaTree = js_shaTree.getAsString();
	    
	    //Create commit
	    List<String> parent = new ArrayList<String>();
	    parent.add(sha);
	    String url3 ="https://api.github.com/repos/"+user+"/"+repo+"/git/commits";
	    GHCommit ghcom = new GHCommit("best commit",parent,shaTree);
	    String jsonCommit = new Gson().toJson(ghcom);
	    HttpEntity<String> entity2 = new HttpEntity<String>(jsonCommit, headers);
	    String res3 = restTemplate.postForObject(url3, entity2, String.class);
	    JsonObject js_rootCom = parser.parse(res3).getAsJsonObject();
	    JsonElement js_shaCom = js_rootCom.get("sha");
	    //commit sha
	    String shaCom = js_shaCom.getAsString();
	    
	    //Update branch ref
	    String url4 ="https://api.github.com/repos/"+user+"/"+repo+"/git/refs/heads/master";
	    RestTemplate restTemplate2 = new RestTemplate(new HttpComponentsClientHttpRequestFactory()); 
	    Map<String,String> hmref = new HashMap<String,String>();
	    hmref.put("sha",shaCom);
	    String jsonRef = new Gson().toJson(hmref);
	    HttpEntity<String> entity3 = new HttpEntity<String>(jsonRef, headers);
	    ResponseEntity<String> result2 = restTemplate2.exchange(url4, HttpMethod.PATCH, entity3, String.class);
	}
	
	public HttpHeaders getHeaders(String token) {
		token = token + ":x-oauth-basic";
		String authString = "Basic " + Base64.encodeBase64String(token.getBytes());
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set("Authorization", authString);
	    return headers;
	}

}