
package aiss.githubminer.githubmodel;

import aiss.githubminer.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("number")
    private String ref_id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private Object description;

    @JsonProperty("state")
    private String state;

    @JsonProperty("created_at")
    private String created_at;

    @JsonProperty("updated_at")
    private String updated_at;

    @JsonProperty("closed_at")
    private Object closed_at;

    @JsonProperty("labels")
    private List<Label> labels;


    @JsonProperty("user")
    private User user;
    @JsonProperty("assignee")
    private User assignee;

    @JsonProperty("comments")
    private List<Comment> comments;


    /*
    @JsonProperty("url")
    private String url;
    @JsonProperty("repository_url")
    private String repositoryUrl;
    @JsonProperty("labels_url")
    private String labelsUrl;
    @JsonProperty("comments_url")
    private String commentsUrl;
    @JsonProperty("events_url")
    private String eventsUrl;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("number")
    private Integer number;
    @JsonProperty("user")
    private User user;
    @JsonProperty("locked")
    private Boolean locked;
    @JsonProperty("assignees")
    private List<Object> assignees;
    @JsonProperty("milestone")
    private Object milestone;
    @JsonProperty("comments")
    private Integer comments;
    @JsonProperty("author_association")
    private String authorAssociation;
    @JsonProperty("active_lock_reason")
    private Object activeLockReason;
    @JsonProperty("draft")
    private Boolean draft;
    @JsonProperty("pull_request")
    private PullRequest pullRequest;
    @JsonProperty("reactions")
    private Reactions reactions;
    @JsonProperty("timeline_url")
    private String timelineUrl;
    @JsonProperty("performed_via_github_app")
    private Object performedViaGithubApp;
    @JsonProperty("state_reason")
    private Object stateReason;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
*/

    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    /*
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }
    @JsonProperty("repository_url")
    public String getRepositoryUrl() {
        return repositoryUrl;
    }
    @JsonProperty("repository_url")
    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }
    @JsonProperty("labels_url")
    public String getLabelsUrl() {
        return labelsUrl;
    }
    @JsonProperty("labels_url")
    public void setLabelsUrl(String labelsUrl) {
        this.labelsUrl = labelsUrl;
    }
    @JsonProperty("events_url")
    public String getEventsUrl() {
        return eventsUrl;
    }
    @JsonProperty("events_url")
    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }
    @JsonProperty("html_url")
    public String getHtmlUrl() {
        return htmlUrl;
    }
    @JsonProperty("html_url")
    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
    */

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("node_id")
    public String getRef_id() {
        return ref_id;
    }
    @JsonProperty("node_id")
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

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("user")
    public User getUser() {
        return user;
    }
    @JsonProperty("user")
    public void setUser(User user) {
        this.user = user;
    }
    @JsonProperty("assignee")
    public User getAssignee() {
        return assignee;
    }
    @JsonProperty("assignee")
    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    @JsonProperty("labels")
    public List<Label> getLabels() {
        return labels;
    }
    @JsonProperty("labels")
    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    /*
    @JsonProperty("locked")
    public Boolean getLocked() {
        return locked;
    }
    @JsonProperty("locked")
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
    @JsonProperty("assignee")
    public Object getAssignee() {
        return assignee;
    }
    @JsonProperty("assignee")
    public void setAssignee(Object assignee) {
        this.assignee = assignee;
    }
    @JsonProperty("assignees")
    public List<Object> getAssignees() {
        return assignees;
    }
    @JsonProperty("assignees")
    public void setAssignees(List<Object> assignees) {
        this.assignees = assignees;
    }
    @JsonProperty("milestone")
    public Object getMilestone() {
        return milestone;
    }
    @JsonProperty("milestone")
    public void setMilestone(Object milestone) {
        this.milestone = milestone;
    }
    */

    @JsonProperty("created_at")
    public String getCreated_at() {
        return created_at;
    }
    @JsonProperty("created_at")
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @JsonProperty("updated_at")
    public String getUpdated_at() {
        return updated_at;
    }
    @JsonProperty("updated_at")
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @JsonProperty("closed_at")
    public Object getClosed_at() {
        return closed_at;
    }
    @JsonProperty("closed_at")
    public void setClosed_at(Object closed_at) {
        this.closed_at = closed_at;
    }


    /*
    @JsonProperty("author_association")
    public String getAuthorAssociation() {
        return authorAssociation;
    }
    @JsonProperty("author_association")
    public void setAuthorAssociation(String authorAssociation) {
        this.authorAssociation = authorAssociation;
    }
    @JsonProperty("active_lock_reason")
    public Object getActiveLockReason() {
        return activeLockReason;
    }
    @JsonProperty("active_lock_reason")
    public void setActiveLockReason(Object activeLockReason) {
        this.activeLockReason = activeLockReason;
    }
    @JsonProperty("draft")
    public Boolean getDraft() {
        return draft;
    }
    @JsonProperty("draft")
    public void setDraft(Boolean draft) {
        this.draft = draft;
    }
    @JsonProperty("pull_request")
    public PullRequest getPullRequest() {
        return pullRequest;
    }
    @JsonProperty("pull_request")
    public void setPullRequest(PullRequest pullRequest) {
        this.pullRequest = pullRequest;
    }
    */

    @JsonProperty("body")
    public Object getDescription() {
        return description;
    }
    @JsonProperty("body")
    public void setDescription(Object description) {
        this.description = description;
    }

    /*
    @JsonProperty("reactions")
    public Reactions getReactions() {
        return reactions;
    }
    @JsonProperty("reactions")
    public void setReactions(Reactions reactions) {
        this.reactions = reactions;
    }
    @JsonProperty("timeline_url")
    public String getTimelineUrl() {
        return timelineUrl;
    }
    @JsonProperty("timeline_url")
    public void setTimelineUrl(String timelineUrl) {
        this.timelineUrl = timelineUrl;
    }
    @JsonProperty("performed_via_github_app")
    public Object getPerformedViaGithubApp() {
        return performedViaGithubApp;
    }
    @JsonProperty("performed_via_github_app")
    public void setPerformedViaGithubApp(Object performedViaGithubApp) {
        this.performedViaGithubApp = performedViaGithubApp;
    }
    @JsonProperty("state_reason")
    public Object getStateReason() {
        return stateReason;
    }
    @JsonProperty("state_reason")
    public void setStateReason(Object stateReason) {
        this.stateReason = stateReason;
    }
    */



}
