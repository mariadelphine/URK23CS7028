import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookAppointmentForm extends JFrame {
    private JTextField patientNameField;
    private JTextField doctorIdField;
    private JTextField appointmentDateField;
    private JButton bookButton;
    private Image backgroundImage;

    public BookAppointmentForm() {
        setTitle("Book Appointment");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load background image
        backgroundImage = new ImageIcon("background.png").getImage(); // <-- Make sure the image is in your project folder

        // Create transparent panel
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setLayout(new GridBagLayout());
        contentPane.setOpaque(false);
        setContentPane(contentPane);

        // Form layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel patientNameLabel = new JLabel("Patient Name:");
        patientNameLabel.setForeground(Color.WHITE);
        contentPane.add(patientNameLabel, gbc);

        gbc.gridx = 1;
        patientNameField = new JTextField(20);
        contentPane.add(patientNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel doctorIdLabel = new JLabel("Doctor ID:");
        doctorIdLabel.setForeground(Color.WHITE);
        contentPane.add(doctorIdLabel, gbc);

        gbc.gridx = 1;
        doctorIdField = new JTextField(20);
        contentPane.add(doctorIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel appointmentDateLabel = new JLabel("Appointment Date (yyyy-mm-dd):");
        appointmentDateLabel.setForeground(Color.WHITE);
        contentPane.add(appointmentDateLabel, gbc);

        gbc.gridx = 1;
        appointmentDateField = new JTextField(20);
        contentPane.add(appointmentDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bookButton = new JButton("Book Appointment");
        bookButton.setBackground(new Color(0, 153, 76)); 
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);
        contentPane.add(bookButton, gbc);

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookAppointment();
            }
        });
    }

    private void bookAppointment() {
        String patientName = patientNameField.getText();
        String doctorId = doctorIdField.getText();
        String appointmentDate = appointmentDateField.getText();

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection(); // your DB connection class
            String sql = "INSERT INTO appointments (patient_name, doctor_id, appointment_date) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, patientName);
            stmt.setInt(2, Integer.parseInt(doctorId));
            stmt.setString(3, appointmentDate);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Doctor ID must be a number.");
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookAppointmentForm form = new BookAppointmentForm();
            form.setVisible(true);
        });
    }
}
