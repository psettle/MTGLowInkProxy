package pdf_driver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import cards.*;

public class MagicProxyFile {
	private String filename;
	private ArrayList<Card> cards;
	
	public MagicProxyFile() {
		cards = new ArrayList<Card>();
		filename = null;
	}
	
	public void addCard(Card card)
	{
		cards.add(card);
	}
	
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	
	public Boolean createProxies()
	{
		if(filename == null)
		{
			return false;
		}
		try
		{
			Document document = new Document(PageSize.A4, 0, 0, 20, 0);
	        // step 2
		    PdfWriter.getInstance(document, new FileOutputStream(filename));
		        // step 3
		    document.open();
		        // step 4
		    document.add(makeTable());
		        // step 5
		    document.close();   
		} catch (DocumentException e){
			return false;
		} catch (FileNotFoundException e) {
			//shouldn't be hit
			return false;
		}
		return true;
	}
	
	private PdfPTable makeTable() throws DocumentException
	{
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(88.3f);
		if(cards.size() == 0)
		{
			throw new DocumentException();
		}
        // the cell object
		for(Card card: cards)
		{
			PdfPCell cell = new PdfPCell();
			Font smaller = FontFactory.getFont(FontFactory.TIMES, 10f);
			Phrase text = new Phrase(card.toString());
			text.setFont(smaller);
			cell.addElement(text);
			cell.setFixedHeight(270f);
			table.addCell(cell);
		}
		int cells_to_fill = (3 - cards.size() % 3) % 3;
		for(int i = 0; i < cells_to_fill; i++)
		{
			table.addCell("");
		}
		return table;
	}
}
