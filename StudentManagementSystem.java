import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystem extends JFrame {

    // ---------- THEME ----------
    static final Color BG_DARK    = new Color(24, 28, 38);
    static final Color BG_CARD    = new Color(33, 38, 51);
    static final Color BRAND_DARK1 = new Color(30, 20, 60);
    static final Color BRAND_DARK2 = new Color(20, 30, 55);
    static final Color ACCENT     = new Color(88, 101, 242);
    static final Color ACCENT_H   = new Color(114, 125, 245);
    static final Color GREEN      = new Color(46, 204, 113);
    static final Color GREEN_H    = new Color(72, 214, 132);
    static final Color RED        = new Color(231, 76, 60);
    static final Color RED_H      = new Color(241, 96, 80);
    static final Color TEXT       = new Color(230, 230, 235);
    static final Color SUBTEXT    = new Color(150, 155, 170);
    static final Font  FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    static final Font  FONT_SUB   = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font  FONT_BTN   = new Font("Segoe UI", Font.BOLD, 15);
    static final Font  FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 14);

    CardLayout cl = new CardLayout();
    JPanel container = new JPanel(cl);

    List<Student> students = new ArrayList<>();
    Student currentStudent = null;

    JLabel loginMsg = new JLabel(" ", SwingConstants.CENTER);
    JLabel welcomeLabel = new JLabel("", SwingConstants.CENTER);

    static class Student {
        String id, name, dept, attendance, score, timetable;

        Student(String id, String name, String dept, String attendance, String score, String timetable) {
            this.id = id;
            this.name = name;
            this.dept = dept;
            this.attendance = attendance;
            this.score = score;
            this.timetable = timetable;
        }

        // ---------- PASSWORD RULE FOR STUDENTS ----------
        // Roll number  -> id
        // Password     -> Name@Branch   e.g.  Ravi@Computer
        String expectedPassword() {
            return name + "@" + dept;
        }
    }

    public StudentManagementSystem() {
        setTitle("Student Management System - Login");
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_DARK);

        students.add(new Student("101", "Ravi", "Computer", "85%", "78", "Mon - Fri : 9 AM - 4 PM"));
        students.add(new Student("102", "Priya", "IT", "90%", "82", "Mon - Fri : 9 AM - 4 PM"));

        container.setBackground(BG_DARK);
        container.add(loginPanel(), "LOGIN");
        container.add(adminMenuPanel(), "ADMIN_MENU");
        container.add(studentMenuPanel(), "STUDENT_MENU");

        add(container);
        cl.show(container, "LOGIN");
        setVisible(true);
    }

    // ================= LOGIN SCREEN (CareLink style) =================
