package GUI_Roma;

import risorse_mappa.GTFSReader;

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
    private double latitude = 41.9028;  // Initial latitude (Rome)
    private double longitude = 12.4964; // Initial longitude (Rome)
    private int zoom = 12;              // Initial zoom level
    private int prevX, prevY;
    private boolean dragging = false;
    private JTextField searchField;
    private HashMap<String, double[]> locationData; // Location data dictionary
    private StopInfoPanel infoPanel; // Stop info panel

    public InteractiveMap(JTextField searchField, StopInfoPanel infoPanel) {
        this.searchField = searchField;
        this.infoPanel = infoPanel;
        locationData = GTFSReader.getLocationData();

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
        System.out.println("Searching location: " + location);
        if (locationData.containsKey(location) || GTFSReader.stopNameToIdMap.containsKey(location)) {
            String stopId = GTFSReader.stopNameToIdMap.getOrDefault(location, location);
            double[] coords = locationData.get(stopId);
            latitude = coords[0];
            longitude = coords[1];
            loadMap();
            repaint();

            // Update the stop info panel
            infoPanel.updateInfo(location, stopId, coords[0], coords[1]);

            // Get all routes for the stop
            List<String> routes = GTFSReader.getRoutesForStop(stopId);
            for (String routeId : routes) {
                // Get all stops for each route
                List<String> stops = GTFSReader.getAllStopsForRoute(routeId);
                System.out.println("Stops for route " + routeId + ": " + stops);
                // Display or process the stops as needed
            }
        } else {
            JOptionPane.showMessageDialog(null, "Location not found in data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Interactive Map with Local Search");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);

            JTextField searchField = new JTextField(20);
            JButton searchButton = new JButton("Search");
            JPanel searchPanel = new JPanel();
            searchPanel.add(searchField);
            searchPanel.add(searchButton);

            StopInfoPanel infoPanel = new StopInfoPanel(); // Side panel with stop info
            InteractiveMap mapPanel = new InteractiveMap(searchField, infoPanel);

            searchButton.addActionListener(e -> {
                String location = searchField.getText();
                if (!location.isEmpty()) {
                    mapPanel.searchLocation(location);
                }
            });

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapPanel, infoPanel);
            splitPane.setDividerLocation(650); // Initial size of the map

            frame.setLayout(new BorderLayout());
            frame.add(searchPanel, BorderLayout.NORTH);
            frame.add(splitPane, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}

class StopInfoPanel extends JPanel {
    private final JLabel titleLabel;
    private final JLabel coordinatesLabel;
    private final JLabel stopIdLabel;
    private final JTextArea linesTextArea;   // Text area for available lines
    private final JTextArea scheduleTextArea; // Text area for schedules

    public StopInfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 0)); // Width of the panel

        titleLabel = new JLabel("Stop Info");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        coordinatesLabel = new JLabel("Lat: - , Lon: -");
        stopIdLabel = new JLabel("Stop ID: -");

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
        add(stopIdLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(coordinatesLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(new JLabel("🚍 Available Lines:"));
        add(linesScrollPane);
        add(new JLabel("⏰ Schedules:"));
        add(scheduleScrollPane);
    }

    public void updateInfo(String stopName, String stopId, double lat, double lon) {
        System.out.println("Updating info for stop: " + stopName);
        titleLabel.setText("Stop: " + stopName);
        stopIdLabel.setText("Stop ID: " + stopId);
        coordinatesLabel.setText("Lat: " + lat + ", Lon: " + lon);

        // Get data from the parser
        List<String> lines = GTFSReader.getLinesForStop(stopId);
        List<String> schedule = GTFSReader.getScheduleForStop(stopId);

        StringBuilder linesText = new StringBuilder();
        for (String line : lines) {
            linesText.append(line).append("\n");
        }

        StringBuilder scheduleText = new StringBuilder();
        for (String time : schedule) {
            scheduleText.append(time).append("\n");
        }

        linesTextArea.setText(linesText.toString());
        scheduleTextArea.setText(scheduleText.toString());
    }
}
