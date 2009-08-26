package br.com.siodoni.nanopiano;

import javax.microedition.lcdui.*;

public class Tela extends Canvas implements CommandListener, ItemStateListener {

    private Piano piano;
    private Command cmdSair, cmdAjuda, cmdConfig, cmdVoltar, cmdSobre;
    private Alert help;
    private Gauge volume;
    private ChoiceGroup mostraNmNota, mostraNotaTocada, listaCores;
    private Form config;
    private Image[] icone;
    private TextBox sobre;
    private int largura, altura, teclaPressionada;
    private int posTBranca[] = new int[7], posTPreta[] = new int[5];
    private int red = 255, green = 215, blue = 0;
    private String[] opcao = {"Sim", "Não"};
    private String[] cores = {"Amarelo", "Azul", "Vermelho", "Verde"};
    private String corSelecionada;

    public Tela(Piano midlet) {
        this.piano = midlet;

        cmdSair = new Command("Sair", Command.EXIT, 0);
        cmdVoltar = new Command("Voltar", Command.BACK, 0);
        cmdAjuda = new Command("Ajuda", Command.HELP, 0);
        cmdConfig = new Command("Configuração", Command.ITEM, 0);
        cmdSobre = new Command("Sobre", Command.ITEM, 0);

        carregaIcone();

        volume = new Gauge("Selecione o volume", true, 10, 1);
        volume.setValue(10);
        mostraNmNota = new ChoiceGroup("Deseja visualizar o nome da nota?", ChoiceGroup.EXCLUSIVE, opcao, null);
        mostraNotaTocada = new ChoiceGroup("Deseja visualizar a tecla tocada?", ChoiceGroup.EXCLUSIVE, opcao, null);
        listaCores = new ChoiceGroup("Escolha a cor da tecla tocada", ChoiceGroup.EXCLUSIVE, cores, icone);

        largura = getWidth();
        altura = getHeight();

        addCommand(cmdSair);
        addCommand(cmdConfig);
        addCommand(cmdAjuda);
        addCommand(cmdSobre);
        setCommandListener(this);

        montaConfiguracao();
        montaHelp();
        montaSobre();
    }

    public void paint(Graphics g) {

        int tamTecla = (getHeight() / 7) - 2;
        int teclaAcum = 2;
        int larguraTPreta = (largura / 2) + (largura / 8);
        int angTBranca = 5, marca = 0, largMarcaB = 0, largMarcaP = 0;

        //Fundo preto
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, largura, altura);

        //Teclas brancas
        g.setColor(255, 255, 255);
        for (int i = 0; i < posTBranca.length; i++) {
            g.fillRoundRect(0, teclaAcum, largura, tamTecla, angTBranca, angTBranca);
            teclaAcum += tamTecla + 2;
            posTBranca[i] = teclaAcum;
        }

        //Teclas pretas
        g.setColor(0, 0, 0);
        g.fillRect(0, posTBranca[0] / 2, larguraTPreta, tamTecla);
        posTPreta[0] = posTBranca[0] / 2;
        g.fillRect(0, posTBranca[2] / 2, larguraTPreta, tamTecla);
        posTPreta[1] = posTBranca[2] / 2;
        g.fillRect(0, posTBranca[4] / 2, larguraTPreta, tamTecla);
        posTPreta[2] = posTBranca[4] / 2;
        g.fillRect(0, posTBranca[4] / 2 + posTBranca[1], larguraTPreta, tamTecla);
        posTPreta[3] = posTBranca[4] / 2 + posTBranca[1];
        g.fillRect(0, posTBranca[6] / 2 + posTBranca[1], larguraTPreta, tamTecla);
        posTPreta[4] = posTBranca[6] / 2 + posTBranca[1];

