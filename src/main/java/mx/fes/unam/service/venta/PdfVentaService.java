package mx.fes.unam.service.venta;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import mx.fes.unam.model.entities.venta.ReporteVentasDTO;
import mx.fes.unam.model.entities.venta.ResumenReporteDTO;
import mx.fes.unam.model.entities.venta.VentaDTO;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
@Service
public class PdfVentaService {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Color COLOR_ENCABEZADO = new Color(167, 218, 255);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 10);

    public byte[] generarReporte(ReporteVentasDTO reporte) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(documento, salida);
            documento.open();

            Paragraph titulo = new Paragraph(reporte.getTitulo(),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10f);
            documento.add(titulo);

            String periodo = reporte.getDesde().equals(reporte.getHasta())
                    ? reporte.getDesde().format(FECHA)
                    : reporte.getDesde().format(FECHA) + " al " + reporte.getHasta().format(FECHA);
            documento.add(new Paragraph("Periodo: " + periodo, NORMAL));
            documento.add(new Paragraph("Filtros: " + reporte.getFiltros(), NORMAL));
            documento.add(new Paragraph("Número de ventas: " + reporte.getNumeroVentas(), NORMAL));
            documento.add(new Paragraph(" "));

            // Detalle
            PdfPTable tabla = new PdfPTable(new float[]{1.6f, 3f, 2f, 3f, 1.2f, 1.8f, 2f});
            tabla.setWidthPercentage(100);
            for (String h : new String[]{"Fecha", "Vendedor", "Bloque", "Producto", "Cant.", "Precio", "Total"}) {
                encabezado(tabla, h);
            }
            for (VentaDTO v : reporte.getVentas()) {
                celda(tabla, v.getFecha().format(FECHA), Element.ALIGN_LEFT, false);
                celda(tabla, v.getVendedorNombre(), Element.ALIGN_LEFT, false);
                celda(tabla, v.getBloqueNombre(), Element.ALIGN_LEFT, false);
                celda(tabla, v.getProductoNombre(), Element.ALIGN_LEFT, false);
                celda(tabla, String.valueOf(v.getCantidad()), Element.ALIGN_RIGHT, false);
                celda(tabla, dinero(v.getPrecioUnitario()), Element.ALIGN_RIGHT, false);
                celda(tabla, dinero(v.getTotal()), Element.ALIGN_RIGHT, false);
            }
            PdfPCell etiqueta = new PdfPCell(new Phrase("Total general", NEGRITA));
            etiqueta.setColspan(6);
            etiqueta.setHorizontalAlignment(Element.ALIGN_RIGHT);
            etiqueta.setPadding(6f);
            tabla.addCell(etiqueta);
            celda(tabla, dinero(reporte.getTotalGeneral()), Element.ALIGN_RIGHT, true);
            documento.add(tabla);

            // Subtotales (solo si se agrupó)
            if (!reporte.getResumen().isEmpty()) {
                Paragraph sub = new Paragraph("Resumen por " + reporte.getAgruparPor().name().toLowerCase(),
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
                sub.setSpacingBefore(16f);
                sub.setSpacingAfter(6f);
                documento.add(sub);

                PdfPTable resumen = new PdfPTable(new float[]{5f, 1.5f, 2.5f});
                resumen.setWidthPercentage(70);
                resumen.setHorizontalAlignment(Element.ALIGN_LEFT);
                encabezado(resumen, reporte.getAgruparPor().name().substring(0, 1)
                        + reporte.getAgruparPor().name().substring(1).toLowerCase());
                encabezado(resumen, "Ventas");
                encabezado(resumen, "Total");
                for (ResumenReporteDTO r : reporte.getResumen()) {
                    celda(resumen, r.getEtiqueta(), Element.ALIGN_LEFT, false);
                    celda(resumen, String.valueOf(r.getNumeroVentas()), Element.ALIGN_RIGHT, false);
                    celda(resumen, dinero(r.getTotal()), Element.ALIGN_RIGHT, false);
                }
                documento.add(resumen);
            }

            documento.close();
            return salida.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }

    private void encabezado(PdfPTable tabla, String texto) {
        PdfPCell c = new PdfPCell(new Phrase(texto, NEGRITA));
        c.setBackgroundColor(COLOR_ENCABEZADO);
        c.setPadding(7f);
        tabla.addCell(c);
    }

    private void celda(PdfPTable tabla, String texto, int alineacion, boolean negrita) {
        PdfPCell c = new PdfPCell(new Phrase(texto, negrita ? NEGRITA : NORMAL));
        c.setHorizontalAlignment(alineacion);
        c.setPadding(5f);
        tabla.addCell(c);
    }

    private String dinero(BigDecimal valor) {
        return valor == null ? "-" : String.format("$%,.2f", valor);
    }
}