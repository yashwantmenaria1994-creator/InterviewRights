package com.example.interviewrights;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.UUID;

public class InsertAdminUser {

    public static void main(String[] args) {

        String url = "jdbc:mysql://mysqldb.c2j2i6qmglma.us-east-1.rds.amazonaws.com:3306/interview_rights";
        String username = "appuser";
        String password = "interviewRight@"; // apna DB password daalna

        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            String sql = "INSERT INTO users (id, email, password, role, first_name, last_name, is_active, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, "superadmin@interviewrights.com");

            // 🔥 password already encoded hona chahiye
            ps.setString(3, "$2a$10$Dow1X9jVh3k8pZK1z8rT5uR5kY5v6Yv1ZyYQXx1YwF9sZy8sQzY2e"); // Admin@123

            ps.setString(4, "ROLE_ADMIN");
            ps.setString(5, "super");
            ps.setString(6, "admin");

            ps.setBoolean(7, true);
            ps.setBoolean(8, false);

            ps.executeUpdate();

            System.out.println("✅ Admin user inserted successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}