        //Parte marrom
        g.setColor(139, 69, 0);
        g.fillRect(0, 0, largura / 4, altura);
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, 2, altura);
        g.fillRect(0, 0, largura, 2);
        g.fillRect(largura / 4, 0, 2, altura);
        g.fillRect(0, altura - 2, largura, 2);

        //Mostra qual a nota tocada
        marca = (posTBranca[6] - posTBranca[5]) - 4;
        largMarcaB = largura - marca;
        largMarcaP = larguraTPreta - marca;
        g.setColor(red, green, blue);

        if (Escala.mostraTecla.equals("Sim")) {
            if (teclaPressionada == KEY_NUM1) {
                g.fillArc(largMarcaB, posTBranca[5], marca, marca, 0, 360);
            } else if (teclaPressionada == KEY_NUM2) {
                g.fillArc(largMarcaP, posTPreta[4], marca, marca, 0, 360);
            } else if (teclaPressionada == KEY_NUM3) {
                g.fillArc(largMarcaB, posTBranca[4], marca, marca, 0, 360);
            } else if (teclaPressionada == KEY_NUM4) {
                g.fillArc(largMarcaP, posTPreta[3], marca, marca, 0, 360);
            } else if (teclaPressionada == KEY_NUM5) {
                g.fillArc(largMarcaB, posTBranca[3], marca, marca, 0, 360);
            } else if (teclaPressionada == KEY_NUM6) {
                g.fillArc(largMarcaB, posTBranca[2], marca, marca, 0, 360);
            } else if (teclaPressionada == KEY_NUM7) {
                g.fillArc(largMarcaP, posTPreta[2], marca, marca, 0, 360);
            } else if (teclaPressionada == KEY_NUM8) {
                g.fillArc(largMarcaB, posTBranca[1], marca, marca, 0, 360);
            } else if (teclaPressionada == KEY_NUM9) {
                g.fillArc(largMarcaP, posTPreta[1], marca, marca, 0, 360);
            } else if (teclaPressionada == KEY_STAR) {
                g.fillArc(largMarcaB, posTBranca[0], marca, marca, 0, 360);
            } else if (teclaPressionada == KEY_NUM0) {
                g.fillArc(largMarcaP, posTPreta[0], marca, marca, 0, 360);
            } else if (teclaPressionada == KEY_POUND) {
                g.fillArc(largMarcaB, 2, marca, marca, 0, 360);
            }
        }

        if (Escala.mostraNota.equals("Sim")) {
            g.setColor(255, 255, 255);
            g.drawString(Escala.nomeNota.toString(), 2, 2, Graphics.TOP | Graphics.LEFT);
        }
    }

    protected void keyPressed(int keyCode) {
        if (keyCode == -1) { //Aumenta a oitava das notas
            if (Escala.oitava >= Escala.MAIOR_OITAVA) {
                Escala.oitava = Escala.MAIOR_OITAVA;
            } else {
                Escala.oitava += 12;
            }
        } else if (keyCode == -2) { //Diminui a oitava das notas
            if (Escala.oitava <= Escala.MENOR_OITAVA) {
                Escala.oitava = Escala.MENOR_OITAVA;
            } else {
                Escala.oitava -= 12;
            }
        }
        notaTocada(keyCode);
        teclaPressionada = keyCode;
        repaint();
    }

    private void notaTocada(int teclaPressionada) {

        if (teclaPressionada == KEY_NUM1) {
            Escala.nomeNota = new StringBuffer("C");
            Escala.tocaNota(Escala.cNat);
        } else if (teclaPressionada == KEY_NUM2) {
            Escala.nomeNota = new StringBuffer("C#");
            Escala.tocaNota(Escala.cSus);
        } else if (teclaPressionada == KEY_NUM3) {
            Escala.nomeNota = new StringBuffer("D");
            Escala.tocaNota(Escala.dNat);
        } else if (teclaPressionada == KEY_NUM4) {
            Escala.nomeNota = new StringBuffer("D#");
            Escala.tocaNota(Escala.dSus);
        } else if (teclaPressionada == KEY_NUM5) {
            Escala.nomeNota = new StringBuffer("E");
            Escala.tocaNota(Escala.eNat);
        } else if (teclaPressionada == KEY_NUM6) {
            Escala.nomeNota = new StringBuffer("F");
            Escala.tocaNota(Escala.fNat);
        } else if (teclaPressionada == KEY_NUM7) {
            Escala.nomeNota = new StringBuffer("F#");
            Escala.tocaNota(Escala.fSus);
        } else if (teclaPressionada == KEY_NUM8) {
            Escala.nomeNota = new StringBuffer("G");
            Escala.tocaNota(Escala.gNat);
        } else if (teclaPressionada == KEY_NUM9) {
            Escala.nomeNota = new StringBuffer("G#");
            Escala.tocaNota(Escala.gSus);
        } else if (teclaPressionada == KEY_STAR) {
            Escala.nomeNota = new StringBuffer("A");
            Escala.tocaNota(Escala.aNat);
        } else if (teclaPressionada == KEY_NUM0) {
            Escala.nomeNota = new StringBuffer("A#");
            Escala.tocaNota(Escala.aSus);
        } else if (teclaPressionada == KEY_POUND) {
            Escala.nomeNota = new StringBuffer("B");
            Escala.tocaNota(Escala.bNat);
        }
    }

    public void montaHelp() {
        try {
            help = new Alert(
                    "Ajuda",
                    "Para tocar o NanoPiano utilize o teclado numerico, " +
                    "as teclas de navegação \"para cima\" e \"para baixo\" " +
                    "alteram as oitavas das notas.",
                    null,
                    null);
            help.addCommand(cmdVoltar);
            help.setCommandListener(this);
            help.setTimeout(10000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void montaConfiguracao() {
        config = new Form("Configuração");

        config.append(volume);
        config.append(mostraNmNota);
        config.append(mostraNotaTocada);
        config.append(listaCores);

        config.addCommand(cmdVoltar);
        config.setCommandListener(this);
        config.setItemStateListener(this);
    }

    public void montaSobre() {
        Runtime runtime = Runtime.getRuntime();

        sobre = new TextBox(
                "Sobre",
                piano.getAppProperty("MIDlet-Name") + "\n" +
                "Versão: " + piano.getAppProperty("MIDlet-Version") + "\n" +
                "Autor: " + piano.getAppProperty("MIDlet-Vendor") + "\n" +
                "Memória total: " + runtime.totalMemory() / 1024 + " kb" + "\n" +
                "Memória livre: " + runtime.freeMemory() / 1024 + " kb" + "\n",
                500,
                TextField.ANY | TextField.UNEDITABLE);

        sobre.addCommand(cmdVoltar);
        sobre.setCommandListener(this);
    }

    public void itemStateChanged(Item item) {
        if (item == volume) {
            Escala.volume = volume.getValue();
        } else if (item == mostraNmNota) {
            for (int i = 0; i < mostraNmNota.size(); i++) {
                if (mostraNmNota.isSelected(i)) {
                    Escala.mostraNota = mostraNmNota.getString(i);
                }
            }
        } else if (item == mostraNotaTocada) {
            for (int i = 0; i < mostraNotaTocada.size(); i++) {
                if (mostraNotaTocada.isSelected(i)) {
                    Escala.mostraTecla = mostraNotaTocada.getString(i);
                }
            }
        } else if (item == listaCores) {
            for (int i = 0; i < listaCores.size(); i++) {
                if (listaCores.isSelected(i)) {
                    corSelecionada = listaCores.getString(i);
                }
            }
            if (corSelecionada.equals("Amarelo")) {
                red = 255;
                green = 215;
                blue = 0;
            } else if (corSelecionada.equals("Azul")) {
                red = 0;
                green = 0;
                blue = 255;
            } else if (corSelecionada.equals("Vermelho")) {
                red = 255;
                green = 0;
                blue = 0;
            } else if (corSelecionada.equals("Verde")) {
                red = 0;
                green = 128;
                blue = 0;
            }
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdSair) {
            piano.destroyApp(false);
        } else if (c == cmdAjuda) {
            Display.getDisplay(piano).setCurrent(help);
        } else if (c == cmdConfig) {
            Display.getDisplay(piano).setCurrent(config);
        } else if (c == cmdVoltar) {
            Display.getDisplay(piano).setCurrent(this);
        } else if (c == cmdSobre) {
            Display.getDisplay(piano).setCurrent(sobre);
        }
    }

    public void carregaIcone() {

        try {
            icone = new Image[4];
            icone[0] = Image.createImage("/amarelo.png");
            icone[1] = Image.createImage("/azul.png");
            icone[2] = Image.createImage("/vermelho.png");
            icone[3] = Image.createImage("/verde.png");
        } catch (Exception e) {
            System.out.println("Impossivel carregar imagem ");
            e.printStackTrace();
        }
    }
}
