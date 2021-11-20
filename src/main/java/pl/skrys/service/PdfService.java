package pl.skrys.service;

import pl.skrys.app.SpFlatCharges;
import pl.skrys.app.SpUserApp;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PdfService {
    void generateRachunekPdf(SpFlatCharges flatCharges, List<SpUserApp> usersList, SpUserApp userCharges, boolean ryczalt,  HttpServletResponse response);
    public void generatePdf(SpUserApp userApp, HttpServletResponse response);
}
