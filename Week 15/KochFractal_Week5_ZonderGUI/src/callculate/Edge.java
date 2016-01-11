/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package callculate;

import java.io.Serializable;
import javafx.scene.paint.Color;

/**
 *
 * @author Peter Boots
 */
public class Edge implements Serializable {

    public double X1, Y1, X2, Y2;
    public double hue, sat, bri;

    public Edge(double X1, double Y1, double X2, double Y2, Color color) {
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;

        this.hue = color.getHue();
        this.sat = color.getSaturation();
        this.bri = color.getBrightness();
    }

    public Edge getWhite() {
        return new Edge(X1, Y1, X2, Y2, Color.WHITE);
    }

    public Color getColor() {
        Color returner = Color.hsb(hue, sat, bri);
        return returner;
    }

    @Override
    public String toString() {
        StringBuilder returner = new StringBuilder();
        returner.append(X1);
        returner.append(", ");
        returner.append(Y1);
        returner.append(", ");
        returner.append(X2);
        returner.append(", ");
        returner.append(Y2);
        returner.append(", ");
        returner.append(hue);
        returner.append(", ");
        returner.append(sat);
        returner.append(", ");
        returner.append(bri);
        returner.append(";");

        return returner.toString();
    }
}
