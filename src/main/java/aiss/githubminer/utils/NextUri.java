package aiss.githubminer.utils;

import org.springframework.http.HttpHeaders;

public class NextUri {

    public static String getNextPageUrl(String uri){
        String nextUri;
        if(uri.contains("page=")){
            Integer numPage = Integer.valueOf(uri.split("page=")[1].replace("\"",""));
            numPage++;
            nextUri = uri.split("page=")[0] + "page=" + numPage;
        } else {
            nextUri = uri + "&page=2";
        }
        return nextUri;
    }

    public static String getNextPageUrl(HttpHeaders headers) {
        if (headers.getFirst("X-Next_Page").equals("")) {
            return null;
        }
        return headers.getFirst(HttpHeaders.LINK)
                .split(";")[0]
                .replaceAll("<|>","")
                .replaceAll(" rel=\"next\"", "");
    }

}
