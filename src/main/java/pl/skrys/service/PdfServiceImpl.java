package pl.skrys.service;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import pl.skrys.app.SpStation;
import pl.skrys.app.SpRobot;
import pl.skrys.app.SpRobotCharges;
import pl.skrys.app.SpUserApp;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService{

    @Override
    public void generateRachunekPdf(SpRobotCharges robotCharges, List<SpUserApp> usersList, SpUserApp userCharges, boolean ryczalt, HttpServletResponse response) {
        try {
            SpRobot tempRobot = robotCharges.getRobot();
            SpStation tempStation = robotCharges.getRobot().getStation();

            OutputStream o = response.getOutputStream();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/pdf");//todo \/ nazwa pliku: idStacja_idRobot_Rok_Miesiac.pdf
            response.setHeader("Content-Disposition", "inline; filename=" +tempRobot.getStation().getId()+"_"+tempRobot.getId()+"_"+robotCharges.getData().getYear()+"_"+robotCharges.getData().getMonth() + ".pdf");
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
            //todo table1.addCell("Ul. "+tempStation.getStreet()+" nr. "+tempStation.getStationNumber()+"/"+tempRobot.getRobotNumber()+"\r\n"
            //        +tempStation.getPostalCode()+", "+tempStation.getCity());

            table1.addCell("Period");
            table1.addCell(robotCharges.getData().getYear()+" "+robotCharges.getData().getMonth());

            pdf.add(table1);



            PdfPTable table2 = new PdfPTable(4);

            double suma = 0;
            DecimalFormat df = new DecimalFormat("#.00");
/*
            table2.addCell("Fundusz Remontowy");
            table2.addCell(String.valueOf(robotCharges.getFunduszRemontowy()));
            table2.addCell(String.valueOf(robotCharges.getFunduszRemontowy_stawka()));
            table2.addCell(String.valueOf(df.format(robotCharges.getFunduszRemontowy_stawka()
                    *robotCharges.getFunduszRemontowy())));
            suma+=robotCharges.getFunduszRemontowy_stawka()
                    *robotCharges.getFunduszRemontowy();

            table2.addCell("Gaz");
            table2.addCell(String.valueOf(robotCharges.getGaz()));
            table2.addCell(String.valueOf(robotCharges.getGaz_stawka()));
            table2.addCell(String.valueOf(df.format(robotCharges.getGaz_stawka()
                    *robotCharges.getGaz())));
            suma+=robotCharges.getGaz_stawka()
                    *robotCharges.getGaz();

            table2.addCell("Ogrzewanie");
            table2.addCell(String.valueOf(robotCharges.getOgrzewanie()));
            table2.addCell(String.valueOf(robotCharges.getOgrzewanie_stawka()));
            table2.addCell(String.valueOf(df.format(robotCharges.getOgrzewanie_stawka()
                    *robotCharges.getOgrzewanie())));
            suma+=robotCharges.getOgrzewanie_stawka()
                    *robotCharges.getOgrzewanie();

            table2.addCell("Prad");
            table2.addCell(String.valueOf(robotCharges.getPrad()));
            table2.addCell(String.valueOf(robotCharges.getPrad_stawka()));
            table2.addCell(String.valueOf(df.format(robotCharges.getPrad_stawka()
                    *robotCharges.getPrad())));
            suma+=robotCharges.getPrad_stawka()
                    *robotCharges.getPrad();

            table2.addCell("Scieki");
            table2.addCell(String.valueOf(robotCharges.getScieki()));
            table2.addCell(String.valueOf(robotCharges.getScieki_stawka()));
            table2.addCell(String.valueOf(df.format(robotCharges.getScieki_stawka()
                    *robotCharges.getScieki())));
            suma+=robotCharges.getScieki_stawka()
                    *robotCharges.getScieki();

            table2.addCell("Ciepla woda");
            table2.addCell(String.valueOf(robotCharges.getWoda_ciepla()));
            table2.addCell(String.valueOf(robotCharges.getWoda_ciepla_stawka()));
            table2.addCell(String.valueOf(df.format(robotCharges.getWoda_ciepla_stawka()
                    *robotCharges.getWoda_ciepla())));
            suma+=robotCharges.getWoda_ciepla_stawka()
                    *robotCharges.getWoda_ciepla();

            table2.addCell("Zimna zimna");
            table2.addCell(String.valueOf(robotCharges.getWoda_zimna()));
            table2.addCell(String.valueOf(robotCharges.getWoda_zimna_stawka()));
            table2.addCell(String.valueOf(df.format(robotCharges.getWoda_zimna_stawka()
                    *robotCharges.getWoda_zimna())));
            suma+=robotCharges.getWoda_zimna_stawka()
                    *robotCharges.getWoda_zimna();*/

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
            table2.addCell("Robprogs");



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