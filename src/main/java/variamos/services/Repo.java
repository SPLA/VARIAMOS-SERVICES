package variamos.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.print.DocFlavor.STRING;

import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.DirectContainer;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.web.bind.annotation.*;
import org.antlr.v4.parse.ANTLRParser.blockEntry_return;
import org.apache.http.HttpStatus;
import org.apache.tomcat.jni.Directory;
import org.apache.tomcat.util.bcel.Const;
import org.apache.tomcat.util.codec.binary.Base64;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.stringtemplate.v4.compiler.CodeGenerator.primary_return;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import components.github.GHCommit;
import components.github.GHVCsSettings;
import components.github.GHRepo;
import components.github.GHTree;

@RestController
@EnableAutoConfiguration
@RequestMapping("/repository")
public class Repo {
	// GitHub Access Token. It shuuld by changed for secrets management at CD
	// pipeline level.
	private final String REPOSITORY_TOKEN = "c1434eaa735d88bf83f923d35b7487fc1df25444";
	private final String REPOSITORY_API_URL = "https://api.github.com/";
	private final String REPOSITORY_ROOT = "target/classes/gitrepositories/";

	@CrossOrigin
	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "text/plain")
	@ResponseBody
	public String home() {

		String result = "";

		return result;
	}

	public String getHttp(String url) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = getHeaders(this.REPOSITORY_TOKEN);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return result.getBody();
	}

	/**
	 * Method that creates a remote repository with GitHub REST API V 3.0
	 * 
	 * @param repositoryName        Short repository name
	 * @param repositoryDescription Description about repository code contents
	 * @param repositoryUrl         Documentation respository URL
	 * @return Status mesaje about the repository creation operation
	 */
	public String createRepo(String repositoryName, String repositoryDescription, String repositoryUrl,
			String gitHubPAT) {

		String result = "";

		try {

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = getHeaders(gitHubPAT);
			GHRepo pocoRepository = new GHRepo(repositoryName, repositoryDescription, repositoryUrl);
			String jsonRepository = new Gson().toJson(pocoRepository);

			HttpEntity<String> entity = new HttpEntity<String>(jsonRepository, headers);
			result = restTemplate.postForObject(this.REPOSITORY_API_URL + "user/repos", entity, String.class);

		} catch (Exception exception) {

			result = exception.getMessage();

		}

		return result;

	}

	/***
	 * Deprecated, becouse the JGit API has all the commands to work with git.
	 * 
	 * @param repositoryUrl Repository URL for clonation
	 * @param gitHubPAT     Github Personal Access Token
	 * @return Empty string if repository was cloned succesfully
	 */
	public String cloneRepository(String repositoryUrl, String repositoryName, String gitHubPAT) {

		try {

			File repositoryDirectory = new File(REPOSITORY_ROOT + repositoryName);
			if (!repositoryDirectory.exists()) {
				repositoryDirectory.mkdir();
			}

			Git.cloneRepository().setURI(repositoryUrl).setDirectory(repositoryDirectory)
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider("token", gitHubPAT)).call();

		} catch (Exception ex) {
			return "ERROR";
		}

		return "";

	}

	public void createCommit(String user, String repo, String branch) {
		// Get branch sha
		String url = this.REPOSITORY_API_URL + "/repos/" + user + "/" + repo + "/branches/" + branch;
		String res = getHttp(url);
		JsonParser parser = new JsonParser();
		JsonObject js_rootObj = parser.parse(res).getAsJsonObject();
		JsonElement js_commit = js_rootObj.get("commit");
		JsonObject js_commitobj = js_commit.getAsJsonObject();
		JsonElement js_sha = js_commitobj.get("sha");
		// parent sha
		String sha = js_sha.getAsString();

		// Create tree
		Resource resource_derived = new ClassPathResource("/uploads/component_pool2/");
		Map<String, List<GHTree>> hm = GHTree.getTrees(resource_derived);
		String json = new Gson().toJson(hm);
		String url2 = this.REPOSITORY_API_URL + "/repos/" + user + "/" + repo + "/git/trees";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = getHeaders(this.REPOSITORY_TOKEN);
		HttpEntity<String> entity = new HttpEntity<String>(json, headers);
		String res2 = restTemplate.postForObject(url2, entity, String.class);
		JsonObject js_rootTree = parser.parse(res2).getAsJsonObject();
		JsonElement js_shaTree = js_rootTree.get("sha");
		// tree sha
		String shaTree = js_shaTree.getAsString();

		// Create commit
		List<String> parent = new ArrayList<String>();
		parent.add(sha);
		String url3 = this.REPOSITORY_API_URL + "/repos/" + user + "/" + repo + "/git/commits";
		GHCommit ghcom = new GHCommit("best commit", parent, shaTree);
		String jsonCommit = new Gson().toJson(ghcom);
		HttpEntity<String> entity2 = new HttpEntity<String>(jsonCommit, headers);
		String res3 = restTemplate.postForObject(url3, entity2, String.class);
		JsonObject js_rootCom = parser.parse(res3).getAsJsonObject();
		JsonElement js_shaCom = js_rootCom.get("sha");
		// commit sha
		String shaCom = js_shaCom.getAsString();

		// Update branch ref
		String url4 = this.REPOSITORY_API_URL + "repos/" + user + "/" + repo + "/git/refs/heads/master";
		RestTemplate restTemplate2 = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		Map<String, String> hmref = new HashMap<String, String>();
		hmref.put("sha", shaCom);
		String jsonRef = new Gson().toJson(hmref);
		HttpEntity<String> entity3 = new HttpEntity<String>(jsonRef, headers);
		ResponseEntity<String> result2 = restTemplate2.exchange(url4, HttpMethod.PATCH, entity3, String.class);
	}

	/**
	 * Constructs the headers needed for GitHub repository operations
	 * 
	 * @return HttpHeader for GitHub API calls
	 */
	public HttpHeaders getHeaders(String gitHubPAT) {

		HttpHeaders headers = new HttpHeaders();
		headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + gitHubPAT);
		headers.add("Content-Type", "application/json");
		return headers;

	}

	/**
	 * Controller method that creates a Git repository Example of controller method
	 * call:
	 * [VARIAMOS-SERVICEURL]/repository/create?repositoryName=[myRepositoryName]&repositoryDescription=[myRepositoryDescription]&repositoryUrl=[myRepositoryUrl]
	 * http://localhost:8080/repository/create?repositoryName=myRepo001&repositoryDescription=Repository
	 * for test
	 * pourposes&repositoryUrl=https://dev.azure.com/jsoto25/Maestr%C3%ADa%20SPLE/_wiki/wikis/Feature-Plants.wiki/89/Maestr%C3%ADa-SPLE
	 * 
	 * @param repositoryName Repository name to be created at the GitHub account
	 *                       configured at server level. * @param
	 *                       repositoryDescription Repository code contents
	 *                       description. * @param repositoryUrl URL for repository
	 *                       documentation.
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/create", method = RequestMethod.GET, produces = "text/plain")
	@ResponseBody
	public String createRepository(@RequestParam String repositoryName, @RequestParam String repositoryDescription,
			@RequestParam String repositoryUrl) {

		String result = this.createRepo(repositoryName, repositoryDescription, repositoryUrl, this.REPOSITORY_TOKEN);
		return result;
	}

	/**
	 * 
	 * @param cvsSettings
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "/validate", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String validateCVsConfig(@RequestBody String cvsSettingsJson) {
		GHVCsSettings cvsSettings = new Gson().fromJson(cvsSettingsJson, GHVCsSettings.class);
		return cvsSettings.domainRepository;

	}

	/**
	 * 1. Creates domain repositories based on cvsConfig 2. Clones the repository
	 * locally 3. Add sple.json file 4. Push the changes to the repository created
	 * to the origin repository 5. Inlcuir modelo de características 6. Incluir el
	 * pool de componentes 7. Push
	 * 
	 * @param cvsSettings
	 * @return If OK returns domain repository url If repository already exist ti
	 *         returns 0 lengt string If Error, retunrs ERROR
	 */
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "/domainrepositoryinitialization", consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String domainRepositoryInitialization(@RequestBody String cvsSettingsJson) {

		/** Repository URL after creation */
		String repositoryUrl = "";
		String gitHubPath = "";

		if (cvsSettingsJson.contains("cvs_settings")) {
			cvsSettingsJson = cvsSettingsJson.substring(16, cvsSettingsJson.length() - 1);
		}

		GHVCsSettings cvsSettings = new Gson().fromJson(cvsSettingsJson, GHVCsSettings.class);
		gitHubPath = cvsSettings.githubPat;

		String repositoryName = cvsSettings.domainRepositoryPrefix + cvsSettings.productLineName.replaceAll(" ","-")
				+ cvsSettings.domainRepositorySufix;
		String result = this.createRepo(repositoryName, cvsSettings.productLineName,
				cvsSettings.productLineName + " domain repository ", gitHubPath);

		try {
			repositoryUrl = (new Gson().fromJson(result, JsonElement.class)).getAsJsonObject().get("html_url")
					.toString().replace("\"", "");
			result = repositoryUrl;
		} catch (JsonSyntaxException ex) {

			if (result.toUpperCase().indexOf("ALREADY EXISTS") > -1) {
				result = "";
			} else {
				result = "ERROR";
			}

		}

		if (result.indexOf("ERROR") < 0) {
			// this.cloneRepository(repositoryUrl,repositoryName,cvsSettings.githubPat);

			try {

				File repositoryDirectory = new File(REPOSITORY_ROOT + repositoryName);
				if (!repositoryDirectory.exists()) {
					repositoryDirectory.mkdir();
				}

				Git git = Git.cloneRepository().setURI(repositoryUrl).setDirectory(repositoryDirectory)
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider("token", gitHubPath)).call();

				cvsSettings.githubPat = "";
				cvsSettings.githubUser = "";
				cvsSettings.domainRepository = repositoryUrl;
				String spleFilePath = REPOSITORY_ROOT + repositoryName + "/sple.json";
				BufferedWriter spleWriter = new BufferedWriter(new FileWriter(spleFilePath));
				spleWriter.write(new Gson().toJson(cvsSettings));
				spleWriter.close();

				git.add().addFilepattern("sple.json").call();
				git.commit().setMessage("sple.json initial version").call();
				git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider("token", gitHubPath)).call();

			} catch (IOException ex) {
				result = "ERROR";
			} catch (GitAPIException ex) {
				result = "ERROR";
			}

		}

		return result;

	}

	/**
	 * 1. Add sple.json to commit
	 * 2. Add component pool to commit
	 * 3. Add domain models to commit
	 * 4. Creates the commit
	 * 5. Push the changes
	 * 
	 * @param cvsSettings
	 * @return If OK returns cero length string. Otherwise retunrs ERROR
	 */
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "/domainrepositoryupdate", consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String domainRepositoryUpdate(@RequestBody String cvsSettingsJson) {

		
		String gitHubPath = "";

		if (cvsSettingsJson.contains("cvs_settings")) {
			cvsSettingsJson = cvsSettingsJson.substring(16, cvsSettingsJson.length() - 1);
		}

		GHVCsSettings cvsSettings = new Gson().fromJson(cvsSettingsJson, GHVCsSettings.class);
		String repositoryUrl = cvsSettings.domainRepository;
		gitHubPath = cvsSettings.githubPat;

		String repositoryName = cvsSettings.domainRepositoryPrefix + cvsSettings.productLineName.replaceAll(" ","-")
				+ cvsSettings.domainRepositorySufix;

		String result = "";
		
		try {

			File repositoryDirectory = new File(REPOSITORY_ROOT + repositoryName + "/.git");
			String commitMessage = cvsSettings.lastCommintMessage;
			cvsSettings.lastCommintMessage = "";
			String modelContent = cvsSettings.modelContent;
			cvsSettings.modelContent = "";
			String modelName = cvsSettings.modelName;
			cvsSettings.modelName = "";
			if(modelName.length()==0)
			{
				modelName = "domainModel.xml";
			}
			Git git = Git.open(repositoryDirectory);
			cvsSettings.githubPat = "";
			cvsSettings.githubUser = "";
			cvsSettings.domainRepository = repositoryUrl;
			String spleFilePath = REPOSITORY_ROOT + repositoryName + "/sple.json";
			BufferedWriter spleWriter = new BufferedWriter(new FileWriter(spleFilePath));
			spleWriter.write(new Gson().toJson(cvsSettings));
			spleWriter.close();
			String domainModelFilePath = REPOSITORY_ROOT + repositoryName + "/domain/models/" + modelName;
			BufferedWriter domainModelWriter = new BufferedWriter(new FileWriter(domainModelFilePath));
			domainModelWriter.write(modelContent);
			domainModelWriter.close();
			git.add().addFilepattern(".").call();
			git.commit().setAll(true).setMessage(commitMessage).call();
			git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider("token", gitHubPath)).call();

		} catch (IOException ex) {
			result = "ERROR";
		} catch (GitAPIException ex) {
			result = "ERROR";
		}

		return result;

	}

	/**
	 * 1. Adds a tag to the domain repository
	 * 2.Push the changes to the origen repository 
	 * 
	 * @param cvsSettings
	 * @return If OK returns cero length string. Otherwise retunrs ERROR
	 */
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "/tagdomainrepository", consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String tagDomainRepository(@RequestBody String cvsSettingsJson) {
		String gitHubPath = "";

		if (cvsSettingsJson.contains("cvs_settings")) {
			cvsSettingsJson = cvsSettingsJson.substring(16, cvsSettingsJson.length() - 1);
		}

		GHVCsSettings cvsSettings = new Gson().fromJson(cvsSettingsJson, GHVCsSettings.class);
		cvsSettings.lastDomainTag = this.generateNextVerion(cvsSettings.lastDomainTag, cvsSettings.versionIncrement);
		gitHubPath = cvsSettings.githubPat;
		String repositoryName = cvsSettings.domainRepositoryPrefix + cvsSettings.productLineName.replaceAll(" ","-")
				+ cvsSettings.domainRepositorySufix;
		String result = "";
		try {

			File repositoryDirectory = new File(REPOSITORY_ROOT + repositoryName + "/.git");
			Git git = Git.open(repositoryDirectory);
			git.tag().setName(cvsSettings.domainTagPrefix + cvsSettings.lastDomainTag).call();
			git.push().setPushTags().setCredentialsProvider(new UsernamePasswordCredentialsProvider("token", gitHubPath)).call();
			result = cvsSettings.lastDomainTag;

		} catch (IOException ex) {
			result = "ERROR";
		} catch (GitAPIException ex) {
			result = "ERROR";
		}
		return result;
	}

	/**
	 * Generates the Next version based on the version increment part
	 * @param currentVersion Actual version
	 * @param versionIncrement Version part to increment: MAYOR, MENOR, PACTH
	 * @return The new generated version
	 */
	public String generateNextVerion(String currentVersion,String versionIncrement)
	{
		String nextVersion = "";
		String[] versionPartValues = currentVersion.replace(".","-").split("-"); 
		Integer mayor = Integer.parseInt(versionPartValues[0]);
		Integer menor = Integer.parseInt(versionPartValues[1]);
		Integer patch = Integer.parseInt(versionPartValues[2]);
		switch(versionIncrement.toUpperCase())
		{
			case "MAJOR":
			{
				mayor++;
				menor = 0;
				patch = 0;
			}break;
			case "MINOR":
			{
				menor++;
				patch = 0;
			}break;
			case "PATCH":
			{
				patch++; 
			}break;
		}
		nextVersion = mayor.toString() + "." + menor.toString() + "." + patch.toString();
		return nextVersion;
	}
	/**
	 * Creates products repositories based on cvsConfig
	 * 
	 * @param cvsSettings
	 * @return If OK returns domain repository url If repository already exist ti
	 *         returns 0 lengt string If Error, retunrs ERROR
	 */
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "/productrepositoryinitialization", consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String productRepositoryInitialization(@RequestBody String cvsSettingsJson) {

		if (cvsSettingsJson.contains("cvs_settings")) {
			cvsSettingsJson = cvsSettingsJson.substring(16, cvsSettingsJson.length() - 1);
		}

		GHVCsSettings cvsSettings = new Gson().fromJson(cvsSettingsJson, GHVCsSettings.class);

		String repositoryName = cvsSettings.applicationRepositoryPrefix + cvsSettings.productLineId
				+ cvsSettings.applicationRepositorySufix;
		String result = this.createRepo(repositoryName, cvsSettings.productLineName,
				cvsSettings.productLineName + " product repository", cvsSettings.githubPat);

		try {
			result = (new Gson().fromJson(result, JsonElement.class)).getAsJsonObject().get("html_url").toString();
		} catch (JsonSyntaxException ex) {
			if (result.toUpperCase().indexOf("ALREADY EXISTS") > -1) {
				result = "";
			} else {
				result = "ERROR";
			}
		}

		return result;

	}

}