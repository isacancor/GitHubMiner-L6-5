package aiss.githubminer.service;

import aiss.githubminer.exception.ProjectNotFoundException;
import aiss.githubminer.githubmodel.Project2;
import aiss.githubminer.model.Commit;
import aiss.githubminer.model.Issue;
import aiss.githubminer.model.Project;
import aiss.githubminer.utils.ParsingModels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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

    public Project getProjectAllData(String owner, String repo,
                                     Integer sinceCommits, Integer sinceIssues, Integer maxPages) throws ProjectNotFoundException {
        Project newProject = getProject(owner, repo);

        if(maxPages == null || maxPages <= 0){
            maxPages = maxPagesDefault;
        }

        if (sinceCommits == null || sinceCommits <= 0) {
            sinceCommits = sinceCommitsDefault;
        }

        if (sinceIssues == null || sinceIssues <= 0) {
            sinceIssues = sinceIssuesDefault;
        }

        List<Commit> commits = commitService.getCommitsPagination(owner, repo, sinceCommits, maxPages);
        List<Issue> issues = issueService.getIssuesPagination(owner, repo, sinceIssues, maxPages);

        newProject.setCommits(commits);
        newProject.setIssues(issues);

        return newProject;
    }

    public Project getProject(String owner, String repo) throws ProjectNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        // Setting token header
        if (token != "") {
            headers.set("Authorization", "Bearer " + token);
        }

        // Send request
        HttpEntity<Project2[]> request = new HttpEntity<>(null, headers);
        String uri = baseUri + owner + "/" + repo;

        Project2 oldProject;
        try {
            oldProject = restTemplate
                    .exchange(uri, HttpMethod.GET, request, Project2.class).getBody();
        } catch (Exception e) {
            throw new ProjectNotFoundException();
        }

        Project project = ParsingModels.parseProject(oldProject);
        return project;
    }
}
