package sensorNetwork;

/**
* sensorNetwork/SensorHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from SensorNetwork.idl
* Thursday, March 26, 2015 8:27:10 PM GMT-03:00
*/

public final class SensorHolder implements org.omg.CORBA.portable.Streamable
{
  public sensorNetwork.Sensor value = null;

  public SensorHolder ()
  {
  }

  public SensorHolder (sensorNetwork.Sensor initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = sensorNetwork.SensorHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    sensorNetwork.SensorHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return sensorNetwork.SensorHelper.type ();
  }

}
