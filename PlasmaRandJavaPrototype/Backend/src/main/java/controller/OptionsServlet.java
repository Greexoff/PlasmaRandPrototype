package controller;

import model.PlasmaService;
import jakarta.servlet.http.*;

import java.io.IOException;

public class OptionsServlet extends BaseServlet {
    private final PlasmaService model;

    public OptionsServlet(PlasmaService model)
    {
        this.model=model;
    }

    public static class SelectObject
    {
        private final String[] algorithms;
        private final String[] generators;

        public SelectObject(String[] algorithms, String[] generators)
        {
            this.algorithms=algorithms;
            this.generators=generators;
        }
        public String[] getAlgorithms()
        {
            return algorithms;
        }
        public String[] getGenerators()
        {
            return generators;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SelectObject selectObject = new SelectObject(model.getAvailableAlgorithms(),  model.getAvailableGenerators());
        sendJson(resp, selectObject);
    }
}