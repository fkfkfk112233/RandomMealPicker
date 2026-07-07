package model;

import java.time.LocalDateTime;

/**
 * 選餐歷史紀錄
 * food_name / food_type 為選餐當下的快照，即使原始餐點之後被刪除或改名，
 * 歷史紀錄仍能正確顯示當時選到的內容。
 */
public class MealHistory {

    private int id;
    private int userId;
    private String username;   // 選餐的帳號（管理員查詢全部歷史時使用，一般查詢不會用到）
    private Integer foodId;    // 對應餐點 id，餐點被刪除後會變成 null
    private String foodName;   // 選餐當下的名稱快照
    private String foodType;   // 選餐當下的類型快照
    private LocalDateTime pickedAt;

    public MealHistory() {
    }

    public MealHistory(int userId, Integer foodId, String foodName, String foodType) {
        this.userId = userId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodType = foodType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public LocalDateTime getPickedAt() {
        return pickedAt;
    }

    public void setPickedAt(LocalDateTime pickedAt) {
        this.pickedAt = pickedAt;
    }
}