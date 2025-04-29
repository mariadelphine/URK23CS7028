import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewAppointmentsForm extends JPanel {
    private JTable appointmentsTable;

    public ViewAppointmentsForm() {
        setLayout(new BorderLayout());

        appointmentsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        add(scrollPane, BorderLayout.CENTER);

        loadAppointments();
    }

    private void loadAppointments() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM appointments";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Patient ID", "Doctor ID", "Date", "Time", "Reason", "Status"}, 0
            );
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getString("appointment_date"),
                    rs.getString("appointment_time"),
                    rs.getString("reason"),
                    rs.getString("status")
                });
            }
            appointmentsTable.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
