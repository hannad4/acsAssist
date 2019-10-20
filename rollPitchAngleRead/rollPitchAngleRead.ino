#include <Wire.h>  // Wire library

int ADXL345 = 0x53; // The ADXL345 sensor I2C address
float X_out, Y_out, Z_out;  // Will hold the X, Y, Z Outputs 
float roll,pitch,rollF=0,pitchF=0; // Will hold the calculated roll and pitch angles as well as filtered calculation

void setup() {
  Serial.begin(9600);                   // Initiate serial communication (for serial output for now)
 
  Wire.begin();                         // Initiate the Wire library
  
  Wire.beginTransmission(ADXL345);      // Start communicating with the device
  Wire.write(0x2D);                     //  Power on chip via POWER_CTL
  
  Wire.write(8);                        // Bit D3 High for measuring enable (8dec -> 0000 1000 binary)
  Wire.endTransmission();
  delay(10);
  
  //Off-set Calibration (+14, +7, +279) initially vs (0, 0, 256)
  
  //X-axis
  Wire.beginTransmission(ADXL345);
  Wire.write(0x1E);
  Wire.write(-4);
  Wire.endTransmission();
  delay(10);
  
  //Y-axis
  Wire.beginTransmission(ADXL345);
  Wire.write(0x1F);
  Wire.write(-2);
  Wire.endTransmission();
  delay(10);
  
  //Z-axis
  Wire.beginTransmission(ADXL345);
  Wire.write(0x20);
  Wire.write(-6);
  Wire.endTransmission();
  delay(10);

}
void loop() {
  // === Read acceleromter data === //
  Wire.beginTransmission(ADXL345);
  Wire.write(0x32); // Start with register 0x32 (ACCEL_XOUT_H)
  Wire.endTransmission(false);
  Wire.requestFrom(ADXL345, 6, true); // Read 6 registers total, each axis value is stored in 2 registers
  X_out = ( Wire.read() | Wire.read() << 8); // X-axis value
  X_out = X_out / 256; //For a range of +-2g, we need to divide the raw values by 256, according to the datasheet
  Y_out = ( Wire.read() | Wire.read() << 8); // Y-axis value
  Y_out = Y_out / 256;
  Z_out = ( Wire.read() | Wire.read() << 8); // Z-axis value
  Z_out = Z_out / 256;
  // Calculate Roll and Pitch (rotation around X-axis, rotation around Y-axis)
  roll = atan(Y_out / sqrt(pow(X_out, 2) + pow(Z_out, 2))) * 180 / PI;
  pitch = atan(-1 * X_out / sqrt(pow(Y_out, 2) + pow(Z_out, 2))) * 180 / PI;
  // Low-pass filter
  rollF = 0.941 * rollF + 0.06 * roll;
  pitchF = 0.941 * pitchF + 0.06 * pitch;
  Serial.print(millis()/1000.0);
  Serial.print(", ");  
  Serial.print(rollF);
  Serial.print(", ");
  Serial.print(pitchF);
  Serial.println(""); 
}
