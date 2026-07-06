package service.impl;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import dao.FoodDAO;
import dao.impl.FoodDAOImpl;
import model.Food;
import service.FoodService;

/**
 * FoodService 的實作
 */
public class FoodServiceImpl implements FoodService {

    /** 代表「全部類型」的字串常數，UI 的篩選下拉選單可使用此值 */
    public static final String TYPE_ALL = "全部";

    private final FoodDAO foodDAO;

    public FoodServiceImpl() {
        this.foodDAO = new FoodDAOImpl();
    }

    @Override
    public void addFood(Food food) {
        validate(food);
        foodDAO.insert(food);
    }

    @Override
    public void updateFood(Food food) {
        validate(food);
        if (food.getId() <= 0) {
            throw new IllegalArgumentException("更新資料時必須指定有效的 id");
        }
        foodDAO.update(food);
    }

    @Override
    public void deleteFood(int id) {
        foodDAO.delete(id);
    }

    @Override
    public List<Food> getAllFoods() {
        return foodDAO.findAll();
    }

    @Override
    public List<Food> getFoodsByType(String type) {
        return foodDAO.findByType(type);
    }

    @Override
    public Food getRandomFood(String type) {
        List<Food> candidates;

        if (type == null || type.isEmpty() || TYPE_ALL.equals(type)) {
            candidates = foodDAO.findAll();
        } else {
            candidates = foodDAO.findByType(type);
        }

        if (candidates == null || candidates.isEmpty()) {
            return null;
        }

        int index = ThreadLocalRandom.current().nextInt(candidates.size());
        return candidates.get(index);
    }

    /** 基本欄位驗證 */
    private void validate(Food food) {
        if (food == null) {
            throw new IllegalArgumentException("餐點資料不可為 null");
        }
        if (food.getName() == null || food.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("名稱不可為空");
        }
        if (food.getType() == null || food.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("類型不可為空");
        }
    }
}
