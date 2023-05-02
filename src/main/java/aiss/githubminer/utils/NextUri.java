package aiss.githubminer.utils;

import org.springframework.http.HttpHeaders;

public class NextUri {

    public static String getNextPageUrl(HttpHeaders headers){
        if(headers.getFirst(HttpHeaders.LINK) == null){
            return null;
        }

        return headers.getFirst(HttpHeaders.LINK)
                    .split(";")[0]
                    .replaceAll("<|>","");
    }

}
