import io.javalin.http.Handler;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;

public class ProcessController {
    public static Handler Upload = ctx -> {
        ctx.req.setAttribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        final Collection<Part> parts = ctx.req.getParts();

        int index = 0;
        for (Part part : parts) {
            System.out.println("Part " + (index++));
            System.out.println("Name: " + part.getName());
            System.out.println("Size: " + part.getSize() + " bytes");
            System.out.println("Filename: " + part.getSubmittedFileName());
            System.out.println("----------------------------------------------------");
        }

        final String description = ctx.req.getParameter("description");
        System.out.println("Description: " + description);

        final Part uploadedFile = ctx.req.getPart("file");
        final Path out = Paths.get(System.getProperty("user.dir") + "/received_data/" + uploadedFile.getSubmittedFileName());

        try (final InputStream in = uploadedFile.getInputStream()) {
            Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
            uploadedFile.delete();
        }

        System.out.println("------------------ End of request ------------------");
        ctx.result("File Saved");
    };
}
