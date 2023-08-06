import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XSDValidator {

   public static void main(String[] args) {

      String XMLPath = "C:\\Users\\mike\\Desktop\\BMW-Work\\quibiq\\testfiles\\Billgate\\";
      String XSDPath = "C:\\Users\\mike\\Desktop\\BMW-Work\\quibiq\\testfiles\\Billgate\\Schemas_Billgate\\";

      String xml = XMLPath.concat("SND_ATLAS_35163_8210269016_bc99b36e-8f1d-4b4a-b322-67d115cf24b7.xml");
      String xsd = XSDPath.concat("BMWBillgate.xsd");

      boolean isValid = validateXMLSchema(xsd, xml);

      if (isValid) {
         System.out.println(xml + " is valid against " + xsd);
      } else {
         System.out.println(xml + " is not valid against " + xsd);
      }

   }

   public static boolean validateXMLSchema(String xsdFilePath, String xmlFilePath) {

   try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(xsdFilePath));
            Validator validator = schema.newValidator();

            Source source = new StreamSource(new File(xmlFilePath));

            ValidationHandler validationHandler = new ValidationHandler();
            validator.setErrorHandler(validationHandler);

            validator.validate(source);

            if (validationHandler.hasErrors()) {
                System.out.println("Validation errors:");
                validationHandler.printErrors();
            } else {
                System.out.println("XML is valid against the XSD.");
            }
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        return true;
    }

}

class ValidationHandler extends org.xml.sax.helpers.DefaultHandler {
    private StringBuilder errorMessage = new StringBuilder();

    @Override
    public void error(org.xml.sax.SAXParseException e) throws org.xml.sax.SAXException {
        errorMessage.append("Error: ").append(e.getMessage()).append("\n");
    }

    public boolean hasErrors() {
        return errorMessage.length() > 0;
    }

    public void printErrors() {
        System.out.println(errorMessage.toString());
    }
}
