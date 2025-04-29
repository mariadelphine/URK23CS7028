import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Dashboard extends JFrame {
    private JPanel mainPanel;
    private JPanel contentPanel;
    
    public Dashboard() {
        setTitle("Clinic Management System Dashboard");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Initialize the main panel with a custom background
        mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Create a beautiful gradient background
                Graphics2D g2d = (Graphics2D) g;
                
                // Use anti-aliasing for smoother rendering
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create a gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(240, 248, 255), // Light blue start
                    getWidth(), getHeight(), new Color(135, 206, 250) // Sky blue end
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add some decorative elements
                g2d.setColor(new Color(255, 255, 255, 50)); // Transparent white
                
                // Draw some circular elements that look like medical symbols
                for (int i = 0; i < 8; i++) {
                    int size = 80 + (i * 15);
                    int x = (int)(Math.random() * getWidth());
                    int y = (int)(Math.random() * getHeight());
                    g2d.fillOval(x, y, size, size);
                }
                
                // Draw some plus signs (medical crosses)
                g2d.setColor(new Color(255, 255, 255, 30)); // More transparent white
                for (int i = 0; i < 5; i++) {
                    int size = 40 + (i * 10);
                    int x = (int)(Math.random() * getWidth());
                    int y = (int)(Math.random() * getHeight());
                    
                    g2d.fillRect(x, y + size/3, size, size/3); // Horizontal bar
                    g2d.fillRect(x + size/3, y, size/3, size); // Vertical bar
                }
            }
        };
        
        // Set layout and make transparent to show background
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);
        
        // Create content panel to hold the actual content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Create sidebar
        JPanel sidebar = createSidebar();
        
        // Add components to frame
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        
        // Show home panel initially
        showHome();
        
        setVisible(true);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(5, 1, 10, 10));
        sidebar.setBackground(new Color(52, 152, 219));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        
        // Create buttons with improved styling
        JButton homeButton = createStyledButton("Home");
        JButton bookAppointmentButton = createStyledButton("Book Appointment");
        JButton viewAppointmentsButton = createStyledButton("View Appointments");
        JButton logoutButton = createStyledButton("Logout");
        
        // Add buttons to sidebar with some padding
        sidebar.add(createButtonPanel(homeButton));
        sidebar.add(createButtonPanel(bookAppointmentButton));
        sidebar.add(createButtonPanel(viewAppointmentsButton));
        sidebar.add(createButtonPanel(logoutButton));
        
        // Add action listeners
        homeButton.addActionListener(e -> showHome());
        bookAppointmentButton.addActionListener(e -> showBookAppointment());
        viewAppointmentsButton.addActionListener(e -> showViewAppointments());
        logoutButton.addActionListener(e -> System.exit(0));
        
        return sidebar;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(41, 128, 185));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(52, 152, 219));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(41, 128, 185));
            }
        });
        
        return button;
    }
    
    private JPanel createButtonPanel(JButton button) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(button, BorderLayout.CENTER);
        return panel;
    }

    private void showHome() {
        contentPanel.removeAll();
        
        // Create a welcome panel with a logo and text
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setOpaque(false);
        
        // Create a medical logo icon
        JLabel logoLabel = new JLabel(createMedicalLogo());
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add welcome text
        JLabel welcomeLabel = new JLabel("Welcome to Clinic Management System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Manage your clinic operations efficiently");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(44, 62, 80));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components with padding
        welcomePanel.add(Box.createVerticalGlue());
        welcomePanel.add(logoLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        welcomePanel.add(subtitleLabel);
        welcomePanel.add(Box.createVerticalGlue());
        
        contentPanel.add(welcomePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private ImageIcon createMedicalLogo() {
        // Create a medical logo programmatically
        BufferedImage logoImage = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = logoImage.createGraphics();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw circular background
        g2d.setColor(new Color(41, 128, 185));
        g2d.fillOval(10, 10, 100, 100);
        
        // Draw white medical cross
        g2d.setColor(Color.WHITE);
        g2d.fillRect(45, 25, 30, 70); // Vertical bar
        g2d.fillRect(25, 45, 70, 30); // Horizontal bar
        
        g2d.dispose();
        
        return new ImageIcon(logoImage);
    }

    private void showBookAppointment() {
        contentPanel.removeAll();
        
        // Create a modified version of BookAppointmentForm that works within our panel
        JPanel bookAppointmentPanel = new ModifiedBookAppointmentForm();
        
        // Add padding around the form
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        wrapperPanel.add(bookAppointmentPanel, BorderLayout.CENTER);
        
        contentPanel.add(wrapperPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showViewAppointments() {
        contentPanel.removeAll();
        
        // Use our ViewAppointmentsForm directly
        ViewAppointmentsForm viewAppointmentsForm = new ViewAppointmentsForm();
        
        // Create a semi-transparent panel to hold the view
        JPanel transparentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(255, 255, 255, 200)); // Semi-transparent white
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        transparentPanel.setLayout(new BorderLayout());
        transparentPanel.setOpaque(false);
        
        // Add a title label
        JLabel titleLabel = new JLabel("Appointments", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        transparentPanel.add(titleLabel, BorderLayout.NORTH);
        transparentPanel.add(viewAppointmentsForm, BorderLayout.CENTER);
        
        // Add padding around the form
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        wrapperPanel.add(transparentPanel, BorderLayout.CENTER);
        
        contentPanel.add(wrapperPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    // Modified version of BookAppointmentForm that works as a panel
    private class ModifiedBookAppointmentForm extends JPanel {
        private JComboBox<PatientItem> patientComboBox;
        private JComboBox<DoctorItem> doctorComboBox;
        private JTextField appointmentDateField;
        private JTextField appointmentTimeField;
        private JTextField reasonField;
        private JButton bookButton;

        public ModifiedBookAppointmentForm() {
            // Create semi-transparent panel with rounded corners
            setLayout(new BorderLayout());
            setOpaque(false);
            
            JPanel formPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(255, 255, 255, 200)); // Semi-transparent white
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                }
            };
            formPanel.setLayout(new GridBagLayout());
            formPanel.setOpaque(false);
            
            // Title
            JLabel titleLabel = new JLabel("Book Appointment", JLabel.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
            
            // Form layout
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            // Patient selection
            gbc.gridx = 0;
            gbc.gridy = 0;
            JLabel patientLabel = new JLabel("Patient:");
            patientLabel.setFont(new Font("Arial", Font.BOLD, 14));
            formPanel.add(patientLabel, gbc);

            gbc.gridx = 1;
            patientComboBox = new JComboBox<>();
            loadPatients(); // Load patients from database
            formPanel.add(patientComboBox, gbc);
            
            // Doctor selection
            gbc.gridx = 0;
            gbc.gridy = 1;
            JLabel doctorLabel = new JLabel("Doctor:");
            doctorLabel.setFont(new Font("Arial", Font.BOLD, 14));
            formPanel.add(doctorLabel, gbc);

            gbc.gridx = 1;
            doctorComboBox = new JComboBox<>();
            loadDoctors(); // Load doctors from database
            formPanel.add(doctorComboBox, gbc);

            // Date
            gbc.gridx = 0;
            gbc.gridy = 2;
            JLabel appointmentDateLabel = new JLabel("Date (yyyy-mm-dd):");
            appointmentDateLabel.setFont(new Font("Arial", Font.BOLD, 14));
            formPanel.add(appointmentDateLabel, gbc);

            gbc.gridx = 1;
            appointmentDateField = new JTextField(20);
            formPanel.add(appointmentDateField, gbc);
            
            // Time
            gbc.gridx = 0;
            gbc.gridy = 3;
            JLabel appointmentTimeLabel = new JLabel("Time (HH:MM):");
            appointmentTimeLabel.setFont(new Font("Arial", Font.BOLD, 14));
            formPanel.add(appointmentTimeLabel, gbc);

            gbc.gridx = 1;
            appointmentTimeField = new JTextField(20);
            formPanel.add(appointmentTimeField, gbc);
            
            // Reason
            gbc.gridx = 0;
            gbc.gridy = 4;
            JLabel reasonLabel = new JLabel("Reason:");
            reasonLabel.setFont(new Font("Arial", Font.BOLD, 14));
            formPanel.add(reasonLabel, gbc);

            gbc.gridx = 1;
            reasonField = new JTextField(20);
            formPanel.add(reasonField, gbc);

            // Book button
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            bookButton = new JButton("Book Appointment");
            bookButton.setFont(new Font("Arial", Font.BOLD, 14));
            bookButton.setBackground(new Color(41, 128, 185));
            bookButton.setForeground(Color.WHITE);
            bookButton.setFocusPainted(false);
            bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            formPanel.add(bookButton, gbc);

            bookButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    bookAppointment();
                }
            });
            
            // Add components to the panel
            add(titleLabel, BorderLayout.NORTH);
            add(formPanel, BorderLayout.CENTER);
        }
        
        private void loadPatients() {
            try {
                Connection conn = DBConnection.getConnection();
                String sql = "SELECT id, name FROM patient";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    PatientItem patient = new PatientItem(
                        rs.getInt("id"),
                        rs.getString("name")
                    );
                    patientComboBox.addItem(patient);
                }
                
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error loading patients: " + ex.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
        
        private void loadDoctors() {
            try {
                Connection conn = DBConnection.getConnection();
                String sql = "SELECT id, name, specialization FROM doctor";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    DoctorItem doctor = new DoctorItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("specialization")
                    );
                    doctorComboBox.addItem(doctor);
                }
                
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error loading doctors: " + ex.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        private void bookAppointment() {
            try {
                if (patientComboBox.getSelectedItem() == null || 
                    doctorComboBox.getSelectedItem() == null ||
                    appointmentDateField.getText().isEmpty() ||
                    appointmentTimeField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                PatientItem patient = (PatientItem) patientComboBox.getSelectedItem();
                DoctorItem doctor = (DoctorItem) doctorComboBox.getSelectedItem();
                String appointmentDate = appointmentDateField.getText();
                String appointmentTime = appointmentTimeField.getText();
                String reason = reasonField.getText();
                
                // Validate date format
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateFormat.setLenient(false);
                    dateFormat.parse(appointmentDate);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid date format. Please use yyyy-MM-dd format.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate time format
                try {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    timeFormat.setLenient(false);
                    timeFormat.parse(appointmentTime);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid time format. Please use HH:MM format.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Connection conn = DBConnection.getConnection();
                String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, reason, status) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, patient.getId());
                stmt.setInt(2, doctor.getId());
                stmt.setString(3, appointmentDate);
                stmt.setString(4, appointmentTime);
                stmt.setString(5, reason);
                stmt.setString(6, "Scheduled");

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(Dashboard.this,
                            "Appointment booked successfully!\n\n" +
                            "Patient: " + patient.getName() + "\n" +
                            "Doctor: " + doctor.getName() + "\n" +
                            "Date: " + appointmentDate + "\n" +
                            "Time: " + appointmentTime,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Clear fields
                    patientComboBox.setSelectedIndex(0);
                    doctorComboBox.setSelectedIndex(0);
                    appointmentDateField.setText("");
                    appointmentTimeField.setText("");
                    reasonField.setText("");
                }
                
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    // Class to represent a patient in the combo box
    private class PatientItem {
        private int id;
        private String name;
        
        public PatientItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return id;
        }
        
        public String getName() {
            return name;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
    
    // Class to represent a doctor in the combo box
    private class DoctorItem {
        private int id;
        private String name;
        private String specialization;
        
        public DoctorItem(int id, String name, String specialization) {
            this.id = id;
            this.name = name;
            this.specialization = specialization;
        }
        
        public int getId() {
            return id;
        }
        
        public String getName() {
            return name;
        }
        
        public String getSpecialization() {
            return specialization;
        }
        
        @Override
        public String toString() {
            return name + " (" + specialization + ")";
        }
    }

    public static void main(String[] args) {
        // Set look and feel to system default for better integration
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new Dashboard());
    }
}

// Modified ViewAppointmentsForm to work better with our Dashboard
class ViewAppointmentsForm extends JPanel {
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    
    public ViewAppointmentsForm() {
        setLayout(new BorderLayout());
        setOpaque(false);  // Make panel transparent to show background
        
        // Define table columns
        String[] columns = {"ID", "Patient Name", "Doctor Name", "Date", "Time", "Reason", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make cells non-editable
            }
        };
        
        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setFillsViewportHeight(true);
        appointmentsTable.setRowHeight(25);
        appointmentsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        appointmentsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Add a refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(41, 128, 185));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadAppointments());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Load appointments from database
        loadAppointments();
    }
    
    private void loadAppointments() {
        try {
            // Clear the table
            tableModel.setRowCount(0);
            
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT a.id, p.name as patient_name, d.name as doctor_name, " +
                         "a.appointment_date, a.appointment_time, a.reason, a.status " +
                         "FROM appointments a " +
                         "JOIN patient p ON a.patient_id = p.id " +
                         "JOIN doctor d ON a.doctor_id = d.id " +
                         "ORDER BY a.appointment_date, a.appointment_time";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("patient_name"),
                    rs.getString("doctor_name"),
                    rs.getString("appointment_date"),
                    rs.getString("appointment_time"),
                    rs.getString("reason"),
                    rs.getString("status")
                });
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            if (tableModel.getRowCount() == 0) {
                // If no appointments were found, show a message
                JOptionPane.showMessageDialog(this,
                    "No appointments found in the database.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading appointments: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}