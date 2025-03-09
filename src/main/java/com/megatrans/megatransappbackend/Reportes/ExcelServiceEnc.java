package com.megatrans.megatransappbackend.Reportes;

import com.megatrans.megatransappbackend.Encomiendas.Entity.DetalleEncomienda;
import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.DetalleTransporte;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ExcelServiceEnc {

    public byte[] generarReporteExcel(List<DetalleEncomienda> encomiendas) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pedidos y Productos");

            // Crear encabezado
            Row headerRow = sheet.createRow(0);
            String[] columns = {
                    "ID Pedido","Usuario","N° Guía","N° Lote","Fecha Pedido","Ruta","Estado","Dirección Remitente",
                    "Dirección Destino","Nombre Destinatario", "Teléfono", "Correo",
                     "Tipo Entrega",  "Productos"
            };

            CellStyle headerStyle = estiloEncabezado(workbook);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar los datos
            int rowNum = 1;
            for (DetalleEncomienda pedido : encomiendas) {
                Row row = sheet.createRow(rowNum++);
                llenarFilaPedido(row, pedido);
            }

            // Autoajustar columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convertir a bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void llenarFilaPedido(Row row, DetalleEncomienda pedido) {
        String nombreComercial = (pedido.getUsuario().getNombreComercial() == null || pedido.getUsuario().getNombreComercial().isEmpty())
                ? "-"
                : pedido.getUsuario().getNombreComercial();

        row.createCell(0).setCellValue(pedido.getId());
        row.createCell(1).setCellValue(pedido.getUsuario().getNombre()+" "+ pedido.getUsuario().getApellido()+" / " +nombreComercial);
        row.createCell(2).setCellValue(pedido.getNumGuia());
        if (pedido.getLote() !=null){
            row.createCell(3).setCellValue(pedido.getLote().getNumLote());
        }else {
            row.createCell(3).setCellValue("Lote");
        }
        row.createCell(4).setCellValue(pedido.getFecha().toString());
        row.createCell(5).setCellValue(pedido.getRuta());
        row.createCell(6).setCellValue(pedido.getEstado());
        row.createCell(7).setCellValue(pedido.getDirRemitente());
        row.createCell(8).setCellValue(pedido.getDirDestino());
        row.createCell(9).setCellValue(pedido.getNombreD() + " " + pedido.getApellidoD());
        row.createCell(10).setCellValue(pedido.getTelfBeneficiario()+" / "+pedido.getTelfEncargado());
        row.createCell(11).setCellValue(pedido.getCorreoD());
        row.createCell(12).setCellValue(pedido.getTipoEntrega());

        // Concatenar los productos en una celda separada por comas
        String productosConcatenados = pedido.getProductos().stream()
                .map(p -> p.getTipoProducto() + " (ID: " + p.getId() + ", " + p.getPeso() + "kg, " + (p.isFragil() ? "Frágil" : "No Frágil") + ")")
                .collect(Collectors.joining("\n"));

        row.createCell(13).setCellValue(productosConcatenados);
    }

    private CellStyle estiloEncabezado(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}

