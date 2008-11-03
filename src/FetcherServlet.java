import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.management.*;
import java.io.IOException;

public class FetcherServlet extends HttpServlet {
    private Stats stats;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.stats = (Stats) servletConfig.getServletContext().getAttribute("stats");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/plain");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            printUsage(res);
            return;
        }

        String[] parts = pathInfo.split("/");
        if (parts == null || parts.length != 4) {
            printUsage(res);
            return;
        }

        String[] serverAndPort = parts[1].split(":");
        if (serverAndPort == null || serverAndPort.length != 2) {
            printUsage(res);
            return;
        }

        String host = serverAndPort[0];
        int port = Integer.parseInt(serverAndPort[1]);
        String mbean = parts[2];

        String attr = parts[3];

        try {
            final String value = stats.fetch(host, port, mbean, attr).toString();
            res.setContentLength(value.length());
            res.getWriter().print(value);
        } catch (MalformedObjectNameException e) {
            throw new ServletException(e);
        } catch (ReflectionException e) {
            throw new ServletException(e);
        } catch (InstanceNotFoundException e) {
            throw new ServletException(e);
        } catch (MBeanException e) {
            throw new ServletException(e);
        } catch (AttributeNotFoundException e) {
            throw new ServletException(e);
        }

    }

    private void printUsage(HttpServletResponse res) throws IOException {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        res.getWriter().print("Format: /<hostname>:<port>/<mbean>/<attrname>");
    }
}
