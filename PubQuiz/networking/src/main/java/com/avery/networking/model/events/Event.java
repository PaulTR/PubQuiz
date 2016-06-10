
package com.avery.networking.model.events;

public class Event {

    private String title;
    private String id;
    private String url;
    private String description;
    private String location;
    private String startsAt;
    private String ticketUrl;

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return The startsAt
     */
    public String getStartsAt() {
        return startsAt;
    }

    /**
     * @param startsAt The starts_at
     */
    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    /**
     * @return The ticketUrl
     */
    public String getTicketUrl() {
        return ticketUrl;
    }

    /**
     * @param ticketUrl The ticket_url
     */
    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

}
