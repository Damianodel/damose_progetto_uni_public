package GUI_Roma;
import gestione_dati_gtfs_offline.stop.StopParser;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class InteractiveMap extends JPanel {
    private BufferedImage mapImage;
    private double latitude = 41.9028;  // Latitudine iniziale (Roma)
    private double longitude = 12.4964; // Longitudine iniziale (Roma)
    private int zoom = 12;              // Livello di zoom iniziale
    private int prevX, prevY;
    private boolean dragging = false;
    private JTextField searchField;
    private HashMap<String, double[]> locationData; // Dizionario delle posizioni
    private StopInfoPanel infoPanel; // Pannello Info Fermata

    public InteractiveMap(JTextField searchField, StopInfoPanel infoPanel) {
        this.searchField = searchField;
        this.infoPanel = infoPanel;
        locationData = StopParser.getLocationData();

        loadMap();

        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            if (notches < 0 && zoom < 18) zoom++;
            else if (notches > 0 && zoom > 3) zoom--;
            loadMap();
            repaint();
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
                dragging = true;
            }

            public void mouseReleased(MouseEvent e) {
                dragging = false;
                loadMap();
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    int dx = e.getX() - prevX;
                    int dy = e.getY() - prevY;

                    double lonPerPixel = 360 / Math.pow(2, zoom + 8);
                    double latPerPixel = 170 / Math.pow(2, zoom + 8);

                    longitude -= dx * lonPerPixel;
                    latitude += dy * latPerPixel;

                    prevX = e.getX();
                    prevY = e.getY();

                    loadMap();
                    repaint();
                }
            }
        });
    }

    private void loadMap() {
        try {
            String url = "https://static-maps.yandex.ru/1.x/?ll=" + longitude + "," + latitude +
                    "&z=" + zoom + "&size=650,450&l=map&lang=it_IT";
            mapImage = ImageIO.read(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapImage != null) {
            g.drawImage(mapImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    public void searchLocation(String location) {
        if (locationData.containsKey(location)) {
            double[] coords = locationData.get(location);
            latitude = coords[0];
            longitude = coords[1];
            loadMap();
            repaint();

            // Aggiorna il pannello info fermata
            infoPanel.updateInfo(location, coords[0], coords[1]);
        } else {
            JOptionPane.showMessageDialog(null, "Luogo non trovato nei dati!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mappa Interattiva con Ricerca Locale");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);

            JTextField searchField = new JTextField(20);
            JButton searchButton = new JButton("Cerca");
            JPanel searchPanel = new JPanel();
            searchPanel.add(searchField);
            searchPanel.add(searchButton);

            StopInfoPanel infoPanel = new StopInfoPanel(); // Pannello laterale con info fermata
            InteractiveMap mapPanel = new InteractiveMap(searchField, infoPanel);

            searchButton.addActionListener(e -> {
                String location = searchField.getText();
                if (!location.isEmpty()) {
                    mapPanel.searchLocation(location);
                }
            });

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapPanel, infoPanel);
            splitPane.setDividerLocation(650); // Dimensione iniziale della mappa

            frame.setLayout(new BorderLayout());
            frame.add(searchPanel, BorderLayout.NORTH);
            frame.add(splitPane, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}

// -----------------------------
// Pannello laterale "Info Fermata"
// -----------------------------
class StopInfoPanel extends JPanel {
    private JLabel titleLabel;
    private JLabel coordinatesLabel;
    private JTextArea linesTextArea;   // Area di testo per le linee disponibili
    private JTextArea scheduleTextArea; // Area di testo per gli orari

    public StopInfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 0)); // Larghezza del pannello

        titleLabel = new JLabel("Info Fermata");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        coordinatesLabel = new JLabel("Lat: - , Lon: -");

        linesTextArea = new JTextArea(5, 20);
        linesTextArea.setEditable(false);
        linesTextArea.setWrapStyleWord(true);
        linesTextArea.setLineWrap(true);
        JScrollPane linesScrollPane = new JScrollPane(linesTextArea);

        scheduleTextArea = new JTextArea(8, 20);
        scheduleTextArea.setEditable(false);
        scheduleTextArea.setWrapStyleWord(true);
        scheduleTextArea.setLineWrap(true);
        JScrollPane scheduleScrollPane = new JScrollPane(scheduleTextArea);

        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(coordinatesLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(new JLabel("🚍 Linee disponibili:"));
        add(linesScrollPane);
        add(new JLabel("⏰ Orari di passaggio:"));
        add(scheduleScrollPane);
    }

    public void updateInfo(String stopName, double lat, double lon) {
        titleLabel.setText("Fermata: " + stopName);
        coordinatesLabel.setText("Lat: " + lat + ", Lon: " + lon);

        // Ottieni i dati dal parser
        List<String> lines = StopParser.getLinesForStop(stopName);
        List<String> schedule = StopParser.getScheduleForStop(stopName);


    }
}