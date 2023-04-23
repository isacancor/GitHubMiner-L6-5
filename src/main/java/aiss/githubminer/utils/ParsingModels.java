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

        // TODO

        return newCommit;
    }

    public static User parseUser(User2 oldUser){
        User newUser = new User();

        // TODO

        return newUser;
    }

    public static Issue parseIssue(Issue2 oldIssue){
        Issue newIssue = new Issue();

        // TODO

        return newIssue;
    }

    public static Comment parseComment(Comment2 oldComment){
        Comment newComment = new Comment();

        // TODO

        return newComment;
    }


}
