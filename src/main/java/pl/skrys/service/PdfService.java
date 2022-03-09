package pl.skrys.service;

import pl.skrys.app.SpRobotStatus;
import pl.skrys.app.SpUserApp;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PdfService {
    void generateRachunekPdf(SpRobotStatus robotStatus, List<SpUserApp> usersList, SpUserApp userStatus, boolean ryczalt,  HttpServletResponse response);
    public void generatePdf(SpUserApp userApp, HttpServletResponse response);
}
