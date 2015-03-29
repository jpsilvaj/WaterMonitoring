package br.edu.ifce.watermonitoring.client.controller;

import br.edu.ifce.watermonitoring.client.Exception.SensorCannotFindException;
import br.edu.ifce.watermonitoring.client.view.WaterMonitoringView;

import org.apache.commons.lang3.ArrayUtils;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.Object;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import sensorNetwork.Sensor;
import sensorNetwork.SensorNetwork;
import sensorNetwork.SensorNetworkHelper;


/**
 * Created by jp-desktop on 28/03/2015.
 */
public class ClientControllerWaterMonitoring {

    private static ORB orb;
    private static Object nameService;
    private static NamingContext namingContext;
    private static NamingContextExt namingContextExt;
    private static SensorNetwork sensorNetwork;
    private static WaterMonitoringView waterMonitoringView;
    private static Sensor[] sensors;


    public static void main(String args[]){

        try {
            orb = ORB.init(args, null);
            nameService = orb.resolve_initial_references("NameService");
            namingContext = NamingContextHelper.narrow(nameService);
            namingContextExt = NamingContextExtHelper.narrow(nameService);
            getTheSensorNetworkFromORB();
            waterMonitoringView = new WaterMonitoringView();
        } catch (InvalidName e) {
            e.printStackTrace();
        }

    }

    public static void getTheSensorNetworkFromORB() {
        NameComponent[] name = { new NameComponent("SensorNetwork", "ServerNetworkImpl") };
        Object objRef = null;
        try {
            objRef = namingContext.resolve(name);
            sensorNetwork = SensorNetworkHelper.narrow(objRef);
            sensors  = sensorNetwork.getSensors();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        } catch (CannotProceed cannotProceed) {
            cannotProceed.printStackTrace();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
            invalidName.printStackTrace();
        }
    }

    public static void updateTemperatureSensorValueToServer(Sensor sensor, int temperature) {
        sensorNetwork.updateTemperatureValueToSensor(sensor, temperature);
    }

    public static void updatePHSensorValueToServer(Sensor sensor, int ph) {
        sensorNetwork.updatePhValueToSensor(sensor, ph);
    }

    public static void updateColorSensorValueToServer(Sensor sensor, int color) {
        sensorNetwork.updateColorValueToSensor(sensor,color);
    }
    
    public static void createSensor(int temperature, int ph, int color){
    	Sensor sensor = new Sensor(sensors.length,temperature, ph, color);
    	addSensorToServer(sensor);
    }
    
    public static void removeSensor(Sensor sensor){
    	sensors = ArrayUtils.removeElement(sensors, sensor);
    	sensorNetwork.deleteSensorFromNetwork(sensor);
    }
    
    private static void addSensorToServer(Sensor sensor){
    	sensorNetwork.addSensorToNetwork(sensor);
    	updateValueToNetworkSensorPanel();
    }
    
    private static void updateValueToNetworkSensorPanel(){
        sensors  = sensorNetwork.getSensors();
        for(Sensor sensor : sensors){
            try {
                waterMonitoringView.updateSensorValueInPanel(sensor);
            } catch (SensorCannotFindException e) {
                e.printStackTrace();
                //TODO:Implement exception which sensor cannot found
            }
        }
    }
}
