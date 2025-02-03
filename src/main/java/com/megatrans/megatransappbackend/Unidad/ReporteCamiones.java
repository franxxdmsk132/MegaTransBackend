package com.megatrans.megatransappbackend.Unidad;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.megatrans.megatransappbackend.Unidad.Entity.Unidad;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@Service
public class ReporteCamiones {

//    private List<Unidad> listaCamiones;
//
//    public ReporteCamiones(List<Unidad> listaCamiones) {
//        super();
//        this.listaCamiones = listaCamiones;
//    }
//
//    private void escribirProductos(PdfPTable table) {
//        for (Unidad unidad : listaCamiones) {
//            table.addCell(String.valueOf(unidad.getId()));
//            table.addCell(unidad.getTipo());
//            table.addCell(unidad.getMarca());
//            table.addCell(unidad.getModelo());
//            table.addCell(unidad.getPlaca());
//            table.addCell(String.valueOf(unidad.getCapacidad()));
//            table.addCell(unidad.getTipo_cajon());
//            table.addCell(String.valueOf(unidad.getAltura()));
//            table.addCell(String.valueOf(unidad.getAncho()));
//            table.addCell(String.valueOf(unidad.getLargo()));
//            table.addCell(String.valueOf(unidad.getEstado()));
//
//        }
//
//    }
//
//    private void escribirCabecera(PdfPTable table) {
//        PdfPCell cell = new PdfPCell();
//
//        cell.setBackgroundColor(Color.blue);
//        cell.setPadding(5);
//        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
//        font.setColor(Color.WHITE);
//
//        cell.setPhrase(new Phrase("ID", font));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Tipo", font));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Marca", font));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Modelo", font));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Placa", font));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Capacidad", font));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Tipo Cajon", font));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Altura", font));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Ancho", font));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Largo", font));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Estado", font));
//        table.addCell(cell);
//    }
//
//    public void exportarReporte(HttpServletResponse response) throws DocumentException, IOException {
//        // Configuración de la página en orientación horizontal
//        Document document = new Document(PageSize.A4.rotate()); // Página en horizontal
//        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream()); // Instancia de PdfWriter
//        // Agregar pie de página
//        writer.setPageEvent(new FooterEvent());
//        document.open();
//
//        // Título del reporte
//        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
//        font.setColor(Color.BLUE);
//        font.setSize(18);
//
//        Paragraph paragraph = new Paragraph("Lista de Unidad", font);
//        paragraph.setAlignment(Element.ALIGN_CENTER);
//        document.add(paragraph);
//
//        // Crear tabla con los anchos ajustados para las columnas
//        PdfPTable table = new PdfPTable(11); // 11 columnas
//        table.setWidthPercentage(100); // Ajustar al 100% del ancho de la página
//        table.setSpacingBefore(15);
//
//        // Anchos personalizados para las columnas
//        table.setWidths(new float[]{
//                1f,   // ID
//                2.5f, // Tipo
//                2.5f, // Marca
//                2.5f, // Modelo
//                2.5f, // Placa
//                1.8f, // Capacidad
//                2.2f, // Tipo Cajón
//                1.5f, // Altura
//                1.5f, // Ancho
//                1.5f, // Largo
//                1.5f  // Estado
//        });
//
//        escribirCabecera(table);
//        escribirProductos(table);
//
//        document.add(table);
//        document.close();
//    }
//    // Clase para el evento del pie de página
//    class FooterEvent extends PdfPageEventHelper {
//        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.GRAY);
//
//        @Override
//        public void onEndPage(PdfWriter writer, Document document) {
//            PdfContentByte cb = writer.getDirectContent();
//            Phrase footer = new Phrase("Mega Trans", footerFont);
//
//            // Coordenadas para el pie de página
//            ColumnText.showTextAligned(
//                    cb,
//                    Element.ALIGN_CENTER,
//                    footer,
//                    (document.right() + document.left()) / 2,
//                    document.bottom() - 10,
//                    0
//            );
//        }
//    }

}