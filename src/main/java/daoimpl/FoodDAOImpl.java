package daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.FoodDAO;
import model.Food;
import util.DBConnection;

/**
 * FoodDAO 的 JDBC 實作
 */
public class FoodDAOImpl implements FoodDAO {

    @Override
    public void insert(Food food) {
        String sql = "INSERT INTO food (name, type) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, food.getName());
            ps.setString(2, food.getType());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("新增餐點資料失敗：" + e.getMessage(), e);
        }
    }

    @Override
    public void update(Food food) {
        String sql = "UPDATE food SET name = ?, type = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, food.getName());
            ps.setString(2, food.getType());
            ps.setInt(3, food.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("更新餐點資料失敗：" + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM food WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("刪除餐點資料失敗：" + e.getMessage(), e);
        }
    }

    @Override
    public Food findById(int id) {
        String sql = "SELECT id, name, type FROM food WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("查詢餐點資料失敗：" + e.getMessage(), e);
        }
    }

    @Override
    public List<Food> findAll() {
        String sql = "SELECT id, name, type FROM food ORDER BY id";
        List<Food> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("查詢餐點清單失敗：" + e.getMessage(), e);
        }
    }

    @Override
    public List<Food> findByType(String type) {
        String sql = "SELECT id, name, type FROM food WHERE type = ? ORDER BY id";
        List<Food> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("依類型查詢餐點失敗：" + e.getMessage(), e);
        }
    }

    /** 將 ResultSet 目前的一列轉換成 Food 物件 */
    private Food mapRow(ResultSet rs) throws SQLException {
        Food food = new Food();
        food.setId(rs.getInt("id"));
        food.setName(rs.getString("name"));
        food.setType(rs.getString("type"));
        return food;
    }
}
