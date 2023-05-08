package aiss.githubminer.service;

import aiss.githubminer.githubmodel.Comment2;
import aiss.githubminer.githubmodel.Commit2;
import aiss.githubminer.githubmodel.Issue2;
import aiss.githubminer.githubmodel.Project2;
import aiss.githubminer.model.Comment;
import aiss.githubminer.model.Commit;
import aiss.githubminer.model.Issue;
import aiss.githubminer.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GitHubServiceTest {
    @Autowired
    ProjectService projectService;
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

    final String owner = "spring-projects";
    final String repo = "spring-framework";
    final Integer issueId = 30340;

    @Test
    void getProjectAllData() {
        Project project = projectService.getProjectAllData(owner,repo,sinceCommitsDefault,
                sinceIssuesDefault,maxPagesDefault);
        assertEquals(project.getId(), "1148753", "The id doesn't match");
        assertEquals(project.getName(), "spring-framework", "The name doesn't match");
        assertEquals(project.getWebUrl(),  "https://github.com/spring-projects/spring-framework", "The web doesn't match");
        assertNotNull(project.getCommits(), "The list of commits is null");
        assertNotNull(project.getIssues(), "The list of issues is null");

        project.prettyPrint();
    }


    // --------------------------------------------------------------------------------------------------------------
    // Project
    @Test
    void getProject() {
        Project project = projectService.getProject(owner, repo);
        assertEquals(project.getId(), "1148753", "The id does not match");
        assertEquals(project.getName(), "spring-framework", "The name does not match");
        assertEquals(project.getWebUrl(), "https://github.com/spring-projects/spring-framework",
                "The url does not match");
        System.out.println(project);
    }

    // --------------------------------------------------------------------------------------------------------------
    // Commits
    @Test
    void getCommits() {
        String uri = baseUri + owner + "/" + repo +  "/commits";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        ResponseEntity<Commit2[]> commits = commitService.getCommits(uri, headers);
        List<Commit2> commitList = Arrays.stream(commits.getBody()).toList();
        assertNotNull(commitList, "The list of commits is null");

        System.out.println(commitList.size());
        commitList.stream().forEach(c -> System.out.println(c + "\n"));
    }

    @Test
    void getCommitsPagination() {
        Integer since = 30;
        Integer maxPage = 5;
        List<Commit> commits = commitService.getCommitsPagination(owner, repo, since, maxPage);

        ZonedDateTime sinceCommit = ZonedDateTime.now().minusDays(since);
        for( Commit commit: commits) {
            ZonedDateTime date = ZonedDateTime.parse(commit.getCommittedDate());
            assertTrue(date.isAfter(sinceCommit),
                    "The commit date can not be earlier than the specified date in sinceCommit");
        }
        // 30 = num elements per page (default value)
        assertTrue(commits.size() <= 30*maxPage,
                "The number of elements can not exceed the default value of page " +
                        "elements multiply by the maximum number of pages accepted");

        commits.stream().forEach(c -> System.out.println(c + "\n"));
    }

    // --------------------------------------------------------------------------------------------------------------
    // Issues

    @Test
    void getIssues() {
        String uri = baseUri + owner + "/" + repo + "/issues";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        ResponseEntity<Issue2[]> issues = issueService.getIssues(uri, headers);
        List<Issue2> issueList = Arrays.stream(issues.getBody()).toList();
        assertNotNull(issueList, "The list of issues is null");

        //System.out.println(issueList.size());
        issueList.stream().forEach(c -> System.out.println(c + "\n"));
    }

    @Test
    void getIssuesPagination() {
        Integer since = 4;
        Integer maxPage = 2;
        List<Issue> issues = issueService.getIssuesPagination(owner, repo, since, maxPage);

        ZonedDateTime sinceIssue = ZonedDateTime.now().minusDays(since);
        for( Issue issue: issues) {
            ZonedDateTime date = ZonedDateTime.parse(issue.getUpdatedAt());
            assertTrue(date.isAfter(sinceIssue),
                    "The issue date can not be earlier than the specified date in sinceIssue");
        }
        // 30 = num elements per page (default value)
        assertTrue(issues.size() <= 30*maxPage,
                "The number of elements can not exceed the default value of page " +
                        "elements multiply by the maximum number of pages accepted");

        issues.stream().forEach(c -> System.out.println(c + "\n"));
    }


    // --------------------------------------------------------------------------------------------------------------
    // Comments

    @Test
    void getComments() {
        String uri = baseUri + owner + "/" + repo +  "/issues/" + issueId + "/comments";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        ResponseEntity<Comment2[]> comments = commentService.getComments(uri, headers);
        List<Comment2> commentList = Arrays.stream(comments.getBody()).toList();
        assertNotNull(commentList, "The list of comments is null");

        commentList.stream().forEach(c -> System.out.println(c + "\n"));
    }

    @Test
    void getCommentsPagination() {
        Integer maxPage = 2;
        List<Comment> comments = commentService.getCommentsPagination(owner, repo, issueId, maxPage);

        // 30 = num elements per page (default value)
        assertTrue(comments.size() <= 30*maxPagesDefault,
                "The number of elements can not exceed the default value of page " +
                        "elements multiplied by the maximum number of pages accepted");

        comments.stream().forEach(c -> System.out.println(c + "\n"));
    }


}