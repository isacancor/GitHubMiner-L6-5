package aiss.githubminer.service;

import aiss.githubminer.githubmodel.Comment2;
import aiss.githubminer.model.Comment;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${githubminer.token}")
    private String token;

    @Value("${githubminer.baseuri}")
    private String baseUri;

    private static Logger logger = LoggerFactory
            .getLogger(CommentService.class);

    public ResponseEntity<Comment2[]> getComments(String uri, HttpHeaders headers){
        // Send request
        HttpEntity<Comment2[]> request = new HttpEntity<>(null, headers);

        ResponseEntity<Comment2[]> response = restTemplate
                .exchange(uri, HttpMethod.GET, request, Comment2[].class);

        return response;
    }

    //POST baseUri/{owner}/{repoName}[?sinceCommits=5&sinceIssues=30&maxPages=2]
    public List<Comment> getCommentsPagination(String owner, String repo, String issueId, int maxPages)
            throws HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        if(token!="") {
            headers.set("Authorization", "Bearer " + token);
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
        String nextPageURL = NextUri.getNextPageUrl(response.getHeaders());
        int page = 2;

        while (nextPageURL != null && page <= maxPages) {
            logger.debug("Retrieving comments from page " + page + ": " + nextPageURL);
            response = getComments(nextPageURL, headers);
            pageComments = Arrays.stream(response.getBody()).toList();
            logger.debug(pageComments.size() + " commits retrieved.");
            comments.addAll(pageComments);
            nextPageURL = NextUri.getNextPageUrl(response.getHeaders());
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
