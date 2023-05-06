
package aiss.githubminer.githubmodel;

import aiss.githubminer.model.Comment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue2 {

    @JsonProperty("id")
    private String id;

    @JsonProperty("number")
    private String ref_id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String description;

    @JsonProperty("state")
    private String state;

    @JsonProperty("created_at")
    private String created_at;

    @JsonProperty("updated_at")
    private String updated_at;

    @JsonProperty("closed_at")
    private String closed_at;

    @JsonProperty("labels")
    private List<Label> labels;

    @JsonProperty("reactions")
    private Reactions reactions;

    @JsonProperty("user")
    private User2 author;
    @JsonProperty("assignee")
    private User2 assignee;

    @JsonProperty("html_url")
    private String webUrl;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    /*
    @JsonProperty("number")
    public Integer getNumber() {
        return number;
    }
    @JsonProperty("number")
    public void setNumber(Integer number) {
        this.number = number;
    }
    */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getClosed_at() {
        return closed_at;
    }

    public void setClosed_at(String closed_at) {
        this.closed_at = closed_at;
    }

    public List<String> getLabels() {

        return labels.stream().map(label -> label.getName()).toList();
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public Reactions getReactions() {
        return reactions;
    }

    public void setReactions(Reactions reactions) {
        this.reactions = reactions;
    }

    public Integer getUpvotes(){
        return reactions.getUpvotes();
    }

    public Integer getDownvotes() {
        return reactions.getDownvotes();
    }



    public User2 getAuthor() {
        return author;
    }

    public void setAuthor(User2 author) {
        this.author = author;
    }

    public User2 getAssignee() {
        return assignee;
    }

    public void setAssignee(User2 assignee) {
        this.assignee = assignee;
    }

    public String getWebUrl(){
        return webUrl;
    }

    public void SetWebUrl(String url) {
        this.webUrl = url;
    }



    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Issue2.class.getName()).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null) ? "<null>" : this.id));
        sb.append(',');
        sb.append("refId");
        sb.append('=');
        sb.append(((this.ref_id == null) ? "<null>" : this.ref_id));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null) ? "<null>" : this.title));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null) ? "<null>" : this.description.replaceAll("\n", "")));
        sb.append(',');
        sb.append("state");
        sb.append('=');
        sb.append(((this.state == null) ? "<null>" : this.state));
        sb.append(',');
        sb.append("createdAt");
        sb.append('=');
        sb.append(((this.created_at == null) ? "<null>" : this.created_at));
        sb.append(',');
        sb.append("updatedAt");
        sb.append('=');
        sb.append(((this.updated_at == null) ? "<null>" : this.updated_at));
        sb.append(',');
        sb.append("closedAt");
        sb.append('=');
        sb.append(((this.closed_at == null) ? "<null>" : this.closed_at));
        sb.append(',');
        sb.append("labels");
        sb.append('=');
        sb.append(((this.getLabels() == null) ? "<null>" : this.getLabels()));
        sb.append(',');
        sb.append("author");
        sb.append('=');
        sb.append(((this.author == null) ? "<null>" : this.author));
        sb.append(',');
        sb.append("assignee");
        sb.append('=');
        sb.append(((this.assignee == null) ? "<null>" : this.assignee));
        sb.append(',');
        sb.append("upvotes");
        sb.append('=');
        sb.append(((this.getUpvotes() == null) ? "<null>" : this.getUpvotes()));
        sb.append(',');
        sb.append("downvotes");
        sb.append('=');
        sb.append(((this.getDownvotes() == null) ? "<null>" : this.getDownvotes()));
        sb.append(',');

        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
