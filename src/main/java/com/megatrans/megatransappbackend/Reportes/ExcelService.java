package com.megatrans.megatransappbackend.Reportes;

import com.megatrans.megatransappbackend.Transporte_Mudanza.Entity.DetalleTransporte;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    public byte[] generarExcel(List<DetalleTransporte> detalles) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte Transporte");

        // Encabezados
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Num. Orden", "Unidad", "Estado", "Fecha Creacion", "Estibaje",
                "Cant. Estibaje", "Origen", "Destino", "Fecha Solicitada ",
                "Pago", "Tipo Servicio", "Usuario"};

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos
        int rowNum = 1;
        for (DetalleTransporte detalle : detalles) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(detalle.getId());
            row.createCell(1).setCellValue(detalle.getNumOrden());
            row.createCell(2).setCellValue(detalle.getUnidad().getTipo()); // Ajusta según tu clase Unidad
            row.createCell(3).setCellValue(detalle.getEstado());
            row.createCell(4).setCellValue(detalle.getFecha().toString());
            row.createCell(5).setCellValue(detalle.getEstibaje() ? "Sí" : "No");
            if (detalle.getCantidadEstibaje() !=null){
                row.createCell(6).setCellValue(detalle.getCantidadEstibaje());
            }else {
                row.createCell(6).setCellValue("N/A");
            }
            if (detalle.getDirOrigen() != null) {
                String direccionOrg = detalle.getDirOrigen().getCallePrincipal() + " - " +  detalle.getDirOrigen().getCalleSecundaria()  + " - " +  detalle.getDirOrigen().getCiudad();
                row.createCell(7).setCellValue(direccionOrg);
            } else {
                row.createCell(7).setCellValue("No disponible");
            }


            // Verificar si dirDestino es null antes de acceder a sus atributos
            if (detalle.getDirDestino() != null) {
                String direccionDes = detalle.getDirDestino().getCallePrincipal() + " - " + detalle.getDirDestino().getCalleSecundaria()+ " - " + detalle.getDirDestino().getCiudad();
                row.createCell(8).setCellValue(direccionDes);
            } else {
                row.createCell(8).setCellValue("No disponible"); // Mensaje por defecto
            }
            row.createCell(9).setCellValue(detalle.getDescripcionProducto());
            row.createCell(10).setCellValue(detalle.getPago());
            row.createCell(11).setCellValue(detalle.getTipoServicio());
            row.createCell(12).setCellValue(detalle.getUsuario().getNombre()); // Ajusta según Usuario
        }

        // Ajustar tamaño de columnas
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }
}