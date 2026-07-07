package controller;

import java.util.List;

import model.Food;
import model.MealHistory;
import service.MealHistoryService;
import service.impl.MealHistoryServiceImpl;

/**
 * 負責銜接選餐歷史畫面與 Service 層
 */
public class MealHistoryController {

    private final MealHistoryService mealHistoryService;

    public MealHistoryController() {
        this.mealHistoryService = new MealHistoryServiceImpl();
    }

    public void recordPick(int userId, Food food) {
        mealHistoryService.recordPick(userId, food);
    }

    public List<MealHistory> getMyHistory(int userId) {
        return mealHistoryService.getMyHistory(userId);
    }

    public List<MealHistory> getAllHistory() {
        return mealHistoryService.getAllHistory();
    }
}
