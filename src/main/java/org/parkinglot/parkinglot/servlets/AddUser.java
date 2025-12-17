package org.parkinglot.parkinglot.servlets;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.parkinglot.parkinglot.ejb.UserBean;

import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "AddUser", value = "/AddUser")
public class AddUser extends HttpServlet {

    @Inject
    UserBean usersBean;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!request.isUserInRole("WRITE_USERS")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. WRITE_USERS role required.");
            return;
        }

        request.setAttribute("userGroups", new String[] {"READ_CARS", "WRITE_CARS", "READ_USERS", "WRITE_USERS"});
        request.getRequestDispatcher("/WEB-INF/pages/addUser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!request.isUserInRole("WRITE_USERS")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. WRITE_USERS role required.");
            return;
        }

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String[] userGroups = request.getParameterValues("user_groups");
        if (userGroups == null)
        {
            userGroups = new String[0];
        }
        usersBean.createUser(username, email, password, Arrays.asList(userGroups));
        response.sendRedirect(request.getContextPath() + "/Users");
    }
}