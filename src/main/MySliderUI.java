package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class MySliderUI extends BasicSliderUI {
	
    private static float[] fracs = {0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f};
    private LinearGradientPaint p;

	public MySliderUI(JSlider slider) {
		super(slider);
	}
	
	public void setColorRange(float[] ranges){
		fracs = ranges;
	}
	
	@Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle t = trackRect;
        Point2D start = new Point2D.Float(t.x, t.y);
        Point2D end = new Point2D.Float(t.width, t.height);
        //Color[] colors = {Color.magenta, Color.blue, Color.cyan,
        //    Color.green, Color.yellow, Color.red};
        
        Color[] colors = {	new Color(255, 0, 0),
        		new Color(204, 51, 0), 
        		new Color(153, 102, 0),
        		new Color(102, 153, 0),
        		new Color(51, 204, 0), 
        		new Color(0, 255, 0),};
//        Color[] colors = {	new Color(178, 34, 34),
//        		new Color(154, 63, 50), 
//        		new Color(130, 92, 66),
//        		new Color(107, 121, 81),
//        		new Color(83, 150, 97), 
//        		new Color(60, 179, 113)};
        
        p = new LinearGradientPaint(start, end, fracs, colors);
        g2d.setPaint(p);
        g2d.fillRect(t.x, t.y, t.width, t.height);
    }

    @Override
    public void paintThumb(Graphics g) {
    	
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle t = thumbRect;
        
        int[] x = new int[3];
        int[] y = new int[3];
        int n = 3; // count of points
        int tw2 = t.width / 2;
        
        x[0] = t.x;
        y[0] = t.y-t.height*2;
        x[1] = t.x+tw2;
        y[1] = t.height+t.height*2-12;
        x[2] = t.x+t.width-1;
        y[2] = t.y-t.height*2;
        Polygon fThumb = new Polygon(x,y,n);
        g2d.setPaint(Color.blue);
        g2d.fill(fThumb);
        
    }

}
