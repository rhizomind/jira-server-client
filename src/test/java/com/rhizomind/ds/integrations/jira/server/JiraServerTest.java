package com.rhizomind.ds.integrations.jira.server;

import com.rhizomind.ds.integrations.jira.server.project.*;
import com.rhizomind.ds.integrations.jira.server.projectcategory.ProjectCategoriesResource;
import com.rhizomind.ds.integrations.jira.server.projectcategory.ProjectCategoryRepresentation;
import com.rhizomind.ds.integrations.jira.server.serverinfo.ServerInfo;
import com.rhizomind.ds.integrations.jira.server.serverinfo.ServerInfoResource;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JiraServerTest {

    @Test
    void serverInfoResource() throws MalformedURLException {
        JiraServer jiraServer = new JiraServer(
                null,
                null,
                null
        );

        ProjectsResource projectsResource = jiraServer.projectsResource();
        ServerInfoResource serverInfoResource = jiraServer.serverInfoResource();
        ProjectCategoriesResource projectCategoriesResource = jiraServer.projectCategoriesResource();

        ServerInfo serverInfo = serverInfoResource.toRepresentation();
        List<ProjectCategoryRepresentation> categories = projectCategoriesResource.findAll();
        List<ProjectRepresentation> projects = projectsResource.findAll();
        projects.forEach(
                project -> {
                    ProjectResource projectResource = projectsResource.findOne(project.getId());

                    ProjectRepresentation projectRepresentation = projectResource.toRepresentation();
                    ProjectAvatarsRepresentation projectAvatars = projectResource.getProjectAvatars();
                    List<ProjectComponentRepresentation> projectComponents = projectResource.getProjectComponents();

                    System.out.println(projectRepresentation);

                }
        );

    }
}