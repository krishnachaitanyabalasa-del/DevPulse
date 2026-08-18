package com.chaitu.devpulse.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Developer")
public class DeveloperNode {

    @Id
    private String id;
    private String name;
    private String team;
    private String tenure;
    private String avatarUrl;

    public DeveloperNode() {}

    public DeveloperNode(String id, String name, String team, String tenure, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.tenure = tenure;
        this.avatarUrl = avatarUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTenure() {
        return tenure;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
