package view;

import rasterize.ImageBuffer;
import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private final Panel panel;
    private JCheckBox cubeBox;
    private JCheckBox pyramidBox;
    private JCheckBox bezierSurfaceBox;
    private final Dimension buttonDimension = new Dimension(50,20);
    private final JButton xTranslationPlus = new JButton("Translation +X");
    private final JButton xTranslationMinus = new JButton("Translation -X");
    private final JButton yTranslationPlus = new JButton("Translation +Y");
    private final JButton yTranslationMinus = new JButton("Translation -Y");
    private final JButton zTranslationPlus = new JButton("Translation +Z");
    private final JButton zTranslationMinus = new JButton("Translation -Z");
    private final JButton xRotationPlus = new JButton("Rotation +X");
    private final JButton xRotationMinus = new JButton("Rotation -X");
    private final JButton yRotationPlus = new JButton("Rotation +Y");
    private final JButton yRotationMinus = new JButton("Rotation -Y");
    private final JButton zRotationPlus = new JButton("Rotation +Z");
    private final JButton zRotationMinus = new JButton("Rotation -Z");
    private final JButton enlargeSolid = new JButton("Enlarge");
    private final JButton shrinkSolid = new JButton("Shrink");
    private final JButton wireframe = new JButton("Wireframe");

    public Window() {
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("PGRF 2: Z-Buffer | Martin Vahala");

        Dimension d = new Dimension(1280, 720);
        ImageBuffer raster = new ImageBuffer(d.width, d.height); //!tady vytváříme raster pro celou appku a předáváme ji do panelu

        panel = new Panel(raster);
        add(panel,BorderLayout.CENTER);

        createControlPanel();
        createMenu();

        pack();
        setLocationRelativeTo(null);
        panel.grabFocus();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        add(menuBar,BorderLayout.NORTH);

        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem fileHelp = new JMenuItem("Help");
        fileHelp.addActionListener((e) -> {
            String napoveda = "\n\tNejprve si z checkboxu v levém horním rohu vyberte těleso k ovládání.\n\tVybrané těleso můžete ovládat příslušnými tlačítky.\n\t" +
                    "Bez vybraného tělesa tlačítka programu nefungují!\n\n\tScénu můžete procházet pomocí kláves \"WSAD\", rozhlížíte se tažením myší (držte levém tlačítko myši).\n\t" +
                    "Projekci na ORTOGONÁLNÍ změníte klávesou \"O\", zpět na PERSPEKTIVNÍ se vrátíte klávesou \"P\"\n\n\t" +
                    "Pro přepnutí do čistě drátového režimu a zpět použijte Mezerník.\n";
            JFrame frame = new JFrame("OVLÁDÁNÍ");
            frame.setFocusable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JPanel inner = new JPanel();
            inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
            inner.setOpaque(true);
            JTextArea textArea = new JTextArea(napoveda);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            textArea.setFont(Font.getFont(Font.SERIF));
            inner.add(textArea);

            frame.getContentPane().add(BorderLayout.CENTER, inner);
            frame.pack();
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
            frame.setResizable(true);
            setLocationRelativeTo(getParent());
            panel.setFocusable(true);
            panel.grabFocus();
        });
        menu.add(fileHelp);

        JMenuItem fileExit = new JMenuItem("Exit");
        fileExit.addActionListener((e)-> {
            int exit = JOptionPane.showConfirmDialog(this,"Opravdu chcete skončit?","Ukončení programu",JOptionPane.YES_NO_OPTION);
            if (exit == 0)
                System.exit(0);
        });
        menu.add(fileExit);

        JMenu aboutMenu = new JMenu("About");
        menuBar.add(aboutMenu);

        JMenuItem about = new JMenuItem("O aplikaci");
        about.addActionListener((e)-> JOptionPane.showMessageDialog(this,"Z-Buffer | Počítačová grafika 2\nAutor: Martin Vahala, Aplikovaná informatika"));

        aboutMenu.add(about);
    }

    private void createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel,BoxLayout.Y_AXIS));
        add(controlPanel,BorderLayout.WEST);

        JPanel solidSelection = new JPanel();
        solidSelection.setLayout(new BoxLayout(solidSelection,BoxLayout.Y_AXIS));
        cubeBox = new JCheckBox("Cube");
        pyramidBox = new JCheckBox("Pyramid");
        bezierSurfaceBox = new JCheckBox("Bezier Surface");

        solidSelection.add(cubeBox);
        solidSelection.add(pyramidBox);
        solidSelection.add(bezierSurfaceBox);
        solidSelection.setBorder(BorderFactory.createTitledBorder("Tělesa"));

        controlPanel.add(solidSelection);

        JPanel translationButtons = new JPanel();
        translationButtons.setLayout(new GridLayout(6,1,5,5));
        JButton[] jButtons = {xTranslationPlus,xTranslationMinus,yTranslationPlus,yTranslationMinus,zTranslationPlus,
                zTranslationMinus,xRotationPlus,xRotationMinus,yRotationPlus,yRotationMinus,zRotationPlus,zRotationMinus,
                enlargeSolid,shrinkSolid,wireframe};
        for (JButton button:jButtons) {
            button.setPreferredSize(buttonDimension);
            button.setMargin(new Insets(2,2,2,2));
        }

        translationButtons.add(xTranslationPlus);
        translationButtons.add(xTranslationMinus);
        translationButtons.add(yTranslationPlus);
        translationButtons.add(yTranslationMinus);
        translationButtons.add(zTranslationPlus);
        translationButtons.add(zTranslationMinus);
        translationButtons.setAlignmentX(LEFT_ALIGNMENT);
        translationButtons.setBorder(BorderFactory.createTitledBorder("Translace"));

        controlPanel.add(translationButtons);

        JPanel rotationButtons = new JPanel();
        rotationButtons.setLayout(new GridLayout(6,1,5,5));

        rotationButtons.add(xRotationPlus);
        rotationButtons.add(xRotationMinus);
        rotationButtons.add(yRotationPlus);
        rotationButtons.add(yRotationMinus);
        rotationButtons.add(zRotationPlus);
        rotationButtons.add(zRotationMinus);
        rotationButtons.setAlignmentX(LEFT_ALIGNMENT);
        rotationButtons.setBorder(BorderFactory.createTitledBorder("Rotace"));

        controlPanel.add(rotationButtons);

        JPanel sizeButtons = new JPanel();
        sizeButtons.setLayout(new GridLayout(2,1,5,5));

        sizeButtons.add(enlargeSolid);
        sizeButtons.add(shrinkSolid);
        sizeButtons.setAlignmentX(LEFT_ALIGNMENT);
        sizeButtons.setBorder(BorderFactory.createTitledBorder("Size"));

        controlPanel.add(sizeButtons);

        JPanel wireframeButton = new JPanel();
        wireframeButton.setLayout(new GridLayout(1,1,5,5));
        wireframeButton.add(wireframe);
        wireframeButton.setAlignmentX(LEFT_ALIGNMENT);
        wireframeButton.setBorder(BorderFactory.createTitledBorder("Wireframe"));

        controlPanel.add(wireframeButton);
    }

    public Panel getPanel() {
        return panel;
    }

    public JCheckBox getCubeBox() {
        return cubeBox;
    }

    public JCheckBox getPyramidBox() {
        return pyramidBox;
    }

    public JCheckBox getBezierSurfaceBox() {
        return bezierSurfaceBox;
    }

    public JButton getxTranslationPlus() {
        return xTranslationPlus;
    }

    public JButton getxTranslationMinus() {
        return xTranslationMinus;
    }

    public JButton getyTranslationPlus() {
        return yTranslationPlus;
    }

    public JButton getyTranslationMinus() {
        return yTranslationMinus;
    }

    public JButton getzTranslationPlus() {
        return zTranslationPlus;
    }

    public JButton getzTranslationMinus() {
        return zTranslationMinus;
    }

    public JButton getxRotationPlus() {
        return xRotationPlus;
    }

    public JButton getxRotationMinus() {
        return xRotationMinus;
    }

    public JButton getyRotationPlus() {
        return yRotationPlus;
    }

    public JButton getyRotationMinus() {
        return yRotationMinus;
    }

    public JButton getzRotationPlus() {
        return zRotationPlus;
    }

    public JButton getzRotationMinus() {
        return zRotationMinus;
    }

    public JButton getEnlargeSolid() {
        return enlargeSolid;
    }

    public JButton getShrinkSolid() {
        return shrinkSolid;
    }

    public JButton getWireframe() {
        return wireframe;
    }
}
