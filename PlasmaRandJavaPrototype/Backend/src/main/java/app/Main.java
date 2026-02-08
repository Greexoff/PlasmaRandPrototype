package app;

import controller.*;
import model.PlasmaService;
import model.repository.ResultRepository;
import model.repository.SQLiteRepository;
import org.apache.catalina.LifecycleException;
import view.MainFrame;
import view.ViewInterface;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import java.io.File;
public class Main {

    private static void startWebApp(PlasmaService model) throws LifecycleException {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        String docBase = new File(".").getAbsolutePath();
        Context ctx = tomcat.addContext("", docBase);

        OptionsServlet optionsServlet = new OptionsServlet(model);
        Tomcat.addServlet(ctx, "optionsServlet", optionsServlet);
        ctx.addServletMappingDecoded("/api/options", "optionsServlet");


        ProcessServlet processServlet = new ProcessServlet(model);
        var wrapper = Tomcat.addServlet(ctx, "processServlet", processServlet);

        wrapper.setMultipartConfigElement(new jakarta.servlet.MultipartConfigElement(
                System.getProperty("java.io.tmpdir"),
                1024 * 1024 * 500,
                1024 * 1024 * 1000,
                1024 * 1024 * 500
        ));
        ctx.addServletMappingDecoded("/api/process", "processServlet");
        tomcat.start();
        tomcat.getServer().await();
    }
    private static void startGUIApp(PlasmaService model)
    {
        try{
            ViewInterface view = new MainFrame();
            AppController controller = new AppController(model, view);
            controller.start();
        }
        catch (Exception e)
        {
            System.err.println("Error: Couldn't start GUI app");
        }
    }

    public static void main(String[] args) throws LifecycleException {
        ResultRepository repo = new SQLiteRepository();

        PlasmaService model = new PlasmaService(repo);

        if(args.length>0 && args[0].equals("-web"))
        {
            startWebApp(model);
        }
        else
        {
            startGUIApp(model);
        }
    }
}