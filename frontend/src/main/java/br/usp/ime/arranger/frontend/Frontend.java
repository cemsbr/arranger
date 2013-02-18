package br.usp.ime.arranger.frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class Frontend {

    public static final int MB = 1024 * 1024;

    public List<String> getMusicians() {
        final List<String> wsdls = new ArrayList<>(7);
        wsdls.add("http://127.0.0.1:8080/arranger1/endpoint?wsdl");
        wsdls.add("http://127.0.0.1:8080/arranger2/endpoint?wsdl");
        wsdls.add("http://127.0.0.1:8080/arranger3/endpoint?wsdl");
        wsdls.add("http://127.0.0.1:8080/arranger4/endpoint?wsdl");
        wsdls.add("http://127.0.0.1:8080/arranger5/endpoint?wsdl");
        wsdls.add("http://127.0.0.1:8080/arranger6/endpoint?wsdl");
        wsdls.add("http://127.0.0.1:8080/arranger7/endpoint?wsdl");
        return wsdls;
    }

    public void display(
            DirectedGraph<WebService, MessageExchange> graph) {
        Layout<WebService, MessageExchange> layout = new CircleLayout<>(graph);
        // sets the initial size of the layout space
        layout.setSize(new Dimension(300, 300));
        BasicVisualizationServer<WebService, MessageExchange> vv = new BasicVisualizationServer<WebService, MessageExchange>(
                layout);
        // Sets the viewing area size
        vv.setPreferredSize(new Dimension(350, 350));

        // Setup up a new vertex to paint transformer...
        Transformer<WebService, Paint> vertexPaint = new Transformer<WebService, Paint>() {
            public Paint transform(final WebService ws) {
                return Color.GREEN;
            }
        };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);

        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }
}