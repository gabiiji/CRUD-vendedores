package vendedor;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class vendedor extends JFrame {

    JMenuBar mb;
    JMenu archivo, bd;
    JMenuItem bd_C, bd_R, bd_U, bd_D;

    public JLabel lbl, lb_id, lb_n, lb_a, lb_nivel, lb_edad, lb_sexo;

    public JTextField id, nombre, apellidos, edad;

    public JComboBox<String> nivel, sexo;

    public JButton btnl, btn_consulta, btn_alta,
            btn_actualizar, btn_eliminar;

    Connection cnx;

    // Constructor
    public vendedor() {

        try {
            cnx = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vendedores_bd",
                    "root",
                    ""
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error al conectar con la base de datos: "
                    + e.getMessage()
            );
        }

        this.setTitle("CRUD de Vendedores");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(750, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.WHITE);

        menu();

        lbl = new JLabel("SISTEMA DE VENDEDORES");
        lbl.setBounds(300, 5, 200, 30);
        this.add(lbl);

        // ID
        lb_id = new JLabel("ID:");
        lb_id.setBounds(50, 55, 100, 25);
        this.add(lb_id);

        id = new JTextField();
        id.setBounds(160, 55, 200, 25);
        this.add(id);

        // Nombre
        lb_n = new JLabel("Nombre:");
        lb_n.setBounds(50, 95, 100, 25);
        this.add(lb_n);

        nombre = new JTextField();
        nombre.setBounds(160, 95, 200, 25);
        this.add(nombre);

        // Apellidos
        lb_a = new JLabel("Apellidos:");
        lb_a.setBounds(50, 135, 100, 25);
        this.add(lb_a);

        apellidos = new JTextField();
        apellidos.setBounds(160, 135, 200, 25);
        this.add(apellidos);

        // Nivel
        lb_nivel = new JLabel("Nivel:");
        lb_nivel.setBounds(50, 175, 100, 25);
        this.add(lb_nivel);

        nivel = new JComboBox<>(
                new String[]{"Junior", "Intermedio", "Senior"}
        );
        nivel.setBounds(160, 175, 200, 25);
        this.add(nivel);

        // Edad
        lb_edad = new JLabel("Edad:");
        lb_edad.setBounds(50, 215, 100, 25);
        this.add(lb_edad);

        edad = new JTextField();
        edad.setBounds(160, 215, 200, 25);
        this.add(edad);

        // Sexo
        lb_sexo = new JLabel("Sexo:");
        lb_sexo.setBounds(50, 255, 100, 25);
        this.add(lb_sexo);

        sexo = new JComboBox<>(
                new String[]{"Masculino", "Femenino", "Otro"}
        );
        sexo.setBounds(160, 255, 200, 25);
        this.add(sexo);

        // Botón Crear
        btn_alta = new JButton("Crear");
        btn_alta.setBounds(310, 320, 100, 40);
        btn_alta.addActionListener(e -> insertar());
        this.add(btn_alta);

        // Botón Consultar
        btn_consulta = new JButton("Consultar");
        btn_consulta.setBounds(70, 320, 120, 40);
        btn_consulta.addActionListener(e -> Consultar());
        this.add(btn_consulta);

        // Botón Actualizar
        btn_actualizar = new JButton("Actualizar");
        btn_actualizar.setBounds(195, 320, 110, 40);
        btn_actualizar.addActionListener(e -> actualizar());
        this.add(btn_actualizar);

        // Botón Eliminar
        btn_eliminar = new JButton("Eliminar");
        btn_eliminar.setBounds(415, 320, 100, 40);
        btn_eliminar.addActionListener(e -> eliminar());
        this.add(btn_eliminar);

        // Botón Limpiar
        btnl = new JButton("Limpiar");
        btnl.setBounds(520, 320, 90, 40);
        btnl.addActionListener(e -> limpiar());
        this.add(btnl);

        this.setVisible(true);
    }

    // Obtener o recuperar la conexión
    private Connection obtenerConexion() throws SQLException {

        if (cnx == null || cnx.isClosed()) {
            cnx = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vendedores_bd",
                    "root",
                    ""
            );
        }

        return cnx;
    }

    // CREATE: insertar vendedor
    public void insertar() {

        String sql = "INSERT INTO vendedor "
                + "(nombre, apellidos, nivel, edad, sexo) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            validarDatos();

            try (PreparedStatement ps
                    = obtenerConexion().prepareStatement(sql)) {

                ps.setString(1, nombre.getText().trim());
                ps.setString(2, apellidos.getText().trim());
                ps.setString(3, nivel.getSelectedItem().toString());
                ps.setInt(4, Integer.parseInt(edad.getText().trim()));
                ps.setString(5, sexo.getSelectedItem().toString());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Vendedor registrado correctamente."
                );

                limpiar();
            }

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al insertar: " + ex.getMessage()
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // READ: consultar vendedores
    public void Consultar() {

        String sql = "SELECT * FROM vendedor";

        try (PreparedStatement ps
                = obtenerConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            StringBuilder resultado = new StringBuilder();

            while (rs.next()) {

                resultado.append("ID: ")
                        .append(rs.getInt("id"))
                        .append("\nNombre: ")
                        .append(rs.getString("nombre"))
                        .append("\nApellidos: ")
                        .append(rs.getString("apellidos"))
                        .append("\nNivel: ")
                        .append(rs.getString("nivel"))
                        .append("\nEdad: ")
                        .append(rs.getInt("edad"))
                        .append("\nSexo: ")
                        .append(rs.getString("sexo"))
                        .append("\n--------------------------\n");
            }

            if (resultado.length() == 0) {
                resultado.append("No hay vendedores registrados.");
            }

            JOptionPane.showMessageDialog(
                    this,
                    resultado.toString(),
                    "Lista de vendedores",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al consultar: " + ex.getMessage()
            );
        }
    }

    // UPDATE: actualizar vendedor por ID
    public void actualizar() {

        String sql = "UPDATE vendedor SET nombre = ?, "
                + "apellidos = ?, nivel = ?, edad = ?, sexo = ? "
                + "WHERE id = ?";

        try {
            validarDatos();

            if (id.getText().trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Escribe el ID del vendedor que deseas actualizar."
                );
            }

            try (PreparedStatement ps
                    = obtenerConexion().prepareStatement(sql)) {

                ps.setString(1, nombre.getText().trim());
                ps.setString(2, apellidos.getText().trim());
                ps.setString(3, nivel.getSelectedItem().toString());
                ps.setInt(4, Integer.parseInt(edad.getText().trim()));
                ps.setString(5, sexo.getSelectedItem().toString());
                ps.setInt(6, Integer.parseInt(id.getText().trim()));

                int filas = ps.executeUpdate();

                if (filas > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Vendedor actualizado correctamente."
                    );

                    limpiar();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "No existe un vendedor con ese ID."
                    );
                }
            }

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al actualizar: " + ex.getMessage()
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // DELETE: eliminar vendedor por ID
    public void eliminar() {

        String sql = "DELETE FROM vendedor WHERE id = ?";

        try {
            if (id.getText().trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Escribe el ID del vendedor que deseas eliminar."
                );
            }

            int idVendedor = Integer.parseInt(id.getText().trim());

            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas eliminar al vendedor con ID "
                    + idVendedor + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            try (PreparedStatement ps
                    = obtenerConexion().prepareStatement(sql)) {

                ps.setInt(1, idVendedor);

                int filas = ps.executeUpdate();

                if (filas > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Vendedor eliminado correctamente."
                    );

                    limpiar();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "No existe un vendedor con ese ID."
                    );
                }
            }

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar: " + ex.getMessage()
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // Validar los datos antes de insertar o actualizar
    private void validarDatos() {

        if (nombre.getText().trim().isEmpty()
                || apellidos.getText().trim().isEmpty()
                || edad.getText().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Completa nombre, apellidos y edad."
            );
        }

        int edadValor = Integer.parseInt(edad.getText().trim());

        if (edadValor < 18 || edadValor > 100) {
            throw new IllegalArgumentException(
                    "La edad debe estar entre 18 y 100 años."
            );
        }
    }

    // Limpiar campos
    public void limpiar() {

        id.setText("");
        nombre.setText("");
        apellidos.setText("");
        edad.setText("");

        nivel.setSelectedIndex(0);
        sexo.setSelectedIndex(0);
    }

    // Menú principal
    public void menu() {

        mb = new JMenuBar();

        archivo = new JMenu("Archivo");
        bd = new JMenu("Base de datos");

        mb.add(archivo);
        mb.add(bd);

        // Crear vendedor
        bd_C = new JMenuItem("Crear vendedor");
        bd_C.addActionListener(e -> insertar());
        bd.add(bd_C);

        // Consultar vendedores
        bd_R = new JMenuItem("Consultar vendedores");
        bd_R.addActionListener(e -> Consultar());
        bd.add(bd_R);

        // Actualizar vendedor
        bd_U = new JMenuItem("Actualizar vendedor");
        bd_U.addActionListener(e -> actualizar());
        bd.add(bd_U);

        // Eliminar vendedor
        bd_D = new JMenuItem("Eliminar vendedor");
        bd_D.addActionListener(e -> eliminar());
        bd.add(bd_D);

        // Salir
        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(e -> System.exit(0));
        archivo.add(salir);

        this.setJMenuBar(mb);
    }

    // Método principal
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            vendedor v = new vendedor();
            v.setVisible(true);
        });
    }
}
