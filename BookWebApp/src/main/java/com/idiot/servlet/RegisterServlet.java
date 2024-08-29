package com.idiot.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final String query = "INSERT INTO BOOKDATA (BOOKNAME, BOOKEDITION, BOOKPRICE) VALUES (?, ?, ?)";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        try (PrintWriter pw = res.getWriter()) {
            // Get book info
            String bookName = req.getParameter("bookName");
            String bookEdition = req.getParameter("bookEdition");
            float bookPrice = Float.parseFloat(req.getParameter("bookPrice"));

            // Load JDBC driver
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                pw.println("<h1>JDBC Driver not found: " + e.getMessage() + "</h1>");
                return;
            }

            // Generate the connection and execute query
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/book", "root", "162311");
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, bookName);
                ps.setString(2, bookEdition);
                ps.setFloat(3, bookPrice);

                int count = ps.executeUpdate();
                if (count == 1) {
                    pw.println("<h2>Record is registered successfully</h2>");
                } else {
                    pw.println("<h2>Record not registered successfully</h2>");
                }
            } catch (SQLException se) {
                pw.println("<h1>SQL Error: " + se.getMessage() + "</h1>");
            } catch (Exception e) {
                pw.println("<h1>Error: " + e.getMessage() + "</h1>");
            }

            pw.println("<a href='home.html'>Home</a>");
            pw.println("<br>");
            pw.println("<a href='bookList'>Book List</a>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
}
