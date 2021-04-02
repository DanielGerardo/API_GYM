package com.example.appgym;

import androidx.appcompat.app.AppCompatActivity;
import com.example.appgym.VentasDAO;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import static com.example.appgym.TableDynamic.data;
import static com.example.appgym.TableDynamic.tableLayout;

public class Ventas_Activity extends AppCompatActivity {

    Productos pd = new Productos();
    EntidadEntrenador En = new EntidadEntrenador();
    ProductosDAO pdao = new ProductosDAO();
    Venta v = new Venta();
    VentasDAO vdao = new VentasDAO();
    DetalleVentas dv = new DetalleVentas();
    int idp;
    double tpagar;
    int cat;
    double preci;
    public Spinner cmbPro,cmbEn;
    public TextView txtPrecio,txtStock,txtSerie,txtTotal;
    public EditText SpCantidad;
    public TableLayout table;
    public TableDynamic tableDynamic = new TableDynamic();
    private Object[]header={"No.Serie","ID","Nombre","Unidad","Precio","Cantidad","Total"};
    private ArrayList<Object[]> rows=new ArrayList<>();
    public Button btnAgregar,btnVenta,btnCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas_);
           cmbPro = (Spinner)findViewById(R.id.spProductos_Venta);
           cmbEn = (Spinner)findViewById(R.id.spEntrenador_Venta);
           SpCantidad = (EditText)findViewById(R.id.txtCantidad_Venta);
           txtSerie = (TextView)findViewById(R.id.txtSerie_Venta);
            txtTotal = (TextView)findViewById(R.id.txtTotal_Venta);
           txtStock = (TextView)findViewById(R.id.txtStock_Venta);
           txtPrecio = (TextView)findViewById(R.id.txtPrecio_Venta);
           btnAgregar = (Button)findViewById(R.id.btnAgregar_ventas);
        btnVenta = (Button)findViewById(R.id.btnVenta_Ventas);
        btnCancelar = (Button)findViewById(R.id.btnCancelar_Venta);
           TableDynamic.tableLayout = (TableLayout)findViewById(R.id.table_Ventas);
           TableDynamic.context = getApplicationContext();

        cmbPro.setAdapter(mostrarProducto());
        cmbEn.setAdapter(mostrarEntrenador());
        cargartable();
        generarSerie();
        SpCantidad.setText("0");
    cmbPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Productos est = (Productos) cmbPro.getSelectedItem();
            txtPrecio.setText("" + est.getPrecio());
            txtStock.setText("" + est.getCan());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
    btnAgregar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            agregarProducto();
        }
    });
        btnVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVentaClick();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevo();
            }
        });
    }
    public TableLayout cargartable(){
        tableDynamic.addHeader(header);
        tableDynamic.backgroundHeader(Color.rgb(255, 87, 34));
        tableDynamic.textColorHeader(Color.rgb(255, 255, 255));

        return TableDynamic.tableLayout;
    }
    public ArrayAdapter<EntidadEntrenador> mostrarEntrenador() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Conexion con = new Conexion();
        Connection acceso;

        ArrayAdapter<EntidadEntrenador> datos = new ArrayAdapter<EntidadEntrenador>(this,R.layout.spinner_ventas);
        EntidadEntrenador dat = null;


        try {
            acceso = con.Conexion();
            String sql = "SELECT * FROM Entrenador WHERE activo=1";
            ps = acceso.prepareStatement(sql);
            rs = ps.executeQuery();
            dat = new EntidadEntrenador();
            dat.setIdEn(0);
            dat.setNom("Selecciona Entrenador");
            datos.add(dat);

            while (rs.next()) {
                dat = new EntidadEntrenador();
                dat.setIdEn(rs.getInt("Id_Entrenador"));
                dat.setNom(rs.getString("Nombre"));
                datos.add(dat);

            }
            rs.close();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");


        }
        return datos;
    }
    public ArrayAdapter<Productos> mostrarProducto() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Conexion con = new Conexion();
        Connection acceso;

        ArrayAdapter<Productos> datos = new ArrayAdapter<Productos>(this,R.layout.spinner_ventas);
        Productos dat = null;


        try {
            acceso = con.Conexion();
            String sql = "SELECT * FROM Productos";
            ps = acceso.prepareStatement(sql);
            rs = ps.executeQuery();
            dat = new Productos();
            dat.setId(0);
            dat.setDes("Selecciona Producto");
            dat.setUnidad("");
            dat.setCan(3);
            dat.setPrecio(4);
            datos.add(dat);

            while (rs.next()) {
                dat = new Productos();
                dat.setId(rs.getInt("Id_Producto"));
                dat.setDes(rs.getString("Descripcion"));
                dat.setUnidad(rs.getString("Unidad"));
                dat.setCan(rs.getInt("Cantidad"));
                dat.setPrecio(rs.getDouble("Precio"));
                datos.add(dat);

            }
            rs.close();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");


        }
        return datos;
    }
    void agregarProducto() {

       double total;
       String uni;
       int item = 0;

       item = item + 1;
       Productos des = (Productos) cmbPro.getSelectedItem();
       idp = des.getId();
       uni = des.getUnidad();
       double pre = Double.parseDouble(txtPrecio.getText().toString());
       int cant = Integer.parseInt(SpCantidad.getText().toString());
       int stock = Integer.parseInt(txtStock.getText().toString());
       int cal = stock - (cant);
       if (cant <= stock) {
           txtStock.setText(String.valueOf(cal));
       }
       total = cant * pre;
       ArrayList<Object> lista = new ArrayList<>();
       if (stock > 0 && cant <= stock) {
           lista.add(item);
           lista.add(idp);
           lista.add(des);
           lista.add(uni);
           lista.add(pre);
           lista.add(cant);
           lista.add(total);
           Object[] items = new Object[]{lista.get(0),lista.get(1),lista.get(2),lista.get(3),lista.get(4),lista.get(5),lista.get(6)};
           tableDynamic.addItems(items);
           calcularTotal();
       }else {
           Toast.makeText(Ventas_Activity.this, "Stock Producto no disponible",Toast.LENGTH_LONG).show();
       }


    }


    void generarSerie() {
        String serie = vdao.SerieVenta();
        if (serie == null) {
            txtSerie.setText("1");
        } else {

            int incrementar = Integer.parseInt(serie);
            incrementar = incrementar + 1;
            txtSerie.setText(""+ incrementar);
        }
    }

    void calcularTotal() {
        tpagar = 0;

        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TextView tvcat = (TextView)tableDynamic.getCell(i, 5);
            cat = Integer.parseInt(tvcat.getText().toString());
            TextView tvpreci = (TextView)tableDynamic.getCell(i, 4);
            preci = Double.parseDouble(tvpreci.getText().toString());
            tpagar = tpagar + (cat * preci);
        }
        txtTotal.setText("$" + tpagar);
    }

    void nuevo() {

        cmbPro.setSelection(0);
        cmbEn.setSelection(0);
        SpCantidad.setText("0");
        txtTotal.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        tableLayout.removeViewsInLayout(1,data.size());
        data.clear();
    }



    void actulizarStock() {
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            Productos pr = new Productos();
           TextView tvidp =(TextView) tableDynamic.getCell(i, 1);
            idp = Integer.parseInt(tvidp.getText().toString());
            TextView tvcat = (TextView)tableDynamic.getCell(i, 5);
            cat = Integer.parseInt(tvcat.getText().toString());
            pr = pdao.listarID(idp);
            int sa = pr.getCan() - cat;
            pdao.actulizarStock(sa, idp);
        }
    }

    void guardarVenta() {
        EntidadEntrenador en = (EntidadEntrenador) cmbEn.getSelectedItem();
        int idE = en.getIdE();
        String serie = txtSerie.getText().toString();
        String fecha = "";
        double monto = tpagar;
        String estado = "1";

        v.setIdEntrenador(idE);
        v.setSerie(serie);
        v.setFecha(fecha);
        v.setMonto(monto);
        v.setEstado(estado);
        vdao.GuardarVentas(v);
    }

    void guardarDetalle() {
        String idV = vdao.IdVentas();
        int idve = Integer.parseInt(idV);
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TextView tvidp =(TextView) tableDynamic.getCell(i, 1);
           int idp = Integer.parseInt(tvidp.getText().toString());
            TextView tvUni =(TextView) tableDynamic.getCell(i, 3);
            String Unidad = (tvUni.getText().toString());
            TextView tvcat = (TextView)tableDynamic.getCell(i, 5);
           int cant = Integer.parseInt(tvcat.getText().toString());
            TextView tvPre =(TextView) tableDynamic.getCell(i, 6);
            double pre = Double.parseDouble(tvPre.getText().toString());
            dv.setIdVenta(idve);
            dv.setIdPro(idp);
            dv.setUnidad(Unidad);
            dv.setCantidad(cant);
            dv.setPreventa(pre);
            vdao.GuardarDetalleVentas(dv);
        }
    }

    void btnVentaClick() {
        if (txtTotal.getText().equals("")) {
            Toast.makeText(Ventas_Activity.this, "Debe de ingresar datos",Toast.LENGTH_LONG).show();
        } else {
            guardarVenta();
            guardarDetalle();
            actulizarStock();
            Toast.makeText(this, "Venta Realizada", Toast.LENGTH_LONG).show();
            generarSerie();
            nuevo();
        }
    }
}