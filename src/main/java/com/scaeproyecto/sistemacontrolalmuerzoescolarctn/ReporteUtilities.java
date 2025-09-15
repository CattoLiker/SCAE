/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author USUARIO
 */
public class ReporteUtilities {
    
    public static void abrirJasper(String rutaJasper, Map<String, Object> parametros, Connection conexion) {
        try {
            if (parametros == null) {
                parametros = new java.util.HashMap<>(); // para evitar NullPointerException >:(
            }
            
            InputStream reporteStream = ReporteUtilities.class.getResourceAsStream(rutaJasper);     // pasa el recurso a un InputStream para que pueda ser encontrado

            if (reporteStream == null) {
                throw new IllegalArgumentException("No se encontró el reporte: " + rutaJasper);     // https://i.redd.it/j50o89q6jlse1.jpeg
            }
            
            JasperPrint printos = JasperFillManager.fillReport(reporteStream, parametros, conexion); // Llena el reporte yipeee
            
            JasperViewer view = new JasperViewer(printos, false); // para ver el reporte (si no funciona te juro que me tiro de un puente)
            view.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    
}
