package com.megatrans.megatransappbackend.Reportes;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.megatrans.megatransappbackend.Encomiendas.DTO.DetalleEncomiendaDTO;
import com.megatrans.megatransappbackend.Encomiendas.Entity.DetalleEncomienda;
import com.megatrans.megatransappbackend.Encomiendas.Repository.DetalleEncomiendaRepository;
import com.megatrans.megatransappbackend.Encomiendas.Service.DetalleEncomiendaService;
import com.megatrans.megatransappbackend.Lote.DTO.LoteDTO;
import com.megatrans.megatransappbackend.Lote.Entity.Lote;
import com.megatrans.megatransappbackend.Lote.Service.LoteService;
import com.megatrans.megatransappbackend.Security.dto.LoginUsuario;
import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.Security;
import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.text.html.HTML.Attribute.N;

@Service
public class PDFReportServiceLot {
    @Autowired
    private DetalleEncomiendaService detalleEncomiendaService;
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LoteService loteService;

    public byte[] generarReportePdfLote(Integer loteId) throws IOException, DocumentException {
        LoteDTO lote = loteService.obtenerLotePorId(loteId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();


        // Obtener las encomiendas
        List<DetalleEncomiendaDTO> encomiendas = detalleEncomiendaService.getDetalleEncomiendaByLoteId(loteId);
        int reportesPorPagina = 2; // Máximo de reportes por hoja
        int contador = 0;

        // Calcular el espacio disponible en la página
        float pageHeight = PageSize.A4.getHeight() - document.topMargin() - document.bottomMargin();
        float reporteHeight = 0;

        for (DetalleEncomiendaDTO encomiendaDTO : encomiendas) {
            PdfPTable reportTable = new PdfPTable(2); // Dos columnas para organizar mejor
            PdfPTable table = new PdfPTable(4); // Usando dos columnas
            table.setWidthPercentage(100f); // Estableciendo que la tabla ocupe todo el ancho
            table.setSpacingBefore(20f); // Espaciado antes de la tabla
            table.setSpacingAfter(20f);  // Espaciado después de la tabla


//Fila1
            // Crear una tabla de 2 columnas (Logo | QR)
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{80, 80}); // Columnas de igual tamaño
            headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER); // Quitar bordes

            try {
                // Cargar logo
                Image logo = Image.getInstance("http://localhost:8080/qr_codes/MegaTransLogo.png");
                logo.scaleToFit(70f, 70f);

                // Crear celda para el logo (sin bordes)
                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                logoCell.setPadding(30f); // Espacio alrededor del logo

                // Cargar código QR
                String qrCodeUrl = "http://localhost:8080" + encomiendaDTO.getQrCodePath();
                Image qrImage = Image.getInstance(new URL(qrCodeUrl));
                qrImage.scaleToFit(70f, 70f);

                // Crear celda para el código QR (sin bordes)
                PdfPCell qrCell = new PdfPCell(qrImage);
                qrCell.setBorder(Rectangle.NO_BORDER);
                qrCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                qrCell.setPadding(30f); // Espacio alrededor del QR

                // Agregar las celdas a la tabla
                headerTable.addCell(logoCell);
                headerTable.addCell(qrCell);

                // Agregar la tabla al documento
                document.add(headerTable);

            } catch (Exception e) {
                e.printStackTrace();
            }

//fila2
//            // Título del documento
//            PdfPTable singleRowTable2 = new PdfPTable(1);  // Tabla de una columna para esta fila
//            singleRowTable1.setWidthPercentage(100);
//            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);Paragraph title = new Paragraph("REPORTE DE LOTE: " + lote.getNumLote(), titleFont);
//            title.setAlignment(Element.ALIGN_CENTER);
//            document.add(title);
//            document.add(new Paragraph("\n"));

// Información de la encomienda

// Fila 3: N° Guía y N° Lote en una sola fila sin columnas
            PdfPTable singleRowTable1 = new PdfPTable(1);  // Tabla de una columna para esta fila
            singleRowTable1.setWidthPercentage(100);
            String guiaYLote = "N° Guía: " + encomiendaDTO.getNumGuia() + "   N° Lote: " + lote.getNumLote();
            addTableRowSingleColumn(singleRowTable1, guiaYLote);
            document.add(singleRowTable1);

// Fila 4: Fecha en una sola fila sin columnas
            PdfPTable singleRowTable2 = new PdfPTable(1);  // Tabla de una columna para esta fila
            singleRowTable2.setWidthPercentage(100);
            addTableRowSingleColumn(singleRowTable2, "Fecha: " + String.valueOf(encomiendaDTO.getFecha()));
            document.add(singleRowTable2);

// Fila 5: Remitente y Identificación
            addTableRow(table, "Remitente:", encomiendaDTO.getUsuario().getNombre() + " " + encomiendaDTO.getUsuario().getApellido());
            addTableRow(table, "Identificación:", encomiendaDTO.getUsuario().getIdentificacion());
            String nombreComercial = encomiendaDTO.getUsuario().getNombreComercial();
            addTableRow(table, "Nombre Comercial:", (nombreComercial != null && !nombreComercial.isEmpty()) ? nombreComercial : "N/A");

// Fila 6: Destinatario y su Identificación
            addTableRow(table, "Destinatario:", encomiendaDTO.getNombreD() + " " + encomiendaDTO.getApellidoD());
            addTableRow(table, "Identificación Destinatario:", encomiendaDTO.getIdentificacionD());

// Fila 7: Dirección y Referencia
            addTableRow(table, "Dirección:", encomiendaDTO.getDirRemitente());
            addTableRow(table, "Referencia:", encomiendaDTO.getReferenciaD());

// Concatenar productos y agregar a la tabla
            String productosConcatenados = encomiendaDTO.getProductosDto().stream()
                    .map(p -> p.getTipoProducto() + " (" + p.getPeso() + "kg, " + p.getAlto() + "x" + p.getAncho() + "x" + p.getLargo() + ")")
                    .collect(Collectors.joining("\n"));
            addTableRow(table, "Productos:", productosConcatenados);

            PdfPTable qrTable = new PdfPTable(2);  // Tabla con dos columnas
            qrTable.setWidthPercentage(100);
            qrTable.setWidths(new float[]{70, 30}); // 70% para datos, 30% para el QR

            reporteHeight = reportTable.getTotalHeight();

            // Verificar si el reporte cabe en la página
            if (reporteHeight > pageHeight) {
                document.newPage(); // Crear nueva página si no cabe el reporte
            }
            document.add(table);
            document.add(new Paragraph("\n"));
        }
        contador++;

