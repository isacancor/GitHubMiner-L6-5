package aiss.githubminer.service;

import aiss.githubminer.githubmodel.Comment2;
import aiss.githubminer.githubmodel.Commit2;
import aiss.githubminer.githubmodel.Issue2;
import aiss.githubminer.githubmodel.Project2;
import aiss.githubminer.model.Comment;
import aiss.githubminer.model.Commit;
import aiss.githubminer.model.Issue;
import aiss.githubminer.model.Project;
import aiss.githubminer.utils.ParsingModels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GitHubService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${githubminer.token}")
    private String token;

    @Value("${githubminer.baseuri}")
    private String baseUri;

    @Value("${gitminer.sincecommits}")
    private int sinceCommitsDefault;

    @Value("${gitminer.sinceissues}")
    private int sinceIssuesDefault;

    @Value("${gitminer.maxpages}")
    private int maxPagesDefault;

    private static Logger logger = LoggerFactory
            .getLogger(GitHubService.class);


    public Project getProjectCommitsIssues(String owner, String repo,
                                            Integer sinceCommits, Integer sinceIssues, Integer maxPages) {

        Project newProject = getProject(owner, repo);

        if(maxPages <= 0){
            maxPages = maxPagesDefault;
        }

        if (sinceCommits <= 0) {
            sinceCommits = sinceCommitsDefault;
        }

        if (sinceIssues <= 0) {
            sinceIssues = sinceIssuesDefault;
        }

        List<Commit> commits = getCommitsPagination(owner, repo, sinceCommits, maxPages);

        List<Issue> issues = getIssuesPagination(owner, repo, sinceCommits, maxPages);

        for(Issue issue: issues) {
            Integer id = Integer.valueOf(issue.getRefId());
            List<Comment> comments = getCommentsPagination(owner, repo, id, maxPages);
            issue.setComments(comments);
        }

        newProject.setCommits(commits);
        newProject.setIssues(issues);

        return newProject;
    }


    // ----------------------------------------------------------------------------------------------------
    // Project

    public Project getProject(String owner, String repo) {
        HttpHeaders headers = new HttpHeaders();

        // Setting token header
        if(token != ""){
            headers.set("Authorization", "Bearer " + token);
        }

        // Send request
        HttpEntity<Project2[]> request = new HttpEntity<>(null, headers);

        String uri = baseUri + owner + "/" + repo;

        ResponseEntity<Project2> projectRE = restTemplate
                .exchange(uri, HttpMethod.GET, request, Project2.class);

        Project2 oldProject = projectRE.getBody();
        Project project = ParsingModels.parseProject(oldProject);

        return project;
    }

    // ----------------------------------------------------------------------------------------------------
    // Commit
    // Función getCommits
    public ResponseEntity<Commit2[]> getCommits(String uri, HttpHeaders headers){
        // Send request
        HttpEntity<Commit2[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Commit2[]> response = restTemplate
                .exchange(uri, HttpMethod.GET, request, Commit2[].class);

        return response;
    }

    //POST baseUri/{owner}/{repoName}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    public List<Commit> getCommitsPagination(String owner, String repo, int sinceCommits, int maxPages)
    throws HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        if(token!="") {
            headers.set("Authorization", "Bearer" + token);
        }

        List<Commit2> commits = new ArrayList<>();

        // Calculate date and time to retrieve commits from based on the number of input days
        String sinceCommit = ZonedDateTime.now().minusDays(sinceCommits)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));

        // First page
        String uri = baseUri + owner + "/" + repo + "/commits?since=" +
                sinceCommit;
        logger.debug("Retrieving commits from page 1:" + uri);

        // Exchange
        ResponseEntity<Commit2[]> response = getCommits(uri, headers);
        List<Commit2> pageCommits = Arrays.stream (response .getBody()).toList();
        logger.debug (pageCommits.size() + " commits retrieved.");
        commits.addAll(pageCommits);

        // 2..n pages
        String nextPageURL = Util.getNextPageUrl(uri);
        int page = 2;

        while (nextPageURL != null && page <= maxPages) {
            logger.debug("Retrieving commits from page " + page + ": " + nextPageURL);
            response = getCommits(nextPageURL, headers);
            pageCommits = Arrays.stream(response.getBody()).toList();
            logger. debug(pageCommits.size() + " commits retrieved.");
            commits.addAll(pageCommits);
            nextPageURL = Util.getNextPageUrl(uri);
            page++;
        }

        List<Commit> newCommits = new ArrayList<>();
        for(Commit2 c: commits){
            Commit newCommit = ParsingModels.parseCommit(c);
            newCommits.add(newCommit);
        }

        return newCommits;
    }


    // ----------------------------------------------------------------------------------------------------
    // Issues
    public ResponseEntity<Issue2[]> getIssues(String uri, HttpHeaders headers){
        // Send request
        HttpEntity<Commit2[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Issue2[]> response = restTemplate
                .exchange(uri, HttpMethod.GET, request, Issue2[].class);

        return response;
    }

    public List<Issue> getIssuesPagination(String owner, String repo, int sinceIssues, int maxPages)
            throws HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        if(token!="") {
            headers.set("Authorization", "Bearer" + token);
        }

        List<Issue2> issues = new ArrayList<>();

        // Calculate date and time to retrieve issues from based on the number of input days
        String sinceIssue = ZonedDateTime.now().minusDays(sinceIssues)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));

        // First page
        String uri = baseUri + owner + "/" + repo + "/issues?since=" + sinceIssue;
        logger.debug("Retrieving issues from page 1:" + uri);

        // Exchange
        ResponseEntity<Issue2[]> response = getIssues(uri, headers);
        List<Issue2> pageIssues = Arrays.stream (response .getBody()).toList();
        logger.debug (pageIssues.size() + " commits retrieved.");
        issues.addAll(pageIssues);

        // 2..n pages
        String nextPageURL = Util.getNextPageUrl(uri);
        int page = 2;

        while (nextPageURL != null && page <= maxPages) {
            logger.debug("Retrieving issues from page " + page + ": " + nextPageURL);
            response = getIssues(nextPageURL, headers);
            pageIssues = Arrays.stream(response.getBody()).toList();
            logger. debug(pageIssues.size() + " issues retrieved.");
            issues.addAll(pageIssues);
            nextPageURL = Util.getNextPageUrl(uri);
            page++;
        }

        List<Issue> newIssues = new ArrayList<>();
        for(Issue2 i: issues){
            Issue newIssue = ParsingModels.parseIssue(i);
            newIssues.add(newIssue);
        }

        return newIssues;
    }


    // ----------------------------------------------------------------------------------------------------
    // Comments

    public ResponseEntity<Comment2[]> getComments(String uri, HttpHeaders headers){
        // Send request
        HttpEntity<Comment2[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Comment2[]> response = restTemplate
                .exchange(uri, HttpMethod.GET, request, Comment2[].class);

        return response;
    }


    //POST baseUri/{owner}/{repoName}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    public List<Comment> getCommentsPagination(String owner, String repo, int issueId, int maxPages)
            throws HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        if(token!="") {
            headers.set("Authorization", "Bearer" + token);
        }

        List<Comment2> comments = new ArrayList<>();

        // First page
        String uri = baseUri + owner + "/" + repo + "/issues/" + issueId + "/comments";
        logger.debug("Retrieving comments from page 1:" + uri);

        // Exchange
        ResponseEntity<Comment2[]> response = getComments(uri, headers);
        List<Comment2> pageComments = Arrays.stream (response .getBody()).toList();
        logger.debug (pageComments.size() + " comments retrieved.");
        comments.addAll(pageComments);

        // 2..n pages
        String nextPageURL = Util.getNextPageUrl(uri);
        int page = 2;

        while (nextPageURL != null && page <= maxPages) {
            logger.debug("Retrieving comments from page " + page + ": " + nextPageURL);
            response = getComments(nextPageURL, headers);
            pageComments = Arrays.stream(response.getBody()).toList();
            logger.debug(pageComments.size() + " commits retrieved.");
            comments.addAll(pageComments);
            nextPageURL = Util.getNextPageUrl(uri);
            page++;
        }

        List<Comment> newComments = new ArrayList<>();
        for(Comment2 c: comments){
            Comment newComment = ParsingModels.parseComment(c);
            newComments.add(newComment);
        }

        return newComments;
    }
}
