package aiss.githubminer.service;

import aiss.githubminer.githubmodel.Commit2;
import aiss.githubminer.model.Commit;
import aiss.githubminer.utils.NextUri;
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
public class CommitService {

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
            .getLogger(CommitService.class);

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
            headers.set("Authorization", "Bearer " + token);
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
        List<Commit2> pageCommits = Arrays.stream(response.getBody()).toList();
        logger.debug (pageCommits.size() + " commits retrieved.");
        commits.addAll(pageCommits);

        // 2..n pages
        String nextPageURL = NextUri.getNextPageUrl(response.getHeaders());
        int page = 2;

        while (nextPageURL != null && page <= maxPages) {
            logger.debug("Retrieving commits from page " + page + ": " + nextPageURL);
            response = getCommits(nextPageURL, headers);
            pageCommits = Arrays.stream(response.getBody()).toList();
            logger.debug(pageCommits.size() + " commits retrieved.");
            commits.addAll(pageCommits);
            nextPageURL = NextUri.getNextPageUrl(response.getHeaders());
            page++;
        }

        List<Commit> newCommits = new ArrayList<>();
        for(Commit2 c: commits){
            Commit newCommit = ParsingModels.parseCommit(c);
            newCommits.add(newCommit);
        }

        return newCommits;
    }
}
