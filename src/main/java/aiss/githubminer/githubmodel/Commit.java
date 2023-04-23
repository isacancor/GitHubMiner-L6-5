
package aiss.githubminer.githubmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {
    @JsonProperty("node_id")
    private String id;
    @JsonProperty("commit")
    private Commit__1 commit;
    @JsonProperty("html_url")
    private String webUrl;

    @JsonProperty("node_id")
    public String getId() {
        return id;
    }

    @JsonProperty("node_id")
    public void setId(String nodeId) {
        this.id = nodeId;
    }

    public String getTitle() {
        return this.commit.getMessage().split("\n")[0];
    }

    public void setTitle(String title) {
        String message2 = this.commit.getMessage().split("\n",2)[1];
        this.commit.setMessage(title + "\n" + message2);
    }

    public String getMessage() {
        return this.commit.getMessage();
    }

    public void setMessage(String message) {
        this.commit.setMessage(message);
    }

    public String getAuthorName() {
        return this.commit.getAuthor().getName();
    }

    public void setAuthorName(String name) {
        this.commit.getAuthor().setName(name);
    }

    public String getAuthorEmail() {
        return this.commit.getAuthor().getEmail();
    }

    public void setAuthorEmail(String email) {
        this.commit.getAuthor().setEmail(email);
    }

    public String getAuthoredDate() {
        return this.commit.getAuthor().getDate();
    }

    public void setAuthoredDate(String date) {
        this.commit.getAuthor().setDate(date);
    }


    public String getCommitterName() {
        return this.commit.getCommitter().getName();
    }

    public void setCommitterName(String name) {
        this.commit.getCommitter().setName(name);
    }

    public String getCommitterEmail() {
        return this.commit.getCommitter().getEmail();
    }

    public void setCommitterEmail(String email) {
        this.commit.getCommitter().setEmail(email);
    }

    public String getCommittedDate() {
        return this.commit.getCommitter().getDate();
    }

    public void setCommittedDate(String date) {
        this.commit.getCommitter().setDate(date);
    }

    @JsonProperty("commit")
    public Commit__1 getCommit() {
        return commit;
    }

    @JsonProperty("commit")
    public void setCommit(Commit__1 commit) {
        this.commit = commit;
    }

    @JsonProperty("html_url")
    public String getWebUrl() {
        return webUrl;
    }

    @JsonProperty("html_url")
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Commit.class.getName()).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((getTitle() == null)?"<null>":getTitle()));
        sb.append(',');
        sb.append("message");
        sb.append('=');
        sb.append(((getMessage() == null)?"<null>":getMessage().replaceAll("\n", "\\\\n")));
        sb.append(',');
        sb.append("authorName");
        sb.append('=');
        sb.append(((getAuthorName() == null)?"<null>":getAuthorName()));
        sb.append(',');
        sb.append("authorEmail");
        sb.append('=');
        sb.append(((getAuthorEmail() == null)?"<null>":getAuthorEmail()));
        sb.append(',');
        sb.append("authoredDate");
        sb.append('=');
        sb.append(((getAuthoredDate() == null)?"<null>":getAuthoredDate()));
        sb.append(',');
        sb.append("commiterName");
        sb.append('=');
        sb.append(((getCommitterName() == null)?"<null>":getCommitterName()));
        sb.append(',');
        sb.append("committerEmail");
        sb.append('=');
        sb.append(((getCommitterEmail() == null)?"<null>":getCommitterEmail()));
        sb.append(',');
        sb.append("committedDate");
        sb.append('=');
        sb.append(((getCommittedDate() == null)?"<null>":getCommittedDate()));
        sb.append(',');
        sb.append("webUrl");
        sb.append('=');
        sb.append(((this.webUrl == null)?"<null>":this.webUrl));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
