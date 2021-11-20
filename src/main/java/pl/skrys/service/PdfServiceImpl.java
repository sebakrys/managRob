package pl.skrys.service;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import pl.skrys.app.SpBuilding;
import pl.skrys.app.SpFlat;
import pl.skrys.app.SpFlatCharges;
import pl.skrys.app.SpUserApp;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService{

    @Override
    public void generateRachunekPdf(SpFlatCharges flatCharges, List<SpUserApp> usersList, SpUserApp userCharges, boolean ryczalt, HttpServletResponse response) {
        try {
            SpFlat tempFlat = flatCharges.getFlat();
            SpBuilding tempBuilding = flatCharges.getFlat().getBuilding();

            OutputStream o = response.getOutputStream();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/pdf");//todo \/ nazwa pliku: idBud_idFlat_Rok_Miesiac.pdf
            response.setHeader("Content-Disposition", "inline; filename=" +tempFlat.getBuilding().getId()+"_"+tempFlat.getId()+"_"+flatCharges.getData().getYear()+"_"+flatCharges.getData().getMonth() + ".pdf");
            Document pdf = new Document();
            PdfWriter.getInstance(pdf, o);
            pdf.open();
            if(ryczalt){
                pdf.add(new Paragraph("Bill with a lump sum"));
            }else{
                pdf.add(new Paragraph("Bill"));
            }
            pdf.add(new Paragraph(Chunk.NEWLINE));
            PdfPTable table1 = new PdfPTable(2);

            table1.addCell("Address");
            table1.addCell("Ul. "+tempBuilding.getStreet()+" nr. "+tempBuilding.getBuildingNumber()+"/"+tempFlat.getFlatNumber()+"\r\n"
                    +tempBuilding.getPostalCode()+", "+tempBuilding.getCity());

            table1.addCell("Period");
            table1.addCell(flatCharges.getData().getYear()+" "+flatCharges.getData().getMonth());

            pdf.add(table1);



            PdfPTable table2 = new PdfPTable(4);

            double suma = 0;
            DecimalFormat df = new DecimalFormat("#.00");

            table2.addCell("Fundusz Remontowy");
            table2.addCell(String.valueOf(flatCharges.getFunduszRemontowy()));
            table2.addCell(String.valueOf(flatCharges.getFunduszRemontowy_stawka()));
            table2.addCell(String.valueOf(df.format(flatCharges.getFunduszRemontowy_stawka()
                    *flatCharges.getFunduszRemontowy())));
            suma+=flatCharges.getFunduszRemontowy_stawka()
                    *flatCharges.getFunduszRemontowy();

            table2.addCell("Gaz");
            table2.addCell(String.valueOf(flatCharges.getGaz()));
            table2.addCell(String.valueOf(flatCharges.getGaz_stawka()));
            table2.addCell(String.valueOf(df.format(flatCharges.getGaz_stawka()
                    *flatCharges.getGaz())));
            suma+=flatCharges.getGaz_stawka()
                    *flatCharges.getGaz();

            table2.addCell("Ogrzewanie");
            table2.addCell(String.valueOf(flatCharges.getOgrzewanie()));
            table2.addCell(String.valueOf(flatCharges.getOgrzewanie_stawka()));
            table2.addCell(String.valueOf(df.format(flatCharges.getOgrzewanie_stawka()
                    *flatCharges.getOgrzewanie())));
            suma+=flatCharges.getOgrzewanie_stawka()
                    *flatCharges.getOgrzewanie();

            table2.addCell("Prad");
            table2.addCell(String.valueOf(flatCharges.getPrad()));
            table2.addCell(String.valueOf(flatCharges.getPrad_stawka()));
            table2.addCell(String.valueOf(df.format(flatCharges.getPrad_stawka()
                    *flatCharges.getPrad())));
            suma+=flatCharges.getPrad_stawka()
                    *flatCharges.getPrad();

            table2.addCell("Scieki");
            table2.addCell(String.valueOf(flatCharges.getScieki()));
            table2.addCell(String.valueOf(flatCharges.getScieki_stawka()));
            table2.addCell(String.valueOf(df.format(flatCharges.getScieki_stawka()
                    *flatCharges.getScieki())));
            suma+=flatCharges.getScieki_stawka()
                    *flatCharges.getScieki();

            table2.addCell("Ciepla woda");
            table2.addCell(String.valueOf(flatCharges.getWoda_ciepla()));
            table2.addCell(String.valueOf(flatCharges.getWoda_ciepla_stawka()));
            table2.addCell(String.valueOf(df.format(flatCharges.getWoda_ciepla_stawka()
                    *flatCharges.getWoda_ciepla())));
            suma+=flatCharges.getWoda_ciepla_stawka()
                    *flatCharges.getWoda_ciepla();

            table2.addCell("Zimna zimna");
            table2.addCell(String.valueOf(flatCharges.getWoda_zimna()));
            table2.addCell(String.valueOf(flatCharges.getWoda_zimna_stawka()));
            table2.addCell(String.valueOf(df.format(flatCharges.getWoda_zimna_stawka()
                    *flatCharges.getWoda_zimna())));
            suma+=flatCharges.getWoda_zimna_stawka()
                    *flatCharges.getWoda_zimna();

            table2.addCell("Suma");
            table2.addCell("");
            table2.addCell("");
            table2.addCell(String.valueOf(df.format(suma)));

            table2.addCell(" ");
            table2.addCell(" ");
            table2.addCell(" ");
            table2.addCell(" ");

            table2.addCell(" ");
            table2.addCell(" ");
            table2.addCell(" ");
            table2.addCell("Locators");



            for (SpUserApp tempUser : usersList) {

                if(tempUser.getId()==userCharges.getId()){
                    table2.addCell("Copy for");
                }else{
                    table2.addCell("");
                }



                table2.addCell("");
                table2.addCell("");
                table2.addCell(tempUser.getFirstName()+" "+tempUser.getLastName());
            }



            pdf.add(table2);






            pdf.close();
            o.close();

        }catch (IOException | com.itextpdf.text.DocumentException e){
            e.printStackTrace();
        }
    }

    @Override
    public void generatePdf(SpUserApp userApp, HttpServletResponse response) {
        try {
            OutputStream o = response.getOutputStream();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + userApp.getPesel() + ".pdf");
            Document pdf = new Document();
            PdfWriter.getInstance(pdf, o);
            pdf.open();
            pdf.add(new Paragraph("Pdf example - KAPJ"));
            pdf.add(new Paragraph(Chunk.NEWLINE));
            PdfPTable table = new PdfPTable(2);
            table.addCell("FirsName");
            table.addCell(userApp.getFirstName());
            table.addCell("LastName");
            table.addCell(userApp.getLastName());
            table.addCell("PESEL");
            table.addCell(userApp.getPesel());
            table.addCell("Login");
            table.addCell(userApp.getPesel());
            table.addCell("Email");
            table.addCell(userApp.getEmail());
            table.addCell("Active");
            table.addCell(String.valueOf(userApp.isEnabled()));
            pdf.add(table);
            pdf.close();
            o.close();

        }catch (IOException | com.itextpdf.text.DocumentException e){
            e.printStackTrace();
        }
    }
}
