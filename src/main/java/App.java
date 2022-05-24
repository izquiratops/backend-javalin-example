import io.javalin.Javalin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.File;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.server(App::CreateSecureServer);
        }).start();

        app.routes(() -> {
            app.get("/", ctx -> ctx.result("Hello World"));
            app.post("/upload", ProcessController.Upload);
        });
    }

    private static Server CreateSecureServer() {
        Server server = new Server();

        // HTTPS
        ServerConnector sslConnector = new ServerConnector(server, getSslContextFactory());
        sslConnector.setPort(443);

        // HTTP
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(80);

        // server.setConnectors(new Connector[]{connector});
        server.setConnectors(new Connector[]{sslConnector, connector});

        return server;
    }

    private static SslContextFactory getSslContextFactory() {
        SslContextFactory sslContextFactory = new SslContextFactory.Server();
        File f = new File("/webserver/certs/keystore.jks");
        System.out.println("!!! ->> " + String.valueOf(f.exists()));
        sslContextFactory.setKeyStorePath("/webserver/certs/keystore.jks");
        sslContextFactory.setKeyStorePassword(System.getenv().get("KEYSTORE_PASSWORD"));
        return sslContextFactory;
    }
}
