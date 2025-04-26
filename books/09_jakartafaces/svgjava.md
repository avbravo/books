To generate SVG files in Java, several libraries can be utilized. These libraries provide functionalities to create and manipulate SVG elements programmatically. Here's a summary of some common approaches:
Apache Batik
Apache Batik is a powerful toolkit for handling SVG graphics in Java. It offers modules for parsing, rendering, and generating SVG documents. To generate SVG using Batik, you can use the SVGGraphics2D class, which extends Graphics2D and allows drawing operations to be translated into SVG elements.


import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class SVGGenerator {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document document = dbf.newDocumentBuilder().newDocument();

        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // Draw a circle
        svgGenerator.drawOval(10, 10, 80, 80);

        Element root = document.createElementNS("http://www.w3.org/2000/svg", "svg");
        document.appendChild(root);
        root.setAttribute("width", "100");
        root.setAttribute("height", "100");
        root.appendChild(svgGenerator.getRoot());

        try (Writer out = new OutputStreamWriter(new FileOutputStream("circle.svg"), "UTF-8")) {
            svgGenerator.stream(root, out, true, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


JFreeSVG
JFreeSVG is a lightweight and fast library specifically designed for generating SVG graphics. It also uses the Graphics2D API, making it easy to integrate with existing Java 2D code.

import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;

public class JFreeSVGExample {
    public static void main(String[] args) {
        SVGGraphics2D g2 = new SVGGraphics2D(100, 100);
        Ellipse2D.Double circle = new Ellipse2D.Double(10, 10, 80, 80);
        g2.draw(circle);
        try {
            SVGUtils.writeToSVG(new File("jfree_circle.svg"), g2.getSVGElement());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
