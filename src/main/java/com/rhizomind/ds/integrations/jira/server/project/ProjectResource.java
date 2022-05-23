package com.rhizomind.ds.integrations.jira.server.project;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

public interface ProjectResource {

    @GET
    ProjectRepresentation toRepresentation();

    @GET
    ProjectAvatarsRepresentation getProjectAvatars();

    @GET
    List<ProjectComponentRepresentation> getProjectComponents();

}
