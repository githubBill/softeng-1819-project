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

public class SignUpServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

          // code to process the form...
          // example of handling

          //*********************************
          final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

          HttpSession session=request.getSession();
          //*********************************

          String firstName = request.getParameter("name");
          String lastName  = request.getParameter("surname");
          String username  = request.getParameter("nickname");  // username
          String email = request.getParameter("email");
          String password = request.getParameter("password");

          boolean signUpOk = false ;
          signUpOk = dataAccess.signUp(username, password, email, firstName, lastName);

          if( signUpOk){
            // successfull sign up
            //response.sendRedirect("/index_user.html?username="+username);
            session.setAttribute("sessionUser", username);
            RequestDispatcher reqDispatcher = getServletConfig().getServletContext().getRequestDispatcher("/index_user.jsp");
            reqDispatcher.forward(request,response);

          }
          else{
            // bad input
            request.setAttribute("name", firstName);
            request.setAttribute("surname", lastName);
            //Servlet JSP communication
            RequestDispatcher reqDispatcher = getServletConfig().getServletContext().getRequestDispatcher("/failedSignUp.jsp");
            reqDispatcher.forward(request,response);
            response.sendRedirect("failedLogin.jsp");

          }

    }

}
