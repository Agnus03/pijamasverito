package com.pijamasverito.app.utilities;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import com.pijamasverito.app.entity.Pedido;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component()
public class ListarPedidoPdf extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Date fechaActual = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String fechaFormateada = sdf.format(fechaActual);

		// Establece el nombre del archivo PDF
		String fileName = "Historial.pdf"; // Cambia el nombre del archivo a lo que desees
		response.setHeader("Content-Disposition", "inline; filename=" + fileName);

		@SuppressWarnings("unchecked")
		List<Pedido> listadoAlquileres = (List<Pedido>) model.get("pedidos");

		document.setPageSize(PageSize.LETTER.rotate());
		document.setMargins(-10, -10, 40, 10);
		document.open();


		Font fuenteEmpresa = FontFactory.getFont("Helvetica", 11, Font.BOLD, Color.BLACK);
		fuenteEmpresa.setStyle(Font.UNDERLINE); 
		fuenteEmpresa.setFamily("Arial"); 
		fuenteEmpresa.setStyle(Font.ITALIC); 

		PdfPTable tablaEncabezado = new PdfPTable(2); // Cambia el número de columnas a 2 para dividir el título del
														// texto
		tablaEncabezado.setSpacingBefore(20); // Espacio antes de la tabla
		tablaEncabezado.setSpacingAfter(20); // Espacio después de la tabla
		tablaEncabezado.getDefaultCell().setBorder(Rectangle.BOX); // Agrega bordes a las celdas

		// Agrega la información de la empresa en la parte superior izquierda
		PdfPCell cellTitulo = new PdfPCell(new Phrase("Empresa:", fuenteEmpresa)); // Título
		cellTitulo.setBackgroundColor(new Color(237, 237, 237));
		cellTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellTitulo.setPadding(6);
		tablaEncabezado.addCell(cellTitulo);

		PdfPCell cellValor = new PdfPCell(new Phrase("Pijamas Verito", fuenteEmpresa)); // Valor
		cellValor.setBackgroundColor(new Color(237, 237, 237));
		cellValor.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellValor.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellValor.setPadding(6);
		tablaEncabezado.addCell(cellValor);

		cellTitulo = new PdfPCell(new Phrase("Dirección:", fuenteEmpresa)); // Título
		cellTitulo.setBackgroundColor(new Color(237, 237, 237));
		cellTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellTitulo.setPadding(6);
		tablaEncabezado.addCell(cellTitulo);

		cellValor = new PdfPCell(new Phrase("Centro Plaza Local 150", fuenteEmpresa)); // Valor
		cellValor.setBackgroundColor(new Color(237, 237, 237));
		cellValor.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellValor.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellValor.setPadding(6);
		tablaEncabezado.addCell(cellValor);

		cellTitulo = new PdfPCell(new Phrase("Teléfono:", fuenteEmpresa)); // Título
		cellTitulo.setBackgroundColor(new Color(237, 237, 237));
		cellTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellTitulo.setPadding(6);
		tablaEncabezado.addCell(cellTitulo);

		cellValor = new PdfPCell(new Phrase("+57 3173142803", fuenteEmpresa)); // Valor
		cellValor.setBackgroundColor(new Color(237, 237, 237));
		cellValor.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellValor.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellValor.setPadding(6);
		tablaEncabezado.addCell(cellValor);

		cellTitulo = new PdfPCell(new Phrase("Correo:", fuenteEmpresa)); // Título
		cellTitulo.setBackgroundColor(new Color(237, 237, 237));
		cellTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellTitulo.setPadding(6);
		tablaEncabezado.addCell(cellTitulo);

		cellValor = new PdfPCell(new Phrase("correoayuda@pijamasverito.com", fuenteEmpresa)); // Valor
		cellValor.setBackgroundColor(new Color(237, 237, 237));
		cellValor.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellValor.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellValor.setPadding(6);
		tablaEncabezado.addCell(cellValor);

		cellTitulo = new PdfPCell(new Phrase("Fecha:", fuenteEmpresa)); // Título
		cellTitulo.setBackgroundColor(new Color(237, 237, 237));
		cellTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellTitulo.setPadding(6);
		tablaEncabezado.addCell(cellTitulo);

		cellValor = new PdfPCell(new Phrase(fechaFormateada, fuenteEmpresa)); // Valor
		cellValor.setBackgroundColor(new Color(237, 237, 237));
		cellValor.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellValor.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cellValor.setPadding(6);
		tablaEncabezado.addCell(cellValor);

		document.add(tablaEncabezado);

		PdfPTable tablaTitulo = new PdfPTable(1);
		PdfPCell celda = null;

		Font fuenteTitulo = FontFactory.getFont("Helvetica", 16, Color.WHITE);

		celda = new PdfPCell(new Phrase("¡PEDIDOS HECHOS!", fuenteTitulo));
		celda.setBorder(0);
		celda.setBackgroundColor(new Color(60, 60, 60));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celda.setPadding(30);

		tablaTitulo.addCell(celda);
		tablaTitulo.setSpacingAfter(30);

		PdfPTable tablaClientes = new PdfPTable(6);

		Font fuenteCeldas = FontFactory.getFont("Helvetica", 9); 
		Font fuenteTitulos = FontFactory.getFont("Helvetica", 10, Color.WHITE);

		// Agrega los títulos de las columnas centrados
		PdfPCell cell = new PdfPCell(new Phrase("ID", fuenteTitulos));

		cell = new PdfPCell(new Phrase("Empleado", fuenteTitulos));
		cell.setPadding(5);
		cell.setBackgroundColor(new Color(60, 60, 60));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tablaClientes.addCell(cell);

		cell = new PdfPCell(new Phrase("Tipo", fuenteTitulos));
		cell.setPadding(5);
		cell.setBackgroundColor(new Color(60, 60, 60));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tablaClientes.addCell(cell);

		cell = new PdfPCell(new Phrase("Referencia ", fuenteTitulos));
		cell.setPadding(5);
		cell.setBackgroundColor(new Color(60, 60, 60));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tablaClientes.addCell(cell);

		cell = new PdfPCell(new Phrase("Fecha", fuenteTitulos));
		cell.setPadding(5);
		cell.setBackgroundColor(new Color(60, 60, 60));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tablaClientes.addCell(cell);

		cell = new PdfPCell(new Phrase("Estado", fuenteTitulos));
		cell.setPadding(5);
		cell.setBackgroundColor(new Color(60, 60, 60));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tablaClientes.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Observaciones", fuenteTitulos));
		cell.setPadding(5);
		cell.setBackgroundColor(new Color(60, 60, 60));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tablaClientes.addCell(cell);

		listadoAlquileres.forEach(pedido -> {
			PdfPCell cell2 = new PdfPCell(new Phrase(pedido.getId(), fuenteCeldas));

			cell2 = new PdfPCell(new Phrase(pedido.getEmpleado().getDocumento(), fuenteCeldas));
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell2.setPadding(5); // Establece el relleno interno
			tablaClientes.addCell(cell2);

			cell2 = new PdfPCell(new Phrase(pedido.getInventario().getTipo(), fuenteCeldas));
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell2.setPadding(5); // Establece el relleno interno
			tablaClientes.addCell(cell2);

			cell2 = new PdfPCell(new Phrase(pedido.getInventario().getReferencia(), fuenteCeldas));
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell2.setPadding(5); // Establece el relleno interno
			tablaClientes.addCell(cell2);

			cell2 = new PdfPCell(new Phrase(pedido.getFecha(), fuenteCeldas));
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell2.setPadding(5); // Establece el relleno interno
			tablaClientes.addCell(cell2);

			cell2 = new PdfPCell(new Phrase(pedido.getEstado(), fuenteCeldas));
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell2.setPadding(5); // Establece el relleno interno
			tablaClientes.addCell(cell2);

			cell2 = new PdfPCell(new Phrase(pedido.getObservaciones(), fuenteCeldas));
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell2.setPadding(5); // Establece el relleno interno
			tablaClientes.addCell(cell2);
		});

		document.add(tablaTitulo);
		document.add(tablaClientes);

	}

}
