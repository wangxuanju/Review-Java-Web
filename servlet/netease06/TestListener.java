import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class TestListener implements HttpSessionAttributeListener,
		ServletContextListener, ServletRequestListener {

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		System.out.println("listener: request destroy");
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		System.out.println("listener: request init");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("listener: context init");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("listener: context destroy");
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		System.out.println("listener: session attribute added.");
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		System.out.println("listener: session attribute removed");
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		System.out.println("listener: session attribute replaced");
	}

}
