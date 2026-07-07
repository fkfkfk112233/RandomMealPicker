package service;

import java.util.Collection;
import java.util.List;

import model.Food;

/**
 * Food 業務邏輯介面
 */
public interface FoodService {

    /** 新增餐點 */
    void addFood(Food food);

    /** 更新餐點 */
    void updateFood(Food food);

    /** 刪除餐點 */
    void deleteFood(int id);

    /** 取得全部餐點清單 */
    List<Food> getAllFoods();

    /** 依類型取得餐點清單 */
    List<Food> getFoodsByType(String type);

    /**
     * 隨機選出一筆餐點
     *
     * @param type 篩選類型；若為 null 或 "全部" 則從所有餐點中隨機挑選
     * @return 隨機選中的 Food；若清單為空則回傳 null
     */
    Food getRandomFood(String type);

    /**
     * 隨機選出一筆餐點（支援排除清單）
     *
     * @param type       篩選類型；若為 null 或 "全部" 則不篩選類型
     * @param excludeIds 要排除的餐點 id 清單；若為 null 或空集合則不排除任何餐點
     * @return 隨機選中的 Food；若符合條件的清單為空則回傳 null
     */
    Food getRandomFood(String type, Collection<Integer> excludeIds);
}
