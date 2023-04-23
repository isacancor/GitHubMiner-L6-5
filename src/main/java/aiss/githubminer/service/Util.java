package aiss.githubminer.service;

public class Util {

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

}
