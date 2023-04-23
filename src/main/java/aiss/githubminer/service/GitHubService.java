package aiss.githubminer.service;

import aiss.githubminer.githubmodel.Comment;
import aiss.githubminer.githubmodel.Commit;
import aiss.githubminer.githubmodel.Issue;
import aiss.githubminer.githubmodel.Project;
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
            Integer id = Integer.valueOf(issue.getRef_id());
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
        HttpEntity<Project[]> request = new HttpEntity<>(null, headers);

        String uri = baseUri + owner + "/" + repo;

        ResponseEntity<Project> project = restTemplate
                .exchange(uri, HttpMethod.GET, request, Project.class);

        return project.getBody();
    }




    // ----------------------------------------------------------------------------------------------------
    // Commit
    // Función getCommits
    public ResponseEntity<Commit[]> getCommits(String uri, HttpHeaders headers){
        // Send request
        HttpEntity<Commit[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Commit[]> response = restTemplate
                .exchange(uri, HttpMethod.GET, request, Commit[].class);

        return response;
    }


    //POST baseUri/{owner}/{repoName}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    public List<Commit> getCommitsPagination(String owner, String repo, int sinceCommits, int maxPages)
    throws HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        if(token!="") {
            headers.set("Authorization", "Bearer" + token);
        }

        List<Commit> commits = new ArrayList<>();

        // Calculate date and time to retrieve commits from based on the number of input days
        String sinceCommit = ZonedDateTime.now().minusDays(sinceCommits)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));

        // First page
        String uri = baseUri + owner + "/" + repo + "/commits?since=" +
                sinceCommit;
        logger.debug("Retrieving commits from page 1:" + uri);

        // Exchange
        ResponseEntity<Commit[]> response = getCommits(uri, headers);
        List<Commit> pageCommits = Arrays.stream (response .getBody()).toList();
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

        return commits;
    }


    // ----------------------------------------------------------------------------------------------------
    // Issues
    public ResponseEntity<Issue[]> getIssues(String uri, HttpHeaders headers){
        // Send request
        HttpEntity<Commit[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Issue[]> response = restTemplate
                .exchange(uri, HttpMethod.GET, request, Issue[].class);

        return response;
    }

    public List<Issue> getIssuesPagination(String owner, String repo, int sinceIssues, int maxPages)
            throws HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        if(token!="") {
            headers.set("Authorization", "Bearer" + token);
        }

        List<Issue> issues = new ArrayList<>();

        // Calculate date and time to retrieve issues from based on the number of input days
        String sinceIssue = ZonedDateTime.now().minusDays(sinceIssues)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));

        // First page
        String uri = baseUri + owner + "/" + repo + "/issues?since=" + sinceIssue;
        logger.debug("Retrieving issues from page 1:" + uri);

        // Exchange
        ResponseEntity<Issue[]> response = getIssues(uri, headers);
        List<Issue> pageIssues = Arrays.stream (response .getBody()).toList();
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

        return issues;
    }


    // ----------------------------------------------------------------------------------------------------
    // Comments

    public ResponseEntity<Comment[]> getComments(String uri, HttpHeaders headers){
        // Send request
        HttpEntity<Comment[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Comment[]> response = restTemplate
                .exchange(uri, HttpMethod.GET, request, Comment[].class);

        return response;
    }


    //POST baseUri/{owner}/{repoName}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    public List<Comment> getCommentsPagination(String owner, String repo,int issueId, int maxPages)
            throws HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        if(token!="") {
            headers.set("Authorization", "Bearer" + token);
        }

        List<Comment> comments = new ArrayList<>();

        // First page
        String uri = baseUri + owner + "/" + repo + "/issues/" + issueId + "/comments";
        logger.debug("Retrieving comments from page 1:" + uri);

        // Exchange
        ResponseEntity<Comment[]> response = getComments(uri, headers);
        List<Comment> pageComments = Arrays.stream (response .getBody()).toList();
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

        return comments;
    }
}
