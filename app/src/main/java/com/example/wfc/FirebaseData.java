package com.example.wfc;

import java.util.HashMap;
import java.util.Map;

public class FirebaseData {
    private String cocktailName; // 칵테일 이름
    private String cockSimpleExplan; //칵테일에 대한 간단한 소개
    private String techniques; //기법
    private String glassName; // 칵테일 담는 글래스 이름
    private String garnish; // 칵테일 위에 올라가는 장식(안주)
    private String recipe; //레시피
    private int cocktailNum; //임의로 지정한 칵테일 번호
    private String imageUrl;

    private String documentId;

    private int likeCount;
    private Map<String, Boolean> likes = new HashMap<>();

    public FirebaseData() {}

    public FirebaseData(String cocktailName, String cockSimpleExplan, String techniques, String glassName, String garnish, 	String recipe, int cocktailNum) {
        this.cocktailName = cocktailName;
        this.cockSimpleExplan = cockSimpleExplan;
        this.techniques = techniques;
        this.glassName = glassName;
        this.garnish = garnish;
        this.recipe = recipe;
        this.cocktailNum = cocktailNum;
    }

    public String getCocktailName(){
        return this.cocktailName;
    }

    public void setCocktailName(String cocktailname) {
        this.cocktailName = cocktailname;
    }

    public void setCockSimpleExplan(String cocksimpleexplan) {this.cockSimpleExplan = cocksimpleexplan;}

    public void setTechniques(String techniques) {
        this.techniques = techniques;
    }

    public void setGlassName(String glassname) {
        this.glassName = glassname;
    }

    public void setGarnish(String garnish) {this.garnish = garnish;}

    public void setRecipe(String recipe) {
        this.recipe= recipe;
    }

    public int getCocktailNum(){return this.cocktailNum;}

        public void setCocktailNum(int number){
            this.cocktailNum = number;
        }

    public String getCockSimpleExplan() {return cockSimpleExplan;}

    public String getTechniques() {return techniques;}

    public String getGlassName() {return glassName;}

    public String getGarnish() {return garnish;}

    public String getRecipe() {return recipe;}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDocumentId() {
        return documentId;
    }

    // 문서 ID를 설정하는 메서드
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    // likes의 getter와 setter
    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

}
