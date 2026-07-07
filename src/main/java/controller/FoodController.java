package controller;

import java.util.Collection;
import java.util.List;

import model.Food;
import service.FoodService;
import service.impl.FoodServiceImpl;

/**
 * 負責銜接 UI 與 Service 層
 * UI 只呼叫 Controller，不直接接觸 Service / DAO
 */
public class FoodController {

    private final FoodService foodService;

    public FoodController() {
        this.foodService = new FoodServiceImpl();
    }

    public void addFood(String name, String type) {
        Food food = new Food(name, type);
        foodService.addFood(food);
    }

    public void updateFood(int id, String name, String type) {
        Food food = new Food(id, name, type);
        foodService.updateFood(food);
    }

    public void deleteFood(int id) {
        foodService.deleteFood(id);
    }

    public List<Food> getAllFoods() {
        return foodService.getAllFoods();
    }

    public List<Food> getFoodsByType(String type) {
        return foodService.getFoodsByType(type);
    }

    /**
     * 隨機選餐
     * @param type 篩選類型，傳入 "全部" 或 null 代表不篩選
     */
    public Food getRandomFood(String type) {
        return foodService.getRandomFood(type);
    }

    /**
     * 隨機選餐（支援排除清單）
     * @param type       篩選類型，傳入 "全部" 或 null 代表不篩選
     * @param excludeIds 要排除的餐點 id 清單，傳入 null 或空集合代表不排除任何餐點
     */
    public Food getRandomFood(String type, Collection<Integer> excludeIds) {
        return foodService.getRandomFood(type, excludeIds);
    }
}