package com.megatrans.megatransappbackend.Reportes;

import com.megatrans.megatransappbackend.Encomiendas.Entity.DetalleEncomienda;
import com.megatrans.megatransappbackend.Lote.Entity.Lote;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelServiceLot {

    public byte[] generarReporteExcelLote(List<Lote> lotes) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Lotes");

        // Estilos para el encabezado
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Crear fila de encabezado
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID Lote");
        headerRow.createCell(1).setCellValue("Número de Lote");
        headerRow.createCell(2).setCellValue("Fecha");
        headerRow.createCell(3).setCellValue("Estado");
        headerRow.createCell(4).setCellValue("Encargado");
        headerRow.createCell(5).setCellValue("Unidad");
        headerRow.createCell(6).setCellValue("Ruta");
        headerRow.createCell(7).setCellValue("Guías Relacionadas");
        headerRow.createCell(8).setCellValue("Conteo de Guías"); // Nueva columna para el conteo

        // Establecer el formato de fecha
        CellStyle dateCellStyle = workbook.createCellStyle();
        short dateFormat = workbook.createDataFormat().getFormat("yyyy-MM-dd");
        dateCellStyle.setDataFormat(dateFormat);

        int rowNum = 1;

        // Llenar las filas con los datos de los lotes
        for (Lote lote : lotes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(lote.getId());
            row.createCell(1).setCellValue(lote.getNumLote());
            Cell fechaCell = row.createCell(2);
            fechaCell.setCellValue(lote.getFecha());
            fechaCell.setCellStyle(dateCellStyle);
            row.createCell(3).setCellValue(lote.getEstado());
            row.createCell(4).setCellValue(lote.getEncargado());
            row.createCell(5).setCellValue(lote.getUnidad().getTipo());  // Asumiendo que la unidad tiene un nombre
            row.createCell(6).setCellValue(lote.getRuta());

            // Obtener los números de guía de los DetalleEncomienda asociados al Lote
            StringBuilder guiaRelacionadas = new StringBuilder();
            for (DetalleEncomienda detalle : lote.getDetalleEncomiendas()) {
                guiaRelacionadas.append(detalle.getNumGuia()).append(", ");
            }

            // Eliminar la última coma y espacio
            if (guiaRelacionadas.length() > 0) {
                guiaRelacionadas.setLength(guiaRelacionadas.length() - 2);
            }

            row.createCell(7).setCellValue(guiaRelacionadas.toString());
            // Agregar el conteo de guías
            int conteoGuías = lote.getDetalleEncomiendas().size();
            row.createCell(8).setCellValue(conteoGuías);
        }

        // Ajustar el tamaño de las columnas
        for (int i = 0; i < 8; i++) {
            sheet.autoSizeColumn(i);
        }

        // Convertir el archivo a byte[] y retornarlo
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } finally {
            workbook.close();
        }
    }
}