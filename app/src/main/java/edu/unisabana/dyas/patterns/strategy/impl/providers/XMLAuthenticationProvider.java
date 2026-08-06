package edu.unisabana.dyas.patterns.strategy.impl.providers;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import edu.unisabana.dyas.patterns.strategy.impl.IAuthenticationStrategy;
import edu.unisabana.dyas.patterns.strategy.impl.Principal;

/**
 @author cesarvefe

 */
public class XMLAuthenticationProvider implements IAuthenticationStrategy {

    private static final String rolXPath = "Users/User[@userName=$userName and @password=$password]/@rol";

    @Override
    public Principal authenticate(String userName, String passwrd) {
        InputStream file = null;
        try {
            file = getClass().getResourceAsStream("/META-INF/UserFile.xml");
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(file);
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setXPathVariableResolver(variableName -> {
                switch (variableName.getLocalPart()) {
                    case "userName": return userName;
                    case "password": return passwrd;
                    default: return null;
                }
            });

            String rol = xPath.compile(rolXPath).evaluate(xmlDocument);
            if(rol!=null && !rol.isEmpty()){
                return new Principal(userName, rol);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try { file.close(); } catch (IOException ex) {}
        }
    }
}