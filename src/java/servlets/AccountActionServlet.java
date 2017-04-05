/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import business.CreditCard;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ekk
 */
public class AccountActionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String url = "/CardTrans.jsp";
        String msg = "";
        String path = getServletContext().getRealPath("/WEB-INF/") + "\\";
        String action = request.getParameter("actiontype").toLowerCase();
        CreditCard card = (CreditCard) request.getSession().getAttribute("card");
        
        if (action.equals("new")) {
            card = new CreditCard(path);
            if (card.getErrorStatus()){
                msg += "Account Error: " + card.getErrorMessage() + ".<br>";
            } else {
                msg += card.getActionMsg() + ".<br>";
            }
        }

        if (action.equals("existing")) {
            String accntNum = request.getParameter("account");
            if (accntNum.isEmpty()) {
                msg += "Account Error: Bad or missing account number.";
            } else {
                card = new CreditCard(Integer.parseInt(accntNum),path);
                if (card.getErrorStatus()) {
                    msg += "Account Error: " + card.getErrorMessage() + ".<br>";
                    card = null;
                    action = "error";
                } else {
                    card.getCreditHistory();
                    msg += card.getActionMsg() + ".<br>";
                }
            }
        }
        
        if (card != null) {
            if (action.equals("history")) {
                url = "/History.jsp";
            }
            
            else if (action.equals("charge")) {
                String chargeS = request.getParameter("cAmt");
                String desc = request.getParameter("cDesc");
                if (chargeS.isEmpty() || desc.isEmpty()) {
                    msg += "Bad or missing " + action + " amount or description.";
                } else if (card.getErrorStatus()) {
                    msg += "Account Error: " + card.getErrorMessage() + ".<br>";
                } else {
                    double chargeD = Double.parseDouble(chargeS);
                    if (chargeD < 0) {
                        msg += "Negative " + action + " amount.";
                    } else {
                        card.setCharge(chargeD,desc);
                        msg += card.getActionMsg() + ".<br>";
                    }
                }
            }

            else if (action.equals("payment")) {
                String pmntS = request.getParameter("pAmt");
                if (pmntS.isEmpty()) {
                    msg += "Bad or missing " + action + " amount.";
                } else if (card.getErrorStatus()) {
                    msg += "Account Error: " + card.getErrorMessage() + ".<br>";
                } else {
                    double pmntD = Double.parseDouble(pmntS);
                    if (pmntD < 0) {
                        msg += "Negative " + action + " amount.";
                    } else {
                        card.setPayment(pmntD);
                        msg += card.getActionMsg() + ".<br>";
                    }
                }
            }

            else if (action.equals("increase")) {
                String increaseS = request.getParameter("cIncrease");
                if (increaseS.isEmpty()) {
                    msg += "Bad or missing " + action + " amount.";
                } else if (card.getErrorStatus()) {
                    msg += "Account Error: " + card.getErrorMessage() + ".<br>";
                } else {
                    double increaseD = Double.parseDouble(increaseS);
                    if (increaseD < 0) {
                        msg += "Negative " + action + " amount.";
                    } else {
                        card.setCreditIncrease(increaseD);
                        msg += card.getActionMsg() + ".<br>";
                    }
                }
            }

            else if (action.equalsIgnoreCase("interest")) {
                String rateS = request.getParameter("iRate");
                if (rateS.isEmpty()) {
                    msg += "Bad or missing " + action + " amount.";
                } else if (card.getErrorStatus()){
                    msg += "Account Error: " + card.getErrorMessage() + ".<br>";
                } else {
                    double rateD = Double.parseDouble(rateS);
                    if (rateD < 0) {
                        msg += "Negative " + action + " amount.";
                    } else {
                        card.setInterestCharge(rateD);
                        msg += card.getActionMsg() + ".<br>";
                    }
                }
            }  
        
            request.getSession().setAttribute("card", card);
            Cookie accnt = new Cookie("accnt",String.valueOf(card.getAccountId()));
            accnt.setMaxAge(120);
            accnt.setPath("/");
            response.addCookie(accnt);
            
        } else {// if card == null
            msg += "No account active <br>";
        }
        request.setAttribute("msg", msg);
        RequestDispatcher disp = getServletContext().getRequestDispatcher(url);
        disp.forward(request,response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
