package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import model.PlasmaService;

import java.io.File;
import java.io.IOException;

@MultipartConfig
public class ProcessServlet extends BaseServlet {
    private final PlasmaService model;

    public ProcessServlet(PlasmaService model)
    {
        this.model=model;
    }
    public static class ResultObject
    {
        private final long generatedResult;
        private final String errorMessage;
        public ResultObject(long generatedResult)
        {

            this.generatedResult=generatedResult;
            this.errorMessage = null;
        }

        public ResultObject(String errorMessage)
        {

            this.generatedResult=-1;
            this.errorMessage = errorMessage;
        }

        public long getGeneratedResult() {
            return generatedResult;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setHeader("Access-Control-Allow-Origin", "*");

        Part filePart = req.getPart("fileSelection");
        String algorithm = req.getParameter("algorithmsSelection");
        String generator = req.getParameter("generatorsSelection");

        String fileName = filePart.getSubmittedFileName();
        File targetFile = null;
        try {
            String projectPath = System.getProperty("user.dir");

            File rawDir = new File(projectPath + File.separator + "raw");
            if (!rawDir.exists()) {
                rawDir.mkdirs();
            }

            targetFile = new File(rawDir, fileName);
            filePart.write(targetFile.getAbsolutePath());

            long result = model.onProcessFrameClick(algorithm,generator,targetFile.getAbsolutePath());
            System.out.println("result: "+result);

            ProcessServlet.ResultObject resultObject = new ProcessServlet.ResultObject(result);
            sendJson(resp, resultObject);

        } catch (Exception e) {
            ProcessServlet.ResultObject resultObject = new ProcessServlet.ResultObject(e.getMessage());
            sendJson(resp, resultObject);
        }
        finally
        {
            if(targetFile != null && targetFile.exists())
            {
                targetFile.delete();
            }
        }


    }

}