package dao;

import java.util.List;

import model.MealHistory;

/**
 * MealHistory 資料存取介面
 */
public interface MealHistoryDAO {

    /** 新增一筆選餐歷史紀錄 */
    void insert(MealHistory history);

    /** 查詢指定會員/管理員自己的選餐歷史（依時間新到舊排序） */
    List<MealHistory> findByUserId(int userId);

    /** 查詢全部使用者的選餐歷史（管理員專用，依時間新到舊排序，含帳號名稱） */
    List<MealHistory> findAll();
}