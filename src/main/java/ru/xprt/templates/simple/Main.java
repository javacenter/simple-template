package ru.xprt.templates.simple;

import javax.servlet.ServletException;

import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import ru.xprt.templates.simple.servlet.SimpleServlet;

public class Main {
    @SuppressWarnings("resource")
    public static void main(String... args) throws ServletException {
        DeploymentInfo deploymentInfo = Servlets.deployment();
        deploymentInfo.setClassLoader(Main.class.getClassLoader());
        deploymentInfo.setContextPath("/");
        deploymentInfo.setDeploymentName("test deployment");
        deploymentInfo.setResourceManager(new ClassPathResourceManager(Main.class.getClassLoader(), "static"));
        deploymentInfo.addWelcomePage("index.html");

        //Servlets
        deploymentInfo.addServlet(Servlets.servlet(SimpleServlet.class).addMapping("/simple"));

        Builder serverBuilder = Undertow.builder();
        serverBuilder.addHttpListener(8080, "0.0.0.0");

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
        manager.deploy();

        HttpHandler handler = manager.start();

        Undertow server = serverBuilder.setHandler(handler).build();

        server.start();
    }
}
