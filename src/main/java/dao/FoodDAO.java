package dao;

import java.util.List;

import model.Food;

/**
 * Food 資料存取介面（僅定義方法，不含實作）
 */
public interface FoodDAO {

    /** 新增一筆餐點資料 */
    void insert(Food food);

    /** 依 id 更新一筆餐點資料 */
    void update(Food food);

    /** 依 id 刪除一筆餐點資料（真刪除） */
    void delete(int id);

    /** 依 id 查詢單筆資料，查無資料回傳 null */
    Food findById(int id);

    /** 查詢全部餐點資料 */
    List<Food> findAll();

    /** 依類型查詢餐點資料 */
    List<Food> findByType(String type);
}
