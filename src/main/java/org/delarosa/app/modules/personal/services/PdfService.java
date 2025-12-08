package org.delarosa.app.modules.personal.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.clinico.dtos.RecetaPDF;
import org.delarosa.app.modules.clinico.entities.Receta;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor

public class PdfService {

    private final SpringTemplateEngine templateEngine;

    public byte[] generarRecetaPdf(RecetaPDF recetaPdf) throws Exception {
        Context context = new Context();
        context.setVariable("receta", recetaPdf);

        String html = templateEngine.process("receta", context);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(baos);
            builder.run();
            return baos.toByteArray();
        }
    }
}
