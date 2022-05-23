package com.rhizomind.ds.integrations.jira.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.rhizomind.ds.integrations.jira.server.issue.IssuesResource;
import com.rhizomind.ds.integrations.jira.server.issuetype.IssueTypesResource;
import com.rhizomind.ds.integrations.jira.server.project.ProjectsResource;
import com.rhizomind.ds.integrations.jira.server.projectcategory.ProjectCategoriesResource;
import com.rhizomind.ds.integrations.jira.server.serverinfo.ServerInfoResource;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.BasicAuthentication;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.net.URL;

public class JiraServer {

    private final Client client;
    private final ResteasyWebTarget target;

    public JiraServer(URL  serverUrl, String username, String password) {
        ObjectMapper objectMapper = new ObjectMapper()
                .disable(
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE
                )
                .disable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        SerializationFeature.FAIL_ON_EMPTY_BEANS
                )
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModules(
                        new Jdk8Module(),
                        new JavaTimeModule()
                );


        this.client = ClientBuilder
                .newBuilder()
                .register(new MyProvider(objectMapper))
                .build();
        this.target = (ResteasyWebTarget) client.target(serverUrl.toString());
        this.target.register(new BasicAuthentication(username, password));
    }

    public ServerInfoResource serverInfoResource(){
        return this.target.proxy(ServerInfoResource.class);
    }

    public IssueTypesResource issueTypesResource(){
        return this.target.proxy(IssueTypesResource.class);
    }

    public ProjectsResource projectsResource(){
        return this.target.proxy(ProjectsResource.class);
    }

    public IssuesResource issuesResource(){
        return this.target.proxy(IssuesResource.class);
    }

    public ProjectCategoriesResource projectCategoriesResource() {
        return this.target.proxy(ProjectCategoriesResource.class);
    }

    @Provider
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    class MyProvider extends JacksonJsonProvider {
        MyProvider(ObjectMapper mapper) {
            super(mapper);
        }
    }
}
