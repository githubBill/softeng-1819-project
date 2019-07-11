package gr.ntua.ece.softeng18b;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
//-----------------------
import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Product;
import java.util.List;
//----------------------

public class LoginHandlerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

          // code to process the form...
          // example of handling

          //*********************************
          final DataAccess dataAccess = Configuration.getInstance().getDataAccess();
          //*********************************
          HttpSession session=request.getSession();

          //*****************************************

          String username = request.getParameter("username");
          String password = request.getParameter("password");
          PrintWriter writer = response.getWriter();
          String htmlRespone = "<html>";

          boolean isValiduser = false;
          isValiduser = dataAccess.validateUser(username, password);

          //-----------------------------------------------------------------------
          if(isValiduser){
            // return new page with extra features
            //response.sendRedirect("/index_user.html?username="+username);
            session.setAttribute("sessionUser", username);
            RequestDispatcher reqDispatcher = getServletConfig().getServletContext().getRequestDispatcher("/index_user.jsp");
		        reqDispatcher.forward(request,response);

          }
          else{
            // reload home page - login failed
            request.setAttribute("usernameFailedLogin", username);
            //Servlet JSP communication
		        RequestDispatcher reqDispatcher = getServletConfig().getServletContext().getRequestDispatcher("/failedLogin.jsp");
		        reqDispatcher.forward(request,response);
            response.sendRedirect("failedLogin.jsp");

          }


    }

}
