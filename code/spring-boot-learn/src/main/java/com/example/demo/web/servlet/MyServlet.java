package com.example.demo.web.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/5/19
 * @Description ${DESCRIPTION}
 */
@WebServlet(urlPatterns = "/my/Servlet",asyncSupported = true)
public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("Hello World  get method");
        AsyncContext asyncContext = req.startAsync();

        asyncContext.start(()->{
            try{
                try {
                    TimeUnit.SECONDS.sleep(5);
                    resp.getWriter().print("Hello asyncSupported");
                    //触发完成
                    asyncContext.complete();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        });

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("Hello World post method");
    }
}
