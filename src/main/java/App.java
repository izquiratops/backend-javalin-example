import io.javalin.Javalin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.server(() -> {
                Server server = new Server();
                ServerConnector sslConnector = new ServerConnector(server, getSslContextFactory());
                sslConnector.setPort(80);
                ServerConnector connector = new ServerConnector(server);
                connector.setPort(443);
                server.setConnectors(new Connector[]{sslConnector, connector});
                return server;
            });
        }).start();

        app.routes(() -> {
            app.get("/", ctx -> ctx.result("Hello World"));
        });
    }
    private static SslContextFactory getSslContextFactory() {
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(System.getProperty("user.dir") + "/keystore.jks");
        sslContextFactory.setKeyStorePassword("rebotado");
        return sslContextFactory;
    }
}