// ================= LOGIN SCREEN (centered card style) =================
    JPanel loginPanel() {
        JPanel root = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(18, 22, 34), 0, getHeight(), new Color(35, 25, 60));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 12));
                for (int x = 0; x < getWidth(); x += 26)
                    for (int y = 0; y < getHeight(); y += 26)
                        g2.fillOval(x, y, 2, 2);
                g2.dispose();
            }
        };
        root.setPreferredSize(new Dimension(1000, 620));

        JPanel card = new JPanel();
        card.setBackground(BG_CARD);
        card.setLayout(new GridBagLayout());
        card.setBounds(300, 60, 400, 500);
        card.setBorder(BorderFactory.createLineBorder(new Color(70, 75, 95), 1, true));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(6, 40, 6, 40);

        JLabel badge = new JLabel("SM", SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(ACCENT);
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 20));
        badge.setPreferredSize(new Dimension(60, 60));
        badge.setBorder(BorderFactory.createLineBorder(ACCENT_H, 3, true));

        JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        badgeWrap.setOpaque(false);
        badgeWrap.add(badge);

        JLabel title = new JLabel("Student Portal", SwingConstants.CENTER);
        title.setFont(FONT_TITLE.deriveFont(22f));
        title.setForeground(TEXT);

        JLabel sub = new JLabel("Sign in to continue", SwingConstants.CENTER);
        sub.setFont(FONT_SUB);
        sub.setForeground(SUBTEXT);

        JTextField userField = styledField();
        JPasswordField passField = styledPassField();

        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Select Role", "Admin", "Student"});
        roleBox.setFont(FONT_LABEL);
        roleBox.setBackground(new Color(45, 50, 65));
        roleBox.setForeground(TEXT);

        RoundButton loginBtn = new RoundButton("Sign In", ACCENT, ACCENT_H);
        loginBtn.setPreferredSize(new Dimension(0, 44));

        loginMsg.setForeground(RED);
        loginMsg.setFont(FONT_LABEL);

        loginBtn.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (role == null || role.equals("Select Role")) {
                loginMsg.setText("Please select a role");
                return;
            }
            if (role.equals("Admin")) {
                if (username.equals("admin") && password.equals("admin")) {
                    loginMsg.setText(" ");
                    clearFields(userField, passField);
                    cl.show(container, "ADMIN_MENU");
                } else {
                    loginMsg.setText("Wrong Username or Password");
                }
            } else {
                Student s = findStudent(username);
                if (s == null) {
                    loginMsg.setText("Roll Number Not Found");
                } else if (!password.equals(s.expectedPassword())) {
                    loginMsg.setText("Wrong Password");
                } else {
                    currentStudent = s;
                    loginMsg.setText(" ");
                    clearFields(userField, passField);
                    refreshStudentMenu();
                    cl.show(container, "STUDENT_MENU");
                }
            }
        });

        g.gridy = 0; g.insets = new Insets(30, 40, 4, 40); card.add(badgeWrap, g);
        g.gridy = 1; g.insets = new Insets(4, 40, 2, 40); card.add(title, g);
        g.gridy = 2; g.insets = new Insets(0, 40, 20, 40); card.add(sub, g);
        g.gridy = 3; g.insets = new Insets(0, 40, 2, 40); card.add(fieldLabel("Username (Roll No. for Student)"), g);
        g.gridy = 4; g.insets = new Insets(0, 40, 14, 40); card.add(userField, g);
        g.gridy = 5; g.insets = new Insets(0, 40, 2, 40); card.add(fieldLabel("Password"), g);
        g.gridy = 6; g.insets = new Insets(0, 40, 14, 40); card.add(passField, g);
        g.gridy = 7; g.insets = new Insets(0, 40, 2, 40); card.add(fieldLabel("Role"), g);
        g.gridy = 8; g.insets = new Insets(0, 40, 18, 40); card.add(roleBox, g);
        g.gridy = 9; g.insets = new Insets(0, 40, 10, 40); card.add(loginBtn, g);
        g.gridy = 10; g.insets = new Insets(0, 40, 0, 40); card.add(loginMsg, g);

        root.add(card);
        return root;
    }

    void clearFields(JTextField u, JPasswordField p) {
        u.setText("");
        p.setText("");
    }

    static class RoundButton extends JButton {
        private Color base, hover;
        RoundButton(String text, Color base, Color hover) {
            super(text);
            this.base = base;
            this.hover = hover;
            setFont(FONT_BTN);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBackground(base);
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { setBackground(hover); repaint(); }
                public void mouseExited(MouseEvent e)  { setBackground(base);  repaint(); }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    JTextField styledField() {
        JTextField f = new JTextField();
        f.setFont(FONT_LABEL);
        f.setBackground(new Color(45, 50, 65));
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 75, 95), 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        return f;
    }

    JPasswordField styledPassField() {
        JPasswordField f = new JPasswordField();
        f.setFont(FONT_LABEL);
        f.setBackground(new Color(45, 50, 65));
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 75, 95), 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        return f;
    }

    JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(SUBTEXT);
        return l;
    }

    JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(new EmptyBorder(30, 40, 30, 40));
        return p;
    }
    // ================= ADMIN MENU =================
    JPanel adminMenuPanel() {
        JPanel outer = card();
        outer.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(8, 0, 8, 0);

        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(FONT_TITLE.deriveFont(22f));
        title.setForeground(TEXT);

        RoundButton addBtn = new RoundButton("Add Student", ACCENT, ACCENT_H);
        RoundButton viewBtn = new RoundButton("View Students", ACCENT, ACCENT_H);
        RoundButton updateBtn = new RoundButton("Update Student", ACCENT, ACCENT_H);
        RoundButton deleteBtn = new RoundButton("Delete Student", ACCENT, ACCENT_H);
        RoundButton searchBtn = new RoundButton("Search Student", ACCENT, ACCENT_H);
        RoundButton logoutBtn = new RoundButton("Logout", RED, RED_H);

        for (RoundButton b : new RoundButton[]{addBtn, viewBtn, updateBtn, deleteBtn, searchBtn, logoutBtn})
            b.setPreferredSize(new Dimension(280, 42));

        addBtn.addActionListener(e -> showAddStudent());
        viewBtn.addActionListener(e -> showViewStudents());
        updateBtn.addActionListener(e -> showUpdateStudent());
        deleteBtn.addActionListener(e -> showDeleteStudent());
        searchBtn.addActionListener(e -> showSearchStudent());
        logoutBtn.addActionListener(e -> cl.show(container, "LOGIN"));

        g.gridy = 0; g.insets = new Insets(0, 0, 20, 0); outer.add(title, g);
        g.gridy = 1; g.insets = new Insets(6, 0, 6, 0); outer.add(addBtn, g);
        g.gridy = 2; outer.add(viewBtn, g);
        g.gridy = 3; outer.add(updateBtn, g);
        g.gridy = 4; outer.add(deleteBtn, g);
        g.gridy = 5; outer.add(searchBtn, g);
        g.gridy = 6; g.insets = new Insets(20, 0, 0, 0); outer.add(logoutBtn, g);
        return outer;
    }

    // ---------- ADD ----------
    void showAddStudent() {
        JTextField id = styledField();
        JTextField name = styledField();
        JTextField dept = styledField();

        JPanel p = darkDialogPanel();
        addRow(p, "Student ID (Roll No.) :", id);
        addRow(p, "Student Name :", name);
        addRow(p, "Department :", dept);
        JLabel hint = new JLabel("Password will be: Name@Department");
        hint.setForeground(SUBTEXT);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        p.add(hint);

        int option = JOptionPane.showConfirmDialog(this, p, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        if (id.getText().trim().isEmpty() || name.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID and Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        students.add(new Student(id.getText(), name.getText(), dept.getText(), "0%", "0", "Not Available"));
        JOptionPane.showMessageDialog(this, "Student Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------- VIEW ----------
    void showViewStudents() {
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Students Found!");
            return;
        }
        String[] cols = {"ID", "Name", "Department", "Attendance", "Score"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Student s : students) model.addRow(new Object[]{s.id, s.name, s.dept, s.attendance, s.score});

        JTable table = new JTable(model);
        table.setFont(FONT_LABEL);
        table.setRowHeight(26);
        table.getTableHeader().setFont(FONT_BTN.deriveFont(13f));
        table.setBackground(new Color(45, 50, 65));
        table.setForeground(TEXT);
        table.getTableHeader().setBackground(ACCENT);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(70, 75, 95));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(460, 200));

        JOptionPane.showMessageDialog(this, scroll, "Student List", JOptionPane.PLAIN_MESSAGE);
    }

    // ---------- UPDATE ----------
    void showUpdateStudent() {
        String id = JOptionPane.showInputDialog(this, "Enter Student ID");
        if (id == null) return;

        Student s = findStudent(id);
        if (s == null) { JOptionPane.showMessageDialog(this, "Student Not Found!"); return; }

        JTextField name = styledField(); name.setText(s.name);
        JTextField dept = styledField(); dept.setText(s.dept);

        JPanel p = darkDialogPanel();
        addRow(p, "Name :", name);
        addRow(p, "Department :", dept);

        int option = JOptionPane.showConfirmDialog(this, p, "Update Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            s.name = name.getText();
            s.dept = dept.getText();
            JOptionPane.showMessageDialog(this, "Student Updated Successfully!\nNew password: " + s.expectedPassword());
        }
    }

    // ---------- DELETE ----------
    void showDeleteStudent() {
        String id = JOptionPane.showInputDialog(this, "Enter Student ID");
        if (id == null) return;

        Student s = findStudent(id);
        if (s == null) { JOptionPane.showMessageDialog(this, "Student Not Found!"); return; }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete " + s.name + " ?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            students.remove(s);
            JOptionPane.showMessageDialog(this, "Student Deleted Successfully!");
        }
    }

    // ---------- SEARCH ----------
    void showSearchStudent() {
        String id = JOptionPane.showInputDialog(this, "Enter Student ID");
        if (id == null) return;

        Student s = findStudent(id);
        if (s == null) { JOptionPane.showMessageDialog(this, "Student Not Found!"); return; }

        String details = "ID : " + s.id + "\n"
                + "Name : " + s.name + "\n"
                + "Department : " + s.dept + "\n"
                + "Attendance : " + s.attendance + "\n"
                + "Score : " + s.score + "\n"
                + "Time Table : " + s.timetable
                + "\nLogin Password : " + s.expectedPassword();

        JTextArea area = new JTextArea(details);
        area.setEditable(false);
        area.setFont(FONT_LABEL);
        area.setBackground(new Color(45, 50, 65));
        area.setForeground(TEXT);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));

        JOptionPane.showMessageDialog(this, area, "Student Found", JOptionPane.PLAIN_MESSAGE);
    }

    Student findStudent(String id) {
        for (Student s : students) if (s.id.equals(id)) return s;
        return null;
    }

    JPanel darkDialogPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_CARD);
        p.setBorder(new EmptyBorder(6, 6, 6, 6));
        UIManager.put("OptionPane.background", BG_CARD);
        UIManager.put("Panel.background", BG_CARD);
        return p;
    }

    void addRow(JPanel p, String label, JComponent field) {
        JLabel l = fieldLabel(label);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(300, 32));
        p.add(l);
        p.add(Box.createVerticalStrut(4));
        p.add(field);
        p.add(Box.createVerticalStrut(12));
    }

    // ================= STUDENT MENU =================
    JPanel studentMenuPanel() {
        JPanel outer = card();
        outer.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(8, 0, 8, 0);

        welcomeLabel.setFont(FONT_TITLE.deriveFont(20f));
        welcomeLabel.setForeground(TEXT);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        RoundButton attendanceBtn = new RoundButton("Attendance", GREEN, GREEN_H);
        RoundButton scoreBtn = new RoundButton("Test Score", GREEN, GREEN_H);
        RoundButton timetableBtn = new RoundButton("Time Table", GREEN, GREEN_H);
        RoundButton logoutBtn = new RoundButton("Logout", RED, RED_H);

        for (RoundButton b : new RoundButton[]{attendanceBtn, scoreBtn, timetableBtn, logoutBtn})
            b.setPreferredSize(new Dimension(280, 42));

        attendanceBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Attendance : " + currentStudent.attendance,
                        "Attendance", JOptionPane.INFORMATION_MESSAGE));
        scoreBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Test Score : " + currentStudent.score,
                        "Test Score", JOptionPane.INFORMATION_MESSAGE));
        timetableBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Time Table\n\n" + currentStudent.timetable,
                        "Time Table", JOptionPane.INFORMATION_MESSAGE));
        logoutBtn.addActionListener(e -> {
            currentStudent = null;
            cl.show(container, "LOGIN");
        });

        g.gridy = 0; g.insets = new Insets(0, 0, 24, 0); outer.add(welcomeLabel, g);
        g.gridy = 1; g.insets = new Insets(6, 0, 6, 0); outer.add(attendanceBtn, g);
        g.gridy = 2; outer.add(scoreBtn, g);
        g.gridy = 3; outer.add(timetableBtn, g);
        g.gridy = 4; g.insets = new Insets(20, 0, 0, 0); outer.add(logoutBtn, g);
        return outer;
    }

    void refreshStudentMenu() {
        if (currentStudent != null) welcomeLabel.setText("Welcome, " + currentStudent.name);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(StudentManagementSystem::new);
    }
}