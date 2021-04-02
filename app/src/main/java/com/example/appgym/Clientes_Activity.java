package com.example.appgym;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.appgym.TableDynamic.data;
import static com.example.appgym.TableDynamic.tableLayout;

public class Clientes_Activity extends AppCompatActivity {
    private ImageView img;

    private EditText et1,et2,et3,et4,et5,et6,et7,et8;
    private Spinner sp1;
    public ImageButton bt1,bt2,bt3,bt4,bt5;
    private TableLayout tl;
    private TableRow tr1;
    private Object[]header={"Nombre","Telefono","FechaDeIngreso","Enfermedad","UserName"};
    private Object[]header1={""};
    private ArrayList<Object[]> rows=new ArrayList<>();
    String [] Optiones = {"Sexo","M","F"};
    public TableDynamic tableDynamic=new TableDynamic();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_);

        img = (ImageView)findViewById(R.id.foto_Cliente);
        if (ContextCompat.checkSelfPermission(Clientes_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Clientes_Activity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Clientes_Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }
        et1 = (EditText) findViewById(R.id.txtNom_Cliente);
        et2 = (EditText) findViewById(R.id.txtApe_Cliente);
        et3 = (EditText) findViewById(R.id.txtDire_Cliente);
        et4 = (EditText) findViewById(R.id.txtTel_Cliente);
        et5 = (EditText) findViewById(R.id.txtTelA_Cliente);
        et6 = (EditText) findViewById(R.id.txtEdad_Cliente);
        et7 = (EditText) findViewById(R.id.txtUser_Cliente);
        et8 = (EditText) findViewById(R.id.txtEnfer_Cliente);
        sp1 = (Spinner) findViewById(R.id.Sexo_Cliente);
        TableDynamic.tableLayout = (TableLayout) findViewById(R.id.table_Cliente);
        TableDynamic.context = getApplicationContext();
        bt1 = (ImageButton) findViewById(R.id.btnBuscar_Cliente);
        bt2 = (ImageButton) findViewById(R.id.btnLimpiar_Cliente);
        bt3 = (ImageButton) findViewById(R.id.btnEditar_Cliente);
        bt4 = (ImageButton) findViewById(R.id.btnEliminar_Cliente);
        bt5 = (ImageButton) findViewById(R.id.btnfoto_Cliente);
        bt3.setVisibility(View.INVISIBLE);
        bt4.setVisibility(View.INVISIBLE);

        cargartable();
        ArrayAdapter<String> item = new ArrayAdapter<String>(this, R.layout.spinner_item,Optiones);
        sp1.setAdapter(item);

        bt1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                BuscarCliente();

            }
        });
    }

    public TableLayout cargartable(){
        tableDynamic.addHeader(header);
        tableDynamic.addData(getClients());
        tableDynamic.backgroundHeader(Color.rgb(0, 149, 255 ));
        tableDynamic.textColorHeader(Color.rgb(0, 0, 0));
        return TableDynamic.tableLayout;
    }

    String currentPhotoPath;

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Backup_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    public void tomarfoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            File photoFile = null;
            try {
                // startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.FileProvider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI.toString());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);
        }
    }

    public ArrayList<Object[]>getClients() {

        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rsmd;
        int colu;

        try {
            ps = cn.prepareStatement("SELECT Nombre,Telefono,FechaDeIngreso,Enfermedad,UserName FROM Clientes WHERE Activo=1");
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            colu = rsmd.getColumnCount();

            while (rs.next()) {

                Object[] fila = new Object[colu];
                for (int indice = 0; indice < colu; indice++) {
                    fila[indice] = rs.getString(indice + 1);

                }

                rows.add((Object[]) fila);

            }


        } catch (SQLException ex) {
            Toast.makeText(this,"Error Tabla Entrenador"+ex,Toast.LENGTH_SHORT).show();
        }
        return rows;
    }


    public void Limpiar(View view){

        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et6.setText("");
        sp1.setSelection(0);
        img.setImageBitmap(null);
        et8.setText("");
        if (et7.getText().length()>= 0) {
            et7.setText("");
            bt1.setVisibility(View.VISIBLE);
        }
        bt3.setVisibility(View.INVISIBLE);
        bt4.setVisibility(View.INVISIBLE);
        tableLayout.removeViewsInLayout(1,data.size());
        tableDynamic.addDatanull();
        tableLayout.removeViewsInLayout(1,data.size());
        data.clear();
        tableDynamic.addData(getClients());
    }

    public void ModificarCliente(View view) {
        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        String sql = "";
        Bitmap imageBitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imagen = baos.toByteArray();
        String nom = et1.getText().toString();
        String ape = et2.getText().toString();
        String dire = et3.getText().toString();
        String Tel = et4.getText().toString();
        String TelA = et5.getText().toString();
        String sexo = sp1.getSelectedItem().toString();
        int edad = Integer.parseInt(et6.getText().toString());
        // byte[] imagen = img.getBytes();
        String user = et7.getText().toString();
        String Enfer = et8.getText().toString();

        sql = "UPDATE Clientes SET Nombre=?, Apellidos=?, Direccion=?, Telefono=?, Tel_Adicional=?, Sexo=?, Edad=?, FechaDeIngreso=GETDATE(), Fotografia=?, UserName=?, activo=1, Enfermedad=? WHERE UserName=?";
        if(Tel.length() == 0){et5.setText("null");}
        if (nom.length() == 0 || ape.length() == 0 || dire.length() == 0 || Tel.length() > 10 || edad<=0 || user.length() == 0 || Enfer.length() == 0) {

            Toast.makeText(this, "Debes de llenar los campos", Toast.LENGTH_LONG).show();
        } else {
            try {
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, nom);
                pst.setString(2, ape);
                pst.setString(3, dire);
                pst.setString(4, Tel);
                pst.setString(5, TelA);
                pst.setString(6, sexo);
                pst.setInt(7, edad);
                pst.setBytes(8, imagen);
                pst.setString(9, user);
                pst.setString(10, Enfer);
                pst.setString(11, user);

                int n = pst.executeUpdate();
                if(n==0){
                    Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
                if (n > 0) {
                    Toast.makeText(this, "Registro Editado con exito!", Toast.LENGTH_SHORT).show();
                }
                Limpiar(view);
            } catch (SQLException ex) {
                Logger.getLogger(NuevoEntrenador.class.getName()).log(Level.SEVERE, "null", ex);
            }
        }
    }

    public void EliminarCliente(View view) {
        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        String sql = "UPDATE Clientes SET activo=0 WHERE UserName=?";
        String user = et7.getText().toString();

        if(user.length() == 0){Toast.makeText(this, "El campo username esta vacio", Toast.LENGTH_LONG).show();
        } else {
            try {
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, user);
                int n = pst.executeUpdate();
                if(n<=0){
                    Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
                if (n > 0) {
                    Toast.makeText(this, "El registro fué eliminado", Toast.LENGTH_SHORT).show();
                    Limpiar(view);
                }

            } catch (SQLException ex) {
                Toast.makeText(this, "El Usario esta en uso. Unicamente se puede Modificar", Toast.LENGTH_SHORT).show();
                Logger.getLogger(NuevoEntrenador.class.getName()).log(Level.SEVERE, "null", ex);
            }
        }
    }
    public void GuardarCliente(View view) {
        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        String sql = "";

        Bitmap imageBitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imagen = baos.toByteArray();
        String nom = et1.getText().toString();
        String ape = et2.getText().toString();
        String dire = et3.getText().toString();
        String Tel = et4.getText().toString();
        String TelA = et5.getText().toString();
        String sexo = sp1.getSelectedItem().toString();
        int edad = Integer.parseInt(et6.getText().toString());
        // byte[] imagen = img.getBytes();
        String user = et7.getText().toString();
        String Enfer = et8.getText().toString();

        sql = "INSERT INTO Clientes (Nombre,Apellidos,Direccion,Telefono,Tel_Adicional,Sexo,Edad,FechaDeIngreso,Fotografia,UserName,activo,Enfermedad) VALUES (?,?,?,?,?,?,?,GETDATE(),?,?,1,?)";
        if(TelA.length() == 0){et5.setText("null");}
        if (nom.length() == 0 || ape.length() == 0 || dire.length() == 0 || Tel.length() == 0&&Tel.length() > 10 || et6.getText().length()<=0 || user.length() == 0 || Enfer.length() == 0) {

            Toast.makeText(this, "Desbes de llenar los campos", Toast.LENGTH_LONG).show();
        } else {
            try {
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, nom);
                pst.setString(2, ape);
                pst.setString(3, dire);
                pst.setString(4, Tel);
                pst.setString(5, TelA);
                pst.setString(6, sexo);
                pst.setInt(7, edad);
                pst.setBytes(8, imagen);
                pst.setString(9, user);
                pst.setString(10, Enfer);

                int n = pst.executeUpdate();
                if(n<=0){
                    Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
                if (n > 0) {
                    Toast.makeText(this, "Registro Guardado con exito!", Toast.LENGTH_SHORT).show();
                }
                Limpiar(view);
            } catch (SQLException ex) {

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    //et1.setTextColor(Color.parseColor("#03A9F4"));
    void LimpiarInvisiblesBuscar(){
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et6.setText("");
        et8.setText("");
        sp1.setVisibility(View.INVISIBLE);
        et1.setVisibility(View.INVISIBLE);
        et2.setVisibility(View.INVISIBLE);
        et3.setVisibility(View.INVISIBLE);
        et4.setVisibility(View.INVISIBLE);
        et5.setVisibility(View.INVISIBLE);
        et6.setVisibility(View.INVISIBLE);
        et8.setVisibility(View.INVISIBLE);
        bt3.setVisibility(View.INVISIBLE);
        bt4.setVisibility(View.INVISIBLE);
        bt5.setVisibility(View.INVISIBLE);
    }
    void VisisblesBuscar(){
        sp1.setVisibility(View.VISIBLE);
        et1.setVisibility(View.VISIBLE);
        et2.setVisibility(View.VISIBLE);
        et3.setVisibility(View.VISIBLE);
        et4.setVisibility(View.VISIBLE);
        et5.setVisibility(View.VISIBLE);
        et6.setVisibility(View.VISIBLE);
        et8.setVisibility(View.VISIBLE);
    }

    public void BuscarCliente() {
        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rsmd;

        int colu;
        String user = et7.getText().toString();
        if (et1.getText().length() >= 0 || et2.getText().length() >= 0 || et3.getText().length() >= 0 || et4.getText().length() >= 0|| et5.getText().length() >= 0 || et6.getText().length() >= 0|| et8.getText().length() >= 0) {
            LimpiarInvisiblesBuscar();
            if (user.length() == 0) {
                Toast.makeText(this, "El campo username esta vacio", Toast.LENGTH_LONG).show();
            } else {
                try {
                    ps = cn.prepareStatement("SELECT Nombre, Apellidos, Direccion, Telefono, Tel_Adicional, Sexo, Edad,Fotografia,UserName,Enfermedad FROM Clientes WHERE UserName=?");
                    ps.setString(1, user);
                    rs = ps.executeQuery();
                    rsmd = rs.getMetaData();
                    colu = rsmd.getColumnCount();

                    while (rs.next()) {
                        et1.setText(rs.getString("Nombre"));
                        et2.setText(rs.getString("Apellidos"));
                        et3.setText(rs.getString("Direccion"));
                        et4.setText(rs.getString("Telefono"));
                        et5.setText(rs.getString("Tel_Adicional"));
                        et6.setText(rs.getString("Edad"));
                        byte[] fotobyte  = rs.getBytes("Fotografia");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(fotobyte, 0, fotobyte.length);
                        img.setImageBitmap(bitmap);
                        et7.setText(rs.getString("UserName"));
                        et8.setText(rs.getString("Enfermedad"));
                        VisisblesBuscar();
                        if(et1.getText().length() > 0){
                            bt1.setVisibility(View.INVISIBLE);
                            bt3.setVisibility(View.VISIBLE);
                            bt4.setVisibility(View.VISIBLE);
                            bt5.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (SQLException ex) {
                    Toast.makeText(this, "Error Tabla Entrenador" + ex, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}