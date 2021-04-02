package com.example.appgym;
import android.content.Context;
import android.graphics.Color;
import android.util.Size;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableDynamic {
    public static TableLayout tableLayout;

    public static Context context;
    private Object[] header;
    public static ArrayList<Object[]> data = new ArrayList<>();

    private TableRow tableRow;
    private TextView txtCell;
    private int indexC;
    private int indexR;
    private boolean multiColor = false;
    int firtColor;
    int secondColor;
    int textColor;
    private static ArrayList<Object[]> rows = new ArrayList<>();
       public TableDynamic(){
           this.tableLayout = tableLayout;
           this.context = context;
       }


    public void addHeader(Object[] header) {
        this.header = header;
        createHeader();

    }

    public void addData(ArrayList<Object[]> data){
        this.data = data;
        createDataTable();
    }

    public void addDatanull(){
           this.data = data;
           createDataTable();
    }

    private void newRow() {
        tableRow = new TableRow(context);
    }


    private void newCell() {
        txtCell = new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(12);

    }



    private void createHeader() {
        indexC = 0;
        newRow();
        if (header.length <= 1) {

        } else {
            while (indexC < header.length) {
                newCell();
                txtCell.setText((String) header[indexC++]);
                tableRow.addView(txtCell, newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
    }


    private void createDataTable() {
                   Object info;
        for (indexR = 1; indexR <= data.size(); indexR++) {
            newRow();
            for (indexC = 0; indexC < header.length; indexC++) {
                newCell();
                Object[] col = data.get(indexR - 1);
                info = (indexC < col.length) ? col[indexC] : "";
                txtCell.setText((String) info);
                tableRow.addView(txtCell, newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
            }


    public void addItems(Object[] item) {
        Object info;
        this.data.add(item);
        indexC = 0;
        newRow();
            while (indexC < header.length) {
                newCell();
                info = (indexC < item.length) ? item[indexC++] : "";
                txtCell.setText(""+ info);
                tableRow.addView(txtCell, newTableRowParams());
            }
            tableLayout.addView(tableRow, data.size());//Se quito el -1 despues de size para corregir
        }



    public void backgroundHeader(int color) {
        indexC = 0;
        newRow();
        if (header.length <= 1) {
            while (indexC < 4) {
                txtCell = getCell(0, indexC++);
                txtCell.setBackgroundColor(color);
            }
        } else {
            while (indexC < header.length) {
                txtCell = getCell(0, indexC++);
                txtCell.setBackgroundColor(color);
            }
        }
    }

    public void fechadehoyVencimiento(int col, int firtColor,String ven) {
        for (indexR = 1; indexR <= data.size(); indexR++) {
            for (indexC = 0; indexC < header.length; indexC++) {
                txtCell = getCell(indexR, col);
                String e = txtCell.getText().toString();
                if (e==ven) {
                    txtCell.setBackgroundColor(firtColor);
                }
            }
        }
        this.firtColor = firtColor;
    }
    public void cellred(int col, int firtColor) {

        for (indexR = 1; indexR <= data.size(); indexR++) {
            for (indexC = 0; indexC < header.length; indexC++) {
                txtCell = getCell(indexR, col);
                int e = Integer.parseInt(txtCell.getText().toString());
                if (e < 10) {
                    txtCell.setBackgroundColor(firtColor);
                }
            }
        }
        this.firtColor = firtColor;
    }

    public void backgroundHistorial(int col, int SalidasColor, int EntradasColor) {
        for (indexR = 1; indexR <= data.size(); indexR++) {

            for (indexC = 0; indexC < header.length; indexC++) {
                txtCell = getCell(indexR, col);
                String e = txtCell.getText().toString();
                switch (e) {
                    case "SALIDA":
                        getRow(indexR).setBackgroundColor(SalidasColor); break;
                    case "ENTRADA":
                        getRow(indexR).setBackgroundColor(EntradasColor); break;
                }

            }
        }

        this.firtColor = SalidasColor;
        this.secondColor = EntradasColor;
    }

    public void backgroundData(int firtColor, int secondColor){
        for (indexR=1; indexR<=data.size(); indexR++){
            multiColor=!multiColor;

            for (indexC=0; indexC<header.length; indexC++){
                txtCell=getCell(indexR,indexC);
                txtCell.setBackgroundColor((multiColor)?firtColor:secondColor);
            }
        }
        this.firtColor=firtColor;
        this.secondColor=secondColor;
    }
    public void lineColor(int color){
        indexR=0;
        while(indexR<data.size()){
            getRow(indexR++).setBackgroundColor(color);
        }
    }
    public void textColorData(int color){
        for (indexR=1; indexR<=data.size(); indexR++){
            if(header.length<=1){
                for (indexC=0; indexC<4; indexC++){
                    getCell(indexR,indexC).setTextColor(color);
                }
            }else{
            for (indexC=0; indexC<header.length; indexC++){
                getCell(indexR,indexC).setTextColor(color);
            }}
        }
        this.textColor=color;
    }
    public void textColorHeader(int color){
        indexC=0;
        if(header.length<=1) {
            while (indexC<4){
                getCell(0,indexC++).setTextColor(color);
            }
        }else{
        while (indexC<header.length){
            getCell(0,indexC++).setTextColor(color);
        }}
    }
    public void reColoring(){
        indexC=0;
        multiColor=!multiColor;
        if(header.length<=1){
            while (indexC<4){
                txtCell=getCell(data.size(),indexC++);//Se quito el -1 despues de size
                txtCell.setBackgroundColor((multiColor)?firtColor:secondColor);
                txtCell.setTextColor(textColor);
            }
        }else{
        while (indexC<header.length){
            txtCell=getCell(data.size(),indexC++);//Se quito el -1 despues de size
            txtCell.setBackgroundColor((multiColor)?firtColor:secondColor);
            txtCell.setTextColor(textColor);
        }}
    }
    public TableRow getRow(int index){
        return (TableRow)tableLayout.getChildAt(index);
    }

    public TextView getcolum(TableRow row,int index,int columIndex){
        return (TextView) row.getChildAt(columIndex);
    }
    public TextView getCell1(int rowIndex,int columIndex){
        tableRow=getRow(rowIndex);
        return (TextView) tableRow.getChildAt(columIndex);
    }
    public TextView getCell(int rowIndex,int columIndex){
        tableRow=getRow(rowIndex);
        return (TextView) tableRow.getChildAt(columIndex);
    }

    public TableRow.LayoutParams newTableRowParams(){
        TableRow.LayoutParams params=new TableRow.LayoutParams();
        params.setMargins(1,1,1,1);
        params.weight=1;
        return params;
    }
}