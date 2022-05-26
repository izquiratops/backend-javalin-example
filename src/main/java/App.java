import io.javalin.Javalin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class App {

    /* TODO: get secrets as env variables */
    final static String KEYSTORE_PASSWORD;
    final static String CRYPTO_KEY;
    final static String CRYPTO_SALT;

    static {
        try {
            KEYSTORE_PASSWORD = new String(Files.readAllBytes(
                    Path.of("/run/secrets/keystore_password"))
            );
            CRYPTO_KEY = new String(Files.readAllBytes(
                    Path.of("/run/secrets/javax_key_encryption"))
            );
            CRYPTO_SALT = new String(Files.readAllBytes(
                    Path.of("/run/secrets/javax_salt"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

        server.setConnectors(new Connector[]{connector});

        return server;
    }

    private static SslContextFactory getSslContextFactory() {
        SslContextFactory sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath("/webserver/certs/keystore.jks");
        sslContextFactory.setKeyStorePassword(KEYSTORE_PASSWORD);

        return sslContextFactory;
    }
}
