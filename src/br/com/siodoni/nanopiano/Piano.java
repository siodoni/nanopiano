package br.com.siodoni.nanopiano;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author Flavio Augusto Siodoni Ximenes
 */
public class Piano extends MIDlet {

    private Canvas canvas;

    public Piano() {
        canvas = new Tela(this);
    }

    public void startApp() {
        Display.getDisplay(this).setCurrent(canvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        notifyDestroyed();
    }
}
