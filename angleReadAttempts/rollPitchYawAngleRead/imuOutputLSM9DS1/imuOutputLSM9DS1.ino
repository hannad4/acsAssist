#include <Arduino_LSM9DS1.h>
#include <ArduinoBLE.h>
#include <BLECharacteristic.h>
#include <BLEDescriptor.h>
#include <BLEDevice.h>
#include <BLEProperty.h>
#include <BLEService.h>
#include <BLETypedCharacteristic.h>
#include <BLETypedCharacteristics.h>
#include <String.h>
#include <PID_v1.h> 
#define initialThermistorResistance 10000 //Thermistor Resistance = 10k ohms 
#define BetaValue 3950             // Beta Value of Thermistor 3950 K 
#define Resistor 10000      // 10k Resistor for Voltage Divider 
#define thermistorPin A0 

float accelX, accelY, accelZ;
float magX, magY, magZ, mag_x, mag_y;
float gyroX, gyroY, gyroZ;
float roll, pitch, yaw, rollF = 0, pitchF = 0, yawF = 0, posiRoll, posiPitch, posiYaw;  
 

//Battery (Power Supply) Variables 
float analogReading; 
float initialBatteryVoltage; 
float voltageRatio; 
float finalBatteryVoltage; 

//Declare Variables as Floats 
float newThermistorResistance; 
float voltageUnit;
float voltageThermistor; 
float thermistorReading; 
float voltageNew; 
float lnRTR0; 
float tempInitial; 
float tempFinal; 
float convertedReading; 


BLEService angleService("1826");
BLEStringCharacteristic pitchBLE("78c5307a-6715-4040-bd50-d64db33e2e9e", BLERead | BLENotify, 20);
BLEStringCharacteristic rollBLE("78c5307b-6715-4040-bd50-d64db33e2e9e", BLERead | BLENotify, 20);
BLEStringCharacteristic yawBLE("78c5307c-6715-4040-bd50-d64db33e2e9e", BLERead | BLENotify, 20);
BLEStringCharacteristic temperature("78c5307d-6715-4040-bd50-d64db33e2e9e", BLERead | BLENotify, 20); 

void setup()
{
  tempInitial = 273.15 + 25; 
  if (!IMU.begin())
  {
    Serial.println("Failed to initialize IMU!");
    exit(1);
  }

  if (!BLE.begin())
  {
    Serial.println("starting BLE failed!");
    while (1)
      ;
  }

  BLE.setLocalName("acsAssist");
  BLE.setAdvertisedService(angleService);
  angleService.addCharacteristic(pitchBLE); 
  angleService.addCharacteristic(rollBLE);
  angleService.addCharacteristic(yawBLE); 
  angleService.addCharacteristic(temperature); 
 
  BLE.addService(angleService);
  rollBLE.writeValue("0"); 
  pitchBLE.writeValue("0");
  yawBLE.writeValue("0"); 
  BLE.advertise();
}

void loop()
{
  BLEDevice central = BLE.central();

  if (central)
  {

    while (central.connected())
    {

      if (IMU.accelerationAvailable() && IMU.magneticFieldAvailable() && IMU.gyroscopeAvailable())
      {
        IMU.readAcceleration(accelX, accelY, accelZ);
        IMU.readMagneticField(magX, magY, magZ);
        IMU.readGyroscope(gyroX, gyroY, gyroZ);
        
        roll = atan(accelY / sqrt(pow(accelX, 2) + pow(accelZ, 2))) * 180 / PI;
        rollF = 0.94 * rollF + 0.06 * roll;
        
        pitch = atan(-1 * accelX / sqrt(pow(accelY, 2) + pow(accelZ, 2))) * 180 / PI;
        pitchF = 0.94 * pitchF + 0.06 * pitch;
        
        yaw = yaw + gyroZ * 0.01;
        yawF = 0.94 * yawF + 0.06 * yaw; 
        // yaw = 180 * atan2(-mag_y,mag_x)/M_PI;
        
        posiRoll = rollF * -1;
        posiPitch = pitchF * -1;
        posiYaw = yawF * -1; 
        rollBLE.writeValue(String(posiRoll)); 
        pitchBLE.writeValue(String(posiPitch));
        yawBLE.writeValue(String(posiYaw)); 
        
        analogReading = analogRead(A2); //Reading the power supply  
        initialBatteryVoltage = analogReading * (3.33/1023.00); 
        voltageRatio = 6.00; 
        finalBatteryVoltage = initialBatteryVoltage * voltageRatio; 
        voltageUnit = analogRead(A0); 
        voltageThermistor = (3.33/1023.00) * voltageUnit; //Convert the analog value (0-1023 volts/unit) to a voltage (0-5V) using conversion factor 
        //voltageNew = InputVoltage - voltageThermistor;
        voltageNew = initialBatteryVoltage - voltageThermistor; 
        newThermistorResistance = voltageThermistor / (voltageNew / Resistor);
        thermistorReading = analogRead(thermistorPin); 
        thermistorReading = (1023/thermistorReading) - 1; 
        thermistorReading = Resistor / thermistorReading;  
        lnRTR0 = log(newThermistorResistance / initialThermistorResistance);
        tempFinal = (1 / ((lnRTR0 / BetaValue) + (1 / tempInitial))); //Temperature from thermistor using beta value 
        tempFinal = (tempFinal - 273.15); //Conversion to Celsius 
        temperature.writeValue(String(tempFinal)); 
      }
      
    
    }
  }
}
