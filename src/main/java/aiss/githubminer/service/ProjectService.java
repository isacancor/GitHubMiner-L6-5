package aiss.githubminer.service;

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
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CommitService commitService;
    @Autowired
    IssueService issueService;
    @Autowired
    CommentService commentService;

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
            .getLogger(ProjectService.class);

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

        List<Commit> commits = commitService.getCommitsPagination(owner, repo, sinceCommits, maxPages);

        List<Issue> issues = issueService.getIssuesPagination(owner, repo, sinceCommits, maxPages);

        for(Issue issue: issues) {
            Integer id = Integer.valueOf(issue.getRefId());
            List<Comment> comments = commentService.getCommentsPagination(owner, repo, id, maxPages);
            issue.setComments(comments);
        }

        newProject.setCommits(commits);
        newProject.setIssues(issues);

        return newProject;
    }


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
}
