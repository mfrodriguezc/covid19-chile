/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proyecto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import weka.classifiers.functions.LinearRegression;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;


/**
 *
 * @author Edgar
 */
public class Logica{
    
    public String HallarCentroides(int C) throws Exception{

        //Declarar las variables necesarias e iniciarlizar en caso de ser necesario
        String r="COVID19.csv", s="", sl = System.getProperty("line.separator");

        //Obtener Instancias del archivo
        Instances Datos = new Instances(new BufferedReader(new FileReader(r)));

        //Hacer uso del metodo Simple KMeans
        SimpleKMeans SKM = new SimpleKMeans();
        //Indicar el numero de centroides a obtener
        SKM.setNumClusters(C);
        //Indicar que obtenga los centroides
        SKM.buildClusterer(Datos);

        //Obtener lo obtenido por el Simple KMeans
        Instances Centroides = SKM.getClusterCentroids();

        //Guardar las instancias en un arreglo
        for(int x=0;x<Centroides.numInstances();x++){
            Instance D = Centroides.instance(x);
            s=s+"Dia: "+D.toString(0)+sl;
            s=s+"Casos: "+D.toString(1)+sl;
            s=s+"Fallecidos: "+D.toString(2)+sl;
            s=s+sl;
    }
    
    //Retornar los centroides obtenidos por medio de un string
    return s;
    }
    
    public Instances ObtenerDS(String r, int c, String nombre) throws Exception{
        
        //Obtener Instancias del archivo
        Instances Datos = new Instances(new BufferedReader(new FileReader(r)));
        //Datos.setClassIndex(Datos.numAttributes()-1);
        
        //Crear un arraylist (FastVector no está disponible)
        ArrayList <Attribute>AL = new ArrayList<>();

        //Agregar atributo de dias
        AL.add(new Attribute("Dias"));

        if(c==1){
            //Agregar atributo de casos
            AL.add(new Attribute("Casos"));
        }else if(c==2){
            //Agregar atributo de casos
            AL.add(new Attribute("Muertes"));
        }
        
        //Crear nuevo set de datos de 2 x N
        Instances DS = new Instances(nombre,AL,Datos.size());
        
        for(int x=0;x<Datos.size();x++){
            
            //Instanciar las, valga la redundancia, instancias temporales usadas para cargar el dataset
            Instance I = new DenseInstance(2);
            
            I.setValue(AL.get(0), Datos.get(x).value(0));
            I.setValue(AL.get(1), Datos.get(x).value(c));

            DS.add(I);
        }
    
    DS.setClassIndex(1);
    return DS;    
    }
    
    public ArrayList RegresionLineal(int o) throws Exception{
        //Declarar la ruta del archivo a leer
        String r="COVID19.csv";
        
        //Declarar el arreglo donde guardaremos todos los datos del la regresion lineal
        ArrayList<Object> DatosRegresion = new ArrayList<>();
        
        //Declarar la instancia de datos para realizar regresion lineal
        Instances Datos = null;
        
        switch (o) {
            case 1:
                Datos = ObtenerDS(r,1,"Dias-Casos");
                break;
            case 2:
                Datos = ObtenerDS(r,2,"Dias-Muertes");
                break;
        }
     
        //Se crea la regresion lineal basada en los datos
        LinearRegression LR = new LinearRegression();
        
        //Crear la clasificacion
        LR.buildClassifier(Datos);
        
        double[] Coeficientes = LR.coefficients();
        String Ecuacion = "Y="+Coeficientes[0]+"X"+Coeficientes[2];
        DatosRegresion.add(Ecuacion);
        DatosRegresion.add(Coeficientes[0]);
        DatosRegresion.add(Coeficientes[2]);
        return DatosRegresion;
    }
}
