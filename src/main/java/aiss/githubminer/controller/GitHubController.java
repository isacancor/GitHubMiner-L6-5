package aiss.githubminer.controller;

import aiss.githubminer.model.Project;
import aiss.githubminer.service.CommentService;
import aiss.githubminer.service.CommitService;
import aiss.githubminer.service.IssueService;
import aiss.githubminer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

public class GitHubController {

    @Autowired
    CommentService commentService;
    CommitService commitService;
    IssueService issueService;
    ProjectService projectService;
    @Autowired
    RestTemplate restTemplate;

    // GET /githubminer/{id}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    @GetMapping("/{id}")
    public Project getProject(@PathVariable String owner, @RequestParam String repo) {
        Project res = projectService.getProject(owner, repo);
        return res;
    }

    // POST /githubminer/{id}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}")
    public Project postProject(@PathVariable String owner, @RequestParam String repo,
                               @RequestParam int sinceCommits, @RequestParam int sinceIssues,
                               @RequestParam int maxPages) {
        String uri = "http://localhost:8080/gitminer/projects";
        Project res = projectService.getProjectAllData(owner, repo, sinceCommits, sinceIssues,maxPages);

        ResponseEntity<Project> response = restTemplate
                .postForEntity(uri, res, Project.class);

        return response.getBody();
    }




}
