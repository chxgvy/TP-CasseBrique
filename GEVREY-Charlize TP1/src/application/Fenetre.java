package application;

import models.Balle;
import models.Barre;
import models.Brique; /* on importe la classe brique */
import models.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Fenetre extends Canvas implements KeyListener {

    public static final int LARGEUR = 500;
    public static final int HAUTEUR = 700;

    protected boolean toucheEspace = false;
    protected boolean toucheGauche = false;
    protected boolean toucheDroite = false;

    ArrayList<Balle> listeBalles = new ArrayList<>();
    ArrayList<Sprite> listeSprites = new ArrayList<>();
    ArrayList<Brique> listeBriques = new ArrayList<>();
    Barre barre;

    Fenetre()  throws InterruptedException {

        JFrame fenetre = new JFrame();

        this.setSize(LARGEUR, HAUTEUR);
        this.setBounds(0, 0, LARGEUR, HAUTEUR);
        this.setIgnoreRepaint(true);
        this.setFocusable(false);

        fenetre.pack();
        fenetre.setSize(LARGEUR, HAUTEUR );
        fenetre.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fenetre.setResizable(false);
        fenetre.requestFocus();
        fenetre.addKeyListener(this);


        Container panneau = fenetre.getContentPane();
        panneau.add(this);

        fenetre.setVisible(true);
        this.createBufferStrategy(2);

        this.demarrer();
    }

    public void demarrer() throws InterruptedException {

        barre = new Barre();
        listeSprites.add(barre);

        Balle balle = new Balle(100, 200 , Color.pink, 30);

        listeBalles.add(balle);
        listeSprites.add(balle);

        //créer une brique
        int brickWidth = 50;
        int brickHeight = 20;
        int rows = 5;
        int columns = 10;
        int padding = 5;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int brickX = col * (brickWidth + padding);
                int brickY = row * (brickHeight + padding);
                Brique brique = new Brique(brickX, brickY, Color.red, brickWidth, brickHeight);
                listeBriques.add(brique);
                listeSprites.add(brique);
            }
        }

        while(true) {

            Graphics2D dessin = (Graphics2D) this.getBufferStrategy().getDrawGraphics();
            dessin.setColor(Color.white);
            dessin.fillRect(0,0,LARGEUR,HAUTEUR);

            //----- app -----
            for(Balle b : listeBalles) {
                b.deplacement(barre, listeBriques);
            }

            List<Sprite> spritesToDraw = new ArrayList<>(listeSprites);
            for (Sprite s : spritesToDraw) {
                if (s instanceof Brique && !listeBriques.contains(s)) {
                    continue; // Skip drawing the brick if it's removed
                }
                s.dessiner(dessin);
            }

            if(toucheEspace) {
                listeBalles.add( new Balle(200, 400 , Color.ORANGE, 50));
            }

            if(toucheGauche){
                barre.deplacerGauche();
            }

            if(toucheDroite){
                barre.deplacerDroite();
            }

            //---------------

            dessin.dispose();
            this.getBufferStrategy().show();
            Thread.sleep(1000 / 60);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            toucheEspace = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            toucheGauche = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            toucheDroite = true;
        }
    }

    @Override
    public void  keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            toucheEspace = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            toucheGauche = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            toucheDroite = false;
        }
    }
}