        // Si ya imprimimos 2 reportes, creamos una nueva página
        if (contador % reportesPorPagina == 0) {
            document.newPage();
        }

        document.close();
        return outputStream.toByteArray();
    }



    private void addTableRowSingleColumn(PdfPTable table, String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value, new Font(Font.HELVETICA, 10)));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);  // Alineación centrada, puedes cambiarla si es necesario
        table.addCell(cell);
    }

    private void addTableRow(PdfPTable table, String label, String value) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, new Font(Font.HELVETICA, 10, Font.BOLD))); // Columna de etiqueta
        PdfPCell cell2 = new PdfPCell(new Phrase(value, new Font(Font.HELVETICA, 10))); // Columna de valor
        cell1.setPadding(5);  // Espaciado dentro de la celda
        cell2.setPadding(5);  // Espaciado dentro de la celda
        table.addCell(cell1);  // Agregar la celda de la etiqueta
        table.addCell(cell2);  // Agregar la celda del valor
    }
    private PdfPTable createLogoTable(String logoUrl) throws BadElementException, IOException {
        // Crear tabla con 1 columna
        PdfPTable logoTable = new PdfPTable(1);
        logoTable.setWidthPercentage(100);  // Hacer que ocupe todo el ancho de la página

        // Crear la imagen
        Image logo = Image.getInstance(logoUrl);  // Usar URL o ruta local
        logo.scaleToFit(100, 50);  // Ajustar tamaño de la imagen
        logo.setAlignment(Element.ALIGN_LEFT);  // Alineación izquierda

        // Crear celda para la imagen
        PdfPCell logoCell = new PdfPCell(logo, true);
        logoCell.setBorder(Rectangle.NO_BORDER);  // Sin borde
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);  // Alineación de la celda
        logoCell.setPaddingBottom(10f);  // Espaciado en la parte inferior

        // Agregar la celda con la imagen a la tabla
        logoTable.addCell(logoCell);

        return logoTable;
    }


    private PdfPTable createTitleTable(String titleText) {
        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(100);

        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        PdfPCell titleCell = new PdfPCell(new Phrase(titleText, titleFont));
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleTable.addCell(titleCell);

        return titleTable;
    }

}
