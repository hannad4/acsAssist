#include <Arduino_LSM9DS1.h>

float accelX, accelY, accelZ;
float rollF, pitchF; 

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
    IMU.readAcceleration(accelX, accelY, accelZ);
    float roll = (float) (atan2(accelY, accelZ)) * RAD_TO_DEG; 
    float pitch = (float) (atan2(accelX, sqrt(accelY * accelY + accelZ * accelZ))) * RAD_TO_DEG; 


    
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
