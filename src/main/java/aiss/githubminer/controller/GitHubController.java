package aiss.githubminer.controller;

import aiss.githubminer.exception.ProjectNotFoundException;
import aiss.githubminer.model.Project;
import aiss.githubminer.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Tag(name = "GitHub Project", description = "GitHub Project management API")
@RestController
@RequestMapping("/githubminer")
public class GitHubController {
    @Autowired
    ProjectService projectService;
    @Autowired
    RestTemplate restTemplate;

    // GET /githubminer/{owner}/{repo}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    @Operation(
            summary = "Retrieve a GitHub Project",
            description = "Get a GitHub Project by specifying some parameters",
            tags = { "project", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Project found",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "Project not found",
                    content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{owner}/{repo}")
    public Project getProject(@Parameter(description = "name of the project owner") @PathVariable String owner,
                              @Parameter(description = "name of the project repository") @PathVariable String repo,
                              @Parameter(description = "number of past days to search for commits")
                                  @RequestParam(required = false) Integer sinceCommits,
                              @Parameter(description = "number of past days to search for issues")
                                  @RequestParam(required = false) Integer sinceIssues,
                              @Parameter(description = "max number of pages to search")
                                  @RequestParam(required = false) Integer maxPages)
            throws ProjectNotFoundException {

        Project res = projectService.getProjectAllData(owner, repo, sinceCommits, sinceIssues, maxPages);
        return res;
    }

    // POST /githubminer/{owner}/{repoName}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    @Operation(
            summary = "Send a GitHub Project to GitMiner",
            description = "Get a GitHub Project to GitMiner by specifying some parameters",
            tags = { "project", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Sent project",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Project could not be sent",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404",
                    description = "Project not found",
                    content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{owner}/{repo}")
    public Project postProject(@Parameter(description = "name of the project owner") @PathVariable String owner,
                               @Parameter(description = "name of the project repository") @PathVariable String repo,
                               @Parameter(description = "number of past days to search for commits")
                                   @RequestParam(required = false) Integer sinceCommits,
                               @Parameter(description = "number of past days to search for issues")
                                   @RequestParam(required = false) Integer sinceIssues,
                               @Parameter(description = "max number of pages to search")
                                   @RequestParam(required = false) Integer maxPages)
            throws ProjectNotFoundException {

        String uri = "http://localhost:8080/gitminer/projects";
        Project res = projectService.getProjectAllData(owner, repo, sinceCommits, sinceIssues, maxPages);

        ResponseEntity<Project> response = restTemplate
                .postForEntity(uri, res, Project.class);

        return response.getBody();
    }

}
