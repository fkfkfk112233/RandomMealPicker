package service.impl;

import java.util.List;

import dao.MealHistoryDAO;
import dao.impl.MealHistoryDAOImpl;
import model.Food;
import model.MealHistory;
import service.MealHistoryService;

/**
 * MealHistoryService 的實作
 */
public class MealHistoryServiceImpl implements MealHistoryService {

    private final MealHistoryDAO mealHistoryDAO;

    public MealHistoryServiceImpl() {
        this.mealHistoryDAO = new MealHistoryDAOImpl();
    }

    @Override
    public void recordPick(int userId, Food food) {
        if (food == null) {
            return; // 沒有抽到任何結果，不需要記錄
        }
        MealHistory history = new MealHistory(userId, food.getId(), food.getName(), food.getType());
        mealHistoryDAO.insert(history);
    }

    @Override
    public List<MealHistory> getMyHistory(int userId) {
        return mealHistoryDAO.findByUserId(userId);
    }

    @Override
    public List<MealHistory> getAllHistory() {
        return mealHistoryDAO.findAll();
    }
}
