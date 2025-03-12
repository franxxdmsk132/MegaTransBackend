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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
public class PDFReportServiceLot {
    @Autowired
    private DetalleEncomiendaService detalleEncomiendaService;

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
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Información de la encomienda
            addTableRow(table, "N° Guía:", encomiendaDTO.getNumGuia());
            addTableRow(table, "Estado:", String.valueOf(encomiendaDTO.getEstado()));
            addTableRow(table, "Destinatario:", encomiendaDTO.getNombreD());
            addTableRow(table, "Dirección:", encomiendaDTO.getDirDestino());

            // Agregar código QR
            try {
                String qrCodeUrl = "http://localhost:8080" + encomiendaDTO.getQrCodePath();
                Image qrImage = Image.getInstance(new URL(qrCodeUrl));
                qrImage.scaleToFit(100, 100);
                PdfPCell qrCell = new PdfPCell(qrImage);
                qrCell.setColspan(2);
                qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
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

    private void addTableRow(PdfPTable table, String label, String value) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, new Font(Font.HELVETICA, 10, Font.BOLD)));
        PdfPCell cell2 = new PdfPCell(new Phrase(value, new Font(Font.HELVETICA, 10)));
        cell1.setPadding(5);
        cell2.setPadding(5);
        table.addCell(cell1);
        table.addCell(cell2);
    }
}
