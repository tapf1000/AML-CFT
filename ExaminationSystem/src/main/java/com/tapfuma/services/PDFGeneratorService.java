package com.tapfuma.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.tapfuma.dto.UserScore;
import com.tapfuma.entities.User;

@Service
public class PDFGeneratorService {
	
//	@Autowired
//	private static ResourceLoader resourceLoader;

	public static ByteArrayInputStream generateCandidateReport(User user, List<UserScore> userScores) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
		Document document = new Document();
		PdfWriter.getInstance(document, baos);
		document.open();
		int i = 1;
		userScores.forEach(u->{
			Paragraph p = new Paragraph(userScores.indexOf(u)+" Test: "+u.getTest().getName()+" Mark:"+u.getScore()+"%");
			try {document.add(p);} catch (DocumentException e) {e.printStackTrace();}
		});
		
//		Phrase phrase = new Phrase();		
//		final Resource fileResource = resourceLoader.getResource("classpath:img/money-transfers.jpg");
//				Image img = Image.getInstance(fileResource.getFilename());
//				img.scaleAbsolute(10, 10);
//				phrase.add(new Chunk(img, 0,0));
//				document.add(img);
			
			
			document.close();
		}catch(DocumentException de){
			de.printStackTrace();
		}
		return new ByteArrayInputStream(baos.toByteArray());		
	}
}
