#include <Arduino_LSM9DS1.h>

float x, y, z;
float roll, pitch, rollF=0, pitchF=0;

void setup()
{
  if (!IMU.begin())
  {
    Serial.println("Failed to initialize IMU!");
    exit(1);
  }
}

void loop()
{
   

  if (IMU.accelerationAvailable())
  {
    IMU.readAcceleration(x, y, z);

    // Serial.print("X is ");
    // Serial.println(x); 
    // Serial.print("Y is ");
    // Serial.println(y); 
    // Serial.print("Z is ");
    // Serial.println(z); 
    // Serial.println(); 
    
    roll = atan(y / sqrt(pow(x, 2) + pow(z, 2))) * 180 / PI;
    pitch = atan(-1 * x / sqrt(pow(y, 2) + pow(z, 2))) * 180 / PI;
    rollF = 0.941 * rollF + 0.06 * roll; 
    pitchF = 0.941 * pitchF + 0.06 * pitch; 
    Serial.print(millis()/1000.0); 
    Serial.print(", "); 
    Serial.print(rollF); 
    Serial.print(", "); 
    Serial.print(pitchF); 
    Serial.println(""); 
  }

}
