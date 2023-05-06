package aiss.githubminer.utils;

import aiss.githubminer.githubmodel.*;
import aiss.githubminer.model.*;

public class ParsingModels {

    // TODO: create classes to parse from githubmodels to aiss models

    public static Project parseProject(Project2 oldProject){
        Project newProject = new Project();
        newProject.setId(oldProject.getId());
        newProject.setName(oldProject.getName());
        newProject.setWebUrl(oldProject.getWebUrl());

        return newProject;
    }

    public static Commit parseCommit(Commit2 oldCommit){
        Commit newCommit = new Commit();
        newCommit.setId(oldCommit.getId());
        newCommit.setTitle(oldCommit.getTitle());
        newCommit.setMessage(oldCommit.getMessage());
        newCommit.setAuthorName(oldCommit.getAuthorName());
        newCommit.setAuthorEmail(oldCommit.getAuthorEmail());
        newCommit.setAuthoredDate(oldCommit.getAuthoredDate());
        newCommit.setCommitterName(oldCommit.getCommitterName());
        newCommit.setCommitterEmail(oldCommit.getCommitterEmail());
        newCommit.setCommittedDate(oldCommit.getCommittedDate());
        newCommit.setWebUrl(oldCommit.getWebUrl());
        return newCommit;
    }

    public static User parseUser(User2 oldUser){
        User newUser = new User();
        newUser.setId(oldUser.getId());
        newUser.setUsername(oldUser.getUsername());
        newUser.setAvatarUrl(oldUser.getAvatarUrl());
        newUser.setWebUrl(oldUser.getHtmlUrl());
        return newUser;
    }

    public static Issue parseIssue(Issue2 oldIssue){
        Issue newIssue = new Issue();
        newIssue.setId(oldIssue.getId());
        newIssue.setRefId(oldIssue.getRef_id());
        newIssue.setTitle(oldIssue.getTitle());
        newIssue.setDescription(oldIssue.getDescription());
        newIssue.setState(oldIssue.getState());
        newIssue.setCreatedAt(oldIssue.getCreated_at());
        newIssue.setUpdatedAt(oldIssue.getUpdated_at());
        newIssue.setClosedAt(oldIssue.getClosed_at());
        newIssue.setLabels(oldIssue.getLabels());
        newIssue.setUpvotes(oldIssue.getUpvotes());
        newIssue.setDownvotes(oldIssue.getDownvotes());
        if (oldIssue.getAuthor() != null) {
            newIssue.setAuthor(parseUser(oldIssue.getAuthor()));
        }
        if (oldIssue.getAssignee() != null ) {
            newIssue.setAssignee(parseUser(oldIssue.getAssignee()));
        }
        newIssue.setWebUrl(oldIssue.getWebUrl());
        return newIssue;
    }

    public static Comment parseComment(Comment2 oldComment){
        Comment newComment = new Comment();
        newComment.setId(oldComment.getId());
        newComment.setBody(oldComment.getBody());
        newComment.setAuthor(oldComment.getAuthor());
        newComment.setCreatedAt(oldComment.getCreated_at());
        newComment.setUpdatedAt(oldComment.getUpdatedAt());
        return newComment;
    }


}
