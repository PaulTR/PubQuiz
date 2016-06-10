
package com.avery.networking.model.beer;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Beer {

    private String url;
    private String name;
    private String id;
    private String description;
    private String style;

    @SerializedName("beer_series_url")
    private String beerSeriesUrl;

    private String abv;

    private String cases;

    @SerializedName("bottled_on")
    private String bottledOn;

    private String batch;

    @SerializedName("bottle_image")
    private BottleImage bottleImage;

    @SerializedName("label_image")
    private LabelImage labelImage;

    private List<String> descriptors = new ArrayList<String>();

    @SerializedName("hop_varieties")
    private List<String> hopVarieties = new ArrayList<String>();

    @SerializedName("dry_hop_varieties")
    private List<String> dryHopVarieties = new ArrayList<String>();

    @SerializedName("yeast_varieties")
    private List<String> yeastVarieties = new ArrayList<String>();

    @SerializedName("malt_varieties")
    private List<String> maltVarieties = new ArrayList<String>();

    private List<String> pairings = new ArrayList<String>();
    private List<String> categories = new ArrayList<String>();
    private List<Object> availabilities = new ArrayList<Object>();
    private List<String> formats = new ArrayList<String>();
    private List<Recipe> recipes = new ArrayList<Recipe>();

    @SerializedName("food_pairings")
    private List<FoodPairing> foodPairings = new ArrayList<FoodPairing>();

    @SerializedName("related_beers")
    private List<Beer> relatedBeers = new ArrayList<Beer>();

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
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
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
     * @return The style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style The style
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @return The beerSeriesUrl
     */
    public String getBeerSeriesUrl() {
        return beerSeriesUrl;
    }

    /**
     * @param beerSeriesUrl The beer_series_url
     */
    public void setBeerSeriesUrl(String beerSeriesUrl) {
        this.beerSeriesUrl = beerSeriesUrl;
    }

    /**
     * @return The abv
     */
    public String getAbv() {
        return abv;
    }

    /**
     * @param abv The abv
     */
    public void setAbv(String abv) {
        this.abv = abv;
    }

    /**
     * @return The cases
     */
    public String getCases() {
        return cases;
    }

    /**
     * @param cases The cases
     */
    public void setCases(String cases) {
        this.cases = cases;
    }

    /**
     * @return The bottledOn date
     */
    public String getBottledOn() {
        return bottledOn;
    }

    /**
     * @param bottledOn The bottledOn date
     */
    public void setBottledOn(String bottledOn) {
        this.bottledOn = bottledOn;
    }

    /**
     * @return The batch
     */
    public String getBatch() {
        return batch;
    }

    /**
     * @param batch The batch number
     */
    public void setBatch(String batch) {
        this.batch = batch;
    }

    /**
     * @return The bottleImage
     */
    public BottleImage getBottleImage() {
        return bottleImage;
    }

    /**
     * @param bottleImage The bottle_image
     */
    public void setBottleImage(BottleImage bottleImage) {
        this.bottleImage = bottleImage;
    }

    /**
     * @return The labelImage
     */
    public LabelImage getLabelImage() {
        return labelImage;
    }

    /**
     * @param labelImage The label_image
     */
    public void setLabelImage(LabelImage labelImage) {
        this.labelImage = labelImage;
    }

    /**
     * @return The descriptors
     */
    public List<String> getDescriptors() {
        return descriptors;
    }

    /**
     * @param descriptors The descriptors
     */
    public void setDescriptors(List<String> descriptors) {
        this.descriptors = descriptors;
    }

    /**
     * @return The hopVarieties
     */
    public List<String> getHopVarieties() {
        return hopVarieties;
    }

    /**
     * @param hopVarieties The hop_varieties
     */
    public void setHopVarieties(List<String> hopVarieties) {
        this.hopVarieties = hopVarieties;
    }

    /**
     * @return The dryHopVarieties
     */
    public List<String> getDryHopVarieties() {
        return dryHopVarieties;
    }

    /**
     * @param dryHopVarieties The dry_hop_varieties
     */
    public void setDryHopVarieties(List<String> dryHopVarieties) {
        this.dryHopVarieties = dryHopVarieties;
    }

    /**
     * @return The yeastVarieties
     */
    public List<String> getYeastVarieties() {
        return yeastVarieties;
    }

    /**
     * @param yeastVarieties The yeast_varieties
     */
    public void setYeastVarieties(List<String> yeastVarieties) {
        this.yeastVarieties = yeastVarieties;
    }

    /**
     * @return The maltVarieties
     */
    public List<String> getMaltVarieties() {
        return maltVarieties;
    }

    /**
     * @param maltVarieties The malt_varieties
     */
    public void setMaltVarieties(List<String> maltVarieties) {
        this.maltVarieties = maltVarieties;
    }

    /**
     * @return The pairings
     */
    public List<String> getPairings() {
        return pairings;
    }

    /**
     * @param pairings The pairings
     */
    public void setPairings(List<String> pairings) {
        this.pairings = pairings;
    }

    /**
     * @return The categories
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * @param categories The categories
     */
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    /**
     * @return The availabilities
     */
    public List<Object> getAvailabilities() {
        return availabilities;
    }

    /**
     * @param availabilities The availabilities
     */
    public void setAvailabilities(List<Object> availabilities) {
        this.availabilities = availabilities;
    }

    /**
     * @return The formats
     */
    public List<String> getFormats() {
        return formats;
    }

    /**
     * @param formats The formats
     */
    public void setFormats(List<String> formats) {
        this.formats = formats;
    }

    /**
     * @return The recipes
     */
    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * @param recipes The recipes
     */
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    /**
     * @return The foodPairings
     */
    public List<FoodPairing> getFoodPairings() {
        return foodPairings;
    }

    /**
     * @param foodPairings The food_pairings
     */
    public void setFoodPairings(List<FoodPairing> foodPairings) {
        this.foodPairings = foodPairings;
    }

    /**
     * @return The relatedBeers
     */
    public List<Beer> getRelatedBeers() {
        return relatedBeers;
    }

    /**
     * @param relatedBeers The related_beers
     */
    public void setRelatedBeers(List<Beer> relatedBeers) {
        this.relatedBeers = relatedBeers;
    }

}
