import javax.swing.*;

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.*;

public class XSDValidatorFrontEnd {

    public static void main(String args[]) throws Exception {
        /* JOptionPane Java user input example */
        myScreen f = new myScreen();

    }
}

class myScreen extends JFrame implements ActionListener {

    // Components of the Form
    private static Container c;
    private JLabel title;
    private JLabel xsdfile;
    private JTextField txsdfile;
    private JLabel xmlfile;
    private JTextField txmlfile;
    private JCheckBox tlogfile;
    private JButton xsdf;
    private JButton xmlf;
    private JButton sub;
    private JButton reset;
    private JLabel res;
    public JTextArea resadd;
    private static JFileChooser fc;
    private String logFile;

    public myScreen() {

        setTitle("XSD Validator");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        title = new JLabel("XSD Validator");
        title.setFont(new Font("Courier", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(300, 30);
        c.add(title);

        xsdfile = new JLabel("XSD File Name");
        xsdfile.setFont(new Font("Arial", Font.PLAIN, 20));
        xsdfile.setSize(250, 20);
        xsdfile.setLocation(100, 100);
        c.add(xsdfile);

        txsdfile = new JTextField();
        txsdfile.setFont(new Font("Arial", Font.PLAIN, 15));
        txsdfile.setSize(400, 20);
        txsdfile.setLocation(300, 100);
        c.add(txsdfile);

        xmlfile = new JLabel("XML File Name");
        xmlfile.setFont(new Font("Arial", Font.PLAIN, 20));
        xmlfile.setSize(250, 20);
        xmlfile.setLocation(100, 150);
        c.add(xmlfile);

        txmlfile = new JTextField();
        txmlfile.setFont(new Font("Arial", Font.PLAIN, 15));
        txmlfile.setSize(400, 20);
        txmlfile.setLocation(300, 150);
        c.add(txmlfile);

        tlogfile = new JCheckBox("Log OutPut to logfile");
        tlogfile.setFont(new Font("Arial", Font.PLAIN, 15));
        tlogfile.setSize(250, 20);
        tlogfile.setLocation(150, 400);
        c.add(tlogfile);

        sub = new JButton("Submit");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(100, 20);
        sub.setLocation(150, 450);
        sub.addActionListener(this);
        c.add(sub);

        reset = new JButton("Reset");
        reset.setFont(new Font("Arial", Font.PLAIN, 15));
        reset.setSize(100, 20);
        reset.setLocation(270, 450);
        reset.addActionListener(this);
        c.add(reset);

        xsdf = new JButton("Files");
        xsdf.setSize(75, 20);
        xsdf.setLocation(725, 100);
        xsdf.addActionListener(this);
        c.add(xsdf);

        xmlf = new JButton("Files");
        xmlf.setSize(75, 20);
        xmlf.setLocation(725, 150);
        xmlf.addActionListener(this);
        c.add(xmlf);

        res = new JLabel("Output");
        res.setFont(new Font("Arial", Font.PLAIN, 20));
        res.setSize(100, 20);
        res.setLocation(100, 200);
        c.add(res);

        resadd = new JTextArea();
        resadd.setFont(new Font("Arial", Font.PLAIN, 15));
        resadd.setSize(500, 200);
        resadd.setLocation(200, 200);
        resadd.setLineWrap(true);
        c.add(resadd);    

        setVisible(true);

    }

    // method actionPerformed()
    // to get the action performed
    // by the user and act accordingly
    public void actionPerformed(ActionEvent e)  {
        boolean logdata = false;
        if (tlogfile.isSelected()) {
            logdata=true;
            File myFile = new File(txmlfile.getText());
            Path myPath = myFile.toPath();
            //Path subPath = myPath.subpath(myPath.getNameCount() -3, myPath.getNameCount() -1);
            Path subPath = myPath.getParent();
            //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd-HH.mm.ss");
            String timestamp = df.format(new Date());
            logFile = subPath.toString() + "\\logfile" + timestamp + ".log" ;
            
            createlogFile(logFile);
            String textMsg = "XSD Validator";
            writelogFile(textMsg, logFile);
        }

        if (e.getSource() == sub) {
            String xsd = txsdfile.getText();
            String xml = txmlfile.getText();

            String response = validateXMLSchema(xsd, xml);

            if (logdata) {
                    String textMsg = "XSD File: " + xsd ;
                    writelogFile(textMsg, logFile);
                    textMsg = "XML File: " + xml ;
                    writelogFile(textMsg, logFile);
                }

            if (response.contains("PASS")) {
                if (logdata) {
                    String textMsg = "Status : is valid" + xsd ;
                    writelogFile(textMsg, logFile);
                }
                resadd.setText(xml + " is valid against " + xsd);
                resadd.setEditable(false);
            } else {
                if (logdata) writelogFile(response, logFile);
                resadd.setText(response);
                resadd.setEditable(false);
            }
        }
        else if (e.getSource() == xsdf) {
            txsdfile.setText(getFile("XSD"));
        }
        else if (e.getSource() == xmlf) {
            txmlfile.setText(getFile("XML"));
        }

        else if (e.getSource() == reset) {
            String def = "";
            txsdfile.setText(def);
            txmlfile.setText(def);
            tlogfile.setSelected(false);
            resadd.setText(def);
        }
    }

    public static String validateXMLSchema(String xsdPath, String xmlPath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException e) {
            return e.getMessage();
        } catch (SAXException e1) {
            return e1.getMessage();
        }
        return "PASS";
    }

    public static String getFile(String fileType) {
        String filePath="";
        fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fc.showOpenDialog(c);
        if (result == JFileChooser.APPROVE_OPTION) {
            if (fileType.contains("XSD")) {
                File selectedFile = fc.getSelectedFile();
                filePath = selectedFile.getAbsolutePath().toString();
            } else if (fileType.contains("XML")) {
                File selectedFile = fc.getSelectedFile();
                filePath = selectedFile.getAbsolutePath().toString();
            }
        }
        return filePath;
    }

    private void createlogFile(String logFilename) {
        try {
            File myObj = new File(logFilename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }    
    }

    private void writelogFile(String textMsg, String logFile) {
        BufferedWriter out = null;
        try {
            FileWriter myWriter = new FileWriter(logFile,true);
            out = new BufferedWriter(myWriter);
            out.write(textMsg);
            out.newLine();
            out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    
    
    }
}
