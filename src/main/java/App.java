import io.javalin.Javalin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.File;

public class App {
    public static void main(String[] args) {
        File directory = new File(ProcessController.RECEIVED_DATA_PATH);
        if (! directory.exists()){
            directory.mkdir();
        }

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
        sslConnector.setPort(7071);

        // HTTP
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(7070);

        // server.setConnectors(new Connector[]{connector});
        server.setConnectors(new Connector[]{sslConnector, connector});

        return server;
    }

    private static SslContextFactory getSslContextFactory() {
        SslContextFactory sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(System.getProperty("user.dir") + "/keystore.jks");
        sslContextFactory.setKeyStorePassword("rebotado");
        return sslContextFactory;
    }
}
