import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class StatsContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("stats", new Stats());
    }

    public void contextDestroyed(ServletContextEvent sce) {
        ((Stats)sce.getServletContext().getAttribute("stats")).destroy();
    }

}
