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

        // Agregar logo
        try {
            Image logo = Image.getInstance("http://localhost:8080/qr_codes/MegaTransLogo.png"); // Cambiar ruta si es necesario
            logo.scaleToFit(150, 50);
            logo.setAlignment(Element.ALIGN_LEFT);
            document.add(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Título del documento
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("REPORTE DE LOTE: " + lote.getNumLote(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));


        // Obtener las encomiendas
        List<DetalleEncomiendaDTO> encomiendas = detalleEncomiendaService.getDetalleEncomiendaByLoteId(loteId);
        for (DetalleEncomiendaDTO encomiendaDTO : encomiendas) {
            PdfPTable table = new PdfPTable(4); // Usando dos columnas
            table.setWidthPercentage(100); // Estableciendo que la tabla ocupe todo el ancho
            table.setSpacingBefore(10f); // Espaciado antes de la tabla
            table.setSpacingAfter(10f);  // Espaciado después de la tabla

// Información de la encomienda

// Fila 1: N° Guía y N° Lote en una sola fila sin columnas
            PdfPTable singleRowTable1 = new PdfPTable(1);  // Tabla de una columna para esta fila
            singleRowTable1.setWidthPercentage(100);
            String guiaYLote = "N° Guía: " + encomiendaDTO.getNumGuia() + "   N° Lote: " + lote.getNumLote();
            addTableRowSingleColumn(singleRowTable1, guiaYLote);
            document.add(singleRowTable1);

// Fila 2: Fecha en una sola fila sin columnas
            PdfPTable singleRowTable2 = new PdfPTable(1);  // Tabla de una columna para esta fila
            singleRowTable2.setWidthPercentage(100);
            addTableRowSingleColumn(singleRowTable2, "Fecha: " + String.valueOf(encomiendaDTO.getFecha()));
            document.add(singleRowTable2);

// Fila 3: Remitente y Identificación
            addTableRow(table, "Remitente:", encomiendaDTO.getUsuario().getNombre() + " " + encomiendaDTO.getUsuario().getApellido());
            addTableRow(table, "Identificación:", encomiendaDTO.getUsuario().getIdentificacion());
            String nombreComercial = encomiendaDTO.getUsuario().getNombreComercial();
            addTableRow(table, "Nombre Comercial:", (nombreComercial != null && !nombreComercial.isEmpty()) ? nombreComercial : "N/A");

// Fila 5: Destinatario y su Identificación
            addTableRow(table, "Destinatario:", encomiendaDTO.getNombreD() + " " + encomiendaDTO.getApellidoD());
            addTableRow(table, "Identificación Destinatario:", encomiendaDTO.getIdentificacionD());

// Fila 6: Dirección y Referencia
            addTableRow(table, "Dirección:", encomiendaDTO.getDirRemitente());
            addTableRow(table, "Referencia:", encomiendaDTO.getReferenciaD());

// Concatenar productos y agregar a la tabla
            String productosConcatenados = encomiendaDTO.getProductosDto().stream()
                    .map(p -> p.getTipoProducto() + " (" + p.getPeso() + "kg, " + p.getAlto() + "x" + p.getAncho() + "x" + p.getLargo() + ")")
                    .collect(Collectors.joining("\n"));
            addTableRow(table, "Productos:", productosConcatenados);


            // Agregar código QR
            try {
                String qrCodeUrl = "http://localhost:8080" + encomiendaDTO.getQrCodePath();
                Image qrImage = Image.getInstance(new URL(qrCodeUrl));
                qrImage.scaleToFit(100, 100);
                PdfPCell qrCell = new PdfPCell(qrImage);
                qrCell.setColspan(4);
                qrCell.setPaddingTop(15f); // Agrega margen superior
                qrCell.setPaddingBottom(5f); // Ajuste de espacio inferior
                qrCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                qrCell.setBorder(Rectangle.NO_BORDER);
                table.addCell(qrCell);
            } catch (Exception e) {
                e.printStackTrace();
            }

            document.add(table);
            document.add(new Paragraph("\n"));
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

}
