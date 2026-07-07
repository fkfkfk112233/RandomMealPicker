package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dao.MealHistoryDAO;
import model.MealHistory;
import util.DBConnection;

/**
 * MealHistoryDAO 的 JDBC 實作
 */
public class MealHistoryDAOImpl implements MealHistoryDAO {

    /** 共用的查詢欄位，LEFT JOIN user 取得帳號名稱 */
    private static final String BASE_SELECT =
            "SELECT mh.id, mh.user_id, u.username, mh.food_id, mh.food_name, mh.food_type, mh.picked_at "
          + "FROM meal_history mh "
          + "LEFT JOIN user u ON mh.user_id = u.id ";

    @Override
    public void insert(MealHistory history) {
        String sql = "INSERT INTO meal_history (user_id, food_id, food_name, food_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, history.getUserId());
            if (history.getFoodId() == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, history.getFoodId());
            }
            ps.setString(3, history.getFoodName());
            ps.setString(4, history.getFoodType());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    history.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("新增選餐歷史失敗：" + e.getMessage(), e);
        }
    }

    @Override
    public List<MealHistory> findByUserId(int userId) {
        String sql = BASE_SELECT + "WHERE mh.user_id = ? ORDER BY mh.picked_at DESC";
        List<MealHistory> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("查詢選餐歷史失敗：" + e.getMessage(), e);
        }
    }

    @Override
    public List<MealHistory> findAll() {
        String sql = BASE_SELECT + "ORDER BY mh.picked_at DESC";
        List<MealHistory> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("查詢全部選餐歷史失敗：" + e.getMessage(), e);
        }
    }

    /** 將 ResultSet 目前的一列轉換成 MealHistory 物件 */
    private MealHistory mapRow(ResultSet rs) throws SQLException {
        MealHistory history = new MealHistory();
        history.setId(rs.getInt("id"));
        history.setUserId(rs.getInt("user_id"));
        history.setUsername(rs.getString("username"));

        int foodId = rs.getInt("food_id");
        history.setFoodId(rs.wasNull() ? null : foodId);

        history.setFoodName(rs.getString("food_name"));
        history.setFoodType(rs.getString("food_type"));

        Timestamp ts = rs.getTimestamp("picked_at");
        if (ts != null) {
            history.setPickedAt(ts.toLocalDateTime());
        }

        return history;
    }
}
