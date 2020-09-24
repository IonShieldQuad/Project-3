package ionshield.project3.main;

import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
import ionshield.project3.graphics.GraphDisplay;
import ionshield.project3.math.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainWindow {
    private JPanel rootPanel;
    private JTextArea log;
    private JTextField c0Field;
    private JButton calculateButton;
    private GraphDisplay graph;
    private JTextField p1Field;
    private JTextField m0Field;
    private JComboBox modeSel;
    private JTextField t0Field;
    private JTextField deltaField;
    private GraphDisplay graph2;
    private JTextField xStartField;
    private JTextField xEndField;
    
    public static final String TITLE = "Project-3";
    
    private MainWindow() {
        initComponents();
    }
    
    private void initComponents() {
        calculateButton.addActionListener(e -> calculate());
    }
    
    
    
    private void calculate() {
        try {
            log.setText("");
            
            double c0 = Double.parseDouble(c0Field.getText());
            double m0 = Double.parseDouble(m0Field.getText());
            double t0 = Double.parseDouble(t0Field.getText());
            double p1 = Double.parseDouble(p1Field.getText());
            double delta = Double.parseDouble(deltaField.getText());
            
            int steps = 0;
            delta = Math.abs(delta);
            
            PipeReactor reactor = new PipeReactor();
            List<Interpolator> r1 = new ArrayList<>();
            List<Interpolator> r2 = new ArrayList<>();
            switch (modeSel.getSelectedIndex()) {
                case 0:
                    if (p1 < c0) {
                        delta *= -1;
                    }
                    steps = (int)Math.round((p1 - c0) / delta);
                    double currC = c0;
                    for (int i = 0; i <= steps; i++) {
                        
                        reactor.calculate(currC, m0, t0);
                        r1.add(reactor.getResult1());
                        r2.add(reactor.getResult2());
                        
                        currC += delta;
                    }
                    
                    break;
                    
                case 1:
                    if (p1 < m0) {
                        delta *= -1;
                    }
                    steps = (int)Math.round((p1 - m0) / delta);
                    double currM = m0;
                    for (int i = 0; i <= steps; i++) {
        
                        reactor.calculate(c0, currM, t0);
                        r1.add(reactor.getResult1());
                        r2.add(reactor.getResult2());
        
                        currM += delta;
                    }
                    
                    break;
                    
                default:
                    if (p1 < t0) {
                        delta *= -1;
                    }
                    steps = (int)Math.round((p1 - t0) / delta);
                    double currT = t0;
                    for (int i = 0; i <= steps; i++) {
        
                        reactor.calculate(c0, m0, currT);
                        r1.add(reactor.getResult1());
                        r2.add(reactor.getResult2());
        
                        currT += delta;
                    }
                    
                    break;
            }
            updateGraph(r1);
            updateGraph2(r2);
        }
        catch (NumberFormatException e) {
            log.append("\nInvalid input format");
        }
    }
    
    private void updateGraph(List<Interpolator> l) {
        try {
            if (l == null || l.isEmpty()) {
                graph.setInterpolators(new ArrayList<>());
                graph.repaint();
                return;
            }
            
            double xs = Double.parseDouble(xStartField.getText());
            double xe = Double.parseDouble(xEndField.getText());
            
            graph.setMinX(Math.max(xs, l.stream().mapToDouble(Interpolator::lower).min().orElse(0)));
            graph.setMaxX(Math.min(xe, l.stream().mapToDouble(Interpolator::upper).max().orElse(0)));
            graph.setMinY(l.stream().mapToDouble(Interpolator::lowerVal).min().orElse(0));
            graph.setMaxY(l.stream().mapToDouble(Interpolator::upperVal).max().orElse(0));
    
            graph.setInterpolators(l);
    
            graph.repaint();
        }
        catch (NumberFormatException e) {
            log.append("\nInvalid input format");
        }
    }
    
    private  void updateGraph2(List<Interpolator> l) {
        try {
            if (l == null || l.isEmpty()) {
                graph2.setInterpolators(new ArrayList<>());
                graph2.repaint();
                return;
            }
    
            double xs = Double.parseDouble(xStartField.getText());
            double xe = Double.parseDouble(xEndField.getText());
    
            graph2.setMinX(Math.max(xs, l.stream().mapToDouble(Interpolator::lower).min().orElse(0)));
            graph2.setMaxX(Math.min(xe, l.stream().mapToDouble(Interpolator::upper).max().orElse(0)));
            graph2.setMinY(l.stream().mapToDouble(Interpolator::lowerVal).min().orElse(0));
            graph2.setMaxY(l.stream().mapToDouble(Interpolator::upperVal).max().orElse(0));
        
            graph2.setInterpolators(l);
        
            graph2.repaint();
        }
        catch (NumberFormatException e) {
            log.append("\nInvalid input format");
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame(TITLE);
        MainWindow gui = new MainWindow();
        frame.setContentPane(gui.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
