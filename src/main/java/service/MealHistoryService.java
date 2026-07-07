package service;

import java.util.List;

import model.Food;
import model.MealHistory;

/**
 * MealHistory 業務邏輯介面
 */
public interface MealHistoryService {

    /** 記錄一次選餐結果（訪客不會呼叫此方法，因為訪客不需登入） */
    void recordPick(int userId, Food food);

    /** 取得指定使用者自己的選餐歷史 */
    List<MealHistory> getMyHistory(int userId);

    /** 取得全部使用者的選餐歷史（管理員專用） */
    List<MealHistory> getAllHistory();
}
