package aiss.githubminer.service;

import aiss.githubminer.githubmodel.Commit2;
import aiss.githubminer.githubmodel.Issue2;
import aiss.githubminer.model.Issue;
import aiss.githubminer.utils.NextUri;
import aiss.githubminer.utils.ParsingModels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class IssueService {

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
            .getLogger(IssueService.class);

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
        String nextPageURL = NextUri.getNextPageUrl(response.getHeaders());
        int page = 2;

        while (nextPageURL != null && page <= maxPages) {
            logger.debug("Retrieving issues from page " + page + ": " + nextPageURL);
            response = getIssues(nextPageURL, headers);
            pageIssues = Arrays.stream(response.getBody()).toList();
            logger. debug(pageIssues.size() + " issues retrieved.");
            issues.addAll(pageIssues);
            nextPageURL = NextUri.getNextPageUrl(response.getHeaders());
            page++;
        }

        List<Issue> newIssues = new ArrayList<>();
        for(Issue2 i: issues){
            Issue newIssue = ParsingModels.parseIssue(i);
            newIssues.add(newIssue);
        }

        return newIssues;
    }
}
