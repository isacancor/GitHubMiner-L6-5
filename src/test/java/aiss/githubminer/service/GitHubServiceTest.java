package aiss.githubminer.service;

import aiss.githubminer.githubmodel.Comment;
import aiss.githubminer.githubmodel.Commit;
import aiss.githubminer.githubmodel.Project;
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
    GitHubService service;

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
/*
    @Test
    void getProjectCommitsIssues() {
        Project project = service.getProjectCommitsIssues(owner,repo,sinceCommitsDefault,
                sinceIssuesDefault,maxPagesDefault);
        assertEquals(project.getId(), "2183581", "The id doesn't match");
        assertEquals(project.getName(), "graphviz", "The name doesn't match");
        assertEquals(project.getWebUrl(),  "https://gitlab.com/graphviz/graphviz", "The web doesn't match");
        assertNotNull(project.getCommits(), "The list of commits is null");
       // assertNotNull(project.getIssues(), "The list of issues is null");
    }

 */
    // --------------------------------------------------------------------------------------------------------------
    // Project


    @Test
    void getProject() {
        Project project = service.getProject(owner,repo);
        assertEquals(project.getId(), "1148753", "The id does not match");
        assertEquals(project.getName(), "spring-framework", "The name does not match");
        assertEquals(project.getWebUrl(), "https://github.com/spring-projects/spring-framework",
                "The url does not match");
        System.out.println(project.toString());
    }

    // --------------------------------------------------------------------------------------------------------------
    // Commits
    @Test
    void getCommits() {
        String uri = baseUri + owner + "/" + repo +  "/commits";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        ResponseEntity<Commit[]> commits = service.getCommits(uri, headers);
        List<Commit> commitList = Arrays.stream(commits.getBody()).toList();
        assertNotNull(commitList, "The list of commits is null");

        commitList.stream().forEach(c -> System.out.println(c + "\n"));
    }

    @Test
    void getCommitsPagination() {
        Integer since = sinceCommitsDefault;
        Integer maxPage = 9;
        List<Commit> commits = service.getCommitsPagination(owner, repo, since, maxPage);
        ZonedDateTime sinceCommit = ZonedDateTime.now().minusDays(since);
        for( Commit commit: commits) {
            ZonedDateTime date = ZonedDateTime.parse(commit.getCommittedDate());
            assertTrue(date.isAfter(sinceCommit),
                    "The commit date can not be earlier than the specified date in sinceCommit");
        }
        // 30 = num elements per page (default value)
        assertTrue(commits.size() <= 30*maxPagesDefault,
                "The number of elements can not exceed the default value of page " +
                        "elements multiply by the maximum number of pages accepted");

        commits.stream().forEach(c -> System.out.println(c + "\n"));
    }

    // --------------------------------------------------------------------------------------------------------------
    // Issues

    // --------------------------------------------------------------------------------------------------------------
    // Comments

    @Test
    void getComments() {
        String uri = baseUri + owner + "/" + repo +  "/issues/" + issueId + "/comments";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        ResponseEntity<Comment[]> comments = service.getComments(uri, headers);
        List<Comment> commentList = Arrays.stream(comments.getBody()).toList();
        assertNotNull(commentList, "The list of comments is null");

        commentList.stream().forEach(c -> System.out.println(c + "\n"));
    }

    @Test
    void getCommentsPagination() {
        Integer maxPage = 2;
        List<Comment> comments = service.getCommentsPagination(owner, repo, issueId, maxPage);

        // 30 = num elements per page (default value)
        assertTrue(comments.size() <= 30*maxPagesDefault,
                "The number of elements can not exceed the default value of page " +
                        "elements multiplied by the maximum number of pages accepted");

        comments.stream().forEach(c -> System.out.println(c + "\n"));
    }


}