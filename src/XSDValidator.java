import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XSDValidator {

   public static void main(String[] args) {

      String XMLPath = "C:\\Users\\qxp5531\\Desktop\\Test\\B1\\";
      String XSDPath = "C:\\Users\\qxp5531\\Desktop\\Test\\B1\\Schemas_Billgate\\";

      String xml = XMLPath.concat("ONB_a6e96ab7-e660-4726-a441-a25871011b3b.xml");
      String xsd = XSDPath.concat("BMWOnboarding.xsd");

      boolean isValid = validateXMLSchema(xsd, xml);

      if (isValid) {
         System.out.println(xml + " is valid against " + xsd);
      } else {
         System.out.println(xml + " is not valid against " + xsd);
      }

   }

   public static boolean validateXMLSchema(String xsdPath, String xmlPath) {
      try {
         SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
         Schema schema = factory.newSchema(new File(xsdPath));
         Validator validator = schema.newValidator();
         validator.validate(new StreamSource(new File(xmlPath)));
      } catch (IOException e) {
         System.out.println("Exception: " + e.getMessage());
         return false;
      } catch (SAXException e1) {
         System.out.println("SAX Exception: " + e1.getMessage());
         return false;
      }

      return true;
   }
}